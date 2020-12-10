package com.ws3dm.app.fragment;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jcodecraeer.demo.MyAdapter;
import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.Constant;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.GameNewsActivity;
import com.ws3dm.app.activity.GonglueListActivity;
import com.ws3dm.app.bean.GameBean;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.databinding.FgGongBinding;
import com.ws3dm.app.mvp.model.RespBean.NewsGLpageRespBean;
import com.ws3dm.app.mvp.presenter.NewsPresenter;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.util.glide.BlurTransformation;
import com.ws3dm.app.util.glide.GlideUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Describution : 新闻页的攻略
 * 
 * Author : DKjuan
 * 
 * Date : 2019/9/27 17:44
 **/
public class FragmentGonglue extends BaseFragment implements View.OnClickListener{

	private static final String TAG = "FragmentGonglue";
	private FgGongBinding mBinding;
	private NewsPresenter mNewsPresenter;
	private Handler mHandler;
	private View header;//存储顶部viewpager
	private boolean hasHead=false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_gong, container, false);
		mBinding.setHandler(this);//添加监听事件
		header = LayoutInflater.from(mContext).inflate(R.layout.header_gonglue, container, false);

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (!mIsRunning) {
					return;
				}
				switch (msg.what) {
					case Constant.Notify.LOAD_START:
						ToastUtil.showToast(mContext, "载入中");
						break;
					case Constant.Notify.LOAD_FAILURE:
						ToastUtil.showToast(mContext, "请求失败");
						break;
					case Constant.Notify.RESULT_NEWS_NEWS_GLPAGE2://处理返回结果
						initDate((NewsGLpageRespBean) msg.obj);
						break;
				}
			}
		};

		mNewsPresenter = NewsPresenter.getInstance().getInstance();
		mNewsPresenter.setHandler(mHandler);

		initView();
		return mBinding.getRoot();
	}

	public void obtainData() {
		//获取数据
		long time = System.currentTimeMillis();
		String sign = StringUtil.MD5(time + "");
		JSONObject obj = new JSONObject();
		try {
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		mNewsPresenter.getGLpage2(obj.toString());//异步请求
	}

	public void initView(){
		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.recyclerview.setLayoutManager(layoutManager);

		mBinding.recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);       //原来就注释掉的
		mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
		mBinding.recyclerview.setLoadingMoreEnabled(false);
		mBinding.recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
			@Override
			public void onRefresh() {
				new Handler().postDelayed(new Runnable() {
					public void run() {
						obtainData();
					}
				}, 50);            //refresh data here
			}

			@Override
			public void onLoadMore() {
			}
		});
		mBinding.recyclerview.setAdapter(new MyAdapter(new ArrayList<String>()));//没啥用，只是让listview可以显示
	}
	
	public void initDate(NewsGLpageRespBean mData) {
		initPC(mData.getData().getDanjilist());
		initNet(mData.getData().getWangyoulist());
		initMG(mData.getData().getShouyoulist());
		header.findViewById(R.id.top_img0).setOnClickListener(this);
		header.findViewById(R.id.tv_more_pc).setOnClickListener(this);
		header.findViewById(R.id.top_img1).setOnClickListener(this);
		header.findViewById(R.id.tv_more_net).setOnClickListener(this);
		header.findViewById(R.id.top_img2).setOnClickListener(this);
		header.findViewById(R.id.tv_more_mg).setOnClickListener(this);
		if(!hasHead){
			mBinding.recyclerview.addHeaderView(header);
			hasHead=true;
		}
		mBinding.recyclerview.refreshComplete();
	}
	public void initPC(final List<NewsBean> list){
		GlideUtil.loadImage(mContext,list.get(0).getLitpic(), (ImageView) header.findViewById(R.id.pc_img1));
		((TextView)header.findViewById(R.id.pc_name1)).setText(list.get(0).getTitle());
		header.findViewById(R.id.pc1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpGonglue(list.get(0).getAid(),list.get(0).getShowtype(),list.get(0).getTitle());
			}
		});
		GlideUtil.loadImage(mContext,list.get(1).getLitpic(),(ImageView) header.findViewById(R.id.pc_img2));
		((TextView)header.findViewById(R.id.pc_name2)).setText(list.get(1).getTitle());
		header.findViewById(R.id.pc2).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpGonglue(list.get(1).getAid(),list.get(1).getShowtype(),list.get(1).getTitle());
			}
		});
		GlideUtil.loadImage(mContext,list.get(2).getLitpic(),(ImageView) header.findViewById(R.id.pc_img3));
		((TextView)header.findViewById(R.id.pc_name3)).setText(list.get(2).getTitle());
		header.findViewById(R.id.pc3).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpGonglue(list.get(2).getAid(),list.get(2).getShowtype(),list.get(2).getTitle());
			}
		});
		GlideUtil.loadImage(mContext,list.get(3).getLitpic(),(ImageView) header.findViewById(R.id.pc_img4));
		((TextView)header.findViewById(R.id.pc_name4)).setText(list.get(3).getTitle());
		header.findViewById(R.id.pc4).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpGonglue(list.get(3).getAid(),list.get(3).getShowtype(),list.get(3).getTitle());
			}
		});
		GlideUtil.loadImage(mContext,list.get(4).getLitpic(),(ImageView) header.findViewById(R.id.pc_img5));
		((TextView)header.findViewById(R.id.pc_name5)).setText(list.get(4).getTitle());
		header.findViewById(R.id.pc5).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpGonglue(list.get(4).getAid(),list.get(4).getShowtype(),list.get(4).getTitle());
			}
		});
		GlideUtil.loadImage(mContext,list.get(5).getLitpic(),(ImageView) header.findViewById(R.id.pc_img6));
		((TextView)header.findViewById(R.id.pc_name6)).setText(list.get(5).getTitle());
		header.findViewById(R.id.pc6).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpGonglue(list.get(5).getAid(),list.get(5).getShowtype(),list.get(5).getTitle());
			}
		});
	}
	
	public void initNet(final List<NewsBean> list){
		GlideUtil.loadImage(mContext,list.get(0).getLitpic(), (ImageView) header.findViewById(R.id.net_img1));
		((TextView)header.findViewById(R.id.net_name1)).setText(list.get(0).getTitle());
		header.findViewById(R.id.net1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpGonglue(list.get(0).getAid(),list.get(0).getShowtype(),list.get(0).getTitle());
			}
		});
		GlideUtil.loadImage(mContext,list.get(1).getLitpic(),(ImageView) header.findViewById(R.id.net_img2));
		((TextView)header.findViewById(R.id.net_name2)).setText(list.get(1).getTitle());
		header.findViewById(R.id.net2).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpGonglue(list.get(1).getAid(),list.get(1).getShowtype(),list.get(1).getTitle());
			}
		});
		GlideUtil.loadImage(mContext,list.get(2).getLitpic(),(ImageView) header.findViewById(R.id.net_img3));
		((TextView)header.findViewById(R.id.net_name3)).setText(list.get(2).getTitle());
		header.findViewById(R.id.net3).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpGonglue(list.get(2).getAid(),list.get(2).getShowtype(),list.get(2).getTitle());
			}
		});
		GlideUtil.loadImage(mContext,list.get(3).getLitpic(),(ImageView) header.findViewById(R.id.net_img4));
		((TextView)header.findViewById(R.id.net_name4)).setText(list.get(3).getTitle());
		header.findViewById(R.id.net4).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpGonglue(list.get(3).getAid(),list.get(3).getShowtype(),list.get(3).getTitle());
			}
		});
	}
	
	public void initMG(final List<NewsBean> list){
		GlideUtil.loadImage(mContext,list.get(0).getLitpic(),(ImageView) header.findViewById(R.id.mg_img1));
		((TextView)header.findViewById(R.id.mg_name1)).setText(list.get(0).getTitle());
		header.findViewById(R.id.mg1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpGonglue(list.get(0).getAid(),list.get(0).getShowtype(),list.get(0).getTitle());
			}
		});
		GlideUtil.loadImage(mContext,list.get(1).getLitpic(),(ImageView) header.findViewById(R.id.mg_img2));
		((TextView)header.findViewById(R.id.mg_name2)).setText(list.get(1).getTitle());
		header.findViewById(R.id.mg2).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpGonglue(list.get(1).getAid(),list.get(1).getShowtype(),list.get(1).getTitle());
			}
		});
		GlideUtil.loadImage(mContext,list.get(2).getLitpic(),(ImageView) header.findViewById(R.id.mg_img3));
		((TextView)header.findViewById(R.id.mg_name3)).setText(list.get(2).getTitle());
		header.findViewById(R.id.mg3).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpGonglue(list.get(2).getAid(),list.get(2).getShowtype(),list.get(2).getTitle());
			}
		});
		GlideUtil.loadImage(mContext,list.get(3).getLitpic(),(ImageView) header.findViewById(R.id.mg_img4));
		((TextView)header.findViewById(R.id.mg_name4)).setText(list.get(3).getTitle());
		header.findViewById(R.id.mg4).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpGonglue(list.get(3).getAid(),list.get(3).getShowtype(),list.get(3).getTitle());
			}
		});
		GlideUtil.loadImage(mContext,list.get(4).getLitpic(),(ImageView) header.findViewById(R.id.mg_img5));
		((TextView)header.findViewById(R.id.mg_name5)).setText(list.get(4).getTitle());
		header.findViewById(R.id.mg5).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpGonglue(list.get(4).getAid(),list.get(4).getShowtype(),list.get(4).getTitle());
			}
		});
		GlideUtil.loadImage(mContext,list.get(5).getLitpic(),(ImageView) header.findViewById(R.id.mg_img6));
		((TextView)header.findViewById(R.id.mg_name6)).setText(list.get(5).getTitle());
		header.findViewById(R.id.mg6).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpGonglue(list.get(5).getAid(),list.get(5).getShowtype(),list.get(5).getTitle());
			}
		});

