package com.ws3dm.app.bean.mysteampsn;

public class MyPsnInfoBean {
    /**
     * psn名片bean类
     */
    /**
     * code : 1
     * data : {"isbang":1,"psn_nickname":"Ubuntu-Fast","psn_avatarstr":"http://static-resource.np.community.playstation.net/avatar_m/WWS_A/A2117_m.png","gameprice":0,"gamecount":0,"cupcount":0,"level":1,"isplus":0,"pricepercent":50,"countpercent":50,"cuppercent":50,"platinum":0,"gold":0,"silver":0,"bronze":0,"isauth":0}
     * msg : 成功
     */

    private int code;
    /**
     * isbang : 1
     * psn_nickname : Ubuntu-Fast
     * psn_avatarstr : http://static-resource.np.community.playstation.net/avatar_m/WWS_A/A2117_m.png
     * gameprice : 0
     * gamecount : 0
     * cupcount : 0
     * level : 1
     * isplus : 0
     * pricepercent : 50
     * countpercent : 50
     * cuppercent : 50
     * platinum : 0
     * gold : 0
     * silver : 0
     * bronze : 0
     * isauth : 0
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
        private String psn_nickname;
        private String psn_avatarstr;
        private int gameprice;
        private int gamecount;
        private int cupcount;
        private int level;
        private int isplus;
        private int pricepercent;
        private int countpercent;
        private int cuppercent;
        private int platinum;
        private int gold;
        private int silver;
        private int bronze;
        private int isauth;

        public int getIsbang() {
            return isbang;
        }

        public void setIsbang(int isbang) {
            this.isbang = isbang;
        }

        public String getPsn_nickname() {
            return psn_nickname;
        }

        public void setPsn_nickname(String psn_nickname) {
            this.psn_nickname = psn_nickname;
        }

        public String getPsn_avatarstr() {
            return psn_avatarstr;
        }

        public void setPsn_avatarstr(String psn_avatarstr) {
            this.psn_avatarstr = psn_avatarstr;
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

        public int getCupcount() {
            return cupcount;
        }

        public void setCupcount(int cupcount) {
            this.cupcount = cupcount;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getIsplus() {
            return isplus;
        }

        public void setIsplus(int isplus) {
            this.isplus = isplus;
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

        public int getCuppercent() {
            return cuppercent;
        }

        public void setCuppercent(int cuppercent) {
            this.cuppercent = cuppercent;
        }

        public int getPlatinum() {
            return platinum;
        }

        public void setPlatinum(int platinum) {
            this.platinum = platinum;
        }

        public int getGold() {
            return gold;
        }

        public void setGold(int gold) {
            this.gold = gold;
        }

        public int getSilver() {
            return silver;
        }

        public void setSilver(int silver) {
            this.silver = silver;
        }

        public int getBronze() {
            return bronze;
        }

        public void setBronze(int bronze) {
            this.bronze = bronze;
        }

        public int getIsauth() {
            return isauth;
        }

        public void setIsauth(int isauth) {
            this.isauth = isauth;
        }
    }
}
