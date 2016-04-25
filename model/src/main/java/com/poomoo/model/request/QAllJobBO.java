/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.request;

import android.text.TextUtils;

import com.poomoo.model.base.BaseRequestBO;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/22 14:44.
 */
public class QAllJobBO extends BaseRequestBO {
    public double lat;//纬度
    public double lng;//经度
    public int cityId;//城市编号（必传）
    public String cateId;//类别编号
    public String areaId;//区域编号
    public int sexReq;//性别要求
    public int workSycle;//工作周期（0：不限，1：长期兼职，2：短期兼职，3：周末兼职）
    public String workday;//工作时间（多选），1上午，2下午，3晚上
    public String startWorkDt;//开始上班时间
    public int orderType;//排序类型；1综合排序，2最新发布，3离我最近
    public int pageNum;
    public int pageSize;

    public QAllJobBO(String bizName, String method, double lat, double lng, int cityId, String cateId, String areaId, int sexReq, int workSycle, String workday, String startWorkDt, int orderType, int pageNum, int pageSize) {
        super(bizName, method);
        this.lat = lat;
        this.lng = lng;
        this.cityId = cityId;
        if (!TextUtils.isEmpty(cateId))
            this.cateId = cateId;
        if (!TextUtils.isEmpty(areaId))
            this.areaId = areaId;
        this.sexReq = sexReq;
        this.workSycle = workSycle;
        if (!TextUtils.isEmpty(workday))
            this.workday = workday;
        if (!TextUtils.isEmpty(startWorkDt))
            this.startWorkDt = startWorkDt;
        this.orderType = orderType;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
}
