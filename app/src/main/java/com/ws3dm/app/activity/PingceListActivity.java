package com.ws3dm.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.bean.OriginalBean;
import com.ws3dm.app.databinding.AcPingceListBinding;
import com.ws3dm.app.mvp.model.RespBean.OrignewListRespBean;
import com.ws3dm.app.mvp.model.RespBean.OrignewlevalRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.GameService;
import com.ws3dm.app.network.service.OriginalService;
import com.ws3dm.app.sqlite.NewsFile;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;
import com.ws3dm.app.util.glide.GlideUtil;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Describution : 评测的文章列表
 * 
 * Author : DKjuan
 * 
 * Date : 2019/8/7 11:34
 **/
public class PingceListActivity extends BaseActivity{
	private CommonRecyclerAdapter<OriginalBean> mAdapter;
	private int mPage;//专栏编号
	private AcPingceListBinding mBinding;
	private boolean hasHead;
	private View header;//存储顶部viewpager
	private NewsFile newsCollectFile;
	private int totalSize,pageType;//0 原创评测  1 手游评测

	@Override
	protected void init() {
		mBinding=bindView(R.layout.ac_pingce_list);
		mBinding.setHandler(this);
		header = LayoutInflater.from(mContext).inflate(R.layout.header_pingce, null);

		initView();
	}

	public void initView(){
		newsCollectFile=new NewsFile(mContext);
		pageType=getIntent().getIntExtra("pageType",0);
		mPage=1;
		hasHead=false;
		
		if(pageType==1)
			mBinding.tvTitle.setText("手游评测");

		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.recyclerview.setLayoutManager(layoutManager);

		mBinding.recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
		mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);
		
