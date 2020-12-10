package com.ws3dm.app.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Describution : 图鉴list Bean
 * 
 * Author : DKjuan
 * 
 * Date : 2019/11/26 16:11
 **/
public class PiclistBean implements Serializable {
    /**
     * id : 48
     * name : 式神图鉴
     * is_more : 1
     * list : [{"aid":102134,"title":"炼狱茨木童子","litpic":"https://shouyou.3dmgame.com/uploadimg/img/2018/1113/1542078565167747.jpg","arcurl":"https://shouyou.3dmgame.com/gl/102134.html","webviewurl":"https://app.3dmgame.com/webview/gl/102134.html","showtype":2},{"aid":98894,"title":"少羽大天狗","litpic":"https://shouyou.3dmgame.com/uploadimg/img/2018/0929/1538187671532293.jpg","arcurl":"https://shouyou.3dmgame.com/gl/98894.html","webviewurl":"https://app.3dmgame.com/webview/gl/98894.html","showtype":2},{"aid":134304,"title":"阴阳师全新SSR大岳丸","litpic":"https://shouyou.3dmgame.com/uploadimg/thumb/2019/0624/1561340048487.jpg","arcurl":"https://shouyou.3dmgame.com/gl/134304.html","webviewurl":"https://app.3dmgame.com/webview/gl/134304.html","showtype":2},{"aid":100799,"title":"桔梗","litpic":"https://shouyou.3dmgame.com/uploadimg/img/2018/1030/1540863422887535.jpg","arcurl":"https://shouyou.3dmgame.com/gl/100799.html","webviewurl":"https://app.3dmgame.com/webview/gl/100799.html","showtype":2},{"aid":99764,"title":"沧海一粟","litpic":"https://shouyou.3dmgame.com/uploadimg/thumb/2018/1017/1539740724354.jpg","arcurl":"https://shouyou.3dmgame.com/gl/99764.html","webviewurl":"https://app.3dmgame.com/webview/gl/99764.html","showtype":2},{"aid":98896,"title":"白藏主","litpic":"https://shouyou.3dmgame.com/uploadimg/img/2018/0929/1538187984310331.jpg","arcurl":"https://shouyou.3dmgame.com/gl/98896.html","webviewurl":"https://app.3dmgame.com/webview/gl/98896.html","showtype":2},{"aid":95992,"title":"鬼切","litpic":"https://shouyou.3dmgame.com/uploadimg/img/2018/0817/1534472942827187.jpg","arcurl":"https://shouyou.3dmgame.com/gl/95992.html","webviewurl":"https://app.3dmgame.com/webview/gl/95992.html","showtype":2},{"aid":92219,"title":"杀生丸","litpic":"https://shouyou.3dmgame.com/uploadimg/img/2018/0801/1533106601624148.png","arcurl":"https://shouyou.3dmgame.com/gl/92219.html","webviewurl":"https://app.3dmgame.com/webview/gl/92219.html","showtype":2}]
     */

    private int id;
    private String name;
    private int is_more;//是否显示更多:1是0否
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

    public List<GonglueBean> getList() {
        return list;
    }

    public void setList(List<GonglueBean> list) {
        this.list = list;
    }
}
