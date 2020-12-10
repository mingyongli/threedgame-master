package com.ws3dm.app.fragment;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.GameHomeActivity;
import com.ws3dm.app.activity.MGDetailActivity;
import com.ws3dm.app.activity.NewsActivity;
import com.ws3dm.app.activity.SearchActivity;
import com.ws3dm.app.adapter.BaseRecyclerAdapter;
import com.ws3dm.app.adapter.SearchMultiAdapter;
import com.ws3dm.app.adapter.SearchMultiItemTypeSupport;
import com.ws3dm.app.bean.GameBean;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.bean.SearchBean;
import com.ws3dm.app.bean.SoftGameBean;
import com.ws3dm.app.databinding.FgNewsearchBinding;
import com.ws3dm.app.mvp.model.RespBean.NewSearchRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.UserService;
import com.ws3dm.app.util.StringUtil;

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
 * Describution :搜索fragment
 * 
 * Author : DKjuan
 * 
 * Date : 2018/1/6 14:05
 **/
public class NewSearchFragment extends BaseFragment {

	private static final String TAG = "NewSearchFragment";

	private SearchMultiAdapter mAdapter;
	public List<SearchBean> listData = new ArrayList<SearchBean>();
	private FgNewsearchBinding mBinding;
	private String showCategory;//0单机，1手游 2网游
	private int mPage=1,type;//1:单机专区 7:单机新闻 8:单机攻略 9:单机原创
						// 2  10:手游应用 11:手游新闻 12:手游攻略 13:手游原创
						// 14:网游新闻 15:网游攻略 16:网游电竞 17:网游视频 18:网游评测

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_newsearch, container, false);
		mBinding.setHandler(this);
