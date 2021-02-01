package com.ws3dm.app.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.dou361.dialogui.holder.AlertDialogHolder;
import com.ws3dm.app.Constant;
import com.ws3dm.app.R;
import com.ws3dm.app.databinding.AcInfoBinding;
import com.ws3dm.app.databinding.AcVersionBinding;
import com.ws3dm.app.mvp.model.RespBean.VersionRespBean;
import com.ws3dm.app.mvp.presenter.UserPresenter;
import com.ws3dm.app.util.AppUtil;
import com.ws3dm.app.util.DialogHelper;
import com.ws3dm.app.util.SharedUtil;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.util.callback.CallBack;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Describution : 权限说明
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2019/11/11 14:10
 **/
public class InfoActivity extends BaseActivity {

    private AcInfoBinding mBinding;
    private static final int RESULTS_CODE = 11;
    private static final int RESULTS_CODE_FALSE = 12;

    @Override
    protected void init() {
        mBinding = bindView(R.layout.ac_info);
        mBinding.setHandler(this);

        initView();
    }

    public void initView() {
//		tv_content.setText(Html.fromHtml(tv_content.getText().toString().trim()));
        //设置部分文字点击事件
        SpannableString spannableString = new SpannableString(getString(R.string.info_aggree));
        MyClickableSpan clickableSpan = new MyClickableSpan(getString(R.string.info_aggree));
        spannableString.setSpan(clickableSpan, 304, 322, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mBinding.tvContent.setMovementMethod(LinkMovementMethod.getInstance());
        mBinding.tvContent.setText(spannableString);

        //设置部分文字颜色
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#d02b29"));
        spannableString.setSpan(foregroundColorSpan, 304, 322, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mBinding.tvContent.setMovementMethod(LinkMovementMethod.getInstance());
        mBinding.tvContent.setText(spannableString);
    }

    class MyClickableSpan extends ClickableSpan {
        private String content;

        public MyClickableSpan(String content) {
            this.content = content;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            Intent aggreement = new Intent(mContext, SingleWebActivity.class);
            aggreement.putExtra("title", "用户协议与隐私政策");
            aggreement.putExtra("url", SingleWebActivity.mUrl_AgreeMent);
            startActivity(aggreement);
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void clickHandler(View view) {
        switch (view.getId()) {
            case R.id.tv_no:
            case R.id.imgReturn:
                SharedUtil.setSharedPreferencesData("hasReadInfo", "no");
//                new AlertDialog.Builder(mContext).setTitle("提示")
//                        .setMessage("请先确认并勾选隐私协议")
//                        .setNeutralButton("确认", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        }).show();
                new DialogHelper(mContext, new CallBack() {
                    @Override
                    public void callBackFunction() {

                    }
                }).showDialog("提示","请先确认并勾选隐私协议",true);
                break;
            case R.id.tv_yes:
                SharedUtil.setSharedPreferencesData("hasReadInfo", "yes");
                setResult(RESULTS_CODE);
                finish();
                break;
        }
    }
}