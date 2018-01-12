package com.recorder.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 设备信息工具类
 *
 * @author liaiguo
 */
public class DeviceInfoUtils {

    private static final String TAG = "DeviceInfoUtils";
    private static File file = new File(
            "data/data/com.subject.ysh/file.xml");

    public static String createDeviceInfo(Context context) {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setID(DeviceInfoUtils.getDeviceId(context));
        deviceInfo.setModel(DeviceInfoUtils.getModel());
        deviceInfo.setOS(DeviceInfoUtils.getSystemVersion());
        deviceInfo.setVer(DeviceInfoUtils.getSoftVersion(context));
        deviceInfo.setType("2");
        deviceInfo.setMAC(DeviceInfoUtils.getLocalMac(context));
        deviceInfo.setRES(DeviceInfoUtils.getResolution(context));
        deviceInfo.setNetOP(DeviceInfoUtils.getProvidersName(context));
        deviceInfo.setNetType(DeviceInfoUtils.getNetType(context));
        // 下载渠道
        deviceInfo.setDownload("haoxiang");
        // 用户id
        deviceInfo.setUid("0");
        String deviceInfoJson = new Gson().toJson(deviceInfo);
        return deviceInfoJson;
    }


    /**
     * 设备名
     *
     * @return
     */
    public static String getDiviceName() {
        return "android";
    }

