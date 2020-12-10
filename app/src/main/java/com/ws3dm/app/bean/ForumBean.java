package com.ws3dm.app.bean;

import java.io.Serializable;

/**
 * Describution :论坛bean
 * 
 * Author : DKjuan
 * 
 * Date : 2018/3/28 18:30
 **/

public class ForumBean implements Serializable {

    /**
     * fid : 685
     * arcurl : http://bbs.3dmgame.com/forum-685-1.html
     * title : 《上古卷轴5：天际》
     * threads : 339812
     * posts : 16083906
     * todayposts : 740
     * yesterdayposts : 1520
     * rank : 5
     * isfavorite : 0
     * litpic : http://att.3dmgame.com/att/common/33/common_685_icon.gif
     */

    private int fid;
    private String arcurl;
    private String title;
    private String threads;
    private String posts;
    private String todayposts;
    private String yesterdayposts;
    private String rank;
    private int isfavorite;
    private String litpic;

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
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

    public String getThreads() {
        return threads;
    }

    public void setThreads(String threads) {
        this.threads = threads;
    }

    public String getPosts() {
        return posts;
    }

    public void setPosts(String posts) {
        this.posts = posts;
    }

    public String getTodayposts() {
        return todayposts;
    }

    public void setTodayposts(String todayposts) {
        this.todayposts = todayposts;
    }

    public String getYesterdayposts() {
        return yesterdayposts;
    }

    public void setYesterdayposts(String yesterdayposts) {
        this.yesterdayposts = yesterdayposts;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public int getIsfavorite() {//0未收藏1已收藏
        return isfavorite;
    }

    public void setIsfavorite(int isfavorite) {
        this.isfavorite = isfavorite;
    }

    public String getLitpic() {
        return litpic;
    }

    public void setLitpic(String litpic) {
        this.litpic = litpic;
    }
}
