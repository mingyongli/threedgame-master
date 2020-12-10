package com.ws3dm.app.fragment;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.SteamCountRankAdapter;
import com.ws3dm.app.adapter.SteamHourRankAdapter;
import com.ws3dm.app.adapter.SteamPriceRankAdapter;
import com.ws3dm.app.bean.SteamCountRankBean;
import com.ws3dm.app.bean.SteamHourRankBean;
import com.ws3dm.app.bean.SteamPriceRankBean;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.databinding.FgSteamRankBinding;
import com.ws3dm.app.util.StringUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 排行榜的viewpage的界面
 */
public class FragmentSteamRank extends BaseFragment {
    private Bundle bundle;
    private static String TAG = FragmentSteamRank.class.getSimpleName();
    private SteamHourRankAdapter hourAdapter;
    private SteamCountRankAdapter countAdapter;
    private SteamPriceRankAdapter priceAdapter;
    private FgSteamRankBinding mBind;

    public FragmentSteamRank newIntance(String s) {
        FragmentSteamRank fragment = new FragmentSteamRank();
        Bundle bundle = new Bundle();
        bundle.putString("title", s);
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
        mBind = DataBindingUtil.inflate(inflater, R.layout.fg_steam_rank, container, false);
        bundle = getArguments();
        String title = bundle.getString("title");
        mBind.recyclerview.setPullRefreshEnabled(false);
        mBind.recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        if ("总价榜".equals(title)) {
            initPriceView();
        } else if ("库存榜".equals(title)) {
            initCountView();
        } else if ("时长榜".equals(title)) {
            initHourView();
        }
        return mBind.getRoot();
    }

    private void initHourView() {
        hourAdapter = new SteamHourRankAdapter(mContext, R.layout.item_steam_rank);
        mBind.changeTitle.setText("总时长");
        mBind.recyclerview.setAdapter(hourAdapter);
        getMysthourrank();
    }

    private void initCountView() {
        mBind.changeTitle.setText("总数量");
        countAdapter = new SteamCountRankAdapter(mContext, R.layout.item_steam_rank);
        mBind.recyclerview.setAdapter(countAdapter);
        getMystcountrank();
    }

    private void initPriceView() {
        mBind.changeTitle.setText("总价值");
        priceAdapter = new SteamPriceRankAdapter(mContext, R.layout.item_steam_rank);
        mBind.recyclerview.setAdapter(priceAdapter);
        getMyStpricerank();
    }

    /**
     * steam的总价排行榜
     */
    public void getMyStpricerank() {
        UserDataBean userData = MyApplication.getUserData();
        String uid = userData.uid;
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(uid + time + NewUrl.KEY);
        Map<String, Object> bean = new HashMap<>();
        bean.put("uid", uid);
        bean.put("time", time);
        bean.put("sign", sign);
        String s = JSONObject.toJSON(bean).toString();
        Log.d(TAG, "getMyStpricerank: " + s);
        OkHttpUtils.postString().url(NewUrl.MY_ST_PRICERANK).content(s).build().execute(new Callback<SteamPriceRankBean>() {
            @Override
            public SteamPriceRankBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, SteamPriceRankBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(SteamPriceRankBean response) {
                priceAdapter.clearAndAddList(response.getData().getList());
            }
        });

    }


    /**
     * 库存排行榜
     */

    public void getMystcountrank() {
        UserDataBean userData = MyApplication.getUserData();
        String uid = userData.uid;
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(uid + time + NewUrl.KEY);
        Map<String, Object> bean = new HashMap<>();
        bean.put("uid", uid);
        bean.put("time", time);
        bean.put("sign", sign);
        String s = JSONObject.toJSON(bean).toString();
        Log.d(TAG, "getMyStpricerank: " + s);
        OkHttpUtils.postString().url(NewUrl.MY_ST_COUNTRANK).content(s).build().execute(new Callback<SteamCountRankBean>() {
            @Override
            public SteamCountRankBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, SteamCountRankBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(SteamCountRankBean response) {
                countAdapter.clearAndAddList(response.getData().getList());
            }
        });
    }

    /**
     * steam的时长排行榜
     */
    public void getMysthourrank() {
        UserDataBean userData = MyApplication.getUserData();
        String uid = userData.uid;
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(uid + time + NewUrl.KEY);
        Map<String, Object> bean = new HashMap<>();
        bean.put("uid", uid);
        bean.put("time", time);
        bean.put("sign", sign);
        String s = JSONObject.toJSON(bean).toString();
        Log.d(TAG, "getMysthourrank: " + s);
        OkHttpUtils.postString().url(NewUrl.MY_ST_HOURRANK).content(s).build().execute(new Callback<SteamHourRankBean>() {
            @Override
            public SteamHourRankBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, SteamHourRankBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(SteamHourRankBean response) {
                hourAdapter.clearAndAddList(response.getData().getList());
            }
        });
    }


}
