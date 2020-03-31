package com.nextinnovation.pitak.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.data.MainRepository;
import com.nextinnovation.pitak.main.MainActivity;
import com.nextinnovation.pitak.model.user.User;
import com.nextinnovation.pitak.model.user.UserSignIn;
import com.nextinnovation.pitak.model.user.UserWhenSignedIn;
import com.nextinnovation.pitak.utils.MSharedPreferences;
import com.nextinnovation.pitak.utils.MToast;
import com.nextinnovation.pitak.utils.Statics;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterClientActivity extends AppCompatActivity {

    private ImageView back;
    private Button save;
    private EditText name;
    private EditText email;
    private boolean edit;
    private View editPhoneView;
    private EditText phoneEdit;
    private Spinner codeEdit;

    private UserWhenSignedIn userToEdit;
    private String phone;

    public static void start(Context context, boolean edit) {
        context.startActivity(new Intent(context, RegisterClientActivity.class).putExtra("edit", edit));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client);

        edit = getIntent().getBooleanExtra("edit", false);
        phone = MSharedPreferences.get(RegisterClientActivity.this, "phone", "").replace("+", "");
        initView();
        listener();
    }

    private void initView() {
        phoneEdit = findViewById(R.id.register_client_phone_et);
        editPhoneView = findViewById(R.id.register_client_phone_layout);
        codeEdit = findViewById(R.id.register_client_phone_code_sp);
        back = findViewById(R.id.register_client_back_img);
        save = findViewById(R.id.register_client_save_btn);
        name = findViewById(R.id.register_client_name_et);
        email = findViewById(R.id.register_client_email_et);
    }

    private void listener() {
        if (edit) {
            userToEdit = new Gson().fromJson(MSharedPreferences.get(RegisterClientActivity.this, Statics.USER, ""), UserWhenSignedIn.class);
            setValues(userToEdit);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save.setVisibility(View.GONE);
                if (edit) {
                    final UserWhenSignedIn userWhenSignedIn = new UserWhenSignedIn(userToEdit.getId(), codeEdit.getSelectedItem().toString().replace("+", "") + Statics.getString(phoneEdit), "surname", "patronymic", Statics.getString(name), Statics.getString(email), null, null, null, codeEdit.getSelectedItem().toString().replace("+", "") + Statics.getString(phoneEdit));
                    if (userToEdit.getUsername().equals(userWhenSignedIn.getUsername())) {
                        MainRepository.getService().editClient(userWhenSignedIn, Statics.getToken(RegisterClientActivity.this)).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    MSharedPreferences.set(RegisterClientActivity.this, Statics.USER, new Gson().toJson(userWhenSignedIn));
                                    finish();
                                } else {
                                    MToast.showResponseError(RegisterClientActivity.this, response.errorBody());
                                    save.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                MToast.showInternetError(RegisterClientActivity.this);
                                save.setVisibility(View.VISIBLE);
                            }
                        });
                    } else {
                        MSharedPreferences.set(RegisterClientActivity.this, "phone", userWhenSignedIn.getUsername());
                        MSharedPreferences.set(RegisterClientActivity.this, "userToEdit", userWhenSignedIn);
                        CodeAuthenticationActivity.start(RegisterClientActivity.this, Statics.PASSENGER);
                    }
                } else {
                    User user = new User(phone, email.getText().toString().trim(), phone, "surname", name.getText().toString().trim(), "patronymic", Statics.PASSENGER, "", "", "");
                    MainRepository.getService().signUp(user).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()) {
                                MSharedPreferences.set(RegisterClientActivity.this, Statics.REGISTERED, true);
                                MainRepository.getService().signIn(new UserSignIn(phone, phone)).enqueue(new Callback<UserWhenSignedIn>() {
                                    @Override
                                    public void onResponse(Call<UserWhenSignedIn> call, Response<UserWhenSignedIn> response) {
                                        if (response.isSuccessful()) {
                                            if (response.body().getRoles()[0].equals("ROLE_DRIVER")) {
                                                MSharedPreferences.set(RegisterClientActivity.this, "who", Statics.DRIVER);
                                            }
                                            if (response.body().getRoles()[0].equals("ROLE_PASSENGER")) {
                                                MSharedPreferences.set(RegisterClientActivity.this, "who", Statics.PASSENGER);
                                            }
                                            MSharedPreferences.set(RegisterClientActivity.this, Statics.USER, new Gson().toJson(response.body()));
                                            MainActivity.start(RegisterClientActivity.this);
                                        } else {
                                            MToast.showResponseError(RegisterClientActivity.this, response.errorBody());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<UserWhenSignedIn> call, Throwable t) {
                                        MToast.showInternetError(RegisterClientActivity.this);
                                        save.setVisibility(View.VISIBLE);
                                    }
                                });
                            } else {
                                MToast.showResponseError(RegisterClientActivity.this, response.errorBody());
                            }
                            save.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            MToast.showInternetError(RegisterClientActivity.this);
                            save.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });
    }

    private void setValues(UserWhenSignedIn user) {
        String userPhone = "+" + user.getUsername();
        editPhoneView.setVisibility(View.VISIBLE);
        name.setText(user.getName());
        email.setText(user.getEmail());
        phoneEdit.setText(userPhone.replace("+7", "+996").replace("+996", ""));
    }
}
