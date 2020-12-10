package com.ws3dm.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.Constant;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.AvatarBean;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.bean.OriginalBean;
import com.ws3dm.app.mvp.model.RespBean.OrigLabelRespBean;
import com.ws3dm.app.mvp.model.RespBean.OrignewColRespBean;
import com.ws3dm.app.mvp.presenter.OriginalPresenter;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.OriginalService;
import com.ws3dm.app.sqlite.NewsFile;
import com.ws3dm.app.util.SharedUtil;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.util.glide.GlideUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Describution : 原创专栏Activity
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2019/8/7 9:49
 **/
public class ColumActivity extends BaseActivity implements View.OnClickListener {
	private CommonRecyclerAdapter<OrignewColRespBean.DataBean.ColumBean> mAdapter;
	private CommonRecyclerAdapter<AvatarBean> mAdapterHoriz;
	private List<AvatarBean> avatarBeanList;//顶部横向图标
	private LinearLayout imgReturn;
	private TextView mTitle;
	private RecyclerView rv_horizontal;
	private XRecyclerView recyclerview;
	private String title;
	private OriginalPresenter mOriginalPresenter;
	private Handler mHandler;
	private int tag;//0不显示顶部  1显示顶部
	private int totalCount, mPage;
	private NewsFile newsCollectFile;
	private View line_view;

