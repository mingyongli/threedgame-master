package com.ws3dm.app.util.Transformer;

import androidx.viewpager.widget.ViewPager;
import android.view.View;

/**
 * Author : DKjuan:
 * <p>
 * Date :  2019/6/21  10:32
 * 
 * 常用画廊效果 ，左右两边缩小
 * 
 * 使用
 *  mViewPager.setPageTransformer(true,new RotationPageTransformer());
    mViewPager.setOffscreenPageLimit(3); //预加载个数
    mViewPager.setPageMargin(30); //用于设置两个Page之间的距离



 page.setTranslationX(mAdapter.dp2px(-60));//这里设置位移，实现中间的pager盖住两边pager的效果
 */
public class GalleryPageTransformer implements ViewPager.PageTransformer {
	private static final float MIN_SCALE = 0.9f;
	private static final float MIN_ALPHA = 0.5f;

	private static float defaultScale = 0.9f;

	public void transformPage(View view, float position) {
		int pageWidth = view.getWidth();
		int pageHeight = view.getHeight();

		if (position < -1) { // [-Infinity,-1)
			// This page is way off-screen to the left.
			view.setAlpha(0);
			view.setScaleX(defaultScale);
			view.setScaleY(defaultScale);
		} else if (position <= 1) { // [-1,1]
			// Modify the default slide transition to shrink the page as well
			float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
			float vertMargin = pageHeight * (1 - scaleFactor) / 2;
			float horzMargin = pageWidth * (1 - scaleFactor) / 2;
			if (position < 0) {
				view.setTranslationX(horzMargin - vertMargin / 2);
			} else {
				view.setTranslationX(-horzMargin + vertMargin / 2);
			}

			// Scale the page down (between MIN_SCALE and 1)
			view.setScaleX(scaleFactor);
			view.setScaleY(scaleFactor);

			// Fade the page relative to its size.
			view.setAlpha(MIN_ALPHA +
					(scaleFactor - MIN_SCALE) /
							(1 - MIN_SCALE) * (1 - MIN_ALPHA));

		} else { // (1,+Infinity]
			// This page is way off-screen to the right.
			view.setAlpha(0);
			view.setScaleX(defaultScale);
			view.setScaleY(defaultScale);
		}
	}
}