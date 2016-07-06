/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.request;

import com.poomoo.model.base.BaseRequestBO;

/**
 * 作者: 李苜菲
 * 日期: 2016/7/5 16:03.
 */
public class QCollectCommodityBO extends BaseRequestBO {
    public int goodsId;
    public int userId;

    public QCollectCommodityBO(String bizName, String method, int goodsId, int userId) {
        super(bizName, method);
        this.goodsId = goodsId;
        this.userId = userId;
    }
}
