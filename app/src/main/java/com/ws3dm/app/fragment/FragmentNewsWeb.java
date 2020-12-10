package com.ws3dm.app.fragment;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.AuthorDetailActivity;
import com.ws3dm.app.activity.ForgetPassActivity;
import com.ws3dm.app.activity.GameHomeActivity;
import com.ws3dm.app.activity.ImageActivity;
import com.ws3dm.app.activity.LoginActivity;
import com.ws3dm.app.activity.NewsActivity;
import com.ws3dm.app.adapter.BaseRecyclerAdapter;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.bean.CommentBean;
import com.ws3dm.app.bean.GameBean;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.Constant;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.databinding.FgNewsWebBinding;
import com.ws3dm.app.emoj.EmotionUtils;
import com.ws3dm.app.emoj.fragment.EmotionMainFragment;
import com.ws3dm.app.mvp.model.RespBean.NewsAboutRespBean;
import com.ws3dm.app.mvp.model.RespBean.NewsHotCommentRespBean;
import com.ws3dm.app.mvp.presenter.NewsPresenter;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.NewsService;
import com.ws3dm.app.network.service.UserService;
import com.ws3dm.app.util.AppUtil;
import com.ws3dm.app.util.DialogHelper;
import com.ws3dm.app.util.SharedUtil;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.util.glide.GlideUtil;
import com.ws3dm.app.view.InputWindowListener;
import com.ws3dm.app.view.RoundCornerProgressBar;
import com.ws3dm.app.view.SharePopupWindow;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Describution :新闻页详情上部的网页
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2017/8/18 17:22
 **/
public class FragmentNewsWeb extends BaseFragment {
    private FgNewsWebBinding mBinding;
    private NewsBean newsBean;
    private int f_sid, replyId = -1, favorite = 2;//favorite:1已收藏，2为收藏
    private int mPage;
    private int c_sid;//当前文章在评论里唯一标识(默认为0)

    private SharePopupWindow pop;
    private PopupWindow popTip;
    private InputMethodManager imm;
    private AppCompatActivity activity;
    private boolean isFirst, isEmoj = false, canReport = true;
    private String strContent;
    private NewsPresenter mNewsPresenter;
    private Handler mHandler, msgHandle;
    private String imgURL, clickURL;
    private EmotionUtils emoutil;
    private NewsHotCommentRespBean myCommentData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (AppCompatActivity) getActivity();
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_news_web, container, false);
        mBinding.setHandler(this);
        msgHandle = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        GlideUtil.loadImage(mContext, imgURL, mBinding.imgAds);
                        mBinding.imgAds.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AppUtil.OpenUrl(mContext, clickURL);
                            }
                        });
                        mBinding.imgAds.setVisibility(View.VISIBLE);
                        break;
                }
                super.handleMessage(msg);
            }
        };
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (!mIsRunning) {
                    return;
                }
                switch (msg.what) {
                    case Constant.Notify.LOAD_START:
                        ToastUtil.showToast(mContext, "载入中");
                        break;
                    case Constant.Notify.LOAD_FAILURE:
                        ToastUtil.showToast(mContext, "请求失败");
                        break;
                    case Constant.Notify.RESULT_NEWS_HOTCOMMENT_LIST:
                        NewsHotCommentRespBean comments = (NewsHotCommentRespBean) msg.obj;
                        initComments((NewsHotCommentRespBean) msg.obj);
                        break;
                }
            }
        };

        mNewsPresenter = NewsPresenter.getInstance();
        mNewsPresenter.setHandler(mHandler);

        initView();

        new Handler().postDelayed(new Runnable() {
            public void run() {
//				new Thread(){//新线程获取广告
//					@Override
//					public void run() {
//						obtainDataAds();
//					}
//				}.start();
                obtainNewsAbout();
                obtainComments();
            }
        }, 1500);
//		if(MyApplication.getUserData().loginStatue){
//			getFavoriteStute();
//		}
        return mBinding.getRoot();

    }


    public void initView() {
        if (getArguments()
                .getSerializable("newsBean") != null)
            newsBean = (NewsBean) getArguments().getSerializable("newsBean");
        isFirst = true;
        c_sid = 0;
        mPage = 1;
        emoutil = new EmotionUtils(getActivity());

        if (newsBean == null) {
            return;
        }

        mBinding.rlComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsActivity.viewPager.setCurrentItem(1);
            }
        });
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        String webUrl = newsBean.getArcurl();
        if (StringUtil.isEmpty(newsBean.getLitpic()))
            pop = new SharePopupWindow(getActivity(), webUrl.replace("_app", ""), newsBean.getTitle(), newsBean.getTitle(), mBinding.loadViewShare);
        else
            pop = new SharePopupWindow(getActivity(), webUrl.replace("_app", ""), newsBean.getTitle(), newsBean.getTitle(), mBinding.loadViewShare, newsBean.getLitpic());

        mBinding.mWebView.getSettings().setJavaScriptEnabled(true);
        mBinding.mWebView.getSettings().setBlockNetworkImage(false);//解决图片不显示
        mBinding.mWebView.getSettings().setDomStorageEnabled(true);
        mBinding.mWebView.getSettings().setSupportZoom(true); // 支持缩放
        mBinding.mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);

        mBinding.mWebView.getSettings().setUseWideViewPort(true);
        mBinding.mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mBinding.mWebView.getSettings().setLoadWithOverviewMode(true);
        if (SharedUtil.getSharedPreferencesData("FontSize").equals("2"))//字体大小，0，小，1中，2大
            mBinding.mWebView.getSettings().setTextSize(WebSettings.TextSize.LARGER);
        else if (SharedUtil.getSharedPreferencesData("FontSize").equals("1"))
            mBinding.mWebView.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
        else
            mBinding.mWebView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            mBinding.mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        mBinding.mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mBinding.mWebView.addJavascriptInterface(new JavaScriptInterface(mContext), "JSInterface");
        mBinding.mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (isFirst)
                    if (newProgress == 100) {
                        if (mBinding.txtProgress != null)
                            mBinding.txtProgress.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mBinding.llProgress != null)
                                    mBinding.llProgress.setVisibility(View.GONE);
                            }

                        }, 300);
                        mBinding.mRecycleView.setVisibility(View.VISIBLE);
                        mBinding.llTip.setVisibility(View.VISIBLE);
                    } else {
                        if (mBinding.llProgress != null)
                            if (mBinding.llProgress.getVisibility() == View.GONE)
                                mBinding.llProgress.setVisibility(View.VISIBLE);
                            else {
                                if (mBinding.txtProgress != null) {
                                    DisplayMetrics dm = new DisplayMetrics();
                                    activity.getWindowManager().getDefaultDisplay().getMetrics(dm);//临时
                                    mBinding.txtProgress.setLayoutParams(new LinearLayout.LayoutParams((int) (dm.widthPixels * newProgress * 0.01), ViewGroup.LayoutParams.MATCH_PARENT));
                                }
                            }
                    }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                showCustomView(view, callback);
            }

            @Override
            public void onHideCustomView() {
                hideCustomView();
            }
        });

        mBinding.mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (mBinding.mWebView != null)
                    mBinding.mWebView.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                isFirst = true;
                loadurlLocalMethod(view, url);
//				view.loadUrl(url);   //在当前的webview中跳转到新的url
                mBinding.viewScroll.fullScroll(ScrollView.FOCUS_UP);
