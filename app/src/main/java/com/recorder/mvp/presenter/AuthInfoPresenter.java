package com.recorder.mvp.presenter;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.http.imageloader.ImageLoader;
import com.core.integration.AppManager;
import com.core.mvp.BasePresenter;
import com.core.utils.CoreUtils;
import com.core.utils.RxLifecycleUtils;
import com.google.gson.Gson;
import com.recorder.R;
import com.recorder.mvp.contract.AuthInfoContract;
import com.recorder.mvp.model.entity.AuthGetBean;
import com.recorder.mvp.model.entity.Bean;
import com.recorder.mvp.model.entity.ImageUploadBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function3;
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
        imgf.add(MultipartBody.Part.createFormData("images", idcard_imgf.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), idcard_imgf)));

        List<MultipartBody.Part> imgb = new ArrayList<>();
        imgb.add(MultipartBody.Part.createFormData("images", idcard_imgb.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), idcard_imgb)));

        Observable.zip(mModel.imageUpload(imgf), mModel.imageUpload(imgb), Observable.fromArray(assets.toArray())
                .flatMap(data -> {
                    List<MultipartBody.Part> parts = new ArrayList<>();
                    File file = new File(((Bean<Boolean>) data).getValue());
                    parts.add(MultipartBody.Part.createFormData("images", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file)));
                    return mModel.imageUpload(parts);
                })
                .flatMap(imageUploadBean -> {
                    stringList.add(imageUploadBean.getData().getImages().get(0));
                    com.orhanobut.logger.Logger.d("upload=> " + stringList);
                    return Observable.just(stringList);
                }), (Function3<ImageUploadBean, ImageUploadBean, List<String>, Object>) (idcard_imgf1, idcard_imgb1, strings) ->
                mModel.authPerson(type, true_name, id_card, idcard_imgf1.getData().getImages().get(0), idcard_imgb1.getData().getImages().get(0), check, new Gson().toJson(strings)))
                .compose(RxLifecycleUtils.transformer(mRootView))
                .subscribe(new ErrorHandleSubscriber<Object>(mErrorHandler) {
                    @Override
                    public void onNext(Object o) {
                        com.orhanobut.logger.Logger.d("upload=> zip " + o);
                        CoreUtils.snackbarText(CoreUtils.getString(mApplication, R.string.text_auth_success));
                    }
                });
    }

    public void upload(String organ_name, String legal_person, String contact, File license, String check, List<File> assets) {

    }


//                .compose(RxLifecycleUtils.transformer(mRootView))
//                .doOnComplete(() -> {
//                    Observable<Object> observable = null;
//                    if (dataEntity.getAuth_type() == 1 || dataEntity.getAuth_type() == 2) {
//                        observable = mModel.authPerson(dataEntity.getAuth_type(), null, null, null, null, null, new Gson().toJson(stringList));
//                    } else if (dataEntity.getAuth_type() == 3) {
//                        observable = mModel.authOrgan(null, null, null, null, null, new Gson().toJson(stringList));
//                    }
//                    observable.compose(RxLifecycleUtils.transformer(mRootView))
//                            .subscribe(new ErrorHandleSubscriber<Object>(mErrorHandler) {
//                                @Override
//                                public void onNext(Object object) {
//                                    CoreUtils.snackbarText(CoreUtils.getString(mApplication, R.string.text_auth_success));
//                                    mRootView.killMyself();
//                                }
//                            });
//                })
//                .subscribe(new ErrorHandleSubscriber<List<String>>(mErrorHandler) {
//                    @Override
//                    public void onNext(List<String> strings) {
//
//                    }
//                });
}
