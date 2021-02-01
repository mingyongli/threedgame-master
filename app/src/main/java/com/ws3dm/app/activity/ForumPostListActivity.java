package com.ws3dm.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.umeng.analytics.MobclickAgent;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.bean.ForumDetail;
import com.ws3dm.app.bean.ForumDetailBean;
import com.ws3dm.app.Constant;
import com.ws3dm.app.databinding.AcForumpostlistBinding;
import com.ws3dm.app.emoj.EmotionUtils;
import com.ws3dm.app.mvp.model.RespBean.ForumTopRespBean;
import com.ws3dm.app.mvp.presenter.ForumPresenter;
import com.ws3dm.app.util.SharedUtil;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.util.glide.GlideUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Describution :论坛帖子（主题）列表,（论坛排行的下一页）
 * 
 * Author : DKjuan
 * 
 * Date : 2017/10/23 10:53
 **/
public class ForumPostListActivity extends BaseActivity{

	private AcForumpostlistBinding mBinding;
	private CommonRecyclerAdapter<ForumDetailBean> mAdapter;
	public ArrayList<ForumDetail> listData = new ArrayList<ForumDetail>();//全部置顶

	private ForumPresenter mForumPresenter;
	private Handler mHandler;
	private int fid;
	private int mPage=1;
	private EmotionUtils emoutil;

	@Override
	protected void init() {
		mBinding = bindView(R.layout.ac_forumpostlist);
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
						mBinding.recyclerview.loadMoreError();
						break;
					case Constant.Notify.RESULT_FORUMTHREAD_LIST://处理返回结果
						ForumTopRespBean bean= (ForumTopRespBean) msg.obj;
						if(mPage>1) {
							mAdapter.appendList(bean.getData().getList());
						}else {
							mAdapter.clearAndAddList(bean.getData().getList());
						}
						if(bean.getData().getList().size()<1) {
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
		emoutil=new EmotionUtils(this);
		//获取 Arguments
		fid=getIntent().getIntExtra("fid",0);
		mBinding.tvTitle.setText(getIntent().getStringExtra("title"));
		
		mPage = 1;

		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.recyclerview.setLayoutManager(layoutManager);

		mBinding.recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
		mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);

		mAdapter = new CommonRecyclerAdapter<ForumDetailBean>(mContext, R.layout.adapter_forum_detail) {
			@Override
			public void bindData(RecyclerViewHolder holder, final int position, final ForumDetailBean forum) {
//				holder.setImageByUrl(R.id.img_head,forum.getUser().avatarstr);
				GlideUtil.loadCircleImage(mContext,forum.getUser().avatarstr, (ImageView) holder.getView(R.id.img_head));
				holder.setText(R.id.tv_name, forum.getUser().nickname);
				int length=forum.getType().length();
				holder.setText(R.id.tv_tag, forum.getType());
				TextView content=holder.getView(R.id.tv_content);
				if(length<=0){
					holder.getView(R.id.tv_tag).setVisibility(View.GONE);
					content.setText(emoutil.addSmileySpans(emoutil.addSmileySpans(forum.getTitle())));
//					holder.setText(R.id.tv_content, forum.getTitle());
				}else if(length>3){
					holder.getView(R.id.tv_tag).setVisibility(View.VISIBLE);
					content.setText(emoutil.addSmileySpans("          "+"          "+emoutil.addSmileySpans(forum.getTitle())));
//					holder.setText(R.id.tv_content, "          "+"          "+forum.getTitle());
				}else{
					holder.getView(R.id.tv_tag).setVisibility(View.VISIBLE);
					content.setText(emoutil.addSmileySpans("          "+forum.getTitle()));
//					holder.setText(R.id.tv_content, "          "+forum.getTitle());
				}
				holder.setText(R.id.tv_time, TimeUtil.getTime(forum.getPubdate_at()));
				holder.setText(R.id.tv_commentnum, forum.getReplies());
				holder.setText(R.id.tv_read, forum.getViews());
			}
		};
		mAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int position) {
				MobclickAgent.onEvent(mContext,"11");
                Intent intent = new Intent(mContext, ForumDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("forumDetailBean", mAdapter.getDataByPosition(position));
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
						obtainData();
					}
				}, 50);            //refresh data here
			}

			@Override
			public void onLoadMore() {
				new Handler().postDelayed(new Runnable(){
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
			case R.id.tv_add_article://发主题
				if (MyApplication.getUserData() == null || !MyApplication.getUserData().loginStatue) {
					startActivity(new Intent(mContext, LoginActivity.class));
				} else if (MyApplication.getUserData().mobile.length() == 0) {//第三方登陆，非绑定跳转绑定界面
					Intent intent = new Intent(mContext, ForgetPassActivity.class);
					intent.putExtra("type", 1);//1,绑定手机号，2修改密码，3忘记密码
					startActivity(intent);
				} else {
					Intent intent = new Intent(mContext, PublishActivity.class);
					intent.putExtra("title",getIntent().getStringExtra("title"));
					intent.putExtra("fid",fid);
					startActivity(intent);
				}
				break;
		}
	}

	public void obtainData(){
		//获取数据
		long time=System.currentTimeMillis();
		String validate= fid+"10"+mPage+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("fid", fid);
			obj.put("pagesize", 10);
			obj.put("page", mPage);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		mForumPresenter.getForumThreadList(obj.toString());//异步请求
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

	@Override
	public void onResume() {
		super.onResume();
		if(mForumPresenter!=null){
			mForumPresenter.setHandler(mHandler);
		}
		if("1".equals(SharedUtil.getSharedPreferencesData("isPublish"))){
			SharedUtil.setSharedPreferencesData("isPublish","0");
			if(mPage==1){
				mPage=1;
				obtainData();
			}
		}
	}
}
