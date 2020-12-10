package com.ws3dm.app.activity;

import android.content.Intent;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.GameGiftBean;
import com.ws3dm.app.mvp.model.RespBean.GameGiftRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.GameService;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.glide.GlideUtil;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Describution : 游戏相关礼包
 * 
 * Author : DKjuan
 * 
 * Date : 2019/8/7 9:49
 **/
public class GiftRelativeActivity extends BaseActivity implements View.OnClickListener{
	private CommonRecyclerAdapter<GameGiftBean> mAdapter;
	private LinearLayout imgReturn;
	private TextView mTitle;
	private XRecyclerView recyclerview;
	private int totalCount,mPage,aid,giftType; //0 网游礼包  1 手游礼包;

	@Override
	protected void init() {
		setContentView(R.layout.ac_base_recyclerview);

		mPage=1;
		imgReturn= (LinearLayout) findViewById(R.id.imgReturn);
		mTitle= (TextView) findViewById(R.id.base_title);
		mTitle.setText(getIntent().getStringExtra("title"));
		aid=getIntent().getIntExtra("aid",0);
		giftType=getIntent().getIntExtra("giftType",0);
		recyclerview= (XRecyclerView) findViewById(R.id.recyclerview);
		imgReturn.setOnClickListener(this);

		initView();
	}
	
	public void initView(){
		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerview.setLayoutManager(layoutManager);

		recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);       //原来就注释掉的
		recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
		recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);

		if(giftType==0) {//网游礼包
			mAdapter = new CommonRecyclerAdapter<GameGiftBean>(mContext, R.layout.adapter_game_gift) {
				@Override
				public void bindData(RecyclerViewHolder holder, final int position, final GameGiftBean game) {
					holder.setImageByUrl(R.id.imgCover, game.getLitpic());
					holder.setText(R.id.txtName, game.getTitle());
					holder.setText(R.id.tv_time, game.getRange_start() + " 至 " + game.getRange_end());
					holder.setText(R.id.tv_remain, "剩余：" + game.getSurplusper() + "%");

					ImageView imgScore = holder.getView(R.id.img_score);
					// 获得ClipDrawable对象  
					ClipDrawable clipDrawable = (ClipDrawable) imgScore.getBackground();
					// 设置裁剪级别，Clip类型的图片默认裁剪级别为0，此时是全部裁剪，图片看不见；  
					// 当级别为10000时，不裁剪图片，图片全部可见  
					// 当全部显示后，设置不可见  
					clipDrawable.setLevel((int) (game.getSurplusper() * 100));
				}
			};
		}else{//手游礼包
			mAdapter = new CommonRecyclerAdapter<GameGiftBean>(mContext, R.layout.adapter_gift) {
				@Override
				public void bindData(RecyclerViewHolder holder, final int position, final GameGiftBean game) {
//					holder.setImageByUrl(R.id.imgCover, game.getLitpic());
					GlideUtil.loadRoundImage(mContext,game.getLitpic(), (ImageView) holder.getView(R.id.imgCover),5);
					holder.setText(R.id.txtName, game.getTitle());
					holder.setText(R.id.tv_time, game.getRange_start() + " 至 " + game.getRange_end());
					holder.setText(R.id.tv_remain, "剩余：" + game.getSurplusper() + "%");

					ImageView imgScore = holder.getView(R.id.img_score);
					// 获得ClipDrawable对象  
					ClipDrawable clipDrawable = (ClipDrawable) imgScore.getBackground();
					// 设置裁剪级别，Clip类型的图片默认裁剪级别为0，此时是全部裁剪，图片看不见；  
					// 当级别为10000时，不裁剪图片，图片全部可见  
					// 当全部显示后，设置不可见  
					clipDrawable.setLevel((int) (game.getSurplusper() * 100));
				}
			};
		}
		recyclerview.setAdapter(mAdapter);
		recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
			@Override
			public void onRefresh() {
				new Handler().postDelayed(new Runnable(){
					public void run() {
						mPage=1;
						obtainData();
					}
				}, 50);            //refresh data here
			}

			@Override
			public void onLoadMore() {
				new Handler().postDelayed(new Runnable(){
					public void run() {
						obtainData();
//                        mBinding.recyclerview.loadMoreComplete();
//						mAdapter.notifyDataSetChanged();
					}
				}, 50);
			}
		});
		
		mAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int position) {
				Intent intent = new Intent(mContext, GiftDetailActivity.class);
				intent.putExtra("pageType",giftType);//pageType //0 网游礼包  1 手游礼包
				Bundle bundle = new Bundle();
				bundle.putSerializable("mGameGiftBean",mAdapter.getDataByPosition(position));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		recyclerview.setRefreshing(true);//refresh的监听处理中已经有加载数据的操作
	}

	public void obtainData(){
		//获取数据
		long time=System.currentTimeMillis();
		String validate= ""+10+mPage+aid+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("pagesize",10);
			obj.put("page",mPage);
			obj.put("aid",aid);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
//		mOriginalPresenter.getOrigauthor(obj.toString());//异步请求
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		GameService.Api service = retrofit.create(GameService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<GameGiftRespBean> call = giftType==1?service.syztlibao(body):service.olztlibao(body);
		call.enqueue(new Callback<GameGiftRespBean>() {
			@Override
			public void onResponse(Call<GameGiftRespBean> call, Response<GameGiftRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				initData(response.body());
			}

			@Override
			public void onFailure(Call<GameGiftRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
				recyclerview.loadMoreError();
			}
		});
	}

	public void initData(GameGiftRespBean bean){
		if(mPage>1) {
			mAdapter.appendList(bean.getData().getList());
		}else {
			totalCount=bean.getData().getTotal();
			recyclerview.refreshComplete();
			mAdapter.clearAndAddList(bean.getData().getList());
		}
		if(mAdapter.getItemCount()==totalCount) {
			recyclerview.setNoMore(true);
		}else {
			recyclerview.refreshComplete();
			mPage++;
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.imgReturn:
				onBackPressed();
				break;
		}
	}
}
