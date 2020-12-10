package com.ws3dm.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.webkit.WebSettings;

import com.umeng.socialize.UMShareAPI;
import com.ws3dm.app.Constant;
import com.ws3dm.app.R;
import com.ws3dm.app.bean.OriginalBean;
import com.ws3dm.app.databinding.AcOriginalBinding;
import com.ws3dm.app.emoj.fragment.EmotionMainFragment;
import com.ws3dm.app.fragment.FragmentOriginalComment;
import com.ws3dm.app.fragment.FragmentOriginalWeb;
import com.ws3dm.app.mvp.model.RespBean.NewsCommentRespBean;
import com.ws3dm.app.mvp.presenter.NewsPresenter;
import com.ws3dm.app.util.AppUtil;
import com.ws3dm.app.util.SharedUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.view.NoScrollWebView;

import java.util.ArrayList;
import java.util.List;

/**
 * Describution :原创页
 * 
 * Author : DKjuan
 * 
 * Date : 2017/9/4 16:32
 **/
public class OriginalActivity extends BaseActivity{
	private OriginalBean mOriginalBean;
	private int position;
	private long firstTime = 0;
	public static ViewPager viewPager;
	private List<Fragment> fragments = new ArrayList<>();
	private  FragmentOriginalWeb mFragmentOriginalWeb;
	private  Fragment mFragmentComments;

	public static EmotionMainFragment emotionMainFragment;
	private AcOriginalBinding mBinding;
	private NewsPresenter mNewsPresenter;
	private Handler mHandler;
	
	@Override
	protected void init() {
		mBinding=bindView(R.layout.ac_original);
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
						break;
					case Constant.Notify.RESULT_NEWS_COMMENT_LIST:
						NewsCommentRespBean comments= (NewsCommentRespBean) msg.obj;
//						initComments((NewsCommentRespBean) msg.obj);
						break;
				}
			}
		};

		mNewsPresenter = NewsPresenter.getInstance();
		mNewsPresenter.setHandler(mHandler);

		initView();
	}

	private void initView(){
		if (getIntent().hasExtra("originalBean"))
			mOriginalBean = (OriginalBean) getIntent().getSerializableExtra("originalBean");

		initFragment();
	}

	//  初始化Fragment
	private void initFragment() {
		fragments.clear();

		mFragmentOriginalWeb = new FragmentOriginalWeb();
		Bundle bundleWeb = new Bundle();
		bundleWeb.putString("type", "OriginalActivity");
		bundleWeb.putSerializable("originalBean", mOriginalBean);
		mFragmentOriginalWeb.setArguments(bundleWeb);
		fragments.add(mFragmentOriginalWeb);

		mFragmentComments= new FragmentOriginalComment();
		Bundle bundleComments = new Bundle();
		bundleComments.putString("type", "OriginalActivity");
		bundleComments.putSerializable("originalBean", mOriginalBean);
		mFragmentComments.setArguments(bundleComments);
		fragments.add(mFragmentComments);

//		mBinding.txtTitle.setText(news.getTitle());
		mBinding.mViewPager.setOverScrollMode(ViewPager.OVER_SCROLL_NEVER);
		mBinding.mViewPager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager()));
		mBinding.mViewPager.addOnPageChangeListener(new MyOnPageChangeListener());
		mBinding.mViewPager.setOffscreenPageLimit(0);
		viewPager=mBinding.mViewPager;
	}

	private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			mBinding.mViewPager.setCurrentItem(arg0);
			position = arg0;//左滑
			if (arg0 == 1) {
//				mBinding.imgRefresh.setVisibility(View.GONE);
//				mBinding.imgChangeTxt.setVisibility(View.GONE);
				mBinding.bgImgBack.setVisibility(View.GONE);
				mBinding.bgImgRefresh.setVisibility(View.GONE);
				mBinding.bgImgTxt.setVisibility(View.GONE);
			} else {
//				mBinding.imgRefresh.setVisibility(View.VISIBLE);
//				mBinding.imgChangeTxt.setVisibility(View.VISIBLE);
//				AppUtil.closeKeyboard(mContext);
				mBinding.bgImgBack.setVisibility(View.VISIBLE);
				mBinding.bgImgRefresh.setVisibility(View.VISIBLE);
				mBinding.bgImgTxt.setVisibility(View.VISIBLE);
				AppUtil.closeKeyboard(mContext);
			}
		}
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
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
					finish();
				} else{
					mBinding.mViewPager.setCurrentItem(0);
				}
				break;
			case R.id.imgRefresh:
				((NoScrollWebView) fragments.get(0).getView().findViewById(R.id.mWebView)).reload();
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

	// 手指向右滑动时的最小速度
	private static final int XSPEED_MIN = 5000;

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
					if (position == 0&&secondTime - firstTime > 500) {                                         //如果两次按键时间间隔大于2秒，则不退出  
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
				if (!emotionMainFragment.isInterceptBackPress()) {//判断是否拦截返回键操作
					if (mFragmentOriginalWeb.customView != null) {//判断是否视频全屏
						mFragmentOriginalWeb.hideCustomView();
					} else {
						finish();
					}
				}
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