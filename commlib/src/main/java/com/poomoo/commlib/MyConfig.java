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
    public static final String emailIllegal = "邮箱不合法";
    public static final String codeEmpty = "验证码为空";
    public static final String passWordEmpty = "密码为空";
    public static final String passWordIllegal = "密码不能少于6位数";
    public static final String contentEmpty = "内容为空";
    public static final String nameEmpty = "姓名为空";
    public static final String provinceEmpty = "请选择省份";
    public static final String cityEmpty = "请选择城市";
    public static final String areaEmpty = "请选择区域";
    public static final String workExpEmpty = "工作经验为空";
    public static final String schoolNameEmpty = "学校为空";
    public static final String dateEmpty = "入校年份为空";
    public static final String idNumEmpty = "身份证号为空";
    public static final String idNumIllegal = "身份证号不合法";
    public static final String exitApp = "再按一次退出";
    public static final String pleaseLogin = "您还没有登录,请先登录";

    public static final String pushKey = "SXv3nlH4o3f30FHeTqGrf9cv";


    public static final String[] jobType = {"不限", "调研", "送餐员", "促销", "礼仪", "安保", "销售", "服务员", "零时工", "校内", "设计", "文员", "派单", "模特", "实习", "家教", "演出", "客服", "翻译", "其他"};
    public static final String[] sortType = {"综合排序", "最新发布", "离我最近"};

    public static final String weixinAppId = "wxdc670b01335d85ed";
    public static final String weixinAppSecret = "0b59c03c63c5966278f0fabd1493dae8";

    public static boolean isRun = false;//判断APP是否打开
}
