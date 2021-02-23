package com.ws3dm.app.mvvm.view.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ws3dm.app.R;
import com.ws3dm.app.activity.BaseActivity;
import com.ws3dm.app.adapter.BaseRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.ForumGidBean;
import com.ws3dm.app.bean.UpLoadImageBean;
import com.ws3dm.app.databinding.AcPostContentBinding;
import com.ws3dm.app.mvp.model.RespBean.ForumTidTypeRespBean;
import com.ws3dm.app.mvvm.viewmodel.BaseViewModel;
import com.ws3dm.app.mvvm.viewmodel.PostContentViewModel;
import com.ws3dm.app.util.ProDialog;
import com.ws3dm.app.util.ToastUtil;

import java.io.File;
import java.util.Objects;

public class PostContentActivity extends BaseActivity {

    private PostContentViewModel viewModel;
    private AcPostContentBinding mBind;
    private String plateTitle;
    private String plateId;
    private BaseRecyclerAdapter<ForumGidBean> mCategoryRecyclerAdapter;
    private ForumTidTypeRespBean.DataBean bean;
    private ForumGidBean selectGid;
    private String typeid;
    private RecyclerView mCategoryRecyclerView;
    private PopupWindow mPopupWindowCate;
    public static final int PICK_PHOTO = 102;
    private Dialog dialog;

    @Override
    protected void init() {
        viewModel = new ViewModelProvider(this).get(PostContentViewModel.class);
        getLifecycle().addObserver(viewModel);
        plateId = getIntent().getStringExtra("plateId");
        plateTitle = getIntent().getStringExtra("plateTitle");
        mBind = bindView(R.layout.ac_post_content);
        initView();
        initData();
        initListener();
    }

    @SuppressLint("ResourceAsColor")
    private void initView() {
        dialog = ProDialog.createLoadingDialog(mContext, "上传中");
        mBind.forumTitle.setText(plateTitle);
        mBind.RichEditor.setEditorFontSize(16);
        mBind.RichEditor.setPadding(10, 10, 10, 10);
        mBind.RichEditor.setAlignCenter();
        initPopupWindow();
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

                selectGid = bean.getList().get(position);
                typeid = selectGid.getTypeid();
                mBind.label.setText(selectGid.getName());
                mPopupWindowCate.dismiss();
            }
        });

        View contentViewCate = LayoutInflater.from(mContext).inflate(R.layout.pop_list_cate, null);
        mCategoryRecyclerView = contentViewCate.findViewById(R.id.recycler_view_cate);
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

    private void initData() {
        viewModel.getLabel(mContext, Integer.parseInt(plateId));
    }

    private void initListener() {
        viewModel.getState().observe(this, new Observer<BaseViewModel.State>() {
            @Override
            public void onChanged(BaseViewModel.State state) {
                switch (state) {
                    case LOADING:
                        dialog.show();
                        break;
                    case ERR:
                    case SUCCESS:
                        dialog.dismiss();
                        break;
                }
            }
        });
        mBind.insertImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_PHOTO);
            }
        });
        mBind.label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] location = new int[2];
                v.getLocationInWindow(location);
                mPopupWindowCate.showAtLocation(v, Gravity.BOTTOM, location[0], location[1] + v.getHeight());
            }
        });
        viewModel.getTidList().observe(this, new Observer<ForumTidTypeRespBean.DataBean>() {
            @Override
            public void onChanged(ForumTidTypeRespBean.DataBean dataBean) {
                bean = dataBean;
                mCategoryRecyclerAdapter.clearAndAddList(bean.getList());
            }
        });
        viewModel.getNetImagePath().observe(this, new Observer<UpLoadImageBean>() {
            @Override
            public void onChanged(UpLoadImageBean bean) {
                if (bean.getCode() == 1) {
                    ToastUtil.showToast(mContext, bean.getMsg() + "");
                    mBind.RichEditor.focusEditor();
                    mBind.RichEditor.insertAutoImage(bean.getData().getAttachurl(), String.valueOf(bean.getData().getAttachid()));
                } else {
                    ToastUtil.showToast(mContext, bean.getMsg() + "");
                }

            }

        });
        mBind.postContentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (typeid != null && typeid.length() > 0) {
                    if (Objects.requireNonNull(mBind.editTitle.getText()).toString().length() > 0) {
                        if (mBind.RichEditor.getHtml().length() > 0) {
                            viewModel.PostContent(mContext, typeid, plateId, mBind.editTitle.getText().toString(), mBind.RichEditor.getHtml());
                        } else {
                            ToastUtil.showToast(mContext, "内容不能为空");
                        }
                    } else {
                        ToastUtil.showToast(mContext, "未设置标题");
                    }
                } else {
                    ToastUtil.showToast(mContext, "未选择标签");
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_PHOTO) {
                upLoadImage(data);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void upLoadImage(Intent data) {
        String imagePath = getImagePath(data);
        if (imagePath != null) {

            File file = new File(imagePath);
            viewModel.upLoadImage(mContext, String.valueOf(plateId), file);
        } else {
            ToastUtil.showToast(mContext, "图片选取失败");
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private String getImagePath(Intent data) {
        Uri uri = data.getData();
        Cursor cursor = getContentResolver()
                .query(uri, null, null, null, null, null);
        if (cursor != null && cursor.moveToNext()) {
            return cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        } else {
            return null;
        }
    }

}
