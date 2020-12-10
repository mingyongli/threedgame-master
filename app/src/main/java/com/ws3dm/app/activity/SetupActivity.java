package com.ws3dm.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.bean.AvatarBean;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.Constant;
import com.ws3dm.app.databinding.AcSetupBinding;
import com.ws3dm.app.event.ErrorEvent;
import com.ws3dm.app.mvp.model.RespBean.GetSettingRespBean;
import com.ws3dm.app.mvp.presenter.UserPresenter;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.UserService;
import com.ws3dm.app.util.DialogHelper;
import com.ws3dm.app.util.ScreenUtil;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.util.callback.CallBack;
import com.ws3dm.app.util.glide.GlideUtil;
import com.ws3dm.app.view.dialog.AbstractWheelTextAdapter;
import com.ws3dm.app.view.dialog.AddressData;
import com.ws3dm.app.view.dialog.ArrayWheelAdapter;
import com.ws3dm.app.view.dialog.MyAlertDialog;
import com.ws3dm.app.view.dialog.OnWheelChangedListener;
import com.ws3dm.app.view.dialog.WheelMain;
import com.ws3dm.app.view.dialog.WheelView;
import com.ws3dm.app.view.dialog.WheelViewTime;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Describution :设置
 * 
 * Author : DKjuan
 * 
 * Date : 2017/9/4 16:32
 **/
public class SetupActivity extends BaseActivity{
	private String lv;
	private AcSetupBinding mBinding;
	private CommonRecyclerAdapter<AvatarBean> mAdapter;
	private int mlevel2,mlevel3,mlevel4,title_level,selectID=0;//中级,高级，特技，用户头衔等级，选择头像的id
	private int cityID,areaID,headID,genderID;//城市ID,县ID，初始头像id，性别
	private String cityTxt,headTitle;//用户头衔
	private List<AvatarBean> mHeadList=new ArrayList<>();
	private UserPresenter mUserPresenter;
	private Handler mHandler;
	private WheelMain wheelMain;;
	private int type=0;//1qq  2微博  3微信
	private String openID="",appOpenid="";//第三方账号标识
	public String mHobbyTxt;//已配置的游戏标签
	public static DialogHelper dialogHelper;
	public boolean canNick=false;
	private int needScore=0;
	
