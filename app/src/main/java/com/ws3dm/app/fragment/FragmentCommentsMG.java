package com.ws3dm.app.fragment;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.ForgetPassActivity;
import com.ws3dm.app.activity.LoginActivity;
import com.ws3dm.app.adapter.BaseRecyclerAdapter;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.bean.CommentBean;
import com.ws3dm.app.bean.SoftGameBean;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.databinding.FgMgCommentsBinding;
import com.ws3dm.app.mvp.model.RespBean.NewsCommentRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.MGService;
import com.ws3dm.app.network.service.NewsService;
import com.ws3dm.app.network.service.UserService;
import com.ws3dm.app.util.SharedUtil;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.util.glide.GlideUtil;
import com.ws3dm.app.view.CircleProgress;
import com.ws3dm.app.view.InputWindowListener;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Describution :手游评论页
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2017/8/18 17:22
 **/
public class FragmentCommentsMG extends BaseFragment {
	private FgMgCommentsBinding mBinding;

//	private View view;
	private int mPage;
	public static String strType = ""; // 判断加载此Fragment的页面
	private String strContent;
	private boolean isFirst;
	private InputMethodManager imm;
	private int f_sid,replyId = 0,favorite=2;//favorite:1已收藏，2为收藏
	private CommonRecyclerAdapter mAdapter;
	public static EditText etComment;
	private List<CommentBean> listData = new ArrayList<>();
	private SoftGameBean mSoftGame;
	private int firPo,secPo;
	private int c_sid;//当前文章在评论里唯一标识(默认为0)
	private View header;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		AppCompatActivity activity = (AppCompatActivity) getActivity();
		mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_mg_comments, container, false);
		mBinding.setHandler(this);
		header = LayoutInflater.from(mContext).inflate(R.layout.header_mg, container,false);
		header.findViewById(R.id.tv_good).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(MyApplication.getUserData()==null||!MyApplication.getUserData().loginStatue){
					ToastUtil.showToast(mContext,"请先登录！");
					startActivity(new Intent(mContext, LoginActivity.class));
				}else if(MyApplication.getUserData().mobile.length()==0){//第三方登陆，非绑定跳转绑定界面
					Intent intent=new Intent(mContext, ForgetPassActivity.class);
					intent.putExtra("type",1);//1,绑定手机号，2修改密码，3忘记密码
					startActivity(intent);
				}else{
					submitScore(1);
				}
			}
		});
		header.findViewById(R.id.tv_bad).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(MyApplication.getUserData()==null||!MyApplication.getUserData().loginStatue){
					ToastUtil.showToast(mContext,"请先登录！");
					startActivity(new Intent(mContext, LoginActivity.class));
				}else if(MyApplication.getUserData().mobile.length()==0){//第三方登陆，非绑定跳转绑定界面
					Intent intent=new Intent(mContext, ForgetPassActivity.class);
					intent.putExtra("type",1);//1,绑定手机号，2修改密码，3忘记密码
					startActivity(intent);
				}else{
					submitScore(2);
				}
			}
		});
		initView();
		return mBinding.getRoot();
	}
	//关注or取消
	private void subfollow(int type,String follow_uid){
		UserDataBean userData = MyApplication.getUserData();
		long time = System.currentTimeMillis();
		String sign = StringUtil.newMD5(userData.uid + follow_uid +type + time + NewUrl.KEY);

		Map<String, Object> values = new HashMap<>();
		values.put("uid",userData.uid);
		values.put("sign",sign);
		values.put("time",time);
		values.put("type",type);
		values.put("follow_uid",follow_uid);
		String json = new JSONObject(values).toString();
		OkHttpUtils
				.postString()
				.url(NewUrl.SUBFOLLOW)
				.content(json)
				.build()
				.execute(new com.zhy.http.okhttp.callback.Callback<String>() {

					@Override
					public String parseNetworkResponse(com.squareup.okhttp.Response response) throws IOException {
						return response.body().string();
					}

					@Override
					public void onError(com.squareup.okhttp.Request request, Exception e) {
						Log.e("message",e.getMessage());
					}

					@Override
					public void onResponse(String response) {
						Log.e("FFFFFFFF",response);
					}

				});

	}
	public void initView() {
		if (getArguments().getSerializable("mSoftGame") != null) {
			mSoftGame = (SoftGameBean) getArguments().getSerializable("mSoftGame");
		}
		mPage = 1;
		isFirst = true;
		etComment = mBinding.etComment;
		imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//		cyanSdk = MyApplication.getInstance().cyanSdk;
//		if (SharedUtil.getSharedPreferencesData("isNight").equals("0"))
//			mBinding.imgLine.setBackgroundResource(R.drawable.line_bottom);
//		else
//			mBinding.imgLine.setBackgroundResource(R.drawable.line_bottom_night);

		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.mRecycleView.setLayoutManager(layoutManager);
		mBinding.mRecycleView.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
		mBinding.mRecycleView.setPullRefreshEnabled(false);
		mBinding.mRecycleView.setLoadingMoreEnabled(true);

		mAdapter = new CommonRecyclerAdapter<CommentBean>(mContext, R.layout.adapter_comments) {
			@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
			@Override
			public void bindData(RecyclerViewHolder holder, final int position, final CommentBean comments) {
				holder.getView(R.id.llAllComments).setVisibility(View.GONE);
				final TextView tvGood=holder.getView(R.id.tv_good);
				final ImageView img_good = holder.getView(R.id.imgGood);
				img_good.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if(MyApplication.getUserData().loginStatue) {
							if(MyApplication.getUserData().mobile.length()==0){
								Intent intent=new Intent(mContext, ForgetPassActivity.class);
								intent.putExtra("type",1);//1,绑定手机号，2修改密码，3忘记密码
								startActivity(intent);
							}else{
								if(comments.getPraise()==0)
									setPraise(img_good,tvGood,comments.getGoodcount(),comments.getId(),1);//1赞2踩
							}
						}else{
							ToastUtil.showToast(mContext,"请先登录！");
							startActivity(new Intent(mContext,LoginActivity.class));
						}
						
					}
				});
