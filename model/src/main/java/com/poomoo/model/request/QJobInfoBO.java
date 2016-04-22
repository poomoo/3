/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.request;

import com.poomoo.model.base.BaseRequestBO;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/22 15:33.
 */
public class QJobInfoBO extends BaseRequestBO {
    public int jobId;

    public QJobInfoBO(String bizName, String method, int jobId) {
        super(bizName, method);
        this.jobId = jobId;
    }
}
