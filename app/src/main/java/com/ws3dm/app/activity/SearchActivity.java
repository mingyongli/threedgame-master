package com.ws3dm.app.activity;

import android.graphics.Rect;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.BaseRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.adapter.TabAdapter;
import com.ws3dm.app.databinding.AcSearchBinding;
import com.ws3dm.app.fragment.SearchFragment;
import com.ws3dm.app.fragment.SearchGameFragment;
import com.ws3dm.app.mvp.model.RespBean.HotSearchRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.NewsService;
import com.ws3dm.app.util.AppUtil;
import com.ws3dm.app.util.SharedUtil;
import com.ws3dm.app.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Describution :搜索界面
 * 
 * Author : DKjuan
 * 
 * Date : 2017/9/4 16:32
 **/
public class SearchActivity extends BaseActivity{

	private AcSearchBinding mBinding;
	private List<String> tabs = new ArrayList<>();//tablayout的标签数组
	private List<Fragment> fragments = new ArrayList<>();

	private String title[] = {"游戏", "文章","攻略","论坛"};
	private BaseRecyclerAdapter<HotSearchRespBean.DataBean.SearchBean> mRecyclerAdapter;
	private List<HotSearchRespBean.DataBean.SearchBean> hotSearchMore=new ArrayList<>();//热门索索记录
	private List<HotSearchRespBean.DataBean.SearchBean> hotSearchLess=new ArrayList<>();//热门索索记录
	private List<HotSearchRespBean.DataBean.SearchBean> listData=new ArrayList<>();//所有的对象合集，包括历史记录和热门记录
	public static String lastKey="",keyWord="";
	private boolean isOpen=false;
	
	@Override
	protected void init() {
		mBinding=bindView(R.layout.ac_search);
		mBinding.setHandler(this);
		
		initView();
	}
	
