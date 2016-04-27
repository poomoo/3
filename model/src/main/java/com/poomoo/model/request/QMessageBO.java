/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.request;

import com.poomoo.model.base.BaseRequestBO;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/27 11:59.
 */
public class QMessageBO extends BaseRequestBO {
    public int userId;
    public int msgId;

    public QMessageBO(String bizName, String method, int userId, int msgId) {
        super(bizName, method);
        this.userId = userId;
        this.msgId = msgId;
    }
}
