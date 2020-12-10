package com.jcodecraeer.xrecyclerview;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.progressindicator.AVLoadingIndicatorView;

public class LoadingMoreFooter extends LinearLayout {

    private SimpleViewSwitcher progressCon;
    public final static int STATE_LOADING = 0;
    public final static int STATE_COMPLETE = 1;
    public final static int STATE_NOMORE = 2;
    public final static int STATE_ERROR = 3;
    private ImageView mImage;
    private TextView mText;
    private String loadingHint;
    private String noMoreHint;
    private String loadingDoneHint;
    private String loadingErrorHint;

    public LoadingMoreFooter(Context context) {
        super(context);
        initView();
    }

    /**
     * @param context
     * @param attrs
     */
    public LoadingMoreFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void setLoadingHint(String hint) {
        loadingHint = hint;
    }

    public void setNoMoreHint(String hint) {
        noMoreHint = hint;
    }

    public void setLoadingDoneHint(String hint) {
        loadingDoneHint = hint;
    }

    public void setLoadingErrorHint(String loadingErrorHint) {
        this.loadingErrorHint = loadingErrorHint;
    }

    public void initView(){
        setGravity(Gravity.CENTER|Gravity.CENTER_VERTICAL);
        setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //旋转图标
        progressCon = new SimpleViewSwitcher(getContext());
        progressCon.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        AVLoadingIndicatorView progressView = new  AVLoadingIndicatorView(this.getContext());
        progressView.setIndicatorColor(0xffB5B5B5);
        progressView.setIndicatorId(ProgressStyle.BallSpinFadeLoader);
        progressCon.setView(progressView);
        addView(progressCon);
        //网络异常图标
        mImage=new ImageView(getContext());
        mImage.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        int padding=(int)getResources().getDimension(R.dimen.net_error_padding);
        mImage.setImageResource(R.drawable.net_error);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins( (int)getResources().getDimension(R.dimen.textandiconmargin),0,0,0 );
        mImage.setLayoutParams(lp);
        addView(mImage);
        //设置底部文字
        mText = new TextView(getContext());
        mText.setGravity(Gravity.CENTER);
        mText.setText("正在加载...");
//        loadingErrorHint = (String)getContext().getText(R.string.loading_error);
//        loadingHint = (String)getContext().getText(R.string.listview_loading);
////        noMoreHint = (String)getContext().getText(R.string.nomore_loading);
//        noMoreHint =getResources().getString(R.string.nomore_loading);
////        loadingDoneHint = (String)getContext().getText(R.string.loading_done);
//        noMoreHint =getResources().getString(R.string.nomore_loading);
//        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        layoutParams.setMargins( (int)getResources().getDimension(R.dimen.textandiconmargin),0,0,0 );
        
//        新修改
        loadingErrorHint = "网络异常，请重新加载！";

        loadingHint = getResources().getString(R.string.listview_loading);
//        noMoreHint = (String)getContext().getText(R.string.nomore_loading);
        noMoreHint =getResources().getString(R.string.nomore_loading);
//        loadingDoneHint = (String)getContext().getText(R.string.loading_done);
        noMoreHint =getResources().getString(R.string.nomore_loading);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins( (int)getResources().getDimension(R.dimen.textandiconmargin),0,0,0 );

        mText.setLayoutParams(layoutParams);
        addView(mText);
    }

    public void setProgressStyle(int style) {
        if(style == ProgressStyle.SysProgress){
            progressCon.setView(new ProgressBar(getContext(), null, android.R.attr.progressBarStyle));
        }else{
            AVLoadingIndicatorView progressView = new  AVLoadingIndicatorView(this.getContext());
            progressView.setIndicatorColor(0xffB5B5B5);
            progressView.setIndicatorId(style);
            progressCon.setView(progressView);
        }
    }

    public void  setState(int state) {
        switch(state) {
            case STATE_LOADING:
                progressCon.setVisibility(View.VISIBLE);
                mImage.setVisibility(View.GONE);
                mText.setText(loadingHint);
                this.setVisibility(View.VISIBLE);
                break;
            case STATE_COMPLETE:
                mText.setText(loadingDoneHint);
                this.setVisibility(View.GONE);
                mImage.setVisibility(View.GONE);
                break;
            case STATE_NOMORE:
                mText.setText(noMoreHint);
                progressCon.setVisibility(View.GONE);
                mImage.setVisibility(View.GONE);
                this.setVisibility(View.VISIBLE);
                break;
            case STATE_ERROR:
                progressCon.setVisibility(View.GONE);
                mImage.setVisibility(View.VISIBLE);
                mText.setText(loadingErrorHint);
                this.setVisibility(View.VISIBLE);
                break;
        }
    }
}