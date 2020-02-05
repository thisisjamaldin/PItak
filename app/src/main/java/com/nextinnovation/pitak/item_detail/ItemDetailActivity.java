package com.nextinnovation.pitak.item_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nextinnovation.pitak.R;

public class ItemDetailActivity extends AppCompatActivity {

    private ImageView back;

    public static void start(Context context) {
        context.startActivity(new Intent(context, ItemDetailActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        initView();
        listener();
    }

    private void initView() {
        back = findViewById(R.id.item_detail_back_img);
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
