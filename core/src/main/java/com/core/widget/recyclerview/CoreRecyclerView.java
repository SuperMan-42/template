package com.core.widget.recyclerview;

import android.animation.Animator;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.core.R;
import com.core.widget.recyclerview.animation.BaseAnimation;
import com.core.widget.recyclerview.listener.OnItemClickListener;
import com.core.widget.recyclerview.pullrefreshlayout.OnRefreshListener;
import com.core.widget.recyclerview.pullrefreshlayout.SwipeToLoadLayout;

/**
 * Created by hpw on 16/11/1.
 */

public class CoreRecyclerView extends LinearLayout implements BaseQuickAdapter.RequestLoadMoreListener, OnRefreshListener {
    private RecyclerView mRecyclerView;
    private SwipeToLoadLayout mSwipeRefreshLayout;
    BaseQuickAdapter mQuickAdapter;
    addDataListener addDataListener;

    public interface addDataListener {
        void addData(int page);
    }

    private View notLoadingView;
    private static int page = 1;

    public CoreRecyclerView(Context context) {
        super(context);
        initView(context);
    }

    public CoreRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CoreRecyclerView initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_recyclerview, null);
        view.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(view);
        mSwipeRefreshLayout = findViewById(R.id.swipeLayout);
        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setRefreshEnabled(false);
        mRecyclerView = findViewById(R.id.swipe_target);
        return this;
    }

    public CoreRecyclerView init(BaseQuickAdapter mQuickAdapter) {
        init(null, mQuickAdapter);
        return this;
    }

    public CoreRecyclerView init(BaseQuickAdapter mQuickAdapter, Boolean isRefresh) {
        init(null, mQuickAdapter, true);
        return this;
    }

    public CoreRecyclerView init(RecyclerView.LayoutManager layoutManager, BaseQuickAdapter mQuickAdapter) {
        init(layoutManager, mQuickAdapter, true);
        return this;
    }

    public CoreRecyclerView init(RecyclerView.LayoutManager layoutManager, BaseQuickAdapter mQuickAdapter, Boolean isRefresh) {
        if (isRefresh != true) {
            mSwipeRefreshLayout.setVisibility(GONE);
            mRecyclerView = findViewById(R.id.rv_list1);
            mRecyclerView.setVisibility(VISIBLE);
        }
        this.mQuickAdapter = mQuickAdapter;
        mQuickAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(mQuickAdapter);
//        mRecyclerView.setHasFixedSize(true);//可能会导致自动计算高度 且让match_partent有问题
        mRecyclerView.setLayoutManager(layoutManager != null ? layoutManager : new LinearLayoutManager(getContext()));
        return this;
    }

    public CoreRecyclerView addOnItemClickListener(OnItemClickListener onItemClickListener) {
        mRecyclerView.addOnItemTouchListener(onItemClickListener);
        return this;
    }

    public void reStart() {
        page = 1;
        mQuickAdapter.getData().clear();
        mQuickAdapter.openLoadMore(mQuickAdapter.getPageSize());
        mQuickAdapter.removeAllFooterView();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        page = 1;
        mQuickAdapter.getData().clear();
        addDataListener.addData(1);
        mQuickAdapter.openLoadMore(mQuickAdapter.getPageSize());
        mQuickAdapter.removeAllFooterView();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoadMoreRequested() {
        mRecyclerView.post(() -> {
            if (mQuickAdapter.getData().size() != (page - 1) * mQuickAdapter.getPageSize()) {
                mQuickAdapter.loadComplete();
                if (notLoadingView == null) {
                    notLoadingView = LayoutInflater.from(getContext()).inflate(R.layout.ptr_footer_load_all, (ViewGroup) mRecyclerView.getParent(), false);
                }
                mQuickAdapter.addFooterView(notLoadingView);
            } else {
                addDataListener.addData(page);
            }
        });
        page += 1;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public BaseQuickAdapter getAdapter() {
        return mQuickAdapter;
    }

    public CoreRecyclerView addFooterView(View footer) {
        mQuickAdapter.addFooterView(footer);
        return this;
    }

    public CoreRecyclerView addFooterView(View footer, int index) {
        mQuickAdapter.addFooterView(footer, index);
        return this;
    }

    public CoreRecyclerView addHeaderView(View header) {
        mQuickAdapter.addHeaderView(header);
        return this;
    }

    public CoreRecyclerView addHeaderView(View header, int index) {
        mQuickAdapter.addHeaderView(header, index);
        return this;
    }

    public CoreRecyclerView addHeaderView(View header, int index, int orientation) {
        mQuickAdapter.addHeaderView(header, index, orientation);
        return this;
    }

    public CoreRecyclerView hideLoadingMore() {
        mQuickAdapter.hideLoadingMore();
        return this;
    }

    public CoreRecyclerView loadComplete() {
        mQuickAdapter.loadComplete();
        return this;
    }

    public CoreRecyclerView openLoadAnimation() {
        mQuickAdapter.openLoadAnimation();
        return this;
    }

    public CoreRecyclerView openLoadAnimation(BaseAnimation animation) {
        mQuickAdapter.openLoadAnimation(animation);
        return this;
    }

    public CoreRecyclerView openLoadAnimation(@BaseQuickAdapter.AnimationType int animationType) {
        mQuickAdapter.openLoadAnimation(animationType);
        return this;
    }

    public CoreRecyclerView openLoadMore(int pageSize) {
//        this.data = data == null ? new ArrayList<T>() : data;
        mQuickAdapter.openLoadMore(pageSize);
        mQuickAdapter.setOnLoadMoreListener(this);
        return this;
    }

    public CoreRecyclerView openLoadMore(int pageSize, addDataListener addDataListener1) {
        addDataListener = addDataListener1;
        mQuickAdapter.openLoadMore(pageSize);
        mQuickAdapter.setOnLoadMoreListener(this);
        return this;
    }

    public CoreRecyclerView remove(int position) {
        mQuickAdapter.remove(position);
        return this;
    }

    public CoreRecyclerView removeAllFooterView() {
        mQuickAdapter.removeAllFooterView();
        return this;
    }

    public CoreRecyclerView removeAllHeaderView() {
        mQuickAdapter.removeAllHeaderView();
        return this;
    }

    public CoreRecyclerView removeFooterView(View footer) {
        mQuickAdapter.removeFooterView(footer);
        return this;
    }

    public CoreRecyclerView removeHeaderView(View header) {
        mQuickAdapter.removeHeaderView(header);
        return this;
    }

    public CoreRecyclerView setDuration(int duration) {
        mQuickAdapter.setDuration(duration);
        return this;
    }

    public CoreRecyclerView setEmptyView(boolean isHeadAndEmpty, boolean isFootAndEmpty, View emptyView) {
        mQuickAdapter.setEmptyView(isHeadAndEmpty, isFootAndEmpty, emptyView);
        mQuickAdapter.notifyDataSetChanged();
        return this;
    }

    public CoreRecyclerView setEmptyView(boolean isHeadAndEmpty, View emptyView) {
        mQuickAdapter.setEmptyView(isHeadAndEmpty, emptyView);
        mQuickAdapter.notifyDataSetChanged();
        return this;
    }

    public CoreRecyclerView setEmptyView(View emptyView) {
        final LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        final ViewGroup.LayoutParams lp = emptyView.getLayoutParams();
        if (lp != null) {
            layoutParams.width = lp.width;
            layoutParams.height = lp.height;
        }
        emptyView.setLayoutParams(layoutParams);
        mQuickAdapter.setEmptyView(emptyView);
        mQuickAdapter.notifyDataSetChanged();
        return this;
    }

    public CoreRecyclerView setLoadingView(View loadingView) {
        mQuickAdapter.setLoadingView(loadingView);
        return this;
    }

    public CoreRecyclerView setLoadMoreFailedView(View view) {
        mQuickAdapter.setLoadMoreFailedView(view);
        return this;
    }

    public CoreRecyclerView setOnLoadMoreListener(BaseQuickAdapter.RequestLoadMoreListener requestLoadMoreListener) {
        mQuickAdapter.setOnLoadMoreListener(requestLoadMoreListener);
        return this;
    }

    public CoreRecyclerView showLoadMoreFailedView() {
        mQuickAdapter.showLoadMoreFailedView();
        return this;
    }

    public CoreRecyclerView startAnim(Animator anim, int index) {
        mQuickAdapter.startAnim(anim, index);
        return this;
    }

    public CoreRecyclerView openRefresh(addDataListener addDataListener) {
        this.addDataListener = addDataListener;
        mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setRefreshEnabled(true);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        return this;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setRecyclerView(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
    }

    public SwipeToLoadLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }
}

