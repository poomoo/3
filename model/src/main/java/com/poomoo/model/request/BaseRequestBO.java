/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.request;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/5 16:38.
 */
public class BaseRequestBO {
    public String bizName;//业务名称
    public String method;//业务下边具体的方法

    public BaseRequestBO(String bizName, String method) {
        this.bizName = bizName;
        this.method = method;
    }
}
