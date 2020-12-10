//package com.ws3dm.app.push;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//
//import com.alibaba.fastjson.JSON;
//import com.umeng.message.UmengNotifyClickActivity;
//import com.ws3dm.app.R;
//import com.ws3dm.app.bean.NewsBean;
//import com.ws3dm.app.mvp.presenter.UMengPresenter;
//
//
///**
// * Author : DKjuan:
// * 小米对后台进程做了诸多限制。若使用一键清理，应用的channel进程被清除，
// * 将接收不到推送。通过接入小米托管弹窗功能，可有效防止以上情况，
// * 增加推送消息的送达率。通知将由小米系统托管弹出，点击通知栏将跳转到指定的Activity。
// * 该Activity需继承自UmengNotifyClickActivity，同时实现父类的onMessage方法，
// * 对该方法的intent参数进一步解析即可，该方法异步调用，不阻塞主线程。
// * <p>
// * Date :  2019/5/28  11:12
// */
//public class MipushTestActivity extends UmengNotifyClickActivity {
//	private static String TAG = MipushTestActivity.class.getName();
//	@Override
//	protected void onCreate(Bundle bundle) {
//		super.onCreate(bundle);
//		setContentView(R.layout.activity_mipush);
//	}
//	@Override
//	public void onMessage(Intent intent) {
//		super.onMessage(intent);  //此方法必须调用，否则无法统计打开数
//		final String newString = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
//		//启动app
//		Intent intentApp = getBaseContext().getPackageManager()
//				.getLaunchIntentForPackage(getPackageName());
//		intentApp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		startActivity(intentApp);
//
//		new Handler().postDelayed(new Runnable(){
//			public void run() {
//				Log.e("umeng_click","点击消息+launchApp");
//				NewsBean news = JSON.parseObject(newString,NewsBean.class);
//				if(news!=null){
//					UMengPresenter.getInstance().postNews(news);
//					Log.e("umeng_click","点击消息+launchApp-postNews");
//				}
//			}
//		}, 5000);
//		Log.i(TAG, newString);
//	}
//}