package com.ws3dm.app.fragment;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.ForgetPassActivity;
import com.ws3dm.app.activity.GameCategoryListActivity;
import com.ws3dm.app.activity.GameNewsActivity;
import com.ws3dm.app.activity.GameVideoActivity;
import com.ws3dm.app.activity.GiftDetailActivity;
import com.ws3dm.app.activity.GiftRelativeActivity;
import com.ws3dm.app.activity.GongLabelActivity;
import com.ws3dm.app.activity.GongTuJianActivity;
import com.ws3dm.app.activity.LoginActivity;
import com.ws3dm.app.activity.NewsActivity;
import com.ws3dm.app.activity.OriginalActivity;
import com.ws3dm.app.adapter.BaseRecyclerAdapter;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.bean.CommentBean;
import com.ws3dm.app.bean.GameBean;
import com.ws3dm.app.bean.GameGiftBean;
import com.ws3dm.app.bean.GllistBean;
import com.ws3dm.app.bean.GonglueBean;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.bean.PiclistBean;
import com.ws3dm.app.databinding.FgGameWebBinding;
import com.ws3dm.app.emoj.EmotionUtils;
import com.ws3dm.app.emoj.fragment.EmotionMainFragment;
import com.ws3dm.app.mvp.model.RespBean.GameDetailRespBean;
import com.ws3dm.app.mvp.model.RespBean.NewsHotCommentRespBean;
import com.ws3dm.app.mvp.presenter.GamePresenter;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.GameService;
import com.ws3dm.app.network.service.NewsService;
import com.ws3dm.app.network.service.UserService;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.util.glide.GlideUtil;
import com.ws3dm.app.view.InputWindowListener;
import com.ws3dm.app.view.SharePopupWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Describution :新闻页详情上部的网页
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2017/8/18 17:22
 **/
public class FragmentGameWeb extends BaseFragment {
	private FgGameWebBinding mBinding;
	private GameBean game;
	private String type;
	private CommonRecyclerAdapter<String> horizonalAdapter;
	private List<CommentBean> listData = new ArrayList<>();
	private int firGameType,f_sid, replyId = -1, favorite = 2;//favorite:1已收藏，2为收藏
	private int mPage, selectID = 0;
	;
	private int c_sid;//当前文章在评论里唯一标识(默认为0)

