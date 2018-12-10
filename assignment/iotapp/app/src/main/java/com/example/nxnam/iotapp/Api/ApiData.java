package com.example.nxnam.iotapp.Api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ApiData {
    @SerializedName("Gateways")
    ArrayList<Gateway> apiData;

    @SerializedName("errorMessage")
    String errorMessage;

    public ArrayList<Gateway> getApiData() {
        return apiData;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
