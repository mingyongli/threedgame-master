package com.ws3dm.app.activity;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;

import com.ws3dm.app.R;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.databinding.AcNewsPhotoBinding;
import com.ws3dm.app.fragment.FragmentNewsPhoto;
import com.ws3dm.app.util.AppUtil;
import com.ws3dm.app.util.FileUtil;
import com.ws3dm.app.util.SharedUtil;
import com.ws3dm.app.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Describution :展示新闻图片
 * 
 * Author : DKjuan
 * 
 * Date : 2017/9/23 16:38
 **/
public class NewsPhotoActivity extends CheckPermissionsActivity {
    private int position;
    private String strTitle;
    String[] arrUrl = new String[]{};
    private List<Fragment> fragments = new ArrayList<>();
//    private boolean isOpen;
    
    private AcNewsPhotoBinding mBinding;
    
    @Override
    protected void init() {
        mBinding=bindView(R.layout.ac_news_photo);
        mBinding.setHandler(this);

        initView();
    }

    private void initView(){
        strTitle = getIntent().getStringExtra("title");
        arrUrl = getIntent().getStringArrayExtra("url");
        position = getIntent().getIntExtra("position", 0);
        mBinding.txtTitle.setText(strTitle);
        mBinding.txtHintTitle.setText("3DM APP保存图片需要的“读写存储”权限您还没有开启，请允许该权限才能更好的体验APP功能!");
        int forSize=arrUrl.length;
        for (int i = 0; i < forSize; i++) {
            Fragment fragment = new FragmentNewsPhoto();
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

    public void clickHandler(View view) {
            switch (view.getId()) {
                // 保存
                case R.id.txtDown:
                    if (AppUtil.isNetworkConnected(this)) {
                        if (SharedUtil.getSharedPreferencesData(arrUrl[position]).equals("0")) {
//                            if (isOpen) {
                                new GetDataTask().execute(arrUrl[position]);
                                SharedUtil.setSharedPreferencesData(arrUrl[position], "1");
//                            } else {
//                                mBinding.txtBg.setVisibility(View.VISIBLE);
//                                mBinding.rlHint.setVisibility(View.VISIBLE);
//                            }
                        } else {
                            ToastUtil.showToast(mContext, "已保存过此图片");
                        }
                    } else{
                        ToastUtil.showToast(mContext,"请确保网络开启");
                    }
                    break;
                case R.id.imgReturn:
                    finish();
                    break;
                default:
                    break;
            }
    }

    private class GetDataTask extends AsyncTask<String, Void, String[]> {
        String path = null;

        @Override
        protected String[] doInBackground(String... params) {
            Bitmap bm = FileUtil.getBitmapFromUrl(params[0]);
            path = MediaStore.Images.Media.insertImage(MyApplication.getInstance().getContentResolver(), bm, "3DM", "来自于3DM");

            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            ToastUtil.showToast(mContext,"保存成功");
        }
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
    
    @Override
    protected void hasGetAllPermissions() {

    }
}
