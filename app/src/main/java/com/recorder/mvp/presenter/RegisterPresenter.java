package com.recorder.mvp.presenter;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.core.di.scope.ActivityScope;
import com.core.http.imageloader.ImageLoader;
import com.core.integration.AppManager;
import com.core.integration.cache.BCache;
import com.core.mvp.BasePresenter;
import com.core.utils.Constants;
import com.core.utils.RxLifecycleUtils;
import com.google.gson.Gson;
import com.recorder.R;
import com.recorder.mvp.contract.RegisterContract;
import com.recorder.mvp.model.entity.LoginBean;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import okhttp3.ResponseBody;

@ActivityScope
public class RegisterPresenter extends BasePresenter<RegisterContract.Model, RegisterContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public RegisterPresenter(RegisterContract.Model model, RegisterContract.View rootView
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

    public void registerUser(String mobile, String password, String code) {
        mModel.registerUser(mobile, password, code)
                .compose(RxLifecycleUtils.transformer(mRootView))
                .subscribe(new ErrorHandleSubscriber<LoginBean>(mErrorHandler) {
                    @Override
                    public void onNext(LoginBean loginBean) {
                        BCache.getInstance().put(Constants.LOGIN_INFO, new Gson().toJson(loginBean));
                        mRootView.showRegisterSuccess(mImageLoader, loginBean);
                    }
                });
    }

    public void smsCode(String mobile, String type, String captcha, ImageView imageView, LinearLayout llPicCode) {
        mModel.smsCode(mobile, type, captcha)
                .compose(RxLifecycleUtils.transformer(mRootView))
                .subscribe(new ErrorHandleSubscriber<Object>(mErrorHandler) {
                    @Override
                    public void onNext(Object o) {
                        try {
                            JSONObject jsonObject = new JSONObject(o.toString());
                            switch (jsonObject.optInt("errno")) {
                                case 102:
                                    verify(mobile, imageView, llPicCode);
                                    break;
                                case 108:
                                    verify(mobile, imageView, llPicCode);
                                    break;
                                case 0:
                                    mRootView.showMessage(mApplication.getString(R.string.text_smsCode));
                                    break;
                            }
                        } catch (JSONException e) {
                            mRootView.showMessage(mApplication.getString(R.string.text_smsCode_fail));
                        }
                    }
                });
    }

    public void verify(String token, ImageView imageView, LinearLayout llPicCode) {
        mModel.verify(token)
                .compose(RxLifecycleUtils.transformer(mRootView))
                .map(ResponseBody::byteStream)
                .map(BitmapFactory::decodeStream)
                .subscribe(new ErrorHandleSubscriber<Bitmap>(mErrorHandler) {
                    @Override
                    public void onNext(Bitmap bitmap) {
                        llPicCode.setVisibility(View.VISIBLE);
                        imageView.setImageBitmap(bitmap);
                    }
                });
    }
}