	@Override
	protected void init() {
		setContentView(R.layout.ac_two_recyclerview);

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (!mIsRunning) {
					return;
				}
				switch (msg.what) {
					case Constant.Notify.LOAD_START:
						ToastUtil.showToast(mContext, "载入中");
						break;
					case Constant.Notify.LOAD_FAILURE:
						ToastUtil.showToast(mContext, "请求失败");
						recyclerview.loadMoreError();
						break;
					case Constant.Notify.ORIGIN_NEWCOL:
						initData((OrignewColRespBean) msg.obj);
						break;
				}
			}
		};

		mOriginalPresenter = OriginalPresenter.getInstance();
		mOriginalPresenter.setHandler(mHandler);

		line_view=findViewById(R.id.line);
		newsCollectFile=new NewsFile(mContext);
		mPage = 1;
		tag = getIntent().getIntExtra("tag", 0);
		title = getIntent().getStringExtra("title");
		imgReturn = (LinearLayout) findViewById(R.id.imgReturn);
		mTitle = (TextView) findViewById(R.id.base_title);
		rv_horizontal = (RecyclerView) findViewById(R.id.rv_horizontal);
		mTitle.setText("3DM专栏");
		recyclerview = (XRecyclerView) findViewById(R.id.recyclerview);
		imgReturn.setOnClickListener(this);

		initView();
	}

	public void initView() {
		rv_horizontal.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));

		mAdapterHoriz = new CommonRecyclerAdapter<AvatarBean>(mContext, R.layout.item_title) {
			@Override
			public void bindData(RecyclerViewHolder holder, int position, AvatarBean bean) {
				holder.setImageByUrl(R.id.img_top, bean.getLitpic());
				holder.setText(R.id.tv_title, bean.getName());
			}
		};
		rv_horizontal.setAdapter(mAdapterHoriz);
		mAdapterHoriz.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int position) {
				Intent intent = new Intent(mContext, ArticleListActivity.class);
				intent.putExtra("tag", 1);//tag:0作者团队,显示顶部个人信息 1专栏，只有背景图
				intent.putExtra("labelid",Integer.parseInt(mAdapterHoriz.getDataByPosition(position).getId()));
				intent.putExtra("title", mAdapterHoriz.getDataByPosition(position).getName());
				startActivity(intent);
			}
		});
		if (TimeUtil.dateDayNow().equals(SharedUtil.getSharedPreferencesData("todaycolum"))) {//判断今天是否有记录
			String json = SharedUtil.getSharedPreferencesData("columcate");
			if (!StringUtil.isEmpty(json) && json.length() > 100) {
				List<AvatarBean> avatarBeanList = JSON.parseArray(json, AvatarBean.class);
				mAdapterHoriz.clearAndAddList(avatarBeanList);
			}
		} else {//没有记录则重新获取
			long time = System.currentTimeMillis();
			String validate = "1" + time;
			String sign = StringUtil.MD5(validate);
			JSONObject obj = new JSONObject();
			try {
				obj.put("type", 1);//类型:1专栏2节目
				obj.put("time", time);
				obj.put("sign", sign);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			//同步请求
			Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
			OriginalService.Api service = retrofit.create(OriginalService.Api.class);
			RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
			Call<OrigLabelRespBean> call = service.getOrigLabel(body);
			call.enqueue(new Callback<OrigLabelRespBean>() {
				@Override
				public void onResponse(Call<OrigLabelRespBean> call, Response<OrigLabelRespBean> response) {
					avatarBeanList = response.body().getData().getList();
					mAdapterHoriz.clearAndAddList(avatarBeanList);
					if (!TimeUtil.dateDayNow().equals(SharedUtil.getSharedPreferencesData("todaycolum"))) {
						SharedUtil.setSharedPreferencesData("todaycolum", TimeUtil.dateDayNow());
						SharedUtil.setSharedPreferencesData("columcate", JSON.toJSONString(avatarBeanList));
					}
				}

				@Override
				public void onFailure(Call<OrigLabelRespBean> call, Throwable throwable) {
					String json = SharedUtil.getSharedPreferencesData("columcate");
					if (!StringUtil.isEmpty(json) && json.length() > 100) {
						List<AvatarBean> avatarBeanList = JSON.parseArray(json, AvatarBean.class);
						mAdapterHoriz.clearAndAddList(avatarBeanList);
					}
				}
			});
		}

//		初始化内容列表
		if(tag == 0){
			line_view.setVisibility(View.GONE);
		}
		recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);       //原来就注释掉的
		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerview.setLayoutManager(layoutManager);
		recyclerview.setLoadingMoreEnabled(false);

		mAdapter = new CommonRecyclerAdapter<OrignewColRespBean.DataBean.ColumBean>(mContext, R.layout.item_colum) {
			@Override
			public void bindData(RecyclerViewHolder holder, final int position, final OrignewColRespBean.DataBean.ColumBean columBean) {
				holder.setText(R.id.tv_title, columBean.getName());
				holder.setImageByUrl(R.id.img_top, columBean.getLitpic());
				holder.getView(R.id.img_top).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext, ArticleListActivity.class);
						intent.putExtra("tag", 1);//tag:0作者团队,显示顶部个人信息 1专栏，只有背景图
						intent.putExtra("labelid", columBean.getId());
						intent.putExtra("title", columBean.getName());
						startActivity(intent);
					}
				});
				holder.getView(R.id.tv_more).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext, ArticleListActivity.class);
						intent.putExtra("tag", 1);//tag:0作者团队,显示顶部个人信息 1专栏，只有背景图
						intent.putExtra("labelid", columBean.getId());
						intent.putExtra("title", columBean.getName());
						startActivity(intent);
					}
				});

				if(columBean.getNewslist().size()>0){
					final List<OriginalBean> mList = columBean.getNewslist();
					holder.setText(R.id.txtTitle1, mList.get(0).getTitle());
					holder.setText(R.id.tv_time1, TimeUtil.getTimeEN(mList.get(0).getPubdate_at()));
					holder.setText(R.id.txtComment1, mList.get(0).getTotal_ct() + "");
					GlideUtil.loadRoundImage(mContext, mList.get(0).getLitpic(), (ImageView) holder.getView(R.id.img1), 5);
					holder.getView(R.id.rlnews1).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							jumpDetail(mList.get(0));
						}
					});

					holder.setText(R.id.txtTitle2, mList.get(1).getTitle());
					holder.setText(R.id.tv_time2, TimeUtil.getTimeEN(mList.get(1).getPubdate_at()));
					holder.setText(R.id.txtComment2, mList.get(1).getTotal_ct() + "");
					GlideUtil.loadRoundImage(mContext, mList.get(1).getLitpic(), (ImageView) holder.getView(R.id.img2), 5);
					holder.getView(R.id.rlnews2).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							jumpDetail(mList.get(1));
						}
					});
				}
			}
		};
		recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
				int firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
				if(firstCompletelyVisibleItemPosition==0){
					line_view.setVisibility(View.VISIBLE);
				}else{
					line_view.setVisibility(View.GONE);
				}
			}
		});
		recyclerview.setAdapter(mAdapter);
		recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
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
//                        mBinding.recyclerview.loadMoreComplete();
//						mAdapter.notifyDataSetChanged();
					}
				}, 50);
			}
		});

		obtainData();
	}
	
	public void jumpDetail(OriginalBean originalBean){
		NewsBean newsBean = new NewsBean();
		newsBean.setArcurl(originalBean.getArcurl());
		newsBean.setWebviewurl(originalBean.getWebviewurl());
		newsBean.setTitle(originalBean.getTitle());
		newsBean.setLitpic(originalBean.getLitpic());
		newsBean.setType("原创");
		newsBean.setSeeDate(TimeUtil.dateDayNow());
		newsCollectFile.addHistory(newsBean);
		
		Intent intent = new Intent(mContext, OriginalActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("originalBean",originalBean);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	public void obtainData() {//获取数据
		long time = System.currentTimeMillis();
		String sign = StringUtil.MD5("" + time);
		JSONObject obj = new JSONObject();
		try {
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		mOriginalPresenter.getOrignewcol(obj.toString());//异步请求
	}

	public void initData(OrignewColRespBean bean) {
		recyclerview.refreshComplete();
		mAdapter.clearAndAddList(bean.getData().getList());
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.imgReturn:
				onBackPressed();
				break;
			default:
				break;
		}
	}
}
