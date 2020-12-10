package com.ws3dm.app.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lc on 2017/3/21.
 */

public class GameBean implements Serializable {
    /**
     * aid : 1
     * arcurl : http://www2.3dmgame.com/games/origin/
     * title : 刺客信条：起源_专题
     * litpic : http://www2.3dmgame.com/uploads/images/thumbkwdfirst/20171228/1514455185_106155.png
     * click : 27
     * system : PC XBOX ONE PS4
     * score : 9.1
     * plpeople : 100
     * pubdate_at : 1516982400
     * typeid : 3
     * type : 冒险
     * made : Ubisoft Montrea
     * publisher : Ubisoft
     * hhpeople : 1
     * is_ch : 0
     * language : 简体中文 英文 日文
     * series_id : 3
     * labels : [{"id":"1","title":"奇幻"},{"id":"3","title":"武侠"}]
     * imgs : []
     * showtype : 3
     * content : 刺客信条：起源_专题
     " webviewurl": "http:\/\/www2.3dmgame.com\/app\/games\/danganronpav3\/",
     * 
     * "num": 1, 排序编号
     */
    private int aid;
    private String arcurl;
    private String title;
    private String litpic;
    private int click;
    private String system;
    private double score;
    private int plpeople;
    private long pubdate_at;
    private String showdate_at;//显示的发售日期
    private int typeid;
    private String type;
    private String made;
    private String publisher;
    private int hhpeople;
    private int is_ch;
    private String language;
    private int series_id;
    private int showtype;
    private String content;
    private List<AvatarBean> labels;
    private List<String> imgs;
    private int isfavorite;//收藏状态0未收藏1已收藏
    private String webviewurl;
    private int num;
    private String pubday;

    public String getShowdate_at() {
        return showdate_at;
    }

    public void setShowdate_at(String showdate_at) {
        this.showdate_at = showdate_at;
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

    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getPlpeople() {
        return plpeople;
    }

    public void setPlpeople(int plpeople) {
        this.plpeople = plpeople;
    }

    public long getPubdate_at() {
        return pubdate_at;
    }

    public void setPubdate_at(Long pubdate_at) {
        this.pubdate_at = pubdate_at;
    }

    public int getTypeid() {
        return typeid;
    }

    public void setTypeid(int typeid) {
        this.typeid = typeid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMade() {
        return made;
    }

    public void setMade(String made) {
        this.made = made;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getHhpeople() {
        return hhpeople;
    }

    public void setHhpeople(int hhpeople) {
        this.hhpeople = hhpeople;
    }

    public int getIs_ch() {
        return is_ch;
    }

    public void setIs_ch(int is_ch) {
        this.is_ch = is_ch;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getSeries_id() {
        return series_id;
    }

    public void setSeries_id(int series_id) {
        this.series_id = series_id;
    }

    public int getShowtype() {
        return showtype;
    }

    public void setShowtype(int showtype) {
        this.showtype = showtype;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<AvatarBean> getLabels() {
        return labels;
    }

    public void setLabels(List<AvatarBean> labels) {
        this.labels = labels;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public int getIsfavorite() {
        return isfavorite;
    }

    public void setIsfavorite(int isfavorite) {
        this.isfavorite = isfavorite;
    }

    public String getWebviewurl() {
        return webviewurl;
    }

    public void setWebviewurl(String webviewurl) {
        this.webviewurl = webviewurl;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getPubday() {
        return pubday;
    }

    public void setPubday(String pubday) {
        this.pubday = pubday;
    }

    public String getLabelString(){
        if(labels==null)
            return "";
        else{
            String str_label="";
            for(AvatarBean bean:labels){
                str_label=str_label+bean.getName()+" ";
            }
            return str_label; 
        }
    }
}
