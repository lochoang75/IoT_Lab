package com.example.nxnam.iotapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nampr on 3/27/2018.
 */

public class ApiController implements Callback<ApiResponse> {
    public static final String BASE_URL = "http:/test.apitiny.com/api/bkit/";
    public ApiListener listener;

    public interface ApiListener {
        void onGetData(ApiResponse data);
        void onFail(Throwable t);
    }
    public void addListener(ApiListener listener){
        this.listener = listener;
    }

    @Override
    public void onResponse(retrofit2.Call<ApiResponse> call, Response<ApiResponse> response) {
        if (response.isSuccessful()){
            ApiResponse data = response.body();
            //if (data.data.get(0).getMssv().compareTo("1512098") == 0)
            listener.onGetData(data);
        } else {
            ApiResponse data = new ApiResponse();
            try {
                data.errorMessage = response.errorBody().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            listener.onGetData(data);
        }
    }

    @Override
    public void onFailure(retrofit2.Call<ApiResponse> call, Throwable t) {
        listener.onFail(t);
        t.printStackTrace();
    }
    public ApiController getData(){
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)).build();
        ApiRoute route = retrofit.create(ApiRoute.class);
        retrofit2.Call<ApiResponse> call = route.getData();
        call.enqueue(this);
        return this;
    }
}
