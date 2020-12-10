package com.ws3dm.app.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.Constant;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.BaseRecyclerAdapter;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.AvatarBean;
import com.ws3dm.app.bean.GameBean;
import com.ws3dm.app.bean.GameHotPhb;
import com.ws3dm.app.databinding.AcMgSelectBinding;
import com.ws3dm.app.mvp.model.RespBean.GameOLHotRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameOLitemRespBean;
import com.ws3dm.app.mvp.presenter.GamePresenter;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.GameService;
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
 * Describution :手有专题检索
 * 
 * Author : DKjuan
 * 
 * Date : 2019/9/28 10:53
 **/
public class SelectMGListActivity extends BaseActivity{

	private AcMgSelectBinding mBinding;
	private CommonRecyclerAdapter<GameHotPhb> mAdapter;
	private BaseRecyclerAdapter<AvatarBean> resultAdapter;
	private RecyclerView mLabelRecyclerView;
	private BaseRecyclerAdapter<AvatarBean> mLabelAdapter;
	private PopupWindow mPopupWindow;
	public List<GameHotPhb> listData =new ArrayList<>();
	public List<AvatarBean> itemResult =new ArrayList<>();
	private String cate_id="0",txt_cate="筛选";////▲  ▼
	private int totalSize,mPage;
	private Boolean onSelect;
	private GameOLitemRespBean mGameOLitemRespBean;
	private GamePresenter mGamePresenter;
	private Handler mHandler;

