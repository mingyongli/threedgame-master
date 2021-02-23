package com.ws3dm.app.mvvm.view.activity;

import android.content.Intent;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.ws3dm.app.R;
import com.ws3dm.app.activity.BaseActivity;
import com.ws3dm.app.activity.PublishActivity;
import com.ws3dm.app.databinding.AcSectionPageBinding;
import com.ws3dm.app.mvvm.adapter.ForumPlateViewPageAdapter;
import com.ws3dm.app.mvvm.adapter.NoticeAdapter;
import com.ws3dm.app.mvvm.bean.PlateListHeadBean;
import com.ws3dm.app.mvvm.viewmodel.BaseViewModel;
import com.ws3dm.app.mvvm.viewmodel.SectionPageViewModel;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.util.glide.GlideUtil;

import java.util.ArrayList;
import java.util.List;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

/**
 * 版块内容
 */
public class SectionPageActivity extends BaseActivity {

    private SectionPageViewModel viewModel;
    private AcSectionPageBinding mBind;
    private String plateId;
    private ForumPlateViewPageAdapter viewPageAdapter;
    private int isFollow;
    private String plateTitle;


    @Override
    protected void init() {
        viewModel = new ViewModelProvider(this).get(SectionPageViewModel.class);
        getLifecycle().addObserver(viewModel);
        mBind = bindView(R.layout.ac_section_page);
        initData();
        initListener();
        LoadData();
        SetTabLayout();
    }

    private void initView(PlateListHeadBean.DataBean dataBean) {
        //判断是否关注
        isFollow = dataBean.getHead().getIsFollow();
        refreshFollow();
        mBind.topPlaced.setLayoutManager(new LinearLayoutManager(mContext));
        NoticeAdapter adapter = new NoticeAdapter(mContext, R.layout.adapter_forum_notice_item);
        mBind.topPlaced.setAdapter(adapter);
        GlideUtil.loadImage(mContext, dataBean.getHead().getPlateIcon(), mBind.forumImg);
        GlideUtil.loadImage(mContext, dataBean.getHead().getPlateIcon(), mBind.forumBg);
        mBind.forumTitle.setText(dataBean.getHead().getPlateTitle());
        mBind.topicCount.setText(dataBean.getHead().getTopicCount());
        mBind.plateFollowCount.setText(dataBean.getHead().getPlateFollowCount());
        mBind.todayReplyCount.setText(dataBean.getHead().getTodayReplyCount());
        if (dataBean.getHead().getNotice() == null) {
            return;
        }
        adapter.clearAndAddList(dataBean.getHead().getNotice());
    }

    private void refreshFollow() {
        if (isFollow == 1) {
            mBind.focusSection.setText("已关注");
        } else {
            mBind.focusSection.setText("关注");
        }
    }

    private void initData() {
        plateId = getIntent().getStringExtra("plateId");
    }

    private void initListener() {
        mBind.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PlateSearchActivity.class);
                intent.putExtra("plateId", plateId);
                startActivity(intent);
            }
        });

        mBind.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBind.focusSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFollow == 1) {
                    viewModel.plateFollow(mContext, Integer.parseInt(plateId), 0);
                } else {
                    viewModel.plateFollow(mContext, Integer.parseInt(plateId), 1);
                }
            }
        });
        viewModel.getFollowState().observe(this, new Observer<BaseViewModel.State>() {
            @Override
            public void onChanged(BaseViewModel.State state) {
                if (state == BaseViewModel.State.SUCCESS) {
                    viewModel.getPlateListHead(Integer.parseInt(plateId));
                } else {
                    ToastUtil.showToast(mContext, "失败");
                }
            }
        });
        viewModel.getHeadData().observe(this, new Observer<PlateListHeadBean.DataBean>() {
            @Override
            public void onChanged(PlateListHeadBean.DataBean dataBean) {
                plateTitle = dataBean.getHead().getPlateTitle();
                initView(dataBean);
            }
        });
        mBind.postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PostContentActivity.class);
                intent.putExtra("plateId", plateId);
                intent.putExtra("plateTitle", plateTitle);
                startActivity(intent);
            }
        });
    }

    private void LoadData() {
        viewModel.getPlateListHead(Integer.parseInt(plateId));
    }

    private void SetTabLayout() {
        /**
         * type 1: 热门,2:最新,3:精华
         */
        List<Integer> type = new ArrayList<>();
        type.add(1);
        type.add(2);
        type.add(3);
        viewPageAdapter = new ForumPlateViewPageAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, plateId, type);
        mBind.tablayout.setupWithViewPager(mBind.viewpager);
        mBind.viewpager.setAdapter(viewPageAdapter);
    }
}
