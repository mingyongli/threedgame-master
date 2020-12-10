package com.ws3dm.app.fragment;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.ws3dm.app.R;
import com.ws3dm.app.databinding.FgNewsPhotoBinding;

/**
 * Describution :新闻图片Fragment
 * 
 * Author : DKjuan
 * 
 * Date : 2017/8/18 17:22
 **/
public class FragmentNewsPhoto extends BaseFragment {
	View view;
	String strUrl;
	
	private FgNewsPhotoBinding mBinding;
//	private Context mContext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		mContext=getActivity();
		mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_news_photo, container, false);

		initView();
		return mBinding.getRoot();
	}
	
	public void initView(){
		strUrl = getArguments().getString("url");
		mBinding.mPhotoView.enable();
		mBinding.mPhotoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		Glide.with(mContext)
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

	@Override
	public void onResume() {
		super.onResume();
	}
}