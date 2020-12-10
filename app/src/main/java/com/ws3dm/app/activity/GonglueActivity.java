package com.ws3dm.app.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.BaseRecyclerAdapter;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.databinding.AcGonglueBinding;
import com.ws3dm.app.mvp.model.RespBean.NewsSpecilReslRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.NewsService;
import com.ws3dm.app.util.AppUtil;
import com.ws3dm.app.util.SoftKeyboardStateHelper;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;
import com.ws3dm.app.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Describution : 全部攻略，单个游戏攻略列表最下方的全部攻略跳转
 * 
 * Author : DKjuan
 * 
 * Date : 2019/9/28 10:53
 **/
public class GonglueActivity extends BaseActivity{

	private AcGonglueBinding mBinding;
	private CommonRecyclerAdapter mAdapter;
	private BaseRecyclerAdapter<NewsBean> mSubRecyclerAdapter,resultAdapter;
	public List<NewsBean> listData =new ArrayList<>();
	private int totalSize,mPage,showtype,aid,isAll=0,isSelf;//17单机18手游19网游   isAll是否为所有攻略0不是1是   isSelf判断是不是自己跳转
	private String title,searchTxt="";

	@Override
	protected void init() {
		mBinding = bindView(R.layout.ac_gonglue);
		mBinding.setHandler(this);

		initView();
	}

	//界面初始化
	private void initView() {//17单机18手游19网游
		title=getIntent().getStringExtra("title");
		showtype=getIntent().getIntExtra("showtype",0);
		aid=getIntent().getIntExtra("aid",0);
		isAll=getIntent().getIntExtra("isAll",0);
		if(isAll==0){
			searchTxt=getIntent().getStringExtra("searchTxt");
		}
		isSelf=getIntent().getIntExtra("isSelf",0);
		if(isSelf==1){
			mBinding.tvTitle.setText("搜索结果");
			mBinding.imgSearch.setVisibility(View.GONE);
			mBinding.llBottom.setVisibility(View.GONE);
		}else{
			mBinding.tvTitle.setText(title);
		}
		initSearchView();

		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.recyclerview.setLayoutManager(layoutManager);

		mBinding.recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
		mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);

