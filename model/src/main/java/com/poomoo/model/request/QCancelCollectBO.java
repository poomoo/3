package com.poomoo.model.request;

import com.poomoo.model.base.BaseRequestBO;

/**
 * Created by 李苜菲 on 2016/4/27.
 */
public class QCancelCollectBO extends BaseRequestBO {
    public int jobId;
    public int userId;

    public QCancelCollectBO(String bizName, String method, int jobId, int userId) {
        super(bizName, method);
        this.jobId = jobId;
        this.userId = userId;
    }
}
