package com.ws3dm.app.bean;

import java.io.Serializable;

/**
 * Describution :论坛分类bean
 * 
 * Author : DKjuan
 * 
 * Date : 2018/3/28 18:30
 **/

public class ForumGidBean implements Serializable {

    /**
     * gid : 437
     * title : 自营H5游戏
     * forumscount : 16
     */

    private String gid;
    private String title;
    private int forumscount;

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getForumscount() {
        return forumscount;
    }

    public void setForumscount(int forumscount) {
        this.forumscount = forumscount;
    }

    /**
     * typeid : 31452
     * name : 原创
     */

    private String typeid;
    private String name;

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
