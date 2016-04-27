/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.view;

import com.poomoo.model.base.BaseJobBO;
import com.poomoo.model.response.RJobInfoBO;

import java.util.List;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/9 11:49.
 */
public interface JobInfoView extends BaseView {
    void succeed(RJobInfoBO rJobInfoBO);

    void loadRecommendsSucceed(List<BaseJobBO> rAdBOs);
}
