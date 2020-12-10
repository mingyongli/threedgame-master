package com.ws3dm.app.fragment;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ws3dm.app.R;
import com.ws3dm.app.adapter.BaseRecyclerAdapter;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.AvatarBean;
import com.ws3dm.app.databinding.FgGameConfigBinding;
import com.ws3dm.app.mvp.model.RespBean.GameConfigRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.GameService;
import com.ws3dm.app.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Describution :单机游戏配置信息
 * 
 * Author : DKjuan
 * 
 * Date : 2017/8/18 17:22
 **/
public class FragmentGameConfig extends BaseFragment {
	private FgGameConfigBinding mBinding;
//	private Context mContext;
	private int aid = 0;//游戏新闻列表用到
	private CommonRecyclerAdapter mAdapter=null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		AppCompatActivity activity = (AppCompatActivity) getActivity();

//		mContext=getActivity();
		mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_game_config, container, false);

		initView();
		return mBinding.getRoot();
	}
	
	public void initView(){
		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.mRecycleView.setLayoutManager(layoutManager);

		mBinding.mRecycleView.setPullRefreshEnabled(false);

		mAdapter = new CommonRecyclerAdapter<GameConfigRespBean.DataBean.ConfiglistBean>(mContext, R.layout.adapter_config) {
				@Override
				public void bindData(RecyclerViewHolder holder, final int position, final GameConfigRespBean.DataBean.ConfiglistBean configBean) {
					holder.setText(R.id.tv_title,configBean.getType());

					RecyclerView recyclerView = holder.getView(R.id.mRecycleView);
					LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
					layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
					recyclerView.setLayoutManager(layoutManager);

					BaseRecyclerAdapter<AvatarBean> mSubRecyclerAdapter= new BaseRecyclerAdapter<AvatarBean>(mContext, R.layout.item_config) {
						@Override
						public void bindData(RecyclerViewHolder holder, final int position, AvatarBean bean) {
							holder.setText(R.id.tv_key,bean.getTitle());
							holder.setText(R.id.tv_value,bean.getContent());
						}
					};
					recyclerView.setAdapter(mSubRecyclerAdapter);
					mSubRecyclerAdapter.clearAndAddList(configBean.getConfiglist());
					holder.getView(R.id.mRecycleView).setVisibility(View.VISIBLE);
				}
			};
		mBinding.mRecycleView.setAdapter(mAdapter);
	}

	public void getData(){
		aid=getArguments().getInt("aid");

		long time=System.currentTimeMillis();
		String validate= ""+aid+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("aid", aid);
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
		Call<GameConfigRespBean> call = service.getGameConfig(body);
		call.enqueue(new Callback<GameConfigRespBean>() {
			@Override
			public void onResponse(Call<GameConfigRespBean> call, Response<GameConfigRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				initData(response.body());
			}

			@Override
			public void onFailure(Call<GameConfigRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
			}
		});
	}

	public void initData(GameConfigRespBean config){
		if(config==null||config.getData()==null||config.getData().getList()==null){
			return;
		}
		mAdapter.clearAndAddList(config.getData().getList());
	}

	@Override
	protected void onFragmentFirstVisible() {
		super.onFragmentFirstVisible();
		getData();
	}
}