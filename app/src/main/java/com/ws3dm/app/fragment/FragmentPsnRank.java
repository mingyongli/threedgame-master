package com.ws3dm.app.fragment;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.ws3dm.app.adapter.PsnCountRankAdapter;
import com.ws3dm.app.adapter.PsnPlatinumRankAdapter;
import com.ws3dm.app.adapter.PsnPriceRankAdapter;
import com.ws3dm.app.bean.PsnCountRankBean;
import com.ws3dm.app.bean.PsnPlatinumRankBean;
import com.ws3dm.app.bean.PsnPriceRankBean;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.databinding.FgPsnRankBinding;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 排行榜的Viewpage界面
 */
public class FragmentPsnRank extends BaseFragment {

    private FgPsnRankBinding mBind;
    private Bundle arguments;
    private String title;
    private PsnPriceRankAdapter priceRankAdapter;
    private PsnCountRankAdapter countRankAdapter;
    private PsnPlatinumRankAdapter platinumRankAdapter;

    public FragmentPsnRank newIntance(String s) {
        FragmentPsnRank fragment = new FragmentPsnRank();
        Bundle bundle = new Bundle();
        bundle.putString("title", s);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBind = DataBindingUtil.inflate(inflater, R.layout.fg_psn_rank, container, false);
        arguments = getArguments();
        title = arguments.getString("title");
        if ("总价榜".equals(title)) {
            initPriceView();
        } else if ("库存榜".equals(title)) {
            initCountView();
        } else if ("白金榜".equals(title)) {
            initPlatinumRankView();
        }
        return mBind.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if ("总价榜".equals(title)) {
            getPriceRank();
        } else if ("库存榜".equals(title)) {
            getCountRank();
        } else if ("白金榜".equals(title)) {
            getPlatinumRank();
        }
    }

    private void initPriceView() {
        mBind.changeTitle.setText("总价值");
        mBind.recyclerview.setPullRefreshEnabled(false);
        mBind.recyclerview.setLoadingMoreEnabled(false);
        priceRankAdapter = new PsnPriceRankAdapter(mContext, R.layout.item_psn_rank);
        mBind.recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mBind.recyclerview.setAdapter(priceRankAdapter);
    }

    private void initCountView() {
        mBind.changeTitle.setText("总库存");

        mBind.recyclerview.setPullRefreshEnabled(false);
        mBind.recyclerview.setLoadingMoreEnabled(false);
        countRankAdapter = new PsnCountRankAdapter(mContext, R.layout.item_psn_rank);
        mBind.recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mBind.recyclerview.setAdapter(countRankAdapter);

    }

    private void initPlatinumRankView() {
        mBind.changeTitle.setText("总白金");

        mBind.recyclerview.setPullRefreshEnabled(false);
        mBind.recyclerview.setLoadingMoreEnabled(false);
        platinumRankAdapter = new PsnPlatinumRankAdapter(mContext, R.layout.item_psn_rank);
        mBind.recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mBind.recyclerview.setAdapter(platinumRankAdapter);
    }

    /**
     * 获取总价排行榜
     */
    private void getPriceRank() {
        UserDataBean userData = MyApplication.getUserData();
        String uid = userData.uid;
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(uid + time + NewUrl.KEY);
        Map<String, Object> bean = new HashMap<>();
        bean.put("uid", uid);
        bean.put("time", time);
        bean.put("sign", sign);
        String s = JSONObject.toJSON(bean).toString();
        OkHttpUtils.postString().content(s).url(NewUrl.MY_PSN_PRICERANK).build().execute(new Callback<PsnPriceRankBean>() {
            @Override
            public PsnPriceRankBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, PsnPriceRankBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(PsnPriceRankBean response) {
                if (response.getCode() == 1) {
                    priceRankAdapter.clearAndAddList(response.getData().getList());

                } else {
                    ToastUtil.showToast(mContext, response.getMsg());
                }
            }

        });
    }

    /**
     * 获取库存排行版
     */
    private void getCountRank() {
        UserDataBean userData = MyApplication.getUserData();
        String uid = userData.uid;
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(uid + time + NewUrl.KEY);
        Map<String, Object> bean = new HashMap<>();
        bean.put("uid", uid);
        bean.put("time", time);
        bean.put("sign", sign);
        String s = JSONObject.toJSON(bean).toString();
        OkHttpUtils.postString().content(s).url(NewUrl.MY_PSN_COUNTRANK).build().execute(new Callback<PsnCountRankBean>() {
            @Override
            public PsnCountRankBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, PsnCountRankBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(PsnCountRankBean response) {
                if (response.getCode() == 1) {
                    countRankAdapter.appendList(response.getData().getList());
                } else {
                    ToastUtil.showToast(mContext, response.getMsg());
                }
            }

        });
    }

    private void getPlatinumRank() {
        UserDataBean userData = MyApplication.getUserData();
        String uid = userData.uid;
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(uid + time + NewUrl.KEY);
        Map<String, Object> bean = new HashMap<>();
        bean.put("uid", uid);
        bean.put("time", time);
        bean.put("sign", sign);
        String s = JSONObject.toJSON(bean).toString();
        OkHttpUtils.postString().content(s).url(NewUrl.MY_PSN_PLATINUMRANK).build().execute(new Callback<PsnPlatinumRankBean>() {
            @Override
            public PsnPlatinumRankBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, PsnPlatinumRankBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(PsnPlatinumRankBean response) {
                if (response.getCode() == 1) {
                    platinumRankAdapter.clearAndAddList(response.getData().getList());
                } else {
                    ToastUtil.showToast(mContext, response.getMsg());
                }
            }

        });
    }
}
