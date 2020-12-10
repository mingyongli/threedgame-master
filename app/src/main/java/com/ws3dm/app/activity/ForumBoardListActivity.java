package com.ws3dm.app.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.bean.ForumBean;
import com.ws3dm.app.Constant;
import com.ws3dm.app.databinding.AcForumlistBinding;
import com.ws3dm.app.mvp.model.RespBean.ForumRankRespBean;
import com.ws3dm.app.mvp.presenter.ForumPresenter;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.ForumService;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Describution :子版块列表 （板块分类列表的下一级,类似  排行fragment）
 * 
 * Author : DKjuan
 * 
 * Date : 2017/10/23 10:53
 **/
public class ForumBoardListActivity extends BaseActivity{
	
	private AcForumlistBinding mBinding;
	private CommonRecyclerAdapter<ForumBean> mAdapter;
	public List<ForumBean> listData = new ArrayList<>();//全部置顶
	private int mPage=1,clickPosition;//点击的位置

	private ForumPresenter mForumPresenter;
	private Handler mHandler;
	private String gid="";

	@Override
	protected void init() {
		mBinding = bindView(R.layout.ac_forumlist);
		mBinding.setHandler(this);

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
						mBinding.recyclerview.loadMoreError();
						break;
					case Constant.Notify.RESULT_FORUM_LIST://处理返回结果
						ForumRankRespBean bean= (ForumRankRespBean) msg.obj;
						listData=bean.getData().getList();
                        if(mPage>1) {
	                        mAdapter.appendList(listData);
                        }else {
	                        mAdapter.clearAndAddList(listData);
                        }
						if(bean.getData().getList().size()==0||bean.getData().getList().size()<1) {
							mBinding.recyclerview.setNoMore(true);
						}else {
							mBinding.recyclerview.refreshComplete();
							mPage++;
						}
						break;
				}
			}
		};

		mForumPresenter = ForumPresenter.getInstance();
		mForumPresenter.setHandler(mHandler);

		initView();
		obtainData();
	}
	
	//界面初始化
	private void initView() {
		//获取 Arguments
		gid = getIntent().getStringExtra("gid");
		mBinding.tvTitle.setText(getIntent().getStringExtra("title"));

		mPage = 1;

		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.recyclerview.setLayoutManager(layoutManager);

		mBinding.recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
		mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);
		((DefaultItemAnimator)mBinding.recyclerview.getItemAnimator()).setSupportsChangeAnimations(false);

		mAdapter = new CommonRecyclerAdapter<ForumBean>(mContext, R.layout.adapter_forum_rank) {
			@Override
			public void bindData(RecyclerViewHolder holder, final int position, final ForumBean forumBean) {
				holder.setImageByUrl(R.id.imgArrayOne, forumBean.getLitpic());
				holder.setText(R.id.tv_title, forumBean.getTitle());
				holder.setText(R.id.tv_readnum, "今日：" + forumBean.getTodayposts());
				holder.setText(R.id.tv_title, forumBean.getTitle());
				holder.setText(R.id.tv_title, forumBean.getTitle());
//				ImageView img_collect = holder.getView(R.id.img_collect);
//				if (forumBean.getIsfavorite() == 0) {
//					img_collect.setImageResource(R.drawable.click_collect);
//				} else {
//					img_collect.setImageResource(R.drawable.collect);
//				}
//				img_collect.setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View view) {
//						if(MyApplication.getUserData().loginStatue){
//							clickPosition=position;
//							addDelFavorite(forumBean.getIsfavorite()==0?1:2);
//						}else {
//							ToastUtil.showToast(mContext,"请先登录！");
//						}
//					}
//				});
			}
		};
		mAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int position) {
				Intent intent = new Intent(mContext, ForumPostListActivity.class);
				intent.putExtra("title", mAdapter.getDataByPosition(position).getTitle());
				intent.putExtra("fid", mAdapter.getDataByPosition(position).getFid());
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
						mBinding.recyclerview.refreshComplete();
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
//        mBinding.recyclerview.addOnItemTouchListener(new OnRecyclerItemClickListener(mBinding.recyclerview) {
//            @Override
//            public void onItemClick(RecyclerView.ViewHolder viewHolder) {//下标从1开始，不是从0开始
//                ToastUtil.showToast(mContext,"第"+viewHolder.getPosition()+"条");
//                Intent intent = new Intent(mContext, GameDetailActivity_.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("game", listData.get(viewHolder.getPosition()-1));
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//
//            @Override
//            public void onItemLOngClick(RecyclerView.ViewHolder viewHolder) {
//
//            }
//        });
	}

	public void clickHandler(View view) {
		switch (view.getId()) {
			case R.id.imgReturn:// 返回
				onBackPressed();
				break;
		}
	}

	public void obtainData(){
		//获取数据
		String uid=MyApplication.getUserData().loginStatue?MyApplication.getUserData().uid:"0";
		long time=System.currentTimeMillis();
		String validate= uid+gid+10+mPage+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid", MyApplication.getUserData().loginStatue?MyApplication.getUserData().uid:0);
			obj.put("gid", gid);
			obj.put("pagesize", 10);
			obj.put("page", mPage);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
        mForumPresenter.getForumList(obj.toString());//异步请求
//		//同步请求
//		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
//		ForumService.Api service = retrofit.create(ForumService.Api.class);
//		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
//		Call<ForumRankRespBean> call = service.getForumList(body);
//		call.enqueue(new Callback<ForumRankRespBean>() {
//			@Override
//			public void onResponse(Call<ForumRankRespBean> call, Response<ForumRankRespBean> response) {
//				Log.e("requestSuccess", "-----------------------" + response.body());
//				ForumRankRespBean bean= response.body();
////                        if(mPage>1)
////                            mAdapter.appendList(bean.getData().getList());
////                        else
//				mAdapter.clearAndAddList(bean.getData().getList());
//				mPage++;
//			}
//
//			@Override
//			public void onFailure(Call<ForumRankRespBean> call, Throwable throwable) {
//				Log.e("requestFailure", throwable.getMessage() + "");
//			}
//		});
	}

	public void addDelFavorite(final int act){//1添加2删除
		String temp = MyApplication.getUserData().loginStatue ? MyApplication.getUserData().uid : "0";
		int uid=Integer.parseInt(temp);
		int fid = mAdapter.getDataByPosition(clickPosition).getFid();
		long time=System.currentTimeMillis();
		String validate= ""+uid+fid+act+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid", MyApplication.getUserData().uid);
			obj.put("fid",fid);
			obj.put("act",act);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		ForumService.Api service = retrofit.create(ForumService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<ResponseBody> call = service.setFidFavorite(body);
		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				try {
					String json=response.body().string();
					Log.e("requestSuccess", "-----------------------" + json);
					JSONObject jsonObject=new JSONObject(json);
					if(jsonObject.optInt("code")==1){
						mAdapter.getDataByPosition(clickPosition).setIsfavorite(act==1?1:0);
						mAdapter.notifyDataSetChanged();
					}else{
						ToastUtil.showToast(mContext,jsonObject.opt("msg")+"");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Call<ResponseBody> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(mForumPresenter!=null){
			mForumPresenter.setHandler(mHandler);
		}
	}
}
