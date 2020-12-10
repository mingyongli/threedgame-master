package com.ws3dm.app.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Author : DKjuan:
 * <p>
 * Date :  2018/4/3  14:02
 */
public class RepliesBean implements Serializable {

	/**
	 * pid : 189136119
	 * position : 5
	 * pubdate_at : 1492082472
	 * content : 这~~怎么没人分享啊
	 * user : {"uid":6892446,"nickname":"无尽的灾难","avatarstr":"http://user.3dmgame.com/data/avatar/006/89/24/46_avatar_small.jpg"}
	 * replies : [{"pid":189135538,"content":"有重制版的。。。http://www.nexusmods.com/skyrimspecialedition/mods/7897/?","pubdate_at":1492081980,"user":{"nickname":"言小夏"}}]
	 */

	private int pid;
	private String type;//最新评论，热门评论，全部评论
	private int position;
	private int pubdate_at;
	private String content;
	private UserDataBean user;
	private List<RepliesBean> replies;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getPubdate_at() {
		return pubdate_at;
	}

	public void setPubdate_at(int pubdate_at) {
		this.pubdate_at = pubdate_at;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public UserDataBean getUser() {
		return user;
	}

	public void setUser(UserDataBean user) {
		this.user = user;
	}

	public List<RepliesBean> getReplies() {
		return replies;
	}

	public void setReplies(List<RepliesBean> replies) {
		this.replies = replies;
	}
}
