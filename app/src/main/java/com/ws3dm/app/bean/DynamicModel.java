package com.ws3dm.app.bean;

public class DynamicModel {
    //评论编号
    private int id;
    //评论文章的编号
    private int aid;
    //文章地址
    private String arcurl;
    //备注展示类型:1新闻2攻略3单机专区4手游下载5杂谈6评测7原创
    // 8安利9礼包10视频11专栏12节目13投票14专题h5链接
    // 15h5游戏16原创作者17单机专区攻略18手游专区攻略
    // 19网游专区攻略20手游专区21网游专区22网游礼包23网游电竞专题
    private String showtype;
    //文章标题
    private String title;
    //评论内容
    private String content;
    //当前评论的总点赞数
    private int goodcount;
    //发布时间戳
    private long pubdate_at;
    //webview的链接地址
    private String webviewurl;
    //会员编号
    private int uid;
    //会员昵称
    private String nickname;

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

    public String getShowtype() {
        return showtype;
    }

    public void setShowtype(String showtype) {
        this.showtype = showtype;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getGoodcount() {
        return goodcount;
    }

    public void setGoodcount(int goodcount) {
        this.goodcount = goodcount;
    }

    public long getPubdate_at() {
        return pubdate_at;
    }

    public void setPubdate_at(long pubdate_at) {
        this.pubdate_at = pubdate_at;
    }

    public String getWebviewurl() {
        return webviewurl;
    }

    public void setWebviewurl(String webviewurl) {
        this.webviewurl = webviewurl;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
