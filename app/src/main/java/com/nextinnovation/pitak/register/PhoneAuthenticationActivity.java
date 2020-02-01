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
import android.widget.Toast;

import com.nextinnovation.pitak.R;

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
                if (code.getSelectedItem().equals("+996") && phone.getText().length()==9){
                    CodeAuthenticationActivity.start(PhoneAuthenticationActivity.this, code.getSelectedItem() + phone.getText().toString());
                } else if (code.getSelectedItem().equals("+7") && phone.getText().length()==10){
                    CodeAuthenticationActivity.start(PhoneAuthenticationActivity.this, code.getSelectedItem() + phone.getText().toString());
                } else {
                    Toast.makeText(PhoneAuthenticationActivity.this, "Wrong number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
