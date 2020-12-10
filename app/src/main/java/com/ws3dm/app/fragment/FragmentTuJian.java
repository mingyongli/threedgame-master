package com.ws3dm.app.fragment;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.NewsActivity;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.GonglueBean;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.databinding.FgBaseRecyclerviewBinding;
import com.ws3dm.app.mvp.model.RespBean.NewsGLzqlabelpaReslRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.NewsService;
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
 * Describution :图鉴 fragment
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2017/8/18 17:22
 **/
public class FragmentTuJian extends BaseFragment {
	private FgBaseRecyclerviewBinding mBinding;
	private CommonRecyclerAdapter<GonglueBean> mAdapter;
	public List<GonglueBean> listData = new ArrayList<GonglueBean>();
	private List<GonglueBean> mList;
	private int mPage = 2, showtype, inId, aid, totalSize, currentShow;//17单机18手游19网游   isAll是否为所有攻略0不是1是

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		AppCompatActivity activity = (AppCompatActivity) getActivity();

		mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_base_recyclerview, container, false);
//		mBinding.setHandler(this);

		initView();

		return mBinding.getRoot();
	}

	public void initView() {
		showtype = getArguments().getInt("showtype", 0);
		aid = getArguments().getInt("aid", 0);
		inId = getArguments().getInt("inId", 0);
		totalSize = getArguments().getInt("inTotal", 0);
		String str_game = getArguments().getString("str_tujian");
		mList = JSON.parseArray(str_game, GonglueBean.class);

		GridLayoutManager layoutManager = new GridLayoutManager(mContext, 4);
		layoutManager.setOrientation(GridLayoutManager.VERTICAL);
		mBinding.recyclerview.setLayoutManager(layoutManager);

		mBinding.recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
		mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);

		mAdapter = new CommonRecyclerAdapter<GonglueBean>(mContext, R.layout.item_mg) {
			@Override
			public void bindData(RecyclerViewHolder holder, int position, GonglueBean bean) {
				holder.setImageByUrl(R.id.img_top, bean.getLitpic());
				holder.setText(R.id.tv_title, bean.getTitle());
			}
		};
		mAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int position) {
				GonglueBean bean = mAdapter.getDataByPosition(position);
				NewsBean news = new NewsBean();
				news.setTitle(bean.getTitle());
				news.setArcurl(bean.getArcurl());
				news.setWebviewurl(bean.getWebviewurl());
				news.setType("" + bean.getShowtype());
				Intent intent = new Intent(mContext, NewsActivity.class);
				intent.putExtra("isGongLue",1);
				Bundle bundle = new Bundle();
				bundle.putSerializable("newsBean", news);
				intent.putExtras(bundle);
				mContext.startActivity(intent);
			}
		});

		mBinding.recyclerview.setAdapter(mAdapter);
		mBinding.recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
			@Override
			public void onRefresh() {
				new Handler().postDelayed(new Runnable() {
					public void run() {
						mAdapter.clearAndAddList(mList);
						mPage = 2;
						mBinding.recyclerview.refreshComplete();
						if (mList.size()== totalSize) {
							mBinding.recyclerview.setNoMore(true);
						}
					}
				}, 50);            //refresh data here
			}

			@Override
			public void onLoadMore() {
				obtainData();
			}
		});
		mAdapter.clearAndAddList(mList);
		if (mList == null || mList.size() <= 28) {
			mBinding.recyclerview.setNoMore(true);
		}
	}

	public void obtainData() {
		//获取数据
		long time = System.currentTimeMillis();
		String validate = "" + showtype + aid + inId + 28 + mPage + time;
		String sign = StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("showtype", showtype);
			obj.put("aid", aid);
			obj.put("id", inId);
			obj.put("pagesize", 28);
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
		Call<NewsGLzqlabelpaReslRespBean> call = service.getGLzqPicPage(body);
		call.enqueue(new Callback<NewsGLzqlabelpaReslRespBean>() {
			@Override
			public void onResponse(Call<NewsGLzqlabelpaReslRespBean> call, Response<NewsGLzqlabelpaReslRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				initMoreData(response.body());
			}

			@Override
			public void onFailure(Call<NewsGLzqlabelpaReslRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
				mBinding.recyclerview.loadMoreError();
			}
		});
	}

	private void initMoreData(final NewsGLzqlabelpaReslRespBean bean) {
		mBinding.recyclerview.refreshComplete();
		mAdapter.appendList(bean.getData().getList());
		if (mAdapter.getItemCount() < totalSize) {
			mPage++;
		} else {
			mBinding.recyclerview.setNoMore(true);
		}
	}

	@Override
	protected void onFragmentFirstVisible() {
		super.onFragmentFirstVisible();
		mBinding.recyclerview.setRefreshing(true);//refresh的监听处理中已经有加载数据的操作
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mAdapter != null)
			mAdapter.notifyDataSetChanged();
	}
}