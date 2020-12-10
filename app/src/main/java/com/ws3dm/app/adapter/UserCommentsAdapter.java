package com.ws3dm.app.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ws3dm.app.fragment.FragmentCommentMessage;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UserCommentsAdapter extends FragmentPagerAdapter {
    private List<String> mdata = new ArrayList<>();

    public UserCommentsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public @NotNull Fragment getItem(int position) {
        return new FragmentCommentMessage().newIntance(mdata.get(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mdata.get(position);
    }

    @Override
    public int getCount() {
        return mdata.size();
    }

    public void setData(List<String> title) {
        mdata.addAll(title);
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return FragmentPagerAdapter.POSITION_NONE;
    }
}
