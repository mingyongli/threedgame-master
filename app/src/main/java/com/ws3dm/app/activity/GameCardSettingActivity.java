package com.ws3dm.app.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.hwangjr.rxbus.Bus;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.R;
import com.ws3dm.app.bean.MySteamBean;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.bean.ReLoadSteamBean;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.bean.mysteampsn.Data;
import com.ws3dm.app.bean.mysteampsn.Psn;
import com.ws3dm.app.bean.mysteampsn.Steam;
import com.ws3dm.app.bean.mysteampsn.SteamPsn;
import com.ws3dm.app.databinding.AcGamecardSettingBinding;
import com.ws3dm.app.event.EventMessage;
import com.ws3dm.app.sqlite.NewsFile;
import com.ws3dm.app.util.DialogHelper;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;
import com.ws3dm.app.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.ws3dm.app.NewUrl.RELOAD_STEAM;
import static com.ws3dm.app.NewUrl.UNBIND_STEAM;

public class GameCardSettingActivity extends BaseActivity {

    private AcGamecardSettingBinding mBinding;
    private UserDataBean userData;
    //存储是否绑定steam的状态
    private int steamIsBind = 0;
    private int psnIsBind = 0;
    private String psn_nickname;

    @Override
    protected void init() {
        userData = MyApplication.getUserData();
        mBinding = bindView(R.layout.ac_gamecard_setting);
        mBinding.setHandle(this);
        getSteamPsnInfo();
        initListener();
    }

