/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.request;

/**
 * 获取验证码
 * 作者: 李苜菲
 * 日期: 2016/4/5 16:37.
 */
public class QCodeBO extends BaseRequestBO {
    public String tel = "";//--手机号码

    public QCodeBO(String bizName, String method, String tel) {
        super(bizName, method);
        this.tel = tel;
    }
}
