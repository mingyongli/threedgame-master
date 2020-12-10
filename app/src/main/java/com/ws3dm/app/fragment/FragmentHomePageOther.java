package com.ws3dm.app.fragment;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.HomePageOtherAdapter;
import com.ws3dm.app.adapter.HomePageOtherSupport;
import com.ws3dm.app.adapter.NewsMultiAdapter;
import com.ws3dm.app.bean.HomeTabsDBBean;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.bean.TopTabDetailBean;
import com.ws3dm.app.databinding.FgOtherListBinding;
import com.ws3dm.app.sqlite.NewsFile;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentHomePageOther extends BaseFragment {
    //	private CommonRecyclerAdapter<NewsBean> mAdapter;
    private NewsMultiAdapter mAdapter;
    public List<NewsBean> listData = new ArrayList<NewsBean>();
    private NewsFile newsCollectFile;

    private int mPage = 1;
    private int cid;
    private int type;
    private FgOtherListBinding mBinding;
    private HomePageOtherAdapter adapter;

    public static FragmentHomePageOther newInstance(HomeTabsDBBean.HomeTabsData tabs) {
        FragmentHomePageOther fragmentHomePageOther = new FragmentHomePageOther();
        Bundle bundle = new Bundle();
        bundle.putInt("cid", tabs.getCid());
        bundle.putInt("type", tabs.getType());
        fragmentHomePageOther.setArguments(bundle);
        return fragmentHomePageOther;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_other_list, container, false);
        Bundle arguments = getArguments();
        cid = arguments.getInt("cid");
        type = arguments.getInt("type");
        newsCollectFile = new NewsFile(getActivity());
        initView();
        initData();
        return mBinding.getRoot();
    }


    public void initView() {
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);
        HomePageOtherSupport support = new HomePageOtherSupport();
        adapter = new HomePageOtherAdapter(mContext, support);
        mBinding.recyclerview.setAdapter(adapter);
        mBinding.recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPage = 1;
                        getListData();
                    }
                }, 50);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPage++;
                        getListData();
                    }
                }, 50);
            }
        });
    }

    private void initData() {
        getListData();
    }

    private void getListData() {
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(type + String.valueOf(cid) + String.valueOf(10) + mPage + time + NewUrl.KEY);
        Map<String, Object> bean = new HashMap<>();
        bean.put("type", type);
        bean.put("cid", cid);
        bean.put("pagesize", 10);
        bean.put("page", mPage);
        bean.put("time", time);
        bean.put("sign", sign);
        String s = JSON.toJSON(bean).toString();
        OkHttpUtils.postString().content(s).url(NewUrl.TOPTABDETAIL).build().execute(new Callback<TopTabDetailBean>() {
            @Override
            public TopTabDetailBean parseNetworkResponse(Response response) throws IOException {
                long end = System.currentTimeMillis();
                Log.d("toptabdetail响应时间", String.valueOf(end - time));
                String string = response.body().string();
                return new Gson().fromJson(string, TopTabDetailBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(TopTabDetailBean response) {
                if (response.getCode() == 1) {
                    mBinding.recyclerview.refreshComplete();
                    mBinding.recyclerview.loadMoreComplete();
                    updataList(response.getData().getList());
                } else {
                    mBinding.recyclerview.refreshComplete();
                    mBinding.recyclerview.loadMoreComplete();
                    ToastUtil.showToast(mContext, response.getMsg());
                }
            }

        });
    }

    private void updataList(List<TopTabDetailBean.DataBean.ListBean> list) {
        if (mPage == 1) {
            adapter.clearAndAddList(list);
        } else if (mPage > 1) {
            adapter.appendList(list);
        }
    }

}