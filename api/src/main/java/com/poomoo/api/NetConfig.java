/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.api;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/5 16:41.
 */
public class NetConfig {
    public static String RemoteUrl = "http://lzrb.91jiaoyou.cn/zgqg/";
    public static String LocalUrl = "http://192.168.0.108:8088/";
    public static String url = LocalUrl;


    /*action 10000*/
    public final static String USERACTION = "10000";//用户相关接口统一业务编号
    public final static String CODE = "10002";//获取验证码
    public final static String REGISTER = "10004";//注册
    public final static String LOGIN = "10005";//登录
    public final static String RESET = "10008";//重置密码


    /*action 30000*/
    public final static String COMMACTION = "30000";//公共接口统一业务编号
    public final static String CITY = "30002";//城市列表
}
