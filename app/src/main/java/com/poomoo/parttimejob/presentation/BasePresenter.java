package com.poomoo.parttimejob.presentation;

import com.poomoo.commlib.RxUtils;

import rx.subscriptions.CompositeSubscription;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/5 09:42.
 */
public class BasePresenter {

    public BasePresenter() {
    }

    protected CompositeSubscription mSubscriptions = new CompositeSubscription();

    void onCreate() {
        mSubscriptions = RxUtils.getNewCompositeSubIfUnsubscribed(mSubscriptions);
    }

    void onResume() {

    }

    void onPause() {

    }


    void onDestroy() {
        mSubscriptions.unsubscribe();
    }


}
