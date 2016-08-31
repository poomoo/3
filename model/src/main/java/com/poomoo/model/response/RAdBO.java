/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.response;

/**
 * 广告
 * 作者: 李苜菲
 * 日期: 2016/4/22 10:08.
 */
public class RAdBO {
    public int advId;
    public String picture;
    public String url;

    @Override
    public String toString() {
        return "RAdBO{" +
                "advId=" + advId +
                ", picture='" + picture + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
