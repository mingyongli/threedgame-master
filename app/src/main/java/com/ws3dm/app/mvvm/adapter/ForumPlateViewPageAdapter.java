package com.ws3dm.app.mvvm.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ws3dm.app.mvvm.view.fragment.ForumPlateContentFragment;

import java.util.ArrayList;
import java.util.List;

public class ForumPlateViewPageAdapter extends FragmentPagerAdapter {

    private List<Integer> mtype = new ArrayList<>();
    private String mPlateId;

    public ForumPlateViewPageAdapter(@NonNull FragmentManager fm, int behavior, String plateId, List<Integer> type) {
        super(fm, behavior);
        mPlateId = plateId;
        mtype.addAll(type);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new ForumPlateContentFragment().getInstant(mPlateId, mtype.get(position));
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "热门";
        } else if (position == 1) {
            return "最新";
        } else {
            return "精华";
        }
    }

    @Override
    public int getCount() {
        return mtype.size();
    }

}
