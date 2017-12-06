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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.core.base.delegate.AppLifecycles;
import com.core.di.module.GlobalConfigModule;
import com.core.http.GlobalHttpHandler;
import com.core.http.RequestInterceptor;
import com.core.http.exception.ApiErrorCode;
import com.core.http.exception.ApiException;
import com.core.integration.ConfigModule;
import com.core.integration.cache.BCache;
import com.core.utils.Constants;
import com.core.utils.CoreUtils;
import com.core.utils.DataHelper;
import com.google.gson.Gson;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.recorder.BuildConfig;
import com.recorder.R;
import com.recorder.mvp.model.api.Api;
import com.recorder.mvp.model.entity.LoginBean;
import com.recorder.mvp.ui.activity.BackStageManagerActivity;
import com.recorder.mvp.ui.activity.HomeActivity;
import com.recorder.mvp.ui.activity.MyAttentionActivity;
import com.recorder.mvp.ui.activity.MyInvestmentActivity;
import com.recorder.mvp.ui.activity.SearchActivity;
import com.recorder.utils.DeviceInfoUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import io.reactivex.exceptions.CompositeException;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

import static com.core.http.exception.ApiErrorCode.ERROR_USER_INFO_AUDIT;
import static com.core.http.exception.ApiErrorCode.ERROR_USER_INFO_NOT_ALL;

/**
 * app的全局配置信息在此配置,需要将此实现类声明到AndroidManifest中
 */

public class GlobalConfiguration implements ConfigModule {

    private static boolean isConnection = true;

