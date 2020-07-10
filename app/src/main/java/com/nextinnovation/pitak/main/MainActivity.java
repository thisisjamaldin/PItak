package com.nextinnovation.pitak.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.fragment.main.MainFragment;
import com.nextinnovation.pitak.fragment.saved.SavedFragment;
import com.nextinnovation.pitak.register.RegisterActivity;
import com.nextinnovation.pitak.utils.MSharedPreferences;
import com.nextinnovation.pitak.utils.Statics;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private Button add;
    private boolean loggedIn;

    public static void start(Context context) {
        context.startActivity(new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("-------token", Statics.getToken(this));
        loggedIn = Statics.getToken(this).length() >= 10;
        FirebaseMessaging.getInstance().subscribeToTopic(MSharedPreferences.get(MainActivity.this, "who", ""));
        initView();
        listener();
    }

    private void initView() {
        viewPager = findViewById(R.id.main_view_pager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), loggedIn));
        viewPager.setOffscreenPageLimit(5);
        bottomNavigationView = findViewById(R.id.main_bottom_navigation);
        if (MSharedPreferences.get(MainActivity.this, "who", "").equals(Statics.DRIVER)) {
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
                } else if (!loggedIn) {
                    startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                } else if (menuItem.getItemId() == R.id.menu_role) {
                    viewPager.setCurrentItem(1, false);
                } else if (menuItem.getItemId() == R.id.menu_saved) {
                    viewPager.setCurrentItem(3, false);
                    if (loggedIn)
                        SavedFragment.getData(MainActivity.this);
                } else if (menuItem.getItemId() == R.id.menu_profile) {
                    viewPager.setCurrentItem(4, false);
                }
                return loggedIn;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                hideKeyboard(MainActivity.this, viewPager);
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
                        new SavedFragment();
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
                if (loggedIn) {
                    viewPager.setCurrentItem(2, false);
                } else {
                    startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() != 0) {
            viewPager.setCurrentItem(0, false);
        } else {
            super.onBackPressed();
        }
    }

    public void openMain() {
        viewPager.setCurrentItem(0, false);
    }

    public static void hideKeyboard(Activity activity, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}
