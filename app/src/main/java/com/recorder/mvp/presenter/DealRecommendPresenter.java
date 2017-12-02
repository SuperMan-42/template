package com.recorder.mvp.presenter;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.http.imageloader.ImageLoader;
import com.core.integration.AppManager;
import com.core.mvp.BasePresenter;
import com.core.utils.CoreUtils;
import com.core.utils.RxLifecycleUtils;
import com.recorder.R;
import com.recorder.mvp.contract.DealRecommendContract;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

@ActivityScope
public class DealRecommendPresenter extends BasePresenter<DealRecommendContract.Model, DealRecommendContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public DealRecommendPresenter(DealRecommendContract.Model model, DealRecommendContract.View rootView
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

    public void dealRecommend(String deal_name, String industry, String requirement, String contact, String phone, String business, String team) {
        mModel.dealRecommend(deal_name, industry, requirement, contact, phone, business, team)
                .compose(RxLifecycleUtils.transformer(mRootView))
                .subscribe(new ErrorHandleSubscriber<Object>(mErrorHandler) {
                    @Override
                    public void onNext(Object o) {
                        mRootView.showMessage(CoreUtils.getString(mApplication, R.string.text_deal_recommend));
                        mRootView.killMyself();
                    }
                });
    }
}
