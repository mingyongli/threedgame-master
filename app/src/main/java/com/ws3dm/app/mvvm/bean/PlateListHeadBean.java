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

                /**
                 * topicId : 5587896
                 * topicIcon : https://att.3dmgame.com/att/common/e0/common_1896_icon.gif
                 * topicTitle : 《生化奇兵：合集（BioShock™ Remastered）》版区索引
                 * topicContent : 游戏名称：生化奇兵：合集英文名称：BioSk:e>游戏类型：第一人称射击类(FPS)游戏游戏制作：Irrionmes、2KMon、BlindSquirrelG>游戏发行：2KG>游戏平台：P>发售时间
                 * topicLable : 置顶
                 * topicSendUid : 9688990
                 * topicSendUserName : ◕小莫◕
                 * topicSendUserIcon : https://user.3dmgame.com/data/avatar/009/68/89/90_avatar_small.jpg
                 * topicSendTime : 2017-05-31 07:00:14
                 * commentCount : 5
                 * readCount : 4976
                 * arcurl : https://bbs.3dmgame.com/thread-5587896-1-1.html
                 * tid : 5587896
                 * webviewurl : https://bbs.3dmgame.com/plugin.php?id=thread:newthread&tid=5587896&fid=1896&time=1615442201&sign=bb3adc5cfc6acd499bd7587b378868b3
                 */

                private String topicId;
                private String topicIcon;
                private String topicTitle;
                private String topicContent;
                private String topicLable;
                private int topicSendUid;
                private String topicSendUserName;
                private String topicSendUserIcon;
                private String topicSendTime;
                private int commentCount;
                private int readCount;
                private String arcurl;
                private String tid;
                private String webviewurl;

                public String getTopicId() {
                    return topicId;
                }

                public void setTopicId(String topicId) {
                    this.topicId = topicId;
                }

                public String getTopicIcon() {
                    return topicIcon;
                }

                public void setTopicIcon(String topicIcon) {
                    this.topicIcon = topicIcon;
                }

                public String getTopicTitle() {
                    return topicTitle;
                }

                public void setTopicTitle(String topicTitle) {
                    this.topicTitle = topicTitle;
                }

                public String getTopicContent() {
                    return topicContent;
                }

                public void setTopicContent(String topicContent) {
                    this.topicContent = topicContent;
                }

                public String getTopicLable() {
                    return topicLable;
                }

                public void setTopicLable(String topicLable) {
                    this.topicLable = topicLable;
                }

                public int getTopicSendUid() {
                    return topicSendUid;
                }

                public void setTopicSendUid(int topicSendUid) {
                    this.topicSendUid = topicSendUid;
                }

                public String getTopicSendUserName() {
                    return topicSendUserName;
                }

                public void setTopicSendUserName(String topicSendUserName) {
                    this.topicSendUserName = topicSendUserName;
                }

                public String getTopicSendUserIcon() {
                    return topicSendUserIcon;
                }

                public void setTopicSendUserIcon(String topicSendUserIcon) {
                    this.topicSendUserIcon = topicSendUserIcon;
                }

                public String getTopicSendTime() {
                    return topicSendTime;
                }

                public void setTopicSendTime(String topicSendTime) {
                    this.topicSendTime = topicSendTime;
                }

                public int getCommentCount() {
                    return commentCount;
                }

                public void setCommentCount(int commentCount) {
                    this.commentCount = commentCount;
                }

                public int getReadCount() {
                    return readCount;
                }

                public void setReadCount(int readCount) {
                    this.readCount = readCount;
                }

                public String getArcurl() {
                    return arcurl;
                }

                public void setArcurl(String arcurl) {
                    this.arcurl = arcurl;
                }

                public String getTid() {
                    return tid;
                }

                public void setTid(String tid) {
                    this.tid = tid;
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
