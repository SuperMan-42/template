package com.recorder;

import android.content.SharedPreferences;

import com.Module;
import com.core.CoreApp;
import com.core.utils.Constants;

import java.io.File;

/**
 * Created by hpw on 2017/2/21.
 */

public class App extends CoreApp {

    private static SharedPreferences mPrefs;

    @Override
    public String setBaseUrl() {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPrefs = getSharedPreferences(Module.PREFS, MODE_WORLD_READABLE);
        File inspeckage = new File(Constants.SD_CARD);
        if (!inspeckage.exists()) {
            inspeckage.mkdirs();
        }
    }

    public static SharedPreferences getmPrefs() {
        return mPrefs;
    }
}
