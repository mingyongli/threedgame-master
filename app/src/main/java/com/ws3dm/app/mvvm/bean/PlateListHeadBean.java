package com.ws3dm.app.mvvm.bean;

import java.util.List;

public class PlateListHeadBean {
    /**
     * code : 1
     * data : {"head":{"plateId":"2341","plateTitle":"《赛博朋克2077》","plateIcon":"https://att.3dmgame.com/att/common/c8/common_2341_icon.png","topicCount":"3697","plateFollowCount":"1","todayReplyCount":"3872","isFollow":0,"notice":[{"topicId":"6073608","topicTitle":"3DM热门游戏《赛博朋克2077》群：9600082 \u2014\u2014 欢迎各位玩家玩家加入。","topicLable":"置顶","webviewurl":"https://bbs.3dmgame.com/plugin.php?id=thread:thread&tid=6073608&fid=2341&time=1608881214&sign=6132fc3ff806ee57db96f57be55fab1b"}]}}
     * msg : 成功
     */

    private int code;
    /**
     * head : {"plateId":"2341","plateTitle":"《赛博朋克2077》","plateIcon":"https://att.3dmgame.com/att/common/c8/common_2341_icon.png","topicCount":"3697","plateFollowCount":"1","todayReplyCount":"3872","isFollow":0,"notice":[{"topicId":"6073608","topicTitle":"3DM热门游戏《赛博朋克2077》群：9600082 \u2014\u2014 欢迎各位玩家玩家加入。","topicLable":"置顶","webviewurl":"https://bbs.3dmgame.com/plugin.php?id=thread:thread&tid=6073608&fid=2341&time=1608881214&sign=6132fc3ff806ee57db96f57be55fab1b"}]}
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
        /**
         * plateId : 2341
         * plateTitle : 《赛博朋克2077》
         * plateIcon : https://att.3dmgame.com/att/common/c8/common_2341_icon.png
         * topicCount : 3697
         * plateFollowCount : 1
         * todayReplyCount : 3872
         * isFollow : 0
         * notice : [{"topicId":"6073608","topicTitle":"3DM热门游戏《赛博朋克2077》群：9600082 \u2014\u2014 欢迎各位玩家玩家加入。","topicLable":"置顶","webviewurl":"https://bbs.3dmgame.com/plugin.php?id=thread:thread&tid=6073608&fid=2341&time=1608881214&sign=6132fc3ff806ee57db96f57be55fab1b"}]
         */

        private HeadBean head;

        public HeadBean getHead() {
            return head;
        }

        public void setHead(HeadBean head) {
            this.head = head;
        }

        public static class HeadBean {
            private String plateId;
            private String plateTitle;
            private String plateIcon;
            private String topicCount;
            private String plateFollowCount;
            private String todayReplyCount;
            private int isFollow;
            /**
             * topicId : 6073608
             * topicTitle : 3DM热门游戏《赛博朋克2077》群：9600082 —— 欢迎各位玩家玩家加入。
             * topicLable : 置顶
             * webviewurl : https://bbs.3dmgame.com/plugin.php?id=thread:thread&tid=6073608&fid=2341&time=1608881214&sign=6132fc3ff806ee57db96f57be55fab1b
             */

            private List<NoticeBean> notice;

            public String getPlateId() {
                return plateId;
            }

            public void setPlateId(String plateId) {
                this.plateId = plateId;
            }

            public String getPlateTitle() {
                return plateTitle;
            }

            public void setPlateTitle(String plateTitle) {
                this.plateTitle = plateTitle;
            }

            public String getPlateIcon() {
                return plateIcon;
            }

            public void setPlateIcon(String plateIcon) {
                this.plateIcon = plateIcon;
            }

            public String getTopicCount() {
                return topicCount;
            }

            public void setTopicCount(String topicCount) {
                this.topicCount = topicCount;
            }

            public String getPlateFollowCount() {
                return plateFollowCount;
            }

            public void setPlateFollowCount(String plateFollowCount) {
                this.plateFollowCount = plateFollowCount;
            }

            public String getTodayReplyCount() {
                return todayReplyCount;
            }

            public void setTodayReplyCount(String todayReplyCount) {
                this.todayReplyCount = todayReplyCount;
            }

            public int getIsFollow() {
                return isFollow;
            }

            public void setIsFollow(int isFollow) {
                this.isFollow = isFollow;
            }

            public List<NoticeBean> getNotice() {
                return notice;
            }

            public void setNotice(List<NoticeBean> notice) {
                this.notice = notice;
            }

            public static class NoticeBean {
                private String topicId;
                private String topicTitle;
                private String topicLable;
                private String webviewurl;

                public String getTopicId() {
                    return topicId;
                }

                public void setTopicId(String topicId) {
                    this.topicId = topicId;
                }

                public String getTopicTitle() {
                    return topicTitle;
                }

                public void setTopicTitle(String topicTitle) {
                    this.topicTitle = topicTitle;
                }

                public String getTopicLable() {
                    return topicLable;
                }

                public void setTopicLable(String topicLable) {
                    this.topicLable = topicLable;
                }

                public String getWebviewurl() {
                    return webviewurl;
                }

                public void setWebviewurl(String webviewurl) {
                    this.webviewurl = webviewurl;
                }
            }
        }
    }
}
