package com.ws3dm.app.bean;

import java.io.Serializable;

/**
 * Created by lc on 2017/3/21.
 */

public class GameHotPhb implements Serializable {
    /**
     * num : 1
     * aid : 63
     * arcurl : https://ol.3dmgame.com/lostark/
     * litpic : https://ol.3dmgame.com/uploads/images/thumbbig/20181119/1542621564_432879.jpg
     * showtype : 21
     * title : 失落的方舟
     * webviewurl : 
     * betatime : 2018-11-07
     * state : 限量测试
     * firm : 腾讯游戏
     * score : 8.5
     * 
     * type : 动作
     * label : 冒险 竞技 魔幻
     */
    private int num;
    private int aid;
    private String arcurl;
    private String litpic;
    private int showtype;
    private String title;
    private String webviewurl;
    private String type;
    private String betatime;
    private String state;
    private String firm;
    private double score;
    private String label;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
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

    public String getBetatime() {
        return betatime;
    }

    public void setBetatime(String betatime) {
        this.betatime = betatime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFirm() {
        return firm;
    }

    public void setFirm(String firm) {
        this.firm = firm;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
