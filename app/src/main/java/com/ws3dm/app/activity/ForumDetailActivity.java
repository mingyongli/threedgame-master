package com.ws3dm.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.webkit.WebSettings;

import com.umeng.socialize.UMShareAPI;
import com.ws3dm.app.R;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.bean.ForumDetailBean;
import com.ws3dm.app.Constant;
import com.ws3dm.app.databinding.AcForumDetailBinding;
import com.ws3dm.app.fragment.FragmentForumComment;
import com.ws3dm.app.fragment.FragmentForumDetailWeb;
import com.ws3dm.app.mvp.presenter.ForumPresenter;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.ForumService;
import com.ws3dm.app.util.AppUtil;
import com.ws3dm.app.util.SharedUtil;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.view.NoScrollWebView;

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
 * Describution :新闻页，展示新闻详情，不是列表
 * 
 * Author : DKjuan
 * 
 * Date : 2017/9/4 16:32
 **/
public class ForumDetailActivity extends BaseActivity{
	private ForumPresenter mForumPresenter;
	private Handler mHandler;
	private ForumDetailBean bean;
	private int position;
	private long firstTime = 0;
	public static ViewPager viewPager;
	private List<Fragment> fragments = new ArrayList<>();
	
	private AcForumDetailBinding mBinding;
	
