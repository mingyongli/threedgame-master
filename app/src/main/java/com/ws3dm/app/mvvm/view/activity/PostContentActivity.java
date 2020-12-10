package com.ws3dm.app.mvvm.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.ws3dm.app.R;
import com.ws3dm.app.activity.BaseActivity;
import com.ws3dm.app.databinding.AcPostContentBinding;
import com.ws3dm.app.mvvm.viewmodel.PostContentViewModel;

import jp.wasabeef.richeditor.RichEditor;

public class PostContentActivity extends BaseActivity {

    private PostContentViewModel viewModel;
    private AcPostContentBinding mBind;

    @Override
    protected void init() {
        viewModel = new ViewModelProvider(this).get(PostContentViewModel.class);
        getLifecycle().addObserver(viewModel);
        mBind = bindView(R.layout.ac_post_content);
        initView();
        initData();
        initListener();
    }

    @SuppressLint("ResourceAsColor")
    private void initView() {
        mBind.RichEditor.setEditorFontSize(16);
        mBind.RichEditor.setPadding(10, 10, 10, 10);
    }

    private void initData() {
        mBind.postContentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String html = mBind.RichEditor.getHtml();
            }
        });
    }

    private void initListener() {

    }
}
