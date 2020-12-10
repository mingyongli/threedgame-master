package com.ws3dm.app.fragment;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.TabAdapter;
import com.ws3dm.app.databinding.AcCollectFragmentBinding;
import com.ws3dm.app.util.AppUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CollectFragment extends BaseFragment{
    private AcCollectFragmentBinding mBinding;

    private String[] title = {"新闻", "游戏","帖子"};
    private List<String> tabs = new ArrayList<>();//tablayout的标签数组

    private List<Fragment> fragments = new ArrayList<>();
    private String uid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.ac_collect_fragment, container, false);
        mBinding.setHandler(this);
        uid = getArguments().getString("uid");
        initView();
        return mBinding.getRoot();
    }


    private void initView() {
        initFragment();
        initTabColumn();
    }

    //  初始化Fragment
    private void initFragment() {
        tabs= Arrays.asList(title);
        int forSize=tabs.size();
        for (int i = 0; i <forSize; i++) {
            Fragment fragment=null;
            if (i ==0) {//新闻收藏
                fragment = new FragmentCollectNews();
            }else if (i ==1) {//游戏收藏fragment
                fragment = new FragmentCollectGames();
            }else{//礼包收藏
                fragment = new FragmentCollectForum();
            }
            Bundle bundle = new Bundle();
            bundle.putString("uid",uid);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
    }

    // 初始化Column栏目项
    private void initTabColumn() {
        //设置TabLayout的模式
        mBinding.mTabLayout.setTabMode(TabLayout.MODE_FIXED);

//		//设置分割线
//		LinearLayout linearLayout = (LinearLayout) mBinding.mTabLayout.getChildAt(0);
//		linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//		linearLayout.setDividerDrawable(ContextCompat.getDrawable(this,
//				R.drawable.divider)); //设置分割线的样式
//		linearLayout.setDividerPadding(dip2px(10)); //设置分割线间隔

        mBinding.mViewPager.setAdapter(new TabAdapter(getActivity().getSupportFragmentManager(),fragments,tabs));
        mBinding.mViewPager.setOffscreenPageLimit(tabs.size());
        mBinding.mTabLayout.setupWithViewPager(mBinding.mViewPager);

//		viewpage禁止滑动
//		mBinding.mTabLayout.setOnTouchListener(new View.OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				switch (event.getAction()) {
//					case MotionEvent.ACTION_MOVE:
//						mBinding.mTabLayout.requestDisallowInterceptTouchEvent(true);
//						break;
//					case MotionEvent.ACTION_CANCEL:
//						mBinding.mTabLayout.requestDisallowInterceptTouchEvent(false);
//					default:
//						break;
//				}
//				return true;
//			}
//		});

        mBinding.mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                AppUtil.setIndicator(mContext,mBinding.mTabLayout,56);
            }
        });
    }

    public void clickHandler(View view) {
        switch (view.getId()) {
            // 返回
            case R.id.imgReturn:

                break;
        }
    }
}
