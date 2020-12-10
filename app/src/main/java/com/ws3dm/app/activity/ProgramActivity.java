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
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.Constant;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.AvatarBean;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.bean.OriginalBean;
import com.ws3dm.app.mvp.model.RespBean.OrigLabelRespBean;
import com.ws3dm.app.mvp.model.RespBean.OrignewListRespBean;
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

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Describution : 节目Activity
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2019/8/7 9:49
 **/
public class ProgramActivity extends BaseActivity implements View.OnClickListener {
	private CommonRecyclerAdapter<OriginalBean> mAdapter;
	private CommonRecyclerAdapter<AvatarBean> mAdapterHoriz;
	private LinearLayout imgReturn;
	private TextView mTitle;
	private RecyclerView rv_horizontal;
	private XRecyclerView recyclerview;
	private List<AvatarBean> avatarBeanList;//顶部横向图标
	private List<NewsBean> listData = new ArrayList<>();
	private int tag;//0不显示顶部  1显示顶部
	private int totalCount,mPage;
	private String title,labelid;
	private OriginalPresenter mOriginalPresenter;
	private Handler mHandler;
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
					case Constant.Notify.ORIGIN_PROGRAM:
					case Constant.Notify.ORIGIN_PROGRAMLABEL://处理返回结果
						initData((OrignewListRespBean) msg.obj);
						break;
				}
			}
		};

		mOriginalPresenter = OriginalPresenter.getInstance();
		mOriginalPresenter.setHandler(mHandler);

		line_view=findViewById(R.id.line);
		newsCollectFile=new NewsFile(mContext);
		tag = getIntent().getIntExtra("tag", 0);
		labelid = getIntent().getStringExtra("labelid");
		title = getIntent().getStringExtra("title");
		imgReturn = (LinearLayout) findViewById(R.id.imgReturn);
		mTitle = (TextView) findViewById(R.id.base_title);
		rv_horizontal = (RecyclerView) findViewById(R.id.rv_horizontal);
		if (tag == 0)
			rv_horizontal.setVisibility(View.GONE);
		if (StringUtil.isEmpty(title))
			mTitle.setText("我们的节目");
		else
			mTitle.setText(title);
		recyclerview = (XRecyclerView) findViewById(R.id.recyclerview);
		imgReturn.setOnClickListener(this);

		initView();
	}

	public void initView() {
		if (tag != 0) {//0不显示顶部  1显示顶部
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
					if(avatarBeanList!=null){
						Intent intent = new Intent(mContext, ProgramActivity.class);
						intent.putExtra("tag", 0);//0不显示顶部  1显示顶部
						intent.putExtra("labelid", avatarBeanList.get(position).getId());
						intent.putExtra("title", avatarBeanList.get(position).getName());
						startActivity(intent);
					}
				}
			});

			long time = System.currentTimeMillis();
			String validate = "2" + time;
			String sign = StringUtil.MD5(validate);
			JSONObject obj = new JSONObject();
			try {
				obj.put("type", 2);//类型:1专栏2节目
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
					if (!TimeUtil.dateDayNow().equals(SharedUtil.getSharedPreferencesData("todayprogram"))) {
						SharedUtil.setSharedPreferencesData("todayprogram", TimeUtil.dateDayNow());
						SharedUtil.setSharedPreferencesData("programcate", JSON.toJSONString(avatarBeanList));
					}
				}

				@Override
				public void onFailure(Call<OrigLabelRespBean> call, Throwable throwable) {
					String json = SharedUtil.getSharedPreferencesData("programcate");
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
		StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerview.setLayoutManager(layoutManager);

		recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);       //原来就注释掉的
		recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
		recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);

		mAdapter = new CommonRecyclerAdapter<OriginalBean>(mContext, R.layout.item_program) {
			@Override
			public void bindData(RecyclerViewHolder holder, final int position, final OriginalBean item) {
				holder.setImageByUrl(R.id.img_top, item.getLitpic());
				if (position == 0) {
					holder.getView(R.id.ll_bottom).setVisibility(View.GONE);
				} else {
					holder.setText(R.id.tv_title, item.getTitle());
					if(tag==1){//显示标签
						TextView label=holder.getView(R.id.tv_label);
						if(item.getLabel().size()>0) {
							label.setText(item.getLabel().get(0).getName());
							label.setVisibility(View.VISIBLE);
							label.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									Intent intent = new Intent(mContext, ProgramActivity.class);
									intent.putExtra("tag", 0);//0不显示顶部  1显示顶部
									intent.putExtra("labelid", item.getLabel().get(0).getId());
									intent.putExtra("title", item.getLabel().get(0).getName());
									startActivity(intent);
								}
							});
						}else{
							label.setVisibility(View.GONE);
						}
					}else{//显示用户信息
						GlideUtil.loadCircleImage(mContext, item.getUser().avatarstr, (ImageView) holder.getView(R.id.img_head));
						holder.setText(R.id.tv_name, item.getUser().nickname);
						holder.getView(R.id.img_head).setVisibility(View.VISIBLE);
						holder.getView(R.id.tv_name).setVisibility(View.VISIBLE);
					}
					holder.setText(R.id.txtComment, item.getTotal_ct() + "");
					holder.getView(R.id.ll_bottom).setVisibility(View.VISIBLE);
				}
			}
		};
		recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				int[] mFirstVisibleItems = null;
				StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
				mFirstVisibleItems= layoutManager.findFirstCompletelyVisibleItemPositions(mFirstVisibleItems);
				if(mFirstVisibleItems != null&&mFirstVisibleItems[0]==1){
					line_view.setVisibility(View.VISIBLE);
				}else if(mFirstVisibleItems != null&&mFirstVisibleItems[0]>2){
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

		mAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int position) {
				if(position>0){
					OriginalBean originalBean = mAdapter.getDataByPosition(position);
					addHistory(originalBean);
					Intent intent = new Intent(mContext, OriginalActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("originalBean",originalBean);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});
		recyclerview.setRefreshing(true);//refresh的监听处理中已经有加载数据的操作
	}

	public void obtainData() {
		//获取数据
		if (tag == 0) {//0不显示顶部  1显示顶部
			long time = System.currentTimeMillis();
			String validate = "" + 10 + mPage+labelid + time;
			String sign = StringUtil.MD5(validate);
			JSONObject obj = new JSONObject();
			try {
				obj.put("pagesize", 10);
				obj.put("page", mPage);
				obj.put("labelid", labelid);
				obj.put("time", time);
				obj.put("sign", sign);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			mOriginalPresenter.getOrigProgramLabel(obj.toString());//异步请求
		} else {
			long time = System.currentTimeMillis();
			String validate = "" + 10 + mPage + time;
			String sign = StringUtil.MD5(validate);
			JSONObject obj = new JSONObject();
			try {
				obj.put("pagesize", 10);
				obj.put("page", mPage);
				obj.put("time", time);
				obj.put("sign", sign);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			mOriginalPresenter.getOrigProgram(obj.toString());//异步请求
		}
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

	public void initData(OrignewListRespBean bean) {
		if (mPage > 1) {
			mAdapter.appendList(bean.getData().getList());
		} else {
			totalCount=bean.getData().getTotal();
			OriginalBean temp=new OriginalBean();
			temp.setLitpic(bean.getData().getLitpic());
			List<OriginalBean> targetList=new ArrayList<OriginalBean>();
			targetList.add(temp);
			targetList.addAll(bean.getData().getList());
			mAdapter.clearAndAddList(targetList);
		}
		if(mAdapter.getItemCount()==totalCount+1) {
			recyclerview.setNoMore(true);
		} else {
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
			default:
				break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mOriginalPresenter != null)
			mOriginalPresenter.setHandler(mHandler);
	}
}
