package com.core;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * Created by hpw on 16/10/28.
 */

public abstract class CoreApp extends Application {
    private static CoreApp mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        BGASwipeBackHelper.init(this, null);
        mApp = this;
    }

    public static synchronized CoreApp getInstance() {
        return mApp;
    }

    public static Context getAppContext() {
        return mApp.getApplicationContext();
    }

    public static Resources getAppResources() {
        return mApp.getResources();
    }

    public abstract String setBaseUrl();
}
