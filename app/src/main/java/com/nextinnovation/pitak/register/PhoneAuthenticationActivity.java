package com.nextinnovation.pitak.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.data.MainRepository;
import com.nextinnovation.pitak.model.car.NewCarResponse;
import com.nextinnovation.pitak.model.user.User;
import com.nextinnovation.pitak.model.user.UserSignIn;
import com.nextinnovation.pitak.model.user.UserWhenSignedIn;
import com.nextinnovation.pitak.utils.MSharedPreferences;
import com.nextinnovation.pitak.utils.MToast;
import com.nextinnovation.pitak.utils.Statics;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhoneAuthenticationActivity extends AppCompatActivity {

    private Button next;
    private ImageView back;
    private EditText phone;
    private Spinner code;

    public static void start(Context context) {
        context.startActivity(new Intent(context, PhoneAuthenticationActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_authentication);

        initView();
        listener();
    }

    private void initView() {
        back = findViewById(R.id.phone_register_back_img);
        next = findViewById(R.id.phone_register_next_btn);
        phone = findViewById(R.id.phone_register_phone_et);
        code = findViewById(R.id.phone_register_code_sp);
    }

    private void listener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phone.getText().length() > 8) {
                    final String fullPhone = code.getSelectedItem() + phone.getText().toString();
                    if (MSharedPreferences.get(PhoneAuthenticationActivity.this, "who", "").equals(Statics.UNAUTHORIZED)) {
                        next.setVisibility(View.GONE);
                        UserSignIn signIn = new UserSignIn(fullPhone.replace("+", ""), fullPhone.replace("+", ""));
                        MainRepository.getService().signIn(signIn).enqueue(new Callback<UserWhenSignedIn>() {
                            @Override
                            public void onResponse(Call<UserWhenSignedIn> call, Response<UserWhenSignedIn> response) {
                                if (response.isSuccessful()) {
                                    Statics.setToken(PhoneAuthenticationActivity.this, response.body().getToken());
                                    MainRepository.getService().getMe(Statics.getToken(PhoneAuthenticationActivity.this)).enqueue(new Callback<User>() {
                                        @Override
                                        public void onResponse(Call<User> call, Response<User> response) {
                                            MSharedPreferences.set(PhoneAuthenticationActivity.this, "phone", fullPhone);
                                            MSharedPreferences.set(PhoneAuthenticationActivity.this, Statics.USER, new Gson().toJson(response.body().getResult()));
                                            MSharedPreferences.set(PhoneAuthenticationActivity.this, Statics.REGISTERED, true);
                                            if (response.body().getResult().getUserType().equals("PASSENGER")) {
                                                MSharedPreferences.set(PhoneAuthenticationActivity.this, "who", Statics.PASSENGER);
                                            }
                                            if (response.body().getResult().getUserType().equals("DRIVER")) {
                                                MSharedPreferences.set(PhoneAuthenticationActivity.this, "who", Statics.DRIVER);
                                                MainRepository.getService().getMyCars(Statics.getToken(PhoneAuthenticationActivity.this)).enqueue(new Callback<NewCarResponse>() {
                                                    @Override
                                                    public void onResponse(Call<NewCarResponse> call, Response<NewCarResponse> response) {
                                                        UserWhenSignedIn uws = new Gson().fromJson(MSharedPreferences.get(PhoneAuthenticationActivity.this, Statics.USER, ""), UserWhenSignedIn.class);
                                                        if (response.body().getResult().isEmpty()) return;
                                                        uws.setCarCommonModel(response.body().getResult().get(0));
                                                        MSharedPreferences.set(PhoneAuthenticationActivity.this, Statics.USER, new Gson().toJson(uws));
                                                    }

                                                    @Override
                                                    public void onFailure(Call<NewCarResponse> call, Throwable t) {

                                                    }
                                                });
                                            }
                                            CodeAuthenticationActivity.start(PhoneAuthenticationActivity.this, null);
                                        }

                                        @Override
                                        public void onFailure(Call<User> call, Throwable t) {

                                        }
                                    });
                                } else {
                                    MToast.show(PhoneAuthenticationActivity.this, getResources().getString(R.string.user_not_found));
                                    next.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onFailure(Call<UserWhenSignedIn> call, Throwable t) {
                                MToast.showInternetError(PhoneAuthenticationActivity.this);
                            }
                        });
                    } else {
                        MSharedPreferences.set(PhoneAuthenticationActivity.this, "phone", fullPhone);
                        CodeAuthenticationActivity.start(PhoneAuthenticationActivity.this, null);
                    }
                } else {
                    MToast.show(PhoneAuthenticationActivity.this, getResources().getString(R.string.wrong_number));
                }
            }
        });
    }
}
