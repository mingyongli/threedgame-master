package com.ws3dm.app.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : DKjuan:tablayout 标签适配器
 * <p>
 * Date :  2017/12/4  14:31
 */
public class TabAdapter extends FragmentPagerAdapter {
	List<String> tabs = new ArrayList<>(); //标签名称
	List<Fragment> fragments = new ArrayList<>();
	public Fragment currentFragment;

	public TabAdapter(FragmentManager fm) {
		super(fm);
	}

	public TabAdapter(FragmentManager fm,List<Fragment> fragments,List<String> tabs) {
		super(fm);
		this.fragments=fragments;
		this.tabs=tabs;
	}
	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		this.currentFragment= (Fragment) object;
		super.setPrimaryItem(container, position, object);
	}
	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	//显示标签上的文字
	@Override
	public CharSequence getPageTitle(int position) {
		return tabs.get(position);
	}

	public void setPage(List<Fragment> fragments, List<String> tabs) {
		this.fragments=fragments;
		this.tabs=tabs;
		notifyDataSetChanged();
	}
}