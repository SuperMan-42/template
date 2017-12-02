package com.recorder.mvp.presenter;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.http.imageloader.ImageLoader;
import com.core.integration.AppManager;
import com.core.integration.cache.BCache;
import com.core.mvp.BasePresenter;
import com.core.utils.Constants;
import com.core.utils.CoreUtils;
import com.core.utils.RxLifecycleUtils;
import com.google.gson.Gson;
import com.recorder.R;
import com.recorder.mvp.contract.PersonContract;
import com.recorder.mvp.model.entity.UserInfoBean;

import org.simple.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import okhttp3.MultipartBody;

@ActivityScope
public class PersonPresenter extends BasePresenter<PersonContract.Model, PersonContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public PersonPresenter(PersonContract.Model model, PersonContract.View rootView
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

    public void imageUpload(List<MultipartBody.Part> images, String path) {
        mModel.imageUpload(images)
                .compose(RxLifecycleUtils.transformer(mRootView))
                .observeOn(Schedulers.io())
                .flatMap(imageUploadBean -> {
                    Gson gson = new Gson();
                    UserInfoBean userInfoBean = gson.fromJson(BCache.getInstance().getString(Constants.USER_INFO), UserInfoBean.class);
                    EventBus.getDefault().post(path, "person_avatar");
                    userInfoBean.getData().setAvatar(path);
                    BCache.getInstance().put(Constants.USER_INFO, gson.toJson(userInfoBean));
                    return mModel.userModify("avatar", null, null, null, null, null, imageUploadBean.getData().getImages().get(0));
                })
                .compose(RxLifecycleUtils.transformer(mRootView))
                .subscribe(new ErrorHandleSubscriber<Object>(mErrorHandler) {
                    @Override
                    public void onNext(Object object) {
                        mRootView.showMessage(CoreUtils.getString(mApplication, R.string.text_avatar));
                    }
                });
    }
}
