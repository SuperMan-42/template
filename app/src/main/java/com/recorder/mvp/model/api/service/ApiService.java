package com.recorder.mvp.model.api.service;

import com.recorder.mvp.model.entity.EquityBean;
import com.recorder.mvp.model.entity.ImageUploadBean;
import com.recorder.mvp.model.entity.LoginBean;
import com.recorder.mvp.model.entity.NewsListBean;
import com.recorder.mvp.model.entity.UserInfoBean;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by hpw on 17-11-18.
 */

public interface ApiService {
    @FormUrlEncoded
    @POST("user/register")
    Observable<Object> registerUser(@Header("DIVERSION-VERSION") String version, @Field("mobile") String mobile,
                                    @Field("password") String password, @Field("code") String code);

    //type  1-众筹 2-私募 不必须 默认为1-众筹
    @GET("deal/list")
    Observable<EquityBean> getEquity(@Header("DIVERSION-VERSION") String version, @Query("type") String type,
                                     @Query("label_id") String label_id, @Query("round_id") String round_id,
                                     @Query("keyword") String keyword, @Query("page") String page,
                                     @Query("page_size") String page_size);

    @FormUrlEncoded
    @POST("user/login")
    Observable<LoginBean> login(@Header("DIVERSION-VERSION") String version, @Field("mobile") String mobile,
                                @Field("password") String password);

    // 1-注册 2-忘记密码 3-修改密码 [必须]
    @FormUrlEncoded
    @POST("sms/code")
    Observable<Object> smsCode(@Header("DIVERSION-VERSION") String version, @Field("mobile") String mobile,
                               @Field("type") String type, @Field("captcha") String captcha);

    // 1-注册 2-忘记密码 3-修改密码 [必须]
    @FormUrlEncoded
    @POST("sms/verify")
    Observable<Object> smsVerify(@Header("DIVERSION-VERSION") String version, @Field("mobile") String mobile,
                                 @Field("code") String code, @Field("type") String type);

    @FormUrlEncoded
    @POST("user/forgetpwd")
    Observable<Object> userForgetpwd(@Header("DIVERSION-VERSION") String version, @Field("mobile") String mobile,
                                     @Field("code") String code, @Field("password") String password);

    @GET("user/info")
    Observable<UserInfoBean> userInfo(@Header("DIVERSION-VERSION") String version);

    @FormUrlEncoded
    @POST("user/modifypwd")
    Observable<Object> userModifypwd(@Header("DIVERSION-VERSION") String version, @Field("old_password") String old_password,
                                     @Field("password") String password, @Field("code") String code);

    @FormUrlEncoded
    @POST("user/modify")
    Observable<Object> userModify(@Header("DIVERSION-VERSION") String version, @Field("field") String field,
                                  @Field("user_name") String user_name, @Field("intro") String intro, @Field("email") String email,
                                  @Field("weixin") String weixin, @Field("address") String address, @Field("avatar") String avatar);

    @Multipart
    @POST("image/upload")
    Observable<ImageUploadBean> imageUpload(@Header("DIVERSION-VERSION") String version, @Part("type") RequestBody type,
                                            @Part List<MultipartBody.Part> images);

    @GET("news/list")
    Observable<NewsListBean> newsList(@Header("DIVERSION-VERSION") String version, @Query("page") String page, @Query("page_size") String page_size);
}
