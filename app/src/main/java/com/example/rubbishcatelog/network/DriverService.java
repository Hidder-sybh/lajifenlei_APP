package com.example.rubbishcatelog.network;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
//retrofit连接接口
public interface DriverService {
    public static final String baseUrl="http://101.200.143.70:11235/";



    @FormUrlEncoded
    @POST("get_all_item/")
    Call<ResponseBody> getall(@Field("class_ID") int id);

    @FormUrlEncoded
    @POST("exact_search//")
    Call<ResponseBody> search(@Field("item_name") String name);

    @Multipart
    @POST("air_search/")
    Call<ResponseBody> pic(@Part MultipartBody.Part file);


}
