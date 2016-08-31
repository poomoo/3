/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.response;

import com.poomoo.model.base.BaseJobBO;

import java.util.List;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/22 15:23.
 */
public class RJobInfoBO extends BaseJobBO {
    public int browseNum;
    public String cateName;
    public boolean isCollect;
    public List<RApplicantBO> applyList;//--职位申请报名列表
    public int jobNumber;//岗位人数
    public int jobAdmissionNumber;//职位已经录取人数

    @Override
    public String toString() {
        return "RJobInfoBO{" +
                "browseNum=" + browseNum +
                ", cateName='" + cateName + '\'' +
                ", isCollect=" + isCollect +
                ", applyList=" + applyList +
                ", jobNumber=" + jobNumber +
                ", jobAdmissionNumber=" + jobAdmissionNumber +
                '}';
    }
}
