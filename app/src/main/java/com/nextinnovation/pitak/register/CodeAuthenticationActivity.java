package com.nextinnovation.pitak.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;
import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.data.MainRepository;
import com.nextinnovation.pitak.main.MainActivity;
import com.nextinnovation.pitak.model.car.NewCarResponse;
import com.nextinnovation.pitak.model.user.EditUser;
import com.nextinnovation.pitak.model.user.User;
import com.nextinnovation.pitak.model.user.UserSignIn;
import com.nextinnovation.pitak.model.user.UserWhenSignedIn;
import com.nextinnovation.pitak.utils.MSharedPreferences;
import com.nextinnovation.pitak.utils.MToast;
import com.nextinnovation.pitak.utils.Statics;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CodeAuthenticationActivity extends AppCompatActivity {

    private ImageView back;
    private Button next;
    private TextView phone;
    private String phoneNumber;
    private EditText smsCode;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mVerificationId;
    private FirebaseAuth mAuth;
    private String edit;

    public static void start(Context context, String edit) {
        context.startActivity(new Intent(context, CodeAuthenticationActivity.class).putExtra("edit", edit));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_authentication);

        edit = getIntent().getStringExtra("edit");
        phoneNumber = MSharedPreferences.get(CodeAuthenticationActivity.this, "phone", "");
        if (!phoneNumber.startsWith("+")) {
            phoneNumber = "+" + phoneNumber;
        }
        mAuth = FirebaseAuth.getInstance();

        initView();
        listener();
        setSmsCallBack();
        startVerification(phoneNumber);

    }

    private void setSmsCallBack() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                signInWithPhoneAuthCredential(credential);
                if (mResendToken != null) {
                    smsCode.setText(credential.getSmsCode());
                }
                next.setVisibility(View.GONE);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    MToast.show(CodeAuthenticationActivity.this, getResources().getString(R.string.invalid_phone_number));
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    MToast.show(CodeAuthenticationActivity.this, getResources().getString(R.string.quota_exceeded));
                }
                finish();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                mResendToken = token;
                next.setVisibility(View.VISIBLE);
            }
        };
    }

    private void initView() {
        back = findViewById(R.id.code_register_back_img);
        next = findViewById(R.id.code_register_next_btn);
        phone = findViewById(R.id.code_register_phone_tv);
        smsCode = findViewById(R.id.code_register_code_et);

        setView();
    }

    private void setView() {
        phone.setText(HtmlCompat.fromHtml(getResources().getString(R.string.we_send_code_to) + phoneNumber + getResources().getString(R.string.enter_received_code), HtmlCompat.FROM_HTML_MODE_LEGACY));
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
                if (smsCode.getText().toString().length() == 6) {
                    next.setVisibility(View.GONE);
                    verifyPhoneNumberWithCode(mVerificationId, smsCode.getText().toString().trim());
                } else {
                    smsCode.setError(getResources().getString(R.string.enter_received_code));
                }
            }
        });
    }

    private void startVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (MSharedPreferences.get(CodeAuthenticationActivity.this, Statics.REGISTERED, false)) {
                                if (edit != null && edit.equals(Statics.DRIVER)) {
                                    UserWhenSignedIn user = new Gson().fromJson(MSharedPreferences.get(CodeAuthenticationActivity.this, "userToEdit", ""), UserWhenSignedIn.class);
                                    MainRepository.getService().editDriver(user, Statics.getToken(CodeAuthenticationActivity.this)).enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {
                                        }
                                    });
                                }
                                if (edit != null && edit.equals(Statics.PASSENGER)) {
                                    EditUser user = new Gson().fromJson(MSharedPreferences.get(CodeAuthenticationActivity.this, "userToEdit", ""), EditUser.class);
                                    MainRepository.getService().editClient(user, Statics.getToken(CodeAuthenticationActivity.this)).enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {
                                        }
                                    });
                                }
                                MainActivity.start(CodeAuthenticationActivity.this);
                                finish();
                            } else if (MSharedPreferences.get(CodeAuthenticationActivity.this, "sign", "").equals(Statics.SIGN_IN)) {
                                    next.setVisibility(View.GONE);
                                    final UserSignIn signIn = new UserSignIn(phoneNumber.replace("+", ""), phoneNumber.replace("+", ""), MSharedPreferences.get(CodeAuthenticationActivity.this, "who", ""));
                                    MainRepository.getService().signIn(signIn).enqueue(new Callback<UserWhenSignedIn>() {
                                        @Override
                                        public void onResponse(Call<UserWhenSignedIn> call, Response<UserWhenSignedIn> response) {
                                            if (response.isSuccessful()) {
                                                Statics.setToken(CodeAuthenticationActivity.this, response.body().getToken());
                                                MainRepository.getService().getMe(Statics.getToken(CodeAuthenticationActivity.this)).enqueue(new Callback<User>() {
                                                    @Override
                                                    public void onResponse(Call<User> call, Response<User> response) {
                                                        MSharedPreferences.set(CodeAuthenticationActivity.this, "phone", phoneNumber);
                                                        MSharedPreferences.set(CodeAuthenticationActivity.this, Statics.USER, new Gson().toJson(response.body().getResult()));
                                                        if (response.body().getResult().getUserType().equals("DRIVER")) {
                                                            MainRepository.getService().getMyCars(Statics.getToken(CodeAuthenticationActivity.this)).enqueue(new Callback<NewCarResponse>() {
                                                                @Override
                                                                public void onResponse(Call<NewCarResponse> call, Response<NewCarResponse> response) {
                                                                    UserWhenSignedIn uws = new Gson().fromJson(MSharedPreferences.get(CodeAuthenticationActivity.this, Statics.USER, ""), UserWhenSignedIn.class);
                                                                    if (response.body().getResult().isEmpty()) return;
                                                                    uws.setCarCommonModel(response.body().getResult().get(0).getCarCommonModel());
                                                                    MSharedPreferences.set(CodeAuthenticationActivity.this, Statics.USER, new Gson().toJson(uws));
                                                                }

                                                                @Override
                                                                public void onFailure(Call<NewCarResponse> call, Throwable t) {

                                                                }
                                                            });
                                                        }
                                                        MainActivity.start(CodeAuthenticationActivity.this);
                                                    }

                                                    @Override
                                                    public void onFailure(Call<User> call, Throwable t) {

                                                    }
                                                });
                                            } else {
                                                MToast.showResponseError(CodeAuthenticationActivity.this, response.errorBody());
                                                Log.e("---------signIn", signIn.toString());
                                                next.setVisibility(View.VISIBLE);
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<UserWhenSignedIn> call, Throwable t) {
                                            MToast.showInternetError(CodeAuthenticationActivity.this);
                                        }
                                    });
                            } else if (MSharedPreferences.get(CodeAuthenticationActivity.this, "sign", "").equals(Statics.SIGN_UP)){
                                switch (MSharedPreferences.get(CodeAuthenticationActivity.this, "who", "")) {
                                    case "PASSENGER":
                                        RegisterClientActivity.start(CodeAuthenticationActivity.this, false);
                                        break;
                                    case "DRIVER":
                                        RegisterDriverActivity.start(CodeAuthenticationActivity.this, false);
                                        break;
                                }
                            }
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                MToast.show(CodeAuthenticationActivity.this, getResources().getString(R.string.wrong_number));
                                next.setVisibility(View.VISIBLE);
                                return;
                            }
                            Toast.makeText(CodeAuthenticationActivity.this, getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
                        }
                        finish();
                    }
                });
    }

}
