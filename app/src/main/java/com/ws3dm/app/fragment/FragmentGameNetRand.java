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
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.GameHomeActivity;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.GameBean;
import com.ws3dm.app.bean.GameHotPhb;
import com.ws3dm.app.databinding.FgGameDetailBinding;
import com.ws3dm.app.mvp.model.RespBean.GameOLHotRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.GameService;
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

public class FragmentGameNetRand extends BaseFragment {
	private FgGameDetailBinding mBinding;
	private CommonRecyclerAdapter<GameHotPhb> mAdapter;
	public List<GameHotPhb> listData = new ArrayList<GameHotPhb>();
	private int mPage = 1, randType;//排行类型:1、总排行2、2019新游排行3、新游期待榜

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
		randType = getArguments().getInt("randType");

		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.recyclerview.setLayoutManager(layoutManager);

		mBinding.recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
		mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);

		mAdapter = new CommonRecyclerAdapter<GameHotPhb>(mContext, R.layout.adapter_game_rank_net) {
			@Override
			public void bindData(RecyclerViewHolder holder, final int position, final GameHotPhb game) {
				ImageView rank = holder.getView(R.id.img_id);
				switch (position) {
					case 0:
						rank.setImageResource(R.drawable.img_rand1);
						holder.setText(R.id.tv_id, "");
						break;
					case 1:
						rank.setImageResource(R.drawable.img_rand2);
						holder.setText(R.id.tv_id, "");
						break;
					case 2:
						rank.setImageResource(R.drawable.img_rand3);
						holder.setText(R.id.tv_id, "");
						break;
					default:
						rank.setImageResource(R.drawable.img_rand4);
						holder.setText(R.id.tv_id, position + 1 + "");
						break;
				}
//				ImageView imageView=holder.getView(R.id.imgCover);
//				GlideUtil.loadRoundImage(mContext,game.getLitpic(),imageView,5);
				holder.setImageByUrl(R.id.imgCover, game.getLitpic());
				holder.setText(R.id.tv_score, game.getScore() + "");
				holder.setText(R.id.txtName, game.getTitle());
				holder.setText(R.id.tv_type, "类型："+game.getType());
				holder.setText(R.id.tv_firm, "运营："+game.getFirm());
				holder.setText(R.id.tv_time,"公测："+game.getBetatime());
				holder.setText(R.id.tv_label, "标签："+game.getLabel() );

//                double score= Keep1DecimalPlaces(10*Math.random());
//                holder.setText(R.id.score, score+"");
//                ImageView imgScore=holder.getView(R.id.img_score);
//                // 获得ClipDrawable对象  
//                ClipDrawable clipDrawable = (ClipDrawable) imgScore.getBackground();
//                // 设置裁剪级别，Clip类型的图片默认裁剪级别为0，此时是全部裁剪，图片看不见；  
//                // 当级别为10000时，不裁剪图片，图片全部可见  
//                // 当全部显示后，设置不可见  
//                clipDrawable.setLevel((int)(score*1000));
			}
		};
		mAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int position) {
				final GameBean mGame = new GameBean();
				GameHotPhb originalBean =mAdapter.getDataByPosition(position);
				mGame.setAid(originalBean.getAid());
				mGame.setArcurl(originalBean.getArcurl());
				mGame.setShowtype(originalBean.getShowtype());
				
				Intent intent = new Intent(mContext, GameHomeActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("str_game", JSON.toJSONString(mGame));
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
						obtainData();
					}
				}, 50);            //refresh data here
			}

			@Override
			public void onLoadMore() {
				new Handler().postDelayed(new Runnable() {
					public void run() {
						obtainData();
					}
				}, 50);
			}
		});

		return mBinding.getRoot();
	}

	public void obtainData() {
		//获取数据
		long time = System.currentTimeMillis();
		String validate = "10" + mPage + randType + time;
		String sign = StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("pagesize", 10);
			obj.put("page", mPage);
			obj.put("type", randType);//排行类型:1、总排行2、2019新游排行3、新游期待榜
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		GameService.Api service = retrofit.create(GameService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<GameOLHotRespBean> call = service.olrank(body);
		call.enqueue(new Callback<GameOLHotRespBean>() {
			@Override
			public void onResponse(Call<GameOLHotRespBean> call, Response<GameOLHotRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				initData(response.body());//临时注释 完整处理返回结果 正常处理返回结果
			}

			@Override
			public void onFailure(Call<GameOLHotRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
				mBinding.recyclerview.loadMoreError();
			}
		});
	}

	private void initData(GameOLHotRespBean bean) {
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