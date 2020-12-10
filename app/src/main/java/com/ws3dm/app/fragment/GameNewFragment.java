package com.ws3dm.app.fragment;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ws3dm.app.Constant;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.GameCategoryActivity;
import com.ws3dm.app.activity.GameChineseActivity;
import com.ws3dm.app.activity.GameHomeActivity;
import com.ws3dm.app.activity.GameLabelActivity;
import com.ws3dm.app.activity.GameRandListActivity;
import com.ws3dm.app.activity.GameSellActivity;
import com.ws3dm.app.adapter.CommonFragmentPagerAdapter;
import com.ws3dm.app.bean.GameBean;
import com.ws3dm.app.databinding.FgGameNewBinding;
import com.ws3dm.app.mvp.model.RespBean.GameDJhomeRespBean;
import com.ws3dm.app.mvp.presenter.GamePresenter;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.util.glide.GlideUtil;
import com.ws3dm.app.view.CoverFlowView;
import com.ws3dm.app.view.ICoverFlowAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Describution :游戏库（底部标签）新版
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2017/8/16 13:20
 **/

public class GameNewFragment extends BaseFragment {

	private static final String TAG = "GameNewFragment";
	private FgGameNewBinding mBinding;
	private CommonFragmentPagerAdapter fragmentPagerAdapter;
	private ArrayList<Fragment> fragments = new ArrayList<>();
	private GamePresenter mGamePresenter;
	private Handler mHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_game_new, container, false);
		mBinding.setHandler(this);//添加监听事件

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
					case Constant.Notify.RESULT_GAME_DJHOME://处理返回结果
						initView((GameDJhomeRespBean) msg.obj);
						break;
				}
			}
		};

		mGamePresenter = GamePresenter.getInstance();
		mGamePresenter.setHandler(mHandler);

		obtainData();
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
		mGamePresenter.getDJhome(obj.toString());//异步请求
	}

	public void initView(GameDJhomeRespBean mData) {
//		mBinding.imgSearch.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
////				startActivity(new Intent(getActivity(), NewSearchActivity.class).putExtra("search", "GameFragment"));
//				startActivity(new Intent(getActivity(), SearchActivity.class).putExtra("search", "NewsFragment"));
////				Intent aggreement=new Intent(mContext,SingleWebActivity.class);
////				aggreement.putExtra("title","搜索");
////				aggreement.putExtra("url","https://wap.sogou.com/");
////				startActivity(aggreement);
//			}
//		});

		initAdvise(mData.getData().getSlides());
		initSell(mData.getData().getSalegame());
		initClass(mData.getData().getClassicgame());
		initChina(mData.getData().getChinesegame());
	}

	//    初始化编辑推荐
	private void initAdvise(List<GameBean> list) {
		mBinding.lineAdvice.setVisibility(View.VISIBLE);
		mBinding.llAdvice.setVisibility(View.VISIBLE);
		if(list==null)
			return;
		
		final List<GameBean> mList = new ArrayList<>();
		mList.addAll(list);
		mBinding.vpAdvice.setVisibility(View.VISIBLE);
		mBinding.vpAdvice.setAdapter(new AdviseAdapter(getActivity(), mList));

		// 给coverFlowView的TOPView 添加点击事件监听
		mBinding.vpAdvice.setOnTopViewClickListener(new CoverFlowView.OnTopViewClickListener() {
			@Override
			public void onClick(int position, View itemView) {
				Intent intent = new Intent(mContext, GameHomeActivity.class);
				Bundle bundle = new Bundle();
//				bundle.putSerializable("game",mList.get(position));
//					bundle.putSerializable("game", finalGame);//数据太大，超出系统限制
				GameBean tempGame=mList.get(position);
				bundle.putString("str_game", JSON.toJSONString(tempGame));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

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
	}

	//    初始化经典大作
	private void initClass(List<GameBean> list) {
		mBinding.lineClass.setVisibility(View.VISIBLE);
		mBinding.llClass.setVisibility(View.VISIBLE);
		list.add(new GameBean());//加一个空的放箭头
		final List<GameBean> mList = new ArrayList<>();
		mList.addAll(list);
		mBinding.vpClass.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.element_margin_9));
		mBinding.vpClass.setAdapter(new classAdapter(mContext, mList));
		mBinding.vpClass.setOffscreenPageLimit(list.size());
		//viewpager设置页面改变的监听
		mBinding.vpClass.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
	}

	//    初始化即将发售
	private void initSell(List<GameDJhomeRespBean.DataBean.SalegameBean> salegame) {
		if(fragments.size()>0)
			fragments.clear();
		mBinding.lineSell.setVisibility(View.VISIBLE);
		mBinding.llSell.setVisibility(View.VISIBLE);
		for (int i = 0; i < salegame.size(); i++) {
			Fragment fragment = new MonthFragment(5, mBinding.vpSell);//新闻列表
			Bundle bundle = new Bundle();
			bundle.putInt("position", i);
			bundle.putString("pre_mon", salegame.get(i).getMonth()+"月");
			bundle.putString("next_mon", i < salegame.size()-1?salegame.get(i+1).getMonth()+"月":"");
			bundle.putSerializable("monList",(Serializable)salegame.get(i).getList());
			fragment.setArguments(bundle);
			fragments.add(fragment);
		}

		fragmentPagerAdapter = new CommonFragmentPagerAdapter(getChildFragmentManager(), fragments);
		mBinding.vpSell.setAdapter(fragmentPagerAdapter);
		mBinding.vpSell.setOffscreenPageLimit(fragments.size());
		mBinding.vpSell.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageSelected(int position) {
				mBinding.vpSell.resetHeight(position);
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				mBinding.vpSell.resetHeight(0);
			}
		}, 1);
	}

	//    初始化汉化
	private void initChina(final List<GameBean> list) {
		mBinding.lineChina.setVisibility(View.VISIBLE);
		mBinding.llChina.setVisibility(View.VISIBLE);
		GlideUtil.loadImage(mContext, list.get(0).getLitpic(), mBinding.imgCover1);
		mBinding.name1.setText(list.get(0).getTitle());
		mBinding.type1.setText("类型："+list.get(0).getType());
		String[] sy1=list.get(0).getSystem().split("/");
		switch (sy1.length) {
			case 7:
			case 6:
			case 5:
				mBinding.txtLabel15.setVisibility(View.VISIBLE);
			case 4:
				mBinding.txtLabel14.setVisibility(View.VISIBLE);
				mBinding.txtLabel14.setText(sy1[3]);
			case 3:
				mBinding.txtLabel13.setVisibility(View.VISIBLE);
				mBinding.txtLabel13.setText(sy1[2]);
			case 2:
				mBinding.txtLabel12.setVisibility(View.VISIBLE);
				mBinding.txtLabel12.setText(sy1[1]);
			case 1:
				mBinding.txtLabel11.setVisibility(View.VISIBLE);
				mBinding.txtLabel11.setText(sy1[0]);
				break;
		}
		mBinding.tag1.setText("标签："+list.get(0).getLabelString());
		mBinding.tvScore1.setText(list.get(0).getScore()+"");
		
		mBinding.rlGame1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, GameHomeActivity.class);
				Bundle bundle = new Bundle();
