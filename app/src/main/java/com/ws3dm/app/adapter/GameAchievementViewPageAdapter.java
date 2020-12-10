package com.ws3dm.app.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ws3dm.app.fragment.FragmentPsnAchievement;
import com.ws3dm.app.fragment.FragmentSteamAchievement;

import java.util.ArrayList;
import java.util.List;

public class GameAchievementViewPageAdapter extends FragmentPagerAdapter {
    private List<String> bean = new ArrayList<>();
    private int addAppid;
    private int psnid;

    public GameAchievementViewPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            FragmentSteamAchievement steamAchievement = new FragmentSteamAchievement().newIntance(bean.get(position), addAppid, psnid);
            return steamAchievement;
        } else if (position == 1) {
            FragmentPsnAchievement psnAchievement = new FragmentPsnAchievement().newIntance(bean.get(position), addAppid, psnid);
            return psnAchievement;
        }
        return null;
    }

    @Override
    public int getCount() {
        return bean.size();
    }

    public void addData(List<String> steam_psn, int appid, int psnid) {
        this.addAppid = appid;
        this.psnid = psnid;
        bean.addAll(steam_psn);
        notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Steam成就";
        } else if (position == 1) {
            return "PSN奖杯";
        }
        return null;
    }
}
