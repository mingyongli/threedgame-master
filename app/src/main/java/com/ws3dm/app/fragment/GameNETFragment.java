package com.ws3dm.app.fragment;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.drawable.ClipDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jcodecraeer.demo.MyAdapter;
import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.umeng.analytics.MobclickAgent;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.DianJingActivity;
import com.ws3dm.app.activity.GameGiftActivity;
import com.ws3dm.app.activity.GameHomeActivity;
import com.ws3dm.app.activity.GameNetRandActivity;
import com.ws3dm.app.activity.GameSpecialActivity;
import com.ws3dm.app.activity.GameTestActivity;
import com.ws3dm.app.activity.GiftDetailActivity;
import com.ws3dm.app.activity.SingleWebActivity;
import com.ws3dm.app.adapter.CommonFragmentPagerAdapter;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.GameBean;
import com.ws3dm.app.bean.GameGiftBean;
import com.ws3dm.app.bean.GameHotPhb;
import com.ws3dm.app.bean.GameTestBean;
import com.ws3dm.app.databinding.FgBaseRecyclerviewBinding;
import com.ws3dm.app.mvp.model.RespBean.GameOLhomeRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.GameService;
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
 * Describution :游戏库（底部标签）新版
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2017/8/16 13:20
 **/

public class GameNETFragment extends BaseFragment implements View.OnClickListener {

