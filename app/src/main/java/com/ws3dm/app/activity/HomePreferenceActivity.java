package com.ws3dm.app.activity;

import android.os.AsyncTask;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.DragItem.DragItemCallBack;
import com.ws3dm.app.adapter.HomePreferenceAdapter;
import com.ws3dm.app.bean.HomeTabsDBBean;
import com.ws3dm.app.databinding.ActivityHomePreferenceBinding;
import com.ws3dm.app.sqlite.TitleFile;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePreferenceActivity extends BaseActivity {

    private ActivityHomePreferenceBinding mBind;
    private HomePreferenceAdapter adapter;
    private TitleFile titleFile;

    @Override
    protected void init() {
        mBind = bindView(R.layout.activity_home_preference);
        setSupportActionBar(mBind.toolbar);
        initView();
        initData();
    }

    private void initView() {
        adapter = new HomePreferenceAdapter();
        mBind.recyclerview.setLayoutManager(new GridLayoutManager(mContext, 3));
        DragItemCallBack callBack = new DragItemCallBack() {
            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }
        };
        ItemTouchHelper helper = new ItemTouchHelper(callBack);
        helper.attachToRecyclerView(mBind.recyclerview);
        mBind.recyclerview.setAdapter(adapter);
    }

    private void initData() {
        titleFile = new TitleFile(mContext);
        getTopTabs();
        mBind.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.submit:
                        adapter.setSubmitListener(new HomePreferenceAdapter.OnSubmitListener() {
                            @Override
                            public void onSubmitListener(List<HomeTabsDBBean.HomeTabsData> mdata) {
                                MyAsyncTask myTask = new MyAsyncTask(mdata);
                                myTask.execute(1000);
                                setResult(RESULT_OK);
                                ToastUtil.showToast(mContext, "修改完成");
                                finish();
                            }
                        });
                }
                return false;
            }
        });
    }

    /**
     * 从网络上拿
     */
    private void getTopTabs() {
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(time + NewUrl.KEY);
        Map<String, Object> values = new HashMap<>();
        values.put("sign", sign);
        values.put("time", time);
        String json = new JSONObject(values).toString();
        OkHttpUtils
                .postString()
                .url(NewUrl.TOP_TABLES)
                .content(json)
                .build()
                .execute(new Callback<HomeTabsDBBean>() {

                    @Override
                    public HomeTabsDBBean parseNetworkResponse(com.squareup.okhttp.Response response) throws IOException {
                        String data = response.body().string();
                        Log.e("3DMAGME", data);
                        return new Gson().fromJson(data, HomeTabsDBBean.class);
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("message", e.getMessage());
                    }

                    @Override
                    public void onResponse(HomeTabsDBBean response) {
                        if (response.getCode() == 1) {
                            //对比完数据
                            ContrastData(response.getData());
                        }
                    }
                });


    }

    private void ContrastData(List<HomeTabsDBBean.HomeTabsData> data) {
        //拿去数据库中的数据和服务器上的数据进行对比
        List<HomeTabsDBBean.HomeTabsData> query = titleFile.query();
        if (query.size() == 0) {
            //当数据库数据为空的时候
            for (HomeTabsDBBean.HomeTabsData homeTabsData : data) {
                homeTabsData.setOpen(1);
            }
            titleFile.add(data);
        } else {
            LocalToServer(query, data);
            ServerToLocal(query, data);
        }
        //最后再从数据库中获取数据显示出来
        List<HomeTabsDBBean.HomeTabsData> endQuery = titleFile.query();
        adapter.setData(endQuery);
    }

    private void LocalToServer(List<HomeTabsDBBean.HomeTabsData> query, List<HomeTabsDBBean.HomeTabsData> data) {
        Map<Integer, Integer> map = new HashMap<>();
        List<HomeTabsDBBean.HomeTabsData> differentList = new ArrayList<>();
        for (HomeTabsDBBean.HomeTabsData datum : data) {
            map.put(datum.getAid(), 1);
        }
        for (HomeTabsDBBean.HomeTabsData homeTabsData : query) {
            if (map.get(homeTabsData.getAid()) == null) {
                //如果找不到 说明服务器上tab数量删除了
                differentList.add(homeTabsData);
                titleFile.delete(homeTabsData.getAid());
            }
        }
    }

    private void ServerToLocal(List<HomeTabsDBBean.HomeTabsData> query, List<HomeTabsDBBean.HomeTabsData> data) {
        Map<Integer, Integer> map = new HashMap<>();
        for (HomeTabsDBBean.HomeTabsData homeTabsData : query) {
            map.put(homeTabsData.getAid(), 1);
        }
        for (HomeTabsDBBean.HomeTabsData datum : data) {
            if (map.get(datum.getAid()) == null) {
                //如果找不到 说明服务器上的tab增多了
                titleFile.addOne(datum);
            }
        }
        titleFile.update(data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_preference_menu, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }

    class MyAsyncTask extends AsyncTask<Object, Object, Object> {
        private List<HomeTabsDBBean.HomeTabsData> data = new ArrayList<>();

        public MyAsyncTask(List<HomeTabsDBBean.HomeTabsData> mdata) {
            data.addAll(mdata);
        }

        @Override
        protected Object doInBackground(Object... objects) {
            titleFile.updateAll(data);
            return null;
        }
    }

}