//				holder.setImageByUrl(R.id.img_head, comments.getUser().avatarstr);
//				holder.setText(R.id.tv_name, comments.getUser().nickname);
//				holder.setText(R.id.tv_locate, "第"+comments.getPosition()+"楼  "+comments.getUser().regionstr+" 网友");
				GlideUtil.loadCircleImage(mContext,comments.getUser().avatarstr,(ImageView) holder.getView(R.id.img_head));
				holder.setText(R.id.tv_name,comments.getUser().nickname);
				holder.setText(R.id.tv_locate,comments.getUser().regionstr.length()>0?comments.getUser().regionstr+" 网友":"");
				TextView tv_head_title=holder.getView(R.id.tv_head_title);
				tv_head_title.setTextColor(getResources().getColor(R.color.lightBlack));
				final TextView follow = holder.getView(R.id.follow);
				if(comments.getUser().getIs_follow() == 0){
					follow.setText("关注");
					follow.setTextColor(Color.parseColor("#ffffff"));
					follow.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_rectangle));
				}else if(comments.getUser().getIs_follow()  == 1){
					follow.setText("已关注");
					follow.setTextColor(Color.parseColor("#444444"));
					follow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_gray));
				}else{
					follow.setText("互相关注");
					follow.setTextColor(Color.parseColor("#444444"));
					follow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_gray));
				}
				follow.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View views) {
						if(comments.getUser().getIs_follow() == 0){
							comments.getUser().setIs_follow(1);
							follow.setText("已关注");
							follow.setTextColor(Color.parseColor("#444444"));
							follow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_gray));
							subfollow(1,comments.getUser().uid+"");

						}else if(comments.getUser().getIs_follow()== 1){
							comments.getUser().setIs_follow(0);
							follow.setText("关注");
							follow.setTextColor(Color.parseColor("#ffffff"));
							follow.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_rectangle));
							subfollow(0,comments.getUser().uid+"");
						}else{
							follow.setText("关注");
							comments.getUser().setIs_follow(0);
							follow.setTextColor(Color.parseColor("#ffffff"));
							follow.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_rectangle));
							subfollow(0,comments.getUser().uid+"");
						}
						mAdapter.notifyDataSetChanged();
					}
				});

				tv_head_title.setText(""+comments.getUser().title);
				holder.setText(R.id.tv_posi,""+comments.getPosition());
				holder.setText(R.id.tv_time, TimeUtil.getFoolishTime(comments.getPubdate_at()));
				holder.setText(R.id.tv_good,comments.getGoodcount()+"");
				holder.setText(R.id.tv_bad,comments.getBadcount()+"");
				holder.setText(R.id.tv_content, comments.getContent());
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
				if(comments.getPraise()==1)
					holder.setImageResource(R.id.imgGood,R.drawable.click_good_red);
				else
					holder.setImageResource(R.id.imgGood,R.drawable.click_good_grey);

				holder.getView(R.id.tv_report).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						submitReport(comments.getId());
					}
				});
				
				TextView tv_reply = holder.getView(R.id.tv_reply);
				tv_reply.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if(MyApplication.getUserData()==null||!MyApplication.getUserData().loginStatue){
							ToastUtil.showToast(mContext,"请先登录！");
							startActivity(new Intent(mContext, LoginActivity.class));
						}else if(MyApplication.getUserData().mobile.length()==0){//第三方登陆，非绑定跳转绑定界面
							Intent intent=new Intent(mContext, ForgetPassActivity.class);
							intent.putExtra("type",1);//1,绑定手机号，2修改密码，3忘记密码
							startActivity(intent);
						}else{
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
						public void bindData(RecyclerViewHolder holder, final int position, CommentBean bean) {
							holder.setText(R.id.tv_time,TimeUtil.getFoolishTime(bean.getPubdate_at()));
							holder.setText(R.id.tv_locate,bean.getUser().regionstr.length()>0?bean.getUser().regionstr+" 网友":"");
//							    holder.setText(R.id.tv_name,bean.getUser().nickname);
//							holder.setText(R.id.tv_content,""+bean.getContent());
							TextView content=holder.getView(R.id.tv_content);
							String str="<font color='#00a0e9'>"+bean.getUser().nickname+"</font>"+" : "+bean.getContent();
							content.setText(Html.fromHtml(str));
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
//							holder.setText(R.id.tv_num_good, bean.getPraise() + "");

							TextView txtComment = holder.getView(R.id.tv_reply);
							txtComment.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View view) {
									if(MyApplication.getUserData()==null||!MyApplication.getUserData().loginStatue){
										ToastUtil.showToast(mContext,"请先登录！");
										startActivity(new Intent(mContext, LoginActivity.class));
									}else if(MyApplication.getUserData().mobile.length()==0){//第三方登陆，非绑定跳转绑定界面
										Intent intent=new Intent(mContext, ForgetPassActivity.class);
										intent.putExtra("type",1);//1,绑定手机号，2修改密码，3忘记密码
										startActivity(intent);
									}else{
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
					List<CommentBean> newList=comments.getReplies();
					Collections.sort(newList, new Comparator<CommentBean>() {
						public int compare(CommentBean o1, CommentBean o2) {
							return o1.getPubdate_at()-o2.getPubdate_at();
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
						obtainComments();
					}
				},50);
			}
		});
		mBinding.mRecycleView.setAdapter(mAdapter);
		mBinding.mRecycleView.addHeaderView(header);