//				return true;
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isFirst = false;
//						mBinding.viewScroll.fullScroll(ScrollView.FOCUS_UP);//添加滚动到顶部
                    }
                }, 500);
            }
        });
        mBinding.mWebView.loadUrl(newsBean.getWebviewurl());
//        mWebView.loadUrl("file:///android_asset/index_video.html");
        //mBinding.mWebView.loadUrl("file:///android_asset/a.html");
        // 监听键盘状态
        mBinding.rlAllComment.setListener(new InputWindowListener() {
            @Override
            public void show() {
//				mBinding.etComment.setText("    ");
//				mBinding.etComment.setSelection("    ".length());
                mBinding.llNoInPut.setVisibility(View.GONE);
                mBinding.llInPut.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBinding.llInPut.setVisibility(View.VISIBLE);
                    }
                }, 1);
            }

            @Override
            public void hidden() {
                if (mBinding.flEmotionviewMain.getHeight() > 100) {

                } else {
                    replyId = 0;
                    mBinding.etComment.setText("");
                    mBinding.etComment.setSelection(0);

                    mBinding.llInPut.setVisibility(View.GONE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mBinding.llNoInPut != null)
                                mBinding.llNoInPut.setVisibility(View.VISIBLE);
                        }
                    }, 1);
                }
            }
        });

        initEmotionMainFragment();
    }

    public void loadurlLocalMethod(final WebView webView, final String url) {   //屏蔽网页内恶意代码跳转app
        new Thread(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl(url);
            }
        });
    }

    public View customView;
    private FrameLayout fullscreenContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;
    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    /**
     * 视频播放全屏
     **/
    private void showCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        // if a view already exists then immediately terminate the new one
        if (customView != null) {
            callback.onCustomViewHidden();
            return;
        }

        getActivity().getWindow().getDecorView();

        FrameLayout decor = (FrameLayout) getActivity().getWindow().getDecorView();
        fullscreenContainer = new FullscreenHolder(mContext);
        fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
        decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
        customView = view;
        setStatusBarVisibility(false);
        customViewCallback = callback;
    }

    /**
     * 隐藏视频全屏
     */
    public void hideCustomView() {
        if (customView == null) {
            return;
        }

        setStatusBarVisibility(true);
        FrameLayout decor = (FrameLayout) getActivity().getWindow().getDecorView();
        decor.removeView(fullscreenContainer);
        fullscreenContainer = null;
        customView = null;
        customViewCallback.onCustomViewHidden();
        mBinding.mWebView.setVisibility(View.VISIBLE);
    }

    /**
     * 全屏容器界面
     */
    static class FullscreenHolder extends FrameLayout {

        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }

        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }

    private void setStatusBarVisibility(boolean visible) {
        int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getActivity().getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 初始化表情面板
     */
    public void initEmotionMainFragment() {
        //构建传递参数
        Bundle bundle = new Bundle();
        //绑定主内容编辑框
        bundle.putBoolean(EmotionMainFragment.BIND_TO_EDITTEXT, false);
        //隐藏控件
        bundle.putBoolean(EmotionMainFragment.HIDE_BAR_EDITTEXT_AND_BTN, true);
        //替换fragment
        //创建修改实例
        NewsActivity.emotionMainFragment = EmotionMainFragment.newInstance(EmotionMainFragment.class, bundle);
        NewsActivity.emotionMainFragment.bindToContentView(mBinding.llInPut);
        NewsActivity.emotionMainFragment.bindToEditView(mBinding.etComment);
        NewsActivity.emotionMainFragment.bindToImageView(mBinding.ivEmoj);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        // Replace whatever is in thefragment_container view with this fragment,
        // and add the transaction to the backstack
        transaction.replace(R.id.fl_emotionview_main, NewsActivity.emotionMainFragment);
        transaction.addToBackStack(null);
        //提交修改
        transaction.commit();
    }

    public void obtainNewsAbout() {//获取评论内容
        String arcurl = newsBean.getArcurl();
        int pagesize = 3;
        long time = System.currentTimeMillis();
        String validate = arcurl + String.valueOf(pagesize) + time;
        String sign = StringUtil.MD5(validate);
        JSONObject obj = new JSONObject();
        try {
            obj.put("arcurl", arcurl);
            obj.put("pagesize", pagesize);
            obj.put("time", time);
            obj.put("sign", sign);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//		mNewsPresenter.getNewsAbout(obj.toString());//异步请求
        //同步请求
        Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
        NewsService.Api service = retrofit.create(NewsService.Api.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
        Call<NewsAboutRespBean> call = service.getNewsAbout(body);
        call.enqueue(new Callback<NewsAboutRespBean>() {
            @Override
            public void onResponse(Call<NewsAboutRespBean> call, Response<NewsAboutRespBean> response) {
                Log.e("requestSuccess", "%-----------------------" + response.body());
                if (getActivity() != null && response.body().getData() != null){
                    mBinding.commentsBg.setVisibility(View.VISIBLE);
                    initNewsAbout(response.body());
                }
            }

            @Override
            public void onFailure(Call<NewsAboutRespBean> call, Throwable throwable) {
                Log.e("requestFailure", throwable.getMessage() + "");
                mBinding.rlGame.setVisibility(View.GONE);
                mBinding.news1.setVisibility(View.GONE);
                mBinding.news2.setVisibility(View.GONE);
                mBinding.news3.setVisibility(View.GONE);
            }
        });
    }

    // 与js交互
    public class JavaScriptInterface {
        Context mContext;

        JavaScriptInterface(Context context) {
            mContext = context;
        }

        @JavascriptInterface
        public void gobrowseurl(String url) {
//			ToastUtil.showToast(mContext,"url:"+url);
            AppUtil.OpenUrl(mContext, url);
        }

        @JavascriptInterface
        public void clickImg(String[] globalimgs, String index) {
//			isJump = true;
            Intent intent = new Intent(mContext, ImageActivity.class);
            intent.putExtra("position", index);
            intent.putExtra("title", newsBean.getTitle());
            intent.putStringArrayListExtra("url", new ArrayList<String>(Arrays.asList(globalimgs)));
            startActivity(intent);
        }

        @JavascriptInterface
        public void goAuthorDetail(String auth_id) {
            if (MyApplication.getUserData() == null || !MyApplication.getUserData().loginStatue) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            } else {
                Intent intent = new Intent(mContext, AuthorDetailActivity.class);
                intent.putExtra("uid", auth_id);
                mContext.startActivity(intent);
            }
        }

        @JavascriptInterface
        public String getUid() {
            if (MyApplication.getUserData() == null || !MyApplication.getUserData().loginStatue) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                return "-1";
            } else {
                return MyApplication.getUserData().uid;
            }
        }

    }

    public void obtainComments() {//获取热门评论内容
        String str_uid = MyApplication.getUserData().loginStatue ? MyApplication.getUserData().uid : "0";
        int uid = Integer.parseInt(str_uid);
        String arcurl = newsBean.getArcurl();
        long time = System.currentTimeMillis();
        String validate = uid + arcurl + c_sid + 3 + mPage + time;
        String sign = StringUtil.MD5(validate);
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", uid);
            obj.put("arcurl", arcurl);
            obj.put("c_sid", c_sid);
            obj.put("pagesize", 3);
            obj.put("page", mPage);
            obj.put("time", time);
            obj.put("sign", sign);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mNewsPresenter.getHotComment(obj.toString());//异步请求
    }

    private void updateComment(int commentType) {
        ArrayList<CommentBean> mCommentList = new ArrayList<CommentBean>();
        if (commentType == 1) {//1:最早
            if (myCommentData.getData().getHotlist() != null && myCommentData.getData().getHotlist().size() > 0) {
                myCommentData.getData().getHotlist().get(0).setType("热门评论");
                mCommentList.addAll(myCommentData.getData().getHotlist());
            }
            if (myCommentData.getData().getTimelist() != null && myCommentData.getData().getTimelist().size() > 0) {
                myCommentData.getData().getTimelist().get(0).setType("所有评论");
                mCommentList.addAll(myCommentData.getData().getTimelist());
            }

            mAdapter.clearAndAddList(mCommentList);
        } else {//最热
            if (myCommentData.getData().getHotlist() != null && myCommentData.getData().getHotlist().size() > 0) {
                myCommentData.getData().getHotlist().get(0).setType("热门评论");
                mCommentList.addAll(myCommentData.getData().getHotlist());
            }
            if (myCommentData.getData().getList() != null && myCommentData.getData().getList().size() > 0) {
                myCommentData.getData().getList().get(0).setType("所有评论");
                mCommentList.addAll(myCommentData.getData().getList());
            }

            mAdapter.clearAndAddList(mCommentList);
        }
    }

    CommonRecyclerAdapter mAdapter = null;

    private void initComments(final NewsHotCommentRespBean commentData) {// 初始化评论
        if (commentData != null && commentData.getData() != null) {
            myCommentData = commentData;
            //设置收藏状态
            f_sid = commentData.getData().getF_sid();
            if (commentData.getData().getFavorite() == 1) {//收藏状态1已收藏0未收藏
                mBinding.imgCollect.setImageResource(R.drawable.collect);
                favorite = 1;
            } else {
                mBinding.imgCollect.setImageResource(R.drawable.click_collect);
                favorite = 2;
            }
        }
        if (commentData == null || commentData.getData() == null || commentData.getData().getList() == null || commentData.getData().getList().size() == 0) {
            mBinding.noData.setVisibility(View.VISIBLE);
            return;
        }
//		mBinding.rlComment.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				NewsActivity.viewPager.setCurrentItem(1);
//			}
//		});
        mBinding.txtComment.setText(commentData.getData().getTotal() + "");
        mBinding.txtComment.setVisibility(View.VISIBLE);
        ArrayList<CommentBean> mCommentList = new ArrayList<CommentBean>();
        if (commentData.getData().getHotlist() != null && commentData.getData().getHotlist().size() > 0) {
            commentData.getData().getHotlist().get(0).setType("热门评论");
            mCommentList.addAll(commentData.getData().getHotlist());
        }
        if (commentData.getData().getList() != null && commentData.getData().getList().size() > 0) {
            commentData.getData().getList().get(0).setType("所有评论");
            mCommentList.addAll(commentData.getData().getList());
        }

//		初始化recyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.mRecycleView.setLayoutManager(layoutManager);

        mBinding.mRecycleView.setPullRefreshEnabled(false);
        mBinding.mRecycleView.setLoadingMoreEnabled(false);

        mBinding.mRecycleView.setNestedScrollingEnabled(false);
        mAdapter = new CommonRecyclerAdapter<CommentBean>(mContext, R.layout.adapter_comments) {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void bindData(final RecyclerViewHolder holder, final int position, final CommentBean comments) {
                if (!StringUtil.isEmpty(comments.getType())) {
                    if ("所有评论".equals(comments.getType())) {
                        holder.getView(R.id.rb2).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                updateComment(2);
                            }
                        });
                        holder.getView(R.id.rb1).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                updateComment(1);
                            }
                        });
                        holder.getView(R.id.rg_order).setVisibility(View.VISIBLE);
                    } else {
                        holder.getView(R.id.rg_order).setVisibility(View.GONE);
                    }

                    holder.getView(R.id.llAllComments).setVisibility(View.VISIBLE);
                    holder.setText(R.id.tv_comment_title, comments.getType());
                } else {
                    holder.getView(R.id.llAllComments).setVisibility(View.GONE);
                }

