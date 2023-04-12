package com.chriswilliams.cal_hope_c868.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chriswilliams.cal_hope_c868.DB.RetrofitClient;
import com.chriswilliams.cal_hope_c868.DB.UnitResponse;
import com.chriswilliams.cal_hope_c868.Entities.Shift;
import com.chriswilliams.cal_hope_c868.Entities.Unit;
import com.chriswilliams.cal_hope_c868.R;

import java.time.format.DateTimeFormatter;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.ShiftViewHolder>{
    private Context mCtx;
    private List<Shift> shiftList;
    private OnItemClickListener listener;

    public ShiftAdapter(Context mCtx, List<Shift> shiftList) {
        this.mCtx = mCtx;
        this.shiftList = shiftList;
    }

    @NonNull
    @Override
    public ShiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_shifts, parent, false);
        return new ShiftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShiftAdapter.ShiftViewHolder holder, int position) {
        Shift shift = shiftList.get(position);

        String[] dateParts = shift.getDate().split("-");
        String shiftYear = dateParts[0];
        String shiftMonth = dateParts[1];
        String shiftDay = dateParts[2];


        String monthText;

        switch (shiftMonth) {
            case "01":
                monthText = "JAN";
                break;
            case "02":
                monthText = "FEB";
                break;
            case "03":
                monthText = "MAR";
                break;
            case "04":
                monthText = "APR";
                break;
            case "05":
                monthText = "MAY";
                break;
            case "06":
                monthText = "JUN";
                break;
            case "07":
                monthText = "JUL";
                break;
            case "08":
                monthText = "AUG";
                break;
            case "9":
                monthText = "SEP";
                break;
            case "10":
                monthText = "OCT";
                break;
            case "11":
                monthText = "NOV";
                break;
            case "12":
                monthText = "DEC";
                break;
            default: monthText = "ERROR";
                break;
        }


        String [] startParts = shift.getStartTime().split(":");
        String startHour = startParts[0];
        String startMin = startParts[1];
        String startTime = (startHour + ":" + startMin);

        String [] endParts = shift.getEndTime().split(":");
        String endHour = endParts[0];
        String endMin = endParts[1];
        String endTime = (endHour + ":" + endMin);



        holder.shiftMonth.setText(monthText);
        holder.shiftDay.setText(shiftDay);
        holder.shiftYear.setText(shiftYear);
        holder.shiftStart.setText(startTime);
        holder.shiftEnd.setText(endTime);

        Call<UnitResponse> call = RetrofitClient.getInstance().getApi().getUnitById(shift.getUnitId());
        call.enqueue(new Callback<UnitResponse>() {
            @Override
            public void onResponse(Call<UnitResponse> call, Response<UnitResponse> response) {
                Unit unit = response.body().getUnit();
                holder.shiftUnit.setText(unit.getUnitName());
            }

            @Override
            public void onFailure(Call<UnitResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return shiftList.size();
    }

    class ShiftViewHolder extends RecyclerView.ViewHolder {
        TextView shiftMonth, shiftDay, shiftYear, shiftUnit, shiftStart, shiftEnd;
        public ShiftViewHolder(@NonNull View itemView) {
            super(itemView);
            shiftMonth = itemView.findViewById(R.id.shiftMonth);
            shiftDay = itemView.findViewById(R.id.shiftDay);
            shiftYear = itemView.findViewById(R.id.shiftYear);
            shiftUnit = itemView.findViewById(R.id.unitName);
            shiftStart = itemView.findViewById(R.id.shiftStartTime);
            shiftEnd = itemView.findViewById(R.id.shiftEndTime);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if(listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(shiftList.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Shift shift);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}