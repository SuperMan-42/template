package com.recorder.mvp.presenter;

import android.Manifest;
import android.app.Activity;
import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.core.di.scope.ActivityScope;
import com.core.http.imageloader.ImageLoader;
import com.core.integration.AppManager;
import com.core.mvp.BasePresenter;
import com.core.utils.RxLifecycleUtils;
import com.recorder.R;
import com.recorder.mvp.contract.SplashContract;
import com.recorder.mvp.model.entity.AppStartBean;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

@ActivityScope
public class SplashPresenter extends BasePresenter<SplashContract.Model, SplashContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public SplashPresenter(SplashContract.Model model, SplashContract.View rootView
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

    public void appStart() {
        mModel.appStart()
                .compose(RxLifecycleUtils.transformer(mRootView))
                .flatMap((Function<AppStartBean, ObservableSource<?>>) appStartBean -> {
                    mRootView.showAppStart(appStartBean.getData());
                    return Observable.timer(3000, TimeUnit.MILLISECONDS);
                })
                .compose(new RxPermissions((Activity) mRootView).ensureEach(Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION))
                .subscribe(new ErrorHandleSubscriber<Permission>(mErrorHandler) {
                    @Override
                    public void onNext(Permission permission) {
                        if (permission.granted) {
                            ARouter.getInstance().build("/app/HomeActivity").withTransition(R.anim.fade_in, R.anim.zoom_out).navigation();
                            mRootView.killMyself();
                        }
                    }
                });
    }
}
