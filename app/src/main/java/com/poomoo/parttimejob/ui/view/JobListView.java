/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.view;

import com.poomoo.model.response.RJobBO;

import java.util.List;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/15 16:30.
 */
public interface JobListView {
    void succeed(List<RJobBO> list);

    void failed(String msg);
}
