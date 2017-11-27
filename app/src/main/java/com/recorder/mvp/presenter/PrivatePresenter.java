package com.recorder.mvp.presenter;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.http.imageloader.ImageLoader;
import com.core.integration.AppManager;
import com.core.mvp.BasePresenter;
import com.core.utils.RxLifecycleUtils;
import com.recorder.mvp.contract.PrivateContract;
import com.recorder.mvp.model.entity.EquityBean;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

@ActivityScope
public class PrivatePresenter extends BasePresenter<PrivateContract.Model, PrivateContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public PrivatePresenter(PrivateContract.Model model, PrivateContract.View rootView
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

    public void dealList(String type, String label_id, String round_id, String keyword, String page, String page_size) {
        mModel.dealList(type, label_id, round_id, keyword, page, page_size)
                .compose(RxLifecycleUtils.transformer(mRootView, 1, 2))
                .subscribe(new ErrorHandleSubscriber<EquityBean>(mErrorHandler) {
                    @Override
                    public void onNext(EquityBean equityBean) {
                        mRootView.showPrivate(equityBean);
                    }
                });
    }
}