    private void initListener() {
        mBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(2);
                finish();
            }
        });
        mBinding.bindUnbindSteam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (steamIsBind == 0) {
                    //前去绑定界面
                    Intent intent = new Intent(GameCardSettingActivity.this, BindSteamActivity.class);
                    intent.putExtra("uid", userData.uid);
                    startActivity(intent);
                } else if (steamIsBind == 1) {
                    //是否取消的dialog
                    View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_unbindsteam_dialog, null);
                    Dialog dialog = DialogHelper.CreateDialog(mContext, inflate);
                    dialog.show();
                    inflate.findViewById(R.id.confirm_unbind).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            unBindSteam();
                            dialog.dismiss();
                        }
                    });
                    inflate.findViewById(R.id.cancel_unbind).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            }
        });
        mBinding.refreshStatusSteam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (steamIsBind == 0) {
                    ToastUtil.showToast(mContext, "还没有绑定Steam账号");
                } else if (steamIsBind == 1) {
                    reLoadSteamInfo();
                }
            }
        });
        mBinding.bindUnbindPsn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (psnIsBind == 0) {
                    //前去绑定界面
                    startActivity(new Intent(mContext, BindPsnActivity.class));
                } else if (psnIsBind == 1) {
                    //是否取消的dialog
                    View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_unbindosn_dialog, null);
                    Dialog dialog = DialogHelper.CreateDialog(mContext, inflate);
                    dialog.show();
                    inflate.findViewById(R.id.confirm_unbind).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            unBindPsn();
                            dialog.dismiss();
                        }
                    });
                    inflate.findViewById(R.id.cancel_unbind).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            }
        });
        mBinding.refreshStatusPsn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (psnIsBind == 0) {
                    ToastUtil.showToast(mContext, "还没有绑定psn账号");
                }
                if (psnIsBind == 1) {
                    reloadPsnInfo();
                }
            }
        });
        mBinding.psnCertification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (psnIsBind == 1) {
                    Intent intent = new Intent(mContext, PsnCertification.class);
                    intent.putExtra("psnid", psn_nickname);
                    startActivity(intent);
                }
            }
        });
        mBinding.ViewBindTutorials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //绑定
                NewsBean newsBean = new NewsBean();
                newsBean.setWebviewurl("https://m.3dmgame.com/webview/news/202012/3803410.html");
                newsBean.setArcurl("https://www.3dmgame.com/news/202012/3803410.html");
                newsBean.setType("游戏杂谈");
                newsBean.setAid(3803410);
                newsBean.setDetail_title("绑定PSN与Steam教程");
                Intent intent = new Intent(mContext, NewsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("newsBean", newsBean);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }


    /**
     * 获取绑定Steam状态信息
     */
    private void getSteamInfo() {
        if (userData == null) {
            userData = MyApplication.getUserData();
        }
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(userData.uid + time + NewUrl.KEY);
        Map<String, Object> bean = new HashMap<>();
        bean.put("uid", userData.uid);
        bean.put("time", time);
        bean.put("sign", sign);
        String s = JSONObject.toJSON(bean).toString();
        Log.d(TAG, "获取我的steam信息" + s);
        OkHttpUtils.postString().url(NewUrl.GET_MYSTEAM_INFO).content(s).build().execute(new Callback<MySteamBean>() {
            @Override
            public MySteamBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, MySteamBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(MySteamBean response) {
                //updateCardSetting(response.getData());
            }

        });
    }

    private void getSteamPsnInfo() {
        UserDataBean userData = MyApplication.getUserData();
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(userData.uid + time + NewUrl.KEY);
        Map<String, Object> values = new HashMap<>();
        values.put("uid", userData.uid);
        values.put("sign", sign);
        values.put("time", time);
        String json = new org.json.JSONObject(values).toString();
        Log.d("获取信息", json);
        OkHttpUtils
                .postString()
                .url(NewUrl.MY_STEAMPSN)
                .content(json)
                .build()
                .execute(new Callback<SteamPsn>() {
                    @Override
                    public SteamPsn parseNetworkResponse(Response response) throws IOException {
                        String data = response.body().string();
                        Log.e("xxxx", data);
                        return new Gson().fromJson(data, SteamPsn.class);
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("message", e.getMessage());
                    }

                    @Override
                    public void onResponse(SteamPsn response) {
                        updata(response.getData());
                    }
                });
    }


    /**
     * 刷新Steam信息
     */
    private void reLoadSteamInfo() {
        if (userData == null) {
            userData = MyApplication.getUserData();
        }
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(userData.uid + time + NewUrl.KEY);
        Map<String, Object> bean = new HashMap<>();
        bean.put("uid", userData.uid);
        bean.put("time", time);
        bean.put("sign", sign);
        String s = JSONObject.toJSON(bean).toString();
        Log.d(TAG, "获取我的steam信息" + s);
        OkHttpUtils.postString().url(RELOAD_STEAM).content(s).build().execute(new Callback<ReLoadSteamBean>() {
            @Override
            public ReLoadSteamBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, ReLoadSteamBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(ReLoadSteamBean response) {
                if (response.getCode() == 0) {
                    ToastUtil.showToast(mContext, "刷新steam账号信息失败");
                } else if (response.getCode() == 1) {
                    setResult(1);
                    ToastUtil.showToast(mContext, "刷新steam账号成功");
                } else if (response.getCode() == -1) {
                    ToastUtil.showToast(mContext, "网络异常，请检查网络");
                }
            }

        });
    }

    private void reloadPsnInfo() {
        if (userData == null) {
            userData = MyApplication.getUserData();
        }
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(userData.uid + time + NewUrl.KEY);
        Map<String, Object> bean = new HashMap<>();
        bean.put("uid", userData.uid);
        bean.put("time", time);
        bean.put("sign", sign);
        String s = JSONObject.toJSON(bean).toString();
        Log.d(TAG, "解绑steam请求 " + s);
        OkHttpUtils.postString().content(s).url(NewUrl.RELAOD_PSN).build().execute(new Callback<ReLoadSteamBean>() {
            @Override
            public ReLoadSteamBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, ReLoadSteamBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(ReLoadSteamBean response) {
                if (response.getCode() == 0) {
                    ToastUtil.showToast(mContext, "刷新psn账号信息失败");
                } else if (response.getCode() == 1) {

                    setResult(1);
                    ToastUtil.showToast(mContext, "刷新psn账号成功");
                } else if (response.getCode() == -1) {
                    ToastUtil.showToast(mContext, "网络异常，请检查网络");
                }
            }
        });
    }

    /**
     * 解绑steam账号
     */
    private void unBindSteam() {
        if (userData == null) {
            userData = MyApplication.getUserData();
        }
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(userData.uid + time + NewUrl.KEY);
        Map<String, Object> bean = new HashMap<>();
        bean.put("uid", userData.uid);
        bean.put("time", time);
        bean.put("sign", sign);
        String s = JSONObject.toJSON(bean).toString();
        Log.d(TAG, "解绑steam请求 " + s);
        OkHttpUtils.postString().url(UNBIND_STEAM).content(s).build().execute(new Callback<ReLoadSteamBean>() {
            @Override
            public ReLoadSteamBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, ReLoadSteamBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(ReLoadSteamBean response) {
                if (response.getCode() == 1) {
                    steamIsBind = 0;
                    getSteamPsnInfo();
                    ToastUtil.showToast(mContext, "解绑Steam账号成功");
                } else if (response.getCode() == 0) {
                    ToastUtil.showToast(mContext, "解绑Steam账号失败");
                } else if (response.getCode() == -1) {
                    ToastUtil.showToast(mContext, "网络异常，请检查网络");
                }
            }


        });
    }

    /**
     * 解绑psn
     */
    private void unBindPsn() {
        if (userData == null) {
            userData = MyApplication.getUserData();
        }
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(userData.uid + time + NewUrl.KEY);
        Map<String, Object> bean = new HashMap<>();
        bean.put("uid", userData.uid);
        bean.put("time", time);
        bean.put("sign", sign);
        String s = JSONObject.toJSON(bean).toString();
        Log.d(TAG, "解绑steam请求 " + s);
        OkHttpUtils.postString().content(s).url(NewUrl.UNBIND_PSN).build().execute(new Callback<ReLoadSteamBean>() {
            @Override
            public ReLoadSteamBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, ReLoadSteamBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(ReLoadSteamBean response) {
                if (response.getCode() == 0) {
                    //解绑失败
                    ToastUtil.showToast(mContext, "解绑psn失败");
                } else if (response.getCode() == 1) {
                    //解绑成功
                    psnIsBind = 0;
                    getSteamPsnInfo();
                    ToastUtil.showToast(mContext, "解绑psn成功");
                } else if (response.getCode() == -1) {
                    //网络异常
                    ToastUtil.showToast(mContext, "网络异常");
                }
            }

        });
    }

    private void updata(Data data) {
        Psn psn = data.getPsn();
        Steam steam = data.getSteam();

        psn_nickname = psn.getPsn_nickname();
        if (steam.getIsbang() == 0) {
            mBinding.steamUserName.setText("");
            mBinding.bindUnbindSteam.setText("点击绑定");
        } else {
            steamIsBind = 1;
            mBinding.steamUserName.setText(steam.getSt_nickname());
            mBinding.bindUnbindSteam.setText("解除绑定");

        }
        if (psn.getIsbang() == 0) {
            mBinding.psnUserName.setText("");
            mBinding.bindUnbindPsn.setText("点击绑定");
        } else {
            psnIsBind = 1;

            mBinding.bindUnbindPsn.setText("解除绑定");
            mBinding.psnUserName.setText(psn.getPsn_nickname());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSteamPsnInfo();
    }
}
