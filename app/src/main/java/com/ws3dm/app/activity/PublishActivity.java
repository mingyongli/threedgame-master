package com.ws3dm.app.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.bumptech.glide.Glide;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.BaseRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.adapter.SelectImageAdapter;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.bean.ForumGidBean;
import com.ws3dm.app.Constant;
import com.ws3dm.app.databinding.AcPublishBinding;
import com.ws3dm.app.mvp.model.RespBean.ForumFirstPostRespBean;
import com.ws3dm.app.mvp.model.RespBean.ForumTidTypeRespBean;
import com.ws3dm.app.mvp.presenter.ForumPresenter;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.ForumService;
import com.ws3dm.app.util.ProDialog;
import com.ws3dm.app.util.SharedUtil;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.util.UploadUtil;
import com.yu.imgpicker.ImagePicker;
import com.yu.imgpicker.ImagePickerConfig;
import com.yu.imgpicker.adapter.baseadapter.RecyclerAdapter;
import com.yu.imgpicker.core.ImageLoader;
import com.yu.imgpicker.core.SimpleSelectListener;
import com.yu.imgpicker.entity.ImageItem;
import com.yu.imgpicker.ui.PreviewDelActivity;
import com.yu.imgpicker.utils.LogUtils;
import com.yu.imgpicker.widget.DividerGridItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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
 * Describution :发布
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2017/9/4 16:32
 **/
public class PublishActivity extends BaseActivity {

	private AcPublishBinding mBinding;
	private BaseRecyclerAdapter<ForumGidBean> mCategoryRecyclerAdapter;
	private RecyclerView mCategoryRecyclerView;
	private PopupWindow mPopupWindowCate;

	private ForumPresenter mForumPresenter;
	private Handler mHandler;
	private ForumTidTypeRespBean bean;//用于获取分类标签
	private ForumFirstPostRespBean mForumFirstPostRespBean;//用户原作者修改跳转，获取的数据
	private ForumGidBean selectGid;
	private int fid, tid, pid, isModify;//isModify   0,不是 1，是
	private Dialog mDialog;
	private String typeid;
	private List<ImageItem> attachimgs = new ArrayList();
	private ArrayList<Integer> imgArray = new ArrayList();

	//选择图片
	private final int maxSize = 6;// 最多可选的图片数量
	private RecyclerAdapter adapter;

	@Override
	protected void init() {
		mBinding = bindView(R.layout.ac_publish);
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
						ToastUtil.showToast(mContext, "请求失败");
						break;
					case Constant.Notify.RESULT_FORUMTIDTYPE_LIST://处理返回结果
						bean = (ForumTidTypeRespBean) msg.obj;
						mCategoryRecyclerAdapter.clearAndAddList(bean.getData().getList());
						break;
//					case Constant.Notify.RESULT_GETFIRSTPOST://当前作者修改自己的主题帖子
//						mForumFirstPostRespBean = (ForumFirstPostRespBean) msg.obj;
//						break;
				}
			}
		};

		mForumPresenter = ForumPresenter.getInstance();
		mForumPresenter.setHandler(mHandler);

		initView();
		obtainFid();
	}

	public void initView() {
		pid = 0;//回帖编号(没有传0)
		mDialog = ProDialog.createLoadingDialog(mContext,true);
		mBinding.tvType.setText(getIntent().getStringExtra("title"));
		String tempMod;
		tempMod = getIntent().getStringExtra("fid");
		if (!StringUtil.isEmpty(tempMod)) {
			fid = Integer.parseInt(tempMod);
		}
		tempMod = getIntent().getStringExtra("tid");
		if (!StringUtil.isEmpty(tempMod)) {
			tid = Integer.parseInt(tempMod);
		}
		tempMod = getIntent().getStringExtra("isModify");
		if (tempMod != null && tempMod.equals("1")) {
			isModify = 1;
//			obtainFirstPost();
		}

		RecyclerView rv = mBinding.rvImg;
		rv.setHasFixedSize(true);
		rv.setLayoutManager(new GridLayoutManager(this, 4));
		rv.addItemDecoration(new DividerGridItemDecoration(this));
		adapter = new SelectImageAdapter(this, null, maxSize);
		rv.setAdapter(adapter);
		adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int position, Object item) {
				if (adapter.getItemViewType(position) == SelectImageAdapter.ITEM_TYPE_IMAGE) {
					// 点击已选图片 ---> 预览删除
					PreviewDelActivity.start(PublishActivity.this, adapter.getDataSet(), position);
				} else {
					// 点击加号 ---> 选择图片
					// 比如总共能选9张，第一次选了三张，第二次只能选6张，就用这个方法
//                    ImagePicker.getInstance().open(MainActivity.this, maxSize - adapter.getDataSet().size());
					ImagePicker.getInstance().open(PublishActivity.this);
				}
			}
		});

		ImagePickerConfig config = new ImagePickerConfig.Builder()
				.imageLoader(new ImageLoader() {    // 不限制图片框架，需要自己实现
					@Override
					public void displayImage(Context context, String path, ImageView imageView) {
						Glide.with(context).load(path).into(imageView);
					}
				})
				.showCamera(true)                   // 第一个item是否显示相机,默认true
				.limited(maxSize)                   // 最多能选的张数（单选填1）

