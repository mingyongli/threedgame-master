package com.ws3dm.app.fragment;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.ForgetPassActivity;
import com.ws3dm.app.activity.ImageActivity;
import com.ws3dm.app.activity.LoginActivity;
import com.ws3dm.app.activity.MGDetailActivity;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.MGMultiAdapter;
import com.ws3dm.app.adapter.MGMultiItemTypeSupport;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.MGListBean;
import com.ws3dm.app.bean.SoftGameBean;
import com.ws3dm.app.databinding.FgMgInfoBinding;
import com.ws3dm.app.mvp.model.RespBean.MGDetailRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.MGService;
import com.ws3dm.app.network.service.UserService;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.util.glide.GlideUtil;
import com.ws3dm.app.view.SharePopupWindow;

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
 * Describution :手游详情界面
 * 
 * Author : DKjuan
 * 
 * Date : 2018/4/26 14:33
 **/
public class FragmentMGInfo extends BaseFragment {	
	private FgMgInfoBinding mBinding;
	private CommonRecyclerAdapter<MGListBean> mRecyclerAdapter;
	private MGMultiAdapter mAdapter;
	private CommonRecyclerAdapter<String> horizonalAdapter;
	private SoftGameBean mSoftGame;
	private int f_sid,favorite=2;//favorite:1已收藏，2未收藏
	private SharePopupWindow pop;
	private MGDetailRespBean mMGDetailRespBean;
	public List<MGListBean> listData = new ArrayList<MGListBean>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		AppCompatActivity activity = (AppCompatActivity) getActivity();

		mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_mg_info, container, false);
		mBinding.setHandler(this);

		initView();
		return mBinding.getRoot();
	}
	
	public void initView(){
		mSoftGame= (SoftGameBean) getArguments().getSerializable("mSoftGame");
//		newsCollectFile = new NewsFile(getActivity());
//		type= (String) getArguments().get("type");
//		if(type!=null&&type.equals("GameDetailActivity_")){
//			aid=getArguments().getInt("aid");
//			dataType=getArguments().getString("dataType");
//		}

		//图片listview
		LinearLayoutManager horizonalManager = new LinearLayoutManager(mContext);
		horizonalManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		mBinding.imgRecyclerview.setLayoutManager(horizonalManager);
//		horizonalAdapter= new CommonRecyclerAdapter<String>(mContext, R.layout.item_game_imageshow) {
		horizonalAdapter= new CommonRecyclerAdapter<String>(mContext, R.layout.item_game_imagehorizon) {
			@Override
			public void bindData(RecyclerViewHolder holder, int position, final String url) {
//				holder.setImageByUrl(R.id.img_game,url);
//				Glide.with(mContext).load(url).asBitmap().centerCrop().placeholder(R.drawable.load_default).into((ImageView) holder.getView(R.id.img_game));
				Glide.with(mContext).load(url).placeholder(R.drawable.load_default).dontAnimate().into((ImageView) holder.getView(R.id.img_game));
			}
		};
		horizonalAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int position) {
//				Intent intent=new Intent(mContext,ImageActivity.class);
//				intent.putExtra("imgUrl",horizonalAdapter.getDataByPosition(position));
//				startActivity(intent);
				Intent intent=new Intent(mContext,ImageActivity.class);
				intent.putExtra("position",""+position);
				intent.putExtra("title",position);
				intent.putStringArrayListExtra("url",new ArrayList<>(mMGDetailRespBean.getData().getImgs()));
				startActivity(intent);
			}
		});
		mBinding.imgRecyclerview.setAdapter(horizonalAdapter);
		mBinding.imgRecyclerview.setNestedScrollingEnabled(false);
		
		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.recyclerview.setLayoutManager(layoutManager);

		mBinding.recyclerview.setLoadingMoreEnabled(false);
		mBinding.recyclerview.setPullRefreshEnabled(false);
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);原来就注释掉的            y
//		mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
//		mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);


//		//添加分割线
//		if (mListV3DividerItemDecoration == null) {
//			mListV3DividerItemDecoration = new DividerItemDecorationAdapter(mContext);
//		} else {
//			mBinding.recyclerview.removeItemDecoration(mListV3DividerItemDecoration);
//		}
//		mBinding.recyclerview.addItemDecoration(mListV3DividerItemDecoration);