	private SharePopupWindow popShare;
	private InputMethodManager imm;
	private boolean isFirst;
	private CommonRecyclerAdapter mAdapter;
	private String strContent;
	private GamePresenter mGamePresenter;
	private EmotionUtils emoutil;
	private NewsHotCommentRespBean myCommentData;
	private boolean isOpen = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_game_web, container, false);
		mBinding.setHandler(this);
		mGamePresenter = GamePresenter.getInstance();

		initView();

		return mBinding.getRoot();
	}

	public void initView() {
		emoutil = new EmotionUtils(getActivity());
		type = getArguments().getString("type");
		if (getArguments().getSerializable("gameBean") != null)
			game = (GameBean) getArguments().getSerializable("gameBean");
		isFirst = true;
		c_sid = 0;
		mPage = 1;

		if (game == null) {
			return;
		}
		imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		//获取数据
		String str_uid = MyApplication.getUserData().loginStatue ? MyApplication.getUserData().uid : "0";
		int uid = Integer.parseInt(str_uid);
		int aid = game.getAid();
		long time = System.currentTimeMillis();
		String validate="";
		if(game.getShowtype()==3) {//20手游专区21网游专区3单机
			validate = "" + uid + aid + time;
		}else{
			validate = "" + aid + time;
		}
		String sign = StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid", uid);
			obj.put("aid", aid);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
//		mGamePresenter.getGameDetail(obj.toString());//异步请求
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		GameService.Api service = retrofit.create(GameService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<GameDetailRespBean> call;
		if(game.getShowtype()==20){//20手游专区21网游专区3单机
			call=service.getSYGameDetail(body);
		}else if(game.getShowtype()==21){
			call=service.getOLGameDetail(body);
		}else {
			call=service.getGameDetail(body);
		}
		call.enqueue(new Callback<GameDetailRespBean>() {
			@Override
			public void onResponse(Call<GameDetailRespBean> call, Response<GameDetailRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				if (mContext != null)
					setHead(response.body());
			}

			@Override
			public void onFailure(Call<GameDetailRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
			}
		});

		imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

//		if(MyApplication.getUserData().loginStatue){
//			getFavoriteStute();
//		}

		// 监听键盘状态
		mBinding.rlAllComment.setListener(new InputWindowListener() {
			@Override
			public void show() {
				String text = "";
				mBinding.etComment.setText("");
				mBinding.etComment.setSelection(text.length());
				mBinding.llNoInPut.setVisibility(View.GONE);
				mBinding.llInPut.postDelayed(new Runnable() {
					@Override
					public void run() {
						mBinding.llInPut.setVisibility(View.VISIBLE);
					}
				}, 1);
			}

			@Override
			public void hidden() {
				if (mBinding.flEmotionviewMain.getHeight() > 100) {

				} else {
					replyId = 0;
					mBinding.etComment.setText("");
					mBinding.llInPut.setVisibility(View.GONE);
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							if (mBinding.llNoInPut != null)
								mBinding.llNoInPut.setVisibility(View.VISIBLE);
						}
					}, 1);
				}
			}
		});

		initEmotionMainFragment();
	}

	/**
	 * 初始化表情面板
	 */
	EmotionMainFragment emotionMainFragment;

	public void initEmotionMainFragment() {
		//构建传递参数
		Bundle bundle = new Bundle();
		//绑定主内容编辑框
		bundle.putBoolean(EmotionMainFragment.BIND_TO_EDITTEXT, false);
		//隐藏控件
		bundle.putBoolean(EmotionMainFragment.HIDE_BAR_EDITTEXT_AND_BTN, true);
		//替换fragment
		//创建修改实例
//		emotionMainFragment = EmotionMainFragment.newInstance(EmotionMainFragment.class, bundle);
		emotionMainFragment = EmotionMainFragment.newInstance(mBinding.llInPut, mBinding.etComment, mBinding.ivEmoj, bundle);
//		emotionMainFragment.bindToContentView(mBinding.llInPut);
//		emotionMainFragment.bindToEditView(mBinding.etComment);
//		emotionMainFragment.bindToImageView(mBinding.ivEmoj);
		FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
		// Replace whatever is in thefragment_container view with this fragment,
		// and add the transaction to the backstack
		transaction.replace(R.id.fl_emotionview_main, emotionMainFragment);
		transaction.addToBackStack(null);
		//提交修改
		transaction.commit();
	}

	public void setHead(final GameDetailRespBean mGameDetailRespBean) {
		if(mGameDetailRespBean.getData()==null||mContext==null) {
			ToastUtil.showToast(mContext,"暂未建立游戏专区");
			return;
		}
		game.setImgs(mGameDetailRespBean.getData().getImgs());
		game.setShowtype(mGameDetailRespBean.getData().getShowtype());
		game.setTitle(mGameDetailRespBean.getData().getTitle());
		if(StringUtil.isEmpty(mGameDetailRespBean.getData().getLitpic()))
			popShare = new SharePopupWindow(mContext,mGameDetailRespBean.getData().getArcurl(), mGameDetailRespBean.getData().getTitle(), mGameDetailRespBean.getData().getContent(), mBinding.loadViewShare);
		else
			popShare = new SharePopupWindow(mContext,game.getArcurl(), game.getTitle(), mGameDetailRespBean.getData().getContent(), mBinding.loadViewShare,mGameDetailRespBean.getData().getLitpic());

		//顶部图片及文字标签
		GlideUtil.loadImage(mContext.getApplicationContext(), mGameDetailRespBean.getData().getBgpic(), mBinding.imgCover);
		if(mGameDetailRespBean.getData().getShowtype()==21){//21网游专区
			//改变图片尺寸
			if(isAdded()){
				ViewGroup.LayoutParams mParams =  mBinding.imgGame.getLayoutParams();
				mParams.height=(int)getActivity().getResources().getDimension(R.dimen.element_margin_100);
				mParams.width=(int)getActivity().getResources().getDimension(R.dimen.element_margin_80);
				mBinding.imgGame.setLayoutParams(mParams);
			}
			mBinding.imgGame.setScaleType(ImageView.ScaleType.FIT_XY);
			mBinding.txtName.setText(mGameDetailRespBean.getData().getTitle());
		}else if(mGameDetailRespBean.getData().getShowtype()==20){//20手游专区
			if(isAdded()){
				ViewGroup.LayoutParams mParams =  mBinding.imgGame.getLayoutParams();
				mParams.height=(int)getActivity().getResources().getDimension(R.dimen.element_margin_100);
				mParams.width=(int)getActivity().getResources().getDimension(R.dimen.element_margin_80);
				mBinding.imgGame.setLayoutParams(mParams);
			}
			mBinding.imgGame.setScaleType(ImageView.ScaleType.FIT_CENTER);
			mBinding.imgGame.setCornerRadius(getResources().getDimension(R.dimen.element_margin_13));
			mBinding.imgGame.setPadding(0,(int)getResources().getDimension(R.dimen.element_margin_10),0,0);
			mBinding.imgGame.setBorderWidth(0f);
			mBinding.txtName.setText(mGameDetailRespBean.getData().getTitle());
		}else{//3单机
			if(isAdded()){
				ViewGroup.LayoutParams mParams =  mBinding.imgGame.getLayoutParams();
				mParams.height=(int)getActivity().getResources().getDimension(R.dimen.element_margin_100);
				mParams.width=(int)getActivity().getResources().getDimension(R.dimen.element_margin_80);
				mBinding.imgGame.setLayoutParams(mParams);
			}
			mBinding.imgGame.setScaleType(ImageView.ScaleType.FIT_XY);
			mBinding.txtName.setText(mGameDetailRespBean.getData().getTitle());
			mBinding.txtNameEn.setText(mGameDetailRespBean.getData().getEname());
		}
		GlideUtil.loadImage(mContext.getApplicationContext(), mGameDetailRespBean.getData().getLitpic(), mBinding.imgGame);

		mBinding.tvScore.setText(mGameDetailRespBean.getData().getScore());
		
		if(mGameDetailRespBean.getData().getShowtype()==21){//20手游专区21网游专区3单机
			mBinding.txtSys1.setText(mGameDetailRespBean.getData().getFirm());
			mBinding.txtSys1.setVisibility(View.VISIBLE);
		}else{
			String str_system=mGameDetailRespBean.getData().getSystem();
			if(str_system==null){
				mBinding.llSystem.setVisibility(View.GONE);
			}else{
				String[] sys = str_system.contains("/")?str_system.split("/"):str_system.split(" ");
				switch (sys.length>6?6:sys.length) {
					case 6:
						mBinding.txtSys6.setVisibility(View.VISIBLE);
					case 5:
						mBinding.txtSys5.setText(sys[4]);
						mBinding.txtSys5.setVisibility(View.VISIBLE);
					case 4:
						mBinding.txtSys4.setText(sys[3]);
						mBinding.txtSys4.setVisibility(View.VISIBLE);
					case 3:
						mBinding.txtSys3.setText(sys[2]);
						mBinding.txtSys3.setVisibility(View.VISIBLE);
					case 2:
						mBinding.txtSys2.setText(sys[1]);
						mBinding.txtSys2.setVisibility(View.VISIBLE);
					case 1:
						mBinding.txtSys1.setText(sys[0]);
						mBinding.txtSys1.setVisibility(View.VISIBLE);
				}
			}
		}

		//游戏详情 及设置标签
		if(mGameDetailRespBean.getData().getShowtype()==3){//20手游专区21网游专区3单机
			mBinding.tvFactory.setText("厂商：" + mGameDetailRespBean.getData().getPublisher());
			mBinding.tvType.setText("类型：" + mGameDetailRespBean.getData().getType());
			mBinding.tvTime.setText("日期：" + TimeUtil.getTimeEN(mGameDetailRespBean.getData().getPubdate_at()));
			mBinding.tvLanguage.setText("语言：" + mGameDetailRespBean.getData().getLanguage());
			if(mGameDetailRespBean.getData().getLabels()!=null){
				switch (mGameDetailRespBean.getData().getLabels().size()) {
					case 6:
						mBinding.txtLabel6.setText(mGameDetailRespBean.getData().getLabels().get(5).getName());
						mBinding.txtLabel6.setVisibility(View.VISIBLE);
						mBinding.txtLabel6.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								jumpGameList(mGameDetailRespBean.getData().getLabels().get(5).getName(), mGameDetailRespBean.getData().getLabels().get(5).getId());
							}
						});
					case 5:
						mBinding.txtLabel5.setText(mGameDetailRespBean.getData().getLabels().get(4).getName());
						mBinding.txtLabel5.setVisibility(View.VISIBLE);
						mBinding.txtLabel5.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								jumpGameList(mGameDetailRespBean.getData().getLabels().get(4).getName(), mGameDetailRespBean.getData().getLabels().get(4).getId());
							}
						});
					case 4:
						mBinding.txtLabel4.setText(mGameDetailRespBean.getData().getLabels().get(3).getName());
						mBinding.txtLabel4.setVisibility(View.VISIBLE);
						mBinding.txtLabel4.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								jumpGameList(mGameDetailRespBean.getData().getLabels().get(3).getName(), mGameDetailRespBean.getData().getLabels().get(3).getId());
							}
						});
					case 3:
						mBinding.txtLabel3.setText(mGameDetailRespBean.getData().getLabels().get(2).getName());
						mBinding.txtLabel3.setVisibility(View.VISIBLE);
						mBinding.txtLabel3.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								jumpGameList(mGameDetailRespBean.getData().getLabels().get(2).getName(), mGameDetailRespBean.getData().getLabels().get(2).getId());
							}
						});
					case 2:
						mBinding.txtLabel2.setText(mGameDetailRespBean.getData().getLabels().get(1).getName());
						mBinding.txtLabel2.setVisibility(View.VISIBLE);
						mBinding.txtLabel2.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								jumpGameList(mGameDetailRespBean.getData().getLabels().get(1).getName(), mGameDetailRespBean.getData().getLabels().get(1).getId());
							}
						});
					case 1:
						mBinding.txtLabel1.setText(mGameDetailRespBean.getData().getLabels().get(0).getName());
						mBinding.txtLabel1.setVisibility(View.VISIBLE);
						mBinding.txtLabel1.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								jumpGameList(mGameDetailRespBean.getData().getLabels().get(0).getName(), mGameDetailRespBean.getData().getLabels().get(0).getId());
							}
						});
				}
				mBinding.tvLabel.setVisibility(View.VISIBLE);
			}
		}else if(mGameDetailRespBean.getData().getShowtype()==21){//20手游专区21网游专区3单机
			mBinding.tvVersion.setText("类型：" +mGameDetailRespBean.getData().getType());
			mBinding.tvSytype.setText("状态：" +mGameDetailRespBean.getData().getState());
			mBinding.tvPublish.setText("公测：" +mGameDetailRespBean.getData().getBetatime());
		}else if(mGameDetailRespBean.getData().getShowtype()==20){
			mBinding.tvVersion.setText("版本：" +mGameDetailRespBean.getData().getVersion());
			mBinding.tvSytype.setText("类型：" +mGameDetailRespBean.getData().getType());
			mBinding.tvPublish.setText("厂商：" +mGameDetailRespBean.getData().getFirm());
		}
		

		//游戏简介
		final int[] okcprogress = {0};
		mBinding.tvGameInfo.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				//这个回调会调用多次，获取完行数记得注销监听
				mBinding.tvGameInfo.getViewTreeObserver().removeOnPreDrawListener(this);

				okcprogress[0] = mBinding.tvGameInfo.getLineCount();
				mBinding.tvGameInfo.setLines(4);
				if(okcprogress[0]<5){
					mBinding.moreImage.setVisibility(View.INVISIBLE);
				}
				return false;
			}
		});
		mBinding.tvGameInfo.setText(mGameDetailRespBean.getData().getContent());
		mBinding.moreImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isOpen) {//没打开
					isOpen = !isOpen;
//					int txtLength=mBinding.tvGameInfo.getText().toString().length();
//					double d = (double) txtLength / 21;//文本长度除以每行字符长度
//					int  okcprogress = (int) (Math.floor(d));//除数取整，也就是行数
					mBinding.tvGameInfo.setLines(okcprogress[0]+1);//展开全部
					mBinding.moreImage.setImageResource(R.drawable.img_tv_less);
				} else {
					isOpen = !isOpen;
					mBinding.tvGameInfo.setLines(4);//收缩为原始两行               isOpen = !isOpen;
					mBinding.moreImage.setImageResource(R.drawable.img_tv_more);
				}
			}
		});

		//游戏图片视频
		final List<String> imgs = mGameDetailRespBean.getData().getImgs();
		if (imgs.size() > 0) {
			mBinding.ryImgs.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));

			final CommonRecyclerAdapter mImageAdapter = new CommonRecyclerAdapter<GameDetailRespBean.DataBean.SmallimgsBean>(mContext, R.layout.adapter_game_video) {
				@Override
				public void bindData(RecyclerViewHolder holder, int position, GameDetailRespBean.DataBean.SmallimgsBean item) {
					holder.setImageByUrl(R.id.img_game, item.getUrl());
					if (position == selectID) {
						holder.getView(R.id.img_select).setVisibility(View.VISIBLE);
					} else {
						holder.getView(R.id.img_select).setVisibility(View.INVISIBLE);
					}
					if(item.getShowtype()==10){
						holder.getView(R.id.img_play).setVisibility(View.VISIBLE);
					}else{
						holder.getView(R.id.img_play).setVisibility(View.GONE);
					}
				}
			};
			mBinding.ryImgs.setAdapter(mImageAdapter);
			mImageAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
				@Override
				public void onItemClick(View itemView, int position) {
					mImageAdapter.notifyItemChanged(selectID);
					selectID = position;
					mImageAdapter.notifyItemChanged(position);
//					GlideUtil.loadImage(mContext,imgs.get(position),mBinding.imgVideo);
					Glide.with(mContext).load(imgs.get(position)).into(mBinding.imgVideo);
					if (mGameDetailRespBean.getData().getSmallimgs().get(position).getShowtype() == 10) {
//						mBinding.imgPlay.setVisibility(View.VISIBLE);
						mBinding.webVideo.setVisibility(View.VISIBLE);
						mBinding.imgVideo.setVisibility(View.INVISIBLE);
					} else {
//						mBinding.imgPlay.setVisibility(View.GONE);
						mBinding.webVideo.setVisibility(View.INVISIBLE);
						mBinding.imgVideo.setVisibility(View.VISIBLE);
					}
				}
			});
			mImageAdapter.clearAndAddList(mGameDetailRespBean.getData().getSmallimgs());
			GlideUtil.loadImage(mContext.getApplicationContext(), imgs.get(0), mBinding.imgVideo);//设置展示第一个
			if (mGameDetailRespBean.getData().getSmallimgs().get(0).getShowtype() == 10) {
				firGameType=game.getShowtype();
				game.setShowtype(10);//10标识为视频
			}else{
				firGameType=game.getShowtype();
			}

			mBinding.imgVideo.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					NewsBean video = mGameDetailRespBean.getData().getSmallimgs().get(selectID);
					if (video.getShowtype() == 10) {
						jumpNews(video);
					}
				}
			});
