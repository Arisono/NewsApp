package com.news.model.db;

import java.util.List;

/**
 * Created by Administrator on 2016/4/19.
 */
public class ListBean<T> {
    public int ret_code;
    public int totalNum;
    public List<T> channelList;

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public List<T> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<T> channelList) {
        this.channelList = channelList;
    }

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }
}
