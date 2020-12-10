package com.ws3dm.app.util.glide;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestListener;
import com.ws3dm.app.R;

import com.bumptech.glide.Glide;
import com.ws3dm.app.util.AppUtil;
import com.ws3dm.app.util.SharedUtil;


public class GlideUtil{
    /**
     * Describution : 加载网络图片
     * 
     * Author : DKjuan
     * 
     * Date : 2017/7/11 8:52
     * 加载网络图片 GlideUtil.loadImage(this,"https://www.baidu.com/img/bdlogo.png",img);
     * 加载本地图片 GlideUtil.loadImage(this,R.mipmap.rocket,img);
     * 加载内存卡上的图片 GlideUtil.loadImage(this,"file://" + Environment.getExternalStorageDirectory().getPath() + "/Pictures/Screenshots/test.jpg",img);
     * 加载GIF图片 GlideUtil.loadGIFImage(this,"http://ww1.sinaimg.cn/large/85cccab3tw1esjw3dyui5g209q0bk1kx.jpg",img);
     * 加载RAW文件夹下的图片 GlideUtil.loadImage(this,R.raw.singledog,img);
     * 加载Assets图片 GlideUtil.loadImage(this,"file:///android_asset/wolf.jpg",img);
     * 加载Assets图片 GlideUtil.loadImage(this,"file:///android_asset/wolf.jpg",img);
     **/

    public static int DefaultSource=R.drawable.load_default;
    public static int ErrorSource=R.drawable.load_error;
    
    public static void loadImage(Context context, String path, ImageView imageView){
        if(SharedUtil.getSharedPreferencesData("NoWifiPic").equals("1")&&!AppUtil.isWifi(context))
            loadImage(context,DefaultSource,imageView, DefaultSource,ErrorSource);
        else
            loadImage(context,path,imageView, DefaultSource,ErrorSource);
    }
    public static void loadImage(Context context, int sourceID, ImageView imageView){
        loadImage(context,sourceID,imageView,DefaultSource,ErrorSource);
    }
    
    /**
     * Describution ://加载本地图片
     * 
     * Author : DKjuan
     * Date : 2017/7/11 8:50
     * 重载方法     path:内存卡上的路径 ,sourceID:APK中的资源文件ID
     **/
//    public static void loadLocalImage(Context context, String path, ImageView imageView){
////        Glide.with(context).load("file://" + Environment.getExternalStorageDirectory().getPath() + path)
////                .placeholder(DefaultSource)
////                .error(R.mipmap.error)
////                .into(img);
//        loadImage(context,"file://" + Environment.getExternalStorageDirectory().getPath() + path,imageView,DefaultSource,R.mipmap.error);
//    }
//    public static void loadLocalImage(Context context, int sourceID, ImageView imageView){
//        Glide.with(context).load(sourceID)
//                .placeholder(DefaultSource)
//                .error(R.mipmap.error)
//                .into(imageView);
//    }

    /**
     * Describution :加载网络图片
     * 
     * Author : DKjuan
     * 
     * Date : 2017/7/11 9:04
     **/
    public static void loadGIFImage(Context context, String url, ImageView imageView){
//                .crossFade()  淡入淡出 ， 也是默认动画
//                .crossFade( int duration )  定义淡入淡出的时间间隔
//                .dontAnimate()   不使用任何动画
        Glide.with(context).load(url)
                .asGif()
                .crossFade()
                .placeholder(DefaultSource)
                .error(ErrorSource)
                .into(imageView);
    }
    public static void loadGIFImage(Context context, int sourceID, ImageView imageView){
//        Glide.with(this).load("http://ww1.sinaimg.cn/large/85cccab3tw1esjw3dyui5g209q0bk1kx.jpg")
//                .asGif()
//                .placeholder(DefaultSource)
//                .error(ErrorSource)
//                .into(img);
        Glide.with(context).load(sourceID)
                .asGif()
                .placeholder(DefaultSource)
                .error(ErrorSource)
                .into(imageView);
    }

    /**
     * Describution :加载RAW图片
     * 
     * Author : DKjuan
     * 
     * Date : 2017/7/11 9:06
     **/
    
//    public static void loadRAWImage(Context context, Integer sourceID, ImageView imageView){
//        Glide.with(context).load(sourceID)
//                .placeholder(DefaultSource)
//                .error(ErrorSource)
//                .into(imageView);
//    }
    /**
     * Describution :加载Assets图片
     * 
     * Author : DKjuan
     * 
     * Date : 2017/7/11 9:06
     **/
//    public static void loadAssetsImage(Context context, String path, ImageView imageView){
////        Glide.with(this).load("file:///android_asset/wolf.jpg")
////                .placeholder(DefaultSource)
////                .error(ErrorSource)
////                .into(img);
//        Glide.with(context).load(path)
//                .placeholder(DefaultSource)
//                .error(ErrorSource)
//                .into(imageView);
//    }

