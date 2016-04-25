/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.view;

import com.poomoo.model.base.BaseJobBO;
import com.poomoo.model.response.RTypeBO;

import java.util.List;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/23 15:53.
 */
public interface JobView extends BaseView {
    void type(List<RTypeBO> rTypeBOs);

    void succeed(List<BaseJobBO> baseJobBOs);
}
