package com.chriswilliams.cal_hope_c868.Entities;

import com.google.gson.annotations.SerializedName;

public class Shift {
    @SerializedName("id")
    private int id;
    @SerializedName("shift_date")
    private String date;
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("end_time")
    private String endTime;
    @SerializedName("unit_id")
    private int unitId;
    @SerializedName("nurse_id")
    private int nurseId;

    public Shift(int id, String date, String startTime, String endTime, int unitId, int nurseId) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.unitId = unitId;
        this.nurseId = nurseId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String start_time) {
        this.startTime = start_time;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String end_time) {
        this.endTime = end_time;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unit_id) {
        this.unitId = unit_id;
    }

    public int getNurseId() {
        return nurseId;
    }

    public void setNurseId(int nurse_id) {
        this.nurseId = nurse_id;
    }
}
