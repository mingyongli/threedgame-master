package com.ws3dm.app.activity;

import android.view.View;

import com.ws3dm.app.R;
import com.ws3dm.app.adapter.SteamPsnRankViewPageAdapter;
import com.ws3dm.app.databinding.ActivtiyPsnRankBinding;
import com.ws3dm.app.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.material.tabs.TabLayout.MODE_SCROLLABLE;

public class MyPSN_RankActivity extends BaseActivity {
    private ActivtiyPsnRankBinding mBind;
    private SteamPsnRankViewPageAdapter adapter;

    @Override
    protected void init() {
        mBind = bindView(R.layout.activtiy_psn_rank);
        initView();
        initListener();
    }


    private void initView() {
        mBind.dataOf.setText("截止至"+ TimeUtil.dateDayNow());
        adapter = new SteamPsnRankViewPageAdapter(getSupportFragmentManager());
        setSupportActionBar(mBind.toolbar);
        mBind.tablayout.setTabMode(MODE_SCROLLABLE);
        mBind.tablayout.setupWithViewPager(mBind.viewpage);
        List<String> title = new ArrayList<>();
        title.add("总价榜");
        title.add("库存榜");
        title.add("白金榜");
        adapter.setData(title, 2);
        mBind.viewpage.setOffscreenPageLimit(3);
        mBind.viewpage.setAdapter(adapter);
    }

    private void initListener() {
        mBind.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
