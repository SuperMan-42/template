/**
 * Copyright 2017 JessYan
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.core.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.view.View;

import com.core.base.delegate.AppDelegate;
import com.core.base.delegate.AppLifecycles;
import com.core.di.component.AppComponent;
import com.core.utils.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * 本框架由 MVP + Dagger2 + Retrofit + RxJava + Androideventbus + Butterknife 组成
 * ================================================
 */
public class BaseApplication extends MultiDexApplication implements App {
    private AppLifecycles mAppDelegate;
    protected List<Class<? extends View>> list = new ArrayList<>();

    /**
     * 这里会在 {@link BaseApplication#onCreate} 之前被调用,可以做一些较早的初始化
     * 常用于 MultiDex 以及插件化框架的初始化
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        if (mAppDelegate == null)
            this.mAppDelegate = new AppDelegate(base);
        this.mAppDelegate.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (mAppDelegate != null)
            this.mAppDelegate.onCreate(this);
    }

    /**
     * 在模拟环境中程序终止时会被调用
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mAppDelegate != null)
            this.mAppDelegate.onTerminate(this);
    }

    /**
     * 将 {@link AppComponent} 返回出去,供其它地方使用,{@link AppComponent} 中声明的方法所返回的实例
     * 在 {@link #getAppComponent()}拿到对象后都可以直接使用
     *
     * @return
     */
    @NonNull
    @Override
    public AppComponent getAppComponent() {
        Preconditions.checkNotNull(mAppDelegate, "%s cannot be null", AppDelegate.class.getName());
        Preconditions.checkState(mAppDelegate instanceof App, "%s must be implements %s", AppDelegate.class.getName(), App.class.getName());
        return ((App) mAppDelegate).getAppComponent();
    }
}
