package com.ws3dm.app.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Describution :游戏热门bean
 * 
 * Author : DKjuan
 * 
 * Date : 2018/4/17 17:16
 **/

public class GameHotBean implements Serializable {
    /**
     * key : newgame
     * name : 最新大作
     * list : [{"aid":"10042","arcurl":"http://www2.3dmgame.com/games/gd4/","title":"孤岛惊魂4","litpic":"http://www2.3dmgame.com/uploads/images/thumbkwdfirst/20180411/1523429063_435494.jpg","pubdate_at":"1523462400","showtype":3},{"aid":"10029","arcurl":"http://www2.3dmgame.com/games/qyrs/","title":"奇异人生","litpic":"http://www2.3dmgame.com/uploads/images/thumbkwdfirst/20180406/1523000194_252514.jpg","pubdate_at":"1422547200","showtype":3},{"aid":"10028","arcurl":"http://www2.3dmgame.com/games/qyrsfbqx/","title":"奇异人生：风暴前夕","litpic":"http://www2.3dmgame.com/uploads/images/thumbkwdfirst/20180406/1522999168_471917.jpg","pubdate_at":"1504108800","showtype":3},{"aid":"10026","arcurl":"http://www2.3dmgame.com/games/hxqs/","title":"火星求生","litpic":"http://www2.3dmgame.com/uploads/images/thumbkwdfirst/20180406/1522990819_647704.jpg","pubdate_at":"1521043200","showtype":3},{"aid":"10025","arcurl":"http://www2.3dmgame.com/games/danganronpav3/","title":"新弹丸论破V3","litpic":"http://www2.3dmgame.com/uploads/images/thumbkwdfirst/20180406/1522983618_981489.jpg","pubdate_at":"1506441600","showtype":3},{"aid":"10022","arcurl":"http://www2.3dmgame.com/games/gd5/","title":"孤岛惊魂5","litpic":"http://www2.3dmgame.com/uploads/images/thumbkwdfirst/20180328/1522209294_996947.jpg","pubdate_at":"1523635200","showtype":3}]
     */

    private int type;
    private String key;
    private String name;
    private List<GameBean> list;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GameBean> getList() {
        return list;
    }

    public void setList(List<GameBean> list) {
        this.list = list;
    }
}