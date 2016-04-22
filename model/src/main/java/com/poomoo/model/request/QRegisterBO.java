/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.request;

import com.poomoo.model.base.BaseRequestBO;

/**
 * 登录
 * 作者: 李苜菲
 * 日期: 2016/4/7 15:33.
 */
public class QRegisterBO extends BaseRequestBO {
    public String tel = "";//--手机号码(参数非空)
    public String password = "";//--密码(参数非空)
    public String code = "";//--验证码(参数非空)
    public String inviteCode = "";//--邀请码
    public String deviceNum = "";//--设备编号（能获取到就传）
    public int deviceType;//--设备类型，1android，2ios，3PC(参数非空)

    public QRegisterBO(String bizName, String method, String tel, String password, String code, String inviteCode, String deviceNum, int deviceType) {
        super(bizName, method);
        this.tel = tel;
        this.password = password;
        this.code = code;
        this.inviteCode = inviteCode;
        this.deviceNum = deviceNum;
        this.deviceType = deviceType;
    }
}
