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
import com.core.utils.DeviceUtils;
import com.core.utils.RxLifecycleUtils;
import com.google.gson.Gson;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.orhanobut.logger.Logger;
import com.recorder.R;
import com.recorder.mvp.contract.HomeContract;
import com.recorder.mvp.model.api.Api;
import com.recorder.mvp.model.entity.AppVersionBean;
import com.recorder.mvp.model.entity.DealFilter;
import com.recorder.mvp.model.entity.HomeRecommendBean;
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

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

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
//                        if (permission.granted) {
//                            mRootView.showMessage(CoreUtils.getString(mApplication, R.string.text_permission_success));
//                        } else {
//                            mRootView.showMessage(CoreUtils.getString(mApplication, R.string.text_permission_fail));
//                        }
                    }
                });
    }

    public void updateApp(Activity activity) {
        new UpdateAppManager.Builder()
                .setActivity(activity)
                .setHttpManager(new HttpManager() {
                    @Override
                    public void asyncGet(@NonNull String url, @NonNull Map<String, String> params, @NonNull Callback callBack) {
                        mModel.appVersion()
                                .compose(RxLifecycleUtils.transformer(mRootView))
                                .subscribe(new ErrorHandleSubscriber<AppVersionBean>(mErrorHandler) {
                                    @Override
                                    public void onNext(AppVersionBean appVersionBean) {
                                        callBack.onResponse(new Gson().toJson(appVersionBean));
                                    }
                                });
                    }

                    @Override
                    public void asyncPost(@NonNull String url, @NonNull Map<String, String> params, @NonNull Callback callBack) {

                    }

                    @Override
                    public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, @NonNull FileCallback callback) {
                        new RxPermissions((Activity) mRootView)
                                .request(WRITE_EXTERNAL_STORAGE)
                                .doOnNext(granted -> {
                                    if (!granted) {
                                        throw new RuntimeException("no permission");
                                    }
                                })
                                .compose(CommonUtils.transformService(mApplication, url, path + UUID.randomUUID() + fileName, false, true, callback))
                                .compose(RxLifecycleUtils.transformer(mRootView))
                                .subscribe(new ErrorHandleSubscriber<BaseDownloadTask>(mErrorHandler) {
                                    @Override
                                    public void onNext(BaseDownloadTask baseDownloadTask) {
                                        Logger.d("path" + baseDownloadTask.getFilename() + "download=> " + baseDownloadTask.getSoFarBytes() + " " + baseDownloadTask.getTotalBytes() + " progress=> " + ((float) baseDownloadTask.getSoFarBytes()) / baseDownloadTask.getTotalBytes());
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
                });
    }
}
