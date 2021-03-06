package com.ws3dm.app.fragment;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.MGDetailActivity;
import com.ws3dm.app.activity.SearchActivity;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.SoftGameBean;
import com.ws3dm.app.databinding.FgMgChinaBinding;
import com.ws3dm.app.mvp.model.RespBean.MGChinaRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.MGService;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.glide.GlideUtil;

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
 * Describution :汉化（手游） fragment
 * 
 * Author : DKjuan
 * 
 * Date : 2018/4/21 11:53
 **/
public class FragmentMGChina extends BaseFragment {
	private FgMgChinaBinding mBinding;
//	private Context mContext;
	private CommonRecyclerAdapter<SoftGameBean> mAdapter;
	public List<SoftGameBean> listData = new ArrayList<SoftGameBean>();
	private int mPage,mOrder=1;//排序类型:1最新2热门
	private String type,inKeyword="";//类型，内部的关键词（用于记录上一次搜索的记录）
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		mContext=getActivity();
		mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_mg_china, container, false);

		initView();
		return mBinding.getRoot();
	}

	public void initView(){
		type = getArguments().getString("type");//type 可能为SearchActivity
		if(type!=null&&type.equals("SearchActivity"))
			mBinding.llSelect.setVisibility(View.GONE);
		mPage=1;
		mOrder=1;//排序类型:1最新2热门
		
		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.recyclerview.setLayoutManager(layoutManager);

		mBinding.recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
		mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);

		mAdapter = new CommonRecyclerAdapter<SoftGameBean>(mContext, R.layout.item_game_horizon) {
			@Override
			public void bindData(RecyclerViewHolder holder, int position, final SoftGameBean item) {
//				holder.setImageByUrl(R.id.imgCover,item.getLitpic());
				GlideUtil.loadRoundImage(mContext,item.getLitpic(), (ImageView) holder.getView(R.id.imgCover),5);
				holder.setText(R.id.tv_title,item.getTitle());
				holder.setText(R.id.tv_data,"v"+item.getSoft_ver()+" | "+item.getSoft_size());
				holder.setText(R.id.tv_info,item.getDesc());
				holder.getView(R.id.bt_download).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent(mContext, MGDetailActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("mSoftGameBean",item);
						intent.putExtras(bundle);
						mContext.startActivity(intent);
//						DownloadUtil.down(mContext,item.getDownurl(),item.getTitle()+"|"+item.getLitpic());
					}
				});
			}
		};
		mBinding.recyclerview.setAdapter(mAdapter);
		mBinding.recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
			@Override
			public void onRefresh() {
				new Handler().postDelayed(new Runnable(){
					public void run() {
						mPage=1;
						if(type!=null&&type.equals("SearchActivity"))
							obtainSearch(mPage);
						else
							obtainData(mPage);
					}
				}, 50);            //refresh data here
			}

			@Override
			public void onLoadMore() {
				new Handler().postDelayed(new Runnable(){
					public void run() {
						if(type!=null&&type.equals("SearchActivity"))
							obtainSearch(mPage);
						else
							obtainData(mPage);
					}
				}, 50);
			}
		});
		mAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int position) {
				Intent intent = new Intent(mContext, MGDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("mSoftGameBean", mAdapter.getDataByPosition(position));
				intent.putExtras(bundle);
				mContext.startActivity(intent);
			}
		});
		
		mBinding.rdOn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mOrder!=1){
					mOrder=1;
					mPage=1;
					obtainData(mPage);
				}
			}
		});
		mBinding.rdOff.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mOrder!=2){
					mOrder=2;
					mPage=1;
					obtainData(mPage);
				}
			}
		});
	}

	public void obtainData(int page){
		//获取数据
		long time=System.currentTimeMillis();
		String validate= "10"+page+mOrder+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("pagesize", 10);
			obj.put("page", page);
			obj.put("order", mOrder);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
//        mForumPresenter.getForumRank(obj.toString());
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		MGService.Api service = retrofit.create(MGService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<MGChinaRespBean> call = service.getChina(body);
		call.enqueue(new Callback<MGChinaRespBean>() {
			@Override
			public void onResponse(Call<MGChinaRespBean> call, Response<MGChinaRespBean> response) {
				initData(response.body());
			}

			@Override
			public void onFailure(Call<MGChinaRespBean> call, Throwable throwable) {
				mBinding.recyclerview.loadMoreError();
			}
		});
	}

	public void obtainSearch(int page){//应用搜索
		//获取数据
		long time=System.currentTimeMillis();
		String validate= "10"+page+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("keyword", inKeyword);
			obj.put("pagesize", 10);
			obj.put("page", page);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
//        mForumPresenter.getForumRank(obj.toString());
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		MGService.Api service = retrofit.create(MGService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<MGChinaRespBean> call = service.getSearchMG(body);
		call.enqueue(new Callback<MGChinaRespBean>() {
			@Override
			public void onResponse(Call<MGChinaRespBean> call, Response<MGChinaRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				initData(response.body());
			}

			@Override
			public void onFailure(Call<MGChinaRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
				mBinding.recyclerview.loadMoreError();
			}
		});
	}
	
	@Override
	protected void onFragmentFirstVisible() {
		super.onFragmentFirstVisible();
		if (!"SearchActivity".equals(getArguments().getString("type")))
			mBinding.recyclerview.setRefreshing(true);//refresh的监听处理中已经有加载数据的操作
	}
	
	@Override
	protected void onFragmentVisibleChange(boolean isVisible){
		super.onFragmentVisibleChange(isVisible);
		if(isVisible){
			if (getArguments().getString("type")!=null&&"SearchActivity".equals(getArguments().getString("type"))&&!inKeyword.equals(SearchActivity.keyWord)){
				mBinding.recyclerview.setRefreshing(true);//refresh的监听处理中已经有加载数据的操作
				inKeyword=SearchActivity.keyWord;
			}
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (mAdapter != null)
			mAdapter.notifyDataSetChanged();
	}

	private void initData(MGChinaRespBean bean) {
		mBinding.recyclerview.refreshComplete();
		if (bean != null && bean.getData() != null) {
			listData = bean.getData().getList();
		}
		if (mPage > 1) {
			mAdapter.appendList(listData);
		} else {
			mAdapter.clearAndAddList(listData);
		}
		if (listData == null || listData.size() < 1) {
			mBinding.recyclerview.setNoMore(true);
		} else {
			mPage++;
		}
	}
}