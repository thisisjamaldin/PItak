package com.nextinnovation.pitak.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nextinnovation.pitak.R;

public class WhoRegisterActivity extends AppCompatActivity {

    private TextView signIn;
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
        signIn = findViewById(R.id.who_register_sign_in);
    }

    private void listener() {
        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterClientActivity.start(WhoRegisterActivity.this);
            }
        });

        driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterDriverActivity.start(WhoRegisterActivity.this);
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthenticationActivity.start(WhoRegisterActivity.this);
            }
        });
    }
}
