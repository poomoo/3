/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.api;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/5 16:41.
 */
public class NetConfig {
    public static String RemoteUrl = "http://www.jianzhigo.cn/";
    public static String LocalUrl = "http://192.168.0.112:8088/";
    public static String url = RemoteUrl;
    public static String imageUrl = url + "lzrb/app/common/uploadPic.ajax";
    public static String wxUrl = "https://api.weixin.qq.com";


    /*action 10000*/
    public final static String USERACTION = "10000";//用户相关接口统一业务编号
    public final static String CHECK = "10001";//校验手机号是否已注册
    public final static String CODE = "10002";//获取验证码
    public final static String REGISTER = "10004";//注册
    public final static String LOGIN = "10005";//登录
    public final static String RESET = "10008";//重置密码
    public final static String RESUMEDOWN = "10009";//下载简历
    public final static String RESUMEUP = "10010";//上传简历
    public final static String INTENTIONDOWN = "10011";//求职意向
    public final static String INTENTIONUP = "10012";//求职意向
    public final static String AUTH = "10013";//实名认证
    public final static String ISBOND = "10014";//微信绑定
    public final static String BOND = "10015";//微信绑定
    public final static String LOGINBYWX = "10016";//微信登录

    /*action 20000*/
    public final static String JOBACTION = "20000";//职位相关统一业务编号
    public final static String JOBTYPE = "20001";//类型
    public final static String RECOMMENDLIST = "20002";//推荐职位
    public final static String SEARCHLIST = "20003";//搜素职位
    public final static String JOBINFO = "20004";//职位详情
    public final static String CATEJOBLIST = "20005";//按种类查询
    public final static String ALLJOBLIST = "20006";//所有的职位
    public final static String APPLYLIST = "20007";//我的申请
    public final static String COLLECTIONLIST = "20008";//我的收藏
    public final static String SIGNUP = "20010";//报名
    public final static String COLLECT = "20011";//收藏
    public final static String CANCELCOLLECT = "20012";//取消收藏

    /*action 30000*/
    public final static String COMMACTION = "30000";//公共接口统一业务编号
    public final static String AD = "30001";//广告
    public final static String CITY = "30002";//城市列表
    public final static String FEEDBACK = "30004";//反馈
    public final static String SERVICELIST = "30005";//客服列表
    public final static String MESSAGELIST = "30006";//聊天记录
    public final static String MESSAGEUP = "30007";//发送聊天
    public final static String ABOUT = "30008";//关于

}
