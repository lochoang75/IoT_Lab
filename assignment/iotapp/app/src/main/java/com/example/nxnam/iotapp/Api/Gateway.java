package com.example.nxnam.iotapp.Api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Gateway {
    @SerializedName("IdGateway")
    private int gatewayId;
    @SerializedName("Nodes")
    private ArrayList<Node> nodeData;

    public int getGatewayId() {
        return gatewayId;
    }

    public ArrayList<Node> getNodeData() {
        return nodeData;
    }
}
