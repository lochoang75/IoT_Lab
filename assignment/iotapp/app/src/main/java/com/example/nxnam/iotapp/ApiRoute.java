package com.example.nxnam.iotapp;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by nampr on 3/27/2018.
 */

public interface ApiRoute {

    @GET("data")
    Call<ApiResponse> getData();
}

