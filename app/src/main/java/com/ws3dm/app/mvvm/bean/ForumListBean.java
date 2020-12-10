package com.ws3dm.app.mvvm.bean;

import java.util.List;

public class ForumListBean {
    /**
     * code : 1
     * data : {"list":[{"plateTypeId":437,"plateTypeName":"自营H5游戏","plateTypeList":[{"plateId":2042,"plateName":"《合体三国》","plateImage":"https://att.3dmgame.com/att/common/71/common_2042_icon.png"},{"plateId":2181,"plateName":"《绝世神功》","plateImage":"https://att.3dmgame.com/att/common/10/common_2181_icon.jpg"},{"plateId":2387,"plateName":"《武圣传奇》","plateImage":"https://att.3dmgame.com/att/common/8b/common_2387_banner.jpg"},{"plateId":2542,"plateName":"《侠客行》","plateImage":"https://att.3dmgame.com/att/common/c3/common_2542_icon.gif"},{"plateId":2545,"plateName":"《荒野求生》","plateImage":"https://att.3dmgame.com/att/common/9d/common_2545_icon.png"},{"plateId":2575,"plateName":"《逐日战神》","plateImage":"https://att.3dmgame.com/att/common/fe/common_2575_icon.png"},{"plateId":2613,"plateName":"《浅浅女王梦》","plateImage":"https://att.3dmgame.com/att/common/6a/common_2613_icon.jpg"},{"plateId":2633,"plateName":"《传奇世界H5》","plateImage":"https://att.3dmgame.com/att/common/fc/common_2633_icon.jpg"},{"plateId":2644,"plateName":"《三国创世纪》","plateImage":"https://att.3dmgame.com/att/common/f3/common_2644_icon.jpg"},{"plateId":2652,"plateName":"《西游七十二变》","plateImage":"https://att.3dmgame.com/att/common/0c/common_2652_icon.jpg"}]},{"plateTypeId":1,"plateTypeName":"站务论坛","plateTypeList":[]},{"plateTypeId":1009,"plateTypeName":"资源发布","plateTypeList":[]},{"plateTypeId":3,"plateTypeName":"综合讨论","plateTypeList":[]},{"plateTypeId":2869,"plateTypeName":"国产单机游戏讨论区","plateTypeList":[]},{"plateTypeId":441,"plateTypeName":"热门游戏","plateTypeList":[]},{"plateTypeId":502,"plateTypeName":"经典游戏","plateTypeList":[]},{"plateTypeId":438,"plateTypeName":"即将发行","plateTypeList":[]},{"plateTypeId":354,"plateTypeName":"动作游戏","plateTypeList":[]},{"plateTypeId":353,"plateTypeName":"角色扮演","plateTypeList":[]},{"plateTypeId":355,"plateTypeName":"射击游戏","plateTypeList":[]},{"plateTypeId":387,"plateTypeName":"模拟经营","plateTypeList":[]},{"plateTypeId":357,"plateTypeName":"策略战棋","plateTypeList":[]},{"plateTypeId":2279,"plateTypeName":"独立游戏","plateTypeList":[]},{"plateTypeId":504,"plateTypeName":"即时战略","plateTypeList":[]},{"plateTypeId":360,"plateTypeName":"体育竞速","plateTypeList":[]},{"plateTypeId":229,"plateTypeName":"电玩专题","plateTypeList":[]},{"plateTypeId":542,"plateTypeName":"动漫专题","plateTypeList":[]},{"plateTypeId":201,"plateTypeName":"娱乐休闲","plateTypeList":[]},{"plateTypeId":478,"plateTypeName":"3DM工作室论坛","plateTypeList":[]},{"plateTypeId":2656,"plateTypeName":"3DM MOD站","plateTypeList":[]}]}
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
         * plateTypeId : 437
         * plateTypeName : 自营H5游戏
         * plateTypeList : [{"plateId":2042,"plateName":"《合体三国》","plateImage":"https://att.3dmgame.com/att/common/71/common_2042_icon.png"},{"plateId":2181,"plateName":"《绝世神功》","plateImage":"https://att.3dmgame.com/att/common/10/common_2181_icon.jpg"},{"plateId":2387,"plateName":"《武圣传奇》","plateImage":"https://att.3dmgame.com/att/common/8b/common_2387_banner.jpg"},{"plateId":2542,"plateName":"《侠客行》","plateImage":"https://att.3dmgame.com/att/common/c3/common_2542_icon.gif"},{"plateId":2545,"plateName":"《荒野求生》","plateImage":"https://att.3dmgame.com/att/common/9d/common_2545_icon.png"},{"plateId":2575,"plateName":"《逐日战神》","plateImage":"https://att.3dmgame.com/att/common/fe/common_2575_icon.png"},{"plateId":2613,"plateName":"《浅浅女王梦》","plateImage":"https://att.3dmgame.com/att/common/6a/common_2613_icon.jpg"},{"plateId":2633,"plateName":"《传奇世界H5》","plateImage":"https://att.3dmgame.com/att/common/fc/common_2633_icon.jpg"},{"plateId":2644,"plateName":"《三国创世纪》","plateImage":"https://att.3dmgame.com/att/common/f3/common_2644_icon.jpg"},{"plateId":2652,"plateName":"《西游七十二变》","plateImage":"https://att.3dmgame.com/att/common/0c/common_2652_icon.jpg"}]
         */

        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private int plateTypeId;
            private String plateTypeName;
            /**
             * plateId : 2042
             * plateName : 《合体三国》
             * plateImage : https://att.3dmgame.com/att/common/71/common_2042_icon.png
             */

            private List<PlateTypeListBean> plateTypeList;

            public int getPlateTypeId() {
                return plateTypeId;
            }

            public void setPlateTypeId(int plateTypeId) {
                this.plateTypeId = plateTypeId;
            }

            public String getPlateTypeName() {
                return plateTypeName;
            }

            public void setPlateTypeName(String plateTypeName) {
                this.plateTypeName = plateTypeName;
            }

            public List<PlateTypeListBean> getPlateTypeList() {
                return plateTypeList;
            }

            public void setPlateTypeList(List<PlateTypeListBean> plateTypeList) {
                this.plateTypeList = plateTypeList;
            }

            public static class PlateTypeListBean {
                private int plateId;
                private String plateName;
                private String plateImage;

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
}