//		mBinding.mRecycleView.setEmptyView(mBinding.txtNoComment);//添加空白页
		mBinding.rlAllComment.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (getActivity() != null) {
					int heightDiff = mBinding.rlAllComment.getRootView().getHeight() - mBinding.rlAllComment.getHeight();
					if (heightDiff > 360) {
//						String text = "    ";
//						etComment.setText(text);
//						etComment.setSelection(text.length());

						etComment.setCursorVisible(true);
						mBinding.txtInput.setVisibility(View.VISIBLE);
					} else {
//						etComment.setText("");

						etComment.setCursorVisible(false);
						mBinding.txtInput.setVisibility(View.GONE);
						SharedUtil.setSharedPreferencesData("replay_item", "0");
						SharedUtil.setSharedPreferencesData("replay_inside", "0");
					}
				}
			}
		});

		// 监听键盘状态
		mBinding.rlAllComment.setListener(new InputWindowListener() {
			@Override
			public void show() {
				String text = "";
				mBinding.etComment.setText(text);
				mBinding.etComment.setSelection(text.length());
			}

			@Override
			public void hidden() {
				mBinding.llInPut.postDelayed(new Runnable() {
					@Override
					public void run() {
						replyId = 0;
						mBinding.etComment.setText("");
					}
				}, 1);
			}
		});
	}

	public void submitReport(int id) {//提交举报内容
		String uid = MyApplication.getUserData().loginStatue ? MyApplication.getUserData().uid : "0";
		String arcurl = mSoftGame.getArcurl();
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
	public void obtainScore() {//获取游戏评分
		//获取数据
		long time = System.currentTimeMillis();
		String validate = ""+mSoftGame.getAid() + time;
		String sign = StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("aid", mSoftGame.getAid());
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		MGService.Api service = retrofit.create(MGService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());

		Call<ResponseBody> call = service.getScore(body);
		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				Log.e("requestSuccess", "%-----------------------" + response.body());
				try {
					String jsonString=response.body().string();
					JSONObject jsonObject = new JSONObject(jsonString);
					if (jsonObject.optInt("code") == 1) {
						JSONObject data = new JSONObject(jsonObject.optString("data"));
						header.findViewById(R.id.rl_comment_score).setVisibility(View.VISIBLE);
						((TextView)header.findViewById(R.id.tv_good)).setText(data.optString("goodpeople"));
						((TextView)header.findViewById(R.id.tv_bad)).setText(data.optString("badpeople"));
						float score= Float.parseFloat(data.optString("score"));
						CircleProgress cpv= (CircleProgress) header.findViewById(R.id.view_process);
						cpv.setAnimDuration(2000);
						cpv.setInterpolator(new AccelerateDecelerateInterpolator());
						cpv.setSweepValue(score*10);
						cpv.setValueText(score+"");
						cpv.anim();
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
				mBinding.mRecycleView.loadMoreError();
			}
		});
	}

	//点赞，踩 接口
	public void setPraise(final ImageView targetImg, final TextView targetView, final int firstNum, int id, final int type){//目标textview，初始数目，评论id，点赞类型:1赞2踩
		String temp = MyApplication.getUserData().loginStatue ? MyApplication.getUserData().uid : "0";
		int uid=Integer.parseInt(temp);
		long time=System.currentTimeMillis();
		String validate= ""+uid+id+type+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid", MyApplication.getUserData().uid);
			obj.put("id",id);
			obj.put("arcurl", mSoftGame.getArcurl());
			obj.put("type",type);
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
					JSONObject jsonObject=new JSONObject(response.body().string());
					if(jsonObject.optInt("code")==1){
						targetView.setText(firstNum+1+"");
						if(type==1) {
							targetImg.setImageResource(R.drawable.click_good_red);
							((CommentBean)mAdapter.getDataByPosition(firPo)).getReplies().get(secPo).setPraise(1);
							((CommentBean)mAdapter.getDataByPosition(firPo)).getReplies().get(secPo).setGoodcount(firstNum+1);
						}else
							targetImg.setImageResource(R.drawable.click_bad_red);
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

	public void submitScore(int type) {//提交游戏评分,类型:1好玩2不好玩
		//获取数据
		String str_uid = MyApplication.getUserData().loginStatue ? MyApplication.getUserData().uid : "0";
		int uid = Integer.parseInt(str_uid);
		long time = System.currentTimeMillis();
		String validate = ""+uid + mSoftGame.getAid() + type + time;
		String sign = StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid", uid);
			obj.put("aid", mSoftGame.getAid());
			obj.put("type", type);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		MGService.Api service = retrofit.create(MGService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());

		Call<ResponseBody> call = service.setScore(body);
		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				Log.e("requestSuccess", "%-----------------------" + response.body());
				try {
					String jsonString=response.body().string();
					JSONObject jsonObject = new JSONObject(jsonString);
					if (jsonObject.optInt("code") == 1) {
						JSONObject data = new JSONObject(jsonObject.optString("data"));
						header.findViewById(R.id.rl_comment_score).setVisibility(View.VISIBLE);
						((TextView)header.findViewById(R.id.tv_good)).setText(data.optString("goodpeople"));
						((TextView)header.findViewById(R.id.tv_bad)).setText(data.optString("badpeople"));
						float score= Float.parseFloat(data.optString("score"));
						CircleProgress cpv= (CircleProgress) header.findViewById(R.id.view_process);
						cpv.setAnimDuration(2000);
						cpv.setInterpolator(new AccelerateDecelerateInterpolator());
						cpv.setSweepValue(score*10);
						cpv.setValueText(score+"");
						cpv.anim();
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
				mBinding.mRecycleView.loadMoreError();
			}
		});
	}

	public void obtainComments() {//获取评论内容
		//获取数据
		String str_uid = MyApplication.getUserData().loginStatue ? MyApplication.getUserData().uid : "0";
		int uid = Integer.parseInt(str_uid);
		String arcurl = mSoftGame.getArcurl();
		long time = System.currentTimeMillis();
		String validate = uid + arcurl + c_sid + 10 + mPage + time;
		String sign = StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid", uid);
			obj.put("arcurl", arcurl);
			obj.put("c_sid", c_sid);
			obj.put("pagesize", 10);
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

		Call<NewsCommentRespBean> call = service.getCommentList(body);
		call.enqueue(new Callback<NewsCommentRespBean>() {
			@Override
			public void onResponse(Call<NewsCommentRespBean> call, Response<NewsCommentRespBean> response) {
				Log.e("requestSuccess", "%-----------------------" + response.body());
				initComments(response.body());
			}

			@Override
			public void onFailure(Call<NewsCommentRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
				mBinding.mRecycleView.loadMoreError();
			}
		});
	}
	
	// 初始化评论
	private void initComments(NewsCommentRespBean commentData) {
		mBinding.mRecycleView.refreshComplete();
		if ((commentData == null || commentData.getData().getList() == null|| commentData.getData().getList().size()==0)&&mAdapter.getItemCount()==0) {
			mBinding.noData.setVisibility(View.VISIBLE);
			return;
		}
		listData=commentData.getData().getList();
		mAdapter.appendList(listData);
		if (listData==null||listData.size()<1) {
			mBinding.mRecycleView.setNoMore(true);
		} else {
			mPage++;
		}
//		mAdapter.clearAndAddList(commentData.getData().getList());
//		mBinding.mRecycleView.setNoMore(true);
//		mBinding.mRecycleView.scrollToPosition(0);
	}

	@Override
	protected void onFragmentVisibleChange(boolean isVisible) {
		super.onFragmentVisibleChange(isVisible);
		if (isVisible && isFirst) {
			isFirst = false;
			obtainScore();
			obtainComments();
			getFavoriteStute();
		}
	}

	public void getFavoriteStute(){//获取收藏状态
		String temp = MyApplication.getUserData().loginStatue ? MyApplication.getUserData().uid : "0";
		int uid=Integer.parseInt(temp);
		String arcurl = mSoftGame.getArcurl();
		long time=System.currentTimeMillis();
		String validate= ""+uid+arcurl+f_sid+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid", MyApplication.getUserData().uid);
			obj.put("arcurl",arcurl);
			obj.put("f_sid",f_sid);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		UserService.Api service = retrofit.create(UserService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<ResponseBody> call = service.getArcFavorite(body);
		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				try {
					JSONObject jsonObject=new JSONObject(response.body().string());
					if(jsonObject.optInt("code")==1){
						JSONObject jsonSub=new JSONObject(jsonObject.opt("data").toString());
						f_sid=jsonSub.optInt("f_sid");
//					}else{
//						ToastUtil.showToast(mContext,jsonObject.opt("msg")+"");
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

	public void addDelFavorite(int isAdd){//1添加2删除
		String temp = MyApplication.getUserData().loginStatue ? MyApplication.getUserData().uid : "0";
		int uid=Integer.parseInt(temp);
		String arcurl = mSoftGame.getArcurl();
		int showtype=isAdd==2?0:4;
		long time=System.currentTimeMillis();
		String validate= ""+uid+arcurl+f_sid+showtype+isAdd+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid", MyApplication.getUserData().uid);
			obj.put("arcurl",arcurl);
			obj.put("f_sid",f_sid);
			obj.put("showtype",showtype);//展示类型:1新闻2攻略3游戏4手游5杂谈6评测7原创8安利9礼包(删除收藏时可传0)
			obj.put("act",isAdd);
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
					JSONObject jsonObject=new JSONObject(response.body().string());
					if(jsonObject.optInt("code")==1){
						JSONObject jsonSub=new JSONObject(jsonObject.opt("data").toString());
						f_sid=jsonSub.optInt("code");
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
	
	public void clickHandler(final View view) {
		switch (view.getId()) {
			case R.id.imgCollect:// 收藏
				if(MyApplication.getUserData().loginStatue) {
					if(MyApplication.getUserData().mobile.length()==0){
						Intent intent=new Intent(mContext, ForgetPassActivity.class);
						intent.putExtra("type",1);//1,绑定手机号，2修改密码，3忘记密码
						startActivity(intent);
					}else{
						addDelFavorite(3-favorite);
					}
				}else{
					ToastUtil.showToast(mContext,"请先登录！");
					startActivity(new Intent(mContext,LoginActivity.class));
				}
				break;
			case R.id.txtInput:
				SharedUtil.setSharedPreferencesData("replay_item", "0");
				SharedUtil.setSharedPreferencesData("replay_inside", "0");
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
				break;
			case R.id.txtSend:// 提交评论
				if(MyApplication.getUserData()==null||!MyApplication.getUserData().loginStatue){
					ToastUtil.showToast(mContext,"请先登录！");
					startActivity(new Intent(mContext, LoginActivity.class));
				}else if(MyApplication.getUserData().mobile.length()==0){//第三方登陆，非绑定跳转绑定界面
					Intent intent=new Intent(mContext, ForgetPassActivity.class);
					intent.putExtra("type",1);//1,绑定手机号，2修改密码，3忘记密码
					startActivity(intent);
				}else{
					strContent = mBinding.etComment.getText().toString().trim();
					if (strContent.length() == 0) {
						ToastUtil.showToast(mContext, "内容不能为空");
						return;
					}
					imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
					AddCommentReply();
				}
				break;
			default:
				Log.i("FragmentNewsWeb", "click");
				break;
		}
	}

	public void AddCommentReply() {//提交评论 replyId<0 添加文章评论  replyId>0 回复别人的评论
		String uid = MyApplication.getUserData().loginStatue ? MyApplication.getUserData().uid : "0";
		String arcurl = mSoftGame.getArcurl();
		String content = mBinding.etComment.getText().toString().trim();
		long time = System.currentTimeMillis();
		String validate;
		String sign;
		JSONObject obj = new JSONObject();
		if (replyId <= 0) {//添加评论
			validate = uid + arcurl + c_sid + time;
			sign = StringUtil.MD5(validate);
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
						String jsonString=response.body().string();
						JSONObject jsonObject = new JSONObject(jsonString);
						if (jsonObject.optInt("code") == 1) {
							JSONObject jsonSub=new JSONObject(jsonObject.opt("data").toString());
							String str_result=jsonSub.optString("integralmsg");
							if(StringUtil.isEmpty(str_result))
								ToastUtil.showToast(mContext,"提交评论成功!");
							else
								ToastUtil.showToast(mContext,str_result+"");
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
		} else {//回复别人的评论
			validate = uid + replyId + time;
			sign = StringUtil.MD5(validate);
			try {
				obj.put("uid", uid);
				obj.put("id", replyId);
				obj.put("arcurl", mSoftGame.getArcurl());
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
						String jsonString=response.body().string();
						JSONObject jsonObject = new JSONObject(jsonString);
						if (jsonObject.optInt("code") == 1) {
							JSONObject jsonSub=new JSONObject(jsonObject.opt("data").toString());
							String str_result=jsonSub.optString("integralmsg");
							if(StringUtil.isEmpty(str_result))
								ToastUtil.showToast(mContext,"提交评论成功!");
							else
								ToastUtil.showToast(mContext,str_result+"");
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
		}
	}
}