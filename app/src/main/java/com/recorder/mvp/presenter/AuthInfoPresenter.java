package com.recorder.mvp.presenter;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.http.imageloader.ImageLoader;
import com.core.integration.AppManager;
import com.core.mvp.BasePresenter;
import com.core.utils.RxLifecycleUtils;
import com.google.gson.Gson;
import com.recorder.mvp.contract.AuthInfoContract;
import com.recorder.mvp.model.entity.AuthBean;
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

    public void upload(int type, String true_name, String id_card, Object idcard_imgf, Object idcard_imgb, String check, List<Bean<Boolean>> assets) {
        com.orhanobut.logger.Logger.d("upload=> " + "idcard_imgf " + (idcard_imgf == null) + "idcard_imgb " + (idcard_imgb == null) + " assets " + (assets == null) + " " + assets);
        List<String> stringList = new ArrayList<>();
        List<MultipartBody.Part> imgf = new ArrayList<>();
        if (idcard_imgf != null && idcard_imgf instanceof File)
            imgf.add(MultipartBody.Part.createFormData("images", ((File) idcard_imgf).getName(), RequestBody.create(MediaType.parse("multipart/form-data"), ((File) idcard_imgf))));

        List<MultipartBody.Part> imgb = new ArrayList<>();
        if (idcard_imgb != null && idcard_imgb instanceof File)
            imgb.add(MultipartBody.Part.createFormData("images", ((File) idcard_imgb).getName(), RequestBody.create(MediaType.parse("multipart/form-data"), ((File) idcard_imgb))));

        if (assets.size() > 1) {
            Observable.fromIterable(assets)
                    .flatMap(data -> {
                        if (data.getValue().contains("com/")) {
                            return Observable.just(data.getValue().split("com/")[1]);
                        } else {
                            List<MultipartBody.Part> parts = new ArrayList<>();
                            File file = new File(data.getValue());
                            parts.add(MultipartBody.Part.createFormData("images", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file)));
                            return mModel.imageUpload(parts);
                        }
                    })
                    .compose(RxLifecycleUtils.transformer(mRootView))
                    .subscribe(new ErrorHandleSubscriber<Object>(mErrorHandler) {
                        @Override
                        public void onNext(Object object) {
                            if (object instanceof ImageUploadBean) {
                                com.orhanobut.logger.Logger.d("upload=> add onNext " + ((ImageUploadBean) object).getData().getImages().get(0));
                                stringList.add(((ImageUploadBean) object).getData().getImages().get(0));
                            } else {
                                com.orhanobut.logger.Logger.d("upload=> add onNext " + object);
                                stringList.add(object.toString());
                            }
                        }

                        @Override
                        public void onComplete() {
                            person(stringList, imgf, imgb, type, true_name, id_card, idcard_imgf, idcard_imgb, check);
                        }
                    });
        } else {
            person(null, imgf, imgb, type, true_name, id_card, idcard_imgf, idcard_imgb, check);
        }
    }

    public void upload(String organ_name, String legal_person, String contact, Object license, String check, List<Bean<Boolean>> assets) {
        com.orhanobut.logger.Logger.d("upload=> " + "license " + (license == null) + " assets " + (assets == null) + " " + assets);
        List<String> stringList = new ArrayList<>();
        List<MultipartBody.Part> licenseParts = new ArrayList<>();
        if (license != null && license instanceof File)
            licenseParts.add(MultipartBody.Part.createFormData("images", ((File) license).getName(), RequestBody.create(MediaType.parse("multipart/form-data"), ((File) license))));

        if (assets.size() > 1) {
            Observable.fromIterable(assets)
                    .flatMap(data -> {
                        if (data.getValue().contains("com/")) {
                            return Observable.just(data.getValue().split("com/")[1]);
                        } else {
                            List<MultipartBody.Part> parts = new ArrayList<>();
                            File file = new File(data.getValue());
                            parts.add(MultipartBody.Part.createFormData("images", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file)));
                            return mModel.imageUpload(parts);
                        }
                    })
                    .compose(RxLifecycleUtils.transformer(mRootView))
                    .subscribe(new ErrorHandleSubscriber<Object>(mErrorHandler) {
                        @Override
                        public void onNext(Object object) {
                            if (object instanceof ImageUploadBean) {
                                com.orhanobut.logger.Logger.d("upload=> add onNext " + ((ImageUploadBean) object).getData().getImages().get(0));
                                stringList.add(((ImageUploadBean) object).getData().getImages().get(0));
                            } else {
                                com.orhanobut.logger.Logger.d("upload=> add onNext " + object);
                                stringList.add(object.toString());
                            }
                        }

                        @Override
                        public void onComplete() {
                            organ(stringList, licenseParts, organ_name, legal_person, contact, license, check);
                        }
                    });
        } else {
            organ(null, licenseParts, organ_name, legal_person, contact, license, check);
        }
    }

    private void person(List<String> stringList, List<MultipartBody.Part> imgf, List<MultipartBody.Part> imgb, int type, String true_name, String id_card, Object idcard_imgf, Object idcard_imgb, String check) {
        Observable<AuthBean> observable;
        if (idcard_imgf instanceof String && idcard_imgb instanceof String) {
            observable = mModel.authPerson(type, true_name, id_card, (String) idcard_imgf, (String) idcard_imgb, check, stringList == null ? null : new Gson().toJson(stringList));
        } else if (idcard_imgf instanceof String) {
            observable = mModel.imageUpload(imgb).flatMap(imageUploadBean -> {
                com.orhanobut.logger.Logger.d("upload=> add image " + imageUploadBean.getData().getImages().get(0));
                return mModel.authPerson(type, true_name, id_card, (String) idcard_imgf, imageUploadBean.getData().getImages().get(1), check, stringList == null ? null : new Gson().toJson(stringList));
            });
        } else if (idcard_imgb instanceof String) {
            observable = mModel.imageUpload(imgf).flatMap(imageUploadBean -> {
                com.orhanobut.logger.Logger.d("upload=> add image " + imageUploadBean.getData().getImages().get(0));
                return mModel.authPerson(type, true_name, id_card, imageUploadBean.getData().getImages().get(1), (String) idcard_imgb, check, stringList == null ? null : new Gson().toJson(stringList));
            });
        } else {
            observable = Observable.zip(mModel.imageUpload(imgf), mModel.imageUpload(imgb), (id_imgf, id_imgb) -> {
                List<String> data = new ArrayList<>();
                data.add(id_imgf.getData().getImages().get(0));
                com.orhanobut.logger.Logger.d("upload=> add imgf " + id_imgf.getData().getImages().get(0));
                data.add(id_imgb.getData().getImages().get(0));
                com.orhanobut.logger.Logger.d("upload=> add imgb " + id_imgb.getData().getImages().get(0));
                return data;
            }).flatMap(strings -> mModel.authPerson(type, true_name, id_card, strings.get(0), strings.get(1), check, stringList == null ? null : new Gson().toJson(stringList)));
        }
        observable.compose(RxLifecycleUtils.transformer(mRootView)).subscribe(new ErrorHandleSubscriber<AuthBean>(mErrorHandler) {
            public void onNext(AuthBean o) {
                if (o.getErrno() == 0) {
                    com.orhanobut.logger.Logger.d("upload=> zip onNext " + new Gson().toJson(o));
                    mRootView.showSuccess(type, o.getData().getSurvey_host());
                } else {
                    mRootView.showFail(o.getError());
                }
            }
        });
    }

    private void organ(List<String> stringList, List<MultipartBody.Part> licenseParts, String organ_name, String legal_person, String contact, Object license, String check) {
        Observable<AuthBean> observable;
        if (license instanceof File) {
            observable = mModel.imageUpload(licenseParts).flatMap(imageUploadBean -> {
                com.orhanobut.logger.Logger.d("upload=> add image " + imageUploadBean.getData().getImages().get(0));
                return mModel.authOrgan(organ_name, legal_person, contact, imageUploadBean.getData().getImages().get(0), check, stringList == null ? null : new Gson().toJson(stringList));
            });
        } else {
            observable = mModel.authOrgan(organ_name, legal_person, contact, (String) license, check, stringList == null ? null : new Gson().toJson(stringList));
        }
        observable.compose(RxLifecycleUtils.transformer(mRootView)).subscribe(new ErrorHandleSubscriber<AuthBean>(mErrorHandler) {
            public void onNext(AuthBean o) {
                if (o.getErrno() == 0) {
                    com.orhanobut.logger.Logger.d("upload=> zip onNext " + new Gson().toJson(o));
                    mRootView.showSuccess(3, o.getData().getSurvey_host());
                } else {
                    mRootView.showFail(o.getError());
                }
            }
        });
    }
}
