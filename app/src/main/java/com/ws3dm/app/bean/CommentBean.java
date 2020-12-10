package com.ws3dm.app.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Describution : 评论实体
 * 
 * Author : DKjuan
 * 
 * Date : 2018/3/27 9:54
 **/
public class CommentBean implements Serializable {
	/**
	 * id : 32
	 * order_num : 32
	 * content : 《英雄联盟》12月8日免费英雄更换《英雄联盟》12月8日免费英雄更换《英雄联盟》12月8日免费英雄更换《英雄联盟》12月8日免费英雄更换《英雄联盟》12月8日免费
	 * position : 3
	 * goodcount : 0
	 * badcount : 0
	 * pubdate_at : 1522410082
	 * user : {"uid":9663679,"nickname":"3DM用户","avatarstr":"http://my.3dmgame.com/uploads/images/avatar/default.jpg","gender":1,"regionstr":"上海宝山区"}
	 * replies : [{"id":14,"position":1,"goodcount":0,"badcount":0,"pubdate_at":1512633145,"user":{"uid":9663679,"nickname":"3DM用户","avatarstr":"http://my.3dmgame.com/uploads/images/avatar/default.jpg","gender":1,"regionstr":"上海宝山区"},"content":"《英雄联盟》12月8日免费英雄更换《英雄联盟》12月8日免费英雄更换《英雄联盟》12月8日免费英雄更换《英雄联盟》12月8日免费英雄更换《英雄联盟》12月8日免费英雄更换","praise":0}]
	 * praise : 0
	 */

	private int id;
	private int order_num;//序号
	private String type;//最新评论，热门评论，全部评论
	private String title;
	private String content;
	private int position;
	private int goodcount;
	private int badcount;
	private int total_ct;//当前文章总评论数
	private int pubdate_at;
	private UserDataBean user;
	private int is_follow;
	private int praise;
	private List<CommentBean> replies;


	public int getIs_follow() {
		return is_follow;
	}

	public void setIs_follow(int is_follow) {
		this.is_follow = is_follow;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getGoodcount() {
		return goodcount;
	}

	public void setGoodcount(int goodcount) {
		this.goodcount = goodcount;
	}

	public int getBadcount() {
		return badcount;
	}

	public void setBadcount(int badcount) {
		this.badcount = badcount;
	}

	public int getPubdate_at() {
		return pubdate_at;
	}

	public void setPubdate_at(int pubdate_at) {
		this.pubdate_at = pubdate_at;
	}

	public UserDataBean getUser() {
		return user;
	}

	public void setUser(UserDataBean user) {
		this.user = user;
	}

	public int getPraise() {
		return praise;
	}

	public void setPraise(int praise) {
		this.praise = praise;
	}

	public List<CommentBean> getReplies() {
		return replies;
	}

	public void setReplies(List<CommentBean> replies) {
		this.replies = replies;
	}

	public int getOrder_num() {
		return order_num;
	}

	public void setOrder_num(int order_num) {
		this.order_num = order_num;
	}

	public int getTotal_ct() {
		return total_ct;
	}

	public void setTotal_ct(int total_ct) {
		this.total_ct = total_ct;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}