//		mRecyclerAdapter = new CommonRecyclerAdapter<MGListBean>(mContext, R.layout.adapter_hot_cate) {
//			@Override
//			public void bindData(RecyclerViewHolder holder, final int position, final MGListBean item) {
//				holder.setText(R.id.txt_cate_name, item.getType());
//
//				RecyclerView recyclerView = holder.getView(R.id.recycler_hot_cates);
//				LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
//				layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//				recyclerView.setLayoutManager(layoutManager);
//				recyclerView.setNestedScrollingEnabled(false);
//
//				final CommonRecyclerAdapter<SoftGameBean> mSubRecyclerAdapter= new CommonRecyclerAdapter<SoftGameBean>(mContext, R.layout.item_game_horizon) {
//					@Override
//					public void bindData(RecyclerViewHolder holder, int position, final SoftGameBean item) {
//						holder.setImageByUrl(R.id.imgCover,item.getLitpic());
//						holder.setText(R.id.tv_title,item.getTitle());
//						holder.setText(R.id.tv_data,"v"+item.getSoft_ver()+" | "+item.getSoft_size());
//						holder.setText(R.id.tv_info,item.getDesc());
//						holder.getView(R.id.bt_download).setOnClickListener(new View.OnClickListener() {
//							@Override
//							public void onClick(View view) {
////								DownloadUtil.down(mContext,item.getDownurl(),item.getTitle()+"|"+item.getLitpic());
//								Intent intent = new Intent(mContext, MGDetailActivity.class);
//								Bundle bundle = new Bundle();
//								bundle.putSerializable("mSoftGameBean", item);
//								intent.putExtras(bundle);
//								mContext.startActivity(intent);
//								getActivity().finish();
//							}
//						});
//					}
//				};
//				mSubRecyclerAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
//					@Override
//					public void onItemClick(View itemView, int posi) {
//						Intent intent = new Intent(mContext, MGDetailActivity.class);
//						Bundle bundle = new Bundle();
//						bundle.putSerializable("mSoftGameBean", mSubRecyclerAdapter.getDataByPosition(posi));
//						intent.putExtras(bundle);
//						mContext.startActivity(intent);
//						getActivity().finish();
//					}
//				});
//				recyclerView.setAdapter(mSubRecyclerAdapter);
//				mSubRecyclerAdapter.clearAndAddList(item.getSoftlist());
//			}
//		};
//
//		mRecyclerAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
//			@Override
//			public void onItemClick(View itemView, int position) {
////				news.setSenddate(TimeUtil.dateDayNow());
////				newsCollectFile.addHistory(news);
//			}
//		});

		MGMultiItemTypeSupport modelMultiItemTypeSupport = new MGMultiItemTypeSupport();
		mAdapter= new MGMultiAdapter(mContext, modelMultiItemTypeSupport);
		
