package com.ws3dm.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.MotionEvent;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.ws3dm.app.R;

import java.util.HashMap;


public abstract class BaseActivity extends AppCompatActivity {

    protected String TAG = "BaseActivity";
    private Dialog loadingDialog;

    protected abstract void init();

    protected Context mContext;

    protected boolean mIsRunning = true;

    protected static HashMap<String, Activity> mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏锁定
//        if (SharedUtil.getSharedPreferencesData("isNight").equals("0"))
        setTheme(R.style.DayTheme);
//        else
//            setTheme(R.style.NightTheme);
        TAG = this.getClass().getSimpleName();
        mContext = this;

        PushAgent.getInstance(mContext).onAppStart();
        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mIsRunning = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsRunning = true;
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    protected <T> T bindView(int layoutId) {
        return (T) DataBindingUtil.setContentView(this, layoutId);
    }

    protected void setSupportActionBarTitle(int resId) {
        getSupportActionBar().setTitle(getResources().getString(resId));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void setSupportActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void singleTask() {
        String className = this.getClass().getSimpleName();
        if (mActivity == null) {
            mActivity = new HashMap<>();
        }
        Activity obj = mActivity.get(className);
        if (obj != null) {
            obj.finish();
            mActivity.remove(className);
        }
        mActivity.put(className, this);
    }

    //    拦截触发2次点击
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isFastDoubleClick()) {
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    long lastClickTime;

    public boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        lastClickTime = time;
        return timeD <= 150;
    }

    //    拦截触发2次点击
    public void showLoading() {
//        View inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_loading_view, null);
//        loadingDialog = DialogHelper.CreateDialog(mContext, inflate);
//        loadingDialog.show();
    }

    public void closeLoading() {
//        loadingDialog.dismiss();
    }
}