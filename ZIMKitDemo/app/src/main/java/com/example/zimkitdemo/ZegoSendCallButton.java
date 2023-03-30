package com.example.zimkitdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.example.zimkitdemo.databinding.SendCallLayoutBinding;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;
import java.util.ArrayList;
import java.util.List;


public class ZegoSendCallButton extends FrameLayout {

    private SendCallLayoutBinding mBinding;
    private String userId;
    private String userName;
    public ZegoSendCallButton(Context context, String userId, String userName) {
        super(context);
        this.userId = userId;
        this.userName = userName;
        initView(context);
    }

    public ZegoSendCallButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ZegoSendCallButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.send_call_layout, this, true);
        initVoiceButton();
        initVideoButton();
    }

    private void initVideoButton() {
        mBinding.newVideoCall.setIsVideoCall(true);
        mBinding.newVideoCall.setOnClickListener(v -> {
            List<ZegoUIKitUser> users = new ArrayList<>();
            users.add(new ZegoUIKitUser(userId, userName));
            mBinding.newVideoCall.setInvitees(users);
        });
    }

    private void initVoiceButton() {
        mBinding.newVoiceCall.setIsVideoCall(false);
        mBinding.newVoiceCall.setOnClickListener(v -> {
            List<ZegoUIKitUser> users = new ArrayList<>();
            users.add(new ZegoUIKitUser(userId, userName));
            mBinding.newVoiceCall.setInvitees(users);
        });
    }
}
