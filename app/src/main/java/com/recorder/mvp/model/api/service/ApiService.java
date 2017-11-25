package com.recorder.mvp.model.api.service;

import com.recorder.mvp.model.entity.EquityBean;
import com.recorder.mvp.model.entity.LoginBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by hpw on 17-11-18.
 */

public interface ApiService {
    @FormUrlEncoded
    @POST("user/register")
    Observable<Object> registerUser(@Header("DIVERSION-VERSION") String version, @Field("mobile") String mobile, @Field("password") String password,
                                    @Field("code") String code);

    //type  1-众筹 2-私募 不必须 默认为1-众筹
    @GET("deal/list")
    Observable<EquityBean> getEquity(@Header("DIVERSION-VERSION") String version, @Query("type") String type, @Query("label_id") String label_id,
                                     @Query("round_id") String round_id, @Query("keyword") String keyword, @Query("page") String page, @Query("page_size") String page_size);

    @FormUrlEncoded
    @POST("user/login")
    Observable<LoginBean> login(@Header("DIVERSION-VERSION") String version, @Field("mobile") String mobile, @Field("password") String password);

    // 1-注册 2-忘记密码 3-修改密码 [必须]
    @FormUrlEncoded
    @POST("sms/code")
    Observable<Object> smsCode(@Header("DIVERSION-VERSION") String version, @Field("mobile") String mobile, @Field("type") String type, @Field("captcha") String captcha);

    // 1-注册 2-忘记密码 3-修改密码 [必须]
    @FormUrlEncoded
    @POST("sms/verify")
    Observable<Object> smsVerify(@Header("DIVERSION-VERSION") String version, @Field("mobile") String mobile, @Field("code") String code, @Field("type") String type);

    @FormUrlEncoded
    @POST("user/forgetpwd")
    Observable<Object> userForgetpwd(@Header("DIVERSION-VERSION") String version, @Field("mobile") String mobile, @Field("code") String code, @Field("password") String password);
}