//		mBinding.recyclerview.setAdapter(mRecyclerAdapter);
		mBinding.recyclerview.setAdapter(mAdapter);
		mBinding.recyclerview.setNestedScrollingEnabled(false);
		
		mBinding.btnDown.setOnClickListener(new View.OnClickListener() {
			@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
			@Override
			public void onClick(View v) {
				mBinding.btnDown.setOnClickListener(null);
				mBinding.btnDown.setTextColor(getResources().getColor(R.color.gray_text));
				mBinding.btnDown.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_fillgreen2));
//				DownloadUtil.down(mContext,mSoftGame.getDownurl(),mSoftGame.getTitle()+"|"+mSoftGame.getLitpic());
			}
		});
	}

	protected void onFragmentFirstVisible(){
		obtainData();
		getFavoriteStute();
	}

	public void obtainData(){//获取游戏详情
		//获取数据
		long time=System.currentTimeMillis();
		String validate= ""+MyApplication.getUserData().uid+mSoftGame.getAid()+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid", MyApplication.getUserData().uid);
			obj.put("aid", mSoftGame.getAid());
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		MGService.Api service = retrofit.create(MGService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<MGDetailRespBean> call = service.getDetailMG(body);
		call.enqueue(new Callback<MGDetailRespBean>() {
			@Override
			public void onResponse(Call<MGDetailRespBean> call, Response<MGDetailRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				initData(response.body());
			}

			@Override
			public void onFailure(Call<MGDetailRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
				mBinding.recyclerview.loadMoreError();
			}
		});
	}

	public void getFavoriteStute(){//获取收藏状态
		String temp = MyApplication.getUserData().loginStatue ? MyApplication.getUserData().uid : "0";
		int uid=Integer.parseInt(temp);
		String arcurl = mSoftGame.getArcurl();
		long time=System.currentTimeMillis();
		String validate= ""+uid+arcurl+f_sid+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid", MyApplication.getUserData().uid);
			obj.put("arcurl",arcurl);
			obj.put("f_sid",f_sid);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		UserService.Api service = retrofit.create(UserService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<ResponseBody> call = service.getArcFavorite(body);
		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				try {
					JSONObject jsonObject=new JSONObject(response.body().string());
					if(jsonObject.optInt("code")==1){
						JSONObject jsonSub=new JSONObject(jsonObject.opt("data").toString());
						f_sid=jsonSub.optInt("f_sid");
						if(jsonSub.optInt("favorite")==1){//收藏状态1已收藏0未收藏
							mBinding.imgCollect.setImageResource(R.drawable.collect);
							favorite=1;
						}else{
							mBinding.imgCollect.setImageResource(R.drawable.click_collect);
							favorite=2;
						}
//					}else{
//						ToastUtil.showToast(mContext,jsonObject.opt("msg")+"");
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

	public void addDelFavorite(int isAdd){//1添加2删除
		String temp = MyApplication.getUserData().loginStatue ? MyApplication.getUserData().uid : "0";
		int uid=Integer.parseInt(temp);
		String arcurl = mSoftGame.getArcurl();
		int showtype=isAdd==2?0:4;
		long time=System.currentTimeMillis();
		String validate= ""+uid+arcurl+f_sid+showtype+isAdd+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid", MyApplication.getUserData().uid);
			obj.put("arcurl",arcurl);
			obj.put("f_sid",f_sid);
			obj.put("showtype",showtype);//展示类型:1新闻2攻略3游戏4手游5杂谈6评测7原创8安利9礼包(删除收藏时可传0)
			obj.put("act",isAdd);
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
					String jsonString=response.body().string();
					JSONObject jsonObject=new JSONObject(jsonString);
					if(jsonObject.optInt("code")==1){
						JSONObject jsonSub=new JSONObject(jsonObject.opt("data").toString());
						f_sid=jsonSub.optInt("code");
						favorite=3-favorite;
						if(favorite==1){
							mBinding.imgCollect.setImageResource(R.drawable.collect);
							ToastUtil.showToast(mContext, "收藏成功！");
						}else{
							mBinding.imgCollect.setImageResource(R.drawable.click_collect);
							ToastUtil.showToast(mContext, "取消成功！");
						}
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
	
	private void initData(MGDetailRespBean bean) {
		if(bean==null||bean.getData()==null){
			return;
		}
		mMGDetailRespBean=bean;
		mBinding.tvInfo.setText("        "+bean.getData().getContent());
//		mBinding.tvInLanguage.setText("语言："+bean.getData().getLanguage());
//		mBinding.tvInVersion.setText("版本："+bean.getData().getSoft_ver());
//		mBinding.tvInType.setText("类型："+bean.getData().getType());
//		mBinding.tvInSize.setText("大小："+bean.getData().getSoft_size());
		MGDetailActivity.mLanguage.setText("语言："+bean.getData().getLanguage());
		MGDetailActivity.mType.setText("类型："+bean.getData().getType());
		MGDetailActivity.mVersion.setText("版本："+" v"+bean.getData().getSoft_ver());
		MGDetailActivity.mSize.setText("大小："+bean.getData().getSoft_size()+"");
		GlideUtil.loadRoundImage(mContext,bean.getData().getLitpic(),MGDetailActivity.mImage,5);
		
		MGDetailActivity.mSoftGame.setLitpic(bean.getData().getLitpic()+"");

		if(StringUtil.isEmpty(mSoftGame.getLitpic()))
			pop = new SharePopupWindow(mContext,mSoftGame.getArcurl(), mSoftGame.getTitle(), bean.getData().getContent(), mBinding.loadViewShare);
		else
			pop = new SharePopupWindow(mContext,mSoftGame.getArcurl(), mSoftGame.getTitle(), bean.getData().getContent(), mBinding.loadViewShare,mSoftGame.getLitpic());

		horizonalAdapter.clearAndAddList(bean.getData().getImgs());
//		mRecyclerAdapter.clearAndAddList(bean.getData().getOthergames());
		listData.clear();
		listData.addAll(bean.getData().getOthergames());
		listData.get(0).setShow(1);
		listData.get(0).setImgnum(1);
		mAdapter.clearAndAddList(listData);
		mBinding.scrollMgInfo.fullScroll(ScrollView.FOCUS_UP);
	}

	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	public void clickHandler(View view) {
		switch (view.getId()) {
			case R.id.img_share://分享
				if (mSoftGame != null)       //临时注释 暂时不分享
					pop.show();
				else
					ToastUtil.showToast(mContext,"网络异常");
				break;
//			case R.id.btn_down:
//				mBinding.btnDown.setOnClickListener(null);
//				mBinding.btnDown.setTextColor(getResources().getColor(R.color.gray_text));
//				mBinding.btnDown.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_fillgreen2));
//				DownloadUtil.down(mContext,mSoftGame.getDownurl(),mSoftGame.getTitle()+"|"+mSoftGame.getLitpic());
//				break;
			case R.id.img_collect:// 收藏
				if(MyApplication.getUserData()==null||!MyApplication.getUserData().loginStatue){
					ToastUtil.showToast(mContext,"请先登录！");
					startActivity(new Intent(mContext, LoginActivity.class));
				}else if(MyApplication.getUserData().mobile.length()==0){//第三方登陆，非绑定跳转绑定界面
					Intent intent=new Intent(mContext, ForgetPassActivity.class);
					intent.putExtra("type",1);//1,绑定手机号，2修改密码，3忘记密码
					startActivity(intent);
				}else{
					addDelFavorite(3-favorite);
				}
				break;
			
		}
	}
}