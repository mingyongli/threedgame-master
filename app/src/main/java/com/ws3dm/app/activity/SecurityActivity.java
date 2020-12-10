package com.ws3dm.app.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.ws3dm.app.R;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.Constant;
import com.ws3dm.app.databinding.AcSecurityBinding;
import com.ws3dm.app.mvp.model.RespBean.VersionRespBean;
import com.ws3dm.app.mvp.presenter.UserPresenter;
import com.ws3dm.app.util.AppUtil;
import com.ws3dm.app.util.DialogHelper;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.util.UpdateUtil;

import java.util.Map;

/**
 * Describution : 安全activity（包含 修改密码，更换手机，收货地址，注销账号）
 * 
 * Author : DKjuan
 * 
 * Date : 2018/11/26 13:35
 **/
public class SecurityActivity extends BaseActivity{

	/**
	 * 弹出框
	 */
	private PopupWindow popupWindow;
	private TextView tv_update,tv_ignore;
	private Handler mHandler;
	private UserPresenter mUserPresenter;

	private AcSecurityBinding mBinding;
	
	@Override
	protected void init() {
		mBinding=bindView(R.layout.ac_security);
		mBinding.setHandler(this);
		
		initView();
	}
	
	public void initView(){
//		try {
//			mBinding.txtVersion.setText("版本信息：" + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
//		} catch (PackageManager.NameNotFoundException e) {
//			e.printStackTrace();
//		}
		String phone = MyApplication.getUserData().mobile;
		if(phone.length() == 11){
			phone = phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
		}
		mBinding.userName.setText(MyApplication.getUserData().nickname);
		mBinding.userPhone.setText(phone);
		mBinding.changePhone.setText(phone);
		mUserPresenter = UserPresenter.getInstance();
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (!mIsRunning) {
					return;
				}
				switch (msg.what) {
					case Constant.Notify.APP_VERSION:
						VersionRespBean mUpdateRespBean = (VersionRespBean) msg.obj;
						if("1".equals(mUpdateRespBean.getCode())&&Double.parseDouble(AppUtil.getVersionName(mContext))
								<Double.parseDouble(mUpdateRespBean.getData().getVersion())){
							showUpdateView(mBinding.llContent);
						}else{
							ToastUtil.showToast(mContext,"已经是最新版本了哦！");
						}
//						mBinding.txtInfo.setText(mUpdateRespBean.getData().getDescription());
						break;
				}
			}
		};
		mUserPresenter.setHandler(mHandler);
	}


	// 选择图片的弹窗
	public void showUpdateView(View v) {// takePhoto, selectPhoto, cancle
		View pubPop = LayoutInflater.from(this).inflate(R.layout.view_update, null);
		popupWindow = DialogHelper.createPopupWindow(this, pubPop, R.style.AnimBottom);
		tv_update = (TextView) pubPop.findViewById(R.id.tv_update);
		tv_update.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				popupWindow.dismiss();
//				mContext.startService(new Intent(mContext, DownLoadService.class));
				UpdateUtil.showDownloadDialog(mContext);
			}
		});
		tv_ignore=(TextView) pubPop.findViewById(R.id.tv_ignore);
		tv_ignore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				popupWindow.dismiss();
			}
		});
		DialogHelper.showPop(SecurityActivity.this, v, popupWindow, Gravity.CENTER, 0, 0);
	}

	// 账号退出
	private void setLogOut() {
		if(MyApplication.getUserData().isThirdPartLogin){
			switch (MyApplication.getUserData().openType) {//1qq2微博3微信(绑定类型)
				case 1:
					UMShareAPI.get(mContext).deleteOauth(this, SHARE_MEDIA.QQ,umDelListener);
					break;
				case 2:
					UMShareAPI.get(mContext).deleteOauth(this, SHARE_MEDIA.SINA,umDelListener);
					break;
				case 3:
					UMShareAPI.get(mContext).deleteOauth(this, SHARE_MEDIA.WEIXIN,umDelListener);
					break;
				default:
					break;
			}
		}

		UserDataBean userDataBean = new UserDataBean();
		MyApplication.writeUserData(userDataBean);

		ToastUtil.showToast(this,"退出成功");
		finish();
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

	public void clickHandler(View view) {
		Intent intent=null;
		switch (view.getId()) {
			case R.id.imgReturn:// 返回上一级
				finish();
				break;
			case R.id.rl_mod_pass:// 修改密码
			case R.id.tv_mod_pass:
			case R.id.iv_mod_pass:
				intent=new Intent(this, ForgetPassActivity.class);
				intent.putExtra("type",2);//1,绑定手机号，2修改密码，3忘记密码
				startActivity(intent);
				break;
			case R.id.rl_changeno://更换手机
			case R.id.tv_changeno:
				intent=new Intent(this, ChangePhoneActivity.class);
				startActivity(intent);
				break;
			case R.id.rl_address://收货地址
			case R.id.tv_address:
			case R.id.iv_address:
				//intent=new Intent(this, AddressActivity.class);
				intent=new Intent(this, BindShareActivity.class);
				startActivity(intent);
				break;
			case R.id.rl_close_account://注销账号
			case R.id.tv_close_account:
			case R.id.iv_close_account:
				intent=new Intent(this, CloseAccountActivity.class);
				startActivity(intent);
				finish();
				break;
			case R.id.tv_log_off://退出登录
				setLogOut();
				break;
			default:
				Log.e(TAG, "TODO: click handler " + view.getId());
				break;
		}
	}
}