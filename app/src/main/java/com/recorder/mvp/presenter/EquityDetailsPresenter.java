package com.recorder.mvp.presenter;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.http.imageloader.ImageLoader;
import com.core.integration.AppManager;
import com.core.mvp.BasePresenter;
import com.core.utils.CoreUtils;
import com.core.utils.RxLifecycleUtils;
import com.recorder.R;
import com.recorder.mvp.contract.EquityDetailsContract;
import com.recorder.mvp.model.entity.DealDetailBean;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

@ActivityScope
public class EquityDetailsPresenter extends BasePresenter<EquityDetailsContract.Model, EquityDetailsContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public EquityDetailsPresenter(EquityDetailsContract.Model model, EquityDetailsContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    public void dealDetail(String dealID) {
        mModel.dealDetail(dealID)
                .compose(RxLifecycleUtils.transformer(mRootView))
                .subscribe(new ErrorHandleSubscriber<DealDetailBean>(mErrorHandler) {
                    @Override
                    public void onNext(DealDetailBean dealDetailBean) {
                        mRootView.showDealDetail(dealDetailBean.getData());
                    }
                });
    }

    public void dealConsult(String dealID) {
        mModel.dealConsult(dealID)
                .compose(RxLifecycleUtils.transformer(mRootView))
                .subscribe(new ErrorHandleSubscriber<Object>(mErrorHandler) {
                    @Override
                    public void onNext(Object o) {
                        mRootView.showMessage(CoreUtils.getString(mApplication, R.string.text_deal_consult));
                    }
                });
    }

    public void dealUnfollow(String dealID) {
        mModel.dealUnfollow(dealID)
                .compose(RxLifecycleUtils.transformer(mRootView))
                .subscribe(new ErrorHandleSubscriber<Object>(mErrorHandler) {
                    @Override
                    public void onNext(Object o) {
                        mRootView.showMessage(CoreUtils.getString(mApplication, R.string.text_deal_unfollow));
                    }
                });
    }

    public void dealFollow(String dealID) {
        mModel.dealFollow(dealID)
                .compose(RxLifecycleUtils.transformer(mRootView))
                .subscribe(new ErrorHandleSubscriber<Object>(mErrorHandler) {
                    @Override
                    public void onNext(Object o) {
                        mRootView.showMessage(CoreUtils.getString(mApplication, R.string.text_deal_follow));
                    }
                });
    }
}
