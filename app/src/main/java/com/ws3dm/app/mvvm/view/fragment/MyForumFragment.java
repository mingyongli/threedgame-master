package com.ws3dm.app.mvvm.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.LoginActivity;
import com.ws3dm.app.databinding.FgMyForumBinding;
import com.ws3dm.app.databinding.HeadMyForumBinding;
import com.ws3dm.app.fragment.BaseFragment;
import com.ws3dm.app.mvvm.adapter.FocusAdapter;
import com.ws3dm.app.mvvm.adapter.RecentAdapter;
import com.ws3dm.app.mvvm.bean.MyIndexBean;
import com.ws3dm.app.mvvm.view.activity.FollowForumActivity;
import com.ws3dm.app.mvvm.view.activity.SectionPageActivity;
import com.ws3dm.app.mvvm.viewmodel.BaseViewModel;
import com.ws3dm.app.mvvm.viewmodel.MyForumViewModel;

import java.util.List;

/**
 * 我的论坛界面,包含最近逛的版块和关注的版块
 */
public class MyForumFragment extends BaseFragment {

    private MyForumViewModel viewModel;
    public static String TAG = MyForumFragment.class.getSimpleName();
    private FgMyForumBinding mBind;
    private HeadMyForumBinding mBindHead;
    private RecentAdapter recentAdapter;
    private FocusAdapter focusAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MyForumViewModel.class);
        getLifecycle().addObserver(viewModel);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBind = DataBindingUtil.inflate(inflater, R.layout.fg_my_forum, container, false);
        mBindHead = DataBindingUtil.inflate(inflater, R.layout.head_my_forum, container, false);
        //判断用户是否登录
        if (MyApplication.getUserData() == null || !MyApplication.getUserData().loginStatue) {
            mBind.prompt.setVisibility(View.VISIBLE);
            mBind.loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mContext, LoginActivity.class));
                }
            });
        } else {
            userLogin();
        }
        return mBind.getRoot();
    }

    private void userLogin() {
        mBind.prompt.setVisibility(View.GONE);
        mBind.FocusSection.setVisibility(View.VISIBLE);
        initView();
        initData();
        initListener();
        mBind.FocusSection.setRefreshing(true);
    }

    private void initView() {
        //给recycleview添加头部
        //最近浏览的版块
        mBind.FocusSection.addHeaderView(mBindHead.getRoot());
        mBindHead.RecentSection.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));

        //关注的版块
        mBind.FocusSection.setLayoutManager(new GridLayoutManager(mContext, 2));
        mBind.FocusSection.setRefreshHeader(new CustomRefreshHeader(mContext));
        mBind.FocusSection.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        mBind.FocusSection.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);

    }

    private void initData() {
        recentAdapter = new RecentAdapter(mContext, R.layout.adapter_forum_recent_item);
        focusAdapter = new FocusAdapter(mContext, R.layout.adapter_forum_focus_item);
        mBindHead.RecentSection.setAdapter(recentAdapter);
        mBind.FocusSection.setAdapter(focusAdapter);

    }

    private void initListener() {
        //打开更多的关注版块
        mBindHead.moreFocusSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, FollowForumActivity.class));
            }
        });
        //禁止加载更多
        mBind.FocusSection.setLoadingMoreEnabled(false);

        mBind.FocusSection.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                viewModel.getMyIndexData();
            }

            @Override
            public void onLoadMore() {

            }
        });
        viewModel.getDataState().observe(this, new Observer<BaseViewModel.State>() {
            @Override
            public void onChanged(MyForumViewModel.State recentState) {
                //不管什么状态都要停止刷新
                mBind.FocusSection.refreshComplete();
            }
        });
        //当数据发生改变时
        viewModel.getLateLiveData().observe(this, new Observer<List<MyIndexBean.DataBean.LatelyPlateBean>>() {
            @Override
            public void onChanged(List<MyIndexBean.DataBean.LatelyPlateBean> latelyPlateBeans) {
                recentAdapter.clearAndAddList(latelyPlateBeans);
            }
        });
        viewModel.getFollowLiveData().observe(this, new Observer<List<MyIndexBean.DataBean.FollowPlateBean>>() {
            @Override
            public void onChanged(List<MyIndexBean.DataBean.FollowPlateBean> followPlateBeans) {
                focusAdapter.clearAndAddList(followPlateBeans);
            }
        });

        recentAdapter.setItemOnclickListener(new RecentAdapter.ItemOnclickListener() {
            @Override
            public void OnClickListener(MyIndexBean.DataBean.LatelyPlateBean item) {
                Intent intent = new Intent(mContext, SectionPageActivity.class);
                intent.putExtra("plateId", item.getPlateId());
                startActivity(intent);
            }
        });

        focusAdapter.setItemOnclickListener(new FocusAdapter.ItemOnclickListener() {
            @Override
            public void OnClickListener(MyIndexBean.DataBean.FollowPlateBean item) {
                Intent intent = new Intent(mContext, SectionPageActivity.class);
                intent.putExtra("plateId", item.getPlateId());
                startActivity(intent);
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
