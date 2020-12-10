package com.ws3dm.app.activity;

import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ws3dm.app.mvp.model.RespBean.VersionRespBean;
import com.ws3dm.app.mvp.presenter.UserPresenter;
import com.ws3dm.app.util.SharedUtil;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.util.UpdateUtil;
import com.ws3dm.app.R;
import com.ws3dm.app.Constant;
import com.ws3dm.app.databinding.AcVersionBinding;
import com.ws3dm.app.util.AppUtil;
import com.ws3dm.app.util.DialogHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Describution :版本检测
 * 
 * Author : DKjuan
 * 
 * Date : 2017/9/4 16:32
 **/
public class VersionActivity extends BaseActivity{

	/**
	 * 弹出框
	 */
	private PopupWindow popupWindow;
	private TextView tv_update,tv_ignore,tv_content;
	private Handler mHandler;
	private UserPresenter mUserPresenter;

	private AcVersionBinding mBinding;
	
	@Override
	protected void init() {
		mBinding=bindView(R.layout.ac_version);
		mBinding.setHandler(this);
		
		initView();
	}
	
	public void initView(){
		try {
			mBinding.txtVersion.setText("版本信息：" + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

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
						if(mUpdateRespBean.getCode()==1&&Double.parseDouble(AppUtil.getVersionName(mContext))
								<Double.parseDouble(mUpdateRespBean.getData().getVersion())){
							showUpdateView(mBinding.content);
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
		tv_content = (TextView) pubPop.findViewById(R.id.txtContent);
		tv_content.setText(SharedUtil.getSharedPreferencesData("upContent"));
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
		DialogHelper.showPop(VersionActivity.this, v, popupWindow, Gravity.CENTER, 0, 0);
	}

	public void checkVerion(){
		long time=System.currentTimeMillis();
		String validate= "1"+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("type", 1);//1安卓2ios
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		mUserPresenter.getVersion(obj.toString());
	}

	public void clickHandler(View view) {
		switch (view.getId()) {
			// 返回上一级
			case R.id.imgReturn:
				finish();
				break;
			// 检测版本
			case R.id.txtCheck:
				checkVerion();
				break;
		}
	}
}