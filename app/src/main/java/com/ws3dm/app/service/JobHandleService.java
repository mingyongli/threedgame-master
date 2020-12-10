package com.ws3dm.app.service;

/**
 * Author : DKjuan:
 * <p>
 * Date :  2018/10/20  9:36
 */

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.ws3dm.app.R;

import java.util.List;

@SuppressLint("NewApi")
public class JobHandleService extends JobService {
    private JobScheduler mJobScheduler;
    private static final String CHANNEL_ID = "My Notification ChannelID";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        Notification build = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_launcher)
                .setContentTitle("3DM 后台服务")
                .setContentText("").build();
        startForeground(1, build);
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
        Log.e("keepService", "JobHandleService服务被创建(onStartCommand)");
        //startService(new Intent(this, LocalService.class));
        //startService(new Intent(this, RemoteService.class));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            JobInfo.Builder builder = new JobInfo.Builder(startId++,
                    new ComponentName(getPackageName(), JobHandleService.class.getName()));

            builder.setPeriodic(60000); //每隔5秒运行一次,后修改为60秒
            builder.setRequiresCharging(true);
            builder.setPersisted(true);  //设置设备重启后，是否重新执行任务
            builder.setRequiresDeviceIdle(true);

            if (mJobScheduler.schedule(builder.build()) <= 0) {
                //If something goes wrong
                Log.e("keepService", "JobHandleService工作失败(onStartCommand)");
            } else {
                Log.e("keepService", "JobHandleService工作成功(onStartCommand)");
            }
        }
        return START_STICKY;
    }


    @Override
    public boolean onStartJob(JobParameters params) {

//        || isServiceRunning(this, "com.ph.myservice.RemoteService") == false
        Log.e("keepService", "JobHandleService开始工作(onStartJob)");
        if (!isServiceRunning(getApplicationContext(), "com.ws3dm.app:channel") || !isServiceRunning(getApplicationContext(), "com.ws3dm.app:remote")) {
            //startService(new Intent(this, LocalService.class));
            //startService(new Intent(this, RemoteService.class));
        }

       /* boolean serviceRunning = isServiceRunning(getApplicationContext(), "com.ph.myservice");
        System.out.println("进程一" + serviceRunning);

        boolean serviceRunning2 = isServiceRunning(getApplicationContext(), "com.ph.myservice:remote");
        System.out.println("进程二" + serviceRunning2);*/
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        if (!isServiceRunning(this, "com.ws3dm.app.LocalService") || !isServiceRunning(this, "com.ws3dm.app.RemoteService")) {
            Log.e("keepService", "JobHandleService开始工作(onStopJob)");
            //startService(new Intent(this, LocalService.class));
            //startService(new Intent(this, RemoteService.class));
        }
        return false;
    }

    // 服务是否运行
    public boolean isServiceRunning(Context context, String serviceName) {
        boolean isRunning = false;
        ActivityManager am = (ActivityManager) this
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> lists = am.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo info : lists) {// 获取运行服务再启动
            System.out.println(info.processName);
            if (info.processName.equals(serviceName)) {
                isRunning = true;
            }
        }
        return isRunning;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
    }
}