package com.example.nxnam.iotapp.Api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiRoute {
    @GET("json.php")
    Call<ApiData> getData();
}
