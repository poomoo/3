/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.response;

import java.io.Serializable;

/**
 * 申请人
 * 作者: 李苜菲
 * 日期: 2016/4/22 16:24.
 */
public class RApplicantBO implements Serializable {
    public int sex;//--性别
    public String schoolName;//--学校名称
    public String height;//--求职意向
    public String intention;//--求职意向
    public String nickName;//--用户昵称
    public int age;//--年龄
    public int userId;//--用户头像
    public String headPic;//--用户头像
    public String registDt;//--用户注册时间
    public String applyDt;//--用户申请时间
}