//				holder.setImageByUrl(R.id.img_head,comments.getUser().avatarstr);
                GlideUtil.loadCircleImage(mContext, comments.getUser().avatarstr, (ImageView) holder.getView(R.id.img_head));
                TextView tvName = holder.getView(R.id.tv_name);
                if ("3DM官方账号".equals(comments.getUser().nickname)) {
                    tvName.setTextColor(0xffd02b29);
                } else {
                    tvName.setTextColor(0xff00a0e9);
                }
                tvName.setText(comments.getUser().nickname);
                holder.setText(R.id.tv_locate, comments.getUser().regionstr.length() > 0 ? comments.getUser().regionstr + " 网友" : "");
                TextView tv_head_title = holder.getView(R.id.tv_head_title);
                tv_head_title.setTextColor(getResources().getColor(R.color.lightBlack));
                switch (comments.getUser().getAuth_level()) {
                    case 0:
                    case 1:
                        tv_head_title.setTextColor(getResources().getColor(R.color.white));
                        tv_head_title.setBackground(getResources().getDrawable(R.drawable.bg_chengse));
                        break;
                    case 2:
                        tv_head_title.setTextColor(getResources().getColor(R.color.white));
                        tv_head_title.setBackground(getResources().getDrawable(R.drawable.bg_green));
                        break;
                    case 3:
                        tv_head_title.setTextColor(getResources().getColor(R.color.white));
                        tv_head_title.setBackground(getResources().getDrawable(R.drawable.bg_gray));
                        break;
                    case 4:
                        tv_head_title.setTextColor(getResources().getColor(R.color.white));
                        tv_head_title.setBackground(getResources().getDrawable(R.drawable.bg_black));
                        break;
                }
                if (StringUtil.isEmpty(comments.getUser().getAuth_title())) {
                    tv_head_title.setTextColor(Color.parseColor("#A10000"));
                    tv_head_title.setBackground(getResources().getDrawable(R.drawable.bg_black));
                    holder.getView(R.id.user_lv).setVisibility(View.VISIBLE);
                    tv_head_title.setVisibility(View.GONE);
                    TextView view = holder.getView(R.id.user_lv);
                    view.setText("Lv." + comments.getUser().getUser_level());
                } else {
                    tv_head_title.setText("" + comments.getUser().getAuth_title());
                    holder.getView(R.id.user_lv).setVisibility(View.GONE);
                    tv_head_title.setVisibility(View.VISIBLE);
                }
                holder.setText(R.id.tv_posi, "" + comments.getPosition());
                holder.setText(R.id.tv_good, comments.getGoodcount() + "");
                holder.setText(R.id.tv_bad, comments.getBadcount() + "");


                final TextView follow = holder.getView(R.id.follow);
                if (comments.getUser().getIs_follow() == 0) {
                    follow.setText("关注");
                    follow.setTextColor(Color.parseColor("#ffffff"));
                    follow.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_rectangle));
                } else if (comments.getUser().getIs_follow() == 1) {
                    follow.setText("已关注");
                    follow.setTextColor(Color.parseColor("#444444"));
                    follow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_gray));
                } else {
                    follow.setText("互相关注");
                    follow.setTextColor(Color.parseColor("#444444"));
                    follow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_gray));
                }
                follow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View views) {
                        if (comments.getUser().getIs_follow() == 0) {
                            comments.getUser().setIs_follow(1);
                            follow.setText("已关注");
                            follow.setTextColor(Color.parseColor("#444444"));
                            follow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_gray));
                            subfollow(1, comments.getUser().uid + "");

                        } else if (comments.getUser().getIs_follow() == 1) {
                            comments.getUser().setIs_follow(0);
                            follow.setText("关注");
                            follow.setTextColor(Color.parseColor("#ffffff"));
                            follow.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_rectangle));
                            subfollow(0, comments.getUser().uid + "");
                        } else {
                            follow.setText("关注");
                            comments.getUser().setIs_follow(0);
                            follow.setTextColor(Color.parseColor("#ffffff"));
                            follow.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_rectangle));
                            subfollow(0, comments.getUser().uid + "");
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });


                TextView content = holder.getView(R.id.tv_content);
                content.setText(emoutil.addSmileySpans(comments.getContent()));
