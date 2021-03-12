package com.ws3dm.app.bean;

import com.ws3dm.app.mvp.model.RespBean.ForumTopRespBean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/26.
 */

public class ForumDetailBean implements Serializable {
    /**
     * fid : 18
     * tid : 5723178
     * arcurl : http://bbs.3dmgame.com/thread-5723178-1.html
     * title : 【3DMGAME新闻中心】招聘值班编辑
     * pubdate_at : 1521706238
     * replies : 5
     * views : 4575
     * type : 公告
     * user : {"uid":"1056597","nickname":"NT","avatarstr":"http://user.3dmgame.com/data/avatar/001/05/65/97_avatar_small.jpg"}
     */

    private String fid;
    private String tid;
    private String arcurl;
    private String title;
    private long pubdate_at;
    private String replies;
    private String views;
    private String type;
    private String webviewurl;
    private String webviewnewurl;

    public String getWebviewnewurl() {
        return webviewnewurl;
    }

    public void setWebviewnewurl(String webviewnewurl) {
        this.webviewnewurl = webviewnewurl;
    }

    private UserDataBean user;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
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

    public long getPubdate_at() {
        return pubdate_at;
    }

    public void setPubdate_at(long pubdate_at) {
        this.pubdate_at = pubdate_at;
    }

    public String getReplies() {
        return replies;
    }

    public void setReplies(String replies) {
        this.replies = replies;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWebviewurl() {
        return webviewurl;
    }

    public void setWebviewurl(String webviewurl) {
        this.webviewurl = webviewurl;
    }

    public UserDataBean getUser() {
        return user;
    }

    public void setUser(UserDataBean user) {
        this.user = user;
    }
}
