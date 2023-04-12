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
import com.chriswilliams.cal_hope_c868.Adapters.AdminRequestAdapter;
import com.chriswilliams.cal_hope_c868.DB.RequestsResponse;
import com.chriswilliams.cal_hope_c868.DB.RetrofitClient;
import com.chriswilliams.cal_hope_c868.Entities.NurseRequest;
import com.chriswilliams.cal_hope_c868.R;
import com.chriswilliams.cal_hope_c868.UI.Activities.DetailRequestActivity;
import java.util.ArrayList;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminRequestsFragment extends Fragment {
    RecyclerView recyclerView;
    AdminRequestAdapter adapter;
    ArrayList<NurseRequest> requestList;
    public AdminRequestsFragment() {
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
        return inflater.inflate(R.layout.fragment_admin_requests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requestList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.adminRequestsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getRequests();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setQueryHint("Search by status");
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


    private void getRequests() {
        Call<RequestsResponse> call = RetrofitClient.getInstance().getApi().getAllRequests();
        call.enqueue((new Callback<RequestsResponse>() {
            @Override
            public void onResponse(Call<RequestsResponse> call, Response<RequestsResponse> response) {
                requestList = response.body().getRequests();
                adapter = new AdminRequestAdapter(getActivity(), requestList);
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(request -> {
                    Intent intent = new Intent(getActivity(), DetailRequestActivity.class);

                    String[] startParts = request.getStartDate().split("-");
                    String startYear = startParts[0];
                    String startMonth = startParts[1];
                    String startDay = startParts[2];

                    String[] endParts = request.getEndDate().split("-");
                    String endYear = endParts[0];
                    String endMonth = endParts[1];
                    String endDay = endParts[2];

                    String startDate = (startMonth + '/' + startDay + '/' + startYear);
                    String endDate = (endMonth + '/' + endDay + '/' + endYear);

                    intent.putExtra(DetailRequestActivity.EXTRA_ID, request.getId());
                    intent.putExtra(DetailRequestActivity.EXTRA_START, startDate);
                    intent.putExtra(DetailRequestActivity.EXTRA_END, endDate);
                    intent.putExtra(DetailRequestActivity.EXTRA_TYPE, request.getType());
                    intent.putExtra(DetailRequestActivity.EXTRA_STATUS, request.getStatus());
                    intent.putExtra(DetailRequestActivity.EXTRA_NOTES, request.getNotes());
                    intent.putExtra(DetailRequestActivity.EXTRA_NURSE_ID, request.getNurseId());
                    startActivity(intent);
                });
            }

            @Override
            public void onFailure(Call<RequestsResponse> call, Throwable t) {

            }
        }));
    }
}