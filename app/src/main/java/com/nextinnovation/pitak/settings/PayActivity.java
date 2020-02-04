package com.nextinnovation.pitak.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nextinnovation.pitak.R;

public class PayActivity extends AppCompatActivity {

    private ImageView back;

    public static void start(Context context) {
        context.startActivity(new Intent(context, PayActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        initAllView();
        listener();
    }

    private void initAllView() {
        back = findViewById(R.id.settings_pay_back_img);
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
