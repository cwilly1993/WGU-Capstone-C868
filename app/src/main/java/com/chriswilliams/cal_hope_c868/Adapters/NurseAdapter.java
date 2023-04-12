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

import com.chriswilliams.cal_hope_c868.Entities.Nurse;
import com.chriswilliams.cal_hope_c868.R;

import java.util.ArrayList;

public class NurseAdapter extends RecyclerView.Adapter<NurseAdapter.NurseViewHolder> implements Filterable {
    private Context mCtx;
    private ArrayList<Nurse> nurseList;
    private ArrayList<Nurse> mFilteredList;
    private OnItemClickListener listener;

    public NurseAdapter(Context mCtx, ArrayList<Nurse> list) {
        this.mCtx = mCtx;
        nurseList = list;
        mFilteredList = list;
    }

    @NonNull
    @Override
    public NurseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_nurses, parent, false);
        return new NurseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NurseViewHolder holder, int position) {
        Nurse nurse = mFilteredList.get(position);
        holder.nurseName.setText(nurse.getName());
        holder.nurseEmail.setText(nurse.getEmail());
        holder.nurseId.setText(String.valueOf(nurse.getId()));
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = nurseList;
                } else {

                    ArrayList<Nurse> filteredList = new ArrayList<>();

                    for (Nurse nurse : nurseList) {

                        if (nurse.getName().toLowerCase().contains(charString) || nurse.getEmail().toLowerCase().contains(charString) || String.valueOf(nurse.getId()).toLowerCase().contains(charString)) {

                            filteredList.add(nurse);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Nurse>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class NurseViewHolder extends RecyclerView.ViewHolder {
        TextView nurseName, nurseEmail, nurseId;

        public NurseViewHolder(@NonNull View itemView) {
            super(itemView);
            nurseName = itemView.findViewById(R.id.nurseName);
            nurseEmail = itemView.findViewById(R.id.nurseEmail);
            nurseId = itemView.findViewById(R.id.nurseId);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if(listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(nurseList.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Nurse nurse);
    }

    public void setOnItemClickListener(NurseAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
