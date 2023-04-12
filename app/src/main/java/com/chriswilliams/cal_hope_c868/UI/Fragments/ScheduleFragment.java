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

import com.chriswilliams.cal_hope_c868.Adapters.ShiftAdapter;
import com.chriswilliams.cal_hope_c868.Adapters.UpcomingShiftAdapter;
import com.chriswilliams.cal_hope_c868.DB.RetrofitClient;
import com.chriswilliams.cal_hope_c868.DB.SharedPrefManager;
import com.chriswilliams.cal_hope_c868.DB.ShiftsResponse;
import com.chriswilliams.cal_hope_c868.Entities.Shift;
import com.chriswilliams.cal_hope_c868.R;
import com.chriswilliams.cal_hope_c868.UI.Activities.DetailShiftActivity;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleFragment extends Fragment {
    RecyclerView recyclerView;
    ShiftAdapter adapter;
    List<Shift> shiftList;

    public ScheduleFragment() {
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
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        shiftList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.shiftsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ShiftAdapter(getActivity(), shiftList);
        recyclerView.setAdapter(adapter);
        getShifts();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private void getShifts() {
        Call<ShiftsResponse> call = RetrofitClient.getInstance().getApi().getShiftsByNurse(SharedPrefManager.getInstance(getActivity()).getNurse().getId());
        call.enqueue(new Callback<ShiftsResponse>() {
            @Override
            public void onResponse(Call<ShiftsResponse> call, Response<ShiftsResponse> response) {
                shiftList = response.body().getShifts();
                adapter = new ShiftAdapter(getActivity(), shiftList);
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(shift -> {

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
}