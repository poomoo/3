package com.poomoo.model.request;

import com.poomoo.model.base.BaseRequestBO;

/**
 * Created by 李苜菲 on 2016/4/27.
 */
public class QCollectBO extends BaseRequestBO {
    public int jobId;
    public int userId;
    public int type;//--类型（1:用户收藏，2：用户浏览，3：用户分享）

    public QCollectBO(String bizName, String method, int jobId, int userId, int type) {
        super(bizName, method);
        this.jobId = jobId;
        this.userId = userId;
        this.type = type;
    }
}
