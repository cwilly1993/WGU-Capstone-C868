package com.chriswilliams.cal_hope_c868.UI.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.chriswilliams.cal_hope_c868.R;

import java.util.List;

public class DetailNurseActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.chriswilliams.cal_hope_c868.UI.Activities.EXTRA_ID";
    public static final String EXTRA_NAME = "com.chriswilliams.cal_hope_c868.UI.Activities.EXTRA_NAME";
    public static final String EXTRA_EMAIL = "com.chriswilliams.cal_hope_c868.UI.Activities.EXTRA_EMAIL";
    public static final String EXTRA_PHONE = "com.chriswilliams.cal_hope_c868.UI.Activities.EXTRA_PHONE";
    public static final String EXTRA_ADDRESS = "com.chriswilliams.cal_hope_c868.UI.Activities.EXTRA_ADDRESS";
    private TextView nurseNameText, nurseIdText, nurseEmailText, nursePhoneText, nurseAddressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_nurse);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        nurseNameText = findViewById(R.id.detailNurseName);
        nurseIdText = findViewById(R.id.detailNurseId);
        nurseEmailText = findViewById(R.id.detailNurseEmail);
        nursePhoneText = findViewById(R.id.detailNursePhone);
        nurseAddressText = findViewById(R.id.detailNurseAddress);

        if(intent.getExtras() != null) {
            String idString = String.valueOf(intent.getIntExtra(EXTRA_ID, -1));
            nurseNameText.setText(intent.getStringExtra(EXTRA_NAME));
            nurseIdText.setText(idString);
            nurseEmailText.setText(intent.getStringExtra(EXTRA_EMAIL));
            nursePhoneText.setText(intent.getStringExtra(EXTRA_PHONE));
            nurseAddressText.setText(intent.getStringExtra(EXTRA_ADDRESS));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_nurse_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String phoneNumber = nursePhoneText.getText().toString().replace("-", "");
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, AdminMainActivity.class);
                intent.putExtra("adminNursesFragment", 1);
                startActivity(intent);
                return true;
            case R.id.nurse_message:
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                Uri smsUri=  Uri.parse("sms:" + phoneNumber);
                sendIntent.putExtra("sms_body", "");
                sendIntent.setType("vnd.android-dir/mms-sms");
                sendIntent.setData(smsUri);
                startActivity(sendIntent);
                return true;
            case R.id.nurse_call:
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(callIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}