package com.recorder.mvp.presenter;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.core.di.scope.ActivityScope;
import com.core.http.imageloader.ImageLoader;
import com.core.integration.AppManager;
import com.core.integration.cache.BCache;
import com.core.mvp.BasePresenter;
import com.core.utils.Constants;
import com.core.utils.CoreUtils;
import com.core.utils.DeviceUtils;
import com.core.utils.RxLifecycleUtils;
import com.google.gson.Gson;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.orhanobut.logger.Logger;
import com.recorder.R;
import com.recorder.mvp.contract.SplashContract;
import com.recorder.mvp.model.api.Api;
import com.recorder.mvp.model.entity.AppStartBean;
import com.recorder.mvp.model.entity.AppVersionBean;
import com.recorder.utils.CommonUtils;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vector.update_app.HttpManager;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

@ActivityScope
public class SplashPresenter extends BasePresenter<SplashContract.Model, SplashContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public SplashPresenter(SplashContract.Model model, SplashContract.View rootView
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

    public void appStart() {
        mModel.appStart()
                .compose(RxLifecycleUtils.transformer(mRootView))
                .subscribe(new ErrorHandleSubscriber<AppStartBean>(mErrorHandler) {
                    @Override
                    public void onNext(AppStartBean appStartBean) {
                        mRootView.showAppStart(appStartBean.getData());
                        BCache.getInstance().put(Constants.APPSTART, new Gson().toJson(appStartBean));
                    }

                    @Override
                    public void onError(Throwable t) {
                        mRootView.showAppStart(null);
                    }
                });
    }

    public void getPermissons() {
        new RxPermissions((Activity) mRootView)
                .requestEach(Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.READ_LOGS,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.SET_DEBUG_APP,
                        Manifest.permission.SYSTEM_ALERT_WINDOW,
                        Manifest.permission.GET_ACCOUNTS,
                        Manifest.permission.WRITE_APN_SETTINGS)
//                .doOnNext(granted -> {
//                    if (!granted) {
//                        throw new RuntimeException("no permission");
//                    }
//                })
                .compose(RxLifecycleUtils.transformer(mRootView))
                .subscribe(new ErrorHandleSubscriber<Permission>(mErrorHandler) {
                    @Override
                    public void onNext(Permission permission) {
                        if (!permission.granted) {
                            if (permission.name.equals("android.permission.WRITE_EXTERNAL_STORAGE"))
                                mRootView.showMessage(CoreUtils.getString(mApplication, R.string.text_permission_fail));
                        }
                        Logger.d("permission=> " + permission);
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("permission=> complete");
                        updateApp((Activity) mRootView);
                    }
                });
    }

    private void updateApp(Activity activity) {
        new UpdateAppManager.Builder()
                .setActivity(activity)
                .setHttpManager(new HttpManager() {
                    @Override
                    public void asyncGet(@NonNull String url, @NonNull Map<String, String> params, @NonNull HttpManager.Callback callBack) {
                        mModel.appVersion()
                                .compose(RxLifecycleUtils.transformer(mRootView))
                                .subscribe(new ErrorHandleSubscriber<AppVersionBean>(mErrorHandler) {
                                    @Override
                                    public void onNext(AppVersionBean appVersionBean) {
                                        callBack.onResponse(new Gson().toJson(appVersionBean));
                                    }

                                    @Override
                                    public void onError(Throwable t) {
                                        callBack.onError("网络错误");
                                    }
                                });
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
                        AppVersionBean.DataEntity.VersionInfoEntity versionInfo = new Gson().fromJson(json, AppVersionBean.class).getData().getVersion_info();
                        List<String> list = versionInfo.getDes();
                        StringBuilder stringBuilder = new StringBuilder();
                        for (String string : list) {
                            stringBuilder.append(string).append("\r\n");
                        }
                        UpdateAppBean updateAppBean = new UpdateAppBean();
                        updateAppBean
                                //（必须）是否更新Yes,No
                                .setUpdate(!DeviceUtils.getVersionName(activity).equals(versionInfo.getNew_version()) ? "Yes" : "No")
                                //（必须）新版本号，
                                .setNewVersion(versionInfo.getNew_version())
                                //（必须）下载地
                                .setApkFileUrl("https://raw.githubusercontent.com/SuperMan42/template/haoxiang/app/release/app-release.apk")
                                //（必须）更新内容
                                .setUpdateLog(stringBuilder.toString())
                                //大小，不设置不显示大小，可以不设置
//                                .setTargetSize(String.valueOf(size))
                                //是否强制更新，可以不设置
                                .setConstraint(versionInfo.getIs_force() == 1);
                        //设置md5，可以不设置
//                                .setNewMd5(jsonObject.optString("new_md51"));
                        return updateAppBean;
                    }

                    @Override
                    protected void noNewApp() {
                        goHome("no");
                    }

                    @Override
                    protected void hasNewApp(UpdateAppBean updateApp, UpdateAppManager updateAppManager) {
                        if (!updateApp.isConstraint()) {
                            goHome(new Gson().toJson(updateApp));
                        } else {
                            super.hasNewApp(updateApp, updateAppManager);
                        }
                    }
                });
    }

    private void goHome(String json) {
        Observable.timer(3000, TimeUnit.MILLISECONDS)
                .subscribe(new ErrorHandleSubscriber<Long>(mErrorHandler) {
                    @Override
                    public void onNext(Long aLong) {
                        ARouter.getInstance().build("/app/HomeActivity").withTransition(R.anim.fade_in, R.anim.zoom_out).withString("update", json).greenChannel().navigation();
                    }
                });
    }
}
