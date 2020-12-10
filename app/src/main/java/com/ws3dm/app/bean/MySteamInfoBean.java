package com.ws3dm.app.bean;

public class MySteamInfoBean {

    /**
     * 个人中心名片信息
     */
    /**
     * code : 1
     * data : {"isbang":0,"st_nickname":"","st_avatarstr":"","gameprice":0,"gamecount":0,"gamehour":"0.0","level":0,"pricepercent":0,"countpercent":0,"hourpercent":0}
     * msg : 成功
     */

    private int code;
    /**
     * isbang : 0
     * st_nickname :
     * st_avatarstr :
     * gameprice : 0
     * gamecount : 0
     * gamehour : 0.0
     * level : 0
     * pricepercent : 0
     * countpercent : 0
     * hourpercent : 0
     */

    private DataBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        private int isbang;
        private String st_nickname;
        private String st_avatarstr;
        private int gameprice;
        private int gamecount;
        private String gamehour;
        private int level;
        private int pricepercent;
        private int countpercent;
        private int hourpercent;

        public int getIsbang() {
            return isbang;
        }

        public void setIsbang(int isbang) {
            this.isbang = isbang;
        }

        public String getSt_nickname() {
            return st_nickname;
        }

        public void setSt_nickname(String st_nickname) {
            this.st_nickname = st_nickname;
        }

        public String getSt_avatarstr() {
            return st_avatarstr;
        }

        public void setSt_avatarstr(String st_avatarstr) {
            this.st_avatarstr = st_avatarstr;
        }

        public int getGameprice() {
            return gameprice;
        }

        public void setGameprice(int gameprice) {
            this.gameprice = gameprice;
        }

        public int getGamecount() {
            return gamecount;
        }

        public void setGamecount(int gamecount) {
            this.gamecount = gamecount;
        }

        public String getGamehour() {
            return gamehour;
        }

        public void setGamehour(String gamehour) {
            this.gamehour = gamehour;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getPricepercent() {
            return pricepercent;
        }

        public void setPricepercent(int pricepercent) {
            this.pricepercent = pricepercent;
        }

        public int getCountpercent() {
            return countpercent;
        }

        public void setCountpercent(int countpercent) {
            this.countpercent = countpercent;
        }

        public int getHourpercent() {
            return hourpercent;
        }

        public void setHourpercent(int hourpercent) {
            this.hourpercent = hourpercent;
        }
    }
}
