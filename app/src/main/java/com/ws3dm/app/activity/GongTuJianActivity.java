package com.ws3dm.app.activity;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.TabAdapter;
import com.ws3dm.app.bean.GonglueBean;
import com.ws3dm.app.databinding.AcTujianBinding;
import com.ws3dm.app.fragment.FragmentTuJian;
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
 * Describution : 攻略图鉴
 * 
 * Author : DKjuan
 * 
 * Date : 2019/9/28 10:53
 **/
public class GongTuJianActivity extends BaseActivity{

	private AcTujianBinding mBinding;
	private CommonRecyclerAdapter<GonglueBean> mAdapter;
	public List<GonglueBean> listData =new ArrayList<>();
	private int showtype,aid,totalSize;//17单机18手游19网游   isAll是否为所有攻略0不是1是

	private List<String> tabs = new ArrayList<>();//tablayout的标签数组
	private List<Fragment> fragments = new ArrayList<>();
	private static TabAdapter mTabAdapter;

	@Override
	protected void init() {
		mBinding = bindView(R.layout.ac_tujian);
		mBinding.setHandler(this);

		showtype=getIntent().getIntExtra("showtype",0);
		aid=getIntent().getIntExtra("aid",0);
		mBinding.tvTitle.setText(getIntent().getStringExtra("title"));

		obtainFirstData();
	}

	//界面初始化
	private void initView(NewsGonglueReslRespBean bean) {//17单机18手游19网游
		totalSize=bean.getData().getList().size();
		for(int i=0;i<totalSize;i++)
			tabs.add(bean.getData().getList().get(i).getName());
		if(totalSize==1)
			mBinding.mTabLayout.setVisibility(View.GONE);

		for (int i=0;i<totalSize;i++){
			Fragment mFragmentTuJian = new FragmentTuJian();
			Bundle bundle = new Bundle();
			bundle.putInt("showtype",showtype);
			bundle.putInt("aid",aid);
			bundle.putInt("inId",bean.getData().getList().get(i).getId());
			bundle.putInt("inTotal",bean.getData().getList().get(i).getTotal());
//			bundle.putSerializable("game", finalGame);//数据太大，超出系统限制
			bundle.putString("str_tujian", JSON.toJSONString(bean.getData().getList().get(i).getList()));
			mFragmentTuJian.setArguments(bundle);
			fragments.add(mFragmentTuJian);
		}

		//设置TabLayout的模式
		mBinding.mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

//		//设置分割线
//		LinearLayout linearLayout = (LinearLayout) mBinding.mTabLayout.getChildAt(0);
//		linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//		linearLayout.setDividerDrawable(ContextCompat.getDrawable(this,
//				R.drawable.divider)); //设置分割线的样式
//		linearLayout.setDividerPadding(dip2px(10)); //设置分割线间隔
		mTabAdapter=new TabAdapter(getSupportFragmentManager(),fragments,tabs);
		mBinding.mViewPager.setAdapter(mTabAdapter);
		mBinding.mViewPager.setOffscreenPageLimit(tabs.size());
		mBinding.mTabLayout.setupWithViewPager(mBinding.mViewPager);

		mBinding.mTabLayout.post(new Runnable() {
			@Override
			public void run() {
				AppUtil.setIndicator(mContext,mBinding.mTabLayout,25);
			}
		});
		if(totalSize<=1){
			mBinding.mTabLayout.setVisibility(View.GONE);
		}else{
			mBinding.mTabLayout.setVisibility(View.VISIBLE);
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

	public void obtainFirstData(){//获取所有攻略列表
		//获取数据
		long time=System.currentTimeMillis();
		String validate= ""+showtype+aid+28+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("showtype", showtype);
			obj.put("aid", aid);
			obj.put("pagesize", 28);
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
		Call<NewsGonglueReslRespBean> call = service.getGLSpecialPic(body);
		call.enqueue(new Callback<NewsGonglueReslRespBean>() {
			@Override
			public void onResponse(Call<NewsGonglueReslRespBean> call, Response<NewsGonglueReslRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				initView(response.body());
			}

			@Override
			public void onFailure(Call<NewsGonglueReslRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
			}
		});
	}
}