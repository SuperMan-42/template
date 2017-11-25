package com.recorder.mvp.presenter;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.http.imageloader.ImageLoader;
import com.core.integration.AppManager;
import com.core.mvp.BasePresenter;
import com.core.utils.CoreUtils;
import com.core.utils.RxLifecycleUtils;
import com.recorder.R;
import com.recorder.mvp.contract.NewPasswordContract;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

@ActivityScope
public class NewPasswordPresenter extends BasePresenter<NewPasswordContract.Model, NewPasswordContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public NewPasswordPresenter(NewPasswordContract.Model model, NewPasswordContract.View rootView
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

    public void userForgetpwd(String mobile, String code, String password) {
        mModel.userForgetpwd(mobile, code, password)
                .compose(RxLifecycleUtils.transformer(mRootView))
                .subscribe(new ErrorHandleSubscriber<Object>(mErrorHandler) {
                    @Override
                    public void onNext(Object o) {
                        mRootView.showMessage(CoreUtils.getString(mApplication, R.string.text_forgetpwd));
                        mRootView.killMyself();
                    }
                });
    }
}
