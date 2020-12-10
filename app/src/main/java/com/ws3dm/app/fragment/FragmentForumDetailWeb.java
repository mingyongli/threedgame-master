package com.ws3dm.app.fragment;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.ForgetPassActivity;
import com.ws3dm.app.activity.ForumDetailActivity;
import com.ws3dm.app.activity.LoginActivity;
import com.ws3dm.app.activity.PublishActivity;
import com.ws3dm.app.adapter.BaseRecyclerAdapter;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.bean.ForumDetailBean;
import com.ws3dm.app.bean.RepliesBean;
import com.ws3dm.app.Constant;
import com.ws3dm.app.databinding.FgForumDetailWebBinding;
import com.ws3dm.app.emoj.EmotionUtils;
import com.ws3dm.app.emoj.fragment.EmotionMainFragment;
import com.ws3dm.app.event.ErrorEvent;
import com.ws3dm.app.mvp.model.RespBean.ForumTidPostRespBean;
import com.ws3dm.app.mvp.model.RespBean.NewsListRespBean;
import com.ws3dm.app.mvp.presenter.ForumPresenter;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.ForumService;
import com.ws3dm.app.util.SharedUtil;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.util.glide.GlideUtil;
import com.ws3dm.app.view.InputWindowListener;
import com.ws3dm.app.view.SharePopupWindow;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Describution :论坛详情页 第一个fragment 显示网页和部分评论
 * 
 * Author : DKjuan
 * 
 * Date : 2017/8/18 17:22
 **/
public class FragmentForumDetailWeb extends BaseFragment {	
	private FgForumDetailWebBinding mBinding;
//	private Context mContext;
	private ForumDetailBean mForumDetailBean;
	private ForumTidPostRespBean resultForumTidPostRespBean;
	private SharePopupWindow pop;
	private long replayId;
	private InputMethodManager imm;
	private AppCompatActivity activity;
	private boolean isFirst, onlyHost=false;
	private String  strContent;
	private int isauthor=0;//是否只看楼主(1是0否)
	private int mPage,favorite=2;//favorite:1已收藏，2为收藏
	private ForumPresenter mForumPresenter;
	private int replyID;//当前回复帖子pid如果没有则传0
	private Handler mHandler;
	private EmotionUtils emoutil;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		activity = (AppCompatActivity) getActivity();
//		mContext=getActivity();
		mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_forum_detail_web, container, false);
		mBinding.setHandler(this);
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
						Log.i("error_postlist","数据异常");
						mBinding.noData.setVisibility(View.VISIBLE);
						ErrorEvent errorEvent= (ErrorEvent) msg.obj;
						if(errorEvent.code==69||errorEvent.code==84||errorEvent.code==85||errorEvent.code==78){
							ToastUtil.showToast(mContext,errorEvent.message+"");
							getActivity().finish();
						}
						break;
					case Constant.Notify.RESULT_FORUMTIDPOST_LIST:
						resultForumTidPostRespBean= (ForumTidPostRespBean) msg.obj;
						initReplyPost(resultForumTidPostRespBean);
						break;
					case Constant.Notify.RESULT_NESS_WEB:
						NewsListRespBean result= (NewsListRespBean) msg.obj;
