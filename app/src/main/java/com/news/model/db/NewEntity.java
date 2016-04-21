package com.news.model.db;

import java.util.List;

/**
 * Created by Administrator on 2016/4/19.
 */
public class NewEntity {

    private String channelId;

    private String channelName;

    private int chinajoy;

    private String desc;

    private List<ImageUrls> imageurls;

    private String link;

    private String long_desc;

    private String nid;

    private String pubDate;

    private String source;

    private String title;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public int getChinajoy() {
        return chinajoy;
    }

    public void setChinajoy(int chinajoy) {
        this.chinajoy = chinajoy;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<ImageUrls> getImageurls() {
        return imageurls;
    }

    public void setImageurls(List<ImageUrls> imageurls) {
        this.imageurls = imageurls;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLong_desc() {
        return long_desc;
    }

    public void setLong_desc(String long_desc) {
        this.long_desc = long_desc;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
