package com.ws3dm.app.activity;

import android.content.Intent;
import android.graphics.drawable.ClipDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ws3dm.app.R;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.bean.GameBean;
import com.ws3dm.app.bean.GameGiftBean;
import com.ws3dm.app.bean.GameHotPhb;
import com.ws3dm.app.databinding.AcMgGiftDetailBinding;
import com.ws3dm.app.mvp.model.RespBean.MGGiftDetailRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.GameService;
import com.ws3dm.app.util.AppUtil;
import com.ws3dm.app.util.DialogHelper;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.util.glide.GlideUtil;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Author : DKjuan: 手游礼包详情界面
 * <p>
 * Date :  2017/9/1  15:57
 */
public class GiftDetailActivity extends BaseActivity {

	private AcMgGiftDetailBinding mBinding;
	private GameGiftBean mGameGiftBean;
	private MGGiftDetailRespBean bean;
	private PopupWindow popupWindow = null;
	private int pageType; //0 网游礼包  1 手游礼包

	@Override
	protected void init() {
		mBinding = bindView(R.layout.ac_mg_gift_detail);
		mBinding.setHandler(this);

		initView();
		obtainData();
	}

	private void initView() {
		pageType=getIntent().getIntExtra("pageType",0);
		if (getIntent().getSerializableExtra("mGameGiftBean") != null)
			mGameGiftBean = (GameGiftBean) getIntent().getSerializableExtra("mGameGiftBean");

	}

	public void obtainData() {
		//获取数据
		long time = System.currentTimeMillis();
		String validate = "" + mGameGiftBean.getAid() + time;
		String sign = StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("aid", mGameGiftBean.getAid());
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		GameService.Api service = retrofit.create(GameService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<MGGiftDetailRespBean> call =pageType==1?service.sylibaodetail3(body):service.ollibaodetail(body);//pageType 0 网游礼包  1 手游礼包
		call.enqueue(new Callback<MGGiftDetailRespBean>() {
			@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
			@Override
			public void onResponse(Call<MGGiftDetailRespBean> call, Response<MGGiftDetailRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				bean=response.body();
				initData(response.body());
			}

			@Override
			public void onFailure(Call<MGGiftDetailRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
			}
		});
	}

	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	private void initData(final MGGiftDetailRespBean bean) {
		if (bean == null || bean.getData() == null) {
			ToastUtil.showToast(mContext,"暂无相关数据");
			return;
		}

		if(mGameGiftBean.getIs_over()==1){
			mBinding.btnGet.setText("已过期");
			mBinding.btnGet.setClickable(false);
			mBinding.btnGet.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_gray));
		}else if(mGameGiftBean.getIs_empty()==1){
			mBinding.btnGet.setText("已领完");
			mBinding.btnGet.setClickable(false);
			mBinding.btnGet.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_gray));
		}
		
//		GlideUtil.loadImage(mContext,bean.getData().getLitpic(),mBinding.imgGame);
		if(pageType==0){//pageType 0 网游礼包  1 手游礼包
			//改变图片尺寸
			ViewGroup.LayoutParams mParams =  mBinding.imgGame.getLayoutParams();
			mParams.height=(int)getResources().getDimension(R.dimen.element_margin_100);
			mParams.width=(int)getResources().getDimension(R.dimen.element_margin_80);
			mBinding.imgGame.setLayoutParams(mParams);
			mBinding.imgGame.setScaleType(ImageView.ScaleType.FIT_XY);
			GlideUtil.loadImage(mContext,bean.getData().getLitpic(),mBinding.imgGame);
		}else{
			GlideUtil.loadRoundImage(mContext,bean.getData().getLitpic(),mBinding.imgGame,10);
		}
		mBinding.tvName.setText(bean.getData().getTitle());
		mBinding.tvTime.setText(bean.getData().getRange_start()+" 至 "+bean.getData().getRange_end());
		mBinding.tvRemain.setText("剩余："+bean.getData().getSurplusper()+"%");
		ImageView imgScoretop=mBinding.imgScore;
		ClipDrawable clipDrawabletop = (ClipDrawable) imgScoretop.getBackground();
		clipDrawabletop.setLevel(bean.getData().getSurplusper()*100);
		
		mBinding.libaoContent.setText(bean.getData().getContent());
