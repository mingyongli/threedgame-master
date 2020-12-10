package com.ws3dm.app.activity;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import android.view.View;

import com.ws3dm.app.R;
import com.ws3dm.app.adapter.TabAdapter;
import com.ws3dm.app.databinding.AcGamerandlistBinding;
import com.ws3dm.app.fragment.FragmentGameRand;
import com.ws3dm.app.util.AppUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameRandListActivity extends BaseActivity{

	private AcGamerandlistBinding mBinding;
	private List<String> tabs = new ArrayList<>();//tablayout的标签数组
	private List<Fragment> fragments = new ArrayList<>();
	private static TabAdapter mTabAdapter;

	private String title[] = {"总排行", "新游排行", "新游期待榜"};

	@Override
	protected void init() {
		mBinding = bindView(R.layout.ac_gamerandlist);
		mBinding.setHandler(this);

		initView();
	}

	//界面初始化
	private void initView() {

		tabs= Arrays.asList(title);

		for (int i=1;i<4;i++){
			Fragment mFragmentNewsList = new FragmentGameRand();
			Bundle bundle = new Bundle();
			bundle.putInt("randType",i);//排行类型:1、总排行2、2019新游排行3、新游期待榜
			mFragmentNewsList.setArguments(bundle);
			fragments.add(mFragmentNewsList);
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
