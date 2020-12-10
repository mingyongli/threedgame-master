package com.ws3dm.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.Constant;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.BaseRecyclerAdapter;
import com.ws3dm.app.adapter.LastMultiAdapter;
import com.ws3dm.app.adapter.LastMultiItemTypeSupport;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.bean.OriginalBean;
import com.ws3dm.app.mvp.model.RespBean.OrignewListRespBean;
import com.ws3dm.app.mvp.presenter.OriginalPresenter;
import com.ws3dm.app.sqlite.NewsFile;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;
import com.ws3dm.app.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Describution :最新原创列表
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2017/10/23 10:53
 **/
public class LastListActivity extends BaseActivity implements View.OnClickListener {
    private LastMultiAdapter mAdapter;
    private TextView mTitle;
    private XRecyclerView recyclerview;
    private int mPage, totalCount;
    private OriginalPresenter mOriginalPresenter;
    private Handler mHandler;
    private NewsFile newsCollectFile;
    private Toolbar toolbar;

    @Override
    protected void init() {
        setContentView(R.layout.ac_base_recyclerview);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTitle = (TextView) findViewById(R.id.base_title);
        recyclerview = (XRecyclerView) findViewById(R.id.recyclerview);


        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (!mIsRunning) {
                    return;
                }
                switch (msg.what) {
                    case Constant.Notify.LOAD_START:
                        ToastUtil.showToast(mContext, "载入中");
                        break;
                    case Constant.Notify.LOAD_FAILURE:
                        ToastUtil.showToast(mContext, "请求失败");
                        recyclerview.loadMoreError();
                        break;
                    case Constant.Notify.ORIGIN_LIST://处理返回结果
                        initData((OrignewListRespBean) msg.obj);
                        break;
                }
            }
        };

        mOriginalPresenter = OriginalPresenter.getInstance();
        mOriginalPresenter.setHandler(mHandler);

        newsCollectFile = new NewsFile(mContext);
        initView();
    }

    public void initView() {
        mPage = 1;
        mTitle.setText("最新原创");
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

        LastMultiItemTypeSupport modelMultiItemTypeSupport = new LastMultiItemTypeSupport();
        mAdapter = new LastMultiAdapter(mContext, modelMultiItemTypeSupport);
        mAdapter.setOnClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                OriginalBean target = mAdapter.getDataByPosition(position);
                addHistory(target);
                if (target.getShowtype() == 1) {
                    NewsBean news = new NewsBean();
                    news.setTitle(target.getTitle());
                    news.setArcurl(target.getArcurl());
                    news.setWebviewurl(target.getWebviewurl());
                    news.setType("7");//1新闻6评测7原创
                    Intent intent = new Intent(mContext, NewsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("newsBean", news);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, OriginalActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("originalBean", mAdapter.getDataByPosition(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
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
        recyclerview.setRefreshing(true);//refresh的监听处理中已经有加载数据的操作
    }

    public void addHistory(OriginalBean originalBean) {
        NewsBean newsBean = new NewsBean();
        newsBean.setArcurl(originalBean.getArcurl());
        newsBean.setWebviewurl(originalBean.getWebviewurl());
        newsBean.setTitle(originalBean.getTitle());
        newsBean.setLitpic(originalBean.getLitpic());
        newsBean.setType(originalBean.getShowtype() == 7 ? "原创" : "新闻");
        newsBean.setSeeDate(TimeUtil.dateDayNow());
        newsCollectFile.addHistory(newsBean);
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

        mOriginalPresenter.getOrignewList(obj.toString());//异步请求
    }

    public void initData(OrignewListRespBean bean) {
        if (mPage > 1) {
            mAdapter.appendList(bean.getData().getList());
        } else {
            mAdapter.clearAndAddList(bean.getData().getList());
            totalCount = bean.getData().getTotal();
        }
        if (bean.getData().getList().size() == 0 || bean.getData().getList().size() < 1) {
            recyclerview.setNoMore(true);
        } else {
            recyclerview.refreshComplete();
            mPage++;
            //临时注释
            if (bean.getCode() == 1 && bean.getData().getList().size() == 0 && mPage <= totalCount / 10 + 1 || bean.getCode() == 1 && mPage < 5 && mAdapter.getItemCount() < 9) {
                obtainData();
            }
            //临时注释
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgReturn:
                onBackPressed();
                break;
            default:
                break;
        }
    }
}
