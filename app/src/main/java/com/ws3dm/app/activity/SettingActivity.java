package com.ws3dm.app.activity;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.R;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.databinding.SettingActivityBinding;
import com.ws3dm.app.util.DataCleanManager;
import com.ws3dm.app.util.SharedUtil;
import com.ws3dm.app.util.ToastUtil;

import java.util.Map;

import ch.ielse.view.SwitchView;

public class SettingActivity extends BaseActivity {
    private SettingActivityBinding mBinding;

    @Override
    protected void init() {
        mBinding = bindView(R.layout.setting_activity);
        mBinding.setHandler(this);
        initView();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back://返回
                finish();
                break;
            case R.id.account_security://账号安全
                if (MyApplication.getUserData() == null || !MyApplication.getUserData().loginStatue) {
                    startActivity(new Intent(this, LoginActivity.class));
                } else if (MyApplication.getUserData().mobile.length() == 0) {//第三方登陆，非绑定跳转绑定界面
                    Intent intent = new Intent(mContext, ForgetPassActivity.class);
                    intent.putExtra("type", 1);//1,绑定手机号，2修改密码，3忘记密码
                    startActivity(intent);
                } else {
                    startActivity(new Intent(this, SecurityActivity.class));
                }
                break;
            case R.id.message_push://消息推送
                startActivity(new Intent(this, MessageColActivity.class));
                break;
            case R.id.rl_version://版本信息
                startActivity(new Intent(this, VersionActivity.class));
                break;
            case R.id.rl_adver://发送邮件
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, "3dm@3dmgame.com");
                intent.putExtra(Intent.EXTRA_SUBJECT, "你好".toCharArray());
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;
            case R.id.rl_suggest://问题反馈
                if (MyApplication.getUserData() == null || !MyApplication.getUserData().loginStatue) {
                    startActivity(new Intent(this, LoginActivity.class));
                } else {
                    startActivity(new Intent(this, OptionActivity.class));
                }
                break;
            case R.id.tv_logout://退出登录
                if (MyApplication.getUserData() == null || !MyApplication.getUserData().loginStatue) {
                    startActivity(new Intent(mContext, LoginActivity.class));
                } else {
                    setLogOut();
                }
                finish();
                break;
            case R.id.rl_agree://用户协议与隐私政策
                Intent aggreement = new Intent(mContext, SingleWebActivity.class);
                aggreement.putExtra("title", "用户协议与隐私政策");
                aggreement.putExtra("url", SingleWebActivity.mUrl_AgreeMent);
                startActivity(aggreement);
                break;
            case R.id.rl_ache://清理缓存
                DataCleanManager.cleanInternalCache(mContext);
                try {
                    String cache = DataCleanManager.getCacheSize(mContext.getCacheDir(), mContext.getExternalCacheDir());
                    mBinding.tvAche.setText(cache);
                } catch (Exception e) {
                    mBinding.tvAche.setText("0M");
                    e.printStackTrace();
                }
                ToastUtil.showToast(mContext, "清理成功！");
                break;
        }
    }

    private void initView() {
        if (MyApplication.getUserData() == null || !MyApplication.getUserData().loginStatue) {
            mBinding.exitLogin.setText("登录");
        } else {
            mBinding.exitLogin.setText("退出登录");
        }
        if (SharedUtil.getSharedPreferencesData("NoWifiPic").equals("0"))
            mBinding.svWifiPic.toggleSwitch(false);
        else
            mBinding.svWifiPic.toggleSwitch(true);

        if (SharedUtil.getSharedPreferencesData("DelApk").equals("0"))
            mBinding.svDelApk.toggleSwitch(false);
        else
            mBinding.svDelApk.toggleSwitch(true);

        if (SharedUtil.getSharedPreferencesData("FontSize") == null || SharedUtil.getSharedPreferencesData("FontSize").equals("0"))
            mBinding.tvNormal.setChecked(true);
        else if (SharedUtil.getSharedPreferencesData("FontSize").equals("1"))
            mBinding.tvSmall.setChecked(true);
        else
            mBinding.tvBig.setChecked(true);


        try {
            String cache = DataCleanManager.getCacheSize(mContext.getCacheDir(), mContext.getExternalCacheDir());
            mBinding.tvAche.setText(cache);
        } catch (Exception e) {
            mBinding.tvAche.setText("0M");
        }

        mBinding.rgFontsize.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.tv_small:
                        SharedUtil.setSharedPreferencesData("FontSize", "1");
                        break;
                    case R.id.tv_normal:
                        SharedUtil.setSharedPreferencesData("FontSize", "0");
                        break;
                    case R.id.tv_big:
                        SharedUtil.setSharedPreferencesData("FontSize", "2");
                        break;
                }
            }
        });

        mBinding.svDelApk.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                view.toggleSwitch(true);
                SharedUtil.setSharedPreferencesData("DelApk", "1");
            }

            @Override
            public void toggleToOff(SwitchView view) {
                view.toggleSwitch(false);
                SharedUtil.setSharedPreferencesData("DelApk", "0");
            }
        });

        mBinding.svWifiPic.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                view.toggleSwitch(true);
                SharedUtil.setSharedPreferencesData("NoWifiPic", "1");
            }

            @Override
            public void toggleToOff(SwitchView view) {
                view.toggleSwitch(false);
                SharedUtil.setSharedPreferencesData("NoWifiPic", "0");
            }
        });
    }

    // 账号退出
    private void setLogOut() {
        if (MyApplication.getUserData().isThirdPartLogin) {
            switch (MyApplication.getUserData().openType) {//1qq2微博3微信(绑定类型)
                case 1:
                    UMShareAPI.get(mContext).deleteOauth(this, SHARE_MEDIA.QQ, umDelListener);
                    break;
                case 2:
                    UMShareAPI.get(mContext).deleteOauth(this, SHARE_MEDIA.SINA, umDelListener);
                    break;
                case 3:
                    UMShareAPI.get(mContext).deleteOauth(this, SHARE_MEDIA.WEIXIN, umDelListener);
                    break;
                default:
                    break;
            }
        }
        UserDataBean userDataBean = new UserDataBean();
        MyApplication.writeUserData(userDataBean);
        ToastUtil.showToast(this, "退出成功");
    }

    private UMAuthListener umDelListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            Log.i("umeng_login_onStart", "删除授权开始");
        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            Log.i("umeng_del_onComplete", "删除授权结束");
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            Log.i("umeng_del_onError", "删除授权失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            Log.i("umeng_del_userinfo", "删除授权取消");
        }
    };
}
