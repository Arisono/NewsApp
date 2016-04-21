package com.news.model.db;

import java.util.List;

/**
 * Created by Administrator on 2016/4/19.
 */
public class NewsChannelEntity {
    public int ret_code;
    public int totalNum;
    public List<ChannelEntity> channelList;

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public List<ChannelEntity> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<ChannelEntity> channelList) {
        this.channelList = channelList;
    }

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }
}
