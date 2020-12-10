package com.ws3dm.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.R;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.bean.ReLoadSteamBean;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.databinding.ActivityBindpsnBinding;
import com.ws3dm.app.event.EventMessage;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BindPsnActivity extends BaseActivity {

    private ActivityBindpsnBinding mBind;
    private UserDataBean userData;

    @Override
    protected void init() {
        mBind = bindView(R.layout.activity_bindpsn);
        setSupportActionBar(mBind.toolbar);
        initView();
        initListener();
    }

    private void initView() {
        switchButton();
    }

    private void switchButton() {
        if (mBind.checkButton.isChecked()) {
            mBind.checkButton.setButtonDrawable(R.drawable.ic_check);
        } else {
            mBind.checkButton.setButtonDrawable(R.drawable.ic_uncheck);
        }
    }

    private void initListener() {
        mBind.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBind.checkButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchButton();
            }
        });
        mBind.bindPsnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBind.checkButton.isChecked()) {
                    ToastUtil.showToast(mContext, "请勾选确认按钮");
                }
                if (mBind.edittext.getText().toString().trim().length() == 0) {
                    ToastUtil.showToast(mContext, "请输入PSN ID");
                }
                if (mBind.edittext.getText().toString().trim().length() != 0 && mBind.checkButton.isChecked()) {
                    mBind.edittext.setFocusable(false);
                    hideInput();
                    mBind.bindPsnBtn.setText("绑定中......");
                    bindPsn();
                }
            }
        });
        mBind.BindingTutorial.setOnClickListener(new View.OnClickListener() {
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

    private void bindPsn() {
        userData = MyApplication.getUserData();
        long time = System.currentTimeMillis();
        String psnid = mBind.edittext.getText().toString().trim();
        String sign = StringUtil.newMD5(userData.uid + psnid + time + NewUrl.KEY);
        Map<String, Object> bean = new HashMap<>();
        bean.put("uid", userData.uid);
        bean.put("psnid", psnid);
        bean.put("time", time);
        bean.put("sign", sign);
        String s = JSON.toJSON(bean).toString();
        OkHttpUtils.postString().content(s).url(NewUrl.BIND_PSN).build().execute(new Callback<ReLoadSteamBean>() {
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
                mBind.bindPsnBtn.setText("绑定");
                if (response.getCode() == 0) {
                    ToastUtil.showToast(mContext, "绑定失败");
                } else if (response.getCode() == 1) {
                    ToastUtil.showToast(mContext, "绑定成功");
                    finish();
                } else if (response.getCode() == -1) {
                    ToastUtil.showToast(mContext, "网络错误");
                } else if (response.getCode() == 231) {
                    ToastUtil.showToast(mContext, response.getMsg());
                } else {
                    ToastUtil.showToast(mContext, response.getMsg());
                }

            }

        });
    }
    /**
     * 隐藏键盘
     */
    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }

    }
    }
