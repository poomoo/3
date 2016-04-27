package com.poomoo.model.request;

import com.poomoo.model.base.BaseRequestBO;

/**
 * Created by 李苜菲 on 2016/4/27.
 */
public class QSignUpBO extends BaseRequestBO {
    public int jobId;
    public int userId;
    public String selfIntro;

    public QSignUpBO(String bizName, String method, int jobId, int userId, String selfIntro) {
        super(bizName, method);
        this.jobId = jobId;
        this.userId = userId;
        this.selfIntro = selfIntro;
    }
}
