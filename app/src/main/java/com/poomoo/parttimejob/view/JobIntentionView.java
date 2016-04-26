package com.poomoo.parttimejob.view;

import com.poomoo.model.response.RIntentionBO;

/**
 * Created by 李苜菲 on 2016/4/26.
 */
public interface JobIntentionView  {
    void UpSucceed(String msg);

    void UpFailed(String msg);

    void DownSucceed(RIntentionBO rIntentionBO);

    void DownFailed(String msg);
}
