package com.ws3dm.app.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Describution :原创实体类
 * 
 * Author : DKjuan
 * 
 * Date : 2018/4/13 11:37
 **/

public class OriginalBean implements Serializable {
    /**
     * aid : 3741888
     * arcurl : https://www.3dmgame.com/original/3741888.html
     * title : 是什么神仙能把世界第一的玩家打入败者组？∣吼狸社解说
     * litpic : https://img.3dmgame.com/uploads/images/thumborigfirst/20190808/1565267173_645111.jpg
     * pubdate_at : 1565267345
     * showtype : 7
     * webviewurl : https://m.3dmgame.com/webview/original/3741888.html
     * label : [{"id":"32","name":"DM杂谈"}]
     * user : {"id":12,"nickname":"ChunTian","avatarstr":"http://work.3dmgame.com/uploads/images/users/12.jpg"}
     * 
     "bigpic": "https://img.3dmgame.com/uploads/images/thumborigfirst/20190815/1565838721_715985.jpg", 
     "is_showbig": 1, 
     "total_ct": 1, 
     "type": "原创", 
     * score : 8
     */

    private int aid;
    private String arcurl;
    private String title;
    private String litpic;
    private String bigpic;
    private int is_showbig;
    private int showtype;
    private int total_ct;
    private int pubdate_at;
    private double score;
    private String webviewurl;
    private UserDataBean user;
    private String type;
    private List<AvatarBean> label;

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

    public String getBigpic() {
        return bigpic;
    }

    public void setBigpic(String bigpic) {
        this.bigpic = bigpic;
    }

    public int getIs_showbig() {
        return is_showbig;
    }

    public void setIs_showbig(int is_showbig) {
        this.is_showbig = is_showbig;
    }

    public int getShowtype() {
        return showtype;
    }

    public void setShowtype(int showtype) {
        this.showtype = showtype;
    }

    public int getTotal_ct() {
        return total_ct;
    }

    public void setTotal_ct(int total_ct) {
        this.total_ct = total_ct;
    }

    public int getPubdate_at() {
        return pubdate_at;
    }

    public void setPubdate_at(int pubdate_at) {
        this.pubdate_at = pubdate_at;
    }

    public String getWebviewurl() {
        return webviewurl;
    }

    public void setWebviewurl(String webviewurl) {
        this.webviewurl = webviewurl;
    }

    public UserDataBean getUser() {
        return user;
    }

    public void setUser(UserDataBean user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<AvatarBean> getLabel() {
        return label;
    }

    public void setLabel(List<AvatarBean> label) {
        this.label = label;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