//						init(result.getChannel().getHtml());
						break;
				}
			}
		};

		mForumPresenter = ForumPresenter.getInstance();
		mForumPresenter.setHandler(mHandler);
		
		initView();
		obtainTipPost(isauthor);//是否只看楼主(1是0否)
		return mBinding.getRoot();
	}
	
	public void initView(){
		if (getArguments().getSerializable("forumDetailBean") != null)
			mForumDetailBean = (ForumDetailBean) getArguments().getSerializable("forumDetailBean");
		emoutil=new EmotionUtils(getActivity());
		mBinding.tvTitle.setText(mForumDetailBean.getTitle());
		GlideUtil.loadCircleImage(mContext,mForumDetailBean.getUser().avatarstr,mBinding.imgHead);
		mBinding.tvName.setText(mForumDetailBean.getUser().nickname);
		mBinding.tvTime.setText(TimeUtil.getTimeEN(mForumDetailBean.getPubdate_at()));
		if(mForumDetailBean.getReplies().length()>3)
			mBinding.txtComment.setText("999+");
		else
			mBinding.txtComment.setText(mForumDetailBean.getReplies()+"");
		mBinding.txtComment.setVisibility(View.VISIBLE);
		isFirst=true;
		mPage=1;
		imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		String webUrl=mForumDetailBean.getArcurl();
		pop = new SharePopupWindow(getActivity(),webUrl.replace("_app",""), mForumDetailBean.getTitle(), mForumDetailBean.getTitle(), mBinding.loadViewShare);

//		// 分享设置
//		web = new UMWeb(news.getArcurl());
//		web.setTitle(news.getTitle());
//		if (news.getDescription() != null){
//			if (news.getDescription().isEmpty()) {
//				web.setDescription("真的不错呦！");
//			}else{
//				web.setDescription(news.getDescription());
//			}
//		}else {
//			web.setDescription("真的不错呦！");
//		}
//		web.setThumb(new UMImage(getActivity(), R.drawable.share_logo));

		mBinding.mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
		if (SharedUtil.getSharedPreferencesData("FontSize").equals("2"))//字体大小，0，小，1中，2大
			mBinding.mWebView.getSettings().setTextSize(WebSettings.TextSize.LARGER);
		else if (SharedUtil.getSharedPreferencesData("FontSize").equals("1"))
			mBinding.mWebView.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
		else
			mBinding.mWebView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			mBinding.mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
		}
		mBinding.mWebView.getSettings().setJavaScriptEnabled(true);
		mBinding.mWebView.getSettings().setBlockNetworkImage(false);//解决图片不显示
		mBinding.mWebView.getSettings().setDomStorageEnabled(true);

		mBinding.mWebView.getSettings().setUseWideViewPort(true);
		mBinding.mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		mBinding.mWebView.getSettings().setLoadWithOverviewMode(true);
		
		mBinding.mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				if (isFirst)
					if (newProgress == 100) {
						if (mBinding.txtProgress != null)
							mBinding.txtProgress.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								if (mBinding.llProgress != null)
									mBinding.llProgress.setVisibility(View.GONE);
							}

						}, 300);
						mBinding.viewScroll.fullScroll(ScrollView.FOCUS_UP);//临时注释 添加滚动到顶部，后期可能要删掉
						mBinding.mRecycleView.setVisibility(View.VISIBLE);
					} else {
						if (mBinding.llProgress != null)
							if (mBinding.llProgress.getVisibility() == View.GONE)
								mBinding.llProgress.setVisibility(View.VISIBLE);
							else {
								if (mBinding.txtProgress != null) {
									DisplayMetrics dm = new DisplayMetrics();
									activity.getWindowManager().getDefaultDisplay().getMetrics(dm);//临时
									mBinding.txtProgress.setLayoutParams(new LinearLayout.LayoutParams((int) (dm.widthPixels * newProgress * 0.01), ViewGroup.LayoutParams.MATCH_PARENT));
								}
							}
					}
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
			}
		});

		mBinding.mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				loadurlLocalMethod(view, url);
				return false;
			}
			
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				if (mBinding.mWebView != null)
					mBinding.mWebView.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						isFirst = false;
//						mBinding.viewScroll.fullScroll(ScrollView.FOCUS_UP);//临时注释 添加滚动到顶部，后期可能要删掉
						
					}
				}, 500);
			}
		});
		String uid=MyApplication.getUserData().loginStatue?MyApplication.getUserData().uid:"0";
		mBinding.mWebView.loadUrl(mForumDetailBean.getWebviewurl()+"&uid="+uid);
