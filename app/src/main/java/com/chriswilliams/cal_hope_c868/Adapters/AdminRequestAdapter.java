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

import com.chriswilliams.cal_hope_c868.DB.NurseResponse;
import com.chriswilliams.cal_hope_c868.DB.RetrofitClient;
import com.chriswilliams.cal_hope_c868.Entities.Nurse;
import com.chriswilliams.cal_hope_c868.Entities.NurseRequest;
import com.chriswilliams.cal_hope_c868.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminRequestAdapter extends RecyclerView.Adapter<AdminRequestAdapter.AdminRequestViewHolder> implements Filterable{
    private Context mCtx;
    private ArrayList<NurseRequest> requestList = new ArrayList<>();
    private ArrayList<NurseRequest> filteredRequestList = new ArrayList<>();
    private OnItemClickListener listener;


    public AdminRequestAdapter(Context mCtx, ArrayList<NurseRequest> list) {
        this.mCtx = mCtx;
        requestList = list;
        filteredRequestList = list;
    }

    @NonNull
    @Override
    public AdminRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_admin_requests, parent, false);
        return new AdminRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminRequestViewHolder holder, int position) {
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

        Call<NurseResponse> call = RetrofitClient.getInstance().getApi().getNurseById(request.getNurseId());
        call.enqueue(new Callback<NurseResponse>() {
            @Override
            public void onResponse(Call<NurseResponse> call, Response<NurseResponse> response) {
                Nurse nurse = response.body().getNurse();
                holder.requestName.setText(nurse.getName());

            }

            @Override
            public void onFailure(Call<NurseResponse> call, Throwable t) {

            }
        });

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

                        if (nurseRequest.getStatus().toLowerCase().contains(charString)) {

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

    class AdminRequestViewHolder extends RecyclerView.ViewHolder {
        TextView requestName, requestType, requestStatus, requestStart, requestEnd;

        public AdminRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            requestName = itemView.findViewById(R.id.adminRequestName);
            requestType = itemView.findViewById(R.id.adminRequestType);
            requestStatus = itemView.findViewById(R.id.adminRequestStatus);
            requestStart = itemView.findViewById(R.id.adminRequestStartDate);
            requestEnd = itemView.findViewById(R.id.adminRequestEndDate);
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

    public void setOnItemClickListener(AdminRequestAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
