package com.chriswilliams.cal_hope_c868.DB;

import com.chriswilliams.cal_hope_c868.Entities.Unit;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UnitResponse {
    @SerializedName("error")
    private boolean error;
    @SerializedName("unit")
    private Unit unit;

    public UnitResponse(boolean error, Unit unit) {
        this.error = error;
        this.unit = unit;
    }

    public boolean isError() {
        return error;
    }

    public Unit getUnit() {
        return unit;
    }
}
