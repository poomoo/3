package com.poomoo.parttimejob.view;

import com.poomoo.model.response.RServiceBO;

import java.util.List;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/27 10:03.
 */
public interface ServiceView extends BaseView {

    void succeed(List<RServiceBO> rServiceBOs);
}
