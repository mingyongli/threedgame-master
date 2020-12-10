package com.ws3dm.app.util.Transformer;

import androidx.viewpager.widget.ViewPager;
import android.view.View;

/**
 * Author : DKjuan:
 * <p>
 * Date :  2019/6/21  10:32
 * 
 * 3D画廊效果 
 * 
 * 使用
 *  mViewPager.setPageTransformer(true,new RotationPageTransformer());
    mViewPager.setOffscreenPageLimit(3); //预加载个数
    mViewPager.setPageMargin(30); //用于设置两个Page之间的距离



 page.setTranslationX(mAdapter.dp2px(-60));//这里设置位移，实现中间的pager盖住两边pager的效果
 */
public class RotationPageTransformer implements ViewPager.PageTransformer {

	private static final float MIN_SCALE=0.85f;

	@Override
	public void transformPage(View page, float position) {
		float scaleFactor = Math.max(MIN_SCALE,1 - Math.abs(position));
		float rotate = 10 * Math.abs(position);
		//position小于等于1的时候，代表page已经位于中心item的最左边，
		//此时设置为最小的缩放率以及最大的旋转度数
		if (position <= -1){
			page.setScaleX(MIN_SCALE);
			page.setScaleY(MIN_SCALE);
			page.setRotationY(rotate);
		}else if (position < 0){//position从0变化到-1，page逐渐向左滑动
			page.setScaleX(scaleFactor);
			page.setScaleY(scaleFactor);
			page.setRotationY(rotate);
		}else if (position >=0 && position < 1){//position从0变化到1，page逐渐向右滑动
			page.setScaleX(scaleFactor);
			page.setScaleY(scaleFactor);
			page.setRotationY(-rotate);
		}else if (position >= 1){//position大于等于1的时候，代表page已经位于中心item的最右边
			page.setScaleX(scaleFactor);
			page.setScaleY(scaleFactor);
			page.setRotationY(-rotate);
		}
	}
}