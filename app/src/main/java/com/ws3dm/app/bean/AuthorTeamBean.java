package com.ws3dm.app.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Describution :作者团队bean
 * 
 * Author : DKjuan
 * 
 * Date : 2018/4/13 11:37
 **/

public class AuthorTeamBean implements Serializable {
    /**
     * id : 36
     * nickname : 吼狸社
     * litpic : https://work.3dmgame.com/uploads/images/users/20190513/1557714265_524711.jpg
     * desc : 为了玩家的欢呼
     * showtype : 16
     * newslist : [{"aid":"3741888","arcurl":"https://www.3dmgame.com/original/3741888.html","title":"是什么神仙能把世界第一的玩家打入败者组？∣吼狸社解说","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20190808/1565267173_645111.jpg","pubdate_at":1565267345,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3741888.html"}]
     *
     * aid : 5
     * name : 我是老瓜皮
     * total_pub : 39
     "avatarstr": "https://work.3dmgame.com/uploads/images/users/200.jpg",
     */

    private int id;
    private String nickname;
    private int aid;
    private String name;
    private String litpic;
    private String desc;
    private int showtype;
    private int author_id;
    private String total_pub;
    private String avatarstr;
    private OriginalBean newslist;

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLitpic() {
        return litpic;
    }

    public void setLitpic(String litpic) {
        this.litpic = litpic;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getShowtype() {
        return showtype;
    }

    public void setShowtype(int showtype) {
        this.showtype = showtype;
    }

    public String getTotal_pub() {
        return total_pub;
    }

    public void setTotal_pub(String total_pub) {
        this.total_pub = total_pub;
    }

    public String getAvatarstr() {
        return avatarstr;
    }

    public void setAvatarstr(String avatarstr) {
        this.avatarstr = avatarstr;
    }

    public OriginalBean getNewslist() {
        return newslist;
    }

    public void setNewslist(OriginalBean newslist) {
        this.newslist = newslist;
    }
}
