package com.ws3dm.app.fragment;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.MGDetailActivity;
import com.ws3dm.app.adapter.MGMultiAdapter;
import com.ws3dm.app.adapter.MGMultiItemTypeSupport;
import com.ws3dm.app.bean.MGListBean;
import com.ws3dm.app.bean.SlidesBean;
import com.ws3dm.app.bean.SoftGameBean;
import com.ws3dm.app.databinding.FgMgListBinding;
import com.ws3dm.app.mvp.model.RespBean.MGListRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.MGService;
import com.ws3dm.app.util.StringUtil;
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
 * Describution :手游 --  游戏和软件
 * 
 * Author : DKjuan
 * 
 * Date : 2017/8/18 17:22
 **/
public class FragmentListMG extends BaseFragment {	
	private FgMgListBinding mBinding;
	private MGMultiAdapter mAdapter;
	public List<MGListBean> listData = new ArrayList<MGListBean>();
	private int type;//0,游戏  1，软件
	private View header;//存储顶部viewpager
	
//	顶部轮播图
	private int mSlideCount=0;
	private List<SlidesBean> mSlidesList;
	private ViewPager mSlideViewPager;
	private Handler mSlideMessager = null;
	private List<View> pointList=new ArrayList<View>();//存放View的List
	private int index;//當前廣告下标
	private TextView titleBanner;
	private boolean hasHead;
//	private ViewPagerAdapter mViewPagerAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		AppCompatActivity activity = (AppCompatActivity) getActivity();

		mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_mg_list, container, false);
		header = LayoutInflater.from(mContext).inflate(R.layout.header_viewpager_mg, container,false);
		initView();
		return mBinding.getRoot();
	}
	
	public void initView(){
		type=getArguments().getInt("type",0);
		hasHead=false;

		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.recyclerview.setLayoutManager(layoutManager);

		mBinding.recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
		mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);

