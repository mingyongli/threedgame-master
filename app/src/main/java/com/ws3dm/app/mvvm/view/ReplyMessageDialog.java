package com.ws3dm.app.mvvm.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.ws3dm.app.R;

public class ReplyMessageDialog extends Dialog {
    private Window window;
    private Context mContext;
    private EditText replay_edit;
    private Button replay_btn;
    private ReplayMessage mListener;
    private static ReplyMessageDialog dialog;
    private InputMethodManager imm;
    private InputMethodManager systemService;
    private static Activity mActivity;

    public ReplyMessageDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    public static ReplyMessageDialog getDialog(Activity activity, Context context, int themeResId) {
        mActivity = activity;
        dialog = new ReplyMessageDialog(context, themeResId);
        return dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        systemService = mContext.getSystemService(InputMethodManager.class);
        setContentView(R.layout.dialog_forum_reply);
        window = getWindow();
        window.setWindowAnimations(R.style.share_animation);
        window.setGravity(Gravity.BOTTOM);
        //window.setContentView(R.layout.dialog_forum_reply);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        initView();
        initListener();
    }

    private void initView() {
        replay_edit = findViewById(R.id.replay_edit);
        replay_btn = findViewById(R.id.replay_btn);

    }

    private void initListener() {
        if (mListener != null) {
            replay_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.getMessage(dialog, replay_edit.getText().toString());

                }
            });
        }
    }

    public ReplyMessageDialog setReplayBtnListener(ReplayMessage listener) {
        mListener = listener;
        return this;
    }

    public void showInput(final EditText et) {
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }
//
//    /**
//     * 隐藏键盘
//     */
//    protected void hideInput() {
//        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//        View v = getWindow().peekDecorView();
//        if (null != v) {
//            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//        }
//
//    }

    public interface ReplayMessage {
        void getMessage(Dialog dialog, String message);
    }
}