    /**
     * 设备id
     */
    public static String getDeviceId(Context context) {
        String deviceId = "";
        try {
            deviceId = getIMIEStatus(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (deviceId == null || "".equals(deviceId) || "0".equals(deviceId)) {
            try {
                deviceId = getLocalMac(context).replace(":", "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (deviceId == null || "".equals(deviceId) || "0".equals(deviceId)) {
            try {
                deviceId = getAndroidId(context);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (deviceId == null || "".equals(deviceId) || "0".equals(deviceId)) {
            try {
                deviceId = read();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (deviceId == null || "".equals(deviceId) || "0".equals(deviceId)) {
                UUID uuid = UUID.randomUUID();
                deviceId = uuid.toString().replace("-", "");
                write(deviceId);
            }
        }
        return deviceId;
    }

    // IMEI码
    private static String getIMIEStatus(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        return deviceId;
    }

    // Mac地址
    public static String getLocalMac(Context context) {
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    public static String getAndroidId(Context context) {
        String androidId = Settings.Secure.getString(
                context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }

    public static void write(String str) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            Writer out = new OutputStreamWriter(fos, "UTF-8");
            out.write(str);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String read() {
        StringBuffer buffer = new StringBuffer();
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            Reader in = new BufferedReader(isr);
            int i;
            while ((i = in.read()) > -1) {
                buffer.append((char) i);
            }
            in.close();
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 软件版本号
     */
    public static String getSoftVersion(Context context) {
        PackageInfo info;
        try {
            info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return info.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "1.0";
        }
    }

    public static String getSystemVersion() {
        return replaceBlank(android.os.Build.VERSION.RELEASE);
    }

    private static String replaceBlank(String mString) {
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        String str = mString;
        Matcher m = p.matcher(str);
        String after = m.replaceAll("");
        return after;
    }

    /**
     * 设备信息 包括厂商 设备分辨率
     */
    public static String getDeviceInfo(Context context) {
        String deviceInfo = "";
        String company = replaceBlank((android.os.Build.MANUFACTURER + android.os.Build.MODEL)
                .replace("_", "").trim());
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        int heightWidth = context.getResources().getDisplayMetrics().heightPixels;

        deviceInfo = company + "#" + screenWidth + "*" + heightWidth;

        return deviceInfo;
    }

    /**
     * 分辨率
     */
    public static String getResolution(Context context) {
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        int heightWidth = context.getResources().getDisplayMetrics().heightPixels;
        return screenWidth + "x" + heightWidth;
    }

    /**
     * 设备厂商
     */
    public static String getModel() {
        String model = replaceBlank((android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL)
                .replace("_", "").trim());
        return model;
    }

    public static String getSerial(Context context) {
        String serialNum = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            serialNum = (String) (get.invoke(c, "ro.serialno", "unknown"));
        } catch (Exception ignored) {
        }
        return serialNum;
    }

    public static String getDeviceType() {
        return "android";
    }

    /**
     * 获取运营商信息
     */
    public static String getProvidersName(Context c) {
//		String providersName = "未知运营商";
        String providersName = "Unknown";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) c
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String operator = telephonyManager.getSimOperator();
            if (operator == null || operator.equals("")) {
                operator = telephonyManager.getSubscriberId();
            }
            if (operator == null || operator.equals("")) {

            }
            if (operator != null) {
                if (operator.startsWith("46000")
                        || operator.startsWith("46002")) {
//					providersName = "中国移动";
                    providersName = "China Mobile";
                } else if (operator.startsWith("46001")) {
//					providersName = "中国联通";
                    providersName = "China Unicom";
                } else if (operator.startsWith("46003")) {
//					providersName = "中国电信";
                    providersName = "China Telecom";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return providersName;
    }

    /**
     * 枚举网络状态 NET_NO：没有网络 NET_2G:2g网络 NET_3G：3g网络 NET_4G：4g网络 NET_WIFI：wifi
     * NET_UNKNOWN：未知网络
     */
    public static enum NetState {
        NET_NO, NET_2G, NET_3G, NET_4G, NET_WIFI, NET_UNKNOWN
    }

    ;

    /**
     * 判断当前是否网络连接
     *
     * @return 状态码
     */
    public static NetState getNetState(Context context) {
        NetState stateCode = NetState.NET_NO;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        try {
            if (ni != null && ni.isConnectedOrConnecting()) {
                switch (ni.getType()) {
                    case ConnectivityManager.TYPE_WIFI:
                        stateCode = NetState.NET_WIFI;
                        break;
                    case ConnectivityManager.TYPE_MOBILE:
                        switch (ni.getSubtype()) {
                            case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g
                            case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
                            case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g
                            case TelephonyManager.NETWORK_TYPE_1xRTT:
                            case TelephonyManager.NETWORK_TYPE_IDEN:
                                stateCode = NetState.NET_2G;
                                break;
                            case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g
                            case TelephonyManager.NETWORK_TYPE_UMTS:
                            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                            case TelephonyManager.NETWORK_TYPE_HSDPA:
                            case TelephonyManager.NETWORK_TYPE_HSUPA:
                            case TelephonyManager.NETWORK_TYPE_HSPA:
                            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                            case TelephonyManager.NETWORK_TYPE_EHRPD:
                            case TelephonyManager.NETWORK_TYPE_HSPAP:
                                stateCode = NetState.NET_3G;
                                break;
                            case TelephonyManager.NETWORK_TYPE_LTE:
                                stateCode = NetState.NET_4G;
                                break;
                            default:
                                stateCode = NetState.NET_UNKNOWN;
                        }
                        break;
                    default:
                        stateCode = NetState.NET_UNKNOWN;
                }

            }
        } catch (Exception e) {
            stateCode = NetState.NET_UNKNOWN;
        }

        return stateCode;
    }

    public static String getNetType(Context context) {
        String netType = "未知网络";
        NetState netState = getNetState(context);
        switch (netState) {
            case NET_NO:
                // NET_NO：没有网络
                netType = "没有网络";
                break;
            case NET_2G:
                // 2g网络
                netType = "2G";
                break;
            case NET_3G:
                // 3g网络
                netType = "3G";
                break;
            case NET_4G:
                // 4g网络
                netType = "4G";
                break;
            case NET_UNKNOWN:
                netType = "未知网络";
                break;
            case NET_WIFI:
                // wifi
                netType = "Wifi";
                break;

            default:
                netType = "未知网络";
                break;
        }
        return netType;
    }

}
