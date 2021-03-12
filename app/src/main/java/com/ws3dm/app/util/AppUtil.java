package com.ws3dm.app.util;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import com.google.android.material.tabs.TabLayout;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ws3dm.app.MyApplication;
import com.ws3dm.app.BuildConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.UUID;


public class AppUtil {
    private static final String TAG = "AppUtil";
    /***************************************************************************
     * 作用: 添加app权限 （安卓6.0需要动态配置权限）(临时作用，6.0以后会删除这个方法)
     **************************************************************************/
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission  
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user  
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    public static boolean CheckStoragePermissions(Activity activity) {
        int readPermission = activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        int writePermission = activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return readPermission == PackageManager.PERMISSION_GRANTED && writePermission == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 根据pacagename判断应用有无安装
     *
     * @param context
     * @param uri
     * @return
     */
    public static boolean isAppInstalled(Context context, String uri) {
        boolean installed = false;
        if (context == null || StringUtil.isEmpty(uri))
            return installed;
        PackageManager pm = context.getPackageManager();

        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    /**
     * 安装APP
     *
     * @param context
     * @param fileName
     */
    public static void installApp(Context context, String fileName) {
        if (context == null || StringUtil.isEmpty(fileName))
            return;
        File file = new File(fileName);
        if (!file.exists()) {
            Toast.makeText(context, "文件已删除，请重新下载！", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(MyApplication.getInstance(), BuildConfig.APPLICATION_ID + ".fileProvider", new File(fileName));
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    public static void installApk(Context context, String path) {
        File fileApk = new File(path);

        //如果文件存在，就获取apk包名，检测是否已安装
        //已安装，则打开文件
        //未安装，则发起安装

        //如果文件不存在
        //改变状态，重新下载
        if (fileApk.exists()) {

            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(fileApk.getPath(), PackageManager.GET_ACTIVITIES);
            if (info != null) {
                ApplicationInfo appInfo = info.applicationInfo;
                String packageName = appInfo.packageName;  //得到安装包名称
                if (checkApkExist(context, packageName)) {
                    openOtherApp(context, packageName);
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //添加这一句表示对目标应用临时授权该Uri所代表的文件
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        Uri contentUri = FileProvider.getUriForFile(context,
                                BuildConfig.APPLICATION_ID + ".fileProvider", new File(path));
                        intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                    } else {
                        intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                    context.startActivity(intent);
                }
            }
        } else {
            Toast.makeText(context, "文件已删除，请重新下载！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 复制文件到SD卡
     *
     * @param context
     * @param fileName 复制的文件名
     * @param path     保存的目录路径
     * @return
     */
    public static boolean copyAssetsFile(Context context, String fileName, String path) {
        // TODO Auto-generated method stub

        try {
            InputStream mInputStream = context.getAssets().open(fileName);
            File file = new File(path);
            if (!file.exists()) {
                file.mkdir();
            }
            File mFile = new File(path + fileName);
            if (!mFile.exists())
                mFile.createNewFile();
            FileOutputStream mFileOutputStream = new FileOutputStream(mFile);
            byte[] mbyte = new byte[1024];
            int i = 0;
            while ((i = mInputStream.read(mbyte)) > 0) {
                mFileOutputStream.write(mbyte, 0, i);
            }
            mInputStream.close();
            mFileOutputStream.close();
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.i(TAG, fileName + "not exists" + "or write err");
            return false;
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
    }


    /**
     * 打开另外的app
     *
     * @param context
     * @param packageName
     */
    public static void openOtherApp(Context context, String packageName) {
        if (context == null || StringUtil.isEmpty(packageName))
            return;
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent != null)
            context.startActivity(intent);

    }


    /**
     * 获得设备型号
     *
     * @return
     */

    @SuppressWarnings("static-access")
    public static String getInfo() {
        Build bd = new Build();
        return bd.MANUFACTURER + " " + bd.MODEL;
    }

    /**
     * 获得操作系统版本
     *
     * @return
     */

    public static String getVersionRelease() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获得App版本号
     *
     * @param context
     * @return
     */

    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获得App版本名称
     *
     * @param context
     * @return
     */

    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "获得版本错误";
        }
    }

    /**
     * 获取重定向地址
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static String getRedirectUrl(String path) {
        String url = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setInstanceFollowRedirects(false);
            conn.setConnectTimeout(5000);
            url = conn.getHeaderField("Location");
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * 获得设备类型
     *
     * @param context
     * @return
     */

    public static String getClientType(Context context) {
        if (isTablet(context)) {
            return "PAD";
        } else {
            return "Phone";
        }
    }

    private static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;

    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前进程的名字
     *
     * @param context
     * @return
     */
    public static String getCurrentProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 获取无线mac地址 不连wifi无法获取
     *
     * @return
     */
    public static String getMac() {
        String macSerial = null;
        String str = "";

        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        return macSerial;
    }

    /**
     * g获取无线mac地址 不连wifi也可获取
     *
     * @param context
     * @return
     */
    public static String getWIFIMac(Context context) {
        String macAddress = "";
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
        if (null != info) {
            macAddress = info.getMacAddress();
        }
        return macAddress;
    }

    /**
     * 获取当前可用内存大小
     *
     * @return
     */
    public static String getAvailMemory(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        // mi.availMem; 当前系统的可用内存
        return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
    }

    /**
     * 获取当前总内存
     *
     * @return
     */
    public static String getTotalMemory(Context context) {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;

        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

            arrayOfString = str2.split("//s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "/t");
            }

            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();

        } catch (IOException e) {
        }
        return Formatter.formatFileSize(context, initial_memory);// Byte转换为KB或者MB，内存大小规格化
    }

    /**
     * 检查应用程序是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

//    /**
//     * 获得设备号
//     *
//     * @param context
//     * @return
//     */
//    public static String getIMEI(Context context) {
//
//        try {
//            TelephonyManager telephonymanager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//            return telephonymanager.getDeviceId();
//        } catch (Exception e) {
//            return "";
//        }
//    }

//    /**
//     * 获取设置唯一标识, 以此标识来确认此设备
//     *
//     * @param context
//     * @return
//     */
//    public static String getDeviceId(Context context) {
//        String mac = getWIFIMac(context);
//        String imei = getIMEI(context);
//
//        if (mac.isEmpty()) {
//            mac = getInfo().replaceAll(" ", ".").replaceAll("_", ".").replaceAll("-", ".");
//        } else {
//            mac = mac.replaceAll(":", "");
//        }
//
//        if (imei.isEmpty()) {
//            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
//            imei = SharedUtil.get(context, "UUID", uuid).toString();
//        }
//
//        return "a" + mac + "_" + imei;
//    }

    /**
     * 判断链接网络是否是wifi
     */
    public static boolean isWifi(Context icontext) {
        Context context = icontext.getApplicationContext();
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info;
        if (connectivity != null) {
            info = connectivity.getAllNetworkInfo();
            if (info != null) {
                int forSize = info.length;
                for (int i = 0; i < forSize; i++) {
                    if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // 判断有无网络链接
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) MyApplication.getInstance()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * @Function：打电话
     */
    public static void daDianHua(Context context, String mobile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + mobile));
        context.startActivity(intent);
    }

//    /**
//     * @Function：发短信
//     */
//    public static void faDuanXin(Context context, String mobile) {
//        Uri uri = Uri.parse("smsto:" + mobile);
//        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
//        intent.putExtra("sms_body", "");
//        context.startActivity(intent);
//    }
//
//    /**
//     * @Function：发短信
//     */
//    public static void faDuanXin(Context context, String mobile, String msg) {
//        Uri uri = Uri.parse("smsto:" + mobile);
//        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
//        intent.putExtra("sms_body", msg);
//        context.startActivity(intent);
//    }

    /**
     * @Function：在浏览器中打开url
     */
    public static void OpenUrl(Context context, String url) {

        Intent intent = new Intent();
        //Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
//        intent.setClassName("com.android.browser","com.android.browser.BrowserActivity");  //调用默认浏览器,可以不写
//        intent.setClassName("com.uc.browser", "com.uc.browser.ActivityUpdate");//uc浏览器"  用其他浏览器打开时必须添加对应浏览器
//        intent.setClassName("com.opera.mini.android", "com.opera.mini.android.Browser");//opera 用其他浏览器打开时必须添加对应浏览器
//        intent.setClassName("com.tencent.mtt", "com.tencent.mtt.MainActivity");//qq浏览器 用其他浏览器打开时必须添加对应浏览器
        context.startActivity(intent);

        //其他浏览器
    }

    /**
     * 关闭软键盘
     */
    public static void closeKeyboard(Context context) {
        View view = ((Activity) context).getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static boolean keyboardIsOpen(Context context) {
        // ToastUtil.showToast(context, "键盘是否打开?"+((Activity)
        // context).getWindow().getAttributes().softInputMode==WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED);
        return ((Activity) context).getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED;
    }

    /**
     * 打开软键盘
     */
    public static void openKeyboard(Context context, View view) {
        InputMethodManager inputmanger = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputmanger.showSoftInput(view, InputMethodManager.SHOW_FORCED);

    }

    /**
     * 动态设置listView 的高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        int forSize = listAdapter.getCount();
        for (int i = 0; i < forSize; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /*
     * 设置gridview高度
     */
    public static void setGridViewHeight(GridView gridView) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        int forSize = listAdapter.getCount();
        for (int i = 0; i < forSize; i++) {
            View listItem = (View) listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0);
            if (totalHeight < listItem.getMeasuredHeight()) {
                totalHeight = listItem.getMeasuredHeight();
            }
        }
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        // if(listAdapter.getCount() % num > 0){
        params.height = totalHeight * 2;
        // }else{
        // params.height = totalHeight + (gridView.getVerticalSpacing() *
        // (listAdapter.getCount()/num - 1));
        // }
        gridView.setLayoutParams(params);
    }


    /**
     * Describution :修改下划线宽度
     * <p>
     * Author : DKjuan
     * <p>
     * Date : 2017/12/5 10:17
     **/
    public static void setIndicator(Context mContext, TabLayout tabs, int padding) {
        try {
            //拿到tabLayout的mTabStrip属性  
            Field mTabStripField = tabs.getClass().getDeclaredField("mTabStrip");
            mTabStripField.setAccessible(true);

            LinearLayout mTabStrip = (LinearLayout) mTabStripField.get(tabs);

            padding = ScreenUtil.dp2px(mContext, padding);
            int forSize = mTabStrip.getChildCount();
            for (int i = 0; i < forSize; i++) {
                View tabView = mTabStrip.getChildAt(i);

                //拿到tabView的mTextView属性  
                Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                mTextViewField.setAccessible(true);
                TextView mTextView = (TextView) mTextViewField.get(tabView);
                tabView.setPadding(0, 0, 0, 0);

                //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度  
                int width = 0;
                width = mTextView.getWidth();
                if (width == 0) {
                    mTextView.measure(0, 0);
                    width = mTextView.getMeasuredWidth();
                }

                //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的  
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                params.width = width;
                params.leftMargin = padding / 2;
                params.rightMargin = padding / 2;
                tabView.setLayoutParams(params);

                tabView.invalidate();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param activity 要判断的Activity
     * @return 是否在前台显示
     */
    public static boolean isForeground(Activity activity) {
        return isForeground(activity, activity.getClass().getName());
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context   Context
     * @param className 界面的类名
     * @return 是否在前台显示
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || StringUtil.isEmpty(className))
            return false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName()))
                return true;
        }
        return false;
    }

    /**
     * 返回当前的应用是否处于前台显示状态
     *
     * @param $packageName
     * @return
     */
    public static boolean isTopActivity(Context context, String $packageName) {
        //_context是一个保存的上下文
        ActivityManager __am = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> __list = __am.getRunningAppProcesses();
        if (__list.size() == 0) return false;
        for (ActivityManager.RunningAppProcessInfo __process : __list) {
            if (__process.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
                    __process.processName.equals($packageName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAPPALive(Context mcontext, String packageName) {
        Boolean isAppRunning = false;
        ActivityManager am = (ActivityManager) mcontext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(packageName) && info.baseActivity.getPackageName().equals(packageName)) {
                isAppRunning = true;
                //find it, break
                break;
            }
        }
        return isAppRunning;
    }

    /**
     * Describution : 复制到剪切板
     * <p>
     * Author : DKjuan
     * <p>
     * Date : 2018/5/17 13:22
     **/
    public static void CopyToClip(Context mContext, String text) {
        // 从API11开始android推荐使用android.content.ClipboardManager
        // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(text);
    }

    /**
     * 复制到剪切板
     */
    public static void CopyMessageToClip(Context context, String text) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("3DM APP", text);
        cm.setPrimaryClip(clipData);
    }
}
