package com.ws3dm.app.fragment;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.GameHomeActivity;
import com.ws3dm.app.adapter.BaseRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.GameBean;
import com.ws3dm.app.bean.GameHotBean;
import com.ws3dm.app.databinding.FgBaseRecyclerviewBinding;
import com.ws3dm.app.mvp.model.RespBean.GameHotRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.GameService;
import com.ws3dm.app.util.SharedUtil;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;
import com.ws3dm.app.util.glide.GlideUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Describution :热点游戏fragment
 * 
 * Author : DKjuan
 * 
 * Date : 2017/8/18 17:22
 **/
public class FragmentHotGame extends BaseFragment {
	private FgBaseRecyclerviewBinding mBinding;
//	private Context mContext;
	private BaseRecyclerAdapter<GameHotBean> mRecyclerAdapter;
	private BaseRecyclerAdapter<GameBean> mSubRecyclerAdapter;
	public ArrayList<GameHotBean> listData = new ArrayList<GameHotBean>();

	private boolean isFirst,hasHead;
	private View header;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		mContext=getActivity();
		mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_base_recyclerview, container, false);
		header = LayoutInflater.from(mContext).inflate(R.layout.header_game, container,false);

		initView();
		return mBinding.getRoot();
	}
	
	public void initView(){
		isFirst = true;
		hasHead=false;

		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.recyclerview.setLayoutManager(layoutManager);

		mBinding.recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
		mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);

		mRecyclerAdapter = new BaseRecyclerAdapter<GameHotBean>(mContext, R.layout.adapter_hot_cate) {
			@Override
			public void bindData(RecyclerViewHolder holder, final int position, final GameHotBean item) {
				holder.setText(R.id.txt_cate_name, item.getName());
				TextView more=holder.getView(R.id.more);
				more.setOnClickListener(new View.OnClickListener() {//最期待->发售  热门跳转排行
					@Override
					public void onClick(View view) {
						if(position==0){
							SharedUtil.setSharedPreferencesData("onSale","1");
							GameFragment.mViewPager.setCurrentItem(1);
						}else if(position==1){
							SharedUtil.setSharedPreferencesData("onSale","2");
							GameFragment.mViewPager.setCurrentItem(1);
						}else if(position==2){
							GameFragment.mViewPager.setCurrentItem(3);
						}else if(position==3){
							GameFragment.mViewPager.setCurrentItem(3);
						}
					}
				});
				more.setVisibility(View.VISIBLE);
//				if(item.getType()%2==0){
//					more.setVisibility(View.VISIBLE);
//				}else{
//					more.setVisibility(View.GONE);
//				}
				
				LinearLayout ll_hot_cate= holder.getView(R.id.ll_hot_cate);
				RecyclerView recyclerView = holder.getView(R.id.recycler_hot_cates);
				GridLayoutManager manager = new GridLayoutManager(mContext, 3);
				recyclerView.setLayoutManager(manager);

				mSubRecyclerAdapter= new BaseRecyclerAdapter<GameBean>(mContext, R.layout.adapter_hot_game) {
					@Override
					public void bindData(RecyclerViewHolder holder, int position, GameBean game) {
						holder.setImageByUrl(R.id.imgCover, game.getLitpic());
						holder.setText(R.id.txtName,game.getTitle());
						
						LinearLayout ll_score=holder.getView(R.id.ll_score);
						TextView tv_time=holder.getView(R.id.tv_time);
						if(item.getType()<3){
							tv_time.setVisibility(View.VISIBLE);
							ll_score.setVisibility(View.GONE);
							tv_time.setText(TimeUtil.getFormatTimeSimple(game.getPubdate_at()));
						}else {
							tv_time.setVisibility(View.GONE);
							ll_score.setVisibility(View.VISIBLE);
							
							double score= game.getScore();
							holder.setText(R.id.score, score+"");
							ImageView imgScore=holder.getView(R.id.img_score);
							// 获得ClipDrawable对象  
							ClipDrawable clipDrawable = (ClipDrawable) imgScore.getBackground();
							// 设置裁剪级别，Clip类型的图片默认裁剪级别为0，此时是全部裁剪，图片看不见；  
							// 当级别为10000时，不裁剪图片，图片全部可见  
							// 当全部显示后，设置不可见  
							clipDrawable.setLevel((int)(score*1000));
						}
					}
				};
				mSubRecyclerAdapter.setOnClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(View itemView, int posi) {
						Intent intent = new Intent(mContext, GameHomeActivity.class);
						Bundle bundle = new Bundle();
//						bundle.putSerializable("game", listData.get(position).getList().get(posi));
//					bundle.putSerializable("game", finalGame);//数据太大，超出系统限制
						GameBean tempGame=listData.get(position).getList().get(posi);
						bundle.putString("str_game", JSON.toJSONString(tempGame));
						intent.putExtras(bundle);
						startActivity(intent);
					}
				});
				recyclerView.setAdapter(mSubRecyclerAdapter);
				mSubRecyclerAdapter.clearAndAddList(item.getList());

