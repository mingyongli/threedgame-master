package com.ws3dm.app.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Describution :视频和新闻共用一个实体
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2018/4/13 11:37
 **/

public class NewsBean implements Serializable {

    /**
     * id : 11
     * aid : 10047
     * arcurl : http://www2.3dmgame.com/gl/10047.html
     * title : 《孤岛惊魂5》再来一篇很屌的攻略看看
     * litpic : http://www2.3dmgame.com/uploads/images/thumbnews/20180411/1523432485_197451.jpg
     * showtype : 2
     * click : 3
     * total_ct : 0
     * pubdate_at : 1523432490
     * webviewurl : http://www2.3dmgame.com/app/gl/10047.html
     * user : {"id":12,"nickname":"ChunTian","avatarstr":"http://work.3dmgame.com/uploads/images/users/12.jpg"}
     * "type": "安利"
     * "bodyimg": ["https://img.3dmgame.com/uploads/images/news/20181103/1541212934_104716.png","https://img.3dmgame.com/uploads/images/news/20181103/1541212942_298282.png","https://img.3dmgame.com/uploads/images/news/20181103/1541212943_412469.png"]
     */

    private int id;
    private int aid;
    private String arcurl;
    private String title;
    private String litpic;
    private int showtype;
    private int click;
    private int total_ct;
    private int pubdate_at;
    private String webviewurl;
    private String type;
    private String seeDate;//浏览时间
    private UserDataBean user;
    private List<AvatarBean> typelist;
    private int havesee;//0未看过，1已看过
    private String bt_title;
    private String detail_title;
    private List<String> bodyimg;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getShowtype() {
        return showtype;
    }

    public void setShowtype(int showtype) {
        this.showtype = showtype;
    }

    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
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

    public String getSeeDate() {
        return seeDate;
    }

    public void setSeeDate(String seeDate) {
        this.seeDate = seeDate;
    }

    public UserDataBean getUser() {
        return user;
    }

    public void setUser(UserDataBean user) {
        this.user = user;
    }

    public List<String> getBodyimg() {
        return bodyimg;
    }

    public void setBodyimg(List<String> bodyimg) {
        this.bodyimg = bodyimg;
    }

    public int getHavesee() {
        return havesee;
    }

    public void setHavesee(int havesee) {
        this.havesee = havesee;
    }

    public String getBt_title() {
        return bt_title;
    }

    public void setBt_title(String bt_title) {
        this.bt_title = bt_title;
    }

    public String getDetail_title() {
        return detail_title;
    }

    public void setDetail_title(String detail_title) {
        this.detail_title = detail_title;
    }

    public List<AvatarBean> getTypelist() {
        return typelist;
    }

    public void setTypelist(List<AvatarBean> typelist) {
        this.typelist = typelist;
    }
}
