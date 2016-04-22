/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.request;

import com.poomoo.model.base.BaseRequestBO;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/22 14:44.
 */
public class QSearchBO extends BaseRequestBO {
    public String searchContent;
    public int pageNum;
    public int pageSize;

    public QSearchBO(String bizName, String method, String searchContent, int pageNum, int pageSize) {
        super(bizName, method);
        this.searchContent = searchContent;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
}
