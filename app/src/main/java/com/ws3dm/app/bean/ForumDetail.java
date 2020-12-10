package com.ws3dm.app.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/26.
 */

public class ForumDetail implements Serializable {

    /**
     * tid : 5524844
     * author : cdq55555
     * authorid : 7371177
     * subject : 【3DM哗哗MOD汉化组】Hammerfell Armory SE-落锤军械库v1.0
     * dateline : 2017-02-21
     * highlight : 0
     * digest : 0
     * rate : 1
     * heats : 118
     * comments : 0
     * authimg : http://user.3dmgame.com/avatar.php?uid=7371177&size=small
     */

    private String tid;
    private String author;
    private String authorid;
    private String subject;
    private String dateline;
    private String highlight;
    private String digest;
    private String rate;
    private String heats;
    private String comments;
    private String authimg;
    private String arttype;
    private String imgUrl;
    private String displayorder;
    private ArrayList<String> litpic;

    public ForumDetail() {
    }

    public ForumDetail(String author, String authimg, String subject, String comments, String arttype, String heats, String dateline) {
        this.author = author;
        this.authimg = authimg;
        this.subject = subject;
        this.comments = comments;
        this.arttype = arttype;
        this.heats = heats;
        this.dateline = dateline;
    }

    public String getDisplayorder() {
        return displayorder;
    }

    public void setDisplayorder(String displayorder) {
        this.displayorder = displayorder;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getArttype() {
        return arttype;
    }

    public void setArttype(String arttype) {
        this.arttype = arttype;
    }

    public List<String> getLitpic() {
        return litpic;
    }

    public void setLitpic(ArrayList<String> litpic) {
        this.litpic = litpic;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorid() {
        return authorid;
    }

    public void setAuthorid(String authorid) {
        this.authorid = authorid;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getHeats() {
        return heats;
    }

    public void setHeats(String heats) {
        this.heats = heats;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAuthimg() {
        return authimg;
    }

    public void setAuthimg(String authimg) {
        this.authimg = authimg;
    }
}
