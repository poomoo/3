/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.request;

import com.poomoo.model.base.BaseRequestBO;

/**
 * 校验手机号是否注册
 * 作者: 李苜菲
 * 日期: 2016/5/30 10:14.
 */
public class QCheckBO extends BaseRequestBO {
    public String tel;

    public QCheckBO(String bizName, String method, String tel) {
        super(bizName, method);
        this.tel = tel;
    }
}
