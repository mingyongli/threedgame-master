package com.ws3dm.app.activity;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import android.view.View;

import com.ws3dm.app.R;
import com.ws3dm.app.adapter.TabAdapter;
import com.ws3dm.app.databinding.AcGameGiftBinding;
import com.ws3dm.app.fragment.FragmentGift;
import com.ws3dm.app.util.AppUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Describution : 礼包界面，网游和手游共用
 * 
 * Author : DKjuan
 * 
 * Date : 2019/11/20 11:28
 **/
public class GameGiftActivity extends BaseActivity{

	private AcGameGiftBinding mBinding;
	private List<String> tabs = new ArrayList<>();//tablayout的标签数组
	private List<Fragment> fragments = new ArrayList<>();
	private static TabAdapter mTabAdapter;
	private int pageType;//0 网游礼包  1 手游礼包
	private String title[] = {"可领取", "已过期", "无剩余"};

	@Override
	protected void init() {
		mBinding = bindView(R.layout.ac_game_gift);
		mBinding.setHandler(this);

		initView();
	}

	//界面初始化
	private void initView() {
		pageType=getIntent().getIntExtra("pageType",0);
		tabs= Arrays.asList(title);

		for (int i=1;i<4;i++){
			Fragment mFragmentGift = new FragmentGift();
			Bundle bundle = new Bundle();
			bundle.putInt("pageType",pageType);//0 网游礼包  1 手游礼包
			bundle.putInt("giftType",i);//排行类型:1、可领取2、已过期 3、无剩余
			mFragmentGift.setArguments(bundle);
			fragments.add(mFragmentGift);
		}

		//设置TabLayout的模式
		mBinding.mTabLayout.setTabMode(TabLayout.MODE_FIXED);

//		//设置分割线
//		LinearLayout linearLayout = (LinearLayout) mBinding.mTabLayout.getChildAt(0);
//		linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//		linearLayout.setDividerDrawable(ContextCompat.getDrawable(this,
//				R.drawable.divider)); //设置分割线的样式
//		linearLayout.setDividerPadding(dip2px(10)); //设置分割线间隔
		mTabAdapter=new TabAdapter(getSupportFragmentManager(),fragments,tabs);
		mBinding.mViewPager.setAdapter(mTabAdapter);
		mBinding.mViewPager.setOffscreenPageLimit(tabs.size());
		mBinding.mTabLayout.setupWithViewPager(mBinding.mViewPager);

		mBinding.mTabLayout.post(new Runnable() {
			@Override
			public void run() {
				AppUtil.setIndicator(mContext,mBinding.mTabLayout,25);
			}
		});
	}

	public void clickHandler(View view) {
		switch (view.getId()) {
			case R.id.imgReturn:// 返回
				onBackPressed();
				break;
		}
	}
}
