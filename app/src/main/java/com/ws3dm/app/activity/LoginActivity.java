package com.ws3dm.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.mvp.presenter.UserPresenter;
import com.ws3dm.app.R;
import com.ws3dm.app.Constant;
import com.ws3dm.app.databinding.AcLoginBinding;
import com.ws3dm.app.event.ErrorEvent;
import com.ws3dm.app.mvp.model.RespBean.UserRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.UserService;
import com.ws3dm.app.util.AppUtil;
import com.ws3dm.app.util.SharedUtil;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Describution :登陆界面
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2017/8/17 11:48
 **/
public class LoginActivity extends BaseActivity {
    private AcLoginBinding mBinding;
    private UserPresenter mUserPresenter;
    private UserRespBean mUserRespBean;
    private String openID, appOpenid = "";//第三方账号标识
    public String avatarstr;//第三方头像
    public String nickname;//第三方昵称
    private int type = 0;//1qq  2微博  3微信
    private Handler mHandler;
    private boolean isSee; //密码是否可见

    @Override
    protected void init() {
        mBinding = bindView(R.layout.ac_login);
        mBinding.setHandler(this);

        mUserPresenter = UserPresenter.getInstance();
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
                        ErrorEvent error = (ErrorEvent) msg.obj;
                        Toast.makeText(mContext, /*"登录失败:"+*/error.message, Toast.LENGTH_SHORT).show();
                        Log.e("requestFailure", "" + error.message);
                        break;
                    case Constant.Notify.RESULT_USER_LOGIN://登陆返回处理
                        mUserRespBean = (UserRespBean) msg.obj;
                        UserDataBean userDataBean = new UserDataBean();
                        userDataBean.loginStatue = true;
                        userDataBean.isThirdPartLogin = false;
                        userDataBean.avatarstr = mUserRespBean.getData().getAvatarstr();
                        userDataBean.nickname = mUserRespBean.getData().getNickname();
                        userDataBean.uid = mUserRespBean.getData().getUid();
                        userDataBean.username = mUserRespBean.getData().getUsername();
                        userDataBean.mobile = mUserRespBean.getData().getMobile();
                        userDataBean.integral = mUserRespBean.getData().getIntegral();

                        MyApplication.writeUserData(userDataBean);
                        SharedUtil.setSharedPreferencesData("userId", "" + userDataBean.uid);

                        if (mBinding.loadView != null)
                            mBinding.loadView.setVisibility(View.GONE);
                        if (StringUtil.isEmpty(mUserRespBean.getData().getIntegralmsg()))
                            Toast.makeText(mContext, "登录成功", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(mContext, "" + mUserRespBean.getData().getIntegralmsg(), Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();

                        break;
                }
            }
        };
        mUserPresenter.setHandler(mHandler);

        initView();
    }

    public void initView() {
        //判断qq微信是否安装
        if (!AppUtil.checkApkExist(mContext, "com.tencent.mm")) {
            mBinding.weixinLogin.setVisibility(View.GONE);
        }
        if (!AppUtil.checkApkExist(mContext, "com.tencent.mobileqq")) {
            mBinding.qqLogin.setVisibility(View.GONE);
        }
    }

    public void clickHandler(View view) {
        switch (view.getId()) {
            case R.id.imgReturn:
                onBackPressed();
                break;
            case R.id.txtLogin:// 登录
                if (mBinding.etName.getText().toString().trim().length() != 0 && mBinding.etPassword.getText().toString().trim().length() != 0) {
                    long time = System.currentTimeMillis();
                    String validate = mBinding.etName.getText().toString().trim() + mBinding.etPassword.getText().toString().trim() + time;
                    String sign = StringUtil.MD5(validate);
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("username", mBinding.etName.getText().toString().trim());
                        obj.put("passwd", mBinding.etPassword.getText().toString().trim());
                        obj.put("time", time);
                        obj.put("sign", sign);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mUserPresenter.userLogin(obj.toString());
                    mBinding.loadView.setVisibility(View.VISIBLE);
//						((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
                    AppUtil.closeKeyboard(this);
                } else {
                    ToastUtil.showToast(this, "所有填写选项不能为空");
                }
                break;
            case R.id.txtRegister:// 注册
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.qq_login:// qq登录
                type = 1;
                UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.QQ, umAuthListener);
//				mShareAPI = UMShareAPI.get(this);
//				SHARE_MEDIA qq = SHARE_MEDIA.QQ; //把后面改成qq,用什么改成什么
//				mShareAPI.doOauthVerify(this, qq, umAuthListener);//友盟API进行核实，调用umAuthListener友盟授权监听
                break;
            case R.id.weixin_login:// 微信登录
                type = 3;
                UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.WEIXIN, umAuthListener);
//				mShareAPI = UMShareAPI.get(this);
//				SHARE_MEDIA weixin = SHARE_MEDIA.WEIXIN; //把后面改成qq,用什么改成什么
//				mShareAPI.doOauthVerify(this, weixin, umAuthListener);//友盟API进行核实，调用umAuthListener友盟授权监听
                break;
            case R.id.weibo_login://新浪微薄  登录
                type = 2;
                UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.SINA, umAuthListener);
