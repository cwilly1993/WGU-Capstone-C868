package com.chriswilliams.cal_hope_c868.UI.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.chriswilliams.cal_hope_c868.Adapters.NurseAdapter;
import com.chriswilliams.cal_hope_c868.DB.AllNursesResponse;
import com.chriswilliams.cal_hope_c868.DB.RetrofitClient;
import com.chriswilliams.cal_hope_c868.Entities.Nurse;
import com.chriswilliams.cal_hope_c868.R;
import com.chriswilliams.cal_hope_c868.UI.Activities.DetailNurseActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminNursesFragment extends Fragment {
    RecyclerView recyclerView;
    NurseAdapter adapter;
    ArrayList<Nurse> nurseList;


    public AdminNursesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_nurses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nurseList = new ArrayList<>();


        recyclerView = view.findViewById(R.id.nursesRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getNurses();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText.toLowerCase());
                return true;
            }
        });
    }

    private void getNurses() {
        Call<AllNursesResponse> call = RetrofitClient.getInstance().getApi().getAllNurses();
        call.enqueue((new Callback<AllNursesResponse>() {
            @Override
            public void onResponse(Call<AllNursesResponse> call, Response<AllNursesResponse> response) {
                nurseList = response.body().getAllNurses();
                adapter = new NurseAdapter(getActivity(), nurseList);
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(nurse -> {
                    Intent intent = new Intent(getActivity(), DetailNurseActivity.class);
                    intent.putExtra(DetailNurseActivity.EXTRA_ID, nurse.getId());
                    intent.putExtra(DetailNurseActivity.EXTRA_NAME, nurse.getName());
                    intent.putExtra(DetailNurseActivity.EXTRA_EMAIL, nurse.getEmail());
                    intent.putExtra(DetailNurseActivity.EXTRA_PHONE, nurse.getPhone());
                    intent.putExtra(DetailNurseActivity.EXTRA_ADDRESS, nurse.getAddress());
                    startActivity(intent);
                });
            }

            @Override
            public void onFailure(Call<AllNursesResponse> call, Throwable t) {

            }
        }));

    }
}