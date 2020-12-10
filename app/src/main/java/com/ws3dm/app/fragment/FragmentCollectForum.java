package com.ws3dm.app.fragment;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ws3dm.app.R;
import com.ws3dm.app.activity.ForumDetailActivity;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.bean.ForumDetailBean;
import com.ws3dm.app.databinding.FgCollectNewsBinding;
import com.ws3dm.app.mvp.model.RespBean.ForumTopRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.ForumService;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.util.glide.GlideUtil;
import com.ws3dm.app.view.delrecyclerview.DelViewHolder;
import com.ws3dm.app.view.delrecyclerview.OnItemDelClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Describution : 收藏的帖子 fragment
 * 
 * Author : DKjuan
 * 
 * Date : 2018/3/27 15:39
 **/
public class FragmentCollectForum extends BaseFragment {
	private FgCollectNewsBinding mBinding;
	private List<ForumDetailBean> listData=new ArrayList<ForumDetailBean>();
	private int clickPosition=0;
	private FragmentCollectForum.MyAdapter adapter;
	private String uid;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_collect_news, container, false);
		uid = getArguments().getString("uid");
//		obtainData();
		return mBinding.getRoot();
	}

	public void obtainData(){
		//获取数据
		long time=System.currentTimeMillis();
		String validate= (StringUtil.isEmpty(uid) ? MyApplication.getUserData().uid : uid)+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid", (StringUtil.isEmpty(uid) ? MyApplication.getUserData().uid : uid));
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		ForumService.Api service = retrofit.create(ForumService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<ForumTopRespBean> call = service.getThreadFavorite(body);
		call.enqueue(new Callback<ForumTopRespBean>() {
			@Override
			public void onResponse(Call<ForumTopRespBean> call, Response<ForumTopRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
//				listData=response.body().getData().getList();
				initData(response.body());
			}

			@Override
			public void onFailure(Call<ForumTopRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
			}
		});
	}

	public void initData(ForumTopRespBean dateBean){
		if(dateBean==null||dateBean.getData()==null||dateBean.getData().getList()==null||dateBean.getData().getList().size()==0){
			mBinding.tvTip.setVisibility(View.VISIBLE);
			return;
		}
		listData=dateBean.getData().getList();
		if(listData.size()>0){
			adapter = new FragmentCollectForum.MyAdapter(mContext, listData);
			mBinding.removeRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
			mBinding.removeRecyclerview.setAdapter(adapter);
			mBinding.removeRecyclerview.setOnItemClickListener(new OnItemDelClickListener() {
				@Override
				public void onItemClick(View view, int position) {
					Intent intent = new Intent(mContext, ForumDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("forumDetailBean",listData.get(position));
					intent.putExtras(bundle);
					startActivity(intent);
				}

				@Override
				public void onDeleteClick(int position) {
//				adapter.removeItem(position);
					clickPosition=position;
					addDelFavorite(position);
				}
			});
		}else{
//			ToastUtil.showToast(mContext,"暂无收藏");
		}
	}

	public void addDelFavorite(int position){//1添加2删除
		String tid=listData.get(position).getTid();
		long time=System.currentTimeMillis();
		String validate= ""+(StringUtil.isEmpty(uid) ? MyApplication.getUserData().uid : uid)+tid+2+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid", (StringUtil.isEmpty(uid) ? MyApplication.getUserData().uid : uid));
			obj.put("tid",tid);
			obj.put("act",2);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		ForumService.Api service = retrofit.create(ForumService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<ResponseBody> call = service.setThreadFavorite(body);
		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				try {
					JSONObject jsonObject=new JSONObject(response.body().string());
					if(jsonObject.optInt("code")==1){
						adapter.removeItem(clickPosition);
					}else{
						ToastUtil.showToast(mContext,jsonObject.opt("msg")+"");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Call<ResponseBody> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
			}
		});
	}

	@Override
	protected void onFragmentFirstVisible() {
		super.onFragmentFirstVisible();
		obtainData();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public class MyAdapter extends RecyclerView.Adapter {
		private Context mContext;
		private LayoutInflater mInflater;
		private List<ForumDetailBean> mList;

		public MyAdapter(Context context, List<ForumDetailBean> list) {
			mContext = context;
			mInflater = LayoutInflater.from(context);
			mList = list;
		}

		@Override
		public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			return new DelViewHolder(mContext,mInflater.inflate(R.layout.item_collect_forum, parent, false));
		}

		@Override
		public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
			DelViewHolder viewHolder = (DelViewHolder) holder;

			GlideUtil.loadCircleImage(mContext,mList.get(position).getUser().avatarstr, (ImageView) viewHolder.getView(R.id.img_head));
			viewHolder.setText(R.id.tv_name, mList.get(position).getUser().nickname);
			int length=mList.get(position).getType().length();
			viewHolder.setText(R.id.tv_tag, mList.get(position).getType());
			if(length<=0){
				viewHolder.getView(R.id.tv_tag).setVisibility(View.GONE);
				viewHolder.setText(R.id.tv_content, mList.get(position).getTitle());
			}else if(length>3){
				viewHolder.getView(R.id.tv_tag).setVisibility(View.VISIBLE);
				viewHolder.setText(R.id.tv_content, "          "+"          "+mList.get(position).getTitle());
			}else{
				viewHolder.getView(R.id.tv_tag).setVisibility(View.VISIBLE);
				viewHolder.setText(R.id.tv_content, "          "+mList.get(position).getTitle());
			}
			viewHolder.setText(R.id.tv_time, TimeUtil.getTime(mList.get(position).getPubdate_at()));
			viewHolder.setText(R.id.tv_commentnum, mList.get(position).getReplies());
			viewHolder.setText(R.id.tv_read, mList.get(position).getViews());
		}

		public int getItemCount() {
			return mList != null ? mList.size() : 0;
		}

		public void removeItem(int position) {
			mList.remove(position);
			notifyItemRemoved(position);
		}
	}

	@Override
	protected void onFragmentVisibleChange(boolean isVisible) {
		super.onFragmentVisibleChange(isVisible);
		if (!isVisible&&adapter!=null){
			mBinding.removeRecyclerview.reset();
		}
	}
}