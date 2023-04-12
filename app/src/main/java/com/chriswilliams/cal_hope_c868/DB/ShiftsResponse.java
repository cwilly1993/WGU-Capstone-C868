package com.chriswilliams.cal_hope_c868.DB;

import com.chriswilliams.cal_hope_c868.Entities.Shift;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShiftsResponse {
    @SerializedName("error")
    private boolean error;
    @SerializedName("shifts")
    private List<Shift> shifts;

    public ShiftsResponse(boolean error, List<Shift> shifts) {
        this.error = error;
        this.shifts = shifts;
    }

    public boolean isError() {
        return error;
    }

    public List<Shift> getShifts() {return shifts;}
}
