package com.recorder.app;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.baidu.android.pushservice.PushMessageReceiver;
import com.core.utils.Constants;
import com.core.utils.CoreUtils;
import com.core.utils.DeviceUtils;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.recorder.R;
import com.recorder.mvp.model.api.Api;
import com.recorder.mvp.model.entity.AppVersionBean;
import com.recorder.utils.CommonUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vector.update_app.HttpManager;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by hpw on 17-12-6.
 */

public class PushReceiver extends PushMessageReceiver {
    @Override
    public void onBind(Context context, int i, String s, String s1, String s2, String s3) {
    }

    @Override
    public void onUnbind(Context context, int i, String s) {
    }

    @Override
    public void onSetTags(Context context, int i, List<String> list, List<String> list1, String s) {
    }

    @Override
    public void onDelTags(Context context, int i, List<String> list, List<String> list1, String s) {
    }

    @Override
    public void onListTags(Context context, int i, List<String> list, String s) {
    }

    @Override
    public void onMessage(Context context, String s, String s1) {
    }

    @Override
    public void onNotificationClicked(Context context, String s, String s1, String s2) {
        update(context, s, s1);
    }

    @Override
    public void onNotificationArrived(Context context, String s, String s1, String s2) {//s=title s1=content
        update(context, s, s1);
    }

    private void update(Context context, String s, String s1) {
        Logger.d("push=> title " + s + " content=> " + s1);
        Activity activity = CoreUtils.obtainAppComponentFromContext(context).appManager().getCurrentActivity();
        new UpdateAppManager.Builder()
                .setActivity(activity)
                .setHttpManager(new HttpManager() {
                    @Override
                    public void asyncGet(@NonNull String url, @NonNull Map<String, String> params, @NonNull HttpManager.Callback callBack) {
                        callBack.onResponse(s1);
                    }

                    @Override
                    public void asyncPost(@NonNull String url, @NonNull Map<String, String> params, @NonNull HttpManager.Callback callBack) {

                    }

                    @Override
                    public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, @NonNull FileCallback callback) {
                        new RxPermissions(activity)
                                .request(WRITE_EXTERNAL_STORAGE)
                                .doOnNext(granted -> {
                                    if (!granted) {
                                        throw new RuntimeException("no permission");
                                    }
                                })
                                .compose(CommonUtils.transformService(context, url, path + UUID.randomUUID() + fileName, false, true, callback))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(baseDownloadTask -> Logger.d("path" + path + " name=> " + baseDownloadTask.getFilename() + "download=> " + baseDownloadTask.getSoFarBytes() + " " + baseDownloadTask.getTotalBytes() + " progress=> " + ((float) baseDownloadTask.getSoFarBytes()) / baseDownloadTask.getTotalBytes()));
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
                                .setApkFileUrl(versionInfo.getDownload().equals("1") ? "https://raw.githubusercontent.com/SuperMan42/template/haoxiang/app/release/app-release.apk" : versionInfo.getDownload())
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
