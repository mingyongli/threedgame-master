package com.ws3dm.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.GameBean;
import com.ws3dm.app.Constant;
import com.ws3dm.app.databinding.AcGamecategorylistBinding;
import com.ws3dm.app.mvp.model.RespBean.GameListRespBean;
import com.ws3dm.app.mvp.presenter.GamePresenter;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;
import com.ws3dm.app.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Describution :游戏分类列表（点击标签后跳转的列表）
 * 
 * Author : DKjuan
 * 
 * Date : 2018/4/24 16:57
 **/
public class GameCategoryListActivity extends BaseActivity{

	private AcGamecategorylistBinding mBinding;
	private CommonRecyclerAdapter<GameBean> mAdapter;
	public List<GameBean> listData = new ArrayList<GameBean>();//全部置顶

	private int mPage=1;
	private GamePresenter mGamePresenter;
	private Handler mHandler;
	private String labelid;//标签编号

	@Override
	protected void init() {
		mBinding = bindView(R.layout.ac_gamecategorylist);
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
					case Constant.Notify.RESULT_GAME_CHOICE://处理返回结果
						initData((GameListRespBean) msg.obj);
						break;
				}
			}
		};

		mGamePresenter = GamePresenter.getInstance();
		mGamePresenter.setHandler(mHandler);

		initView();
		obtainData();
	}

	//界面初始化
	private void initView() {
		//获取 Arguments
		mBinding.tvTitle.setText(getIntent().getStringExtra("title"));
		labelid=getIntent().getStringExtra("labelid")==null?"0":getIntent().getStringExtra("labelid");
		mPage = 1;

		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.recyclerview.setLayoutManager(layoutManager);

		mBinding.recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
		mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);

		mAdapter = new CommonRecyclerAdapter<GameBean>(mContext, R.layout.adapter_game) {
			@Override
			public void bindData(RecyclerViewHolder holder, final int position, final GameBean game) {
				holder.setImageByUrl(R.id.imgCover,game.getLitpic());
				holder.setText(R.id.tv_score, game.getScore()+"");
				holder.setText(R.id.txtName, game.getTitle());
				holder.setText(R.id.tv_type, "类型：" +game.getType());
				holder.setText(R.id.txtTime, "发售："+ (game.getPubdate_at()==0?"未知":TimeUtil.getFormatTimeSimple(game.getPubdate_at())));
				holder.setText(R.id.tv_label, "标签：" +game.getLabelString());

				holder.getView(R.id.txt_label1).setVisibility(View.GONE);
				holder.getView(R.id.txt_label2).setVisibility(View.GONE);
				holder.getView(R.id.txt_label3).setVisibility(View.GONE);
				holder.getView(R.id.txt_label4).setVisibility(View.GONE);
				holder.getView(R.id.txt_label5).setVisibility(View.GONE);
				String[] sy=game.getSystem().split("/");
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

//                double score= Keep1DecimalPlaces(10*Math.random());
//                holder.setText(R.id.score, score+"");
//                ImageView imgScore=holder.getView(R.id.img_score);
//                // 获得ClipDrawable对象  
//                ClipDrawable clipDrawable = (ClipDrawable) imgScore.getBackground();
//                // 设置裁剪级别，Clip类型的图片默认裁剪级别为0，此时是全部裁剪，图片看不见；  
//                // 当级别为10000时，不裁剪图片，图片全部可见  
//                // 当全部显示后，设置不可见  
//                clipDrawable.setLevel((int)(score*1000));
			}
		};
		mAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int position) {
				Intent intent = new Intent(mContext, GameHomeActivity.class);
				Bundle bundle = new Bundle();
//				bundle.putSerializable("game", mAdapter.getDataByPosition(position));
//					bundle.putSerializable("game", finalGame);//数据太大，超出系统限制
				GameBean tempGame=mAdapter.getDataByPosition(position);
				bundle.putString("str_game", JSON.toJSONString(tempGame));
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
					}
				}, 50);
			}
		});
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
		long time=System.currentTimeMillis();
		String validate= "10"+mPage+labelid+time;
		String sign= StringUtil.MD5(validate);
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
		mGamePresenter.getGameChice(obj.toString());//异步请求
	}

	private void initData(GameListRespBean bean) {
		mBinding.recyclerview.refreshComplete();
		if(bean!=null&&bean.getData()!=null) {
			listData = bean.getData().getList();
		}
		if(mPage>1) {
			mAdapter.appendList(listData);
		}else {
			mAdapter.clearAndAddList(listData);
		}
		if (listData==null||listData.size()<10) {
			mBinding.recyclerview.setNoMore(true);
		} else {
			mPage++;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if(mGamePresenter!=null){
			mGamePresenter.setHandler(mHandler);
		}
	}
}
