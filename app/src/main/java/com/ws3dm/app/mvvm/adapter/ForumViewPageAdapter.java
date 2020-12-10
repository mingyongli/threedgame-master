package com.ws3dm.app.mvvm.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import com.ws3dm.app.fragment.FragmentForumRank;
import com.ws3dm.app.mvvm.view.fragment.ForumHotsFragment;
import com.ws3dm.app.mvvm.view.fragment.ForumSectionFragment;
import com.ws3dm.app.mvvm.view.fragment.MyForumFragment;

import java.util.ArrayList;
import java.util.List;

public class ForumViewPageAdapter extends FragmentPagerAdapter {

    private List<String> mdata = new ArrayList<>();

    public ForumViewPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            //我的版块
            return new MyForumFragment();
        } else if (position == 1) {
            //论坛版块
            return new ForumSectionFragment();
        } else if (position == 2) {
            //论坛热帖
            return new ForumHotsFragment();
        } else {
            return new Fragment();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mdata.get(position);
    }

    @Override
    public int getCount() {
        return mdata.size();
    }

    public void addData(List<String> title) {
        mdata.addAll(title);
        notifyDataSetChanged();
    }
}
