package com.ws3dm.app.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Describution :手游列表里面的itembean，有分类标题，图片，和分类游戏list,手游详情里面信息页中相关游戏
 * 
 * Author : DKjuan
 * 
 * Date : 2018/4/13 11:37
 **/

public class MGListBean implements Serializable {
    /**
     * imgnum
     * type : 动作竞技
     * typeid : 5
     * bigpic : http://shouyou2.3dmgame.com/uploadimg/ico/2018/0406/1523004674388828.png
     * tagid : 3
     * labels : [{"id":427,"name":"明星代言"},{"id":435,"name":"火柴人"},{"id":448,"name":"合成游戏"},{"id":450,"name":"进化"},{"id":470,"name":"对战"},{"id":471,"name":"激情"},{"id":474,"name":"类似阴阳师"},{"id":477,"name":"红警"},{"id":481,"name":"蝙蝠侠"},{"id":483,"name":"类似我的世界"},{"id":522,"name":"没有战力"},{"id":523,"name":"重力感应"},{"id":524,"name":"耐力"},{"id":543,"name":"chinajoy"},{"id":586,"name":"敏捷"},{"id":597,"name":"偶像"},{"id":613,"name":"打怪兽"},{"id":3,"name":"躲避"},{"id":4,"name":"跑酷"},{"id":5,"name":"丧尸"},{"id":6,"name":"多人"},{"id":21,"name":"战争"},{"id":22,"name":"玄幻"},{"id":40,"name":"血腥"},{"id":43,"name":"僵尸"},{"id":75,"name":"男生"},{"id":76,"name":"犯罪"},{"id":77,"name":"跳跃"},{"id":78,"name":"大型"},{"id":160,"name":"穿越"},{"id":206,"name":"类似LOL"},{"id":383,"name":"机器人游戏"},{"id":420,"name":"动作"},{"id":423,"name":"类似王者荣耀"}]
     * softlist : [{"arcurl":"http://shouyou2.3dmgame.com/android/10000.html","title":"王者荣耀","litpic":"http://shouyou2.3dmgame.com/uploadimg/ico/2018/0329/1522300652618348.png","soft_size":"185.03MB","soft_ver":"1.0","desc":"","showtype":4,"downurl":"http://dl6.cudown.com/3DMGAME_Travel_Adventures_World_Wonders.EN.Green.rar"},{"arcurl":"http://shouyou2.3dmgame.com/android/10008.html","title":"孤岛先锋","litpic":"http://shouyou2.3dmgame.com/uploadimg/ico/2018/0404/1522832092526780.png","soft_size":"523.21MB","soft_ver":"1.0.1","desc":"","showtype":4,"downurl":"https://down4.cudown.com/app/gudaoxianfeng.apk"},{"arcurl":"http://shouyou2.3dmgame.com/android/10009.html","title":"魂斗罗：归来","litpic":"http://shouyou2.3dmgame.com/uploadimg/ico/2018/0404/1522832293157531.png","soft_size":"729.16MB","soft_ver":"1.7.39.6033","desc":"","showtype":4,"downurl":"http://down.s.qq.com/download/1104338667/apk/10023758_com.tencent.shootgame.apk"}]
     */

    private int show;//是否显示详情按钮 0，展示 1，不展示
    private int imgnum;//是否有图片，0，没有顶部图片，1，一张图片（分类顶部大图）
    private String type;
    private int typeid;
    private String bigpic;
    private int tagid;
    private List<AvatarBean> labels;
    private List<SoftGameBean> softlist;

    public int getShow() {
        return show;
    }

    public void setShow(int show) {
        this.show = show;
    }

    public int getImgnum() {
        return imgnum;
    }

    public void setImgnum(int imgnum) {
        this.imgnum = imgnum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTypeid() {
        return typeid;
    }

    public void setTypeid(int typeid) {
        this.typeid = typeid;
    }

    public String getBigpic() {
        return bigpic;
    }

    public void setBigpic(String bigpic) {
        this.bigpic = bigpic;
    }

    public int getTagid() {
        return tagid;
    }

    public void setTagid(int tagid) {
        this.tagid = tagid;
    }

    public List<AvatarBean> getLabels() {
        return labels;
    }

    public void setLabels(List<AvatarBean> labels) {
        this.labels = labels;
    }

    public List<SoftGameBean> getSoftlist() {
        return softlist;
    }

    public void setSoftlist(List<SoftGameBean> softlist) {
        this.softlist = softlist;
    }
}
