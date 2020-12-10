package com.ws3dm.app.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ws3dm.app.Constant;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.bean.OriginalBean;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.databinding.AcArticleListBinding;
import com.ws3dm.app.mvp.model.RespBean.OrigauthListRespBean;
import com.ws3dm.app.mvp.model.RespBean.OrignewListRespBean;
import com.ws3dm.app.mvp.presenter.OriginalPresenter;
import com.ws3dm.app.sqlite.NewsFile;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.util.glide.GlideUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Describution : 作者团队的文章列表
 * 
 * Author : DKjuan
 * 
 * Date : 2019/8/7 11:34
 **/
public class ArticleListActivity extends BaseActivity{
	private CommonRecyclerAdapter<OriginalBean> mAdapter;
	private AcArticleListBinding mBinding;
	private int authorTeamid,totalCount,mPage,tag;//tag:0作者团队,显示顶部个人信息 1专栏，只有背景图
	private String str_title;
	private OriginalPresenter mOriginalPresenter;
	private Handler mHandler;
	private NewsFile newsCollectFile;

	@Override
	protected void init() {
		mBinding=bindView(R.layout.ac_article_list);
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
					case Constant.Notify.ORIGIN_AUTHORLIST://处理作者团队
						initData((OrigauthListRespBean) msg.obj);
						break;
					case Constant.Notify.ORIGIN_COLLABEL://处理专栏
						initData((OrignewListRespBean) msg.obj);
						break;
				}
			}
		};
		mOriginalPresenter = OriginalPresenter.getInstance();
		mOriginalPresenter.setHandler(mHandler);

		newsCollectFile=new NewsFile(mContext);
		initView();
		getAuthinfo();
	}

	private void getAuthinfo(){
		UserDataBean userData = MyApplication.getUserData();
		long time = System.currentTimeMillis();
		String sign = StringUtil.newMD5(userData.uid +authorTeamid+ time + NewUrl.KEY);
		Map<String, Object> values = new HashMap<>();
		values.put("uid",userData.uid);
		values.put("sign",sign);
		values.put("time",time);
		values.put("author_id",authorTeamid);
		String json = new JSONObject(values).toString();
		OkHttpUtils
				.postString()
				.url(NewUrl.AUTH_INFO)
				.content(json)
				.build()
				.execute(new Callback<AuthorDetailActivity.AuthorDetail>() {

					@Override
					public AuthorDetailActivity.AuthorDetail parseNetworkResponse(Response response) throws IOException {
						String body = response.body().string();
						return new Gson().fromJson(body, AuthorDetailActivity.AuthorDetail.class);
					}

					@Override
					public void onError(Request request, Exception e) {
						Log.e("message",e.getMessage());
					}

					@Override
					public void onResponse(AuthorDetailActivity.AuthorDetail response) {
						if(response.getCode() == 1){
							initView(response.getData().getInfo());
						}
					}
				});
	}

	private void initView(final AuthorDetailActivity.AuthorDetail.Info udi) {
		if(udi.getIs_follow() == 0){
			mBinding.isFollow.setText("+ 关注");
			mBinding.isFollow.setTextColor(Color.parseColor("#ffffff"));
			mBinding.isFollow.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_rectangle));

		}else if(udi.getIs_follow()  == 1){
			mBinding.isFollow.setText("√ 已关注");
			mBinding.isFollow.setTextColor(Color.parseColor("#444444"));
			mBinding.isFollow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_gray));
		}else{
			mBinding.isFollow.setText("= 互相关注");
			mBinding.isFollow.setTextColor(Color.parseColor("#444444"));
			mBinding.isFollow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_gray));
		}

		mBinding.isFollow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(udi.getIs_follow() == 0){
					udi.setIs_follow(1);
					mBinding.isFollow.setText("√ 已关注");
					mBinding.isFollow.setTextColor(Color.parseColor("#444444"));
					mBinding.isFollow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_gray));
					subfollow(1,udi.getAuthor_id()+"");
				}else if(udi.getIs_follow() == 1){
					udi.setIs_follow(1);
					mBinding.isFollow.setText("+ 关注");
					mBinding.isFollow.setTextColor(Color.parseColor("#ffffff"));
					mBinding.isFollow.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_rectangle));
					subfollow(2,udi.getAuthor_id()+"");
				}else{
					udi.setIs_follow(0);
					mBinding.isFollow.setText("+ 关注");
					mBinding.isFollow.setTextColor(Color.parseColor("#ffffff"));
					mBinding.isFollow.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_rectangle));
					subfollow(2,udi.getAuthor_id()+"");
				}
			}
		});
	}
	//关注or取消
	private void subfollow(int type,String follow_uid){
		UserDataBean userData = MyApplication.getUserData();
		long time = System.currentTimeMillis();
		String sign = StringUtil.newMD5(userData.uid + follow_uid +type + time + NewUrl.KEY);

		Map<String, Object> values = new HashMap<>();
		values.put("uid",userData.uid);
		values.put("sign",sign);
		values.put("time",time);
		values.put("type",type);
		values.put("author_id",follow_uid);
		String json = new JSONObject(values).toString();
		OkHttpUtils
				.postString()
				.url(NewUrl.SUBFOLLOW_AUTH)
				.content(json)
				.build()
				.execute(new Callback<String>() {

					@Override
					public String parseNetworkResponse(Response response) throws IOException {
						return response.body().string();
					}

					@Override
					public void onError(Request request, Exception e) {
						Log.e("message",e.getMessage());
					}

					@Override
					public void onResponse(String response) {
						Log.e("FFFFFFFF",response);
					}

				});

	}

	public void initView(){
		mPage=1;
		tag=getIntent().getIntExtra("tag",0);
		if(tag<1)
			authorTeamid=getIntent().getIntExtra("authorid",0);
		else{
			authorTeamid=getIntent().getIntExtra("labelid",0);
			str_title=getIntent().getStringExtra("title");
		}

		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.recyclerview.setLayoutManager(layoutManager);

		mBinding.recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
		mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);
		
		mAdapter = new CommonRecyclerAdapter<OriginalBean>(mContext, R.layout.adapter_article_list) {
			@Override
			public void bindData(RecyclerViewHolder holder, final int position, OriginalBean item) {
				holder.setText(R.id.tv_title, item.getTitle());
				holder.setText(R.id.tv_time, TimeUtil.getFoolishTime2(item.getPubdate_at()+"000"));
				holder.setText(R.id.txtComment, item.getTotal_ct()+"");
				GlideUtil.loadRoundImage(mContext,item.getLitpic(),(ImageView)holder.getView(R.id.img_news),5);
			}
		};
		mAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int position) {
				OriginalBean originalBean = mAdapter.getDataByPosition(position);
				addHistory(originalBean);
				Intent intent = new Intent(mContext, OriginalActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("originalBean",originalBean);
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
						mBinding.recyclerview.refreshComplete();
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
//                        mBinding.mBinding.recyclerview.loadMoreComplete();
//						mAdapter.notifyDataSetChanged();
					}
				}, 50);
			}
		});

		mBinding.recyclerview.setRefreshing(true);//refresh的监听处理中已经有加载数据的操作
	}

	public void obtainData(){
		if (tag<1){//tag:0作者团队,显示顶部个人信息 1专栏，只有背景图
			//获取数据
			long time=System.currentTimeMillis();
			String validate= ""+10+mPage+authorTeamid+time;
			String sign= StringUtil.MD5(validate);
			JSONObject obj = new JSONObject();
			try {
				obj.put("pagesize",10);
				obj.put("page",mPage);
				obj.put("authorid",authorTeamid);
				obj.put("time", time);
				obj.put("sign", sign);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			mOriginalPresenter.getOrigauthList(obj.toString());//异步请求
		}else{//专栏标签详情数据
			long time = System.currentTimeMillis();
			String validate = "" + 10 + mPage+authorTeamid + time;
			String sign = StringUtil.MD5(validate);
			JSONObject obj = new JSONObject();
			try {
				obj.put("pagesize", 10);
				obj.put("page", mPage);
				obj.put("labelid", authorTeamid);
				obj.put("time", time);
				obj.put("sign", sign);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			mOriginalPresenter.getOrigcollabel(obj.toString());//异步请求
		}
		
	}

	public void initData(OrigauthListRespBean bean){
		if(mPage>1) {
			mAdapter.appendList(bean.getData().getList());
		}else {
			totalCount=bean.getData().getTotal();
//			处理顶部信息
			GlideUtil.loadImage(mContext,bean.getData().getUser().getLitpic(),mBinding.bgAuthor);
			GlideUtil.loadCircleImage(mContext,bean.getData().getUser().getAvatarstr(),mBinding.imgHead);
			mBinding.tvName.setText(bean.getData().getUser().getNickname());
			mBinding.tvInfo.setText(bean.getData().getUser().getDesc());
			mBinding.bgAuthor.setVisibility(View.VISIBLE);
			mBinding.imgShade.setVisibility(View.VISIBLE);
			mBinding.bgImgBack.setVisibility(View.VISIBLE);
			
			mAdapter.clearAndAddList(bean.getData().getList());
		}
		if(mAdapter.getItemCount()==totalCount) {
			mBinding.recyclerview.setNoMore(true);
		}else {
			mBinding.recyclerview.refreshComplete();
			mPage++;
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

	public void initData(OrignewListRespBean bean){
		if(mPage>1) {
			mAdapter.appendList(bean.getData().getList());
		}else {
			totalCount=bean.getData().getTotal();
//			处理顶部信息
			GlideUtil.loadImage(mContext,bean.getData().getLitpic(),mBinding.bgImg);
			mBinding.tvTitle.setText(str_title);
			
			mAdapter.clearAndAddList(bean.getData().getList());
		}
		if(mAdapter.getItemCount()==totalCount) {
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
			default:
				break;
		}
	}
}
