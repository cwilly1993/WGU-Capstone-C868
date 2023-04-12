package com.chriswilliams.cal_hope_c868.Entities;

import com.google.gson.annotations.SerializedName;

public class NurseRequest {
    @SerializedName("id")
    private int id;
    @SerializedName("start_date")
    private String startDate;
    @SerializedName("end_date")
    private String endDate;
    @SerializedName("type")
    private String type;
    @SerializedName("status")
    private String status;
    @SerializedName("notes")
    private String notes;
    @SerializedName("nurse_id")
    private int nurseId;

    public NurseRequest(int id, String startDate, String endDate, String type, String status, String note, int nurseId) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.status = status;
        this.notes = note;
        this.nurseId = nurseId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getNurseId() {
        return nurseId;
    }

    public void setNurseId(int nurseId) {
        this.nurseId = nurseId;
    }
}
