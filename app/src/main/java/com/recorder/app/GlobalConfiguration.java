package com.recorder.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.core.base.delegate.AppLifecycles;
import com.core.di.module.GlobalConfigModule;
import com.core.http.GlobalHttpHandler;
import com.core.http.RequestInterceptor;
import com.core.integration.ConfigModule;
import com.core.utils.CoreUtils;
import com.core.utils.DataHelper;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.recorder.BuildConfig;
import com.recorder.R;
import com.recorder.YshNameValuePair;
import com.recorder.mvp.model.api.Api;
import com.recorder.mvp.ui.activity.HomeActivity;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

/**
 * app的全局配置信息在此配置,需要将此实现类声明到AndroidManifest中
 */

public class GlobalConfiguration implements ConfigModule {

    @Override
    public void applyOptions(Context context, GlobalConfigModule.Builder builder) {
        //使用builder可以为框架配置一些配置信息
        builder.baseurl(Api.APP_DOMAIN)
                .cacheFile(DataHelper.getCacheFile(context))
                .globalHttpHandler(new GlobalHttpHandler() {
                    @Override
                    public Response onHttpResultResponse(String httpResult, Interceptor.Chain chain, Response response) {
                      /* 这里可以先客户端一步拿到每一次http请求的结果,可以解析成json,做一些操作,如检测到token过期后
                           重新请求token,并重新执行请求 */
                        if (!TextUtils.isEmpty(httpResult) && RequestInterceptor.isJson(response.body().contentType())) {
                            Logger.json(httpResult);
                        }
                        /* 这里如果发现token过期,可以先请求最新的token,然后在拿新的token放入request里去重新请求
                        注意在这个回调之前已经调用过proceed,所以这里必须自己去建立网络请求,如使用okhttp使用新的request去请求
                        create a new request and modify it accordingly using the new token
                        Request newRequest = chain.request().newBuilder().header("token", newToken)
                                             .build();
                        retry the request
                        response.body().close();
                        如果使用okhttp将新的请求,请求成功后,将返回的response  return出去即可
                        如果不需要返回新的结果,则直接把response参数返回出去 */
                        return response;
                    }

                    @Override
                    public Request onHttpRequestBefore(Interceptor.Chain chain, Request request) {
//                         如果需要再请求服务器之前做一些操作,则重新返回一个做过操作的的requeat如增加header,不做操作则直接返回request参数
                        YshNameValuePair nameValuePair = new YshNameValuePair("version", "20");
                        List<YshNameValuePair> list = new ArrayList<>();
                        list.add(nameValuePair);
                        return chain.request().newBuilder().header("sign", new Jni().getSign(context, list))
                                .build();
                    }
                })
                .responseErrorListener((context1, t) -> {
                    Logger.d("=============>" + t.getMessage());
                    CoreUtils.snackbarText(t.getMessage());
                })
                .gsonConfiguration((context12, builder1) -> builder1.serializeNulls())
                .rxCacheConfiguration((context13, builder12) -> builder12.useExpiredDataIfLoaderNotAvailable(true));
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
                BGASwipeBackHelper.init(application, null);
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
                CoreUtils.obtainAppComponentFromContext(context).extras().put(RefWatcher.class.getName(), BuildConfig.USE_CANARY ? LeakCanary.install(application) : RefWatcher.DISABLED);

                //ARouter初始化
                if (BuildConfig.LOG_DEBUG) {
                    ARouter.openLog();     // 打印日志
                    ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
                }
                ARouter.init(application); // 尽可能早，推荐在Application中初始化

                CoreUtils.obtainAppComponentFromContext(application).appManager().setHandleListener((appManager, message) -> {
                    switch (message.what) {
                        case 0:
                            break;
                    }
                });
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
                if (activity.findViewById(R.id.toolbar_left) != null && !(activity instanceof HomeActivity)) {
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
                ((RefWatcher) CoreUtils.obtainAppComponentFromContext(f.getActivity().getApplication()).extras().get(RefWatcher.class.getName())).watch(f);
            }
        });
    }
}
