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
import com.ws3dm.app.databinding.FgForumBinding;
import com.ws3dm.app.util.AppUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Describution :论坛 （底部标签）
 * 
 * Author : DKjuan
 * 
 * Date : 2018/1/6 14:05
 **/
public class ForumFragment extends BaseFragment {

	private static final String TAG = "ForumFragment";

	private FgForumBinding mBinding;
	private List<String> tabs = new ArrayList<>();//tablayout的标签数组
	private List<Fragment> fragments = new ArrayList<>();

	private String title[] = {"排行", "板块", "置顶"};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_forum, container, false);

//        mBinding.MyUtilbar.setTitle(getResources().getString(R.string.person));
//        activity.setSupportActionBar(mBinding.MyUtilbar);

		initView();
		return mBinding.getRoot();
	}

	public void initView() {
//		mBinding.viewTop.findViewById(R.id.img_head).setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				if(MyApplication.getUserData()==null||!MyApplication.getUserData().loginStatue){
//					startActivity(new Intent(getActivity(), LoginActivity.class));
//				}else if(MyApplication.getUserData().mobile.length()==0){//第三方登陆，非绑定跳转绑定界面
//					Intent intent=new Intent(mContext, ForgetPassActivity.class);
//					intent.putExtra("type",1);//1,绑定手机号，2修改密码，3忘记密码
//					startActivity(intent);
//				}else{
//					getActivity().startActivity(new Intent(getActivity(), SetupActivity.class));
//				}
//			}
//		});
//		mBinding.viewTop.findViewById(R.id.imgSearch).setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
////				startActivity(new Intent(getActivity(), NewSearchActivity.class).putExtra("search", "GameFragment"));
//				startActivity(new Intent(getActivity(), SearchActivity.class).putExtra("search", "NewsFragment"));
////				Intent aggreement=new Intent(mContext,SingleWebActivity.class);
////				aggreement.putExtra("title","搜索");
////				aggreement.putExtra("url","https://wap.sogou.com/");
////				startActivity(aggreement);
//			}
//		});

		tabs= Arrays.asList(title);
		//排行
		FragmentForumRank rankFragment = new FragmentForumRank();
		fragments.add(rankFragment);

		//板块
		FragmentBoard boardFragment = new FragmentBoard();
		fragments.add(boardFragment);

		//置顶
		FragmentForumTopthread stickFragment = new FragmentForumTopthread();
		fragments.add(stickFragment);

		//设置TabLayout的模式
		mBinding.mTabLayout.setTabMode(TabLayout.MODE_FIXED);

//		//设置分割线
//		LinearLayout linearLayout = (LinearLayout) mBinding.mTabLayout.getChildAt(0);
//		linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//		linearLayout.setDividerDrawable(ContextCompat.getDrawable(this,
//				R.drawable.divider)); //设置分割线的样式
//		linearLayout.setDividerPadding(dip2px(10)); //设置分割线间隔

		mBinding.mViewPager.setAdapter(new TabAdapter(getChildFragmentManager(),fragments,tabs));
		mBinding.mViewPager.setOffscreenPageLimit(tabs.size());
		mBinding.mTabLayout.setupWithViewPager(mBinding.mViewPager);

		mBinding.mTabLayout.post(new Runnable() {
			@Override
			public void run() {
				AppUtil.setIndicator(mContext,mBinding.mTabLayout,25);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
//		if(MyApplication.getUserData().loginStatue){
//			GlideUtil.loadCircleImage(mContext, MyApplication.getUserData().avatarstr, (ImageView) mBinding.viewTop.findViewById(R.id.img_head));
//		}else{
//			GlideUtil.loadImage(mContext,R.drawable.no_login,(ImageView) mBinding.viewTop.findViewById(R.id.img_head));
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
}
