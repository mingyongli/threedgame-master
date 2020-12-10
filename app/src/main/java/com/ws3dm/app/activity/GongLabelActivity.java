package com.ws3dm.app.activity;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.RequiresApi;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.TabAdapter;
import com.ws3dm.app.bean.GonglueBean;
import com.ws3dm.app.fragment.FragmentLabel;
import com.ws3dm.app.mvp.model.RespBean.NewsGLzqlabelpaReslRespBean;
import com.ws3dm.app.mvp.model.RespBean.NewsGonglueReslRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.NewsService;
import com.ws3dm.app.util.AppUtil;
import com.ws3dm.app.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Describution : 攻略专区攻略标签详情
 * 
 * Author : DKjuan
 * 
 * Date : 2019/9/28 10:53
 **/
public class GongLabelActivity extends BaseActivity implements View.OnClickListener{

	private CommonRecyclerAdapter<GonglueBean> mAdapter;
	private GridLayoutManager manager;
	public List<GonglueBean> listData =new ArrayList<>();
	private int mPage,showtype,aid,isAll=0,currentShow,zq_id,position;//17单机18手游19网游   isAll是否为所有攻略0不是1是
	private LinearLayout imgReturn;
	private TextView mTitle;
	private XRecyclerView recyclerview;
	private RadioGroup radioCategory;
	private int inId,totalSize;//id 攻略标签详情编号

	private List<String> tabs = new ArrayList<>();//tablayout的标签数组
	private List<Fragment> fragments = new ArrayList<>();
	private static TabAdapter mTabAdapter;
	private TabLayout mTabLayout;
	private ViewPager mViewPager;

	@Override
	protected void init() {
		setContentView(R.layout.ac_tujian);

		imgReturn= (LinearLayout) findViewById(R.id.imgReturn);
		mTitle= (TextView) findViewById(R.id.tv_title);
		recyclerview= (XRecyclerView) findViewById(R.id.recyclerview);
		radioCategory= (RadioGroup) findViewById(R.id.radio_category);
		mTabLayout= (TabLayout) findViewById(R.id.mTabLayout);
		mViewPager= (ViewPager) findViewById(R.id.mViewPager);
		imgReturn.setOnClickListener(this);

		showtype=getIntent().getIntExtra("showtype",0);
		zq_id=getIntent().getIntExtra("zq_id",0);
		aid=getIntent().getIntExtra("aid",0);
		position=getIntent().getIntExtra("position",0);
		mTitle.setText(getIntent().getStringExtra("title"));

		obtainFirstData();
	}

	//界面初始化
	private void initView(NewsGonglueReslRespBean bean) {
		if(bean==null||bean.getData()==null)
			return;
		totalSize=bean.getData().getList().size();
		for(int i=0;i<totalSize;i++)
			tabs.add(bean.getData().getList().get(i).getName());
		if(totalSize==1)
			mTabLayout.setVisibility(View.GONE);

		for (int i=0;i<totalSize;i++){
			Fragment mFragmentLabel = new FragmentLabel();
			Bundle bundle = new Bundle();
			bundle.putInt("showtype",showtype);
			bundle.putInt("zq_id",zq_id);
			bundle.putInt("aid",aid);
			bundle.putInt("inId",bean.getData().getList().get(i).getId());
			bundle.putInt("inTotal",bean.getData().getList().get(i).getTotal());
			bundle.putInt("position",position);
//			bundle.putSerializable("game", finalGame);//数据太大，超出系统限制
			bundle.putString("str_tujian", JSON.toJSONString(bean.getData().getList().get(i).getList()));
			mFragmentLabel.setArguments(bundle);
			fragments.add(mFragmentLabel);
		}

		//设置TabLayout的模式
		mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE );

//		//设置分割线
//		LinearLayout linearLayout = (LinearLayout) mBinding.mTabLayout.getChildAt(0);
//		linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//		linearLayout.setDividerDrawable(ContextCompat.getDrawable(this,
//				R.drawable.divider)); //设置分割线的样式
//		linearLayout.setDividerPadding(dip2px(10)); //设置分割线间隔
		mTabAdapter=new TabAdapter(getSupportFragmentManager(),fragments,tabs);
		mViewPager.setAdapter(mTabAdapter);
		mViewPager.setOffscreenPageLimit(tabs.size());
		mTabLayout.setupWithViewPager(mViewPager);

