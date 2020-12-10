package com.ws3dm.app.fragment;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.ImageActivity;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.databinding.FgBaseRecyclerviewBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Describution :游戏图片列表
 * 
 * Author : DKjuan
 * 
 * Date : 2017/8/18 17:22
 **/
public class FragmentGameImage extends BaseFragment {
	private FgBaseRecyclerviewBinding mBinding;
	private CommonRecyclerAdapter<String> mAdapter;
	private List<String> imgs;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		AppCompatActivity activity = (AppCompatActivity) getActivity();

		mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_base_recyclerview, container, false);

		initView();
		return mBinding.getRoot();
	}

	public void initView(){
		imgs = getArguments().getStringArrayList("images");//type 可能为SearchActivity
		
		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.recyclerview.setLayoutManager(layoutManager);

		mBinding.recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
		mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);
		mBinding.recyclerview.setPullRefreshEnabled(false);

		mAdapter = new CommonRecyclerAdapter<String>(mContext, R.layout.adapter_game_image) {
			@Override
			public void bindData(RecyclerViewHolder holder, int position, final String url) {
				holder.setImageByUrl(R.id.img_game,url);
			}
		};
		mBinding.recyclerview.setAdapter(mAdapter);
		mAdapter.clearAndAddList(imgs);
		mAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int position) {
				Intent intent=new Intent(mContext,ImageActivity.class);
				intent.putExtra("position",""+position);
				intent.putExtra("title",position);
				intent.putStringArrayListExtra("url",new ArrayList<>(imgs));
				startActivity(intent);
			}
		});
	}
}