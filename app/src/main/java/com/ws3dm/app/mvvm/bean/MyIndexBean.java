package com.ws3dm.app.mvvm.bean;

import java.util.List;

public class MyIndexBean {

    /**
     * code : 1
     * data : {"latelyPlate":[{"plateId":"2341","plateName":"《赛博朋克2077》","plateImage":"https://att.3dmgame.com/att/common/c8/common_2341_icon.png","todayPostTopicCount":3631},{"plateId":"1330","plateName":"《模拟人生4》","plateImage":"https://att.3dmgame.com/att/common/fe/common_1330_icon.gif","todayPostTopicCount":494},{"plateId":"2544","plateName":"《怪物猎人：世界》","plateImage":"https://att.3dmgame.com/att/common/f0/common_2544_icon.gif","todayPostTopicCount":653},{"plateId":"1158","plateName":"《巫师3：狂猎》","plateImage":"https://att.3dmgame.com/att/common/0c/common_1158_icon.gif","todayPostTopicCount":384}],"followPlate":[{"plateId":"2341","plateName":"《赛博朋克2077》","plateImage":"https://att.3dmgame.com/att/common/c8/common_2341_icon.png","todayPostTopicCount":3631}]}
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
         * plateId : 2341
         * plateName : 《赛博朋克2077》
         * plateImage : https://att.3dmgame.com/att/common/c8/common_2341_icon.png
         * todayPostTopicCount : 3631
         */

        private List<LatelyPlateBean> latelyPlate;
        /**
         * plateId : 2341
         * plateName : 《赛博朋克2077》
         * plateImage : https://att.3dmgame.com/att/common/c8/common_2341_icon.png
         * todayPostTopicCount : 3631
         */

        private List<FollowPlateBean> followPlate;

        public List<LatelyPlateBean> getLatelyPlate() {
            return latelyPlate;
        }

        public void setLatelyPlate(List<LatelyPlateBean> latelyPlate) {
            this.latelyPlate = latelyPlate;
        }

        public List<FollowPlateBean> getFollowPlate() {
            return followPlate;
        }

        public void setFollowPlate(List<FollowPlateBean> followPlate) {
            this.followPlate = followPlate;
        }

        public static class LatelyPlateBean {
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
