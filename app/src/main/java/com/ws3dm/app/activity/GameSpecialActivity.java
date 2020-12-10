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
import com.ws3dm.app.databinding.AcGameSpecialBinding;
import com.ws3dm.app.mvp.model.RespBean.GameOLHotRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameOLitemRespBean;
import com.ws3dm.app.mvp.presenter.GamePresenter;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.GameService;
import com.ws3dm.app.util.SharedUtil;
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
 * Describution : 游戏专题 热门网游
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2019/11/19 14:45
 **/
public class GameSpecialActivity extends BaseActivity {

	private AcGameSpecialBinding mBinding;
	private CommonRecyclerAdapter<GameHotPhb> mAdapter;

	private GamePresenter mGamePresenter;
	private Handler mHandler;

	private RecyclerView mLabelRecyclerView;
	private BaseRecyclerAdapter<AvatarBean> mLabelAdapter;
	private PopupWindow mPopupWindow;
	public List<GameHotPhb> listData = new ArrayList<GameHotPhb>();
	private int mPage, currentShow = -1;////0平台 1语言 2类型 3发售时间 -1啥都不选
	private String platid = "0", langid = "0", typeid = "0", saletime = "全部";//平台语言分类选项
	private String txt_type = "类型", txt_publish = "厂商", txt_label = "标签", txt_time = "时间";////▲  ▼
	private List<AvatarBean> listYear = new ArrayList<>();
	private GameOLitemRespBean mGameOLitemRespBean;
	private boolean onSelect, isCate;//是否为分类专题

