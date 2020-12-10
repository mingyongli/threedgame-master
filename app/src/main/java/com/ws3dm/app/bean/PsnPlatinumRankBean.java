package com.ws3dm.app.bean;

import java.util.List;

public class PsnPlatinumRankBean {
    /**
     * code : 1
     * data : {"list":[{"rank":2,"psn_nickname":"Ubuntu-Fast","psn_avatarstr":"http://static-resource.np.community.playstation.net/avatar_m/WWS_A/A2117_m.png","platinum":0,"nickname":"三减二","avatarstr":"https://my.3dmgame.com/uploads/images/avatar/20180810/1533888691_529275.jpg","uid":16504466,"is_me":1},{"uid":3990805,"rank":1,"psn_nickname":"cs4399","psn_avatarstr":"http://psn-rsc.prod.dl.playstation.net/psn-rsc/avatar/HP0102/CUSA06605_00-PREMIUMAVATAR021_C9109F1A9E3A075EE225_m.png","platinum":1,"nickname":"cs4399","avatarstr":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520313496_167955.jpg","is_me":0},{"uid":16504466,"rank":2,"psn_nickname":"Ubuntu-Fast","psn_avatarstr":"http://static-resource.np.community.playstation.net/avatar_m/WWS_A/A2117_m.png","platinum":0,"nickname":"三减二","avatarstr":"https://my.3dmgame.com/uploads/images/avatar/20180810/1533888691_529275.jpg","is_me":0}]}
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
         * rank : 2
         * psn_nickname : Ubuntu-Fast
         * psn_avatarstr : http://static-resource.np.community.playstation.net/avatar_m/WWS_A/A2117_m.png
         * platinum : 0
         * nickname : 三减二
         * avatarstr : https://my.3dmgame.com/uploads/images/avatar/20180810/1533888691_529275.jpg
         * uid : 16504466
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
            private String psn_nickname;
            private String psn_avatarstr;
            private int platinum;
            private String nickname;
            private String avatarstr;
            private int uid;
            private int is_me;

            public int getRank() {
                return rank;
            }

            public void setRank(int rank) {
                this.rank = rank;
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

            public int getPlatinum() {
                return platinum;
            }

            public void setPlatinum(int platinum) {
                this.platinum = platinum;
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

            public int getIs_me() {
                return is_me;
            }

            public void setIs_me(int is_me) {
                this.is_me = is_me;
            }
        }
    }
}