	private void initView() {
		mBinding.etSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				if (charSequence.length() > 0)
					mBinding.imgClean.setVisibility(View.VISIBLE);
				else {
					mBinding.imgClean.setVisibility(View.GONE);
				}
				//刷新界面
				mBinding.rlHistory.setVisibility(View.VISIBLE);
				mBinding.rlContent.setVisibility(View.GONE);
			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});

		mBinding.etSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mBinding.etSearch.setFocusable(true);
				mBinding.etSearch.setFocusableInTouchMode(true);
				mBinding.etSearch.requestFocus();
			}
		});
		mBinding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId== EditorInfo.IME_ACTION_SEARCH){
					mBinding.txtSearch.performClick ();
				}
				return false;
			}
		});
		
		mBinding.tvDel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mBinding.tvDel.setVisibility(View.INVISIBLE);
				listData.clear();
				SharedUtil.writePreferences(mContext, "search_data", JSON.toJSONString(listData));
				mRecyclerAdapter.clearAndAddList(listData);
			}
		});

		//初始化recyclerview
		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.mRecycleView.setLayoutManager(layoutManager);
		mBinding.mRecycleView.setPullRefreshEnabled(false);
		mBinding.mRecycleView.setLoadingMoreEnabled(false);
		mBinding.mRecycleView.setEmptyView(mBinding.tvNodate);

		mRecyclerAdapter = new BaseRecyclerAdapter<HotSearchRespBean.DataBean.SearchBean>(mContext, R.layout.item_search) {
			@Override
			public void bindData(RecyclerViewHolder holder, int position, final HotSearchRespBean.DataBean.SearchBean item) {
				holder.getView(R.id.img_del).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						delOneSearch(item);
					}
				});
				holder.setText(R.id.body_title,item.getKeyword());
			}
		};
		mRecyclerAdapter.setOnClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int posi) {
				keyWord=listData.get(posi).getKeyword();
				mBinding.etSearch.setText(keyWord);
				mBinding.etSearch.setSelection(mBinding.etSearch.getText().length());
				mBinding.txtSearch.performClick ();
			}
		});
		mBinding.mRecycleView.setAdapter(mRecyclerAdapter);

		//设置TabLayout的模式
		mBinding.mTabLayout.setTabMode(TabLayout.MODE_FIXED);
		mBinding.mTabLayout.post(new Runnable() {
			@Override
			public void run() {
				AppUtil.setIndicator(mContext,mBinding.mTabLayout,105);
			}
		});
		obtainData();
	}
	
	private void initFragment(){
		if(fragments.size()>0)
			fragments.clear();
		//初始化viewpage
		tabs= Arrays.asList(title);

		SearchGameFragment mSearchGameFragment= new SearchGameFragment();//汉化
		Bundle bundle = new Bundle();
		bundle.putInt("type",1);//1单机专区2新闻3攻略4论坛
		mSearchGameFragment.setArguments(bundle);
		fragments.add(mSearchGameFragment);
		for(int i=2;i<5;i++){
			SearchFragment mSearchFragment= new SearchFragment();
			Bundle bundleApp = new Bundle();
			bundleApp.putInt("type",i);//1单机专区2新闻3攻略4论坛
			mSearchFragment.setArguments(bundleApp);
			fragments.add(mSearchFragment);
		}

//		//设置分割线
//		LinearLayout linearLayout = (LinearLayout) mBinding.mTabLayout.getChildAt(0);
//		linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//		linearLayout.setDividerDrawable(ContextCompat.getDrawable(this,
//				R.drawable.divider)); //设置分割线的样式
//		linearLayout.setDividerPadding(dip2px(10)); //设置分割线间隔

		mBinding.mViewPager.setAdapter(new TabAdapter(getSupportFragmentManager(),fragments,tabs));
		mBinding.mViewPager.setOffscreenPageLimit(tabs.size());
		mBinding.mTabLayout.setupWithViewPager(mBinding.mViewPager);
	}
	private void obtainData(){
		long time=System.currentTimeMillis();
		String sign= StringUtil.MD5(""+time);
		JSONObject obj = new JSONObject();
		try {
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		NewsService.Api service = retrofit.create(NewsService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<HotSearchRespBean> call = service.getNewhotso(body);
		call.enqueue(new Callback<HotSearchRespBean>() {
			@Override
			public void onResponse(Call<HotSearchRespBean> call, Response<HotSearchRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				initData(response.body());
			}

			@Override
			public void onFailure(Call<HotSearchRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
			}
		});
	}
	
	private void initData(HotSearchRespBean bean){
		if(bean==null||bean.getData()==null){
			return;
		}
		hotSearchMore =bean.getData().getList();
		if(hotSearchMore.size()>10){
			for(int i=0;i<10;i++)
				hotSearchLess.add(hotSearchMore.get(i));
		}else{
			hotSearchLess=hotSearchMore;
		}
		
		setFlowView(hotSearchLess);
		//从share里面读取历史记录
		String json = SharedUtil.readPreferences(mContext, "search_data");
		if (!StringUtil.isEmpty(json)&&json.length()>10) {
			listData=JSON.parseArray(json, HotSearchRespBean.DataBean.SearchBean.class);
//		} else {
//			List<HotSearchRespBean.DataBean.ListBean.ShouyouBean> tempList=new ArrayList<>();
//			oldBean.setList(tempList);
		}

		if(listData.size()>0)
			mBinding.tvDel.setVisibility(View.VISIBLE);
		mRecyclerAdapter.clearAndAddList(listData);
		
		mBinding.imgMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isOpen){
					isOpen=false;
					setFlowView(hotSearchLess);
					mBinding.imgMore.setImageResource(R.drawable.img_search_down);
				}else{
					isOpen=true;
					setFlowView(hotSearchMore);
					mBinding.imgMore.setImageResource(R.drawable.img_search_up);
				}
			}
		});
	}

	private void addOneSearch(String keys) {
		List<HotSearchRespBean.DataBean.SearchBean> searchData;
		HotSearchRespBean.DataBean.SearchBean newBean=new HotSearchRespBean.DataBean.SearchBean();
		newBean.setId(1);
		newBean.setKeyword(keys);
		String json = SharedUtil.readPreferences(mContext, "search_data");
		if (!StringUtil.isEmpty(json)) {
			searchData=JSON.parseArray(json, HotSearchRespBean.DataBean.SearchBean.class);
			if(searchData.contains(newBean)) {
				searchData.remove(newBean);
				listData.remove(newBean);
			}
			listData.add(0, newBean);
			searchData.add(0, newBean);
			if(searchData.size()>10) {
				searchData.remove(10);
				listData.remove(10);
			}
		} else {
			searchData=new ArrayList<>();
			searchData.add(newBean);
			listData.add(newBean);
		}
		SharedUtil.writePreferences(mContext, "search_data",JSON.toJSONString(searchData));
	}

	private void delOneSearch(HotSearchRespBean.DataBean.SearchBean target) {
		listData.remove(target);
		SharedUtil.writePreferences(mContext, "search_data", JSON.toJSONString(listData));
		mRecyclerAdapter.clearAndAddList(listData);
		if(listData.size()==0)
			mBinding.tvDel.setVisibility(View.INVISIBLE);
	}
	
	private void setFlowView(List<HotSearchRespBean.DataBean.SearchBean> mList){
		mBinding.flLabel.removeAllViews();
		for (int i = 0; i <mList.size();i++) {
			final TextView textView = new TextView(mContext);
			ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.setMargins(20,5,5,20);
			textView.setLayoutParams(layoutParams);
			textView.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_gray));
			textView.setPadding(15,9,15,9);
			textView.setGravity(Gravity.CENTER);
			textView.setText(""+mList.get(i).getKeyword());
			textView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mBinding.etSearch.setText(textView.getText().toString());
					mBinding.etSearch.setSelection(mBinding.etSearch.getText().length());
					mBinding.txtSearch.performClick ();
				}
			});
			mBinding.flLabel.addView(textView);
		}
	}
	
	public void clickHandler(View view) {
		switch (view.getId()) {
			case R.id.imgReturn:
				if(mBinding.etSearch.getText().toString().length()>0){
					mBinding.imgClean.performClick();
				}else{
					onBackPressed();
				}
				break;
			case R.id.imgClean:
				if(listData.size()>0)
					mBinding.tvDel.setVisibility(View.VISIBLE);
				mRecyclerAdapter.clearAndAddList(listData);
				mBinding.etSearch.setText("");//刷新界面
				mBinding.rlContent.setVisibility(View.GONE);
				mBinding.rlHistory.setVisibility(View.VISIBLE);
				break;
			case R.id.txtSearch://点击搜索
				if(mBinding.etSearch.getText().toString().length()>0){
					AppUtil.closeKeyboard(mContext);
					//刷新界面
					mBinding.rlContent.setVisibility(View.VISIBLE);
					mBinding.rlHistory.setVisibility(View.GONE);
					keyWord=mBinding.etSearch.getText().toString();
					//添加到本地历史搜索记录
					addOneSearch(keyWord);
					initFragment();
				}
				break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(mBinding.etSearch.getText().toString().length()>0){
				mBinding.imgClean.performClick();
			}else{
				onBackPressed();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 分发点击除 EditText 外区域，当前焦点在 ET 上，EditText 取消焦点
	 *
	 * @param event
	 * @return
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		View v = getCurrentFocus();
		if (v != null && v instanceof EditText) {
			Rect outRect = new Rect();
			v.getGlobalVisibleRect(outRect);
			if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
				v.clearFocus();
				AppUtil.closeKeyboard(mContext);
			}
		}
		return super.dispatchTouchEvent(event);
	}
}