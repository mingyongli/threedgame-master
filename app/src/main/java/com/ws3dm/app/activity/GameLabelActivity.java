package com.ws3dm.app.activity;

import android.content.Intent;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ws3dm.app.R;
import com.ws3dm.app.adapter.BaseRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.AvatarBean;
import com.ws3dm.app.databinding.AcGameLabelBinding;
import com.ws3dm.app.mvp.model.RespBean.GameDJLabelRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.GameService;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Describution : 游戏标签
 * 
 * Author : DKjuan
 * 
 * Date : 2019/6/19 13:46
 **/
public class GameLabelActivity extends BaseActivity{
	
	private AcGameLabelBinding mBinding;
	private BaseRecyclerAdapter<GameDJLabelRespBean.DataBean.ListBean> mLabelAdapter;
	private BaseRecyclerAdapter<AvatarBean> mSubRecyclerAdapter;
	private GameDJLabelRespBean mGameDJLabelRespBean;

	@Override
	protected void init() {
		mBinding = bindView(R.layout.ac_game_label);
		mBinding.setHandler(this);

		initView();
		
	}
	
	//界面初始化
	private void initView() {
//		//测试
//		AvatarBean tAvatarBean;
//		CategoryBean mCategoryBean;
//		ArrayList<AvatarBean> labels=new ArrayList<>();
//		ArrayList<CategoryBean> datas=new ArrayList<>();
//		for(int i=0;i<7;i++){
//			tAvatarBean=new AvatarBean();
//			tAvatarBean.setName("tag"+i);
//			labels.add(tAvatarBean);
//		}
//
//		for (int i=0; i<26; i++){
//			mCategoryBean=new CategoryBean((char)(65+i)+"",labels);
//			datas.add(mCategoryBean);
//		}
//		//测试
		

		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.recyclerview.setLayoutManager(layoutManager);

//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);原来就注释掉的           y
//		mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
//		mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);
		mBinding.recyclerview.setLoadingMoreEnabled(false);
		mBinding.recyclerview.setPullRefreshEnabled(false);

		mLabelAdapter = new BaseRecyclerAdapter<GameDJLabelRespBean.DataBean.ListBean>(mContext, R.layout.adapter_label) {
			@Override
			public void bindData(RecyclerViewHolder holder, final int position, final GameDJLabelRespBean.DataBean.ListBean dataBean) {
				holder.setText(R.id.tv_title,dataBean.getName());

				RecyclerView recyclerView_labels = holder.getView(R.id.recyclerView_labels);
				GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);

				mSubRecyclerAdapter = new BaseRecyclerAdapter<AvatarBean>(mContext, R.layout.item_pop_label) {
					@Override
					public void bindData(RecyclerViewHolder holder, int position, AvatarBean item) {
						TextView tv_name=holder.getView(R.id.tv_name);
						tv_name.setHeight(25);
						tv_name.setText(item.getName());
//						holder.setText(R.id.tv_name, item.getName());
					}
				};
				mSubRecyclerAdapter.setOnClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
					@Override
					public void onItemClick(View itemView, int position) {
						Intent intent=new Intent(mContext, GameCategoryListActivity.class);
						intent.putExtra("title",dataBean.getLabels().get(position).getName());
						intent.putExtra("labelid",dataBean.getLabels().get(position).getId());
						mContext.startActivity(intent);
					}
				});

				recyclerView_labels.setLayoutManager(gridLayoutManager);
				recyclerView_labels.setAdapter(mSubRecyclerAdapter);
				mSubRecyclerAdapter.clearAndAddList(dataBean.getLabels());
			}
		};
		mBinding.recyclerview.setAdapter(mLabelAdapter);
		
		//获取数据
		long time=System.currentTimeMillis();
		String sign= StringUtil.MD5(time+"");
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
		GameService.Api service = retrofit.create(GameService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<GameDJLabelRespBean> call = service.djlabel(body);
		call.enqueue(new Callback<GameDJLabelRespBean>() {
			@Override
			public void onResponse(Call<GameDJLabelRespBean> call, Response<GameDJLabelRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				mGameDJLabelRespBean=response.body();
				if(mGameDJLabelRespBean.getData()!=null){
					mLabelAdapter.clearAndAddList(mGameDJLabelRespBean.getData().getList());
				}else{
					ToastUtil.showToast(mContext,"数据异常！");
				}
			}

			@Override
			public void onFailure(Call<GameDJLabelRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
				mBinding.recyclerview.loadMoreError();
			}
		});
	}

	public void clickHandler(View view) {
		switch (view.getId()) {
			// 返回
			case R.id.imgReturn:
				finish();
				break;
		}
	}
}
