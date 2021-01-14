package com.ws3dm.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.alibaba.fastjson.JSON;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.mvp.presenter.UMengPresenter;
import com.ws3dm.app.util.BadgeUtil;
import com.ws3dm.app.util.DataCleanManager;
import com.ws3dm.app.util.SharedUtil;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.glide.GlideUtil;

public class MyApplication extends MultiDexApplication {
    public static PushAgent mPushAgent;
    private static MyApplication mInstance = null;
    private static UserDataBean mUserDataBean = null;//存储用户信息
    private int num_badge = 0;
    public static MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
//        设置角标
        BadgeUtil.setBadgeCount(this, 0);
        num_badge = 0;
        // 初始化友盟登录和分享
        UMShareAPI.get(this);
        // 初始化友盟推送
       // UMConfigure.init(this, "599e9c648630f54145000944", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "d98dec10e8edd7cff97d6742054b4830");
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "d98dec10e8edd7cff97d6742054b4830");
        // 初始化友盟统计
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
        mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                Log.e("token", deviceToken);//注册成功会返回device token
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.e("token", "fail：" + s + "---" + s1);
            }
        });
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            @Override
            public void dealWithNotificationMessage(final Context context, final UMessage msg) {
                super.dealWithNotificationMessage(context, msg);
                Log.e("umeng_handle", "系统默认消息处理");
                BadgeUtil.setBadgeCount(mInstance, 1 + num_badge++);
            }

            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                Log.e("umeng_handle", "自定义消息处理");
                String newString = "";
                newString = msg.getRaw().optString("extra");
                NewsBean news = JSON.parseObject(newString, NewsBean.class);
//                newsCollectFile.addPush(news);
                UMengPresenter.getInstance().postNews(news);
            }
        };

        mPushAgent.setMessageHandler(messageHandler);
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                Log.e("umeng_click", "点击消息+dealWithCustomAction");
                String newString = "";
                newString = msg.getRaw().optString("extra");
                NewsBean news = JSON.parseObject(newString, NewsBean.class);
                UMengPresenter.getInstance().postNews(news);
            }

            @Override
            public void launchApp(Context context, UMessage msg) {//进入打开app
                //启动app
                Intent intentApp = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getPackageName());
                intentApp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentApp);

                final UMessage tempMsg = msg;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        Log.e("umeng_click", "点击消息+launchApp");
                        String newString = "";
                        newString = tempMsg.getRaw().optString("extra");
                        NewsBean news = JSON.parseObject(newString, NewsBean.class);
                        if (news != null) {
                            UMengPresenter.getInstance().postNews(news);
                            Log.e("umeng_click", "点击消息+launchApp-postNews");
                        }
                    }
                }, 5000);
//                BadgeUtil.setBadgeCount(getBaseContext(),0);
//                Intent intent = new Intent(mInstance, NewsActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("newsBean",JSON.parseObject(msg.getRaw().optString("extra"),NewsBean.class));
//                intent.putExtras(bundle);
//                mInstance.startActivity(intent);
            }

            @Override
            public void openUrl(Context context, UMessage msg) {
                Log.e("umeng_click", "点击消息+openUrl");
            }

            @Override
            public void openActivity(Context context, UMessage msg) {
                Log.e("umeng_click", "点击消息+openActivity");
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
        mPushAgent.setDisplayNotificationNumber(0);

    }

    // qq，微信，微博平台的配置
    static {
        PlatformConfig.setQQZone("1106013937", "lJBgfBBhprDLakwe");
        PlatformConfig.setQQFileProvider("com.ws3dm.app.fileProvider");
        PlatformConfig.setWeixin("wxb1478c513baea637", "3af06b379b12912e8f10ec5f2b605b6a");
        PlatformConfig.setSinaWeibo("2708848138", "2eb98d788c7e99febeac499648b91bbb", "http://www.3dmgame.com/");
    }

    //    读用户数据
    public static UserDataBean getUserData() {
        if (mUserDataBean == null) {
            String json = SharedUtil.readPreferences(mInstance, "user_data");
            if (!StringUtil.isEmpty(json)) {
                mUserDataBean = JSON.parseObject(json, UserDataBean.class);
            } else {
                mUserDataBean = new UserDataBean();
            }
        }
        return mUserDataBean;
    }

    //    写用户数据
    public static void writeUserData(UserDataBean userDataBean) {
        SharedUtil.writePreferences(mInstance, "user_data", JSON.toJSONString(userDataBean));
        mUserDataBean = userDataBean;
    }

    public static String getmVersion() {
        return SharedUtil.getSharedPreferencesData("Version");
    }

    public static void setmVersion(String mVersion) {
        SharedUtil.setSharedPreferencesData("Version", mVersion);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        GlideUtil.GuideClearMemory(this);
        DataCleanManager.cleanInternalCache(this);
    }

}