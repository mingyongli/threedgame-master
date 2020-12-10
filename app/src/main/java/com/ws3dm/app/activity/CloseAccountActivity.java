package com.ws3dm.app.activity;

import android.util.Log;
import android.view.View;

import com.ws3dm.app.R;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.databinding.AcCloseAccountBinding;
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
 * Describution : 关闭账号
 * 
 * Author : DKjuan
 * 
 * Date : 2018/11/27 10:21
 **/
public class CloseAccountActivity extends BaseActivity{
    private UserPresenter mUserPresenter;
    private AcCloseAccountBinding mBinding;
    private DownTimer mTimer;

    @Override
    protected void init() {
        mBinding=bindView(R.layout.ac_close_account);
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
        
        mBinding.tvPhone.setText("当前手机号："+StringUtil.hidePhone(MyApplication.getUserData().mobile));
    }
    
    public void clickHandler(View view) {//点击事件
        switch (view.getId()) {
            // 返回
            case R.id.imgReturn:
                finish();
                break;
            case R.id.tv_getsms:
                getSMS(MyApplication.getUserData().mobile, 7);
                mTimer.start();
                break;
            case R.id.tv_submit:
                if(mBinding.etPhonePass.getText().toString().trim().length()<=0){
                    ToastUtil.showToast(mContext,"验证码不能为空！");
                    return;
                }
                long time=System.currentTimeMillis();
                String validate=""+MyApplication.getUserData().uid+MyApplication.getUserData().mobile+mBinding.etPhonePass.getText().toString().trim()+time;
                String sign= StringUtil.MD5(validate);
                JSONObject obj = new JSONObject();
                try {
                    obj.put("uid", MyApplication.getUserData().uid);
                    obj.put("mobile", MyApplication.getUserData().mobile);
                    obj.put("validate", mBinding.etPhonePass.getText().toString().trim());//验证码
                    obj.put("time", time);
                    obj.put("sign", sign);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                mUserPresenter.userRegist(obj.toString());
                //同步请求
                Retrofit retrofit= RetrofitFactory.getNewRetrofit(0);
                UserService.Api service=retrofit.create(UserService.Api.class);
                RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),obj.toString());
                Call<ResponseBody> call=service.closeAccount(body);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.e("requestSuccess","-----------------------"+response.body());
                        try {
                            JSONObject jsonObject=new JSONObject(response.body().string());
                            if(jsonObject.optInt("code")==1){
                                ToastUtil.showToast(mContext, "用户注销成功！");
                                UserDataBean userDataBean =new UserDataBean();
                                userDataBean.loginStatue=false;
                                userDataBean.isThirdPartLogin=false;

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
                        mTimer.stop();
                        mBinding.tvGetsms.setText("重新获取");
                        Log.e("requestFailure",throwable.getMessage()+"");
                    }
                });
                break;
        }
    }
    
    public void getSMS(String phoneNo,int act){//act ： 1注册 3绑定手机号或者换绑手机号时新手机号 4找回密码 5修改密码  6换绑手机旧手机短信、7注销帐号
        try {
            long time=System.currentTimeMillis();
            String validate=phoneNo+act+MyApplication.getUserData().uid+time;
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

    @Override
    public void onResume() {
        super.onResume();
    }
}