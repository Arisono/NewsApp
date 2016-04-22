package com.news.model.db;

import java.util.List;

/**
 * Created by Administrator on 2016/4/19.
 * 分页类
 */
public class PageBean<T> {

    private int allNum;
    private int allPages;
    private int currentPage;
    private int maxResult;
    private List<T> contentlist;

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

    public List<T> getContentlist() {
        return contentlist;
    }

    public void setContentlist(List<T> contentlist) {
        this.contentlist = contentlist;
    }
}
