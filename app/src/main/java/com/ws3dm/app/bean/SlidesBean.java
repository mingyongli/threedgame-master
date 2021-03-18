package com.ws3dm.app.bean;

import java.io.Serializable;

/**
 * Author : DKjuan:
 * <p>
 * Date :  2018/4/12  17:13
 */
public class SlidesBean implements Serializable {
    /**
     * aid : 1
     * arcurl : http://www2.3dmgame.com/news/201804/10018.html
     * litpic : /uploads/images/thumbnews/20180408/1523196114_567513.jpg
     * title : 3DM汉化组制作《拳皇97：全球对决》汉化补丁发布
     * showtype : 1
     * webviewurl : http://www2.3dmgame.com/app/news/201804/10018.html
     * system : PC PS4 Win Mixed Reality PSV
     * score : 0
     * pubdate_at : 1522984045
     * desc": "ChunTian"
     */

    private int aid;
    private int id;
    private String arcurl;
    private String litpic;
    private String title;
    private String showtype;
    private String webviewurl;
    private String system;
    private String desc;
    private double score;
    private int http;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHttp() {
        return http;
    }

    public void setHttp(int http) {
        this.http = http;
    }

    private long pubdate_at;

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getArcurl() {
        return arcurl;
    }

    public void setArcurl(String arcurl) {
        this.arcurl = arcurl;
    }

    public String getLitpic() {
        return litpic;
    }

    public void setLitpic(String litpic) {
        this.litpic = litpic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShowtype() {
        return showtype;
    }

    public void setShowtype(String showtype) {
        this.showtype = showtype;
    }

    public String getWebviewurl() {
        return webviewurl;
    }

    public void setWebviewurl(String webviewurl) {
        this.webviewurl = webviewurl;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public long getPubdate_at() {
        return pubdate_at;
    }

    public void setPubdate_at(long pubdate_at) {
        this.pubdate_at = pubdate_at;
    }
}
