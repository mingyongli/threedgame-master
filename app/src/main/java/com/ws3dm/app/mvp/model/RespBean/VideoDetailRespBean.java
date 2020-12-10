package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.Video;

import java.util.List;

/**
 * Author : DKjuan:新闻视频页分类列表
 * <p>
 * Date :  2017/8/21  12:06
 */
public class VideoDetailRespBean extends BaseRespBean<Video>{

	/**
	 * video : [{"id":"3689153","title":"痛苦之地 #3 恐怖游戏实况攻略解说1080P","click":"0","feedback":"0","senddate":"6天前","videopic":"http://aimg.3dmgame.com/uploads/video/201709/r_1506021583Qnqg.jpg","videourl":"http://v.3dmgame.com/201709/3689153_app.html","changyan_id":"news_3689153","description":"痛苦之地 #3 丨 恐怖游戏实况攻略解说1080P 丨 死亡列车之没有终点站 丨第三期","lmfl":"视频"},{"id":"3687487","title":"痛苦之地视频","click":"4","feedback":"0","senddate":"13天前","videopic":"http://aimg.3dmgame.com/uploads/video/201709/r_1505404607RjGv.jpg","videourl":"http://v.3dmgame.com/201709/3687487_app.html","changyan_id":"news_3687487","description":"","lmfl":"视频"},{"id":"3684595","title":"《痛苦之地》全中文游戏最新预告片(第一版)","click":"3","feedback":"0","senddate":"24天前","videopic":"http://aimg.3dmgame.com/uploads/video/201709/r_15044277515UPv.jpg","videourl":"http://v.3dmgame.com/201709/3684595_app.html","changyan_id":"news_3684595","description":"","lmfl":"视频"},{"id":"3680682","title":"恐怖新作《痛苦之地》新预告","click":"13","feedback":"0","senddate":"2017-08-17","videopic":"http://aimg.3dmgame.com/templets/3dmwap2/images/no-litpic.jpg","videourl":"http://v.3dmgame.com/201708/3680682_app.html","changyan_id":"news_3680682","description":"怎么感觉还是恐怖游戏","lmfl":"视频"},{"id":"3636377","title":"《无主之地3》的缩影：无主之地 虚幻4技术Demo","click":"25","feedback":"0","senddate":"2017-03-02","videopic":"http://aimg.3dmgame.com/uploads/video/201703/r_1488439736yNca.jpg","videourl":"http://v.3dmgame.com/201703/3636377_app.html","changyan_id":"news_3636377","description":"","lmfl":"视频"}]
	 * totalrow : 500
	 */

	private int totalrow;
	private List<Video> video;

	public int getTotalrow() {
		return totalrow;
	}

	public void setTotalrow(int totalrow) {
		this.totalrow = totalrow;
	}

	public List<Video> getVideo() {
		return video;
	}

	public void setVideo(List<Video> video) {
		this.video = video;
	}
}
