package com.ws3dm.app.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.BaseRecyclerAdapter;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.GameBean;
import com.ws3dm.app.bean.GameTestBean;
import com.ws3dm.app.databinding.AcGameTestBinding;
import com.ws3dm.app.mvp.model.RespBean.GameOLtestRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.GameService;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;
import com.ws3dm.app.util.ToastUtil;

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
 * Describution : 开测表
 * 
 * Author : DKjuan
 * 
 * Date : 2019/6/19 9:19
 **/
public class GameTestActivity extends BaseActivity{

	private AcGameTestBinding mBinding;
	private CommonRecyclerAdapter<GameTestBean> mAdapter;
	private List<GameTestBean> listData = new ArrayList<GameTestBean>();
	private RecyclerView mLabelRecyclerView;
	private BaseRecyclerAdapter<String> mLabelAdapter;
	private PopupWindow mPopupWindow;
	private int mPage,currentShow = -1;////0年份 1月份 -1啥都不选
	private String txt_year,txt_month;////▲  ▼
	private List<String> listYear=new ArrayList<>();
	private List<String> listMonth=new ArrayList<>();
	private boolean onSelect;

	@Override
	protected void init() {
		mBinding = bindView(R.layout.ac_game_test);
		mBinding.setHandler(this);

		initView();
//		obtainData();//在recyclerview中获取数据
	}

	//界面初始化
	private void initView() {
		//初始化year，month
		int year=TimeUtil.getCurrentYear();
		for(int i=-3;i<4;i++)
			listYear.add(year-i+"");
		for (int i=1;i<13;i++)
			listMonth.add(i+"");

		txt_year=year+"";
		txt_month=TimeUtil.getCurrentMonth()+"";
		mBinding.tvYear.setText(txt_year+"年 ▼");
		mBinding.tvMonth.setText(txt_month+"月 ▼");
		
		mPage = 1;

		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.recyclerview.setLayoutManager(layoutManager);

		mBinding.recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
		mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);

