package com.nextinnovation.pitak.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nextinnovation.pitak.R;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private Button add;
    private boolean driver;

    public static void start(Context context, boolean driver) {
        context.startActivity(new Intent(context, MainActivity.class).putExtra("driver", driver));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        driver = getIntent().getBooleanExtra("driver", false);
        initView();
        listener();
    }

    private void initView() {
        viewPager = findViewById(R.id.main_view_pager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), driver));
        viewPager.setOffscreenPageLimit(5);
        bottomNavigationView = findViewById(R.id.main_bottom_navigation);
        if (driver) {
            bottomNavigationView.getMenu().getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_profile));
            bottomNavigationView.getMenu().getItem(1).setTitle(getResources().getString(R.string.clients));
        } else {
            bottomNavigationView.getMenu().getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_vehicle));
            bottomNavigationView.getMenu().getItem(1).setTitle(getResources().getString(R.string.drivers));
        }
        add = findViewById(R.id.main_add);
    }

    private void listener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_main) {
                    viewPager.setCurrentItem(0, false);
                } else if (menuItem.getItemId() == R.id.menu_role) {
                    viewPager.setCurrentItem(1, false);
                } else if (menuItem.getItemId() == R.id.menu_saved) {
                    viewPager.setCurrentItem(3, false);
                } else if (menuItem.getItemId() == R.id.menu_profile) {
                    viewPager.setCurrentItem(4, false);
                }
                return true;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    bottomNavigationView.setSelectedItemId(R.id.menu_main);
                    add.setBackgroundResource(R.drawable.bg_floating_button);
                } else if (position == 1) {
                    bottomNavigationView.setSelectedItemId(R.id.menu_role);
                    add.setBackgroundResource(R.drawable.bg_floating_button);
                } else if (position == 2) {
                    bottomNavigationView.setSelectedItemId(R.id.menu_add);
                    add.setBackgroundResource(R.drawable.bg_floating_button_clicked);
                } else if (position == 3) {
                    bottomNavigationView.setSelectedItemId(R.id.menu_saved);
                    add.setBackgroundResource(R.drawable.bg_floating_button);
                } else if (position == 4) {
                    bottomNavigationView.setSelectedItemId(R.id.menu_profile);
                    add.setBackgroundResource(R.drawable.bg_floating_button);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(2, false);
            }
        });
    }
}
