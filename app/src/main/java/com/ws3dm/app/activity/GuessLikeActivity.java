package com.ws3dm.app.activity;

import android.content.Intent;
import android.os.Handler;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.squareup.okhttp.Request;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.GuessLikeListBean;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.sqlite.NewsFile;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.util.glide.GlideUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Describution :娱乐页面点击猜你喜欢的结果页,附加 日常安利
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2017/10/23 10:53
 **/

/**
 * 更多猜你喜欢
 */
public class GuessLikeActivity extends BaseActivity {
    private CommonRecyclerAdapter<GuessLikeListBean.DataBean.ListBean> mAdapter;
    private XRecyclerView recyclerview;
    private List<NewsBean> listData = new ArrayList<>();
    private NewsFile newsCollectFile;

    private int totalSize, mPage = 1, pageType;//pageType 0猜你喜欢 1日常安利
    private String type;
    private Toolbar toolbar;
    private TextView title;

    @Override
    protected void init() {
        setContentView(R.layout.ac_base_recyclerview);
        newsCollectFile = new NewsFile(this);
        initView();
    }

    //界面初始化
    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerview = (XRecyclerView) findViewById(R.id.recyclerview);
        title = findViewById(R.id.base_title);
        title.setText("专题推荐");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        recyclerview.setLayoutManager(new GridLayoutManager(mContext, 2));
        recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);
        mAdapter = new CommonRecyclerAdapter<GuessLikeListBean.DataBean.ListBean>(mContext, R.layout.adapter_guesslike_item) {
            @Override
            public void bindData(RecyclerViewHolder holder, int position, GuessLikeListBean.DataBean.ListBean item) {
                holder.setText(R.id.title, item.getTitle());
                GlideUtil.loadImage(mContext, item.getLitpic(), holder.getView(R.id.like_bg));
                holder.setText(R.id.time, "更新时间:"+TimeUtil.getFormatTimeSimple(item.getPubdate_at()));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, GuessNewsListActivity.class);
                        intent.putExtra("aid", item.getAid());
                        startActivity(intent);
                    }
                });
            }
        };
//        mAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View itemView, int position) {
//                mAdapter.getDataByPosition(position).setHavesee(1);
//                NewsBean news = mAdapter.getDataByPosition(position);
//                news.setSeeDate(TimeUtil.dateDayNow());
//                news.setType("娱乐");
//                newsCollectFile.addHistory(news);
//                Intent intent = new Intent(mContext, NewsActivity.class);
//                if (pageType == 1)
//                    intent.putExtra("isGongLue", 2);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("newsBean", news);
//                intent.putExtras(bundle);
//                startActivity(intent);

//            }
//        });

        recyclerview.setAdapter(mAdapter);
        recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        mPage = 1;
                        getGuessList(mPage);
                    }
                }, 50);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {

                    }
                }, 50);
            }
        });

        getGuessList(1);
    }

    private void getGuessList(int page) {
        long timeMillis = System.currentTimeMillis();
        String sign = StringUtil.newMD5(10 + String.valueOf(page) + timeMillis + NewUrl.KEY);
        Map<String, Object> map = new HashMap<>();
        map.put("pagesize", 10);
        map.put("page", page);
        map.put("time", timeMillis);
        map.put("sign", sign);
        String s = JSON.toJSON(map).toString();
        OkHttpUtils.postString().content(s).url(NewUrl.GUESSLIST).build().execute(new Callback<GuessLikeListBean>() {
            @Override
            public GuessLikeListBean parseNetworkResponse(com.squareup.okhttp.Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, GuessLikeListBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(GuessLikeListBean response) {
                if (response.getCode() == 1) {
                    showGuessList(response.getData());
                } else {
                    ToastUtil.showToast(mContext, response.getMsg() + response.getCode());
                }
            }

        });
    }

    private void showGuessList(GuessLikeListBean.DataBean data) {

        recyclerview.refreshComplete();

        if (mPage == 1) {
            mAdapter.clearAndAddList(data.getList());
        } else {
            mAdapter.appendList(data.getList());
        }
    }

//    public void obtainData() {
//        //获取数据
//        long time = System.currentTimeMillis();
//        String validate = type + "10" + mPage + time;//type 娱乐栏目编号全部时则传入0
//        String sign = StringUtil.MD5(validate);
//        JSONObject obj = new JSONObject();
//        try {
//            obj.put("type", type);
//            obj.put("pagesize", 10);
//            obj.put("page", mPage);
//            obj.put("time", time);
//            obj.put("sign", sign);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
////        mForumPresenter.getForumRank(obj.toString());
//        //同步请求
//        Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
//        NewsService.Api service = retrofit.create(NewsService.Api.class);
//        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
//        Call<NewsDigitalRespBean> call = service.getNewsENT(body);
//        call.enqueue(new Callback<NewsDigitalRespBean>() {
//            @Override
//            public void onResponse(Call<NewsDigitalRespBean> call, Response<NewsDigitalRespBean> response) {
//                Log.e("requestSuccess", "-----------------------" + response.body());
//                initData(response.body());
//            }
//
//            @Override
//            public void onFailure(Call<NewsDigitalRespBean> call, Throwable throwable) {
//                Log.e("requestFailure", throwable.getMessage() + "");
//                recyclerview.loadMoreError();
//            }
//        });
//    }

//    private void initData(NewsDigitalRespBean bean) {
//        recyclerview.refreshComplete();
//        if (bean == null || bean.getData().getList() == null)
//            return;
//        listData = bean.getData().getList();
//        if (mPage > 1) {
//            mAdapter.appendList(listData);
//        } else {
//            mAdapter.clearAndAddList(listData);
//        }
//        if (listData == null || listData.size() < 1) {
//            recyclerview.setNoMore(true);
//        } else {
//            mPage++;
//        }
//    }

//    public void obtainAnliData() {
//        //获取数据
//        long time = System.currentTimeMillis();
//        String validate = "10" + mPage + time;//type 娱乐栏目编号全部时则传入0
//        String sign = StringUtil.MD5(validate);
//        JSONObject obj = new JSONObject();
//        try {
//            obj.put("pagesize", 10);
//            obj.put("page", mPage);
//            obj.put("time", time);
//            obj.put("sign", sign);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
////        mForumPresenter.getForumRank(obj.toString());
//        //同步请求
//        Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
//        GameService.Api service = retrofit.create(GameService.Api.class);
//        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
//        Call<NewsDigitalRespBean> call = service.syanli(body);
//        call.enqueue(new Callback<NewsDigitalRespBean>() {
//            @Override
//            public void onResponse(Call<NewsDigitalRespBean> call, Response<NewsDigitalRespBean> response) {
//                Log.e("requestSuccess", "-----------------------" + response.body());
//                initAnliData(response.body());
//            }
//
//            @Override
//            public void onFailure(Call<NewsDigitalRespBean> call, Throwable throwable) {
//                Log.e("requestFailure", throwable.getMessage() + "");
//                recyclerview.loadMoreError();
//            }
//        });
//    }

//    private void initAnliData(NewsDigitalRespBean bean) {
//        recyclerview.refreshComplete();
//        if (bean == null || bean.getData().getList() == null)
//            return;
//        listData = bean.getData().getList();
//        if (mPage > 1) {
//            mAdapter.appendList(listData);
//        } else {
//            totalSize = bean.getData().getTotal();
//            mAdapter.clearAndAddList(listData);
//        }
//        if (totalSize == mAdapter.getItemCount()) {
//            recyclerview.setNoMore(true);
//        } else {
//            mPage++;
//        }
//    }

}
