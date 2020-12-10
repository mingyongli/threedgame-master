package com.ws3dm.app.activity;

import android.content.Intent;
import com.google.android.material.tabs.TabLayout;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.SteamGameUserViewpageAdapter;
import com.ws3dm.app.bean.SteamGameAchBean;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.databinding.ActivitySteamUserRankBinding;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SteamGameUserRankActivity extends BaseActivity {

    private ActivitySteamUserRankBinding mBind;
    private int appid;
    private SteamGameUserViewpageAdapter steamGameUserViewpageAdapter;
    private List<String> gameTitle;

    @Override
    protected void init() {
        mBind = bindView(R.layout.activity_steam_user_rank);
        Intent intent = getIntent();
        appid = intent.getIntExtra("appid", 0);
        getGameInfo();
        initView();
    }

    private void initView() {
        gameTitle = new ArrayList<>();
        gameTitle.add("成就榜");
        gameTitle.add("时长榜");
        mBind.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        steamGameUserViewpageAdapter = new SteamGameUserViewpageAdapter(getSupportFragmentManager());
        mBind.tablayout.setTabMode(TabLayout.MODE_FIXED);
        mBind.viewpager.setAdapter(steamGameUserViewpageAdapter);
        mBind.tablayout.setupWithViewPager(mBind.viewpager);
        steamGameUserViewpageAdapter.setData(gameTitle, appid);
    }

    private void getGameInfo() {
        UserDataBean userData = MyApplication.getUserData();
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(appid + userData.uid + time + NewUrl.KEY);
        Map<String, Object> values = new HashMap<>();
        values.put("appid", appid);
        values.put("uid", userData.uid);
        values.put("sign", sign);
        values.put("time", time);
        String json = new JSONObject(values).toString();
        Log.d("获取信息", json);
        OkHttpUtils.postString().content(json).url(NewUrl.GAME_STEAM).build().execute(new Callback<SteamGameAchBean>() {
            @Override
            public SteamGameAchBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, SteamGameAchBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(SteamGameAchBean response) {
                if (response.getCode() == 1) {
                    mBind.gameTitle.setText(response.getData().getTitle());
                } else {
                    ToastUtil.showToast(mContext, "获取游戏信息失败");
                }
            }

        });
    }

}
