package com.nextinnovation.pitak.start;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nextinnovation.pitak.main.MainActivity;
import com.nextinnovation.pitak.register.WhoRegisterActivity;
import com.nextinnovation.pitak.utils.MSharedPreferences;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MSharedPreferences.get(this, "first", true)){
            OnBoardActivity.start(this);
        } else if (MSharedPreferences.get(this, "register", true)){
            WhoRegisterActivity.start(this);
        } else {
            MainActivity.start(this, false);
        }
        finish();
    }
}