    @Override
    public void applyOptions(Context context, GlobalConfigModule.Builder builder) {
        //使用builder可以为框架配置一些配置信息
        builder.baseurl(Api.APP_DOMAIN)
                .cacheFile(DataHelper.getCacheFile(context))
                .globalHttpHandler(new GlobalHttpHandler() {
                    @Override
                    public Response onHttpResultResponse(String httpResult, Interceptor.Chain chain, Response response) {
                        if (!TextUtils.isEmpty(httpResult) && RequestInterceptor.isJson(response.body().contentType())) {
                            Logger.json(httpResult);
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(httpResult);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            int code = jsonObject.optInt("errno");
                            switch (code) {
                                case 0:
                                    isConnection = true;
                                    if (chain.request().url().toString().contains("/user/login")) {
                                        CoreUtils.obtainRxCache(context).remove("isClear");
                                        BCache.getInstance().put(Constants.TOKEN, response.header("SESSION-TOKEN"));
                                    }
                                    break;
                                case ApiErrorCode.ERROR_USER_AUTHORIZED:
                                    isConnection = false;
                                    response.body().close();
                                    CoreUtils.obtainRxCache(context).put("isClear", "false");
                                    CoreUtils.showEmpty(Constants.NO_AUTH, R.drawable.ic_no_auth, R.string.empty_no_auth, "去认证");
                                    throw new ApiException(code, jsonObject.optString("error"));
                                case ERROR_USER_INFO_NOT_ALL:
                                    CoreUtils.snackbarText(jsonObject.optString("error"));
                                    break;
                                case ERROR_USER_INFO_AUDIT:
                                    CoreUtils.snackbarText(jsonObject.optString("error"));
                                    break;
                                default:
                                    response.body().close();
                                    CoreUtils.snackbarText(jsonObject.optString("error"));
                            }
                            Object data = jsonObject.optJSONObject("data").opt("total_count");
                            if (response.request().method().equals("GET") && data != null && (Integer) data == 0) {
                                CoreUtils.showEmpty(Constants.NO_DATA, R.drawable.ic_list_empty, R.string.empty_list_empty, "去看项目");
                            }
                        }
                        return response;
                    }

                    @Override
                    public Request onHttpRequestBefore(Interceptor.Chain chain, Request request) {
                        Request.Builder requestBuilder = null;
                        try {
                            String content;
                            if (request.method().equals("POST")) {
                                RequestBody requestBody;
                                Buffer buffer = new Buffer();
                                if (request.body() instanceof MultipartBody) {
//                                    requestBody = ((MultipartBody) request.body()).parts().get(0).body();
//                                    requestBody.writeTo(buffer);
//                                    content = "type=" + URLDecoder.decode(buffer.readUtf8(), "utf-8");
                                    content = "";
                                } else {
                                    requestBody = request.body();
                                    requestBody.writeTo(buffer);
                                    content = URLDecoder.decode(buffer.readUtf8(), "utf-8");
                                }
                            } else {
                                content = chain.request().url().query();
                            }
                            String time = String.valueOf(System.currentTimeMillis() / 1000);
                            String token = BCache.getInstance().getString(Constants.TOKEN);
                            if (TextUtils.isEmpty(content)) {
                                content = "time=" + time;
                            } else {
                                content = (content + "&time=" + time);
                            }
                            String[] strings = content.split("&");
                            Arrays.sort(strings);
                            StringBuilder stringBuilder = new StringBuilder();
                            for (String string : strings) {
                                String[] split = string.split("=");
                                stringBuilder.append(split[0]).append("=").append(split[1].toLowerCase()).append("|");
                            }
                            stringBuilder.append(TextUtils.isEmpty(token) ? "" : token + "|");
                            JSONObject jsonObject = new JSONObject();
                            LoginBean loginBean = new Gson().fromJson(BCache.getInstance().getString(Constants.LOGIN_INFO), LoginBean.class);
                            jsonObject.put("sign", new Jni().getSign(context, stringBuilder.toString()))
                                    .put("time", time)
                                    .put("client", "android");
                            if (loginBean != null) {
                                jsonObject.put("userID", loginBean.getData().getUserID());
                            }
                            requestBuilder = chain.request().newBuilder()
                                    .addHeader("DEVICE-INFO", DeviceInfoUtils.createDeviceInfo(context))
                                    .addHeader("PLATFORM", "api")
                                    .addHeader("AUTH-INFO", jsonObject.toString());
                            if (token != null) {
                                requestBuilder.addHeader("SESSION-TOKEN", token);
                            } else {
                                if (chain.request().url().toString().contains("/deal/list")
                                        || chain.request().url().toString().contains("/user/followlist")
                                        || chain.request().url().toString().contains("/order/list")) {
                                    CoreUtils.showEmpty(Constants.NO_LOGIN, R.drawable.ic_no_login, R.string.empty_no_login, "去登录");
                                    isConnection = false;
                                    return null;
                                }
                            }
                        } catch (Exception e) {
                            Logger.d("AUTH-INFO生成错误: " + e);
                        }
                        return requestBuilder.build();
                    }
                })
                .responseErrorListener((context1, t) -> {
                    if (t instanceof CompositeException && isConnection) {
                        if (((CompositeException) t).getExceptions().toString().contains("RxCacheException")) {
                            CoreUtils.showEmpty(Constants.NO_NET, R.drawable.ic_no_net, R.string.empty_no_net, "重新连接");
                        }
                    } else {
                        if (t instanceof CompositeException) {
                            Logger.d(((CompositeException) t).getExceptions().toString());
                        } else {
                            Logger.d("=============>" + t.getMessage());
                        }
                    }
                })
                .gsonConfiguration((context12, builder1) -> builder1.serializeNulls().enableComplexMapKeySerialization())
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
//                    Timber.plant(new Timber.DebugTree() {
//                        @Override
//                        protected void log(int priority, String tag, String message, Throwable t) {
//                            Logger.log(priority, tag, message, t);
//                        }
//                    });//TODO 暂时关闭(用来打印框架的一些日志)
                }
                //leakCanary内存泄露检查
                CoreUtils.obtainRxCache(context).put(RefWatcher.class.getName(), BuildConfig.IS_RELEASE ? LeakCanary.install(application) : RefWatcher.DISABLED);

                //ARouter初始化
                if (BuildConfig.LOG_DEBUG) {
                    ARouter.openLog();     // 打印日志
                    ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
                }
                ARouter.init(application); // 尽可能早，推荐在Application中初始化
                //友盟相关
                MobclickAgent.setScenarioType(context, MobclickAgent.EScenarioType.E_UM_NORMAL);
                UMConfigure.init(context, "58747d635312dd8e3f000d62", "haoxiang", UMConfigure.DEVICE_TYPE_PHONE, null);
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

