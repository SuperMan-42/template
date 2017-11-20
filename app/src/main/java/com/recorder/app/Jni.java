package com.recorder.app;

import android.content.Context;

/**
 * Created by hpw on 17-11-19.
 */

public class Jni {
    static {
        System.loadLibrary("hx-lib");
    }

    public native String getSign(Context context, String query);
}
