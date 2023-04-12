package com.chriswilliams.cal_hope_c868.DB;

import com.chriswilliams.cal_hope_c868.Entities.NurseRequest;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RequestsResponse {
    @SerializedName("error")
    private boolean error;
    @SerializedName("nurse_requests")
    private ArrayList<NurseRequest> requests;

    public RequestsResponse(boolean error, ArrayList<NurseRequest> requests) {
        this.error = error;
        this.requests = requests;
    }

    public boolean isError() {
        return error;
    }

    public ArrayList<NurseRequest> getRequests() {
        return requests;
    }
}
