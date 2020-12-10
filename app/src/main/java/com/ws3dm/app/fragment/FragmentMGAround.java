package com.ws3dm.app.fragment;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ws3dm.app.R;
import com.ws3dm.app.activity.GiftDetailActivity;
import com.ws3dm.app.activity.MGDetailActivity;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.GameGiftBean;
import com.ws3dm.app.bean.MGListBean;
import com.ws3dm.app.bean.SoftGameBean;
import com.ws3dm.app.bean.VersionMG;
import com.ws3dm.app.databinding.FgBaseRecyclerviewBinding;
import com.ws3dm.app.mvp.model.RespBean.MGAroundRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.MGService;
import com.ws3dm.app.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Describution :手游详情界面
 * 
 * Author : DKjuan
 * 
 * Date : 2018/4/26 14:33
 **/
public class FragmentMGAround extends BaseFragment {	
	private FgBaseRecyclerviewBinding mBinding;
	private CommonRecyclerAdapter<MGListBean> mRecyclerAdapter;
	private CommonRecyclerAdapter<SoftGameBean> mSubRecyclerAdapter;
	private SoftGameBean mSoftGame;
	private View header;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		AppCompatActivity activity = (AppCompatActivity) getActivity();

		mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_base_recyclerview, container, false);
		header = LayoutInflater.from(mContext).inflate(R.layout.header_mg, container,false);

		initView();
		return mBinding.getRoot();
	}
	
	public void initView(){
		mSoftGame= (SoftGameBean) getArguments().getSerializable("mSoftGame");

		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.recyclerview.setLayoutManager(layoutManager);

		mBinding.recyclerview.setPullRefreshEnabled(false);
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);原来就注释掉的    y
//		mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
//		mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);


//		//添加分割线
//		if (mListV3DividerItemDecoration == null) {
//			mListV3DividerItemDecoration = new DividerItemDecorationAdapter(mContext);
//		} else {
//			mBinding.recyclerview.removeItemDecoration(mListV3DividerItemDecoration);
//		}
//		mBinding.recyclerview.addItemDecoration(mListV3DividerItemDecoration);

		mRecyclerAdapter = new CommonRecyclerAdapter<MGListBean>(mContext, R.layout.adapter_hot_cate) {
			@Override
			public void bindData(RecyclerViewHolder holder, final int position, final MGListBean item) {
				holder.setText(R.id.txt_cate_name, item.getType());

				RecyclerView recyclerView = holder.getView(R.id.recycler_hot_cates);
				LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
				layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
				recyclerView.setLayoutManager(layoutManager);

				mSubRecyclerAdapter= new CommonRecyclerAdapter<SoftGameBean>(mContext, R.layout.item_game_horizon) {
					@Override
					public void bindData(RecyclerViewHolder holder, int position, final SoftGameBean item) {
						holder.setImageByUrl(R.id.imgCover,item.getLitpic());
						holder.setText(R.id.tv_title,item.getTitle());
						holder.setText(R.id.tv_data,"v"+item.getSoft_ver()+" | "+item.getSoft_size());
						holder.setText(R.id.tv_info,item.getDesc());
						holder.getView(R.id.bt_download).setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View view) {
								Intent intent = new Intent(mContext, MGDetailActivity.class);
								Bundle bundle = new Bundle();
								bundle.putSerializable("mSoftGameBean", item);
								intent.putExtras(bundle);
								mContext.startActivity(intent);
								getActivity().finish();
//								DownloadUtil.down(mContext,item.getDownurl(),item.getTitle()+"|"+item.getLitpic());
							}
						});
					}
				};
				mSubRecyclerAdapter.setOnClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(View itemView, int posi) {
						Intent intent = new Intent(mContext, MGDetailActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("mSoftGameBean", mSubRecyclerAdapter.getDataByPosition(posi));
						intent.putExtras(bundle);
						mContext.startActivity(intent);
						getActivity().finish();
					}
				});
				recyclerView.setAdapter(mSubRecyclerAdapter);
				mSubRecyclerAdapter.clearAndAddList(item.getSoftlist());
			}
		};
		mRecyclerAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int position) {
//				news.setSenddate(TimeUtil.dateDayNow());
//				newsCollectFile.addHistory(news);
			}
		});
		mBinding.recyclerview.setAdapter(mRecyclerAdapter);
		mBinding.recyclerview.addHeaderView(header);
	}

	protected void onFragmentFirstVisible(){
		obtainData();
	}

	public void obtainData(){//获取游戏详情
		//获取数据
		long time=System.currentTimeMillis();
		String validate= ""+mSoftGame.getAid()+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("aid", mSoftGame.getAid());
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		MGService.Api service = retrofit.create(MGService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<MGAroundRespBean> call = service.getAroundMG(body);
		call.enqueue(new Callback<MGAroundRespBean>() {
			@Override
			public void onResponse(Call<MGAroundRespBean> call, Response<MGAroundRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				initData(response.body());
			}

			@Override
			public void onFailure(Call<MGAroundRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
				mBinding.recyclerview.loadMoreError();
			}
		});
	}
	
	private void initData(MGAroundRespBean bean) {
		if(bean==null||bean.getData()==null){
			return;
		}

		initHeadView(bean);
		mRecyclerAdapter.clearAndAddList(bean.getData().getList());
	}
	
	public void initHeadView(final MGAroundRespBean bean){
		header.findViewById(R.id.rl_comment_around).setVisibility(View.VISIBLE);
		if(bean.getData().getLibao()==null||bean.getData().getLibao().size()==0){
			header.findViewById(R.id.ll_top).setVisibility(View.GONE);
		}else{
			int libaoSize=bean.getData().getLibao().size()>5?5:bean.getData().getLibao().size();
			switch (libaoSize) {
				case 5:
					TextView libao5= (TextView) header.findViewById(R.id.libao5);
					libao5.setText(bean.getData().getLibao().get(4).getTitle());
					header.findViewById(R.id.libao5).setVisibility(View.VISIBLE);
					libao5.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							jumpGiftDetail(bean.getData().getLibao().get(4));
						}
					});
				case 4:
					TextView libao4= (TextView) header.findViewById(R.id.libao4);
					libao4.setText(bean.getData().getLibao().get(3).getTitle());
					header.findViewById(R.id.libao4).setVisibility(View.VISIBLE);
					libao4.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							jumpGiftDetail(bean.getData().getLibao().get(3));
						}
					});
				case 3:
					TextView libao3= (TextView) header.findViewById(R.id.libao3);
					libao3.setText(bean.getData().getLibao().get(2).getTitle());
					header.findViewById(R.id.libao3).setVisibility(View.VISIBLE);
					libao3.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							jumpGiftDetail(bean.getData().getLibao().get(2));
						}
					});
				case 2:
					TextView libao2= (TextView) header.findViewById(R.id.libao2);
					libao2.setText(bean.getData().getLibao().get(1).getTitle());
					header.findViewById(R.id.libao2).setVisibility(View.VISIBLE);
					libao2.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							jumpGiftDetail(bean.getData().getLibao().get(1));
						}
					});
				case 1:
					TextView libao1= (TextView) header.findViewById(R.id.libao1);
					libao1.setText(bean.getData().getLibao().get(0).getTitle());
					header.findViewById(R.id.libao1).setVisibility(View.VISIBLE);
					libao1.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							jumpGiftDetail(bean.getData().getLibao().get(0));
						}
					});
			}
		}

		
		if(bean.getData().getLabels()==null||bean.getData().getLabels().size()==0){
			header.findViewById(R.id.ll_bottom).setVisibility(View.GONE);
		}else{
			int labelSize=bean.getData().getLabels().size()>6?6:bean.getData().getLabels().size();
			if(labelSize<4){
				header.findViewById(R.id.tag6).setVisibility(View.GONE);
				header.findViewById(R.id.tag5).setVisibility(View.GONE);
				header.findViewById(R.id.tag4).setVisibility(View.GONE);
			}
			switch (labelSize) {
				case 6:
					TextView tag6= (TextView) header.findViewById(R.id.tag6);
					tag6.setText(bean.getData().getLabels().get(5).getName());
					header.findViewById(R.id.tag6).setVisibility(View.VISIBLE);
					tag6.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
//							ToastUtil.showToast(mContext,"标签6");
						}
					});
				case 5:
					TextView tag5= (TextView) header.findViewById(R.id.tag5);
					tag5.setText(bean.getData().getLabels().get(4).getName());
					header.findViewById(R.id.tag5).setVisibility(View.VISIBLE);
					tag5.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
