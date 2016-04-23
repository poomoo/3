/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.request;

import com.poomoo.model.base.BaseRequestBO;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/23 16:23.
 */
public class QJobTypeBO extends BaseRequestBO {
    public int type;//--1:大类别（首页显示），2小类别：其他页面显示

    public QJobTypeBO(String bizName, String method, int type) {
        super(bizName, method);
        this.type = type;
    }
}
