/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.request;

import com.poomoo.model.base.BaseRequestBO;

/**
 * 换购商品详情
 * 作者: 李苜菲
 * 日期: 2016/7/5 15:59.
 */
public class QCommodityInfoBO extends BaseRequestBO {
    public int goodsId;
    public int userId;

    public QCommodityInfoBO(String bizName, String method, int goodsId, int userId) {
        super(bizName, method);
        this.goodsId = goodsId;
        this.userId = userId;
    }
}
