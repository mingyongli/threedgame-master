package com.ws3dm.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.GameBean;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.databinding.LevelActivityBinding;
import com.ws3dm.app.util.SharedUtil;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class LevelActivity extends BaseActivity {

	private LevelActivityBinding mBinding;
	private boolean isFirst=true;
	private CommonRecyclerAdapter<AssetsList.Data.Assets> mAdapter;

	@Override
	protected void init() {
		mBinding=bindView(R.layout.level_activity);
		mBinding.setHandler(this);
		myassets();
		getUserAssets();
		initView();
	}

	private void initDynamic(AssetsList.Data ud){
		if (MyApplication.getUserData().loginStatue) {//已经登录
			LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
			layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
			mBinding.recyclerview.setLayoutManager(layoutManager);
			mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
			mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
			mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);

			mAdapter = new CommonRecyclerAdapter<AssetsList.Data.Assets>(mContext, R.layout.adapter_user_assets_item) {
				@Override
				public void bindData(RecyclerViewHolder holder, final int position, final AssetsList.Data.Assets item) {
					holder.setText(R.id.content, item.getMessage()+"");
					String date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(item.getCreated_at()*1000);

					holder.setText(R.id.time, date);
				}
			};
			mBinding.recyclerview.setAdapter(mAdapter);
			mBinding.recyclerview.setPullRefreshEnabled(false);
			mBinding.recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
				@Override
				public void onRefresh() {
					page = 1;
					getUserAssets();
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							mBinding.recyclerview.refreshComplete();
						}
					},1000);
				}

				@Override
				public void onLoadMore() {
					page++;
					getUserAssets();
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							mBinding.recyclerview.loadMoreComplete();
						}
					},1000);
				}
			});
			if(page == 1){
				mAdapter.clearAndAddList(ud.getList());
			}else{
				mAdapter.appendList(ud.getList());
			}
		}
	}

	int page = 1;
	int pageSize = 20;
	private void getUserAssets(){
		UserDataBean userData = MyApplication.getUserData();
		long time = System.currentTimeMillis();
		String sign = StringUtil.newMD5(userData.uid + pageSize + page + time + NewUrl.KEY);

		Map<String, Object> values = new HashMap<>();
		values.put("uid",userData.uid);
		values.put("sign",sign);
		values.put("time",time);
		values.put("pagesize",pageSize);
		values.put("page",page);
		String json = new JSONObject(values).toString();
		OkHttpUtils
				.postString()
				.url(NewUrl.MY_JFLIST)
				.content(json)
				.build()
				.execute(new Callback<AssetsList>() {

					@Override
					public AssetsList parseNetworkResponse(Response response) throws IOException {
						String string = response.body().string();
						return new Gson().fromJson(string, AssetsList.class);
					}

					@Override
					public void onError(Request request, Exception e) {
						Log.e("message",e.getMessage());
					}

					@Override
					public void onResponse(AssetsList response) {
						if(response.getCode() == 1){
							initDynamic(response.getData());
						}else{
							ToastUtil.showToast(LevelActivity.this,response.getMsg());
						}
					}

				});

	}

	public class AssetsList{
		private int code;
		private Data data;
		private String msg;

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public Data getData() {
			return data;
		}

		public void setData(Data data) {
			this.data = data;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public class Data {

			private List<Assets> list;

			public List<Assets> getList() {
				return list;
			}

			public void setList(List<Assets> list) {
				this.list = list;
			}

			public class Assets {

				private long id;
				private String message;
				private long created_at;
				public void setId(long id) {
					this.id = id;
				}
				public long getId() {
					return id;
				}

				public void setMessage(String message) {
					this.message = message;
				}
				public String getMessage() {
					return message;
				}

				public void setCreated_at(long created_at) {
					this.created_at = created_at;
				}
				public long getCreated_at() {
					return created_at;
				}

			}
		}
	}


	private void myassets(){
		long time = System.currentTimeMillis();
		String sign = StringUtil.newMD5(MyApplication.getUserData().uid + time + NewUrl.KEY);
		Map<String, Object> values = new HashMap<>();
		values.put("uid",MyApplication.getUserData().uid);
		values.put("sign",sign);
		values.put("time",time);
		String json = new JSONObject(values).toString();
		OkHttpUtils
				.postString()
				.url(NewUrl.MY_ASSETS)
				.content(json)
				.build()
				.execute(new Callback<Assets>() {

					@Override
					public Assets parseNetworkResponse(Response response) throws IOException {
						String string = response.body().string();
						return new Gson().fromJson(string, Assets.class);
					}

					@Override
					public void onError(Request request, Exception e) {
						Log.e("message",e.getMessage());
					}

					@Override
					public void onResponse(Assets response) {
						if(response.getCode() == 1){
							initData(response.getData().getInfo());
						}else{
							ToastUtil.showToast(LevelActivity.this,response.getMsg());
						}
					}

				});
	}
	Assets.Data.Info mdi;
	private void initData(Assets.Data.Info info) {
		mdi = info;
		mBinding.jingyan.setText(info.getNext_level_need()+"经验");
		mBinding.jifen.setText(info.getIntegral()+"");
		mBinding.userLv.setText("Lv."+info.getUser_level());
	}

	public void clickHandler(View view) {
		switch (view.getId()) {
			// 返回
			case R.id.imgReturn:
				if(mBinding.mWebView.canGoBack())
					mBinding.mWebView.goBack();
				else
					finish();
				break;
			case R.id.jfgojy:
				DialogUIUtils.showAlertInput(LevelActivity.this, "积分兑换", "请输入要兑换的积分数量（今日最大为"+mdi.getCan_integral()+"）",
						"", "确定", "取消", new DialogUIListener() {
							@Override
							public void onPositive() {

							}
							@Override
							public void onNegative() {

							}
							@Override
							public void onGetInput(CharSequence input1, CharSequence input2) {
								if(!StringUtil.isEmpty(input1.toString())){
									if(isInteger(input1.toString().trim())){
										if(Integer.parseInt(input1.toString())>mdi.getCan_growth()){
											ToastUtil.showToast(LevelActivity.this,"超出可兑换积分");
										}else{
											convasset(1,input1.toString());
										}
									}else{
										ToastUtil.showToast(LevelActivity.this,"请输入正确的数量");
									}
								}else{
									ToastUtil.showToast(LevelActivity.this,"数量不能为空");
								}
							}
						}).show();
				break;
			case R.id.jygojf:
				DialogUIUtils.showAlertInput(LevelActivity.this, "经验兑换", "请输入要兑换的经验数量（今日最大为"+mdi.getCan_growth()+"）",
						"", "确定", "取消", new DialogUIListener() {
							@Override
							public void onPositive() {

							}
							@Override
							public void onNegative() {

							}
							@Override
							public void onGetInput(CharSequence input1, CharSequence input2) {
								if(!StringUtil.isEmpty(input1.toString())){
									if(isInteger(input1.toString().trim())){
										if(Integer.parseInt(input1.toString())>mdi.getCan_growth()){
											ToastUtil.showToast(LevelActivity.this,"超出可兑换数量");
										}else{
											convasset(2,input1.toString());
										}
									}else{
										ToastUtil.showToast(LevelActivity.this,"请输入正确的数量");
									}
								}else{
									ToastUtil.showToast(LevelActivity.this,"数量不能为空");
								}
							}
						}).show();
				break;
		}
	}

	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}
	private void convasset(int type,String asserts){
		long time = System.currentTimeMillis();
		String sign = StringUtil.newMD5(MyApplication.getUserData().uid + asserts + type + time + NewUrl.KEY);
		Map<String, Object> values = new HashMap<>();
		values.put("uid",MyApplication.getUserData().uid);
		values.put("sign",sign);
		values.put("time",time);
		values.put("type",type);
		values.put("asserts",asserts);
		String json = new JSONObject(values).toString();
		OkHttpUtils
				.postString()
				.url(NewUrl.CONVASSET)
				.content(json)
				.build()
				.execute(new Callback<Assets>() {

					@Override
					public Assets parseNetworkResponse(Response response) throws IOException {
						String string = response.body().string();
						return new Gson().fromJson(string, Assets.class);
					}

					@Override
					public void onError(Request request, Exception e) {
						Log.e("message",e.getMessage());
					}

					@Override
					public void onResponse(Assets response) {
						if(response.getCode() == 1){
							initData(response.getData().getInfo());
						}else{
							ToastUtil.showToast(LevelActivity.this,response.getMsg());
						}
					}

				});
	}


	public class Assets {

		private int code;
		private Data data;
		private String msg;
		public void setCode(int code) {
			this.code = code;
		}
		public int getCode() {
			return code;
		}

		public void setData(Data data) {
			this.data = data;
		}
		public Data getData() {
			return data;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}
		public String getMsg() {
			return msg;
		}

		public class Data {

			private Info info;
			public void setInfo(Info info) {
				this.info = info;
			}
			public Info getInfo() {
				return info;
			}


			public class Info {

				private int integral;
				private int user_level;
				private int next_level_need;
				private int can_integral;
				private int can_growth;
				public void setIntegral(int integral) {
					this.integral = integral;
				}
				public int getIntegral() {
					return integral;
				}

				public void setUser_level(int user_level) {
					this.user_level = user_level;
				}
				public int getUser_level() {
					return user_level;
				}

				public void setNext_level_need(int next_level_need) {
					this.next_level_need = next_level_need;
				}
				public int getNext_level_need() {
					return next_level_need;
				}

				public void setCan_integral(int can_integral) {
					this.can_integral = can_integral;
				}
				public int getCan_integral() {
					return can_integral;
				}

				public void setCan_growth(int can_growth) {
					this.can_growth = can_growth;
				}
				public int getCan_growth() {
					return can_growth;
				}

			}
		}
	}


	private void initView(){
		mBinding.tvTitle.setText("等级积分");
		mBinding.mWebView.getSettings().setJavaScriptEnabled(true);
		mBinding.mWebView.getSettings().setBlockNetworkImage(false);//解决图片不显示
		mBinding.mWebView.getSettings().setDomStorageEnabled(true);
		mBinding.mWebView.getSettings().setSupportZoom(true); // 支持缩放
		mBinding.mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);

		mBinding.mWebView.getSettings().setUseWideViewPort(true);
		mBinding.mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		mBinding.mWebView.getSettings().setLoadWithOverviewMode(true);
		if (SharedUtil.getSharedPreferencesData("FontSize").equals("2"))//字体大小，0，小，1中，2大
			mBinding.mWebView.getSettings().setTextSize(WebSettings.TextSize.LARGER);
		else if (SharedUtil.getSharedPreferencesData("FontSize").equals("1"))
			mBinding.mWebView.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
		else
			mBinding.mWebView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
			mBinding.mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

		mBinding.mWebView.addJavascriptInterface(new JavaScriptInterface(mContext), "JSInterface");
		mBinding.mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				loadurlLocalMethod(view, url);
				return false;
