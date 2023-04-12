package com.chriswilliams.cal_hope_c868.Entities;

import com.google.gson.annotations.SerializedName;

public class Unit {
    @SerializedName("id")
    private int id;
    @SerializedName("unit_name")
    private String unitName;
    @SerializedName("location")
    private String location;

    public Unit(int id, String unitName, String location) {
        this.id = id;
        this.unitName = unitName;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
