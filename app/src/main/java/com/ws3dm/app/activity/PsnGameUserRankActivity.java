package com.ws3dm.app.activity;

import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.PsnCupRankListAdapter;
import com.ws3dm.app.bean.PsnCupRankBean;
import com.ws3dm.app.bean.PsnGameAchBean;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.databinding.ActivityPsnUserRankBinding;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PsnGameUserRankActivity extends BaseActivity {

    private int psnid;
    private ActivityPsnUserRankBinding mBind;
    private PsnCupRankListAdapter psnCupRankListAdapter;
    private Intent intent;

    @Override
    protected void init() {
        mBind = bindView(R.layout.activity_psn_user_rank);
        intent = getIntent();
        psnid = intent.getIntExtra("psnid", 0);
        initView();
        getPsnAchieveInfo();
        initData();
    }

    private void initData() {

        getPsnCupRank();
    }

    private void initView() {
        setSupportActionBar(mBind.toolbar);
        mBind.recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mBind.recyclerview.setPullRefreshEnabled(false);
        psnCupRankListAdapter = new PsnCupRankListAdapter(mContext, R.layout.adapter_psn_cup_rank_list);
        mBind.recyclerview.setAdapter(psnCupRankListAdapter);
        mBind.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

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
                    mBind.gameTitle.setText(response.getData().getTitle());
                } else {
                    ToastUtil.showToast(mContext, "获取游戏信息失败");
                }
            }

        });
    }

    private void getPsnCupRank() {
        String uid = MyApplication.getUserData().uid;
        long time = System.currentTimeMillis();
        String s = StringUtil.newMD5(psnid + uid + 10 + String.valueOf(time) + NewUrl.KEY);
        Map<String, Object> values = new HashMap<>();
        values.put("psn_prodid", psnid);
        values.put("uid", uid);
        values.put("pagesize", 10);
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
                    upAverage(response.getData().getAveragerate(), response.getData().getPlatinumrate());
                    psnCupRankListAdapter.clearAndAddList(response.getData().getList());
                } else {
                    ToastUtil.showToast(mContext, response.getCode());
                }
            }
        });
    }

    private void upAverage(int averagerate, int platinumrate) {
         mBind.userAverage.setText(averagerate+"%");
         mBind.cupAverage.setText(platinumrate+"%");

    }


}