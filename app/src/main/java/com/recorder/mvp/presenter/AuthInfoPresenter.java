package com.recorder.mvp.presenter;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.http.imageloader.ImageLoader;
import com.core.integration.AppManager;
import com.core.mvp.BasePresenter;
import com.core.utils.RxLifecycleUtils;
import com.google.gson.Gson;
import com.recorder.mvp.contract.AuthInfoContract;
import com.recorder.mvp.model.entity.AuthGetBean;
import com.recorder.mvp.model.entity.Bean;
import com.recorder.mvp.model.entity.ImageUploadBean;

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
public class AuthInfoPresenter extends BasePresenter<AuthInfoContract.Model, AuthInfoContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public AuthInfoPresenter(AuthInfoContract.Model model, AuthInfoContract.View rootView
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

    public void authGet(int type) {
        mModel.authGet(type)
                .compose(RxLifecycleUtils.transformer(mRootView))
                .subscribe(new ErrorHandleSubscriber<AuthGetBean>(mErrorHandler) {
                    @Override
                    public void onNext(AuthGetBean authGetBean) {
                        mRootView.showAuthGet(authGetBean.getData());
                    }
                });
    }

    public void upload(int type, String true_name, String id_card, File idcard_imgf, File idcard_imgb, String check, List<Bean<Boolean>> assets) {
        List<String> stringList = new ArrayList<>();
        List<MultipartBody.Part> imgf = new ArrayList<>();
        if (idcard_imgf != null)
            imgf.add(MultipartBody.Part.createFormData("images", idcard_imgf.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), idcard_imgf)));

        List<MultipartBody.Part> imgb = new ArrayList<>();
        if (idcard_imgb != null)
            imgb.add(MultipartBody.Part.createFormData("images", idcard_imgb.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), idcard_imgb)));

        Observable.fromIterable(assets)
                .flatMap(data -> {
                    List<MultipartBody.Part> parts = new ArrayList<>();
                    File file = new File(data.getValue());
                    parts.add(MultipartBody.Part.createFormData("images", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file)));
                    return mModel.imageUpload(parts);
                })
                .compose(RxLifecycleUtils.transformer(mRootView))
                .subscribe(new ErrorHandleSubscriber<ImageUploadBean>(mErrorHandler) {
                    @Override
                    public void onNext(ImageUploadBean imageUploadBean) {
                        com.orhanobut.logger.Logger.d("upload=> add " + imageUploadBean.getData().getImages().get(0));
                        stringList.add(imageUploadBean.getData().getImages().get(0));
                    }

                    @Override
                    public void onComplete() {
                        Observable.zip(mModel.imageUpload(imgf), mModel.imageUpload(imgb), (id_imgf, id_imgb) -> {
                            List<String> data = new ArrayList<>();
                            data.add(id_imgf.getData().getImages().get(0));
                            com.orhanobut.logger.Logger.d("upload=> add " + id_imgf.getData().getImages().get(0));
                            data.add(id_imgb.getData().getImages().get(0));
                            com.orhanobut.logger.Logger.d("upload=> add " + id_imgb.getData().getImages().get(0));
                            return data;
                        }).flatMap(strings -> mModel.authPerson(type, true_name, id_card, strings.get(0), strings.get(1), check, new Gson().toJson(stringList))).compose(RxLifecycleUtils.transformer(mRootView)).subscribe(new ErrorHandleSubscriber<Object>(mErrorHandler) {
                            public void onNext(Object o) {
                                com.orhanobut.logger.Logger.d("upload=> zip " + new Gson().toJson(o));
                                mRootView.showSuccess(3);
                            }

                            @Override
                            public void onError(Throwable t) {
                                mRootView.showFail(t);
                            }
                        });
                    }
                });
    }

    public void upload(String organ_name, String legal_person, String contact, File license, String check, List<Bean<Boolean>> assets) {
        List<String> stringList = new ArrayList<>();
        List<MultipartBody.Part> licenseParts = new ArrayList<>();
        if (license != null)
            licenseParts.add(MultipartBody.Part.createFormData("images", license.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), license)));

        Observable.fromIterable(assets)
                .flatMap(data -> {
                    List<MultipartBody.Part> parts = new ArrayList<>();
                    File file = new File(data.getValue());
                    parts.add(MultipartBody.Part.createFormData("images", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file)));
                    return mModel.imageUpload(parts);
                })
                .compose(RxLifecycleUtils.transformer(mRootView))
                .subscribe(new ErrorHandleSubscriber<ImageUploadBean>(mErrorHandler) {
                    @Override
                    public void onNext(ImageUploadBean imageUploadBean) {
                        com.orhanobut.logger.Logger.d("upload=> add " + imageUploadBean.getData().getImages().get(0));
                        stringList.add(imageUploadBean.getData().getImages().get(0));
                    }

                    @Override
                    public void onComplete() {
                        mModel.imageUpload(licenseParts).flatMap(imageUploadBean -> {
                            com.orhanobut.logger.Logger.d("upload=> add " + imageUploadBean.getData().getImages().get(0));
                            return mModel.authOrgan(organ_name, legal_person, contact, imageUploadBean.getData().getImages().get(0), check, new Gson().toJson(stringList));
                        }).compose(RxLifecycleUtils.transformer(mRootView)).subscribe(new ErrorHandleSubscriber<Object>(mErrorHandler) {
                            public void onNext(Object o) {
                                com.orhanobut.logger.Logger.d("upload=> zip " + new Gson().toJson(o));
                                mRootView.showSuccess(3);
                            }

                            @Override
                            public void onError(Throwable t) {
                                mRootView.showFail(t);
                            }
                        });
                    }
                });
    }
}
