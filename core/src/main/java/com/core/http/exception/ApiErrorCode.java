package com.core.http.exception;

/**
 * Created by hpw on 17-11-23.
 */

public interface ApiErrorCode {
    /**
     * 用户授权失败
     */
    int ERROR_USER_AUTHORIZED = 301;
    int ERROR_USER_INFO_NOT_ALL = 113;
    int ERROR_USER_INFO_AUDIT = 114;
    int ERROR_PARAMETER = 1000;

    /**
     * 账户或密码错误
     */
    int ERROR_USER = 107;

    int ERROR_USER_1 = 106;
}
