package com.example.nxnam.iotapp.Api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Gateway {
    @SerializedName("GatewayID")
    private int gatewayId;
    @SerializedName("GatewayData")
    private ArrayList<Node> nodeData;

    public int getGatewayId() {
        return gatewayId;
    }

    public ArrayList<Node> getNodeData() {
        return nodeData;
    }
}