//		//添加分割线
//		if (mListV3DividerItemDecoration == null) {
//			mListV3DividerItemDecoration = new DividerItemDecorationAdapter(mContext);
//		} else {
//			mBinding.recyclerview.removeItemDecoration(mListV3DividerItemDecoration);
//		}
//		mBinding.recyclerview.addItemDecoration(mListV3DividerItemDecoration);

		MGMultiItemTypeSupport modelMultiItemTypeSupport = new MGMultiItemTypeSupport();
		mAdapter= new MGMultiAdapter(mContext, modelMultiItemTypeSupport);
		mBinding.recyclerview.setAdapter(mAdapter);
		mBinding.recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
			@Override
			public void onRefresh() {
				new Handler().postDelayed(new Runnable(){
					public void run() {
						obtainData();
					}
				}, 50);            //refresh data here
			}

			@Override
			public void onLoadMore() {
				new Handler().postDelayed(new Runnable(){
					public void run() {
						mBinding.recyclerview.setNoMore(true);
					}
				}, 50);
			}
		});
	}

	protected void onFragmentFirstVisible(){
		mBinding.recyclerview.setRefreshing(true);//refresh的监听处理中已经有加载数据的操作
	}
	
	public void obtainData(){
		//获取数据
		long time=System.currentTimeMillis();
		String validate= ""+time;
		String sign= StringUtil.MD5(validate);
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
		MGService.Api service = retrofit.create(MGService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<MGListRespBean> call = type==0?service.getGameMG(body):service.getSoftMG(body);
		call.enqueue(new Callback<MGListRespBean>() {
			@Override
			public void onResponse(Call<MGListRespBean> call, Response<MGListRespBean> response) {
				MGListRespBean bean= response.body();
				if(bean!=null&&bean.getData()!=null) {
					mSlidesList=bean.getData().getSlides();
					if(mSlidesList!=null&&mSlidesList.size()>0&&!hasHead){
						initHeadView(mSlidesList);
						mBinding.recyclerview.addHeaderView(header);
						hasHead=true;
					}else if(hasHead){
						initHeadView(mSlidesList);
					}
				}
				init(response.body());
			}

			@Override
			public void onFailure(Call<MGListRespBean> call, Throwable throwable) {
				mBinding.recyclerview.loadMoreError();
			}
		});
	}

	//头部的滑动广告栏
	private void initHeadView(final List<SlidesBean> bannerList) {
		pointList=new ArrayList<View>();
		mSlideViewPager = (ViewPager) header.findViewById(R.id.top_slide);
		titleBanner = (TextView) header.findViewById(R.id.tv_info);
		titleBanner.setText(bannerList.get(0).getTitle());
		mSlideCount=bannerList.size();
//		mViewPagerAdapter=new ViewPagerAdapter(bannerList,mContext);
		final LinearLayout ll_points = (LinearLayout) header.findViewById(R.id.point_group);
		if(ll_points.getChildCount()>0)
			ll_points.removeAllViews();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		for(int i=0;i<mSlideCount;i++){
			ImageView imageView = new ImageView(getActivity());
			if(i == 0){
				imageView.setBackgroundResource(R.drawable.red_rectangle);
			}else{
				imageView.setBackgroundResource(R.drawable.white_rectangle);
				params.leftMargin = 10;
			}
			params.leftMargin = 5;
			params.rightMargin = 5;
			pointList.add(imageView);
			ll_points.addView(imageView,params);
		}
		ll_points.setGravity(Gravity.CENTER_VERTICAL);
		
		mSlideViewPager.setAdapter(new mgPagerAdapter(bannerList,getActivity()));
		mSlideViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int position) {
				for(int i=0;i<mSlideCount;i++){
					if(i==(position%mSlideCount)){
						pointList.get(i).setBackgroundResource(R.drawable.red_rectangle);
					}else{
						//设置非当前显示页圆点
						pointList.get(i).setBackgroundResource(R.drawable.white_rectangle);
					}
				}
				index = position;
				titleBanner.setText(bannerList.get(position%mSlideCount).getTitle());
			}
		});
		//如果图片多于1张开启广告轮播
		if(mSlideCount>1){
			//开启广告轮播
			index = 0;
			if (mSlideMessager == null) {
				// 发消息让轮播图动起来
				mSlideMessager = new Handler() {
					public void handleMessage(Message msg) {
						if (mSlidesList.size()>0) {
							// 执行滑动到下一个页面
							mSlideViewPager.setCurrentItem(index + 1);
							// 在发一个handler延时
							mSlideMessager.sendEmptyMessageDelayed(0, 5000);
						}
					}
				};
				mSlideMessager.sendEmptyMessageDelayed(0, 3500);
			}
		}
	}
	
	private void init(MGListRespBean bean) {
		mBinding.recyclerview.refreshComplete();
		if(bean==null||bean.getData()==null)
			return;
		listData.clear();
		if(bean.getData().getHotgame()!=null){
			MGListBean hotBean=new MGListBean();
			hotBean.setImgnum(1);
			hotBean.setType("热门推荐");
			hotBean.setSoftlist(bean.getData().getHotgame());
			listData.add(hotBean);
		}
		if(bean.getData().getList()!=null)
			listData.addAll(bean.getData().getList());
		
		mAdapter.clearAndAddList(listData);
		mBinding.recyclerview.setNoMore(true);
	}

	private class mgPagerAdapter extends PagerAdapter {
		private List<SlidesBean> mBannerList;
		//	private List<ImageView> viewList;
//	private List<ImageView> imageViewList=new ArrayList<ImageView>();
		private Context mContext;

		// 构造方法
		public mgPagerAdapter(List<SlidesBean> bannerList, Context context) {
			this.mBannerList=bannerList;
			this.mContext = context;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}
		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}
		@Override
		public Parcelable saveState() {
			return null;
		}
		@Override
		public void startUpdate(View arg0) {
		}
		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			if (position<0){
				position = mBannerList.size()+position;
			}
			final int p = position%mBannerList.size();
			View imgLayout = View.inflate(mContext, R.layout.item_img_vp, null);
			final ImageView view=(ImageView) imgLayout.findViewById(R.id.imageView);
			GlideUtil.loadImage(mContext,mBannerList.get(p).getLitpic(),view);

			// 如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
			ViewParent vp = view.getParent();
			if (vp != null) {
				ViewGroup parent = (ViewGroup) vp;
				parent.removeView(view);
			}
			container.addView(view);
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {//:1新闻2攻略3游戏4手游5杂谈6评测7原创8安利9礼包10视频11专栏
					Intent intent = null;
					SlidesBean slidesBean=mBannerList.get(p);
					SoftGameBean soft = new SoftGameBean();
					soft.setTitle(slidesBean.getTitle());
					soft.setArcurl(slidesBean.getArcurl());
					soft.setType(slidesBean.getShowtype());
					soft.setAid(slidesBean.getAid());
					intent = new Intent(mContext, MGDetailActivity.class);
					Bundle bundlesoft = new Bundle();
					bundlesoft.putSerializable("mSoftGameBean", soft);
					intent.putExtras(bundlesoft);
					mContext.startActivity(intent);
				}
			});
			return view;
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}
	}

}