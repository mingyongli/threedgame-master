package com.ws3dm.app.fragment;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.NewsActivity;
import com.ws3dm.app.adapter.BaseRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.databinding.FgNewsListBinding;
import com.ws3dm.app.mvp.model.RespBean.NewsSpecilReslRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.NewsService;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;

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
 * Describution : 游戏 --  新闻
 * 
 * Author : DKjuan
 * 
 * Date : 2019/10/9 14:04
 **/
public class FragmentGameNews extends BaseFragment {	
	private FgNewsListBinding mBinding;
	private BaseRecyclerAdapter<NewsBean> mAdapter;
//	private NewsMultiAdapter mAdapter;
	public List<NewsBean> listData = new ArrayList<NewsBean>();
	private int showtype,aid = 0;//游戏新闻列表用到
//	private NewsFile newsCollectFile;
//	private String dataType;//dataType 1,新闻  2.攻略  3.视频
	
	private int mPage=1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_news_list, container, false);

		initView();
		return mBinding.getRoot();
	}
	
	public void initView(){
//		newsCollectFile = new NewsFile(getActivity());
		showtype=getArguments().getInt("showtype");
		aid=getArguments().getInt("aid");

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

		mAdapter = new BaseRecyclerAdapter<NewsBean>(mContext, R.layout.adapter_news_one_right_pic) {
			@Override
			public void bindData(RecyclerViewHolder holder, final int position, final NewsBean item) {
//				LinearLayout ll_user=holder.getView(R.id.ll_user);
				holder.setText(R.id.tv_time_game,TimeUtil.getTimeEN(item.getPubdate_at()));
				holder.getView(R.id.tv_time_game).setVisibility(View.VISIBLE);
				holder.getView(R.id.ll_user).setVisibility(View.GONE);
				holder.setText(R.id.txtTitle, item.getTitle());
				holder.setText(R.id.txtComment, item.getTotal_ct()+"");
				holder.setText(R.id.tv_time, TimeUtil.getFriendlyTime(""+item.getPubdate_at()+"000"));
				holder.setImageByUrl(R.id.imgArrayOne,item.getLitpic());
				if(item.getShowtype()==10){//视频
					holder.getView(R.id.img_play).setVisibility(View.VISIBLE);
				}else{
					holder.getView(R.id.img_play).setVisibility(View.GONE);
				}
			}
		};
//		NewsMultiItemTypeSupport modelMultiItemTypeSupport = new NewsMultiItemTypeSupport();
//		mAdapter= new NewsMultiAdapter(mContext, modelMultiItemTypeSupport);
		mAdapter.setOnClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int position) {
				mAdapter.getDataByPosition(position).setHavesee(1);
				NewsBean news = mAdapter.getDataByPosition(position);
				news.setSeeDate(TimeUtil.dateDayNow());
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
						obtainGameData(mPage);
					}
				}, 50);            //refresh data here
			}

			@Override
			public void onLoadMore() {
				new Handler().postDelayed(new Runnable(){
					public void run() {
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
	
	public void obtainGameData(int page){//获取游戏相关数据
		long time=System.currentTimeMillis();
		String validate= ""+showtype+aid+"10"+page+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("showtype", showtype);
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
		Call<NewsSpecilReslRespBean> call = service.getGLSpecialNewList(body);
		call.enqueue(new Callback<NewsSpecilReslRespBean>() {
			@Override
			public void onResponse(Call<NewsSpecilReslRespBean> call, Response<NewsSpecilReslRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				initData(response.body());
			}

			@Override
			public void onFailure(Call<NewsSpecilReslRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
				mBinding.recyclerview.loadMoreError();
			}
		});
	}
	
	private void initData(NewsSpecilReslRespBean bean) {
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