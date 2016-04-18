/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.base;

import com.poomoo.model.Entity;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/15 15:20.
 */
public class BaseJobBO extends Entity {
    public String jobId;//职位编号
    public String companyId;//企业编号
    public String jobName;//职位名称
    public String firstCateId;//大类别编号（一级分类）
    public String cateId;//小类别编号
    public String pay;//薪资（APP端数据显示统一使用pay字段，后台已经将单位带上）
    public String unit;//单位
    public String sexReq;//性别要求 1：男 2：女
    public String workday;//工作时间（空：不限，具体信息参照工作时间表格）
    public String startWorkDt;//开始工作时间
    public String workSycle;//工作周期（0：不限，1：长期兼职，2：短期兼职，3：周末兼职）
    public String cityId;//城市编号
    public String areaId;//区域编号
    public String lat;//纬度
    public String lng;//经度
    public String jobDesc;//职位描述（HTML富文本）
    public String contactTel;//联系电话
    public String contact;//联系人信息
    public int status;//状态（0：关闭，1：开启（默认值）），后台使用字段，APP前端不需要处理
    public String searchContent;//后台模糊搜索字段，前端不需要处理
    public String publishDt;//发布时间
    public String areaName;//区名
    public String applyDt;//申请时间
}
