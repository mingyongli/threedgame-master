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

import com.alibaba.fastjson.JSON;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.GameHomeActivity;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.bean.CollectGamebean;
import com.ws3dm.app.bean.GameBean;
import com.ws3dm.app.databinding.FgCollectNewsBinding;
import com.ws3dm.app.mvp.model.RespBean.CollectGameRespBean;
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
public class FragmentCollectGames extends BaseFragment {
	private FgCollectNewsBinding mBinding;
	private List<CollectGamebean> listData=new ArrayList<CollectGamebean>();
	private int collectType =1,f_sid=0,clickPosition=0;//collectTYpe 1单机 2网游 3手游
	private RecyclerView.Adapter adapter;
	private String uid;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_collect_news, container, false);
		uid = getArguments().getString("uid");
////		obtainData();
		mBinding.llChoice.setVisibility(View.VISIBLE);
		mBinding.btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setButton(1);
			}
		});
		mBinding.btn2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setButton(2);
			}
		});
		mBinding.btn3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setButton(3);
			}
		});
		setButton(1);
		
		return mBinding.getRoot();
	}

	public void setButton(int select) {
		if(collectType ==select)
			return;
		collectType = select;
		switch (select) {//1单机 2网游 3手游
			case 1:
				mBinding.btn1.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_gray));
				mBinding.btn2.setBackground(null);
				mBinding.btn3.setBackground(null);
				break;
			case 2:
				mBinding.btn1.setBackground(null);
				mBinding.btn2.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_gray));
				mBinding.btn3.setBackground(null);
				break;
			case 3:
				mBinding.btn1.setBackground(null);
				mBinding.btn2.setBackground(null);
				mBinding.btn3.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_gray));
				break;
		}
		listData.clear();
		if(adapter!=null){
			adapter.notifyDataSetChanged();
		}
		setRecy();
		obtainData();
	}

	private void setRecy(){
		if(collectType ==1)
			adapter = new MyPCAdapter(mContext, listData);
		else if(collectType ==2)
			adapter = new MyNetAdapter(mContext, listData);
		else
			adapter = new MyMGAdapter(mContext, listData);
		mBinding.removeRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
		mBinding.removeRecyclerview.setAdapter(adapter);
		mBinding.removeRecyclerview.setOnItemClickListener(new OnItemDelClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				Intent intent = new Intent(mContext, GameHomeActivity.class);
				Bundle bundle = new Bundle();
				CollectGamebean tempGame=listData.get(position);
				bundle.putString("str_game", JSON.toJSONString(tempGame));
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
	}
	
	public void obtainData(){
		//获取数据
		long time=System.currentTimeMillis();
		String validate= (StringUtil.isEmpty(uid) ? MyApplication.getUserData().uid : uid)+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid", StringUtil.isEmpty(uid) ? MyApplication.getUserData().uid : uid);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		UserService.Api service = retrofit.create(UserService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<CollectGameRespBean> call = collectType==1?service.getCollectGame(body):collectType==2?service.getCollectNet(body):service.getCollectSY(body);
		call.enqueue(new Callback<CollectGameRespBean>() {
			@Override
			public void onResponse(Call<CollectGameRespBean> call, Response<CollectGameRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
//				listData=response.body().getData().getList();
				initData(response.body());
			}

			@Override
			public void onFailure(Call<CollectGameRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
			}
		});
	}
	
	public void initData(CollectGameRespBean dateBean){
		if(dateBean==null||dateBean.getData()==null||dateBean.getData().getList()==null||dateBean.getData().getList().size()==0){
			mBinding.tvTip.setVisibility(View.VISIBLE);
			return;
		}else{
			mBinding.tvTip.setVisibility(View.GONE);
		}
		listData=dateBean.getData().getList();
		if(listData.size()>0){
			setRecy();
			mBinding.removeRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
			mBinding.removeRecyclerview.setAdapter(adapter);
			mBinding.removeRecyclerview.setOnItemClickListener(new OnItemDelClickListener() {
				@Override
				public void onItemClick(View view, int position) {
					Intent intent = new Intent(mContext, GameHomeActivity.class);
					Bundle bundle = new Bundle();
//					bundle.putSerializable("game", listData.get(position));
//					bundle.putSerializable("game", finalGame);//数据太大，超出系统限制
					CollectGamebean originalBean=listData.get(position);
					GameBean mGame=new GameBean();
					mGame.setAid(originalBean.getAid());
					mGame.setArcurl(originalBean.getArcurl());
					mGame.setShowtype(originalBean.getShowtype());
					bundle.putString("str_game", JSON.toJSONString(mGame));
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
		String validate= ""+(StringUtil.isEmpty(uid) ? MyApplication.getUserData().uid : uid)+arcurl+0+showtype+2+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid", StringUtil.isEmpty(uid) ? MyApplication.getUserData().uid : uid);
			obj.put("arcurl",arcurl);
			obj.put("f_sid",f_sid);
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
						listData.remove(clickPosition);
						adapter.notifyDataSetChanged();
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

	public class MyPCAdapter extends RecyclerView.Adapter {
		private Context mContext;
		private LayoutInflater mInflater;
		private List<CollectGamebean> mList;

		public MyPCAdapter(Context context, List<CollectGamebean> list) {
			mContext = context;
			mInflater = LayoutInflater.from(context);
			mList = list;
		}

		@Override
		public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			return new DelViewHolder(mContext,mInflater.inflate(R.layout.item_collect_game, parent, false));
		}

		@Override
		public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
			DelViewHolder viewHolder = (DelViewHolder) holder;
			CollectGamebean game=mList.get(position);
			
			viewHolder.setImageUrl(R.id.imgCover,game.getLitpic());
			viewHolder.setText(R.id.tv_score, game.getScore()+"");
			viewHolder.setText(R.id.txtName, game.getTitle());
			viewHolder.setText(R.id.tv_type, "类型：" +game.getType());
			viewHolder.setText(R.id.txtTime, "发售："+ (game.getPubdate_at()==0?"未知":TimeUtil.getFormatTimeSimple(game.getPubdate_at())));
			viewHolder.setText(R.id.tv_label, "标签：" +game.getLabels());

			viewHolder.getView(R.id.txt_label1).setVisibility(View.GONE);
			viewHolder.getView(R.id.txt_label2).setVisibility(View.GONE);
			viewHolder.getView(R.id.txt_label3).setVisibility(View.GONE);
			viewHolder.getView(R.id.txt_label4).setVisibility(View.GONE);
			viewHolder.getView(R.id.txt_label5).setVisibility(View.GONE);
			String[] sy=game.getSystem().contains("/")?game.getSystem().split("/"):game.getSystem().split(" ");
			switch (sy.length>5?5:sy.length) {
				case 5:
					viewHolder.getView(R.id.txt_label5).setVisibility(View.VISIBLE);
				case 4:
					viewHolder.getView(R.id.txt_label4).setVisibility(View.VISIBLE);
					viewHolder.setText(R.id.txt_label4,sy[3]);
				case 3:
					viewHolder.getView(R.id.txt_label3).setVisibility(View.VISIBLE);
					viewHolder.setText(R.id.txt_label3,sy[2]);
				case 2:
					viewHolder.getView(R.id.txt_label2).setVisibility(View.VISIBLE);
					viewHolder.setText(R.id.txt_label2,sy[1]);
				case 1:
					viewHolder.getView(R.id.txt_label1).setVisibility(View.VISIBLE);
					viewHolder.setText(R.id.txt_label1,sy[0]);
					break;
			}
		}

		public int getItemCount() {
			return mList != null ? mList.size() : 0;
		}

		public void removeItem(int position) {
			mList.remove(position);
			notifyItemRemoved(position);
		}
	}

	public class MyNetAdapter extends RecyclerView.Adapter {
		private Context mContext;
		private LayoutInflater mInflater;
		private List<CollectGamebean> mList;

		public MyNetAdapter(Context context, List<CollectGamebean> list) {
			mContext = context;
			mInflater = LayoutInflater.from(context);
			mList = list;
		}

		@Override
		public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			return new DelViewHolder(mContext,mInflater.inflate(R.layout.item_collect_game, parent, false));
		}

		@Override
		public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
			DelViewHolder viewHolder = (DelViewHolder) holder;
			CollectGamebean game=mList.get(position);
			
			viewHolder.setImageUrl(R.id.imgCover,game.getLitpic());
			viewHolder.setText(R.id.tv_score, game.getScore()+"");
			viewHolder.setText(R.id.txtName, game.getTitle());
			viewHolder.setText(R.id.tv_type, "类型：" +game.getType());
			viewHolder.setText(R.id.tv_plan, "运营：" +game.getFirm());
			viewHolder.setText(R.id.txtTime, "公测："+ game.getBetatime());
			viewHolder.setText(R.id.tv_label, "标签：" +(StringUtil.isEmpty(game.getLabels())?"待补充":game.getLabels()));

		}

		public int getItemCount() {
			return mList != null ? mList.size() : 0;
		}

		public void removeItem(int position) {
			mList.remove(position);
			notifyItemRemoved(position);
		}
	}

	public class MyMGAdapter extends RecyclerView.Adapter {
		private Context mContext;
		private LayoutInflater mInflater;
		private List<CollectGamebean> mList;

		public MyMGAdapter(Context context, List<CollectGamebean> list) {
			mContext = context;
			mInflater = LayoutInflater.from(context);
			mList = list;
		}

		@Override
		public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			return new DelViewHolder(mContext,mInflater.inflate(R.layout.item_collect_gamemg, parent, false));
		}

		@Override
		public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
			DelViewHolder viewHolder = (DelViewHolder) holder;
			CollectGamebean game=mList.get(position);
			
			viewHolder.setImageUrl(R.id.imgCover,game.getLitpic());
			viewHolder.setText(R.id.txtName, game.getTitle());
			viewHolder.setText(R.id.tv_type,"类型："+game.getType());
			viewHolder.setText(R.id.tv_label,"平台："+(StringUtil.isEmpty(game.getSystem())?"待补充":game.getSystem()));
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