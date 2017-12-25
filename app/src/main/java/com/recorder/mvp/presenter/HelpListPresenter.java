package com.recorder.mvp.presenter;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.http.imageloader.ImageLoader;
import com.core.integration.AppManager;
import com.core.mvp.BasePresenter;
import com.core.utils.RxLifecycleUtils;
import com.recorder.mvp.contract.HelpListContract;
import com.recorder.mvp.model.entity.HelpContentBean;
import com.recorder.mvp.model.entity.HelpListBean;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

@ActivityScope
public class HelpListPresenter extends BasePresenter<HelpListContract.Model, HelpListContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public HelpListPresenter(HelpListContract.Model model, HelpListContract.View rootView
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

    public void helpList() {
        mModel.helpList()
                .compose(RxLifecycleUtils.transformer(mRootView))
                .subscribe(new ErrorHandleSubscriber<HelpListBean>(mErrorHandler) {
                    @Override
                    public void onNext(HelpListBean helpListBean) {
                        mRootView.showHelpList(helpListBean.getData());
                    }
                });
    }

    public void helpContent(HelpListBean.DataEntity.ListEntity entity) {
        mModel.helpContent(entity.getHelperID())
                .compose(RxLifecycleUtils.transformer(mRootView))
                .subscribe(new ErrorHandleSubscriber<HelpContentBean>(mErrorHandler) {
                    @Override
                    public void onNext(HelpContentBean helpContent) {
                        mRootView.showHelpContent(helpContent.getData(), entity);
                    }
                });
    }
}
