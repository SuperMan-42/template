package com.recorder.mvp.presenter;

import android.Manifest;
import android.app.Activity;
import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.http.imageloader.ImageLoader;
import com.core.integration.AppManager;
import com.core.mvp.BasePresenter;
import com.core.utils.RxLifecycleUtils;
import com.recorder.mvp.contract.HomeContract;
import com.recorder.mvp.model.entity.DealFilter;
import com.recorder.mvp.model.entity.HomeRecommendBean;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

@ActivityScope
public class HomePresenter extends BasePresenter<HomeContract.Model, HomeContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public HomePresenter(HomeContract.Model model, HomeContract.View rootView
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

    public void dealFilter() {
        mModel.dealFilter()
                .compose(RxLifecycleUtils.transformer(mRootView))
                .subscribe(new ErrorHandleSubscriber<DealFilter>(mErrorHandler) {
                    @Override
                    public void onNext(DealFilter referFilter) {
                        mRootView.showFilter(mImageLoader, referFilter.getData());
                    }
                });
    }

    public void homeRecommend() {
        mModel.homeRecommend()
                .compose(RxLifecycleUtils.transformer(mRootView))
                .subscribe(new ErrorHandleSubscriber<HomeRecommendBean>(mErrorHandler) {
                    @Override
                    public void onNext(HomeRecommendBean homeRecommendBean) {
                        mRootView.showHomeRecomment(homeRecommendBean.getData());
                    }
                });
    }

    public void getPermissons() {
        new RxPermissions((Activity) mRootView)
                .requestEach(Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.READ_LOGS,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.SET_DEBUG_APP,
                        Manifest.permission.SYSTEM_ALERT_WINDOW,
                        Manifest.permission.GET_ACCOUNTS,
                        Manifest.permission.WRITE_APN_SETTINGS)
//                .doOnNext(granted -> {
//                    if (!granted) {
//                        throw new RuntimeException("no permission");
//                    }
//                })
                .compose(RxLifecycleUtils.transformer(mRootView))
                .subscribe(new ErrorHandleSubscriber<Permission>(mErrorHandler) {
                    @Override
                    public void onNext(Permission permission) {
//                        if (permission.granted) {
//                            mRootView.showMessage(CoreUtils.getString(mApplication, R.string.text_permission_success));
//                        } else {
//                            mRootView.showMessage(CoreUtils.getString(mApplication, R.string.text_permission_fail));
//                        }
                    }
                });
    }
}
