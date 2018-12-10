package com.example.nxnam.iotapp.Api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiRoute {
    @GET("1czkdq/")
    Call<ApiData> getData();
}