//			设置视频
			if(mGameDetailRespBean.getData().getSmallimgs().get(0).getShowtype() == 10){
				mBinding.webVideo.setVisibility(View.VISIBLE);
				mBinding.imgVideo.setVisibility(View.INVISIBLE);
				
				mBinding.webVideo.getSettings().setJavaScriptEnabled(true);
				mBinding.webVideo.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
				mBinding.webVideo.getSettings().setBlockNetworkImage(false);//解决图片不显示
				mBinding.webVideo.getSettings().setDomStorageEnabled(true);
				mBinding.webVideo.getSettings().setDatabaseEnabled(true);
				mBinding.webVideo.getSettings().setSupportZoom(true); // 支持缩放

				mBinding.webVideo.setWebViewClient(new WebViewClient());
				mBinding.webVideo.setWebChromeClient(new WebChromeClient(){
					@Override
					public void onShowCustomView(View view, CustomViewCallback callback) {
						showCustomView(view, callback);
					}

					@Override
					public void onHideCustomView() {
						hideCustomView();
					}
				});

				mBinding.webVideo.loadUrl(mGameDetailRespBean.getData().getSmallimgs().get(0).getArcurl());
			}
		} else {
			mBinding.rlImages.setVisibility(View.GONE);
		}

		//游戏配置
		int configSize=mGameDetailRespBean.getData().getHighconfig()==null?0:mGameDetailRespBean.getData().getHighconfig().size();
		if (configSize> 0) {
			mBinding.tvCon00.setText(mGameDetailRespBean.getData().getLowconfig().get(0).getTitle());
			mBinding.tvCon01.setText(mGameDetailRespBean.getData().getLowconfig().get(0).getContent());
			mBinding.tvCon10.setText(mGameDetailRespBean.getData().getLowconfig().get(1).getTitle());
			mBinding.tvCon11.setText(mGameDetailRespBean.getData().getLowconfig().get(1).getContent());
			mBinding.tvCon20.setText(mGameDetailRespBean.getData().getLowconfig().get(2).getTitle());
			mBinding.tvCon21.setText(mGameDetailRespBean.getData().getLowconfig().get(2).getContent());
			try {
				mBinding.tvCon30.setText(mGameDetailRespBean.getData().getLowconfig().get(3).getTitle());
				mBinding.tvCon31.setText(mGameDetailRespBean.getData().getLowconfig().get(3).getContent());
			} catch (Exception e) {
				mBinding.llCon3.setVisibility(View.GONE);
			}
			try {
				mBinding.tvCon40.setText(mGameDetailRespBean.getData().getLowconfig().get(4).getTitle());
				mBinding.tvCon41.setText(mGameDetailRespBean.getData().getLowconfig().get(4).getContent());
			} catch (Exception e) {
				mBinding.llCon4.setVisibility(View.GONE);
			}
			mBinding.tvHigh.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mBinding.tvHigh.setTextColor(getResources().getColor(R.color.red));
					mBinding.lineHigh.setVisibility(View.VISIBLE);
					mBinding.tvLow.setTextColor(getResources().getColor(R.color.gray_text));
					mBinding.lineLow.setVisibility(View.INVISIBLE);

					mBinding.tvCon00.setText(mGameDetailRespBean.getData().getHighconfig().get(0).getTitle());
					mBinding.tvCon01.setText(mGameDetailRespBean.getData().getHighconfig().get(0).getContent());
					mBinding.tvCon10.setText(mGameDetailRespBean.getData().getHighconfig().get(1).getTitle());
					mBinding.tvCon11.setText(mGameDetailRespBean.getData().getHighconfig().get(1).getContent());
					mBinding.tvCon20.setText(mGameDetailRespBean.getData().getHighconfig().get(2).getTitle());
					mBinding.tvCon21.setText(mGameDetailRespBean.getData().getHighconfig().get(2).getContent());
					try {
						mBinding.tvCon30.setText(mGameDetailRespBean.getData().getHighconfig().get(3).getTitle());
						mBinding.tvCon31.setText(mGameDetailRespBean.getData().getHighconfig().get(3).getContent());
					} catch (Exception e) {
						mBinding.llCon3.setVisibility(View.GONE);
					}
					try {
						mBinding.tvCon40.setText(mGameDetailRespBean.getData().getHighconfig().get(4).getTitle());
						mBinding.tvCon41.setText(mGameDetailRespBean.getData().getHighconfig().get(4).getContent());
					} catch (Exception e) {
						mBinding.llCon4.setVisibility(View.GONE);
					}
				}
			});
			mBinding.tvLow.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mBinding.tvLow.setTextColor(getResources().getColor(R.color.red));
					mBinding.lineLow.setVisibility(View.VISIBLE);
					mBinding.tvHigh.setTextColor(getResources().getColor(R.color.gray_text));
					mBinding.lineHigh.setVisibility(View.INVISIBLE);

					mBinding.tvCon00.setText(mGameDetailRespBean.getData().getLowconfig().get(0).getTitle());
					mBinding.tvCon01.setText(mGameDetailRespBean.getData().getLowconfig().get(0).getContent());
					mBinding.tvCon10.setText(mGameDetailRespBean.getData().getLowconfig().get(1).getTitle());
					mBinding.tvCon11.setText(mGameDetailRespBean.getData().getLowconfig().get(1).getContent());
					mBinding.tvCon20.setText(mGameDetailRespBean.getData().getLowconfig().get(2).getTitle());
					mBinding.tvCon21.setText(mGameDetailRespBean.getData().getLowconfig().get(2).getContent());
					try {
						mBinding.tvCon30.setText(mGameDetailRespBean.getData().getLowconfig().get(3).getTitle());
						mBinding.tvCon31.setText(mGameDetailRespBean.getData().getLowconfig().get(3).getContent());
					} catch (Exception e) {
						mBinding.llCon3.setVisibility(View.GONE);
					}
					try {
						mBinding.tvCon40.setText(mGameDetailRespBean.getData().getLowconfig().get(4).getTitle());
						mBinding.tvCon41.setText(mGameDetailRespBean.getData().getLowconfig().get(4).getContent());
					} catch (Exception e) {
						mBinding.llCon4.setVisibility(View.GONE);
					}
				}
			});
		} else {
			mBinding.rlConfig.setVisibility(View.GONE);
		}
		
		//图文攻略
		if(mGameDetailRespBean.getData().getGltxtlist()!=null&&mGameDetailRespBean.getData().getGltxtlist().size()>0){
			final List<GllistBean> mList=mGameDetailRespBean.getData().getGltxtlist();

			switch (mList.size()) {
				case 3:
					mBinding.tvGtitle3.setText(mList.get(2).getName());
					if(mList.get(2).getIs_more()==1){
						mBinding.moreTitle3.setVisibility(View.VISIBLE);
						mBinding.moreTitle3.setOnClickListener(new View.OnClickListener() {//最期待->发售  热门跳转排行
							@Override
							public void onClick(View view) {
								Intent intent = new Intent(mContext, GongLabelActivity.class);
								intent.putExtra("showtype", mGameDetailRespBean.getData().getShowtype());
								intent.putExtra("zq_id", mGameDetailRespBean.getData().getAid());
								intent.putExtra("aid", mList.get(2).getId());
								intent.putExtra("position", mList.get(2).getPosition());
								intent.putExtra("title", mList.get(2).getName());
								startActivity(intent);
							}
						});
					}else{
						mBinding.moreTitle3.setVisibility(View.GONE);
					}

					RecyclerView recyclerView3 = mBinding.ryTitle3;
					recyclerView3.setNestedScrollingEnabled(false);
					GridLayoutManager manager3 = new GridLayoutManager(mContext, 2);
					recyclerView3.setLayoutManager(manager3);

					final BaseRecyclerAdapter<GonglueBean> mSubRecyclerAdapter3 = new BaseRecyclerAdapter<GonglueBean>(mContext, R.layout.item_pop_label) {
						@Override
						public void bindData(RecyclerViewHolder holder, int position, GonglueBean child) {
							holder.setText(R.id.tv_name, child.getTitle());
						}
					};
					mSubRecyclerAdapter3.setOnClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
						@Override
						public void onItemClick(View itemView, int posi) {
							GonglueBean bean =mSubRecyclerAdapter3.getDataByPosition(posi);
							NewsBean news = new NewsBean();
							news.setTitle(bean.getTitle());
							news.setArcurl(bean.getArcurl());
							news.setWebviewurl(bean.getWebviewurl());
							news.setType("" + bean.getShowtype());
							Intent intent = new Intent(mContext, NewsActivity.class);
							intent.putExtra("isGongLue",1);
							Bundle bundle = new Bundle();
							bundle.putSerializable("newsBean", news);
							intent.putExtras(bundle);
							mContext.startActivity(intent);
						}
					});
					recyclerView3.setAdapter(mSubRecyclerAdapter3);
					mSubRecyclerAdapter3.clearAndAddList(mList.get(2).getList());
					mBinding.rlGong3.setVisibility(View.VISIBLE);
				case 2:
					mBinding.tvGtitle2.setText(mList.get(1).getName());
					if(mList.get(1).getIs_more()==1){
						mBinding.moreTitle2.setVisibility(View.VISIBLE);
						mBinding.moreTitle2.setOnClickListener(new View.OnClickListener() {//最期待->发售  热门跳转排行
							@Override
							public void onClick(View view) {
								Intent intent = new Intent(mContext, GongLabelActivity.class);
								intent.putExtra("showtype", mGameDetailRespBean.getData().getShowtype());
								intent.putExtra("zq_id", mGameDetailRespBean.getData().getAid());
								intent.putExtra("aid", mList.get(1).getId());
								intent.putExtra("position", mList.get(1).getPosition());
								intent.putExtra("title", mList.get(1).getName());
								startActivity(intent);
							}
						});
					}else{
						mBinding.moreTitle2.setVisibility(View.GONE);
					}

					RecyclerView recyclerView2 = mBinding.ryTitle2;
					recyclerView2.setNestedScrollingEnabled(false);
					GridLayoutManager manager2 = new GridLayoutManager(mContext, 2);
					recyclerView2.setLayoutManager(manager2);

					final BaseRecyclerAdapter<GonglueBean> mSubRecyclerAdapter2 = new BaseRecyclerAdapter<GonglueBean>(mContext, R.layout.item_pop_label) {
						@Override
						public void bindData(RecyclerViewHolder holder, int position, GonglueBean child) {
							holder.setText(R.id.tv_name, child.getTitle());
						}
					};
					mSubRecyclerAdapter2.setOnClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
						@Override
						public void onItemClick(View itemView, int posi) {
							GonglueBean bean =mSubRecyclerAdapter2.getDataByPosition(posi);
							NewsBean news = new NewsBean();
							news.setTitle(bean.getTitle());
							news.setArcurl(bean.getArcurl());
							news.setWebviewurl(bean.getWebviewurl());
							news.setType("" + bean.getShowtype());
							Intent intent = new Intent(mContext, NewsActivity.class);
							intent.putExtra("isGongLue",1);
							Bundle bundle = new Bundle();
							bundle.putSerializable("newsBean", news);
							intent.putExtras(bundle);
							mContext.startActivity(intent);
						}
					});
					recyclerView2.setAdapter(mSubRecyclerAdapter2);
					mSubRecyclerAdapter2.clearAndAddList(mList.get(1).getList());
					mBinding.rlGong2.setVisibility(View.VISIBLE);
				case 1:
					mBinding.tvGtitle1.setText(mList.get(0).getName());
					if(mList.get(0).getIs_more()==1){
						mBinding.moreTitle1.setVisibility(View.VISIBLE);
						mBinding.moreTitle1.setOnClickListener(new View.OnClickListener() {//最期待->发售  热门跳转排行
							@Override
							public void onClick(View view) {
								Intent intent = new Intent(mContext, GongLabelActivity.class);
								intent.putExtra("showtype", mGameDetailRespBean.getData().getShowtype());
								intent.putExtra("zq_id", mGameDetailRespBean.getData().getAid());
								intent.putExtra("aid", mList.get(0).getId());
								intent.putExtra("position", mList.get(0).getPosition());
								intent.putExtra("title", mList.get(0).getName());
								startActivity(intent);
							}
						});
					}else{
						mBinding.moreTitle1.setVisibility(View.GONE);
					}

					RecyclerView recyclerView1 = mBinding.ryTitle1;
					recyclerView1.setNestedScrollingEnabled(false);
					GridLayoutManager manager = new GridLayoutManager(mContext, 2);
					recyclerView1.setLayoutManager(manager);

					final BaseRecyclerAdapter<GonglueBean> mSubRecyclerAdapter1 = new BaseRecyclerAdapter<GonglueBean>(mContext, R.layout.item_pop_label) {
						@Override
						public void bindData(RecyclerViewHolder holder, int position, GonglueBean child) {
							holder.setText(R.id.tv_name, child.getTitle());
						}
					};
					mSubRecyclerAdapter1.setOnClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
						@Override
						public void onItemClick(View itemView, int posi) {
							GonglueBean bean =mSubRecyclerAdapter1.getDataByPosition(posi);
							NewsBean news = new NewsBean();
							news.setTitle(bean.getTitle());
							news.setArcurl(bean.getArcurl());
							news.setWebviewurl(bean.getWebviewurl());
							news.setType("" + bean.getShowtype());
							Intent intent = new Intent(mContext, NewsActivity.class);
							intent.putExtra("isGongLue",1);
							Bundle bundle = new Bundle();
							bundle.putSerializable("newsBean", news);
							intent.putExtras(bundle);
							mContext.startActivity(intent);
						}
					});
					recyclerView1.setAdapter(mSubRecyclerAdapter1);
					mSubRecyclerAdapter1.clearAndAddList(mList.get(0).getList());
					mBinding.rlGong1.setVisibility(View.VISIBLE);
					break;
			}
		}
		
		//角色图鉴
		if(mGameDetailRespBean.getData().getGlpictlist()!=null&&mGameDetailRespBean.getData().getGlpictlist().size()>0){
			final List<PiclistBean> mList=mGameDetailRespBean.getData().getGlpictlist();

			switch (mList.size()) {
				case 3:
					RecyclerView picRecyclerView3 =mBinding.ryTu3;
					picRecyclerView3.setNestedScrollingEnabled(false);
					mBinding.tvTutitle3.setText(mList.get(2).getName());
					if(mList.get(2).getIs_more()==1){
						mBinding.moreTutitle3.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(mContext, GongTuJianActivity.class);
								intent.putExtra("aid", mList.get(2).getId());
								intent.putExtra("showtype", mGameDetailRespBean.getData().getShowtype());
								intent.putExtra("title", mList.get(2).getName());
								startActivity(intent);
							}
						});
						mBinding.moreTutitle3.setVisibility(View.VISIBLE);
					}else{
						mBinding.moreTutitle3.setVisibility(View.VISIBLE);
					}
					GridLayoutManager layoutManager3 = new GridLayoutManager(mContext, 4);
					layoutManager3.setOrientation(GridLayoutManager.VERTICAL);
					picRecyclerView3.setLayoutManager(layoutManager3);
					final CommonRecyclerAdapter mPicAdapter3 = new CommonRecyclerAdapter<GonglueBean>(mContext, R.layout.item_mg) {
						@Override
						public void bindData(RecyclerViewHolder holder, int position, GonglueBean bean) {
							holder.setImageByUrl(R.id.img_top, bean.getLitpic());
							holder.setText(R.id.tv_title, bean.getTitle());
						}
					};
					mPicAdapter3.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
						@Override
						public void onItemClick(View itemView, int posi) {
							GonglueBean bean =mList.get(2).getList().get(posi);
							NewsBean news = new NewsBean();
							news.setTitle(bean.getTitle());
							news.setArcurl(bean.getArcurl());
							news.setWebviewurl(bean.getWebviewurl());
							news.setType("" + bean.getShowtype());
							Intent intent = new Intent(mContext, NewsActivity.class);
							intent.putExtra("isGongLue",1);
							Bundle bundle = new Bundle();
							bundle.putSerializable("newsBean", news);
							intent.putExtras(bundle);
							mContext.startActivity(intent);
						}
					});
					picRecyclerView3.setAdapter(mPicAdapter3);
					mPicAdapter3.clearAndAddList(mList.get(2).getList());
					mBinding.rlTu3.setVisibility(View.VISIBLE);
				case 2:
					RecyclerView picRecyclerView2 =mBinding.ryTu2;
					picRecyclerView2.setNestedScrollingEnabled(false);
					mBinding.tvTutitle2.setText(mList.get(1).getName());
					if(mList.get(0).getIs_more()==1){
						mBinding.moreTutitle2.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(mContext, GongTuJianActivity.class);
								intent.putExtra("aid", mList.get(1).getId());
								intent.putExtra("showtype", mGameDetailRespBean.getData().getShowtype());
								intent.putExtra("title", mList.get(1).getName());
								startActivity(intent);
							}
						});
						mBinding.moreTutitle2.setVisibility(View.VISIBLE);
					}else{
						mBinding.moreTutitle2.setVisibility(View.VISIBLE);
					}
					GridLayoutManager layoutManager2 = new GridLayoutManager(mContext, 4);
					layoutManager2.setOrientation(GridLayoutManager.VERTICAL);
					picRecyclerView2.setLayoutManager(layoutManager2);
					final CommonRecyclerAdapter mPicAdapter2 = new CommonRecyclerAdapter<GonglueBean>(mContext, R.layout.item_mg) {
						@Override
						public void bindData(RecyclerViewHolder holder, int position, GonglueBean bean) {
							holder.setImageByUrl(R.id.img_top, bean.getLitpic());
							holder.setText(R.id.tv_title, bean.getTitle());
						}
					};
					mPicAdapter2.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
						@Override
						public void onItemClick(View itemView, int posi) {
							GonglueBean bean =mList.get(1).getList().get(posi);
							NewsBean news = new NewsBean();
							news.setTitle(bean.getTitle());
							news.setArcurl(bean.getArcurl());
							news.setWebviewurl(bean.getWebviewurl());
							news.setType("" + bean.getShowtype());
							Intent intent = new Intent(mContext, NewsActivity.class);
							intent.putExtra("isGongLue",1);
							Bundle bundle = new Bundle();
							bundle.putSerializable("newsBean", news);
							intent.putExtras(bundle);
							mContext.startActivity(intent);
						}
					});
					picRecyclerView2.setAdapter(mPicAdapter2);
					mPicAdapter2.clearAndAddList(mList.get(1).getList());
					mBinding.rlTu2.setVisibility(View.VISIBLE);
				case 1:
					RecyclerView picRecyclerView =mBinding.ryTu1;
					picRecyclerView.setNestedScrollingEnabled(false);
					mBinding.tvTutitle1.setText(mList.get(0).getName());
					if(mList.get(0).getIs_more()==1){
						mBinding.moreTutitle1.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(mContext, GongTuJianActivity.class);
								intent.putExtra("aid", mList.get(0).getId());
								intent.putExtra("showtype", mGameDetailRespBean.getData().getShowtype());
								intent.putExtra("title", mList.get(0).getName());
								startActivity(intent);
							}
						});
						mBinding.moreTutitle1.setVisibility(View.VISIBLE);
					}else{
						mBinding.moreTutitle1.setVisibility(View.VISIBLE);
					}
					GridLayoutManager layoutManager1 = new GridLayoutManager(mContext, 4);
					layoutManager1.setOrientation(GridLayoutManager.VERTICAL);
					picRecyclerView.setLayoutManager(layoutManager1);
					final CommonRecyclerAdapter mPicAdapter1 = new CommonRecyclerAdapter<GonglueBean>(mContext, R.layout.item_mg) {
						@Override
						public void bindData(RecyclerViewHolder holder, int position, GonglueBean bean) {
							holder.setImageByUrl(R.id.img_top, bean.getLitpic());
							holder.setText(R.id.tv_title, bean.getTitle());
						}
					};
					mPicAdapter1.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
						@Override
						public void onItemClick(View itemView, int posi) {
							GonglueBean bean =mList.get(0).getList().get(posi);
							NewsBean news = new NewsBean();
							news.setTitle(bean.getTitle());
							news.setArcurl(bean.getArcurl());
							news.setWebviewurl(bean.getWebviewurl());
							news.setType("" + bean.getShowtype());
							Intent intent = new Intent(mContext, NewsActivity.class);
							intent.putExtra("isGongLue",1);
							Bundle bundle = new Bundle();
							bundle.putSerializable("newsBean", news);
							intent.putExtras(bundle);
							mContext.startActivity(intent);
						}
					});
					picRecyclerView.setAdapter(mPicAdapter1);
					mPicAdapter1.clearAndAddList(mList.get(0).getList());
					mBinding.rlTu1.setVisibility(View.VISIBLE);
					break;
			}
		}
		
		//礼包信息
		if(mGameDetailRespBean.getData().getLibao()!=null&&mGameDetailRespBean.getData().getLibao().size()>0){
			final List<GameGiftBean> list=mGameDetailRespBean.getData().getLibao();

			switch (list.size()) {
				case 3:
					if(mGameDetailRespBean.getData().getShowtype()==20) {//20手游专区21网游专区3单机
						//改变图片尺寸
						ViewGroup.LayoutParams mParams = mBinding.giftImg3.getLayoutParams();
						mParams.height = (int) getResources().getDimension(R.dimen.element_margin_80);
						mParams.width = (int) getResources().getDimension(R.dimen.element_margin_80);
						mBinding.giftImg3.setLayoutParams(mParams);
						mBinding.giftImg3.setScaleType(ImageView.ScaleType.FIT_XY);
						mBinding.giftImg3.setBorderWidth(0f);
					}
					GlideUtil.loadImage(mContext, list.get(2).getLitpic(), mBinding.giftImg3);
					mBinding.giftName3.setText(list.get(2).getTitle());
					mBinding.giftTime3.setText(list.get(2).getRange_start()+" 至 "+list.get(2).getRange_end());
					mBinding.giftName3.setText(list.get(2).getTitle());
					mBinding.tvRemain3.setText("剩余："+list.get(2).getSurplusper()+"%");
					ImageView imgScore3=mBinding.imgScore3;
					// 获得ClipDrawable对象  
					ClipDrawable clipDrawable3 = (ClipDrawable) imgScore3.getBackground();
					// 设置裁剪级别，Clip类型的图片默认裁剪级别为0，此时是全部裁剪，图片看不见；  
					// 当级别为10000时，不裁剪图片，图片全部可见  
					// 当全部显示后，设置不可见  
					clipDrawable3.setLevel(list.get(2).getSurplusper()*100);
					mBinding.rlGift3.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(mContext, GiftDetailActivity.class);
							intent.putExtra("pageType",game.getShowtype()==20?1:0);//pageType //0 网游礼包  1 手游礼包  //20手游专区21网游专区3单机
							Bundle bundle = new Bundle();
							bundle.putSerializable("mGameGiftBean",list.get(2));
							intent.putExtras(bundle);
							startActivity(intent);
						}
					});
					mBinding.lineGift3.setVisibility(View.VISIBLE);
					mBinding.rlGift3.setVisibility(View.VISIBLE);
				case 2:
					if(mGameDetailRespBean.getData().getShowtype()==20) {//20手游专区21网游专区3单机
						//改变图片尺寸
						ViewGroup.LayoutParams mParams = mBinding.giftImg2.getLayoutParams();
						mParams.height = (int) getResources().getDimension(R.dimen.element_margin_80);
						mParams.width = (int) getResources().getDimension(R.dimen.element_margin_80);
						mBinding.giftImg2.setLayoutParams(mParams);
						mBinding.giftImg2.setScaleType(ImageView.ScaleType.FIT_XY);
						mBinding.giftImg2.setBorderWidth(0f);
					}
					GlideUtil.loadImage(mContext, list.get(1).getLitpic(), mBinding.giftImg2);
					mBinding.giftName2.setText(list.get(1).getTitle());
					mBinding.giftTime2.setText(list.get(1).getRange_start()+" 至 "+list.get(1).getRange_end());
					mBinding.giftName2.setText(list.get(1).getTitle());
					mBinding.tvRemain2.setText("剩余："+list.get(1).getSurplusper()+"%");
					ImageView imgScore2=mBinding.imgScore2;
					// 获得ClipDrawable对象  
					ClipDrawable clipDrawable2 = (ClipDrawable) imgScore2.getBackground();
					// 设置裁剪级别，Clip类型的图片默认裁剪级别为0，此时是全部裁剪，图片看不见；  
					// 当级别为10000时，不裁剪图片，图片全部可见  
					// 当全部显示后，设置不可见  
					clipDrawable2.setLevel(list.get(1).getSurplusper()*100);
					mBinding.rlGift2.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(mContext, GiftDetailActivity.class);
							intent.putExtra("pageType",game.getShowtype()==20?1:0);//pageType //0 网游礼包  1 手游礼包  //20手游专区21网游专区3单机
							Bundle bundle = new Bundle();
							bundle.putSerializable("mGameGiftBean",list.get(1));
							intent.putExtras(bundle);
							startActivity(intent);
						}
					});
					mBinding.lineGift2.setVisibility(View.VISIBLE);
					mBinding.rlGift2.setVisibility(View.VISIBLE);
				case 1:
					if(mGameDetailRespBean.getData().getShowtype()==20) {//20手游专区21网游专区3单机
						//改变图片尺寸
						ViewGroup.LayoutParams mParams = mBinding.giftImg1.getLayoutParams();
						mParams.height = (int) getResources().getDimension(R.dimen.element_margin_80);
						mParams.width = (int) getResources().getDimension(R.dimen.element_margin_80);
						mBinding.giftImg1.setLayoutParams(mParams);
						mBinding.giftImg1.setScaleType(ImageView.ScaleType.FIT_XY);
						mBinding.giftImg1.setBorderWidth(0f);
					}
					GlideUtil.loadImage(mContext, list.get(0).getLitpic(), mBinding.giftImg1);
					mBinding.giftName1.setText(list.get(0).getTitle());
					mBinding.giftTime1.setText(list.get(0).getRange_start()+" 至 "+list.get(0).getRange_end());
					mBinding.giftName1.setText(list.get(0).getTitle());

					mBinding.tvRemain1.setText("剩余："+list.get(0).getSurplusper()+"%");
					ImageView imgScore1=mBinding.imgScore1;
					// 获得ClipDrawable对象  
					ClipDrawable clipDrawable1 = (ClipDrawable) imgScore1.getBackground();
					// 当级别为10000时，不裁剪图片，图片全部可见  
					// 当全部显示后，设置不可见  
					clipDrawable1.setLevel(list.get(0).getSurplusper()*100);
					mBinding.rlGift1.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(mContext, GiftDetailActivity.class);
							intent.putExtra("pageType",game.getShowtype()==20?1:0);//pageType //0 网游礼包  1 手游礼包  //20手游专区21网游专区3单机
							Bundle bundle = new Bundle();
							bundle.putSerializable("mGameGiftBean",list.get(0));
							intent.putExtras(bundle);
							startActivity(intent);
						}
					});
					mBinding.rlGift1.setVisibility(View.VISIBLE);
					break;
			}
			
			mBinding.tvMoreGift.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, GiftRelativeActivity.class);
					if(game.getShowtype()==20)//20手游专区21网游专区3单机
						intent.putExtra("giftType",1);
					intent.putExtra("title",mGameDetailRespBean.getData().getTitle());
					intent.putExtra("aid",mGameDetailRespBean.getData().getAid());
					startActivity(intent);
				}
			});//礼包的更多
			
			mBinding.llGift.setVisibility(View.VISIBLE);
			mBinding.llGiftContent.setVisibility(View.VISIBLE);
		}

		//游戏攻略
		switch (mGameDetailRespBean.getData().getGllist().size()) {
			case 3:
				mBinding.gTitle3.setText(mGameDetailRespBean.getData().getGllist().get(2).getTitle());
				mBinding.gTime3.setText(TimeUtil.getTimeEN(mGameDetailRespBean.getData().getGllist().get(2).getPubdate_at()));
				mBinding.gComment3.setText(mGameDetailRespBean.getData().getGllist().get(2).getTotal_ct() + "");
				GlideUtil.loadImage(mContext.getApplicationContext(), mGameDetailRespBean.getData().getGllist().get(2).getLitpic(), mBinding.gImg3);
				mBinding.lineG3.setVisibility(View.VISIBLE);
				mBinding.gNews3.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						jumpNews(mGameDetailRespBean.getData().getGllist().get(2));
					}
				});
				mBinding.gNews3.setVisibility(View.VISIBLE);
			case 2:
				mBinding.gTitle2.setText(mGameDetailRespBean.getData().getGllist().get(1).getTitle());
				mBinding.gTime2.setText(TimeUtil.getTimeEN(mGameDetailRespBean.getData().getGllist().get(1).getPubdate_at()));
				mBinding.gComment2.setText(mGameDetailRespBean.getData().getGllist().get(1).getTotal_ct() + "");
				GlideUtil.loadImage(mContext.getApplicationContext(), mGameDetailRespBean.getData().getGllist().get(1).getLitpic(), mBinding.gImg2);
				mBinding.lineG2.setVisibility(View.VISIBLE);
				mBinding.gNews2.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						jumpNews(mGameDetailRespBean.getData().getGllist().get(1));
					}
				});
				mBinding.gNews2.setVisibility(View.VISIBLE);
			case 1:
				mBinding.gTitle1.setText(mGameDetailRespBean.getData().getGllist().get(0).getTitle());
				mBinding.gTime1.setText(TimeUtil.getTimeEN(mGameDetailRespBean.getData().getGllist().get(0).getPubdate_at()));
				mBinding.gComment1.setText(mGameDetailRespBean.getData().getGllist().get(0).getTotal_ct() + "");
				GlideUtil.loadImage(mContext.getApplicationContext(), mGameDetailRespBean.getData().getGllist().get(0).getLitpic(), mBinding.gImg1);
				mBinding.gNews1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						jumpNews(mGameDetailRespBean.getData().getGllist().get(0));
					}
				});
				break;
			default:
				mBinding.rlGonglue.setVisibility(View.GONE);
				break;
		}

		//游戏评测
		if (mGameDetailRespBean.getData().getEvaluatelist().size() > 0) {
			mBinding.tvSyscore.setText(mGameDetailRespBean.getData().getEvaluatelist().get(0).getScore()==0?"待补充":mGameDetailRespBean.getData().getEvaluatelist().get(0).getScore()+"");
			mBinding.tvSycontent.setText(mGameDetailRespBean.getData().getEvaluatelist().get(0).getTitle());
			mBinding.rlSyPince.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, OriginalActivity.class);
					Bundle bundleNews = new Bundle();
					bundleNews.putSerializable("originalBean", mGameDetailRespBean.getData().getEvaluatelist().get(0));
					intent.putExtras(bundleNews);
					mContext.startActivity(intent);
					
				}
			});
			mBinding.rlSyPince.setVisibility(View.VISIBLE);
		}

		//相关资讯
		switch (mGameDetailRespBean.getData().getNewslist().size()) {
			case 3:
				mBinding.zTitle3.setText(mGameDetailRespBean.getData().getNewslist().get(2).getTitle());
				mBinding.zTime3.setText(TimeUtil.getTimeEN(mGameDetailRespBean.getData().getNewslist().get(2).getPubdate_at()));
				mBinding.zComment3.setText(mGameDetailRespBean.getData().getNewslist().get(2).getTotal_ct() + "");
				GlideUtil.loadImage(mContext.getApplicationContext(), mGameDetailRespBean.getData().getNewslist().get(2).getLitpic(), mBinding.zImg3);
				mBinding.zNews3.setVisibility(View.VISIBLE);
				mBinding.lineZ3.setVisibility(View.VISIBLE);
				mBinding.zNews3.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						jumpNews(mGameDetailRespBean.getData().getNewslist().get(2));
					}
				});
			case 2:
				mBinding.zTitle2.setText(mGameDetailRespBean.getData().getNewslist().get(1).getTitle());
				mBinding.zTime2.setText(TimeUtil.getTimeEN(mGameDetailRespBean.getData().getNewslist().get(1).getPubdate_at()));
				mBinding.zComment2.setText(mGameDetailRespBean.getData().getNewslist().get(1).getTotal_ct() + "");
				GlideUtil.loadImage(mContext.getApplicationContext(), mGameDetailRespBean.getData().getNewslist().get(1).getLitpic(), mBinding.zImg2);
				mBinding.zNews2.setVisibility(View.VISIBLE);
				mBinding.lineZ2.setVisibility(View.VISIBLE);
				mBinding.zNews2.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						jumpNews(mGameDetailRespBean.getData().getNewslist().get(1));
					}
				});
			case 1:
				mBinding.zTitle1.setText(mGameDetailRespBean.getData().getNewslist().get(0).getTitle());
				mBinding.zTime1.setText(TimeUtil.getTimeEN(mGameDetailRespBean.getData().getNewslist().get(0).getPubdate_at()));
				mBinding.zComment1.setText(mGameDetailRespBean.getData().getNewslist().get(0).getTotal_ct() + "");
				GlideUtil.loadImage(mContext.getApplicationContext(), mGameDetailRespBean.getData().getNewslist().get(0).getLitpic(), mBinding.zImg1);
				mBinding.zNews1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						jumpNews(mGameDetailRespBean.getData().getNewslist().get(0));
					}
				});
				break;
			default:
				mBinding.rlZixun.setVisibility(View.GONE);
				break;
		}

		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.mRecycleView.setLayoutManager(layoutManager);
		mBinding.mRecycleView.setPullRefreshEnabled(false);
		mBinding.mRecycleView.setLoadingMoreEnabled(false);
		mBinding.mRecycleView.setNestedScrollingEnabled(false);

		mAdapter = new CommonRecyclerAdapter<CommentBean>(mContext, R.layout.adapter_comments) {
			@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
			@Override
			public void bindData(RecyclerViewHolder holder, final int position, final CommentBean comments) {
				if (position == 0) {
					holder.getView(R.id.rb2).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							updateComment(2);
						}
					});
					holder.getView(R.id.rb1).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							updateComment(1);
						}
					});
					holder.getView(R.id.rg_order).setVisibility(View.VISIBLE);

					holder.getView(R.id.llAllComments).setVisibility(View.VISIBLE);
					holder.setText(R.id.tv_comment_title, "所有评论");
				} else {
					holder.getView(R.id.llAllComments).setVisibility(View.GONE);
				}
