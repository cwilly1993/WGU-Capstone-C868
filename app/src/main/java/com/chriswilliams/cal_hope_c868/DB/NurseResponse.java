package com.chriswilliams.cal_hope_c868.DB;

import com.chriswilliams.cal_hope_c868.Entities.Nurse;
import com.google.gson.annotations.SerializedName;

public class NurseResponse {
    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;
    @SerializedName("nurse")
    private Nurse nurse;

    public NurseResponse(boolean error, String message, Nurse nurse) {
        this.error = error;
        this.message = message;
        this.nurse = nurse;
    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public Nurse getNurse() {
        return nurse;
    }
}