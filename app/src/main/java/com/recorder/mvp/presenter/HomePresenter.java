package com.recorder.mvp.presenter;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.support.annotation.NonNull;

import com.core.di.scope.ActivityScope;
import com.core.http.imageloader.ImageLoader;
import com.core.integration.AppManager;
import com.core.mvp.BasePresenter;
import com.core.utils.Constants;
import com.core.utils.CoreUtils;
import com.core.utils.RxLifecycleUtils;
import com.google.gson.Gson;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.orhanobut.logger.Logger;
import com.recorder.R;
import com.recorder.mvp.contract.HomeContract;
import com.recorder.mvp.model.api.Api;
import com.recorder.mvp.model.entity.DealFilter;
import com.recorder.mvp.model.entity.HomeRecommendBean;
import com.recorder.utils.CommonUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vector.update_app.HttpManager;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;

import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

@ActivityScope
public class HomePresenter extends BasePresenter<HomeContract.Model, HomeContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public HomePresenter(HomeContract.Model model, HomeContract.View rootView
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

    public void dealFilter() {
        mModel.dealFilter()
                .compose(RxLifecycleUtils.transformer(mRootView))
                .subscribe(new ErrorHandleSubscriber<DealFilter>(mErrorHandler) {
                    @Override
                    public void onNext(DealFilter referFilter) {
                        mRootView.showFilter(mImageLoader, referFilter.getData());
                    }
                });
    }

    public void homeRecommend() {
        mModel.homeRecommend()
                .compose(RxLifecycleUtils.transformer(mRootView))
                .subscribe(new ErrorHandleSubscriber<HomeRecommendBean>(mErrorHandler) {
                    @Override
                    public void onNext(HomeRecommendBean homeRecommendBean) {
                        mRootView.showHomeRecomment(homeRecommendBean.getData());
                    }
                });
    }

    public void updateApp(Activity activity, String json) {
        new UpdateAppManager.Builder()
                .setActivity(activity)
                .setHttpManager(new HttpManager() {
                    @Override
                    public void asyncGet(@NonNull String url, @NonNull Map<String, String> params, @NonNull HttpManager.Callback callBack) {
                        callBack.onResponse(json);
                    }

                    @Override
                    public void asyncPost(@NonNull String url, @NonNull Map<String, String> params, @NonNull Callback callBack) {

                    }

                    @Override
                    public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, @NonNull FileCallback callback) {
                        new RxPermissions(activity)
                                .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .subscribe(permission -> {
                                    if (permission.granted) {
                                        io.reactivex.Observable.empty()
                                                .compose(CommonUtils.transformService(mApplication, url, path + UUID.randomUUID() + fileName, false, true, callback))
                                                .compose(RxLifecycleUtils.transformer(mRootView))
                                                .subscribe(new ErrorHandleSubscriber<BaseDownloadTask>(mErrorHandler) {
                                                    @Override
                                                    public void onNext(BaseDownloadTask baseDownloadTask) {
                                                        Logger.d("path" + baseDownloadTask.getFilename() + "download=> " + baseDownloadTask.getSoFarBytes() + " " + baseDownloadTask.getTotalBytes() + " progress=> " + ((float) baseDownloadTask.getSoFarBytes()) / baseDownloadTask.getTotalBytes());
                                                    }
                                                });
                                    } else {
                                        CoreUtils.snackbarText(CoreUtils.getString(activity, R.string.text_permission));
                                    }
                                });
                    }
                })
                .setUpdateUrl(Api.APP_DOMAIN + "app/version")
                .hideDialogOnDownloading(false)
                .setTopPic(R.mipmap.top_4)
                .setThemeColor(CoreUtils.getColor(activity, R.color.colorStatus))
                .setTargetPath(Constants.SDCARD_PATH)
                .showIgnoreVersion()
                .build()
                .checkNewApp(new UpdateCallback() {
                    @Override
                    protected UpdateAppBean parseJson(String json) {
                        return new Gson().fromJson(json, UpdateAppBean.class);
                    }
                });
    }
}
