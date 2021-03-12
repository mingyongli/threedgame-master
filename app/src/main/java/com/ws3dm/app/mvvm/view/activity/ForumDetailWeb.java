package com.ws3dm.app.mvvm.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ws3dm.app.MyApplication;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.BaseActivity;
import com.ws3dm.app.databinding.AcForumDetailWebBinding;
import com.ws3dm.app.mvvm.view.ForumUserOperationDialog;
import com.ws3dm.app.mvvm.view.ReplyMessageDialog;
import com.ws3dm.app.mvvm.viewmodel.ForumDetailWebViewModel;
import com.ws3dm.app.util.AppUtil;
import com.ws3dm.app.util.SharedUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.view.SharePopupWindow;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;
import static android.webkit.WebSettings.MIXED_CONTENT_NEVER_ALLOW;

public class ForumDetailWeb extends BaseActivity {

    private String url;
    private String title;
    private SharePopupWindow popShare;
    private ForumDetailWebViewModel viewModel;
    private String tid;
    private String fid;
    //当前收藏的状态
    private int isCollect = 0;
    private Dialog dialog;
    private AcForumDetailWebBinding mBind;
    private Activity activity;

    //排序状态
    private enum order {
        DESC, ASC
    }

    //默认排序状态
    private order orderState = order.ASC;

    @Override
    protected void init() {
        activity = this;
        viewModel = new ViewModelProvider(this).get(ForumDetailWebViewModel.class);
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        tid = getIntent().getStringExtra("tid");
        fid = getIntent().getStringExtra("fid");
        mBind = DataBindingUtil.setContentView(this, R.layout.ac_forum_detail_web);
        initView();
        initListener();
        loadData();
    }

    private void loadData() {
        viewModel.getThreadCollect(String.valueOf(tid));
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    private void initView() {
        setSupportActionBar(mBind.toolbar);
        mBind.forumTitle.setText(title);
        popShare = new SharePopupWindow(mContext, url.replace("_app", ""), title, title, mBind.loadViewShare);
        if (SharedUtil.getSharedPreferencesData("FontSize").equals("2")) {
            mBind.webView.getSettings().setTextZoom(150);
        } else if (SharedUtil.getSharedPreferencesData("FontSize").equals("1")) {
            mBind.webView.getSettings().setTextZoom(75);
        } else {
            mBind.webView.getSettings().setTextZoom(100);
        }
        mBind.webView.getSettings().setJavaScriptEnabled(true);
        mBind.webView.addJavascriptInterface(new Object() {

            @JavascriptInterface
            public void showOpenNav(String uid, String tid, String fid, String pid, int type, String msg) {
                PanelDialog(uid, tid, fid, pid, type, msg);
            }


            @JavascriptInterface
            public void showReply(String uid, String tid, String fid, String pid, int type) {
                if (type == 1) {
                    replayDialog(uid, tid, fid, pid, type);
                }
            }
        }, "JSInterface");
        mBind.webView.getSettings().setBlockNetworkImage(false);//解决图片不显示
        mBind.webView.getSettings().setDomStorageEnabled(true);
        mBind.webView.getSettings().setUseWideViewPort(true);
        mBind.webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        mBind.webView.getSettings().setLoadWithOverviewMode(true);
        mBind.webView.getSettings().setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);
        mBind.webView.setWebChromeClient(new thisWebChromeClient());
        mBind.webView.setWebViewClient(new thisWebClient());
        String uid = MyApplication.getUserData().loginStatue ? MyApplication.getUserData().uid : "0";
        String currentUrl = url + "&uid=" + uid;
        mBind.webView.loadUrl(currentUrl);

    }