//				holder.setImageByUrl(R.id.img_head,comments.getUser().avatarstr);
//				holder.setText(R.id.tv_name,comments.getUser().nickname);
//				holder.setText(R.id.tv_locate,"第"+comments.getPosition()+"楼  "+comments.getUser().regionstr+" 网友");
				GlideUtil.loadCircleImage(mContext, comments.getUser().avatarstr, (ImageView) holder.getView(R.id.img_head));
				holder.setText(R.id.tv_time, TimeUtil.getFoolishTime(comments.getPubdate_at()));
				holder.setText(R.id.tv_name, comments.getUser().nickname);
				holder.setText(R.id.tv_locate, comments.getUser().regionstr.length() > 0 ? comments.getUser().regionstr + " 网友" : "");
				TextView tv_head_title = holder.getView(R.id.tv_head_title);
				tv_head_title.setTextColor(getResources().getColor(R.color.lightBlack));
				switch (comments.getUser().title_level) {
					case 0:
					case 1:
						tv_head_title.setTextColor(getResources().getColor(R.color.white));
						tv_head_title.setBackground(getResources().getDrawable(R.drawable.ic_title1));
						break;
					case 2:
						tv_head_title.setBackground(getResources().getDrawable(R.drawable.ic_title2));
						break;
					case 3:
						tv_head_title.setBackground(getResources().getDrawable(R.drawable.ic_title3));
						break;
					case 4:
						tv_head_title.setBackground(getResources().getDrawable(R.drawable.ic_title4));
						break;
				}
				tv_head_title.setText("" + comments.getUser().title);
				holder.setText(R.id.tv_posi, "" + comments.getPosition());
				holder.setText(R.id.tv_good, comments.getGoodcount() + "");
