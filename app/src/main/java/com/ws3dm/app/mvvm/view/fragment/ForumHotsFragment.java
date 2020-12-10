package com.ws3dm.app.mvvm.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.ForumDetailActivity;
import com.ws3dm.app.bean.ForumDetailBean;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.databinding.FgHotForumBinding;
import com.ws3dm.app.fragment.BaseFragment;
import com.ws3dm.app.mvvm.adapter.ForumHotAdapter;
import com.ws3dm.app.mvvm.bean.PlateContentBean;
import com.ws3dm.app.mvvm.messageEvent.ConstantEvent;
import com.ws3dm.app.mvvm.messageEvent.MessageEvent;
import com.ws3dm.app.mvvm.viewmodel.BaseViewModel;
import com.ws3dm.app.mvvm.viewmodel.ForumHotsViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * 论坛热帖
 */
public class ForumHotsFragment extends BaseFragment {

    private ForumHotsViewModel viewModel;
    public static String TAG = ForumHotsFragment.class.getSimpleName();
    private FgHotForumBinding mBind;
    private ForumHotAdapter adapter;
    private int page = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ForumHotsViewModel.class);
        getLifecycle().addObserver(viewModel);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBind = DataBindingUtil.inflate(inflater, R.layout.fg_hot_forum, container, false);
        initView();
        initData();
        initListener();
        LoadData();
        return mBind.getRoot();
    }


    private void initView() {
        mBind.recyclerview.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        mBind.recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
        mBind.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        mBind.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);
    }

    private void initData() {
        adapter = new ForumHotAdapter(mContext, R.layout.adapter_forum_hot_item);
        mBind.recyclerview.setAdapter(adapter);
    }

    private void initListener() {
        viewModel.state.observe(this, new Observer<BaseViewModel.State>() {
            @Override
            public void onChanged(BaseViewModel.State state) {
                mBind.recyclerview.loadMoreComplete();
                mBind.recyclerview.refreshComplete();
            }
        });
        viewModel.hotListData.observe(this, new Observer<List<PlateContentBean.DataBean.ListBean>>() {
            @Override
            public void onChanged(List<PlateContentBean.DataBean.ListBean> listBeans) {
                if (page == 1) {
                    adapter.clearAndAddList(listBeans);
                } else {
                    adapter.appendList(listBeans);
                }
            }
        });
        mBind.recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                viewModel.getHotsList(page);
            }

            @Override
            public void onLoadMore() {
                page += 1;
                viewModel.getHotsList(page);
            }
        });
        adapter.setHotItemClickListener(new ForumHotAdapter.HotItemClickListener() {
            @Override
            public void ItemClickListener(PlateContentBean.DataBean.ListBean item) {
                ForumDetailBean bean = new ForumDetailBean();
                UserDataBean userDataBean = new UserDataBean();
                bean.setArcurl(item.getArcurl());
                bean.setTid(String.valueOf(item.getTid()));
                bean.setFid(String.valueOf(item.getTopicId()));
                bean.setTitle(item.getTopicTitle());
                bean.setWebviewurl(item.getWebviewurl());
                bean.setPubdate_at(item.getCommentCount());
                bean.setReplies(String.valueOf(item.getCommentCount()));

                userDataBean.setNickname(item.getTopicSendUserName());
                userDataBean.setAvatarstr(item.getTopicSendUserIcon());
                bean.setUser(userDataBean);
                Intent intent = new Intent(mContext, ForumDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("forumDetailBean", bean);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void LoadData() {
        viewModel.getHotsList(page);
    }
}
