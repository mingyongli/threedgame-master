package com.ws3dm.app.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ws3dm.app.MyApplication;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.event.ErrorEvent;
import com.ws3dm.app.mvp.model.RespBean.UserRespBean;
import com.ws3dm.app.mvp.presenter.UserPresenter;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.UserService;
import com.ws3dm.app.util.DownTimer;
import com.ws3dm.app.util.SharedUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.R;
import com.ws3dm.app.Constant;
import com.ws3dm.app.databinding.AcRegisterBinding;
import com.ws3dm.app.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Describution :注册界面
 * 
 * Author : DKjuan
 * 
 * Date : 2017/8/30 16:24
 **/
public class RegisterActivity extends BaseActivity{
    private UserPresenter mUserPresenter;
    private boolean isSeeOne; //密码是否可见
//    private int position; //安全问题标识
//    private boolean isSee; //密码是否可见
//    private boolean isRegiter; //判断是否跳到注册页面
    private AcRegisterBinding mBinding;
    private Handler mHandler;
    private DownTimer mTimer;

    @Override
    protected void init() {
        mBinding=bindView(R.layout.ac_register);
        mBinding.setHandler(this);

        initView();
    }

    private void initView(){
        isSeeOne = false;
        mUserPresenter=UserPresenter.getInstance();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (!mIsRunning) {
                    return;
                }
                switch (msg.what) {
                    case Constant.Notify.LOAD_FAILURE://请求失败
                        if (mBinding.loadView != null)
                            mBinding.loadView.setVisibility(View.GONE);
                        ErrorEvent error=(ErrorEvent) msg.obj;
                        Log.e("requestFailure",/* "登录失败:"+*/error.message);
                        ToastUtil.showToast(mContext,error.message);
                        break;
                    case Constant.Notify.RESULT_USER_REGISTER://注册返回处理
                        UserRespBean mUserRespBean = (UserRespBean) msg.obj;
                        UserDataBean userDataBean =new UserDataBean();
                        userDataBean.avatarstr=mUserRespBean.getData().getAvatarstr();
                        userDataBean.nickname=mUserRespBean.getData().getNickname();
                        userDataBean.uid=mUserRespBean.getData().getUid();
                        userDataBean.username=mUserRespBean.getData().getUsername();
                        userDataBean.mobile=mUserRespBean.getData().getMobile();
                        userDataBean.integral=mUserRespBean.getData().getIntegral();

                        MyApplication.writeUserData(userDataBean);
                        SharedUtil.setSharedPreferencesData("userId", userDataBean.uid);

                        if (mBinding.loadView != null)
                            mBinding.loadView.setVisibility(View.GONE);
                        if(StringUtil.isEmpty(mUserRespBean.getData().getIntegralmsg()))
                            Toast.makeText(mContext, "注册成功,请登录", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(mContext, ""+mUserRespBean.getData().getIntegralmsg(), Toast.LENGTH_SHORT).show();
                        finish();
//                        UserRespBean mUserRespBean=(UserRespBean) msg.obj;
//                        if (mUserRespBean.getCode()==1) {
////                            SharedUtil.setSharedPreferencesData("userId", mUserRespBean.getUid());
////                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
//                            JSONObject obj = new JSONObject();
//                            try {
//                                obj.put("module", "userinfo");
//                                obj.put("uid", SharedUtil.getSharedPreferencesData("userId"));
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                           mUserPresenter.BBSLogin(obj.toString());
//                        }
                        break;
                }
            }
        };

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
        
