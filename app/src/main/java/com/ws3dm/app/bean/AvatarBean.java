package com.ws3dm.app.bean;

import java.io.Serializable;

/**
 * Author : DKjuan: 头像bean,游戏分类，单个游戏里面的标签,游戏配置信息,手游分类标签
 * <p>
 * Date :  2018/3/26  10:33
 */
public class AvatarBean implements Serializable {
    /**
     * id : 1
     * url : /uploads/images/avatar/20171202/1512217747_855103.jpg
     * litpic
     * "title": "武侠"
     * "name": "武侠"
     * content : Windows 7 64bit or better
     */

    private String id;
    private String url;
    private int Aid;
    private String litpic;
    private String title;
    private String name;
    private String content;
    private int pubdate_at;

    public int getPubdate_at() {
        return pubdate_at;
    }

    public void setPubdate_at(int pubdate_at) {
        this.pubdate_at = pubdate_at;
    }

    public int getAid() {
        return Aid;
    }

    public void setAid(int aid) {
        Aid = aid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLitpic() {
        return litpic;
    }

    public void setLitpic(String litpic) {
        this.litpic = litpic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public AvatarBean() {
        this.name = name;
    }

    public AvatarBean(String name) {
        this.name = name;
    }
}