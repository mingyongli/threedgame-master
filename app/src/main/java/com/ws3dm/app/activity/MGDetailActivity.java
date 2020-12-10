package com.ws3dm.app.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ws3dm.app.R;
import com.ws3dm.app.bean.SoftGameBean;
import com.ws3dm.app.databinding.AcMgDetailBinding;
import com.ws3dm.app.fragment.FragmentCommentsMG;
import com.ws3dm.app.fragment.FragmentMGAround;
import com.ws3dm.app.fragment.FragmentMGInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : DKjuan: 手游详情页面 包含3个fragment：详情、评论、周边
 * <p>
 * Date :  2017/9/1  15:57
 */
public class MGDetailActivity extends BaseActivity {

	private AcMgDetailBinding mBinding;
	public static SoftGameBean mSoftGame;
	private int barPosition;

	private List<Fragment> fragments = new ArrayList<>();
	
	public static TextView mLanguage,mType,mVersion,mSize;
	public static ImageView mImage;

	@Override
	protected void init() {
		mBinding = bindView(R.layout.ac_mg_detail);
		mBinding.setHandler(this);

		initView();
	}

	private void initView() {
		if (getIntent().getSerializableExtra("mSoftGameBean") != null)
			mSoftGame = (SoftGameBean) getIntent().getSerializableExtra("mSoftGameBean");

		barPosition = 0;

		if(mSoftGame!=null){
			mVersion=mBinding.tvVersion;
			mSize=mBinding.tvSize;
			mLanguage=mBinding.tvLanguage;
			mType=mBinding.tvType;
			mImage=mBinding.imgGame;
			mBinding.tvName.setText(mSoftGame.getTitle());
//			mBinding.tvVersion.setText("版本："+mSoftGame.getSoft_ver()+" v");
//			mBinding.tvSize.setText("大小："+mSoftGame.getSoft_size()+"");
//			mLanguage.setText("语言：");
//			mType.setText("类型：");
//			GlideUtil.loadImage(mContext,mSoftGame.getLitpic(),mBinding.imgGame);
		}
		initFragment();
		setBar();
	}

	// 滑动栏
	private void setBar() {
		mBinding.lineDetail.setVisibility(View.INVISIBLE);
		mBinding.lineComment.setVisibility(View.INVISIBLE);
		mBinding.lineAround.setVisibility(View.INVISIBLE);
		mBinding.tvDetail.setTextColor(0xff555555);
		mBinding.tvComment.setTextColor(0xff555555);
		mBinding.tvAround.setTextColor(0xff555555);
		switch (barPosition) {
			case 0:
				mBinding.lineDetail.setVisibility(View.VISIBLE);
				mBinding.tvDetail.setTextColor(0xffe8433c);
				mBinding.idStickynavlayoutViewpager.setCurrentItem(0);
				break;
			case 1:
				mBinding.lineComment.setVisibility(View.VISIBLE);
				mBinding.tvComment.setTextColor(0xffe8433c);//0xffab2f2b可能是另一个眼色，没有用
				mBinding.idStickynavlayoutViewpager.setCurrentItem(1);
				break;
			case 2:
				mBinding.lineAround.setVisibility(View.VISIBLE);
				mBinding.tvAround.setTextColor(0xffe8433c);
				mBinding.idStickynavlayoutViewpager.setCurrentItem(2);
				break;
		}
	}

	//  初始化Fragment
	private void initFragment() {
		Fragment mFragmentMGInfo=new FragmentMGInfo();//手游详情
		Bundle bundleInfo = new Bundle();
		bundleInfo.putSerializable("mSoftGame",mSoftGame);
		mFragmentMGInfo.setArguments(bundleInfo);
		fragments.add(mFragmentMGInfo);

		Fragment mFragmentCommentsMG=new FragmentCommentsMG();//手游评论
		Bundle bundleComment = new Bundle();
		bundleComment.putSerializable("mSoftGame",mSoftGame);
		mFragmentCommentsMG.setArguments(bundleComment);
		fragments.add(mFragmentCommentsMG);

		Fragment mFragmentMGAround=new FragmentMGAround();//手游周边
		Bundle bundleAround = new Bundle();
		bundleAround.putSerializable("mSoftGame",mSoftGame);
		mFragmentMGAround.setArguments(bundleAround);
		fragments.add(mFragmentMGAround);

		mBinding.idStickynavlayoutViewpager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager()));
		mBinding.idStickynavlayoutViewpager.addOnPageChangeListener(new MyOnPageChangeListener());
		mBinding.idStickynavlayoutViewpager.setOffscreenPageLimit(4);
	}

	public void clickHandler(View view) {
		switch (view.getId()) {
			case R.id.imgReturn:
				onBackPressed();
				break;
			case R.id.rl_detail:
			case R.id.tv_detail:
				barPosition = 0;
				setBar();
				break;
			case R.id.rlComment:
			case R.id.tv_comment:
				barPosition = 1;
				setBar();
				break;
			case R.id.rl_around:
			case R.id.tv_around:
				barPosition = 2;
				setBar();
				break;
			case R.id.img_download:
				startActivity(new Intent(mContext, DownListActivity.class));
				break;
			case R.id.bt_downgame:
//				DownloadUtil.down(mContext,mSoftGame.getDownurl(),mSoftGame.getTitle()+"|"+mSoftGame.getLitpic());
				break;
			case R.id.txtAttention:
				// 0：没有收藏  1：已收藏
//				if (game != null) {
//					if (SharedUtil.getSharedPreferencesData("game" + game.getSeries_id()).equals("0")) {
//						gameCollectFile.add(game);
//						SharedUtil.setSharedPreferencesData("game" + game.getAid(), "1");
//						mBinding.txtAttention.setText("已收藏");
//					} else {
//						gameCollectFile.delete(game.getAid()+"");
//						SharedUtil.setSharedPreferencesData("game" + game.getAid(), "0");
//						mBinding.txtAttention.setText("收藏");
//					}
//				} else
//					ToastUtil.showToast(mContext,"网络异常");
				break;
		}
	}

	private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			barPosition = arg0;
			setBar();
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
}