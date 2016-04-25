package com.poomoo.parttimejob.view;

import com.poomoo.model.base.BaseJobBO;
import com.poomoo.model.response.RAdBO;

import java.util.List;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/22 10:06.
 */
public interface MainView {
    void loadAdSucceed(List<RAdBO> rAdBOs);

    void loadRecommendsSucceed(List<BaseJobBO> rAdBOs);

    void failed(String msg);
}
