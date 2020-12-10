package com.ws3dm.app.bean;

/**
 * Created by lc on 2017/3/22.
 */

public class Forum {

    /**
     * fid : 406
     * name : 游戏杂谈与STN
     * todayposts : 0
     * rank : 128
     * icon : null
     * type : forum
     */

    private String fid;
    private String name;
    private String todayposts;
    private String rank;
    private String icon;
    private String type;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTodayposts() {
        return todayposts;
    }

    public void setTodayposts(String todayposts) {
        this.todayposts = todayposts;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
