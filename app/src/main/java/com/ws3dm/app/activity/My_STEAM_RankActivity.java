//package com.ws3dm.app.activity;
//
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.alibaba.fastjson.JSONObject;
//import com.google.gson.Gson;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.Response;
//import com.ws3dm.app.MyApplication;
//import com.ws3dm.app.NewUrl;
//import com.ws3dm.app.R;
//import com.ws3dm.app.bean.SteamCountRankBean;
//import com.ws3dm.app.bean.SteamHourRankBean;
//import com.ws3dm.app.bean.SteamPriceRankBean;
//import com.ws3dm.app.bean.UserDataBean;
//import com.ws3dm.app.databinding.ActivtiySteamRankBinding;
//import com.ws3dm.app.util.StringUtil;
//import com.ws3dm.app.util.glide.GlideUtil;
//import com.zhy.http.okhttp.OkHttpUtils;
//import com.zhy.http.okhttp.callback.Callback;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class My_STEAM_RankActivity extends BaseActivity {
//    private String type;
//    private ActivtiySteamRankBinding mBind;
//    private SteamRankAdapter adapter;
//
//    @Override
//    protected void init() {
//        mBind = bindView(R.layout.activtiy_steam_rank);
//        mBind.setHandler(this);
//        initView();
//        getMyStpricerank();
//    }
//
//    private void initView() {
//        mBind.recyclerview.setPullRefreshEnabled(false);
//        mBind.recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
//        adapter = new SteamRankAdapter();
//        mBind.recyclerview.setAdapter(adapter);
//        mBind.pricerank.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getMyStpricerank();
//            }
//        });
//        mBind.countRank.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getMystcountrank();
//            }
//        });
//        mBind.hourRank.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getMysthourrank();
//            }
//        });
//    }
//
//    /**
//     * steam的总价排行榜
//     */
//    public void getMyStpricerank() {
//        UserDataBean userData = MyApplication.getUserData();
//        String uid = userData.uid;
//        long time = System.currentTimeMillis();
//        String sign = StringUtil.newMD5(uid + time + NewUrl.KEY);
//        Map<String, Object> bean = new HashMap<>();
//        bean.put("uid", uid);
//        bean.put("time", time);
//        bean.put("sign", sign);
//        String s = JSONObject.toJSON(bean).toString();
//        Log.d(TAG, "getMyStpricerank: " + s);
//        OkHttpUtils.postString().url(NewUrl.MY_ST_PRICERANK).content(s).build().execute(new Callback<SteamPriceRankBean>() {
//            @Override
//            public SteamPriceRankBean parseNetworkResponse(Response response) throws IOException {
//                String string = response.body().string();
//                return new Gson().fromJson(string, SteamPriceRankBean.class);
//            }
//
//            @Override
//            public void onError(Request request, Exception e) {
//
//            }
//
//            @Override
//            public void onResponse(SteamPriceRankBean response) {
//                showMyPriceRank(response.getData().getMy());
//            }
//
//        });
//    }
//
//
//    /**
//     * steam的库存排行榜
//     */
//    public void getMystcountrank() {
//        UserDataBean userData = MyApplication.getUserData();
//        String uid = userData.uid;
//        long time = System.currentTimeMillis();
//        String sign = StringUtil.newMD5(uid + time + NewUrl.KEY);
//        Map<String, Object> bean = new HashMap<>();
//        bean.put("uid", uid);
//        bean.put("time", time);
//        bean.put("sign", sign);
//        String s = JSONObject.toJSON(bean).toString();
//        Log.d(TAG, "getMyStpricerank: " + s);
//        OkHttpUtils.postString().url(NewUrl.MY_ST_COUNTRANK).content(s).build().execute(new Callback<SteamCountRankBean>() {
//            @Override
//            public SteamCountRankBean parseNetworkResponse(Response response) throws IOException {
//                String string = response.body().string();
//                return new Gson().fromJson(string, SteamCountRankBean.class);
//            }
//
//            @Override
//            public void onError(Request request, Exception e) {
//
//            }
//
//            @Override
//            public void onResponse(SteamCountRankBean response) {
//                showMyCountRank(response.getData().getMy());
//
//            }
//
//        });
//    }
//
//    /**
//     * steam的时长排行榜
//     */
//    public void getMysthourrank() {
//        UserDataBean userData = MyApplication.getUserData();
//        String uid = userData.uid;
//        long time = System.currentTimeMillis();
//        String sign = StringUtil.newMD5(uid + time + NewUrl.KEY);
//        Map<String, Object> bean = new HashMap<>();
//        bean.put("uid", uid);
//        bean.put("time", time);
//        bean.put("sign", sign);
//        String s = JSONObject.toJSON(bean).toString();
//        Log.d(TAG, "getMysthourrank: " + s);
//        OkHttpUtils.postString().url(NewUrl.MY_ST_HOURRANK).content(s).build().execute(new Callback<SteamHourRankBean>() {
//            @Override
//            public SteamHourRankBean parseNetworkResponse(Response response) throws IOException {
//                String string = response.body().string();
//                return new Gson().fromJson(string, SteamHourRankBean.class);
//            }
//
//            @Override
//            public void onError(Request request, Exception e) {
//
//            }
//
//            @Override
//            public void onResponse(SteamHourRankBean response) {
//                showMyHourRank(response.getData().getMy());
//
//            }
//        });
//    }
//
//    private void showMyPriceRank(SteamPriceRankBean.DataBean.MyBean my) {
//        TextView myPrice = mBind.myRank.findViewById(R.id.steam_price);
//        TextView ranking = mBind.myRank.findViewById(R.id.steam_ranking);
//        TextView steam_name = mBind.myRank.findViewById(R.id.steam_name);
//        TextView dm_name = mBind.myRank.findViewById(R.id.dm_name);
//        GlideUtil.loadImage(mContext, my.getSt_avatarstr(), mBind.myRank.findViewById(R.id.steam_head_img));
//        GlideUtil.loadImage(mContext, my.getAvatarstr(), mBind.myRank.findViewById(R.id.dm_head_img));
//        myPrice.setText(my.getGameprice() + "");
//        ranking.setText(my.getRank() + "");
//        steam_name.setText(my.getSt_nickname());
//        dm_name.setText(my.getNickname());
//    }
//
//    private void showMyCountRank(SteamCountRankBean.DataBean.MyBean my) {
//        TextView myPrice = mBind.myRank.findViewById(R.id.steam_price);
//        TextView ranking = mBind.myRank.findViewById(R.id.steam_ranking);
//        TextView steam_name = mBind.myRank.findViewById(R.id.steam_name);
//        TextView dm_name = mBind.myRank.findViewById(R.id.dm_name);
//        GlideUtil.loadImage(mContext, my.getSt_avatarstr(), mBind.myRank.findViewById(R.id.steam_head_img));
//        GlideUtil.loadImage(mContext, my.getAvatarstr(), mBind.myRank.findViewById(R.id.dm_head_img));
//        myPrice.setText(my.getGamecount() + "");
//        ranking.setText(my.getRank() + "");
//        steam_name.setText(my.getSt_nickname());
//        dm_name.setText(my.getNickname());
//    }
//
//    private void showMyHourRank(SteamHourRankBean.DataBean.MyBean my) {
//        TextView myPrice = mBind.myRank.findViewById(R.id.steam_price);
//        TextView ranking = mBind.myRank.findViewById(R.id.steam_ranking);
//        TextView steam_name = mBind.myRank.findViewById(R.id.steam_name);
//        TextView dm_name = mBind.myRank.findViewById(R.id.dm_name);
//        GlideUtil.loadImage(mContext, my.getSt_avatarstr(), mBind.myRank.findViewById(R.id.steam_head_img));
//        GlideUtil.loadImage(mContext, my.getAvatarstr(), mBind.myRank.findViewById(R.id.dm_head_img));
//        myPrice.setText(my.getGameprice() + "");
//        ranking.setText(my.getRank() + "");
//        steam_name.setText(my.getSt_nickname());
//        dm_name.setText(my.getNickname());
//    }
//}
//
//class SteamRankAdapter extends RecyclerView.Adapter<SteamRankAdapter.InnerHolder> {
//    List<SteamPriceRankBean.DataBean.ListBean> mData = new ArrayList<>();
//
//    @Override
//    public InnerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_steam_psn_rank_item, parent, false);
//
//        return new InnerHolder(inflate);
//    }
//
//    @Override
//    public void onBindViewHolder(InnerHolder holder, int position) {
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return 0;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
//    }
//
//
//    class InnerHolder extends RecyclerView.ViewHolder {
//
//        public InnerHolder(View itemView) {
//            super(itemView);
//        }
//    }
//}
