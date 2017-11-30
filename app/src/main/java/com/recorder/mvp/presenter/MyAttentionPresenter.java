package com.recorder.mvp.presenter;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.http.imageloader.ImageLoader;
import com.core.integration.AppManager;
import com.core.mvp.BasePresenter;
import com.core.utils.RxLifecycleUtils;
import com.recorder.mvp.contract.MyAttentionContract;
import com.recorder.mvp.model.entity.UserFollowListBean;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

@ActivityScope
public class MyAttentionPresenter extends BasePresenter<MyAttentionContract.Model, MyAttentionContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public MyAttentionPresenter(MyAttentionContract.Model model, MyAttentionContract.View rootView
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

    public void userFollowlist(String page, String page_size) {
        mModel.userFollowlist(page, page_size)
                .compose(RxLifecycleUtils.transformer(mRootView))
                .subscribe(new ErrorHandleSubscriber<UserFollowListBean>(mErrorHandler) {
                    @Override
                    public void onNext(UserFollowListBean userFollowListBean) {
                        mRootView.showUserFollowList(userFollowListBean.getData());
                    }
                });
    }
}
