package com.recorder.mvp.presenter;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.http.imageloader.ImageLoader;
import com.core.integration.AppManager;
import com.core.integration.cache.BCache;
import com.core.mvp.BasePresenter;
import com.core.utils.Constants;
import com.core.utils.RxLifecycleUtils;
import com.google.gson.Gson;
import com.recorder.mvp.contract.LoginContract;
import com.recorder.mvp.model.entity.LoginBean;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

@ActivityScope
public class LoginPresenter extends BasePresenter<LoginContract.Model, LoginContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public LoginPresenter(LoginContract.Model model, LoginContract.View rootView
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

    public void login(String mobile, String password) {
        mModel.login(mobile, password)
                .compose(RxLifecycleUtils.transformer(mRootView, 0, 2))
                .subscribe(new ErrorHandleSubscriber<LoginBean>(mErrorHandler) {
                    @Override
                    public void onNext(LoginBean loginBean) {
                        BCache.getInstance().put(Constants.LOGIN_INFO, new Gson().toJson(loginBean));
                        mRootView.showLoginSuccess(loginBean);
                    }
                });
    }
}
