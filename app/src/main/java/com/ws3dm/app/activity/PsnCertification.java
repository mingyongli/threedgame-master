package com.ws3dm.app.activity;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.R;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.databinding.ActivityPsnCertificationBinding;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PsnCertification extends BaseActivity {


    private String psnid;
    private ActivityPsnCertificationBinding mBind;
    private int time;


    @Override
    protected void init() {
        mBind = DataBindingUtil.setContentView(this, R.layout.activity_psn_certification);
        setSupportActionBar(mBind.toolbar);
        Intent intent = getIntent();
        psnid = intent.getStringExtra("psnid");
        //拿到认证码
        getCode();
        mBind.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBind.renzheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBind.renzheng.setText("认证中......");
                cerPsnCode();
            }
        });
        mBind.toastSeconds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCode();
            }
        });

        mBind.CertificationCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //认证
                NewsBean newsBean = new NewsBean();
                newsBean.setWebviewurl("https://m.3dmgame.com/webview/news/202012/3803413.html");
                newsBean.setArcurl("https://www.3dmgame.com/news/202012/3803413.html");
                newsBean.setType("游戏杂谈");
                newsBean.setAid(3803413);
                newsBean.setDetail_title("PSN认证教程");
                Intent intent = new Intent(mContext, NewsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("newsBean", newsBean);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

    }

    private void getCode() {
        UserDataBean userData = MyApplication.getUserData();
        long timeMillis = System.currentTimeMillis();
        String sign = StringUtil.newMD5(userData.uid + psnid + timeMillis + NewUrl.KEY);
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("uid", userData.uid);
        objectMap.put("psnid", psnid);
        objectMap.put("time", timeMillis);
        objectMap.put("sign", sign);
        String s = JSON.toJSON(objectMap).toString();
        OkHttpUtils.postString().content(s).url(NewUrl.GETPSNCODE).build().execute(new Callback<codeBean>() {
            @Override
            public codeBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, codeBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(codeBean response) {
                if (response.getCode() == 1) {
                    updataCode(response.getData().getAuthcode());
                } else {
                    ToastUtil.showToast(mContext, response.getMsg() + "");
                }
            }
        });
    }

    private void updataCode(String authcode) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < authcode.length(); i++) {
            list.add(String.valueOf(authcode.charAt(i)));
        }
        mBind.v1.setText(list.get(0));
        mBind.v2.setText(list.get(1));
        mBind.v3.setText(list.get(2));
        mBind.v4.setText(list.get(3));
        mBind.v5.setText(list.get(4));
        mBind.v6.setText(list.get(5));
        countDown();
    }

    private void cerPsnCode() {
        String uid = MyApplication.getUserData().uid;
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(uid + psnid + time + NewUrl.KEY);
        Map<String, Object> bean = new HashMap<>();
        bean.put("uid", uid);
        bean.put("psnid", psnid);
        bean.put("time", time);
        bean.put("sign", sign);
        String s = JSON.toJSON(bean).toString();
        OkHttpUtils.postString().content(s).url(NewUrl.AUTHPSNCODE).build().execute(new Callback<certCodeBean>() {
            @Override
            public certCodeBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, certCodeBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(certCodeBean response) {
                mBind.renzheng.setText("认证");
                if (response.getCode() == 1) {
                    ToastUtil.showToast(mContext, "认证成功");
                    finish();
                } else {

                    ToastUtil.showToast(mContext, response.getMsg());
                }
            }

        });
    }

    /**
     * 倒计时显示
     */
    private void countDown() {

        CountDownTimer timer = new CountDownTimer(6 * 10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time = (int) (millisUntilFinished / 1000);
                mBind.toastSeconds.setVisibility(View.GONE);
                mBind.time.setVisibility(View.VISIBLE);
                mBind.time.setText(time + "");
            }

            @Override
            public void onFinish() {
                mBind.toastSeconds.setVisibility(View.VISIBLE);
                mBind.time.setVisibility(View.GONE);
                mBind.toastSeconds.setText("再次获取");
            }
        }.start();
        timer.start();
    }

}

class codeBean {

    /**
     * code : 1
     * data : {"authcode":"GKPXDM"}
     * msg : 成功
     */

    private int code;
    /**
     * authcode : GKPXDM
     */

    private DataBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        private String authcode;

        public String getAuthcode() {
            return authcode;
        }

        public void setAuthcode(String authcode) {
            this.authcode = authcode;
        }
    }
}

class certCodeBean {

    /**
     * code : 235
     * msg : 认证失败
     */

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