		mAdapter = new CommonRecyclerAdapter<OriginalBean>(mContext, R.layout.adapter_pingce) {
			@Override
			public void bindData(RecyclerViewHolder holder, final int position, final OriginalBean item) {
				GlideUtil.loadCircleImage(mContext,item.getUser().avatarstr,(ImageView)holder.getView(R.id.img_head));
				holder.setText(R.id.tv_name, item.getUser().nickname);
				GlideUtil.loadRoundImage(mContext,item.getLitpic(),(ImageView)holder.getView(R.id.img_news),5);
				holder.setText(R.id.tv_title, item.getTitle());
				holder.setText(R.id.tv_time, TimeUtil.getFoolishTime2(item.getPubdate_at()+"000"));
				holder.setText(R.id.txtComment, item.getTotal_ct()+"");
				holder.setText(R.id.tv_score, item.getScore()+"");
			}
		};
		mAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int position) {
				OriginalBean originalBean = mAdapter.getDataByPosition(position);
				addHistory(mAdapter.getDataByPosition(position));
				Intent intent = new Intent(mContext, OriginalActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("originalBean",originalBean);
//					bundle.putSerializable("game", finalGame);//数据太大，超出系统限制
//					bundle.putString("str_game", JSON.toJSONString(mGame));
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
						if(pageType>0)
							obtainMGData();
						else
							obtainData();
					}
				}, 50);            //refresh data here
			}

			@Override
			public void onLoadMore() {
				new Handler().postDelayed(new Runnable(){
					public void run() {
						if(pageType>0)
							obtainMGData();
						else
							obtainData();
//                        mBinding.mBinding.recyclerview.loadMoreComplete();
//						mAdapter.notifyDataSetChanged();
					}
				}, 50);
			}
		});
		mBinding.recyclerview.setRefreshing(true);
	}

	public void obtainData(){
		//获取数据
		long time=System.currentTimeMillis();
		String validate= ""+10+mPage+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("pagesize",10);
			obj.put("page",mPage);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		OriginalService.Api service = retrofit.create(OriginalService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<OrignewlevalRespBean> call = service.getOrignewleval(body);
		call.enqueue(new Callback<OrignewlevalRespBean>() {
			@Override
			public void onResponse(Call<OrignewlevalRespBean> call, Response<OrignewlevalRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				OrignewlevalRespBean bean= response.body();
				if(pageType==0&&!hasHead&&bean!=null&&bean.getData()!=null&&bean.getData().getBig_eval()!=null) {
					GlideUtil.loadImage(mContext,bean.getData().getBig_eval().getLitpic(),(ImageView) header.findViewById(R.id.img_top));
					((TextView)header.findViewById(R.id.tv_title)).setText(bean.getData().getBig_eval().getTitle());
					GlideUtil.loadCircleImage(mContext,bean.getData().getBig_eval().getUser().avatarstr,(ImageView) header.findViewById(R.id.img_head));
					((TextView)header.findViewById(R.id.tv_name)).setText(bean.getData().getBig_eval().getUser().nickname);
					((TextView)header.findViewById(R.id.tv_time)).setText(TimeUtil.getFoolishTime2(bean.getData().getBig_eval().getPubdate_at()+"000"));
					((TextView)header.findViewById(R.id.txtComment)).setText(bean.getData().getBig_eval().getTotal_ct()+"");
					((TextView)header.findViewById(R.id.tv_score)).setText(bean.getData().getBig_eval().getScore()+"");

					final OriginalBean myHead=bean.getData().getBig_eval();
					header.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							addHistory(myHead);
							Intent intent = new Intent(mContext, OriginalActivity.class);
							Bundle bundle = new Bundle();
							bundle.putSerializable("originalBean",myHead);
							intent.putExtras(bundle);
							startActivity(intent);
						}
					});

					mBinding.recyclerview.addHeaderView(header);
					hasHead=true;
				}
				if(response.body()!=null)
					initData(response.body());
			}

			@Override
			public void onFailure(Call<OrignewlevalRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
				mBinding.recyclerview.loadMoreError();
			}
		});
	}

	public void obtainMGData(){
		//获取数据
		long time=System.currentTimeMillis();
		String validate= ""+10+mPage+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("pagesize",10);
			obj.put("page",mPage);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		GameService.Api service = retrofit.create(GameService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<OrignewListRespBean> call = service.syeval(body);
		call.enqueue(new Callback<OrignewListRespBean>() {
			@Override
			public void onResponse(Call<OrignewListRespBean> call, Response<OrignewListRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				initMGData(response.body());
			}

			@Override
			public void onFailure(Call<OrignewListRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
				mBinding.recyclerview.loadMoreError();
			}
		});
	}
	
	public void addHistory(OriginalBean originalBean){
		NewsBean newsBean = new NewsBean();
		newsBean.setArcurl(originalBean.getArcurl());
		newsBean.setWebviewurl(originalBean.getWebviewurl());
		newsBean.setTitle(originalBean.getTitle());
		newsBean.setLitpic(originalBean.getLitpic());
		newsBean.setType("原创");
		newsBean.setSeeDate(TimeUtil.dateDayNow());
		newsCollectFile.addHistory(newsBean);
	}

	public void initData(OrignewlevalRespBean bean){
		if(mPage>1) {
			mAdapter.appendList(bean.getData().getList());
		}else {
			mAdapter.clearAndAddList(bean.getData().getList());
		}
		if(bean.getData().getList().size()==0||bean.getData().getList().size()<1) {
			mBinding.recyclerview.setNoMore(true);
		}else {
			mBinding.recyclerview.refreshComplete();
			mPage++;
		}
	}

	public void initMGData(OrignewListRespBean bean){
		if(mPage>1) {
			mAdapter.appendList(bean.getData().getList());
		}else {
			totalSize=bean.getData().getTotal();
			mAdapter.clearAndAddList(bean.getData().getList());
		}
		if (totalSize==mAdapter.getItemCount()) {
			mBinding.recyclerview.setNoMore(true);
		}else {
			mBinding.recyclerview.refreshComplete();
			mPage++;
		}
	}

	public void clickHandler(View view) {
		switch (view.getId()) {
			case R.id.imgReturn:
				onBackPressed();
				break;
		}
	}
}
