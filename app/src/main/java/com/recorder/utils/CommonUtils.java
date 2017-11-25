package com.recorder.utils;

import android.text.TextUtils;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by hpw on 17-11-23.
 */

public class CommonUtils {
    /**
     * 判断是否为合法邮箱
     */
    public static boolean isEmail(String strEmail) {
        // String strPattern =
        // "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        String strPattern = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }

    /**
     * 判断是否为合法邮箱
     */
    public static boolean isEmai(String strEmail) {
        if (TextUtils.isEmpty(strEmail)) {
            return false;
        }
        Pattern p = Pattern.compile("^[0-9a-z][[a-z0-9_]*[-_\\.]?[a-z0-9_]+]*@[a-z0-9_][a-z0-9\\-]*[a-z0-9](\\.[a-z0-9][a-z0-9\\-]*[a-z0-9]){1,2}$");
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }

    /**
     * 判断是否为合法手机号
     */
    public static boolean isPhone(String strPhone) {
        if (TextUtils.isEmpty(strPhone)) {
            return false;
        }
        Pattern p = Pattern.compile("(^1[3456789][0-9]{9}$)|(^(0[0-9]{2,3}(\\-)?)?([2-9][0-9]{6,7})+((\\-)?[0-9]{1,4})?$)");
//        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(strPhone);
        return m.matches();
    }

    /**
     * 判断是否为合法微信号或QQ
     */
    public static boolean isQQOrWX(String qqorwx) {
        if (TextUtils.isEmpty(qqorwx)) {
            return false;
        }
        Pattern p = Pattern.compile("^[a-zA-Z0-9_-]{5,19}$");
        Matcher m = p.matcher(qqorwx);
        return m.matches();
    }

    public static void getCode(TextView tvGetCode) {
        final int count = 60;//倒计时10秒
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(count + 1)
                .map(aLong -> count - aLong)
                .observeOn(AndroidSchedulers.mainThread())//ui线程中进行控件更新
                .doOnSubscribe(disposable -> {
                    tvGetCode.setEnabled(false);
                })
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long num) {
                        tvGetCode.setText(num + "S重新获取");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        tvGetCode.setEnabled(true);
                        tvGetCode.setText("获取验证码");
                    }
                });
    }
}