        mUserPresenter.setHandler(mHandler);
    }
    //点击事件
    public void clickHandler(View view) {
        switch (view.getId()) {
            // 返回
            case R.id.imgReturn:
                finish();
                break;
            case R.id.tv_getsms:
                if(mBinding.etName.getText().toString().length()==0){
                    ToastUtil.showToast(mContext,"手机号不能为空！");
                }else if(!StringUtil.isMobieNumber(mBinding.etName.getText().toString())){
                    ToastUtil.showToast(mContext,"手机号错误！");
                }else{
                    getSMS(mBinding.etName.getText().toString(),1);
                    mTimer.start();
                }
                break;
            case R.id.sign_in_aggreement:
                Intent aggreement=new Intent(this,SingleWebActivity.class);
                aggreement.putExtra("title","注册协议");
                aggreement.putExtra("url",SingleWebActivity.mUrl_AgreeMent);
                startActivity(aggreement);
                break;
            case R.id.imgSeeOne:
                if (isSeeOne) {
                    isSeeOne = false;
                    mBinding.imgSeeOne.setImageResource(R.drawable.no_see);
                    mBinding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mBinding.etPassword.setSelection(mBinding.etPassword.getText().length());
                } else {
                    isSeeOne = true;
                    mBinding.imgSeeOne.setImageResource(R.drawable.see);
                    mBinding.etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mBinding.etPassword.setSelection(mBinding.etPassword.getText().length());
                }
                break;
            case R.id.feedback:
                if(mBinding.etName.getText().toString().length()==0){
                    ToastUtil.showToast(mContext,"手机号不能为空！");
                    return;
                }else if(!StringUtil.isMobieNumber(mBinding.etName.getText().toString())){
                    ToastUtil.showToast(mContext,"手机号错误！");
                    return;
                }
                long feedTime=System.currentTimeMillis();
                int uid;
                if(MyApplication.getUserData().uid==null||"".equals(MyApplication.getUserData().uid)) {
                    uid=0;
                }else{
                    uid=Integer.parseInt(MyApplication.getUserData().uid);
                }
                String feedvalidate=mBinding.etName.getText().toString().trim()+uid+feedTime;
                String feedSign= StringUtil.MD5(feedvalidate);
                JSONObject feedobj = new JSONObject();
                try {
                    feedobj.put("mobile", mBinding.etName.getText().toString().trim());
                    feedobj.put("uid", uid);
                    feedobj.put("time", feedTime);
                    feedobj.put("sign", feedSign);

                    //同步请求
                    Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
                    UserService.Api service = retrofit.create(UserService.Api.class);
                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), feedobj.toString());
                    Call<ResponseBody> call = service.mobileFeed(body);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Log.e("requestSuccess", "%-----------------------" + response.body() + "提交评论成功");
                            try {
                                String jsonString=response.body().string();
                                JSONObject jsonObject = new JSONObject(jsonString);
                                if (jsonObject.optInt("code") == 1) {
                                    ToastUtil.showToast(mContext, "提交反馈成功！");
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_submit:
                if(mBinding.etVerify.getText().toString().trim().length()<=0){
                    ToastUtil.showToast(mContext,"验证码不能为空！");
                    return;
                }
                long time=System.currentTimeMillis();
                String validate=mBinding.etName.getText().toString().trim()+mBinding.etPassword.getText().toString().trim()+mBinding.etVerify.getText().toString().trim().toString()+time;
                String sign= StringUtil.MD5(validate);
                JSONObject obj = new JSONObject();
                try {
                    obj.put("mobile", mBinding.etName.getText().toString().trim());
                    obj.put("passwd", mBinding.etPassword.getText().toString().trim());
                    obj.put("validate", mBinding.etVerify.getText().toString().trim());
                    obj.put("time", time);
                    obj.put("sign", sign);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mUserPresenter.userRegist(obj.toString());
                break;
        }
    }


//    private void register() {//表单验证 
//        if (mBinding.etName.getText().toString().trim().length() != 0 && mBinding.etPassword.getText().toString().trim().length() != 0 && mBinding.etEndPassword.getText().toString().trim().length() != 0
//                && mBinding.etEmail.getText().toString().trim().length() != 0 && mBinding.etQuestion.getText().toString().trim().length() != 0 && mBinding.etVerify.getText().toString().trim().length() != 0) {
//            if (mBinding.etPassword.getText().length() < 8 && mBinding.etEndPassword.getText().length() < 8)
//                ToastUtil.showToast(mContext,"密码不能少于8位");
//            else {
//                if (!mBinding.etPassword.getText().toString().trim().equals(mBinding.etEndPassword.getText().toString().trim()))
//                    ToastUtil.showToast(mContext,"两次输入密码不同");
//                else {
//                    if (!StringUtil.isEmail(mBinding.etEmail.getText().toString().trim()))
//                        ToastUtil.showToast(mContext,"邮箱格式错误");
//                    else {
//                        if (!mBinding.etQuestion.getText().toString().trim().equals(e + f + "")) {
//                            ToastUtil.showToast(mContext, "验证答案错误");
//                        }else {
//                            if (!mBinding.etVerify.getText().toString().trim().equals(a + "" + b + "" + c + "" + d))
//                                ToastUtil.showToast(mContext,"验证码错误");
//                            else {
//                                JSONObject obj = new JSONObject();
//                                try {
//                                    obj.put("module", "userregister");
//                                    obj.put("email", mBinding.etEmail.getText().toString().trim());
//                                    obj.put("username", mBinding.etName.getText().toString().trim());
//                                    obj.put("passwd", mBinding.etPassword.getText().toString().trim());
//                                    obj.put("ckpasswd", mBinding.etEndPassword.getText().toString().trim());
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                                mUserPresenter.userRegist(obj.toString());//注册
//                                mBinding.loadView.setVisibility(View.VISIBLE);
//                            }
//                        }
//                    }
//                }
//            }
//        } else
//            ToastUtil.showToast(mContext,"所有填写选项不能为空");
//    }

//    // 设置验证码
//    private void setVerify() {
//        a = (int) (Math.random() * 9);
//        b = (int) (Math.random() * 9);
//        c = (int) (Math.random() * 9);
//        d = (int) (Math.random() * 9);
//        mBinding.txtVertifyOne.setText(a + "");
//        mBinding.txtVertifyTwo.setText(b + "");
//        mBinding.txtVertifyThree.setText(c + "");
//        mBinding.txtVertifyFour.setText(d + "");
//    }
    
    public void getSMS(String phoneNo,int act){//act ： 1注册 4找回密码 5修改密码
        try {
            int uid=0;
            long time=System.currentTimeMillis();
            String validate=phoneNo+act+uid+time;
            String sign= StringUtil.MD5(validate);
            
            JSONObject obj = new JSONObject();
            obj.put("mobile",phoneNo);
            obj.put("act",1);
            obj.put("uid", uid);
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
        if(mUserPresenter!=null){
            mUserPresenter.setHandler(mHandler);
        }
    }
}