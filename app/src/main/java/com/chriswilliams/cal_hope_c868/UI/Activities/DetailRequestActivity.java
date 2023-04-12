package com.chriswilliams.cal_hope_c868.UI.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import static android.content.Intent.EXTRA_TEXT;
import static android.content.Intent.EXTRA_TITLE;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chriswilliams.cal_hope_c868.DB.DefaultResponse;
import com.chriswilliams.cal_hope_c868.DB.RetrofitClient;
import com.chriswilliams.cal_hope_c868.DB.SharedPrefManager;
import com.chriswilliams.cal_hope_c868.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailRequestActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.chriswilliams.cal_hope_c868.UI.Activities.EXTRA_ID";
    public static final String EXTRA_START = "com.chriswilliams.cal_hope_c868.UI.Activities.EXTRA_START";
    public static final String EXTRA_END = "com.chriswilliams.cal_hope_c868.UI.Activities.EXTRA_END";
    public static final String EXTRA_TYPE = "com.chriswilliams.cal_hope_c868.UI.Activities.EXTRA_TYPE";
    public static final String EXTRA_STATUS = "com.chriswilliams.cal_hope_c868.UI.Activities.EXTRA_STATUS";
    public static final String EXTRA_NOTES = "com.chriswilliams.cal_hope_c868.UI.Activities.EXTRA_NOTES";
    public static final String EXTRA_NURSE_ID = "com.chriswilliams.cal_hope_c868.UI.Activities.EXTRA_NURSE_ID";
    private TextView requestId, requestStart, requestEnd, requestType, requestStatus, requestNotes;
    private Button deleteButton, editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_request);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();

        requestId = findViewById(R.id.request_id_detail);
        requestStart = findViewById(R.id.request_start_detail);
        requestEnd = findViewById(R.id.request_end_detail);
        requestType = findViewById(R.id.request_type_detail);
        requestStatus = findViewById(R.id.request_status_detail);
        requestNotes = findViewById(R.id.request_notes_detail);
        editButton = findViewById(R.id.edit_request_button);
        deleteButton = findViewById(R.id.delete_request_button);

        if (intent.getExtras() != null) {
            String idString = String.valueOf(intent.getIntExtra(EXTRA_ID, -1));
            requestId.setText(idString);
            requestType.setText(intent.getStringExtra(EXTRA_TYPE));
            requestStart.setText(intent.getStringExtra(EXTRA_START));
            requestEnd.setText(intent.getStringExtra(EXTRA_END));
            requestStatus.setText(intent.getStringExtra(EXTRA_STATUS));
            requestNotes.setText(intent.getStringExtra(EXTRA_NOTES));

            if(SharedPrefManager.getInstance(this).isAdminLoggedIn()) {
                deleteButton.setVisibility(View.INVISIBLE);
                deleteButton.setClickable(false);
            }
            else if(SharedPrefManager.getInstance(this).isLoggedIn()) {
                if((intent.getStringExtra(EXTRA_STATUS).equals("Approved")) || (intent.getStringExtra(EXTRA_STATUS).equals("Denied")))  {
                    editButton.setAlpha(.5f);
                    editButton.setClickable(false);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.requests_menu, menu);
        onPrepareOptionsMenu(menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem share = menu.findItem(R.id.request_share);
        share.setVisible(!SharedPrefManager.getInstance(this).isAdminLoggedIn());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(SharedPrefManager.getInstance(this).isLoggedIn()) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("requestsFragment", 1);
                    startActivity(intent);
                    return true;
                case R.id.request_share:
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(EXTRA_TITLE, "Request for " + SharedPrefManager.getInstance(this).getNurse().getName());
                    sendIntent.putExtra(EXTRA_TEXT, "Request for " + SharedPrefManager.getInstance(this).getNurse().getName() + "\n"
                            + "Request ID: " + requestId.getText().toString() + "\n"
                            + "Request Type: " + requestType.getText().toString() + "\n"
                            + "Request Start: " + requestStart.getText().toString() + "\n"
                            + "Request End: " + requestEnd.getText().toString() + "\n"
                            + "Notes: " + requestNotes.getText().toString());
                    sendIntent.setType("text/plain");
                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    startActivity(shareIntent);
                    return true;
            }
        }else if(SharedPrefManager.getInstance(this).isAdminLoggedIn()) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    Intent intent = new Intent(this, AdminMainActivity.class);
                    intent.putExtra("adminRequestsFragment", 1);
                    startActivity(intent);
                    return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void onEdit(View view) {
        Intent intent = new Intent(this,AddEditRequestActivity.class);
        intent.putExtra(AddEditRequestActivity.EXTRA_ID, getIntent().getIntExtra(EXTRA_ID, 0));
        intent.putExtra(AddEditRequestActivity.EXTRA_TYPE, getIntent().getStringExtra(EXTRA_TYPE));
        intent.putExtra(AddEditRequestActivity.EXTRA_START, getIntent().getStringExtra(EXTRA_START));
        intent.putExtra(AddEditRequestActivity.EXTRA_END, getIntent().getStringExtra(EXTRA_END));
        intent.putExtra(AddEditRequestActivity.EXTRA_STATUS, getIntent().getStringExtra(EXTRA_STATUS));
        intent.putExtra(AddEditRequestActivity.EXTRA_NOTES, getIntent().getStringExtra(EXTRA_NOTES));
        startActivity(intent);
    }

    public void onDelete(View view) {
        int id = Integer.parseInt(requestId.getText().toString().trim());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to delete this request?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().deleteRequest(id);
                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        if(!response.body().isError()) {
                            Intent intent = new Intent(DetailRequestActivity.this, MainActivity.class);
                            intent.putExtra("requestsFragment", 1);
                            startActivity(intent);
                        }
                        Toast.makeText(DetailRequestActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {

                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}