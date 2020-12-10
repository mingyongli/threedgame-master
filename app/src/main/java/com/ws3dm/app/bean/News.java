package com.ws3dm.app.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lc on 2017/3/16.
 */

public class News implements Serializable {

    /**
     * arcurl : http://m.3dmgame.com/events/201701/3624645.html
     * title : 快播正式回归！熟悉的味道但已不是原来的配方
     * senddate : 1484662619
     * feedback : 0
     * id : 3624645
     * arttype : 2
     * litpic : ["/uploads/allimg/170117/276_170117221002_1_lit.jpg","/uploads/allimg/170117/276_170117221027_1_lit.jpg","/uploads/allimg/170117/276_170117221123_1_lit.jpg","/uploads/allimg/170117/276_170117221138_1_lit.jpg","/uploads/allimg/170117/276_170117221152_1_lit.jpg","/uploads/allimg/170117/276_170117221210_1_lit.jpg","/uploads/allimg/170117/276_170117221232_1_lit.jpg","/uploads/allimg/170117/276_170117221246_1_lit.jpg"]
     */

    private String arcurl;
    private String title;
    private String senddate;
    private String feedback;
    private String id;
    private String arttype;
    private List<String> litpic;
    private List<News> listBanner;
    private String imgUrl;
    private String count;
    private String page;
    private String cyId;
    private String description;
    private String lmfl;
    private String appid;
    private int total;
    private String changyan_id;
    private String typeid;
    
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getLmfl() {
        return lmfl;
    }

    public void setLmfl(String lmfl) {
        this.lmfl = lmfl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCyId() {
        return cyId;
    }

    public void setCyId(String cyId) {
        this.cyId = cyId;
    }

    public String getChangyan_id() {
        return changyan_id;
    }

    public void setChangyan_id(String changyan_id) {
        this.changyan_id = changyan_id;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public List<News> getListBanner() {
        return listBanner;
    }

    public void setListBanner(List<News> listBanner) {
        this.listBanner = listBanner;
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

    public String getTime() {
        return senddate;
    }

    public void setTime(String senddate) {
        this.senddate = senddate;
    }

    public String getComment() {
        return feedback;
    }

    public void setComment(String feedback) {
        this.feedback = feedback;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return arttype;
    }

    public void setType(String arttype) {
        this.arttype = arttype;
    }

    public List<String> getPhoto() {
        return litpic;
    }

    public void setPhoto(List<String> litpic) {
        this.litpic = litpic;
    }
}
