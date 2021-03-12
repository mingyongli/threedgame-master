package com.ws3dm.app.mvvm.bean;

import java.util.List;

public class ForumPlateBean {
    /**
     * code : 1
     * data : {"list":[{"topicId":"2445","topicIcon":"https://att.3dmgame.com/att/common/9e/common_2445_icon.gif","topicTitle":"【12/26：更新支持1.11】轩辕剑7 多功能修改器 V1.8.3 By 小幸","topicContent":"","topicLabel":"原创","topicSendUid":2949430,"topicSendUserName":"O2O31O312","topicSendUserIcon":"https://user.3dmgame.com/data/avatar/002/94/94/30_avatar_small.jpg","topicSendTime":"2020-10-07 17:21:04","commentCount":5620,"readCount":55859,"arcurl":"https://bbs.3dmgame.com/thread-6086465-1-1.html","webviewurl":"https://bbs.3dmgame.com/plugin.php?id=thread:thread&tid=6086465&fid=2445&time=1609126607&sign=a518e007cc6cf9fd9cbe37c6726ba31e"},{"topicId":"2445","topicIcon":"https://att.3dmgame.com/att/common/9e/common_2445_icon.gif","topicTitle":"【原创！3DM下载站/附件/网盘分流】《轩辕剑7（Xuan-Yuan Sword VII）》v1.02-v1.03 十八项修改器 By 风灵月影 [更新]","topicContent":"对应游戏版本：v1.02-v1.03/如无意外全部或部分功能也会支持其他后续版本下载地址：点击简体、繁体、Englis>按rl+S+fon>数字键1-无敌模式/无视伤害判定数字键2-无限生命数字键3-","topicLabel":"原创","topicSendUid":1621223,"topicSendUserName":"风灵月影","topicSendUserIcon":"https://user.3dmgame.com/data/avatar/001/62/12/23_avatar_small.jpg","topicSendTime":"2020-10-30 23:09:59","commentCount":4618,"readCount":40726,"arcurl":"https://bbs.3dmgame.com/thread-6095465-1-1.html","webviewurl":"https://bbs.3dmgame.com/plugin.php?id=thread:thread&tid=6095465&fid=2445&time=1609126607&sign=4801fe25c5842144367130e9851994d3"},{"topicId":"2445","topicIcon":"https://att.3dmgame.com/att/common/9e/common_2445_icon.gif","topicTitle":"【10.29.20】《轩辕剑柒（Xuan-Yuan Sword VII）》官方中文 正式版 Steam正版分流[CN/TW/EN]","topicContent":"﻿显卡:NVIDIX960或其他厂牌同性能显卡DireX版本:11存储空间:需要50GB可用空间声卡:Direudio推荐配置:操作系统:Win7／8／10(64位)处理器:Inorei77700或>","topicLabel":"资源","topicSendUid":13417540,"topicSendUserName":"闪电cc","topicSendUserIcon":"https://user.3dmgame.com/data/avatar/013/41/75/40_avatar_small.jpg","topicSendTime":"2020-10-29 06:48:38","commentCount":1749,"readCount":30319,"arcurl":"https://bbs.3dmgame.com/thread-6094690-1-1.html","webviewurl":"https://bbs.3dmgame.com/plugin.php?id=thread:thread&tid=6094690&fid=2445&time=1609126607&sign=3140fa61a300245b638e4d622d784b42"},{"topicId":"2445","topicIcon":"https://att.3dmgame.com/att/common/9e/common_2445_icon.gif","topicTitle":"【3DM攻略站】《轩辕剑7》全剧情流程图文攻略，全支线任务全收集攻略（含\u201c通关谜题解答\u201d\u201c主/支线任务\u201d\u201c宝箱/秘方收集\u201d）。【完结】","topicContent":"以下内容由，来更直观的浏览本篇的图文攻略！链接：点我进入浏览！《轩辕剑7》全剧情流程图文攻略，全支线任务全收集攻略（含\u201c通关谜题解答\u201d\u201c主/支线任务\u201d\u201c宝箱/秘方收集\u201d）。《轩辕剑7》是一款即时制角色","topicLabel":"攻略","topicSendUid":1527022,"topicSendUserName":"179960743","topicSendUserIcon":"https://user.3dmgame.com/data/avatar/001/52/70/22_avatar_small.jpg","topicSendTime":"2020-10-29 16:36:12","commentCount":1612,"readCount":24866,"arcurl":"https://bbs.3dmgame.com/thread-6094862-1-1.html","webviewurl":"https://bbs.3dmgame.com/plugin.php?id=thread:thread&tid=6094862&fid=2445&time=1609126607&sign=44ac5a4cdfc987070478740f50f1f6fd"},{"topicId":"2445","topicIcon":"https://att.3dmgame.com/att/common/9e/common_2445_icon.gif","topicTitle":"【10.07.20】《轩辕剑柒（Xuan-Yuan Sword VII）》官方中文 试玩版 Steam正版分流[CN/TW/EN]","topicContent":"﻿显卡:NVIDIX960或其他厂牌同性能显卡DireX版本:11存储空间:需要50GB可用空间声卡:Direudio推荐配置:操作系统:Win7／8／10(64位)处理器:Inorei77700或>","topicLabel":"资源","topicSendUid":13417465,"topicSendUserName":"a76743166","topicSendUserIcon":"https://user.3dmgame.com/data/avatar/013/41/74/65_avatar_small.jpg","topicSendTime":"2020-10-07 10:14:27","commentCount":755,"readCount":41111,"arcurl":"https://bbs.3dmgame.com/thread-6086286-1-1.html","webviewurl":"https://bbs.3dmgame.com/plugin.php?id=thread:thread&tid=6086286&fid=2445&time=1609126607&sign=eace040ac8d11b3b70b2686007d0af3a"}]}
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
         * topicId : 2445
         * topicIcon : https://att.3dmgame.com/att/common/9e/common_2445_icon.gif
         * topicTitle : 【12/26：更新支持1.11】轩辕剑7 多功能修改器 V1.8.3 By 小幸
         * topicContent :
         * topicLabel : 原创
         * topicSendUid : 2949430
         * topicSendUserName : O2O31O312
         * topicSendUserIcon : https://user.3dmgame.com/data/avatar/002/94/94/30_avatar_small.jpg
         * topicSendTime : 2020-10-07 17:21:04
         * commentCount : 5620
         * readCount : 55859
         * arcurl : https://bbs.3dmgame.com/thread-6086465-1-1.html
         * webviewurl : https://bbs.3dmgame.com/plugin.php?id=thread:thread&tid=6086465&fid=2445&time=1609126607&sign=a518e007cc6cf9fd9cbe37c6726ba31e
         */

        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String topicId;
            private String topicIcon;
            private String topicTitle;
            private String topicContent;
            private String topicLabel;
            private int topicSendUid;
            private String topicSendUserName;
            private String topicSendUserIcon;
            private String topicSendTime;
            private int commentCount;
            private int readCount;
            private String arcurl;
            private String webviewurl;
            private String tid;
            private String webviewnewurl;

            public String getWebviewnewurl() {
                return webviewnewurl;
            }

            public void setWebviewnewurl(String webviewnewurl) {
                this.webviewnewurl = webviewnewurl;
            }

            public String getTid() {
                return tid;
            }

            public void setTid(String tid) {
                this.tid = tid;
            }

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

            public String getTopicLabel() {
                return topicLabel;
            }

            public void setTopicLabel(String topicLabel) {
                this.topicLabel = topicLabel;
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

            public String getWebviewurl() {
                return webviewurl;
            }

            public void setWebviewurl(String webviewurl) {
                this.webviewurl = webviewurl;
            }
        }
    }
}
