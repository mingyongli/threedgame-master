package com.ws3dm.app.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ws3dm.app.R;
import com.ws3dm.app.bean.Video;
import com.ws3dm.app.sqlite.VideoFile;
import com.ws3dm.app.util.SharedUtil;
import com.ws3dm.app.util.glide.GlideUtil;

import java.util.ArrayList;

/**
 * Describution :视屏列表适配器
 * 
 * Author : DKjuan
 * 
 * Date : 2017/8/21 11:47
 **/
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    public ArrayList<Video> datas = null;
    public static Context context;
    public static VideoFile videoCollectFile;
    public VideoAdapter(Context context, ArrayList<Video> datas) {
        this.datas = datas;
        this.context=context;
        videoCollectFile=new VideoFile(context);
    }
    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_video,viewGroup,false);
        return new ViewHolder(view);
    }
    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ((ViewHolder) holder).bindDateView(position, datas.get(position));
    }
    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }
    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llVideo;
        ImageView imgPlay, imgCover, imgComment;
        TextView txtLine, txtName, txtPlay, txtTime, txtComment, txtVideoTime;

        ViewHolder(View view) {
            super(view);
//            AutoUtils.autoSize(itemView);
//            llVideo = (LinearLayout) view.findViewById(R.id.llVideo);
//            imgPlay = (ImageView) view.findViewById(R.id.imgPlay);
//            imgCover = (ImageView) view.findViewById(R.id.imgCover);
//            imgComment = (ImageView) view.findViewById(R.id.imgComment);
//            txtLine = (TextView) view.findViewById(R.id.txtLine);
//            txtName = (TextView) view.findViewById(R.id.txtName);
//            txtPlay = (TextView) view.findViewById(R.id.txtPlay);
//            txtTime = (TextView) view.findViewById(R.id.txtTime);
//            txtComment = (TextView) view.findViewById(R.id.txtComment);
//            txtVideoTime = (TextView) view.findViewById(R.id.txtVideoTime);
        }

        void bindDateView(final int position, final Video video) {
//            if (position == 0)
//                txtLine.setVisibility(View.GONE);
//            else
//                txtLine.setVisibility(View.VISIBLE);
            txtTime.setText(video.getSenddate());
            txtName.setText(video.getTitle());
            txtPlay.setText(video.getClick() + "次播放");
            GlideUtil.loadImage(context,video.getVideopic(),imgCover,R.drawable.video_loading, R.drawable.video_error);
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        // 播放详情页
                        case R.id.imgPlay:
                        case R.id.llVideo:
                        case R.id.imgComment:
                        case R.id.txtComment:
                            imgPlay.setOnClickListener(null);
                            llVideo.setOnClickListener(null);
                            imgComment.setOnClickListener(null);
                            txtComment.setOnClickListener(null);
//                            Intent intent = new Intent(context, VideoActivity.class);//临时注释 不跳转
//                            Bundle bundle = new Bundle();
//                            bundle.putSerializable("video", video);
//                            intent.putExtras(bundle);
//                            context.startActivity(intent);
                            break;
                    }
                }
            };
            imgPlay.setOnClickListener(listener);
            llVideo.setOnClickListener(listener);
            txtComment.setOnClickListener(listener);
            imgComment.setOnClickListener(listener);

            // 更换主题
            if (SharedUtil.getSharedPreferencesData("isNight").equals("0")) {
                txtTime.setTextColor(0xff9c9c9c);
                txtPlay.setTextColor(0xff9c9c9c);
                txtLine.setBackgroundColor(0xffe7e7e7);
                llVideo.setBackgroundColor(0xffffffff);
            } else {
                txtTime.setTextColor(0xff555555);
                txtPlay.setTextColor(0xff555555);
                txtLine.setBackgroundColor(0xff666666);
                llVideo.setBackgroundColor(0xff2a2a2a);
            }
        }
    }
}