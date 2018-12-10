package com.example.nxnam.iotapp.Api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Node {
    @SerializedName("IdNode")
    private int nodeId;
    @SerializedName("data")
    ArrayList<Data> data;

    public int getNodeId() {
        return nodeId;
    }

    public ArrayList<Data> getData() {
        return data;
    }
}