//				holder.setText(R.id.tv_content,comments.getContent());
                holder.setText(R.id.tv_time, TimeUtil.getFoolishTime(comments.getPubdate_at()));
                final TextView tvGood = holder.getView(R.id.tv_good);
                final ImageView imgGood = holder.getView(R.id.imgGood);
                holder.getView(R.id.imgGood).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (MyApplication.getUserData().loginStatue) {
                            if (MyApplication.getUserData().mobile.length() == 0) {
                                Intent intent = new Intent(mContext, ForgetPassActivity.class);
                                intent.putExtra("type", 1);//1,绑定手机号，2修改密码，3忘记密码
                                startActivity(intent);
                            } else {
                                if (comments.getPraise() == 0)
                                    setPraise(imgGood, tvGood, comments.getGoodcount(), comments.getId(), 1);//1赞2踩
                            }
                        } else {
                            ToastUtil.showToast(mContext, "请先登录！");
                            startActivity(new Intent(mContext, LoginActivity.class));
                        }
                    }
                });
//				final TextView tvGad=holder.getView(R.id.tv_bad);
//				final ImageView imgBad=holder.getView(R.id.imgBad);
//				holder.getView(R.id.imgBad).setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View view) {
//						if(comments.getPraise()==0)
//							setPraise(imgBad,tvGad,comments.getBadcount(),comments.getId(),2);//1赞2踩
//					}
//				});
                holder.setImageResource(R.id.imgGood, R.drawable.click_good_grey);
//				holder.setImageResource(R.id.imgBad,R.drawable.click_bad_grey);
                switch (comments.getPraise()) {
                    case 1:
                        holder.setImageResource(R.id.imgGood, R.drawable.click_good_red);
                        break;
                    case 2:
                        holder.setImageResource(R.id.imgBad, R.drawable.click_bad_red);
                        break;
                }

                holder.getView(R.id.tv_report).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submitReport(comments.getId());
                    }
                });

                TextView tv_reply = holder.getView(R.id.tv_reply);
                tv_reply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (MyApplication.getUserData().loginStatue) {
                            if (MyApplication.getUserData().mobile.length() == 0) {
                                Intent intent = new Intent(mContext, ForgetPassActivity.class);
                                intent.putExtra("type", 1);//1,绑定手机号，2修改密码，3忘记密码
                                startActivity(intent);
                            } else {
                                replyId = comments.getId();
                                // 获取编辑框焦点
                                mBinding.etComment.setFocusable(true);
                                //打开软键盘
                                final InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                                mBinding.etComment.requestFocus();
                            }
                        } else {
                            ToastUtil.showToast(mContext, "请先登录！");
                            startActivity(new Intent(mContext, LoginActivity.class));
                        }
                    }
                });

                if (comments.getReplies() != null && comments.getReplies().size() > 0) {
                    XRecyclerView recyclerView = holder.getView(R.id.mRecycleView);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setNestedScrollingEnabled(false);
                    recyclerView.setPullRefreshEnabled(false);
                    recyclerView.setLoadingMoreEnabled(false);

                    BaseRecyclerAdapter<CommentBean> mSubRecyclerAdapter = new BaseRecyclerAdapter<CommentBean>(mContext, R.layout.adapter_child_comments) {
                        @Override
                        public void bindData(RecyclerViewHolder holder, final int position, final CommentBean bean) {
                            holder.setText(R.id.tv_time, TimeUtil.getFoolishTime(bean.getPubdate_at()));
                            holder.setText(R.id.tv_locate, bean.getUser().regionstr.length() > 0 ? bean.getUser().regionstr + " 网友" : "");
//							holder.setText(R.id.tv_name,bean.getUser().nickname);
//							holder.setText(R.id.tv_content,bean.getContent());
                            TextView content = holder.getView(R.id.tv_content);
                            if ("3DM官方账号".equals(bean.getUser().nickname)) {
                                String str = "<font color='#d02b29'>" + bean.getUser().nickname + "</font>" + " : " + bean.getContent();
                                content.setText(emoutil.addSmileySpans(Html.fromHtml(str)));
                            } else {
                                String str = "<font color='#00a0e9'>" + bean.getUser().nickname + "</font>" + " : " + bean.getContent();
                                content.setText(emoutil.addSmileySpans(Html.fromHtml(str)));
                            }
//							String str="<font color='#00a0e9'>"+bean.getUser().nickname+"</font>"+" : "+emoutil.addSmileySpans(comments.getContent());
//							content.setText(Html.fromHtml(str));
//							TextView tv_head_title=holder.getView(R.id.tv_head_title);
//							switch (comments.getUser().title_level) {
//								case 0:
//								case 1:
//									tv_head_title.setBackground(getResources().getDrawable(R.drawable.ic_title1));
//									break;
//								case 2:
//									tv_head_title.setBackground(getResources().getDrawable(R.drawable.ic_title1));
//									break;
//								case 3:
//									tv_head_title.setBackground(getResources().getDrawable(R.drawable.ic_title1));
//									break;
//								case 4:
//									tv_head_title.setBackground(getResources().getDrawable(R.drawable.ic_title1));
//									break;
//							}
//							tv_head_title.setText(""+comments.getUser().title);
//							设置子品论点赞数目
                            holder.setText(R.id.tv_num_good, bean.getGoodcount() + "");
                            final TextView tvGood = holder.getView(R.id.tv_num_good);
                            final ImageView imgGood = holder.getView(R.id.imgGood);
                            holder.getView(R.id.imgGood).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (bean.getPraise() == 0)
                                        setPraise(imgGood, tvGood, bean.getGoodcount(), bean.getId(), 1);//1赞2踩
                                }
                            });
                            holder.setImageResource(R.id.imgGood, R.drawable.click_good_grey);
