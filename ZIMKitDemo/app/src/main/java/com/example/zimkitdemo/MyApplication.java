package com.example.zimkitdemo;

import android.app.Application;

import com.zegocloud.zimkit.services.ZIMKit;

public class MyApplication extends Application {
    public static MyApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        ZIMKit.initWith(this, KeyCenter.APP_ID, KeyCenter.APP_SIGN);
        ZIMKit.initNotifications();
    }
}