//                .titleBarColor(Color.GRAY)        //titlebar的颜色和文字等自定义选项
//                .titleText("选择图片")
//                .titleTextColor(Color.parseColor("#ffffff"))
//                .btnResId(R.drawable.selector_back_press)
//                .btnTextColor(Color.parseColor("#ffffff"))
//                .backResId(R.mipmap.ic_launcher)

//                .needCrop(true)                   // 是否裁剪（只有单选时才有效）,如果裁剪就不会执行压缩
//                .cropSize(1, 1, 400, 400)         // 裁剪比例和大小
//                .compress(false)                  // 是否压缩,默认ture
//                .maxWidthAndHeight(720, 960)      // 压缩最大尺寸，默认720*960
//                .quality(80)                      // 压缩质量，默认80
				.callback(new SimpleSelectListener() {
					@Override
					public void onSelect(List<ImageItem> data) {
						handleImages(data);
					}
				})
				.build();

		ImagePicker.getInstance().setConfig(config);

		initPopupWindow();
	}

	public void obtainFid() {
		//获取数据  {"fid":"2331","time":"1522378864907","sign":"e3ee1ad68c23951cee4b4c1237e0bc62"}
		long time = System.currentTimeMillis();
		String validate = "" + 2341 + time;
		String sign = StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("fid", 2341);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		mForumPresenter.getForumTidType(obj.toString());//异步请求
	}

