package com.chriswilliams.cal_hope_c868.UI.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chriswilliams.cal_hope_c868.DB.AdminResponse;
import com.chriswilliams.cal_hope_c868.DB.RetrofitClient;
import com.chriswilliams.cal_hope_c868.DB.SharedPrefManager;
import com.chriswilliams.cal_hope_c868.R;
import com.chriswilliams.cal_hope_c868.UI.Activities.AdminMainActivity;
import com.chriswilliams.cal_hope_c868.UI.Activities.MainActivity;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginAdminFragment extends Fragment {
    TextInputEditText adminUsernameInput, adminPasswordInput;
    Button loginBtn;
    ProgressBar progressBar;

    public LoginAdminFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adminUsernameInput = view.findViewById(R.id.adminUsernameEditText);
        adminPasswordInput = view.findViewById(R.id.adminPasswordEditText);
        loginBtn = view.findViewById(R.id.adminButtonLogin);
        progressBar = view.findViewById(R.id.adminProgressLogin);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminLogin();
            }
        });
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        if(SharedPrefManager.getInstance(getActivity()).isAdminLoggedIn()) {
//            Intent intent = new Intent(getActivity(), AdminMainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//        }
//    }

    public void adminLogin() {
        String username = adminUsernameInput.getText().toString().trim();
        String password = adminPasswordInput.getText().toString().trim();
        if (username.isEmpty()) {
            adminUsernameInput.setError("Username is required");
            adminUsernameInput.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            adminPasswordInput.setError("Password is required");
            adminPasswordInput.requestFocus();
            return;
        }
        if (password.length() < 6) {
            adminPasswordInput.setError("Password must be greater than 6 characters");
            adminPasswordInput.requestFocus();
            return;
        }

        Call<AdminResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .adminLogin(username, password);
        call.enqueue(new Callback<AdminResponse>() {
            @Override
            public void onResponse(Call<AdminResponse> call, Response<AdminResponse> response) {
                AdminResponse loginResponse = response.body();
                if(!loginResponse.isError()) {
                    SharedPrefManager.getInstance(getActivity())
                            .saveAdmin(loginResponse.getAdmin());
                    Intent intent = new Intent(getActivity(), AdminMainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AdminResponse> call, Throwable t) {

            }
        });
    }
}