package com.example.nxnam.iotapp;

import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("time")
    private long time;
    @SerializedName("value")
    private float humid;

    public Data(long time, float humid) {
        this.time = time;
        this.humid = humid;
    }

    public long getTime() {
        return time;
    }

    public float getHumid() {
        return humid;
    }
}
