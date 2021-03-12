package com.ws3dm.app.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.view.ViewGroup;

import com.ws3dm.app.bean.HomeTabsDBBean;
import com.ws3dm.app.fragment.FragmentGonglue;
import com.ws3dm.app.fragment.FragmentHomePage;
import com.ws3dm.app.fragment.FragmentHomePageOther;
import com.ws3dm.app.fragment.FragmentNewsDigital;
import com.ws3dm.app.fragment.FragmentNewsENT;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : DKjuan:tablayout 标签适配器
 * <p>
 * Date :  2017/12/4  14:31
 */
public class TabAdapters extends FragmentPagerAdapter {
    private List<HomeTabsDBBean.HomeTabsData> tabsData = new ArrayList<>();
    public Fragment currentFragment;

    public TabAdapters(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }


    @Override
    public Fragment getItem(int position) {
        if (tabsData.get(position).getType() == 0) {
            if (tabsData.get(position).getCid() == 0) {
                return new FragmentHomePage();
            }
            if (tabsData.get(position).getCid() == 1) {
                //数码
                return new FragmentNewsDigital();
            } else if (tabsData.get(position).getCid() == 2) {
                //娱乐
                return new FragmentNewsENT();
            } else if (tabsData.get(position).getCid() == 3) {
                //攻略
                return new FragmentGonglue();
            } else {
                return new FragmentHomePageOther().newInstance(tabsData.get(position));
            }
        } else {
            return new FragmentHomePageOther().newInstance(tabsData.get(position));
        }
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        this.currentFragment = (Fragment) object;
        super.setPrimaryItem(container, 0, object);
    }

    @Override
    public int getCount() {
        return tabsData.size();
    }

    //显示标签上的文字
    @Override
    public CharSequence getPageTitle(int position) {
        return tabsData.get(position).getTitle();
    }

    public void setPage(List<HomeTabsDBBean.HomeTabsData> tabs) {
        tabsData.clear();
        tabsData.addAll(tabs);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return tabsData.get(position).hashCode();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}