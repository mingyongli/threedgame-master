package com.ws3dm.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.BaseRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.GuessNewsListBean;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.util.glide.GlideUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuessNewsListActivity extends BaseActivity {

    private int aid;
    private int page = 1;
    private int pageSize = 10;
    private XRecyclerView recyclerView;
    private Adapter adapter;
    private ImageView title_bg;
    private Toolbar toolbar;

    @Override
    protected void init() {
        setContentView(R.layout.activity_guessnews_list);
        Intent intent = getIntent();
        aid = intent.getIntExtra("aid", 1);
        initView();
        getGuessInfo();
        getGuessNewsList();
    }

    private void initView() {
        title_bg = findViewById(R.id.title_img);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter = new Adapter(mContext, R.layout.adapter_guessnewslist_item);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setRefreshHeader(new CustomRefreshHeader(mContext));
//		recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        recyclerView.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);

        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        getGuessNewsList();
                    }
                }, 50);
            }

            @Override
            public void onLoadMore() {
                page++;
                new Handler().postDelayed(new Runnable() {

                    public void run() {
                        getGuessNewsList();
                    }
                }, 0);

            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setRefreshing(true);
    }

    private void getGuessInfo() {
        long timeMillis = System.currentTimeMillis();
        String sign = StringUtil.newMD5(aid + String.valueOf(timeMillis) + NewUrl.KEY);
        Map<String, Object> map = new HashMap<>();
        map.put("aid", aid);
        map.put("time", timeMillis);
        map.put("sign", sign);
        String s = JSON.toJSON(map).toString();
        OkHttpUtils.postString().content(s).url(NewUrl.GUESSDETAIL).build().execute(new Callback<detailInfo>() {
            @Override
            public detailInfo parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, detailInfo.class);
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(detailInfo response) {
                if (response.getCode() == 1) {
                    initDetial(response.getData().getInfo());
                } else {
                    ToastUtil.showToast(mContext, response.getCode() + response.getMsg());
                }
            }

        });
    }

    private void initDetial(detailInfo.DataBean.InfoBean info) {
        toolbar.setTitle(info.getTitle());
        GlideUtil.loadImage(mContext, info.getBgpic(), title_bg);
    }

    private void getGuessNewsList() {
        long timeMillis = System.currentTimeMillis();
        String sign = StringUtil.newMD5(aid + String.valueOf(pageSize) + page + timeMillis + NewUrl.KEY);
        Map<String, Object> map = new HashMap<>();
        map.put("aid", aid);
        map.put("pagesize", pageSize);
        map.put("page", page);
        map.put("time", timeMillis);
        map.put("sign", sign);
        String s = JSON.toJSON(map).toString();
        OkHttpUtils.postString().content(s).url(NewUrl.GUESSNEWS).build().execute(new Callback<GuessNewsListBean>() {
            @Override
            public GuessNewsListBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, GuessNewsListBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(GuessNewsListBean response) {
                if (response.getCode() == 1) {
                    updateList(response.getData().getList());
                } else {
                    ToastUtil.showToast(mContext, response.getCode() + response.getMsg());
                }
            }

        });
    }

    private void updateList(List<GuessNewsListBean.DataBean.ListBean> list) {
        recyclerView.refreshComplete();
        recyclerView.loadMoreComplete();
        if (page == 1) {
            adapter.clearAndAddList(list);
        } else {
            adapter.appendList(list);
        }
    }


}

class Adapter extends BaseRecyclerAdapter<GuessNewsListBean.DataBean.ListBean> {


    public Adapter(Context ctx, int layoutId) {
        super(ctx, layoutId);
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, GuessNewsListBean.DataBean.ListBean item) {
        holder.setText(R.id.title, item.getTitle());
        holder.setText(R.id.time, TimeUtil.getFormatTime(item.getPubdate_at()));
        GlideUtil.loadImage(mContext, item.getLitpic(), holder.getView(R.id.news_img));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsBean bean = new NewsBean();
                bean.setAid(item.getAid());
                bean.setArcurl(item.getArcurl());
                bean.setLitpic(item.getLitpic());
                bean.setWebviewurl(item.getWebviewurl());
                Intent intent = new Intent(mContext, NewsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("newsBean", bean);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }
}

class detailInfo {

    /**
     * code : 1
     * data : {"info":{"aid":2,"title":"分享每日最有趣、最无节操的内涵囧图，你的快乐源泉","bgpic":"https://img.3dmgame.com/uploads/images/thumbnews/20200918/1600412239_721443.jpg"}}
     * msg : 成功
     */

    private int code;
    /**
     * info : {"aid":2,"title":"分享每日最有趣、最无节操的内涵囧图，你的快乐源泉","bgpic":"https://img.3dmgame.com/uploads/images/thumbnews/20200918/1600412239_721443.jpg"}
     */

    private DataBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * aid : 2
         * title : 分享每日最有趣、最无节操的内涵囧图，你的快乐源泉
         * bgpic : https://img.3dmgame.com/uploads/images/thumbnews/20200918/1600412239_721443.jpg
         */

        private InfoBean info;

        public InfoBean getInfo() {
            return info;
        }

        public void setInfo(InfoBean info) {
            this.info = info;
        }

        public static class InfoBean {
            private int aid;
            private String title;
            private String bgpic;

            public int getAid() {
                return aid;
            }

            public void setAid(int aid) {
                this.aid = aid;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getBgpic() {
                return bgpic;
            }

            public void setBgpic(String bgpic) {
                this.bgpic = bgpic;
            }
        }
    }
}
