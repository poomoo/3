/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.response;

import java.util.ArrayList;
import java.util.List;

/**
 * 返回参数的业务模型类
 * 作者: 李苜菲
 * 日期: 2015/11/27 09:28.
 */
public class ResponseBO<T> {
    public String jsonData = "";    // "jsonData":"请求的结果集，主要正对查询功能"
    public String otherData = "";    // "otherData":"扩展结果集"
    public String msg = "";    // "msg":"请求成功",
    public int rsCode = 0;    // "rsCode":"1" 1:成功，-1：失败，-2：必要参数为空
    public T obj;
    public List<T> objList = new ArrayList<>();
    public int totalCount;

    public ResponseBO(int rsCode, String msg) {
        this.msg = msg;
        this.rsCode = rsCode;
    }

    @Override
    public String toString() {
        return "ResponseBO{" +
                "jsonData='" + jsonData + '\'' +
                ", otherData='" + otherData + '\'' +
                ", msg='" + msg + '\'' +
                ", rsCode=" + rsCode +
                ", obj=" + obj +
                ", objList=" + objList +
                ", totalCount=" + totalCount +
                '}';
    }
}
