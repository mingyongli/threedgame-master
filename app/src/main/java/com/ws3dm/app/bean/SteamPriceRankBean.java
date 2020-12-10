package com.ws3dm.app.bean;

import java.util.List;

public class SteamPriceRankBean {

    /**
     * code : 1
     * data : {"list":[{"rank":2,"st_nickname":"三减二","st_avatarstr":"https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/c3/c37bbc9f7087988b8823eca8f4f3aecb33efa243.jpg","gameprice":2757,"nickname":"三减二","avatarstr":"https://my.3dmgame.com/uploads/images/avatar/20180810/1533888691_529275.jpg","uid":16504466,"is_me":1},{"uid":3990805,"rank":1,"st_nickname":"【SVIP】APPSES","st_avatarstr":"https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/c7/c74afd4f55f5c413f08e4155b68f7bddc7ebfe7e.jpg","gameprice":2914,"nickname":"cs4399","avatarstr":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520313496_167955.jpg","is_me":0},{"uid":16504466,"rank":2,"st_nickname":"三减二","st_avatarstr":"https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/c3/c37bbc9f7087988b8823eca8f4f3aecb33efa243.jpg","gameprice":2757,"nickname":"三减二","avatarstr":"https://my.3dmgame.com/uploads/images/avatar/20180810/1533888691_529275.jpg","is_me":0},{"uid":9663679,"rank":3,"st_nickname":"763526631","st_avatarstr":"https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/fe/fef49e7fa7e1997310d705b2a6158ff8dc1cdfeb.jpg","gameprice":0,"nickname":"若即若离哈","avatarstr":"https://my.3dmgame.com/uploads/images/avatar/20171202/1512217860_121300.jpg","is_me":0},{"uid":17087564,"rank":4,"st_nickname":"961070029","st_avatarstr":"https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/fe/fef49e7fa7e1997310d705b2a6158ff8dc1cdfeb.jpg","gameprice":0,"nickname":"3dm_17087564","avatarstr":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520233039_189446.jpg","is_me":0}]}
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
         * st_nickname : 三减二
         * st_avatarstr : https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/c3/c37bbc9f7087988b8823eca8f4f3aecb33efa243.jpg
         * gameprice : 2757
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
            private String st_nickname;
            private String st_avatarstr;
            private int gameprice;
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
