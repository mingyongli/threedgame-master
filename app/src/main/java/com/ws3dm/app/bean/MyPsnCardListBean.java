package com.ws3dm.app.bean;

import java.util.List;

public class MyPsnCardListBean {

    /**
     * code : 1
     * data : {"list":[{"psn_prodid":13,"litpic":"http://trophy01.np.community.playstation.net/trophy/np/NPWR13567_00_003C9DDD926BC8C6F842E240C8F9BDF40B55702EB5/1DC49BB50A285877FD49F47776D3E30D3F8545A0.PNG","title":"LEGO® MARVEL Super Heroes 2","platinum":0,"gold":0,"silver":0,"bronze":0,"achieve_percent":0,"aid":0,"arcurl":"","showtype":0,"appid":0},{"psn_prodid":53,"litpic":"http://trophy01.np.community.playstation.net/trophy/np/NPWR09911_00_00D5B23142C4FC82BF45A8174C976838D045552C9F/E68D8BE39D52C51F6AFF24E1C2F04836C6F31F75.PNG","title":"龍が如く６　命の詩。","platinum":0,"gold":0,"silver":0,"bronze":24,"achieve_percent":34,"aid":0,"arcurl":"","showtype":0,"appid":0},{"psn_prodid":702110,"litpic":"http://trophy01.np.community.playstation.net/trophy/np/NPWR10613_00_00C5F2B271FED94835932B8EC69B9AC5051F4C2C3E/B5F5F97201DB0FF961BF7ED4CE7EB219E00D43C9.PNG","title":"月华剑士2","platinum":0,"gold":0,"silver":0,"bronze":0,"achieve_percent":0,"aid":3701446,"arcurl":"https://www.3dmgame.com/games/tlb2/","showtype":3,"appid":0},{"psn_prodid":19,"litpic":"http://trophy01.np.community.playstation.net/trophy/np/NPWR11465_00_00AB3D87382BEB1FBE407D2F43ED586838AAE7295C/D9EFB1844E98400D1E4753B1673910260ADB5C7B.PNG","title":"STAR WARS™ Battlefront™ II Trophies","platinum":0,"gold":1,"silver":6,"bronze":10,"achieve_percent":41,"aid":0,"arcurl":"","showtype":0,"appid":0},{"psn_prodid":26,"litpic":"http://trophy01.np.community.playstation.net/trophy/np/NPWR07942_00_006F781DB9EE3B1A96EB9472B006DA21899A916D8F/0A529D9F4EA9446B6946C0CDC64C5DD853DC79D8.PNG","title":"瑞奇与叮当","platinum":0,"gold":0,"silver":0,"bronze":0,"achieve_percent":0,"aid":0,"arcurl":"","showtype":0,"appid":0},{"psn_prodid":3,"litpic":"http://trophy01.np.community.playstation.net/trophy/np/NPWR10963_00_00A556831B1EEF6272129B68B4821D7F23A10460CF/FCF6D5129C32F6BF3C47DD20C27B17B2B4D63D61.PNG","title":"最终幻想15","platinum":0,"gold":0,"silver":0,"bronze":0,"achieve_percent":0,"aid":3518994,"arcurl":"https://www.3dmgame.com/games/ff15/","showtype":3,"appid":0},{"psn_prodid":8,"litpic":"http://trophy01.np.community.playstation.net/trophy/np/NPWR06616_00_00EB277B7EC4BA4D93F39512B611F38502780E7821/7188BC1FEF2CBD47B77122FFE78EF742011117FD.PNG","title":"The Order: 1886","platinum":0,"gold":0,"silver":0,"bronze":0,"achieve_percent":0,"aid":0,"arcurl":"","showtype":0,"appid":0},{"psn_prodid":11,"litpic":"http://trophy01.np.community.playstation.net/trophy/np/NPWR11631_00_00AF96CD661F7ECAC2E9F9DBADC3A1C0C2BBB04299/40F98318A4254F94244CB9E9D0FE1ABC3EBE75C3.PNG","title":"Monster Hunter: World","platinum":0,"gold":0,"silver":0,"bronze":25,"achieve_percent":35,"aid":0,"arcurl":"","showtype":0,"appid":0},{"psn_prodid":40,"litpic":"http://trophy01.np.community.playstation.net/trophy/np/NPWR12737_00_00462D755AD4BCA2701295EEDB4F4C46A57F959389/85DECF6D4C42ABD45747A031B409D4F474A241F1.PNG","title":"女神異聞錄５","platinum":0,"gold":0,"silver":0,"bronze":18,"achieve_percent":26,"aid":0,"arcurl":"","showtype":0,"appid":0},{"psn_prodid":46,"litpic":"http://trophy01.np.community.playstation.net/trophy/np/NPWR07221_00_00016D6BCD432E0DD797AE186F2A4B94D441B8B9ED/168C02CDB47A20E4BD583A2E0DDFEF8853173A71.PNG","title":"小小大星球3","platinum":0,"gold":0,"silver":0,"bronze":0,"achieve_percent":0,"aid":0,"arcurl":"","showtype":0,"appid":0}]}
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
         * psn_prodid : 13
         * litpic : http://trophy01.np.community.playstation.net/trophy/np/NPWR13567_00_003C9DDD926BC8C6F842E240C8F9BDF40B55702EB5/1DC49BB50A285877FD49F47776D3E30D3F8545A0.PNG
         * title : LEGO® MARVEL Super Heroes 2
         * platinum : 0
         * gold : 0
         * silver : 0
         * bronze : 0
         * achieve_percent : 0
         * aid : 0
         * arcurl :
         * showtype : 0
         * appid : 0
         */

        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private int psn_prodid;
            private String litpic;
            private String title;
            private int platinum;
            private int gold;
            private int silver;
            private int bronze;
            private int achieve_percent;
            private int aid;
            private String arcurl;
            private int showtype;
            private int appid;

            public int getPsn_prodid() {
                return psn_prodid;
            }

            public void setPsn_prodid(int psn_prodid) {
                this.psn_prodid = psn_prodid;
            }

            public String getLitpic() {
                return litpic;
            }

            public void setLitpic(String litpic) {
                this.litpic = litpic;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getPlatinum() {
                return platinum;
            }

            public void setPlatinum(int platinum) {
                this.platinum = platinum;
            }

            public int getGold() {
                return gold;
            }

            public void setGold(int gold) {
                this.gold = gold;
            }

            public int getSilver() {
                return silver;
            }

            public void setSilver(int silver) {
                this.silver = silver;
            }

            public int getBronze() {
                return bronze;
            }

            public void setBronze(int bronze) {
                this.bronze = bronze;
            }

            public int getAchieve_percent() {
                return achieve_percent;
            }

            public void setAchieve_percent(int achieve_percent) {
                this.achieve_percent = achieve_percent;
            }

            public int getAid() {
                return aid;
            }

            public void setAid(int aid) {
                this.aid = aid;
            }

            public String getArcurl() {
                return arcurl;
            }

            public void setArcurl(String arcurl) {
                this.arcurl = arcurl;
            }

            public int getShowtype() {
                return showtype;
            }

            public void setShowtype(int showtype) {
                this.showtype = showtype;
            }

            public int getAppid() {
                return appid;
            }

            public void setAppid(int appid) {
                this.appid = appid;
            }
        }
    }
}
