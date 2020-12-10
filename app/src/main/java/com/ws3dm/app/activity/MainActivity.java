package com.ws3dm.app.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ws3dm.app.Constant;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.R;
import com.ws3dm.app.bean.NewUserInfo;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.databinding.AcMainBinding;
import com.ws3dm.app.fragment.GameFragment;
import com.ws3dm.app.fragment.NewUserFragment;
import com.ws3dm.app.fragment.NewsFragment;
import com.ws3dm.app.fragment.OriginalFragment;
import com.ws3dm.app.mvp.model.RespBean.VersionRespBean;
import com.ws3dm.app.mvp.presenter.UserPresenter;
import com.ws3dm.app.mvvm.view.fragment.ForumFragment;
import com.ws3dm.app.service.JobHandleService;
import com.ws3dm.app.util.AppUtil;
import com.ws3dm.app.util.DialogHelper;
import com.ws3dm.app.util.EventBus;
import com.ws3dm.app.util.FileUtil;
import com.ws3dm.app.util.SharedUtil;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.util.UpdateUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Describution :主页面
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2017/8/16 13:30
 **/
public class MainActivity extends BaseActivity {

    private FragmentManager mFragmentManager;
    private NewsFragment mNewsFragment;//新闻
    private GameFragment mGameFragment;//游戏
    private OriginalFragment mOriginalFragment;//手游页
    //	private MGFragment mMGFragment;//手游页
    //private ForumFragment mForumFragment;//论坛
    private ForumFragment forumFragment;//新版论坛
    private NewUserFragment userFragment;
    //    private UserFragment mUserFragment;//用户
    private Fragment mCurrentFragment, mOldFragment;
    private long firstTime = 0;
    private Handler mHandler;
    private UserPresenter mUserPresenter;
    private int mTag;//标记当前fragment的ID
    /**
     * 弹出框
     */

    private PopupWindow popupWindow;
    public static AcMainBinding mBinding;
    private TextView tv_update, tv_ignore, tv_content;
    NewUserInfo.Info info;

    @Override
    protected void init() {
        mBinding = bindView(R.layout.ac_main);
        setSupportActionBar(mBinding.toolbar);
        mBinding.setHandler(this);
        initFragment();
        //增加后台进程保活
        getUserData();
        //startService(new Intent(this, LocalService.class));
        //startService(new Intent(this, RemoteService.class));
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });
        mBinding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.search) {
                    startActivity(new Intent(mContext, SearchActivity.class).putExtra("search", "NewsFragment"));
                }
                if (item.getItemId() == R.id.notification) {
                    if (MyApplication.getUserData() == null || !MyApplication.getUserData().loginStatue) {
                        startActivity(new Intent(mContext, LoginActivity.class));
                    } else {
                        startActivity(new Intent(mContext, RecommendActivity.class));
                    }
                }
                if (item.getItemId() == R.id.message) {
                    if (MyApplication.getUserData() == null || !MyApplication.getUserData().loginStatue) {
                        startActivity(new Intent(mContext, LoginActivity.class));
                    } else {
                        startActivity(new Intent(mContext, MessageActivity.class));
                    }
                }
                if (item.getItemId() == R.id.setting) {
                    mContext.startActivity(new Intent(mContext, SettingActivity.class));
                }
                if (item.getItemId() == R.id.edit_user_information) {
                    if (MyApplication.getUserData() == null || !MyApplication.getUserData().loginStatue) {
                        startActivity(new Intent(mContext, LoginActivity.class));
                    } else if (MyApplication.getUserData().mobile.length() == 0) {//第三方登陆，非绑定跳转绑定界面
                        Intent intent = new Intent(mContext, ForgetPassActivity.class);
                        intent.putExtra("type", 1);//1,绑定手机号，2修改密码，3忘记密码
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(mContext, SetupActivity.class);
                        intent.putExtra("lv", info.getUser_level() + "");
                        mContext.startActivity(intent);
                    }
                }
                return true;
            }

        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, JobHandleService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
               startForegroundService(intent);
            } else {
                startService(intent);
            }
        } else {
			//startService(new Intent(this, LocalService.class));
			//startService(new Intent(this, RemoteService.class));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //startForegroundService(new Intent(this, LocalService.class));
                //startForegroundService(new Intent(this, RemoteService.class));
            } else {
                //startService(new Intent(this, LocalService.class));
                //startService(new Intent(this, RemoteService.class));
            }
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
                        if (mUpdateRespBean.getCode() == 1 && Double.parseDouble(AppUtil.getVersionName(mContext))
                                < Double.parseDouble(mUpdateRespBean.getData().getVersion())) {
                            SharedUtil.setSharedPreferencesData("upContent", mUpdateRespBean.getData().getDescription());
                            showUpdateView(mBinding.content);
                        }
                        break;
                }
            }
        };

        mUserPresenter.setHandler(mHandler);
        UpDateLogic();
        delAPKLogic();

