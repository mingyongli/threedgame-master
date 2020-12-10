package com.ws3dm.app.fragment;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.SteamUserHourAdapter;
import com.ws3dm.app.adapter.SteamUserRankAdapter;
import com.ws3dm.app.bean.SteamUserAchieveBean;
import com.ws3dm.app.bean.SteamUserHourBean;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.databinding.FragmentSteamUserRankBinding;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FragmentSteamUserRank extends BaseFragment {

    private FragmentSteamUserRankBinding mBind;
    private Bundle arguments;
    private int appid;
    private String title;
    private SteamUserRankAdapter steamUserRankAdapter;
    private SteamUserHourAdapter steamUserHourAdapter;

    public FragmentSteamUserRank newIntance(String s, int appid) {
        FragmentSteamUserRank fragment = new FragmentSteamUserRank();
        Bundle bundle = new Bundle();
        bundle.putString("title", s);
        bundle.putInt("appid", appid);
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
        mBind = DataBindingUtil.inflate(inflater, R.layout.fragment_steam_user_rank, container, false);
        initView();
        return mBind.getRoot();
    }

    private void initView() {
        arguments = getArguments();
        appid = arguments.getInt("appid");
        title = arguments.getString("title");
        mBind.recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mBind.recyclerview.setPullRefreshEnabled(false);
        if ("成就榜".equals(title)) {
            getAchieveInfo();
        } else if ("时长榜".equals(title)) {
            getHourRankInfo();
        }
    }

    private void getHourRankInfo() {
        UserDataBean userData = MyApplication.getUserData();
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(appid + userData.uid + "100" + time + NewUrl.KEY);
        Map<String, Object> values = new HashMap<>();
        values.put("appid", appid);
        values.put("uid", userData.uid);
        values.put("pagesize", 100);
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
        steamUserHourAdapter = new SteamUserHourAdapter(mContext, R.layout.adapter_steam_hour_rank_item);
        mBind.recyclerview.setAdapter(steamUserHourAdapter);
        steamUserHourAdapter.clearAndAddList(data.getList());

    }


    private void getAchieveInfo() {
        UserDataBean userData = MyApplication.getUserData();
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(appid + userData.uid + "100" + time + NewUrl.KEY);
        Map<String, Object> values = new HashMap<>();
        values.put("appid", appid);
        values.put("uid", userData.uid);
        values.put("pagesize", 100);
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
                    updataAchList(response.getData());
                } else {
                    ToastUtil.showToast(mContext, response.getCode());
                }
            }

        });
    }

    private void updataAchList(SteamUserAchieveBean.DataBean data) {
        steamUserRankAdapter = new SteamUserRankAdapter(mContext, R.layout.adapter_steam_achieve_rank_item);
        mBind.recyclerview.setAdapter(steamUserRankAdapter);
        steamUserRankAdapter.clearAndAddList(data.getList());
    }
}
