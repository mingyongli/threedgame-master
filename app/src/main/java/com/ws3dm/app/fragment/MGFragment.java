package com.ws3dm.app.fragment;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.TabAdapter;
import com.ws3dm.app.Constant;
import com.ws3dm.app.databinding.FgMgBinding;
import com.ws3dm.app.util.AppUtil;
import com.ws3dm.app.util.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Describution :手游（底部标签）
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2017/8/16 13:20
 **/

public class MGFragment extends BaseFragment {

	private static final String TAG = "UserFragment";

	private FgMgBinding mBinding;
	private List<String> tabs = new ArrayList<>();//tablayout的标签数组
	private List<Fragment> fragments = new ArrayList<>();
	public static int num_down=0;

	private String title[] = {"游戏", "软件", "汉化", "分类", "礼包"};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		EventBus.getDefault().register(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_mg, container, false);
		mBinding.setHandler(this);

//        mBinding.MyUtilbar.setTitle(getResources().getString(R.string.person));
//        activity.setSupportActionBar(mBinding.MyUtilbar);

		initView();
		return mBinding.getRoot();
	}

	public void initView() {
//		mBinding.imgHead.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				if (MyApplication.getUserData() == null || !MyApplication.getUserData().loginStatue) {
//					startActivity(new Intent(getActivity(), LoginActivity.class));
//				} else if (MyApplication.getUserData().mobile.length() == 0) {//第三方登陆，非绑定跳转绑定界面
//					Intent intent = new Intent(mContext, ForgetPassActivity.class);
//					intent.putExtra("type", 1);//1,绑定手机号，2修改密码，3忘记密码
//					startActivity(intent);
//				} else {
//					getActivity().startActivity(new Intent(getActivity(), SetupActivity.class));
//				}
//			}
//		});
		mBinding.imgSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
//				startActivity(new Intent(getActivity(), NewSearchActivity.class).putExtra("search", "GameFragment"));
//				Intent aggreement=new Intent(mContext,SingleWebActivity.class);
//				aggreement.putExtra("title","搜索");
//				aggreement.putExtra("url","https://wap.sogou.com/");
//				startActivity(aggreement);
			}
		});

		tabs = Arrays.asList(title);
		Fragment gameFragment = new FragmentListMG();
		Bundle bundleGame = new Bundle();
		bundleGame.putInt("type", 0);//游戏
		gameFragment.setArguments(bundleGame);
		fragments.add(gameFragment);

		Fragment softFragment = new FragmentListMG();
		Bundle bundlesoft = new Bundle();
		bundlesoft.putInt("type", 1);//软件
		softFragment.setArguments(bundlesoft);
		fragments.add(softFragment);

		FragmentMGChina chinaFragment = new FragmentMGChina();//汉化
		Bundle bundlechina = new Bundle();
		bundlechina.putString("type", "MGFragment");
		chinaFragment.setArguments(bundlechina);
		fragments.add(chinaFragment);

		Fragment cateFragment = new FragmentCategoryMG();//分类
		Bundle bundlecate = new Bundle();
		bundlecate.putString("type", "MGFragment");
		cateFragment.setArguments(bundlecate);
		fragments.add(cateFragment);

		Fragment giftFragment = new FragmentGift();//礼包
		Bundle bundlegift = new Bundle();
		bundlegift.putString("type", "MGFragment");
		giftFragment.setArguments(bundlegift);
		fragments.add(giftFragment);

		//设置TabLayout的模式
		mBinding.mTabLayout.setTabMode(TabLayout.MODE_FIXED);

//		//设置分割线
//		LinearLayout linearLayout = (LinearLayout) mBinding.mTabLayout.getChildAt(0);
//		linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//		linearLayout.setDividerDrawable(ContextCompat.getDrawable(this,
//				R.drawable.divider)); //设置分割线的样式
//		linearLayout.setDividerPadding(dip2px(10)); //设置分割线间隔

		mBinding.mViewPager.setAdapter(new TabAdapter(getChildFragmentManager(), fragments, tabs));
		mBinding.mViewPager.setOffscreenPageLimit(tabs.size());
		mBinding.mTabLayout.setupWithViewPager(mBinding.mViewPager);

		mBinding.mTabLayout.post(new Runnable() {
			@Override
			public void run() {
				AppUtil.setIndicator(mContext, mBinding.mTabLayout, 25);
			}
		});
	}
	
	public void setNum_down(int no){
//		if(no>0){
//			this.num_down+=no;
//			mBinding.tvNumDown.setText(""+num_down);
//			mBinding.tvNumDown.setVisibility(View.VISIBLE);
//		}else {
//			mBinding.tvNumDown.setVisibility(View.GONE);
//		}
	}

	public void clickHandler(View view) {
		Intent intent = null;
		switch (view.getId()) {
			case R.id.imgSearch:
//				ToastUtil.showToast(getActivity(), "手游搜索点击");
//				startActivity(new Intent(getActivity(), NewSearchActivity.class).putExtra("search", "MGFragment"));
				break;
//			case R.id.img_download:
//				startActivity(new Intent(mContext, DownListActivity.class));
//				this.setNum_down(0);
//				break;
//            case R.id.label:
//                intent=new Intent(getActivity(), PhotosWall.class);
//                startActivity(intent);
//                getActivity().setTheme(R.style.NightTheme);
//
//                View rootView = getActivity().getWindow().getDecorView();
//                ColorUiUtil.changeTheme(rootView, getActivity().getTheme());
//                if (getChildFragmentManager().getFragments() != null)
//                    for (int i = 0; i < getChildFragmentManager().getFragments().size(); i++)
//                        getChildFragmentManager().getFragments().get(i).onResume();
//                break;
			default:
				Log.e(TAG, "TODO: click handler " + view.getId());
				break;
		}
	}

	@Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.ADD_DOWNLIST)})
	public void refreshDown(String num) {
		if(num.contains("+"))
			this.setNum_down(1);
		else if(num.contains("-"))
			this.setNum_down(-1);
		else{
			this.num_down=0;
			this.setNum_down(0);
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
//		if (MyApplication.getUserData().loginStatue) {
//			GlideUtil.loadCircleImage(mContext, MyApplication.getUserData().avatarstr, mBinding.imgHead);
//		} else {
//			GlideUtil.loadImage(mContext, R.drawable.no_login, mBinding.imgHead);
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

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
