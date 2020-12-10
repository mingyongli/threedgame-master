package com.ws3dm.app.activity;

import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.R;
import com.ws3dm.app.databinding.AcOptionBinding;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Describution :意见反馈
 * 
 * Author : DKjuan
 * 
 * Date : 2017/9/4 16:32
 **/
public class OptionActivity extends BaseActivity{
	private AcOptionBinding mBinding;
	private int index =1;
	@Override
	protected void init() {
		mBinding=bindView(R.layout.ac_option);
		mBinding.setHandler(this);

	}


	public void clickHandler(View view) {
		switch (view.getId()) {
			case R.id.txtSubmit:
				if (StringUtil.isEmpty(mBinding.etContent.getText().toString().trim())
					||StringUtil.isEmpty(mBinding.etTitle.getText().toString().trim()))
					ToastUtil.showToast(mContext,"标题或内容不能为空");
				else {
					feedback();
				}
				break;
			case R.id.imgReturn:
				finish();
				break;
			case R.id.jianyi:
				index = 1;
				mBinding.jianyiTx.setTextColor(Color.parseColor("#FF4500"));
				mBinding.cuowuTx.setTextColor(Color.parseColor("#333333"));
				mBinding.qitaTx.setTextColor(Color.parseColor("#333333"));
				mBinding.jianyiV.setVisibility(View.VISIBLE);
				mBinding.cuowuV.setVisibility(View.GONE);
				mBinding.qitaV.setVisibility(View.GONE);
				break;
			case R.id.error:
				index = 2;
				mBinding.jianyiTx.setTextColor(Color.parseColor("#333333"));
				mBinding.cuowuTx.setTextColor(Color.parseColor("#FF4500"));
				mBinding.qitaTx.setTextColor(Color.parseColor("#333333"));
				mBinding.jianyiV.setVisibility(View.GONE);
				mBinding.cuowuV.setVisibility(View.VISIBLE);
				mBinding.qitaV.setVisibility(View.GONE);
				break;
			case R.id.qita:
				index = 3;
				mBinding.jianyiTx.setTextColor(Color.parseColor("#333333"));
				mBinding.cuowuTx.setTextColor(Color.parseColor("#333333"));
				mBinding.qitaTx.setTextColor(Color.parseColor("#FF4500"));
				mBinding.jianyiV.setVisibility(View.GONE);
				mBinding.cuowuV.setVisibility(View.GONE);
				mBinding.qitaV.setVisibility(View.VISIBLE);
				break;
		}
	}

	private void feedback(){
		UserDataBean userData = MyApplication.getUserData();
		long time = System.currentTimeMillis();
		String sign = StringUtil.newMD5(userData.uid + index + time + NewUrl.KEY);
		Map<String, Object> values = new HashMap<>();
		values.put("uid",userData.uid);
		values.put("sign",sign);
		values.put("time",time);
		values.put("type",index);
		values.put("title",mBinding.etTitle.getText().toString().trim());
		values.put("content",mBinding.etContent.getText().toString().trim());
		values.put("contact",mBinding.phone.getText().toString().trim());
		String json = new JSONObject(values).toString();
		OkHttpUtils
				.postString()
				.url(NewUrl.SUBMIT_FEED)
				.content(json)
				.build()
				.execute(new Callback<String>() {

					@Override
					public String parseNetworkResponse(Response response) throws IOException {
						return response.body().string();
					}

					@Override
					public void onError(Request request, Exception e) {
						Log.e("message",e.getMessage());
					}

					@Override
					public void onResponse(String response) {
						Log.e("message",response);
						ToastUtil.showToast(OptionActivity.this,"提交成功");
						OptionActivity.this.finish();
					}

				});

	}


}