package com.core.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            return true;
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
            return true;
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
            return true;
        }
        Pattern p = Pattern.compile("^[a-zA-Z0-9_-]{5,19}$");
        Matcher m = p.matcher(qqorwx);
        return m.matches();
    }
}
