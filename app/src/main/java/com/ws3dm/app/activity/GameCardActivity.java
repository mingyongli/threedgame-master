package com.ws3dm.app.activity;

import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.R;
import com.ws3dm.app.bean.NewUserInfo;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.bean.mysteampsn.Psn;
import com.ws3dm.app.bean.mysteampsn.Steam;
import com.ws3dm.app.bean.mysteampsn.SteamPsn;
import com.ws3dm.app.databinding.AcGamecardBinding;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.glide.GlideUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameCardActivity extends BaseActivity {

    private AcGamecardBinding mBinding;
    private long uid;

    @Override
    protected void init() {
        Window window=getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        mBinding = bindView(R.layout.ac_gamecard);
        mBinding.setHandler(this);
        initView();
        initData();
    }

    private void initData() {
        getMyInfo();
        getMySteamPsn();
    }

    private void initView() {
        GlideUtil.loadImage(mContext, R.drawable.gamecardheadbg, mBinding.bgCard);
        setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBinding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.setting:
                        startActivity(new Intent(mContext, GameCardSettingActivity.class));
                }
                return true;
            }
        });
        mBinding.steamLoginedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, STEAM_ID_Activity.class));
            }
        });
        mBinding.psnLoginedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, PSN_ID_Activity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_card_toolbar_item, menu);
        return true;
    }

    /**
     * 获取3dm的个人信息
     */
    private void getMyInfo() {
        UserDataBean userData = MyApplication.getUserData();
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(userData.uid + time + NewUrl.KEY);

        Map<String, Object> values = new HashMap<>();
        values.put("uid", userData.uid);
        values.put("sign", sign);
        values.put("time", time);
        String json = new JSONObject(values).toString();

        OkHttpUtils
                .postString()
                .url(NewUrl.MY_HOME)
                .content(json)
                .build()
                .execute(new Callback<NewUserInfo>() {
                    @Override
                    public NewUserInfo parseNetworkResponse(Response response) throws IOException {
                        String string = response.body().string();
                        return new Gson().fromJson(string, NewUserInfo.class);
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("message", e.getMessage());
                    }

                    @Override
                    public void onResponse(NewUserInfo response) {
                        updateView(response);
                    }

                });

    }

    private void updateView(NewUserInfo response) {
        uid = response.getData().getInfo().getUid();
        NewUserInfo.Info info = response.getData().getInfo();
        GlideUtil.loadImage(mContext, info.getAvatarstr(), mBinding.dmHeadImg);
        mBinding.dmName.setText(info.getNickname());
        mBinding.fans.setText(info.getFansi_total() + "");
        mBinding.guanzhu.setText(info.getFollow_total() + "");
        mBinding.shoucang.setText(info.getFavorite_total()+"");
    }

    /**
     * 获取两个卡片的信息
     */
    private void getMySteamPsn() {
        UserDataBean userData = MyApplication.getUserData();
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(userData.uid + time + NewUrl.KEY);
        Map<String, Object> values = new HashMap<>();
        values.put("uid", userData.uid);
        values.put("sign", sign);
        values.put("time", time);
        String json = new JSONObject(values).toString();
        Log.d("获取信息", json);
        OkHttpUtils
                .postString()
                .url(NewUrl.MY_STEAMPSN)
                .content(json)
                .build()
                .execute(new Callback<SteamPsn>() {
                    @Override
                    public SteamPsn parseNetworkResponse(Response response) throws IOException {
                        String data = response.body().string();
                        Log.e("xxxx", data);
                        return new Gson().fromJson(data, SteamPsn.class);
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("message", e.getMessage());
                    }

                    @Override
                    public void onResponse(SteamPsn response) {
                        updateCardView(response);
                    }
                });
    }

    private void updateCardView(SteamPsn response) {
        Psn psn = response.getData().getPsn();
        Steam steam = response.getData().getSteam();
        if (psn.getIsbang() == 0) {
            mBinding.psnNoLoginLayout.setVisibility(View.VISIBLE);
            mBinding.psnLoginedLayout.setVisibility(View.GONE);
            mBinding.bindPsn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mContext, BindPsnActivity.class));
                }
            });
        } else {
            mBinding.psnNoLoginLayout.setVisibility(View.GONE);
            mBinding.psnLoginedLayout.setVisibility(View.VISIBLE);
            GlideUtil.loadImage(mContext, psn.getPsn_avatarstr(), mBinding.psnUserImg);
            mBinding.psnGameprice.setText(psn.getGameprice() + "");
            mBinding.psnGamecount.setText(psn.getGamecount() + "");
            mBinding.psnUserName.setText(psn.getPsn_nickname());
            mBinding.masonry.setText(psn.getMasonry() + "");
            mBinding.gold.setText(psn.getGold() + "");
            mBinding.silver.setText(psn.getSilver() + "");
            mBinding.copper.setText(psn.getCopper() + "");
        }
        if (steam.getIsbang() == 0) {
            mBinding.steamNoLoginLayout.setVisibility(View.VISIBLE);
            mBinding.steamLoginedLayout.setVisibility(View.GONE);
            mBinding.bindSteam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, BindSteamActivity.class);
                    intent.putExtra("uid", String.valueOf(uid));
                    startActivity(intent);
                }
            });
        } else {
            mBinding.steamNoLoginLayout.setVisibility(View.GONE);
            mBinding.steamLoginedLayout.setVisibility(View.VISIBLE);
            GlideUtil.loadImage(mContext, steam.getSt_avatarstr(), mBinding.steamUserImg);
            mBinding.steamGameCount.setText(steam.getGamecount() + "");
            mBinding.steamGamePrice.setText(steam.getGameprice() + "");
            mBinding.steamGameHour.setText(steam.getGamehour() + "");
            mBinding.steamUserName.setText(steam.getSt_nickname());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
}
