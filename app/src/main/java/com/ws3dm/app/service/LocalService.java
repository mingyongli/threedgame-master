package com.ws3dm.app.service;

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
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import android.util.Log;

import com.ws3dm.app.R;

/**
 * Author : DKjuan:本地服务code
 * <p>
 * Date :  2018/10/20  9:35
 */

public class  LocalService extends Service {
	MyBinder binder;
	MyConn conn;
	private static final String CHANNEL_ID = "My Notification ChannelID";
	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
	@Override
	public void onCreate() {
		super.onCreate();
		createNotificationChannel();
		Notification build = new NotificationCompat.Builder(this, CHANNEL_ID)
				.setSmallIcon(R.drawable.icon_launcher)
				.setContentTitle("3DM 后台服务")
				.setContentText("").build();
		startForeground(1,build);
		binder = new MyBinder();
		conn = new MyConn();
	}
	private void createNotificationChannel() {
		// Create the NotificationChannel, but only on API 26+ because
		// the NotificationChannel class is new and not in the support library
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			CharSequence name = "3DM 后台服务";
			int importance = NotificationManager.IMPORTANCE_MIN;
			NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
			// Register the channel with the system; you can't change the importance
			// or other notification behaviors after this
			NotificationManager notificationManager = getSystemService(NotificationManager.class);
			notificationManager.createNotificationChannel(channel);
		}
	}

	class MyBinder extends IMyAidlInterface.Stub {
		@Override
		public String getServiceName() throws RemoteException {
			return LocalService.class.getSimpleName();
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e("keepService"," LocalService started(onStartCommand)");
		this.bindService(new Intent(LocalService.this, RemoteService.class), conn, Context.BIND_IMPORTANT);
		return START_STICKY;
	}

	class MyConn implements ServiceConnection {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.e("keepService"," LocalService killed(onServiceDisconnected)");
			//开启远程服务
			startService(new Intent(LocalService.this, RemoteService.class));
			//绑定远程服务
			bindService(new Intent(LocalService.this, RemoteService.class), conn, Context.BIND_IMPORTANT);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
//		//开启远程服务
//		startService(new Intent(this, RemoteService.class));
//		//绑定远程服务
//		bindService(new Intent(this, RemoteService.class), conn, Context.BIND_IMPORTANT);
	}
}