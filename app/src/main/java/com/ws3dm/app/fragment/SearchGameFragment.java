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
import com.ws3dm.app.activity.SearchActivity;
import com.ws3dm.app.adapter.BaseRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.GameBean;
import com.ws3dm.app.bean.SearchBean;
import com.ws3dm.app.bean.SearchGamebean;
import com.ws3dm.app.databinding.FgSearchGameBinding;
import com.ws3dm.app.mvp.model.RespBean.SearchGameRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.UserService;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;
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
 * Describution :搜索fragment
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2018/1/6 14:05
 **/
public class SearchGameFragment extends BaseFragment {
	private BaseRecyclerAdapter<SearchGamebean> mAdapter;
	public List<SearchBean> listData = new ArrayList<SearchBean>();
	private FgSearchGameBinding mBinding;
	private int mPage = 1, searchType,totalCount;//searchType 1单机2网游3手游
	private String mWord;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_search_game, container, false);
//        mBinding.MyUtilbar.setTitle(getResources().getString(R.string.person));
//        activity.setSupportActionBar(mBinding.MyUtilbar);

		initView();
		return mBinding.getRoot();
	}

	public void initView() {
		searchType = 1;
		setButton(1);
		
		mBinding.btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setButton(1);
			}
		});
		mBinding.btn2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setButton(2);
			}
		});
		mBinding.btn3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setButton(3);
			}
		});

		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.recyclerview.setLayoutManager(layoutManager);

		mBinding.recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);

