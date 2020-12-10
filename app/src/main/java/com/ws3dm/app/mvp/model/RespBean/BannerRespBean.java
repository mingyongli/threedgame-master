package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.NewsBar;

import java.util.List;

/**
 * Author : DKjuan:
 * <p>
 * Date :  2017/8/18  14:57
 */
public class BannerRespBean extends BaseRespBean<NewsBar>{

	/**
	 * code : 1
	 * channels_version : 1.0
	 * html : [{"appid":"1","title":"最新","type":"0"},{"appid":"3","title":"游戏","type":"0"},{"appid":"4","title":"热点","type":"0"},{"appid":"2","title":"视频","type":"1"},{"appid":"5","title":"杂谈","type":"0"},{"appid":"6","title":"评测","type":"0"},{"appid":"7","title":"原创","type":"0"},{"appid":"8","title":"前瞻","type":"0"},{"appid":"9","title":"盘点","type":"0"},{"appid":"10","title":"时事","type":"0"},{"appid":"11","title":"发售","type":"0"},{"appid":"12","title":"汉化","type":"0"},{"appid":"13","title":"攻略","type":"0"}]
	 */

	private String channels_version;
	private List<NewsBar> html;

	public String getChannels_version() {
		return channels_version;
	}

	public void setChannels_version(String channels_version) {
		this.channels_version = channels_version;
	}

	public List<NewsBar> getHtml() {
		return html;
	}

	public void setHtml(List<NewsBar> html) {
		this.html = html;
	}
}
