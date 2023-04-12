package com.chriswilliams.cal_hope_c868.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chriswilliams.cal_hope_c868.Entities.NurseRequest;
import com.chriswilliams.cal_hope_c868.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NurseRequestAdapter extends RecyclerView.Adapter<NurseRequestAdapter.NurseRequestViewHolder> implements Filterable {
    private Context mCtx;
    private ArrayList<NurseRequest> requestList = new ArrayList<>();
    private ArrayList<NurseRequest> filteredRequestList = new ArrayList<>();
    private OnItemClickListener listener;

    public NurseRequestAdapter(Context mCtx, ArrayList<NurseRequest> list) {
        this.mCtx = mCtx;
        requestList = list;
        filteredRequestList = list;
    }

    @NonNull
    @Override
    public NurseRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_requests, parent, false);
        return new NurseRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NurseRequestViewHolder holder, int position) {
        NurseRequest request = filteredRequestList.get(position);

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

        holder.requestType.setText(request.getType());
        holder.requestStatus.setText(request.getStatus());
        holder.requestStart.setText(startDate);
        holder.requestEnd.setText(endDate);
    }

    @Override
    public int getItemCount() {
        return filteredRequestList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    filteredRequestList = requestList;
                } else {

                    ArrayList<NurseRequest> filteredList = new ArrayList<>();

                    for (NurseRequest nurseRequest : requestList) {

                        if (nurseRequest.getType().toLowerCase().contains(charString) || nurseRequest.getStatus().toLowerCase().contains(charString)) {

                            filteredList.add(nurseRequest);
                        }
                    }

                    filteredRequestList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredRequestList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredRequestList = (ArrayList<NurseRequest>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class NurseRequestViewHolder extends RecyclerView.ViewHolder {
        TextView requestType, requestStatus, requestStart, requestEnd;

        public NurseRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            requestType = itemView.findViewById(R.id.requestType);
            requestStatus = itemView.findViewById(R.id.requestStatus);
            requestStart = itemView.findViewById(R.id.requestStartDate);
            requestEnd = itemView.findViewById(R.id.requestEndDate);
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if(listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(requestList.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(NurseRequest nurseRequest);
    }

    public void setOnItemClickListener(OnItemClickListener listener){this.listener = listener;}
}
