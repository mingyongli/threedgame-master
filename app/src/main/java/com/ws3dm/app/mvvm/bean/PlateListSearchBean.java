package com.ws3dm.app.mvvm.bean;

import java.util.List;

public class PlateListSearchBean {
    /**
     * code : 1
     * data : {"list":[{"plateId":2042,"plateName":"《合体三国》","plateImage":"https://att.3dmgame.com/att/common/71/common_2042_icon.png"},{"plateId":2644,"plateName":"《三国创世纪》","plateImage":"https://att.3dmgame.com/att/common/f3/common_2644_icon.jpg"},{"plateId":2724,"plateName":"《神奇三国》","plateImage":"https://att.3dmgame.com/att/common/64/common_2724_icon.png"},{"plateId":2861,"plateName":"《三国计》","plateImage":"https://att.3dmgame.com/att/common/bd/common_2861_icon.jpg"},{"plateId":2998,"plateName":"《步战三国》","plateImage":"https://att.3dmgame.com/att/common/71/common_2998_icon.jpg"},{"plateId":3237,"plateName":"《名酱三国》","plateImage":"https://att.3dmgame.com/att/common/54/common_3237_icon.png"},{"plateId":3069,"plateName":"《三国志14》","plateImage":"https://att.3dmgame.com/att/common/bd/common_3069_icon.gif"},{"plateId":2556,"plateName":"《全面战争：三国》","plateImage":"https://att.3dmgame.com/att/common/1d/common_2556_icon.gif"},{"plateId":1619,"plateName":"《三国志13》","plateImage":"https://att.3dmgame.com/att/common/d1/common_1619_icon.gif"},{"plateId":1039,"plateName":"《真·三国无双7》","plateImage":"https://att.3dmgame.com/att/common/27/common_1039_icon.gif"}]}
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
         * plateId : 2042
         * plateName : 《合体三国》
         * plateImage : https://att.3dmgame.com/att/common/71/common_2042_icon.png
         */

        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private int plateId;
            private String plateName;
            private String plateImage;
            private String todayPostTopicCount;

            public String getTodayPostTopicCount() {
                return todayPostTopicCount;
            }

            public void setTodayPostTopicCount(String todayPostTopicCount) {
                this.todayPostTopicCount = todayPostTopicCount;
            }

            public int getPlateId() {
                return plateId;
            }

            public void setPlateId(int plateId) {
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
        }
    }
}
