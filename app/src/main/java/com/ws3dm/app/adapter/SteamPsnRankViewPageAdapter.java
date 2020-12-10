package com.ws3dm.app.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ws3dm.app.fragment.FragmentPsnRank;
import com.ws3dm.app.fragment.FragmentSteamRank;

import java.util.ArrayList;
import java.util.List;

public class SteamPsnRankViewPageAdapter extends FragmentPagerAdapter {

    private List<String> data = new ArrayList<>();
    private int type;

    public SteamPsnRankViewPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (type == 1) {
            //返回steam的viewpage
            FragmentSteamRank fragmentSteamRank = new FragmentSteamRank().newIntance(data.get(position));
            return fragmentSteamRank;
        } else if (type == 2) {
            FragmentPsnRank fragmentPsnRank = new FragmentPsnRank().newIntance(data.get(position));
            return fragmentPsnRank;
        }
        return null;

    }

    @Override
    public CharSequence getPageTitle(int position) {

        return data.get(position);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    public void setData(List<String> list, int type) {
        this.type = type;
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }
}
