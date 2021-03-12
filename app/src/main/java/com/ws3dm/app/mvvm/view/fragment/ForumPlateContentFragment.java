package com.ws3dm.app.mvvm.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.ws3dm.app.R;
import com.ws3dm.app.fragment.BaseFragment;
import com.ws3dm.app.mvvm.adapter.ForumPlateListAdapter;
import com.ws3dm.app.mvvm.bean.ForumPlateBean;
import com.ws3dm.app.mvvm.view.activity.ForumDetailWeb;
import com.ws3dm.app.mvvm.viewmodel.BaseViewModel;
import com.ws3dm.app.mvvm.viewmodel.ForumPlateViewModel;
import com.ws3dm.app.view.DMFreshHead;

import java.util.List;

/**
 * 版块对应的内容,热门,最新,精华
 */
public class ForumPlateContentFragment extends BaseFragment {

    private ForumPlateContentFragment contentFragment;
    private View inflate;
    private RecyclerView recyclerView;
    private RefreshLayout refreshLayout;
    private ForumPlateViewModel viewModel;
    private ForumPlateListAdapter adapter;
    private int type;
    private String plateId;
    private int page;

    public ForumPlateContentFragment getInstant(String PlateId, int type) {
        contentFragment = new ForumPlateContentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("plateId", PlateId);
        bundle.putInt("type", type);
        contentFragment.setArguments(bundle);
        return contentFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ForumPlateViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fg_forum_plate_content, container, false);
        initView();
        initListener();
        LoadData();
        return inflate;
    }

    private void initView() {
        refreshLayout = inflate.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new DMFreshHead(mContext));
        refreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
        adapter = new ForumPlateListAdapter(mContext, R.layout.adapter_forum_item);
        recyclerView = inflate.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);
    }

    private void initListener() {
        viewModel.getState().observe(this, new Observer<BaseViewModel.State>() {
            @Override
            public void onChanged(BaseViewModel.State state) {
                refreshLayout.finishLoadMore();
                refreshLayout.finishRefresh();
            }
        });
        viewModel.getPlateBean().observe(this, new Observer<List<ForumPlateBean.DataBean.ListBean>>() {
            @Override
            public void onChanged(List<ForumPlateBean.DataBean.ListBean> listBeans) {
                if (page == 1) {
                    adapter.clearAndAddList(listBeans);
                } else {
                    adapter.appendList(listBeans);
                }
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                viewModel.getPlateContentList(plateId, type, page);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                page += 1;
                viewModel.getPlateContentList(plateId, type, page);
            }
        });

        adapter.setPlateItemClickListener(new ForumPlateListAdapter.PlateItemClickListener() {
            @Override
            public void ItemClick(ForumPlateBean.DataBean.ListBean item) {
//                ForumDetailBean bean = new ForumDetailBean();
//                UserDataBean userDataBean = new UserDataBean();
//                bean.setArcurl(item.getArcurl());
//                //TODO:
//                bean.setTid(String.valueOf(item.getTid()));
//                bean.setFid(String.valueOf(item.getTopicId()));
//                bean.setTitle(item.getTopicTitle());
//                bean.setWebviewurl(item.getWebviewurl());
//                bean.setPubdate_at(item.getCommentCount());
//                bean.setReplies(String.valueOf(item.getCommentCount()));
//
//                userDataBean.setNickname(item.getTopicSendUserName());
//                userDataBean.setAvatarstr(item.getTopicSendUserIcon());
//                bean.setUser(userDataBean);
//                Intent intent = new Intent(mContext, ForumDetailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("forumDetailBean", bean);
//                intent.putExtras(bundle);
//                startActivity(intent);
                Intent intent = new Intent(mContext, ForumDetailWeb.class);
                intent.putExtra("url", item.getWebviewurl());
                intent.putExtra("title", item.getTopicTitle());
                intent.putExtra("fid", item.getTopicId());
                intent.putExtra("tid", item.getTid());
                startActivity(intent);
            }
        });
    }

    private void LoadData() {
        Bundle arguments = getArguments();
        type = arguments.getInt("type");
        plateId = arguments.getString("plateId");
        page = 1;
        viewModel.getPlateContentList(plateId, type, page);
    }
}
