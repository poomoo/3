/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.commlib;


/**
 * 配置文件
 * 作者: 李苜菲
 * 日期: 2015/11/23 10:41.
 */
public class MyConfig {
    // 时间
    public static final int advTime = 1 * 5 * 1000;// 广告轮播时间
    public static final long SMSCOUNTDOWNTIME = 60 * 1000;//发送验证码倒计时
    public static final long COUNTDOWNTIBTERVAL = 1000;//倒计时间隔时间

    public static final String[] genders = {"男", "女"};
    public static final int AGE = 1;
    public static final int GENDER = 2;
    public static final int ACTIVE = 3;

    public static final int PAGESIZE = 10;//--页面大小，默认值10


    //String
    public static final String phoneNumEmpty = "手机号为空";
    public static final String phoneNumIllegal = "手机号不合法";
    public static final String codeEmpty = "验证码为空";
    public static final String passWordEmpty = "密码为空";
    public static final String passWordIllegal = "密码不能少于6位数";
    public static final String exitApp = "再按一次退出";


}
