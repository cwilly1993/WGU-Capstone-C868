package com.chriswilliams.cal_hope_c868.DB;

import com.chriswilliams.cal_hope_c868.Entities.Admin;
import com.google.gson.annotations.SerializedName;

public class AdminResponse {
    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;
    @SerializedName("admin")
    private Admin admin;

    public AdminResponse(boolean error, String message, Admin admin) {
        this.error = error;
        this.message = message;
        this.admin = admin;
    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public Admin getAdmin() {
        return admin;
    }
}
