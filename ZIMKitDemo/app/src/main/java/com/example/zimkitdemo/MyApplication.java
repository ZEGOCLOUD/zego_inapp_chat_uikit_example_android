package com.example.zimkitdemo;

import android.app.Application;
import com.zegocloud.zimkit.services.ZIMKit;
import com.zegocloud.zimkit.services.ZIMKitConfig;

public class MyApplication extends Application {

    public static MyApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        ZIMKitConfig zimKitConfig = new ZIMKitConfig();

        /**
         // if you want to use call plugin,following steps(be sure you have already integrate callkit succeed):

         //step 1. add dependency in build.gradle
         //   implementation 'com.github.ZEGOCLOUD:zego_uikit_prebuilt_call_android:+'

         //step 2. add call buttons in message page.
         zimKitConfig.inputConfig.expandButtons.add(ZIMKitInputButtonName.VOICE_CALL);
         zimKitConfig.inputConfig.expandButtons.add(ZIMKitInputButtonName.VIDEO_CALL);

         //step 3. add call plugin config to init it.
         zimKitConfig.callPluginConfig = new ZegoCallPluginConfig();

         //step 4. custom call plugin config if you need(optional)
         ZegoUIKitPrebuiltCallInvitationConfig invitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();
         zimKitConfig.callPluginConfig.invitationConfig = invitationConfig;
         // offline resourceID,please get value from ZEGOCLOUD Admin Console
         // zimKitConfig.callPluginConfig.resourceID =;

         */

        ZIMKit.initWith(this, KeyCenter.APP_ID, KeyCenter.APP_SIGN, zimKitConfig);
        ZIMKit.initNotifications();
    }
}
