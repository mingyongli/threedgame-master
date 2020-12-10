package com.ws3dm.app.fragment;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.GameCategoryListActivity;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.AvatarBean;
import com.ws3dm.app.bean.CategoryBean;
import com.ws3dm.app.Constant;
import com.ws3dm.app.databinding.FgBaseRecyclerviewBinding;
import com.ws3dm.app.mvp.model.RespBean.GameCategoryRespBean;
import com.ws3dm.app.mvp.presenter.GamePresenter;
import com.ws3dm.app.util.SharedUtil;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;
import com.ws3dm.app.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Describution :分类fragment，PC分类
 * 
 * Author : DKjuan
 * 
 * Date : 2017/8/18 17:22
 **/
public class FragmentCategory extends BaseFragment {
	private FgBaseRecyclerviewBinding mBinding;
	private CommonRecyclerAdapter<CategoryBean> mRecyclerAdapter;
	private CommonRecyclerAdapter<AvatarBean> mSubRecyclerAdapter;
	public ArrayList<CategoryBean> listData = new ArrayList<CategoryBean>();
	public GameCategoryRespBean result;
	
	private GamePresenter mGamePresenter;
	private Handler mHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		AppCompatActivity activity = (AppCompatActivity) getActivity();

		mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_base_recyclerview, container, false);

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
						if(listData.size()==0){
							String json = SharedUtil.getSharedPreferencesData("gamecate");
							if (!StringUtil.isEmpty(json)&&json.length()>100) {
								result= JSON.parseObject(json,GameCategoryRespBean.class);
								initData(result);
							}
						}
						mBinding.recyclerview.loadMoreError();
						break;
					case Constant.Notify.RESULT_GAME_CATEGORY:
						result= (GameCategoryRespBean) msg.obj;
						initData(result);
						if(!TimeUtil.dateDayNow().equals(SharedUtil.getSharedPreferencesData("todaygame"))){
							SharedUtil.setSharedPreferencesData("todaygame",TimeUtil.dateDayNow());
							SharedUtil.setSharedPreferencesData("gamecate", JSON.toJSONString(msg.obj));
						}
						break;
				}
			}
		};

		mGamePresenter = GamePresenter.getInstance();
		mGamePresenter.setHandler(mHandler);

		initView();
		return mBinding.getRoot();
	}

	public void initView(){
		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.recyclerview.setLayoutManager(layoutManager);

//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);原来就注释掉的           y
//		mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
//		mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);
		mBinding.recyclerview.setLoadingMoreEnabled(false);
		mBinding.recyclerview.setPullRefreshEnabled(false);

		mRecyclerAdapter = new CommonRecyclerAdapter<CategoryBean>(mContext, R.layout.adapter_category) {
			@Override
			public void bindData(RecyclerViewHolder holder, final int position, final CategoryBean category) {
				holder.setText(R.id.tv_title, category.getTitle());
				holder.setImageResource(R.id.img_title,category.getImg());

				RecyclerView recyclerView_labels = holder.getView(R.id.recyclerView_labels);
				GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);

				mSubRecyclerAdapter = new CommonRecyclerAdapter<AvatarBean>(mContext, R.layout.adapter_cate_child) {
					@Override
					public void bindData(RecyclerViewHolder holder, int position, AvatarBean item) {
						holder.setText(R.id.tv_label, item.getName());
					}
				};
				mSubRecyclerAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
					@Override
					public void onItemClick(View itemView, int position) {
						Intent intent=new Intent(mContext, GameCategoryListActivity.class);
						intent.putExtra("title",category.getLabel().get(position).getName());
						switch (category.getTitle()) {
							case "游戏平台":
								intent.putExtra("platid",category.getLabel().get(position).getId());
								break;
							case "游戏分类":
								intent.putExtra("typeid",category.getLabel().get(position).getId());
								break;
							case "游戏标签":
								intent.putExtra("tagid",category.getLabel().get(position).getId());
								break;
						}
						mContext.startActivity(intent);
					}
				});

				recyclerView_labels.setLayoutManager(gridLayoutManager);
				recyclerView_labels.setAdapter(mSubRecyclerAdapter);
				mSubRecyclerAdapter.clearAndAddList(category.getLabel());
			}
		};
		mBinding.recyclerview.setAdapter(mRecyclerAdapter);
//		mBinding.recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
//			@Override
//			public void onRefresh() {
//				new Handler().postDelayed(new Runnable(){
//					public void run() {
//						mBinding.recyclerview.refreshComplete();
//					}
//				}, 450);            //refresh data here
//			}
//
//			@Override
//			public void onLoadMore() {
//				new Handler().postDelayed(new Runnable(){
//					public void run() {
//						mBinding.recyclerview.setNoMore(true);
//					}
//				}, 450);
//			}
//		});

		//获取数据
		long time=System.currentTimeMillis();
		String validate=""+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		mGamePresenter.getGameCategory(obj.toString());
	}

	private void initData(GameCategoryRespBean bean) {
		if(bean==null){
			return;
		}

		CategoryBean platlist=new CategoryBean();
		platlist.setImg(R.drawable.ic_game1);
		platlist.setTitle("游戏平台");
		platlist.setLabel(bean.getData().getPlatlist());
		listData.add(platlist);

		CategoryBean typelist=new CategoryBean();
		typelist.setImg(R.drawable.ic_game2);
		typelist.setTitle("游戏分类");
		typelist.setLabel(bean.getData().getTypelist());
		listData.add(typelist);

		CategoryBean labellist=new CategoryBean();
		labellist.setImg(R.drawable.ic_game3);
		labellist.setTitle("游戏标签");
		labellist.setLabel(bean.getData().getLabellist());
		listData.add(labellist);

		mRecyclerAdapter.clearAndAddList(listData);
	}

	@Override
	protected void onFragmentFirstVisible() {
		super.onFragmentFirstVisible();
		mBinding.recyclerview.setRefreshing(true);//refresh的监听处理中已经有加载数据的操作
	}
}