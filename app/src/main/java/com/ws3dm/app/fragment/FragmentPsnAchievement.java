package com.ws3dm.app.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.PsnGameUserRankActivity;
import com.ws3dm.app.adapter.PsnAcheveListAdapter;
import com.ws3dm.app.adapter.PsnCupRankListAdapter;
import com.ws3dm.app.bean.PsnCupListBean;
import com.ws3dm.app.bean.PsnCupRankBean;
import com.ws3dm.app.bean.PsnGameAchBean;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.bean.mysteampsn.Psn;
import com.ws3dm.app.bean.mysteampsn.SteamPsn;
import com.ws3dm.app.databinding.ActivityPsnAchievementLayoutBinding;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.util.glide.GlideUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FragmentPsnAchievement extends BaseFragment {

    private int appid;
    private int psnid;
    private String gametitle;
    private ActivityPsnAchievementLayoutBinding mBind;

    public FragmentPsnAchievement newIntance(String game, int appid, int psnid) {
        FragmentPsnAchievement fragment = new FragmentPsnAchievement();
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        appid = arguments.getInt("appid");
        psnid = arguments.getInt("psnid");
        gametitle = arguments.getString("gametitle");

        if (psnid == 0) {
            ToastUtil.showToast(mContext, "PSN没有找到对应的游戏");
            View noFount = inflater.inflate(R.layout.nofound_achievement_layout, container, false);
            TextView tip = noFount.findViewById(R.id.tip);
            tip.setText("暂无Psn成就");
            return noFount;
        }
        mBind = DataBindingUtil.inflate(inflater, R.layout.activity_psn_achievement_layout, container, false);
        GlideUtil.loadImage(mContext,R.drawable.psn_achieve_bg,mBind.psnBg);
        mBind.rankRecycleView.setLayoutManager(new LinearLayoutManager(mContext));
        mBind.rankRecycleView.setLoadingMoreEnabled(false);
        mBind.rankRecycleView.setPullRefreshEnabled(false);
        mBind.rankRecycleView.setNestedScrollingEnabled(true);
        mBind.achieveRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mBind.achieveRecyclerview.setNestedScrollingEnabled(true);
        mBind.achieveRecyclerview.setPullRefreshEnabled(false);
        mBind.achieveRecyclerview.setLoadingMoreEnabled(false);
        return mBind.getRoot();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (psnid == 0) {
            return;
        }
        getPsnInfo();
        getPsnCupRank();
        showUserPsnRank();
    }

    private void showUserPsnRank() {
        getPsnCupList();
        mBind.moreRankList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PsnGameUserRankActivity.class);
                intent.putExtra("psnid", psnid);
                startActivity(intent);
            }
        });
        mBind.otherAchieve.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                psnMyAchieve = true;
                switchPsnButtonUI();
                getMyPsnCup();
            }
        });
        mBind.allAchieve.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                psnMyAchieve = false;
                switchPsnButtonUI();
                getPsnCupList();
            }
        });
    }

    /**
     * 默认未点击的按钮
     */
    private boolean psnMyAchieve = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    private void switchPsnButtonUI() {
        if (psnMyAchieve == true) {
            mBind.otherAchieve.setBackground(mContext.getDrawable(R.drawable.shape_bule_button));
            mBind.allAchieve.setBackground(null);
            mBind.allAchieve.setTextColor(Color.BLACK);
            mBind.otherAchieve.setTextColor(Color.WHITE);
        } else {
            mBind.otherAchieve.setBackground(null);
            mBind.allAchieve.setBackground(mContext.getDrawable(R.drawable.shape_bule_button));
            mBind.allAchieve.setTextColor(Color.WHITE);
            mBind.otherAchieve.setTextColor(Color.BLACK);
        }
    }


    /**
     * 判断是否绑定SteamPSN
     */
    private void getPsnInfo() {
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
                            updataPsnView(response.getData().getPsn());
                        } else {
                            ToastUtil.showToast(mContext, response.getMsg());
                        }
                    }
                });
    }

    private void updataPsnView(Psn psn) {
        if (psn.getIsbang() == 1) {
            // getPsnAchieveInfo();
            if (psnid == 0) {
                mBind.psnAchieveLayout.setVisibility(View.GONE);
                ToastUtil.showToast(mContext, "PSN没有找到对应的游戏");
                return;
            }
            getPsnAchieveInfo();
        } else {
            mBind.psnAchieveLayout.setVisibility(View.GONE);
            ToastUtil.showToast(mContext, "未绑定Psn账号");
        }
    }

    /**
     * 获取psn的成就信息
     */
    private void getPsnAchieveInfo() {
        UserDataBean userData = MyApplication.getUserData();
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(psnid + userData.uid + time + NewUrl.KEY);
        Map<String, Object> values = new HashMap<>();
        values.put("psn_prodid", psnid);
        values.put("uid", userData.uid);
        values.put("sign", sign);
        values.put("time", time);
        String json = new JSONObject(values).toString();
        Log.d("获取信息", json);
        OkHttpUtils.postString().content(json).url(NewUrl.GAME_PSN).build().execute(new Callback<PsnGameAchBean>() {
            @Override
            public PsnGameAchBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, PsnGameAchBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(PsnGameAchBean response) {
                if (response.getCode() == 1) {
                    updataPsnGameInfo(response.getData());
                } else {
                    ToastUtil.showToast(mContext, "获取游戏信息失败");
                }
            }

        });
    }

    private void updataPsnGameInfo(PsnGameAchBean.DataBean data) {
        GlideUtil.loadImage(mContext, data.getLitpic(), mBind.gameImg);
        mBind.gameTitle.setText(data.getTitle());
        mBind.platinum.setText(data.getPlatinum() + "");
        mBind.gold.setText(data.getGold() + "");
        mBind.silver.setText(data.getSilver() + "");
        mBind.copper.setText(data.getBronze() + "");
        mBind.progress.setProgress(data.getAchieve_percent());
        mBind.gameAchievePercent.setText(data.getAchieve_percent() + "%");
        mBind.achieveShow.setText(data.getAchieve_show() + "");
        mBind.achieveRank.setText(data.getAchieve_rank() + "");
        mBind.coupRank.setText(data.getCup_rank() + "");
    }

    private void getPsnCupRank() {
        String uid = MyApplication.getUserData().uid;
        long time = System.currentTimeMillis();
        String s = StringUtil.newMD5(psnid + uid + 3 + String.valueOf(time) + NewUrl.KEY);
        Map<String, Object> values = new HashMap<>();
        values.put("psn_prodid", psnid);
        values.put("uid", uid);
        values.put("pagesize", 3);
        values.put("time", time);
        values.put("sign", s);
        String json = new JSONObject(values).toString();
        OkHttpUtils.postString().content(json).url(NewUrl.GPSNCUPRANK).build().execute(new Callback<PsnCupRankBean>() {
            @Override
            public PsnCupRankBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, PsnCupRankBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(PsnCupRankBean response) {
                if (response.getCode() == 1) {
                    updataPsnCupRankList(response.getData());
                } else {
                    ToastUtil.showToast(mContext, response.getCode());
                }
            }
        });
    }

    private void updataPsnCupRankList(PsnCupRankBean.DataBean data) {
        PsnCupRankListAdapter psnCupRankListAdapter = new PsnCupRankListAdapter(mContext, R.layout.adapter_psn_cup_rank_nohour_list);
        mBind.rankRecycleView.setAdapter(psnCupRankListAdapter);
        psnCupRankListAdapter.clearAndAddList(data.getList());
    }

    /**
     * psn的奖杯
     */
    private void getPsnCupList() {
        String uid = MyApplication.getUserData().uid;
        long time = System.currentTimeMillis();
        String s = StringUtil.newMD5(psnid + String.valueOf(time) + NewUrl.KEY);
        Map<String, Object> values = new HashMap<>();
        values.put("psn_prodid", psnid);
        values.put("time", time);
        values.put("sign", s);
        String json = new JSONObject(values).toString();
        OkHttpUtils.postString().content(json).url(NewUrl.GPSNCUPLIST).build().execute(new Callback<PsnCupListBean>() {
            @Override
            public PsnCupListBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, PsnCupListBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(PsnCupListBean response) {
                if (response.getCode() == 1) {
                    updataPsnAcheveList(response.getData());
                } else {
                    ToastUtil.showToast(mContext, response.getCode());
                }
            }

        });
    }

    private void updataPsnAcheveList(PsnCupListBean.DataBean data) {
        PsnAcheveListAdapter psnAcheveListAdapter = new PsnAcheveListAdapter(mContext, R.layout.adapter_psn_acheve_list_item);
        mBind.achieveRecyclerview.setAdapter(psnAcheveListAdapter);
        psnAcheveListAdapter.clearAndAddList(data.getList());
    }

    /**
     * 我未获得的奖杯
     */
    private void getMyPsnCup() {
        String uid = MyApplication.getUserData().uid;
        long time = System.currentTimeMillis();
        String s = StringUtil.newMD5(psnid + uid + String.valueOf(time) + NewUrl.KEY);
        Map<String, Object> values = new HashMap<>();
        values.put("psn_prodid", psnid);
        values.put("uid", uid);
        values.put("time", time);
        values.put("sign", s);
        String json = new JSONObject(values).toString();
        OkHttpUtils.postString().content(json).url(NewUrl.GMYPSNCUPLIST).build().execute(new Callback<PsnCupListBean>() {
            @Override
            public PsnCupListBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, PsnCupListBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(PsnCupListBean response) {
                if (response.getCode() == 1) {
                    updataPsnAcheveList(response.getData());
                } else {
                    ToastUtil.showToast(mContext, response.getCode());
                }
            }
        });
    }

}
