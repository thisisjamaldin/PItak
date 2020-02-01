package com.nextinnovation.pitak.register;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import com.nextinnovation.pitak.R;

public class CodeAuthenticationActivity extends AppCompatActivity {

    private ImageView back;
    private Button next;
    private TextView phone;
    private String phoneNumber;

    public static void start(Context context, String phoneNumber) {
        Intent intent = new Intent(context, CodeAuthenticationActivity.class);
        intent.putExtra("phone", phoneNumber);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_authentication);

        phoneNumber = getIntent().getStringExtra("phone");
        initView();
        listener();
    }

    private void initView() {
        back = findViewById(R.id.code_register_back_img);
        next = findViewById(R.id.code_register_next_btn);
        phone = findViewById(R.id.code_register_phone_tv);

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
    }
}
