package com.nextinnovation.pitak.start;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.main.MainActivity;
import com.nextinnovation.pitak.utils.MSharedPreferences;

public class OnBoardActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Button btnNext;
    private TextView btnSkip;

    public static void start(Context context) {
        context.startActivity(new Intent(context, OnBoardActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board);

        initView();
        listener();
    }

    private void listener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    btnNext.setText(getResources().getString(R.string.start));
                } else {
                    btnNext.setText(getResources().getString(R.string.next));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int nextPage = viewPager.getCurrentItem() + 1;
                if (nextPage == 3) {
                    onBoardFinish();
                } else {
                    viewPager.setCurrentItem(nextPage);
                }
            }
        });
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBoardFinish();
            }
        });

    }

    private void onBoardFinish() {
        MSharedPreferences.set(this, "first", false);
        startActivity(new Intent(this, SplashActivity.class));
        finish();
    }

    private void initView() {
        viewPager = findViewById(R.id.on_board_view_pager);
        tabLayout = findViewById(R.id.on_board_tab_indicator);
        viewPager.setAdapter(new OnBoardAdapter());
        tabLayout.setupWithViewPager(viewPager, true);
        btnNext = findViewById(R.id.on_board_btn);
        btnSkip = findViewById(R.id.on_board_skip_btn);
    }
}
