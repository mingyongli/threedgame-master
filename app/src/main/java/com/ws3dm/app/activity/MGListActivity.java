package com.ws3dm.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.SoftGameBean;
import com.ws3dm.app.Constant;
import com.ws3dm.app.databinding.AcMglistBinding;
import com.ws3dm.app.mvp.model.RespBean.MGChinaRespBean;
import com.ws3dm.app.mvp.presenter.MGPresenter;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Describution :手游-游戏和软件顶部图片点击后跳转
 * 
 * Author : DKjuan
 * 
 * Date : 2018/4/24 16:57
 **/
public class MGListActivity extends BaseActivity{

	private AcMglistBinding mBinding;
	private CommonRecyclerAdapter<SoftGameBean> mAdapter;
	public List<SoftGameBean> listData = new ArrayList<SoftGameBean>();//全部置顶

	private int mPage=1;
	private MGPresenter mMGPresenter;
	private Handler mHandler;
	private String platid,typeid,tagid;//平台编号,分类编号,标签编号

	@Override
	protected void init() {
		mBinding = bindView(R.layout.ac_mglist);
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
					case Constant.Notify.RESULT_MG_CHOICE://处理返回结果
						initData((MGChinaRespBean) msg.obj);
						break;
				}
			}
		};

		mMGPresenter = MGPresenter.getInstance();
		mMGPresenter.setHandler(mHandler);

		initView();
	}

	//界面初始化
	private void initView() {
		//获取 Arguments  platid,typeid,tagid;//平台编号,分类编号,标签编号
		mBinding.tvTitle.setText(getIntent().getStringExtra("title"));
		platid=getIntent().getStringExtra("platid")==null?"0":getIntent().getStringExtra("platid");
		typeid=getIntent().getIntExtra("typeid",0)+"";
		tagid=getIntent().getIntExtra("tagid",0)+"";
		mPage = 1;

		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.recyclerview.setLayoutManager(layoutManager);

		mBinding.recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
		mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);

		mAdapter=new CommonRecyclerAdapter<SoftGameBean>(mContext, R.layout.item_game_horizon) {
			@Override
			public void bindData(RecyclerViewHolder holder, int position, final SoftGameBean item) {
				holder.setImageByUrl(R.id.imgCover,item.getLitpic());
				holder.setText(R.id.tv_title,item.getTitle());
				holder.setText(R.id.tv_data,"v"+item.getSoft_ver()+" | "+item.getSoft_size());
				holder.setText(R.id.tv_info,item.getDesc());
				holder.getView(R.id.bt_download).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
//						DownloadUtil.down(mContext,item.getDownurl(),item.getTitle()+"|"+item.getLitpic());
						Intent intent = new Intent(mContext, MGDetailActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("mSoftGameBean",item);
						intent.putExtras(bundle);
						mContext.startActivity(intent);
					}
				});
			}
		};
		mAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int position) {
				Intent intent = new Intent(mContext, MGDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("mSoftGameBean", mAdapter.getDataByPosition(position));
				intent.putExtras(bundle);
				mContext.startActivity(intent);
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

		obtainData();
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
		String currentOrder="1";///currentOrder,typeid;//，排序（1最新2热门）,分类编号
		long time=System.currentTimeMillis();
		String validate= ""+10+mPage+typeid+tagid+currentOrder+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("pagesize", 10);
			obj.put("page", mPage);
			obj.put("typeid", typeid);
			obj.put("tagid", tagid);
			obj.put("order", currentOrder);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		mMGPresenter.getChoiceMG(obj.toString());//异步请求
	}

	private void initData(MGChinaRespBean bean) {
		mBinding.recyclerview.refreshComplete();
		if(bean!=null&&bean.getData()!=null) {
			listData = bean.getData().getList();
		}
		if(mPage>1) {
			mAdapter.appendList(listData);
		}else {
			mAdapter.clearAndAddList(listData);
		}
		if (listData==null||listData.size()<1) {
			mBinding.recyclerview.setNoMore(true);
		} else {
			mPage++;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if(mMGPresenter!=null){
			mMGPresenter.setHandler(mHandler);
		}
	}
}
