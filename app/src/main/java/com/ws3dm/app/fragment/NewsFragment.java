package com.ws3dm.app.fragment;

import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.umeng.analytics.MobclickAgent;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.HomePreferenceActivity;
import com.ws3dm.app.adapter.TabAdapters;
import com.ws3dm.app.bean.HomeTabsDBBean;
import com.ws3dm.app.databinding.FgNewsBinding;
import com.ws3dm.app.sqlite.TitleFile;
import com.ws3dm.app.util.AppUtil;
import com.ws3dm.app.util.StringUtil;
import com.yu.imgpicker.utils.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

/**
 * Describution :新闻（底部标签）
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2018/4/10 9:45
 **/
public class NewsFragment extends BaseFragment {

    private static final String TAG = "NewsFragment";

    private FgNewsBinding mBinding;
    private List<HomeTabsDBBean.HomeTabsData> tabs = new ArrayList<>();//tablayout的标签数组
    private static TabAdapters mTabAdapter;
    private TitleFile titleFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  AppUtil.verifyStoragePermissions(getActivity());
        titleFile = new TitleFile(mContext);
        setHasOptionsMenu(true);
    }

    private void getTopTabs() {
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(time + NewUrl.KEY);
        Map<String, Object> values = new HashMap<>();
        values.put("sign", sign);
        values.put("time", time);
        String json = new JSONObject(values).toString();
        OkHttpUtils
                .postString()
                .url(NewUrl.TOP_TABLES)
                .content(json)
                .build()
                .execute(new com.zhy.http.okhttp.callback.Callback<HomeTabsDBBean>() {
                    @Override
                    public HomeTabsDBBean parseNetworkResponse(com.squareup.okhttp.Response response) throws IOException {
                        String data = response.body().string();
                        Log.e("3DMAGME", data);
                        return new Gson().fromJson(data, HomeTabsDBBean.class);
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("message", e.getMessage());
                    }

                    @Override
                    public void onResponse(HomeTabsDBBean response) {
                        if (response.getCode() == 1) {
                            initView(response);
                        }
                    }
                });


    }

    /**
     * 从本地拿设置完成的tab  如果没有就从网络上获取
     */
    private void getDBTabs() {
        List<HomeTabsDBBean.HomeTabsData> query = titleFile.query();
        if (query.size() == 0) {
            getTopTabs();
        } else {
            reLoadView(query);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_news, container, false);

//        mBinding.MyUtilbar.setTitle(getResources().getString(R.string.person));
//        activity.setSupportActionBar(mBinding.MyUtilbar);
//        mBinding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.notification:
//                        if (MyApplication.getUserData() == null || !MyApplication.getUserData().loginStatue) {
//                            startActivity(new Intent(getActivity(), LoginActivity.class));
//                        } else {
//                            getActivity().startActivity(new Intent(getActivity(), RecommendActivity.class));
//                        }
//                        break;
//                    case R.id.message:
//                        if (MyApplication.getUserData() == null || !MyApplication.getUserData().loginStatue) {
//                            startActivity(new Intent(getActivity(), LoginActivity.class));
//                        } else {
//                            //getActivity().startActivity(new Intent(getActivity(), CommentActivity.class));
//                            getActivity().startActivity(new Intent(getActivity(), MessageActivity.class));
//                        }
//                        break;
//                    case R.id.setting:
//                        if (MyApplication.getUserData() == null || !MyApplication.getUserData().loginStatue) {
//                            startActivity(new Intent(getActivity(), LoginActivity.class));
//                        } else {
//                            //getActivity().startActivity(new Intent(getActivity(), CommentActivity.class));
//                            getActivity().startActivity(new Intent(getActivity(), SettingActivity.class));
//                        }
//                        break;
//                }
//                return true;
//            }
//        });
//        mBinding.viewTop.findViewById(R.id.imgSearch).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (MyApplication.getUserData() == null || !MyApplication.getUserData().loginStatue) {
//                    startActivity(new Intent(getActivity(), LoginActivity.class));
//                } else {
//                    getActivity().startActivity(new Intent(getActivity(), RecommendActivity.class));
//                }
//
//            }
//        });
//        mBinding.viewTop.findViewById(R.id.imgEmail).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (MyApplication.getUserData() == null || !MyApplication.getUserData().loginStatue) {
//                    startActivity(new Intent(getActivity(), LoginActivity.class));
//                } else {
//                    //getActivity().startActivity(new Intent(getActivity(), CommentActivity.class));
//                    getActivity().startActivity(new Intent(getActivity(), MessageActivity.class));
//                }
//            }
//        });
//        mBinding.viewTop.findViewById(R.id.imgSet).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (MyApplication.getUserData() == null || !MyApplication.getUserData().loginStatue) {
//                    startActivity(new Intent(getActivity(), LoginActivity.class));
//                } else {
//                    //getActivity().startActivity(new Intent(getActivity(), CommentActivity.class));
//                    getActivity().startActivity(new Intent(getActivity(), SettingActivity.class));
//                }
//            }
//        });
        mBinding.preferenceSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, HomePreferenceActivity.class);
                startActivityForResult(intent, 2);
            }
        });

        getDBTabs();
        return mBinding.getRoot();
    }


    public void initView(HomeTabsDBBean response) {
        tabs = response.getData();
        /*Fragment mFragmentNewsHot = new FragmentNewsHot();//热点新闻
        fragments.add(mFragmentNewsHot);

        Fragment mFragmentNewsDigital = new FragmentNewsDigital();//数码列表
        fragments.add(mFragmentNewsDigital);

        Fragment mFragmentNewsENT = new FragmentNewsENT();//娱乐
        fragments.add(mFragmentNewsENT);

//		Fragment mFragmentVideo = new Fragment();//视频
//		fragments.add(mFragmentVideo);

        Fragment mFragmentGonglue = new FragmentGonglue();//攻略
        fragments.add(mFragmentGonglue);

        Fragment mFragmentGonglues = new FragmentGonglue();//攻略
        fragments.add(mFragmentGonglues);*/
        //设置TabLayout的模式

//        HomePageAdapter adapter = new HomePageAdapter(getChildFragmentManager());
//        adapter.setData(tabs);

        mBinding.mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        TabAdapters mTabAdapter = new TabAdapters(getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mBinding.mViewPager.setAdapter(mTabAdapter);
        mBinding.mViewPager.setOffscreenPageLimit(3);
        mBinding.mTabLayout.setupWithViewPager(mBinding.mViewPager);
        mTabAdapter.setPage(tabs);
        mBinding.mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                //AppUtil.setIndicator(mContext,mBinding.mTabLayout,25);
            }
        });

        mBinding.mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView textView = new TextView(NewsFragment.this.getActivity());
                textView.setTextSize(22);
                textView.setTextColor(Color.parseColor("#ffffff"));
                textView.setText(tab.getText());
                tab.setCustomView(textView);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setCustomView(null);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void reLoadView(List<HomeTabsDBBean.HomeTabsData> tabs) {
        List<HomeTabsDBBean.HomeTabsData> newTabs = new ArrayList<>();
        for (HomeTabsDBBean.HomeTabsData tab : tabs) {
            if (tab.isOpen() == 1) {
                newTabs.add(tab);
            }
        }
        mBinding.mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        TabAdapters mTabAdapter = new TabAdapters(getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mBinding.mViewPager.setAdapter(mTabAdapter);
        mBinding.mViewPager.setOffscreenPageLimit(3);
        mBinding.mTabLayout.setupWithViewPager(mBinding.mViewPager);
        mTabAdapter.setPage(newTabs);
        mBinding.mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView textView = new TextView(NewsFragment.this.getActivity());
                textView.setTextSize(22);
                textView.setTextColor(Color.parseColor("#ffffff"));
                textView.setText(tab.getText());
                tab.setCustomView(textView);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setCustomView(null);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("NewsFragment");
//		if(MyApplication.getUserData().loginStatue){
//			GlideUtil.loadCircleImage(mContext, MyApplication.getUserData().avatarstr, (ImageView) mBinding.viewTop.findViewById(R.id.img_head));
//		}else{
//			GlideUtil.loadImage(mContext,R.drawable
// .no_login,(ImageView) mBinding.viewTop.findViewById(R.id.img_head));
//		}
//		// 更换主题
//		if (SharedUtil.getSharedPreferencesData("newsTheme").equals("1")) {
//			SharedUtil.setSharedPreferencesData("newsTheme", "0");
//			if (SharedUtil.getSharedPreferencesData("isNight").equals("0"))
//				getActivity().setTheme(R.style.DayTheme);
//			else
//				getActivity().setTheme(R.style.NightTheme);
//			View rootView = getActivity().getWindow().getDecorView();
//			ColorUiUtil.changeTheme(rootView, getActivity().getTheme());
//			if (getChildFragmentManager().getFragments() != null)
//				for (int i = 0; i < getChildFragmentManager().getFragments().size(); i++)
//					getChildFragmentManager().getFragments().get(i).onResume();
//		}
    }

    public static void reTop() {
//        if (mTabAdapter.currentFragment instanceof FragmentNewsHot) {
//            ((FragmentNewsHot) mTabAdapter.currentFragment).reLoad();
//        } else if (mTabAdapter.currentFragment instanceof FragmentNewsDigital) {
//            ((FragmentNewsDigital) mTabAdapter.currentFragment).reLoad();
//        } else if (mTabAdapter.currentFragment instanceof FragmentNewsENT) {
//            ((FragmentNewsENT) mTabAdapter.currentFragment).reLoad();
//		}else if(mTabAdapter.currentFragment instanceof FragmentGonglue){
//			((FragmentNewsENT) mTabAdapter.currentFragment).reLoad();
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 2) {
                getDBTabs();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.home_toolbar_item, menu);
    }
}
