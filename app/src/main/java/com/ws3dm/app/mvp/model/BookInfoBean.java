package com.ws3dm.app.mvp.model;

import java.io.Serializable;
import java.util.List;

public class BookInfoBean implements Serializable {


    /**
     * id : 0
     * provider_id : 0
     * provider_name : string
     * name : string
     * cover : string
     * author_id : 0
     * author_name : string
     * cate1_id : 0
     * cate2_id : 0
     * cate3_id : 0
     * cate1_name : string
     * cate2_name : string
     * cate3_name : string
     * intro : string
     * description : string
     * word_count : 0
     * word_count_cn : string
     * volume_count : 0
     * volume_count_cn : string
     * chapter_count : 0
     * chapter_count_cn : string
     * favorite_count : 0
     * favorite_count_cn : string
     * click_count : 0
     * click_count_cn : string
     * week_click_count : 0
     * week_click_count_cn : string
     * month_click_count : 0
     * month_click_count_cn : string
     * recommend_count : 0
     * recommend_count_cn : string
     * comment_count : 0
     * comment_count_cn : string
     * read_count : 0
     * read_count_cn : string
     * buy_count : 0
     * buy_count_cn : string
     * rank : 0
     * vip : 0
     * price : 0
     * state : 0
     * state_cn : string
     * finish : 0
     * finish_cn : string
     * finish_time : 2017-04-28T07:03:14Z
     * tags : ["string"]
     * author_tags : ["string"]
     * last_update_chapter : {"link":"string","name":"string","time":"2017-04-28T07:03:14Z"}
     * version : 0
     */

    private int id;
    private int                   provider_id;
    private String provider_name;
    private String name;
    private String cover;
    private int                   author_id;
    private String author_name;
    private int                   cate1_id;
    private int                   cate2_id;
    private int                   cate3_id;
    private String cate1_name;
    private String cate2_name;
    private String cate3_name;
    private String intro;
    private String description;
    private int                   word_count;
    private String word_count_cn;
    private int                   volume_count;
    private String volume_count_cn;
    private int                   chapter_count;
    private String chapter_count_cn;
    private int                   favorite_count;
    private String favorite_count_cn;
    private int                   click_count;
    private String click_count_cn;
    private int                   week_click_count;
    private String week_click_count_cn;
    private int                   month_click_count;
    private String month_click_count_cn;
    private int                   recommend_count;
    private String recommend_count_cn;
    private int                   comment_count;
    private String comment_count_cn;
    private int                   read_count;
    private String read_count_cn;
    private int                   buy_count;
    private String buy_count_cn;
    private double                rank;
    private int                   vip;
    private int                   price;
    private int                   state;
    private String state_cn;
    private int                   finish;
    private String finish_cn;
    private String finish_time;
    private LastUpdateChapterBean last_update_chapter;
    private int                   version;
    private List<String> tags;
    private List<String> author_tags;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(int provider_id) {
        this.provider_id = provider_id;
    }

    public String getProvider_name() {
        return provider_name;
    }

    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public int getCate1_id() {
        return cate1_id;
    }

    public void setCate1_id(int cate1_id) {
        this.cate1_id = cate1_id;
    }

    public int getCate2_id() {
        return cate2_id;
    }

    public void setCate2_id(int cate2_id) {
        this.cate2_id = cate2_id;
    }

    public int getCate3_id() {
        return cate3_id;
    }

    public void setCate3_id(int cate3_id) {
        this.cate3_id = cate3_id;
    }

    public String getCate1_name() {
        return cate1_name;
    }

    public void setCate1_name(String cate1_name) {
        this.cate1_name = cate1_name;
    }

    public String getCate2_name() {
        return cate2_name;
    }

    public void setCate2_name(String cate2_name) {
        this.cate2_name = cate2_name;
    }

    public String getCate3_name() {
        return cate3_name;
    }

