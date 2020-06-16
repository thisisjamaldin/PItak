package com.nextinnovation.pitak.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.utils.MSharedPreferences;
import com.nextinnovation.pitak.utils.Statics;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button signIn = findViewById(R.id.register_sign_in);
        Button register = findViewById(R.id.register_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MSharedPreferences.set(RegisterActivity.this, "who", Statics.DRIVER);
                startActivity(new Intent(RegisterActivity.this, WhoRegisterActivity.class));
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MSharedPreferences.set(RegisterActivity.this, "who", Statics.UNAUTHORIZED);
                startActivity(new Intent(RegisterActivity.this, WhoRegisterActivity.class));
            }
        });
    }
}