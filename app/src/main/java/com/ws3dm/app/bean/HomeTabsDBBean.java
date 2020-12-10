/**
 * Copyright 2020 bejson.com
 */
package com.ws3dm.app.bean;

import java.util.List;

/**
 * Auto-generated: 2020-09-22 14:22:3
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class HomeTabsDBBean {

    private int code;
    private List<HomeTabsData> data;
    private String msg;

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setData(List<HomeTabsData> data) {
        this.data = data;
    }

    public List<HomeTabsData> getData() {
        return data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public static class HomeTabsData {

        private int aid;
        private String litpic;
        private String title;
        private int type;
        private int cid;
        private int open;

        public int isOpen() {
            return open;
        }

        public void setOpen(int open) {
            this.open = open;
        }

        public void setAid(int aid) {
            this.aid = aid;
        }

        public int getAid() {
            return aid;
        }

        public void setLitpic(String litpic) {
            this.litpic = litpic;
        }

        public String getLitpic() {
            return litpic;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public void setCid(int cid) {
            this.cid = cid;
        }

        public int getCid() {
            return cid;
        }

    }

}