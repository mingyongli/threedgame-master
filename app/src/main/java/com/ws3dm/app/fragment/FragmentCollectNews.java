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

import com.ws3dm.app.R;
import com.ws3dm.app.activity.NewsActivity;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.databinding.FgCollectNewsBinding;
import com.ws3dm.app.mvp.model.RespBean.CollectNewsRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.UserService;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;
import com.ws3dm.app.util.ToastUtil;
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
 * Describution : 收藏的新闻 fragment
 * 
 * Author : DKjuan
 * 
 * Date : 2018/3/27 15:39
 **/
public class FragmentCollectNews extends BaseFragment {
	private FgCollectNewsBinding mBinding;
//	private Context mContext;
	private List<NewsBean> listData=new ArrayList<NewsBean>();
	private int clickPosition=0;
	private MyAdapter adapter;
	private String uuid;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		mContext=getActivity();
		mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_collect_news, container, false);
		uuid = getArguments().getString("uid");
//		obtainData();
		return mBinding.getRoot();
	}
	
	public void obtainData(){
		//获取数据
		long time=System.currentTimeMillis();
		String validate= (StringUtil.isEmpty(uuid) ? MyApplication.getUserData().uid : uuid)+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid", StringUtil.isEmpty(uuid) ? MyApplication.getUserData().uid : uuid);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		UserService.Api service = retrofit.create(UserService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<CollectNewsRespBean> call = service.getCollectNews(body);
		call.enqueue(new Callback<CollectNewsRespBean>() {
			@Override
			public void onResponse(Call<CollectNewsRespBean> call, Response<CollectNewsRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
//				listData=response.body().getData().getList();
				initData(response.body());
			}

			@Override
			public void onFailure(Call<CollectNewsRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
			}
		});
	}
	
	public void initData(CollectNewsRespBean dateBean){
		if(dateBean==null||dateBean.getData()==null||dateBean.getData().getList()==null||dateBean.getData().getList().size()==0){
			mBinding.tvTip.setVisibility(View.VISIBLE);
			return;
		}
		listData=dateBean.getData().getList();
		if(listData.size()>0){
			adapter = new MyAdapter(mContext,listData);
			mBinding.removeRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
			mBinding.removeRecyclerview.setAdapter(adapter);
			mBinding.removeRecyclerview.setOnItemClickListener(new OnItemDelClickListener() {
				@Override
				public void onItemClick(View view, int position) {
					NewsBean news =listData.get(position);
					Intent intent = new Intent(mContext, NewsActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("newsBean",news);
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

		String arcurl = listData.get(position).getArcurl();
		int showtype=0;
		long time=System.currentTimeMillis();
		String validate= ""+(StringUtil.isEmpty(uuid) ? MyApplication.getUserData().uid : uuid)+arcurl+0+showtype+2+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid", StringUtil.isEmpty(uuid) ? MyApplication.getUserData().uid : uuid);
			obj.put("arcurl",arcurl);
			obj.put("f_sid",0);
			obj.put("showtype",showtype);//展示类型:1新闻2攻略3游戏4手游5杂谈6评测7原创8安利9礼包(删除收藏时可传0)
			obj.put("act",2);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		UserService.Api service = retrofit.create(UserService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<ResponseBody> call = service.setArcFavorite(body);
		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
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
	
	public class MyAdapter extends RecyclerView.Adapter {
		private Context mContext;
		private LayoutInflater mInflater;
		private List<NewsBean> mList;

		public MyAdapter(Context context, List<NewsBean> list) {
			mContext = context;
			mInflater = LayoutInflater.from(context);
			mList = list;
		}

		@Override
		public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			return new DelViewHolder(mContext,mInflater.inflate(R.layout.item_collect_news, parent, false));
		}

		@Override
		public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
			DelViewHolder viewHolder = (DelViewHolder) holder;
			viewHolder.setText(R.id.txtTitle,mList.get(position).getTitle());
			viewHolder.setText(R.id.txtTime, TimeUtil.getFoolishTime2(mList.get(position).getPubdate_at()+"000"));
			viewHolder.setText(R.id.txtComment,mList.get(position).getTotal_ct()+"");
			viewHolder.setImageUrl(R.id.imgArrayOne,mList.get(position).getLitpic());
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