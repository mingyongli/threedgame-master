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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
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
import com.ws3dm.app.bean.ForumDetailBean;
import com.ws3dm.app.bean.RepliesBean;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.databinding.FgForumCommentBinding;
import com.ws3dm.app.emoj.EmotionUtils;
import com.ws3dm.app.mvp.model.RespBean.ForumTidPostRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.ForumService;
import com.ws3dm.app.network.service.UserService;
import com.ws3dm.app.util.SharedUtil;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.util.glide.GlideUtil;
import com.ws3dm.app.view.InputWindowListener;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
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
 * Describution :新闻页详情下部的评论
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2017/8/18 17:22
 **/
public class FragmentForumComment extends BaseFragment {
	private FgForumCommentBinding mBinding;

	private int mPage;
	private int replyId = 0;
	public static String strType = ""; // 判断加载此Fragment的页面
	private String strContent,type,arcUrl;
	private boolean isFirst;
	private InputMethodManager imm;
	private CommonRecyclerAdapter mAdapter;
	public static EditText etComment;
	private List<CommentBean> listData = new ArrayList<>();
	private List<RepliesBean> listPost = new ArrayList<>();
	private int firPo,secPo;
	private int c_sid;//当前文章在评论里唯一标识(默认为0)
	private EmotionUtils emoutil;
	private ForumDetailBean mForumDetailBean;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		AppCompatActivity activity = (AppCompatActivity) getActivity();

		mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_forum_comment, container, false);
		mBinding.setHandler(this);

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
		emoutil=new EmotionUtils(getActivity());
		type = getArguments().getString("type");
		mForumDetailBean = (ForumDetailBean) getArguments().getSerializable("forumDetailBean");
		arcUrl=mForumDetailBean.getArcurl();
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

		mBinding.mRecycleView.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		mBinding.mRecycleView.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
		mBinding.mRecycleView.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);
		mBinding.mRecycleView.setPullRefreshEnabled(true);
		mBinding.mRecycleView.setLoadingMoreEnabled(true);

		mAdapter = new CommonRecyclerAdapter<RepliesBean>(mContext, R.layout.adapter_comments) {
			@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
			@Override
			public void bindData(RecyclerViewHolder holder, final int position, final RepliesBean comments) {
				if(position==0){
					holder.getView(R.id.llAllComments).setVisibility(View.VISIBLE);
					holder.setText(R.id.tv_comment_title,comments.getType());
				}else {
					holder.getView(R.id.llAllComments).setVisibility(View.GONE);
				}
				ImageView img_good=holder.getView(R.id.imgGood);
				img_good.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
//							ToastUtil.showToast(mContext,"点赞"+position);
					}
				});
//					holder.setImageByUrl(R.id.img_head,comments.getUser().avatarstr);
//					holder.setText(R.id.tv_name,comments.getUser().nickname);
//					holder.setText(R.id.tv_locate,"第"+comments.getPosition()+"楼  "+comments.getUser().regionstr+" 网友");
				GlideUtil.loadCircleImage(mContext,comments.getUser().avatarstr,(ImageView) holder.getView(R.id.img_head));
				holder.setText(R.id.tv_name,comments.getUser().nickname);
//					holder.setText(R.id.tv_locate,comments.getUser().regionstr.length()>0?comments.getUser().regionstr+" 网友":"");
				holder.setText(R.id.tv_posi2,"第"+comments.getPosition()+"楼");
				holder.getView(R.id.tv_head_title).setVisibility(View.GONE);
				TextView tv_head_title=holder.getView(R.id.tv_head_title);
				tv_head_title.setTextColor(getResources().getColor(R.color.lightBlack));

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

				TextView content=holder.getView(R.id.tv_content);
				content.setText(emoutil.addSmileySpans(emoutil.addSmileySpans(comments.getContent())));
//					holder.setText(R.id.tv_content,comments.getContent());
				holder.setText(R.id.tv_time,TimeUtil.getFoolishTime(comments.getPubdate_at()));
				holder.getView(R.id.imgGood).setVisibility(View.GONE);
