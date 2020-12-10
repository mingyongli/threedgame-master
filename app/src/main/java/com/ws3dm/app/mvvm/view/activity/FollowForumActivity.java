package com.ws3dm.app.mvvm.view.activity;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.BaseActivity;
import com.ws3dm.app.databinding.ActivityFollowForumBinding;
import com.ws3dm.app.mvvm.adapter.FollowForumAdapter;
import com.ws3dm.app.mvvm.bean.MyFollowBean;
import com.ws3dm.app.mvvm.viewmodel.BaseViewModel;
import com.ws3dm.app.mvvm.viewmodel.ForumFollowViewModel;
import com.ws3dm.app.view.DMFreshHead;

import java.util.List;

import static com.ws3dm.app.mvvm.viewmodel.BaseViewModel.State.ERR;
import static com.ws3dm.app.mvvm.viewmodel.BaseViewModel.State.SUCCESS;

/**
 * 全部的关注
 */
public class FollowForumActivity extends BaseActivity {

    private ActivityFollowForumBinding mBind;
    private ForumFollowViewModel viewModel;
    private FollowForumAdapter adapter;
    private int page = 1;

    @Override
    protected void init() {
        viewModel = new ViewModelProvider(this).get(ForumFollowViewModel.class);
        getLifecycle().addObserver(viewModel);
        mBind = bindView(R.layout.activity_follow_forum);
        initView();
        initData();
        initListener();
        LoadData();
    }

    private void LoadData() {
        viewModel.getFollowList(page);
    }


    private void initView() {
        setSupportActionBar(mBind.toolbar);
        mBind.refreshLayout.setRefreshHeader(new DMFreshHead(mContext));
        mBind.refreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
        mBind.recyclerview.setLayoutManager(new GridLayoutManager(mContext, 2));

    }

    private void initData() {
        adapter = new FollowForumAdapter(mContext, R.layout.adapter_forum_focus_item);
        mBind.recyclerview.setAdapter(adapter);
    }

    private void initListener() {
        mBind.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter.setItemOnclickListener(new FollowForumAdapter.ItemOnclickListener() {
            @Override
            public void OnClickListener(MyFollowBean.DataBean.FollowPlateBean item) {
                Intent intent = new Intent(mContext, SectionPageActivity.class);
                intent.putExtra("plateId", item.getPlateId());
                startActivity(intent);
            }
        });
        viewModel.dataState.observe(this, new Observer<BaseViewModel.State>() {
            @Override
            public void onChanged(BaseViewModel.State state) {
                if (state == ERR) {
                    if (page > 1) {
                        page--;
                    }
                } else if (state == SUCCESS) {
                    mBind.refreshLayout.finishLoadMore();
                    mBind.refreshLayout.finishRefresh();
                }
            }
        });
        viewModel.myFollowData.observe(this, new Observer<List<MyFollowBean.DataBean.FollowPlateBean>>() {
            @Override
            public void onChanged(List<MyFollowBean.DataBean.FollowPlateBean> myFollowBeans) {
                if (page == 1) {
                    adapter.clearAndAddList(myFollowBeans);
                } else {
                    adapter.appendList(myFollowBeans);
                }
            }
        });
        mBind.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                viewModel.getFollowList(page);
            }
        });
        mBind.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page += 1;
                viewModel.getFollowList(page);
            }
        });

    }
}
