package com.ws3dm.app.activity;

import android.util.Log;
import android.view.View;

import com.ws3dm.app.R;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.databinding.AcChangePhoneBinding;
import com.ws3dm.app.mvp.presenter.UserPresenter;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.UserService;
import com.ws3dm.app.util.DownTimer;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Describution : 更换手机号
 * 
 * Author : DKjuan
 * 
 * Date : 2018/11/27 10:21
 **/
public class ChangePhoneActivity extends BaseActivity{
    private UserPresenter mUserPresenter;
    private AcChangePhoneBinding mBinding;
    private DownTimer mTimer;
    private boolean hasInvalid=false;//是否验证过旧手机
    private String firstSMS="";

    @Override
    protected void init() {
        mBinding=bindView(R.layout.ac_change_phone);
        mBinding.setHandler(this);

        initView();
    }

    private void initView(){
        mUserPresenter=UserPresenter.getInstance();

        mTimer = new DownTimer();
        mTimer.setOnCountDownTimerListener(new DownTimer.OnCountDownTimerListener() {
            @Override
            public void onTick(long millisUntilFinished) {
                mBinding.tvGetsms.setText(millisUntilFinished / 1000 + "秒");
            }

            @Override
            public void onFinish() {
                mBinding.tvGetsms.setText("重新获取");
            }
        });
        
        mBinding.tvPhone.setText("+86："+StringUtil.hidePhone(MyApplication.getUserData().mobile));
        mBinding.etPhonenum.setHint(""+StringUtil.hidePhone(MyApplication.getUserData().mobile));
    }
    