    //用户dialog
    private void PanelDialog(String id, String tid, String fid, String pid, int type, String msg) {
        ForumUserOperationDialog dialog = ForumUserOperationDialog.getDialog(mContext, R.style.dialog_bottom_full, type);
        dialog.setOnlyMaterListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onlyAuthor();
                dialog.dismiss();
            }
        }).setOrderListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (orderState == order.DESC) {
                    orderState = order.ASC;
                    orderByAsc();
                } else {
                    orderState = order.DESC;
                    orderByDesc();
                }
                dialog.dismiss();
            }
        }).setReportListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewModel.submitReport(mContext, id, tid, pid, fid);
                dialog.dismiss();
            }
        }).setCopyListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppUtil.CopyMessageToClip(mContext, msg);
                ToastUtil.showToast(mContext, "已复制内容到剪切板！");
                dialog.dismiss();
            }
        }).setReplyListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                replayDialog(id, tid, fid, pid, type);
            }
        }).show();
    }

    //回复dialog
    private void replayDialog(String uid, String tid, String fid, String pid, int type) {
        ReplyMessageDialog replyDialog = ReplyMessageDialog.getDialog(activity, mContext, R.style.dialog_bottom_full);
        replyDialog.setReplayBtnListener(new ReplyMessageDialog.ReplayMessage() {
            @Override
            public void getMessage(Dialog replyDialog, String message) {
                if (message.length() > 0) {
                    setWebMessage(uid, tid, fid, pid, message);
                } else {
                    ToastUtil.showToast(mContext, "请输入内容");
                }
                replyDialog.dismiss();
            }
        }).show();
    }

    private void initListener() {
        mBind.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBind.replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReplyMessageDialog dialog = ReplyMessageDialog.getDialog(activity, mContext, R.style.dialog_bottom_full);
                dialog.setReplayBtnListener(new ReplyMessageDialog.ReplayMessage() {
                    @Override
                    public void getMessage(Dialog replayDialog, String message) {
                        if (message.length() > 0) {
                            setWebMessage(MyApplication.getUserData().uid, tid, fid, String.valueOf("0"), message);
                        }
                        dialog.dismiss();
                    }
                }).show();
            }
        });
        mBind.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popShare.show();
            }
        });
        mBind.collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.addDelFavorite(String.valueOf(tid), isCollect + 1);
            }
        });
        viewModel.getIsCollect().observe(this, new Observer<ForumDetailWebViewModel.resultsBean>() {
            @Override
            public void onChanged(ForumDetailWebViewModel.resultsBean resultsBean) {
                if (resultsBean.getCode() == 1) {
                    isCollect = resultsBean.getData().getIsCollect();
                    switchCollectState(resultsBean.getData().getIsCollect() == 1);
                } else {
                    ToastUtil.showToast(mContext, "获取状态失败");
                }
            }
        });
    }

    //只看楼主
    private void onlyAuthor() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBind.webView.loadUrl("javascript:onlyAuthor()");
            }
        });

    }

    //排序DESC
    private void orderByDesc() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBind.webView.loadUrl("javascript:orderByDesc()");
            }
        });
    }

    //排序ASC
    private void orderByAsc() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBind.webView.loadUrl("javascript:orderByAsc()");
            }
        });
    }

    //通过js发送message
    private void setWebMessage(String uid, String tid, String fid, String pid, String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBind.webView.loadUrl("javascript:getMessage('" + uid + "','" + tid + "','" + fid + "','" + pid + "','" + msg + "')");
            }
        });
    }

    private void closeReply() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBind.webView.loadUrl("javascript:closeReply()");
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mBind.webView.canGoBack()) {
            mBind.webView.goBack();
        } else {
//            closeReply();
            super.onBackPressed();
        }
    }

    private void switchCollectState(boolean state) {
        if (state) {
            mBind.collection.setImageResource(R.mipmap.ic_collected);
        } else {
            mBind.collection.setImageResource(R.mipmap.ic_collection);
        }
    }

    static class thisWebChromeClient extends WebChromeClient {

    }

    static class thisWebClient extends WebViewClient {
        public void loadurlLocalMethod(final WebView webView, final String url) {    //屏蔽网页内恶意代码跳转app
            new Thread(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl(url);
                }
            });
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            loadurlLocalMethod(view, url);
            return false;
        }
    }

}
