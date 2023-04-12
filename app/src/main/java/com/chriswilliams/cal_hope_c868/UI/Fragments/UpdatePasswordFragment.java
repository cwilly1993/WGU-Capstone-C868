package com.chriswilliams.cal_hope_c868.UI.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.chriswilliams.cal_hope_c868.DB.DefaultResponse;
import com.chriswilliams.cal_hope_c868.DB.RetrofitClient;
import com.chriswilliams.cal_hope_c868.DB.SharedPrefManager;
import com.chriswilliams.cal_hope_c868.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePasswordFragment extends Fragment {
    TextInputEditText currentPasswordInput, newPasswordInput;
    private Button updatePasswordBtn;

    public UpdatePasswordFragment() {
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
        return inflater.inflate(R.layout.fragment_update_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentPasswordInput = view.findViewById(R.id.currentPasswordEditText);
        newPasswordInput = view.findViewById(R.id.newPasswordEditText);
        updatePasswordBtn = view.findViewById(R.id.updatePasswordButton);

        updatePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePassword();
            }
        });
    }

    public void updatePassword() {
        String currentPassword = currentPasswordInput.getText().toString().trim();
        String newPassword = newPasswordInput.getText().toString().trim();
        if(currentPassword.isEmpty()) {
            currentPasswordInput.setError("Field cannot be blank");
            currentPasswordInput.requestFocus();
            return;
        }
        if(newPassword.isEmpty()) {
            newPasswordInput.setError("Field cannot be blank");
            newPasswordInput.requestFocus();
            return;
        }
        if(newPassword.length() < 6) {
            newPasswordInput.setError("Password must be greater than 6 characters");
            newPasswordInput.requestFocus();
            return;
        }
        if(SharedPrefManager.getInstance(getActivity()).isLoggedIn()) {
            String username = SharedPrefManager.getInstance(getActivity()).getNurse().getUsername();
            Call<DefaultResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .updateNursePassword(currentPassword, newPassword, username);
            call.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                    if (response.code() == 201) {
                        DefaultResponse dr = response.body();
                        Toast.makeText(getActivity(), dr.getMessage(), Toast.LENGTH_SHORT).show();
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        fm.popBackStack();
                    } else if (response.code() == 401) {
                        Toast.makeText(getActivity(), "Incorrect password, please try again", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 411) {
                        Toast.makeText(getActivity(), "Unable to update password", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        if(SharedPrefManager.getInstance(getActivity()).isAdminLoggedIn()) {
            String username = SharedPrefManager.getInstance(getActivity()).getAdmin().getUsername();
            Call<DefaultResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .updateAdminPassword(currentPassword, newPassword, username);
            call.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                    if (response.code() == 201) {
                        DefaultResponse dr = response.body();
                        Toast.makeText(getActivity(), dr.getMessage(), Toast.LENGTH_SHORT).show();
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        fm.popBackStack();
                    } else if (response.code() == 401) {
                        Toast.makeText(getActivity(), "Incorrect password, please try again", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 411) {
                        Toast.makeText(getActivity(), "Unable to update password", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }
}