//				mShareAPI = UMShareAPI.get(this);
//				SHARE_MEDIA xinlang = SHARE_MEDIA.SINA; //把后面改成qq,用什么改成什么
//				mShareAPI.doOauthVerify(this, xinlang, umAuthListener);//友盟API进行核实，调用umAuthListener友盟授权监听
                break;
//			case R.id.txtAnswer:// 安全问题
//				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//				builder.setTitle("安全问题");
//				//    指定下拉列表的显示数据
//				final String[] safeArray = {"母亲的名字", "爷爷的名字", "父亲出生的城市", "您其中一位老师的名字", "您个人计算机的型号", "您最喜欢的餐馆名称", "驾驶执照最后四位数字"};
//				//    设置一个下拉的列表选择项
//				builder.setItems(safeArray, new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						position = which + 1;
//						mBinding.txtSafe.setText(safeArray[which]);
//						mBinding.txtSafe.setVisibility(View.VISIBLE);
//					}
//				});
//				builder.show();
//				break;
            case R.id.imgSee:// 密码是否可见
                if (isSee) {
                    isSee = false;
                    mBinding.imgSee.setImageResource(R.drawable.no_see);
                    mBinding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mBinding.etPassword.setSelection(mBinding.etPassword.getText().length());
                } else {
                    isSee = true;
                    mBinding.imgSee.setImageResource(R.drawable.see);
                    mBinding.etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mBinding.etPassword.setSelection(mBinding.etPassword.getText().length());
                }
                break;
//			case R.id.imgRefresh:// 刷新验证码
//				setVerify();
//				break;
            case R.id.tv_forget:
                Intent intent = new Intent(this, ForgetPassActivity.class);
                intent.putExtra("type", 3);//1,绑定手机号，2修改密码，3忘记密码
                startActivity(intent);
                break;
        }
    }

    public void GetOtherInfo() {//app第三方登录获取绑定信息接口
        Log.e("requestSuccess", "----------请求了一次-----------");
        long time = System.currentTimeMillis();
        String validate = "" + MyApplication.getUserData().openID + type + MyApplication.getUserData().appopenid + time;
        String sign = StringUtil.MD5(validate);
        JSONObject obj = new JSONObject();
        try {
            obj.put("openid", MyApplication.getUserData().openID);
            obj.put("type", type);
            obj.put("appopenid", MyApplication.getUserData().appopenid);
            obj.put("time", time);
            obj.put("sign", sign);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //同步请求
        Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
        UserService.Api service = retrofit.create(UserService.Api.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
        Call<UserRespBean> call = service.getOpenUserInfo(body);
        call.enqueue(new Callback<UserRespBean>() {
            @Override
            public void onResponse(Call<UserRespBean> call, Response<UserRespBean> response) {
                Log.e("requestSuccess", "-----------------------" + response.body());
                if (response != null && response.body() != null && response.body().getData() != null) {
                    mUserRespBean = response.body();
                    UserDataBean userDataBean = new UserDataBean();
                    userDataBean.loginStatue = true;
                    userDataBean.isThirdPartLogin = true;
                    userDataBean.openType = type;
                    userDataBean.avatarstr = mUserRespBean.getData().getAvatarstr();
                    userDataBean.nickname = mUserRespBean.getData().getNickname();
                    userDataBean.uid = mUserRespBean.getData().getUid();
                    userDataBean.username = mUserRespBean.getData().getUsername();
                    userDataBean.mobile = mUserRespBean.getData().getMobile();
                    userDataBean.integral = mUserRespBean.getData().getIntegral();

                    MyApplication.writeUserData(userDataBean);
                    SharedUtil.setSharedPreferencesData("userId", "" + userDataBean.uid);

                    if (!StringUtil.isEmpty(mUserRespBean.getData().getIntegralmsg()))
                        Toast.makeText(mContext, "" + mUserRespBean.getData().getIntegralmsg(), Toast.LENGTH_SHORT).show();
                }
                setResult(Activity.RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(Call<UserRespBean> call, Throwable throwable) {
                Log.e("requestFailure", throwable.getMessage() + "");
                finish();
            }
        });

    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            Log.i("umeng_login_userinfo", "获取开始");
        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            Log.i("umeng_login_userinfo", "获取开始");
            if (type == 3) {//3微信
                openID = map.get("unionid");
                appOpenid = map.get("openid");
            } else if (type == 2) {//2微博
                openID = map.get("uid");
            } else {//1qq
                openID = map.get("unionid");
                appOpenid = map.get("openid");
            }
            avatarstr = map.get("iconurl");
            nickname = map.get("name");

            UserDataBean userDataBean = new UserDataBean();
            userDataBean.loginStatue = true;
            userDataBean.isThirdPartLogin = true;
            userDataBean.openType = type;
            userDataBean.avatarstr = avatarstr;
            userDataBean.nickname = nickname;
            userDataBean.openID = openID;
            userDataBean.appopenid = appOpenid;

            MyApplication.writeUserData(userDataBean);
            GetOtherInfo();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            //Toast.makeText(MainActivity.this, "Authorize fail", Toast.LENGTH_SHORT).show();
            Log.i("umeng_login", "登录失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            Log.i("umeng_login", "登录取消");
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mUserPresenter != null) {
            mUserPresenter.setHandler(mHandler);
        }
    }
}