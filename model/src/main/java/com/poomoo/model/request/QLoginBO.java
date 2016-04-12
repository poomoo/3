/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.request;

/**
 * 登录
 * 作者: 李苜菲
 * 日期: 2016/4/8 09:25.
 */
public class QLoginBO extends BaseRequestBO {
    public String tel = "";
    public String password = "";
    public String deviceNum = "";//--设备编号（能获取到就传），目的是当用户更换了设备后，同步设备信息，便于推送
    public int deviceType;//--设备类型，1android，2ios，3PC(参数非空)

    public QLoginBO(String bizName, String method, String tel, String password, String deviceNum, int deviceType) {
        super(bizName, method);
        super.bizName = bizName;
        super.method = method;
        this.tel = tel;
        this.password = password;
        this.deviceNum = deviceNum;
        this.deviceType = deviceType;
    }
}
