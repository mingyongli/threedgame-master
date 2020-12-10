package com.ws3dm.app.fragment;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.os.Handler;
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
import com.ws3dm.app.activity.GiftDetailActivity;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.GameGiftBean;
import com.ws3dm.app.databinding.FgGameDetailBinding;
import com.ws3dm.app.mvp.model.RespBean.GameGiftRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.GameService;
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
 * Describution :礼包  fragment
 * 
 * Author : DKjuan
 * 
 * Date : 2017/8/18 17:22
 **/
public class FragmentGift extends BaseFragment {
	private FgGameDetailBinding mBinding;
	private CommonRecyclerAdapter<GameGiftBean> mAdapter;
	public List<GameGiftBean> listData = new ArrayList<GameGiftBean>();
	private int mPage = 1, giftType,pageType;//排行类型:1、可领取2、已过期 3、无剩余   pageType //0 网游礼包  1 手游礼包

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        AppCompatActivity activity = (AppCompatActivity) getActivity();

//        mContext=getActivity();
		mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_game_detail, container, false);

		//获取 Arguments
		pageType = getArguments().getInt("pageType");
		giftType = getArguments().getInt("giftType");

		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.recyclerview.setLayoutManager(layoutManager);

		mBinding.recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
		mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);

		if(pageType==0) {//网游礼包
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
					GlideUtil.loadRoundImage(mContext,game.getLitpic(), (ImageView) holder.getView(R.id.imgCover),10);
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
		
		mAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int position) {
				Intent intent = new Intent(mContext, GiftDetailActivity.class);
				intent.putExtra("pageType",pageType);//pageType //0 网游礼包  1 手游礼包
				Bundle bundle = new Bundle();
				bundle.putSerializable("mGameGiftBean",mAdapter.getDataByPosition(position));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		mBinding.recyclerview.setAdapter(mAdapter);
		mBinding.recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
			@Override
			public void onRefresh() {
				new Handler().postDelayed(new Runnable() {
					public void run() {
						mPage = 1;
						if(pageType==0) {//网游礼包
							obtainData();
						}else{//手游礼包
							obtainMGData();
						}
					}
				}, 50);            //refresh data here
			}

			@Override
			public void onLoadMore() {
				new Handler().postDelayed(new Runnable() {
					public void run() {
						if(pageType==0) {//网游礼包
							obtainData();
						}else{//手游礼包
							obtainMGData();
						}
					}
				}, 50);
			}
		});

		return mBinding.getRoot();
	}

	public void obtainData() {
		//获取数据
		long time = System.currentTimeMillis();
		String validate = "10" + mPage + giftType + time;
		String sign = StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("pagesize", 10);
			obj.put("page", mPage);
			obj.put("type", giftType);//排行类型:1、可领取2、已过期 3、无剩余
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		GameService.Api service = retrofit.create(GameService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<GameGiftRespBean> call = service.ollibao(body);
		call.enqueue(new Callback<GameGiftRespBean>() {
			@Override
			public void onResponse(Call<GameGiftRespBean> call, Response<GameGiftRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				initData(response.body());//临时注释 完整处理返回结果 正常处理返回结果
			}

			@Override
			public void onFailure(Call<GameGiftRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
				mBinding.recyclerview.loadMoreError();
			}
		});
	}

	public void obtainMGData() {
		//获取数据
		long time = System.currentTimeMillis();
		String validate = "10" + mPage + giftType + time;
		String sign = StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("pagesize", 10);
			obj.put("page", mPage);
			obj.put("type", giftType);//排行类型:1、可领取2、已过期 3、无剩余
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		GameService.Api service = retrofit.create(GameService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<GameGiftRespBean> call = service.sylibao3(body);
		call.enqueue(new Callback<GameGiftRespBean>() {
			@Override
			public void onResponse(Call<GameGiftRespBean> call, Response<GameGiftRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				initData(response.body());//临时注释 完整处理返回结果 正常处理返回结果
			}

			@Override
			public void onFailure(Call<GameGiftRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
				mBinding.recyclerview.loadMoreError();
			}
		});
	}

	private void initData(GameGiftRespBean bean) {
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