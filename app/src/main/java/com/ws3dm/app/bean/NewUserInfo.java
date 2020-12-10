package com.ws3dm.app.bean;

public class NewUserInfo {
    private int code;
    private Data data;
    private String msg;

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }


    public class Info {

        private long uid;
        private String username;
        private String nickname;
        private String avatarstr;
        private int follow_total;
        private int fansi_total;
        private String posts_total;
        private int user_level;
        private int auth_level;
        private String auth_title;
        private int favorite_total;

        public int getFavorite_total() {
            return favorite_total;
        }

        public void setFavorite_total(int favorite_total) {
            this.favorite_total = favorite_total;
        }

        public void setUid(long uid) {
            this.uid = uid;
        }

        public long getUid() {
            return uid;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUsername() {
            return username;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getNickname() {
            return nickname;
        }

        public void setAvatarstr(String avatarstr) {
            this.avatarstr = avatarstr;
        }

        public String getAvatarstr() {
            return avatarstr;
        }

        public void setFollow_total(int follow_total) {
            this.follow_total = follow_total;
        }

        public int getFollow_total() {
            return follow_total;
        }

        public void setFansi_total(int fansi_total) {
            this.fansi_total = fansi_total;
        }

        public int getFansi_total() {
            return fansi_total;
        }

        public void setPosts_total(String posts_total) {
            this.posts_total = posts_total;
        }

        public String getPosts_total() {
            return posts_total;
        }

        public void setUser_level(int user_level) {
            this.user_level = user_level;
        }

        public int getUser_level() {
            return user_level;
        }

        public void setAuth_level(int auth_level) {
            this.auth_level = auth_level;
        }

        public int getAuth_level() {
            return auth_level;
        }

        public void setAuth_title(String auth_title) {
            this.auth_title = auth_title;
        }

        public String getAuth_title() {
            return auth_title;
        }

    }

    public class Data {

        private Info info;

        public void setInfo(Info info) {
            this.info = info;
        }

        public Info getInfo() {
            return info;
        }

    }
}
