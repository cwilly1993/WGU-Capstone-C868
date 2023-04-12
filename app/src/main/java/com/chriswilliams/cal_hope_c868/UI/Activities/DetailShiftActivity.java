package com.chriswilliams.cal_hope_c868.UI.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.chriswilliams.cal_hope_c868.DB.RetrofitClient;
import com.chriswilliams.cal_hope_c868.DB.UnitResponse;
import com.chriswilliams.cal_hope_c868.Entities.Unit;
import com.chriswilliams.cal_hope_c868.R;
import com.chriswilliams.cal_hope_c868.UI.NotificationReceiver;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailShiftActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.chriswilliams.cal_hope_c868.UI.Activities.EXTRA_ID";
    public static final String EXTRA_DATE = "com.chriswilliams.cal_hope_c868.UI.Activities.EXTRA_DATE";
    public static final String EXTRA_START_TIME = "com.chriswilliams.cal_hope_c868.UI.Activities.EXTRA_START_TIME";
    public static final String EXTRA_END_TIME = "com.chriswilliams.cal_hope_c868.UI.Activities.EXTRA_END_TIME";
    public static final String EXTRA_UNIT_ID = "com.chriswilliams.cal_hope_c868.UI.Activities.EXTRA_UNIT_ID";
    public static final String EXTRA_NURSE_ID = "com.chriswilliams.cal_hope_c868.UI.Activities.EXTRA_NURSE_ID";
    private TextView shiftDate, shiftStart, shiftEnd, shiftUnit, shiftLocation;
    int mYear, mMonth, mDay, mHour, mMinute;
    Unit unit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_shift);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        shiftDate = findViewById(R.id.shift_date_detail);
        shiftStart = findViewById(R.id.shift_start_detail);
        shiftEnd = findViewById(R.id.shift_end_detail);
        shiftUnit = findViewById(R.id.shift_unit_detail);
        shiftLocation = findViewById(R.id.shift_location_detail);

        if (intent.getExtras() != null) {
            shiftDate.setText(intent.getStringExtra(EXTRA_DATE));
            shiftStart.setText(intent.getStringExtra(EXTRA_START_TIME));
            shiftEnd.setText(intent.getStringExtra(EXTRA_END_TIME));
            getUnit();
        }
    }

    private void getUnit() {
        Call<UnitResponse> call = RetrofitClient.getInstance().getApi().getUnitById(getIntent().getIntExtra(EXTRA_UNIT_ID, 0));
        call.enqueue(new Callback<UnitResponse>() {
            @Override
            public void onResponse(Call<UnitResponse> call, Response<UnitResponse> response) {
                unit = response.body().getUnit();
                shiftUnit.setText(unit.getUnitName());
                shiftLocation.setText(unit.getLocation());
            }

            @Override
            public void onFailure(Call<UnitResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shift_menu, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("scheduleFragment", 1);
                startActivity(intent);
                return true;
            case R.id.shift_start_alarm:
                dateTimePicker();
                return true;
            case R.id.shift_start_cancel:
                cancelAlarm();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void dateTimePicker() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mYear = year;
                mMonth = month + 1;
                mDay = dayOfMonth;
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selHour, int selMin) {
                        mHour = selHour;
                        mMinute = selMin;
                        c.set(Calendar.HOUR_OF_DAY, mHour);
                        c.set(Calendar.MINUTE, mMinute);
                        c.set(Calendar.SECOND, 0);
                        setAlarm(c);
                        Toast.makeText(getApplicationContext(), "Alarm set",Toast.LENGTH_SHORT).show();
                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(DetailShiftActivity.this, android.R.style.Theme_Holo_Light_Dialog, listener, mHour, mMinute, false);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.setTitle("Select a time");
                timePickerDialog.show();
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, listener, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
    @RequiresApi(api = Build.VERSION_CODES.S)
    private void setAlarm(Calendar c) {
        String unitName = shiftUnit.getText().toString();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent (this, NotificationReceiver.class);
        alarmIntent.putExtra("key", "Your shift in Unit: " + unitName + " starts at " + shiftStart.getText());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(DetailShiftActivity.this, 1, alarmIntent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent (this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(DetailShiftActivity.this, 1, alarmIntent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(DetailShiftActivity.this, "Alarm canceled", Toast.LENGTH_SHORT).show();
    }
}