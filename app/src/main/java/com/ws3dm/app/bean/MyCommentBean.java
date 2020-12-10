package com.ws3dm.app.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Describution : 我的评论 bean
 * 
 * Author : DKjuan
 * 
 * Date : 2018/3/27 9:54
 **/
public class MyCommentBean implements Serializable {
	/**
	 * order_num : 8
	 * id : 412
	 * content : 路口来咯
	 * aid : 74343
	 * arcurl : http://shouyou2.3dmgame.com/android/74343.html
	 * title : 骰子格斗：首领联盟
	 * showtype : 4
	 * total_ct : 1
	 * pubdate_at : 1527752712
	 * has_reply : 0
	 * webviewurl : http://shouyou2.3dmgame.com/webview/android/74343.html
	 */

	private int order_num;
	private int id;
	private String content;
	private int aid;
	private String arcurl;
	private String title;
	private int showtype;
	private int total_ct;
	private int pubdate_at;
	private int has_reply;
	private String webviewurl;

	public int getOrder_num() {
		return order_num;
	}

	public void setOrder_num(int order_num) {
		this.order_num = order_num;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getAid() {
		return aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}

	public String getArcurl() {
		return arcurl;
	}

	public void setArcurl(String arcurl) {
		this.arcurl = arcurl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getShowtype() {
		return showtype;
	}

	public void setShowtype(int showtype) {
		this.showtype = showtype;
	}

	public int getTotal_ct() {
		return total_ct;
	}

	public void setTotal_ct(int total_ct) {
		this.total_ct = total_ct;
	}

	public int getPubdate_at() {
		return pubdate_at;
	}

	public void setPubdate_at(int pubdate_at) {
		this.pubdate_at = pubdate_at;
	}

	public int getHas_reply() {
		return has_reply;
	}

	public void setHas_reply(int has_reply) {
		this.has_reply = has_reply;
	}

	public String getWebviewurl() {
		return webviewurl;
	}

	public void setWebviewurl(String webviewurl) {
		this.webviewurl = webviewurl;
	}
}