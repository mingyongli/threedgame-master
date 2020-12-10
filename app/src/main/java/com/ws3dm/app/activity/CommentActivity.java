package com.ws3dm.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.bean.GameBean;
import com.ws3dm.app.bean.MyCommentBean;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.bean.SoftGameBean;
import com.ws3dm.app.Constant;
import com.ws3dm.app.emoj.EmotionUtils;
import com.ws3dm.app.event.ErrorEvent;
import com.ws3dm.app.mvp.model.RespBean.MyCommentRespBean;
import com.ws3dm.app.mvp.presenter.UserPresenter;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Describution :CommentActivity 我的评论
 * 
 * Author : DKjuan
 * 
 * Date : 2018/3/26 19:47
 **/
public class CommentActivity extends BaseActivity implements View.OnClickListener{
	private UserPresenter mUserPresenter;
	private Handler mHandler;
	private CommonRecyclerAdapter<MyCommentBean> mAdapter;
	private LinearLayout imgReturn;
	private TextView mTitle;
	private XRecyclerView recyclerview;
	private int mPage;
	private EmotionUtils emoutil;

	@Override
	protected void init() {
		setContentView(R.layout.ac_base_recyclerview);
		
		emoutil=new EmotionUtils(this);
		imgReturn= (LinearLayout) findViewById(R.id.imgReturn);
		mTitle= (TextView) findViewById(R.id.base_title);
		recyclerview= (XRecyclerView) findViewById(R.id.recyclerview);
		imgReturn.setOnClickListener(this);

		mUserPresenter=UserPresenter.getInstance();
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (!mIsRunning) {
					return;
				}
				switch (msg.what) {
					case Constant.Notify.LOAD_FAILURE://请求失败
//						if (mBinding.loadView != null)
//							mBinding.loadView.setVisibility(View.GONE);
						ErrorEvent error = (ErrorEvent) msg.obj;
						Log.i("error_comment",""+error.message);
						recyclerview.loadMoreError();
						break;
					case Constant.Notify.RESULT_GETMYCOMMENT://查询成功
						initData((MyCommentRespBean) msg.obj);
						mPage++;
						break;
				}
			}
		};
		mUserPresenter.setHandler(mHandler);

		initView();
		loadByPage(mPage);
	}
	
	public void initView(){
		mPage=1;
		mTitle.setText("我的评论");

		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerview.setLayoutManager(layoutManager);

		recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
		recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);
		recyclerview.setPullRefreshEnabled(true);
		recyclerview.setLoadingMoreEnabled(true);

		mAdapter = new CommonRecyclerAdapter<MyCommentBean>(mContext, R.layout.item_comment) {
			@Override
			public void bindData(RecyclerViewHolder holder, int position, MyCommentBean bean) {
//				holder.setImageByUrl(R.id.imgCover, game.getCover());
				holder.setText(R.id.tv_title,(position+1)+"、"+bean.getTitle().trim());
				TextView content=holder.getView(R.id.tv_content);
				content.setText(emoutil.addSmileySpans(emoutil.addSmileySpans(bean.getContent())));
//				holder.setText(R.id.tv_content,emoutil.addSmileySpans(bean.getContent()));
				holder.setText(R.id.tv_time, TimeUtil.getTime(bean.getPubdate_at()));
				holder.setText(R.id.tv_num,bean.getTotal_ct()+"");
//				holder.setText(R.id.tv_num,bean.getHas_reply()+"");
			}
		};
		recyclerview.setAdapter(mAdapter);
		recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
			@Override
			public void onRefresh() {
				new Handler().postDelayed(new Runnable(){
					public void run() {
						recyclerview.refreshComplete();
						mPage=1;
						loadByPage(mPage);
					}
				}, 50);            //refresh data here
			}

			@Override
			public void onLoadMore() {
				new Handler().postDelayed(new Runnable(){
					public void run() {
						loadByPage(mPage);
//                        mBinding.recyclerview.loadMoreComplete();
//						mAdapter.notifyDataSetChanged();
					}
				}, 50);
			}
		});

		mAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int position) {
				MyCommentBean bean=mAdapter.getDataByPosition(position);
				jump(bean);
			}
		});
	}
	
	public void jump(MyCommentBean bean){//1新闻2攻略3游戏4手游5杂谈6评测7原创8安利9礼包10视频11专栏
		Intent intent=null;
		switch(bean.getShowtype()) {
			case 1:
			case 2:
			case 5:
			case 6:
			case 7:
			case 8:
			case 10:
				NewsBean news = new NewsBean();
				news.setTitle(bean.getTitle());
				news.setArcurl(bean.getArcurl());
				news.setWebviewurl(bean.getWebviewurl());
				news.setType(""+bean.getShowtype());
				intent = new Intent(mContext, NewsActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("newsBean", news);
				intent.putExtras(bundle);
				mContext.startActivity(intent);
				break;
			case 3://游戏
				intent = new Intent(mContext, GameHomeActivity.class);
				GameBean game=new GameBean();
				game.setAid(bean.getAid());
				game.setTitle(bean.getTitle());
				game.setArcurl(bean.getArcurl());
				game.setWebviewurl(bean.getWebviewurl());
				game.setType(""+bean.getShowtype());
				Bundle bundlegame = new Bundle();
//					bundle.putSerializable("game", finalGame);//数据太大，超出系统限制
				bundlegame.putString("str_game", JSON.toJSONString(bundlegame));
				intent.putExtras(bundlegame);
				startActivity(intent);
				break;
			case 4://手游
				SoftGameBean soft = new SoftGameBean();
				soft.setTitle(bean.getTitle());
				soft.setArcurl(bean.getArcurl());
				soft.setAid(bean.getAid());
				soft.setType(""+bean.getShowtype());
				intent = new Intent(mContext, MGDetailActivity.class);
				Bundle bundlesoft = new Bundle();
				bundlesoft.putSerializable("mSoftGameBean", soft);
				intent.putExtras(bundlesoft);
				mContext.startActivity(intent);
				break;
		}
	}

	public void loadByPage(int p){
		//获取数据
		long time=System.currentTimeMillis();
//		int isavatar=1;//是否展示头像库1是0否
		String validate= ""+MyApplication.getUserData().uid+10+p+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid", MyApplication.getUserData().uid);
			obj.put("pagesize",10);
			obj.put("page",p);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		mUserPresenter.getMyComment(obj.toString());
	}
	
	public void initData(MyCommentRespBean bean){
		if(mPage>1) {
			mAdapter.appendList(bean.getData().getList());
		}else {
			mAdapter.clearAndAddList(bean.getData().getList());
		}
		if(bean.getData().getList().size()==0||bean.getData().getList().size()<1) {
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
			default:
				break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if(mUserPresenter!=null){
			mUserPresenter.setHandler(mHandler);
		}
	}
}
