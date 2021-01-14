package com.ws3dm.app.mvvm.view.activity;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.BaseActivity;
import com.ws3dm.app.databinding.AcSectionSearchBinding;
import com.ws3dm.app.mvvm.adapter.ForumPlateListAdapter;
import com.ws3dm.app.mvvm.bean.ForumPlateBean;
import com.ws3dm.app.mvvm.viewmodel.BaseViewModel;
import com.ws3dm.app.mvvm.viewmodel.PlateSearchViewModel;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.view.DMFreshHead;
import com.yu.imgpicker.utils.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;

public class PlateSearchActivity extends BaseActivity {

    private PlateSearchViewModel viewModel;
    private AcSectionSearchBinding mBind;
    private ForumPlateListAdapter adapter;
    private String plateContent;
    private int page = 1;
    private String plateId;

    @Override
    protected void init() {
        viewModel = new ViewModelProvider(this).get(PlateSearchViewModel.class);
        plateId = getIntent().getStringExtra("plateId");
        initView();
        initListener();
    }


    private void initView() {
        mBind = bindView(R.layout.ac_section_search);
        mBind.refreshLayout.setRefreshHeader(new DMFreshHead(this));
        mBind.refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        mBind.recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new ForumPlateListAdapter(mContext, R.layout.adapter_forum_item);
        mBind.recyclerview.setAdapter(adapter);
    }

    private void initListener() {
        mBind.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBind.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plateContent = mBind.search.getText().toString();
                if (plateContent.length() != 0) {
                    page = 1;
                    viewModel.SearchPlate(plateId,plateContent,page);
                    hideInput();
                }else {
                    ToastUtil.showToast(mContext,"请输入关键字");
                }
            }
        });
        mBind.search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                LogUtils.d("Edit" + keyCode + "" + event.toString());
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    plateContent = mBind.search.getText().toString().trim();
                    // 监听到回车键，会执行2次该方法。按下与松开
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        //按下事件
                        if (plateContent.length() != 0) {
                            page = 1;
                            viewModel.SearchPlate(plateId, plateContent, page);
                            hideInput();
                        } else {
                            ToastUtil.showToast(mContext,"请输入关键字");
                        }
                    }
                }

                return false;
            }
        });

        viewModel.getState().observe(this, new Observer<BaseViewModel.State>() {
            @Override
            public void onChanged(BaseViewModel.State state) {
                if (state == BaseViewModel.State.LOADING) {
                    if (page == 1) {
                        mBind.loadView.setVisibility(View.VISIBLE);
                        mBind.refreshLayout.setVisibility(View.GONE);
                    }

                } else if (state == BaseViewModel.State.SUCCESS) {
                    if (page == 1) {
                        mBind.loadView.setVisibility(View.GONE);
                        mBind.refreshLayout.setVisibility(View.VISIBLE);
                    }
                } else if (state == BaseViewModel.State.EMPTY) {
                    if (page == 1) {
                        mBind.loadView.setVisibility(View.VISIBLE);
                        mBind.refreshLayout.setVisibility(View.GONE);
                        mBind.tip.setText("没有更多数据了");
                    }
                } else if (state == BaseViewModel.State.ERR) {
                    mBind.loadView.setVisibility(View.VISIBLE);
                    mBind.refreshLayout.setVisibility(View.GONE);
                    mBind.tip.setText("加载出错,请重新加载");
                }
                mBind.refreshLayout.finishRefresh();
                mBind.refreshLayout.finishLoadMore();
            }
        });
        viewModel.getPlateBean().observe(this, new Observer<List<ForumPlateBean.DataBean.ListBean>>() {
            @Override
            public void onChanged(List<ForumPlateBean.DataBean.ListBean> listBeans) {
                if (page == 1) {
                    adapter.clearAndAddList(listBeans);
                } else {
                    adapter.appendList(listBeans);
                }
            }
        });

        mBind.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                viewModel.SearchPlate(plateId, plateContent, page);
            }
        });
        mBind.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page += 1;
                viewModel.SearchPlate(plateId, plateContent, page);
            }
        });
    }

    public void showInput(final EditText et) {
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 隐藏键盘
     */
    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }

    }
}
