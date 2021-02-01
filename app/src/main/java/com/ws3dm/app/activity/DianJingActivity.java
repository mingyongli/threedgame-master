package com.ws3dm.app.activity;

import android.content.Intent;
import android.os.Handler;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.GameTestBean;
import com.ws3dm.app.mvp.model.RespBean.GameOLtestRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.GameService;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Describution : 电竞专题
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2019/8/7 9:49
 **/
public class DianJingActivity extends BaseActivity {
    private CommonRecyclerAdapter<GameTestBean> mAdapter;
    private LinearLayout imgReturn;
    private TextView mTitle;
    private XRecyclerView recyclerview;
    private int totalCount, mPage;
    private Toolbar toolbar;

    @Override
    protected void init() {
        setContentView(R.layout.ac_base_recyclerview);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mPage = 1;
        mTitle = (TextView) findViewById(R.id.base_title);
        recyclerview = (XRecyclerView) findViewById(R.id.recyclerview);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initView();
    }

    public void initView() {
        mTitle.setText("电竞专题");

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);

        recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);       //原来就注释掉的
        recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);

        mAdapter = new CommonRecyclerAdapter<GameTestBean>(mContext, R.layout.adapter_egame) {
            @Override
            public void bindData(RecyclerViewHolder holder, final int position, final GameTestBean item) {
                holder.setImageByUrl(R.id.imgCover, item.getLitpic());
                holder.setText(R.id.tv_name, item.getTitle());
                holder.setText(R.id.tv_reward, "冠军奖励：" + item.getReward());
                holder.setText(R.id.tv_place, "比赛地点：" + item.getPlace());
                holder.setText(R.id.tv_time, "比赛时间：" + TimeUtil.getTimeEN(item.getStart_date()) + " 至 " + TimeUtil.getTimeEN(item.getEnd_date()));
            }
        };
        recyclerview.setAdapter(mAdapter);
        recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        mPage = 1;
                        obtainData();
                    }
                }, 50);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        obtainData();
//                        mBinding.recyclerview.loadMoreComplete();
//						mAdapter.notifyDataSetChanged();
                    }
                }, 50);
            }
        });

        mAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Intent aggreement = new Intent(mContext, SingleWebActivity.class);
                aggreement.putExtra("title", mAdapter.getDataByPosition(position).getTitle());
                aggreement.putExtra("url", mAdapter.getDataByPosition(position).getWebviewurl());
                startActivity(aggreement);
            }
        });
        recyclerview.setRefreshing(true);//refresh的监听处理中已经有加载数据的操作
    }

    public void obtainData() {
        //获取数据
        long time = System.currentTimeMillis();
        String validate = "" + 10 + mPage + time;
        String sign = StringUtil.MD5(validate);
        JSONObject obj = new JSONObject();
        try {
            obj.put("pagesize", 10);
            obj.put("page", mPage);
            obj.put("time", time);
            obj.put("sign", sign);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//		mOriginalPresenter.getOrigauthor(obj.toString());//异步请求
        //同步请求
        Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
        GameService.Api service = retrofit.create(GameService.Api.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
        Call<GameOLtestRespBean> call = service.olesportzt(body);
        call.enqueue(new Callback<GameOLtestRespBean>() {
            @Override
            public void onResponse(Call<GameOLtestRespBean> call, Response<GameOLtestRespBean> response) {
                Log.e("requestSuccess", "-----------------------" + response.body());
                initData(response.body());
            }

            @Override
            public void onFailure(Call<GameOLtestRespBean> call, Throwable throwable) {
                Log.e("requestFailure", throwable.getMessage() + "");
                recyclerview.loadMoreError();
            }
        });
    }

    public void initData(GameOLtestRespBean bean) {
        if (mPage > 1) {
            mAdapter.appendList(bean.getData().getList());
        } else {
            recyclerview.refreshComplete();
            totalCount = bean.getData().getTotal();
            mAdapter.clearAndAddList(bean.getData().getList());
        }
        if (mAdapter.getItemCount() == totalCount) {
            recyclerview.setNoMore(true);
        } else {
            recyclerview.refreshComplete();
            mPage++;
        }
    }

}
