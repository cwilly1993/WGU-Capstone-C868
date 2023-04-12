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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUsernameFragment extends Fragment {
    TextInputEditText currentUsernameInput, newUsernameInput;
    private Button updateUsernameBtn;
    public UpdateUsernameFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_username, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentUsernameInput = view.findViewById(R.id.currentUsernameEditText);
        newUsernameInput = view.findViewById(R.id.newUsernameEditText);
        updateUsernameBtn = view.findViewById(R.id.updateUsernameButton);

        updateUsernameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUsername();
            }
        });
    }

    public void updateUsername() {
        String currentUsername = currentUsernameInput.getText().toString().trim();
        String newUsername = newUsernameInput.getText().toString().trim();
        if(currentUsername.isEmpty()) {
            currentUsernameInput.setError("Field cannot be blank");
            currentUsernameInput.requestFocus();
            return;
        }
        if(newUsername.isEmpty()) {
            newUsernameInput.setError("Field cannot be blank");
            newUsernameInput.requestFocus();
            return;
        }

        if(SharedPrefManager.getInstance(getActivity()).isLoggedIn()) {
            int id = SharedPrefManager.getInstance(getActivity()).getNurse().getId();
            Call<DefaultResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .updateNurseUsername(id, currentUsername, newUsername);
            call.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                    if (response.code() == 201) {
                        DefaultResponse dr = response.body();
                        Toast.makeText(getActivity(), dr.getMessage(), Toast.LENGTH_SHORT).show();
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        fm.popBackStack();
                    } else if (response.code() == 401) {
                        Toast.makeText(getActivity(), "Incorrect username, please try again", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 411) {
                        Toast.makeText(getActivity(), "Unable to update username", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        if(SharedPrefManager.getInstance(getActivity()).isAdminLoggedIn()){
            int id = SharedPrefManager.getInstance(getActivity()).getAdmin().getId();
            Call<DefaultResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .updateAdminUsername(id, currentUsername, newUsername);
            call.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                    if (response.code() == 201) {
                        DefaultResponse dr = response.body();
                        Toast.makeText(getActivity(), dr.getMessage(), Toast.LENGTH_SHORT).show();
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        fm.popBackStack();
                    } else if (response.code() == 401) {
                        Toast.makeText(getActivity(), "Incorrect username, please try again", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 411) {
                        Toast.makeText(getActivity(), "Unable to update username", Toast.LENGTH_SHORT).show();
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