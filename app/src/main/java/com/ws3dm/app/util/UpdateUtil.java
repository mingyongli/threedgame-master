package com.ws3dm.app.util;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import androidx.core.app.NotificationCompat;

import com.ws3dm.app.BuildConfig;
import com.ws3dm.app.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateUtil {

	/**
     *更新进度条
     */
	public static ProgressBar mProgress;
	private static Dialog mDownloadDialog;
	/* 下载保存路径 */
	private String mSavePath;
	/* 记录进度条数量 */
	private static int progress;
	/* 是否取消更新 */
	private static boolean cancelUpdate = false;
	/* 下载中 */
	private static final int DOWNLOAD = 1;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 2;
	/* 待处理的activity */
	private static Context context;
	private static String downUrl="";
	static NotificationCompat.Builder builderProgress;
	static NotificationManager notificationManager;

	private UpdateUtil()
	{
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 显示软件下载框
	 *
	 * Context con：传入activity.class 传入getApplication会报异常
	 */
	public static void showDownloadDialog(Context con) {
		context= con;
//		SharedUtil.setSharedPreferencesData("noUp","1");
		// 构造软件下载对话框
		Builder builder = new Builder(con);
		builder.setTitle("下载中");
		// 给下载对话框增加进度条
		final LayoutInflater inflater = LayoutInflater.from(con);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);

		// 取消更新
		builder.setNegativeButton(R.string.cancel,new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						// 设置取消状态
						cancelUpdate = true;
					}
				});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		// 现在文件
		new Thread(new Runnable() {
			@Override
			public void run() {
				downloadApk();
			}
		}).start();
	}


	public static void showTitleDownload(Context con) {
		context= con;
		//进度条通知
		builderProgress = new NotificationCompat.Builder(context);
		builderProgress.setContentTitle("下载中");
		builderProgress.setSmallIcon(R.mipmap.ic_launcher);
		builderProgress.setTicker("进度条通知");
		builderProgress.setProgress(100, 0, false);
		final Notification notification = builderProgress.build();
		notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
		//发送一个通知
		notificationManager.notify(2, notification);

		// 现在文件
		downloadApk();
	}

	/**
	 * 下载apk文件
	 */
	private static void downloadApk() {
		// 启动新线程下载软件
		new downloadApkThread().start();
	}

	/**
	 * 下载文件线程
	 *
	 * @author coolszy
	 * @date 2012-4-26
	 * @blog http://blog.92coding.com
	 */
	public static class downloadApkThread extends Thread {
		@Override
		public void run() {
			try {
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// 获得存储卡的路径 //http://shouyou.3dmgame.com/img/3dmshoujikehuduan.apk
//					downUrl="https://mytest.3dmgame.com/app/apptest";
					downUrl="https://down.hyds360.com/1_40565";
					URL url = new URL(AppUtil.getRedirectUrl(downUrl));
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// 创建输入流
					InputStream is = conn.getInputStream();

					File pathdir = new File(Environment.getExternalStorageDirectory(),"3DM");

					if (!pathdir.exists()) {
						pathdir.mkdirs();
					}

					File apkFile = new File(pathdir, "3dm.apk");
	            	if(apkFile.exists()){
	            		FileUtil.deleteFile(apkFile);
	            	}
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do {
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位置
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						downHandle.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0) {
							// 下载完成
							downHandle.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// 点击取消就停止下载.
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 取消下载对话框显示
//			mDownloadDialog.dismiss();
		}
	};

	private static Handler downHandle = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 正在下载
			case DOWNLOAD:
				// 设置进度条位置
				mProgress.setProgress(progress);
//				//更新进度条
//				builderProgress.setProgress(100, progress, false);
//				//再次通知
//				notificationManager.notify(2, builderProgress.build());
				break;
			case DOWNLOAD_FINISH:
				// 安装文件
//				installAPK();
				AppUtil.installApp(context,Environment.getExternalStorageDirectory() + "/3DM/3dm.apk");
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 安装APK文件
	 */
//	public static void installAPK() {
//		Intent intent = new Intent();
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		intent.setAction(Intent.ACTION_VIEW);
//		// 下载之后自动安装APK
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//			intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//			Uri contentUri = FileProvider.getUriForFile(MyApplication.getInstance(), BuildConfig.APPLICATION_ID + ".fileProvider", new File(Environment.getExternalStorageDirectory() + "/3DM/3dm.apk"));
//			intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
//		} else {
//			intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/3DM/3dm.apk")), "application/vnd.android.package-archive");
//		}
//		MyApplication.getInstance().startActivity(intent);
//	}
}
