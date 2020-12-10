package com.ws3dm.app.mvvm.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayout;
import com.ws3dm.app.R;
import com.ws3dm.app.databinding.FragmentForumBinding;
import com.ws3dm.app.fragment.BaseFragment;
import com.ws3dm.app.mvvm.adapter.ForumViewPageAdapter;
import com.ws3dm.app.mvvm.messageEvent.ConstantEvent;
import com.ws3dm.app.mvvm.messageEvent.MessageEvent;
import com.ws3dm.app.mvvm.view.activity.SectionSearchActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

/**
 * 论坛的主界面,里面有3个ViewPage
 */
public class ForumFragment extends BaseFragment {
    private FragmentForumBinding mBind;
    private ForumViewPageAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBind = DataBindingUtil.inflate(inflater, R.layout.fragment_forum, container, false);
        initView();
        initListener();
        return mBind.getRoot();
    }

    private void initView() {
        List<String> title = new ArrayList<>();
        title.add("我的论坛");
        title.add("论坛版块");
        title.add("论坛热帖");
        adapter = new ForumViewPageAdapter(getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mBind.tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mBind.tablayout.setupWithViewPager(mBind.viewpager);
        mBind.viewpager.setAdapter(adapter);
        adapter.addData(title);
    }

    private void initListener() {
        mBind.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SectionSearchActivity.class);
                
                startActivity(intent);
            }
        });
    }
}
