/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.request;

import com.poomoo.model.base.BaseRequestBO;

/**
 * 简历
 * 作者: 李苜菲
 * 日期: 2016/4/25 14:13.
 */
public class QResumeBO extends BaseRequestBO {
    public int userId;
    public String headPic;//	--头像（选填）
    public String realName;//--用户姓名（必填）
    public int sex;//--性别（必填），1男，2女
    public String height;//--身高（选填）
    public String birthday;//--生日（选填）
    public int provinceId;//--省份编号（必填）
    public int cityId;//--城市编号（必填）
    public int areaId;//--区域编号（必填）
    public String schoolName;//--学校名称（选填）
    public String email;//-邮箱（选填）
    public String qqNum;//--QQ号码（选填）
    public String contactTel;//--联系电话（必填）
    public String workResume;//--工作简历（选填）
    public String workExp;//--工作经验（必填）

    public QResumeBO(String bizName, String method, int userId, String headPic, String realName, int sex, String height, String birthday, int provinceId, int cityId, int areaId, String schoolName, String email, String qqNum, String contactTel, String workResume, String workExp) {
        super(bizName, method);
        this.userId = userId;
        this.headPic = headPic;
        this.realName = realName;
        this.sex = sex;
        this.height = height;
        this.birthday = birthday;
        this.provinceId = provinceId;
        this.cityId = cityId;
        this.areaId = areaId;
        this.schoolName = schoolName;
        this.email = email;
        this.qqNum = qqNum;
        this.contactTel = contactTel;
        this.workResume = workResume;
        this.workExp = workExp;
    }
}