//				holder.setText(R.id.tv_bad,comments.getBadcount()+"");
				TextView content = holder.getView(R.id.tv_content);
				content.setText(emoutil.addSmileySpans(emoutil.addSmileySpans(comments.getContent())));
//				holder.setText(R.id.tv_content,comments.getContent());
				final TextView tvGood = holder.getView(R.id.tv_good);
				final ImageView imgGood = holder.getView(R.id.imgGood);
				holder.getView(R.id.imgGood).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if (MyApplication.getUserData().loginStatue) {
							if (MyApplication.getUserData().mobile.length() == 0) {
								Intent intent = new Intent(mContext, ForgetPassActivity.class);
								intent.putExtra("type", 1);//1,绑定手机号，2修改密码，3忘记密码
								startActivity(intent);
							} else {
								if (comments.getPraise() == 0)
									setPraise(imgGood, tvGood, comments.getGoodcount(), comments.getId(), 1);//1赞2踩
							}
						} else {
							ToastUtil.showToast(mContext, "请先登录！");
							startActivity(new Intent(mContext, LoginActivity.class));
						}
					}
				});
//				final TextView tvGad=holder.getView(R.id.tv_bad);
//				final ImageView imgBad=holder.getView(R.id.imgBad);
//				holder.getView(R.id.imgBad).setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View view) {
//						if(comments.getPraise()==0)
//							setPraise(imgBad,tvGad,comments.getBadcount(),comments.getId(),2);//1赞2踩
//					}
//				});
				holder.setImageResource(R.id.imgGood, R.drawable.click_good_grey);
