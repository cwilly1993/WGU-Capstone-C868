package com.chriswilliams.cal_hope_c868.DB;

import com.chriswilliams.cal_hope_c868.Entities.Unit;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllUnitsResponse {
    @SerializedName("error")
    private boolean error;
    @SerializedName("units")
    private List<Unit> units;

    public AllUnitsResponse(boolean error, List<Unit> units) {
        this.error = error;
        this.units = units;
    }

    public boolean isError() {
        return error;
    }

    public List<Unit> getUnits() {
        return units;
    }
}
