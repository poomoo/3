/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.poomoo.commlib.LogUtils;
import com.poomoo.commlib.MyUtils;
import com.poomoo.model.response.RMessageBO;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.adapter.BaseListAdapter;
import com.poomoo.parttimejob.adapter.MessageListAdapter;
import com.poomoo.parttimejob.presentation.MessagePresenter;
import com.poomoo.parttimejob.ui.base.BaseActivity;
import com.poomoo.parttimejob.view.MessageView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/27 10:49.
 */
public class MessageActivity extends BaseActivity implements MessageView {
    @Bind(R.id.recycler_message)
    RecyclerView recyclerView;
    @Bind(R.id.edt_content)
    EditText contentEdt;
    @Bind(R.id.btn_reply)
    Button replyBtn;

    private String name = "测试";
    private int msgId;
    private String content;
    private MessageListAdapter adapter;
    private MessagePresenter messagePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = getIntent().getStringExtra(getString(R.string.intent_value));
        msgId = getIntent().getIntExtra(getString(R.string.intent_msgId), -1);
        setBack();
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected String onSetTitle() {
        return name;
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_message;
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .color(getResources().getColor(R.color.transparent))
                .size((int) getResources().getDimension(R.dimen.divider_height5))
                .build());
        adapter = new MessageListAdapter(this, BaseListAdapter.NEITHER);
        recyclerView.setAdapter(adapter);
        messagePresenter = new MessagePresenter(this);
        showProgressDialog(getString(R.string.dialog_msg));
        messagePresenter.getMessageList(application.getUserId(), msgId);

        contentEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                LogUtils.d(TAG, "afterTextChanged");
                if (s.length() > 0) {
                    replyBtn.setClickable(true);
                    replyBtn.setBackgroundResource(R.drawable.selector_btn_common);
                } else {
                    replyBtn.setClickable(false);
                    replyBtn.setBackgroundResource(R.drawable.selector_btn_reply);
                }
            }
        });
    }

    /**
     * 回复
     *
     * @param view
     */
    public void toReply(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        content = contentEdt.getText().toString().trim();
        messagePresenter.putMessage(application.getUserId(), msgId, content);
    }

    @Override
    public void downSucceed(List<RMessageBO> rMessageBOList) {
        closeProgressDialog();
//        for (int i = 0; i < 10; i++) {
//            RMessageBO rMessageBO = new RMessageBO();
//            if (i % 2 == 0) {
//                rMessageBO.isAdminReply = true;
//                rMessageBO.content = "我是客服" + i;
//                rMessageBO.replyDt = "13:" + (25 + i);
//            } else {
//                rMessageBO.isAdminReply = false;
//                rMessageBO.content = "我是自己" + i;
//                rMessageBO.replyDt = "13:" + (25 + i);
//            }
//            rMessageBOList.add(rMessageBO);
//        }
        if (rMessageBOList == null) return;
        adapter.clear();
        LogUtils.d(TAG, "rMessageBOList:" + rMessageBOList + "len:" + rMessageBOList.size());
        adapter.addItems(0, rMessageBOList);
        recyclerView.smoothScrollToPosition(rMessageBOList.size() - 1);
    }

    @Override
    public void upSucceed(String msg) {
//        RMessageBO rMessageBO = new RMessageBO();
//        rMessageBO.content = content;
//        rMessageBO.isAdminReply = false;
//        rMessageBO.msgId = msgId;
//        rMessageBO.replyDt = "16:33";
//        adapter.getDataSet().add(rMessageBO);
//        adapter.notifyDataSetChanged();
        messagePresenter.getMessageList(application.getUserId(), msgId);
        contentEdt.setText("");
    }

    @Override
    public void upFailed(String msg) {
        MyUtils.showToast(getApplicationContext(), msg);
    }

    @Override
    public void failed(String msg) {
        closeProgressDialog();
        MyUtils.showToast(getApplicationContext(), msg);
        finish();
    }
}
