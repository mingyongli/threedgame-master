package com.ws3dm.app.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.SteamGameUserRankActivity;
import com.ws3dm.app.adapter.SteamGameAcheveListAdapter;
import com.ws3dm.app.adapter.SteamUserHourAdapter;
import com.ws3dm.app.adapter.SteamUserRankAdapter;
import com.ws3dm.app.bean.SteamGameAchBean;
import com.ws3dm.app.bean.SteamGameAcheveListBean;
import com.ws3dm.app.bean.SteamUserAchieveBean;
import com.ws3dm.app.bean.SteamUserHourBean;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.bean.mysteampsn.Steam;
import com.ws3dm.app.bean.mysteampsn.SteamPsn;
import com.ws3dm.app.databinding.ActivitySteamAchievementLayoutBinding;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.util.glide.GlideUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FragmentSteamAchievement extends BaseFragment {

    private int appid;
    private int psnid;
    private String gametitle;
    private SteamUserRankAdapter steamUserRankAdapter;
    private SteamUserHourAdapter steamHourRankAdapter;
    private ActivitySteamAchievementLayoutBinding mBind;

    public FragmentSteamAchievement newIntance(String game, int appid, int psnid) {
        FragmentSteamAchievement fragment = new FragmentSteamAchievement();
        Bundle bundle = new Bundle();
        bundle.putInt("appid", appid);
        bundle.putInt("psnid", psnid);
        bundle.putString("gametitle", game);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        appid = arguments.getInt("appid");
        psnid = arguments.getInt("psnid");
        gametitle = arguments.getString("gametitle");

        if (appid == 0) {
            View noFount = inflater.inflate(R.layout.nofound_achievement_layout, container, false);
            TextView tip = noFount.findViewById(R.id.tip);
            tip.setText("暂无steam成就");
            ToastUtil.showToast(mContext, "STEAM没有找到对应的游戏");
            return noFount;
        }
        mBind = DataBindingUtil.inflate(inflater, R.layout.activity_steam_achievement_layout, container, false);
        mBind.rankRecycle.setLayoutManager(new LinearLayoutManager(mContext));
        mBind.rankRecycle.setPullRefreshEnabled(false);
        mBind.rankRecycle.setLoadingMoreEnabled(false);
        mBind.rankRecycle.setNestedScrollingEnabled(true);
        mBind.achieveRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mBind.achieveRecyclerview.setNestedScrollingEnabled(true);
        mBind.achieveRecyclerview.setPullRefreshEnabled(false);
        mBind.achieveRecyclerview.setLoadingMoreEnabled(false);
        return mBind.getRoot();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (appid == 0) {
            return;
        }
        getSteamInfo();
        getSteamAchievenRank();
        showUserRank();
    }

    private void showUserRank() {
        getGameAcheveList();
        mBind.moreRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SteamGameUserRankActivity.class);
                intent.putExtra("appid", appid);
                startActivity(intent);
            }
        });
        mBind.otherAchieve.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                steamMyAchieve = true;
                switchSteamButtonUI();
                getMyGameAcheveList();
            }
        });
        mBind.allAchieve.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                steamMyAchieve = false;
                switchSteamButtonUI();
                getGameAcheveList();
            }
        });
        mBind.steamAchieveText.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                steamHourRank = false;
                switchSteamTextUI();
                getSteamAchievenRank();
            }
        });
        mBind.steamHourText.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                steamHourRank = true;
                switchSteamTextUI();
                getSteamHourRank();
            }
        });
    }

    private boolean steamMyAchieve = false;
    private boolean steamHourRank = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    private void switchSteamButtonUI() {
        if (steamMyAchieve == true) {
            mBind.otherAchieve.setBackground(mContext.getDrawable(R.drawable.shape_bule_button));
            mBind.allAchieve.setBackground(null);
        } else {
            mBind.otherAchieve.setBackground(null);
            mBind.allAchieve.setBackground(mContext.getDrawable(R.drawable.shape_bule_button));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    private void switchSteamTextUI() {
        if (steamHourRank == true) {
            mBind.steamHourText.setTextColor(mContext.getColor(R.color.steam_text));
            mBind.steamAchieveText.setTextColor(mContext.getColor(R.color.white));
        } else {
            mBind.steamHourText.setTextColor(mContext.getColor(R.color.white));
            mBind.steamAchieveText.setTextColor(mContext.getColor(R.color.steam_text));
        }
    }

    /**
     * 判断是否绑定SteamPSN
     */
    private void getSteamInfo() {
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
                        return new Gson().fromJson(data, SteamPsn.class);
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("message", e.getMessage());
                    }

                    @Override
                    public void onResponse(SteamPsn response) {
                        if (response.getCode() == 1) {
                            updataSteamView(response.getData().getSteam());
                        } else {
                            ToastUtil.showToast(mContext, response.getMsg());
                        }
                    }
                });
    }


    private void updataSteamView(Steam steam) {
        if (steam.getIsbang() == 1) {
            if (appid == 0) {
                ToastUtil.showToast(mContext, "Steam没有找到对应的游戏");
                mBind.steamAchieveLayout.setVisibility(View.GONE);
                return;
            }
            getSteamAchieveInfo();
        } else {
            mBind.steamAchieveLayout.setVisibility(View.GONE);
            ToastUtil.showToast(mContext, "未绑定Steam账号");
        }
    }

    /**
     * 获取steam的成就信息
     */
    private void getSteamAchieveInfo() {
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
                    updataSteamGameInf(response.getData());

                } else {
                    ToastUtil.showToast(mContext, "获取游戏信息失败");
                }
            }

        });

    }

    private void updataSteamGameInf(SteamGameAchBean.DataBean data) {
        GlideUtil.loadImage(mContext, data.getLitpic(), mBind.gameImg);
        mBind.gameTitle.setText(data.getTitle());
        mBind.gameHour.setText("总时长:" + data.getGamehour());
        mBind.gameRecenthour.setText("两周内" + data.getRecenthour() + "");
        mBind.achieveShow.setText(data.getAchieve_show() + "");
        mBind.achieveRank.setText(data.getAchieve_rank() + "");
        mBind.hourRank.setText(+data.getHour_rank() + "");
        mBind.progress.setProgress(data.getAchieve_percent());
        mBind.gameAchievePercent.setText(data.getAchieve_percent() + "%");
    }

    /**
     * steam的成就排行榜信息
     */
    private void getSteamAchievenRank() {
        UserDataBean userData = MyApplication.getUserData();
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(appid + userData.uid + "3" + time + NewUrl.KEY);
        Map<String, Object> values = new HashMap<>();
        values.put("appid", appid);
        values.put("uid", userData.uid);
        values.put("pagesize", 3);
        values.put("time", time);
        values.put("sign", sign);
        String json = new JSONObject(values).toString();
        OkHttpUtils.postString().content(json).url(NewUrl.GSTACHIEVERANK).build().execute(new Callback<SteamUserAchieveBean>() {
            @Override
            public SteamUserAchieveBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, SteamUserAchieveBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(SteamUserAchieveBean response) {
                if (response.getCode() == 1) {
                    updataUserAchList(response.getData());
                } else {
                    ToastUtil.showToast(mContext, response.getCode());
                }
            }

        });
    }

    private void updataUserAchList(SteamUserAchieveBean.DataBean data) {
        steamUserRankAdapter = new SteamUserRankAdapter(mContext, R.layout.adapter_steam_achieve_rank_item);
        mBind.rankRecycle.setAdapter(steamUserRankAdapter);
        steamUserRankAdapter.clearAndAddList(data.getList());
    }

    /**
     * steam 的时长排行榜
     */
    private void getSteamHourRank() {
        UserDataBean userData = MyApplication.getUserData();
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(appid + userData.uid + "3" + time + NewUrl.KEY);
        Map<String, Object> values = new HashMap<>();
        values.put("appid", appid);
        values.put("uid", userData.uid);
        values.put("pagesize", 3);
        values.put("time", time);
        values.put("sign", sign);
        String json = new JSONObject(values).toString();
        OkHttpUtils.postString().content(json).url(NewUrl.GSTHOURRANK).build().execute(new Callback<SteamUserHourBean>() {
            @Override
            public SteamUserHourBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, SteamUserHourBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(SteamUserHourBean response) {
                if (response.getCode() == 1) {
                    updataUserHourList(response.getData());
                } else {
                    ToastUtil.showToast(mContext, response.getCode());
                }
            }


        });
    }

    private void updataUserHourList(SteamUserHourBean.DataBean data) {
        steamHourRankAdapter = new SteamUserHourAdapter(mContext, R.layout.adapter_steam_hour_rank_item);
        mBind.rankRecycle.setAdapter(steamHourRankAdapter);
        steamHourRankAdapter.clearAndAddList(data.getList());
    }

    /**
     * 获取steam 的全部成就
     */
    private void getGameAcheveList() {
        long time = System.currentTimeMillis();
        String s = StringUtil.newMD5(appid + String.valueOf(time) + NewUrl.KEY);
        Map<String, Object> values = new HashMap<>();
        values.put("appid", appid);
        values.put("time", time);
        values.put("sign", s);
        String json = new JSONObject(values).toString();
        OkHttpUtils.postString().content(json).url(NewUrl.GSTACHIEVELIST).build().execute(new Callback<SteamGameAcheveListBean>() {
            @Override
            public SteamGameAcheveListBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, SteamGameAcheveListBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(SteamGameAcheveListBean response) {
                if (response.getCode() == 1) {
                    updataSteamAchieveList(response.getData());
                }
            }

        });
    }

    private void updataSteamAchieveList(SteamGameAcheveListBean.DataBean data) {
        SteamGameAcheveListAdapter acheveListAdapter = new SteamGameAcheveListAdapter(mContext, R.layout.adapter_steam_acheve_list_item);
        mBind.achieveRecyclerview.setAdapter(acheveListAdapter);
        acheveListAdapter.clearAndAddList(data.getList());
    }

    /**
     * 获取steam的未获得的成就
     */
    private void getMyGameAcheveList() {
        UserDataBean userData = MyApplication.getUserData();
        long time = System.currentTimeMillis();
        String s = StringUtil.newMD5(appid + userData.uid + time + NewUrl.KEY);
        Map<String, Object> values = new HashMap<>();
        values.put("appid", appid);
        values.put("uid", userData.uid);
        values.put("time", time);
        values.put("sign", s);
        String json = new JSONObject(values).toString();
        OkHttpUtils.postString().content(json).url(NewUrl.GSTMYACHIEVELIST).build().execute(new Callback<SteamGameAcheveListBean>() {
            @Override
            public SteamGameAcheveListBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, SteamGameAcheveListBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(SteamGameAcheveListBean response) {
                if (response.getCode() == 1) {
                    updataMySteamAchieveList(response.getData());
                }
            }

        });
    }

    private void updataMySteamAchieveList(SteamGameAcheveListBean.DataBean data) {
        SteamGameAcheveListAdapter acheveListAdapter = new SteamGameAcheveListAdapter(mContext, R.layout.adapter_steam_acheve_list_item);
        mBind.achieveRecyclerview.setAdapter(acheveListAdapter);
        acheveListAdapter.clearAndAddList(data.getList());
    }


}
