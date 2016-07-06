package com.poomoo.parttimejob.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.poomoo.commlib.MyUtils;
import com.poomoo.model.Page;
import com.poomoo.model.response.RBuyBO;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.adapter.BaseListAdapter;
import com.poomoo.parttimejob.adapter.BuyListAdapter;
import com.poomoo.parttimejob.presentation.MyCommodityCollectionPresenter;
import com.poomoo.parttimejob.ui.activity.CommodityInfoActivity;
import com.poomoo.parttimejob.ui.activity.JobInfoActivity;
import com.poomoo.parttimejob.ui.base.BaseListFragment;
import com.poomoo.parttimejob.ui.custom.ErrorLayout;
import com.poomoo.parttimejob.view.MyCommodityCollectionView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.yqritc.recyclerviewflexibledivider.VerticalDividerItemDecoration;

import java.util.List;


/**
 * 我的换购收藏
 */
public class CommodityCollectFragment extends BaseListFragment<RBuyBO> implements BaseListAdapter.OnItemClickListener, BaseListAdapter.OnItemLongClickListener, MyCommodityCollectionView {

    private BuyListAdapter adapter;
    private MyCommodityCollectionPresenter myCommodityCollectionPresenter;
    private int currPage = 1;
    private int cancelPos = 0;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    protected BaseListAdapter<RBuyBO> onSetupAdapter() {
        adapter = new BuyListAdapter(getActivity(), BaseListAdapter.ONLY_FOOTER);
        return adapter;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mListView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .color(getResources().getColor(R.color.transparent))
                .size((int) getResources().getDimension(R.dimen.divider_height2))
                .build());
        mListView.addItemDecoration(new VerticalDividerItemDecoration.Builder(getActivity())
                .color(getResources().getColor(R.color.transparent))
                .size((int) getResources().getDimension(R.dimen.divider_height2))
                .build());

        mListView.setPadding(0, setDividerSize(), 0, 0);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);

        EMPTY_DATA=ErrorLayout.NO_COLLECTED;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myCommodityCollectionPresenter = new MyCommodityCollectionPresenter(this);
        myCommodityCollectionPresenter.getCollectionList(application.getUserId(), mCurrentPage);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mCurrentPage = 1;
        myCommodityCollectionPresenter.getCollectionList(application.getUserId(), mCurrentPage);
    }

    @Override
    public void onLoading() {
        super.onLoading();
        myCommodityCollectionPresenter.getCollectionList(application.getUserId(), mCurrentPage);
    }

    @Override
    public void onLoadActiveClick() {
        super.onLoadActiveClick();
        mCurrentPage = 1;
        myCommodityCollectionPresenter.getCollectionList(application.getUserId(), mCurrentPage);
    }

    @Override
    public void succeed(List<RBuyBO> list) {
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
        MyUtils.showToast(getActivity().getApplicationContext(), "取消收藏成功");
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
        MyUtils.showToast(getActivity().getApplicationContext(), "取消收藏成功");
    }

    @Override
    public void onItemClick(int position, long id, View view) {
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.intent_value), adapter.getItem(position).goodsId);
        openActivity(CommodityInfoActivity.class, bundle);
    }

    @Override
    public void onLongClick(int position, long id, View view) {
        Dialog dialog = new AlertDialog.Builder(getActivity()).setMessage("确认删除该条换购收藏").setNegativeButton("取消", (dialog1, which) -> {
        }).setPositiveButton("确定", (dialog1, which) -> {
            cancelPos = position;
            showProgressDialog(getString(R.string.dialog_msg));
            myCommodityCollectionPresenter.cancelCollect(adapter.getItem(position).goodsId, application.getUserId());
        }).create();
        dialog.show();
    }
}