    public void setCate3_name(String cate3_name) {
        this.cate3_name = cate3_name;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWord_count() {
        return word_count;
    }

    public void setWord_count(int word_count) {
        this.word_count = word_count;
    }

    public String getWord_count_cn() {
        return word_count_cn;
    }

    public void setWord_count_cn(String word_count_cn) {
        this.word_count_cn = word_count_cn;
    }

    public int getVolume_count() {
        return volume_count;
    }

    public void setVolume_count(int volume_count) {
        this.volume_count = volume_count;
    }

    public String getVolume_count_cn() {
        return volume_count_cn;
    }

    public void setVolume_count_cn(String volume_count_cn) {
        this.volume_count_cn = volume_count_cn;
    }

    public int getChapter_count() {
        return chapter_count;
    }

    public void setChapter_count(int chapter_count) {
        this.chapter_count = chapter_count;
    }

    public String getChapter_count_cn() {
        return chapter_count_cn;
    }

    public void setChapter_count_cn(String chapter_count_cn) {
        this.chapter_count_cn = chapter_count_cn;
    }

    public int getFavorite_count() {
        return favorite_count;
    }

    public void setFavorite_count(int favorite_count) {
        this.favorite_count = favorite_count;
    }

    public String getFavorite_count_cn() {
        return favorite_count_cn;
    }

    public void setFavorite_count_cn(String favorite_count_cn) {
        this.favorite_count_cn = favorite_count_cn;
    }

    public int getClick_count() {
        return click_count;
    }

    public void setClick_count(int click_count) {
        this.click_count = click_count;
    }

    public String getClick_count_cn() {
        return click_count_cn;
    }

    public void setClick_count_cn(String click_count_cn) {
        this.click_count_cn = click_count_cn;
    }

    public int getWeek_click_count() {
        return week_click_count;
    }

    public void setWeek_click_count(int week_click_count) {
        this.week_click_count = week_click_count;
    }

    public String getWeek_click_count_cn() {
        return week_click_count_cn;
    }

    public void setWeek_click_count_cn(String week_click_count_cn) {
        this.week_click_count_cn = week_click_count_cn;
    }

    public int getMonth_click_count() {
        return month_click_count;
    }

    public void setMonth_click_count(int month_click_count) {
        this.month_click_count = month_click_count;
    }

    public String getMonth_click_count_cn() {
        return month_click_count_cn;
    }

    public void setMonth_click_count_cn(String month_click_count_cn) {
        this.month_click_count_cn = month_click_count_cn;
    }

    public int getRecommend_count() {
        return recommend_count;
    }

    public void setRecommend_count(int recommend_count) {
        this.recommend_count = recommend_count;
    }

    public String getRecommend_count_cn() {
        return recommend_count_cn;
    }

    public void setRecommend_count_cn(String recommend_count_cn) {
        this.recommend_count_cn = recommend_count_cn;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public String getComment_count_cn() {
        return comment_count_cn;
    }

    public void setComment_count_cn(String comment_count_cn) {
        this.comment_count_cn = comment_count_cn;
    }

    public int getRead_count() {
        return read_count;
    }

    public void setRead_count(int read_count) {
        this.read_count = read_count;
    }

    public String getRead_count_cn() {
        return read_count_cn;
    }

    public void setRead_count_cn(String read_count_cn) {
        this.read_count_cn = read_count_cn;
    }

    public int getBuy_count() {
        return buy_count;
    }

    public void setBuy_count(int buy_count) {
        this.buy_count = buy_count;
    }

    public String getBuy_count_cn() {
        return buy_count_cn;
    }

    public void setBuy_count_cn(String buy_count_cn) {
        this.buy_count_cn = buy_count_cn;
    }

    public double getRank() {
        return rank;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getState_cn() {
        return state_cn;
    }

    public void setState_cn(String state_cn) {
        this.state_cn = state_cn;
    }

    public int getFinish() {
        return finish;
    }

    public void setFinish(int finish) {
        this.finish = finish;
    }

    public String getFinish_cn() {
        return finish_cn;
    }

    public void setFinish_cn(String finish_cn) {
        this.finish_cn = finish_cn;
    }

    public String getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(String finish_time) {
        this.finish_time = finish_time;
    }

    public LastUpdateChapterBean getLast_update_chapter() {
        return last_update_chapter;
    }

    public void setLast_update_chapter(LastUpdateChapterBean last_update_chapter) {
        this.last_update_chapter = last_update_chapter;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getAuthor_tags() {
        return author_tags;
    }

    public void setAuthor_tags(List<String> author_tags) {
        this.author_tags = author_tags;
    }

    public static class LastUpdateChapterBean {

        /**
         * link : string
         * name : string
         * time : 2017-04-28T07:03:14Z
         */

        private String link;
        private String name;
        private String time;

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
