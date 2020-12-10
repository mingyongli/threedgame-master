package com.ws3dm.app.adapter;

import android.annotation.SuppressLint;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ws3dm.app.MyApplication;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.DragItem.OnDragVHListener;
import com.ws3dm.app.adapter.DragItem.OnItemMoveListener;
import com.ws3dm.app.bean.HomeTabsDBBean;
import com.ws3dm.app.util.glide.GlideUtil;

import java.util.ArrayList;
import java.util.List;

import ch.ielse.view.SwitchView;

public class HomePreferenceAdapter extends RecyclerView.Adapter<HomePreferenceAdapter.InneHolder> implements OnItemMoveListener {
    private List<HomeTabsDBBean.HomeTabsData> mdata = new ArrayList<>();
    private OnSubmitListener mSubmitListener = null;
    private List<Integer> isCheck = new ArrayList<>();

    @Override
    public InneHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_title_setting_item, parent, false);

        return new InneHolder(inflate);
    }

    @Override
    public void onBindViewHolder(InneHolder holder, int position) {
        HomeTabsDBBean.HomeTabsData homeTabsData = mdata.get(position);
        holder.title.setText(homeTabsData.getTitle());
        if (homeTabsData == mdata.get(position)) {
            if (homeTabsData.getType() == 0) {
                holder.switchView.setVisibility(View.GONE);
            }else {
                holder.switchView.setVisibility(View.VISIBLE);
            }
        }
        if (homeTabsData.isOpen() == 1) {
            holder.switchView.setOpened(true);
        } else if (homeTabsData.isOpen() == 0) {
            holder.switchView.setOpened(false);
        }
        holder.switchView.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                view.setOpened(true);
                homeTabsData.setOpen(1);
            }

            @Override
            public void toggleToOff(SwitchView view) {
                view.setOpened(false);
                homeTabsData.setOpen(0);
            }
        });
        GlideUtil.loadImage(MyApplication.getInstance().getBaseContext(), homeTabsData.getLitpic(), holder.titlebg);
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(List<HomeTabsDBBean.HomeTabsData> data) {
        mdata.addAll(data);
        for (HomeTabsDBBean.HomeTabsData datum : data) {
            isCheck.add(datum.isOpen());
        }
        notifyDataSetChanged();
    }

    @Override
    public void moveItem(int fromitem, int toProstion) {
        HomeTabsDBBean.HomeTabsData homeTabsData = mdata.get(fromitem);
        mdata.remove(homeTabsData);
        mdata.add(toProstion, homeTabsData);
        notifyItemMoved(fromitem, toProstion);
        for (HomeTabsDBBean.HomeTabsData mdatum : mdata) {

            Log.d("TAG", "" + mdatum.isOpen() + mdatum.getTitle());

        }
    }

    public class InneHolder extends RecyclerView.ViewHolder implements OnDragVHListener {
        private final TextView title;
        private final SwitchView switchView;
        private final ImageView titlebg;

        public InneHolder(View itemView) {
            super(itemView);
            switchView = itemView.findViewById(R.id.open_off);
            title = itemView.findViewById(R.id.title_text);
            titlebg = itemView.findViewById(R.id.title_img);
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onItemSelected() {
        }

        @Override
        public void onItemFinsh() {
        }
    }

    public void setSubmitListener(OnSubmitListener listener) {
        this.mSubmitListener = listener;
        mSubmitListener.onSubmitListener(mdata);
    }

    public interface OnSubmitListener {
        void onSubmitListener(List<HomeTabsDBBean.HomeTabsData> mdata);
    }
}