//        mWebView.loadUrl("file:///android_asset/index_video.html");

		// 监听键盘状态
		mBinding.rlAllComment.setListener(new InputWindowListener() {
			@Override
			public void show() {
				String text = "";
				mBinding.etComment.setText(text);
				mBinding.etComment.setSelection(text.length());
				mBinding.llNoInPut.setVisibility(View.GONE);
				mBinding.txtInput.setVisibility(View.VISIBLE);
				mBinding.llInPut.postDelayed(new Runnable() {
					@Override
					public void run() {
						mBinding.llInPut.setVisibility(View.VISIBLE);
					}
				}, 1);
			}

			@Override
			public void hidden() {
				if(mBinding.flEmotionviewMain.getHeight()>100){

				}else{
					replayId = 0;
					mBinding.etComment.setText("");
					mBinding.txtInput.setVisibility(View.GONE);
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
		
//      //显示编辑按钮
//		if(true||mForumDetailBean.getUser().uid.equals(MyApplication.getUserData().uid)){
//			mBinding.imgEdit.setVisibility(View.VISIBLE);
//		}

		initEmotionMainFragment();
	}

	public void loadurlLocalMethod(final WebView webView, final String url) {    //屏蔽网页内恶意代码跳转app
		new Thread(new Runnable() {
			@Override
			public void run() {
				webView.loadUrl(url);
			}
		});
	}

	/**
	 * 初始化表情面板
	 */
	EmotionMainFragment emotionMainFragment;
	public void initEmotionMainFragment(){
		//构建传递参数
		Bundle bundle = new Bundle();
		//绑定主内容编辑框
		bundle.putBoolean(EmotionMainFragment.BIND_TO_EDITTEXT,false);
		//隐藏控件
		bundle.putBoolean(EmotionMainFragment.HIDE_BAR_EDITTEXT_AND_BTN,true);
		//替换fragment
		//创建修改实例
		emotionMainFragment =EmotionMainFragment.newInstance(EmotionMainFragment.class,bundle);
		emotionMainFragment.bindToContentView(mBinding.llInPut);
		emotionMainFragment.bindToEditView(mBinding.etComment);
		emotionMainFragment.bindToImageView(mBinding.ivEmoj);
		FragmentTransaction transaction =getActivity().getSupportFragmentManager().beginTransaction();
		// Replace whatever is in thefragment_container view with this fragment,
		// and add the transaction to the backstack
		transaction.replace(R.id.fl_emotionview_main,emotionMainFragment);
		transaction.addToBackStack(null);
		//提交修改
		transaction.commit();
	}
	
	public void obtainTipPost(int isauthor){//获取回帖 //是否只看楼主(1是0否)
		//获取数据
		String uid=MyApplication.getUserData().loginStatue?MyApplication.getUserData().uid:"0";
		String fid=mForumDetailBean.getFid();
		String tid=mForumDetailBean.getTid();
		long time=System.currentTimeMillis();
		String validate= uid+fid+tid+35+mPage+isauthor+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid", uid);
			obj.put("fid", fid);
			obj.put("tid", tid);
			obj.put("pagesize", 35);
			obj.put("page", mPage);
			obj.put("isauthor", isauthor);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		mForumPresenter.getForumTidPost(obj.toString());//异步请求
	}

// 初始化回帖
	private void initReplyPost(ForumTidPostRespBean replyBean) {
		if(replyBean!=null&&replyBean.getData()!=null){
			//设置收藏状态
			if(replyBean.getData().getIsfavorite()==1){//收藏状态1已收藏0未收藏
				mBinding.imgCollect.setImageResource(R.drawable.collect);
				favorite=1;
			}else{
				mBinding.imgCollect.setImageResource(R.drawable.click_collect);
				favorite=2;
			}
		}
		if(replyBean==null||replyBean.getData().getList()==null||replyBean.getData().getList().size()<=0){
			mBinding.noData.setVisibility(View.VISIBLE);
			return;
		}

		mBinding.rlComment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ForumDetailActivity.viewPager.setCurrentItem(1);
			}
		});
		replyBean.getData().getList().get(0).setType("最新回帖");
		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.mRecycleView.setLayoutManager(layoutManager);

		mBinding.mRecycleView.setPullRefreshEnabled(false);
		mBinding.mRecycleView.setLoadingMoreEnabled(false);
		mBinding.mRecycleView.setNestedScrollingEnabled(false);
		CommonRecyclerAdapter mAdapter = new CommonRecyclerAdapter<RepliesBean>(mContext, R.layout.adapter_comments) {
			@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
			@Override
			public void bindData(RecyclerViewHolder holder, final int position, final RepliesBean comments) {
				if(position==0){
					holder.getView(R.id.llAllComments).setVisibility(View.VISIBLE);
					holder.setText(R.id.tv_comment_title,"最新回帖");
					holder.getView(R.id.tv_comment_title).setVisibility(View.VISIBLE);
					holder.getView(R.id.rg_order).setVisibility(View.GONE);
				}else {
					holder.getView(R.id.llAllComments).setVisibility(View.GONE);
				}
//				ImageView img_good=holder.getView(R.id.imgGood);
//				img_good.setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View view) {
////						ToastUtil.showToast(mContext,"点赞"+position);
//					}
//				});
//				holder.setI(R.id.tv_lomageByUrl(R.id.img_head,comments.getUser().avatarstr);
//				holder.setText(R.id.tv_name,comments.getUser().nickname);
//				holder.setTextcate,"第"+comments.getPosition()+"楼  "+ comments.getUser().regionstr+"网友"+" ");
				GlideUtil.loadCircleImage(mContext,comments.getUser().avatarstr,(ImageView) holder.getView(R.id.img_head));
				holder.setText(R.id.tv_name,comments.getUser().nickname);
//				holder.setText(R.id.tv_locate,comments.getUser().regionstr.length()>0?comments.getUser().regionstr+" 网友":"");
				holder.getView(R.id.tv_head_title).setVisibility(View.GONE);
				holder.setText(R.id.tv_posi2,"第"+comments.getPosition()+"楼");

				TextView content=holder.getView(R.id.tv_content);
				content.setText(emoutil.addSmileySpans(comments.getContent()));
//				holder.setText(R.id.tv_content,comments.getContent());
				holder.setText(R.id.tv_time,TimeUtil.getFoolishTime(comments.getPubdate_at()));
				holder.getView(R.id.imgGood).setVisibility(View.GONE);
				holder.getView(R.id.tv_report).setVisibility(View.GONE);
				TextView tv_reply=holder.getView(R.id.tv_reply);
				tv_reply.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if(MyApplication.getUserData().loginStatue){
							replyID=comments.getPid();
							// 获取编辑框焦点
							mBinding.etComment.setFocusable(true);
							//打开软键盘
							final InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
							mBinding.etComment.requestFocus();
						}else {
							ToastUtil.showToast(mContext,"请先登录！");
						}
					}
				});

				if(comments.getReplies()!=null&&comments.getReplies().size()>0){
					XRecyclerView recyclerView = holder.getView(R.id.mRecycleView);
					LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
					layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
					recyclerView.setLayoutManager(layoutManager);
					recyclerView.setNestedScrollingEnabled(false);
					recyclerView.setPullRefreshEnabled(false);
					recyclerView.setLoadingMoreEnabled(false);

					BaseRecyclerAdapter<RepliesBean> mSubRecyclerAdapter= new BaseRecyclerAdapter<RepliesBean>(mContext, R.layout.adapter_child_comments) {
						@Override
						public void bindData(RecyclerViewHolder holder, final int position, final RepliesBean bean) {
//							holder.setText(R.id.tv_name,bean.getUser().nickname);
//							holder.setText(R.id.tv_content,""+bean.getContent());
							TextView content=holder.getView(R.id.tv_content);
							String str="<font color='#00a0e9'>"+bean.getUser().nickname+"</font>"+" : "+bean.getContent();
							content.setText(emoutil.addSmileySpans(Html.fromHtml(str)));
//							holder.getView(R.id.tv_head_title).setVisibility(View.GONE);
							holder.getView(R.id.imgGood).setVisibility(View.INVISIBLE);
//							holder.getView(R.id.imgBad).setVisibility(View.INVISIBLE);

							TextView txtComment=holder.getView(R.id.tv_reply);
							txtComment.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View view) {
									if(MyApplication.getUserData().loginStatue){
										replyID=bean.getPid();
										// 获取编辑框焦点
										mBinding.etComment.setFocusable(true);
										//打开软键盘
										final InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
										imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
										mBinding.etComment.requestFocus();
									}else {
										ToastUtil.showToast(mContext,"请先登录！");
									}
								}
							});
						}
					};
					recyclerView.setAdapter(mSubRecyclerAdapter);
					mSubRecyclerAdapter.clearAndAddList(comments.getReplies());
					holder.getView(R.id.mRecycleView).setVisibility(View.VISIBLE);
				}else{
					holder.getView(R.id.mRecycleView).setVisibility(View.GONE);
				}
			}
		};
		mBinding.mRecycleView.setAdapter(mAdapter);
		mAdapter.clearAndAddList(replyBean.getData().getList());
		mBinding.llMore.setVisibility(View.VISIBLE);
	}
	public void addDelFavorite(final int isAdd){//1添加2删除
		String temp = MyApplication.getUserData().loginStatue ? MyApplication.getUserData().uid : "0";
		int uid=Integer.parseInt(temp);
		String tid=mForumDetailBean.getTid();
		long time=System.currentTimeMillis();
		String validate= ""+uid+tid+isAdd+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid", MyApplication.getUserData().uid);
			obj.put("tid",tid);
			obj.put("act",isAdd);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		ForumService.Api service = retrofit.create(ForumService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<ResponseBody> call = service.setThreadFavorite(body);
		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				try {
					JSONObject jsonObject=new JSONObject(response.body().string());
					if(jsonObject.optInt("code")==1){
						if(isAdd==1){
							ToastUtil.showToast(mContext,"收藏成功！");
							mBinding.imgCollect.setImageResource(R.drawable.collect);
							favorite=1;
						}else{
							ToastUtil.showToast(mContext,"取消成功！");
							mBinding.imgCollect.setImageResource(R.drawable.click_collect);
							favorite=2;
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

/*	// 与js交互
		public class JavaScriptInterface {
			Context mContext;

		JavaScriptInterface(Context context) {
			mContext = context;
		}

		@JavascriptInterface
		public int changeTheme() {
			return Integer.parseInt(SharedUtil.getSharedPreferencesData("isNight"));
		}

		@JavascriptInterface
		public void clickPic(int position, String[] arrImg) {//临时注释
			isJump = true;
			getActivity().startActivity(new Intent(getActivity(), NewsPhotoActivity.class)
					.putExtra("position", position)
					.putExtra("title", strTitle)
					.putExtra("url", arrImg));
		}

	}*/

	public void clickHandler(View view) {
		switch (view.getId()) {
			// 滑动到评论页面
			case R.id.ll_more:
			case R.id.tv_more:
				((ViewPager) getActivity().findViewById(R.id.mViewPager)).setCurrentItem(1);
				break;
			// 隐藏键盘
			case R.id.txtInput:
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
				break;
			// 提交评论
			case R.id.txtSend:
				if(!MyApplication.getUserData().loginStatue){
					ToastUtil.showToast(mContext,"请先登录！");
					startActivity(new Intent(mContext,LoginActivity.class));
					return;
				}else if(MyApplication.getUserData().mobile.length()==0) {//第三方登陆，非绑定跳转绑定界面
					Intent intent = new Intent(mContext, ForgetPassActivity.class);
					intent.putExtra("type", 1);//1,绑定手机号，2修改密码，3忘记密码
					startActivity(intent);
					return;
				}
				if (mForumDetailBean != null) {
					strContent = mBinding.etComment.getText().toString().trim();
					if (strContent.length() == 0) {
						ToastUtil.showToast(mContext,"内容不能为空");
						return;
					}
					imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//					提交回帖
					String uid=MyApplication.getUserData().loginStatue?MyApplication.getUserData().uid:"0";
					String fid=mForumDetailBean.getFid();
					String tid=mForumDetailBean.getTid();
					String reppid=replyID+"";
					String content=mBinding.etComment.getText().toString().trim();
					long time=System.currentTimeMillis();
					String validate= uid+fid+tid+reppid+time;
					String sign= StringUtil.MD5(validate);
					JSONObject obj = new JSONObject();
					try {
						obj.put("uid", uid);
						obj.put("fid", fid);
						obj.put("tid", tid);
						obj.put("reppid", reppid);
						obj.put("content", content);
						obj.put("time", time);
						obj.put("sign", sign);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					//同步请求
					Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
					ForumService.Api service = retrofit.create(ForumService.Api.class);
					RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());

					Call<ResponseBody> call=service.addTidPost(body);
					call.enqueue(new Callback<ResponseBody>() {
						@Override
						public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
							Log.e("requestSuccess","%-----------------------"+response.body()+"提交回帖成功");
							try {
								JSONObject jsonObject=new JSONObject(response.body().string());
								if(jsonObject.optInt("code")==1){
									ToastUtil.showToast(mContext, "提交回帖成功！");
									mBinding.etComment.setText("");
								}else{
									ToastUtil.showToast(mContext,jsonObject.opt("msg")+"");
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						@Override
						public void onFailure(Call<ResponseBody> call, Throwable throwable) {
							Log.e("requestFailure",throwable.getMessage()+"提交回帖失败");
							ToastUtil.showToast(mContext, "提交回帖失败！");
						}
					});
				} else{
					ToastUtil.showToast(mContext,"网络异常");
				}
				break;
			case R.id.txtNoInput:// 隐藏键盘
				mBinding.etComment.setFocusable(true);// 获取编辑框焦点
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//打开软键盘
				mBinding.etComment.requestFocus();
				break;
			case R.id.tv_statue://只看楼主
				mBinding.tvStatue.setText(onlyHost?"只看楼主":"查看所有");
				obtainTipPost(onlyHost?0:1);
				onlyHost=!onlyHost;
				break;
			case R.id.imgShare:// 分享
				if (mForumDetailBean != null)       //临时注释 暂时不分享
					pop.show();
				else
					ToastUtil.showToast(mContext,"网络异常");
				break;
			case R.id.imgCollect:
				if(MyApplication.getUserData().loginStatue) {
					if(MyApplication.getUserData().mobile.length()==0){
						Intent intent=new Intent(mContext, ForgetPassActivity.class);
						intent.putExtra("type",1);//1,绑定手机号，2修改密码，3忘记密码
						startActivity(intent);
					}else{
						addDelFavorite(3 - favorite);
					}
				}else{
					ToastUtil.showToast(mContext,"请先登录！");
					startActivity(new Intent(mContext,LoginActivity.class));
				}
				break;
			case R.id.img_edit:
				Intent intent=new Intent(mContext, PublishActivity.class);
				intent.putExtra("isModify","1");
				intent.putExtra("fid",mForumDetailBean.getFid());
				intent.putExtra("tid",mForumDetailBean.getTid());
				startActivity(intent);
				break;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mBinding.mWebView != null) {
			mBinding.mWebView.removeAllViews();
			mBinding.mWebView.destroy();
		}
		super.onDestroy();
	}

	// 分享回调
	private UMShareListener umShareListener = new UMShareListener() {
		@Override
		public void onStart(SHARE_MEDIA platform) {
			if (mBinding.loadViewShare != null)
				mBinding.loadViewShare.setVisibility(View.GONE);
		}

		@Override
		public void onResult(SHARE_MEDIA platform) {
			if (pop != null)
				pop.dismiss();
			if (mBinding.loadViewShare != null)
				mBinding.loadViewShare.setVisibility(View.GONE);
			ToastUtil.showToast(mContext,"分享成功");
		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			if (pop != null)
				pop.dismiss();
			if (mBinding.loadViewShare != null)
				mBinding.loadViewShare.setVisibility(View.GONE);
			ToastUtil.showToast(mContext,"没有安装此应用");
		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
			if (pop != null)
				pop.dismiss();
			if (mBinding.loadViewShare != null)
				mBinding.loadViewShare.setVisibility(View.GONE);
		}
	};
}