		mAdapter = new CommonRecyclerAdapter<NewsBean>(mContext, R.layout.adapter_news_one_right_pic) {
			@Override
			public void bindData(RecyclerViewHolder holder, final int position, final NewsBean item) {
				holder.setText(R.id.txtTitle, item.getTitle());
				holder.setText(R.id.txtComment, item.getTotal_ct()+"");
				holder.setImageByUrl(R.id.imgArrayOne,item.getLitpic());
				holder.setText(R.id.tv_time_game, TimeUtil.getTimeEN(item.getPubdate_at()));
				holder.getView(R.id.tv_time_game).setVisibility(View.VISIBLE);
				holder.getView(R.id.ll_user).setVisibility(View.GONE);
			}
		};
		mAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int position) {
				NewsBean news = (NewsBean) mAdapter.getDataByPosition(position);
				Intent intent = new Intent(mContext, NewsActivity.class);
				intent.putExtra("isGongLue",1);
				Bundle bundle = new Bundle();
				bundle.putSerializable("newsBean", news);
				intent.putExtras(bundle);
				mContext.startActivity(intent);
			}
		});

		mBinding.recyclerview.setAdapter(mAdapter);
		mBinding.recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
			@Override
			public void onRefresh() {
				new Handler().postDelayed(new Runnable(){
					public void run() {
						mPage=1;
						if(isAll==1){
							obtainAllData();
						}else{
							obtainSearchData();
						}
					}
				}, 50);            //refresh data here
			}

			@Override
			public void onLoadMore() {
				if(isAll==1){
					obtainAllData();
				}else{
					obtainSearchData();
				}
			}
		});
		mBinding.recyclerview.setRefreshing(true);
	}

	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	public void clickHandler(View view) {
		switch (view.getId()) {
			case R.id.imgReturn:// 返回
				onBackPressed();
				break;
		}
	}

	public void obtainAllData(){//获取所有攻略列表
		//获取数据
		long time=System.currentTimeMillis();
		String validate= ""+showtype+aid+10+mPage+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("showtype", showtype);
			obj.put("aid", aid);
			obj.put("pagesize", 10);
			obj.put("page", mPage);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
//        mForumPresenter.getForumRank(obj.toString());
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		NewsService.Api service = retrofit.create(NewsService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<NewsSpecilReslRespBean> call = service.getGLSpecialList(body);
		call.enqueue(new Callback<NewsSpecilReslRespBean>() {
			@Override
			public void onResponse(Call<NewsSpecilReslRespBean> call, Response<NewsSpecilReslRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				initData(response.body());
			}

			@Override
			public void onFailure(Call<NewsSpecilReslRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
				mBinding.recyclerview.loadMoreError();
			}
		});
	}

	private void initData(NewsSpecilReslRespBean bean) {
		mBinding.recyclerview.refreshComplete();
		if(bean!=null&&bean.getData()!=null) {
			listData = bean.getData().getList();
		}
		if(mPage>1) {
			mAdapter.appendList(listData);
		}else {
			totalSize=bean.getData().getTotal();
			mAdapter.clearAndAddList(listData);
		}
		if(mAdapter.getItemCount()==totalSize){
			mBinding.recyclerview.setNoMore(true);
		}else{
			mPage++;
		}
	}

	public void obtainSearchData(){//获取小分类攻略列表
		//获取数据
		long time=System.currentTimeMillis();
		String validate= ""+showtype+aid+10+mPage+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("showtype", showtype);
			obj.put("aid", aid);
			obj.put("pagesize", 10);
			obj.put("page", mPage);
			obj.put("title", searchTxt);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
//        mForumPresenter.getForumRank(obj.toString());
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		NewsService.Api service = retrofit.create(NewsService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<NewsSpecilReslRespBean> call = service.getSpecialGLso(body);
		call.enqueue(new Callback<NewsSpecilReslRespBean>() {
			@Override
			public void onResponse(Call<NewsSpecilReslRespBean> call, Response<NewsSpecilReslRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				initSearchData(response.body());
			}

			@Override
			public void onFailure(Call<NewsSpecilReslRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
				mBinding.recyclerview.loadMoreError();
			}
		});
	}

	private void initSearchData(NewsSpecilReslRespBean bean) {
		mBinding.recyclerview.refreshComplete();
		if(bean!=null&&bean.getData()!=null) {
			listData = bean.getData().getList();
		}
		if(mPage>1) {
			mAdapter.appendList(listData);
		}else {
			totalSize=bean.getData().getTotal();
			mAdapter.clearAndAddList(listData);
		}
		if(mAdapter.getItemCount()==totalSize){
			mBinding.recyclerview.setNoMore(true);
		}else{
			mPage++;
		}
	}

	private void initSearchView(){
		mBinding.imgSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mBinding.searchWord.requestFocus();
				AppUtil.openKeyboard(mContext,mBinding.searchWord);
				mBinding.llBottom.setVisibility(View.VISIBLE);
				mBinding.viewShade.setVisibility(View.VISIBLE);
				mBinding.imgSearch.setVisibility(View.GONE);
			}
		});
		mBinding.txtSend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AppUtil.closeKeyboard(mContext);
				mBinding.llBottom.setVisibility(View.GONE);
				mBinding.imgSearch.setVisibility(View.VISIBLE);
				mBinding.viewShade.setVisibility(View.GONE);
				mBinding.searchWord.setText("");
			}
		});
		mBinding.viewShade.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mBinding.txtSend.performClick();
			}
		});
		mBinding.searchWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId== EditorInfo.IME_ACTION_SEARCH){
					if(mBinding.searchWord.getText().toString().length()>0){
						AppUtil.closeKeyboard(mContext);
						Intent intent = new Intent(mContext, GonglueActivity.class);
						intent.putExtra("aid", aid);
						intent.putExtra("showtype", showtype);
						intent.putExtra("title", title);
						intent.putExtra("searchTxt", mBinding.searchWord.getText().toString());
						intent.putExtra("isSelf", 1);
						startActivity(intent);
						mBinding.txtSend.performClick();
					}else{
						ToastUtil.showToast(mContext,"搜索内容不能为空！");
					}
				}
				return false;
			}
		});

		setListenerForEditText(mBinding.searchWord);
	}

	private void setListenerForEditText(View view){
		SoftKeyboardStateHelper softKeyboardStateHelper = new SoftKeyboardStateHelper(view);
		softKeyboardStateHelper.addSoftKeyboardStateListener(new SoftKeyboardStateHelper.SoftKeyboardStateListener() {
			@Override
			public void onSoftKeyboardOpened(int keyboardHeightInPx) {
				//键盘打开
//				int ss=1;
			}

			@Override
			public void onSoftKeyboardClosed() {
				//键盘关闭
				mBinding.txtSend.performClick();
			}
		});
	}
}