//		//添加分割线
//		if (mListV3DividerItemDecoration == null) {
//			mListV3DividerItemDecoration = new DividerItemDecorationAdapter(mContext);
//		} else {
//			mBinding.recyclerview.removeItemDecoration(mListV3DividerItemDecoration);
//		}
//		mBinding.recyclerview.addItemDecoration(mListV3DividerItemDecoration);
		setRecy();
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

		mBinding.recyclerview.setRefreshing(true);
	}
	
	private void setRecy(){
		switch (searchType) {//11单机2网游3手游
			case 1:
				mAdapter = new BaseRecyclerAdapter<SearchGamebean>(mContext, R.layout.adapter_game) {
					@Override
					public void bindData(RecyclerViewHolder holder, final int position, final SearchGamebean game) {
						holder.setImageByUrl(R.id.imgCover,game.getLitpic());
						holder.setText(R.id.tv_score, game.getScore()+"");
						holder.setText(R.id.txtName, game.getTitle());
						holder.setText(R.id.tv_type, "类型：" +game.getType());
						holder.setText(R.id.txtTime, "发售："+ (game.getPubdate_at()==0?"未知":TimeUtil.getFormatTimeSimple(game.getPubdate_at())));
						holder.setText(R.id.tv_label, "标签：" +game.getLabel());

						holder.getView(R.id.txt_label1).setVisibility(View.GONE);
						holder.getView(R.id.txt_label2).setVisibility(View.GONE);
						holder.getView(R.id.txt_label3).setVisibility(View.GONE);
						holder.getView(R.id.txt_label4).setVisibility(View.GONE);
						holder.getView(R.id.txt_label5).setVisibility(View.GONE);
						String[] sy=game.getSystem().split(" ");
						switch (sy.length>5?5:sy.length) {
							case 5:
								holder.getView(R.id.txt_label5).setVisibility(View.VISIBLE);
							case 4:
								holder.getView(R.id.txt_label4).setVisibility(View.VISIBLE);
								holder.setText(R.id.txt_label4,sy[3]);
							case 3:
								holder.getView(R.id.txt_label3).setVisibility(View.VISIBLE);
								holder.setText(R.id.txt_label3,sy[2]);
							case 2:
								holder.getView(R.id.txt_label2).setVisibility(View.VISIBLE);
								holder.setText(R.id.txt_label2,sy[1]);
							case 1:
								holder.getView(R.id.txt_label1).setVisibility(View.VISIBLE);
								holder.setText(R.id.txt_label1,sy[0]);
								break;
						}
					}
				};
				break;
			case 2:
				mAdapter = new BaseRecyclerAdapter<SearchGamebean>(mContext, R.layout.adapter_special_hot) {
					@Override
					public void bindData(RecyclerViewHolder holder, final int position, final SearchGamebean game) {
						holder.setImageByUrl(R.id.imgCover, game.getLitpic());
						holder.setText(R.id.tv_score, game.getScore() + "");
						holder.setText(R.id.txtName, game.getTitle());
						holder.setText(R.id.tv_type, "类型：" + game.getType());
						holder.setText(R.id.tv_firm, "运营：" + game.getFirm());
						holder.setText(R.id.txtTime, "公测：" + game.getBetatime());
						holder.setText(R.id.tv_label, "标签：" + (StringUtil.isEmpty(game.getLabel())?"待补充":game.getLabel()));
					}
				};
				break;
			case 3:
				mAdapter = new BaseRecyclerAdapter<SearchGamebean>(mContext, R.layout.adapter_mg_game) {
					@Override
					public void bindData(RecyclerViewHolder holder, final int position, final SearchGamebean item) {
						GlideUtil.loadRoundImage(mContext,item.getLitpic(), (ImageView) holder.getView(R.id.imgCover),5);
						holder.setText(R.id.txtName,item.getTitle());
						holder.setText(R.id.tv_type,"类型："+item.getType());
						holder.setText(R.id.tv_label,"平台："+item.getSystem());
					}
				};
				break;
		}
		mAdapter.setOnClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int position) {
				Intent intent;
				SearchGamebean clickBean = mAdapter.getDataByPosition(position);
				GameBean mGame = new GameBean();
				mGame.setAid(clickBean.getAid());
				mGame.setArcurl(clickBean.getArcurl());
				mGame.setShowtype(clickBean.getShowtype());
				intent = new Intent(mContext, GameHomeActivity.class);
				Bundle bundleGame = new Bundle();
				bundleGame.putString("str_game", JSON.toJSONString(mGame));
				intent.putExtras(bundleGame);
				startActivity(intent);
			}
		});
		mBinding.recyclerview.setAdapter(mAdapter);
	}

	public void obtainData() {
		//获取数据
		long time = System.currentTimeMillis();
		String validate = "10" + mPage+ SearchActivity.keyWord + time;
		String sign = StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("pagesize", 10);
			obj.put("page", mPage);
			obj.put("keyword", SearchActivity.keyWord);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		UserService.Api service = retrofit.create(UserService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<SearchGameRespBean> call;
		if(searchType==1)
			call= service.sodjzt(body);
		else if(searchType==2)
			call= service.soolzt(body);
		else
			call= service.sosyzt(body);
		call.enqueue(new Callback<SearchGameRespBean>() {
			@Override
			public void onResponse(Call<SearchGameRespBean> call, Response<SearchGameRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				initData(response.body());
			}

			@Override
			public void onFailure(Call<SearchGameRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
			}
		});
	}

	private void initData(SearchGameRespBean bean) {
		mBinding.recyclerview.refreshComplete();
		if (bean == null || bean.getData() == null) {
			mBinding.recyclerview.setNoMore(true);
			return;
		}
		if (mPage > 1) {
			mAdapter.appendList(bean.getData().getList());
		} else {
			totalCount=bean.getData().getTotal();
			mAdapter.clearAndAddList(bean.getData().getList());
		}
		if(mAdapter.getItemCount()==totalCount||mPage==totalCount/10+1) {
			mBinding.recyclerview.setNoMore(true);
		} else {
			mBinding.recyclerview.refreshComplete();
			mPage++;
			//临时注释 
			if(bean.getCode()==1&&bean.getData().getList().size()==0&&mPage<=totalCount/10+1||bean.getCode()==1&&mPage<5&&mPage<=totalCount/10+1&&mAdapter.getItemCount()<9){
				obtainData();
			}
			//临时注释 
		}
	}

	public void setButton(int select) {
		if(searchType==select)
			return;
		mPage=1;
		searchType = select;
		switch (select) {
			case 1:
				mBinding.btn1.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_gray));
				mBinding.btn2.setBackground(null);
				mBinding.btn3.setBackground(null);
				break;
			case 2:
				mBinding.btn1.setBackground(null);
				mBinding.btn2.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_gray));
				mBinding.btn3.setBackground(null);
				break;
			case 3:
				mBinding.btn1.setBackground(null);
				mBinding.btn2.setBackground(null);
				mBinding.btn3.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_gray));
				break;
		}
		setRecy();
		obtainData();
	}
	@Override
	protected void onFragmentVisibleChange(boolean isVisible) {
		super.onFragmentVisibleChange(isVisible);
//		if (isVisible&&!mBinding.recyclerview.canScrollVertically(1)) {//canScrollVertically是否可向向下滑动
//			obtainData();
//		}
	}
}
