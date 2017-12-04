package com.recorder.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
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

    public static void setStatusBarLightMode(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //判断是否为小米或魅族手机，如果是则将状态栏文字改为黑色
            if (MIUISetStatusBarLightMode(activity, true) || FlymeSetStatusBarLightMode(activity, true)) {
                //设置状态栏为指定颜色
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0
                    activity.getWindow().setStatusBarColor(color);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4
                    //调用修改状态栏颜色的方法
                    StatusBarUtil.setColorForSwipeBack(activity, color, 0);
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //如果是6.0以上将状态栏文字改为黑色，并设置状态栏颜色
                activity.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                activity.getWindow().setStatusBarColor(color);

                //fitsSystemWindow 为 false, 不预留系统栏位置.
                ViewGroup mContentView = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
                View mChildView = mContentView.getChildAt(0);
                if (mChildView != null) {
                    ViewCompat.setFitsSystemWindows(mChildView, true);
                    ViewCompat.requestApplyInsets(mChildView);
                }
            }
        }
    }

    static boolean MIUISetStatusBarLightMode(Activity activity, boolean darkmode) {
        boolean result = false;
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    static boolean FlymeSetStatusBarLightMode(Activity activity, boolean darkmode) {
        boolean result = false;
        try {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class
                    .getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (darkmode) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            activity.getWindow().setAttributes(lp);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String toStringFromList(List<String> selectorList, String tag) {
        if (selectorList.size() != 0) {
            StringBuilder builder = new StringBuilder();
            for (String string : selectorList) {
                builder.append(string).append(tag);
            }
            return builder.deleteCharAt(builder.length() - 1).toString();
        } else {
            return "";
        }
    }

    /**
     * Service download version of the Transformer.
     *
     * @param url      url
     * @param savePath savePath
     * @param isDir    isDir
     * @return Transformer
     */
    public static <Upstream> ObservableTransformer<Upstream, BaseDownloadTask> transformService(
            Context context, String url, String savePath, boolean isDir, boolean hasNext) {
        return new ObservableTransformer<Upstream, BaseDownloadTask>() {
            @Override
            public ObservableSource<BaseDownloadTask> apply(io.reactivex.Observable<Upstream> upstream) {
                return upstream.create(new ObservableOnSubscribe<BaseDownloadTask>() {
                    @Override
                    public void subscribe(ObservableEmitter<BaseDownloadTask> observableEmitter) throws Exception {
                        if (!TextUtils.isEmpty(url)) {
                            FileDownloader.setup(context);
                            FileDownloader.getImpl().create(url).setPath(savePath, isDir)
                                    .setCallbackProgressTimes(300)
                                    .setMinIntervalUpdateSpeed(400)
                                    .setListener(new FileDownloadSampleListener() {

                                        @Override
                                        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                            super.pending(task, soFarBytes, totalBytes);
                                        }

                                        @Override
                                        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                            super.progress(task, soFarBytes, totalBytes);
                                            if (hasNext)
                                                observableEmitter.onNext(task);
                                        }

                                        @Override
                                        protected void error(BaseDownloadTask task, Throwable e) {
                                            super.error(task, e);
                                            observableEmitter.onError(e);
                                        }

                                        @Override
                                        protected void connected(BaseDownloadTask task, String etag, boolean isContinue,
                                                                 int soFarBytes, int totalBytes) {
                                            super.connected(task, etag, isContinue, soFarBytes, totalBytes);
                                        }

                                        @Override
                                        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                            super.paused(task, soFarBytes, totalBytes);
                                        }

                                        @Override
                                        protected void completed(BaseDownloadTask task) {
                                            super.completed(task);
                                            if (hasNext) {
                                                observableEmitter.onComplete();
                                            } else {
                                                observableEmitter.onNext(task);
                                            }
                                        }

                                        @Override
                                        protected void warn(BaseDownloadTask task) {
                                            super.warn(task);
                                        }
                                    }).start();
                        } else {
                            if (hasNext) {
                                observableEmitter.onError(new Throwable("url为空"));
                            } else {
                                observableEmitter.onComplete();
                            }
                        }
                    }
                });
            }
        };
    }

    public static <Upstream> ObservableTransformer<Upstream, BaseDownloadTask> transformPay(
            Context context, String url, String savePath, boolean isDir, boolean hasNext) {
        return new ObservableTransformer<Upstream, BaseDownloadTask>() {
            @Override
            public ObservableSource<BaseDownloadTask> apply(io.reactivex.Observable<Upstream> upstream) {
                return upstream.create(new ObservableOnSubscribe<BaseDownloadTask>() {
                    @Override
                    public void subscribe(ObservableEmitter<BaseDownloadTask> observableEmitter) throws Exception {
                        if (!TextUtils.isEmpty(url)) {
                            FileDownloader.setup(context);
                            FileDownloader.getImpl().create(url).setPath(savePath, isDir)
                                    .setCallbackProgressTimes(300)
                                    .setMinIntervalUpdateSpeed(400)
                                    .setListener(new FileDownloadSampleListener() {

                                        @Override
                                        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                            super.pending(task, soFarBytes, totalBytes);
                                        }

                                        @Override
                                        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                            super.progress(task, soFarBytes, totalBytes);
                                            if (hasNext)
                                                observableEmitter.onNext(task);
                                        }

                                        @Override
                                        protected void error(BaseDownloadTask task, Throwable e) {
                                            super.error(task, e);
                                            observableEmitter.onError(e);
                                        }

                                        @Override
                                        protected void connected(BaseDownloadTask task, String etag, boolean isContinue,
                                                                 int soFarBytes, int totalBytes) {
                                            super.connected(task, etag, isContinue, soFarBytes, totalBytes);
                                        }

                                        @Override
                                        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                            super.paused(task, soFarBytes, totalBytes);
                                        }

                                        @Override
                                        protected void completed(BaseDownloadTask task) {
                                            super.completed(task);
                                            if (hasNext) {
                                                observableEmitter.onComplete();
                                            } else {
                                                observableEmitter.onNext(task);
                                            }
                                        }

                                        @Override
                                        protected void warn(BaseDownloadTask task) {
                                            super.warn(task);
                                        }
                                    }).start();
                        } else {
                            if (hasNext) {
                                observableEmitter.onError(new Throwable("url为空"));
                            } else {
                                observableEmitter.onComplete();
                            }
                        }
                    }
                });
            }
        };
    }

