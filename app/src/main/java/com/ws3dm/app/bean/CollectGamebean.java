package com.ws3dm.app.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Describution :搜索
 * 
 * Author : DKjuan
 * 
 * Date : 2018/4/13 11:37
 **/

public class CollectGamebean implements Serializable {
    /**
     * aid : 267619
     * showtype : 3
     * arcurl : https://www.3dmgame.com/games/tes4/
     * title : 上古卷轴4：湮没
     * litpic : https://img.3dmgame.com/uploads/images/thumbkwdfirst/20180528/1527486460_440299.jpg
     * type : 角色扮演
     * system : PC PS3 XBOX360
     * label : 沙盒 魔幻 开放世界 暴力
     * pubdate_at : 1142870400
     * score : 9.2
     * webviewurl : 
     */

    private int aid;
    private int showtype;
    private String arcurl;
    private String title;
    private String litpic;
    private String type;
    private String system;
    private List<AvatarBean> labels;
    private int pubdate_at;
    private String score;
    private String webviewurl;
    private String firm;
    private String betatime;

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public int getShowtype() {
        return showtype;
    }

    public void setShowtype(int showtype) {
        this.showtype = showtype;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getLabels() {
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

    public void setLabel(List<AvatarBean> labels) {
        this.labels = labels;
    }

    public int getPubdate_at() {
        return pubdate_at;
    }

    public void setPubdate_at(int pubdate_at) {
        this.pubdate_at = pubdate_at;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getWebviewurl() {
        return webviewurl;
    }

    public void setWebviewurl(String webviewurl) {
        this.webviewurl = webviewurl;
    }

    public String getFirm() {
        return firm;
    }

    public void setFirm(String firm) {
        this.firm = firm;
    }

    public String getBetatime() {
        return betatime;
    }

    public void setBetatime(String betatime) {
        this.betatime = betatime;
    }
}
