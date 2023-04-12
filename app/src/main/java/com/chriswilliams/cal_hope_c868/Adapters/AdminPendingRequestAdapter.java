package com.chriswilliams.cal_hope_c868.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chriswilliams.cal_hope_c868.DB.NurseResponse;
import com.chriswilliams.cal_hope_c868.DB.RetrofitClient;
import com.chriswilliams.cal_hope_c868.Entities.Nurse;
import com.chriswilliams.cal_hope_c868.Entities.NurseRequest;
import com.chriswilliams.cal_hope_c868.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminPendingRequestAdapter extends RecyclerView.Adapter<AdminPendingRequestAdapter.AdminPendingRequestViewHolder>{
    private Context mCtx;
    private List<NurseRequest> pendingRequestList = new ArrayList<>();
    private OnItemClickListener listener;

    public AdminPendingRequestAdapter(Context mCtx, List<NurseRequest> pendingRequestList) {
        this.mCtx = mCtx;
        this.pendingRequestList = pendingRequestList;
    }

    @NonNull
    @Override
    public AdminPendingRequestAdapter.AdminPendingRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_admin_pending_requests, parent, false);
        return new AdminPendingRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminPendingRequestAdapter.AdminPendingRequestViewHolder holder, int position) {
        NurseRequest request = pendingRequestList.get(position);

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

        holder.pendingRequestType.setText(request.getType());
        holder.pendingRequestStart.setText(startDate);
        holder.pendingRequestEnd.setText(endDate);

        Call<NurseResponse> call = RetrofitClient.getInstance().getApi().getNurseById(request.getNurseId());
        call.enqueue(new Callback<NurseResponse>() {
            @Override
            public void onResponse(Call<NurseResponse> call, Response<NurseResponse> response) {
                Nurse nurse = response.body().getNurse();
                holder.pendingRequestName.setText(nurse.getName());
            }

            @Override
            public void onFailure(Call<NurseResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return pendingRequestList.size();
    }

    class AdminPendingRequestViewHolder extends RecyclerView.ViewHolder {
        TextView pendingRequestName, pendingRequestType, pendingRequestStart, pendingRequestEnd;

        public AdminPendingRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            pendingRequestName = itemView.findViewById(R.id.adminPendingRequestName);
            pendingRequestType = itemView.findViewById(R.id.adminPendingRequestType);
            pendingRequestStart = itemView.findViewById(R.id.adminPendingRequestStartDate);
            pendingRequestEnd = itemView.findViewById(R.id.adminPendingRequestEndDate);
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if(listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(pendingRequestList.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(NurseRequest pendingNurseRequest);
    }

    public void setOnItemClickListener(AdminPendingRequestAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
