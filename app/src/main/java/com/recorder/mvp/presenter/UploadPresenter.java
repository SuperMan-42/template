package com.recorder.mvp.presenter;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.http.imageloader.ImageLoader;
import com.core.integration.AppManager;
import com.core.mvp.BasePresenter;
import com.core.utils.Constants;
import com.core.utils.CoreUtils;
import com.core.utils.RxLifecycleUtils;
import com.google.gson.Gson;
import com.recorder.R;
import com.recorder.mvp.contract.UploadContract;

import org.simple.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import okhttp3.MultipartBody;

@ActivityScope
public class UploadPresenter extends BasePresenter<UploadContract.Model, UploadContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public UploadPresenter(UploadContract.Model model, UploadContract.View rootView
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

    public void imageUpload(String orderID, List<MultipartBody.Part> images, boolean isOrderProof) {
        mModel.imageUpload(images)
                .compose(RxLifecycleUtils.transformer(mRootView))
                .observeOn(Schedulers.io())
                .flatMap(imageUploadBean -> mModel.orderProof(orderID, new Gson().toJson(imageUploadBean.getData().getImages())))
                .compose(RxLifecycleUtils.transformer(mRootView))
                .subscribe(new ErrorHandleSubscriber<Object>(mErrorHandler) {
                    @Override
                    public void onNext(Object object) {
                        mRootView.showMessage(CoreUtils.getString(mApplication, R.string.text_order_proof_success));
                        if (isOrderProof) {
                            EventBus.getDefault().post(object, Constants.ORDER_PROOF);
                        }
                        mRootView.killMyself();
                    }
                });
    }
}
