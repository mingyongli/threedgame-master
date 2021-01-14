package com.ws3dm.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.GameBean;
import com.ws3dm.app.bean.SoftGameBean;
import com.ws3dm.app.mvp.model.RespBean.GameSYhotRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.GameService;
import com.ws3dm.app.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Describution : 热门手游
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2019/8/7 9:49
 **/
public class HotMGActivity extends BaseActivity {
    private CommonRecyclerAdapter<SoftGameBean> mAdapter;
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
        imgReturn = (LinearLayout) findViewById(R.id.imgReturn);
        mTitle = (TextView) findViewById(R.id.base_title);
        recyclerview = (XRecyclerView) findViewById(R.id.recyclerview);

        initView();
    }

    public void initView() {
        mTitle.setText("热门手游");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);

        recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);       //原来就注释掉的
        recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);

        mAdapter = new CommonRecyclerAdapter<SoftGameBean>(mContext, R.layout.adapter_hot_mg) {
            @Override
            public void bindData(RecyclerViewHolder holder, final int position, final SoftGameBean item) {
                holder.setImageByUrl(R.id.img_top, item.getLitpic());
                holder.setText(R.id.tv_title, item.getTitle());
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
                SoftGameBean originalBean = mAdapter.getDataByPosition(position);
                GameBean mGame = new GameBean();
                mGame.setAid(originalBean.getAid());
                mGame.setArcurl(originalBean.getArcurl());
                mGame.setShowtype(originalBean.getShowtype());
                Intent intent = new Intent(mContext, GameHomeActivity.class);
                Bundle bundle = new Bundle();
//						bundle.putSerializable("game",mGame);
//					bundle.putSerializable("game", finalGame);//数据太大，超出系统限制
                bundle.putString("str_game", JSON.toJSONString(mGame));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        recyclerview.setRefreshing(true);//refresh的监听处理中已经有加载数据的操作
    }

    public void obtainData() {
        //获取数据
        long time = System.currentTimeMillis();
        String sign = StringUtil.MD5("" + time);
        JSONObject obj = new JSONObject();
        try {
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
        Call<GameSYhotRespBean> call = service.syhot(body);
        call.enqueue(new Callback<GameSYhotRespBean>() {
            @Override
            public void onResponse(Call<GameSYhotRespBean> call, Response<GameSYhotRespBean> response) {
                Log.e("requestSuccess", "-----------------------" + response.body());
                initData(response.body());
            }

            @Override
            public void onFailure(Call<GameSYhotRespBean> call, Throwable throwable) {
                Log.e("requestFailure", throwable.getMessage() + "");
                recyclerview.loadMoreError();
            }
        });
    }

    public void initData(GameSYhotRespBean bean) {
        if (mPage > 1) {
            mAdapter.appendList(bean.getData().getList());
        } else {
            recyclerview.refreshComplete();
            mAdapter.clearAndAddList(bean.getData().getList());
            recyclerview.setNoMore(true);
        }
    }
	
}
