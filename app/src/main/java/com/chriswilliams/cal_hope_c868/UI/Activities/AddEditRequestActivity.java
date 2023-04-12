package com.chriswilliams.cal_hope_c868.UI.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chriswilliams.cal_hope_c868.DB.DefaultResponse;
import com.chriswilliams.cal_hope_c868.DB.RetrofitClient;
import com.chriswilliams.cal_hope_c868.DB.SharedPrefManager;
import com.chriswilliams.cal_hope_c868.R;

import java.time.LocalDate;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditRequestActivity extends AppCompatActivity  {

    public static final String EXTRA_ID = "com.chriswilliams.cal_hope_c868.UI.Activities.EXTRA_ID";
    public static final String EXTRA_START = "com.chriswilliams.cal_hope_c868.UI.Activities.EXTRA_START";
    public static final String EXTRA_END = "com.chriswilliams.cal_hope_c868.UI.Activities.EXTRA_END";
    public static final String EXTRA_TYPE = "com.chriswilliams.cal_hope_c868.UI.Activities.EXTRA_TYPE";
    public static final String EXTRA_STATUS = "com.chriswilliams.cal_hope_c868.UI.Activities.EXTRA_STATUS";
    public static final String EXTRA_NOTES = "com.chriswilliams.cal_hope_c868.UI.Activities.EXTRA_NOTES";
    private DatePickerDialog datePicker;
    private Spinner requestTypeSpinner, requestStatusSpinner;
    private EditText requestNotes;
    private TextView requestId, requestStart, requestEnd;
    private Button startBtn, endBtn;
    private boolean editRequest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_request);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cancel);

        requestId = findViewById(R.id.text_request_id);
        requestTypeSpinner = findViewById(R.id.spinner_request_type);
        requestStart = findViewById(R.id.text_request_start);
        startBtn = findViewById(R.id.request_start_button);
        requestEnd = findViewById(R.id.text_request_end);
        endBtn = findViewById(R.id.request_end_button);
        requestStatusSpinner = findViewById(R.id.spinner_request_status);
        requestNotes = findViewById(R.id.edit_request_notes);


        Intent intent = getIntent();
        if(intent.getExtras() != null) {
            editRequest = true;
            toolbar.setTitle("Edit Request");
            requestId.setText(String.valueOf(intent.getIntExtra(EXTRA_ID, -1)));
            requestStart.setText(intent.getStringExtra(EXTRA_START));
            requestEnd.setText(intent.getStringExtra(EXTRA_END));
            requestNotes.setText(intent.getStringExtra(EXTRA_NOTES));
        }

        String [] statusArray = new String[]{"Pending", "Approved", "Denied"};
        final ArrayAdapter<String> spinnerStatusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, statusArray);
        spinnerStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        requestStatusSpinner.setAdapter(spinnerStatusAdapter);

        String [] typeArray = new String []{"Parental Leave", "Personal Time", "Shift Change", "Sick Leave", "Vacation"};
        final ArrayAdapter<String> spinnerTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, typeArray);
        spinnerTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        requestTypeSpinner.setAdapter(spinnerTypeAdapter);

        if(intent.getExtras() != null) {
            int typePosition = spinnerTypeAdapter.getPosition(getIntent().getStringExtra(EXTRA_TYPE));
            int statusPosition = spinnerStatusAdapter.getPosition(getIntent().getStringExtra(EXTRA_STATUS));
            requestStatusSpinner.setSelection(statusPosition);
            requestTypeSpinner.setSelection(typePosition);
        }
        if(SharedPrefManager.getInstance(this).isAdminLoggedIn()) {
            requestTypeSpinner.setEnabled(false);
            requestNotes.setEnabled(false);
            startBtn.setVisibility(View.INVISIBLE);
            startBtn.setEnabled(false);
            endBtn.setVisibility(View.INVISIBLE);
            endBtn.setEnabled(false);
        } else if(SharedPrefManager.getInstance(this).isLoggedIn()){
            requestStatusSpinner.setEnabled(false);
        }
    }

    public void onSelectStart(View view) {
        final Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        datePicker = new DatePickerDialog(AddEditRequestActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month+1;
                String formatMonth = "" + month;
                if(month < 10){

                    formatMonth = "0" + month;
                }
                if(day < 10){

                    formatMonth  = "0" + day ;
                }
                String date = (formatMonth + "/" + day + "/" + year);
                requestStart.setText(date);
            }
        }, year, month, day);
        datePicker.show();
    }

    public void onSelectEnd(View view) {
        final Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        datePicker = new DatePickerDialog(AddEditRequestActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month+1;
                String formatMonth = "" + month;
                if(month < 10){

                    formatMonth = "0" + month;
                }
                if(day < 10){

                    formatMonth  = "0" + day ;
                }
                String date = (formatMonth + "/" + day + "/" + year);
                requestEnd.setText(date);
            }
        }, year, month, day);
        datePicker.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSaveRequest(View view) {

        String start = this.requestStart.getText().toString().trim();
        String end = this.requestEnd.getText().toString().trim();
        String type = requestTypeSpinner.getSelectedItem().toString();
        String status = requestStatusSpinner.getSelectedItem().toString();
        String notes = requestNotes.getText().toString().trim();
        int nurseId = SharedPrefManager.getInstance(this).getNurse().getId();

        if (start.isEmpty()) {
            requestStart.setError("Start Date is required");
            requestStart.requestFocus();
            return;
        }
        if (end.isEmpty()) {
            requestEnd.setError("End Date is required");
            requestEnd.requestFocus();
            return;
        }

        String[] startDateParts = start.split("/");
        String startYear = startDateParts[2];
        String startMonth = startDateParts[0];
        String startDay = startDateParts[1];

        String[] endDateParts = end.split("/");
        String endYear = endDateParts[2];
        String endMonth = endDateParts[0];
        String endDay = endDateParts[1];

        String endDate = (endYear + "-" + endMonth + "-" + endDay);
        String startDate = (startYear + "-" + startMonth + "-" + startDay);

        LocalDate dateStart = LocalDate.parse(startDate);
        LocalDate dateEnd = LocalDate.parse(endDate);
        int compareDate = dateStart.compareTo(dateEnd);
        if(compareDate > 0) {
            Toast.makeText(AddEditRequestActivity.this, "Start date must be before the end date", Toast.LENGTH_SHORT).show();
        }

        else if (!editRequest) {
            Call<DefaultResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .createNurseRequest(startDate, endDate, status, type, notes, nurseId);
            call.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                    if (response.code() == 201) {
                        DefaultResponse dr = response.body();
                        Toast.makeText(AddEditRequestActivity.this, dr.getMessage(), Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 411) {
                        DefaultResponse dr = response.body();
                        Toast.makeText(AddEditRequestActivity.this, dr.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                    Toast.makeText(AddEditRequestActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("requestsFragment", 1);
            startActivity(intent);
        }
        else if (editRequest) {
            int id = (getIntent().getIntExtra(EXTRA_ID, -1));
            Call<DefaultResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .updateNurseRequest(id, startDate, endDate, status, type, notes);
            call.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                    Toast.makeText(AddEditRequestActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                    Toast.makeText(AddEditRequestActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            Intent intent = new Intent(this, DetailRequestActivity.class);
            intent.putExtra(DetailRequestActivity.EXTRA_ID, id);
            intent.putExtra(DetailRequestActivity.EXTRA_START, start);
            intent.putExtra(DetailRequestActivity.EXTRA_END, end);
            intent.putExtra(DetailRequestActivity.EXTRA_TYPE, type);
            intent.putExtra(DetailRequestActivity.EXTRA_STATUS, status);
            intent.putExtra(DetailRequestActivity.EXTRA_NOTES, notes);
            startActivity(intent);
        }
    }



    public void onCancel(View view) {
        finish();
    }
}