//    public static void pay(Activity mActivity, String order_sn, String prj_name, String brief,
//                           String img, String productID, boolean isEmail, boolean isMy) {
//        ArrayList<YshNameValuePair> valuePairs = new ArrayList<>();
//        valuePairs.add(new YshNameValuePair("order_sn", order_sn));
//        RxUtil.post(mActivity, Action.PRODUCT_PAY, "32", OrderPayBean.class, valuePairs)
//                .subscribe(
//                        data -> {
//                            OrderPayBean temp = (OrderPayBean) data;
//                            // 封装接口参数
//                            Bundle b = new Bundle();
//                            b.putString("merchantId", temp.getMerchantId()); // 商户ID
//                            b.putString("userId", Session.getInstance(mActivity).getUid()); // 商户系统用户ID
//                            b.putString("outOrderId", temp.getOuterOrderId()); // 商户订单号
////                            b.putString("merchantBankCardNo", temp.get); // 银行卡号(非必填)
//                            b.putString("sign", temp.getSign()); // 签名由商户后台签名后传给客户端，分为MD5和RSA两种签名加密
//
//                            // 认证支付接口
//                            UcfpayInterface.gotoPay(mActivity, b, new ResultReceiver(new Handler()) {
//                                @Override
//                                protected void onReceiveResult(int resultCode, Bundle resultData) {
//                                    super.onReceiveResult(resultCode, resultData);
//                                    // 获取Bundle中的数据，做业务处理
//                                    switch (resultCode) {
//                                        case -5:
//                                            // 充值处理中
//                                            LogUtils.i(TAG, "充值处理中");
//                                            break;
//                                        case -4:
//                                            // 认证结果失败
//                                            LogUtils.i(TAG, "认证结果失败");
//                                            payFailed(mActivity, temp, prj_name, brief, img, order_sn, productID, "认证结果失败", isEmail, isMy);
//                                            break;
//                                        case -3:
//                                            // 认证结果处理中
//                                            LogUtils.i(TAG, "认证结果处理中");
//                                            break;
//                                        case -2:
//                                            // 错误
//                                            LogUtils.i(TAG, "错误");
//                                            payFailed(mActivity, temp, prj_name, brief, img, order_sn, productID, "支付超时", isEmail, isMy);
//                                            break;
//                                        case -1:
//                                            // 用户主动退出
//                                            LogUtils.i(TAG, "用户主动退出");
//                                            payFailed(mActivity, temp, prj_name, brief, img, order_sn, productID, "用户主动退出", isEmail, isMy);
//                                            break;
//                                        case 0:
//                                            // 充值成功
//                                            LogUtils.i(TAG, "充值成功");
//                                            paySuccess(mActivity, temp, prj_name, brief, img, order_sn, productID, isEmail, isMy);
//                                            break;
//                                    }
//                                }
//                            });
//                        },
//                        e -> ToastUtils.displayToast(mActivity, ((CoreApiException) e).getMessage()));
//    }
}
