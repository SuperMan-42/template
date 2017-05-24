package com.hooks;

import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import com.Module;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class FlagSecureHook extends XC_MethodHook {

    public static final String TAG = "Inspeckage_FlagSecure:";
    private static XSharedPreferences sPrefs;

    public static void loadPrefs() {
        sPrefs = new XSharedPreferences(Module.class.getPackage().getName(), Module.PREFS);
        sPrefs.makeWorldReadable();
    }

    public static void initAllHooks(final XC_LoadPackage.LoadPackageParam loadPackageParam) {

        findAndHookMethod(Window.class, "setFlags",
                "int", "int", new XC_MethodHook() {

                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        loadPrefs();
                        if (sPrefs.getBoolean("flag_secure", false)) {
                            if ((Integer) param.args[0] == WindowManager.LayoutParams.FLAG_SECURE) {
                                param.args[0] = 0;
                                param.args[1] = 0;
                            }
                        }
                    }
                });

        //Build.VERSION.SDK_INT >= 17
        findAndHookMethod(SurfaceView.class, "setSecure", boolean.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        loadPrefs();
                        if (sPrefs.getBoolean("flag_secure", false)) {
                            param.args[0] = false;
                        }
                    }
                });
    }
}
