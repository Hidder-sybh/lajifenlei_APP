package com.example.rubbishcatelog.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
//每日一句连接
public interface dailynote {
    public static final String baseUrl="http://api.tianapi.com/txapi/one/";

    @FormUrlEncoded
    @POST("index")
    Call<ResponseBody> getall(@Field("key") String key);
}
