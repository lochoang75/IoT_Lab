package com.example.nxnam.iotapp.Api;

import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("time")
    private long time;
    @SerializedName("humidnity")
    private double humid;

    public long getTime() {
        return time;
    }

    public double getHumid() {
        return humid;
    }
}
