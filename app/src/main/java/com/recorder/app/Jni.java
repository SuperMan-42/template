package com.recorder.app;

/**
 * Created by hpw on 17-11-19.
 */

public class Jni {
    static {
        System.loadLibrary("hx_lib");
    }
    public native String getSign();
}