	@Override
	protected void init() {
		mBinding = bindView(R.layout.ac_mg_select);
		mBinding.setHandler(this);
		
		mBinding.tvTitle.setText("游戏专题");

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
						break;
					case Constant.Notify.RESULT_GAME_SYITEM://处理返回结果
						mGameOLitemRespBean = (GameOLitemRespBean) msg.obj;
						SharedUtil.setSharedPreferencesData("todaySYGame", TimeUtil.dateDayNow());
						SharedUtil.setSharedPreferencesData("mGameSYitemRespBean", JSON.toJSONString(msg.obj));
						break;
				}
			}
		};

		mGamePresenter = GamePresenter.getInstance();
		mGamePresenter.setHandler(mHandler);

		//		obtainData();//在recyclerview中获取数据
		if (TimeUtil.dateDayNow().equals(SharedUtil.getSharedPreferencesData("todaySYGame"))) {//存储游戏库标签
			String json = SharedUtil.getSharedPreferencesData("mGameSYitemRespBean");
			if (!StringUtil.isEmpty(json) && json.length() > 100) {
				mGameOLitemRespBean = JSON.parseObject(json, GameOLitemRespBean.class);
			}
		} else {
			obtainItems();
		}

		initView();
	}

	//界面初始化
	private void initView() {
		mPage = 1;

		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.recyclerview.setLayoutManager(layoutManager);

		mBinding.recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
		mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);

		mAdapter = new CommonRecyclerAdapter<GameHotPhb>(mContext, R.layout.adapter_mg_game) {
			@Override
			public void bindData(RecyclerViewHolder holder, final int position, final GameHotPhb item) {
				GlideUtil.loadRoundImage(mContext,item.getLitpic(), (ImageView) holder.getView(R.id.imgCover),5);
//				holder.setImageByUrl(R.id.imgCover,item.getLitpic());
				holder.setText(R.id.txtName,item.getTitle());
				holder.setText(R.id.tv_type,"类型："+item.getType());
				holder.setText(R.id.tv_label,"标签："+item.getLabel());
			}
		};
		mAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int position) {
				GameHotPhb originalBean=mAdapter.getDataByPosition(position);
				GameBean mGame=new GameBean();
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
				obtainData();
			}
		});
		mBinding.recyclerview.setRefreshing(true);

		//初始化popview
		mLabelAdapter = new BaseRecyclerAdapter<AvatarBean>(mContext, R.layout.item_pop_label) {
			@Override
			public void bindData(RecyclerViewHolder holder, int position, AvatarBean item) {
				onSelect=item.getName().equals(txt_cate)||txt_cate.equals("筛选") && item.getName().equals("全部")?true:false;
				TextView textView=holder.getView(R.id.tv_name);
				if (onSelect) {
					textView.setTextColor(getResources().getColor(R.color.white));
					textView.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_fillred));
				} else {
					textView.setTextColor(getResources().getColor(R.color.black_55));
					textView.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_gray));
				}
				textView.setText(item.getName());
			}
		};
		
		mLabelAdapter.setOnClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int position) {
				cate_id=mLabelAdapter.getDataByPosition(position).getId();;//▲  ▼
				txt_cate=mLabelAdapter.getDataByPosition(position).getName();
				if(txt_cate.equals("全部")){
					txt_cate = "筛选";
					mBinding.tvCate.setText("筛选▼");
				}else{
					mBinding.tvCate.setText(txt_cate+"▼");
				}
				mPage=1;
				obtainData();
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

	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	private void showPopView() {
		//展示popview
		mBinding.tvCate.setText(txt_cate+"▲");//▲  ▼
		mLabelAdapter.clearAndAddList(mGameOLitemRespBean.getData().getTypelist());

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
			case R.id.tv_cate:
				if(mPopupWindow.isShowing()) {
					mPopupWindow.dismiss();
					mBinding.tvCate.setText(txt_cate+"▼");
				}else 
					showPopView();
				break;
			case R.id.ll_topbar:
			case R.id.rlHead://头部       
				mPopupWindow.dismiss();
				mBinding.tvCate.setText(txt_cate+"▼");
				break;
			case R.id.tv_reset:
				mPopupWindow.dismiss();//cate_id="0",txt_cate="类型";////▲  ▼
				if (!txt_cate.equals("筛选")) {
					txt_cate = "筛选";
					cate_id = "0";
					mBinding.tvCate.setText("筛选▼");

//					mBinding.tvType.setTextColor(getResources().getColor(R.color.black_55));
//					mBinding.tvType.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_gray));

					mPage = 1;
					obtainData();
				}else if (txt_cate.equals("筛选")) {
					mBinding.tvCate.setText("筛选▼");
				}
				break;
		}
	}

	public void obtainData(){
		//获取数据
		long time=System.currentTimeMillis();
		String validate= "10"+mPage+cate_id+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("pagesize", 10);
			obj.put("page", mPage);
			obj.put("typeid", cate_id);
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
		Call<GameOLHotRespBean> call = service.syscreen(body);
		call.enqueue(new Callback<GameOLHotRespBean>() {
			@Override
			public void onResponse(Call<GameOLHotRespBean> call, Response<GameOLHotRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				initData(response.body());
			}

			@Override
			public void onFailure(Call<GameOLHotRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
				mBinding.recyclerview.loadMoreError();
			}
		});
	}

	private void initData(GameOLHotRespBean bean) {
		mBinding.recyclerview.refreshComplete();
		if(bean!=null&&bean.getData()!=null) {
			listData = bean.getData().getList();
		}
		if(mPage>1) {
			mAdapter.appendList(listData);
		}else {
			totalSize=bean.getData().getTotal();
			mAdapter.clearAndAddList(listData);
		}
		if (totalSize==mAdapter.getItemCount()) {
			mBinding.recyclerview.setNoMore(true);
		} else {
			mPage++;
		}
	}

	public void obtainItems(){
		//获取数据
		long time = System.currentTimeMillis();
		String sign = StringUtil.MD5(time + "");
		JSONObject obj = new JSONObject();
		try {
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		mGamePresenter.getSYitem(obj.toString());//异步请求
	}

	public void jumpGonglue(int aid,int showtype,String title){
		GameBean game=new GameBean();
		game.setAid(aid);
		game.setShowtype(showtype);
		game.setTitle(title);

		Intent intent = new Intent(mContext, GameNewsActivity.class);
		intent.putExtra("tag", 1);//设置哪个标签选中
		Bundle bundle1 = new Bundle();
		bundle1.putSerializable("game", game);
		intent.putExtras(bundle1);
		startActivity(intent);
	}

	private void initItems(GameOLitemRespBean bean) {
		mBinding.recyclerview.refreshComplete();
		if(bean!=null&&bean.getData()!=null) {
			itemResult = bean.getData().getTypelist();
		}else{
			mBinding.recyclerview.setNoMore(true);
			return;
		}
		if(mPage>1) {
			resultAdapter.appendList(itemResult);
		}else {
			resultAdapter.clearAndAddList(itemResult);
		}
	}
}