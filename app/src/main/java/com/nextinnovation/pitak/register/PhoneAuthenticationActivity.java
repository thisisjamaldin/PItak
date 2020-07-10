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
                final String fullPhone = code.getSelectedItem() + phone.getText().toString();
                MSharedPreferences.set(PhoneAuthenticationActivity.this, "phone", fullPhone);
                CodeAuthenticationActivity.start(PhoneAuthenticationActivity.this, null);
            }
        });
    }
}
