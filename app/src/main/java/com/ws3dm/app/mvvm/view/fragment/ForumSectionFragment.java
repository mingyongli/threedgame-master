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

import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.R;
import com.ws3dm.app.databinding.FgSectionForumBinding;
import com.ws3dm.app.fragment.BaseFragment;
import com.ws3dm.app.mvvm.adapter.ForumTabAdapter;
import com.ws3dm.app.mvvm.adapter.ForumTabContentAdapter;
import com.ws3dm.app.mvvm.bean.ForumListBean;
import com.ws3dm.app.mvvm.view.activity.SectionPageActivity;
import com.ws3dm.app.mvvm.viewmodel.BaseViewModel;
import com.ws3dm.app.mvvm.viewmodel.ForumSectionViewModel;

import java.util.List;
import java.util.Map;

/**
 * 论坛版块,所有的版块
 */
public class ForumSectionFragment extends BaseFragment {

    private ForumSectionViewModel viewModel;
    public static String TAG = ForumSectionFragment.class.getSimpleName();
    private FgSectionForumBinding mBind;
    //初始化page
    private int page = 1;
    private int plateTypeId = 0;
    private ForumTabAdapter tabAdapter;
    private ForumTabContentAdapter contentAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ForumSectionViewModel.class);
        getLifecycle().addObserver(viewModel);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBind = DataBindingUtil.inflate(inflater, R.layout.fg_section_forum, container, false);
        initView();
        initData();
        initListener();
        LoadData();
        return mBind.getRoot();
    }

    private void initView() {
        mBind.tab.setLayoutManager(new LinearLayoutManager(mContext));
        tabAdapter = new ForumTabAdapter(mContext, R.layout.adapter_forum_tab_item);
        mBind.tab.setAdapter(tabAdapter);
        mBind.content.setLayoutManager(new GridLayoutManager(mContext, 2));
        contentAdapter = new ForumTabContentAdapter(mContext, R.layout.adapter_forum_section_item);
        mBind.content.setRefreshHeader(new CustomRefreshHeader(mContext));
        mBind.content.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        mBind.content.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);
        mBind.content.setAdapter(contentAdapter);
    }

    private void initData() {

    }

    private void initListener() {
        viewModel.getState().observe(this, new Observer<BaseViewModel.State>() {
            @Override
            public void onChanged(BaseViewModel.State state) {
            mBind.content.refreshComplete();
            mBind.content.loadMoreComplete();
            }
        });
        //tab数据
        viewModel.getForumSection().observe(this, new Observer<List<ForumListBean.DataBean.ListBean>>() {
            @Override
            public void onChanged(List<ForumListBean.DataBean.ListBean> listBeans) {
                tabAdapter.clearAndAddList(listBeans);
            }
        });
        //拿到真实的platetypeId
        viewModel.getPlateTypeId().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                plateTypeId = integer;
            }
        });
        //tab对应的content数据
        viewModel.getForumBean().observe(this, new Observer<Map<Integer, ForumListBean.DataBean.ListBean>>() {
            @Override
            public void onChanged(Map<Integer, ForumListBean.DataBean.ListBean> integerObjectMap) {
                List<ForumListBean.DataBean.ListBean.PlateTypeListBean> plateTypeList = integerObjectMap.get(plateTypeId).getPlateTypeList();
                if (page == 1) {
                    contentAdapter.clearAndAddList(plateTypeList);
                } else {
                    contentAdapter.appendList(plateTypeList);
                }
            }
        });

        tabAdapter.setItemOnclick(new ForumTabAdapter.ItemOnclick() {
            @Override
            public void Onclick(ForumListBean.DataBean.ListBean item) {
                plateTypeId = item.getPlateTypeId();
                page = 1;
                viewModel.getContentList(plateTypeId, page);
            }
        });
        mBind.content.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                viewModel.getContentList(plateTypeId, page);
            }

            @Override
            public void onLoadMore() {
                page += 1;
                viewModel.getContentList(plateTypeId, page);
            }
        });
        contentAdapter.setItemOnclickListener(new ForumTabContentAdapter.ItemOnclickListener() {
            @Override
            public void OnClickListener(ForumListBean.DataBean.ListBean.PlateTypeListBean item) {
                Intent intent = new Intent(mContext, SectionPageActivity.class);
                intent.putExtra("plateId", String.valueOf(item.getPlateId()));
                startActivity(intent);
            }
        });
    }

    private void LoadData() {
        //加载默认数据
        viewModel.getInitList();
        viewModel.getContentList(plateTypeId,page);
    }

}
