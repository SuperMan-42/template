package com.recorder.mvp.presenter;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.http.imageloader.ImageLoader;
import com.core.integration.AppManager;
import com.core.integration.cache.BCache;
import com.core.mvp.BasePresenter;
import com.core.utils.CoreUtils;
import com.core.utils.RxLifecycleUtils;
import com.google.gson.Gson;
import com.recorder.Constants;
import com.recorder.R;
import com.recorder.mvp.contract.UserModifyContract;
import com.recorder.mvp.model.entity.UserInfoBean;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

@ActivityScope
public class UserModifyPresenter extends BasePresenter<UserModifyContract.Model, UserModifyContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public UserModifyPresenter(UserModifyContract.Model model, UserModifyContract.View rootView
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

    public void userModify(String field, String user_name, String intro, String email, String weixin, String address) {
        mModel.userModify(field, user_name, intro, email, weixin, address)
                .compose(RxLifecycleUtils.transformer(mRootView))
                .subscribe(new ErrorHandleSubscriber<Object>(mErrorHandler) {
                    @Override
                    public void onNext(Object o) {
                        Gson gson = new Gson();
                        UserInfoBean userInfoBean = gson.fromJson(BCache.getInstance().getString(Constants.USER_INFO), UserInfoBean.class);
                        switch (field) {
                            case "user_name":
                                userInfoBean.getData().setUser_name(user_name);
                                break;
                            case "intro":
                                userInfoBean.getData().setIntro(intro);
                                break;
                            case "email":
                                userInfoBean.getData().setEmail(email);
                                break;
                            case "weixin":
                                userInfoBean.getData().setWeixin(weixin);
                                break;
                            case "address":
                                userInfoBean.getData().setAddress(address);
                                break;
                        }
                        BCache.getInstance().put(Constants.USER_INFO, gson.toJson(userInfoBean));
                        mRootView.showMessage(CoreUtils.getString(mApplication, R.string.text_user_modify));
                        mRootView.killMyself();
                    }
                });
    }
}
