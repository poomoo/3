/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.request;

/**
 * 重置密码
 * 作者: 李苜菲
 * 日期: 2016/4/8 14:33.
 */
public class QResetPDBO extends BaseRequestBO {
    public String tel = "";
    public String code = "";
    public String password = "";

    public QResetPDBO(String bizName, String method, String tel, String code, String passWord) {
        super(bizName, method);
        this.tel = tel;
        this.code = code;
        this.password = passWord;
    }
}