	@Override
	protected void init() {
		mBinding = bindView(R.layout.ac_game_special);
		mBinding.setHandler(this);

		isCate = getIntent().getBooleanExtra("isCate", false);
		if (isCate) {
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
						case Constant.Notify.RESULT_GAME_OLITEM://处理返回结果
							mGameOLitemRespBean = (GameOLitemRespBean) msg.obj;
							SharedUtil.setSharedPreferencesData("todayOLGame", TimeUtil.dateDayNow());
							SharedUtil.setSharedPreferencesData("mGameOLitemRespBean", JSON.toJSONString(msg.obj));
							break;
					}
				}
			};

			mGamePresenter = GamePresenter.getInstance();
			mGamePresenter.setHandler(mHandler);

			mBinding.tvTitle.setText("游戏专题");
			mBinding.llTopbar.setVisibility(View.VISIBLE);
			mBinding.tvReset.setVisibility(View.VISIBLE);

			//		obtainData();//在recyclerview中获取数据
			if (TimeUtil.dateDayNow().equals(SharedUtil.getSharedPreferencesData("todayOLGame"))) {//存储游戏库标签
				String json = SharedUtil.getSharedPreferencesData("mGameOLitemRespBean");
				if (!StringUtil.isEmpty(json) && json.length() > 100) {
					mGameOLitemRespBean = JSON.parseObject(json, GameOLitemRespBean.class);
				}
			} else {
				obtainItems();
			}
		}

		initView();
	}

	//界面初始化
	private void initView() {
		int year = TimeUtil.getCurrentYear();
		listYear.add(new AvatarBean("全部"));
		listYear.add(new AvatarBean("After " + year));
		for (int i = 0; i < 5; i++)
			listYear.add(new AvatarBean(year - i + ""));
		listYear.add(new AvatarBean("Before"));
		//获取 Arguments  platid,typeid,tagid;//平台编号,分类编号,标签编号
		mPage = 1;

		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.recyclerview.setLayoutManager(layoutManager);

		mBinding.recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
		mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);

		mAdapter = new CommonRecyclerAdapter<GameHotPhb>(mContext, R.layout.adapter_special_hot) {
			@Override
			public void bindData(RecyclerViewHolder holder, final int position, final GameHotPhb game) {
				holder.setImageByUrl(R.id.imgCover, game.getLitpic());
				holder.setText(R.id.tv_score, game.getScore() + "");
				holder.setText(R.id.txtName, game.getTitle());
				holder.setText(R.id.tv_type, "类型：" + game.getType());
				holder.setText(R.id.tv_firm, "运营：" + game.getFirm());
				holder.setText(R.id.txtTime, "公测：" + game.getBetatime());
				holder.setText(R.id.tv_label, "标签：" + game.getLabel());

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
				final GameBean mGame = new GameBean();
				GameHotPhb originalBean = mAdapter.getDataByPosition(position);
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
				new Handler().postDelayed(new Runnable() {
					public void run() {
						mPage = 1;
						if(isCate)
							obtainCateData();
						else 
							obtainData();
					}
				}, 50);            //refresh data here
			}

			@Override
			public void onLoadMore() {
				new Handler().postDelayed(new Runnable() {
					public void run() {
						obtainCateData();
					}
				}, 50);
			}
		});
		mBinding.recyclerview.setRefreshing(true);

		if (isCate) {
			//初始化popview
			mLabelAdapter = new BaseRecyclerAdapter<AvatarBean>(mContext, R.layout.item_pop_label) {
				@Override
				public void bindData(RecyclerViewHolder holder, int position, AvatarBean item) {
					switch (currentShow) {
						case 0:
							onSelect = item.getName().equals(txt_type) || txt_type.equals("类型") && item.getName().equals("全部") ? true : false;
							break;
						case 1:
							onSelect = item.getName().equals(txt_publish) || txt_publish.equals("厂商") && item.getName().equals("全部") ? true : false;
							break;
						case 2:
							onSelect = item.getName().equals(txt_label) || txt_label.equals("标签") && item.getName().equals("全部") ? true : false;
							break;
						case 3:
							onSelect = item.getName().equals(txt_time) || txt_time.equals("时间") && item.getName().equals("全部") ? true : false;
							break;
					}
					TextView textView = holder.getView(R.id.tv_name);
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
					switch (currentShow) {//platid=0,langid=0,typeid=0;//平台语言分类选项
						case 0:
							txt_type = mLabelAdapter.getDataByPosition(position).getName();//▲  ▼
							mBinding.tvType.setText(txt_type + " ▼");
							platid = mLabelAdapter.getDataByPosition(position).getId();
							mPage = 1;
							obtainCateData();
							break;
						case 1:
							txt_publish = mLabelAdapter.getDataByPosition(position).getName();//▲  ▼
							mBinding.tvPublish.setText(txt_publish + " ▼");
							langid = mLabelAdapter.getDataByPosition(position).getId();
							mPage = 1;
							obtainCateData();
							break;
						case 2:
							txt_label = mLabelAdapter.getDataByPosition(position).getName();//▲  ▼
							mBinding.tvLabel.setText(txt_label + " ▼");
							typeid = mLabelAdapter.getDataByPosition(position).getId();
							mPage = 1;
							obtainCateData();
							break;
						case 3:
							txt_time = mLabelAdapter.getDataByPosition(position).getName();//▲  ▼
							mBinding.tvTime.setText(txt_time + " ▼");
							saletime = txt_time;
							mPage = 1;
							obtainCateData();
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
			mPopupWindow.setOutsideTouchable(true);
			mPopupWindow.setFocusable(false); // pop 显示时，让外部 view 响应点击事件
		}
	}

	private void reset() {
		switch (currentShow) {//还原状态
			case 0:
				if (txt_type.equals("全部")) {
					mBinding.tvType.setText("类型 ▼");
				} else {
					mBinding.tvType.setText(txt_type + " ▼");//▲  ▼
				}
				mBinding.tvType.setTextColor(getResources().getColor(R.color.black_55));
				mBinding.tvType.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_gray));
				break;
			case 1:
				if (txt_publish.equals("全部")) {
					mBinding.tvPublish.setText("厂商 ▼");
				} else {
					mBinding.tvPublish.setText(txt_publish + " ▼");//▲  ▼
				}
				mBinding.tvPublish.setTextColor(getResources().getColor(R.color.black_55));
				mBinding.tvPublish.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_gray));
				break;
			case 2:
				if (txt_label.equals("全部")) {
					mBinding.tvLabel.setText("标签 ▼");
				} else {
					mBinding.tvLabel.setText(txt_label + " ▼");//▲  ▼
				}
				mBinding.tvLabel.setTextColor(getResources().getColor(R.color.black_55));
				mBinding.tvLabel.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_gray));
				break;
			case 3:
				if (txt_time.equals("全部")) {
					mBinding.tvTime.setText("时间 ▼");
				} else {
					mBinding.tvTime.setText(txt_time + " ▼");//▲  ▼
				}
				mBinding.tvTime.setTextColor(getResources().getColor(R.color.black_55));
				mBinding.tvTime.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_gray));
				break;
		}
		currentShow = -1;
	}

	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	private void showPopView(int tag) {//0平台 1语言 2类型 3发售时间
		reset();
		currentShow = tag;

		//展示popview
		switch (currentShow) {//设置新状态
			case 0:
				if (txt_type.equals("全部")) {
					mBinding.tvType.setText("类型 ▲");
				} else {
					mBinding.tvType.setText(txt_type + " ▲");//▲  ▼
				}
				mBinding.tvType.setTextColor(getResources().getColor(R.color.white));
				mBinding.tvType.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_fillred));
				mLabelAdapter.clearAndAddList(mGameOLitemRespBean.getData().getTypelist());
				break;
			case 1:
				if (txt_publish.equals("全部")) {
					mBinding.tvPublish.setText("厂商 ▲");
				} else {
					mBinding.tvPublish.setText(txt_publish + " ▲");//▲  ▼
				}
				mBinding.tvPublish.setTextColor(getResources().getColor(R.color.white));
				mBinding.tvPublish.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_fillred));
				mLabelAdapter.clearAndAddList(mGameOLitemRespBean.getData().getFirmlist());
				break;
			case 2:
				if (txt_label.equals("全部")) {
					mBinding.tvLabel.setText("标签 ▲");
				} else {
					mBinding.tvLabel.setText(txt_label + " ▲");//▲  ▼
				}
				mBinding.tvLabel.setTextColor(getResources().getColor(R.color.white));
				mBinding.tvLabel.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_fillred));
				mLabelAdapter.clearAndAddList(mGameOLitemRespBean.getData().getLabellist());
				break;
			case 3:
				if (txt_time.equals("全部")) {
					mBinding.tvTime.setText("时间 ▲");
				} else {
					mBinding.tvTime.setText(txt_time + " ▲");//▲  ▼
				}
				mBinding.tvTime.setTextColor(getResources().getColor(R.color.white));
				mBinding.tvTime.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_fillred));
				mLabelAdapter.clearAndAddList(listYear);
				break;
		}

		if (Build.VERSION.SDK_INT >= 24) {//适配安卓7.0popview 显示位置不对
			Rect visibleFrame = new Rect();
			mBinding.recyclerview.getGlobalVisibleRect(visibleFrame);
			mPopupWindow.setHeight(visibleFrame.height());
			mPopupWindow.showAtLocation(mBinding.llTopbar, Gravity.TOP, visibleFrame.left, visibleFrame.top);
		} else {
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
			case R.id.tv_type:
				mPopupWindow.dismiss();
				if (currentShow != 0)
					showPopView(0);
				else
					reset();
				break;
			case R.id.tv_publish:
				mPopupWindow.dismiss();
				if (currentShow != 1)
					showPopView(1);
				else
					reset();
				break;
			case R.id.tv_label:
				mPopupWindow.dismiss();
				if (currentShow != 2)
					showPopView(2);
				else
					reset();
				break;
			case R.id.tv_time:
				mPopupWindow.dismiss();
				if (currentShow != 3)
					showPopView(3);
				else
					reset();
				break;
			case R.id.rlHead://头部
				mPopupWindow.dismiss();
				reset();
				break;
			case R.id.tv_reset:
				mPopupWindow.dismiss();
				if (!txt_type.equals("类型") || !txt_publish.equals("厂商") || !txt_label.equals("标签") || !txt_time.equals("时间")) {
					txt_type = "类型";
					txt_publish = "厂商";
					txt_label = "标签";
					txt_time = "时间";

					platid = "0";
					langid = "0";
					typeid = "0";
					saletime = "全部";

					mBinding.tvType.setText("类型 ▼");
					mBinding.tvPublish.setText("厂商 ▼");
					mBinding.tvLabel.setText("标签 ▼");
					mBinding.tvTime.setText("时间 ▼");

//					mBinding.tvType.setTextColor(getResources().getColor(R.color.black_55));
//					mBinding.tvType.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_gray));
//					mBinding.tvLanguage.setTextColor(getResources().getColor(R.color.black_55));
//					mBinding.tvLanguage.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_gray));
//					mBinding.tvType.setTextColor(getResources().getColor(R.color.black_55));
//					mBinding.tvType.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_gray));
//					mBinding.tvTime.setTextColor(getResources().getColor(R.color.black_55));
//					mBinding.tvTime.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_gray));

					mPage = 1;
					obtainCateData();
				}
				break;
		}
	}

	public void obtainData() {//获取热门列表
		//获取数据
		long time = System.currentTimeMillis();
		String sign = StringUtil.MD5("" + time);
		JSONObject obj = new JSONObject();
		try {
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
		Call<GameOLHotRespBean> call = service.olHot(body);
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
		if (bean != null && bean.getData() != null) {
			listData = bean.getData().getList();
		}
		if (mPage > 1) {
			mAdapter.appendList(listData);
		} else {
			mBinding.recyclerview.scrollToPosition(0);
			mAdapter.clearAndAddList(listData);
		}

		mBinding.recyclerview.setNoMore(true);
	}

	public void obtainCateData() {//获取热门列表
		//获取数据
		long time=System.currentTimeMillis();
		String validate= "10"+mPage+platid+typeid+langid+saletime+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("pagesize", 10);
			obj.put("page", mPage);
			obj.put("typeid", platid);
			obj.put("labelid", typeid);
			obj.put("firmid", langid);
			obj.put("year", saletime);
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
		Call<GameOLHotRespBean> call = service.olscreen(body);
		call.enqueue(new Callback<GameOLHotRespBean>() {
			@Override
			public void onResponse(Call<GameOLHotRespBean> call, Response<GameOLHotRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				initCateData(response.body());
			}

			@Override
			public void onFailure(Call<GameOLHotRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
				mBinding.recyclerview.loadMoreError();
			}
		});
	}

	private void initCateData(GameOLHotRespBean bean) {
		mBinding.recyclerview.refreshComplete();
		if (bean != null && bean.getData() != null) {
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

	public void obtainItems() {//异步获取标签
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
		mGamePresenter.getOLitem(obj.toString());//异步请求
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mGamePresenter != null) {
			mGamePresenter.setHandler(mHandler);
		}
	}
}