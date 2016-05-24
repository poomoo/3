/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.request;

import com.poomoo.model.base.BaseRequestBO;

/**
 * 作者: 李苜菲
 * 日期: 2016/5/24 10:35.
 */
public class QMyCollectionBO extends BaseRequestBO {
    private int userId;
    private int pageNum;
    private int pageSize;

    public QMyCollectionBO(String bizName, String method, int userId, int pageNum, int pageSize) {
        super(bizName, method);
        this.userId = userId;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
}
