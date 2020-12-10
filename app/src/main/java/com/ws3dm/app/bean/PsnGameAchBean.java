package com.ws3dm.app.bean;

public class PsnGameAchBean {
    /**
     * code : 1
     * data : {"psn_prodid":702110,"litpic":"","title":"","gamehour":"0.0","achieve_percent":0,"achieve_show":"","achieve_rank":0,"cup_rank":0,"platinum":0,"gold":0,"silver":0,"bronze":0}
     * msg : 成功
     */

    private int code;
    /**
     * psn_prodid : 702110
     * litpic :
     * title :
     * gamehour : 0.0
     * achieve_percent : 0
     * achieve_show :
     * achieve_rank : 0
     * cup_rank : 0
     * platinum : 0
     * gold : 0
     * silver : 0
     * bronze : 0
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
        private int psn_prodid;
        private String litpic;
        private String title;
        private String gamehour;
        private int achieve_percent;
        private String achieve_show;
        private int achieve_rank;
        private int cup_rank;
        private int platinum;
        private int gold;
        private int silver;
        private int bronze;

        public int getPsn_prodid() {
            return psn_prodid;
        }

        public void setPsn_prodid(int psn_prodid) {
            this.psn_prodid = psn_prodid;
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

        public int getCup_rank() {
            return cup_rank;
        }

        public void setCup_rank(int cup_rank) {
            this.cup_rank = cup_rank;
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
    }
}
