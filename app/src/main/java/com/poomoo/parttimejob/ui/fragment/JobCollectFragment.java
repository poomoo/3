package com.poomoo.parttimejob.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import com.poomoo.commlib.MyUtils;
import com.poomoo.model.base.BaseJobBO;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.adapter.BaseListAdapter;
import com.poomoo.parttimejob.adapter.JobsAdapter;
import com.poomoo.parttimejob.presentation.MyJobCollectionPresenter;
import com.poomoo.parttimejob.ui.activity.JobInfoActivity;
import com.poomoo.parttimejob.ui.base.BaseListFragment;
import com.poomoo.parttimejob.ui.custom.ErrorLayout;
import com.poomoo.parttimejob.view.MyJobCollectionView;

import java.util.List;


/**
 * 我的职位收藏
 */
public class JobCollectFragment extends BaseListFragment<BaseJobBO> implements BaseListAdapter.OnItemClickListener, BaseListAdapter.OnItemLongClickListener, MyJobCollectionView {

    private JobsAdapter adapter;
    private MyJobCollectionPresenter myJobCollectionPresenter;
    private int currPage = 1;
    private int cancelPos = 0;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
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
        mAdapter.setOnItemLongClickListener(this);

        EMPTY_DATA=ErrorLayout.NO_COLLECTED;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myJobCollectionPresenter = new MyJobCollectionPresenter(this);
        myJobCollectionPresenter.getCollectionList(application.getUserId(), mCurrentPage);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mCurrentPage = 1;
        myJobCollectionPresenter.getCollectionList(application.getUserId(), mCurrentPage);
    }

    @Override
    public void onLoading() {
        super.onLoading();
        myJobCollectionPresenter.getCollectionList(application.getUserId(), mCurrentPage);
    }

    @Override
    public void onLoadActiveClick() {
        super.onLoadActiveClick();
        mCurrentPage = 1;
        myJobCollectionPresenter.getCollectionList(application.getUserId(), mCurrentPage);
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

    @Override
    public void cancelSucceed(String msg) {
        closeProgressDialog();
        MyUtils.showToast(getActivity().getApplicationContext(), msg);
        adapter.removeItem(cancelPos);
        if (adapter.getItemCount() == 1) {
            mErrorLayout.setState(ErrorLayout.NO_COLLECTED);
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(false);
            mAdapter.setState(BaseListAdapter.STATE_HIDE);
        }
    }

    @Override
    public void cancelFailed(String msg) {
        closeProgressDialog();
        MyUtils.showToast(getActivity().getApplicationContext(), msg);
    }

    @Override
    public void onItemClick(int position, long id, View view) {
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.intent_value), adapter.getItem(position).jobId);
        openActivity(JobInfoActivity.class, bundle);
    }

    @Override
    public void onLongClick(int position, long id, View view) {
        Dialog dialog = new AlertDialog.Builder(getActivity()).setMessage("确认删除该条职位收藏信息").setNegativeButton("取消", (dialog1, which) -> {
        }).setPositiveButton("确定", (dialog1, which) -> {
            cancelPos = position;
            showProgressDialog(getString(R.string.dialog_msg));
            myJobCollectionPresenter.cancelCollect(adapter.getItem(position).jobId, application.getUserId());
        }).create();
        dialog.show();
    }
}