    public void clickHandler(View view) {//点击事件
        switch (view.getId()) {
            // 返回
            case R.id.imgReturn:
                finish();
                break;
            case R.id.tv_getsms:
                if(!hasInvalid) {//验证旧手机验证码
                    getSMS(MyApplication.getUserData().mobile,6);
                    mTimer.start();
                }else {
                    if(mBinding.etPhonenum.getText().toString().length()==0){
                        ToastUtil.showToast(mContext,"手机号不能为空！");
                    }else if(!StringUtil.isMobieNumber(mBinding.etPhonenum.getText().toString())){
                        ToastUtil.showToast(mContext,"手机号错误！");
                    }else{
                        getSMS(mBinding.etPhonenum.getText().toString(),3);
                        mTimer.start();
                    }
                }
                break;
            case R.id.tv_submit:
                if(!hasInvalid&&mBinding.etSms.getText().toString().trim().length()<=0){
                    ToastUtil.showToast(mContext,"验证码不能为空！");
                    return;
                }else if(hasInvalid){
                    if(mBinding.etPhonenum.getText().toString().length()==0){
                        ToastUtil.showToast(mContext,"手机号不能为空！");
                        return;
                    }else if(!StringUtil.isMobieNumber(mBinding.etPhonenum.getText().toString())){
                        ToastUtil.showToast(mContext,"手机号错误！");
                        return;
                    }                    
                }
                if(!hasInvalid){//验证旧手机验证码
                    final long time=System.currentTimeMillis();
                    firstSMS=mBinding.etSms.getText().toString().trim();
                    String validate=""+MyApplication.getUserData().uid+MyApplication.getUserData().mobile+mBinding.etSms.getText().toString().trim()+time;
                    String sign= StringUtil.MD5(validate);
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("uid", MyApplication.getUserData().uid);
                        obj.put("oldmobile", MyApplication.getUserData().mobile);
                        obj.put("oldvalidate", mBinding.etSms.getText().toString().trim());
                        obj.put("time", time);
                        obj.put("sign", sign);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //同步请求
                    Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
                    UserService.Api service = retrofit.create(UserService.Api.class);
                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
                    Call<ResponseBody> call = service.invalOldPhone(body);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                JSONObject jsonObject=new JSONObject(response.body().string());
                                if(jsonObject.optInt("code")==1){
                                    ToastUtil.showToast(mContext,"原手机验证通过！");
                                    //初始化下一次界面
                                    mBinding.label.setText("更换手机号后，下次登录需使用新手机号！");
                                    mBinding.etPhonenum.setHint("请输入新的手机号码");
                                    mBinding.etPhonenum.setText("");
                                    mBinding.etSms.setText("");

                                    hasInvalid=true;
                                    mTimer.reset();
                                    mBinding.tvGetsms.setText("获取验证码");
                                }else{
                                    ToastUtil.showToast(mContext,jsonObject.opt("msg")+"");
                                    mTimer.stop();
                                    mBinding.tvGetsms.setText("重新获取");
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
                }else{//验证信手机验证码
                    final long time=System.currentTimeMillis();
                    String validate=""+MyApplication.getUserData().uid+MyApplication.getUserData().mobile+firstSMS+mBinding.etPhonenum.getText().toString().trim()+mBinding.etSms.getText().toString().trim()+time;
                    String sign= StringUtil.MD5(validate);
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("uid", MyApplication.getUserData().uid);
                        obj.put("oldmobile", MyApplication.getUserData().mobile);
                        obj.put("oldvalidate", firstSMS);
                        obj.put("newmobile", mBinding.etPhonenum.getText().toString().trim());
                        obj.put("newvalidate", mBinding.etSms.getText().toString().trim());
                        obj.put("time", time);
                        obj.put("sign", sign);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //同步请求
                    Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
                    UserService.Api service = retrofit.create(UserService.Api.class);
                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
                    Call<ResponseBody> call = service.invalNewPhone(body);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                JSONObject jsonObject=new JSONObject(response.body().string());
                                if(jsonObject.optInt("code")==1){
                                    JSONObject jsonSub=new JSONObject(jsonObject.opt("data").toString());
                                    ToastUtil.showToast(mContext,"手机更换成功！");
                                    UserDataBean userDataBean = MyApplication.getUserData();
                                    userDataBean.mobile=jsonSub.optString("mobile");
                                    MyApplication.writeUserData(userDataBean);
                                    finish();
                                }else{
                                    ToastUtil.showToast(mContext,jsonObject.opt("msg")+"");
                                    mTimer.stop();
                                    mBinding.tvGetsms.setText("重新获取");
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
                break;
        }
    }
    
    public void getSMS(String phoneNo,int act){//act ： 1注册 3绑定手机号或者换绑手机号时新手机号 4找回密码 5修改密码  6换绑手机旧手机短信、7注销帐号
        try {
            long time=System.currentTimeMillis();
            String validate=""+phoneNo+act+MyApplication.getUserData().uid+time;
            String sign= StringUtil.MD5(validate);
            
            JSONObject obj = new JSONObject();
            obj.put("mobile",phoneNo);
            obj.put("act",act);
            obj.put("uid", MyApplication.getUserData().uid);
            obj.put("time", time);
            obj.put("sign",sign);
            //同步请求
            Retrofit retrofit= RetrofitFactory.getNewRetrofit(0);
            UserService.Api service=retrofit.create(UserService.Api.class);
            RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),obj.toString());
            Call<ResponseBody> call=service.getSMS(body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.e("requestSuccess","-----------------------"+response.body());
                    try {
                        JSONObject jsonObject=new JSONObject(response.body().string());
                        if(jsonObject.optInt("code")==1){
                            ToastUtil.showToast(mContext, "短信获取成功！");
                        }else{
                            ToastUtil.showToast(mContext,jsonObject.opt("msg")+"");
                            mTimer.stop();
                            mBinding.tvGetsms.setText("重新获取");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    mTimer.stop();
                    mBinding.tvGetsms.setText("重新获取");
                    Log.e("requestFailure",throwable.getMessage()+"");
                }
            });
        } catch (Exception e) {
        	// TODO: handle exception
        	e.printStackTrace();
        }
    }
}