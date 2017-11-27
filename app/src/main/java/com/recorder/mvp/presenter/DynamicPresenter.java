package com.recorder.mvp.presenter;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.http.imageloader.ImageLoader;
import com.core.integration.AppManager;
import com.core.mvp.BasePresenter;
import com.core.utils.RxLifecycleUtils;
import com.recorder.mvp.contract.DynamicContract;
import com.recorder.mvp.model.entity.NewsListBean;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

@ActivityScope
public class DynamicPresenter extends BasePresenter<DynamicContract.Model, DynamicContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public DynamicPresenter(DynamicContract.Model model, DynamicContract.View rootView
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

    public void newsList(String page, String page_size) {
        mModel.newsList(page, page_size)
        .compose(RxLifecycleUtils.transformer(mRootView))
        .subscribe(new ErrorHandleSubscriber<NewsListBean>(mErrorHandler) {
            @Override
            public void onNext(NewsListBean newsListBean) {
                mRootView.showNewsList(newsListBean.getData());
            }
        });
    }
}
