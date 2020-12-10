package com.ws3dm.app.activity;


import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ws3dm.app.R;
import com.ws3dm.app.adapter.SteamPsnRankViewPageAdapter;
import com.ws3dm.app.databinding.ActivtiySteamRankBinding;
import com.ws3dm.app.util.TimeUtil;
import com.ws3dm.app.util.glide.GlideUtil;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.material.tabs.TabLayout.MODE_SCROLLABLE;

public class MySteamRankActivity extends BaseActivity {

    private ActivtiySteamRankBinding mBind;
    private SteamPsnRankViewPageAdapter adapter;

    @Override
    protected void init() {
        Window window=getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        mBind = bindView(R.layout.activtiy_steam_rank);
        setSupportActionBar(mBind.toolbar);
        mBind.setHandler(this);
        initView();
        mBind.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        GlideUtil.loadImage(mContext,R.drawable.steam_rank_head,mBind.userRankBg);
        mBind.dataOf.setText("截止至" + TimeUtil.dateDayNow());
        adapter = new SteamPsnRankViewPageAdapter(getSupportFragmentManager());
        mBind.steamRankTabLayout.setTabMode(MODE_SCROLLABLE);
        mBind.steamRankTabLayout.setupWithViewPager(mBind.steamRankViewPage);
        List<String> title = new ArrayList<>();
        title.add("总价榜");
        title.add("库存榜");
        title.add("时长榜");
        adapter.setData(title, 1);
        mBind.steamRankViewPage.setOffscreenPageLimit(3);
        mBind.steamRankViewPage.setAdapter(adapter);
    }
}
