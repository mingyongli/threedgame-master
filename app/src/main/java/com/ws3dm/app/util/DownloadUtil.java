package com.ws3dm.app.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import com.ws3dm.app.mvp.presenter.MGPresenter;
import com.ws3dm.app.util.callback.CallBack;

import java.io.File;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

//import com.tpnet.downmanager.download.DownInfo;
//import com.tpnet.downmanager.download.DownManager;
//import com.tpnet.downmanager.download.db.DBUtil;

/**
 * Author : DKjuan:
 * <p>
 * Date :  2018/5/8  17:53
 */
public class DownloadUtil {
	public static Context mContext;
	public static DialogHelper dialogHelper;
	
	//创建下载
	public static void down(final Context context, final String url, final String name) {
		mContext=context;
		if(StringUtil.isEmpty(url)){
			Toast.makeText(context,"地址错误！",Toast.LENGTH_SHORT).show();
			return;
		}
		final String[] finalUrl = new String[1];
		if(!url.endsWith(".apk")){
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					finalUrl[0] =AppUtil.getRedirectUrl(url);
//					if(!url.endsWith(".apk")){
//						Looper.prepare();
//						Toast.makeText(context,"不支持非apk文件下载！",Toast.LENGTH_SHORT).show();
//						Looper.loop();
//						ToastUtil.showToast();
//						return;
//					}else{
//						down(finalUrl[0],name);
//					}
//				}
//			}).start();
			
			new Thread(){
				@Override
				public void run() {
					finalUrl[0] =AppUtil.getRedirectUrl(url);
					if(finalUrl[0]==null||!finalUrl[0].endsWith(".apk")){
						Looper.prepare();
						Toast.makeText(context,"不支持非apk文件下载！",Toast.LENGTH_SHORT).show();
						Looper.loop();
						return;
					}else{
//						down(finalUrl[0],name);
					}
				}
			}.start();
		}else{
			finalUrl[0]=url;
//			down(finalUrl[0],name);
		}
	}
	
//	public static void down(final String downUrl, final String name){
//		//判断时候已经在下载列表了EE
//		DBUtil.getInstance().getDownSavePath(downUrl)
//				.observeOn(AndroidSchedulers.mainThread())
//				.subscribe(new Action1<String>() {
//					@Override
//					public void call(final String savePath) {
//						Log.e("@@", "路径" + savePath);
//						if (!TextUtils.isEmpty(savePath)) {
//							showDialog(savePath, downUrl, name);
//						} else {
//							ToastUtil.showToast(mContext,"添加 "+name.split("\\|")[0]+" 到手游下载列表");
//							startDown(downUrl, name);
//							MGPresenter.getInstance().postDown("+");
//						}
//					}
//				});
//	}

	private static String getPath(String link) {
		File downPath = new File(Environment.getExternalStorageDirectory(), "/3DM/Download");
		if (!downPath.exists()) {
			downPath.mkdirs();
		}
		File file = new File(Environment.getExternalStorageDirectory() + "/3DM/Download/" + link.substring(link.lastIndexOf("/") + 1));

		if (file.exists()) {
			file.delete();
		}
		return file.getAbsolutePath();
	}
	private static void showDialog(final String savePath, final String downUrl, final String name) {
		//提示已经存在，是否重新下载
		dialogHelper = new DialogHelper(mContext, new CallBack() {
			@Override
			public void callBackFunction() {
				// TODO Auto-generated method stub
				if(dialogHelper.isConfirm()){
					showLoadingDialog();
					Observable.just(savePath)
							.observeOn(Schedulers.computation())
							.map(new Func1<String, Boolean>() {
								@Override
								public Boolean call(String s) {//删除原来的文件
									return com.ws3dm.app.util.FileUtil.delFile(s);
								}
							}).doAfterTerminate(new Action0() {
						@Override
						public void call() {
							progressDialog.dismiss();
						}
					})
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe(new Action1<Boolean>() {
								@Override
								public void call(Boolean aBoolean) {
									ToastUtil.showToast(mContext,"添加 "+name.split("\\|")[0]+" 到下载列表");
									if (aBoolean) {//删除成功开始下载
										MGPresenter.getInstance().postDown("+");
//										startDown(downUrl, name);
									} else {//提示删除源文件失败
//										com.tpnet.downmanager.utils.ToastUtil.show("删除失败");
									}
								}
							});
				}
			}
		});
		dialogHelper.showDialog("提示", "已经存在该下载记录，是否覆盖?", false);
	}

	static ProgressDialog progressDialog;

	public static void showLoadingDialog() {
		if (progressDialog == null) {
			progressDialog = ProgressDialog.show(mContext, "提示", "正在删除");
		} else {
			progressDialog.show();
		}
	}

	//开始下载
//	public static void startDown(String url, String name) {
//		Program program = Program.create(url, name);
//		//插入到下载bean
//		DatabaseUtil.getInstance().insertProgrmm(program);
//		//创建基本任务，参数为:文件保存的路径、下载的url要全路径、下载任务的名称
//		DownInfo downInfo = DownInfo.builder().create(getPath(url), url, name);
//		//开始下载
//		DownManager.getInstance().startDown(downInfo);
//	}

	//删除下载
//	public static void delAll() {
//		//删除下载
//		DownManager.getInstance().delall();
//	}
}