//					holder.getView(R.id.imgBad).setVisibility(View.GONE);
				holder.getView(R.id.tv_report).setVisibility(View.GONE);
				TextView tv_reply=holder.getView(R.id.tv_reply);
				tv_reply.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if(MyApplication.getUserData().loginStatue) {
							if(MyApplication.getUserData().mobile.length()==0){
								Intent intent=new Intent(mContext, ForgetPassActivity.class);
								intent.putExtra("type",1);//1,绑定手机号，2修改密码，3忘记密码
								startActivity(intent);
							}else{
								replyId=comments.getPid();
								// 获取编辑框焦点
								mBinding.etComment.setFocusable(true);
								//打开软键盘
								final InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
								mBinding.etComment.requestFocus();
							}
						}else{
							ToastUtil.showToast(mContext,"请先登录！");
							startActivity(new Intent(mContext,LoginActivity.class));
						}
					}
				});

				if(comments.getReplies()!=null&&comments.getReplies().size()>0){
					XRecyclerView recyclerView = holder.getView(R.id.mRecycleView);
					LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
					layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
					recyclerView.setLayoutManager(layoutManager);
					recyclerView.setNestedScrollingEnabled(false);
					recyclerView.setLoadingMoreEnabled(false);
					recyclerView.setPullRefreshEnabled(false);

					BaseRecyclerAdapter<RepliesBean> mSubRecyclerAdapter= new BaseRecyclerAdapter<RepliesBean>(mContext, R.layout.adapter_child_comments) {
						@Override
						public void bindData(RecyclerViewHolder holder, final int position, RepliesBean bean) {
//							    holder.setText(R.id.tv_name,bean.getUser().nickname);
//								holder.setText(R.id.tv_content,""+bean.getContent());//emoutil.addSmileySpans(comments.getContent())
							TextView content=holder.getView(R.id.tv_content);
							String str="<font color='#00a0e9'>"+bean.getUser().nickname+"</font>"+" : "+bean.getContent();
							content.setText(emoutil.addSmileySpans(Html.fromHtml(str)));
//								holder.getView(R.id.tv_head_title).setVisibility(View.GONE);
							holder.getView(R.id.imgGood).setVisibility(View.GONE);
//								holder.getView(R.id.imgBad).setVisibility(View.GONE);

							TextView txtComment=holder.getView(R.id.tv_reply);
							txtComment.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View view) {
									if(MyApplication.getUserData().loginStatue) {
										if(MyApplication.getUserData().mobile.length()==0){
											Intent intent=new Intent(mContext, ForgetPassActivity.class);
											intent.putExtra("type",1);//1,绑定手机号，2修改密码，3忘记密码
											startActivity(intent);
										}else{
											replyId=comments.getPid();
											// 获取编辑框焦点
											mBinding.etComment.setFocusable(true);
											//打开软键盘
											final InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
											imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
											mBinding.etComment.requestFocus();
										}
									}else{
										ToastUtil.showToast(mContext,"请先登录！");
										startActivity(new Intent(mContext,LoginActivity.class));
									}
								}
							});

						}
					};
					recyclerView.setAdapter(mSubRecyclerAdapter);
					mSubRecyclerAdapter.clearAndAddList(comments.getReplies());
				}else{
					holder.getView(R.id.mRecycleView).setVisibility(View.GONE);
				}
			}
		};
		
		mBinding.mRecycleView.setAdapter(mAdapter);
		mBinding.mRecycleView.setLoadingListener(new XRecyclerView.LoadingListener() {
			@Override
			public void onRefresh() {
				new Handler().postDelayed(new Runnable() {
					public void run() {
						mPage=1;
						obtainComments();
					}
				}, 50);            //refresh data here
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
			obj.put("arcurl", arcUrl);
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
	
	public void obtainComments() {//获取评论内容
		//获取数据
		String uid = MyApplication.getUserData().loginStatue ? MyApplication.getUserData().uid : "0";
		String fid = mForumDetailBean.getFid();
		String tid = mForumDetailBean.getTid();
		int isauthor = 0;//是否只看楼主(1是0否)
		long time = System.currentTimeMillis();
		String validate = uid + fid + tid + 10 + mPage + isauthor + time;
		String sign = StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid", uid);
			obj.put("fid", fid);
			obj.put("tid", tid);
			obj.put("pagesize", 10);
			obj.put("page", mPage);
			obj.put("isauthor", isauthor);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
//		mForumPresenter.getForumTidPost(obj.toString());//异步请求
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		ForumService.Api service = retrofit.create(ForumService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());

		Call<ForumTidPostRespBean> call = service.getTidPost(body);
		call.enqueue(new Callback<ForumTidPostRespBean>() {
			@Override
			public void onResponse(Call<ForumTidPostRespBean> call, Response<ForumTidPostRespBean> response) {
				Log.e("requestSuccess", "%-----------------------" + response.body());
				if (response.body() != null && response.body().getData() != null)
					initReplyPost(response.body());
			}

			@Override
			public void onFailure(Call<ForumTidPostRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
			}
		});
	}

	// 初始化评论
	private void initReplyPost(ForumTidPostRespBean postData) {
		mBinding.mRecycleView.refreshComplete();
		if (postData == null || postData.getData().getList() == null||postData.getData().getList().size()==0) {
			if(mPage==1)
				mBinding.txtNoComment.setVisibility(View.VISIBLE);
			else
				mBinding.mRecycleView.setNoMore(true);
			return;
		}
		mBinding.txtNoComment.setVisibility(View.GONE);
		listPost=postData.getData().getList();
		if(mPage>1) {
			mAdapter.appendList(listPost);
		}else {
			postData.getData().getList().get(0).setType("所有回帖");
			mAdapter.clearAndAddList(listPost);
		}
		if (listData==null||listPost.size()<1) {
			mBinding.mRecycleView.setNoMore(true);
		} else {
			mPage++;
		}
		super.onResume();
	}

	@Override
	protected void onFragmentVisibleChange(boolean isVisible) {
		super.onFragmentVisibleChange(isVisible);
//		if (!strType.equals("CollectActivity"))
		if (isVisible && isFirst) {
			isFirst = false;
			obtainComments();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mAdapter != null)
			mAdapter.notifyDataSetChanged();
	}

	public void clickHandler(final View view) {
		switch (view.getId()) {
			case R.id.txtInput:
				SharedUtil.setSharedPreferencesData("replay_item", "0");
				SharedUtil.setSharedPreferencesData("replay_inside", "0");
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
				break;
			case R.id.txtSend:// 提交评论
				if(MyApplication.getUserData().loginStatue) {
					if(MyApplication.getUserData().mobile.length()==0){
						Intent intent=new Intent(mContext, ForgetPassActivity.class);
						intent.putExtra("type",1);//1,绑定手机号，2修改密码，3忘记密码
						startActivity(intent);
						return;
					}else{
						strContent = mBinding.etComment.getText().toString().trim();
						if (strContent.length() == 0) {
							ToastUtil.showToast(mContext, "内容不能为空");
							return;
						}
						imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
						AddCommentReply();
					}
				}else{
					ToastUtil.showToast(mContext,"请先登录！");
					startActivity(new Intent(mContext,LoginActivity.class));
					return;
				}
				break;
			default:
				Log.i("FragmentNewsWeb", "click");
				break;
		}
	}

	public void AddCommentReply() {//提交评论 replyId<0 添加文章评论  replyId>0 回复别人的评论
		String uid=MyApplication.getUserData().loginStatue?MyApplication.getUserData().uid:"0";
		String fid=mForumDetailBean.getFid();
		String tid=mForumDetailBean.getTid();
		String reppid=replyId+"";
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
	}
}