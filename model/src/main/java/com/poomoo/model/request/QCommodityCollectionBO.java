/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.request;

import com.poomoo.model.base.BaseRequestBO;

/**
 * 换购商品收藏列表
 * 作者: 李苜菲
 * 日期: 2016/7/5 16:07.
 */
public class QCommodityCollectionBO extends BaseRequestBO {
    public int userId;
    public int pageNum;
    public int pageSize;

    public QCommodityCollectionBO(String bizName, String method, int userId, int pageNum, int pageSize) {
        super(bizName, method);
        this.userId = userId;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
}