//				view.loadUrl(url);
//				return super.shouldOverrideUrlLoading(view, url);
			}
		});
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
					} else {
						if (mBinding.llProgress != null)
							if (mBinding.llProgress.getVisibility() == View.GONE)
								mBinding.llProgress.setVisibility(View.VISIBLE);
							else {
								if (mBinding.txtProgress != null) {
									DisplayMetrics dm = new DisplayMetrics();
									getWindowManager().getDefaultDisplay().getMetrics(dm);//临时
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

		mBinding.mWebView.loadUrl("https://my.3dmgame.com/webview/jfrule.html");
	}

	public void loadurlLocalMethod(final WebView webView, final String url) {//屏蔽网页内恶意代码跳转app
		new Thread(new Runnable() {
			@Override
			public void run() {
				webView.loadUrl(url);
			}
		});
	}

	// 与js交互
	public class JavaScriptInterface {
		Context mContext;

		JavaScriptInterface(Context context) {
			mContext = context;
		}

		@JavascriptInterface
		public void showpagedetail(String arcurl,String title,String litpic,String webviewurl,int showtype) {
			NewsBean news = new NewsBean();
			news.setArcurl(arcurl);
			news.setTitle(title);
			news.setLitpic(litpic);
			news.setWebviewurl(webviewurl);
			news.setShowtype(showtype);

			Intent intent = new Intent(mContext, NewsActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("newsBean",news);
			intent.putExtras(bundle);
			startActivity(intent);
		}

		@JavascriptInterface
		public void showztdetail(String arcurl,int aid,String title) {
			GameBean gameBean = new GameBean();
			gameBean.setArcurl(arcurl);
			gameBean.setAid(aid);

			Intent intent = new Intent(mContext, GameHomeActivity.class);
			Bundle bundle = new Bundle();
//			bundle.putSerializable("game", gameBean);
//					bundle.putSerializable("game", finalGame);//数据太大，超出系统限制
			bundle.putString("str_game", JSON.toJSONString(gameBean));
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(mBinding.mWebView.canGoBack()){
				mBinding.mWebView.goBack();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}


}