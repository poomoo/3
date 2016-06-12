/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.response;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/23 16:26.
 */
public class RWxTokenBO {
    public long errcode;
    public String errmsg;
    public String access_token;
    public double expires_in;
    public String refresh_token;
    public String openid;
    public String scope;
    public String unionid;

    @Override
    public String toString() {
        return "RWxTokenBO{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", access_token='" + access_token + '\'' +
                ", expires_in='" + expires_in + '\'' +
                ", openid=" + openid +
                ", scope=" + scope +
                '}';
    }
}
