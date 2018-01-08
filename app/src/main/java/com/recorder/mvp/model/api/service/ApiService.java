package com.recorder.mvp.model.api.service;

import com.recorder.mvp.model.entity.AppMsgsBean;
import com.recorder.mvp.model.entity.AppStartBean;
import com.recorder.mvp.model.entity.AppVersionBean;
import com.recorder.mvp.model.entity.AuthBean;
import com.recorder.mvp.model.entity.AuthGetBean;
import com.recorder.mvp.model.entity.DealDetailBean;
import com.recorder.mvp.model.entity.DealFilter;
import com.recorder.mvp.model.entity.EquityBean;
import com.recorder.mvp.model.entity.HelpContentBean;
import com.recorder.mvp.model.entity.HelpListBean;
import com.recorder.mvp.model.entity.HomeRecommendBean;
import com.recorder.mvp.model.entity.ImageUploadBean;
import com.recorder.mvp.model.entity.LoginBean;
import com.recorder.mvp.model.entity.NewsListBean;
import com.recorder.mvp.model.entity.OrderListBean;
import com.recorder.mvp.model.entity.OrderPimanageBean;
import com.recorder.mvp.model.entity.PayCheckBean;
import com.recorder.mvp.model.entity.PayPayBean;
import com.recorder.mvp.model.entity.PayPayOffLineBean;
import com.recorder.mvp.model.entity.UserAuthInfoBean;
import com.recorder.mvp.model.entity.UserFollowListBean;
import com.recorder.mvp.model.entity.UserInfoBean;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
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
    Observable<LoginBean> registerUser(@Header("DIVERSION-VERSION") String version, @Field("mobile") String mobile,
                                       @Field("password") String password, @Field("code") String code);

    //type  1-众筹 2-私募 不必须 默认为1-众筹
    @GET("deal/list")
    Observable<EquityBean> dealList(@Header("DIVERSION-VERSION") String version, @Query("type") String type,
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
    Observable<ImageUploadBean> imageUpload(@Header("DIVERSION-VERSION") String version, @Part List<MultipartBody.Part> images);

    @GET("news/list")
    Observable<NewsListBean> newsList(@Header("DIVERSION-VERSION") String version, @Query("page") String page, @Query("page_size") String page_size);

    @GET("deal/filter")
    Observable<DealFilter> dealFilter(@Header("DIVERSION-VERSION") String version);

    @GET("home/recommend")
    Observable<HomeRecommendBean> homeRecommend(@Header("DIVERSION-VERSION") String version);

    @GET("deal/detail")
    Observable<DealDetailBean> dealDetail(@Header("DIVERSION-VERSION") String version, @Query("dealID") String dealID);

    @FormUrlEncoded
    @POST("deal/consult")
    Observable<Object> dealConsult(@Header("DIVERSION-VERSION") String version, @Field("dealID") String dealID);

    @FormUrlEncoded
    @POST("deal/unfollow")
    Observable<Object> dealUnfollow(@Header("DIVERSION-VERSION") String version, @Field("dealID") String dealID);

    @FormUrlEncoded
    @POST("deal/follow")
    Observable<Object> dealFollow(@Header("DIVERSION-VERSION") String version, @Field("dealID") String dealID);

    @GET("user/followlist")
    Observable<UserFollowListBean> userFollowlist(@Header("DIVERSION-VERSION") String version, @Query("page") String page,
                                                  @Query("page_size") String page_size);

    @FormUrlEncoded
    @POST("deal/recommend")
    Observable<Object> dealRecommend(@Header("DIVERSION-VERSION") String version, @Field("deal_name") String deal_name,
                                     @Field("industry") String industry, @Field("requirement") String requirement,
                                     @Field("contact") String contact, @Field("phone") String phone,
                                     @Field("business") String business, @Field("team") String team);

    @GET("pay/check")
    Observable<PayCheckBean> payCheck(@Header("DIVERSION-VERSION") String version, @Query("dealID") String dealID);

    @FormUrlEncoded
    @POST("pay/pay")
    Observable<PayPayBean> payPay(@Header("DIVERSION-VERSION") String version, @Field("dealID") String dealID,
                                  @Field("amount") String amount, @Field("payment_way") String payment_way);

    @FormUrlEncoded
    @POST("pay/pay")
    Observable<PayPayOffLineBean> payPayOffLine(@Header("DIVERSION-VERSION") String version, @Field("dealID") String dealID,
                                                @Field("amount") String amount, @Field("payment_way") String payment_way);

    @GET("order/list")
    Observable<OrderListBean> orderList(@Header("DIVERSION-VERSION") String version, @Query("page") String page,
                                        @Query("page_size") String page_size);

    @FormUrlEncoded
    @POST("order/pay")
    Observable<PayPayBean> orderPay(@Header("DIVERSION-VERSION") String version, @Field("orderID") String orderID,
                                    @Field("payment_way") String payment_way);

    @GET("order/pimanage")
    Observable<OrderPimanageBean> orderPimanage(@Header("DIVERSION-VERSION") String version);

    @GET("app/start")
    Observable<AppStartBean> appStart(@Header("DIVERSION-VERSION") String version);

    @FormUrlEncoded
    @POST("app/feedback")
    Observable<Object> appFeedback(@Header("DIVERSION-VERSION") String version, @Field("content") String content,
                                   @Field("contact") String contact);

    @GET("help/list")
    Observable<HelpListBean> helpList(@Header("DIVERSION-VERSION") String version);

    @GET("help/content")
    Observable<HelpContentBean> helpContent(@Header("DIVERSION-VERSION") String version, @Query("helperID") String helperID);

    @FormUrlEncoded
    @POST("order/proof")
    Observable<Object> orderProof(@Header("DIVERSION-VERSION") String version, @Field("orderID") String orderID, @Field("proof") String proof);

    @FormUrlEncoded
    @POST("auth/person")
    Observable<AuthBean> authPerson(@Header("DIVERSION-VERSION") String version, @Field("type") int type, @Field("true_name") String true_name,
                                    @Field("id_card") String id_card, @Field("idcard_imgf") String idcard_imgf, @Field("idcard_imgb") String idcard_imgb,
                                    @Field("check") String check, @Field("assets") String assets);

    @FormUrlEncoded
    @POST("auth/organ")
    Observable<AuthBean> authOrgan(@Header("DIVERSION-VERSION") String version, @Field("organ_name") String organ_name, @Field("legal_person") String legal_person,
                                   @Field("contact") String contact, @Field("license") String license, @Field("check") String check, @Field("assets") String assets);

    @GET("user/authinfo")
    Observable<UserAuthInfoBean> userAuthInfo(@Header("DIVERSION-VERSION") String version);

    @GET("auth/get")
    Observable<AuthGetBean> authGet(@Header("DIVERSION-VERSION") String version, @Query("type") int type);

    @GET("app/version")
    Observable<AppVersionBean> appVersion(@Header("DIVERSION-VERSION") String version);

    @GET("app/msgs")
    Observable<AppMsgsBean> appMsgs(@Header("DIVERSION-VERSION") String version, @Query("page") String page, @Query("page_size") String page_size);

    @GET("verify.php")
    Observable<ResponseBody> verify(@Header("DIVERSION-VERSION") String version, @Query("token") String token);
}
