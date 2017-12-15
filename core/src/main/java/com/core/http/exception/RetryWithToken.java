package com.core.http.exception;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by hpw on 17-11-23.
 */

public class RetryWithToken implements Function<Observable<Throwable>, ObservableSource<?>> {

    public final String TAG = this.getClass().getSimpleName();
    private final int maxRetries;
    private final int retryDelaySecond;
    private int retryCount;

    public RetryWithToken(int maxRetries, int retryDelaySecond) {
        this.maxRetries = maxRetries;
        this.retryDelaySecond = retryDelaySecond;
    }

    @Override
    public ObservableSource<?> apply(@NonNull Observable<Throwable> throwableObservable) throws Exception {
        return throwableObservable
                .flatMap((Function<Throwable, ObservableSource<?>>) throwable -> {
//                    if (throwable instanceof ApiException) {
//                        switch (((ApiException) throwable).getErrorCode()) {
//                            case ApiErrorCode.ERROR_USER_AUTHORIZED:
//                                for (int i = 0; i < 60; i++) {
//                                    if (BCache.getInstance().getString("token") != null || CoreUtils.obtainRxCache(BaseApplication.getContext()).get("isClear") == null) {
//                                        break;
//                                    } else {
//                                        Logger.d("apiexception, retrywithtoken=> sleep");
//                                        sleep(1000);
//                                    }
//                                }
//                                Logger.d("apiexception, retrywithtoken=> break");
//                                return Observable.timer(100, TimeUnit.MILLISECONDS);
//                        }
//                    } else if (throwable instanceof CompositeException && ((CompositeException) throwable).getExceptions().toString().contains("ApiException")) {
//                        for (int i = 0; i < 60; i++) {
//                            if (BCache.getInstance().getString("token") != null || CoreUtils.obtainRxCache(BaseApplication.getContext()).get("isClear") == null) {
//                                break;
//                            } else {
//                                Logger.d("CompositeException, retrywithtoken=> sleep");
//                                sleep(1000);
//                            }
//                        }
//                        Logger.d("CompositeException, retrywithtoken=> break");
//                        return Observable.timer(100, TimeUnit.MILLISECONDS);
//                    } else {
                    com.orhanobut.logger.Logger.d("retry=> " + throwable.toString() + " " + new Gson().toJson(throwable));
                    if (++retryCount <= maxRetries) {
                        return Observable.timer(retryDelaySecond, TimeUnit.SECONDS);
                    }
//                    }
                    return Observable.error(throwable);
                });
    }
}