/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.request;

import com.poomoo.model.base.BaseRequestBO;

/**
 * 我的申请
 * 作者: 李苜菲
 * 日期: 2016/4/15 16:33.
 */
public class QApplyBO extends BaseRequestBO {
    public int userId;//	--用户编号
    public int status;// --申请职位状态,1已经报名，2已经录用，3已经到岗，4已经结算,0表示全部
    public int pageNum;
    public int pageSize;

    public QApplyBO(String bizName, String method, int userId, int status, int pageNum, int pageSize) {
        super(bizName, method);
        this.userId = userId;
        this.status = status;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
}
