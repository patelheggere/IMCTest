package com.patelheggere.imctest;

import android.app.Application;
import android.os.StrictMode;


public class IMCApplication extends Application {
    private static IMCApplication mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

    }

    public static synchronized IMCApplication getInstance() {
        return mInstance;
    }

}