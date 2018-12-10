package com.example.nxnam.iotapp.Api;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiController implements Callback<ApiData> {

    public static final String BASE_URL = "https://api.myjson.com/bins/";
    public ApiListener apiListener;

    public interface ApiListener {
        void onGetData(ApiData data);
        void onFail(Throwable t);
    }

    public void setApiListener(ApiListener apiListener) {
        this.apiListener = apiListener;
    }

    @Override
    public void onResponse(Call<ApiData> call, Response<ApiData> response) {
        System.out.print("[mDEBUG] onRespone: " + response.isSuccessful() + "\n");
        if (response.isSuccessful())
        {
            ApiData responeBody = response.body();
            //Log.d("GET_DATA", "Gateway ID: " + responeBody.apiData.get(0).getGatewayId());
            //System.out.print("Respone Body: " + responeBody.toString());
            apiListener.onGetData(responeBody);
        }
    }

    @Override
    public void onFailure(Call<ApiData> call, Throwable t) {

    }

    public ApiController apiGetData(){
        //System.out.print("[DEBUG] ApiController getData\n");
        Gson gson = new GsonBuilder().setLenient().create();
        //System.out.print("[DEBUG] new Gson\n");
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        //System.out.print("[DEBUG] new Retrofit\n");
        ApiRoute apiRoute = retrofit.create(ApiRoute.class);
        //System.out.print("[DEBUG] create ApiRoute\n");
        retrofit2.Call<ApiData> call = apiRoute.getData();
        call.enqueue(this);
        return this;
    }
}
