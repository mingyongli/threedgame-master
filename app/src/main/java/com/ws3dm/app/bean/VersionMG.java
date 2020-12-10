package com.ws3dm.app.bean;

import java.io.Serializable;

/**
 * Describution :手游详情里面的其他版本，礼包
 * 
 * Author : DKjuan
 * 
 * Date : 2018/4/26 15:29
 **/
public class VersionMG implements Serializable {
    private String aid;
    private String arcurl;
    private String title;
    private int showtype;//备注展示类型:1新闻2攻略3游戏4手游5杂谈6评测7原创8安利9礼包10视频11专栏

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
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
}
