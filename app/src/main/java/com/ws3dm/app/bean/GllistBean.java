package com.ws3dm.app.bean;

import java.io.Serializable;
import java.util.List;


/**
 * Describution : 攻略list Bean
 * 
 * Author : DKjuan
 * 
 * Date : 2019/11/26 16:10
 **/
public class GllistBean implements Serializable {
    /**
     * id : 63
     * name : 副本技巧
     * is_more : 1
     * list : [{"aid":123359,"title":"阴阳师御心道场","arcurl":"https://shouyou.3dmgame.com/gl/123359.html","webviewurl":"https://app.3dmgame.com/webview/gl/123359.html","showtype":2},{"aid":121216,"title":"阴阳师月之羽姬阵容","arcurl":"https://shouyou.3dmgame.com/gl/121216.html","webviewurl":"https://app.3dmgame.com/webview/gl/121216.html","showtype":2},{"aid":115219,"title":"道馆里摇铃婆怎么打","arcurl":"https://shouyou.3dmgame.com/gl/115219.html","webviewurl":"https://app.3dmgame.com/webview/gl/115219.html","showtype":2},{"aid":115209,"title":"秘闻阵容最简单通关方法","arcurl":"https://shouyou.3dmgame.com/gl/115209.html","webviewurl":"https://app.3dmgame.com/webview/gl/115209.html","showtype":2},{"aid":105733,"title":"八岐大蛇超鬼王12月24日打法","arcurl":"https://shouyou.3dmgame.com/gl/105733.html","webviewurl":"https://app.3dmgame.com/webview/gl/105733.html","showtype":2},{"aid":105090,"title":"八岐大蛇超鬼王开启方法一览","arcurl":"https://shouyou.3dmgame.com/gl/105090.html","webviewurl":"https://app.3dmgame.com/webview/gl/105090.html","showtype":2},{"aid":102395,"title":"时间停止流打法攻略","arcurl":"https://shouyou.3dmgame.com/gl/102395.html","webviewurl":"https://app.3dmgame.com/webview/gl/102395.html","showtype":2}]
     */

    private int id;
    private String name;
    private int is_more;
    private int position;
    private List<GonglueBean> list;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIs_more() {
        return is_more;
    }

    public void setIs_more(int is_more) {
        this.is_more = is_more;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<GonglueBean> getList() {
        return list;
    }

    public void setList(List<GonglueBean> list) {
        this.list = list;
    }
}
