package com.ws3dm.app.activity;

import android.content.Intent;
import android.view.View;

import com.ws3dm.app.R;
import com.ws3dm.app.adapter.GameAchievementViewPageAdapter;
import com.ws3dm.app.databinding.ActivityGameAchievementBinding;

import java.util.ArrayList;
import java.util.List;

public class GameAchievementActivity extends BaseActivity {

    private Intent intent;
    private int appid;
    private ActivityGameAchievementBinding mBind;
    private List<String> steam_psn;
    private GameAchievementViewPageAdapter achievementViewPageAdapter;
    private int psnid;

    @Override
    protected void init() {
        mBind = bindView(R.layout.activity_game_achievement);
        setSupportActionBar(mBind.toolbar);
        mBind.setHandler(this);
        intent = getIntent();
        String game = intent.getStringExtra("game");
        appid = intent.getIntExtra("appid", 0);
        psnid = intent.getIntExtra("psnid", 0);
        steam_psn = new ArrayList<>();
        steam_psn.add("steam");
        steam_psn.add("psn");
        achievementViewPageAdapter = new GameAchievementViewPageAdapter(getSupportFragmentManager());
        mBind.viewpager.setOffscreenPageLimit(1);
        mBind.viewpager.setAdapter(achievementViewPageAdapter);
        mBind.tablayout.setupWithViewPager(mBind.viewpager);
        achievementViewPageAdapter.addData(steam_psn, appid, psnid);
        if ("psn".equals(game)) {
            mBind.viewpager.setCurrentItem(1);
        }
        mBind.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

