package com.ws3dm.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ws3dm.app.R;
import com.ws3dm.app.bean.HistoryBean;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.util.TimeUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.view.PinnedSectionListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 章节列表 adapter
 */

public class HistoryAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter, View.OnClickListener {

    private List<HistoryBean> mItems = new ArrayList<>();
    private Context mContext;
    private String timeLabel = "";
    private OnItemClickListener listener;

    public HistoryAdapter(Context context, List<NewsBean> listData) {
        if (listData == null || listData.size() == 0) {
            return;
        }
        mContext = context;

        for (NewsBean bean : listData) {
            if (!bean.getSeeDate().equals(timeLabel)) {
                timeLabel = bean.getSeeDate();
                HistoryBean itemHeader = new HistoryBean();
                itemHeader.type = HistoryBean.ITEM_VIEW_TYPE_HEADER;
                itemHeader.title = bean.getSeeDate();
                mItems.add(itemHeader);
            }
            HistoryBean itemBody = new HistoryBean();
            itemBody.type = HistoryBean.ITEM_VIEW_TYPE_BODY;
            itemBody.lmfl = bean.getType();
            itemBody.title = bean.getTitle();
            itemBody.arcurl = bean.getArcurl();
            itemBody.webviewurl = bean.getWebviewurl();
            itemBody.description = bean.getTitle();
            mItems.add(itemBody);
        }
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public HistoryBean getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        int itemViewType = getItemViewType(position);
        if (itemViewType == HistoryBean.ITEM_VIEW_TYPE_HEADER) {
            HistoryBean HistoryBean = mItems.get(position);
            view = View.inflate(mContext, R.layout.item_history_head, null);
            TextView tv_head = (TextView) view.findViewById(R.id.tv_head);
            tv_head.setText(friendDate(HistoryBean.title));
        } else {
            HistoryBean bcb = mItems.get(position);
            view = View.inflate(mContext, R.layout.item_history_body, null);
            TextView tv_label = (TextView) view.findViewById(R.id.label);
            tv_label.setText(bcb.lmfl);
            TextView tv_body = (TextView) view.findViewById(R.id.body_title);
            tv_body.setText(bcb.title);
            if (listener != null) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.ItemClickListener(bcb);
                    }
                });
            }
        }

        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).type;
    }

    @Override
    public int getViewTypeCount() {
        // 一共四种type类型
        return 2;
    }

    public boolean isItemViewTypePinned(int viewType) {
        return viewType == HistoryBean.ITEM_VIEW_TYPE_HEADER;
    }

    @Override
    public void onClick(View v) {
        ToastUtil.showToast(mContext, "下载中……");
    }

    public String friendDate(String date) {
        String str = date;
        if (str.equals(TimeUtil.dateDayNow()))
            str = "今天";
        else if (str.equals(TimeUtil.dateYesterday()))
            str = "昨天";
        return str;
    }

    public void setItemClickListener(OnItemClickListener Listener) {
        listener = Listener;
    }

    public interface OnItemClickListener {
        void ItemClickListener(HistoryBean bean);
    }
}

