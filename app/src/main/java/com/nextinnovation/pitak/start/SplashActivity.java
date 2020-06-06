package com.nextinnovation.pitak.start;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.nextinnovation.pitak.data.MainRepository;
import com.nextinnovation.pitak.main.MainActivity;
import com.nextinnovation.pitak.model.user.User;
import com.nextinnovation.pitak.model.user.UserSignIn;
import com.nextinnovation.pitak.model.user.UserWhenSignedIn;
import com.nextinnovation.pitak.register.WhoRegisterActivity;
import com.nextinnovation.pitak.utils.MSharedPreferences;
import com.nextinnovation.pitak.utils.MToast;
import com.nextinnovation.pitak.utils.Statics;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserWhenSignedIn user = new Gson().fromJson(MSharedPreferences.get(this, Statics.USER, ""), UserWhenSignedIn.class);
        if (MSharedPreferences.get(this, "first", true)) {
            OnBoardActivity.start(this);
            finish();
        } else if (user==null) {
            MainActivity.start(SplashActivity.this);
        } else if (!MSharedPreferences.get(this, Statics.REGISTERED, false)) {
            startActivity(new Intent(SplashActivity.this, WhoRegisterActivity.class));
        } else {
            MainRepository.getService().signIn(new UserSignIn(user.getUsername(), user.getUsername())).enqueue(new Callback<UserWhenSignedIn>() {
                @Override
                public void onResponse(Call<UserWhenSignedIn> call, Response<UserWhenSignedIn> response) {
                    if (response.isSuccessful()) {
                        Statics.setToken(SplashActivity.this, response.body().getToken());
                        MainRepository.getService().getMe(Statics.getToken(SplashActivity.this)).enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.body().getResult().getUserType().equals("DRIVER")) {
                                    MSharedPreferences.set(SplashActivity.this, "who", Statics.DRIVER);
                                }
                                if (response.body().getResult().getUserType().equals("PASSENGER")) {
                                    MSharedPreferences.set(SplashActivity.this, "who", Statics.PASSENGER);
                                }
                                MSharedPreferences.set(SplashActivity.this, Statics.USER, new Gson().toJson(response.body().getResult()));
                                MSharedPreferences.set(SplashActivity.this, "phone", response.body().getResult().getUsername());
                                MainActivity.start(SplashActivity.this);
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                            }
                        });
                    } else {
                        MToast.showResponseError(SplashActivity.this, response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<UserWhenSignedIn> call, Throwable t) {
                    startActivity(new Intent(SplashActivity.this, NoInternetActivity.class));
                    finish();
                }
            });
        }
    }
}
