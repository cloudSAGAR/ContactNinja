package com.contactninja.retrofit;


import com.google.gson.JsonObject;
import com.contactninja.Model.SignModel;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
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


    @POST("emailNumberUpdate")
    Call<ApiResponse> EmailNumberUpdate(@Header("Accept") String api,@Header("Authorization") String auth, @Body JsonObject body);



    @POST("contact/add")
    Call<ApiResponse> Addcontect(@Header("Accept") String api,@Header("Authorization") String auth, @Body JsonObject body);

    @POST("contact/group/add")
    Call<ApiResponse> Addgroup(@Header("Accept") String api,@Header("Authorization") String auth,@Body JsonObject jsonObject);


    @POST("contact/group/list")
    Call<ApiResponse> Group_List(@Header("Accept") String api,@Header("Authorization") String auth,@Body JsonObject jsonObject);

}