//				// 更换主题
//				if (SharedUtil.getSharedPreferencesData("isNight").equals("0")) {
//					ll_hot_cate.setBackgroundColor(0xffffffff);
//					txtLine.setBackgroundColor(0xffe7e7e7);
//					((TextView)holder.getView(R.id.txt_cate_name)).setTextColor(0xff555555);
//					((TextView)holder.getView(R.id.more)).setTextColor(0xff555555);
//				} else {
//					ll_hot_cate.setBackgroundColor(0xff2a2a2a);
//					txtLine.setBackgroundColor(0xff666666);
//					((TextView)holder.getView(R.id.txt_cate_name)).setTextColor(0xff9c9c9c);
//					((TextView)holder.getView(R.id.more)).setTextColor(0xff9c9c9c);
//				}
			}
		};
//		mRecyclerAdapter.setOnClickListener(new BaseRecyclerAdapter.OnItemClickListener(){
//			@Override
//			public void onItemClick(View itemView, int position) {//最期待->发售  热门跳转排行
//				ToastUtil.showToast(mContext,"更多"+position);
//				if(position==3){
//					GameFragment.mViewPager.setCurrentItem(1);
//				}else if(position==1){
//					GameFragment.mViewPager.setCurrentItem(3);
//				}
//			}
//		});
		mBinding.recyclerview.setAdapter(mRecyclerAdapter);
		mBinding.recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
			@Override
			public void onRefresh() {
				new Handler().postDelayed(new Runnable(){
					public void run() {
						obtainData();
						mBinding.recyclerview.refreshComplete();
					}
				}, 50);            //refresh data here
			}

			@Override
			public void onLoadMore() {
				new Handler().postDelayed(new Runnable(){
					public void run() {
						mBinding.recyclerview.setNoMore(true);
					}
				}, 50);
			}
		});
	}
	
	public void obtainData(){
		//获取数据
		long time=System.currentTimeMillis();
		String validate=""+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
//		mGamePresenter.getHotGame(obj.toString()); 异步请求
		//同步请求
		Retrofit retrofit= RetrofitFactory.getNewRetrofit(0);
		GameService.Api service=retrofit.create(GameService.Api.class);
		RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),obj.toString());
		Call<GameHotRespBean> call=service.getHotGame(body);
		call.enqueue(new Callback<GameHotRespBean>() {
			@Override
			public void onResponse(Call<GameHotRespBean> call, Response<GameHotRespBean> response) {
				Log.e("requestSuccess","-----------------------"+response.body());
				if(!hasHead){
					initHeadView(response.body());
					hasHead=true;
					mBinding.recyclerview.addHeaderView(header);
				}
				initData(response.body());
			}

			@Override
			public void onFailure(Call<GameHotRespBean> call, Throwable throwable) {
				Log.e("requestFailure",throwable.getMessage()+"");
				mBinding.recyclerview.loadMoreError();
			}
		});
	}

	//头部的滑动广告栏
	private void initHeadView(final GameHotRespBean bean) {
		if(bean==null||bean.getData()==null||bean.getData().getSlides()==null)
			return;
		TextView tv_name= (TextView) header.findViewById(R.id.tv_name);
		TextView tv_time= (TextView) header.findViewById(R.id.tv_time);
		TextView tv_stage= (TextView) header.findViewById(R.id.tv_stage);
		TextView tv_score= (TextView) header.findViewById(R.id.tv_score);
		ImageView img_bg= (ImageView) header.findViewById(R.id.img_bg);
		img_bg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				GameBean topGame=new GameBean();
				topGame.setLitpic(bean.getData().getSlides().get(0).getLitpic());
				topGame.setAid(bean.getData().getSlides().get(0).getAid());
				topGame.setArcurl(bean.getData().getSlides().get(0).getArcurl());
				topGame.setShowtype(bean.getData().getSlides().get(0).getAid());
				topGame.setWebviewurl(bean.getData().getSlides().get(0).getWebviewurl());
				topGame.setPubdate_at(bean.getData().getSlides().get(0).getPubdate_at());
				Intent intent = new Intent(mContext, GameHomeActivity.class);
				Bundle bundle = new Bundle();
//				bundle.putSerializable("game", topGame);
//					bundle.putSerializable("game", finalGame);//数据太大，超出系统限制
				bundle.putString("str_game", JSON.toJSONString(topGame));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		tv_name.setText(bean.getData().getSlides().get(0).getTitle());
		tv_time.setText("发售："+TimeUtil.getFormatTimeSimple(bean.getData().getSlides().get(0).getPubdate_at()));
		tv_stage.setText("平台："+bean.getData().getSlides().get(0).getSystem());
		tv_score.setText(bean.getData().getSlides().get(0).getScore()+"");
		String url=bean.getData().getSlides().get(0).getLitpic();
		GlideUtil.loadImage(mContext,url,img_bg);

		ImageView imgScore= (ImageView) header.findViewById(R.id.img_score);
		// 获得ClipDrawable对象  
		ClipDrawable clipDrawable = (ClipDrawable) imgScore.getBackground();
		// 设置裁剪级别，Clip类型的图片默认裁剪级别为0，此时是全部裁剪，图片看不见；  
		// 当级别为10000时，不裁剪图片，图片全部可见  
		// 当全部显示后，设置不可见  
		clipDrawable.setLevel((int)bean.getData().getSlides().get(0).getScore()*1000);
		
		
	}

	@Override
	protected void onFragmentVisibleChange(boolean isVisible) {
		super.onFragmentVisibleChange(isVisible);
//		if (!strType.equals("CollectActivity"))
			if (isVisible)
				if (isFirst) {
					obtainData();
					isFirst = false;
					mBinding.recyclerview.setRefreshing(true);//refresh的监听处理中已经有加载数据的操作
				}
	}
	
	private void initData(GameHotRespBean bean) {
		if(bean==null||listData.size()>0){
			return;
		}
		
		GameHotBean newgame=new GameHotBean();
		newgame.setType(1);
		newgame.setName("最新大作");
		newgame.setList(bean.getData().getNewgame());
		listData.add(newgame);
		
		GameHotBean expectgame=new GameHotBean();
		expectgame.setType(2);
		expectgame.setName("最期待游戏");
		expectgame.setList(bean.getData().getExpectgame());
		listData.add(expectgame);
		
		GameHotBean classGame=new GameHotBean();
		classGame.setType(3);
		classGame.setName("经典大作");
		classGame.setList(bean.getData().getClassicgame());
		listData.add(classGame);
		
		GameHotBean hotgame=new GameHotBean();
		hotgame.setType(4);
		hotgame.setName("热门游戏");
		hotgame.setList(bean.getData().getHotgame());
		listData.add(hotgame);

		mRecyclerAdapter.clearAndAddList(listData);
	}
}