	@Override
	protected void init() {
		mBinding=bindView(R.layout.ac_forum_detail);
		mBinding.setHandler(this);

		mForumPresenter = ForumPresenter.getInstance();
		mForumPresenter.setHandler(mHandler);
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
//					case Constant.Notify.RESULT_TIDFAVORITE://处理返回结果
//						JSONObject object= (JSONObject) msg.obj;
//						
//						break;
				}
			}
		};
		
		initView();
	}

	private void initView(){
		if (getIntent().hasExtra("forumDetailBean"))
			bean = ((ForumDetailBean) getIntent().getSerializableExtra("forumDetailBean"));

		//获取数据
		long time=System.currentTimeMillis();
		String validate= MyApplication.getUserData().uid+bean.getTid()+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid", MyApplication.getUserData().uid);
			obj.put("tid", bean.getTid());
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
//		mForumPresenter.getTidFavorite(obj.toString());//异步请求
		//同步请求
//		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
//		ForumService.Api service = retrofit.create(ForumService.Api.class);
//		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
//
//		Call<JSONObject> call=service.getTidFavorite(body);
//		call.enqueue(new Callback<JSONObject>() {
//			@Override
//			public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
//				Log.e("requestSuccess","%-----------------------"+response.body());
//			}
//
//			@Override
//			public void onFailure(Call<JSONObject> call, Throwable throwable) {
//				Log.e("requestFailure",throwable.getMessage()+"");
//			}
//		});
		
		fragments.clear();
		for (int i = 0; i < 2; i++) {
			Fragment fragment;
			Bundle bundle = new Bundle();
			if (i == 0) {
				fragment = new FragmentForumDetailWeb();
			} else {
				fragment = new FragmentForumComment();
			}
			bundle.putString("type", "ForumDetailActivity");
			bundle.putSerializable("forumDetailBean", bean);
//			if (strId != null)
//				bundle.putBoolean("isFinish", false);
//			else
//				bundle.putBoolean("isFinish", true);
			fragment.setArguments(bundle);
			fragments.add(fragment);
		}
//		mBinding.txtTitle.setText(bean.getTitle());
		mBinding.mViewPager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager()));
		mBinding.mViewPager.addOnPageChangeListener(new MyOnPageChangeListener());
		mBinding.mViewPager.setOffscreenPageLimit(0);
		viewPager=mBinding.mViewPager;
	}

	private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			mBinding.mViewPager.setCurrentItem(arg0);
			position = arg0;
			if (arg0 == 1) {
//				mBinding.imgRefresh.setVisibility(View.GONE);
				mBinding.imgChangeTxt.setVisibility(View.GONE);
			} else {
//				mBinding.imgRefresh.setVisibility(View.VISIBLE);
				mBinding.imgChangeTxt.setVisibility(View.VISIBLE);
				AppUtil.closeKeyboard(mContext);
			}
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	private class MyFragmentAdapter extends FragmentPagerAdapter {
		public MyFragmentAdapter(FragmentManager fm) {
			super(fm);
		}
		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}
		@Override
		public int getCount() {
			return fragments.size();
		}
	}
	
	public void clickHandler(View view) {
		switch (view.getId()) {
			// 返回
			case R.id.imgReturn:
				if (position == 0) {
//					if (!isFinish){
////						startActivity(new Intent(NewsActivity.this, AppLoadingActivity.class));
//					}
					finish();
				} else{
					mBinding.mViewPager.setCurrentItem(0);
				}
				break;
			case R.id.imgRefresh:
//				initFragment();//临时注释 不重新初始化fragment，只加载网页
				((NoScrollWebView) fragments.get(0).getView().findViewById(R.id.mWebView)).reload();
				ToastUtil.showToast(mContext,"页面已重新加载");
				break;
			// 选择字体大小
			case R.id.imgChangeTxt:
				mBinding.txtBig.setTextColor(0xff555555);
				mBinding.txtSmall.setTextColor(0xff555555);
				mBinding.txtNormal.setTextColor(0xff555555);
				mBinding.imgBig.setImageResource(R.drawable.next);
				mBinding.imgSmall.setImageResource(R.drawable.next);
				mBinding.imgNormal.setImageResource(R.drawable.next);
				if (SharedUtil.getSharedPreferencesData("FontSize").equals("1")) {
					mBinding.txtSmall.setTextColor(0xffe8433c);
					mBinding.imgSmall.setImageResource(R.drawable.next_red);
				}
				if (SharedUtil.getSharedPreferencesData("FontSize").equals("0")) {
					mBinding.txtNormal.setTextColor(0xffe8433c);
					mBinding.imgNormal.setImageResource(R.drawable.next_red);
				}
				if (SharedUtil.getSharedPreferencesData("FontSize").equals("2")) {
					mBinding.txtBig.setTextColor(0xffe8433c);
					mBinding.imgBig.setImageResource(R.drawable.next_red);
				}
				mBinding.rlChangeTxt.setVisibility(View.VISIBLE);
				break;
			// 取消提示框
			case R.id.txtHide:
			case R.id.txtCancel:
				mBinding.rlChangeTxt.setVisibility(View.GONE);
				break;
			// 小字体
			case R.id.rlSmall:
			case R.id.txtSmall:
			case R.id.imgSmall:
				mBinding.rlChangeTxt.setVisibility(View.GONE);
				SharedUtil.setSharedPreferencesData("FontSize", "1");
				((NoScrollWebView) fragments.get(0).getView().findViewById(R.id.mWebView)).getSettings().setTextSize(WebSettings.TextSize.SMALLER);
				break;
			// 中字体
			case R.id.rlNormal:
			case R.id.txtNormal:
			case R.id.imgNormal:
				mBinding.rlChangeTxt.setVisibility(View.GONE);
				SharedUtil.setSharedPreferencesData("FontSize", "0");
				((NoScrollWebView) fragments.get(0).getView().findViewById(R.id.mWebView)).getSettings().setTextSize(WebSettings.TextSize.NORMAL);
				break;
			// 大字体
			case R.id.rlBig:
			case R.id.txtBig:
			case R.id.imgBig:
				mBinding.rlChangeTxt.setVisibility(View.GONE);
				SharedUtil.setSharedPreferencesData("FontSize", "2");
				((NoScrollWebView) fragments.get(0).getView().findViewById(R.id.mWebView)).getSettings().setTextSize(WebSettings.TextSize.LARGER);
				break;
		}
	}
	
	public void setFavourite(boolean operate){//添加删除收藏
		//获取数据
		long time=System.currentTimeMillis();
		String validate= MyApplication.getUserData().uid+bean.getTid()+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid", MyApplication.getUserData().uid);
			obj.put("tid", bean.getTid());
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		ForumService.Api service = retrofit.create(ForumService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());

		Call<JSONObject> call=service.getTidFavorite(body);
		call.enqueue(new Callback<JSONObject>() {
			@Override
			public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
				Log.e("requestSuccess","%-----------------------"+response.body());
			}

			@Override
			public void onFailure(Call<JSONObject> call, Throwable throwable) {
				Log.e("requestFailure",throwable.getMessage()+"");
			}
		});
	}


	// 手指向右滑动时的最小速度
	private static final int XSPEED_MIN = 7000;

	// 记录手指按下时的横坐标。
	private float xDown;

	// 记录手指移动时的横坐标。
	private float xMove;

	// 手指向右滑动时的最小距离
	private int XDISTANCE_MIN = 400;

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		createVelocityTracker(event);
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				xDown = event.getRawX();
				break;
			case MotionEvent.ACTION_MOVE:
				xMove = event.getRawX();
				// 活动的距离
				int distanceX = (int) (xMove - xDown);
				// 获取顺时速度
				int xSpeed = getScrollVelocity();
				// 当滑动的距离大于我们设定的最小距离且滑动的瞬间速度大于我们设定的速度时，返回到上一个activity
				if (distanceX > XDISTANCE_MIN && xSpeed > XSPEED_MIN) {
					//判断是否间隔800毫秒
					long secondTime = System.currentTimeMillis();
					if (position == 0&&secondTime - firstTime > 800) {                                         //如果两次按键时间间隔大于2秒，则不退出  
						finish();
					} else {                                                    //两次按键小于2秒时，退出应用  
						firstTime = secondTime;//更新firstTime
						mBinding.mViewPager.setCurrentItem(0);
					}
				}
				break;
			case MotionEvent.ACTION_UP:
//				recycleVelocityTracker();
				break;
			default:
				break;
		}
		return super.dispatchTouchEvent(event);
	}

	// 用于计算手指滑动的速度。
	private VelocityTracker mVelocityTracker;

	/**
	 * 创建VelocityTracker对象，并将触摸content界面的滑动事件加入到VelocityTracker当中。
	 *
	 * @param event
	 *
	 */
	private void createVelocityTracker(MotionEvent event) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
	}

	/**
	 * 回收VelocityTracker对象。
	 */
	private void recycleVelocityTracker() {
		mVelocityTracker.recycle();
		mVelocityTracker = null;
	}

	/**
	 * 获取手指在content界面滑动的速度。
	 *
	 * @return 滑动速度，以每秒钟移动了多少像素值为单位。
	 */
	private int getScrollVelocity() {
		mVelocityTracker.computeCurrentVelocity(1000);
		int velocity = (int) mVelocityTracker.getXVelocity();
		return Math.abs(velocity);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (position == 0) {
				finish();
			} else{
				mBinding.mViewPager.setCurrentItem(0);
			}
			return false;
		}else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMShareAPI.get(mContext).onActivityResult(requestCode, resultCode, data);
	}
}