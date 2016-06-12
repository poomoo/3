/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.request;

import com.poomoo.model.base.BaseRequestBO;

/**
 * 微信号登录
 * 作者: 李苜菲
 * 日期: 2016/4/15 16:33.
 */
public class QLoginByWXBO extends BaseRequestBO {
    public String wxNum;
    public String deviceNum;
    public int deviceType;//--设备类型，1android，2ios，3PC

    public QLoginByWXBO(String bizName, String method, String wxNum, String deviceNum, int deviceType) {
        super(bizName, method);
        this.wxNum = wxNum;
        this.deviceNum = deviceNum;
        this.deviceType = deviceType;
    }
}
