package com.nextinnovation.pitak.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nextinnovation.pitak.R;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private List<String> strings = new ArrayList<>();
    private ImageView back;
    private Button month, before, canceled;


    public static void start(Context context) {
        context.startActivity(new Intent(context, HistoryActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        strings.add("");
        strings.add("");
        strings.add("");
        strings.add("");
        strings.add("");
        strings.add("");
        strings.add("");
        strings.add("");
        initAllView();
        listener();
    }

    private void initAllView() {
        month = findViewById(R.id.settings_history_month);
        before = findViewById(R.id.settings_history_before);
        canceled = findViewById(R.id.settings_history_canceled);
        back = findViewById(R.id.settings_history_back_img);
        adapter = new HistoryAdapter();
        adapter.setList(strings, false);
        recyclerView = findViewById(R.id.history_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void listener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetButtons();
                month.setBackground(getResources().getDrawable(R.drawable.bg_round_blue_button));
                month.setTextColor(getResources().getColor(R.color.white));
            }
        });
        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetButtons();
                before.setBackground(getResources().getDrawable(R.drawable.bg_round_blue_button));
                before.setTextColor(getResources().getColor(R.color.white));
            }
        });
        canceled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetButtons();
                canceled.setBackground(getResources().getDrawable(R.drawable.bg_round_blue_button));
                canceled.setTextColor(getResources().getColor(R.color.white));
            }
        });
    }

    private void resetButtons() {
        month.setBackground(getResources().getDrawable(R.drawable.bg_dark_blue_stroke));
        before.setBackground(getResources().getDrawable(R.drawable.bg_dark_blue_stroke));
        canceled.setBackground(getResources().getDrawable(R.drawable.bg_dark_blue_stroke));
        month.setTextColor(getResources().getColor(R.color.bluish_black));
        before.setTextColor(getResources().getColor(R.color.bluish_black));
        canceled.setTextColor(getResources().getColor(R.color.bluish_black));
    }
}
