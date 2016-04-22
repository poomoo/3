/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.response;

import com.poomoo.model.base.BaseJobBO;

/**
 * 我的职位申请
 * 作者: 李苜菲
 * 日期: 2016/4/15 15:25.
 */
public class RApplyJobBO extends BaseJobBO {
    public static final int JOB_ALL=0;
    public static final int JOB_SIGNED=1;
    public static final int JOB_HIRED=2;
    public static final int JOB_TOPOST=3;
    public static final int JOB_SETTLEMENT =4;

    public RApplyJobBO(String areaName, String jobId, int status, String pay, String jobName, String applyDt) {
        super.areaName = areaName;
        super.jobId = jobId;
        super.status = status;
        super.pay = pay;
        super.jobName = jobName;
        super.applyDt = applyDt;
    }
}