		mAdapter = new CommonRecyclerAdapter<GameTestBean>(mContext, R.layout.adapter_game_test) {
			@Override
			public void bindData(RecyclerViewHolder holder, final int position, final GameTestBean game) {
				holder.setImageByUrl(R.id.imgCover,game.getLitpic());
				if(game.getScore()==0){
					holder.getView(R.id.tv_score).setVisibility(View.GONE);
				}else{
					holder.setText(R.id.tv_score, game.getScore()+"");
					holder.getView(R.id.tv_score).setVisibility(View.VISIBLE);
				}
				
				holder.setText(R.id.txtName, game.getTitle());
				holder.setText(R.id.tv_type, "类型：" +(StringUtil.isEmpty(game.getType())?"待补充":game.getType()));
				holder.setText(R.id.tv_firm, "运营：" +game.getFirm());
				holder.setText(R.id.txtTime, "时间："+ (game.getPubdate_at()==0?"未知":TimeUtil.getTimeEN(game.getPubdate_at())));
				holder.setText(R.id.tv_label, "状态：" +game.getState());
			}
		};
		mAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int position) {
				final GameBean mGame = new GameBean();
				GameTestBean originalBean = mAdapter.getDataByPosition(position);
				if(originalBean.getAid()==0){
					ToastUtil.showToast(mContext,"暂未建立游戏专区");
				}else {
					mGame.setAid(originalBean.getAid());
					mGame.setArcurl(originalBean.getArcurl());
					mGame.setShowtype(originalBean.getShowtype());

					Intent intent = new Intent(mContext, GameHomeActivity.class);
					Bundle bundle = new Bundle();
//						bundle.putSerializable("game",mGame);
//					bundle.putSerializable("game", finalGame);//数据太大，超出系统限制
					bundle.putString("str_game", JSON.toJSONString(mGame));
					intent.putExtras(bundle);
					startActivity(intent);
				}
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
		mBinding.recyclerview.setRefreshing(true);

		//初始化popview
		mLabelAdapter = new BaseRecyclerAdapter<String>(mContext, R.layout.item_pop_label) {
			@Override
			public void bindData(RecyclerViewHolder holder, int position, String item) {
				switch (currentShow) {
					case 0:
						onSelect=item.equals(txt_year)?true:false;
						break;
					case 1:
						onSelect=item.equals(txt_month)?true:false;
						break;
				}
				TextView textView=holder.getView(R.id.tv_name);
				if(onSelect){
					textView.setTextColor(getResources().getColor(R.color.white));
					textView.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_fillred));
				}else{
					textView.setTextColor(getResources().getColor(R.color.black_55));
					textView.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_gray));
				}
				textView.setText(item);
			}
		};

		mLabelAdapter.setOnClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int position) {
				switch (currentShow) {
					case 0:
						txt_year=mLabelAdapter.getDataByPosition(position);//▲  ▼
						mBinding.tvYear.setText(txt_year);
						mPage=1;
						obtainData();
						break;
					case 1:
						txt_month=mLabelAdapter.getDataByPosition(position);//▲  ▼
						mBinding.tvMonth.setText(txt_month);
						mPage=1;
						obtainData();
						break;
				}
				reset();
				mPopupWindow.dismiss();
			}
		});

		View contentViewCate = LayoutInflater.from(mContext).inflate(R.layout.pop_list_game, null);
		mLabelRecyclerView = (RecyclerView) contentViewCate.findViewById(R.id.recycler_view_cate);
		mLabelRecyclerView.setAdapter(mLabelAdapter);
		contentViewCate.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mPopupWindow.dismiss();
			}
		});

		GridLayoutManager manager = new GridLayoutManager(mContext, 4);
		mLabelRecyclerView.setLayoutManager(manager);

		mPopupWindow = new PopupWindow(contentViewCate, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setAnimationStyle(R.style.AnimBottom);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(false);
		mPopupWindow.setFocusable(false); // pop 显示时，让外部 view 响应点击事件
	}

	private void reset(){
		switch (currentShow) {//还原状态
			case 0:
				mBinding.tvYear.setText(txt_year+"年 ▼");//▲  ▼
				mBinding.tvYear.setTextColor(getResources().getColor(R.color.black_55));
				mBinding.tvYear.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_gray));
				break;
			case 1:
				mBinding.tvMonth.setText(txt_month+"月 ▼");//▲  ▼  ∧ ∨
				mBinding.tvMonth.setTextColor(getResources().getColor(R.color.black_55));
				mBinding.tvMonth.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_gray));
				break;
			default:
				break;
		}
		currentShow=-1;
	}

	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	private void showPopView(int tag) {//0平台 1语言 2类型 3发售时间
		reset();
		currentShow=tag;
		switch (currentShow) {//设置新状态
			case 0:
				mBinding.tvYear.setText(txt_year+"年 ▲");//▲  ▼
				mBinding.tvYear.setTextColor(getResources().getColor(R.color.white));
				mBinding.tvYear.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_fillred));
				mLabelAdapter.clearAndAddList(listYear);
				break;
			case 1:
				mBinding.tvMonth.setText(txt_month+"月 ▲");//▲  ▼  ∧ ∨
				mBinding.tvMonth.setTextColor(getResources().getColor(R.color.white));
				mBinding.tvMonth.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_fillred));
				mLabelAdapter.clearAndAddList(listMonth);
				break;
			default:
				break;
		}

		if (Build.VERSION.SDK_INT >= 24) {//适配安卓7.0popview 显示位置不对
			Rect visibleFrame = new Rect();
			mBinding.recyclerview.getGlobalVisibleRect(visibleFrame);
			mPopupWindow.setHeight(visibleFrame.height());
			mPopupWindow.showAtLocation(mBinding.llTopbar, Gravity.TOP, visibleFrame.left,visibleFrame.top);
		}else{
			int[] location = new int[2];
			mBinding.llTopbar.getLocationInWindow(location);
			mPopupWindow.showAsDropDown(mBinding.llTopbar, Gravity.TOP, location[0], location[1] + mBinding.llTopbar.getHeight());
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	public void clickHandler(View view) {
		switch (view.getId()) {
			case R.id.imgReturn:// 返回
				onBackPressed();
				break;
			case R.id.tv_year:
				mPopupWindow.dismiss();
				if(currentShow!=0)
					showPopView(0);
				else
					reset();
				break;
			case R.id.tv_month:
				mPopupWindow.dismiss();
				if(currentShow!=1)
					showPopView(1);
				else
					reset();
				break;
			case R.id.rlHead://头部
				mPopupWindow.dismiss();
				reset();
				break;
			case R.id.tv_reset://重置
				mPopupWindow.dismiss();
				int year=TimeUtil.getCurrentYear();
				int month=TimeUtil.getCurrentMonth();
				if(!txt_year.equals(""+year)||!txt_month.equals(""+month)){
					//初始化year，month
					txt_year = year + "";
					txt_month = month + "";
					mBinding.tvYear.setText(txt_year + "年 ▼");
					mBinding.tvMonth.setText(txt_month + "月 ▼");
//					mBinding.tvYear.setTextColor(getResources().getColor(R.color.black_55));
//					mBinding.tvYear.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_gray));
//					mBinding.tvMonth.setTextColor(getResources().getColor(R.color.black_55));
//					mBinding.tvMonth.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_gray));

					mPage=1;
					obtainData();
				}
				break;
		}
	}

	public void obtainData(){
		//获取数据
		long time=System.currentTimeMillis();
		String validate= "10"+mPage+txt_year+txt_month+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("pagesize", 10);
			obj.put("page", mPage);
			obj.put("year", txt_year);
			obj.put("month", txt_month);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
//        mForumPresenter.getForumRank(obj.toString());
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		GameService.Api service = retrofit.create(GameService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<GameOLtestRespBean> call = service.oltest(body);
		call.enqueue(new Callback<GameOLtestRespBean>() {
			@Override
			public void onResponse(Call<GameOLtestRespBean> call, Response<GameOLtestRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				initData(response.body());
			}

			@Override
			public void onFailure(Call<GameOLtestRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
				mBinding.recyclerview.loadMoreError();
			}
		});
	}

	private void initData(GameOLtestRespBean bean) {
		mBinding.recyclerview.refreshComplete();
		if(bean!=null&&bean.getData()!=null) {
			listData = bean.getData().getList();
		}
		if(mPage>1) {
			mAdapter.appendList(listData);
		}else {
			mBinding.recyclerview.scrollToPosition(0);
			mAdapter.clearAndAddList(listData);
		}
		if (listData==null||listData.size()<10) {
			mBinding.recyclerview.setNoMore(true);
		} else {
			mPage++;
		}
	}
}