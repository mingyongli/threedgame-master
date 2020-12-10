package com.ws3dm.app.activity;

import android.content.Intent;
import android.view.View;
import com.umeng.message.IUmengCallback;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.R;
import com.ws3dm.app.databinding.MessageColLayoutBinding;
import com.ws3dm.app.util.SharedUtil;
import ch.ielse.view.SwitchView;


public class MessageColActivity extends BaseActivity{
    private MessageColLayoutBinding mBinding;
    @Override
    protected void init() {
        mBinding = bindView(R.layout.message_col_layout);
        mBinding.setHandler(this);
        initView();
    }

    private void initView() {
        if (SharedUtil.getSharedPreferencesData("PushAgent").equals("0"))
            mBinding.mPushAgent.toggleSwitch(false);
        else
            mBinding.mPushAgent.toggleSwitch(true);

        mBinding.mPushAgent.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(final SwitchView view) {
                MyApplication.mPushAgent.enable(new IUmengCallback() {
                    @Override
                    public void onSuccess() {
                        view.toggleSwitch(true);
                        SharedUtil.setSharedPreferencesData("PushAgent", "1");
                    }
                    @Override
                    public void onFailure(String s, String s1) {
                    }
                });
            }

            @Override
            public void toggleToOff(final SwitchView view) {
                MyApplication.mPushAgent.disable(new IUmengCallback() {
                    @Override
                    public void onSuccess() {
                        view.toggleSwitch(false);
                        SharedUtil.setSharedPreferencesData("PushAgent", "0");
                    }
                    @Override
                    public void onFailure(String s, String s1) {
                    }
                });
            }
        });




    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back://返回
                finish();
                break;
            case R.id.message_push://返回
                startActivity(new Intent(this, PushRecommendActivity.class));
                break;
        }
    }

}