    /**
     * Describution :Glide基本方法
     * 
     * Author : DKjuan
     * 
     * Date : 2017/7/11 9:59
     **/
    //路径可以是 url，本地文件地址
    public static void loadImage(Context context, String path, ImageView imageView,int DefaultSource,int ErrorSource){
        Glide.with(context).load(path).dontAnimate()
//                .animate(R.anim.crop_image_fade_anim)//添加自定义效果
                .placeholder(DefaultSource)
                .error(ErrorSource)
                .into(imageView);
    }
    //加载本地资源
    public static void loadImage(Context context, int sourceID, ImageView imageView,int DefaultSource,int ErrorSource){
        Glide.with(context).load(sourceID)
                .placeholder(DefaultSource)
                .error(ErrorSource)
                .into(imageView);
    }
    //监听加载过程
    public static void loadImage(Context context, String path, ImageView imageView,int DefaultSource,int ErrorSource,RequestListener listener){
        Glide.with(context).load(path)
                .placeholder(DefaultSource)
                .error(ErrorSource)
                .listener(listener)
                .into(imageView);
    }

    /**
     * Describution :加载圆形图片
     * 
     * Author : DKjuan
     * 
     * Date : 2017/7/11 9:26
     * GlideUtil.loadCircleImage(this,R.mipmap.qin,img);
     **/

    public static void loadCircleImage(Context context, String path, ImageView imageView){//路径可以是 url，本地文件地址
        Glide.with(context).load(path)
                .transform(new GlideCircleTransform(context))
                .placeholder(DefaultSource)
                .error(ErrorSource)
                .into(imageView);
    }

    public static void loadCircleImage(Context context, int sourceID, ImageView imageView){
        Glide.with(context).load(sourceID)
                .transform(new GlideCircleTransform(context))
                .placeholder(DefaultSource)
                .error(ErrorSource)
                .into(imageView);
    }
    /**
     * Describution :加载圆角矩形图片
     * 
     * Author : DKjuan
     * 
     * Date : 2017/7/11 9:28
     * GlideUtil.loadRectangleImage(this,R.mipmap.qin,img);
     **/
    public static void loadRoundImage(Context context, String path, ImageView imageView){//路径可以是 url，本地文件地址
        Glide.with(context).load(path)
                .transform(new GlideRoundTransform(context, 20))//20：圆角半径
                .placeholder(DefaultSource)
                .error(ErrorSource)
                .into(imageView);
    }
    public static void loadRoundImage(Context context, String path, ImageView imageView,int defaults,int error){//路径可以是 url，本地文件地址
        Glide.with(context).load(path)
                .placeholder(defaults)
                .error(error)
                .transform(new GlideRoundTransform(context, 20))//20：圆角半径
                .into(imageView);
    }
    public static void loadRoundImage(Context context, String path, ImageView imageView,int radius){
        Glide.with(context).load(path)
                .asBitmap()
                .placeholder(DefaultSource)
                .error(ErrorSource)
                .transform(new GlideRoundTransform(context, radius))//20：圆角半径
                .into(imageView);
    }
    
    public static void loadRoundImage(Context context, int sourceID, ImageView imageView){
        Glide.with(context).load(sourceID)
                .placeholder(DefaultSource)
                .error(ErrorSource)
                .transform(new GlideRoundTransform(context, 20))//20：圆角半径
                .into(imageView);
    }
    public static void loadRoundImage(Context context,int sourceID, ImageView imageView,int radius){
        Glide.with(context).load(sourceID)
                .placeholder(DefaultSource)
                .error(ErrorSource)
                .transform(new GlideRoundTransform(context, radius))//20：圆角半径
                .into(imageView);
    }
    
    /*
    
      在listview列表中使用Glide
        //         all:缓存源资源和转换后的资源
        //        none:不作任何磁盘缓存
        //        source:缓存源资源         次快
        //        result：缓存转换后的资源  最快
        Glide.with(mContext).load(path)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(R.mipmap.placeimg)
                .centerCrop()  //转换宽高比
                .into(vh.img);
        return convertView;
    
     */
    
    /*
    加载视频    没测试过
    String filePath = "/storage/emulated/0/Pictures/example_video.mp4";
    Glide.with( context )
    .load( Uri.fromFile( new File( filePath ) ) )
    .into( imageViewGifAsBitmap );
     */

    //清理磁盘缓存
    public static void GuideClearDiskCache(Context mContext) {
        //理磁盘缓存 需要在子线程中执行
        Glide.get(mContext).clearDiskCache();
    }

    //清理内存缓存
    public static void GuideClearMemory(Context mContext) {
        //清理内存缓存  可以在UI主线程中进行
        Glide.get(mContext).clearMemory();
    }
}
