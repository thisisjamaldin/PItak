package com.nextinnovation.pitak.start;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.nextinnovation.pitak.data.MainRepository;
import com.nextinnovation.pitak.main.MainActivity;
import com.nextinnovation.pitak.model.user.UserSignIn;
import com.nextinnovation.pitak.model.user.UserWhenSignedIn;
import com.nextinnovation.pitak.register.RegisterClientActivity;
import com.nextinnovation.pitak.register.RegisterDriverActivity;
import com.nextinnovation.pitak.register.WhoRegisterActivity;
import com.nextinnovation.pitak.utils.MSharedPreferences;
import com.nextinnovation.pitak.utils.Statics;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MSharedPreferences.get(this, "first", true)) {
            OnBoardActivity.start(this);
            finish();
        } else if (FirebaseAuth.getInstance().getCurrentUser() != null && !MSharedPreferences.get(this, Statics.REGISTERED, false)) {
            switch (MSharedPreferences.get(SplashActivity.this, "who", "")) {
                case "PASSENGER":
                    WhoRegisterActivity.start(this);
                    RegisterClientActivity.start(SplashActivity.this, false);
                    break;
                case "DRIVER":
                    WhoRegisterActivity.start(this);
                    RegisterDriverActivity.start(SplashActivity.this, false);
                    break;
            }
        } else if (!MSharedPreferences.get(this, Statics.REGISTERED, false)) {
            WhoRegisterActivity.start(this);
        } else {
            UserWhenSignedIn user = new Gson().fromJson(MSharedPreferences.get(this, Statics.USER, ""), UserWhenSignedIn.class);
            MainRepository.getService().signIn(new UserSignIn(user.getUsername(), user.getUsername())).enqueue(new Callback<UserWhenSignedIn>() {
                @Override
                public void onResponse(Call<UserWhenSignedIn> call, Response<UserWhenSignedIn> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getRoles()[0].equals("ROLE_DRIVER")) {
                            MSharedPreferences.set(SplashActivity.this, "who", Statics.DRIVER);
                        }
                        if (response.body().getRoles()[0].equals("ROLE_PASSENGER")) {
                            MSharedPreferences.set(SplashActivity.this, "who", Statics.PASSENGER);
                        }
                        MSharedPreferences.set(SplashActivity.this, Statics.USER, new Gson().toJson(response.body()));
                        MSharedPreferences.set(SplashActivity.this, "phone", response.body().getUsername());
                        MainActivity.start(SplashActivity.this);
                    } else {
                        try {
                            Log.e("----------splash", response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserWhenSignedIn> call, Throwable t) {
                    Log.e("---------splashFail", t.getMessage() + call.request());
                    startActivity(new Intent(SplashActivity.this, NoInternetActivity.class));
                    finish();
                }
            });
        }
    }
}
