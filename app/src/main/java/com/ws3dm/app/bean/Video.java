package com.ws3dm.app.bean;

import java.io.Serializable;

/**
 * Created by lc on 2017/3/22.
 */

public class Video implements Serializable {

    /**
     * "typeid": "211",
     * id : 3685145
     * title : 《黑暗与光明》小科普之魔法篇
     * click : 39
     * feedback : 0
     * senddate : 54分钟前
     * videopic : http://aimg.3dmgame.com/uploads/170905/383-1FZ51F9124S.jpg
     * videourl : http://v.3dmgame.com/201709/3685145_app.html
     * changyan_id : news_3685145
     * description : 魔幻沙盒生存游戏《黑暗与光明》中着丰富多样的魔法，本期黑光小科普为大家介绍了法杖类魔法，包括火系、冰系、电系、法师之手等游戏中常用的法杖类法术，还有萌萌的变羊术，能把恶狼
     * lmfl : 视频
     */

    private String typeid;
    private String id;
    private String title;
    private String click;
    private String feedback;
    private String senddate;
    private String videopic;
    private String videourl;
    private String changyan_id;
    private String description;
    private String lmfl;

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClick() {
        return click;
    }

    public void setClick(String click) {
        this.click = click;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getSenddate() {
        return senddate;
    }

    public void setSenddate(String senddate) {
        this.senddate = senddate;
    }

    public String getVideopic() {
        return videopic;
    }

    public void setVideopic(String videopic) {
        this.videopic = videopic;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public String getChangyan_id() {
        return changyan_id;
    }

    public void setChangyan_id(String changyan_id) {
        this.changyan_id = changyan_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLmfl() {
        return lmfl;
    }

    public void setLmfl(String lmfl) {
        this.lmfl = lmfl;
    }
}
