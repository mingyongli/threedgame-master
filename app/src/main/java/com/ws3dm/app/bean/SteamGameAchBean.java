package com.ws3dm.app.bean;

public class SteamGameAchBean {
    /**
     * code : 1
     * data : {"appid":38400,"litpic":"http://media.steampowered.com/steamcommunity/public/images/apps/38400/ab93e9e42e069b83926c8554de635142455aa271.jpg","title":"辐射","gamehour":"0.0","recenthour":"0.0","achieve_percent":0,"achieve_show":"","achieve_rank":1,"hour_rank":1}
     * msg : 成功
     */

    private int code;
    /**
     * appid : 38400
     * litpic : http://media.steampowered.com/steamcommunity/public/images/apps/38400/ab93e9e42e069b83926c8554de635142455aa271.jpg
     * title : 辐射
     * gamehour : 0.0
     * recenthour : 0.0
     * achieve_percent : 0
     * achieve_show :
     * achieve_rank : 1
     * hour_rank : 1
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
        private int appid;
        private String litpic;
        private String title;
        private String gamehour;
        private String recenthour;
        private int achieve_percent;
        private String achieve_show;
        private int achieve_rank;
        private int hour_rank;

        public int getAppid() {
            return appid;
        }

        public void setAppid(int appid) {
            this.appid = appid;
        }

        public String getLitpic() {
            return litpic;
        }

        public void setLitpic(String litpic) {
            this.litpic = litpic;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getGamehour() {
            return gamehour;
        }

        public void setGamehour(String gamehour) {
            this.gamehour = gamehour;
        }

        public String getRecenthour() {
            return recenthour;
        }

        public void setRecenthour(String recenthour) {
            this.recenthour = recenthour;
        }

        public int getAchieve_percent() {
            return achieve_percent;
        }

        public void setAchieve_percent(int achieve_percent) {
            this.achieve_percent = achieve_percent;
        }

        public String getAchieve_show() {
            return achieve_show;
        }

        public void setAchieve_show(String achieve_show) {
            this.achieve_show = achieve_show;
        }

        public int getAchieve_rank() {
            return achieve_rank;
        }

        public void setAchieve_rank(int achieve_rank) {
            this.achieve_rank = achieve_rank;
        }

        public int getHour_rank() {
            return hour_rank;
        }

        public void setHour_rank(int hour_rank) {
            this.hour_rank = hour_rank;
        }
    }
}