	private static final String TAG = "GamePCFragment";
	private FgBaseRecyclerviewBinding mBinding;
	private CommonFragmentPagerAdapter fragmentPagerAdapter;
	private ArrayList<Fragment> fragments = new ArrayList<>();
	private View header;//存储顶部viewpager
	private CommonRecyclerAdapter<GameTestBean> mAdapter;
	private boolean hasHead = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_base_recyclerview, container, false);
		header = LayoutInflater.from(mContext).inflate(R.layout.header_fg_game_net, container, false);

		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.recyclerview.setLayoutManager(layoutManager);

		mBinding.recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);       //原来就注释掉的
		mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
		mBinding.recyclerview.setLoadingMoreEnabled(false);
		mBinding.recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
			@Override
			public void onRefresh() {
				new Handler().postDelayed(new Runnable() {
					public void run() {
						obtainData();
					}
				}, 50);            //refresh data here
			}

			@Override
			public void onLoadMore() {
			}
		});
		mBinding.recyclerview.setAdapter(new MyAdapter(new ArrayList<String>()));//没啥用，只是让listview可以显示

		return mBinding.getRoot();
	}

	public void obtainData() {
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
//		mGamePresenter.getDJhome(obj.toString());//异步请求
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		GameService.Api service = retrofit.create(GameService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<GameOLhomeRespBean> call = service.olHome(body);
		call.enqueue(new Callback<GameOLhomeRespBean>() {
			@Override
			public void onResponse(Call<GameOLhomeRespBean> call, Response<GameOLhomeRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				initView(response.body());
			}

			@Override
			public void onFailure(Call<GameOLhomeRespBean> call, Throwable throwable) {
				mBinding.recyclerview.loadMoreError();
			}
		});
	}

	public void initView(GameOLhomeRespBean mData) {
		if (!hasHead) {
			mBinding.recyclerview.addHeaderView(header);
			hasHead = true;
		}
		initHotnet(mData.getData().getSlides());//热门网游

		header.findViewById(R.id.img0).setOnClickListener(this);
		header.findViewById(R.id.img1).setOnClickListener(this);
		header.findViewById(R.id.img2).setOnClickListener(this);
		header.findViewById(R.id.img3).setOnClickListener(this);
		header.findViewById(R.id.img4).setOnClickListener(this);
		header.findViewById(R.id.ll_tabs).setVisibility(View.VISIBLE);

		initTest(mData.getData().getNewtest());//开测表
		initEsport(mData.getData().getEsport_zt());//电竞专题
		initGift(mData.getData().getLibbao());//礼包
		initRank(mData.getData().getHot_phb());//热门排行

		mBinding.recyclerview.refreshComplete();
	}

	//    初始化热门网游
	private void initHotnet(List<GameTestBean> mList) {
		if (mList == null || mList.size() == 0) {
			return;
		}
		ViewPager vp_hotnet = header.findViewById(R.id.vp_hotnet);
		vp_hotnet.setPageMargin((int) getResources().getDimensionPixelOffset(R.dimen.element_margin_9));
		vp_hotnet.setAdapter(new hotnetAdapter(mContext, mList));
		vp_hotnet.setOffscreenPageLimit(mList.size());
		//viewpager设置页面改变的监听
		vp_hotnet.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

		header.findViewById(R.id.ll_hotnet).setVisibility(View.VISIBLE);
		// 发消息让轮播图动起来
//		if (mSlideMessager == null) {
//			// 发消息让轮播图动起来
//			mSlideMessager = new Handler() {
//				public void handleMessage(Message msg) {
//					mBinding.vpAdvice.gotoForward();//向后一页// 在发一个handler延时
//					mSlideMessager.sendEmptyMessageDelayed(0, 5000);
//				}
//			};
//			mSlideMessager.sendEmptyMessageDelayed(0, 5000);
//		}


		header.findViewById(R.id.tv_more_hot).setOnClickListener(this);//热门网游的更多
	}


	private class hotnetAdapter extends PagerAdapter {
		List<GameTestBean> list;
		Context context;

		public hotnetAdapter(Context context, List<GameTestBean> list) {
			this.list = list;
			this.context = context;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			final GameBean mGame = new GameBean();
			GameTestBean originalBean = list.get(position);
			mGame.setAid(originalBean.getAid());
			mGame.setArcurl(originalBean.getArcurl());
			mGame.setShowtype(originalBean.getShowtype());

			View view = View.inflate(mContext, R.layout.vp_program, null);

			ImageView img_top = view.findViewById(R.id.img_top);
			GlideUtil.loadImage(mContext, originalBean.getLitpic(), img_top);
			TextView title_column = view.findViewById(R.id.title_column);
			title_column.setText(originalBean.getTitle());

			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, GameHomeActivity.class);
					Bundle bundle = new Bundle();
//						bundle.putSerializable("game",mGame);
//					bundle.putSerializable("game", finalGame);//数据太大，超出系统限制
					bundle.putString("str_game", JSON.toJSONString(mGame));
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});
			container.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	//    初始化开测表
	private void initTest(List<GameTestBean> list) {
		if(list.size()==0)
			return;
		RecyclerView rv_test = header.findViewById(R.id.rv_test);
		rv_test.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));

		mAdapter = new CommonRecyclerAdapter<GameTestBean>(mContext, R.layout.item_net_test) {
			@Override
			public void bindData(RecyclerViewHolder holder, int position, GameTestBean bean) {
//				holder.setText(R.id.tv_info, bean.getDesc());
				holder.setText(R.id.tv_label, TimeUtil.getTimeENMD(bean.getPubdate_at()));
				holder.setImageByUrl(R.id.img_top, bean.getLitpic());
				holder.setText(R.id.tv_title, bean.getTitle());
				holder.setText(R.id.tv_statue, bean.getState());
			}
		};
		rv_test.setAdapter(mAdapter);
		mAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int position) {
				final GameBean mGame = new GameBean();
				GameTestBean originalBean = mAdapter.getDataByPosition(position);
				if(originalBean.getAid()==0){
					ToastUtil.showToast(mContext,"暂未建立游戏专区");
				}else{
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
		mAdapter.clearAndAddList(list);

		header.findViewById(R.id.tv_more_test).setOnClickListener(this);//team的更多
	}

	//    初始化电竞专题
	private void initEsport(final List<GameTestBean> list) {
		if(list.size()==0)
			return;
		header.findViewById(R.id.line_esport).setVisibility(View.VISIBLE);
		header.findViewById(R.id.ll_esport).setVisibility(View.VISIBLE);
		GlideUtil.loadImage(mContext, list.get(0).getLitpic(), (ImageView) header.findViewById(R.id.dian_img1));
		((TextView) header.findViewById(R.id.dian_name1)).setText(list.get(0).getTitle());
		header.findViewById(R.id.dian1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent aggreement = new Intent(mContext, SingleWebActivity.class);
				aggreement.putExtra("title", list.get(0).getTitle());
				aggreement.putExtra("url", list.get(0).getWebviewurl());
				startActivity(aggreement);
			}
		});

		GlideUtil.loadImage(mContext, list.get(1).getLitpic(), (ImageView) header.findViewById(R.id.dian_img2));
		((TextView) header.findViewById(R.id.dian_name2)).setText(list.get(1).getTitle());
		header.findViewById(R.id.dian2).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent aggreement = new Intent(mContext, SingleWebActivity.class);
				aggreement.putExtra("title", list.get(1).getTitle());
				aggreement.putExtra("url", list.get(1).getWebviewurl());
				startActivity(aggreement);
			}
		});
		
		GlideUtil.loadImage(mContext, list.get(2).getLitpic(), (ImageView) header.findViewById(R.id.dian_img3));
		((TextView) header.findViewById(R.id.dian_name3)).setText(list.get(2).getTitle());
		header.findViewById(R.id.dian3).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent aggreement = new Intent(mContext, SingleWebActivity.class);
				aggreement.putExtra("title", list.get(2).getTitle());
				aggreement.putExtra("url", list.get(2).getWebviewurl());
				startActivity(aggreement);
			}
		});

		GlideUtil.loadImage(mContext, list.get(3).getLitpic(), (ImageView) header.findViewById(R.id.dian_img4));
		((TextView) header.findViewById(R.id.dian_name4)).setText(list.get(3).getTitle());
		header.findViewById(R.id.dian4).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent aggreement = new Intent(mContext, SingleWebActivity.class);
				aggreement.putExtra("title", list.get(3).getTitle());
				aggreement.putExtra("url", list.get(3).getWebviewurl());
				startActivity(aggreement);
			}
		});


		header.findViewById(R.id.tv_more_esport).setOnClickListener(this);//电竞的更多
	}

	//    初始化礼包
	private void initGift(final List<GameGiftBean> list) {
		if (list == null || list.size() == 0)
			return;
		header.findViewById(R.id.ll_gift).setVisibility(View.VISIBLE);

		GlideUtil.loadImage(mContext, list.get(0).getLitpic(), (ImageView) header.findViewById(R.id.gift_img1));
		((TextView) header.findViewById(R.id.gift_name1)).setText(list.get(0).getTitle());
		((TextView) header.findViewById(R.id.tv_time1)).setText(list.get(0).getRange_start() + " 至 " + list.get(0).getRange_end());
		((TextView) header.findViewById(R.id.gift_name1)).setText(list.get(0).getTitle());

		((TextView) header.findViewById(R.id.tv_remain1)).setText("剩余：" + list.get(0).getSurplusper() + "%");
		ImageView imgScore1 = header.findViewById(R.id.img_score1);
		// 获得ClipDrawable对象  
		ClipDrawable clipDrawable1 = (ClipDrawable) imgScore1.getBackground();
		// 设置裁剪级别，Clip类型的图片默认裁剪级别为0，此时是全部裁剪，图片看不见；  
		// 当级别为10000时，不裁剪图片，图片全部可见  
		// 当全部显示后，设置不可见  
		clipDrawable1.setLevel(list.get(0).getSurplusper() * 100);

		header.findViewById(R.id.rl_gift1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, GiftDetailActivity.class);
				intent.putExtra("pageType",0);//pageType //0 网游礼包  1 手游礼包
				Bundle bundle = new Bundle();
				bundle.putSerializable("mGameGiftBean",list.get(0));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		GlideUtil.loadImage(mContext, list.get(1).getLitpic(), (ImageView) header.findViewById(R.id.gift_img2));
		((TextView) header.findViewById(R.id.gift_name2)).setText(list.get(1).getTitle());
		((TextView) header.findViewById(R.id.tv_time2)).setText(list.get(1).getRange_start() + " 至 " + list.get(1).getRange_end());
		((TextView) header.findViewById(R.id.gift_name2)).setText(list.get(1).getTitle());
		((TextView) header.findViewById(R.id.tv_remain2)).setText("剩余：" + list.get(1).getSurplusper() + "%");
		ImageView imgScore2 = header.findViewById(R.id.img_score2);
		// 获得ClipDrawable对象  
		ClipDrawable clipDrawable2 = (ClipDrawable) imgScore2.getBackground();
		// 设置裁剪级别，Clip类型的图片默认裁剪级别为0，此时是全部裁剪，图片看不见；  
		// 当级别为10000时，不裁剪图片，图片全部可见  
		// 当全部显示后，设置不可见  
		clipDrawable2.setLevel(list.get(1).getSurplusper() * 100);
		header.findViewById(R.id.rl_gift2).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, GiftDetailActivity.class);
				intent.putExtra("pageType",0);//pageType //0 网游礼包  1 手游礼包
				Bundle bundle = new Bundle();
				bundle.putSerializable("mGameGiftBean",list.get(1));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		GlideUtil.loadImage(mContext, list.get(2).getLitpic(), (ImageView) header.findViewById(R.id.gift_img3));
		((TextView) header.findViewById(R.id.gift_name3)).setText(list.get(2).getTitle());
		((TextView) header.findViewById(R.id.tv_time3)).setText(list.get(2).getRange_start() + " 至 " + list.get(2).getRange_end());
		((TextView) header.findViewById(R.id.gift_name3)).setText(list.get(2).getTitle());
		((TextView) header.findViewById(R.id.tv_remain3)).setText("剩余：" + list.get(2).getSurplusper() + "%");
		ImageView imgScore3 = header.findViewById(R.id.img_score3);
		// 获得ClipDrawable对象  
		ClipDrawable clipDrawable3 = (ClipDrawable) imgScore3.getBackground();
		// 设置裁剪级别，Clip类型的图片默认裁剪级别为0，此时是全部裁剪，图片看不见；  
		// 当级别为10000时，不裁剪图片，图片全部可见  
		// 当全部显示后，设置不可见  
		clipDrawable3.setLevel(list.get(2).getSurplusper() * 100);
		header.findViewById(R.id.rl_gift3).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, GiftDetailActivity.class);
				intent.putExtra("pageType",0);//pageType //0 网游礼包  1 手游礼包
				Bundle bundle = new Bundle();
				bundle.putSerializable("mGameGiftBean",list.get(2));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		header.findViewById(R.id.tv_more_gift).setOnClickListener(this);//礼包的更多
	}

	//    初始化排行
	private void initRank(final List<GameHotPhb> list) {
		if (list == null || list.size() == 0)
			return;

		header.findViewById(R.id.ll_rank).setVisibility(View.VISIBLE);
		GlideUtil.loadImage(mContext, list.get(0).getLitpic(), (ImageView) header.findViewById(R.id.rank_cover1));
		((TextView) header.findViewById(R.id.rank_name1)).setText(list.get(0).getTitle());
		((TextView) header.findViewById(R.id.firm1)).setText("运营商：" + list.get(0).getFirm());
		((TextView) header.findViewById(R.id.betime1)).setText("公测：" + list.get(0).getBetatime());
		((TextView) header.findViewById(R.id.rank_score1)).setText(list.get(0).getScore() + "");
		header.findViewById(R.id.rl_rank1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final GameBean mGame = new GameBean();
				GameHotPhb originalBean =list.get(0);
				mGame.setAid(originalBean.getAid());
				mGame.setArcurl(originalBean.getArcurl());
				mGame.setShowtype(originalBean.getShowtype());

				Intent intent = new Intent(mContext, GameHomeActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("str_game", JSON.toJSONString(mGame));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		GlideUtil.loadImage(mContext, list.get(1).getLitpic(), (ImageView) header.findViewById(R.id.rank_cover2));
		((TextView) header.findViewById(R.id.rank_name2)).setText(list.get(1).getTitle());
		((TextView) header.findViewById(R.id.firm2)).setText("运营商：" + list.get(1).getFirm());
		((TextView) header.findViewById(R.id.betime2)).setText("公测：" + list.get(1).getBetatime());
		((TextView) header.findViewById(R.id.rank_score2)).setText(list.get(1).getScore() + "");
		header.findViewById(R.id.rl_rank2).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final GameBean mGame = new GameBean();
				GameHotPhb originalBean =list.get(1);
				mGame.setAid(originalBean.getAid());
				mGame.setArcurl(originalBean.getArcurl());
				mGame.setShowtype(originalBean.getShowtype());

				Intent intent = new Intent(mContext, GameHomeActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("str_game", JSON.toJSONString(mGame));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		GlideUtil.loadImage(mContext, list.get(2).getLitpic(), (ImageView) header.findViewById(R.id.rank_cover3));
		((TextView) header.findViewById(R.id.rank_name3)).setText(list.get(2).getTitle());
		((TextView) header.findViewById(R.id.firm3)).setText("运营商：" + list.get(2).getFirm());
		((TextView) header.findViewById(R.id.betime3)).setText("公测：" + list.get(2).getBetatime());
		((TextView) header.findViewById(R.id.rank_score3)).setText(list.get(2).getScore() + "");
		header.findViewById(R.id.rl_rank3).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final GameBean mGame = new GameBean();
				GameHotPhb originalBean =list.get(2);
				mGame.setAid(originalBean.getAid());
				mGame.setArcurl(originalBean.getArcurl());
				mGame.setShowtype(originalBean.getShowtype());

				Intent intent = new Intent(mContext, GameHomeActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("str_game", JSON.toJSONString(mGame));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		GlideUtil.loadImage(mContext, list.get(3).getLitpic(), (ImageView) header.findViewById(R.id.rank_cover4));
		((TextView) header.findViewById(R.id.rank_name4)).setText(list.get(3).getTitle());
		((TextView) header.findViewById(R.id.firm4)).setText("运营商：" + list.get(3).getFirm());
		((TextView) header.findViewById(R.id.betime4)).setText("公测：" + list.get(3).getBetatime());
		((TextView) header.findViewById(R.id.rank_score4)).setText(list.get(3).getScore() + "");
		header.findViewById(R.id.rl_rank4).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final GameBean mGame = new GameBean();
				GameHotPhb originalBean =list.get(3);
				mGame.setAid(originalBean.getAid());
				mGame.setArcurl(originalBean.getArcurl());
				mGame.setShowtype(originalBean.getShowtype());

				Intent intent = new Intent(mContext, GameHomeActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("str_game", JSON.toJSONString(mGame));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		header.findViewById(R.id.tv_more_rank).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		Intent intent;
		MobclickAgent.onEvent(mContext,"05");
		switch (view.getId()) {
			case R.id.tv_more_hot://热门网游
				intent = new Intent(mContext, GameSpecialActivity.class);
				startActivity(intent);
				break;
			case R.id.img0://游戏专题
				intent = new Intent(mContext, GameSpecialActivity.class);
				intent.putExtra("isCate", true);
				startActivity(intent);
				break;
			case R.id.tv_more_test://开测表
			case R.id.img1:
				intent = new Intent(mContext, GameTestActivity.class);
				startActivity(intent);
				break;
			case R.id.tv_more_esport://电竞
			case R.id.img2:
				intent = new Intent(mContext, DianJingActivity.class);
				startActivity(intent);
				break;
			case R.id.tv_more_gift://礼包
			case R.id.img3:
				intent = new Intent(mContext, GameGiftActivity.class);
				startActivity(intent);
				break;
			case R.id.tv_more_rank://排行
			case R.id.img4:
				intent = new Intent(mContext, GameNetRandActivity.class);
				startActivity(intent);
				break;
			default:
				Log.e(TAG, "TODO: ClickListener" + view.getId());
				break;
		}
	}

	protected void onFragmentFirstVisible() {
		mBinding.recyclerview.setRefreshing(true);//refresh的监听处理中已经有加载数据的操作
	}
}
