package com.chriswilliams.cal_hope_c868.UI.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chriswilliams.cal_hope_c868.DB.AdminResponse;
import com.chriswilliams.cal_hope_c868.DB.NurseResponse;
import com.chriswilliams.cal_hope_c868.DB.RetrofitClient;
import com.chriswilliams.cal_hope_c868.DB.SharedPrefManager;
import com.chriswilliams.cal_hope_c868.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAccountActivity extends AppCompatActivity {
    public static final String EXTRA_NAME = "com.chriswilliams.cal_hope_c868.UI.Activities.EXTRA_NAME";
    public static final String EXTRA_EMAIL = "com.chriswilliams.cal_hope_c868.UI.Activities.EXTRA_EMAIL";
    public static final String EXTRA_PHONE = "com.chriswilliams.cal_hope_c868.UI.Activities.EXTRA_PHONE";
    public static final String EXTRA_ADDRESS = "com.chriswilliams.cal_hope_c868.UI.Activities.EXTRA_ADDRESS";
    TextView accountName;
    EditText accountEmail, accountPhone, accountAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cancel);
        accountName = findViewById(R.id.accountNameView);
        accountEmail = findViewById(R.id.accountEmailEdit);
        accountPhone = findViewById(R.id.accountPhoneEdit);
        accountAddress = findViewById(R.id.accountAddressEdit);

        Intent intent = getIntent();
        accountName.setText(intent.getStringExtra(EXTRA_NAME));
        accountEmail.setText(intent.getStringExtra(EXTRA_EMAIL));
        accountPhone.setText(intent.getStringExtra(EXTRA_PHONE));
        accountAddress.setText(intent.getStringExtra(EXTRA_ADDRESS));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSaveAccount(View view) {
        String email = accountEmail.getText().toString().trim();
        String phone = accountPhone.getText().toString().trim();
        String address = accountAddress.getText().toString().trim();

        if(email.isEmpty()) {
            accountEmail.setError("Field cannot be blank");
            accountEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            accountEmail.setError("Please enter a valid email");
            accountEmail.requestFocus();
            return;
        }
        if(phone.isEmpty()) {
            accountPhone.setError("Field cannot be blank");
            accountPhone.requestFocus();
            return;
        }
        if(address.isEmpty()) {
            accountAddress.setError("Field cannot be blank");
            accountAddress.requestFocus();
            return;
        }



         if(SharedPrefManager.getInstance(this).isLoggedIn()) {
            int id = SharedPrefManager.getInstance(this).getNurse().getId();
            String username = SharedPrefManager.getInstance(this).getNurse().getUsername();
            String name = SharedPrefManager.getInstance(this).getNurse().getName();


            Call<NurseResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .updateNurse(id, username, email, name, phone, address);

            call.enqueue(new Callback<NurseResponse>() {
                @Override
                public void onResponse(Call<NurseResponse> call, Response<NurseResponse> response) {
                    NurseResponse nurseResponse = response.body();
                    if (!nurseResponse.isError()) {
                        SharedPrefManager.getInstance(EditAccountActivity.this)
                                .saveNurse(nurseResponse.getNurse());
                        Toast.makeText(EditAccountActivity.this, nurseResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else {
                        Toast.makeText(EditAccountActivity.this, nurseResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<NurseResponse> call, Throwable t) {

                }
            });
        }
        if(SharedPrefManager.getInstance(this).isAdminLoggedIn()) {
            int id = SharedPrefManager.getInstance(this).getAdmin().getId();
            String username = SharedPrefManager.getInstance(this).getAdmin().getUsername();
            String name = SharedPrefManager.getInstance(this).getAdmin().getName();

            Call<AdminResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .updateAdmin(id, username, email, name, phone, address);
            call.enqueue(new Callback<AdminResponse>() {
                @Override
                public void onResponse(Call<AdminResponse> call, Response<AdminResponse> response) {
                    AdminResponse adminResponse = response.body();
                    if (!adminResponse.isError()) {
                        SharedPrefManager.getInstance(EditAccountActivity.this)
                                .saveAdmin(adminResponse.getAdmin());
                        System.out.println(adminResponse.getAdmin());
                        Toast.makeText(EditAccountActivity.this, adminResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else {
                        Toast.makeText(EditAccountActivity.this, adminResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<AdminResponse> call, Throwable t) {

                }
            });
        }
    }
}