//				bundle.putSerializable("game", list.get(0));
//					bundle.putSerializable("game", finalGame);//数据太大，超出系统限制
				GameBean tempGame=list.get(0);
				bundle.putString("str_game", JSON.toJSONString(tempGame));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		GlideUtil.loadImage(mContext, list.get(1).getLitpic(), mBinding.imgCover2);
		mBinding.name2.setText(list.get(1).getTitle());
		mBinding.type2.setText("类型："+list.get(1).getType());
		String[] sy2=list.get(1).getSystem().split("/");
		switch (sy2.length) {
			case 7:
			case 6:
			case 5:
				mBinding.txtLabel25.setVisibility(View.VISIBLE);
			case 4:
				mBinding.txtLabel24.setVisibility(View.VISIBLE);
				mBinding.txtLabel24.setText(sy2[3]);
			case 3:
				mBinding.txtLabel23.setVisibility(View.VISIBLE);
				mBinding.txtLabel23.setText(sy2[2]);
			case 2:
				mBinding.txtLabel22.setVisibility(View.VISIBLE);
				mBinding.txtLabel22.setText(sy2[1]);
			case 1:
				mBinding.txtLabel21.setVisibility(View.VISIBLE);
				mBinding.txtLabel21.setText(sy2[0]);
				break;
		}
		mBinding.tag2.setText("标签："+list.get(1).getLabelString());
		mBinding.tvScore2.setText(list.get(1).getScore()+"");

		mBinding.rlGame2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, GameHomeActivity.class);
				Bundle bundle = new Bundle();
//				bundle.putSerializable("game", list.get(1));
//					bundle.putSerializable("game", finalGame);//数据太大，超出系统限制
				GameBean tempGame=list.get(1);
				bundle.putString("str_game", JSON.toJSONString(tempGame));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		GlideUtil.loadImage(mContext, list.get(2).getLitpic(), mBinding.imgCover3);
		mBinding.name3.setText(list.get(2).getTitle());
		mBinding.type3.setText("类型："+list.get(2).getType());
		String[] sy3=list.get(2).getSystem().split("/");
		switch (sy3.length) {
			case 7:
			case 6:
			case 5:
				mBinding.txtLabel35.setVisibility(View.VISIBLE);
			case 4:
				mBinding.txtLabel34.setVisibility(View.VISIBLE);
				mBinding.txtLabel34.setText(sy3[3]);
			case 3:
				mBinding.txtLabel33.setVisibility(View.VISIBLE);
				mBinding.txtLabel33.setText(sy3[2]);
			case 2:
				mBinding.txtLabel32.setVisibility(View.VISIBLE);
				mBinding.txtLabel32.setText(sy3[1]);
			case 1:
				mBinding.txtLabel31.setVisibility(View.VISIBLE);
				mBinding.txtLabel31.setText(sy3[0]);
				break;
		}
		mBinding.tag3.setText("标签："+list.get(2).getLabelString());
		mBinding.tvScore3.setText(list.get(2).getScore()+"");

		mBinding.rlGame3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, GameHomeActivity.class);
				Bundle bundle = new Bundle();
