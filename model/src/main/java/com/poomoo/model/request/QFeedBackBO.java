/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.request;

import com.poomoo.model.base.BaseRequestBO;

/**
 * 反馈
 * 作者: 李苜菲
 * 日期: 2016/4/25 15:28.
 */
public class QFeedBackBO extends BaseRequestBO {
    public int userId;
    public String contact;
    public String content;


    public QFeedBackBO(String bizName, String method, int userId, String contact, String content) {
        super(bizName, method);
        this.userId = userId;
        this.contact = contact;
        this.content = content;
    }
}
