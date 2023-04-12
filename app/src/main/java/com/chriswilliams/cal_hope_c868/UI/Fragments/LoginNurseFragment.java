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

import com.chriswilliams.cal_hope_c868.DB.NurseResponse;
import com.chriswilliams.cal_hope_c868.DB.RetrofitClient;
import com.chriswilliams.cal_hope_c868.DB.SharedPrefManager;
import com.chriswilliams.cal_hope_c868.R;
import com.chriswilliams.cal_hope_c868.UI.Activities.MainActivity;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginNurseFragment extends Fragment {
    TextInputEditText nurseUsernameInput, nursePasswordInput;
    Button loginBtn;
    ProgressBar progressBar;

    public LoginNurseFragment() {
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
        return inflater.inflate(R.layout.fragment_nurse_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nurseUsernameInput = view.findViewById(R.id.nurseUsernameEditText);
        nursePasswordInput = view.findViewById(R.id.nursePasswordEditText);
        loginBtn = view.findViewById(R.id.nurseLoginButton);
        progressBar = view.findViewById(R.id.nurseProgressLogin);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nurseLogin();
            }
        });
    }

    public void nurseLogin() {
        String username = nurseUsernameInput.getText().toString().trim();
        String password = nursePasswordInput.getText().toString().trim();
        if(username.isEmpty()) {
            nurseUsernameInput.setError("Username is required");
            nurseUsernameInput.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            nursePasswordInput.setError("Password is required");
            nursePasswordInput.requestFocus();
            return;
        }
        if(password.length() < 6) {
            nursePasswordInput.setError("Password must be greater than 6 characters");
            nursePasswordInput.requestFocus();
            return;
        }

        Call<NurseResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .nurseLogin(username, password);

        call.enqueue(new Callback<NurseResponse>() {
            @Override
            public void onResponse(Call<NurseResponse> call, Response<NurseResponse> response) {
                NurseResponse loginResponse = response.body();
                if (!loginResponse.isError()) {
                    SharedPrefManager.getInstance(getActivity())
                            .saveNurse(loginResponse.getNurse());

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NurseResponse> call, Throwable t) {

            }
        });
    }

}