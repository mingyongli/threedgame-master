package com.ws3dm.app.bean;

/**
 * Created by lc on 2017/3/20.
 */

public class NewsBar {
    /**
     * appid : 1
     * title : 最新
     * type : 0
     */
    private String appid;
    private String title;
    private String type;
    private String fid;
    private String name;
    private String is_ty;

    public String getIs_ty() {
        return is_ty;
    }

    public void setIs_ty(String is_ty) {
        this.is_ty = is_ty;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
