/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.request;

import com.poomoo.model.base.BaseRequestBO;

/**
 * 推荐职位
 * 作者: 李苜菲
 * 日期: 2016/4/22 10:30.
 */
public class QRecommendBO extends BaseRequestBO {
    public String userId;
    public int cityId;
    public int pageNum;
    public int pageSize;

    public QRecommendBO(String bizName, String method, String userId, int cityId, int pageNum, int pageSize) {
        super(bizName, method);
        if (!userId.equals(""))
            this.userId = userId;
        this.cityId = cityId;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
}
