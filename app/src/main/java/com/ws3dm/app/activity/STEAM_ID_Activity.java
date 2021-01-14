package com.ws3dm.app.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.BaseRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.MySteamCardListBean;
import com.ws3dm.app.bean.MySteamInfoBean;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.databinding.SteamIdLayoutBinding;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.util.glide.GlideUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.ws3dm.app.NewUrl.MY_STEAM_CARD;
import static com.ws3dm.app.NewUrl.MY_STEAM_CARD_LIST;

public class STEAM_ID_Activity extends BaseActivity {

    private SteamIdLayoutBinding mBinding;
    private UserDataBean userData;
    private int mCurrentPageSize = 10;
    private MyListAdapter adapter;
    private int mCurrentPage = 1;

    @Override
    protected void init() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        mBinding = bindView(R.layout.steam_id_layout);
        mBinding.setHandler(this);

        initView();
        initData();
        getSteamCardListInfo(mCurrentPage);
        initListener();
    }

    private void initListener() {
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        mBinding.recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
        mBinding.recyclerview.setNestedScrollingEnabled(true);
        mBinding.steamRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MySteamRankActivity.class);
                startActivity(intent);
            }
        });
        mBinding.recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        mCurrentPage = 1;
                        getSteamCardListInfo(mCurrentPage);
                    }
                }, 50);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        mCurrentPage++;
                        getSteamCardListInfo(mCurrentPage);
                    }
                }, 50);
            }
        });
        mBinding.recyclerview.setRefreshing(true);
    }

    private void initView() {
        setSupportActionBar(mBinding.toolbar);
        GlideUtil.loadImage(mContext, R.drawable.steam_rank_head, mBinding.titleBg);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBinding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.refresh) {
                    mCurrentPage = 1;
                    getSteamCardListInfo(mCurrentPage);
                }
                return true;
            }
        });
        adapter = new MyListAdapter(mContext, R.layout.item_steam_card_list);
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerview.setAdapter(adapter);

    }


    private void initData() {
        //获取用户的uuid
        userData = MyApplication.getUserData();
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(userData.uid + time + NewUrl.KEY);
        Map<String, Object> bean = new HashMap<>();
        bean.put("uid", userData.uid);
        bean.put("time", time);
        bean.put("sign", sign);
        String s = JSONObject.toJSON(bean).toString();
        Log.d("个人中心steam名片信息", s);
        OkHttpUtils.postString().url(MY_STEAM_CARD).content(s).build().execute(new Callback<MySteamInfoBean>() {
            @Override
            public MySteamInfoBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                Log.d(TAG, "parseNetworkResponse: " + string);
                return new Gson().fromJson(string, MySteamInfoBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onResponse(MySteamInfoBean response) {
                //请求回来之后更新ui
                updateView(response);
            }

        });
    }

    public void getSteamCardListInfo(int mCurrentPage) {
        if (userData == null) {
            userData = MyApplication.getUserData();
        }
        long time = System.currentTimeMillis();
        //第一次请求时的参数
        String sign = StringUtil.newMD5(userData.uid + mCurrentPageSize + mCurrentPage + time + NewUrl.KEY);
        Map<String, Object> bean = new HashMap<>();
        bean.put("uid", userData.uid);
        bean.put("pagesize", mCurrentPageSize);
        bean.put("page", mCurrentPage);
        bean.put("time", time);
        bean.put("sign", sign);
        String s = JSONObject.toJSON(bean).toString();
        OkHttpUtils.postString().url(MY_STEAM_CARD_LIST).content(s).build().execute(new Callback<MySteamCardListBean>() {
            @Override
            public MySteamCardListBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, MySteamCardListBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(MySteamCardListBean response) {
                if (response.getCode() == 1) {
                    updateRecycleList(response);
                } else {
                    ToastUtil.showToast(mContext, response.getCode() + response.getMsg());
                    mBinding.recyclerview.loadMoreComplete();
                }
            }
        });
    }

    private void updateRecycleList(MySteamCardListBean response) {
        mBinding.recyclerview.refreshComplete();
        if (mCurrentPage == 1) {
            adapter.clearAndAddList(response.getData().getList());
        } else {
            adapter.appendList(response.getData().getList());
        }
        mBinding.recyclerview.loadMoreComplete();
    }

    private void updateView(MySteamInfoBean response) {
        MySteamInfoBean.DataBean data = response.getData();
        GlideUtil.loadCircleImage(mContext, data.getSt_avatarstr(), mBinding.imgHead);
        mBinding.steamUserName.setText(data.getSt_nickname());
        mBinding.steamGameprice.setText("￥" + data.getGameprice());
        mBinding.steamGamecount.setText(data.getGamecount() + "款");
        mBinding.steamGametime.setText(data.getGamehour() + "/" + "H");
        mBinding.steamUserLevel.setText(data.getLevel() + "");
        mBinding.steamPricepercent.setText(data.getPricepercent() + "%");
        mBinding.steamCountpercent.setText(data.getCountpercent() + "%");
        mBinding.steamHourpercent.setText(data.getHourpercent() + "%");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.psn_toolbar_item, menu);
        return true;
    }

    class MyListAdapter extends BaseRecyclerAdapter<MySteamCardListBean.DataBean.ListBean> {

        public MyListAdapter(Context ctx, int layoutId) {
            super(ctx, layoutId);
        }

        @Override
        public void bindData(RecyclerViewHolder holder, int position, MySteamCardListBean.DataBean.ListBean item) {
            GlideUtil.loadImage(mContext, item.getLitpic(), holder.itemView.findViewById(R.id.steam_game_img));
            TextView game_title = holder.itemView.findViewById(R.id.steam_game_title);
            TextView game_hour = holder.itemView.findViewById(R.id.steam_game_hour);
            TextView game_recenthour = holder.itemView.findViewById(R.id.steam_game_recenthour);
            TextView game_achieve = holder.itemView.findViewById(R.id.steam_game_achieve_percent);
            ProgressBar achieve_progress = holder.itemView.findViewById(R.id.steam_achieve_progress);
            game_title.setText(item.getTitle());
            game_hour.setText(item.getGamehour() + "h");
            game_recenthour.setText(item.getRecenthour() + "h");
            game_achieve.setText(item.getAchieve_percent() + "%");
            achieve_progress.setProgress(item.getAchieve_percent());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, GameAchievementActivity.class);
                    intent.putExtra("appid", item.getAppid());
                    intent.putExtra("psnid", item.getPsn_prodid());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.with(getApplicationContext()).pauseRequests();
    }
}
