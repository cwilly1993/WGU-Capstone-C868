package com.chriswilliams.cal_hope_c868.UI;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.chriswilliams.cal_hope_c868.DB.SharedPrefManager;
import com.chriswilliams.cal_hope_c868.R;
import com.chriswilliams.cal_hope_c868.UI.Activities.MainActivity;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String  channel_id = "calHopeChannel";
    static int notificationId;
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, intent.getStringExtra("key"), Toast.LENGTH_LONG).show();
        createNotificationChannel(context, channel_id);
        Notification n = new NotificationCompat.Builder(context, channel_id)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentText(intent.getStringExtra("key"))
                .setContentTitle("CalHope Shift Reminder")
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId++, n);
    }


    private void createNotificationChannel (Context context, String CHANNEL_ID) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "CalHope Channel";
            String description = "CalHope Reminder";
            int importance;
            if (!SharedPrefManager.getInstance(context).isNotificationOn()) {
                importance = NotificationManager.IMPORTANCE_NONE;
            } else {
                importance = NotificationManager.IMPORTANCE_HIGH;
            }
            if (!SharedPrefManager.getInstance(context).isSoundOn()) {
                importance = NotificationManager.IMPORTANCE_LOW;

            }
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            if (!SharedPrefManager.getInstance(context).isVibrateOn()) {
                channel.setVibrationPattern(new long[]{ 0 });
                channel.enableVibration(true);
            }
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
