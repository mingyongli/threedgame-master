package com.ws3dm.app.fragment;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.ws3dm.app.R;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.databinding.FgImageBinding;

/**
 * Describution :图片 fragment
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2017/8/18 17:22
 **/
public class FragmentImage extends BaseFragment {
	private FgImageBinding mBinding;
	private String strUrl;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_image, container, false);
//		mBinding.setHandler(this);

		initView();

		return mBinding.getRoot();
	}

	public void initView() {
		strUrl = getArguments().getString("url");
		mBinding.mPhotoView.enable();
		mBinding.mPhotoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		Glide.with(MyApplication.getInstance())
				.load(strUrl)
				.into(new GlideDrawableImageViewTarget(mBinding.mPhotoView) {
					@Override
					public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
						super.onResourceReady(resource, animation);
						if (mBinding.mProgressBar != null)
							mBinding.mProgressBar.setVisibility(View.GONE);
					}
				});
	}
}