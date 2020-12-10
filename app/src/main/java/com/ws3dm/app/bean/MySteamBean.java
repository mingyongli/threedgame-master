package com.ws3dm.app.bean;

public class MySteamBean {
    /**
     * 用于获取个人中心steam绑定状态获取
     */
    /**
     * code : 1
     * data : {"isbang":0,"st_nickname":"","st_avatarstr":"","gameprice":0,"gamecount":0,"gamehour":"0.0"}
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
    }
}
