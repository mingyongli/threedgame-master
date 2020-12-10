package com.ws3dm.app.activity;

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
import com.ws3dm.app.R;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.Constant;
import com.ws3dm.app.databinding.AcForgetpassBinding;
import com.ws3dm.app.event.ErrorEvent;
import com.ws3dm.app.mvp.model.RespBean.UserRespBean;
import com.ws3dm.app.mvp.presenter.UserPresenter;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.UserService;
import com.ws3dm.app.util.DownTimer;
import com.ws3dm.app.util.SharedUtil;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Describution :忘记,修改密码
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2017/8/30 16:24
 **/
public class ForgetPassActivity extends BaseActivity {

	private AcForgetpassBinding mBinding;
	private UserPresenter mUserPresenter;
	private boolean isSeeOne; //密码是否可见
	private Handler mHandler;
	private DownTimer mTimer;
	private int type=0;//1,绑定手机号，2修改密码，3忘记密码
	private int openType=0;//1qq2微博3微信(绑定类型)

	@Override
	protected void init() {
		mBinding = bindView(R.layout.ac_forgetpass);
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
						Log.e("requestFailure", "失败:"+error.message);
						ToastUtil.showToast(mContext,error.message);
						break;
					case Constant.Notify.RESULT_RESET_PASS://重置密码返回处理
						UserRespBean mUserRespBean = (UserRespBean) msg.obj;
						if(mUserRespBean.getCode()==1){
							if(mUserRespBean.getData()==null||StringUtil.isEmpty(mUserRespBean.getData().getIntegralmsg()))
								Toast.makeText(mContext, "重置成功", Toast.LENGTH_SHORT).show();
							else
								Toast.makeText(mContext, ""+mUserRespBean.getData().getIntegralmsg(), Toast.LENGTH_SHORT).show();
//							Toast.makeText(mContext, "修改成功,请重新登陆！", Toast.LENGTH_SHORT).show();
							finish();
						}else{
							Toast.makeText(mContext, "修改失败！", Toast.LENGTH_SHORT).show();
						}
						break;
					case Constant.Notify.RESULT_MODIFY_PASS://修改密码返回处理
						UserRespBean bean = (UserRespBean) msg.obj;
						if(bean.getCode()==1){
							setLogOut();
							if(bean.getData()==null||StringUtil.isEmpty(bean.getData().getIntegralmsg()))
								Toast.makeText(mContext, "修改成功！", Toast.LENGTH_SHORT).show();
							else
								Toast.makeText(mContext, ""+bean.getData().getIntegralmsg(), Toast.LENGTH_SHORT).show();
//							Toast.makeText(mContext, "修改成功,请重新登陆！", Toast.LENGTH_SHORT).show();
							startActivity(new Intent(mContext,LoginActivity.class));
							finish();
						}else{
							Toast.makeText(mContext, "修改失败！", Toast.LENGTH_SHORT).show();
						}
						break;
					case Constant.Notify.RESULT_USER_BINDBYID://老用户绑定手机号
						UserRespBean oldBind = (UserRespBean) msg.obj;
						if(oldBind.getCode()==1){
							UserDataBean userDataBean =new UserDataBean();
							userDataBean.loginStatue=true;
							userDataBean.avatarstr=oldBind.getData().getAvatarstr();
							userDataBean.nickname=oldBind.getData().getNickname();
							userDataBean.uid=oldBind.getData().getUid();
							userDataBean.username=oldBind.getData().getUsername();
							userDataBean.mobile=oldBind.getData().getMobile();
							userDataBean.integral=oldBind.getData().getIntegral();

							MyApplication.writeUserData(userDataBean);
							SharedUtil.setSharedPreferencesData("userId", userDataBean.uid);
							if(StringUtil.isEmpty(oldBind.getData().getIntegralmsg()))
								Toast.makeText(mContext, "绑定成功！", Toast.LENGTH_SHORT).show();
							else
								Toast.makeText(mContext, ""+oldBind.getData().getIntegralmsg(), Toast.LENGTH_SHORT).show();
							finish();
						}else{
							Toast.makeText(mContext, "绑定失败！", Toast.LENGTH_SHORT).show();
						}
						break;
					case Constant.Notify.RESULT_USER_BINDPHONE://第三方登陆绑定，非老用户
						UserRespBean oldBind2 = (UserRespBean) msg.obj;
						if(oldBind2.getCode()==1){
							UserDataBean userDataBean =new UserDataBean();
							userDataBean.loginStatue=true;
							userDataBean.avatarstr=oldBind2.getData().getAvatarstr();
							userDataBean.nickname=oldBind2.getData().getNickname();
							userDataBean.uid=oldBind2.getData().getUid();
							userDataBean.username=oldBind2.getData().getUsername();
							userDataBean.mobile=mBinding.etPhonenum.getText().toString().trim();
							userDataBean.integral=oldBind2.getData().getIntegral();

							MyApplication.writeUserData(userDataBean);
							SharedUtil.setSharedPreferencesData("userId", userDataBean.uid);
							if(StringUtil.isEmpty(oldBind2.getData().getIntegralmsg()))
								Toast.makeText(mContext, "绑定成功,请重新登陆", Toast.LENGTH_SHORT).show();
							else
								Toast.makeText(mContext, ""+oldBind2.getData().getIntegralmsg(), Toast.LENGTH_SHORT).show();
							finish();
						}else{
							Toast.makeText(mContext, "绑定失败！", Toast.LENGTH_SHORT).show();
						}
						break;
				}
			}
		};
		mUserPresenter.setHandler(mHandler);

		initView();
	}

	// 账号退出
	private void setLogOut() {
		if(MyApplication.getUserData().isThirdPartLogin){
			switch (MyApplication.getUserData().openType) {//1qq2微博3微信(绑定类型)
				case 1:
					UMShareAPI.get(mContext).deleteOauth(ForgetPassActivity.this, SHARE_MEDIA.QQ,umDelListener);
					break;
				case 2:
					UMShareAPI.get(mContext).deleteOauth(ForgetPassActivity.this, SHARE_MEDIA.SINA,umDelListener);
					break;
				case 3:
					UMShareAPI.get(mContext).deleteOauth(ForgetPassActivity.this, SHARE_MEDIA.WEIXIN,umDelListener);
					break;
				default:
					break;
			}
		}

		UserDataBean userDataBean = new UserDataBean();
		MyApplication.writeUserData(userDataBean);
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

	private void initView() {
		type=getIntent().getIntExtra("type",0);//1,绑定手机号，2修改密码，3忘记密码
		if(type==1){
			openType=MyApplication.getUserData().openType;
		}

		if(type==2){//已登录 -> 修改
			mBinding.tvTitle.setText("修改密码");
			mBinding.etName.setText(MyApplication.getUserData().mobile);
			mBinding.etName.setFocusable(false);
			mBinding.etName.setFocusableInTouchMode(false);
		}else if(type==1){//绑定手机号
			if(!MyApplication.getUserData().isThirdPartLogin){
				mBinding.llPassword.setVisibility(View.GONE);
				mBinding.llPassword2.setVisibility(View.GONE);
			}
			mBinding.llUser.setVisibility(View.GONE);
			mBinding.llPhone.setVisibility(View.VISIBLE);
			mBinding.tvTitle.setText("绑定手机号");
		}else{//未登录 -> 忘记密码 type=3
			mBinding.tvTitle.setText("忘记密码");
		}

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
	}

	//点击事件
	public void clickHandler(View view) {
		switch (view.getId()) {
			// 返回
			case R.id.imgReturn:
				finish();
				break;
			case R.id.tv_submit:
				if(mBinding.etVerify.getText().toString().trim().length()<=0){
					ToastUtil.showToast(mContext,"验证码不能为空！");
					return;
				}


				long time = System.currentTimeMillis();
				JSONObject obj;
				//分情况调用接口
				if(type==2){//已登录 -> 修改
					String validate = MyApplication.getUserData().uid + mBinding.etVerify.getText().toString().trim().toString()
							+mBinding.etPassword.getText().toString().trim()+mBinding.etPassword.getText().toString().trim() +  time;
					String sign = StringUtil.MD5(validate);
					obj = new JSONObject();
					try {
						obj.put("uid", MyApplication.getUserData().uid);
						obj.put("validate", mBinding.etVerify.getText().toString().trim());
						obj.put("passwd", mBinding.etPassword.getText().toString().trim());
						obj.put("checkpasswd", mBinding.etPassword.getText().toString().trim());
						obj.put("time", time);
						obj.put("sign", sign);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					mUserPresenter.modifyPass(obj.toString());
				}else if(type==1){//绑定手机号
					if(false&&MyApplication.getUserData().isThirdPartLogin){//第三方新用户绑定手机号，非老用户
						String validate =mBinding.etPhonenum.getText().toString().trim()+mBinding.etVerify.getText().toString().trim().toString()
								+MyApplication.getUserData().openID+mBinding.etPassword.getText().toString().trim()+openType+MyApplication.getUserData().appopenid+time;
						String sign = StringUtil.MD5(validate);
						obj = new JSONObject();
						try {
							obj.put("mobile", mBinding.etPhonenum.getText().toString().trim());
							obj.put("validate", mBinding.etVerify.getText().toString().trim());
							obj.put("openid", MyApplication.getUserData().openID);
							obj.put("passwd", mBinding.etPassword.getText().toString().trim());
							obj.put("type", openType);//1qq2微博3微信(绑定类型)
							obj.put("appopenid", MyApplication.getUserData().appopenid);//第三方微信登陆暂时传空字符串
							obj.put("time", time);
							obj.put("sign", sign);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						mUserPresenter.bindPhone(obj.toString());
					}else{//老用户
						String uid=MyApplication.getUserData().uid;
						String validate =mBinding.etPhonenum.getText().toString().trim()
								+mBinding.etVerify.getText().toString().trim().toString()+uid+time;
						String sign = StringUtil.MD5(validate);
						obj = new JSONObject();
						try {
							obj.put("mobile", mBinding.etPhonenum.getText().toString().trim());
							obj.put("validate", mBinding.etVerify.getText().toString().trim());
							obj.put("uid", uid);
							obj.put("time", time);
							obj.put("sign", sign);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						mUserPresenter.bindPhoneByUid(obj.toString());
					}
				}else{//未登录 -> 忘记密码
					String validate = mBinding.etName.getText().toString().trim() + mBinding.etVerify.getText().toString().trim().toString()
							+mBinding.etPassword.getText().toString().trim()+mBinding.etPassword.getText().toString().trim() +  time;
					String sign = StringUtil.MD5(validate);
					obj = new JSONObject();
					try {
						obj.put("mobile", mBinding.etName.getText().toString().trim());
						obj.put("validate", mBinding.etVerify.getText().toString().trim());
						obj.put("passwd", mBinding.etPassword.getText().toString().trim());
						obj.put("checkpasswd", mBinding.etPassword.getText().toString().trim());
						obj.put("time", time);
						obj.put("sign", sign);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					mUserPresenter.resetPass(obj.toString());
				}
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
			case R.id.tv_getsms:
				if(type!=1){//2修改密码，3忘记密码
					if (mBinding.etName.getText().toString().length() == 0) {
						ToastUtil.showToast(mContext, "手机号不能为空！");
					} else if (!StringUtil.isMobieNumber(mBinding.etName.getText().toString())) {
						ToastUtil.showToast(mContext, "手机号错误！");
					}else if(!mBinding.etPassword.getText().toString().trim()
							.equalsIgnoreCase(mBinding.etPassword2.getText().toString().trim())){
						ToastUtil.showToast(mContext, "两次密码不一致！");
					} else {
						if(MyApplication.getUserData().loginStatue){//已登录 -> 修改
							getSMS(mBinding.etName.getText().toString(),5);
							mTimer.start();
						}else{//未登录 -> 忘记密码 
							getSMS(mBinding.etName.getText().toString(),4);
							mTimer.start();
						}
					}
				}else{//1,绑定手机号
					if (mBinding.etPhonenum.getText().toString().length() == 0) {
						ToastUtil.showToast(mContext, "手机号不能为空！");
					} else if (!StringUtil.isMobieNumber(mBinding.etPhonenum.getText().toString())) {
						ToastUtil.showToast(mContext, "手机号错误！");
					} else {
						if(false&&MyApplication.getUserData().isThirdPartLogin){//第三方登陆
							getSMS(mBinding.etPhonenum.getText().toString(),1);
							mTimer.start();
						}else{//老用户
							getSMS(mBinding.etPhonenum.getText().toString(),3);
							mTimer.start();
						}
					}
				}
				break;
		}
	}

	public void getSMS(String phoneNo, int act) {//act ： 1注册 4找回密码 5修改密码，第三方登陆act：1，uid传0，老用户act：3，uid传老用户uid
		try {
			String uid=MyApplication.getUserData().loginStatue?MyApplication.getUserData().uid:"0";
			long time = System.currentTimeMillis();
			String validate = phoneNo + act+uid + time;
			String sign = StringUtil.MD5(validate);

			JSONObject obj = new JSONObject();
			obj.put("mobile", phoneNo);
			obj.put("act", act);
			obj.put("uid", uid);
			obj.put("time", time);
			obj.put("sign", sign);
			//同步请求
			Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
			UserService.Api service = retrofit.create(UserService.Api.class);
			RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
			Call<ResponseBody> call = service.getSMS(body);
			call.enqueue(new Callback<ResponseBody>() {
				@Override
				public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
					Log.e("requestSuccess", "-----------------------" + response.body());
					try {
						JSONObject jsonObject=new JSONObject(response.body().string());
						if(jsonObject.optInt("code")==1){
							ToastUtil.showToast(mContext, "短信获取成功！");
						}else{
							mTimer.stop();
							mBinding.tvGetsms.setText("重新获取");
							ToastUtil.showToast(mContext,jsonObject.opt("msg")+"");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(Call<ResponseBody> call, Throwable throwable) {
					Log.e("requestFailure", throwable.getMessage() + "");
					mTimer.stop();
					mBinding.tvGetsms.setText("重新获取");
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