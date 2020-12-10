package com.ws3dm.app.service;

/**
 * Author : DKjuan:远程服务code:
 * <p>
 * Date :  2018/10/20  9:34
 */
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import android.util.Log;

import com.ws3dm.app.R;

public class RemoteService extends Service {
	MyConn conn;
	MyBinder binder;
	private static final String CHANNEL_ID = "My Notification ChannelID";
	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		createNotificationChannel();
		Notification build = new NotificationCompat.Builder(this, CHANNEL_ID)
				.setSmallIcon(R.drawable.icon_launcher)
				.setContentTitle("3DM 后台服务")
				.setContentText("").build();
		startForeground(1,build);
		conn = new MyConn();
		binder = new MyBinder();
	}
	private void createNotificationChannel() {
		// Create the NotificationChannel, but only on API 26+ because
		// the NotificationChannel class is new and not in the support library
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			CharSequence name = "3DM 后台服务";
			int importance = NotificationManager.IMPORTANCE_LOW;
			NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
			// Register the channel with the system; you can't change the importance
			// or other notification behaviors after this
			NotificationManager notificationManager = getSystemService(NotificationManager.class);
			notificationManager.createNotificationChannel(channel);
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e("keepService"," RemoteService started(onStartCommand)");
		this.bindService(new Intent(this, LocalService.class), conn, Context.BIND_IMPORTANT);

		return START_STICKY;
	}

	class MyBinder extends IMyAidlInterface.Stub {
		@Override
		public String getServiceName() throws RemoteException {
			return RemoteService.class.getSimpleName();
		}
	}

	class MyConn implements ServiceConnection {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {

		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.e("keepService"," RemoteService killed(onServiceDisconnected)");
			//开启本地服务
			startService(new Intent(RemoteService.this, LocalService.class));
			//绑定本地服务
			bindService(new Intent(RemoteService.this, LocalService.class), conn, Context.BIND_IMPORTANT);
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
//		//开启本地服务
//		startService(new Intent(RemoteService.this, LocalService.class));
//		//绑定本地服务
//		bindService(new Intent(RemoteService.this, LocalService.class), conn, Context.BIND_IMPORTANT);

	}
}