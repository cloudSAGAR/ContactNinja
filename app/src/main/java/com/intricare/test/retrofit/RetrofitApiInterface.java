package com.intricare.test.retrofit;


import com.google.gson.JsonObject;
import com.intricare.test.Model.SignModel;
import com.intricare.test.Model.SignResponseModel;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface RetrofitApiInterface {
    @POST("login")
    Call<ApiResponse> loginUser (@Body SignModel signModel);

/*    @Headers("Content-Type: application/json")
    @POST("register")
    Call<ApiResponse> Register(@Body Map<String, String> registerinfo);*/

  /*  @POST("register")
    Call<JsonObject> Register(@Body JsonObject body);*/

    @POST("register")
    Call<ApiResponse> Register(@Header("Accept") String api, @Body JsonObject body);
}
