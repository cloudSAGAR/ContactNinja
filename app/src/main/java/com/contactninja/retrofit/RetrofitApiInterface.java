package com.contactninja.retrofit;


import com.google.gson.JsonObject;
import com.contactninja.Model.SignModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

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


    @POST("uservalidate")
    Call<ApiResponse> Uservalidate(@Header("Accept") String api, @Body JsonObject body);


    @POST("login")
    Call<ApiResponse> Userlogin(@Header("Accept") String api, @Body JsonObject body);
}