//		GlideUtil.loadImage(mContext,list.get(4).getLitpic(),mBinding.mgBg1);
		Glide.with(mContext).load(list.get(0).getLitpic()).dontAnimate().error(R.drawable.load_error)
				// 设置高斯模糊,模糊程度(最大25)  缩放比例
				.bitmapTransform(new BlurTransformation(mContext, 20, 8)).into((ImageView) header.findViewById(R.id.mg_bg1));
		Glide.with(mContext).load(list.get(1).getLitpic()).dontAnimate().error(R.drawable.load_error)
				// 设置高斯模糊,模糊程度(最大25)  缩放比例
				.bitmapTransform(new BlurTransformation(mContext, 20, 8)).into((ImageView) header.findViewById(R.id.mg_bg2));
		Glide.with(mContext).load(list.get(2).getLitpic()).dontAnimate().error(R.drawable.load_error)
				// 设置高斯模糊,模糊程度(最大25)  缩放比例
				.bitmapTransform(new BlurTransformation(mContext, 20, 8)).into((ImageView) header.findViewById(R.id.mg_bg3));
		Glide.with(mContext).load(list.get(3).getLitpic()).dontAnimate().error(R.drawable.load_error)
				// 设置高斯模糊,模糊程度(最大25)  缩放比例
				.bitmapTransform(new BlurTransformation(mContext, 20, 8)).into((ImageView) header.findViewById(R.id.mg_bg4));
		Glide.with(mContext).load(list.get(4).getLitpic()).dontAnimate().error(R.drawable.load_error)
				// 设置高斯模糊,模糊程度(最大25)  缩放比例
				.bitmapTransform(new BlurTransformation(mContext, 20, 8)).into((ImageView) header.findViewById(R.id.mg_bg5));
		Glide.with(mContext).load(list.get(5).getLitpic()).dontAnimate().error(R.drawable.load_error)
				// 设置高斯模糊,模糊程度(最大25)  缩放比例
				.bitmapTransform(new BlurTransformation(mContext, 20, 8)).into((ImageView) header.findViewById(R.id.mg_bg6));
	}


	@Override
	public void onClick(View view) {
		Intent intent;
		switch (view.getId()) {//17单机18手游19网游
			case R.id.top_img0://单机
			case R.id.tv_more_pc:
				intent = new Intent(mContext, GonglueListActivity.class);
				intent.putExtra("showtype",17);
				intent.putExtra("title","单机攻略");
				startActivity(intent);
				break;
			case R.id.top_img1://网游
			case R.id.tv_more_net:
				intent = new Intent(mContext, GonglueListActivity.class);
				intent.putExtra("showtype",19);
				intent.putExtra("title","网游攻略");
				startActivity(intent);
				break;
			case R.id.top_img2://手游
			case R.id.tv_more_mg:
				intent = new Intent(mContext, GonglueListActivity.class);
				intent.putExtra("showtype",18);
				intent.putExtra("title","手游攻略");
				startActivity(intent);
				break;
			default:
				Log.e(TAG, "TODO: ClickListener" + view.getId());
				break;
		}
	}
	
	protected void onFragmentFirstVisible(){
		mBinding.recyclerview.setRefreshing(true);//refresh的监听处理中已经有加载数据的操作
	}
	
	public void jumpGonglue(int aid,int showtype,String title){
		GameBean game=new GameBean();
		game.setAid(aid);
		game.setShowtype(showtype);
		game.setTitle(title);
		
		Intent intent = new Intent(mContext, GameNewsActivity.class);
		intent.putExtra("tag", 1);//设置哪个标签选中
		Bundle bundle1 = new Bundle();
		bundle1.putSerializable("game", game);
		intent.putExtras(bundle1);
		startActivity(intent);
	}

	@Override
	public void onResume(){
		super.onResume();
		if(mNewsPresenter!=null){
			mNewsPresenter.setHandler(mHandler);
		}
	}
}
