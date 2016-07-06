/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.request;

import com.poomoo.model.base.BaseRequestBO;

/**
 * 查询兑换商品列表
 * 作者: 李苜菲
 * 日期: 2016/7/5 15:55.
 */
public class QBuyBO extends BaseRequestBO {
    public int pageNum;
    public int pageSize;

    public QBuyBO(String bizName, String method, int pageNum, int pageSize) {
        super(bizName, method);
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
}
