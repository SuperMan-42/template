package com.recorder.mvp.presenter;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.http.imageloader.ImageLoader;
import com.core.integration.AppManager;
import com.core.mvp.BasePresenter;
import com.core.utils.RxLifecycleUtils;
import com.orhanobut.logger.Logger;
import com.recorder.mvp.contract.CashierContract;
import com.recorder.mvp.model.entity.PayPayOffLineBean;
import com.recorder.utils.CommonUtils;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

@ActivityScope
public class CashierPresenter extends BasePresenter<CashierContract.Model, CashierContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public CashierPresenter(CashierContract.Model model, CashierContract.View rootView
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

    public void payPay(String dealID, String amount, String payment_way) {
        mModel.payPay(dealID, amount, payment_way)
                .compose(RxLifecycleUtils.transformer(mRootView))
                .flatMap(payPayBean -> Observable.create((ObservableOnSubscribe<String>) e -> CommonUtils.pay(mAppManager.getCurrentActivity(), e, payPayBean.getData())))
                .subscribe(data -> {
                    //处理中的情况
                    Logger.d("buy=> " + data);
                    mRootView.showResult(false, data);
                }, e -> {
                    Logger.d("buy=> e " + e.getMessage() + " " + e.toString());
//                    mRootView.showMessage(e.getMessage());
                }, () -> {
                    mRootView.showResult(true, amount);
                    Logger.d("buy=> complete");
                });
    }

    public void payPayOffLine(String dealID, String amount, String payment_way) {
        mModel.payPayOffLine(dealID, amount, payment_way)
                .compose(RxLifecycleUtils.transformer(mRootView))
                .subscribe(new ErrorHandleSubscriber<PayPayOffLineBean>(mErrorHandler) {
                    @Override
                    public void onNext(PayPayOffLineBean payOffLineBean) {
                        mRootView.showPayOffLine(payOffLineBean.getData());
                    }
                });
    }
}
