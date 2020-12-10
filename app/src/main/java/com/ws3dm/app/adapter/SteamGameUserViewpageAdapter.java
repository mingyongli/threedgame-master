package com.ws3dm.app.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ws3dm.app.fragment.FragmentSteamUserRank;

import java.util.ArrayList;
import java.util.List;

public class SteamGameUserViewpageAdapter extends FragmentPagerAdapter {

    private List<String> bean = new ArrayList<>();
    private int appid;

    public SteamGameUserViewpageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        FragmentSteamUserRank fragmentSteamUserRank = new FragmentSteamUserRank().newIntance(bean.get(position), appid);
        return fragmentSteamUserRank;
    }

    @Override
    public int getCount() {
        return bean.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return bean.get(position);
    }

    public void setData(List<String> gameTitle, int appid) {
        this.bean.addAll(gameTitle);
        this.appid = appid;
        notifyDataSetChanged();
    }
}
