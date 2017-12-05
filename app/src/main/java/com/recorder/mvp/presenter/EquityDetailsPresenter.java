package com.recorder.mvp.presenter;

import android.app.Application;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.core.di.scope.ActivityScope;
import com.core.http.imageloader.ImageLoader;
import com.core.integration.AppManager;
import com.core.mvp.BasePresenter;
import com.core.utils.CoreUtils;
import com.core.utils.RxLifecycleUtils;
import com.core.widget.CustomPopupWindow;
import com.google.gson.Gson;
import com.recorder.R;
import com.recorder.mvp.contract.EquityDetailsContract;
import com.recorder.mvp.model.entity.DealDetailBean;
import com.recorder.mvp.model.entity.PayCheckBean;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import static com.core.http.exception.ApiErrorCode.ERROR_USER_AUTHORIZED;
import static com.core.http.exception.ApiErrorCode.ERROR_USER_INFO_AUDIT;
import static com.core.http.exception.ApiErrorCode.ERROR_USER_INFO_NOT_ALL;

@ActivityScope
public class EquityDetailsPresenter extends BasePresenter<EquityDetailsContract.Model, EquityDetailsContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public EquityDetailsPresenter(EquityDetailsContract.Model model, EquityDetailsContract.View rootView
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

    public void dealDetail(String dealID) {
        mModel.dealDetail(dealID)
                .compose(RxLifecycleUtils.transformer(mRootView))
                .subscribe(new ErrorHandleSubscriber<DealDetailBean>(mErrorHandler) {
                    @Override
                    public void onNext(DealDetailBean dealDetailBean) {
                        mRootView.showDealDetail(dealDetailBean.getData());
                    }
                });
    }

    public void dealConsult(String dealID) {
        mModel.dealConsult(dealID)
                .compose(RxLifecycleUtils.transformer(mRootView))
                .subscribe(new ErrorHandleSubscriber<Object>(mErrorHandler) {
                    @Override
                    public void onNext(Object o) {
                        mRootView.showMessage(CoreUtils.getString(mApplication, R.string.text_deal_consult));
                    }
                });
    }

    public void dealUnfollow(String dealID) {
        mModel.dealUnfollow(dealID)
                .compose(RxLifecycleUtils.transformer(mRootView))
                .subscribe(new ErrorHandleSubscriber<Object>(mErrorHandler) {
                    @Override
                    public void onNext(Object o) {
                        mRootView.showMessage(CoreUtils.getString(mApplication, R.string.text_deal_unfollow));
                    }
                });
    }

    public void dealFollow(String dealID) {
        mModel.dealFollow(dealID)
                .compose(RxLifecycleUtils.transformer(mRootView))
                .subscribe(new ErrorHandleSubscriber<Object>(mErrorHandler) {
                    @Override
                    public void onNext(Object o) {
                        mRootView.showMessage(CoreUtils.getString(mApplication, R.string.text_deal_follow));
                    }
                });
    }

    public void payCheck(String dealID) {
        mModel.payCheck(dealID)
                .compose(RxLifecycleUtils.transformer(mRootView))
                .subscribe(new ErrorHandleSubscriber<PayCheckBean>(mErrorHandler) {
                    @Override
                    public void onNext(PayCheckBean payCheckBean) {
                        switch (payCheckBean.getErrno()) {
                            case ERROR_USER_AUTHORIZED:
                                showDialog(CoreUtils.getString(mApplication, R.string.text_error_user_authorized));
                                break;
                            case ERROR_USER_INFO_NOT_ALL:
                                showDialog(CoreUtils.getString(mApplication, R.string.text_error_user_info_not_all));
                                break;
                            case ERROR_USER_INFO_AUDIT:
                                showDialog(CoreUtils.getString(mApplication, R.string.text_error_user_info_audit));
                                break;
                            case 0:
                                ARouter.getInstance().build("/app/BuyActivity").withString("payCheck", new Gson().toJson(payCheckBean)).navigation();
                                break;
                        }
                    }
                });
    }

    private void showDialog(String content) {
        CustomPopupWindow.builder().contentView(CoreUtils.inflate(mAppManager.getCurrentActivity(), R.layout.layout_dialog_two_button)).isOutsideTouch(false)
                .customListener(contentView -> {
                    ((TextView) contentView.findViewById(R.id.tv_content)).setText(content);
                    contentView.findViewById(R.id.tv_sure).setOnClickListener(view -> {
                        ARouter.getInstance().build("/app/AuthActivity").navigation();
                        CustomPopupWindow.killMySelf();
                    });
                    contentView.findViewById(R.id.tv_cancel).setOnClickListener(view -> CustomPopupWindow.killMySelf());
                }).build().show();
    }
}
