package com.ws3dm.app.mvvm.bean;

import java.util.List;

public class MyFollowBean {

    /**
     * code : 1
     * data : {"followPlate":[{"plateId":"1774","plateName":"《星露谷物语》","plateImage":"https://att.3dmgame.com/att/common/f0/common_1774_icon.gif","todayPostTopicCount":154},{"plateId":"2011","plateName":"《犯人就是我》","plateImage":"https://att.3dmgame.com/att/common/c8/common_2011_icon.png","todayPostTopicCount":0},{"plateId":"2010","plateName":"《活动专区》","plateImage":"","todayPostTopicCount":0},{"plateId":"2009","plateName":"商务活动专区","plateImage":"","todayPostTopicCount":0},{"plateId":"2008","plateName":null,"plateImage":"","todayPostTopicCount":0},{"plateId":"2007","plateName":null,"plateImage":"","todayPostTopicCount":0},{"plateId":"2006","plateName":"《拘束少女》","plateImage":"https://att.3dmgame.com/att/common/ea/common_2006_icon.png","todayPostTopicCount":0},{"plateId":"2005","plateName":"《抢匪鲍勃3》","plateImage":"https://att.3dmgame.com/att/common/d4/common_2005_icon.png","todayPostTopicCount":0},{"plateId":"2004","plateName":"手游垃圾回收站","plateImage":"","todayPostTopicCount":0},{"plateId":"2003","plateName":"资源链补缺申请区","plateImage":"https://att.3dmgame.com/att/common/a5/common_2003_icon.png","todayPostTopicCount":0},{"plateId":"2002","plateName":"3DM汉化组(日文手游区)","plateImage":"https://att.3dmgame.com/att/common/4b/common_2002_icon.png","todayPostTopicCount":0},{"plateId":"2001","plateName":"手谈汉化组","plateImage":"https://att.3dmgame.com/att/common/d0/common_2001_icon.png","todayPostTopicCount":0},{"plateId":"2000","plateName":"3DM汉化组(英文手游区)","plateImage":"https://att.3dmgame.com/att/common/08/common_2000_icon.png","todayPostTopicCount":0},{"plateId":"1330","plateName":"《模拟人生4》","plateImage":"https://att.3dmgame.com/att/common/fe/common_1330_icon.gif","todayPostTopicCount":575},{"plateId":"2544","plateName":"《怪物猎人：世界》","plateImage":"https://att.3dmgame.com/att/common/f0/common_2544_icon.gif","todayPostTopicCount":732},{"plateId":"1158","plateName":"《巫师3：狂猎》","plateImage":"https://att.3dmgame.com/att/common/0c/common_1158_icon.gif","todayPostTopicCount":425},{"plateId":"2341","plateName":"《赛博朋克2077》","plateImage":"https://att.3dmgame.com/att/common/c8/common_2341_icon.png","todayPostTopicCount":4146}]}
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
         * plateId : 1774
         * plateName : 《星露谷物语》
         * plateImage : https://att.3dmgame.com/att/common/f0/common_1774_icon.gif
         * todayPostTopicCount : 154
         */

        private List<FollowPlateBean> followPlate;

        public List<FollowPlateBean> getFollowPlate() {
            return followPlate;
        }

        public void setFollowPlate(List<FollowPlateBean> followPlate) {
            this.followPlate = followPlate;
        }

        public static class FollowPlateBean {
            private String plateId;
            private String plateName;
            private String plateImage;
            private int todayPostTopicCount;

            public String getPlateId() {
                return plateId;
            }

            public void setPlateId(String plateId) {
                this.plateId = plateId;
            }

            public String getPlateName() {
                return plateName;
            }

            public void setPlateName(String plateName) {
                this.plateName = plateName;
            }

            public String getPlateImage() {
                return plateImage;
            }

            public void setPlateImage(String plateImage) {
                this.plateImage = plateImage;
            }

            public int getTodayPostTopicCount() {
                return todayPostTopicCount;
            }

            public void setTodayPostTopicCount(int todayPostTopicCount) {
                this.todayPostTopicCount = todayPostTopicCount;
            }
        }
    }
}
