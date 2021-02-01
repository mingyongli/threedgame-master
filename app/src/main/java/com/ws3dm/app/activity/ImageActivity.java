package com.ws3dm.app.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.provider.MediaStore;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.View;

import com.ws3dm.app.R;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.databinding.AcImageBinding;
import com.ws3dm.app.fragment.FragmentImage;
import com.ws3dm.app.util.AppUtil;
import com.ws3dm.app.util.FileUtil;
import com.ws3dm.app.util.SharedUtil;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;
import com.ws3dm.app.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Describution :单个图片展示
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2017/8/30 14:48
 **/
public class ImageActivity extends BaseActivity {
    private AcImageBinding mBinding;
    private int position;
    private String strTitle;
    String[] arrUrl = new String[]{};
    private List<Fragment> fragments = new ArrayList<>();
    private Boolean WRITE_PERMISSION = false;

    @Override
    protected void init() {
        mBinding = bindView(R.layout.ac_image);
        mBinding.setHandler(this);

        initView();
    }

    //界面初始化
    private void initView() {
        strTitle = getIntent().getStringExtra("title");
        ArrayList<String> list = getIntent().getStringArrayListExtra("url");
        arrUrl = (String[]) list.toArray(new String[list.size()]);
        String posiStr = getIntent().getStringExtra("position");
        if (list.size() == 0) {
            finish();
        }
        if (!StringUtil.isEmpty(posiStr))
            position = Integer.parseInt(posiStr);
        mBinding.txtTitle.setText(strTitle);
        int forSize = arrUrl.length;
        for (int i = 0; i < forSize; i++) {
            Fragment fragment = new FragmentImage();
            Bundle bundle = new Bundle();
            bundle.putString("url", arrUrl[i]);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        mBinding.txtPosition.setText(1 + "/" + arrUrl.length);
        mBinding.mViewPager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager()));
        mBinding.mViewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        mBinding.mViewPager.setCurrentItem(position);
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            position = arg0;
            mBinding.txtPosition.setText(arg0 + 1 + "/" + arrUrl.length);
            mBinding.mViewPager.setCurrentItem(arg0);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    }

    private class MyFragmentAdapter extends FragmentPagerAdapter {

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    public void clickHandler(View view) {
        switch (view.getId()) {
            case R.id.imgReturn:
                onBackPressed();
                break;
            case R.id.txtBg:
            case R.id.txtCancel:
                mBinding.txtBg.setVisibility(View.GONE);
                break;
            case R.id.txtSetup:
                finish();
                Uri packageURI = Uri.parse("package:" + "com.ws3dm.app");
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                startActivity(intent);
                break;
            case R.id.txtDown:// 保存

                checkAndRequestPermission();
                if (WRITE_PERMISSION) {
                    if (AppUtil.isNetworkConnected(this)) {
                        if (SharedUtil.getSharedPreferencesData(arrUrl[position]).equals("0")) {
                            new GetDataTask().execute(arrUrl[position]);
                            SharedUtil.setSharedPreferencesData(arrUrl[position], "1");
                        } else
                            ToastUtil.showToast(mContext, "已保存过此图片");
                    } else {
                        ToastUtil.showToast(mContext, "请确保网络开启");
                    }
                } else {
                    ToastUtil.showToast(mContext, "未获取读写权限");
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkAndRequestPermission() {
        List<String> lackedPermission = new ArrayList<String>();
        if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            WRITE_PERMISSION = true;
        }
        // 权限都已经有了，那么直接调用SDK
        if (lackedPermission.size() == 0) {
        } else {
            // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            requestPermissions(requestPermissions, 1024);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1024) {
            WRITE_PERMISSION = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //保存图片
    private class GetDataTask extends AsyncTask<String, Void, String[]> {
        String path = null;

        @Override
        protected String[] doInBackground(String... params) {
            Bitmap bm = FileUtil.getBitmapFromUrl(params[0]);
            path = MediaStore.Images.Media.insertImage(MyApplication.getInstance().getContentResolver(), bm, "3DM" + System.currentTimeMillis(), "来自于3DM");

            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            ToastUtil.showToast(mContext, "保存成功");
        }
    }
}