//        mBinding.MyUtilbar.setTitle(getResources().getString(R.string.person));
//        activity.setSupportActionBar(mBinding.MyUtilbar);

		initView();
		return mBinding.getRoot();
	}

	public void initView() {
		if(!StringUtil.isEmpty(getArguments().getString("showCategory"))){
			showCategory=getArguments().getString("showCategory");
			setButton(showCategory);//0,单机 1，手游 2.网游
		}

		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.recyclerview.setLayoutManager(layoutManager);

		mBinding.recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
		mBinding.recyclerview.setPullRefreshEnabled(false);
		mBinding.recyclerview.setLoadingMoreEnabled(true);

//		//添加分割线
//		if (mListV3DividerItemDecoration == null) {
//			mListV3DividerItemDecoration = new DividerItemDecorationAdapter(mContext);
//		} else {
//			mBinding.recyclerview.removeItemDecoration(mListV3DividerItemDecoration);
//		}
//		mBinding.recyclerview.addItemDecoration(mListV3DividerItemDecoration);

		SearchMultiItemTypeSupport modelMultiItemTypeSupport = new SearchMultiItemTypeSupport();
		mAdapter= new SearchMultiAdapter(mContext, modelMultiItemTypeSupport);
		mBinding.recyclerview.setAdapter(mAdapter);
		mBinding.recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
			@Override
			public void onRefresh() {
				new Handler().postDelayed(new Runnable(){
					public void run() {
						obtainData();
					}
				}, 50);            //refresh data here
			}

			@Override
			public void onLoadMore() {
				new Handler().postDelayed(new Runnable(){
					public void run() {
						obtainData();
					}
				}, 50);
			}
		});
		mAdapter.setOnClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int position) {
				SearchBean item=mAdapter.getDataByPosition(position);
				Intent intent=null;
				switch (item.getShowtype()) {
					case 4://手游应用
						SoftGameBean soft = new SoftGameBean();
						soft.setTitle(item.getTitle());
						soft.setArcurl(item.getArcurl());
						soft.setType(item.getType());
						soft.setAid(item.getAid());
						soft.setLitpic(item.getLitpic());
						intent = new Intent(mContext, MGDetailActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("mSoftGameBean", soft);
						intent.putExtras(bundle);
						mContext.startActivity(intent);
						break;
					case 3:// 专题
						GameBean game=new GameBean();
						game.setAid(item.getAid());
						game.setTitle(item.getTitle());
						game.setArcurl(item.getArcurl());
						game.setWebviewurl(item.getWebviewurl());
						game.setType(""+item.getShowtype());
						intent = new Intent(mContext, GameHomeActivity.class);
						Bundle bundle2 = new Bundle();
//						bundle2.putSerializable("game", game);
//					bundle.putSerializable("game", finalGame);//数据太大，超出系统限制
						bundle2.putString("str_game", JSON.toJSONString(game));
						intent.putExtras(bundle2);
						startActivity(intent);
						break;
					case 10:// 视频
						NewsBean news=new NewsBean();
						news.setArcurl(item.getArcurl());
						news.setWebviewurl(item.getWebviewurl());
						news.setTitle(item.getTitle());
						news.setType(item.getType());
						intent = new Intent(mContext, NewsActivity.class);
						Bundle bundle3 = new Bundle();
						bundle3.putSerializable("newsBean",news);
						intent.putExtras(bundle3);
						startActivity(intent);
						break;
					default://新闻
						NewsBean news2=new NewsBean();
						news2.setArcurl(item.getArcurl());
						news2.setWebviewurl(item.getWebviewurl());
						news2.setTitle(item.getTitle());
						news2.setType(item.getType());
						intent = new Intent(mContext, NewsActivity.class);
						Bundle bundle4 = new Bundle();
						bundle4.putSerializable("newsBean",news2);
						intent.putExtras(bundle4);
						startActivity(intent);
				}
			}
		});
	}

	public void obtainData(){
		//获取数据
		long time=System.currentTimeMillis();
		String validate= ""+type+10+mPage+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("keyword", SearchActivity.keyWord);
			obj.put("type",type);
			obj.put("pagesize",10);
			obj.put("page",mPage);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		UserService.Api service = retrofit.create(UserService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<NewSearchRespBean> call = service.allso(body);
		call.enqueue(new Callback<NewSearchRespBean>() {
			@Override
			public void onResponse(Call<NewSearchRespBean> call, Response<NewSearchRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				initData(response.body());
			}

			@Override
			public void onFailure(Call<NewSearchRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
			}
		});
	}

	private void initData(NewSearchRespBean bean) {
		mBinding.recyclerview.refreshComplete();
		if (bean != null && bean.getData() != null) {
			listData = bean.getData().getList();
		}else if(mPage==1){
			listData.clear();
			mAdapter.appendList(listData);
		}
		if (mPage > 1) {
			mAdapter.appendList(listData);
		} else {
			mAdapter.clearAndAddList(listData);
		}
		if (listData == null || listData.size() < 10) {
			mBinding.recyclerview.setNoMore(true);
		} else {
			mPage++;
		}
	}
	
	public void setButton(String showCategory) {
		switch (showCategory) {
			case "0":
				mBinding.cbSpecial.setVisibility(View.VISIBLE);
				mBinding.cbSpecial.setChecked(true);
				mBinding.cbNews.setVisibility(View.VISIBLE);
				mBinding.cbGonglue.setVisibility(View.VISIBLE);
				mBinding.cbOriginal.setVisibility(View.VISIBLE);
				type=1;
				break;
			case "1":
				mBinding.cbYingyong.setVisibility(View.VISIBLE);
				mBinding.cbYingyong.setChecked(true);
//				mBinding.cbGift.setVisibility(View.VISIBLE);
				mBinding.cbNews.setVisibility(View.VISIBLE);
				mBinding.cbGonglue.setVisibility(View.VISIBLE);
				mBinding.cbOriginal.setVisibility(View.VISIBLE);
				type=10;
				break;
			case "2":
				mBinding.cbNews.setVisibility(View.VISIBLE);
				mBinding.cbNews.setChecked(true);
				mBinding.cbGonglue.setVisibility(View.VISIBLE);
				mBinding.cbDianjing.setVisibility(View.VISIBLE);
				mBinding.cbVideo.setVisibility(View.VISIBLE);
				mBinding.cbPingce.setVisibility(View.VISIBLE);
				type=14;
				break;

			default:
				break;
		}
	}
	
	public void clickHandler(View view) {
		switch (view.getId()) {
			case R.id.cb_special://专区
				if(type!=1){
					mPage=1;
					type=1;
					obtainData();
				}
				break;
			case R.id.cb_news://新闻
				if(showCategory.equals("0")&&type!=7){
						mPage=1;
						type=7;
						obtainData();
				}else if(showCategory.equals("1")&&type!=11){
					mPage=1;
					type=11;
					obtainData();
				}else if(showCategory.equals("2")&&type!=14){
					mPage=1;
					type=14;
					obtainData();
				}
				break;
			case R.id.cb_gonglue://攻略
				if(showCategory.equals("0")&&type!=8){
					mPage=1;
					type=8;
					obtainData();
				}else if(showCategory.equals("1")&&type!=12){
					mPage=1;
					type=12;
					obtainData();
				}else if(showCategory.equals("2")&&type!=15){
					mPage=1;
					type=15;
					obtainData();
				}
				break;
			case R.id.cb_original://原创
				if(showCategory.equals("0")&&type!=9){
					mPage=1;
					type=9;
					obtainData();
				}else if(showCategory.equals("1")&&type!=13){
					mPage=1;
					type=13;
					obtainData();
				}
				break;
			case R.id.cb_yingyong://应用
				if(type!=2){
					mPage=1;
					type=2;
					obtainData();
				}
//				if(type!=10){   //之前是10
//					mPage=1;
//					type=10;
//					obtainData();
//				}
				break;
			case R.id.cb_dianjing://电竞
				if(type!=16){
					mPage=1;
					type=16;
					obtainData();
				}
				break;
			case R.id.cb_video://视频
				if(type!=17){
					mPage=1;
					type=17;
					obtainData();
				}
				break;
			case R.id.cb_pingce://评测
				if(type!=18){
					mPage=1;
					type=18;
					obtainData();
				}
				break;
			default:
				Log.i("click",""+view.getId());
				break;
		}
	}

	@Override
	protected void onFragmentVisibleChange(boolean isVisible) {
		super.onFragmentVisibleChange(isVisible);
		if (isVisible){
			obtainData();
		}
	}
}
