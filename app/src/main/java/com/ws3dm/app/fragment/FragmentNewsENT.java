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

import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.umeng.analytics.MobclickAgent;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.NewsActivity;
import com.ws3dm.app.adapter.BaseRecyclerAdapter;
import com.ws3dm.app.adapter.NewsENTMultiAdapter;
import com.ws3dm.app.adapter.NewsENTMultiItemTypeSupport;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.databinding.FgNewsEntBinding;
import com.ws3dm.app.mvp.model.RespBean.NewsDigitalRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.NewsService;
import com.ws3dm.app.sqlite.NewsFile;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;

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
 * Describution :新闻 --  娱乐
 * 
 * Author : DKjuan
 * 
 * Date : 2017/8/18 17:22
 **/
public class FragmentNewsENT extends BaseFragment {	
	private FgNewsEntBinding mBinding;
	//	private CommonRecyclerAdapter<NewsBean> mAdapter;
	private NewsENTMultiAdapter mAdapter;
	public List<NewsBean> listData = new ArrayList<NewsBean>();
	private NewsFile newsCollectFile;
	
	private int mPage=1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		AppCompatActivity activity = (AppCompatActivity) getActivity();

		mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_news_ent, container, false);

		initView();
		return mBinding.getRoot();
	}
	
	public void initView(){
		newsCollectFile = new NewsFile(getActivity());

		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.recyclerview.setLayoutManager(layoutManager);

		mBinding.recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
		mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);


//		//添加分割线
//		if (mListV3DividerItemDecoration == null) {
//			mListV3DividerItemDecoration = new DividerItemDecorationAdapter(mContext);
//		} else {
//			mBinding.recyclerview.removeItemDecoration(mListV3DividerItemDecoration);
//		}
//		mBinding.recyclerview.addItemDecoration(mListV3DividerItemDecoration);

//		mAdapter = new CommonRecyclerAdapter<NewsBean>(mContext, R.layout.adapter_news_one_right_pic) {
//			@Override
//			public void bindData(RecyclerViewHolder holder, final int position, final NewsBean item) {
//				holder.setText(R.id.txtTitle, item.getTitle());
//				holder.setText(R.id.txtComment, item.getTotal_ct()+"");
//				holder.setText(R.id.tv_time, TimeUtil.getFriendlyTime(""+item.getPubdate_at()+"000"));
//				holder.setText(R.id.tv_name, item.getUser().nickname);
////				holder.setImageByUrl(R.id.img_head,item.getUser().avatarstr);
//				GlideUtil.loadCircleImage(mContext,item.getUser().avatarstr,(ImageView)holder.getView(R.id.img_head));
//				holder.setImageByUrl(R.id.imgArrayOne,item.getLitpic());
//			}
//		};
		NewsENTMultiItemTypeSupport modelMultiItemTypeSupport = new NewsENTMultiItemTypeSupport();
		mAdapter= new NewsENTMultiAdapter(mContext, modelMultiItemTypeSupport);
		mBinding.recyclerview.setAdapter(mAdapter);
		mAdapter.setOnClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int position) {
				MobclickAgent.onEvent(mContext,"03");
				mAdapter.getDataByPosition(position).setHavesee(1);
				NewsBean news = mAdapter.getDataByPosition(position);
				if(!StringUtil.isEmpty(news.getTitle())){
					news.setSeeDate(TimeUtil.dateDayNow());
					news.setType("娱乐");
					newsCollectFile.addHistory(news);
					Intent intent = new Intent(mContext, NewsActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("newsBean",news);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});
		mBinding.recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
			@Override
			public void onRefresh() {
				new Handler().postDelayed(new Runnable(){
					public void run() {
						mPage=1;
						obtainData(mPage);
					}
				}, 50);            //refresh data here
			}

			@Override
			public void onLoadMore() {
				new Handler().postDelayed(new Runnable(){
					public void run() {
						obtainData(mPage);
					}
				}, 50);
			}
		});

		mBinding.recyclerview.setRefreshing(true);
	}

	protected void onFragmentFirstVisible(){
//		mBinding.recyclerview.setRefreshing(true);//refresh的监听处理中已经有加载数据的操作
	}

	public void obtainData(int page){
		//获取数据
		long time=System.currentTimeMillis();
		String validate= "010"+page+time;//type 娱乐栏目编号全部时则传入0
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("type", 0);
			obj.put("pagesize", 10);
			obj.put("page", page);
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
		Call<NewsDigitalRespBean> call = service.getNewsENT(body);
		call.enqueue(new Callback<NewsDigitalRespBean>() {
			@Override
			public void onResponse(Call<NewsDigitalRespBean> call, Response<NewsDigitalRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				initData(response.body());
			}

			@Override
			public void onFailure(Call<NewsDigitalRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
				mBinding.recyclerview.loadMoreError();
			}
		});
	}
	
	private void initData(NewsDigitalRespBean bean) {
		mBinding.recyclerview.refreshComplete();
		if(bean==null||bean.getData().getList()==null)
			return;
		listData=bean.getData().getList();
		if(mPage>1) {
			mAdapter.appendList(listData);
		}else {
			if(listData.size()>2){
				NewsBean tarbean=new NewsBean();
				tarbean.setTypelist(bean.getData().getTypelist());
				listData.add(2,tarbean);
			}else if(bean.getData().getTypelist()!=null){
				NewsBean tarbean=new NewsBean();
				tarbean.setTypelist(bean.getData().getTypelist());
				listData.add(0,tarbean);
			}
			mAdapter.clearAndAddList(listData);
		}
		if (listData==null||listData.size()<1) {
			mBinding.recyclerview.setNoMore(true);
		} else {
			mPage++;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
//		if (mAdapter != null)
//			mAdapter.notifyDataSetChanged();
	}

	public void reLoad(){
		mBinding.recyclerview.scrollToPosition(0);
//		mBinding.recyclerview.setRefreshing(true);
//		mPage=1;
//		obtainData(mPage);
	}
}