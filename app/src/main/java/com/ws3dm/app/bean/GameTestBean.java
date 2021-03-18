package com.ws3dm.app.bean;

import java.io.Serializable;

/**
 * Created by lc on 2017/3/21.
 */

public class GameTestBean implements Serializable {//游戏-网游 开测实体和Slide共用
    /**
     * aid : 23
     * arcurl : https://ol.3dmgame.com/esports/kpl/
     * litpic : https://ol.3dmgame.com/uploads/images/thumbgamezt/20191017/1571275364_502501.png
     * showtype : 23
     * title : 王者荣耀KPL赛事专题
     * webviewurl : https://m.3dmgame.com/ol/webview/esports/kpl/
     * <p>
     * state : 新服公测
     * pubdate_at : 1573056000
     * type :
     * firm : 完美世界
     * score : 0
     */
    private int aid;
    private int id;
    private String arcurl;
    private String litpic;
    private int showtype;
    private String title;
    private String webviewurl;
    private String type;
    private String firm;
    private String state;
    private int pubdate_at;
    private double score;
    private String reward;
    private int http;
    private String place;
    private int start_date;
    private int end_date;

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

    public String getLitpic() {
        return litpic;
    }

    public void setLitpic(String litpic) {
        this.litpic = litpic;
    }

    public int getShowtype() {
        return showtype;
    }

    public void setShowtype(int showtype) {
        this.showtype = showtype;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWebviewurl() {
        return webviewurl;
    }

    public void setWebviewurl(String webviewurl) {
        this.webviewurl = webviewurl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFirm() {
        return firm;
    }

    public void setFirm(String firm) {
        this.firm = firm;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getPubdate_at() {
        return pubdate_at;
    }

    public void setPubdate_at(int pubdate_at) {
        this.pubdate_at = pubdate_at;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getStart_date() {
        return start_date;
    }

    public void setStart_date(int start_date) {
        this.start_date = start_date;
    }

    public int getEnd_date() {
        return end_date;
    }

    public void setEnd_date(int end_date) {
        this.end_date = end_date;
    }
}
