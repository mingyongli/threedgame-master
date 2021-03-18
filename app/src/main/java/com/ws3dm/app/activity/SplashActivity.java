package com.ws3dm.app.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.R;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.bean.SlidesBean;
import com.ws3dm.app.bean.SoftGameBean;
import com.ws3dm.app.databinding.AcSplashBinding;
import com.ws3dm.app.fragment.WelcomeFragmentGuide;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.UserService;
import com.ws3dm.app.util.AppUtil;
import com.ws3dm.app.util.DownTimer;
import com.ws3dm.app.util.SharedUtil;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.glide.GlideUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;

/**
 * Describution :启动页
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2017/8/30 16:25
 **/
public class SplashActivity extends BaseActivity {
    private AcSplashBinding mBinding;
    private SlidesBean mSlidesBean;
    private PagerAdapter mPgAdapter;
    // 引导页数据
    private int[] guideRes = {R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3, R.drawable.guide_4};
    private List<WelcomeFragmentGuide> mListFragment = new ArrayList<>();
    private static final int TIME_OUT = 4000;
    private static final int RESULTS_CODE = 11;
    private static final int RESULTS_CODE_FALSE = 12;
    private static final int REQUEST_CODE = 1;
    String inner_title, inner_url, inner_img;
    //	Uri uri;
    boolean jumpWeb = false, countOver = false;
    /**
     * 处理请求欢迎页返回的数据
     */
    Handler welcomePager = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 1:
                    //成功有数据
                    if (SplashActivity.this != null) {
                        try {
                            GlideUtil.loadImage(SplashActivity.this, mSlidesBean.getLitpic(), mBinding.imgCover, R.drawable.bg_glide_trans, R.drawable.bg_glide_trans);
                        } catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                        }
	                    /*Glide.with(SplashActivity.this).load(mSlidesBean.getLitpic())
                                .listener(new RequestListener<String, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, String model,
                                                               Target<GlideDrawable> target,
                                                               boolean isFirstResource) {
//                                        // 可替换成进度条
//                                        Toast.makeText(context, "图片加载失败", Toast.LENGTH_SHORT).show();
                                        goToOut();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource, String model,
                                                                   Target<GlideDrawable> target,
                                                                   boolean isFromMemoryCache,
                                                                   boolean isFirstResource) {
                                        // 图片加载完成，取消进度条
//                                        Toast.makeText(context, "图片加载成功", Toast.LENGTH_SHORT).show();
                                        showCounter();
                                        return false;
                                    }
                                }).error(R.drawable.bg_glide_trans)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(mBinding.imgCover);*/
                    }
                    break;
                case 5:
                    //2秒后执行的动作
                    goToOut();
                    break;
            }
        }
    };

    @Override
    protected void init() {
        //修改状态栏
//        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉Activity上面的状态栏

        if (!isTaskRoot()) {
            finish();
            return;
        }
        mBinding = bindView(R.layout.ac_splash);
        loadAd();
        mBinding.setHandler(this);
//        getWebData();//防止恶意跳转，同MainActivity
        initView();
    }

    private void loadAd() {
        long time = System.currentTimeMillis();
        String type = "1";//1 android 2 ios
        String sign = StringUtil.newMD5(type + time + NewUrl.KEY);

        Map<String, Object> values = new HashMap<>();
        values.put("sign", sign);
        values.put("time", time);
        values.put("type", type);
        String json = new JSONObject(values).toString();
        OkHttpUtils
                .postString()
                .url(NewUrl.SP_ADS)
                .content(json)
                .build()
                .execute(new com.zhy.http.okhttp.callback.Callback<Ads>() {

                    @Override
                    public Ads parseNetworkResponse(com.squareup.okhttp.Response response) throws IOException {
                        String string = response.body().string();
                        return new Gson().fromJson(string, Ads.class);
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("message", e.getMessage());
                    }

                    @SuppressLint("SetJavaScriptEnabled")
                    @Override
                    public void onResponse(Ads response) {
                        if (response.getCode() == 1) {
                            Ads.DataDTO data = response.getData();
                            if (data.getIsopen() == 1) {
                                mBinding.webview.setVisibility(View.VISIBLE);
                                mBinding.skipView.setVisibility(View.VISIBLE);
                                mBinding.imgCover.setVisibility(View.VISIBLE);
                                mBinding.webview.getSettings().setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);
                                mBinding.webview.getSettings().setJavaScriptEnabled(true);
                                mBinding.webview.getSettings().setLoadsImagesAutomatically(true);
                                mBinding.webview.setWebChromeClient(new WebChromeClient());
                                mBinding.webview.setWebViewClient(new WebViewClient() {
                                    @Override
                                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                        //该方法在Build.VERSION_CODES.LOLLIPOP以前有效，从Build.VERSION_CODES.LOLLIPOP起，建议使用shouldOverrideUrlLoading(WebView, WebResourceRequest)} instead
                                        //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
                                        //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242
                                        if (!url.equals(data.getWebviewurl())) {
                                            goToOut();
                                            Uri uri = Uri.parse(url);
                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                            startActivity(intent);
                                            return true;
                                        }
                                        return true;
                                    }
                                });
                                mBinding.webview.loadUrl(data.getWebviewurl());
                            } else {
                                mBinding.webview.setVisibility(View.GONE);
                                mBinding.skipView.setVisibility(View.GONE);
                                mBinding.imgCover.setVisibility(View.GONE);
                            }
                        } else {
                            mBinding.webview.setVisibility(View.GONE);
                            mBinding.skipView.setVisibility(View.GONE);
                            mBinding.imgCover.setVisibility(View.GONE);
                        }
                    }

                });
    }


    static class Ads {

        /**
         * code : 1
         * data : {"isopen":0,"webviewurl":"https://m.3dmgame.com/fakerment.html","countdown":0}
         * msg : 成功
         */

        private int code;
        /**
         * isopen : 0
         * webviewurl : https://m.3dmgame.com/fakerment.html
         * countdown : 0
         */

        private DataDTO data;
        private String msg;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public DataDTO getData() {
            return data;
        }

        public void setData(DataDTO data) {
            this.data = data;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public static class DataDTO {
            private int isopen;
            private String webviewurl;
            private int countdown;

            public int getIsopen() {
                return isopen;
            }

            public void setIsopen(int isopen) {
                this.isopen = isopen;
            }

            public String getWebviewurl() {
                return webviewurl;
            }

            public void setWebviewurl(String webviewurl) {
                this.webviewurl = webviewurl;
            }

            public int getCountdown() {
                return countdown;
            }

            public void setCountdown(int countdown) {
                this.countdown = countdown;
            }
        }
    }


    private void getWebData() {
        Intent intent = getIntent();
//		uri = intent.getData();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndRequestPermission();
        }
        if (MyApplication.getmVersion().equals("" + AppUtil.getVersionCode(MyApplication.getInstance()))) {
            //obtainData();
            showCounter();
        } else {
            goToOut();
        }
        
/*//        loadSplashAd();//广告
        // 如果targetSDKVersion >= 23，就要申请好权限。如果您的App没有适配到Android6.0（即targetSDKVersion < 23），那么只需要在这里直接调用fetchSplashAD接口。
        if (Build.VERSION.SDK_INT >= 23) {
            checkAndRequestPermission();
//        } else {
//            // 如果是Android6.0以下的机器，默认在安装时获得了所有权限，可以直接调用SDK
//            loadSplashAD();
        }
        
//        //3秒跳转
//        new Thread() {
//            public void run() {
//                try {
//                    Thread.sleep(TIME_OUT);
//                    Message msg = welcomePager.obtainMessage();
//                    msg.what = 5;
//                    welcomePager.sendMessage(msg);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
        
//        //第二种
//        showCounter();*/
    }

    private void showCounter() {
        mBinding.skipView.setVisibility(View.VISIBLE);

        DownTimer mTimer = new DownTimer();
        mTimer.setMillisInFuture(4000);
        mTimer.setCountDownInterval(950);
        mTimer.setOnCountDownTimerListener(new DownTimer.OnCountDownTimerListener() {
            @Override
            public void onTick(long millisUntilFinished) {
                mBinding.skipView.setText("跳过 " + millisUntilFinished / 950);
                Log.i("showCounter", millisUntilFinished + "  " + millisUntilFinished / 950 + "");
            }

            @Override
            public void onFinish() {
                mBinding.skipView.setText("跳过 " + 0);
                Message msg = welcomePager.obtainMessage();
                msg.what = 5;
                welcomePager.sendMessage(msg);
                countOver = true;
                Log.i("showCounter", "onFinish");
            }
        });
        mTimer.start();
    }

    private void goToOut() {
//		if (AppUtil.isTopActivity(mContext, "com.ws3dm.app")) {//app不在前台不跳转
        if (AppUtil.isForeground(SplashActivity.this)) {//app不在前台不跳转

            if (!"yes".equals(SharedUtil.getSharedPreferencesData("hasReadInfo"))) {
                startActivityForResult(new Intent(this, InfoActivity.class), REQUEST_CODE);
            } else {
                if (MyApplication.getmVersion().equals("" + AppUtil.getVersionCode(MyApplication.getInstance()))) {
                    //跳转至首页
                    Intent intent = new Intent(this, MainActivity.class);
//				if (uri != null) {
//					intent.setData(uri);
//					intent.putExtra("isWeb", "1");//表示从网页上传来
//				}
                    startActivity(intent);
                    finish();
                } else {
                    //实例化引导页视图
                    initialise();
                    //向sharedPreferences写用户使用的版本标记
                    MyApplication.setmVersion(AppUtil.getVersionCode(MyApplication.getInstance()) + "");
                    SharedUtil.setSharedPreferencesData("noUp", "0");
                    SharedUtil.setSharedPreferencesData("PushAgent", "1");
                }
            }
        }
    }

    public void initialise() {
        mBinding.imgShow.setVisibility(View.GONE);
        mBinding.vpWelcome.setVisibility(View.VISIBLE);
        int forSize = guideRes.length;
        for (int i = 0; i < forSize; i++) {
            if (i == forSize - 1) {
                mListFragment.add(WelcomeFragmentGuide.newInstance(guideRes[i], true));
            } else {
                mListFragment.add(WelcomeFragmentGuide.newInstance(guideRes[i], false));
            }
        }
        mPgAdapter = new SplashViewPagerAdapter(getSupportFragmentManager(), mListFragment);
        mBinding.vpWelcome.setAdapter(mPgAdapter);
        mBinding.vpWelcome.setOffscreenPageLimit(mListFragment.size());
        mBinding.vpWelcome.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
        });
    }

    private class SplashViewPagerAdapter extends FragmentPagerAdapter {
        private List<WelcomeFragmentGuide> fragmentList = new ArrayList<WelcomeFragmentGuide>();

        public SplashViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public SplashViewPagerAdapter(FragmentManager fragmentManager, List<WelcomeFragmentGuide> arrayList) {
            super(fragmentManager);
            this.fragmentList = arrayList;
        }

        @Override
        public WelcomeFragmentGuide getItem(int arg0) {
            return fragmentList.get(arg0);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    public void obtainData() {
        //获取数据
        long time = System.currentTimeMillis();
        String validate = "" + 1 + time;
        String sign = StringUtil.MD5(validate);
        JSONObject obj = new JSONObject();
        try {
            obj.put("type", 1);//int1安卓2ios
            obj.put("time", time);
            obj.put("sign", sign);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //同步请求
        Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
        UserService.Api service = retrofit.create(UserService.Api.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
        Call<ResponseBody> call = service.getAppad(body);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("requestSuccess", "-----------------------" + response.body());
                try {
                    String jsonString = response.body().string();
                    Log.e("requestSuccess", "-----------------------" + jsonString);
                    JSONObject jsonObject = new JSONObject(jsonString);
                    if (jsonObject.optInt("code") == 1 && jsonObject.optString("data").length() > 10) {
                        mSlidesBean = JSON.parseObject(jsonObject.optString("data"), SlidesBean.class);
                        Message msg = welcomePager.obtainMessage();
                        msg.what = 1;
                        welcomePager.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    goToOut();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("requestFailure", throwable.getMessage() + "");
                goToOut();
            }
        });
    }

    public void clickHandler(View view) {
        switch (view.getId()) {
            case R.id.skip_view:
                goToOut();
                break;
            case R.id.imgCover:
                Intent intent = null;
                if (mSlidesBean != null)
                    switch (mSlidesBean.getShowtype()) {
                        case "0":
                            goToOut();
                            AppUtil.OpenUrl(mContext, mSlidesBean.getWebviewurl());
                            jumpWeb = true;

                            break;
                        case "1":
                        case "2":
                        case "5":
                        case "6":
                        case "7":
                        case "8":
                        case "10":
                            NewsBean news = new NewsBean();
                            news.setTitle(mSlidesBean.getTitle());
                            news.setArcurl(mSlidesBean.getArcurl());
                            news.setWebviewurl(mSlidesBean.getWebviewurl());
                            news.setType(mSlidesBean.getShowtype());
                            intent = new Intent(mContext, NewsActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("newsBean", news);
                            intent.putExtras(bundle);
                            mContext.startActivity(intent);
                            break;
                        case "4"://手游
                            SoftGameBean soft = new SoftGameBean();
                            soft.setTitle(mSlidesBean.getTitle());
                            soft.setArcurl(mSlidesBean.getArcurl());
                            soft.setType(mSlidesBean.getShowtype());
                            soft.setAid(mSlidesBean.getAid());
                            intent = new Intent(mContext, MGDetailActivity.class);
                            Bundle bundlesoft = new Bundle();
                            bundlesoft.putSerializable("mSoftGameBean", soft);
                            intent.putExtras(bundlesoft);
                            mContext.startActivity(intent);
                            break;
                    }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkAndRequestPermission() {
        List<String> lackedPermission = new ArrayList<String>();
        if (!(checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        // 权限都已经有了，那么直接调用SDK
        if (lackedPermission.size() == 0) {
//            loadSplashAD();
        } else {
            // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            requestPermissions(requestPermissions, 1024);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (countOver) {
            goToOut();
            countOver = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULTS_CODE) {
                if (MyApplication.getmVersion().equals("" + AppUtil.getVersionCode(MyApplication.getInstance()))) {
                    //跳转至首页
                    Intent intent = new Intent(this, MainActivity.class);
//				if (uri != null) {
//					intent.setData(uri);
//					intent.putExtra("isWeb", "1");//表示从网页上传来
//				}
                    startActivity(intent);
                    finish();
                } else {
                    //实例化引导页视图
                    initialise();
                    //向sharedPreferences写用户使用的版本标记
                    MyApplication.setmVersion(AppUtil.getVersionCode(MyApplication.getInstance()) + "");
                    SharedUtil.setSharedPreferencesData("noUp", "0");
                    SharedUtil.setSharedPreferencesData("PushAgent", "1");
                }
            } else if (resultCode == RESULTS_CODE_FALSE) {
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}