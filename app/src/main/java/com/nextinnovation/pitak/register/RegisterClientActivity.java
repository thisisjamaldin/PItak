package com.nextinnovation.pitak.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.main.MainActivity;

public class RegisterClientActivity extends AppCompatActivity {

    private ImageView back;
    private Button save;

    public static void start(Context context) {
        context.startActivity(new Intent(context, RegisterClientActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client);

        initView();
        listener();
    }

    private void initView() {
        back = findViewById(R.id.register_client_back_img);
        save = findViewById(R.id.register_client_save_btn);
    }

    private void listener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.start(RegisterClientActivity.this, false);
            }
        });
    }
}
