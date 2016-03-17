package com.news.net;

/**
 * Created by Arsiono on 2015/12/20.11:35
 * 监听器 HTTPurlConnection
 */
public interface HttpListener {

    /**
     * @desc: 实例变量
     * @author: Arison
     * @create: 2015/12/21 0:09
     */
    public static final int EVENT_BASE = 0x100;

    /**
     * 没有网络的信息提示
     * */
    public static final int EVENT_NOT_NETWORD = EVENT_BASE + 1;

    /**
     * 网络异常的信息提示
     * */
    public static final int EVENT_NETWORD_EEEOR = EVENT_BASE + 2;

    /**
     * 获取网络数据失败
     * */
    public static final int EVENT_GET_DATA_EEEOR = EVENT_BASE + 3;

    /**
     * 获取网络数据成功
     * */
    public static final int EVENT_GET_DATA_SUCCESS = EVENT_BASE + 4;
    /**
     * 获取网络数据成功
     * */
    public static final int EVENT_CLOSE_SOCKET = EVENT_BASE + 5;

    public void action(int actionCode, Object object);
}
