package com.example.uberv.vugraph2;

import android.app.Application;

import com.example.uberv.vugraph2.utils.PreferencesUtils;


public class VuGraphApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // ensure the shared pref is initialized with the Global Context
        PreferencesUtils.initSharedPref(this);
    }

}
