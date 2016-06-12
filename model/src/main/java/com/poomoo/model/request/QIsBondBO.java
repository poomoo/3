/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.request;

import com.poomoo.model.base.BaseRequestBO;

/**
 * 微信号是否绑定
 * 作者: 李苜菲
 * 日期: 2016/4/15 16:33.
 */
public class QIsBondBO extends BaseRequestBO {
    public String wxNum;//	--用户编号

    public QIsBondBO(String bizName, String method, String wxNum) {
        super(bizName, method);
        this.wxNum = wxNum;
    }
}
