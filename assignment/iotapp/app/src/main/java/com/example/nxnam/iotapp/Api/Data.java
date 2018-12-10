package com.example.nxnam.iotapp.Api;

import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("time")
    private long time;
    @SerializedName("value")
    private float humid;

    public long getTime() {
        return time;
    }

    public float getHumid() {
        return humid;
    }
}
