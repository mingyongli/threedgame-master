package com.ws3dm.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.bean.AddressBean;
import com.ws3dm.app.mvp.model.RespBean.AddressRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.UserService;
import com.ws3dm.app.util.DialogHelper;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.util.callback.CallBack;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Describution : 收货地址
 * 
 * Author : DKjuan
 * 
 * Date : 2018/12/5 16:36
 **/
public class AddressActivity extends BaseActivity implements View.OnClickListener{
	private CommonRecyclerAdapter<AddressBean> mAdapter;
	private LinearLayout imgReturn;
	private TextView mTitle,mTitle2;
	private XRecyclerView recyclerview;
	private int posiDefault,posiClick;//初始的选中位置，点击的位置
	public static DialogHelper dialogHelper;

	@Override
	protected void init() {
		setContentView(R.layout.ac_base_recyclerview);

		imgReturn= (LinearLayout) findViewById(R.id.imgReturn);
		mTitle= (TextView) findViewById(R.id.base_title);
		mTitle2= (TextView) findViewById(R.id.base_title2);
		recyclerview= (XRecyclerView) findViewById(R.id.recyclerview);
		imgReturn.setOnClickListener(this);
		mTitle2.setOnClickListener(this);
		
		initView();
		obtainData();
	}

	public void initView(){
		mTitle.setText("收货地址");
		mTitle2.setText("新增");

		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerview.setLayoutManager(layoutManager);

		recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
		recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);

		mAdapter = new CommonRecyclerAdapter<AddressBean>(mContext, R.layout.adapter_address) {
			@Override
			public void bindData(RecyclerViewHolder holder, final int position, final AddressBean item) {
				holder.setText(R.id.tv_name, item.getConcat()+"  "+item.getMobile());
				holder.setText(R.id.tv_address, item.getAddress());

				CheckBox add_default=holder.getView(R.id.item_default);
				if(item.getIs_default()==1){
					add_default.setChecked(true);
					posiDefault=position;
				}else{
					add_default.setChecked(false);
				}

				add_default.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						posiClick=position;
						setDefault(item);
					}
				});
				
				holder.getView(R.id.tv_edit).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext, AddPlaceActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("place",item);
						intent.putExtras(bundle);
						startActivity(intent);
					}
				});

				holder.getView(R.id.tv_del).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialogHelper = new DialogHelper(mContext, new CallBack() {
							@Override
							public void callBackFunction() {
								// TODO Auto-generated method stub
								if(dialogHelper.isConfirm()){
									delAddress(item);
								}
							}
						});
						dialogHelper.showDialog("提示", "确定要删除此条地址吗?", false);
					}
				});
			}
		};
//		mAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
//			@Override
//			public void onItemClick(View itemView, int position) {
////				NewsBean news = mAdapter.getDataByPosition(position);
////				Intent intent = new Intent(mContext, NewsActivity.class);
////				Bundle bundle = new Bundle();
////				bundle.putSerializable("newsBean",news);
////				intent.putExtras(bundle);
////				startActivity(intent);
//			}
//		});
		recyclerview.setAdapter(mAdapter);
		recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
			@Override
			public void onRefresh() {
				new Handler().postDelayed(new Runnable(){
					public void run() {
						recyclerview.refreshComplete();
						obtainData();
					}
				}, 50);            //refresh data here
			}

			@Override
			public void onLoadMore() {
			}
		});
	}

	public void obtainData(){//获取地址列表
		//获取数据
		long time=System.currentTimeMillis();
		String validate= ""+ MyApplication.getUserData().uid+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid", MyApplication.getUserData().uid);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//同步请求
		Retrofit retrofit= RetrofitFactory.getNewRetrofit(0);
		UserService.Api service=retrofit.create(UserService.Api.class);
		RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),obj.toString());
		Call<AddressRespBean> call=service.getAddress(body);
		call.enqueue(new Callback<AddressRespBean>() {
			@Override
			public void onResponse(Call<AddressRespBean> call, Response<AddressRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				initData(response.body());
			}

			@Override
			public void onFailure(Call<AddressRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
				recyclerview.loadMoreError();
			}
		});
	}
	
	public void initData(AddressRespBean bean){
		recyclerview.setNoMore(true);
		mAdapter.clearAndAddList(bean.getData().getList());
	}
	
	public void delAddress(AddressBean item){
		long time=System.currentTimeMillis();
		String validate=MyApplication.getUserData().uid+item.getId()+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid", MyApplication.getUserData().uid);
			obj.put("id", item.getId());
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		UserService.Api service = retrofit.create(UserService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<ResponseBody> call = service.delAddress(body);
		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				try {
					String jsonString=response.body().string();
					JSONObject jsonObject=new JSONObject(jsonString);
					if(jsonObject.optInt("code")==1){
						ToastUtil.showToast(mContext,"删除成功！");
						onResume();
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
	
	public void setDefault(AddressBean item){
		long time=System.currentTimeMillis();
		String validate=MyApplication.getUserData().uid+item.getId()+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid", MyApplication.getUserData().uid);
			obj.put("id", item.getId());
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		UserService.Api service = retrofit.create(UserService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<ResponseBody> call = service.setDefault(body);
		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				try {
					String jsonString=response.body().string();
					JSONObject jsonObject=new JSONObject(jsonString);
					if(jsonObject.optInt("code")==1){
						ToastUtil.showToast(mContext,"默认地址设置成功！");
						mAdapter.getDataByPosition(posiDefault).setIs_default(0);
						mAdapter.getDataByPosition(posiClick).setIs_default(1);
						mAdapter.notifyDataSetChanged();
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
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.imgReturn:
				onBackPressed();
				break;
			case R.id.base_title2:
				startActivity(new Intent(this, AddPlaceActivity.class));
				break;
			default:
				break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if(recyclerview!=null){
			obtainData();
		}
	}
}
