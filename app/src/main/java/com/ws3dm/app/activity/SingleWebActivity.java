package com.ws3dm.app.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.alibaba.fastjson.JSON;
import com.ws3dm.app.R;
import com.ws3dm.app.bean.GameBean;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.databinding.AcSinglewebBinding;
import com.ws3dm.app.util.SharedUtil;
import com.ws3dm.app.util.StringUtil;

public class SingleWebActivity extends BaseActivity {

	private AcSinglewebBinding mBinding;
	private boolean isFirst=true;
	private Intent mIntent;
	public static String mUrl_AgreeMent="https://my.3dmgame.com/appagreement.html";//注册协议
//	public static String mUrl_ScoreInfo="https://mytest.3dmgame.com/integapp";//积分说明
	public static String mUrl_ScoreInfo="https://my.3dmgame.com//integapp";//积分说明

	@Override
	protected void init() {
		mBinding=bindView(R.layout.ac_singleweb);
		mBinding.setHandler(this);

		initView();
	}

	private void initView(){
		mIntent=getIntent();
		mBinding.tvTitle.setText(StringUtil.isEmpty(mIntent.getStringExtra("title"))?"静态页面":mIntent.getStringExtra("title"));

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
		
		mBinding.mWebView.loadUrl(StringUtil.isEmpty(mIntent.getStringExtra("url"))?"https://www.baidu.com/":mIntent.getStringExtra("url"));
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
	
	//点击事件
	public void clickHandler(View view) {
		switch (view.getId()) {
			// 返回
			case R.id.imgReturn:
				if(mBinding.mWebView.canGoBack())
					mBinding.mWebView.goBack();
				else
					finish();
				break;
		}
	}
}