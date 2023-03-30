package com.example.zimkitdemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zegocloud.zimkit.components.conversation.ui.ZIMKitConversationActivity;
import com.zegocloud.zimkit.services.ZIMKit;

import java.util.Random;

import im.zego.zim.enums.ZIMErrorCode;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initData();
    }

    private void initData() {
        SharedPreferences prefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        String userID = prefs.getString("userID", randomUserId());
        String userName = prefs.getString("userName", Utils.randomName());

        EditText userIDText = findViewById(R.id.et_user_id);
        userIDText.setText(userID);

        EditText userNameText = findViewById(R.id.et_user_name);
        userNameText.setText(userName);

        Button loginBtn = findViewById(R.id.login_button);
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        EditText userIDText = findViewById(R.id.et_user_id);
        EditText userNameText = findViewById(R.id.et_user_name);

        String userId = userIDText.getText().toString();
        String userName = userNameText.getText().toString();
        String avatarUrl = "https://storage.zego.im/IMKit/avatar/avatar-0.png";

        // save userid and username
        SharedPreferences.Editor editor = getSharedPreferences("myPrefs", MODE_PRIVATE).edit();
        editor.putString("userID", userId);
        editor.putString("userName", userName);
        editor.apply();

        ZIMKit.connectUser(userId, userName, avatarUrl, error -> {
            if (error.code != ZIMErrorCode.SUCCESS) {
                String message = error.message + ": " + error.code.value();
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, ConversationActivity.class);
            startActivity(intent);
        });
    }


    private  String randomUserId() {
        int randomNum = 100 + new Random().nextInt(9901);
        return  String.valueOf(randomNum);
    }
}