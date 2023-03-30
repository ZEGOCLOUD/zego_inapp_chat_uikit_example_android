package com.example.zimkitdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;
import com.zegocloud.zimkit.common.ZIMKitRouter;
import com.zegocloud.zimkit.common.enums.ZIMKitConversationType;
import com.zegocloud.zimkit.components.message.interfaces.ZIMKitMessagesListListener;
import com.zegocloud.zimkit.components.message.model.ZIMKitHeaderBar;
import com.zegocloud.zimkit.components.message.ui.ZIMKitMessageFragment;
import com.zegocloud.zimkit.services.ZIMKit;
import com.zegocloud.zimkit.services.callback.CreateGroupCallback;
import com.zegocloud.zimkit.services.callback.JoinGroupCallback;
import com.zegocloud.zimkit.services.model.ZIMKitGroupInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import im.zego.zim.ZIM;
import im.zego.zim.entity.ZIMError;
import im.zego.zim.entity.ZIMErrorUserInfo;
import im.zego.zim.enums.ZIMConversationType;
import im.zego.zim.enums.ZIMErrorCode;

public class ConversationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        initData();
    }

    private void initData() {
        String userID = ZIMKit.getLocalUser().getId();
        String userName = ZIMKit.getLocalUser().getName();
        ZegoUIKitPrebuiltCallInvitationConfig config = new ZegoUIKitPrebuiltCallInvitationConfig();
        ZegoUIKitPrebuiltCallInvitationService.init(MyApplication.sInstance,
                                                    KeyCenter.APP_ID,
                                                    KeyCenter.APP_SIGN,
                                                    userID,
                                                    userName,
                                                    config);

        ZIMKit.registerMessageListListener(new ZIMKitMessagesListListener() {
            @Override
            public ZIMKitHeaderBar getMessageListHeaderBar(ZIMKitMessageFragment fragment) {
                if (fragment != null) {
                    if (fragment.getConversationType() == ZIMConversationType.PEER) {
                        String conversationID = fragment.getConversationID();
                        String conversationName = fragment.getConversationName();
                        ZegoSendCallButton sendCallButton = new ZegoSendCallButton(ConversationActivity.this, conversationID, conversationName);
                        ZIMKitHeaderBar headerBar = new ZIMKitHeaderBar();
                        headerBar.setRightView(sendCallButton);
                        return headerBar;
                    }
                }
                return null;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZIMKit.disconnectUser();
        ZegoUIKitPrebuiltCallInvitationService.unInit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (item.getItemId() == R.id.new_chat) {
            showNewChatDialog();
            return true;
        }
        if (item.getItemId() == R.id.new_group) {
            showNewGroupDialog();
            return true;
        }
        if (item.getItemId() == R.id.join_group) {
            showJoinGroupDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showNewChatDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Chat");

        EditText editText = new EditText(this);
        editText.setHint("User ID");
        builder.setView(editText);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String userID = editText.getText().toString();
                startChat(userID, ZIMKitConversationType.ZIMKitConversationTypePeer);
            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showNewGroupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Group");

        final EditText editText1 = new EditText(this);
        final EditText editText2 = new EditText(this);
        final EditText editText3 = new EditText(this);

        editText1.setHint("Group Name");
        editText2.setHint("Group ID (Optional)");
        editText3.setHint("Invite User IDs (eg. 123;456)");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(editText1);
        layout.addView(editText2);
        layout.addView(editText3);
        builder.setView(layout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String groupName = editText1.getText().toString();
                String groupId = editText2.getText().toString();
                String[] userIDs = editText3.getText().toString().split(";");
                createGroup(groupName, groupId, new ArrayList<>(Arrays.asList(userIDs)));
            }
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showJoinGroupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Join Group");

        EditText editText = new EditText(this);
        editText.setHint("Group ID");
        builder.setView(editText);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String groupID = editText.getText().toString();
                joinGroup(groupID);
            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void startChat(String conversationID, ZIMKitConversationType type) {
        ZIMKitRouter.toMessageActivity(this, conversationID, type);
    }

    private void createGroup(String groupName, String groupID, List<String> userIDs) {
        ZIMKit.createGroup(groupName, groupID, userIDs, new CreateGroupCallback() {
            @Override
            public void onCreateGroup(ZIMKitGroupInfo groupInfo, ArrayList<ZIMErrorUserInfo> inviteUserErrors, ZIMError error) {
                if (error.code == ZIMErrorCode.SUCCESS) {
                    startChat(groupInfo.getId(), ZIMKitConversationType.ZIMKitConversationTypeGroup);
                }
            }
        });
    }

    private void joinGroup(String groupID) {
        ZIMKit.joinGroup(groupID, new JoinGroupCallback() {
            @Override
            public void onJoinGroup(ZIMKitGroupInfo groupInfo, ZIMError error) {
                if (error.code == ZIMErrorCode.SUCCESS || error.code == ZIMErrorCode.MEMBER_IS_ALREADY_IN_THE_GROUP) {
                    startChat(groupID, ZIMKitConversationType.ZIMKitConversationTypeGroup);
                }
            }
        });
    }
}