package com.core.http.exception;

import android.content.Context;

import com.core.utils.CoreUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.functions.Function;

import static android.os.SystemClock.sleep;

/**
 * Created by hpw on 17-11-23.
 */

public class RetryWithToken implements Function<Observable<Throwable>, ObservableSource<?>> {

    public final String TAG = this.getClass().getSimpleName();
    private final int maxRetries;
    private final int retryDelaySecond;
    private int retryCount;
    private Context mContext;

    public RetryWithToken(int maxRetries, int retryDelaySecond, Context mContext) {
        this.maxRetries = maxRetries;
        this.retryDelaySecond = retryDelaySecond;
        this.mContext = mContext;
    }

    @Override
    public ObservableSource<?> apply(@NonNull Observable<Throwable> throwableObservable) throws Exception {
        return throwableObservable
                .flatMap((Function<Throwable, ObservableSource<?>>) throwable -> {
                    if (throwable instanceof ApiException) {
                        switch (((ApiException) throwable).getErrorCode()) {
                            case ApiErrorCode.ERROR_USER_AUTHORIZED:
                                while (true) {
                                    if (CoreUtils.obtainRxCache(mContext).get("token") != null) {
                                        break;
                                    } else {
                                        sleep(1000);
                                    }
                                }
                                return Observable.timer(100, TimeUnit.MILLISECONDS);
                        }
                    } else if (throwable instanceof CompositeException && ((CompositeException) throwable).getExceptions().toString().contains("ApiException")) {
                        while (true) {
                            if (CoreUtils.obtainRxCache(mContext).get("token") != null) {
                                break;
                            } else {
                                sleep(1000);
                            }
                        }
                        return Observable.timer(100, TimeUnit.MILLISECONDS);
                    } else {
                        if (++retryCount <= maxRetries) {
                            return Observable.timer(retryDelaySecond, TimeUnit.SECONDS);
                        }
                    }
                    return Observable.error(throwable);
                });
    }
}