	@Override
	protected void init() {
		mBinding=bindView(R.layout.ac_setup);
		mBinding.setHandler(this);
		lv = getIntent().getStringExtra("lv");
		mUserPresenter = UserPresenter.getInstance();
		mHandler = new Handler() {
			@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
			@Override
			public void handleMessage(Message msg) {
				if (!mIsRunning) {
					return;
				}
				switch (msg.what) {
					case Constant.Notify.LOAD_FAILURE://请求失败
//						if (mBinding.loadView != null)
//							mBinding.loadView.setVisibility(View.GONE);
						ErrorEvent error = (ErrorEvent) msg.obj;
						Log.i("error_comment",""+error.message);
						break;
					case Constant.Notify.RESULT_GETSETTING://查询成功
						initData((GetSettingRespBean) msg.obj);
						break;
					case Constant.Notify.RESULT_MODITYSETTING://修改信息成功
						initData((GetSettingRespBean) msg.obj);
						break;
				}
			}
		};
		mUserPresenter.setHandler(mHandler);
		
		//获取数据
		long time=System.currentTimeMillis();
		int isavatar=1;//是否展示头像库1是0否
		String validate=MyApplication.getUserData().uid+isavatar+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid", MyApplication.getUserData().uid);
			obj.put("isavatar",isavatar);
			obj.put("is_area",1);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		mUserPresenter.getSetting(obj.toString());

//		GridLayoutManager layoutManager = new GridLayoutManager(this,4);
//		layoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
//		mBinding.recyclerview.setLayoutManager(layoutManager);
		mBinding.recyclerview.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.HORIZONTAL));

		mAdapter = new CommonRecyclerAdapter<AvatarBean>(mContext, R.layout.adapter_head) {
			@Override
			public void bindData(RecyclerViewHolder holder, int position, AvatarBean bean) {
//				holder.setImageByUrl(R.id.imgCover, game.getCover());
//				holder.setText(R.id.txtName,game.getName());
				holder.setImageByUrl(R.id.img_head,bean.getUrl(),5);
				if(bean.getId().equals(""+headID)){
					(holder.getView(R.id.img_tag)).setVisibility(View.VISIBLE);
				}else{
					(holder.getView(R.id.img_tag)).setVisibility(View.GONE);
				}
				if(position==selectID){
					holder.getView(R.id.img_select).setVisibility(View.VISIBLE);
				}else{
					holder.getView(R.id.img_select).setVisibility(View.GONE);
				}
			}
		};
		mBinding.recyclerview.setAdapter(mAdapter);
		mAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int position) {
				mAdapter.notifyItemChanged(selectID);
				selectID=position;
//				mAdapter.notifyDataSetChanged();
				mAdapter.notifyItemChanged(position);
			}
		});
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		if(MyApplication.getUserData().loginStatue){
			mBinding.tvEditPass.setVisibility(View.VISIBLE);
		}else{
			finish();
		}
	}

	public void clickHandler(View view) {
		Intent intent=null;
		switch (view.getId()) {
			case R.id.tv_edit:
				if(mBinding.tvEdit.getText().toString().contains("修改")){
					mBinding.tvEdit.setText("确定");
					mBinding.hobbyFlowlayout.setVisibility(View.VISIBLE);
				}else{
					mBinding.tvEdit.setText("修改");
					mBinding.hobbyFlowlayout.setVisibility(View.GONE);
				}
				break;
			case R.id.imgReturn:
				onBackPressed();
				break;
			case R.id.tv_account:
				if(!mBinding.tvAccount.getText().toString().contains("手机号")){
					return;
				}
				intent=new Intent(this, ForgetPassActivity.class);
				intent.putExtra("type",1);//1,绑定手机号，2修改密码，3忘记密码
				startActivity(intent);
				break;
			case R.id.tv_submit://提交数据
				if(mBinding.etBirth.getText().toString().contains("设置生日")){
					ToastUtil.showToast(mContext,"请选择生日！");
					return;
				}
				int uid= Integer.parseInt(MyApplication.getUserData().uid);
				genderID=mBinding.dataeditManRadiobutton.isChecked()?1:2;
				String tempID=mHeadList.get(selectID).getId();
				long time=System.currentTimeMillis();
				String validate=""+uid+mBinding.tvName.getText().toString()+tempID+genderID+mBinding.etBirth.getText().toString()+cityID+headTitle+time;
				String sign= StringUtil.MD5(validate);
				JSONObject obj = new JSONObject();
				try {
					obj.put("uid", uid);
					obj.put("nickname", mBinding.tvName.getText().toString());
					obj.put("avatar", tempID);
					obj.put("gender", genderID);//1男2女
					obj.put("birthday", mBinding.etBirth.getText().toString());//2018-03-24
					obj.put("personalized", mHobbyTxt);//个人签名
					obj.put("region",cityID);//城市编号
					obj.put("region_area",areaID);//县编号
					obj.put("title", headTitle);
					obj.put("time", time);
					obj.put("sign", sign);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				//同步请求
				Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
				UserService.Api service = retrofit.create(UserService.Api.class);
				RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
				Call<ResponseBody> call = service.modifySettingInfo(body);
				call.enqueue(new Callback<ResponseBody>() {
					@Override
					public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
						Log.e("requestSuccess", "-----------------------" + response.body());
						try {
							String jsonString=response.body().string();
							JSONObject jsonObject=new JSONObject(jsonString);
							if(jsonObject.optInt("code")==1){
								UserDataBean userDataBean =MyApplication.getUserData();
								userDataBean.nickname=mBinding.tvName.getText().toString();
								userDataBean.avatarstr=mHeadList.get(selectID).getUrl();
								MyApplication.writeUserData(userDataBean);

								JSONObject jsonSub=new JSONObject(jsonObject.opt("data").toString());
								int newScore=jsonSub.optInt("integral");//剩余积分
								MyApplication.getUserData().integral=newScore;
								mBinding.tvScore.setText("我的积分："+newScore);
								String str_result=jsonSub.optString("integralmsg");
								if(StringUtil.isEmpty(str_result))
									ToastUtil.showToast(mContext,jsonObject.optString("msg")+"");
								else
									ToastUtil.showToast(mContext,str_result+"");
								finish();
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
				break;
			case R.id.tv_edit_pass:

				if(canNick){
					dialogHelper = new DialogHelper(mContext, new CallBack() {
						@Override
						public void callBackFunction() {
							// TODO Auto-generated method stub
							if(dialogHelper.isConfirm()){
								DialogUIUtils.showAlertInput(SetupActivity.this, "修改昵称", "请输入昵称",
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
													mBinding.tvName.setText(input1.toString());
													mBinding.tvName.setText(input1.toString());
												}else{
													ToastUtil.showToast(SetupActivity.this,"昵称不能为空");
												}
											}
										}).show();
							}
						}
					});
					dialogHelper.showDialog("提示", "修改昵称将扣除"+needScore+"积分", false);
				}else{
					showPopView(view,"无法修改昵称！");
				}



				break;
			case R.id.xiugai_birther:
				Calendar calendar = Calendar.getInstance();
				LayoutInflater inflater1 = LayoutInflater.from(this);
				final View timepickerview1 = inflater1.inflate(R.layout.pop_timepicker,null);
//			ScreenInfo screenInfo1 = new ScreenInfo(MainActivity.this);
				wheelMain = new WheelMain(timepickerview1);
				wheelMain.screenheight = ScreenUtil.getScreenHeight(this);
				String time1 =calendar.get(Calendar.YEAR) + "-"
						+ (calendar.get(Calendar.MONTH) + 1) + "-"
						+ calendar.get(Calendar.DAY_OF_MONTH) + "";
				Calendar calendar1 = Calendar.getInstance();
				int year1 = calendar1.get(Calendar.YEAR);
				int month1 = calendar1.get(Calendar.MONTH);
				int day1 = calendar1.get(Calendar.DAY_OF_MONTH);
				wheelMain.initDateTimePicker(year1, month1, day1);
				final MyAlertDialog dialog = new MyAlertDialog(this)
						.builder()
						.setTitle("选择时间")
//			 .setMsg("再连续登陆15天，就可变身为QQ达人。退出QQ可能会使你现有记录归零，确定退出？")
//			 .setEditText("1111111111111")
						.setView(timepickerview1)
						.setNegativeButton("取消", new View.OnClickListener() {
							@Override
							public void onClick(View v) {

							}
						});
				dialog.setPositiveButton("保存", new View.OnClickListener() {
					@Override
					public void onClick(View v) {
//						ToastUtil.showToast(mContext,wheelMain.getTime());
						if(TimeUtil.isTrueDate(wheelMain.getTime())){
							ToastUtil.showToast(mContext,"出生日期必须小于当前日期！");
						}else{
							mBinding.etBirth.setText(wheelMain.getTime());
						}
					}
				});
				dialog.show();
				break;
			case R.id.xiugai_address:
				View dialogView = dialogm();
				final MyAlertDialog dialog1 = new MyAlertDialog(mContext)
						.builder()
						.setTitle(mBinding.btnSelectArea.getText().toString())
						// .setMsg("再连续登陆15天，就可变身为QQ达人。退出QQ可能会使你现有记录归零，确定退出？")
						// .setEditText("1111111111111")
						.setView(dialogView)
						.setNegativeButton("取消", new View.OnClickListener() {
							@Override
							public void onClick(View v) {

							}
						});
				dialog1.setPositiveButton("保存", new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mBinding.btnSelectArea.setText(cityTxt);
					}
				});
				dialog1.show();
				break;
			case R.id.tv_qq:
				type=1;
				if(mBinding.tvQq.getText().toString().contains("解除")){
					UMShareAPI.get(this).deleteOauth(this,SHARE_MEDIA.QQ,umDelListener);
					unBindopen();
				}else{
//					UMShareAPI.get(this).doOauthVerify(this, SHARE_MEDIA.QQ, umAuthListener);
					UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.QQ, umAuthListener);
				}
				break;
			case R.id.tv_weixin:
				type=3;
				if(mBinding.tvWeixin.getText().toString().contains("解除")){
					UMShareAPI.get(this).deleteOauth(this,SHARE_MEDIA.WEIXIN,umDelListener);
					unBindopen();
				}else{
					UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.WEIXIN, umAuthListener);
				}
				break;
			case R.id.tv_sina:
				type=2;
				if(mBinding.tvSina.getText().toString().contains("解除")){
					UMShareAPI.get(this).deleteOauth(this,SHARE_MEDIA.SINA,umDelListener);
					unBindopen();
				}else{
//					UMShareAPI.get(this).doOauthVerify(this, SHARE_MEDIA.SINA, umAuthListener);
					UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.SINA, umAuthListener);
				}
				break;
		}
	}
	
	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	private void initData(GetSettingRespBean bean){
		mlevel2=bean.getData().getLevel2();
		mlevel3=bean.getData().getLevel3();
		mlevel4=bean.getData().getLevel4();
		headTitle=bean.getData().getTitle();//用户头衔
		title_level=bean.getData().getTitle_level();//用户头衔等级
		headID=bean.getData().getAvatar();
		cityID=bean.getData().getRegion();
		areaID=bean.getData().getRegion_area();
		mHeadList=bean.getData().getAvatarlist();
		int forSize=mHeadList.size();
		for(int i=0;i<forSize;i++){
			if(mHeadList.get(i).getId().equals(""+headID)){
				selectID=i;
				break;
			}
		}
		
		GlideUtil.loadRoundImage(mContext,bean.getData().getAvatarstr(),mBinding.imgHead,5);
		mBinding.tvName.setText(bean.getData().getNickname());
		mBinding.tvScore.setText("我的积分："+bean.getData().getIntegral());
//		mBinding.tvName.setText(bean.getData().getUsername());
		if(StringUtil.isEmpty(bean.getData().getMobile())){
			mBinding.tvEditPass.setVisibility(View.INVISIBLE);
			mBinding.tvAccount.setText("暂无账号，请绑定手机号！");
		}else{
			mBinding.tvEditPass.setVisibility(View.VISIBLE);
			mBinding.tvAccount.setText(bean.getData().getMobile());
			mBinding.tvAccount.setTextColor(getResources().getColor(R.color.white_9c));
		}
		mBinding.userLv.setText("Lv."+lv);

		mBinding.etNickname.setText(bean.getData().getNickname());
		needScore=bean.getData().getUpnickintegral();
		if(bean.getData().getIntegral()<needScore){//昵称是否可修改：1是0否  bean.getData().getIscannick()==0
			mBinding.etNickname.setFocusable(false);
			mBinding.etNickname.setFocusableInTouchMode(false);
			mBinding.etNickname.setTextColor(getResources().getColor(R.color.white_9c));
		}else{
			canNick=true;
			mBinding.etNickname.setTextColor(getResources().getColor(R.color.blue));
		}
		mBinding.tvQq.setText(bean.getData().getIsqq()==0?"请先绑定账号":"解除绑定");
		mBinding.tvWeixin.setText(bean.getData().getIswechat()==0?"请先绑定账号":"解除绑定");
		mBinding.tvSina.setText(bean.getData().getIssina()==0?"请先绑定账号":"解除绑定");
		if(bean.getData().getGender()==1){
			mBinding.dataeditManRadiobutton.setChecked(true);
		}else if(bean.getData().getGender()==2){
			mBinding.dataeditWomanRadiobutton.setChecked(true);
		}
		mBinding.etBirth.setText(StringUtil.isEmpty(bean.getData().getBirthday())?"未设置生日":bean.getData().getBirthday());
//		mBinding.etSign.setText(bean.getData().getPersonalized());
		mBinding.tvHobby.setText(StringUtil.isEmpty(bean.getData().getPersonalized())?"请选择":bean.getData().getPersonalized());
		mHobbyTxt=bean.getData().getPersonalized();//获取初始的兴趣爱好
		initFlow(bean);
		if(bean.getData().getRegion_province()>0&&bean.getData().getRegion_province()<34){//Arrays.binarySearch  数组查询方法
			try {
				int provinceIndex=bean.getData().getRegion_province()-1;
				int cityIndex=Arrays.binarySearch(AddressData.C_ID[provinceIndex],bean.getData().getRegion());
				int countryIndex=Arrays.binarySearch(AddressData.C_C_ID[provinceIndex][cityIndex],bean.getData().getRegion_area());
				mBinding.btnSelectArea.setText(AddressData.PROVINCES[provinceIndex]+" | "+ AddressData.CITIES[provinceIndex][cityIndex]+" | "+
						AddressData.COUNTIES[provinceIndex][cityIndex][countryIndex]);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				mBinding.btnSelectArea.setText("选择地址");
			}
		}
		mAdapter.clearAndAddList(mHeadList);
	}

	@SuppressWarnings("ResourceType")
	public void initFlow(GetSettingRespBean respBean) {
		LayoutInflater mInflater = LayoutInflater.from(mContext);
		if(respBean.getData().getLevels()==null)
			return;
		//初级头衔
		if(respBean.getData().getLevels().getLevel1().size()>0){
			String[] level1=respBean.getData().getLevels().getLevel1().toArray(new String[respBean.getData().getLevels().getLevel1().size()+1]);
			final int len1=level1.length;
			level1[len1-1]="已开启";
			for (int i = 0; i <len1;i++) {
				final TextView textView = new TextView(mContext);
				//这里的Textview的父layout是ListView，所以要用ListView.LayoutParams
				ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				layoutParams.setMargins(1,3,35,3);
				textView.setLayoutParams(layoutParams);
				textView.setPadding(1,2,5,2);
				textView.setGravity(Gravity.CENTER);
				textView.setText(""+level1[i]);
				textView.setTextColor(level1[i].endsWith(headTitle)?getResources().getColor(R.color.blue):getResources().getColor(R.color.white_9c));
				if(i==len1-1)
					textView.setTextColor(getResources().getColor(R.color.blue));
				final int finalI = i;
				textView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						reSetStatue();
						title_level = 1;
						headTitle = textView.getText().toString();
						textView.setTextColor(getResources().getColor(R.color.blue));
					}
				});
				mBinding.level1Flowlayout.addView(textView);
			}
		}
		
		
		//中级头衔
		if(respBean.getData().getLevels().getLevel2().size()>0){
			String[] level2=respBean.getData().getLevels().getLevel2().toArray(new String[respBean.getData().getLevels().getLevel2().size()+1]);
			final int len2=level2.length;
			level2[len2-1]="开启";
			for (int i = 0; i <len2;i++) {
				final TextView textView = new TextView(mContext);
				//这里的Textview的父layout是ListView，所以要用ListView.LayoutParams
				ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				layoutParams.setMargins(1,3,35,3);
				textView.setLayoutParams(layoutParams);
				textView.setPadding(1,2,5,2);
				textView.setGravity(Gravity.CENTER);
				textView.setText(""+level2[i]);
				textView.setTextColor(level2[i].endsWith(headTitle)?getResources().getColor(R.color.blue):getResources().getColor(R.color.white_9c));
				if(i==len2-1){
					textView.setText(0==mlevel2?"开启":"已开启");
					textView.setTextColor(0==mlevel2?getResources().getColor(R.color.white_9c):getResources().getColor(R.color.blue));
				}
				final int finalI = i;
				textView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if(finalI ==len2-1){
							if(0==mlevel2){
								if(MyApplication.getUserData().integral<2){//积分不足
									showPopView(textView,"无法开启头衔！");
								}else {
									dialogHelper = new DialogHelper(mContext, new CallBack() {
										@Override
										public void callBackFunction() {
											// TODO Auto-generated method stub
											if (dialogHelper.isConfirm()) {
												openTitle(2, textView);
											}
										}
									});
									dialogHelper.showDialog("提示", "开启中级头衔，\n需要扣除2积分！", false);
								}
							}
						}else{
							reSetStatue();
							title_level=2;
							headTitle=textView.getText().toString();
							textView.setTextColor(getResources().getColor(R.color.blue));
						}
					}
				});
				mBinding.level2Flowlayout.addView(textView);
			}
		}
		
		
		//高级头衔
		if(respBean.getData().getLevels().getLevel3().size()>0){
			String[] level3=respBean.getData().getLevels().getLevel3().toArray(new String[respBean.getData().getLevels().getLevel3().size()+1]);
			final int len3=level3.length;
			level3[len3-1]="开启";
			for (int i = 0; i <len3;i++) {
				final TextView textView = new TextView(mContext);
				//这里的Textview的父layout是ListView，所以要用ListView.LayoutParams
				ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				layoutParams.setMargins(1,3,35,3);
				textView.setLayoutParams(layoutParams);
				textView.setPadding(1,2,5,2);
				textView.setGravity(Gravity.CENTER);
				textView.setText(""+level3[i]);
				textView.setTextColor(level3[i].endsWith(headTitle)?getResources().getColor(R.color.blue):getResources().getColor(R.color.white_9c));
				if(i==len3-1){
					textView.setText(0==mlevel3?"开启":"已开启");
					textView.setTextColor(0==mlevel3?getResources().getColor(R.color.white_9c):getResources().getColor(R.color.blue));
				}
				final int finalI = i;
				textView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if(finalI ==len3-1){
							if(0==mlevel3){
								if(0==mlevel3){
									if(MyApplication.getUserData().integral<4){//积分不足
										showPopView(textView,"无法开启头衔！");
									}else {
										dialogHelper = new DialogHelper(mContext, new CallBack() {
											@Override
											public void callBackFunction() {
												// TODO Auto-generated method stub
												if (dialogHelper.isConfirm()) {
													openTitle(3, textView);
												}
											}
										});
										dialogHelper.showDialog("提示", "开启高级头衔，\n需要扣除4积分！", false);
									}
								}
							}
						}else{
							reSetStatue();
							title_level=3;
							headTitle=textView.getText().toString();
							textView.setTextColor(getResources().getColor(R.color.blue));
						}
					}
				});
				mBinding.level3Flowlayout.addView(textView);
			}
		}
		
		
		//顶级头衔
		if(respBean.getData().getLevels().getLevel4().size()>0){
			String[] level4=respBean.getData().getLevels().getLevel4().toArray(new String[respBean.getData().getLevels().getLevel4().size()+1]);
			final int len4=level4.length;
			level4[len4-1]="开启";
			for (int i = 0; i <len4;i++) {
				final TextView textView = new TextView(mContext);
				//这里的Textview的父layout是ListView，所以要用ListView.LayoutParams
				ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				layoutParams.setMargins(1,3,35,3);
				textView.setLayoutParams(layoutParams);
				textView.setPadding(1,2,5,2);
				textView.setGravity(Gravity.CENTER);
				textView.setText(""+level4[i]);
				textView.setTextColor(level4[i].endsWith(headTitle)?getResources().getColor(R.color.blue):getResources().getColor(R.color.white_9c));
				if(i==len4-1){
					textView.setText(0==mlevel4?"开启":"已开启");
					textView.setTextColor(0==mlevel4?getResources().getColor(R.color.white_9c):getResources().getColor(R.color.blue));
				}
				final int finalI = i;
				textView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if(finalI ==len4-1){
							if(0==mlevel4){//未开启
								if(MyApplication.getUserData().integral<6){//积分不足
									showPopView(textView,"无法开启头衔！");
								}else{
									dialogHelper = new DialogHelper(mContext, new CallBack() {
										@Override
										public void callBackFunction() {
											// TODO Auto-generated method stub
											if(dialogHelper.isConfirm()){
												openTitle(4,textView);
											}
										}
									});
									dialogHelper.showDialog("提示", "开启特级头衔，\n需要扣除6积分！", false);
								}
							}
						}else{
							reSetStatue();
							title_level=4;
							headTitle=textView.getText().toString();
							textView.setTextColor(getResources().getColor(R.color.blue));
						}
					}
				});
				mBinding.level4Flowlayout.addView(textView);
			}
		}
		
		String str_hobby=respBean.getData().getHobbyconfig();
		final String[] config=str_hobby.split(",");
		int forSize=config.length;
		for (int i = 0; i <forSize;i++) {
			final CheckBox button = (CheckBox) mInflater.inflate(R.layout.layout_flow_checkbox, mBinding.hobbyFlowlayout,false);
			final String label=config[i];
			button.setText(label);
			if (mHobbyTxt.contains(config[i])) {
				button.setChecked(true);
			}
			boolean statue=button.isChecked();
			button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						if(StringUtil.countStr(mHobbyTxt,",")<4){
							setHobbyText(label,isChecked);
						}else{
							ToastUtil.showToast(mContext,"最多设置5个标签！");
							button.setChecked(!isChecked);
						}
					}else{
						setHobbyText(label,isChecked);
					}
				}
			});
			mBinding.hobbyFlowlayout.addView(button);
		}
	}
	
	public void setHobbyText(String text,boolean isChecked){
		if(isChecked) {
			mHobbyTxt = mHobbyTxt + "," + text;
			if (mHobbyTxt.startsWith(","))
				mHobbyTxt = mHobbyTxt.subSequence(1, mHobbyTxt.length()).toString();
		}else{
			mHobbyTxt=mHobbyTxt.replace(text,"");
			if(mHobbyTxt.startsWith(","))
				mHobbyTxt=mHobbyTxt.subSequence(1, mHobbyTxt.length()).toString();
			else if(mHobbyTxt.endsWith(","))
				mHobbyTxt=mHobbyTxt.subSequence(0, mHobbyTxt.length()-1).toString();
			else if(mHobbyTxt.contains(",,"))
				mHobbyTxt=mHobbyTxt.replace(",,",",");
		}
		mBinding.tvHobby.setText(StringUtil.isEmpty(mHobbyTxt)?"请选择":mHobbyTxt);
	}

	private void bindopen(){
		long time=System.currentTimeMillis();
		String validate=""+MyApplication.getUserData().uid+openID+type+appOpenid+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid",MyApplication.getUserData().uid);
			obj.put("openid", openID);
			obj.put("type", type);
			obj.put("appopenid", appOpenid);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		UserService.Api service = retrofit.create(UserService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<ResponseBody> call = service.bindopen(body);
		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				try {
					String jsonString=response.body().string();
					Log.e("requestSuccess", "-----------------------" + jsonString);
					JSONObject jsonObject=new JSONObject(jsonString);
					if(jsonObject.optInt("code")==1){
						ToastUtil.showToast(mContext,"绑定成功！");
						switch (type) {
							case 1:
								mBinding.tvQq.setText("解除绑定");//"请先绑定账号":"解除绑定"
								break;
							case 2:
								mBinding.tvSina.setText("解除绑定");//"请先绑定账号":"解除绑定"
								break;
							case 3:
								mBinding.tvWeixin.setText("解除绑定");//"请先绑定账号":"解除绑定"
								break;
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
	
	private void reSetStatue(){
		int length=0;
		switch (title_level) {
			case 1:
				length=mBinding.level1Flowlayout.getChildCount();
				for(int j=0;j<length-1;j++){
					TextView tv= (TextView) mBinding.level1Flowlayout.getChildAt(j);
					tv.setTextColor(getResources().getColor(R.color.white_9c));
				}
				break;
			case 2:
				length=mBinding.level2Flowlayout.getChildCount();
				for(int j=0;j<length-1;j++){
					TextView tv= (TextView) mBinding.level2Flowlayout.getChildAt(j);
					tv.setTextColor(getResources().getColor(R.color.white_9c));
				}
				break;
			case 3:
				length=mBinding.level3Flowlayout.getChildCount();
				for(int j=0;j<length-1;j++){
					TextView tv= (TextView) mBinding.level3Flowlayout.getChildAt(j);
					tv.setTextColor(getResources().getColor(R.color.white_9c));
				}
				break;
			case 4:
				length=mBinding.level4Flowlayout.getChildCount();
				for(int j=0;j<length-1;j++){
					TextView tv= (TextView) mBinding.level4Flowlayout.getChildAt(j);
					tv.setTextColor(getResources().getColor(R.color.white_9c));
				}
				break;
			default:
				break;
		}
	}

	// 选择图片的弹窗
	public void showPopView(View v,String text) {//是否显示开启头衔
		View pubPop = LayoutInflater.from(this).inflate(R.layout.view_nopoint, null);
		final PopupWindow popupWindow = DialogHelper.createPopupWindow(this, pubPop, R.style.AnimBottom);
		TextView tv_content = (TextView) pubPop.findViewById(R.id.txtContent);
		tv_content.setText(text);
		TextView tv_update = (TextView) pubPop.findViewById(R.id.txtSubmit);
		tv_update.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent aggreement = new Intent(mContext, SingleWebActivity.class);
				aggreement.putExtra("title", "积分说明");
				aggreement.putExtra("url", SingleWebActivity.mUrl_ScoreInfo + "?uid=" + MyApplication.getUserData().uid);
				startActivity(aggreement);
				popupWindow.dismiss();
			}
		});
		TextView tv_ignore=(TextView) pubPop.findViewById(R.id.tv_ignore);
		tv_ignore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				popupWindow.dismiss();
//				SharedUtil.setSharedPreferencesData("noUp","1");
			}
		});
		DialogHelper.showPop(SetupActivity.this, v, popupWindow, Gravity.CENTER, 0, 0);
	}

	//开启头衔
	private void openTitle(int level, final TextView tv){//2中级头衔3高级头衔4顶级头衔
		long time=System.currentTimeMillis();
		String validate=""+MyApplication.getUserData().uid+level+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid",MyApplication.getUserData().uid);
			obj.put("level", level);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		UserService.Api service = retrofit.create(UserService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<ResponseBody> call = service.openTitle(body);
		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				try {
					String jsonString=response.body().string();
					Log.e("requestSuccess", "-----------------------" + jsonString);
					JSONObject jsonObject=new JSONObject(jsonString);
					if(jsonObject.optInt("code")==1){
						tv.setTextColor(getResources().getColor(R.color.blue));
						JSONObject jsonSub=new JSONObject(jsonObject.opt("data").toString());
						int newScore=jsonSub.optInt("integral");//剩余积分
						mBinding.tvScore.setText("我的积分："+newScore);
						UserDataBean teUser =MyApplication.getUserData();
						teUser.integral=newScore;
						MyApplication.writeUserData(teUser);
						String str_result=jsonSub.optString("integralmsg");
						if(!StringUtil.isEmpty(str_result))
							ToastUtil.showToast(mContext,str_result+"");
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
	
	private void unBindopen(){
		long time=System.currentTimeMillis();
		String validate=""+MyApplication.getUserData().uid+type+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid",MyApplication.getUserData().uid);
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
		Call<ResponseBody> call = service.unbindopen(body);
		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				try {
					String jsonString=response.body().string();
					Log.e("requestSuccess", "-----------------------" + jsonString);
					JSONObject jsonObject=new JSONObject(jsonString);
					if(jsonObject.optInt("code")==1){
						ToastUtil.showToast(mContext,"解绑成功！");
						switch (type) {
							case 1:
								mBinding.tvQq.setText("请先绑定账号");//"请先绑定账号":"解除绑定"
								break;
							case 2:
								mBinding.tvSina.setText("请先绑定账号");//"请先绑定账号":"解除绑定"
								break;
							case 3:
								mBinding.tvWeixin.setText("请先绑定账号");//"请先绑定账号":"解除绑定"
								break;
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

	private UMAuthListener umAuthListener = new UMAuthListener(){
		@Override
		public void onStart(SHARE_MEDIA share_media) {
			Log.i("umeng_login_onStart","获取开始");
		}

		@Override
		public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
			Log.i("umeng_login_onComplete","获取开始");
			if(type==3){//1qq  2微博  3微信
				openID=map.get("unionid");
				appOpenid=map.get("openid");
			}else if(type==2){
				openID=map.get("uid");
			}else{
				openID=map.get("unionid");
				appOpenid=map.get("openid");
			}

			bindopen();
		}

		@Override
		public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
			Log.i("umeng_login_onError","获取失败");
//			ToastUtil.showToast(mContext,throwable.getMessage());
		}

		@Override
		public void onCancel(SHARE_MEDIA share_media, int i) {
			Log.i("umeng_login_userinfo","获取取消");

		}
	};

	private UMAuthListener umDelListener = new UMAuthListener(){
		@Override
		public void onStart(SHARE_MEDIA share_media) {
			Log.i("umeng_login_onStart","删除授权开始");
		}

		@Override
		public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
			Log.i("umeng_del_onComplete","删除授权结束");
		}

		@Override
		public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
			Log.i("umeng_del_onError","删除授权失败");

		}

		@Override
		public void onCancel(SHARE_MEDIA share_media, int i) {
			Log.i("umeng_del_userinfo","删除授权取消");
		}
	};

	/**
	 * Adapter for countries
	 */
	private class CountryAdapter extends AbstractWheelTextAdapter {
		// Countries names
		private String countries[] = AddressData.PROVINCES;

		/**
		 * Constructor
		 */
		protected CountryAdapter(Context context) {
			super(context, R.layout.wheelcity_country_layout, NO_RESOURCE);
			setItemTextResource(R.id.wheelcity_country_name);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return countries.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return countries[index];
		}
	}

	private View dialogm() {
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.wheelcity_cities_layout, null);
		final WheelView country = (WheelView) contentView
				.findViewById(R.id.wheelcity_country);
		country.setVisibleItems(3);
		country.setViewAdapter(new CountryAdapter(this));

		final String cities[][] = AddressData.CITIES;
		final String ccities[][][] = AddressData.COUNTIES;
		final WheelView city = (WheelView) contentView
				.findViewById(R.id.wheelcity_city);
		city.setVisibleItems(3);
		city.setVisibility(View.VISIBLE);

		// 地区选择
		final WheelView ccity = (WheelView) contentView
				.findViewById(R.id.wheelcity_ccity);
		ccity.setVisibleItems(3);// 不限城市
		ccity.setVisibility(View.VISIBLE);

		country.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateCities(city, cities, newValue);
				updatecCities(ccity, ccities, newValue,0);
				cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
						+ " | "
						+ AddressData.CITIES[country.getCurrentItem()][city
						.getCurrentItem()]
						+ " | "
						+ AddressData.COUNTIES[country.getCurrentItem()][city
						.getCurrentItem()][ccity.getCurrentItem()];
				cityID=AddressData.C_ID[country.getCurrentItem()][city.getCurrentItem()];
				areaID=AddressData.C_C_ID[country.getCurrentItem()][city.getCurrentItem()][ccity.getCurrentItem()];
			}

			@Override
			public void onChanged(WheelViewTime wheel, int oldValue,
			                      int newValue) {
				// TODO Auto-generated method stub

			}
		});

		city.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updatecCities(ccity, ccities, country.getCurrentItem(),
						city.getCurrentItem());

				cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
						+ " | "
						+ AddressData.CITIES[country.getCurrentItem()][city
						.getCurrentItem()]
						+ " | "
						+ AddressData.COUNTIES[country.getCurrentItem()][city
						.getCurrentItem()][ccity.getCurrentItem()];
				cityID=AddressData.C_ID[country.getCurrentItem()][city.getCurrentItem()];
				areaID=AddressData.C_C_ID[country.getCurrentItem()][city.getCurrentItem()][ccity.getCurrentItem()];
			}

			@Override
			public void onChanged(WheelViewTime wheel, int oldValue,
			                      int newValue) {
				// TODO Auto-generated method stub

			}
		});

		ccity.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
						+ " | "
						+ AddressData.CITIES[country.getCurrentItem()][city
						.getCurrentItem()]
						+ " | "
						+ AddressData.COUNTIES[country.getCurrentItem()][city
						.getCurrentItem()][ccity.getCurrentItem()];
				cityID=AddressData.C_ID[country.getCurrentItem()][city.getCurrentItem()];
				areaID=AddressData.C_C_ID[country.getCurrentItem()][city.getCurrentItem()][ccity.getCurrentItem()];
			}

			@Override
			public void onChanged(WheelViewTime wheel, int oldValue,
			                      int newValue) {
				// TODO Auto-generated method stub

			}
		});

		country.setCurrentItem(1);// 设置上海
		city.setCurrentItem(1);
		ccity.setCurrentItem(0);
		return contentView;
	}

	/**
	 * Updates the city wheel
	 */
	private void updateCities(WheelView city, String cities[][], int index) {
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this,
				cities[index]);
		adapter.setTextSize(18);
		city.setViewAdapter(adapter);
		city.setCurrentItem(0);
	}

	/**
	 * Updates the ccity wheel
	 */
	private void updatecCities(WheelView city, String ccities[][][], int index,
	                           int index2) {
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this,
				ccities[index][index2]);
		adapter.setTextSize(18);
		city.setViewAdapter(adapter);
		city.setCurrentItem(0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
	}
}