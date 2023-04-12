package com.chriswilliams.cal_hope_c868.UI.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chriswilliams.cal_hope_c868.Adapters.PendingRequestAdapter;
import com.chriswilliams.cal_hope_c868.Adapters.UpcomingShiftAdapter;
import com.chriswilliams.cal_hope_c868.DB.RequestsResponse;
import com.chriswilliams.cal_hope_c868.DB.RetrofitClient;
import com.chriswilliams.cal_hope_c868.DB.SharedPrefManager;
import com.chriswilliams.cal_hope_c868.DB.ShiftsResponse;
import com.chriswilliams.cal_hope_c868.Entities.Nurse;
import com.chriswilliams.cal_hope_c868.Entities.NurseRequest;
import com.chriswilliams.cal_hope_c868.Entities.Shift;
import com.chriswilliams.cal_hope_c868.R;
import com.chriswilliams.cal_hope_c868.UI.Activities.DetailRequestActivity;
import com.chriswilliams.cal_hope_c868.UI.Activities.DetailShiftActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    RecyclerView recyclerViewShifts;
    RecyclerView recyclerViewRequests;
    PendingRequestAdapter requestAdapter;
    UpcomingShiftAdapter shiftAdapter;
    List<NurseRequest> pendingRequestList;
    List<Shift> upcomingShiftList;


    public HomeFragment() {
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        upcomingShiftList = new ArrayList<>();
        pendingRequestList = new ArrayList<>();

        shiftAdapter = new UpcomingShiftAdapter(getActivity(), upcomingShiftList);
        recyclerViewShifts = view.findViewById(R.id.upcomingRecyclerView);
        recyclerViewShifts.setHasFixedSize(true);
        recyclerViewShifts.setAdapter(shiftAdapter);
        recyclerViewShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
        getShifts();

        requestAdapter = new PendingRequestAdapter(getActivity(), pendingRequestList);
        recyclerViewRequests = view.findViewById(R.id.pendingRecyclerView);
        recyclerViewRequests.setHasFixedSize(true);
        recyclerViewRequests.setAdapter(requestAdapter);
        recyclerViewRequests.setLayoutManager(new LinearLayoutManager(getActivity()));
        getRequests();
    }

    @Override
    public void onResume() {
        super.onResume();
        getShifts();
        getRequests();
    }

    private void getShifts() {
        Call<ShiftsResponse> call = RetrofitClient.getInstance().getApi().getUpcomingShiftsByNurse(SharedPrefManager.getInstance(getActivity()).getNurse().getId());
        call.enqueue(new Callback<ShiftsResponse>() {
            @Override
            public void onResponse(Call<ShiftsResponse> call, Response<ShiftsResponse> response) {
                upcomingShiftList = response.body().getShifts();
                shiftAdapter = new UpcomingShiftAdapter(getActivity(), upcomingShiftList);
                recyclerViewShifts.setAdapter(shiftAdapter);
                shiftAdapter.setOnItemClickListener(shift -> {

                    String[] dateParts = shift.getDate().split("-");
                    String shiftYear = dateParts[0];
                    String shiftMonth = dateParts[1];
                    String shiftDay = dateParts[2];
                    String shiftDate = (shiftMonth + "/" + shiftDay + "/" + shiftYear);

                    String [] startParts = shift.getStartTime().split(":");
                    String startHour = startParts[0];
                    String startMin = startParts[1];
                    String startTime = (startHour + ":" + startMin);

                    String [] endParts = shift.getEndTime().split(":");
                    String endHour = endParts[0];
                    String endMin = endParts[1];
                    String endTime = (endHour + ":" + endMin);

                    Intent intent = new Intent(getActivity(), DetailShiftActivity.class);
                    intent.putExtra(DetailShiftActivity.EXTRA_ID, shift.getId());
                    intent.putExtra(DetailShiftActivity.EXTRA_DATE, shiftDate);
                    intent.putExtra(DetailShiftActivity.EXTRA_START_TIME, startTime);
                    intent.putExtra(DetailShiftActivity.EXTRA_END_TIME, endTime);
                    intent.putExtra(DetailShiftActivity.EXTRA_UNIT_ID, shift.getUnitId());
                    intent.putExtra(DetailShiftActivity.EXTRA_NURSE_ID, shift.getNurseId());
                    startActivity(intent);
                });
            }

            @Override
            public void onFailure(Call<ShiftsResponse> call, Throwable t) {

            }
        });
    }
    private void getRequests() {
        Call<RequestsResponse> call = RetrofitClient.getInstance().getApi().getPendingRequestsByNurse(SharedPrefManager.getInstance(getActivity()).getNurse().getId());
        call.enqueue((new Callback<RequestsResponse>() {
            @Override
            public void onResponse(Call<RequestsResponse> call, Response<RequestsResponse> response) {
                pendingRequestList = response.body().getRequests();
                requestAdapter = new PendingRequestAdapter(getActivity(), pendingRequestList);
                recyclerViewRequests.setAdapter(requestAdapter);
                requestAdapter.setOnItemClickListener(request -> {
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