package com.recorder.mvp.presenter;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.core.di.scope.ActivityScope;
import com.core.http.imageloader.ImageLoader;
import com.core.integration.AppManager;
import com.core.integration.cache.BCache;
import com.core.mvp.BasePresenter;
import com.core.utils.Constants;
import com.core.utils.RxLifecycleUtils;
import com.google.gson.Gson;
import com.recorder.R;
import com.recorder.mvp.contract.SplashContract;
import com.recorder.mvp.model.entity.AppStartBean;

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
                    BCache.getInstance().put(Constants.APPSTART, new Gson().toJson(appStartBean));
                    mRootView.showAppStart(appStartBean.getData());
                    return Observable.timer(3000, TimeUnit.MILLISECONDS);
                })
                .subscribe(new ErrorHandleSubscriber<Object>(mErrorHandler) {
                    @Override
                    public void onNext(Object o) {
                        ARouter.getInstance().build("/app/HomeActivity").withTransition(R.anim.fade_in, R.anim.zoom_out).navigation();
                    }

                    @Override
                    public void onError(Throwable t) {
                        ARouter.getInstance().build("/app/HomeActivity").withTransition(R.anim.fade_in, R.anim.zoom_out).navigation();
                    }
                });
    }
}
