package com.chriswilliams.cal_hope_c868.UI.Activities;

import static android.content.Intent.EXTRA_TEXT;
import static android.content.Intent.EXTRA_TITLE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import com.chriswilliams.cal_hope_c868.DB.SharedPrefManager;
import com.chriswilliams.cal_hope_c868.R;

public class SettingsActivity extends AppCompatActivity implements PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_CalHope_ActionBar);
        setContentView(R.layout.settings_activity);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings_container, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Settings");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(SharedPrefManager.getInstance(this).isLoggedIn()) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    finish();
                    return true;
            }
        }
        else if(SharedPrefManager.getInstance(this).isAdminLoggedIn()) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    finish();
                    return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {
        final Bundle args = pref.getExtras();
        final Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate(
                getClassLoader(),
                pref.getFragment());
        fragment.setArguments(args);
        fragment.setTargetFragment(caller, 0);
        // Replace the existing Fragment with the new Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.settings_container, fragment)
                .addToBackStack(null)
                .commit();
        return true;
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
        private static final String SHARED_PREF = "SHARED_PREF";
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }

        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("notifications")) {
                boolean notifications = sharedPreferences.getBoolean("notifications", true);
                if(notifications) {
                    Toast.makeText(getActivity(), "Notifications are enabled", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), "Notifications are disabled", Toast.LENGTH_SHORT).show();
                }
                SharedPrefManager.getInstance(getActivity()).setNotifications(notifications);
            }
            if (key.equals("notification_sound")) {
                boolean notificationSound = sharedPreferences.getBoolean("notification_sound", true);

                if(notificationSound) {
                    Toast.makeText(getActivity(), "Notification sound is on", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), "Notification sound is off", Toast.LENGTH_SHORT).show();
                }
                SharedPrefManager.getInstance(getActivity()).setSound(notificationSound);
            }
            if (key.equals("notification_vibrate")) {
                boolean notificationVibrate = sharedPreferences.getBoolean("notification_vibrate", true);
                if(notificationVibrate) {
                    Toast.makeText(getActivity(), "Notification vibrate is on", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), "Notification vibrate is off", Toast.LENGTH_SHORT).show();
                }
                SharedPrefManager.getInstance(getActivity()).setVibrate(notificationVibrate);
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }
    }
}