//		new Handler().postDelayed(new Runnable(){//三秒后检测是否安装内部apk
//			public void run() {
//				InnerLogic();
//			}
//		}, 3000);

        EventBus.getDefault().register(this);
    }

    public void UpDateLogic() {
//		if("1".equals(SharedUtil.getSharedPreferencesData("noUp"))){
//			return;
//		}
        new Handler().postDelayed(new Runnable() {//两秒后检测更新
            public void run() {
                long time = System.currentTimeMillis();
                String validate = "1" + time;
                String sign = StringUtil.MD5(validate);
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
        }, 2000);
    }

    public void delAPKLogic() {
        if ("1".equals(SharedUtil.getSharedPreferencesData("needRD"))) {
//			DownloadUtil.delAll();
            File downPath = new File(Environment.getExternalStorageDirectory(), "3DM/Download");
            if (downPath.exists()) {
                FileUtil.clearDir(downPath, null);
            }
            SharedUtil.setSharedPreferencesData("needRD", "0");
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);//设置新的intent

//		InnerLogic();//防止恶意跳转，同splashactivity
    }

//	public void InnerLogic(){
//		String innerTitle=null,innerUrl=null,innerImg=null;
//		Intent intent=getIntent();
//		if("1".equals(intent.getStringExtra("isWeb"))){
//			Uri uri=intent.getData();
//			if (uri != null) {
//				innerImg = uri.getQueryParameter("img");
//				innerUrl = uri.getQueryParameter("url");
//				innerTitle = uri.getQueryParameter("title");
//			}
//		}else{
//			innerTitle=getString(R.string.inner_title);
//			innerUrl=getString(R.string.inner_url);
//			innerImg=getString(R.string.inner_img);
//		}
//
//		if(StringUtil.isEmpty(innerUrl)||innerUrl.equals(SharedUtil.getSharedPreferencesData("has"))||!innerUrl.startsWith("http")){
//			return;
//		}
//		SharedUtil.setSharedPreferencesData("has",innerUrl);
////		DownloadUtil.down(mContext,innerUrl,innerTitle+"|"+innerImg);
//		//设置显示手游页
//		mOldFragment=mCurrentFragment;
//		mCurrentFragment = mOriginalFragment;
//
//		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//		if (mOldFragment != null && mOldFragment.isAdded()) {
//			mOldFragment.onPause();
//			ft.hide(mOldFragment);
//		}
//		if (mCurrentFragment.isAdded()) {
//			mCurrentFragment.onResume();
//			ft.show(mCurrentFragment);
//		} else {
//			ft.add(R.id.content, mCurrentFragment);
//		}
//		ft.commitAllowingStateLoss();
//		mBinding.tab2.setChecked(true);
//	}

    private void initFragment() {
        mTag = R.id.tab_0;
        mNewsFragment = new NewsFragment();
        mGameFragment = new GameFragment();
        mOriginalFragment = new OriginalFragment();
//		mMGFragment = new MGFragment();
        forumFragment = new ForumFragment();
        //  mUserFragment = new UserFragment();
        userFragment = new NewUserFragment();
        mCurrentFragment = mNewsFragment;

        mFragmentManager = getSupportFragmentManager();
        final FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.content, mNewsFragment).commit();
    }

    // 选择图片的弹窗
    public void showUpdateView(View v) {// takePhoto, selectPhoto, cancle
        View pubPop = LayoutInflater.from(this).inflate(R.layout.view_update, null);
        popupWindow = DialogHelper.createPopupWindow(this, pubPop, R.style.AnimBottom);
        tv_content = (TextView) pubPop.findViewById(R.id.txtContent);
        tv_content.setText(SharedUtil.getSharedPreferencesData("upContent"));
        tv_update = (TextView) pubPop.findViewById(R.id.tv_update);
        tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
//				mContext.startService(new Intent(mContext, DownLoadService.class));
                UpdateUtil.showDownloadDialog(mContext);
//				UpdateUtil.showTitleDownload(mContext);
            }
        });
        tv_ignore = (TextView) pubPop.findViewById(R.id.tv_ignore);
        tv_ignore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//				finish();
                popupWindow.dismiss();
                SharedUtil.setSharedPreferencesData("noUp", "1");
            }
        });
        DialogHelper.showPop(MainActivity.this, v, popupWindow, Gravity.CENTER, 0, 0);
    }

    private void reloadImg() {
        mBinding.img0.setBackgroundResource(R.drawable.good);
        mBinding.img1.setBackgroundResource(R.drawable.game);
        mBinding.img2.setBackgroundResource(R.drawable.lt);
        mBinding.img3.setBackgroundResource(R.drawable.yc2);
        mBinding.img4.setBackgroundResource(R.drawable.me);
    }

    //获取用户中心数据
    private void getUserData() {
        if (MyApplication.getUserData() == null || !MyApplication.getUserData().loginStatue) {
            return;
        } else {
            UserDataBean userData = MyApplication.getUserData();
            long time = System.currentTimeMillis();
            String sign = StringUtil.newMD5(userData.uid + time + NewUrl.KEY);

            Map<String, Object> values = new HashMap<>();
            values.put("uid", userData.uid);
            values.put("sign", sign);
            values.put("time", time);
            String json = new JSONObject(values).toString();

            OkHttpUtils
                    .postString()
                    .url(NewUrl.MY_HOME)
                    .content(json)
                    .build()
                    .execute(new Callback<NewUserInfo>() {

                        @Override
                        public NewUserInfo parseNetworkResponse(Response response) throws IOException {
                            String string = response.body().string();
                            return new Gson().fromJson(string, NewUserInfo.class);
                        }

                        @Override
                        public void onError(Request request, Exception e) {
                            Log.e("message", e.getMessage());
                        }

                        @Override
                        public void onResponse(NewUserInfo response) {
                            if (response.getCode() == 1) {
                                info = response.getData().getInfo();
                            }
                        }
                    });
            //签到
            OkHttpUtils.postString().content(json).url(NewUrl.SUB_SIGN).build().execute(new Callback<subSign>() {
                @Override
                public subSign parseNetworkResponse(Response response) throws IOException {

                    return new Gson().fromJson(response.body().string(), subSign.class);
                }

                @Override
                public void onError(Request request, Exception e) {

                }

                @Override
                public void onResponse(subSign response) {
                    if (response.getCode() == 1) {
                        ToastUtil.showToast(mContext, "完成每日签到");
                    }
                }
            });

        }

    }

    public void clickHandler(View view) {
        if (view.getId() == R.id.tab_4) {
            if (!MyApplication.getUserData().loginStatue) {//未登录
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                return;
            }
        }
        reloadImg();
        if (view.getId() == mTag) {
            switch (view.getId()) {
                case R.id.tab_0:
                    mNewsFragment.reTop();
                    mBinding.img0.setBackgroundResource(R.drawable.good2);
                    break;
                case R.id.tab_1:
                    mCurrentFragment = mGameFragment;
                    mBinding.img1.setBackgroundResource(R.drawable.game2);
                    break;
                case R.id.tab_2:
                    mCurrentFragment = mOriginalFragment;
                    mBinding.img2.setBackgroundResource(R.drawable.lt2);
                    break;
                case R.id.tab_3:
                    mCurrentFragment = forumFragment;
                    mBinding.img3.setBackgroundResource(R.drawable.yc1);
                    break;
                case R.id.tab_4:
                    mCurrentFragment = userFragment;
                    mBinding.img4.setBackgroundResource(R.drawable.me2);
                    break;
                default:
                    break;
            }
            return;
        } else {
            mOldFragment = mCurrentFragment;
            mTag = view.getId();
            switch (view.getId()) {
                case R.id.tab_0:
                    mCurrentFragment = mNewsFragment;
                    mBinding.img0.setBackgroundResource(R.drawable.good2);
                    break;
                case R.id.tab_1:
                    mCurrentFragment = mGameFragment;
                    mBinding.img1.setBackgroundResource(R.drawable.game2);
                    break;
                case R.id.tab_2:
                    mCurrentFragment = mOriginalFragment;
                    mBinding.img2.setBackgroundResource(R.drawable.lt2);
                    break;
                case R.id.tab_3:
                    mCurrentFragment = forumFragment;
                    mBinding.img3.setBackgroundResource(R.drawable.yc1);
                    break;
                case R.id.tab_4:
                    mCurrentFragment = userFragment;
                    mBinding.img4.setBackgroundResource(R.drawable.me2);
                    break;
                default:
                    break;
            }

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (mOldFragment != null && mOldFragment.isAdded()) {
                mOldFragment.onPause();
                ft.hide(mOldFragment);
            }
            if (mCurrentFragment.isAdded()) {
                mCurrentFragment.onResume();
                ft.show(mCurrentFragment);
            } else {
                ft.add(R.id.content, mCurrentFragment);
            }
            ft.commitAllowingStateLoss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserData();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//			if (mCurrentFragment.getId() == mNewsFragment.getId()) {//判断是否为第一个fragment
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 800) {                                         //如果两次按键时间间隔大于2秒，则不退出
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;//更新firstTime
                return true;
            } else {
                finish();
                //两次按键小于2秒时，退出应用
                //System.exit(0);
            }
//			} else {
//				mFragmentManager.beginTransaction().replace(R.id.content, mNewsFragment).commit();
//				mCurrentFragment = mNewsFragment;
//				mBinding.tab0.setChecked(true);
//				return true;
//			}
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        stopService(new Intent(this,JobHandleService.class));
        super.onDestroy();
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.UMENG_PUSH_NEWS)})
    public void postNews(NewsBean news) {
        Intent intent = new Intent(this, NewsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("newsBean", news);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}

class subSign {

    /**
     * code : 219
     * msg : 已签到成功
     */

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}