package com.ws3dm.app.activity;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import android.view.View;

import com.ws3dm.app.adapter.TabAdapter;
import com.ws3dm.app.fragment.FragmentCollectGames;
import com.ws3dm.app.fragment.FragmentCollectForum;
import com.ws3dm.app.fragment.FragmentCollectNews;
import com.ws3dm.app.util.AppUtil;
import com.ws3dm.app.R;
import com.ws3dm.app.databinding.AcCollectBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Describution :收藏页面
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2017/8/17 11:48
 **/
public class CollectActivity extends BaseActivity {

	private AcCollectBinding mBinding;

	private String[] title = {"新闻", "游戏","帖子"};
	private List<String> tabs = new ArrayList<>();//tablayout的标签数组

	private List<Fragment> fragments = new ArrayList<>();

	@Override
	protected void init() {
		mBinding = bindView(R.layout.ac_collect);
		mBinding.setHandler(this);

		initView();
	}

	private void initView() {
		initFragment();
		initTabColumn();
	}

	//  初始化Fragment
	private void initFragment() {
		tabs= Arrays.asList(title);
		int forSize=tabs.size();
		for (int i = 0; i <forSize; i++) {
			Fragment fragment=null;
			if (i ==0) {//新闻收藏
				fragment = new FragmentCollectNews();
			}else if (i ==1) {//游戏收藏fragment
				fragment = new FragmentCollectGames();
			}else{//礼包收藏
				fragment = new FragmentCollectForum();
			}
			Bundle bundle = new Bundle();
			fragment.setArguments(bundle);
			fragments.add(fragment);
		}
	}

	// 初始化Column栏目项
	private void initTabColumn() {
		//设置TabLayout的模式
		mBinding.mTabLayout.setTabMode(TabLayout.MODE_FIXED);

//		//设置分割线
//		LinearLayout linearLayout = (LinearLayout) mBinding.mTabLayout.getChildAt(0);
//		linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//		linearLayout.setDividerDrawable(ContextCompat.getDrawable(this,
//				R.drawable.divider)); //设置分割线的样式
//		linearLayout.setDividerPadding(dip2px(10)); //设置分割线间隔

		mBinding.mViewPager.setAdapter(new TabAdapter(getSupportFragmentManager(),fragments,tabs));
		mBinding.mViewPager.setOffscreenPageLimit(tabs.size());
		mBinding.mTabLayout.setupWithViewPager(mBinding.mViewPager);

//		viewpage禁止滑动
//		mBinding.mTabLayout.setOnTouchListener(new View.OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				switch (event.getAction()) {
//					case MotionEvent.ACTION_MOVE:
//						mBinding.mTabLayout.requestDisallowInterceptTouchEvent(true);
//						break;
//					case MotionEvent.ACTION_CANCEL:
//						mBinding.mTabLayout.requestDisallowInterceptTouchEvent(false);
//					default:
//						break;
//				}
//				return true;
//			}
//		});

		mBinding.mTabLayout.post(new Runnable() {
			@Override
			public void run() {
				//AppUtil.setIndicator(mContext,mBinding.mTabLayout,56);
			}
		});
	}

	public void clickHandler(View view) {
		switch (view.getId()) {
			// 返回
			case R.id.imgReturn:
				finish();
				break;
		}
	}
}