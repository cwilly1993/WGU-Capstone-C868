package com.chriswilliams.cal_hope_c868.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.chriswilliams.cal_hope_c868.UI.Fragments.LoginAdminFragment;
import com.chriswilliams.cal_hope_c868.UI.Fragments.LoginNurseFragment;

public class LoginAdapter extends FragmentStateAdapter {
    public LoginAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if (position == 1) {
            return new LoginAdminFragment();
        }
        else {
            return new LoginNurseFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