		mTabLayout.post(new Runnable() {
			@Override
			public void run() {
				AppUtil.setIndicator(mContext,mTabLayout,25);
			}
		});
		mTabLayout.setVisibility(View.VISIBLE);
		if(totalSize<=1){
			mTabLayout.setVisibility(View.GONE);
		}else{
			mTabLayout.setVisibility(View.VISIBLE);
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	public void clickHandler(View view) {
		switch (view.getId()) {
			case R.id.imgReturn:// 返回
				onBackPressed();
				break;
		}
	}

	public void obtainFirstData(){//获取小分类攻略列表
		//获取数据
		long time=System.currentTimeMillis();
		String validate= ""+showtype+zq_id+aid+position+20+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("showtype", showtype);
			obj.put("zq_id", zq_id);
			obj.put("aid", aid);
			obj.put("position", position);
			obj.put("pagesize", 20);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
//        mForumPresenter.getForumRank(obj.toString());
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		NewsService.Api service = retrofit.create(NewsService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<NewsGonglueReslRespBean> call = service.getSpecialLabel(body);
		call.enqueue(new Callback<NewsGonglueReslRespBean>() {
			@Override
			public void onResponse(Call<NewsGonglueReslRespBean> call, Response<NewsGonglueReslRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				initView(response.body());
			}

			@Override
			public void onFailure(Call<NewsGonglueReslRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
				recyclerview.loadMoreError();
			}
		});
	}

	private void initCateData(final NewsGonglueReslRespBean bean) {
		recyclerview.refreshComplete();
		listData=bean.getData().getList().get(currentShow).getList();
		if(mPage>1) {
			mAdapter.appendList(listData);
		}else {
			totalSize=bean.getData().getList().get(currentShow).getTotal();
			// 初始化按钮
			radioCategory.removeAllViews();
			if (radioCategory.getChildCount() == 0) {
				radioCategory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
						// 点击分类右侧图书跳转
						int forSize = group.getChildCount();
						for (int i = 0; i < forSize; i++) {
							int id = group.getChildAt(i).getId();
							if (id == checkedId) {
								mPage=1;
								currentShow = i;
								totalSize=bean.getData().getList().get(i).getTotal();
								inId=bean.getData().getList().get(i).getId();
								mAdapter.clearAndAddList(bean.getData().getList().get(i).getList());
								if(mAdapter.getItemCount()<totalSize){
									mPage++;
								}else{
									recyclerview.setNoMore(true);
								}
								return;
							}
						}
					}
				});

				int forSize = bean.getData().getList().size();
				for (int i = 0; i < forSize; i++) {
					RadioButton button = (RadioButton) LayoutInflater.from(mContext).inflate(R.layout.layout_radiobutton, null);
					button.setText(bean.getData().getList().get(i).getName());
					AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT);
					button.setLayoutParams(params);
					radioCategory.addView(button);
					if (i == currentShow) {
						button.setChecked(true);
					}
				}
				inId=bean.getData().getList().get(0).getId();
			}
			mAdapter.clearAndAddList(listData);
		}
		if(mAdapter.getItemCount()<totalSize){
			mPage++;
		}else{
			recyclerview.setNoMore(true);
		}
	}

	public void obtainMoreData(){//获取小分类攻略列表
		//获取数据
		long time=System.currentTimeMillis();
		String validate= ""+showtype+aid+inId+20+mPage+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("showtype", showtype);
			obj.put("aid", aid);
			obj.put("id", inId);
			obj.put("pagesize", 20);
			obj.put("page", mPage);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
//        mForumPresenter.getForumRank(obj.toString());
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		NewsService.Api service = retrofit.create(NewsService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<NewsGLzqlabelpaReslRespBean> call = service.getSpecLabPage(body);
		call.enqueue(new Callback<NewsGLzqlabelpaReslRespBean>() {
			@Override
			public void onResponse(Call<NewsGLzqlabelpaReslRespBean> call, Response<NewsGLzqlabelpaReslRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				initLabelPageData(response.body());
			}

			@Override
			public void onFailure(Call<NewsGLzqlabelpaReslRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
				recyclerview.loadMoreError();
			}
		});
	}

	private void initLabelPageData(final NewsGLzqlabelpaReslRespBean bean) {
		recyclerview.refreshComplete();
		mAdapter.appendList(bean.getData().getList());
		if(mAdapter.getItemCount()<totalSize){
			mPage++;
		}else{
			recyclerview.setNoMore(true);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.imgReturn:
				onBackPressed();
				break;
			default:
				break;
		}
	}
}