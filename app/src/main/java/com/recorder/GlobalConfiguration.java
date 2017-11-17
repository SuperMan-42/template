package com.recorder;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.core.base.delegate.AppLifecycles;
import com.core.di.module.GlobalConfigModule;
import com.core.integration.ConfigModule;
import com.core.utils.DataHelper;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.List;

import timber.log.Timber;

/**
 * app的全局配置信息在此配置,需要将此实现类声明到AndroidManifest中
 */

public class GlobalConfiguration implements ConfigModule {

    @Override
    public void applyOptions(Context context, GlobalConfigModule.Builder builder) {
        //使用builder可以为框架配置一些配置信息
        builder.baseurl("http://gank.io/").cacheFile(DataHelper.getCacheFile(context));
    }

    @Override
    public void injectAppLifecycle(Context context, List<AppLifecycles> lifecycles) {
        //AppDelegate.Lifecycle 的所有方法都会在基类Application对应的生命周期中被调用,所以在对应的方法中可以扩展一些自己需要的逻辑
        lifecycles.add(new AppLifecycles() {

            @Override
            public void attachBaseContext(Context base) {

            }

            @Override
            public void onCreate(Application application) {
                if (BuildConfig.LOG_DEBUG) {//日志打印
                    FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder().tag("hpw").build();
                    Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
                        @Override
                        public boolean isLoggable(int priority, String tag) {
                            return BuildConfig.LOG_DEBUG;
                        }
                    });
                    Timber.plant(new Timber.DebugTree() {
                        @Override
                        protected void log(int priority, String tag, String message, Throwable t) {
                            Logger.log(priority, tag, message, t);
                        }
                    });
                }
                //leakCanary内存泄露检查
                ((App) application).getAppComponent().extras().put(RefWatcher.class.getName(), BuildConfig.USE_CANARY ? LeakCanary.install(application) : RefWatcher.DISABLED);

                //ARouter初始化
//                if (BuildConfig.LOG_DEBUG) {
//                    ARouter.openLog();     // 打印日志
//                    ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
//                }
//                ARouter.init(application); // 尽可能早，推荐在Application中初始化
            }

            @Override
            public void onTerminate(Application application) {

            }
        });
    }

    @Override
    public void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles) {
        //向Activity的生命周期中注入一些自定义逻辑
        lifecycles.add(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                //这里全局给Activity设置toolbar和title,你想象力有多丰富,这里就有多强大,以前放到BaseActivity的操作都可以放到这里
                if (activity.findViewById(R.id.toolbar) != null) {
                    if (activity instanceof AppCompatActivity) {
                        ((AppCompatActivity) activity).setSupportActionBar(activity.findViewById(R.id.toolbar));
                        ((AppCompatActivity) activity).getSupportActionBar().setDisplayShowTitleEnabled(false);
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            activity.setActionBar(activity.findViewById(R.id.toolbar));
                            activity.getActionBar().setDisplayShowTitleEnabled(false);
                        }
                    }
                }
                if (activity.findViewById(R.id.toolbar_left) != null) {
                    activity.findViewById(R.id.toolbar_left).setOnClickListener(v -> activity.onBackPressed());
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    @Override
    public void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {
        //向Fragment的生命周期中注入一些自定义逻辑
        lifecycles.add(new FragmentManager.FragmentLifecycleCallbacks() {

            @Override
            public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
                // 在配置变化的时候将这个 Fragment 保存下来,在 Activity 由于配置变化重建是重复利用已经创建的Fragment。
                // https://developer.android.com/reference/android/app/Fragment.html?hl=zh-cn#setRetainInstance(boolean)
                // 在 Activity 中绑定少量的 Fragment 建议这样做,如果需要绑定较多的 Fragment 不建议设置此参数,如 ViewPager 需要展示较多 Fragment
                f.setRetainInstance(true);
            }

            @Override
            public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
                ((RefWatcher) ((App) f.getActivity().getApplication()).getAppComponent().extras().get(RefWatcher.class.getName())).watch(f);
            }
        });
    }
}
