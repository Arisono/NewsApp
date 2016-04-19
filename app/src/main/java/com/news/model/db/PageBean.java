package com.news.model.db;

import java.util.List;

/**
 * Created by Administrator on 2016/4/19.
 * 分页类
 */
public class PageBean {
    private int allNum;
    private int allPages;
    private int currentPage;
    private int maxResult;
    private List<Object> contentlist;

    public int getAllNum() {
        return allNum;
    }

    public void setAllNum(int allNum) {
        this.allNum = allNum;
    }

    public int getAllPages() {
        return allPages;
    }

    public void setAllPages(int allPages) {
        this.allPages = allPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getMaxResult() {
        return maxResult;
    }

    public void setMaxResult(int maxResult) {
        this.maxResult = maxResult;
    }

    public List<Object> getContentlist() {
        return contentlist;
    }

    public void setContentlist(List<Object> contentlist) {
        this.contentlist = contentlist;
    }
}
