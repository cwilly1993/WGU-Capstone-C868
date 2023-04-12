package com.chriswilliams.cal_hope_c868.DB;

import android.content.Context;
import android.content.SharedPreferences;

import com.chriswilliams.cal_hope_c868.Entities.Admin;
import com.chriswilliams.cal_hope_c868.Entities.Nurse;

public class SharedPrefManager {
    private static final String SHARED_PREF = "SHARED_PREF";
    private static SharedPrefManager mInstance;
    private Context mCtx;

    private SharedPrefManager(Context mCtx) {
        this.mCtx = mCtx;
    }

    public static synchronized SharedPrefManager getInstance(Context mCtx) {
        if(mInstance == null) {
            mInstance = new SharedPrefManager(mCtx);
        }
        return mInstance;
    }

    public void saveNurse(Nurse nurse) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("id", nurse.getId());
        editor.putString("username", nurse.getUsername());
        editor.putString("password", nurse.getPassword());
        editor.putString("email", nurse.getEmail());
        editor.putString("name", nurse.getName());
        editor.putString("phone", nurse.getPhone());
        editor.putString("address", nurse.getAddress());

        editor.apply();
    }

    public void saveAdmin(Admin admin) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("admin_id", admin.getId());
        editor.putString("username", admin.getUsername());
        editor.putString("password", admin.getPassword());
        editor.putString("email", admin.getEmail());
        editor.putString("name", admin.getName());
        editor.putString("phone", admin.getPhone());
        editor.putString("address", admin.getAddress());

        editor.apply();
    }

    public void setNotifications(boolean notifications) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("notifications", notifications);
        editor.apply();
    }

    public void setSound(boolean sound) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("notification_sound", sound);
        editor.apply();
    }

    public void setVibrate(boolean vibrate) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("notification_vibrate", vibrate);
        editor.apply();
    }

    public boolean isNotificationOn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("notification_sound", true);
    }

    public boolean isSoundOn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("notification_sound", true);
    }

    public boolean isVibrateOn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("notification_vibrate", true);
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("id", -1) != -1;
    }

    public boolean isAdminLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("admin_id", -1) != -1;
    }

    public Nurse getNurse() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return new Nurse(
                sharedPreferences.getInt("id", -1),
                sharedPreferences.getString("username", null),
                sharedPreferences.getString("password", null),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("name", null),
                sharedPreferences.getString("phone", null),
                sharedPreferences.getString("address", null)
        );
    }

    public Admin getAdmin() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return new Admin(
                sharedPreferences.getInt("admin_id", -1),
                sharedPreferences.getString("username", null),
                sharedPreferences.getString("password", null),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("name", null),
                sharedPreferences.getString("phone", null),
                sharedPreferences.getString("address", null)
        );
    }

    public void clear() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
