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
import com.recorder.mvp.model.entity.Bean;
import com.recorder.mvp.model.entity.ImageUploadBean;
import com.recorder.mvp.ui.activity.UploadActivity;
import com.recorder.utils.CommonUtils;

import org.simple.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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

    public void upload(int type, String orderID, List<Bean<Boolean>> list, boolean isOrderProof, int position) {
        List<String> stringList = new ArrayList<>();
        Observable.fromArray(list.toArray())
                .flatMap(data -> {
                    Bean<Boolean> bean = (Bean<Boolean>) data;
                    List<MultipartBody.Part> parts = new ArrayList<>();
                    File file = new File(bean.getValue());
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part part = MultipartBody.Part.createFormData("images", file.getName(), requestBody);
                    parts.add(part);
                    return mModel.imageUpload(parts);
                })
                .compose(RxLifecycleUtils.transformer(mRootView))
                .doOnComplete(() -> {
                    Observable<?> observable = null;
                    if (isOrderProof) {
                        observable = mModel.orderProof(orderID, new Gson().toJson(stringList));
                    } else {
                        if (type == 1 || type == 2) {
                            observable = mModel.authPerson(type, null, null, null, null, null, new Gson().toJson(stringList));
                        } else if (type == 3) {
                            observable = mModel.authOrgan(null, null, null, null, null, new Gson().toJson(stringList));
                        }
                    }
                    observable.compose(RxLifecycleUtils.transformer(mRootView))
                            .subscribe(new ErrorHandleSubscriber<Object>(mErrorHandler) {
                                @Override
                                public void onNext(Object object) {
                                    CoreUtils.snackbarText(CoreUtils.getString(mApplication, R.string.text_order_proof_success));
                                    if (isOrderProof) {
                                        EventBus.getDefault().post(position, Constants.ORDER_PROOF);
                                    } else {
                                        EventBus.getDefault().post(object, Constants.AUTH_TYPE);
                                    }
                                    CommonUtils.hide(((UploadActivity) mRootView).getAvi());
                                    mRootView.killMyself();
                                }
                            });
                })
                .subscribe(new ErrorHandleSubscriber<ImageUploadBean>(mErrorHandler) {
                    @Override
                    public void onNext(ImageUploadBean imageUploadBean) {
                        stringList.add(imageUploadBean.getData().getImages().get(0));
                    }
                });
    }
}
