package com.news.util.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.news.model.db.PageBean;
import com.news.model.db.PageBeanBody;
import com.news.model.db.RootEntity;

/**
 * Created by Administrator on 2016/4/20.
 */
public class ApiUtils {

    /**
     * @desc:解析新闻api数据
     * @author：Administrator on 2016/4/20 15:56
     */
    public static <T> RootEntity<T> parseNewsList(String jsonData,Class<T> mClazz) {
        JSONObject root= JSON.parseObject(jsonData);
        JSONObject body=JSON.parseObject(jsonData).getJSONObject("showapi_res_body");
        JSONObject page=JSON.parseObject(jsonData).getJSONObject("showapi_res_body").getJSONObject("pagebean");
        String contentList=JSON.parseObject(jsonData).getJSONObject("showapi_res_body").getJSONObject("pagebean").getJSONArray("contentlist").toJSONString();
        //实例化分页类
        PageBean<T> pageBean=new PageBean<T>();
        pageBean.setContentlist(JSON.parseArray(contentList, mClazz));//可能报错
        pageBean.setAllNum(page.getIntValue("allNum"));
        pageBean.setAllPages(page.getIntValue("allPages"));
        pageBean.setCurrentPage(page.getIntValue("currentPage"));
        pageBean.setMaxResult(page.getIntValue("maxResult"));
        //实例化Body类
        PageBeanBody<T> pageBeanBody=new PageBeanBody<T>();
        pageBeanBody.setPagebean(pageBean);
        pageBeanBody.setRet_code(body.getIntValue("ret_code"));
        //实例化Root类
        RootEntity<T> rootEntity=new RootEntity<T>();
        rootEntity.setShowapi_res_body(pageBeanBody);
        rootEntity.setShowapi_res_code(root.getIntValue("showapi_res_code"));
        rootEntity.setShowapi_res_error(root.getString("showapi_res_error"));
        return rootEntity;
    }
}
