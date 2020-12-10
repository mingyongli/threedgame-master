package com.ws3dm.app.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import androidx.webkit.ProxyController;
import androidx.webkit.WebViewCompat;
import androidx.webkit.WebViewFeature;

import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.R;
import com.ws3dm.app.bean.MySteamBean;
import com.ws3dm.app.databinding.ActivityBindsteamBinding;
import com.ws3dm.app.util.SharedUtil;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BindSteamActivity extends BaseActivity {

    private String uid;
    private ActivityBindsteamBinding mBind;

    @SuppressLint("JavascriptInterface")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void init() {
//        Properties prop = System.getProperties();
//        prop.setProperty("proxySet", "true");
//        prop.setProperty("proxyHost", "192.168.3.72");
//        prop.setProperty("proxyPort", "8888");
        mBind = bindView(R.layout.activity_bindsteam);
        setSupportActionBar(mBind.toolbar);
        mBind.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBind.setHandler(this);
        uid = getIntent().getStringExtra("uid");
        mBind.bindSteamWeb.getSettings().setJavaScriptEnabled(true);
        mBind.bindSteamWeb.getSettings().setBlockNetworkImage(false);//解决图片不显示
        mBind.bindSteamWeb.getSettings().setDomStorageEnabled(true);
        mBind.bindSteamWeb.getSettings().setSupportZoom(true); // 支持缩放
        mBind.bindSteamWeb.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mBind.bindSteamWeb.getSettings().setUseWideViewPort(true);
        mBind.bindSteamWeb.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mBind.bindSteamWeb.getSettings().setLoadWithOverviewMode(true);
        if (SharedUtil.getSharedPreferencesData("FontSize").equals("2"))//字体大小，0，小，1中，2大
            mBind.bindSteamWeb.getSettings().setTextSize(WebSettings.TextSize.LARGER);
        else if (SharedUtil.getSharedPreferencesData("FontSize").equals("1"))
            mBind.bindSteamWeb.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
        else
            mBind.bindSteamWeb.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            mBind.bindSteamWeb.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        mBind.bindSteamWeb.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mBind.bindSteamWeb.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {

            }

            @Override
            public void onHideCustomView() {

            }
        });
        mBind.bindSteamWeb.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (mBind.bindSteamWeb != null)
                    mBind.bindSteamWeb.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                loadurlLocalMethod(view, url);

                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//						mBinding.viewScroll.fullScroll(ScrollView.FOCUS_UP);//添加滚动到顶部
                    }
                }, 500);
            }
        });
        //NetworkProxy.setProxy(mBind.bindSteamWeb, "papp.3dmgame.com", 9999, "com.ws3dm.app.MyApplication");
        mBind.bindSteamWeb.loadUrl("https://my.3dmgame.com/steam/login?userid=" + uid);
    }

    public void loadurlLocalMethod(final WebView webView, final String url) {
        //屏蔽网页内恶意代码跳转app
        if (url.startsWith("https://steamcommunity.com/profiles")) {
            getSteamInfo();
            finish();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {

                webView.loadUrl(url);
            }
        });
    }

    public void getSteamInfo() {
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(uid + time + NewUrl.KEY);
        Map<String, Object> bean = new HashMap<>();
        bean.put("uid", uid);
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
                if (response.getData().getIsbang() == 1) {
                    ToastUtil.showToast(mContext, "绑定成功");
                    finish();
                } else {
                    ToastUtil.showToast(mContext, "绑定失败");
                }
            }

        });
    }

    @Override
    public void onBackPressed() {
        if (mBind.bindSteamWeb.canGoBack()) {
            mBind.bindSteamWeb.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
