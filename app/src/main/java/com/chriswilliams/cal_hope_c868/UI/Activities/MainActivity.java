package com.chriswilliams.cal_hope_c868.UI.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.chriswilliams.cal_hope_c868.DB.SharedPrefManager;
import com.chriswilliams.cal_hope_c868.R;
import com.chriswilliams.cal_hope_c868.UI.Fragments.HomeFragment;
import com.chriswilliams.cal_hope_c868.UI.Fragments.RequestsFragment;
import com.chriswilliams.cal_hope_c868.UI.Fragments.ScheduleFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    NavigationView navView;
    TextView nameTextView, emailTextView, userTextView;
    FragmentTransaction fragmentTransaction;
    HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Cal_Hope_C868);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer);
        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        View headerView = navView.getHeaderView(0);
        nameTextView = headerView.findViewById(R.id.name_textView);
        emailTextView = headerView.findViewById(R.id.email_textView);
        userTextView = headerView.findViewById(R.id.user_textView);
        nameTextView.setText(SharedPrefManager.getInstance(this).getNurse().getName());
        emailTextView.setText(SharedPrefManager.getInstance(this).getNurse().getEmail());
        userTextView.setText(R.string.nurse);



        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            homeFragment = new HomeFragment();
            fragmentTransaction = getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new HomeFragment());
            fragmentTransaction.commit();
        }
        if (extras != null) {
            if (intent.getIntExtra("scheduleFragment", 0) == 1) {
                loadFragment(new ScheduleFragment());
                toolbar.setTitle("Schedule");
            }
            if (intent.getIntExtra("requestsFragment", 0) == 1) {
                loadFragment(new RequestsFragment());
                toolbar.setTitle("Requests");
            }

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if(!SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        closeDrawer();
        if (item.getItemId() == R.id.home_menu) {
            loadFragment(new HomeFragment());
            toolbar.setTitle("Home");
        }
        if (item.getItemId() == R.id.schedule_menu) {
            loadFragment(new ScheduleFragment());
            toolbar.setTitle("Schedule");
        }
        if (item.getItemId() == R.id.requests_menu) {
            loadFragment(new RequestsFragment());
            toolbar.setTitle("Requests");
        }
        if (item.getItemId() == R.id.settings_menu) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.logout_menu) {
            SharedPrefManager.getInstance(this).clear();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return true;
    }

    public void loadFragment(Fragment fragment) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }


    private void closeDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }

    public void addRequest(View view) {
        Intent intent = new Intent(this, AddEditRequestActivity.class);
        startActivity(intent);
    }
}