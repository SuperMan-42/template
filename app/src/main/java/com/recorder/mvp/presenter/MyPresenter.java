package com.recorder.mvp.presenter;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.http.imageloader.ImageLoader;
import com.core.integration.AppManager;
import com.core.mvp.BasePresenter;
import com.core.utils.RxLifecycleUtils;
import com.recorder.mvp.contract.MyContract;
import com.recorder.mvp.model.entity.UserInfoBean;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

@ActivityScope
public class MyPresenter extends BasePresenter<MyContract.Model, MyContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public MyPresenter(MyContract.Model model, MyContract.View rootView
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

    public void userInfo() {
        mModel.userInfo().compose(RxLifecycleUtils.transformer(mRootView))
                .subscribe(new ErrorHandleSubscriber<UserInfoBean>(mErrorHandler) {
                    @Override
                    public void onNext(UserInfoBean userInfoBean) {
                        mRootView.showUserInfo(mImageLoader, userInfoBean);
                    }

                    @Override
                    public void onError(Throwable t) {
                        mRootView.showUserInfo(mImageLoader, null);
                    }
                });
    }
}
