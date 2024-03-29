package com.nextinnovation.pitak.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.nextinnovation.pitak.fragment.add.AddFragment;
import com.nextinnovation.pitak.fragment.main.MainFragment;
import com.nextinnovation.pitak.fragment.profile.ProfileFragment;
import com.nextinnovation.pitak.fragment.role.RoleFragment;
import com.nextinnovation.pitak.fragment.saved.SavedFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private boolean loggedIn;

    public ViewPagerAdapter(@NonNull FragmentManager fm, boolean loggedIn) {
        super(fm);
        this.loggedIn = loggedIn;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 4) {
            return new ProfileFragment();
        } else if (position == 3) {
            return new SavedFragment();
        } else if (position == 2) {
            return new AddFragment();
        } else if (position == 1) {
            return new RoleFragment();
        } else {
            return new MainFragment(!loggedIn);
        }
    }

    @Override
    public int getCount() {
        if (loggedIn)
            return 5;
        else
            return 1;
    }
}