//							holder.setText(R.id.tv_num_bad,bean.getBadcount()+"");
//							final TextView tvGad=holder.getView(R.id.tv_num_bad);
//							final ImageView imgBad=holder.getView(R.id.imgBad);
//							holder.getView(R.id.imgBad).setOnClickListener(new View.OnClickListener() {
//								@Override
//								public void onClick(View view) {
//									if(comments.getPraise()==0)
//										setPraise(imgBad,tvGad,bean.getBadcount(),bean.getId(),2);//1赞2踩
//								}
//							});
//							holder.setImageResource(R.id.imgBad,R.drawable.click_bad_grey);
                            switch (bean.getPraise()) {
                                case 1:
                                    holder.setImageResource(R.id.imgGood, R.drawable.click_good_red);
                                    break;
                                case 2:
                                    holder.setImageResource(R.id.imgBad, R.drawable.click_bad_red);
                                    break;
                            }

                            TextView txtComment = holder.getView(R.id.tv_reply);
                            txtComment.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (MyApplication.getUserData().loginStatue) {
                                        if (MyApplication.getUserData().mobile.length() == 0) {
                                            Intent intent = new Intent(mContext, ForgetPassActivity.class);
                                            intent.putExtra("type", 1);//1,绑定手机号，2修改密码，3忘记密码
                                            startActivity(intent);
                                        } else {
                                            replyId = bean.getId();
                                            // 获取编辑框焦点
                                            mBinding.etComment.setFocusable(true);
                                            //打开软键盘
                                            final InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                                            mBinding.etComment.requestFocus();
                                        }
                                    } else {
                                        ToastUtil.showToast(mContext, "请先登录！");
                                        startActivity(new Intent(mContext, LoginActivity.class));
                                    }
                                }
                            });
                        }
                    };
                    recyclerView.setAdapter(mSubRecyclerAdapter);
                    List<CommentBean> newList = comments.getReplies();
                    Collections.reverse(newList);
                    mSubRecyclerAdapter.clearAndAddList(newList);
//					mSubRecyclerAdapter.clearAndAddList(comments.getReplies());
                    holder.getView(R.id.mRecycleView).setVisibility(View.VISIBLE);
                } else {
                    holder.getView(R.id.mRecycleView).setVisibility(View.GONE);
                }
            }
        };
        mBinding.mRecycleView.setAdapter(mAdapter);
        mAdapter.clearAndAddList(mCommentList);
        mBinding.llMore.setVisibility(View.VISIBLE);
    }

    //关注or取消
    private void subfollow(int type, String follow_uid) {
        UserDataBean userData = MyApplication.getUserData();
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(userData.uid + follow_uid + type + time + NewUrl.KEY);

        Map<String, Object> values = new HashMap<>();
        values.put("uid", userData.uid);
        values.put("sign", sign);
        values.put("time", time);
        values.put("type", type);
        values.put("follow_uid", follow_uid);
        String json = new JSONObject(values).toString();
        OkHttpUtils
                .postString()
                .url(NewUrl.SUBFOLLOW)
                .content(json)
                .build()
                .execute(new com.zhy.http.okhttp.callback.Callback<String>() {

                    @Override
                    public String parseNetworkResponse(com.squareup.okhttp.Response response) throws IOException {
                        return response.body().string();
                    }

                    @Override
                    public void onError(com.squareup.okhttp.Request request, Exception e) {
                        Log.e("message", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("FFFFFFFF", response);
                    }

                });

    }

    private void initNewsAbout(final NewsAboutRespBean about) {
        //初始化投票
        if (about.getData() != null) {
            final List<NewsAboutRespBean.DataBean.VotetopicBean> voteList = about.getData().getVotetopic();
            if (voteList != null) {
                int totalNum = voteList.get(0).getTopic_num() + voteList.get(1).getTopic_num() + voteList.get(2).getTopic_num();
                mBinding.tvChoiceTitle1.setText(voteList.get(0).getTopic_title());
                mBinding.btVote1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setVote(voteList.get(0).getTopic_id(), mBinding.pbView1, mBinding.tvVoteNum1);
                    }
                });
                mBinding.tvVoteNum1.setText(voteList.get(0).getTopic_num() + "");
                mBinding.pbView1.setProgress(100 * voteList.get(0).getTopic_num() / totalNum);

                mBinding.tvChoiceTitle2.setText(voteList.get(1).getTopic_title());
                mBinding.btVote2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setVote(voteList.get(1).getTopic_id(), mBinding.pbView2, mBinding.tvVoteNum2);
                    }
                });
                mBinding.tvVoteNum2.setText(voteList.get(1).getTopic_num() + "");
                mBinding.pbView2.setProgress(100 * voteList.get(1).getTopic_num() / totalNum);

                mBinding.tvChoiceTitle3.setText(voteList.get(2).getTopic_title());
                mBinding.btVote3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setVote(voteList.get(2).getTopic_id(), mBinding.pbView3, mBinding.tvVoteNum3);
                    }
                });
                mBinding.tvVoteNum3.setText(voteList.get(2).getTopic_num() + "");
                mBinding.pbView3.setProgress(100 * voteList.get(2).getTopic_num() / totalNum);

                mBinding.llVote.setVisibility(View.VISIBLE);
            }
        }


        //初始化相关内容
        if (about == null || about.getData() == null) {
            mBinding.tvLine2.setVisibility(View.GONE);
            mBinding.tvLine4.setVisibility(View.GONE);
            mBinding.rlGame.setVisibility(View.GONE);
            mBinding.tvTitle1.setVisibility(View.GONE);
            mBinding.news1.setVisibility(View.GONE);
            mBinding.news2.setVisibility(View.GONE);
            mBinding.news3.setVisibility(View.GONE);
            return;
        }
        GameBean game = null;
        if (about != null && about.getData().getGame() != null) {
            game = about.getData().getGame();
            GlideUtil.loadImage(mContext.getApplicationContext().getApplicationContext(), game.getLitpic(), mBinding.imgCover);
            mBinding.txtName.setText(game.getTitle());
            mBinding.tvScore.setText(game.getScore() + "");
            mBinding.tvType.setText("类型：" + game.getType());
            mBinding.txtTime.setText("发售：" + (game.getPubdate_at() == 0 ? "未知" : TimeUtil.getFormatTimeSimple(game.getPubdate_at())));
            mBinding.tvLabel.setText("标签：" + game.getLabelString());

            String[] sy = game.getSystem().split("/");
            switch (sy.length > 5 ? 5 : sy.length) {
                case 5:
                    mBinding.txtLabel5.setVisibility(View.VISIBLE);
                case 4:
                    mBinding.txtLabel4.setText(sy[3]);
                    mBinding.txtLabel4.setVisibility(View.VISIBLE);
                case 3:
                    mBinding.txtLabel3.setText(sy[2]);
                    mBinding.txtLabel3.setVisibility(View.VISIBLE);
                case 2:
                    mBinding.txtLabel2.setText(sy[1]);
                    mBinding.txtLabel2.setVisibility(View.VISIBLE);
                case 1:
                    mBinding.txtLabel1.setText(sy[0]);
                    mBinding.txtLabel1.setVisibility(View.VISIBLE);
            }

            mBinding.rlGame.setVisibility(View.VISIBLE);

            final GameBean finalGame = game;
            mBinding.rlGame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, GameHomeActivity.class);
                    Bundle bundle = new Bundle();
