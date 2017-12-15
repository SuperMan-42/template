package com.recorder.mvp.presenter;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.core.di.scope.ActivityScope;
import com.core.http.imageloader.ImageLoader;
import com.core.integration.AppManager;
import com.core.mvp.BasePresenter;
import com.core.utils.CoreUtils;
import com.core.utils.RxLifecycleUtils;
import com.core.widget.CustomPopupWindow;
import com.recorder.R;
import com.recorder.mvp.contract.ModifyPasswordContract;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import okhttp3.ResponseBody;

@ActivityScope
public class ModifyPasswordPresenter extends BasePresenter<ModifyPasswordContract.Model, ModifyPasswordContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;
    private ImageView image;
    private EditText code;

    @Inject
    public ModifyPasswordPresenter(ModifyPasswordContract.Model model, ModifyPasswordContract.View rootView
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
//                                    verify(mobile, imageView, llPicCode);
                                    CustomPopupWindow.builder().contentView(CoreUtils.inflate((Activity) mRootView, R.layout.layout_pic_code)).isOutsideTouch(false)
                                            .customListener(contentView -> {
                                                image = contentView.findViewById(R.id.im_pic_code);
                                                code = contentView.findViewById(R.id.et_content);
                                                verify(mobile, image, null);
                                                image.setOnClickListener(view -> verify(mobile, image, null));
                                                contentView.findViewById(R.id.tv_sure).setOnClickListener(view -> {
                                                    CoreUtils.hideSoftInput(code);
                                                    if (TextUtils.isEmpty(code.getText().toString())) {
                                                        CoreUtils.snackbarText(mApplication.getString(R.string.text_pic_code_null));
                                                        return;
                                                    }
                                                    smsCode(mobile, type, code.getText().toString(), imageView, llPicCode);
                                                });
                                            }).build().show();
                                    break;
                                case 108:
//                                    verify(mobile, imageView, llPicCode);
                                    verify(mobile, image, null);
                                    break;
                                case 0:
                                    CustomPopupWindow.killMySelf();
                                    mRootView.showMessage(mApplication.getString(R.string.text_smsCode));
                                    break;
                            }
                        } catch (JSONException e) {
                            mRootView.showMessage(mApplication.getString(R.string.text_smsCode_fail));
                        }
                    }
                });
    }

    public void userModifypwd(String old_password, String password, String code) {
        mModel.userModifypwd(old_password, password, code)
                .compose(RxLifecycleUtils.transformer(mRootView, 0, 2))
                .subscribe(new ErrorHandleSubscriber<Object>(mErrorHandler) {
                    @Override
                    public void onNext(Object o) {
                        mRootView.showMessage(CoreUtils.getString(mApplication, R.string.text_forgetpwd));
                        mRootView.killMyself();
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
                        if (llPicCode != null)
                            llPicCode.setVisibility(View.VISIBLE);
                        imageView.setImageBitmap(bitmap);
                        code.setText(null);
                    }
                });
    }
}
