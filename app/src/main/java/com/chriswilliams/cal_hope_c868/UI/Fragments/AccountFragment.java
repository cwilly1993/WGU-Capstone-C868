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
import android.widget.TextView;

import com.chriswilliams.cal_hope_c868.DB.SharedPrefManager;
import com.chriswilliams.cal_hope_c868.R;
import com.chriswilliams.cal_hope_c868.UI.Activities.EditAccountActivity;

public class AccountFragment extends Fragment {
    private TextView idTextView, emailTextView, nameTextView, phoneTextView, addressTextView;
    private Button editAccountBtn;

    public AccountFragment() {
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
        return inflater.inflate(R.layout.fragment_account, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        idTextView = view.findViewById(R.id.user_id);
        emailTextView = view.findViewById(R.id.user_email);
        nameTextView = view.findViewById(R.id.user_name);
        phoneTextView = view.findViewById(R.id.user_phone);
        addressTextView = view.findViewById(R.id.user_address);
        if(SharedPrefManager.getInstance(getActivity()).isLoggedIn()) {
            idTextView.setText(String.valueOf(SharedPrefManager.getInstance(getActivity()).getNurse().getId()));
            emailTextView.setText(SharedPrefManager.getInstance(getActivity()).getNurse().getEmail());
            nameTextView.setText(SharedPrefManager.getInstance(getActivity()).getNurse().getName());
            phoneTextView.setText(SharedPrefManager.getInstance(getActivity()).getNurse().getPhone());
            addressTextView.setText(SharedPrefManager.getInstance(getActivity()).getNurse().getAddress());
        } else if (SharedPrefManager.getInstance(getActivity()).isAdminLoggedIn()) {
            idTextView.setText(String.valueOf(SharedPrefManager.getInstance(getActivity()).getAdmin().getId()));
            emailTextView.setText(SharedPrefManager.getInstance(getActivity()).getAdmin().getEmail());
            nameTextView.setText(SharedPrefManager.getInstance(getActivity()).getAdmin().getName());
            phoneTextView.setText(SharedPrefManager.getInstance(getActivity()).getAdmin().getPhone());
            addressTextView.setText(SharedPrefManager.getInstance(getActivity()).getAdmin().getAddress());
        }

        editAccountBtn = view.findViewById(R.id.edit_account_button);

        editAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditAccountActivity.class);
                intent.putExtra(EditAccountActivity.EXTRA_NAME, nameTextView.getText());
                intent.putExtra(EditAccountActivity.EXTRA_EMAIL, emailTextView.getText());
                intent.putExtra(EditAccountActivity.EXTRA_PHONE, phoneTextView.getText());
                intent.putExtra(EditAccountActivity.EXTRA_ADDRESS, addressTextView.getText());
                startActivity(intent);
            }
        });
    }

    public void onStart(){
        super.onStart();
        if(SharedPrefManager.getInstance(getActivity()).isLoggedIn()) {
            emailTextView.setText(SharedPrefManager.getInstance(getActivity()).getNurse().getEmail());
            phoneTextView.setText(SharedPrefManager.getInstance(getActivity()).getNurse().getPhone());
            addressTextView.setText(SharedPrefManager.getInstance(getActivity()).getNurse().getAddress());
        } else if(SharedPrefManager.getInstance(getActivity()).isAdminLoggedIn()) {
            emailTextView.setText(SharedPrefManager.getInstance(getActivity()).getAdmin().getEmail());
            phoneTextView.setText(SharedPrefManager.getInstance(getActivity()).getAdmin().getPhone());
            addressTextView.setText(SharedPrefManager.getInstance(getActivity()).getAdmin().getAddress());
        }
    }
}