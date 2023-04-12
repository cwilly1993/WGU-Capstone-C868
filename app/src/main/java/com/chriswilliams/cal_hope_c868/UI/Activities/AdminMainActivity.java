package com.chriswilliams.cal_hope_c868.UI.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.chriswilliams.cal_hope_c868.DB.SharedPrefManager;
import com.chriswilliams.cal_hope_c868.R;
import com.chriswilliams.cal_hope_c868.UI.Fragments.AdminHomeFragment;
import com.chriswilliams.cal_hope_c868.UI.Fragments.AdminNursesFragment;
import com.chriswilliams.cal_hope_c868.UI.Fragments.AdminRequestsFragment;
import com.google.android.material.navigation.NavigationView;

public class AdminMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    NavigationView navView;
    TextView nameTextView, emailTextView, userTextView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    AdminHomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Cal_Hope_C868);
        setContentView(R.layout.activity_admin_main);

        Intent intent = getIntent();
        toolbar = findViewById(R.id.admin_toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.admin_drawer);
        navView = findViewById(R.id.admin_nav_view);
        navView.setNavigationItemSelectedListener(this);

        View headerView = navView.getHeaderView(0);
        nameTextView = headerView.findViewById(R.id.name_textView);
        emailTextView = headerView.findViewById(R.id.email_textView);
        userTextView = headerView.findViewById(R.id.user_textView);
        nameTextView.setText(SharedPrefManager.getInstance(this).getAdmin().getName());
        emailTextView.setText(SharedPrefManager.getInstance(this).getAdmin().getEmail());
        userTextView.setText(R.string.admin);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            homeFragment = new AdminHomeFragment();
            fragmentTransaction = getSupportFragmentManager().beginTransaction()
                    .add(R.id.admin_fragment_container, new AdminHomeFragment());
            fragmentTransaction.commit();
        }
        if (intent.getIntExtra("adminNursesFragment", 0) == 1) {
            loadFragment(new AdminNursesFragment());
            toolbar.setTitle("Nurses");
        }
        if (extras != null) {
            if (intent.getIntExtra("adminRequestsFragment", 0) == 1) {
                loadFragment(new AdminRequestsFragment());
                toolbar.setTitle("Requests");
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!SharedPrefManager.getInstance(this).isAdminLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        closeDrawer();
        if (item.getItemId() == R.id.admin_home_menu) {
            loadFragment(new AdminHomeFragment());
            toolbar.setTitle("Home");
        }
        if (item.getItemId() == R.id.admin_nurses_menu) {
            loadFragment(new AdminNursesFragment());
            toolbar.setTitle("Nurses");
        }
        if (item.getItemId() == R.id.admin_requests_menu) {
            loadFragment(new AdminRequestsFragment());
            toolbar.setTitle("Requests");
        }
        if (item.getItemId() == R.id.admin_settings_menu) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.admin_logout_menu) {
            SharedPrefManager.getInstance(this).clear();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return true;
    }

    public void loadFragment(Fragment fragment) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.admin_fragment_container, fragment);
        fragmentTransaction.commit();
    }


    private void closeDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }
}