//	//加载一楼的信息
//	public void obtainFirstPost() {
//		long time = System.currentTimeMillis();
//		String validate = MyApplication.getUserData().uid + fid + tid + time;
//		String sign = StringUtil.MD5(validate);
//		JSONObject object = new JSONObject();
//		try {
//			object.put("uid", MyApplication.getUserData().uid);
//			object.put("fid", fid);
//			object.put("tid", tid);
//			object.put("time", time);
//			object.put("sign", sign);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		mForumPresenter.getForumFirstPost(object.toString());//异步请求
//	}

	private void handleImages(List<ImageItem> data) {//获取到的图片合集
		LogUtils.e("data.size = " + data.size());
		adapter.addItems(data);
		attachimgs = data;
	}

	public void clickHandler(View view) {
		switch (view.getId()) {
			case R.id.imgReturn:
				onBackPressed();
				break;
			case R.id.tv_label:
				int[] location = new int[2];
				view.getLocationInWindow(location);
				mPopupWindowCate.showAtLocation(view, Gravity.BOTTOM, location[0], location[1] + view.getHeight());
				break;
			case R.id.txtSubmit:
				if (StringUtil.isEmpty(mBinding.tvLabel.getText().toString()) ||
						StringUtil.isEmpty(mBinding.etTitle.getText().toString()) ||
						StringUtil.isEmpty(mBinding.etContent.getText().toString())) {
					ToastUtil.showToast(mContext, "内容不能为空");
				}else if(mBinding.etContent.getText().toString().length()<10){
					ToastUtil.showToast(mContext, "内容不能小于10个字符！");
				} else {
					mDialog.show();
					if(attachimgs.size()==0){
					    submitData();
					}else{
						new Thread(new Runnable() {
							@Override
							public void run() {
								UpLoadImages();
							}
						}).start();
					}
				}
				break;
			default:
				break;
		}
	}

	public void UpLoadImages() {
		String url="https://my.3dmgame.com/app/uploadbbs";
		try {
			int uid=Integer.parseInt(MyApplication.getUserData().uid);
			long time = System.currentTimeMillis();
			String validate = ""+MyApplication.getUserData().uid + fid + tid + pid + time;
			String sign = StringUtil.MD5(validate);
			Map<String, String> strParams = new HashMap<String,String>();
			strParams.put("uid",uid+"");
			strParams.put("fid",fid+"");
			strParams.put("tid",tid+"");
			strParams.put("pid",pid+"");
			strParams.put("time",time+"");
			strParams.put("sign",sign+"");

			Map<String, File> fileParams = new HashMap<String,File>();
			int forSize=attachimgs.size();
			for(int i=0;i<forSize;i++){
				String fileName=attachimgs.get(i).name;
				File file = new File(attachimgs.get(i).path);
				fileParams.put(fileName,file);
//				File file2 = new File(attachimgs.get(0).path);
				String result=UploadUtil.uploadFile(strParams, file,url);
				JSONObject jsonObject=new JSONObject(result);
				if(jsonObject.optInt("code")==1){
					JSONObject subJson=new JSONObject(jsonObject.optString("data"));
					imgArray.add(subJson.optInt("attachid"));//attachurl 图片地址
					if(imgArray.size()==attachimgs.size())
						submitData();
				}
			}
			int sdgk=0;
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void submitData() {
		try {
			JSONArray jsonArray = new JSONArray(); //保存数组数据的JSONArray对象  
			int forSize=imgArray.size();
			for(int i = 0 ; i <forSize;i++){  //依次将数组元素添加进JSONArray对象中
				jsonArray.put(imgArray.get(i));
//          两种添加方式一样			
//			try {
//				jsonArray.put(i, dou[i]);
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			}


			long time = System.currentTimeMillis();
			String validate = MyApplication.getUserData().uid + fid + typeid + mBinding.etTitle.getText().toString() + time;
			String sign = StringUtil.MD5(validate);
			JSONObject obj = new JSONObject();
			obj.put("uid", MyApplication.getUserData().uid);
			obj.put("fid", fid);
			obj.put("typeid", typeid);
			obj.put("title", mBinding.etTitle.getText().toString());
			obj.put("content", mBinding.etContent.getText().toString());
			obj.put("attachimgs", jsonArray);
			obj.put("time", time);
			obj.put("sign", sign);
			//同步请求
			Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
			ForumService.Api service = retrofit.create(ForumService.Api.class);
			RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
			Call<ResponseBody> call = service.submitNewThread(body);
			call.enqueue(new Callback<ResponseBody>() {
				@Override
				public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
					mDialog.dismiss();
					try {
						String jsonString=response.body().string();
						JSONObject jsonObject = new JSONObject(jsonString);
						if (jsonObject.optInt("code") == 1) {
							ToastUtil.showToast(mContext,"发布成功！");
							SharedUtil.setSharedPreferencesData("isPublish","1");
							finish();
							//把当日提交的帖子保存到本地，方便修改查找
							
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
					mDialog.dismiss();
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void initPopupWindow() {

		mCategoryRecyclerAdapter = new BaseRecyclerAdapter<ForumGidBean>(mContext, R.layout.item_pop_cate_string) {
			@Override
			public void bindData(RecyclerViewHolder holder, int position, ForumGidBean item) {
				holder.setText(R.id.txt_view_cate, item.getName());
			}
		};

		mCategoryRecyclerAdapter.setOnClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int position) {

				selectGid = bean.getData().getList().get(position);
				typeid=selectGid.getTypeid();
				mBinding.tvLabel.setText(selectGid.getName());
				mPopupWindowCate.dismiss();
			}
		});


		View contentViewCate = LayoutInflater.from(mContext).inflate(R.layout.pop_list_cate, null);
		mCategoryRecyclerView = (RecyclerView) contentViewCate.findViewById(R.id.recycler_view_cate);
		mCategoryRecyclerView.setAdapter(mCategoryRecyclerAdapter);
		contentViewCate.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mPopupWindowCate.dismiss();
			}
		});

		LinearLayoutManager linearManager = new LinearLayoutManager(this);
		linearManager.setOrientation(LinearLayoutManager.VERTICAL);
		mCategoryRecyclerView.setLayoutManager(linearManager);

		mPopupWindowCate = new PopupWindow(contentViewCate, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
		mPopupWindowCate.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindowCate.setAnimationStyle(R.style.AnimBottom);
		mPopupWindowCate.setTouchable(true);
		mPopupWindowCate.setOutsideTouchable(true);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** 预览(可删除)后获取结果刷新已选择的图片 */
		if (requestCode == PreviewDelActivity.REQUEST_PREVIEW_CODE) {
			if (resultCode == RESULT_OK) {
				List<ImageItem> imageItems = (List<ImageItem>) data.getSerializableExtra(PreviewDelActivity.KEY_PREVIEW_DEL_DATA);
				adapter.refreshData(imageItems);
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ImagePicker.getInstance().clear();
		if(mDialog!=null)
			mDialog.dismiss();
	}

	@Override
	public void onResume() {
		super.onResume();
		if(mForumPresenter!=null){
			mForumPresenter.setHandler(mHandler);
		}
	}
}