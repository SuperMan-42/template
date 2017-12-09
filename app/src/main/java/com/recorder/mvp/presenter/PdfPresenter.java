package com.recorder.mvp.presenter;

import android.app.Activity;
import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.http.imageloader.ImageLoader;
import com.core.integration.AppManager;
import com.core.mvp.BasePresenter;
import com.core.utils.RxLifecycleUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.recorder.mvp.contract.PdfContract;
import com.recorder.utils.CommonUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

@ActivityScope
public class PdfPresenter extends BasePresenter<PdfContract.Model, PdfContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public PdfPresenter(PdfContract.Model model, PdfContract.View rootView
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

    public void download(String pdf, String path) {
        new RxPermissions((Activity) mRootView)
                .request(WRITE_EXTERNAL_STORAGE)
                .doOnNext(granted -> {
                    if (!granted) {
                        throw new RuntimeException("no permission");
                    }
                })
                .compose(CommonUtils.transformService(mApplication, pdf, path, false, false, null))
                .compose(RxLifecycleUtils.transformer(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseDownloadTask>(mErrorHandler) {
                    @Override
                    public void onNext(BaseDownloadTask baseDownloadTask) {
                        mRootView.showMessage(baseDownloadTask.getTargetFilePath());
                    }
                });
    }

    private static void writeFile(InputStream in, File file) {
        try {
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            if (file != null && file.exists())
                file.delete();
            FileOutputStream out = new FileOutputStream(file);
            byte[] buffer = new byte[1024 * 128];
            int len = -1;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
            out.close();
            in.close();
        } catch (Exception e) {
            com.orhanobut.logger.Logger.d("下载pdf失败");
        }
    }
}
