package com.chriswilliams.cal_hope_c868.DB;

import com.chriswilliams.cal_hope_c868.Entities.Nurse;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AllNursesResponse {
    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;
    @SerializedName("nurses")
    private ArrayList<Nurse> nurses;

    public AllNursesResponse(boolean error, String message, ArrayList<Nurse> nurses) {
        this.error = error;
        this.message = message;
        this.nurses = nurses;
    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<Nurse> getAllNurses() {return nurses;}
}