//				holder.setImageResource(R.id.imgBad,R.drawable.click_bad_grey);
				switch (comments.getPraise()) {
					case 1:
						holder.setImageResource(R.id.imgGood, R.drawable.click_good_red);
						break;
					case 2:
						holder.setImageResource(R.id.imgBad, R.drawable.click_bad_red);
						break;
				}
				holder.getView(R.id.tv_report).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						submitReport(comments.getId());
					}
				});

				switch (comments.getUser().getAuth_level()) {
					case 0:
					case 1:
						tv_head_title.setTextColor(getResources().getColor(R.color.white));
						tv_head_title.setBackground(getResources().getDrawable(R.drawable.bg_chengse));
						break;
					case 2:
						tv_head_title.setTextColor(getResources().getColor(R.color.white));
						tv_head_title.setBackground(getResources().getDrawable(R.drawable.bg_green));
						break;
					case 3:
						tv_head_title.setTextColor(getResources().getColor(R.color.white));
						tv_head_title.setBackground(getResources().getDrawable(R.drawable.bg_gray));
						break;
					case 4:
						tv_head_title.setTextColor(getResources().getColor(R.color.white));
						tv_head_title.setBackground(getResources().getDrawable(R.drawable.bg_black));
						break;
				}
				if(StringUtil.isEmpty(comments.getUser().getAuth_title())){
					tv_head_title.setTextColor(Color.parseColor("#A10000"));
					tv_head_title.setBackground(getResources().getDrawable(R.drawable.bg_black));
					holder.getView(R.id.user_lv).setVisibility(View.VISIBLE);
					tv_head_title.setVisibility(View.GONE);
					TextView view = holder.getView(R.id.user_lv);
					view.setText("Lv." +comments.getUser().getUser_level());
				}else{
					tv_head_title.setText("" + comments.getUser().getAuth_title());
					holder.getView(R.id.user_lv).setVisibility(View.GONE);
					tv_head_title.setVisibility(View.VISIBLE);
				}
				TextView tv_reply = holder.getView(R.id.tv_reply);
				tv_reply.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if (MyApplication.getUserData() == null || !MyApplication.getUserData().loginStatue) {
							ToastUtil.showToast(mContext, "请先登录！");
							startActivity(new Intent(mContext, LoginActivity.class));
						} else if (MyApplication.getUserData().mobile.length() == 0) {//第三方登陆，非绑定跳转绑定界面
							Intent intent = new Intent(mContext, ForgetPassActivity.class);
							intent.putExtra("type", 1);//1,绑定手机号，2修改密码，3忘记密码
							startActivity(intent);
						} else {
							replyId = comments.getId();
							// 获取编辑框焦点
							mBinding.etComment.setFocusable(true);
							//打开软键盘
							final InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
							mBinding.etComment.requestFocus();
						}
					}
				});

				if (comments.getReplies() != null && comments.getReplies().size() > 0) {
					XRecyclerView recyclerView = holder.getView(R.id.mRecycleView);
					LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
					layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
					recyclerView.setLayoutManager(layoutManager);
					recyclerView.setNestedScrollingEnabled(false);
					recyclerView.setLoadingMoreEnabled(false);
					recyclerView.setPullRefreshEnabled(false);

					BaseRecyclerAdapter<CommentBean> mSubRecyclerAdapter = new BaseRecyclerAdapter<CommentBean>(mContext, R.layout.adapter_child_comments) {
						@Override
						public void bindData(final RecyclerViewHolder holder, final int position, final CommentBean bean) {
							holder.setText(R.id.tv_time, TimeUtil.getFoolishTime(bean.getPubdate_at()));
							holder.setText(R.id.tv_locate, bean.getUser().regionstr.length() > 0 ? bean.getUser().regionstr + " 网友" : "");
//							holder.setText(R.id.tv_name,bean.getUser().nickname);
//							holder.setText(R.id.tv_content,""+bean.getContent());
							TextView content = holder.getView(R.id.tv_content);
							String str = "<font color='#00a0e9'>" + bean.getUser().nickname + "</font>" + " : " + bean.getContent();
							content.setText(emoutil.addSmileySpans(Html.fromHtml(str)));
//							TextView tv_head_title=holder.getView(R.id.tv_head_title);
//							switch (comments.getUser().title_level) {
//								case 0:
//								case 1:
//									tv_head_title.setBackground(getResources().getDrawable(R.drawable.ic_title1));
//									break;
//								case 2:
//									tv_head_title.setBackground(getResources().getDrawable(R.drawable.ic_title1));
//									break;
//								case 3:
//									tv_head_title.setBackground(getResources().getDrawable(R.drawable.ic_title1));
//									break;
//								case 4:
//									tv_head_title.setBackground(getResources().getDrawable(R.drawable.ic_title1));
//									break;
//							}
//							tv_head_title.setText(""+comments.getUser().title);
//							设置子品论点赞数目
							holder.setText(R.id.tv_num_good, bean.getGoodcount() + "");
							final TextView tvGood = holder.getView(R.id.tv_num_good);
							final ImageView imgGood = holder.getView(R.id.imgGood);
							holder.getView(R.id.imgGood).setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View view) {
									if (bean.getPraise() == 0)
										setPraise(imgGood, tvGood, bean.getGoodcount(), bean.getId(), 1);//1赞2踩
								}
							});
							holder.setImageResource(R.id.imgGood, R.drawable.click_good_grey);
