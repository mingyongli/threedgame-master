package com.ws3dm.app.mvvm.bean;

import java.util.List;

/**
 * 用户论坛热帖,和对应版块的热帖
 */
public class PlateContentBean {

    /**
     * code : 1
     * data : {"list":[{"topicIcon":"https://att.3dmgame.com/att/common/e2/common_1156_icon.png","topicId":1156,"topicName":"《龙腾世纪3：审判》","tid":4575631,"topicTitle":"3DM轩辕精翻组制作《龙腾世纪3：审判（DA：I）》完整汉化发布贴[10月19日更新汉化补丁v4.0a 支持10-11号升级档和全部DLC 支持正版和破解版]","topicContent":"补翻DL>汉化说明：完整汉化策划：不死鸟负责人：柔软技术：大维翻译：MeowSe、MoggleMog、syboy、Niger、欧瑞拉斯、koedmber、PerseusVeil、初七、Some7、Se","topicLabel":"游戏资源","topicSendUid":1067184,"topicSendUserName":"3DMGAME","topicSendUserIcon":"https://user.3dmgame.com/data/avatar/001/06/71/84_avatar_small.jpg","topicSendTime":"2014-12-21 11:37:18","commentCount":97912,"readCount":2516864,"moved":0,"arcurl":"https://bbs.3dmgame.com/thread-4575631-1-1.html","webviewurl":"https://bbs.3dmgame.com/plugin.php?id=thread:thread&tid=4575631&fid=1156&time=1608876023&sign=375149d757109d1830f8d31347aa3afa"},{"topicIcon":"https://att.3dmgame.com/att/common/72/common_1484_icon.gif","topicId":1484,"topicName":"《永恒之柱》","tid":4408443,"topicTitle":"3DM轩辕汉化组制作《永恒之柱（Pillars of Eternity）》正式版完整汉化发布贴[4月6日更新完整汉化v6.0 支持白色远征1和2 支持正版]","topicContent":"","topicLabel":"原创","topicSendUid":1067184,"topicSendUserName":"3DMGAME","topicSendUserIcon":"https://user.3dmgame.com/data/avatar/001/06/71/84_avatar_small.jpg","topicSendTime":"2014-08-22 14:52:54","commentCount":54797,"readCount":1189131,"moved":0,"arcurl":"https://bbs.3dmgame.com/thread-4408443-1-1.html","webviewurl":"https://bbs.3dmgame.com/plugin.php?id=thread:thread&tid=4408443&fid=1484&time=1608876023&sign=f748c270afce1c0cf33964cc3232a856"},{"topicIcon":"https://att.3dmgame.com/att/common/0e/common_1412_icon.gif","topicId":1412,"topicName":"《崛起3：泰坦之王》","tid":4401202,"topicTitle":"3DM轩辕汉化组制作《崛起3：泰坦之王（Risen 3）》完整汉化发布贴[2015年8月23日更新v6.0 支持最新增强版 支持正版全部DLC 支持简繁切换]","topicContent":"","topicLabel":"原创","topicSendUid":1067184,"topicSendUserName":"3DMGAME","topicSendUserIcon":"https://user.3dmgame.com/data/avatar/001/06/71/84_avatar_small.jpg","topicSendTime":"2014-08-13 09:33:11","commentCount":19539,"readCount":465622,"moved":0,"arcurl":"https://bbs.3dmgame.com/thread-4401202-1-1.html","webviewurl":"https://bbs.3dmgame.com/plugin.php?id=thread:thread&tid=4401202&fid=1412&time=1608876023&sign=63c15d1d373164ba9f7ab82550df4ff8"},{"topicIcon":"https://att.3dmgame.com/att/common/57/common_1475_icon.gif","topicId":1475,"topicName":"《地铁：归来》","tid":4411772,"topicTitle":"3DM轩辕汉化组制作《地铁：归来（Metro Redux）》汉化发布贴[9月4日更新v3.0 修复缺字 支持升级档 内核汉化 支持正版]","topicContent":"","topicLabel":"原创","topicSendUid":1067184,"topicSendUserName":"3DMGAME","topicSendUserIcon":"https://user.3dmgame.com/data/avatar/001/06/71/84_avatar_small.jpg","topicSendTime":"2014-08-26 16:24:26","commentCount":22150,"readCount":351633,"moved":0,"arcurl":"https://bbs.3dmgame.com/thread-4411772-1-1.html","webviewurl":"https://bbs.3dmgame.com/plugin.php?id=thread:thread&tid=4411772&fid=1475&time=1608876023&sign=644b58b1736e59e2937208ccc19d7ba5"},{"topicIcon":"https://att.3dmgame.com/att/common/ef/common_1364_icon.gif","topicId":1364,"topicName":"《中土世界：魔多阴影》","tid":4491149,"topicTitle":"3DM轩辕精翻汉化组制作《中土世界：魔多阴影（Shadow of Mordor）》完整汉化发布贴[2月25日更新精翻组汉化v4.5 支持8号升级档 支持正版]","topicContent":"","topicLabel":"原创","topicSendUid":1067184,"topicSendUserName":"3DMGAME","topicSendUserIcon":"https://user.3dmgame.com/data/avatar/001/06/71/84_avatar_small.jpg","topicSendTime":"2014-10-19 06:14:57","commentCount":54204,"readCount":971873,"moved":0,"arcurl":"https://bbs.3dmgame.com/thread-4491149-1-1.html","webviewurl":"https://bbs.3dmgame.com/plugin.php?id=thread:thread&tid=4491149&fid=1364&time=1608876023&sign=0069afd2226281fbe9ff4610da4778d3"},{"topicIcon":"https://att.3dmgame.com/att/common/83/common_1177_icon.gif","topicId":1177,"topicName":"《圣域3》","tid":4392570,"topicTitle":"3DM轩辕汉化组制作《圣域3（Sacred 3）》PC版完整汉化发布贴[9月1日更新v5.0 支持8月31日的兽人角色DLC 支持正版]","topicContent":"狐狸大人，Dmp;Snow，mdi，Sie，不虎自砍手，肖正泰，威，超越常理的人工命运解放者，安楠IOOL，fonfon>校对：Om>技术：大维/dogk>美工：节操猎人使用方法：1、解压缩2、复制到","topicLabel":"原创","topicSendUid":1067184,"topicSendUserName":"3DMGAME","topicSendUserIcon":"https://user.3dmgame.com/data/avatar/001/06/71/84_avatar_small.jpg","topicSendTime":"2014-08-01 10:45:12","commentCount":19772,"readCount":335502,"moved":0,"arcurl":"https://bbs.3dmgame.com/thread-4392570-1-1.html","webviewurl":"https://bbs.3dmgame.com/plugin.php?id=thread:thread&tid=4392570&fid=1177&time=1608876023&sign=53906672e47a7bf7ef2850965169e3a4"},{"topicIcon":"https://att.3dmgame.com/att/common/a5/common_1208_icon.gif","topicId":1208,"topicName":"《消逝的光芒》","tid":4608959,"topicTitle":"3DM轩辕汉化组制作《消逝的光芒：加强版》完整汉化发布贴[3月4日更新完整汉化补丁v9.0 支持加强版14号升级档 支持信徒DLC 支持正版]","topicContent":"","topicLabel":"原创","topicSendUid":1067184,"topicSendUserName":"3DMGAME","topicSendUserIcon":"https://user.3dmgame.com/data/avatar/001/06/71/84_avatar_small.jpg","topicSendTime":"2015-01-27 17:52:02","commentCount":100054,"readCount":2369070,"moved":0,"arcurl":"https://bbs.3dmgame.com/thread-4608959-1-1.html","webviewurl":"https://bbs.3dmgame.com/plugin.php?id=thread:thread&tid=4608959&fid=1208&time=1608876023&sign=85dbfb19991ae77c80b7ea1e87203fdf"},{"topicIcon":"https://att.3dmgame.com/att/common/1d/common_1221_icon.gif","topicId":1221,"topicName":"《墨西哥英雄大混战》","tid":4408442,"topicTitle":"3DM轩辕汉化组制作《墨西哥英雄大混战：超级漩涡冠军版（Guacamelee! ）》汉化发布贴[单独汉化补丁及汉化硬盘版均已放出 内核汉化 支持正版]","topicContent":"狐狸大人，Dmp;Snow，mdi，Sie，不虎自砍手，肖正泰，威，超越常理的人工命运解放者，安楠IOOL，fonfon>校对：Om>技术：>美工：节操猎人使用方法：1、解压缩2、复制到游戏目录运行更","topicLabel":"原创","topicSendUid":1067184,"topicSendUserName":"3DMGAME","topicSendUserIcon":"https://user.3dmgame.com/data/avatar/001/06/71/84_avatar_small.jpg","topicSendTime":"2014-08-22 14:52:30","commentCount":2870,"readCount":77025,"moved":0,"arcurl":"https://bbs.3dmgame.com/thread-4408442-1-1.html","webviewurl":"https://bbs.3dmgame.com/plugin.php?id=thread:thread&tid=4408442&fid=1221&time=1608876023&sign=bd6d59a3076a6f6442a3d6fb2538478c"},{"topicIcon":"https://att.3dmgame.com/att/common/78/common_1477_icon.gif","topicId":1477,"topicName":"《命运之手》","tid":4395176,"topicTitle":"3DM轩辕汉化组制作《命运之手（Hand of Fate）》完整汉化发布贴[15年5月26日更新v3.0 支持正式版v1.1和DLC 内核汉化 支持正版]","topicContent":"","topicLabel":"原创","topicSendUid":1067184,"topicSendUserName":"3DMGAME","topicSendUserIcon":"https://user.3dmgame.com/data/avatar/001/06/71/84_avatar_small.jpg","topicSendTime":"2014-08-04 15:04:32","commentCount":29925,"readCount":587340,"moved":0,"arcurl":"https://bbs.3dmgame.com/thread-4395176-1-1.html","webviewurl":"https://bbs.3dmgame.com/plugin.php?id=thread:thread&tid=4395176&fid=1477&time=1608876023&sign=9e39d2eb0c91a4bcde3b9a4f057689eb"},{"topicIcon":"https://att.3dmgame.com/att/common/23/common_1416_icon.gif","topicId":1416,"topicName":"《国王的恩赐：黑暗面》","tid":4389087,"topicTitle":"3DM轩辕汉化组制作《国王的恩赐：黑暗面（Dark Side）》汉化发布贴[8月20日更新v2.0 支持v1.5.1047版 校对文本 支持正版]","topicContent":"","topicLabel":"原创","topicSendUid":1067184,"topicSendUserName":"3DMGAME","topicSendUserIcon":"https://user.3dmgame.com/data/avatar/001/06/71/84_avatar_small.jpg","topicSendTime":"2014-07-27 07:45:43","commentCount":728,"readCount":284101,"moved":0,"arcurl":"https://bbs.3dmgame.com/thread-4389087-1-1.html","webviewurl":"https://bbs.3dmgame.com/plugin.php?id=thread:thread&tid=4389087&fid=1416&time=1608876023&sign=2374f7f4587ce0f504dc312e14e5c960"}]}
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
        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String topicIcon;
            private int topicId;
            private String topicName;
            private int tid;
            private String topicTitle;
            private String topicContent;
            private String topicLabel;
            private int topicSendUid;
            private String topicSendUserName;
            private String topicSendUserIcon;
            private String topicSendTime;
            private int commentCount;
            private int readCount;
            private int moved;
            private String arcurl;
            private String webviewurl;

            public String getTopicIcon() {
                return topicIcon;
            }

            public void setTopicIcon(String topicIcon) {
                this.topicIcon = topicIcon;
            }

            public int getTopicId() {
                return topicId;
            }

            public void setTopicId(int topicId) {
                this.topicId = topicId;
            }

            public String getTopicName() {
                return topicName;
            }

            public void setTopicName(String topicName) {
                this.topicName = topicName;
            }

            public int getTid() {
                return tid;
            }

            public void setTid(int tid) {
                this.tid = tid;
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

            public int getMoved() {
                return moved;
            }

            public void setMoved(int moved) {
                this.moved = moved;
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
