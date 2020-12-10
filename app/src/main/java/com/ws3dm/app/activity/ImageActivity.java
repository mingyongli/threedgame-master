package com.ws3dm.app.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.provider.MediaStore;
import android.provider.Settings;
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
import com.ws3dm.app.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Describution :单个图片展示
 * 
 * Author : DKjuan
 * 
 * Date : 2017/8/30 14:48
 **/
public class 	ImageActivity extends BaseActivity{
	private AcImageBinding mBinding;
	private int position;
	private String strTitle;
	String[] arrUrl = new String[]{};
	private List<Fragment> fragments = new ArrayList<>();
	
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
		arrUrl = (String[])list.toArray(new String[list.size()]);
		String posiStr= getIntent().getStringExtra("position");
		if(!StringUtil.isEmpty(posiStr))
			position = Integer.parseInt(posiStr);
		mBinding.txtTitle.setText(strTitle);
		int forSize=arrUrl.length;
		for (int i = 0; i <forSize; i++) {
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
				if (AppUtil.isNetworkConnected(this)) {
					if (SharedUtil.getSharedPreferencesData(arrUrl[position]).equals("0")) {
						new GetDataTask().execute(arrUrl[position]);
						SharedUtil.setSharedPreferencesData(arrUrl[position], "1");
					} else
						ToastUtil.showToast(mContext,"已保存过此图片");
				} else{
					ToastUtil.showToast(mContext,"请确保网络开启");
				}
				break;
		}
	}

//保存图片
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
}