//							holder.setText(R.id.tv_num_bad,bean.getBadcount()+"");
//							final TextView tvGad=holder.getView(R.id.tv_num_bad);
//							final ImageView imgBad=holder.getView(R.id.imgBad);
//							holder.getView(R.id.imgBad).setOnClickListener(new View.OnClickListener() {
//								@Override
//								public void onClick(View view) {
//									if(bean.getPraise()==0)
//										setPraise(imgBad,tvGad,bean.getBadcount(),bean.getId(),2);//1赞2踩
//								}
//							});
//							holder.setImageResource(R.id.imgBad,R.drawable.click_bad_grey);
							switch (bean.getPraise()) {
								case 1:
									holder.setImageResource(R.id.imgGood, R.drawable.click_good_red);
									break;
								case 2:
									holder.setImageResource(R.id.imgBad, R.drawable.click_bad_red);
									break;
							}

							TextView txtComment = holder.getView(R.id.tv_reply);
							txtComment.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View view) {
									if (MyApplication.getUserData() == null || !MyApplication.getUserData().loginStatue) {
										ToastUtil.showToast(mContext, "请先登录！");
										startActivity(new Intent(mContext, LoginActivity.class));
									} else if (MyApplication.getUserData().mobile.length() == 0) {//第三方登陆，非绑定跳转绑定界面
										Intent intent = new Intent(mContext, ForgetPassActivity.class);
										intent.putExtra("type", 1);//1,绑定手机号，2修改密码，3忘记密码
										startActivity(intent);
									} else {
										replyId = comments.getId();
										// 获取编辑框焦点
										mBinding.etComment.setFocusable(true);
										//打开软键盘
										final InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
										imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
										mBinding.etComment.requestFocus();
									}
								}
							});

						}
					};
					recyclerView.setAdapter(mSubRecyclerAdapter);
					List<CommentBean> newList = comments.getReplies();
					Collections.sort(newList, new Comparator<CommentBean>() {
						public int compare(CommentBean o1, CommentBean o2) {
							return o1.getPubdate_at() - o2.getPubdate_at();
						}
					});
					mSubRecyclerAdapter.clearAndAddList(newList);
					holder.getView(R.id.mRecycleView).setVisibility(View.VISIBLE);
				} else {
					holder.getView(R.id.mRecycleView).setVisibility(View.GONE);
				}
			}
		};
		mBinding.mRecycleView.setLoadingListener(new XRecyclerView.LoadingListener() {
			@Override
			public void onRefresh() {
				mBinding.mRecycleView.refreshComplete();
			}

			@Override
			public void onLoadMore() {
				new Handler().postDelayed(new Runnable() {
					public void run() {
//						obtainComments();
					}
				}, 50);
			}
		});
		mBinding.mRecycleView.setAdapter(mAdapter);

		obtainComments();
	}

	public void submitReport(int id) {//提交举报内容
		String uid = MyApplication.getUserData().loginStatue ? MyApplication.getUserData().uid : "0";
		String arcurl = game.getArcurl();
		long time = System.currentTimeMillis();
		String validate = uid+id + time;
		String sign = StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid", uid);
			obj.put("id", id);
			obj.put("arcurl", arcurl);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		NewsService.Api service = retrofit.create(NewsService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());

		Call<ResponseBody> call = service.report(body);
		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				try {
					JSONObject jsonObject = new JSONObject(response.body().string());
					if (jsonObject.optInt("code") == 1) {
						ToastUtil.showToast(mContext, "举报提交成功！");
					} else {
						ToastUtil.showToast(mContext, jsonObject.opt("msg") + "");
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
	private void updateComment(int commentType) {
		ArrayList<CommentBean> mCommentList = new ArrayList<CommentBean>();
		if (commentType == 1) {//1:最早 
			if (myCommentData.getData().getTimelist() != null && myCommentData.getData().getTimelist().size() > 0) {
				myCommentData.getData().getTimelist().get(0).setType("所有评论");
				mCommentList.addAll(myCommentData.getData().getTimelist());
			}

			mAdapter.clearAndAddList(mCommentList);
		} else {//最热
			if (myCommentData.getData().getList() != null && myCommentData.getData().getList().size() > 0) {
				myCommentData.getData().getList().get(0).setType("所有评论");
				mCommentList.addAll(myCommentData.getData().getList());
			}

			mAdapter.clearAndAddList(mCommentList);
		}
	}

	public void jumpGameList(String title, String id) {//跳转标签列表
		Intent intent = new Intent(mContext, GameCategoryListActivity.class);
		intent.putExtra("title", title);
		intent.putExtra("labelid", id);
		mContext.startActivity(intent);
	}

	public void jumpNews(NewsBean news) {//跳转标签列表
		Intent intent = new Intent(mContext, NewsActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("newsBean", news);
		intent.putExtras(bundle);
		mContext.startActivity(intent);
	}

	public void obtainComments() {//获取评论内容
		//获取数据
		String str_uid = MyApplication.getUserData().loginStatue ? MyApplication.getUserData().uid : "0";
		int uid = Integer.parseInt(str_uid);
		String arcurl = game.getArcurl();
		long time = System.currentTimeMillis();
		String validate = uid + arcurl + f_sid + 3 + mPage + time;
		String sign = StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid", uid);
			obj.put("arcurl", arcurl);
			obj.put("c_sid", f_sid);
			obj.put("pagesize", 3);
			obj.put("page", mPage);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
//		mNewsPresenter.getCommentList(obj.toString());//异步请求
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		NewsService.Api service = retrofit.create(NewsService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());

		Call<NewsHotCommentRespBean> call = service.getHotComment(body);
		call.enqueue(new Callback<NewsHotCommentRespBean>() {
			@Override
			public void onResponse(Call<NewsHotCommentRespBean> call, Response<NewsHotCommentRespBean> response) {
				Log.e("requestSuccess", "%-----------------------" + response.body());
				if (response.body() != null && response.body().getData() != null) {
					initComments(response.body());
				}
			}

			@Override
			public void onFailure(Call<NewsHotCommentRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
				mBinding.mRecycleView.loadMoreError();
			}
		});
	}

	// 初始化评论
	private void initComments(NewsHotCommentRespBean commentData) {
		if (commentData.getData().getFavorite() == 1) {//收藏状态1已收藏0未收藏
			mBinding.imgCollect.setImageResource(R.drawable.collect);
			favorite = 1;
		} else {
			mBinding.imgCollect.setImageResource(R.drawable.click_collect);
			favorite = 2;
		}

		mBinding.mRecycleView.refreshComplete();
		if (commentData == null || commentData.getData().getList() == null || commentData.getData().getList().size() == 0) {
			mBinding.mRecycleView.setLoadingMoreEnabled(false);
			mBinding.noData.setVisibility(View.VISIBLE);
			mBinding.txtComment.setText(0 + "");
			return;
		}
//		mBinding.rlComment.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				GameHomeActivity.viewPager.setCurrentItem(1);
//			}
//		});
		myCommentData = commentData;
		listData = commentData.getData().getList();
		mBinding.txtComment.setText("" + commentData.getData().getTotal());
		mBinding.txtComment.setVisibility(View.VISIBLE);
		mAdapter.clearAndAddList(listData);
		mBinding.llMore.setVisibility(View.VISIBLE);
		if (listData == null || listData.size() < 1) {
			mBinding.mRecycleView.setNoMore(true);
		} else {
			mPage++;
		}
	}
//	
//	public void getFavoriteStute(){//获取收藏状态
//		String temp = MyApplication.getUserData().loginStatue ? MyApplication.getUserData().uid : "0";
//		int uid=Integer.parseInt(temp);
//		String arcurl = game.getArcurl();
//		long time=System.currentTimeMillis();
//		String validate= ""+uid+arcurl+f_sid+time;
//		String sign= StringUtil.MD5(validate);
//		JSONObject obj = new JSONObject();
//		try {
//			obj.put("uid", MyApplication.getUserData().uid);
//			obj.put("arcurl",arcurl);
//			obj.put("f_sid",f_sid);
//			obj.put("time", time);
//			obj.put("sign", sign);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		//同步请求
//		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
//		UserService.Api service = retrofit.create(UserService.Api.class);
//		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
//		Call<ResponseBody> call = service.getArcFavorite(body);
//		call.enqueue(new Callback<ResponseBody>() {
//			@Override
//			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//				Log.e("requestSuccess", "-----------------------" + response.body());
//				try {
//					JSONObject jsonObject=new JSONObject(response.body().string());
//					if(jsonObject.optInt("code")==1){
//						JSONObject jsonSub=new JSONObject(jsonObject.opt("data").toString());
//						f_sid=jsonSub.optInt("f_sid");
//						if(jsonSub.optInt("favorite")==1){//收藏状态1已收藏0未收藏
//							mBinding.imgCollect.setImageResource(R.drawable.collect);
//							favorite=1;
//						}else{
//							mBinding.imgCollect.setImageResource(R.drawable.click_collect);
//							favorite=2;
//						}
////					}else{
////						ToastUtil.showToast(mContext,jsonObject.opt("msg")+"");
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//
//			@Override
//			public void onFailure(Call<ResponseBody> call, Throwable throwable) {
//				Log.e("requestFailure", throwable.getMessage() + "");
//			}
//		});
//	}

	//点赞，踩 接口
	public void setPraise(final ImageView targetImg, final TextView targetView, final int firstNum, int id, final int type) {//目标textview，初始数目，评论id，点赞类型:1赞2踩
		String temp = MyApplication.getUserData().loginStatue ? MyApplication.getUserData().uid : "0";
		int uid = Integer.parseInt(temp);
		long time = System.currentTimeMillis();
		String validate = "" + uid + id + type + time;
		String sign = StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid", MyApplication.getUserData().uid);
			obj.put("id", id);
			obj.put("arcurl", game.getArcurl());
			obj.put("type", type);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		UserService.Api service = retrofit.create(UserService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<ResponseBody> call = service.addPraise(body);
		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				try {
					JSONObject jsonObject = new JSONObject(response.body().string());
					if (jsonObject.optInt("code") == 1) {
						targetView.setText(firstNum + 1 + "");
						if (type == 1)
							targetImg.setImageResource(R.drawable.click_good_red);
						else
							targetImg.setImageResource(R.drawable.click_bad_red);
					} else {
						ToastUtil.showToast(mContext, jsonObject.opt("msg") + "");
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

	public void addDelFavorite(int isAdd) {//1添加2删除
		String temp = MyApplication.getUserData().loginStatue ? MyApplication.getUserData().uid : "0";
		int uid = Integer.parseInt(temp);
		String arcurl = game.getArcurl();
		int showtype = game.getShowtype();
		long time = System.currentTimeMillis();
		String validate = "" + uid + arcurl + f_sid + showtype + isAdd + time;
		String sign = StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid", MyApplication.getUserData().uid);
			obj.put("arcurl", arcurl);
			obj.put("f_sid", f_sid);
			obj.put("showtype", showtype);//展示类型:1新闻2攻略3游戏4手游5杂谈6评测7原创8安利9礼包(删除收藏时可传0)
			obj.put("act", isAdd);
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
					JSONObject jsonObject = new JSONObject(response.body().string());
					if (jsonObject.optInt("code") == 1) {
						JSONObject jsonSub = new JSONObject(jsonObject.opt("data").toString());
						f_sid = jsonSub.optInt("code");
						favorite = 3 - favorite;
						if (favorite == 1) {
							mBinding.imgCollect.setImageResource(R.drawable.collect);
							ToastUtil.showToast(mContext, "收藏成功！");
						} else {
							mBinding.imgCollect.setImageResource(R.drawable.click_collect);
							ToastUtil.showToast(mContext, "取消成功！");
						}
					} else {
						ToastUtil.showToast(mContext, jsonObject.opt("msg") + "");
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
	
	//初始化视频全屏
	public View customView;
	private FrameLayout fullscreenContainer;
	private WebChromeClient.CustomViewCallback customViewCallback;
	protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
	/** 视频播放全屏 **/
	private void showCustomView(View view, WebChromeClient.CustomViewCallback callback) {
		// if a view already exists then immediately terminate the new one
		if (customView != null) {
			callback.onCustomViewHidden();
			return;
		}

		getActivity().getWindow().getDecorView();

		FrameLayout decor = (FrameLayout) getActivity().getWindow().getDecorView();
		fullscreenContainer = new FullscreenHolder(mContext);
		fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
		decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
		customView = view;
		setStatusBarVisibility(false);
		customViewCallback = callback;
	}

	/** 隐藏视频全屏 */
	public void hideCustomView() {
		if (customView == null) {
			return;
		}

		setStatusBarVisibility(true);
		FrameLayout decor = (FrameLayout) getActivity().getWindow().getDecorView();
		decor.removeView(fullscreenContainer);
		fullscreenContainer = null;
		customView = null;
		customViewCallback.onCustomViewHidden();
		mBinding.webVideo.setVisibility(View.VISIBLE);
	}

	/** 全屏容器界面 */
	static class FullscreenHolder extends FrameLayout {

		public FullscreenHolder(Context ctx) {
			super(ctx);
			setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
		}

		@Override
		public boolean onTouchEvent(MotionEvent evt) {
			return true;
		}
	}

	private void setStatusBarVisibility(boolean visible) {
		int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
		getActivity().getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	public void clickHandler(View view) {
		Intent intent;
		GameBean jumpGame=game;
		jumpGame.setShowtype(firGameType);//视频和攻略showtype分开
		switch (view.getId()) {
			case R.id.more_video://更多视频
				intent = new Intent(mContext, GameVideoActivity.class);
				intent.putExtra("tag", 1);//设置哪个标签选中
				Bundle bVideo = new Bundle();
				bVideo.putSerializable("game", jumpGame);
				intent.putExtras(bVideo);
				startActivity(intent);
				break;
			case R.id.more_zixun://更多资讯
				intent = new Intent(mContext, GameNewsActivity.class);
				intent.putExtra("tag", 0);
				Bundle bundle1 = new Bundle();
				bundle1.putSerializable("game", jumpGame);
				intent.putExtras(bundle1);
				startActivity(intent);
				break;
			case R.id.more_gonglue://更多攻略
				intent = new Intent(mContext, GameNewsActivity.class);
				intent.putExtra("tag", 1);//设置哪个标签选中
				Bundle bundle0 = new Bundle();
				bundle0.putSerializable("game", jumpGame);
				intent.putExtras(bundle0);
				startActivity(intent);
				break;
			case R.id.ll_more:// 滑动到评论页面
			case R.id.rlComment:
			case R.id.tv_more:
				((ViewPager) getActivity().findViewById(R.id.mViewPager)).setCurrentItem(1);
				break;
			// 隐藏键盘
			case R.id.txtInput:
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
				break;
			// 提交评论
			case R.id.txtSend:
				if (!MyApplication.getUserData().loginStatue) {
					ToastUtil.showToast(mContext, "请先登录！");
					startActivity(new Intent(mContext, LoginActivity.class));
					return;
				} else if (MyApplication.getUserData().mobile.length() == 0) {//第三方登陆，非绑定跳转绑定界面
					intent = new Intent(mContext, ForgetPassActivity.class);
					intent.putExtra("type", 1);//1,绑定手机号，2修改密码，3忘记密码
					startActivity(intent);
					return;
				}
				if (game != null) {
					if (replyId <= 0) {//添加评论
						strContent = mBinding.etComment.getText().toString().trim();
						if (strContent.length() == 0) {
							ToastUtil.showToast(mContext, "内容不能为空");
							return;
						}
						imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//					提交评论
						String uid = MyApplication.getUserData().loginStatue ? MyApplication.getUserData().uid : "0";
						String arcurl = game.getArcurl();
						String content = mBinding.etComment.getText().toString().trim();
						long time = System.currentTimeMillis();
						String validate = uid + arcurl + c_sid + time;
						String sign = StringUtil.MD5(validate);
						JSONObject obj = new JSONObject();
						try {
							obj.put("uid", uid);
							obj.put("arcurl", arcurl);
							obj.put("c_sid", c_sid);
							obj.put("content", content);
							obj.put("time", time);
							obj.put("sign", sign);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						//同步请求
						Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
						NewsService.Api service = retrofit.create(NewsService.Api.class);
						RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());

						Call<ResponseBody> call = service.addComment(body);
						call.enqueue(new Callback<ResponseBody>() {
							@Override
							public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
								Log.e("requestSuccess", "%-----------------------" + response.body() + "提交评论成功");
								try {
									String jsonString = response.body().string();
									JSONObject jsonObject = new JSONObject(jsonString);
									if (jsonObject.optInt("code") == 1) {
										JSONObject jsonSub = new JSONObject(jsonObject.opt("data").toString());
										String str_result = jsonSub.optString("integralmsg");
										if (StringUtil.isEmpty(str_result))
											ToastUtil.showToast(mContext, "提交评论成功!");
										else
											ToastUtil.showToast(mContext, str_result + "");
										mBinding.etComment.setText("");
									} else {
										ToastUtil.showToast(mContext, jsonObject.opt("msg") + "");
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

							@Override
							public void onFailure(Call<ResponseBody> call, Throwable throwable) {
								Log.e("requestFailure", throwable.getMessage() + "提交回帖失败");
							}
						});
					} else {//回复评论
						imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
						String uid = MyApplication.getUserData().loginStatue ? MyApplication.getUserData().uid : "0";
						String content = mBinding.etComment.getText().toString().trim();
						long time = System.currentTimeMillis();
						String validate = uid + replyId + time;
						String sign = StringUtil.MD5(validate);
						JSONObject obj = new JSONObject();
						try {
							obj.put("uid", uid);
							obj.put("id", replyId);
							obj.put("arcurl", game.getArcurl());
							obj.put("content", content);
							obj.put("time", time);
							obj.put("sign", sign);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						//同步请求
						Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
						NewsService.Api service = retrofit.create(NewsService.Api.class);
						RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());

						Call<ResponseBody> call = service.replyComment(body);
						call.enqueue(new Callback<ResponseBody>() {
							@Override
							public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
								Log.e("requestSuccess", "%-----------------------" + response.body() + "提交评论成功");
								try {
									String jsonString = response.body().string();
									JSONObject jsonObject = new JSONObject(jsonString);
									if (jsonObject.optInt("code") == 1) {
										JSONObject jsonSub = new JSONObject(jsonObject.opt("data").toString());
										String str_result = jsonSub.optString("integralmsg");
										if (StringUtil.isEmpty(str_result))
											ToastUtil.showToast(mContext, "提交评论成功!");
										else
											ToastUtil.showToast(mContext, str_result + "");
									} else {
										ToastUtil.showToast(mContext, jsonObject.opt("msg") + "");
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

							@Override
							public void onFailure(Call<ResponseBody> call, Throwable throwable) {
								Log.e("requestFailure", throwable.getMessage() + "提交失败");
							}
						});
					}
				} else {
					ToastUtil.showToast(mContext, "网络异常");
				}
				break;
			case R.id.txtNoInput:// 隐藏键盘
				mBinding.etComment.setFocusable(true);// 获取编辑框焦点
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//打开软键盘
				mBinding.etComment.requestFocus();
				break;
			case R.id.imgCollect:// 收藏
				if (MyApplication.getUserData().loginStatue) {
					if (MyApplication.getUserData().mobile.length() == 0) {
						intent = new Intent(mContext, ForgetPassActivity.class);
						intent.putExtra("type", 1);//1,绑定手机号，2修改密码，3忘记密码
						startActivity(intent);
					} else {
						addDelFavorite(3 - favorite);
					}
				} else {
					ToastUtil.showToast(mContext, "请先登录！");
					startActivity(new Intent(mContext, LoginActivity.class));
				}
				break;
			case R.id.imgShare:// 分享
				if (game != null)       //临时注释 暂时不分享
					popShare.show();
				else
					ToastUtil.showToast(mContext, "网络异常");
				break;
		}
	}

	@Override
	public void onDestroy() {
		if (mBinding.webVideo != null) {
			mBinding.webVideo.destroy();
		}
		super.onDestroy();
	}
}