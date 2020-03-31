package com.nextinnovation.pitak.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.register.RegisterClientActivity;
import com.nextinnovation.pitak.register.RegisterDriverActivity;
import com.nextinnovation.pitak.register.WhoRegisterActivity;
import com.nextinnovation.pitak.utils.MSharedPreferences;
import com.nextinnovation.pitak.utils.Statics;

public class NotificationActivity extends AppCompatActivity {

    private TextView editProfile;
    private TextView signOut;
    private ImageView back;

    public static void start(Context context) {
        context.startActivity(new Intent(context, NotificationActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        initAllView();
        listener();
    }

    private void initAllView() {
        editProfile = findViewById(R.id.settings_notification_profile_edit);
        back = findViewById(R.id.settings_notification_back_img);
        signOut = findViewById(R.id.settings_notification_sign_out);
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
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(NotificationActivity.this);
                alert.setTitle(getResources().getString(R.string.sign_out));
                alert.setMessage(getResources().getString(R.string.are_you_sure_to_sign_out));
                alert.setNeutralButton(getResources().getString(R.string.cancel), null);
                alert.setPositiveButton(getResources().getString(R.string.sign_out), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(NotificationActivity.this, WhoRegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        MSharedPreferences.clear(NotificationActivity.this);
                        MSharedPreferences.set(NotificationActivity.this, "first", false);
                    }
                });
                alert.show();
            }
        });
    }
}