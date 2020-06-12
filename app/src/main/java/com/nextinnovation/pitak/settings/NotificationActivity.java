package com.nextinnovation.pitak.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.main.MainActivity;
import com.nextinnovation.pitak.register.RegisterActivity;
import com.nextinnovation.pitak.register.RegisterClientActivity;
import com.nextinnovation.pitak.register.RegisterDriverActivity;
import com.nextinnovation.pitak.register.WhoRegisterActivity;
import com.nextinnovation.pitak.utils.MSharedPreferences;
import com.nextinnovation.pitak.utils.Statics;

public class NotificationActivity extends AppCompatActivity {

    private TextView editProfile;
    private ImageView back;
    private Switch notifyNewPost;

    public static void start(Context context) {
        context.startActivity(new Intent(context, NotificationActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        findView();
        listener();
        initView();
    }

    private void findView() {
        notifyNewPost = findViewById(R.id.settings_notification_new_notification_switch);
        editProfile = findViewById(R.id.settings_notification_profile_edit);
        back = findViewById(R.id.settings_notification_back_img);
    }

    private void listener() {
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MSharedPreferences.get(NotificationActivity.this, "who", "").equals(Statics.PASSENGER)) {
                    RegisterClientActivity.start(NotificationActivity.this, true);
                } else if (MSharedPreferences.get(NotificationActivity.this, "who", "").equals(Statics.DRIVER)) {
                    RegisterDriverActivity.start(NotificationActivity.this, true);
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        notifyNewPost.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MSharedPreferences.set(NotificationActivity.this, "newPostNotification", isChecked);
                if (isChecked) {
                    FirebaseMessaging.getInstance().subscribeToTopic(MSharedPreferences.get(NotificationActivity.this, "who", ""));
                } else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(MSharedPreferences.get(NotificationActivity.this, "who", ""));
                }
            }
        });
    }

    private void initView() {
        notifyNewPost.setChecked(MSharedPreferences.get(NotificationActivity.this, "newPostNotification", true));
    }
}