package com.example.nxnam.iotapp;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ApiResponse {
    @SerializedName("Response")
    ArrayList<Device> response;

    @SerializedName("errorMessage")
    String errorMessage;
}
