package com.ws3dm.app.bean;

import java.io.Serializable;

/**
 * Author : DKjuan:
 * <p>
 * Date :  2018/4/12  17:13
 */
public class GonglueBean implements Serializable {
	/**
	 * aid : 102134
	 * title : 炼狱茨木童子
	 * litpic : https://shouyou.3dmgame.com/uploadimg/img/2018/1113/1542078565167747.jpg
	 * arcurl : https://shouyou.3dmgame.com/gl/102134.html
	 * webviewurl : https://app.3dmgame.com/webview/gl/102134.html
	 * showtype : 2
	 */

	private int aid;
	private String title;
	private String litpic;
	private String arcurl;
	private String webviewurl;
	private int showtype;

	public int getAid() {
		return aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLitpic() {
		return litpic;
	}

	public void setLitpic(String litpic) {
		this.litpic = litpic;
	}

	public String getArcurl() {
		return arcurl;
	}

	public void setArcurl(String arcurl) {
		this.arcurl = arcurl;
	}

	public String getWebviewurl() {
		return webviewurl;
	}

	public void setWebviewurl(String webviewurl) {
		this.webviewurl = webviewurl;
	}

	public int getShowtype() {
		return showtype;
	}

	public void setShowtype(int showtype) {
		this.showtype = showtype;
	}
}
