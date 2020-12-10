package com.ws3dm.app.bean;

import java.io.Serializable;

/**
 * Describution :视频和新闻共用一个实体
 * 
 * Author : DKjuan
 * 
 * Date : 2018/4/13 11:37
 **/

public class SoftGameBean implements Serializable {

    /**
     * aid : 12
     * arcurl : http://shouyou2.3dmgame.com/android/10000.html
     * title : 王者荣耀
     * litpic : http://shouyou2.3dmgame.com/uploadimg/ico/2018/0329/1522300652618348.png
     * soft_size : 185.03MB
     * soft_ver : 1.0
     * desc : 
     * showtype : 4
     * webviewurl : http://dl6.cudown.com/3DMGAME_Travel_Adventures_World_Wonders.EN.Green.rar
     * type	string	类型名称
     */

    private int aid;
    private String arcurl;
    private String title;
    private String litpic;
    private String type;
    private String soft_size;
    private String soft_ver;
    private String desc;
    private int showtype;
    private String webviewurl;
    private String label;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSoft_size() {
        return soft_size;
    }

    public void setSoft_size(String soft_size) {
        this.soft_size = soft_size;
    }

    public String getSoft_ver() {
        return soft_ver;
    }

    public void setSoft_ver(String soft_ver) {
        this.soft_ver = soft_ver;
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

    public String getWebviewurl() {
        return webviewurl;
    }

    public void setWebviewurl(String webviewurl) {
        this.webviewurl = webviewurl;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
