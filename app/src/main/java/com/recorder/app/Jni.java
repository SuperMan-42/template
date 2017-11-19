package com.recorder.app;

import android.content.Context;

import com.recorder.YshNameValuePair;

import java.util.List;

/**
 * Created by hpw on 17-11-19.
 */

public class Jni {
    static {
        System.loadLibrary("hx-lib");
    }

    public native String getSign(Context context, List<YshNameValuePair> nameValuePairs);
}
