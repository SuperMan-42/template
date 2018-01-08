package com.recorder.mvp.presenter;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.http.imageloader.ImageLoader;
import com.core.integration.AppManager;
import com.core.mvp.BasePresenter;
import com.recorder.mvp.contract.BuyContract;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

@ActivityScope
public class BuyPresenter extends BasePresenter<BuyContract.Model, BuyContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public BuyPresenter(BuyContract.Model model, BuyContract.View rootView
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

//    public void payPay(String dealID, String amount, String payment_way) {
//        mModel.payPay(dealID, amount, payment_way)
//                .compose(RxLifecycleUtils.transformer(mRootView))
//                .flatMap(payPayBean -> Observable.create((ObservableOnSubscribe<String>) e -> CommonUtils.pay(mAppManager.getCurrentActivity(), e, payPayBean.getData())))
//                .subscribe(data -> {
//                    //处理中的情况
//                    Logger.d("buy=> " + data);
//                    mRootView.showMessage(data);
//                }, e -> {
//                    Logger.d("buy=> e " + e.getMessage() + " " + e.toString());
//                    //处理失败情况
//                }, () -> {
//                    //处理成功情况
//                    Logger.d("buy=> complete");
//                });
//    }
}
