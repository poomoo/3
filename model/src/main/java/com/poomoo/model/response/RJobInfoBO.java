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


}
