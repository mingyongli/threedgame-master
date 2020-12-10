package com.ws3dm.app.bean;

import java.util.List;

public class PsnCupRankBean {
    /**
     * code : 1
     * data : {"list":[{"rank":0,"nickname":"cs4399","avatarstr":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520313496_167955.jpg","uid":3990805,"platinum":0,"gold":0,"silver":0,"bronze":0,"gamehour":"0.0","is_me":1}]}
     * msg : 成功
     */

    private int code;
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
        /**
         * rank : 0
         * nickname : cs4399
         * avatarstr : https://my.3dmgame.com/uploads/images/avatar/20180306/1520313496_167955.jpg
         * uid : 3990805
         * platinum : 0
         * gold : 0
         * silver : 0
         * bronze : 0
         * gamehour : 0.0
         * is_me : 1
         */

        private List<ListBean> list;
        /**
         * averagerate : 0
         * platinumrate : 0
         */

        private int averagerate;
        private int platinumrate;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public int getAveragerate() {
            return averagerate;
        }

        public void setAveragerate(int averagerate) {
            this.averagerate = averagerate;
        }

        public int getPlatinumrate() {
            return platinumrate;
        }

        public void setPlatinumrate(int platinumrate) {
            this.platinumrate = platinumrate;
        }

        public static class ListBean {
            private int rank;
            private String nickname;
            private String avatarstr;
            private int uid;
            private int platinum;
            private int gold;
            private int silver;
            private int bronze;
            private String gamehour;
            private int is_me;

            public int getRank() {
                return rank;
            }

            public void setRank(int rank) {
                this.rank = rank;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getAvatarstr() {
                return avatarstr;
            }

            public void setAvatarstr(String avatarstr) {
                this.avatarstr = avatarstr;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
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

            public String getGamehour() {
                return gamehour;
            }

            public void setGamehour(String gamehour) {
                this.gamehour = gamehour;
            }

            public int getIs_me() {
                return is_me;
            }

            public void setIs_me(int is_me) {
                this.is_me = is_me;
            }
        }
    }
}
