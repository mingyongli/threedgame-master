package com.ws3dm.app.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Describution :搜索
 * 
 * Author : DKjuan
 * 
 * Date : 2018/4/13 11:37
 **/

public class SearchBean implements Serializable {
    private int aid;
    private int fid;
    private int threads;//当前板块的发主题数
    private int posts;//总回帖数
    private int todayposts;//今天总回帖数
    private String arcurl;
    private String title;
    private String litpic;
    private String system;
    private String type;
    private double score;
    private int showtype;
    private long pubdate_at;
    private List<AvatarBean> labels;
    private int total_ct;
    private String webviewurl;

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }

    public int getTodayposts() {
        return todayposts;
    }

    public void setTodayposts(int todayposts) {
        this.todayposts = todayposts;
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

    public String getLitpic() {
        return litpic;
    }

    public void setLitpic(String litpic) {
        this.litpic = litpic;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getShowtype() {
        return showtype;
    }

    public void setShowtype(int showtype) {
        this.showtype = showtype;
    }

    public long getPubdate_at() {
        return pubdate_at;
    }

    public void setPubdate_at(long pubdate_at) {
        this.pubdate_at = pubdate_at;
    }

    public List<AvatarBean> getLabels() {
        return labels;
    }

    public void setLabels(List<AvatarBean> labels) {
        this.labels = labels;
    }

    public int getTotal_ct() {
        return total_ct;
    }

    public void setTotal_ct(int total_ct) {
        this.total_ct = total_ct;
    }

    public String getWebviewurl() {
        return webviewurl;
    }

    public void setWebviewurl(String webviewurl) {
        this.webviewurl = webviewurl;
    }

    public String getLabelString(){
        if(labels==null)
            return "";
        String str_label="";
        for(AvatarBean bean:labels){
            str_label=str_label+bean.getName()+" ";
        }
        return str_label;
    }
}
