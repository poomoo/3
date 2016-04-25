/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.request;

import com.poomoo.model.base.BaseRequestBO;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/15 16:33.
 */
public class QUserIdBO extends BaseRequestBO {
    public int userId;//	--用户编号

    public QUserIdBO(String bizName, String method, int userId) {
        super(bizName, method);
        this.userId = userId;
    }
}