//					bundle.putSerializable("game", finalGame);//数据太大，超出系统限制
                    bundle.putString("str_game", JSON.toJSONString(finalGame));
                    intent.putExtras(bundle);
                    startActivity(intent);
                    getActivity().finish();
                }
            });

            mBinding.tvLine1.setVisibility(View.VISIBLE);
            mBinding.rlGame.setVisibility(View.VISIBLE);
        } else {
            mBinding.tvLine1.setVisibility(View.GONE);
            mBinding.rlGame.setVisibility(View.GONE);
        }

        if (about != null && about.getData().getList() != null) {
            mBinding.tvTitle1.setVisibility(View.VISIBLE);
            mBinding.tvLine2.setVisibility(View.VISIBLE);

            int aboutSize = about.getData().getList().size() > 3 ? 3 : about.getData().getList().size();
            switch (aboutSize) {
                case 3:
                    GlideUtil.loadImage(mContext.getApplicationContext(), about.getData().getList().get(2).getLitpic(), mBinding.img3);
                    mBinding.title3.setText(about.getData().getList().get(2).getTitle());
                    mBinding.time3.setText(TimeUtil.getFoolishTime2(about.getData().getList().get(2).getPubdate_at() + "000"));
                    mBinding.comment3.setText(about.getData().getList().get(2).getTotal_ct() + "");
                    mBinding.news3.setVisibility(View.VISIBLE);
                case 2:
                    GlideUtil.loadImage(mContext.getApplicationContext(), about.getData().getList().get(1).getLitpic(), mBinding.img2);
                    mBinding.title2.setText(about.getData().getList().get(1).getTitle());
                    mBinding.time2.setText(TimeUtil.getFoolishTime2(about.getData().getList().get(1).getPubdate_at() + "000"));
                    mBinding.comment2.setText(about.getData().getList().get(1).getTotal_ct() + "");
                    mBinding.news2.setVisibility(View.VISIBLE);
                case 1:
                    GlideUtil.loadImage(mContext.getApplicationContext(), about.getData().getList().get(0).getLitpic(), mBinding.img1);
                    mBinding.title1.setText(about.getData().getList().get(0).getTitle());
                    mBinding.time1.setText(TimeUtil.getFoolishTime2(about.getData().getList().get(0).getPubdate_at() + "000"));
                    mBinding.comment1.setText(about.getData().getList().get(0).getTotal_ct() + "");
                    mBinding.news1.setVisibility(View.VISIBLE);
            }
        } else {
            mBinding.news1.setVisibility(View.GONE);
            mBinding.news2.setVisibility(View.GONE);
            mBinding.news3.setVisibility(View.GONE);
        }
        mBinding.news1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("newsBean", about.getData().getList().get(0));
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
//				getActivity().finish();
            }
        });
        mBinding.news2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("newsBean", about.getData().getList().get(1));
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
//				getActivity().finish();
            }
        });
        mBinding.news3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("newsBean", about.getData().getList().get(2));
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
//				getActivity().finish();
            }
        });
    }

    public void setVote(int vid, final RoundCornerProgressBar pb, final TextView tv) {
        if (!MyApplication.getUserData().loginStatue) {
            ToastUtil.showToast(mContext, "请先登录！");
            startActivity(new Intent(mContext, LoginActivity.class));
            return;
        } else if (MyApplication.getUserData().mobile.length() == 0) {//第三方登陆，非绑定跳转绑定界面
            Intent intent = new Intent(mContext, ForgetPassActivity.class);
            intent.putExtra("type", 1);//1,绑定手机号，2修改密码，3忘记密码
            startActivity(intent);
            return;
        }

        String temp = MyApplication.getUserData().loginStatue ? MyApplication.getUserData().uid : "0";
        int uid = Integer.parseInt(temp);
        long time = System.currentTimeMillis();
        String validate = "" + uid + vid + time;
        String sign = StringUtil.MD5(validate);
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", MyApplication.getUserData().uid);
            obj.put("vid", vid);
            obj.put("time", time);
            obj.put("sign", sign);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //同步请求
        Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
        UserService.Api service = retrofit.create(UserService.Api.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
        Call<ResponseBody> call = service.addVote(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("requestSuccess", "-----------------------" + response.body());
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if (jsonObject.optInt("code") == 1) {
                        pb.setProgress(pb.getProgress() + 1);
                        tv.setText(1 + Integer.parseInt(tv.getText().toString()) + "");
                    } else {
                        //ToastUtil.showToast(mContext, jsonObject.optString("msg") + "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("requestFailure", throwable.getMessage() + "");
            }
        });
    }

    /*	public void getFavoriteStute(){//获取收藏状态
            String temp = MyApplication.getUserData().loginStatue ? MyApplication.getUserData().uid : "0";
            int uid=Integer.parseInt(temp);
            String arcurl = newsBean.getArcurl();
            long time=System.currentTimeMillis();
            String validate= ""+uid+arcurl+f_sid+time;
            String sign= StringUtil.MD5(validate);
            JSONObject obj = new JSONObject();
            try {
                obj.put("uid", MyApplication.getUserData().uid);
                obj.put("arcurl",arcurl);
                obj.put("f_sid",f_sid);
                obj.put("time", time);
                obj.put("sign", sign);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //同步请求
            Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
            UserService.Api service = retrofit.create(UserService.Api.class);
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
            Call<ResponseBody> call = service.getArcFavorite(body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.e("requestSuccess", "-----------------------" + response.body());
                    try {
                        JSONObject jsonObject=new JSONObject(response.body().string());
                        if(jsonObject.optInt("code")==1){
                            JSONObject jsonSub=new JSONObject(jsonObject.opt("data").toString());
                            f_sid=jsonSub.optInt("f_sid");
                            if(jsonSub.optInt("favorite")==1){//收藏状态1已收藏0未收藏
                                mBinding.imgCollect.setImageResource(R.drawable.collect);
                                favorite=1;
                            }else{
                                mBinding.imgCollect.setImageResource(R.drawable.click_collect);
                                favorite=2;
                            }
    //					}else{
    //						ToastUtil.showToast(mContext,jsonObject.opt("msg")+"");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    Log.e("requestFailure", throwable.getMessage() + "");
                }
            });
        }
        */
    //点赞，踩 接口
    public void setPraise(final ImageView targetImg, final TextView targetView, final int firstNum, int id, final int type) {//目标textview，初始数目，评论id，点赞类型:1赞2踩
        String temp = MyApplication.getUserData().loginStatue ? MyApplication.getUserData().uid : "0";
        int uid = Integer.parseInt(temp);
        long time = System.currentTimeMillis();
        String validate = "" + uid + id + type + time;
        String sign = StringUtil.MD5(validate);
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", MyApplication.getUserData().uid);
            obj.put("id", id);
            obj.put("arcurl", newsBean.getArcurl());
            obj.put("type", type);
            obj.put("time", time);
            obj.put("sign", sign);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //同步请求
        Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
        UserService.Api service = retrofit.create(UserService.Api.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
        Call<ResponseBody> call = service.addPraise(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("requestSuccess", "-----------------------" + response.body());
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if (jsonObject.optInt("code") == 1) {
                        targetView.setText(firstNum + 1 + "");
                        if (type == 1)
                            targetImg.setImageResource(R.drawable.click_good_red);
                        else
                            targetImg.setImageResource(R.drawable.click_bad_red);
                    } else {
                        //ToastUtil.showToast(mContext, jsonObject.opt("msg") + "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("requestFailure", throwable.getMessage() + "");
            }
        });
    }

    public void addDelFavorite(int isAdd) {//1添加2删除
        String temp = MyApplication.getUserData().loginStatue ? MyApplication.getUserData().uid : "0";
        int uid = Integer.parseInt(temp);
        String arcurl = newsBean.getArcurl();
        int showtype = isAdd == 2 ? 0 : 1;
        long time = System.currentTimeMillis();
        String validate = "" + uid + arcurl + f_sid + showtype + isAdd + time;
        String sign = StringUtil.MD5(validate);
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", MyApplication.getUserData().uid);
            obj.put("arcurl", arcurl);
            obj.put("f_sid", f_sid);
            obj.put("showtype", showtype);//展示类型:1新闻2攻略3游戏4手游5杂谈6评测7原创8安利9礼包(删除收藏时可传0)
            obj.put("act", isAdd);
            obj.put("time", time);
            obj.put("sign", sign);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //同步请求
        Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
        UserService.Api service = retrofit.create(UserService.Api.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
        Call<ResponseBody> call = service.setArcFavorite(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("requestSuccess", "-----------------------" + response.body());
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if (jsonObject.optInt("code") == 1) {
                        JSONObject jsonSub = new JSONObject(jsonObject.opt("data").toString());
                        f_sid = jsonSub.optInt("code");
                        favorite = 3 - favorite;
                        if (favorite == 1) {
                            mBinding.imgCollect.setImageResource(R.drawable.collect);
                            ToastUtil.showToast(mContext, "收藏成功！");
                        } else {
                            mBinding.imgCollect.setImageResource(R.drawable.click_collect);
                            ToastUtil.showToast(mContext, "取消成功！");
                        }
                    } else {
                        //ToastUtil.showToast(mContext, jsonObject.opt("msg") + "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("requestFailure", throwable.getMessage() + "");
            }
        });
    }

    private OkHttpClient mOkHttpClient;//okHttpClient 实例

    public void obtainDataAds() {
        mOkHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS)//设置写入超时时间
                .build();

        String actionUrl = "https://e.sogou.com/ads";
        String requestID = "001850001125" + System.currentTimeMillis();
        String auth = StringUtil.getMD5(requestID + "CHPOSXUnDNUSpUmBd3kiQ");
        JSONObject supObj = new JSONObject();
        try {
            supObj.put("requestId", requestID);
            supObj.put("auth", auth);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject obj = new JSONObject();
        try {
            obj.put("deviceId", "861852034952905");
            obj.put("network", 6);
            obj.put("deviceType", 1);
            obj.put("imei", "861852034952905");
            obj.put("androidId", "8F029BD7BCF2942");
            obj.put("brand", "360");
            obj.put("model", "1501_M02");
            obj.put("os", "android");
            obj.put("osVersion", "5.1");
            obj.put("carrier", 1);
            obj.put("ip", "61.191.24.233");

            supObj.put("device", obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray();
        try {
            obj = new JSONObject();
            obj.put("id", 1);//sgxxl001 需要替换
            obj.put("count", 0);
            jsonArray.put(obj);

            supObj.put("imps", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        obj = new JSONObject();
        try {
            obj.put("pkgName", "com.ws3dm.app");

            supObj.put("app", obj);
            supObj.put("version", "3.5");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request.Builder builder = new Request.Builder()
                .addHeader("Connection", "keep-alive")
                .addHeader("platform", "2")
                .addHeader("phoneModel", Build.MODEL)
                .addHeader("systemVersion", Build.VERSION.RELEASE)
                .addHeader("appVersion", "3.2.0");

        try {
            //处理参数
            String requestUrl = actionUrl;
            //生成参数
            //创建一个请求实体对象 RequestBody
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), supObj.toString());
            //创建一个请求
            final Request request = builder.url(requestUrl).post(body).build();
            //创建一个Call
            final okhttp3.Call call = mOkHttpClient.newCall(request);
            //执行请求
            okhttp3.Response response = call.execute();

            String jsonStr = response.body().string();
            Log.e("kkk", jsonStr);
            //请求执行成功
            if (response.isSuccessful()) {
                //获取返回数据 可以是String，bytes ,byteStream
                if (!StringUtil.isEmpty(jsonStr)) {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        String dataStr = jsonObject.optString("data");
                        JSONObject json2 = new JSONObject(dataStr);
                        String dataStr2 = json2.optString("groups");
                        com.alibaba.fastjson.JSONArray jsonArrayRows = JSON.parseArray(dataStr2);
                        com.alibaba.fastjson.JSONObject json4 = jsonArrayRows.getJSONObject(0);
                        com.alibaba.fastjson.JSONArray ads = json4.getJSONArray("ads");

                        com.alibaba.fastjson.JSONObject imgJson = ads.getJSONObject(0);
                        com.alibaba.fastjson.JSONArray imgArray = imgJson.getJSONArray("imgs");
                        com.alibaba.fastjson.JSONObject img0 = imgArray.getJSONObject(0);
                        imgURL = img0.getString("url");
//                        imgURL="http://adstream.123.sogoucdn.com/lst/pic/1520592467453.jpg";//测试用的

                        clickURL = imgJson.getString("link");

                        if (!StringUtil.isEmpty(imgURL) && !StringUtil.isEmpty(clickURL)) {
                            Message message = new Message();
                            message.what = 1;
                            msgHandle.sendMessage(message);
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                } else {
                    Log.e("KKK", "广告获取错误");
                }
            }
        } catch (Exception e) {
            Log.e("kkk", e.toString());
        }
    }

    public void showTipError(View v) {
        final View vPop = LayoutInflater.from(getActivity()).inflate(R.layout.view_tip_error, null);
        popTip = DialogHelper.createPopupWindow(getActivity(), vPop, R.style.AnimBottom);
        popTip.setFocusable(true);

        final EditText et_title = (EditText) vPop.findViewById(R.id.et_title);
        final EditText etContent = (EditText) vPop.findViewById(R.id.etContent);
        final EditText etconnect = (EditText) vPop.findViewById(R.id.etconnect);
        TextView tv_link_url = (TextView) vPop.findViewById(R.id.tv_link_url);
        tv_link_url.setText(newsBean.getArcurl());
        final RadioButton rd_real = (RadioButton) vPop.findViewById(R.id.rd_real);
        rd_real.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    vPop.findViewById(R.id.tv_connect).setVisibility(View.VISIBLE);
                    vPop.findViewById(R.id.ll_connect).setVisibility(View.VISIBLE);
                } else {
                    vPop.findViewById(R.id.tv_connect).setVisibility(View.GONE);
                    vPop.findViewById(R.id.ll_connect).setVisibility(View.GONE);
                }
            }
        });

        vPop.findViewById(R.id.tv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringUtil.isEmpty(et_title.getText().toString().trim())) {
                    ToastUtil.showToast(mContext, "标题不能为空！");
                } else if (StringUtil.isEmpty(etContent.getText().toString().trim())) {
                    ToastUtil.showToast(mContext, "举报内容不能为空！");
                } else if (rd_real.isChecked() && StringUtil.isEmpty(etconnect.getText().toString().trim())) {
                    ToastUtil.showToast(mContext, "联系方式不能为空！");
                } else {
                    submitError(et_title.getText().toString().trim(), etContent.getText().toString().trim(), etconnect.getText().toString().trim());
                    popTip.dismiss();
                }
            }
        });
        vPop.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popTip.dismiss();
            }
        });

        DialogHelper.showPop(getActivity(), v, popTip, Gravity.CENTER, 0, 0);
    }

    public void submitError(String title, String content, String contact) {//提交举报内容
        String uid = MyApplication.getUserData().loginStatue ? MyApplication.getUserData().uid : "0";
        String arcurl = newsBean.getArcurl();
        long time = System.currentTimeMillis();
        String validate = uid + arcurl + time;
        String sign = StringUtil.MD5(validate);
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", uid);
            obj.put("arcurl", arcurl);
            obj.put("title", title);
            obj.put("content", content);
            obj.put("contact", contact);
            obj.put("time", time);
            obj.put("sign", sign);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //同步请求
        Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
        NewsService.Api service = retrofit.create(NewsService.Api.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());

        Call<ResponseBody> call = service.pagereport(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if (jsonObject.optInt("code") == 1) {
                        ToastUtil.showToast(mContext, "举报提交成功！");
                        canReport = false;
                    } else {
                        ToastUtil.showToast(mContext, jsonObject.opt("msg") + "");
                        canReport = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("requestFailure", throwable.getMessage() + "");
            }
        });
    }

    public void submitReport(int id) {//提交举报内容
        String uid = MyApplication.getUserData().loginStatue ? MyApplication.getUserData().uid : "0";
        String arcurl = newsBean.getArcurl();
        long time = System.currentTimeMillis();
        String validate = uid + id + time;
        String sign = StringUtil.MD5(validate);
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", uid);
            obj.put("id", id);
            obj.put("arcurl", arcurl);
            obj.put("time", time);
            obj.put("sign", sign);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //同步请求
        Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
        NewsService.Api service = retrofit.create(NewsService.Api.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());

        Call<ResponseBody> call = service.report(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if (jsonObject.optInt("code") == 1) {
                        ToastUtil.showToast(mContext, "举报提交成功！");
                    } else {
                        ToastUtil.showToast(mContext, jsonObject.opt("msg") + "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("requestFailure", throwable.getMessage() + "");
            }
        });
    }

    public void clickHandler(View view) {
        switch (view.getId()) {
            case R.id.iv_emoj:
                isEmoj = !isEmoj;
                break;
            case R.id.ll_more:// 滑动到评论页面
            case R.id.tv_more:
                ((ViewPager) getActivity().findViewById(R.id.mViewPager)).setCurrentItem(1);
                break;
            case R.id.txtInput:// 隐藏键盘
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                break;
            case R.id.txtSend:// 提交评论
                if (!MyApplication.getUserData().loginStatue) {
                    ToastUtil.showToast(mContext, "请先登录！");
                    startActivity(new Intent(mContext, LoginActivity.class));
                    return;
                } else if (MyApplication.getUserData().mobile.length() == 0) {//第三方登陆，非绑定跳转绑定界面
                    Intent intent = new Intent(mContext, ForgetPassActivity.class);
                    intent.putExtra("type", 1);//1,绑定手机号，2修改密码，3忘记密码
                    startActivity(intent);
                    return;
                }
                if (newsBean != null) {
                    if (replyId <= 0) {//添加评论
                        strContent = mBinding.etComment.getText().toString().trim();
                        if (strContent.length() == 0) {
                            ToastUtil.showToast(mContext, "内容不能为空");
                            return;
                        }
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//					提交评论
                        String uid = MyApplication.getUserData().loginStatue ? MyApplication.getUserData().uid : "0";
                        String arcurl = newsBean.getArcurl();
                        String content = mBinding.etComment.getText().toString().trim();
                        long time = System.currentTimeMillis();
                        String validate = uid + arcurl + c_sid + time;
                        String sign = StringUtil.MD5(validate);
                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("uid", uid);
                            obj.put("arcurl", arcurl);
                            obj.put("c_sid", c_sid);
                            obj.put("content", content);
                            obj.put("time", time);
                            obj.put("sign", sign);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //同步请求
                        Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
                        NewsService.Api service = retrofit.create(NewsService.Api.class);
                        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());

                        Call<ResponseBody> call = service.addComment(body);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Log.e("requestSuccess", "%-----------------------" + response.body() + "提交评论成功");
                                try {
                                    String jsonString = response.body().string();
                                    JSONObject jsonObject = new JSONObject(jsonString);
                                    if (jsonObject.optInt("code") == 1) {
                                        JSONObject jsonSub = new JSONObject(jsonObject.opt("data").toString());
                                        String str_result = jsonSub.optString("integralmsg");
                                        if (StringUtil.isEmpty(str_result))
                                            ToastUtil.showToast(mContext, "提交评论成功!");
                                        else
                                            ToastUtil.showToast(mContext, str_result + "");
                                        mBinding.etComment.setText("");
                                    } else {
                                        ToastUtil.showToast(mContext, jsonObject.opt("msg") + "");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                                Log.e("requestFailure", throwable.getMessage() + "提交回帖失败");
                            }
                        });
                    } else {//回复评论
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        String uid = MyApplication.getUserData().loginStatue ? MyApplication.getUserData().uid : "0";
                        String content = mBinding.etComment.getText().toString().trim();
                        long time = System.currentTimeMillis();
                        String validate = uid + replyId + time;
                        String sign = StringUtil.MD5(validate);
                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("uid", uid);
                            obj.put("id", replyId);
                            obj.put("arcurl", newsBean.getArcurl());
                            obj.put("content", content);
                            obj.put("time", time);
                            obj.put("sign", sign);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //同步请求
                        Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
                        NewsService.Api service = retrofit.create(NewsService.Api.class);
                        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());

                        Call<ResponseBody> call = service.replyComment(body);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Log.e("requestSuccess", "%-----------------------" + response.body() + "提交评论成功");
                                try {
                                    String jsonString = response.body().string();
                                    JSONObject jsonObject = new JSONObject(jsonString);
                                    if (jsonObject.optInt("code") == 1) {
                                        JSONObject jsonSub = new JSONObject(jsonObject.opt("data").toString());
                                        String str_result = jsonSub.optString("integralmsg");
                                        if (StringUtil.isEmpty(str_result))
                                            ToastUtil.showToast(mContext, "提交评论成功!");
                                        else
                                            ToastUtil.showToast(mContext, str_result + "");
                                    } else {
                                        //ToastUtil.showToast(mContext, jsonObject.opt("msg") + "");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                                Log.e("requestFailure", throwable.getMessage() + "提交失败");
                            }
                        });
                    }
                } else {
                    ToastUtil.showToast(mContext, "网络异常");
                }
                break;
            case R.id.txtNoInput:// 隐藏键盘
                mBinding.etComment.setFocusable(true);// 获取编辑框焦点
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//打开软键盘
                mBinding.etComment.requestFocus();
                break;
            case R.id.imgCollect:// 收藏
                if (MyApplication.getUserData().loginStatue) {
                    if (MyApplication.getUserData().mobile.length() == 0) {
                        Intent intent = new Intent(mContext, ForgetPassActivity.class);
                        intent.putExtra("type", 1);//1,绑定手机号，2修改密码，3忘记密码
                        startActivity(intent);
                    } else {
                        addDelFavorite(3 - favorite);
                    }
                } else {
                    ToastUtil.showToast(mContext, "请先登录！");
                    startActivity(new Intent(mContext, LoginActivity.class));
                }
                break;
            case R.id.imgShare:// 分享
                if (newsBean != null)       //临时注释 暂时不分享
                    pop.show();
                else
                    ToastUtil.showToast(mContext, "网络异常");
                break;
            case R.id.tv_tip:// 举报
                if (canReport)
                    showTipError(view);
                else
                    ToastUtil.showToast(mContext, "已经举报过了！");
                break;
        }
    }

    @Override
    public void onDestroy() {
        if (mBinding.mWebView != null) {
            mBinding.mWebView.destroy();
        }
        super.onDestroy();
    }
}