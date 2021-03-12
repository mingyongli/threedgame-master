package com.ws3dm.app.mvvm.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ws3dm.app.R;

public class ForumUserOperationDialog extends Dialog {

    private final Context mContent;
    private Window window;
    private static int mType;
    private View only_master;
    private View order;
    private View report;
    private View copy_message;
    private TextView cancel;
    private OnClickListener onlyMaterListener;
    private OnClickListener orderListener;
    private OnClickListener reportListener;
    private static ForumUserOperationDialog forumUserOperationDialog;
    private OnClickListener onCopyListener;
    private OnClickListener replyListener;
    private View replay;


    public ForumUserOperationDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContent = context;
    }

    public ForumUserOperationDialog(@NonNull Context context) {
        super(context);
        mContent = context;
    }

    public static ForumUserOperationDialog getDialog(Context context, int themeResId, int type) {
        mType = type;
        forumUserOperationDialog = new ForumUserOperationDialog(context, themeResId);
        return forumUserOperationDialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        window = getWindow();
        window.setWindowAnimations(R.style.share_animation);
        window.setGravity(Gravity.BOTTOM);
        if (mType == 1) {
            window.setContentView(R.layout.dialog_forum_master_operation);
            only_master = findViewById(R.id.only_master);
            order = findViewById(R.id.order);
        } else if (mType == 2) {
            window.setContentView(R.layout.dialog_forum_user_operation);
            copy_message = findViewById(R.id.copy_message);
            replay = findViewById(R.id.user_replay);
        }
        cancel = findViewById(R.id.cancel);
        report = findViewById(R.id.report);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        initListener();
    }

    private void initListener() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        if (reportListener != null) {
            report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reportListener.onClick(forumUserOperationDialog, DialogInterface.BUTTON_NEGATIVE);
                }
            });
        }
        if (mType == 1) {
            if (onlyMaterListener != null) {
                only_master.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onlyMaterListener.onClick(forumUserOperationDialog, DialogInterface.BUTTON_NEGATIVE);
                    }
                });

            }
            if (orderListener != null) {
                order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        orderListener.onClick(forumUserOperationDialog, DialogInterface.BUTTON_POSITIVE);
                    }
                });
            }
        } else if (mType == 2) {
            if (onCopyListener != null) {
                copy_message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCopyListener.onClick(forumUserOperationDialog, DialogInterface.BUTTON_POSITIVE);
                    }
                });
            }
            if (replyListener != null) {
                replay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        replyListener.onClick(forumUserOperationDialog, DialogInterface.BUTTON_POSITIVE);
                    }
                });
            }
        }
    }

    public ForumUserOperationDialog setOnlyMaterListener(DialogInterface.OnClickListener onClickListener) {
        onlyMaterListener = onClickListener;
        return this;
    }

    public ForumUserOperationDialog setOrderListener(DialogInterface.OnClickListener onClickListener) {
        orderListener = onClickListener;
        return this;
    }

    public ForumUserOperationDialog setReplyListener(DialogInterface.OnClickListener onClickListener) {
        replyListener = onClickListener;
        return this;
    }

    public ForumUserOperationDialog setReportListener(DialogInterface.OnClickListener onClickListener) {
        reportListener = onClickListener;
        return this;
    }

    public ForumUserOperationDialog setCopyListener(DialogInterface.OnClickListener onClickListener) {
        onCopyListener = onClickListener;
        return this;
    }

}
