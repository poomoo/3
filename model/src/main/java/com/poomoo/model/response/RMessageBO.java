/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.response;

import com.poomoo.model.Entity;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/27 10:53.
 */
public class RMessageBO extends Entity{
    public String content;
    public boolean isAdminReply;
    public int msgId;
    public String headPic;
    public String replyDt;
}
