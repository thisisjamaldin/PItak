package com.nextinnovation.pitak.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.utils.MSharedPreferences;
import com.nextinnovation.pitak.utils.Statics;

public class WhoRegisterActivity extends AppCompatActivity {

    private Button client;
    private Button driver;

    public static void start(Context context) {
        context.startActivity(new Intent(context, WhoRegisterActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_register);

        initView();
        listener();
    }

    private void initView() {
        client = findViewById(R.id.who_register_client_btn);
        driver = findViewById(R.id.who_register_driver_btn);
    }

    private void listener() {
        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                client.setEnabled(false);
                if (!MSharedPreferences.get(WhoRegisterActivity.this, "who", "").equals(Statics.UNAUTHORIZED)){
                    MSharedPreferences.set(WhoRegisterActivity.this, "who", Statics.PASSENGER);
                }
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    PhoneAuthenticationActivity.start(WhoRegisterActivity.this);
                } else {
                    RegisterClientActivity.start(WhoRegisterActivity.this, false);
                }
            }
        });

        driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                driver.setEnabled(false);
                if (!MSharedPreferences.get(WhoRegisterActivity.this, "who", "").equals(Statics.UNAUTHORIZED)){
                    MSharedPreferences.set(WhoRegisterActivity.this, "who", Statics.DRIVER);
                }
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    PhoneAuthenticationActivity.start(WhoRegisterActivity.this);
                } else {
                    RegisterDriverActivity.start(WhoRegisterActivity.this, false);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        client.setEnabled(true);
        driver.setEnabled(true);
    }
}
