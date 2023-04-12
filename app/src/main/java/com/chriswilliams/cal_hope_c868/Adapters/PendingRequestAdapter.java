package com.chriswilliams.cal_hope_c868.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chriswilliams.cal_hope_c868.Entities.NurseRequest;
import com.chriswilliams.cal_hope_c868.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PendingRequestAdapter extends RecyclerView.Adapter<PendingRequestAdapter.PendingRequestViewHolder>{
    private Context mCtx;
    private List<NurseRequest> pendingRequestList = new ArrayList<>();
    private OnItemClickListener listener;

    public PendingRequestAdapter(Context mCtx, List<NurseRequest> pendingRequestList) {
        this.mCtx = mCtx;
        this.pendingRequestList = pendingRequestList;
    }

    @NonNull
    @Override
    public PendingRequestAdapter.PendingRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_pending_requests, parent, false);
        return new PendingRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingRequestAdapter.PendingRequestViewHolder holder, int position) {
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
    }

    @Override
    public int getItemCount() {
        return pendingRequestList.size();
    }

    class PendingRequestViewHolder extends RecyclerView.ViewHolder {
        TextView pendingRequestType, pendingRequestStart, pendingRequestEnd;

        public PendingRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            pendingRequestType = itemView.findViewById(R.id.pendingRequestType);
            pendingRequestStart = itemView.findViewById(R.id.pendingRequestStartDate);
            pendingRequestEnd = itemView.findViewById(R.id.pendingRequestEndDate);
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

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