//							ToastUtil.showToast(mContext,"标签5");
						}
					});
				case 4:
					TextView tag4= (TextView) header.findViewById(R.id.tag4);
					tag4.setText(bean.getData().getLabels().get(3).getName());
					header.findViewById(R.id.tag4).setVisibility(View.VISIBLE);
					tag4.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
//							ToastUtil.showToast(mContext,"标签4");
						}
					});
				case 3:
					TextView tag3= (TextView) header.findViewById(R.id.tag3);
					tag3.setText(bean.getData().getLabels().get(2).getName());
					header.findViewById(R.id.tag3).setVisibility(View.VISIBLE);
					tag3.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
//							ToastUtil.showToast(mContext,"标签3");
						}
					});
				case 2:
					TextView tag2= (TextView) header.findViewById(R.id.tag2);
					tag2.setText(bean.getData().getLabels().get(1).getName());
					header.findViewById(R.id.tag2).setVisibility(View.VISIBLE);
					tag2.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
//							ToastUtil.showToast(mContext,"标签2");
						}
					});
				case 1:
					TextView tag1= (TextView) header.findViewById(R.id.tag1);
					tag1.setText(bean.getData().getLabels().get(0).getName());
					header.findViewById(R.id.tag1).setVisibility(View.VISIBLE);
					tag1.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
//							ToastUtil.showToast(mContext,"标签1");
						}
					});
			}
		}
	}
	
	public void jumpGiftDetail(VersionMG bean){
		GameGiftBean giftBean=new GameGiftBean();
//		giftBean.setAid(bean.getAid());
//		giftBean.setArcurl(bean.getArcurl());
//		giftBean.setTitle(bean.getTitle());
//		giftBean.setShowtype(bean.getShowtype());
		
		Intent intent = new Intent(mContext, GiftDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("mGameGiftBean", giftBean);
		intent.putExtras(bundle);
		mContext.startActivity(intent);
	}
}