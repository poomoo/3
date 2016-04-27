/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.request;

import com.poomoo.model.base.BaseRequestBO;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/27 11:59.
 */
public class QMessageUpBO extends BaseRequestBO {
    public int userId;
    public int msgId;
    public String content;

    public QMessageUpBO(String bizName, String method, int userId, int msgId, String content) {
        super(bizName, method);
        this.userId = userId;
        this.msgId = msgId;
        this.content = content;
    }
}