//				bundle.putSerializable("game", list.get(2));
//					bundle.putSerializable("game", finalGame);//数据太大，超出系统限制
				GameBean tempGame=list.get(2);
				bundle.putString("str_game", JSON.toJSONString(tempGame));
				intent.putExtras(bundle);
				startActivity(intent);
				bundle.clear();
			}
		});
	}

	private class AdviseAdapter implements ICoverFlowAdapter {
		List<GameBean> list;
		Context context;

		public AdviseAdapter(Context context, List<GameBean> list) {
			this.list = list;
			this.context = context;
		}

		@Override
		public int getCount() {
			return list == null ? 0 : list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder = null;
			if (convertView == null) {
				holder = new Holder();
				convertView = View.inflate(context, R.layout.vp_game_advice, null);
				holder.img_game = (ImageView) convertView.findViewById(R.id.img_game);
				holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
				holder.tv_plat = (TextView) convertView.findViewById(R.id.tv_plat);
				holder.tv_score = (TextView) convertView.findViewById(R.id.tv_score);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}

			return convertView;
		}

		@Override
		public void getData(View view, int position) {
			Holder holder = (Holder) view.getTag();
			final GameBean mGameBean = list.get(position);
			GlideUtil.loadImage(mContext, mGameBean.getLitpic(), holder.img_game);

			holder.tv_name.setText(mGameBean.getTitle());
			holder.tv_time.setText("发售："+TimeUtil.getTimeEN(mGameBean.getPubdate_at()));
			holder.tv_plat.setText("平台："+mGameBean.getSystem());
			holder.tv_score.setText(mGameBean.getScore() + "");

			holder.tv_score.setText(mGameBean.getScore() + "");
		}

		public class Holder {
			ImageView img_game;
			TextView tv_name;
			TextView tv_time;
			TextView tv_plat;
			TextView tv_score;
		}
	}

	private class classAdapter extends PagerAdapter {
		List<GameBean> list;
		Context context;

		public classAdapter(Context context, List<GameBean> list) {
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
			if (position < list.size() - 1) {
				final GameBean mGame = list.get(position);
				View view = View.inflate(mContext, R.layout.vp_game_hot, null);
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
				ImageView gameBg = (ImageView) view.findViewById(R.id.img_game);
				GlideUtil.loadImage(mContext, mGame.getLitpic(), gameBg);

				TextView tv = (TextView) view.findViewById(R.id.tv_name);
				tv.setText(mGame.getTitle());
				TextView type = (TextView) view.findViewById(R.id.tv_type);
				type.setText(mGame.getType());
				TextView plat = (TextView) view.findViewById(R.id.tv_plat);
				plat.setText(mGame.getSystem());
				container.addView(view);
				return view;
			} else {
				View more = View.inflate(mContext, R.layout.view_more, null);

				more.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext, GameRandListActivity.class);
						startActivity(intent);
					}
				});
				container.addView(more);
				return more;
			}
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public float getPageWidth(int position) {
			if (position == list.size() - 1)
				return 0.25f;
			else
				return 1f;
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	public void clickHandler(View view) {
		Intent intent;
		switch (view.getId()) {
			case R.id.img0://游戏库
				intent = new Intent(mContext, GameCategoryActivity.class);
				startActivity(intent);
				break;
			case R.id.tv_more_china://汉化更多
			case R.id.img1://汉化
				intent = new Intent(mContext, GameChineseActivity.class);
				startActivity(intent);
				break;
			case R.id.tv_more_sell://即将发售更多
			case R.id.img2://发售
				intent = new Intent(mContext, GameSellActivity.class);
				startActivity(intent);
				break;
			case R.id.tv_more_hot://经典更多
			case R.id.img3://排行
				intent = new Intent(mContext, GameRandListActivity.class);
				startActivity(intent);
				break;
			case R.id.img4://游戏标签
				intent = new Intent(mContext, GameLabelActivity.class);
				startActivity(intent);
				break;
			default:
				Log.e(TAG, "TODO: ClickListener" + view.getId());
				break;
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume(){
		super.onResume();
		if(mGamePresenter!=null){
			mGamePresenter.setHandler(mHandler);
		}
//		mBinding.vpAdvice.gotoForward();//向后一页
//		obtainData();
	}
}
