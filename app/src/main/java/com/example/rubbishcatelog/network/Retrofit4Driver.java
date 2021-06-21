package com.example.rubbishcatelog.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//retrofit连接工具类
public class Retrofit4Driver {
    Retrofit retrofit = null;
    DriverService siterService = null;

    public Retrofit4Driver() {
        retrofit = new Retrofit.Builder()
                .baseUrl(DriverService.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        siterService = retrofit.create(DriverService.class);
    }
}
