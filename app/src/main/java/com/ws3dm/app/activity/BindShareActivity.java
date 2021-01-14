package com.ws3dm.app.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.R;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.databinding.BindShareLayoutBinding;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.UserService;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

public class BindShareActivity extends BaseActivity{
    private BindShareLayoutBinding mBinding;
    private int type=0;//1qq  2微博  3微信
    private String openID="",appOpenid="";//第三方账号标识
    @Override
    protected void init() {
        mBinding = bindView(R.layout.bind_share_layout);
        mBinding.setHandler(this);
        getBindInfo();
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.bind_qq:
                type = 1;
                if(mBinding.bindQq.getText().toString().contains("解除绑定")){
                    UMShareAPI.get(this).deleteOauth(this, SHARE_MEDIA.QQ,umDelListener);
                    unBindopen();
                }else{
                    UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.QQ, umAuthListener);
                }
                break;
            case R.id.bind_wb:
                type=2;
                if(mBinding.bindWb.getText().toString().contains("解除")){
                    UMShareAPI.get(this).deleteOauth(this,SHARE_MEDIA.SINA,umDelListener);
                    unBindopen();
                }else{
                    UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.SINA, umAuthListener);
                }
                break;
            case R.id.bind_wx:
                type=3;
                if(mBinding.bindWx.getText().toString().contains("解除")){
                    UMShareAPI.get(this).deleteOauth(this,SHARE_MEDIA.WEIXIN,umDelListener);
                    unBindopen();
                }else{
                    UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.WEIXIN, umAuthListener);
                }
                break;
        }
    }

    private UMAuthListener umDelListener = new UMAuthListener(){
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            Log.i("umeng_login_onStart","删除授权开始");
        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            Log.i("umeng_del_onComplete","删除授权结束");
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            Log.i("umeng_del_onError","删除授权失败");

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            Log.i("umeng_del_userinfo","删除授权取消");
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void getBindInfo(){
        UserDataBean userData = MyApplication.getUserData();
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(userData.uid+ time + NewUrl.KEY);
        Map<String, Object> values = new HashMap<>();
        values.put("uid",userData.uid);
        values.put("sign",sign);
        values.put("time",time);
        String json = new JSONObject(values).toString();
        OkHttpUtils
                .postString()
                .url(NewUrl.USER_BIND_STATUS)
                .content(json)
                .build()
                .execute(new Callback<BindStatus>() {

                    @Override
                    public BindStatus parseNetworkResponse(Response response) throws IOException {
                        String body = response.body().string();
                        return new Gson().fromJson(body, BindStatus.class);
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("message",e.getMessage());
                    }

                    @Override
                    public void onResponse(BindStatus response) {
                        if(response.getCode() == 1){
                            initView(response.getData().getInfo());
                        }
                    }

                });
    }



    private void initView(BindStatus.Data.Info info) {
        mBinding.qqImg.setBackgroundResource(info.isqq==0?R.drawable.qq_fridend_no:R.drawable.qq_fridend);
        mBinding.wxImg.setBackgroundResource(info.iswechat==0?R.drawable.wx_friend_no:R.drawable.wx_friend);
        mBinding.wbImg.setBackgroundResource(info.issina==0?R.drawable.xl_fridend_no:R.drawable.xl_fridend);

        mBinding.bindQq.setText(info.isqq==0?"点击绑定":"解除绑定");
        mBinding.bindWx.setText(info.iswechat==0?"点击绑定":"解除绑定");
        mBinding.bindWb.setText(info.issina==0?"点击绑定":"解除绑定");
    }

    private UMAuthListener umAuthListener = new UMAuthListener(){
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            Log.i("umeng_login_onStart","获取开始");
        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            Log.i("umeng_login_onComplete","获取开始");
            if(type==3){//1qq  2微博  3微信
                openID=map.get("unionid");
                appOpenid=map.get("openid");
            }else if(type==2){
                openID=map.get("uid");
            }else{
                openID=map.get("unionid");
                appOpenid=map.get("openid");
            }
            bindopen();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            Log.i("umeng_login_onError","获取失败");
//			ToastUtil.showToast(mContext,throwable.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            Log.i("umeng_login_userinfo","获取取消");

        }
    };

    private void bindopen(){
        long time=System.currentTimeMillis();
        String validate=""+MyApplication.getUserData().uid+openID+type+appOpenid+time;
        String sign= StringUtil.MD5(validate);
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid",MyApplication.getUserData().uid);
            obj.put("openid", openID);
            obj.put("type", type);
            obj.put("appopenid", appOpenid);
            obj.put("time", time);
            obj.put("sign", sign);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //同步请求
        Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
        UserService.Api service = retrofit.create(UserService.Api.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
        Call<ResponseBody> call = service.bindopen(body);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    String jsonString=response.body().string();
                    Log.e("requestSuccess", "-----------------------" + jsonString);
                    JSONObject jsonObject=new JSONObject(jsonString);
                    if(jsonObject.optInt("code")==1){
                        ToastUtil.showToast(mContext,"绑定成功！");
                        switch (type) {
                            case 1:
                                mBinding.bindQq.setText("解除绑定");//"请先绑定账号":"解除绑定"
                                mBinding.qqImg.setBackgroundResource(R.drawable.qq_fridend);
                                break;
                            case 2:
                                mBinding.bindWb.setText("解除绑定");//"请先绑定账号":"解除绑定"
                                mBinding.wbImg.setBackgroundResource(R.drawable.xl_fridend);
                                break;
                            case 3:
                                mBinding.bindWx.setText("解除绑定");//"请先绑定账号":"解除绑定"
                                mBinding.wxImg.setBackgroundResource(R.drawable.wx_friend);
                                break;
                        }
                    }else{
                        ToastUtil.showToast(mContext,jsonObject.opt("msg")+"");
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
    private void unBindopen(){
        long time=System.currentTimeMillis();
        String validate=""+MyApplication.getUserData().uid+type+time;
        String sign= StringUtil.MD5(validate);
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid",MyApplication.getUserData().uid);
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
        Call<ResponseBody> call = service.unbindopen(body);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    String jsonString=response.body().string();
                    Log.e("requestSuccess", "-----------------------" + jsonString);
                    JSONObject jsonObject=new JSONObject(jsonString);
                    if(jsonObject.optInt("code")==1){
                        ToastUtil.showToast(mContext,"解绑成功！");
                        switch (type) {
                            case 1:
                                mBinding.bindQq.setText("点击绑定");//"请先绑定账号":"解除绑定"
                                mBinding.qqImg.setBackgroundResource(R.drawable.qq_fridend_no);
                                break;
                            case 2:
                                mBinding.bindWb.setText("点击绑定");//"请先绑定账号":"解除绑定"
                                mBinding.wbImg.setBackgroundResource(R.drawable.xl_fridend_no);
                                break;
                            case 3:
                                mBinding.bindWx.setText("点击绑定");//"请先绑定账号":"解除绑定"
                                mBinding.wxImg.setBackgroundResource(R.drawable.wx_friend_no);
                                break;
                        }
                    }else{
                        ToastUtil.showToast(mContext,jsonObject.opt("msg")+"");
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


    public class BindStatus{
        private int code;
        private Data data;
        private String msg;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public class Data {

            private Info info;
            public void setInfo(Info info) {
                this.info = info;
            }
            public Info getInfo() {
                return info;
            }

            /**
             * Copyright 2020 bejson.com
             */
            public class Info {

                private int isqq;
                private int issina;
                private int iswechat;
                public void setIsqq(int isqq) {
                    this.isqq = isqq;
                }
                public int getIsqq() {
                    return isqq;
                }

                public void setIssina(int issina) {
                    this.issina = issina;
                }
                public int getIssina() {
                    return issina;
                }

                public void setIswechat(int iswechat) {
                    this.iswechat = iswechat;
                }
                public int getIswechat() {
                    return iswechat;
                }

            }

        }
    }
}
