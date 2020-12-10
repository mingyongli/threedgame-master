package com.ws3dm.app.bean;

import java.util.List;

public class SteamUserAchieveBean {
    /**
     * code : 1
     * data : {"list":[{"rank":1,"nickname":"cs4399","avatarstr":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520313496_167955.jpg","uid":3990805,"achieve_percent":66,"achieve_show":"110/167","is_me":1},{"rank":1,"nickname":"cs4399","avatarstr":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520313496_167955.jpg","uid":3990805,"achieve_percent":66,"achieve_show":"110/167","is_me":0},{"rank":2,"nickname":"三减二","avatarstr":"https://my.3dmgame.com/uploads/images/avatar/20180810/1533888691_529275.jpg","uid":16504466,"achieve_percent":20,"achieve_show":"32/167","is_me":0}]}
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
         * rank : 1
         * nickname : cs4399
         * avatarstr : https://my.3dmgame.com/uploads/images/avatar/20180306/1520313496_167955.jpg
         * uid : 3990805
         * achieve_percent : 66
         * achieve_show : 110/167
         * is_me : 1
         */

        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private int rank;
            private String nickname;
            private String avatarstr;
            private int uid;
            private int achieve_percent;
            private String achieve_show;
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

            public int getIs_me() {
                return is_me;
            }

            public void setIs_me(int is_me) {
                this.is_me = is_me;
            }
        }
    }
}
