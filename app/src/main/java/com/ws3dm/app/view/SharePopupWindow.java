package com.ws3dm.app.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.ws3dm.app.R;
import com.ws3dm.app.util.AppUtil;
import com.ws3dm.app.util.ToastUtil;

/**
 * 分享侧边栏页
 */
public class SharePopupWindow implements OnClickListener {

    private View v;
    private UMWeb web;
    private PopupWindow pop;
    private Context context;
    private LinearLayout llShow;
    private RelativeLayout loadViewShare;
    private Animation mShowAnimation, mHideAnimation;
    private String url = "";

    public SharePopupWindow(Context context, String strUrl, String strTitle, String strContent, RelativeLayout loadViewShare) {
        this.context = context;
        this.url = strUrl;
        this.loadViewShare = loadViewShare;
        web = new UMWeb(strUrl);
        web.setTitle(strTitle);
        if (strContent != null)
            if (strContent.isEmpty())
                web.setDescription("真的不错呦！");
            else
                web.setDescription(strContent);
        else
            web.setDescription("真的不错呦！");
        web.setThumb(new UMImage(context, R.drawable.share_logo));
    }

    public SharePopupWindow(Context context, String strUrl, String strTitle, String strContent, RelativeLayout loadViewShare, String imgUrl) {
        this.context = context;
        this.url = strUrl;
        this.loadViewShare = loadViewShare;
        web = new UMWeb(strUrl);
        web.setTitle(strTitle);
        if (strContent != null)
            if (strContent.isEmpty())
                web.setDescription("真的不错呦！");
            else
                web.setDescription(strContent);
        else
            web.setDescription("真的不错呦！");
        web.setThumb(new UMImage(context, imgUrl));
    }

    // 分享回调
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            loadViewShare.setVisibility(View.GONE);
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            dismiss();
            loadViewShare.setVisibility(View.GONE);
            ToastUtil.showToast(context, "分享成功");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            dismiss();
            loadViewShare.setVisibility(View.GONE);
            ToastUtil.showToast(context, "没有安装此应用");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            dismiss();
            loadViewShare.setVisibility(View.GONE);
        }
    };

    public void initPopupWindow() {
        DisplayMetrics metrics = new DisplayMetrics();
        Rect frame = new Rect();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        v = ((Activity) context).getWindow().findViewById(Window.ID_ANDROID_CONTENT);

        // 获取自定义布局文件pop.xml的视图
        final View popupWindow_view = LayoutInflater.from(context).inflate(R.layout.pop_window_share, null, false);
        llShow = (LinearLayout) popupWindow_view.findViewById(R.id.llShow);
        (popupWindow_view.findViewById(R.id.ll_sina)).setOnClickListener(this);
        (popupWindow_view.findViewById(R.id.btn_cancel)).setOnClickListener(this);
        (popupWindow_view.findViewById(R.id.img_copy_url)).setOnClickListener(this);
        (popupWindow_view.findViewById(R.id.ll_qq_space)).setOnClickListener(this);
        (popupWindow_view.findViewById(R.id.ll_qq_friends)).setOnClickListener(this);
        (popupWindow_view.findViewById(R.id.ll_wechat_friends)).setOnClickListener(this);
        (popupWindow_view.findViewById(R.id.ll_wechat_moments)).setOnClickListener(this);


        // 创建PopupWindow实例,200,screenHeight分别是宽度和高度
        pop = new PopupWindow(popupWindow_view, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, true);
        popupWindow_view.setFocusable(true);
        mShowAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHideAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        mShowAnimation.setDuration(200);
        mHideAnimation.setDuration(200);

        llShow.startAnimation(mShowAnimation);

        mHideAnimation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                pop.dismiss();
                pop = null;
            }
        });

        popupWindow_view.setFocusableInTouchMode(true);
        // 点击其他地方消失
        popupWindow_view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (pop != null && pop.isShowing())
                    llShow.startAnimation(mHideAnimation);

                return false;
            }
        });
    }

    public SharePopupWindow show() {
        if (pop != null && !pop.isShowing()) {
            pop.showAtLocation(v, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
        } else if (pop != null && pop.isShowing()) {
            llShow.startAnimation(mHideAnimation);
        } else {
            initPopupWindow();
            pop.showAtLocation(v, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
        }
        return null;
    }

    public void dismiss() {
        if (pop != null)
            pop.dismiss();
    }

    public boolean isShowing() {
        return pop != null && pop.isShowing();
    }

    @Override
    public void onClick(View v) {
        bShare(v.getId());
    }

    public void bShare(int id) {
        switch (id) {
            case R.id.btn_cancel:
                llShow.startAnimation(mHideAnimation);
                break;
            case R.id.img_copy_url:
                AppUtil.CopyToClip(context, url);
                ToastUtil.showToast(context, "已复制地址到剪切板！");
                llShow.startAnimation(mHideAnimation);
                break;
            case R.id.ll_sina:
                new ShareAction((Activity) context)
                        .withMedia(web)
                        .setPlatform(SHARE_MEDIA.SINA)
                        .setCallback(umShareListener).share();
                break;
            case R.id.ll_qq_space:
                new ShareAction((Activity) context)
                        .withMedia(web)
                        .setPlatform(SHARE_MEDIA.QZONE)
                        .setCallback(umShareListener).share();
                llShow.startAnimation(mHideAnimation);


                break;

            case R.id.ll_qq_friends:
                new ShareAction((Activity) context)
                        .withMedia(web)
                        .setPlatform(SHARE_MEDIA.QQ)
                        .setCallback(umShareListener).share();
                llShow.startAnimation(mHideAnimation);
                break;
            case R.id.ll_wechat_friends:
                new ShareAction((Activity) context)
                        .withMedia(web)
                        .setPlatform(SHARE_MEDIA.WEIXIN)
                        .setCallback(umShareListener).share();
                break;
            case R.id.ll_wechat_moments:
                new ShareAction((Activity) context)
                        .withMedia(web)
                        .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                        .setCallback(umShareListener).share();
                break;
            default:
                break;
        }
    }
}
