package com.news.model.db;

/**
 * Created by Administrator on 2016/4/19.
 */
public class PageBeanBody<T> {
    private PageBean<T> pagebean;
    private int ret_code;

    public PageBean<T> getPagebean() {
        return pagebean;
    }

    public void setPagebean(PageBean<T> pagebean) {
        this.pagebean = pagebean;
    }

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }
}
