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
    public static String LocalUrl = "http://192.168.0.104:8088/";
    public static String url = LocalUrl;


    /*action 10000*/
    public final static String USERACTION = "10000";//用户相关接口统一业务编号
    public final static String CODE = "10002";//获取验证码
    public final static String REGISTER = "10004";//注册
    public final static String LOGIN = "10005";//登录
    public final static String RESET = "10008";//重置密码

    /*action 20000*/
    public final static String JOBACTION = "20000";//职位相关统一业务编号
    public final static String RECOMMENDLIST = "20002";//推荐职位
    public final static String SEARCHLIST = "20003";//搜素职位
    public final static String JOBINFO = "20004";//职位详情
    public final static String APPLYLIST = "20007";//我的申请
    public final static String COLLECTIONLIST = "20008";//我的收藏

    /*action 30000*/
    public final static String COMMACTION = "30000";//公共接口统一业务编号
    public final static String AD = "30001";//广告
    public final static String CITY = "30002";//城市列表
}
