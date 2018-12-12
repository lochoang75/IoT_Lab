package com.example.nxnam.iotapp.Api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Node {
    @SerializedName("NodeID")
    private int nodeId;
    @SerializedName("NodeData")
    ArrayList<Data> data;

    public int getNodeId() {
        return nodeId;
    }

    public ArrayList<Data> getData() {
        return data;
    }
}
