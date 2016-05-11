package com.poomoo.parttimejob.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.poomoo.commlib.MyUtils;
import com.poomoo.model.base.BaseJobBO;
import com.poomoo.model.response.RApplyJobBO;
import com.poomoo.model.response.RTypeBO;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.adapter.JobsAdapter;
import com.poomoo.parttimejob.adapter.BaseListAdapter;
import com.poomoo.parttimejob.presentation.AllJobListPresenter;
import com.poomoo.parttimejob.ui.activity.JobInfoActivity;
import com.poomoo.parttimejob.ui.activity.JobListByCateActivity;
import com.poomoo.parttimejob.ui.base.BaseListFragment;
import com.poomoo.parttimejob.view.JobListView;

import java.util.List;


/**
 * 我的申请
 */
public class MyApplyFragment extends BaseListFragment<BaseJobBO> implements BaseListAdapter.OnItemClickListener, JobListView {
    public int mCatalog;

    private JobsAdapter adapter;
    private AllJobListPresenter allJobListPresenter;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mCatalog = getArguments().getInt("BUNDLE_TYPE", RApplyJobBO.JOB_ALL);
    }

    @Override
    protected BaseListAdapter<BaseJobBO> onSetupAdapter() {
        adapter = new JobsAdapter(getActivity(), BaseListAdapter.ONLY_FOOTER, true);
        return adapter;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView.setPadding(0, setDividerSize(), 0, 0);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        allJobListPresenter = new AllJobListPresenter(this);
        allJobListPresenter.getApplyList(application.getUserId(), mCatalog);
    }

    @Override
    public void onItemClick(int position, long id, View view) {
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.intent_value), adapter.getItem(position).jobId);
        openActivity(JobInfoActivity.class, bundle);
    }

    @Override
    public void succeed(List<BaseJobBO> list) {
        onLoadFinishState(action);
        onLoadResultData(list);
    }

    @Override
    public void failed(String msg) {
        if (msg.contains("检查网络"))
            onNetworkInvalid(action);
        else
            onLoadErrorState(action);
    }

    /**
     * 触发下拉刷新事件
     */
    @Override
    public void onRefresh() {
        super.onRefresh();
        allJobListPresenter.getApplyList(application.getUserId(), mCatalog);
    }


    /**
     * 再错误页面点击重新加载
     */
    @Override
    public void onLoadActiveClick() {
        super.onLoadActiveClick();
        allJobListPresenter.getApplyList(application.getUserId(), mCatalog);
    }

}
