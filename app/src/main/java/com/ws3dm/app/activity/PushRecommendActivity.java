package com.ws3dm.app.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.mvp.model.RespBean.NewsListRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.NewsService;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Describution : 特别推荐activity
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2018/5/5 11:13
 **/
public class PushRecommendActivity extends BaseActivity {
    private CommonRecyclerAdapter<NewsBean> mAdapter;
    private LinearLayout imgReturn;
    private TextView mTitle;
    private XRecyclerView recyclerview;
    private List<NewsBean> listData = new ArrayList<>();
    private Toolbar toolbar;

    @Override
    protected void init() {
        setContentView(R.layout.ac_base_recyclerview);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitle = findViewById(R.id.base_title);
        recyclerview = findViewById(R.id.recyclerview);

        initView();
    }

    public void initView() {
        mTitle.setText("推送列表");
//		mNewsFile= new NewsFile(mContext);
//		listData=mNewsFile.queryPush();

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);

        recyclerview.setPullRefreshEnabled(false);

        mAdapter = new CommonRecyclerAdapter<NewsBean>(mContext, R.layout.adapter_news_one_right_pic) {
            @Override
            public void bindData(RecyclerViewHolder holder, final int position, final NewsBean item) {
//				holder.setText(R.id.tv_time, item.getType());
//				holder.setText(R.id.tv_name, item.getUser().nickname);
//				holder.setImageByUrl(R.id.img_head,item.getUser().avatarstr);
                holder.setText(R.id.txtTitle, item.getTitle());
                holder.setText(R.id.txtComment, item.getTotal_ct() + "");
                holder.setImageByUrl(R.id.imgArrayOne, item.getLitpic());
                holder.setText(R.id.tv_time_game, TimeUtil.getTimeEN(item.getPubdate_at()));
                holder.getView(R.id.tv_time_game).setVisibility(View.VISIBLE);
                holder.getView(R.id.img_time_game).setVisibility(View.VISIBLE);
                holder.getView(R.id.ll_user).setVisibility(View.GONE);
            }
        };
        recyclerview.setAdapter(mAdapter);

        mAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                NewsBean news = mAdapter.getDataByPosition(position);
                Intent intent = new Intent(mContext, NewsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("newsBean", news);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        obtainData();
    }

    private void obtainData() {
        long time = System.currentTimeMillis();
        String validate = "" + time;
        String sign = StringUtil.MD5(validate);
        JSONObject obj = new JSONObject();
        try {
            obj.put("time", time);
            obj.put("sign", sign);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //同步请求
        Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
        NewsService.Api service = retrofit.create(NewsService.Api.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
        Call<NewsListRespBean> call = service.getPushNews(body);
        call.enqueue(new Callback<NewsListRespBean>() {
            @Override
            public void onResponse(Call<NewsListRespBean> call, Response<NewsListRespBean> response) {
                Log.e("requestSuccess", "-----------------------" + response.body());
                if (response.body() != null || response.body() != null || response.body().getData().getList() != null) {
                    listData = response.body().getData().getList();
                    mAdapter.clearAndAddList(listData);
                }
            }

            @Override
            public void onFailure(Call<NewsListRespBean> call, Throwable throwable) {
                Log.e("requestFailure", throwable.getMessage() + "");
            }
        });
    }

}
