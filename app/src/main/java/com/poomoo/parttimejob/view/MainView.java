package com.poomoo.parttimejob.view;

import com.poomoo.model.base.BaseJobBO;
import com.poomoo.model.response.RAdBO;
import com.poomoo.model.response.RJobInfoBO;
import com.poomoo.model.response.RTypeBO;

import java.util.List;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/22 10:06.
 */
public interface MainView {
    void loadAdSucceed(List<RAdBO> rAdBOs);

    void loadTypeSucceed(List<RTypeBO> rTypeBOs);

    void loadRecommendsSucceed(List<BaseJobBO> rAdBOs);

    void failed(String msg);

    void type(List<RTypeBO> rTypeBOs);
}
