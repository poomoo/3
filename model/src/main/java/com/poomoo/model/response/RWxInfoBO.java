/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.response;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/23 16:26.
 */
public class RWxInfoBO {
    public long errcode;
    public String errmsg;
    public String openid;
    public String nickname;
    public String access_token;
    public String headimgurl;
    public String unionid;

    @Override
    public String toString() {
        return "RWxInfoBO{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", openid='" + openid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", access_token='" + access_token + '\'' +
                ", headimgurl='" + headimgurl + '\'' +
                ", unionid='" + unionid + '\'' +
                '}';
    }
}
