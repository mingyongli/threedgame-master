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
import com.ws3dm.app.activity.NewsActivity;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.mvp.model.RespBean.NewsListRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.NewsService;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;
import com.ws3dm.app.R;
import com.ws3dm.app.databinding.FgVideoBinding;
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
 * Describution :单个视频列表
 * 
 * Author : DKjuan
 * 
 * Date : 2017/8/18 17:22
 **/
public class FragmentVideo extends BaseFragment {
	private FgVideoBinding mBinding;
	private CommonRecyclerAdapter<NewsBean> mAdapter;
	public List<NewsBean> listData = new ArrayList<NewsBean>();
	private int aid = 0;//游戏列表用到

	private String type;

	private int mPage=1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_video, container, false);

		initView();
		return mBinding.getRoot();
	}

	public void initView(){
		type= (String) getArguments().get("type");
		if(type!=null&&type.equals("GameVideoActivity")){
			aid=getArguments().getInt("aid");
		}

		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.recyclerview.setLayoutManager(layoutManager);

		mBinding.recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
		mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);


//		//添加分割线
//		if (mListV3DividerItemDecoration == null) {
//			mListV3DividerItemDecoration = new DividerItemDecorationAdapter(mContext);
//		} else {
//			mBinding.recyclerview.removeItemDecoration(mListV3DividerItemDecoration);
//		}
//		mBinding.recyclerview.addItemDecoration(mListV3DividerItemDecoration);

		mAdapter = new CommonRecyclerAdapter<NewsBean>(mContext, R.layout.adapter_video) {
			@Override
			public void bindData(RecyclerViewHolder holder, final int position, final NewsBean item) {
				ImageView imgCover = holder.getView(R.id.imgCover);
				GlideUtil.loadImage(mContext,item.getLitpic(),imgCover,R.drawable.video_loading, R.drawable.video_error);

//				holder.setImageByUrl(R.id.img_head,item.getUser().avatarstr);
				GlideUtil.loadCircleImage(mContext,item.getUser().avatarstr,(ImageView)holder.getView(R.id.img_head));
				holder.setText(R.id.tv_name, item.getUser().nickname);
				holder.setText(R.id.tv_time,TimeUtil.getFoolishTime2(""+item.getPubdate_at()+"000"));
				holder.setText(R.id.tv_num, item.getTotal_ct()+"");
				holder.setText(R.id.tv_title, item.getTitle());
//				TextView tv_title=holder.getView(R.id.tv_title);
//				tv_title.setText(item.getTitle());
//				tv_title.setTextColor(item.getHavesee()==0?0xff2a2a2a:0xff999999);
			}
		};
		mAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int position) {
				mAdapter.getDataByPosition(position).setHavesee(1);
				NewsBean news = mAdapter.getDataByPosition(position);
//				news.setSenddate(TimeUtil.dateDayNow());
//				newsCollectFile.addHistory(news);
				Intent intent = new Intent(mContext, NewsActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("newsBean",news);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		mBinding.recyclerview.setAdapter(mAdapter);
		mBinding.recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
			@Override
			public void onRefresh() {
				new Handler().postDelayed(new Runnable(){
					public void run() {
						mPage=1;
						if(type.equals("NewsFragment"))
							obtainData(mPage);
						else
							obtainGameData(mPage);
					}
				}, 50);            //refresh data here
			}

			@Override
			public void onLoadMore() {
				new Handler().postDelayed(new Runnable(){
					public void run() {
						if(type.equals("NewsFragment"))
							obtainData(mPage);
						else
							obtainGameData(mPage);
					}
				}, 50);
			}
		});
		mBinding.recyclerview.setRefreshing(true);
	}

	protected void onFragmentFirstVisible(){
//		mBinding.recyclerview.setRefreshing(true);//refresh的监听处理中已经有加载数据的操作
	}

//	@Override
//	protected void onFragmentVisibleChange(boolean isVisible) {
//		super.onFragmentVisibleChange(isVisible);
////		if (!strType.equals("CollectActivity"))
//			if (isVisible)
//				if (isFirst) {
//					isFirst = false;
//					mBinding.recyclerview.setRefreshing(true);//refresh的监听处理中已经有加载数据的操作
//
//					if (strType.equals("GameActivity")){
//						mBinding.recyclerview.setPullRefreshEnabled(false);
//					}
////					if (!strType.equals("CollectActivity")) {
////						mBinding.recyclerview.setRefreshing(true);
////					}
//				}
//	}

	@Override
	public void onResume() {
		super.onResume();
		if (mAdapter != null)
			mAdapter.notifyDataSetChanged();
	}

	public void obtainData(int page){//获取新闻数据
		long time=System.currentTimeMillis();
		String validate= "10"+page+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
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
		NewsService.Api service = retrofit.create(NewsService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<NewsListRespBean> call = service.getNewsVideo(body);
		call.enqueue(new Callback<NewsListRespBean>() {
			@Override
			public void onResponse(Call<NewsListRespBean> call, Response<NewsListRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				initData(response.body());
			}

			@Override
			public void onFailure(Call<NewsListRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
				mBinding.recyclerview.loadMoreError();
			}
		});
	}

	public void obtainGameData(int page){//获取游戏相关数据
		long time=System.currentTimeMillis();
		String validate= ""+aid+"10"+page+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("aid", aid);
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
		NewsService.Api service = retrofit.create(NewsService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<NewsListRespBean> call = service.getGameVideo(body);
		call.enqueue(new Callback<NewsListRespBean>() {
			@Override
			public void onResponse(Call<NewsListRespBean> call, Response<NewsListRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				initData(response.body());
			}

			@Override
			public void onFailure(Call<NewsListRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
				mBinding.recyclerview.loadMoreError();
			}
		});
	}

	private void initData(NewsListRespBean bean) {
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

	public void reLoad(){
		mBinding.recyclerview.scrollToPosition(0);
//		mBinding.recyclerview.setRefreshing(true);
//		mPage=1;
//		obtainData(mPage);
	}
}