//		mBinding.libaoInfo.setText(bean.getData().getContent());
		
		//相关礼包
		if(bean.getData().getRelate_libao().size()>0){
			switch (bean.getData().getRelate_libao().size()) {
				case 3:
					if(pageType==0) {//pageType 0 网游礼包  1 手游礼包
						ViewGroup.LayoutParams mParams = mBinding.giftImg3.getLayoutParams();//改变图片尺寸
						mParams.height = (int) getResources().getDimension(R.dimen.element_margin_100);
						mParams.width = (int) getResources().getDimension(R.dimen.element_margin_80);
						mBinding.giftImg3.setLayoutParams(mParams);
						mBinding.giftImg3.setScaleType(ImageView.ScaleType.FIT_XY);
						mBinding.giftName3.setPadding(0,(int) getResources().getDimension(R.dimen.element_margin_7),0,0);
					}else{
						mBinding.giftImg3.setBorderWidth(0f);
					}
					GlideUtil.loadImage(mContext, bean.getData().getRelate_libao().get(2).getLitpic(), mBinding.giftImg3);
					mBinding.giftName3.setText(bean.getData().getRelate_libao().get(2).getTitle());
					mBinding.tvTime3.setText(bean.getData().getRelate_libao().get(2).getRange_start()+" 至 "+bean.getData().getRelate_libao().get(2).getRange_end());
					mBinding.giftName3.setText(bean.getData().getRelate_libao().get(2).getTitle());
					mBinding.tvRemain3.setText("剩余："+bean.getData().getRelate_libao().get(2).getSurplusper()+"%");
					ImageView imgScore3=mBinding.imgScore3;
					// 获得ClipDrawable对象  
					ClipDrawable clipDrawable3 = (ClipDrawable) imgScore3.getBackground();
					// 设置裁剪级别，Clip类型的图片默认裁剪级别为0，此时是全部裁剪，图片看不见；  
					// 当级别为10000时，不裁剪图片，图片全部可见  
					// 当全部显示后，设置不可见  
					clipDrawable3.setLevel(bean.getData().getRelate_libao().get(2).getSurplusper()*100);
					mBinding.rlGift3.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(mContext, GiftDetailActivity.class);
							intent.putExtra("pageType",pageType);//pageType //0 网游礼包  1 手游礼包
							Bundle bundle = new Bundle();
							bundle.putSerializable("mGameGiftBean",bean.getData().getRelate_libao().get(2));
							intent.putExtras(bundle);
							startActivity(intent);
						}
					});
					mBinding.line3.setVisibility(View.VISIBLE);
					mBinding.rlGift3.setVisibility(View.VISIBLE);
				case 2:
					if(pageType==0) {//pageType 0 网游礼包  1 手游礼包
						ViewGroup.LayoutParams mParams = mBinding.giftImg2.getLayoutParams();//改变图片尺寸
						mParams.height = (int) getResources().getDimension(R.dimen.element_margin_100);
						mParams.width = (int) getResources().getDimension(R.dimen.element_margin_80);
						mBinding.giftImg2.setLayoutParams(mParams);
						mBinding.giftImg2.setScaleType(ImageView.ScaleType.FIT_XY);
						mBinding.giftName2.setPadding(0,(int) getResources().getDimension(R.dimen.element_margin_7),0,0);
					}else{
						mBinding.giftImg2.setBorderWidth(0f);
					}
					GlideUtil.loadImage(mContext, bean.getData().getRelate_libao().get(1).getLitpic(),  mBinding.giftImg2);
					mBinding.giftName2.setText(bean.getData().getRelate_libao().get(1).getTitle());
					mBinding.tvTime2.setText(bean.getData().getRelate_libao().get(1).getRange_start()+" 至 "+bean.getData().getRelate_libao().get(1).getRange_end());
					mBinding.giftName2.setText(bean.getData().getRelate_libao().get(1).getTitle());
					mBinding.tvRemain2.setText("剩余："+bean.getData().getRelate_libao().get(1).getSurplusper()+"%");
					ImageView imgScore2=mBinding.imgScore2;
					// 获得ClipDrawable对象  
					ClipDrawable clipDrawable2 = (ClipDrawable) imgScore2.getBackground();
					// 设置裁剪级别，Clip类型的图片默认裁剪级别为0，此时是全部裁剪，图片看不见；  
					// 当级别为10000时，不裁剪图片，图片全部可见  
					// 当全部显示后，设置不可见  
					clipDrawable2.setLevel(bean.getData().getRelate_libao().get(1).getSurplusper()*100);
					mBinding.rlGift2.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(mContext, GiftDetailActivity.class);
							intent.putExtra("pageType",pageType);//pageType //0 网游礼包  1 手游礼包
							Bundle bundle = new Bundle();
							bundle.putSerializable("mGameGiftBean",bean.getData().getRelate_libao().get(1));
							intent.putExtras(bundle);
							startActivity(intent);
						}
					});
					mBinding.line2.setVisibility(View.VISIBLE);
					mBinding.rlGift2.setVisibility(View.VISIBLE);
				case 1:
					if(pageType==0) {//pageType 0 网游礼包  1 手游礼包
						ViewGroup.LayoutParams mParams = mBinding.giftImg1.getLayoutParams();//改变图片尺寸
						mParams.height = (int) getResources().getDimension(R.dimen.element_margin_100);
						mParams.width = (int) getResources().getDimension(R.dimen.element_margin_80);
						mBinding.giftImg1.setLayoutParams(mParams);
						mBinding.giftImg1.setScaleType(ImageView.ScaleType.FIT_XY);
						mBinding.giftName1.setPadding(0, (int) getResources().getDimension(R.dimen.element_margin_7),0,0);
					}else{
						mBinding.giftImg1.setBorderWidth(0f);
					}

					GlideUtil.loadImage(mContext, bean.getData().getRelate_libao().get(0).getLitpic(), mBinding.giftImg1);
					mBinding.giftName1.setText(bean.getData().getRelate_libao().get(0).getTitle());
					mBinding.tvTime1.setText(bean.getData().getRelate_libao().get(0).getRange_start()+" 至 "+bean.getData().getRelate_libao().get(0).getRange_end());
					mBinding.giftName1.setText(bean.getData().getRelate_libao().get(0).getTitle());
					mBinding.tvRemain1.setText("剩余："+bean.getData().getRelate_libao().get(0).getSurplusper()+"%");
					ImageView imgScore1=mBinding.imgScore1;
					// 获得ClipDrawable对象  
					ClipDrawable clipDrawable1 = (ClipDrawable) imgScore1.getBackground();
					// 当级别为10000时，不裁剪图片，图片全部可见  
					// 当全部显示后，设置不可见  
					clipDrawable1.setLevel(bean.getData().getRelate_libao().get(0).getSurplusper()*100);
					mBinding.rlGift1.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(mContext, GiftDetailActivity.class);
							intent.putExtra("pageType",pageType);//pageType //0 网游礼包  1 手游礼包
							Bundle bundle = new Bundle();
							bundle.putSerializable("mGameGiftBean",bean.getData().getRelate_libao().get(0));
							intent.putExtras(bundle);
							startActivity(intent);
						}
					});
					break;
			}

			mBinding.lineLibao.setVisibility(View.VISIBLE);
			mBinding.llLibaoHot.setVisibility(View.VISIBLE);
		}
		
