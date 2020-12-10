package com.ws3dm.app.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lc on 2017/3/21.
 */

public class GameGiftBean implements Serializable {
    /**
     * aid : 10195
     * arcurl : https://ol.3dmgame.com/hao/10195.html
     * litpic : https://ol.3dmgame.com/uploads/images/thumbbig/20191120/1574214074_688219.jpg
     * showtype : 22
     * title : 《古剑奇谭网络版》 怀仙寻梦礼包
     * webviewurl : 
     * range_start : 1574265600
     * range_end : 1577721600
     * surplusper : 99
     * is_over : 1
     * is_empty : 0
     * is_avail : 0
     */
    private int aid;
    private String arcurl;
    private String litpic;
    private int showtype;
    private String title;
    private String webviewurl;
    private String range_start;
    private String range_end;
    private int surplusper;
    private int is_over;
    private int is_empty;
    private int is_avail;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWebviewurl() {
        return webviewurl;
    }

    public void setWebviewurl(String webviewurl) {
        this.webviewurl = webviewurl;
    }

    public String getRange_start() {
        return range_start;
    }

    public void setRange_start(String range_start) {
        this.range_start = range_start;
    }

    public String getRange_end() {
        return range_end;
    }

    public void setRange_end(String range_end) {
        this.range_end = range_end;
    }

    public int getSurplusper() {
        return surplusper;
    }

    public void setSurplusper(int surplusper) {
        this.surplusper = surplusper;
    }

    public int getIs_over() {
        return is_over;
    }

    public void setIs_over(int is_over) {
        this.is_over = is_over;
    }

    public int getIs_empty() {
        return is_empty;
    }

    public void setIs_empty(int is_empty) {
        this.is_empty = is_empty;
    }

    public int getIs_avail() {
        return is_avail;
    }

    public void setIs_avail(int is_avail) {
        this.is_avail = is_avail;
    }
}
