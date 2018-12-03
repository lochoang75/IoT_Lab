package com.example.nxnam.iotapp;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Device {
    @SerializedName("ID")
    private String id;
    @SerializedName("Data")
    private ArrayList<Data> dataArrayList;

    private ApiController apiController = new ApiController();

    public Device(String id) {
        this.id = id;
    }

    public Device(String id, ArrayList<Data> dataArrayList) {

        this.id = id;
        this.dataArrayList = dataArrayList;
    }

    public String getId() {
        return id;
    }
}
