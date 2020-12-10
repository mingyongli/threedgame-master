package com.ws3dm.app.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
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
import com.ws3dm.app.bean.MyPsnCardListBean;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.bean.mysteampsn.MyPsnInfoBean;
import com.ws3dm.app.databinding.PsnIdLayoutBinding;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.util.glide.GlideUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PSN_ID_Activity extends BaseActivity {
    private PsnIdLayoutBinding mBinding;
    private int mCurrentPage = 1;
    private int pageSize = 10;
    private MyPsnGameListAdapter adapter;

    @Override
    protected void init() {
        Window window=getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        mBinding = bindView(R.layout.psn_id_layout);
        mBinding.setHandler(this);
        setSupportActionBar(mBinding.toolbar);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        GlideUtil.loadImage(mContext,R.drawable.psn_card_bg,mBinding.psnCardBg);
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        mBinding.recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
        mBinding.recyclerview.setNestedScrollingEnabled(true);
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new MyPsnGameListAdapter(mContext, R.layout.item_psn_card_list);
        mBinding.recyclerview.setAdapter(adapter);
        mBinding.recyclerview.setRefreshing(true);
    }

    private void initData() {
        getPsnInfo();
        getPsnGameList();
    }


    private void initListener() {
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
                    case R.id.refresh:
                        getPsnInfo();
                        mCurrentPage = 1;
                        getPsnGameList();
                        break;
                }
                return true;
            }
        });
        mBinding.psnUserRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, MyPSN_RankActivity.class));
            }
        });
        mBinding.recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        mCurrentPage++;
                        getPsnGameList();
                    }
                }, 50);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.psn_toolbar_item, menu);
        return true;
    }

    /**
     * 获取psn的信息
     * app4/mypsncard
     */
    public void getPsnInfo() {
        UserDataBean userData = MyApplication.getUserData();
        String uid = userData.uid;
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(uid + time + NewUrl.KEY);
        Map<String, Object> bean = new HashMap<>();
        bean.put("uid", uid);
        bean.put("time", time);
        bean.put("sign", sign);
        String s = JSON.toJSON(bean).toString();
        OkHttpUtils.postString().content(s).url(NewUrl.MY_PSN_CARD).build().execute(new Callback<MyPsnInfoBean>() {
            @Override
            public MyPsnInfoBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, MyPsnInfoBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(MyPsnInfoBean response) {
                if (response.getCode() == 0) {
                    ToastUtil.showToast(mContext, response.getMsg());
                } else if (response.getCode() == 1) {
                    updateCardInfo(response.getData());
                } else if (response.getCode() == -1) {
                    ToastUtil.showToast(mContext, response.getMsg());
                }
            }
        });

    }


    /**
     * 获取游戏列表
     * app4/mypsncardlist
     */
    private void getPsnGameList() {
        UserDataBean userData = MyApplication.getUserData();
        //每页10条数据
        String uid = userData.uid;
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(uid + pageSize + mCurrentPage + time + NewUrl.KEY);
        Map<String, Object> bean = new HashMap<>();
        bean.put("uid", uid);
        bean.put("pagesize", pageSize);
        bean.put("page", mCurrentPage);
        bean.put("time", time);
        bean.put("sign", sign);
        String s = JSON.toJSON(bean).toString();
        OkHttpUtils.postString().content(s).url(NewUrl.MY_PSN_CARD_LIST).build().execute(new Callback<MyPsnCardListBean>() {
            @Override
            public MyPsnCardListBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, MyPsnCardListBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(MyPsnCardListBean response) {
                mBinding.recyclerview.refreshComplete();
                mBinding.recyclerview.loadMoreComplete();
                if (response.getCode() == 1) {
                    updateList(response.getData());
                }
            }
        });
    }


    private void updateCardInfo(MyPsnInfoBean.DataBean data) {
        mBinding.psnUserName.setText(data.getPsn_nickname());
        mBinding.psnPrice.setText(data.getGameprice() + "");
        mBinding.psnCount.setText(data.getGamecount() + "");
        mBinding.psnTrophies.setText(data.getCupcount() + "");
        mBinding.priceUser.setText(data.getPricepercent() + "%");
        mBinding.countUser.setText(data.getCountpercent() + "%");
        mBinding.trophyUser.setText(data.getCuppercent() + "%");
        mBinding.masonry.setText(data.getPlatinum() + "");
        mBinding.silver.setText(data.getSilver() + "");
        mBinding.gold.setText(data.getGold() + "");
        mBinding.copper.setText(data.getBronze() + "");
        mBinding.psnUserLevel.setText(data.getLevel() + "");
        GlideUtil.loadImage(mContext, data.getPsn_avatarstr(), mBinding.psnUserImg);

        if (data.getIsauth() == 1) {
            //已认证
            mBinding.toCertification.setVisibility(View.GONE);
            GlideUtil.loadImage(mContext, R.drawable.psn_certification, mBinding.psnUserCertification);
        } else {
            //未认证
            mBinding.toCertification.setVisibility(View.VISIBLE);
            mBinding.toCertification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PsnCertification.class);
                    intent.putExtra("psnid", data.getPsn_nickname());
                    startActivity(intent);
                }
            });
            GlideUtil.loadImage(mContext, R.drawable.psn_uncertification, mBinding.psnUserCertification);
        }

    }


    private void updateList(MyPsnCardListBean.DataBean data) {
        if (mCurrentPage == 1) {
            adapter.clearAndAddList(data.getList());
        } else {
            adapter.appendList(data.getList());
        }
    }

    class MyPsnGameListAdapter extends BaseRecyclerAdapter<MyPsnCardListBean.DataBean.ListBean> {


        public MyPsnGameListAdapter(Context ctx, int layoutId) {
            super(ctx, layoutId);
        }

        @Override
        public void bindData(RecyclerViewHolder holder, int position, MyPsnCardListBean.DataBean.ListBean item) {
            TextView title = holder.itemView.findViewById(R.id.psn_game_title);
            TextView masonry = holder.itemView.findViewById(R.id.masonry);
            TextView gold = holder.itemView.findViewById(R.id.gold);
            TextView silver = holder.itemView.findViewById(R.id.silver);
            TextView copper = holder.itemView.findViewById(R.id.copper);
            TextView achieve = holder.itemView.findViewById(R.id.psn_achieve);
            ProgressBar progressBar = holder.itemView.findViewById(R.id.psn_achieve_progress);

            GlideUtil.loadImage(mContext, item.getLitpic(), holder.itemView.findViewById(R.id.psn_game_img));
            title.setText(item.getTitle());
            masonry.setText(item.getPlatinum() + "");
            gold.setText(item.getGold() + "");
            silver.setText(item.getSilver() + "");
            copper.setText(item.getBronze() + "");
            achieve.setText(item.getAchieve_percent() + "%");
            progressBar.setProgress(item.getAchieve_percent());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, GameAchievementActivity.class);
                    intent.putExtra("game", "psn");
                    intent.putExtra("psnid", item.getPsn_prodid());
                    intent.putExtra("appid", item.getAppid());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

}

