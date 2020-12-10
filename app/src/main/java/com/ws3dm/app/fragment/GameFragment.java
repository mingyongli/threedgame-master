package com.ws3dm.app.fragment;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ws3dm.app.R;
import com.ws3dm.app.adapter.TabAdapter;
import com.ws3dm.app.databinding.FgGameBinding;
import com.ws3dm.app.util.AppUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Describution :游戏库（底部标签）
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2017/8/16 13:20
 **/

public class GameFragment extends BaseFragment {

    private FgGameBinding mBinding;
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> tabs = new ArrayList<>();//tablayout的标签数组

    private String title[] = {"单机", "网游", "手游"};

    public static ViewPager mViewPager = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_game, container, false);

        initView();
        return mBinding.getRoot();
    }

    public void initView() {
//        mBinding.viewTop.findViewById(R.id.img_head).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(MyApplication.getUserData()==null||!MyApplication.getUserData().loginStatue){
//                    startActivity(new Intent(getActivity(), LoginActivity.class));
//                }else if(MyApplication.getUserData().mobile.length()==0){//第三方登陆，非绑定跳转绑定界面
//                    Intent intent=new Intent(mContext, ForgetPassActivity.class);
//                    intent.putExtra("type",1);//1,绑定手机号，2修改密码，3忘记密码
//                    startActivity(intent);
//                }else{
//                    getActivity().startActivity(new Intent(getActivity(), SetupActivity.class));
//                }
//            }
//        });
//        mBinding.viewTop.findViewById(R.id.imgSearch).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////				startActivity(new Intent(getActivity(), NewSearchActivity.class).putExtra("search", "NewsFragment"));
//                startActivity(new Intent(getActivity(), SearchActivity.class).putExtra("search", "GameFragment"));
////				Intent aggreement=new Intent(mContext,SingleWebActivity.class);
////				aggreement.putExtra("title","搜索");
////				aggreement.putExtra("url","https://wap.sogou.com/");
////				startActivity(aggreement);
//            }
//        });

        // 初始化Column栏目项
        tabs = Arrays.asList(title);
        Fragment hotFragment = new GamePCFragment();//单机
        fragments.add(hotFragment);

        Fragment saleFragment = new GameNETFragment();//网游 
        fragments.add(saleFragment);

        Fragment mFragmentCategory = new GameMGFragment();//手游
        fragments.add(mFragmentCategory);

        //设置TabLayout的模式
        mBinding.mTabLayout.setTabMode(TabLayout.MODE_FIXED);

//		//设置分割线
//		LinearLayout linearLayout = (LinearLayout) mBinding.mTabLayout.getChildAt(0);
//		linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//		linearLayout.setDividerDrawable(ContextCompat.getDrawable(this,
//				R.drawable.divider)); //设置分割线的样式
//		linearLayout.setDividerPadding(dip2px(10)); //设置分割线间隔
        TabAdapter tabAdapter = new TabAdapter(getChildFragmentManager());
//        mBinding.mViewPager.setAdapter(new TabAdapter(getChildFragmentManager(), fragments, tabs));
        mBinding.mViewPager.setAdapter(tabAdapter);
        mBinding.mViewPager.setOffscreenPageLimit(tabs.size());
        mBinding.mTabLayout.setupWithViewPager(mBinding.mViewPager);
        tabAdapter.setPage(fragments, tabs);
        mBinding.mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                AppUtil.setIndicator(mContext, mBinding.mTabLayout, 25);
            }
        });

        mViewPager = mBinding.mViewPager;
//        mBinding.mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                TextView textView = new TextView(mContext);
//                textView.setTextSize(22);
//                textView.setTextColor(Color.parseColor("#ffffff"));
//                textView.setText(tab.getText());
//                tab.setCustomView(textView);
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//                tab.setCustomView(null);
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        if(MyApplication.getUserData().loginStatue){
//            GlideUtil.loadCircleImage(mContext, MyApplication.getUserData().avatarstr, (ImageView) mBinding.viewTop.findViewById(R.id.img_head));
//        }else{
//            GlideUtil.loadImage(mContext,R.drawable.no_login,(ImageView) mBinding.viewTop.findViewById(R.id.img_head));
//        }
//        // 更换主题
//        if (SharedUtil.getSharedPreferencesData("newsTheme").equals("1")) {
//            SharedUtil.setSharedPreferencesData("newsTheme", "0");
//            if (SharedUtil.getSharedPreferencesData("isNight").equals("0"))
//                getActivity().setTheme(R.style.DayTheme);
//            else
//                getActivity().setTheme(R.style.NightTheme);
//            View rootView = getActivity().getWindow().getDecorView();
//            ColorUiUtil.changeTheme(rootView, getActivity().getTheme());
//            if (getChildFragmentManager().getFragments() != null)
//                for (int i = 0; i < getChildFragmentManager().getFragments().size(); i++)
//                    getChildFragmentManager().getFragments().get(i).onResume();
//        }
    }
}
