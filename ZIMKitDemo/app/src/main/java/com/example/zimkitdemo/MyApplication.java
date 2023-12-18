package com.example.zimkitdemo;

import android.app.Application;

import com.zegocloud.zimkit.services.ZIMKit;
import com.zegocloud.zimkit.services.config.InputConfig;

public class MyApplication extends Application {
    public static MyApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        ZIMKit.initWith(this, KeyCenter.APP_ID, KeyCenter.APP_SIGN);
        ZIMKit.initNotifications();

        InputConfig inputConfig = new InputConfig();
        inputConfig.showVoiceButton = true;
        inputConfig.showEmojiButton = true;
        inputConfig.showAddButton = true;
        ZIMKit.setInputConfig(inputConfig);
    }
}
