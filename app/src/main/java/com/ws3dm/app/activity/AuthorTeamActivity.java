package com.ws3dm.app.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.Constant;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.AuthorTeamBean;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.mvp.model.RespBean.OrigauthorRespBean;
import com.ws3dm.app.mvp.presenter.OriginalPresenter;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.util.glide.GlideUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Describution : 团队和作者Activity
 * 
 * Author : DKjuan
 * 
 * Date : 2019/8/7 9:49
 **/
public class AuthorTeamActivity extends BaseActivity implements View.OnClickListener{
	private CommonRecyclerAdapter<AuthorTeamBean> mAdapter;
	private LinearLayout imgReturn;
	private TextView mTitle;
	private XRecyclerView recyclerview;
	private List<NewsBean> listData = new ArrayList<>();
	private int type;//2原创作者3入住媒体
	private int totalCount,mPage;
	private OriginalPresenter mOriginalPresenter;
	private Handler mHandler;
	private Toolbar toolbar;

	@Override
	protected void init() {
		setContentView(R.layout.ac_base_recyclerview);
		toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		type=getIntent().getIntExtra("type",0);
		mPage=1;

		mTitle= (TextView) findViewById(R.id.base_title);
		recyclerview= (XRecyclerView) findViewById(R.id.recyclerview);


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
					case Constant.Notify.ORIGIN_AUTHOR://处理返回结果
						initData((OrigauthorRespBean) msg.obj);
						break;
				}
			}
		};
		mOriginalPresenter = OriginalPresenter.getInstance();
		mOriginalPresenter.setHandler(mHandler);
		
		initView();
	}
	
	public void initView(){
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		if(type==3)//3入住媒体
			mTitle.setText("入驻作者");
		else//2原创作者团队
			mTitle.setText("我们的团队");

		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerview.setLayoutManager(layoutManager);

		recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);       //原来就注释掉的
		recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
		recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);
		
		mAdapter = new CommonRecyclerAdapter<AuthorTeamBean>(mContext, R.layout.adapter_author_team) {
			@Override
			public void bindData(RecyclerViewHolder holder, final int position, final AuthorTeamBean item) {
				GlideUtil.loadCircleImage(mContext,item.getLitpic(),(ImageView)holder.getView(R.id.img_head));
				holder.setText(R.id.tv_name, item.getName());
				holder.setText(R.id.tv_info, item.getDesc());
				holder.setText(R.id.txt_label, "发布 "+item.getTotal_pub()+" 篇");
			}
		};
		recyclerview.setAdapter(mAdapter);
		recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
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
//                        mBinding.recyclerview.loadMoreComplete();
//						mAdapter.notifyDataSetChanged();
					}
				}, 50);
			}
		});
		
		mAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int position) {

				Intent intent=new Intent(mContext,ArticleListActivity.class);
				intent.putExtra("authorid",mAdapter.getDataByPosition(position).getAid());
//				Intent intent=new Intent(mContext, AuthorDetailActivity.class);
//				intent.putExtra("uid",mAdapter.getDataByPosition(position).getAuthor_id()+"");
				startActivity(intent);
			}
		});
		recyclerview.setRefreshing(true);//refresh的监听处理中已经有加载数据的操作
	}

	public void obtainData(){
		//获取数据
		long time=System.currentTimeMillis();
		String validate= ""+10+mPage+type+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("pagesize",10);
			obj.put("page",mPage);
			obj.put("type",type);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		mOriginalPresenter.getOrigauthor(obj.toString());//异步请求
	}

	public void initData(OrigauthorRespBean bean){
		if(mPage>1) {
			mAdapter.appendList(bean.getData().getList());
		}else {
			recyclerview.refreshComplete();
			recyclerview.setPullRefreshEnabled(false);
			totalCount=bean.getData().getTotal();;
			mAdapter.clearAndAddList(bean.getData().getList());
		}
		if(mAdapter.getItemCount()==totalCount) {
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
		}
	}
}