//		相关游戏
		if(bean.getData().getGame()!=null){
			if(pageType==0){//pageType; //0 网游礼包  1 手游礼包
				GlideUtil.loadImage(mContext, bean.getData().getGame().getLitpic(), mBinding.imgCover);
				mBinding.txtName.setText(bean.getData().getGame().getTitle());
				mBinding.tvType.setText("类型："+(StringUtil.isEmpty(bean.getData().getGame().getType())?"待补充":bean.getData().getGame().getType()));
				mBinding.tvFirm.setText("运营商："+(StringUtil.isEmpty(bean.getData().getGame().getFirm())?"":bean.getData().getGame().getFirm()));
				mBinding.txtTime.setText("公测："+bean.getData().getGame().getBetatime());
				mBinding.tvLabel.setText("标签："+bean.getData().getGame().getLabel());

				mBinding.llBottom.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						GameHotPhb originalBean=bean.getData().getGame();
						GameBean mGame=new GameBean();
						mGame.setAid(originalBean.getAid());
						mGame.setArcurl(originalBean.getArcurl());
						mGame.setShowtype(originalBean.getShowtype());

						Intent intent = new Intent(mContext, GameHomeActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("str_game", JSON.toJSONString(mGame));
						intent.putExtras(bundle);
						startActivity(intent);
					}
				});
				mBinding.llBottom.setVisibility(View.VISIBLE);
			}else{
				GlideUtil.loadImage(mContext, bean.getData().getGame().getLitpic(), mBinding.imgSy);
				mBinding.syName.setText(bean.getData().getGame().getTitle());
				mBinding.syType.setText("类型："+bean.getData().getGame().getType());
				mBinding.syLabel.setText("标签："+bean.getData().getGame().getLabel());

				mBinding.llSyBottom.setVisibility(View.VISIBLE);
			}
			
			if(bean.getData().getRelate_libao().size()==3)
				mBinding.lineBottom.setVisibility(View.VISIBLE);
			mBinding.tvBottom.setVisibility(View.VISIBLE);
		}
	}

	public void clickHandler(View view) {
		switch (view.getId()) {
			case R.id.imgReturn:
				finish();
				break;
			case R.id.btn_get:
				if(MyApplication.getUserData().loginStatue) {
					if(MyApplication.getUserData().mobile.length()==0){
						Intent intent=new Intent(mContext, ForgetPassActivity.class);
						intent.putExtra("type",1);//1,绑定手机号，2修改密码，3忘记密码
						startActivity(intent);
					}else{
						obtainGiftcode();
					}
				}else{
					ToastUtil.showToast(mContext,"请先登录！");
					startActivity(new Intent(mContext,LoginActivity.class));
				}
				break;
		}
	}

	public void obtainGiftcode() {//领取手游礼包数据
		//获取数据
		int uid=Integer.parseInt(MyApplication.getUserData().uid);
		long time = System.currentTimeMillis();
		String validate = ""+mGameGiftBean.getAid() +uid + time;
		String sign = StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("aid", mGameGiftBean.getAid());
			obj.put("uid", uid);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		GameService.Api service = retrofit.create(GameService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		//pageType 0 网游礼包  1 手游礼包
		Call<ResponseBody> call =pageType==1?service.giftsylibao(body):service.giftollibao(body);
		call.enqueue(new Callback<ResponseBody>() {
			@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				Log.e("requestSuccess","-----------------------"+response.body());
				try {
					JSONObject jsonObject=new JSONObject(response.body().string());
					if(jsonObject.optInt("code")==1){
						showPopwindow(mBinding.btnGet, jsonObject.optString("hao"));
					}else{
						showPopwindow(mBinding.btnGet, jsonObject.optString("hao"));
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

	// 选择图片的弹窗
	public void showPopwindow(View v, final String code) {// takePhoto, selectPhoto, cancle
		View pubPop = LayoutInflater.from(this).inflate(R.layout.view_giftinfo, null);
		popupWindow = DialogHelper.createPopupWindow(this, pubPop, R.style.AnimBottom);
		pubPop.findViewById(R.id.tv_copy).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				popupWindow.dismiss();
				AppUtil.CopyToClip(mContext, code);
				ToastUtil.showToast(mContext, "已复制到剪切板");
			}
		});
		pubPop.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				popupWindow.dismiss();
			}
		});
		TextView tvID = (TextView) pubPop.findViewById(R.id.id_gift);
		tvID.setText("卡号：" + code);
		DialogHelper.showPop(GiftDetailActivity.this, v, popupWindow, Gravity.CENTER, 0, 0);
	}
}