                CoreUtils.obtainAppComponentFromContext(context).appManager().setHandleListener((appManager, msg) -> {
                    try {
                        if (msg.what != 5001) {
                            Activity currentActivity = appManager.getCurrentActivity();
                            currentActivity.findViewById(R.id.view_empty).setVisibility(View.VISIBLE);
                            Button button = currentActivity.findViewById(R.id.bt_empty);
                            switch (msg.what) {
                                case Constants.NO_NET:
                                    currentActivity.findViewById(R.id.im_empty).setBackgroundResource(msg.arg1);
                                    ((TextView) currentActivity.findViewById(R.id.tv_empty)).setText(CoreUtils.getString(currentActivity, msg.arg2));
                                    button.setVisibility(View.VISIBLE);
                                    button.setText("重新连接");
                                    break;
                                case Constants.NO_LOGIN:
                                    currentActivity.findViewById(R.id.im_empty).setBackgroundResource(msg.arg1);
                                    ((TextView) currentActivity.findViewById(R.id.tv_empty)).setText(CoreUtils.getString(currentActivity, msg.arg2));
                                    button.setVisibility(View.VISIBLE);
                                    button.setText((CharSequence) msg.obj);
                                    button.setOnClickListener(view -> {
                                        String retry = "";
                                        if (currentActivity instanceof HomeActivity) {
                                            retry = Constants.RETRY_FRAGMENT;
                                        } else if (currentActivity instanceof SearchActivity) {
                                            retry = Constants.RETRY_SEARCH;
                                        } else if (currentActivity instanceof MyAttentionActivity) {
                                            retry = Constants.RETRY_MYATTENTION;
                                        } else if (currentActivity instanceof MyInvestmentActivity) {
                                            retry = Constants.RETRY_MYINVESTMENT;
                                        } else if (currentActivity instanceof BackStageManagerActivity) {
                                            retry = Constants.RETRY_BACKSTAGEMANAGER;
                                        }
                                        ARouter.getInstance().build("/app/LoginActivity").withString(Constants.RETRY_WHEN_LOGIN_OR_AUTH, retry).navigation();
                                    });
                                    break;
                                case Constants.NO_AUTH:
                                    currentActivity.findViewById(R.id.im_empty).setBackgroundResource(msg.arg1);
                                    ((TextView) currentActivity.findViewById(R.id.tv_empty)).setText(CoreUtils.getString(currentActivity, msg.arg2));
                                    button.setVisibility(View.VISIBLE);
                                    button.setText((CharSequence) msg.obj);
                                    button.setOnClickListener(view -> ARouter.getInstance().build("/app/AuthActivity").navigation());
                                    break;
                                case Constants.NO_DATA:
                                    if (currentActivity instanceof HomeActivity) {
                                        button.setVisibility(View.GONE);
                                        currentActivity.findViewById(R.id.im_empty).setBackgroundResource(R.drawable.ic_list_empty);
                                        ((TextView) currentActivity.findViewById(R.id.tv_empty)).setText(CoreUtils.getString(context, R.string.empty_list_empty));
                                    } else if (currentActivity instanceof SearchActivity) {
                                        button.setVisibility(View.GONE);
                                        currentActivity.findViewById(R.id.im_empty).setBackgroundResource(R.drawable.ic_search_emtpy);
                                        ((TextView) currentActivity.findViewById(R.id.tv_empty)).setText(CoreUtils.getString(activity, R.string.ic_search_emtpy));
                                    } else if (currentActivity instanceof MyInvestmentActivity) {
                                        setEmpty(currentActivity, button, R.drawable.ic_investment_empty, R.string.ic_investment_empty);
                                    } else if (currentActivity instanceof MyAttentionActivity) {
                                        setEmpty(currentActivity, button, R.drawable.ic_attention_emtyp, R.string.ic_attention_emtyp);
                                    } else if (currentActivity instanceof BackStageManagerActivity) {
                                        setEmpty(currentActivity, button, R.drawable.ic_manager_empty, R.string.ic_manager_empty);
                                    }
                                    break;
                            }
                        }
                    } catch (Exception e) {
                        Logger.d("Activity设置空状态页面异常" + e.getMessage());
                    }
                });
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                MobclickAgent.onResume(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {
                MobclickAgent.onPause(activity);
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
                ((RefWatcher) CoreUtils.obtainRxCache(f.getActivity().getApplication()).get(RefWatcher.class.getName())).watch(f);
            }

            @Override
            public void onFragmentResumed(FragmentManager fm, Fragment f) {
                super.onFragmentResumed(fm, f);
                MobclickAgent.onResume(f.getContext());
                MobclickAgent.onPageStart(f.getClass().getName());
            }

            @Override
            public void onFragmentPaused(FragmentManager fm, Fragment f) {
                super.onFragmentPaused(fm, f);
                MobclickAgent.onPause(f.getContext());
                MobclickAgent.onPageEnd(f.getClass().getName());
            }
        });
    }

    private void setEmpty(Activity activity, Button button, int res, int text) {
        button.setVisibility(View.VISIBLE);
        button.setText("去看项目");
        button.setVisibility(View.VISIBLE);
        activity.findViewById(R.id.im_empty).setBackgroundResource(res);
        ((TextView) activity.findViewById(R.id.tv_empty)).setText(CoreUtils.getString(activity, text));
        button.setOnClickListener(view -> {
            activity.findViewById(R.id.view_empty).setVisibility(View.GONE);
            EventBus.getDefault().post(1, Constants.HOME_INDEX);
            ARouter.getInstance().build("/app/HomeActivity").navigation();
        });
    }
}
