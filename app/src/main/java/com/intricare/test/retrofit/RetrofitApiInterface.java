package com.intricare.test.retrofit;


import com.intricare.test.Model.SignModel;
import com.intricare.test.Model.SignResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RetrofitApiInterface {
    @POST("login")
    Call<ApiResponse> loginUser (@Body SignModel signModel);

    @POST("/register")
    Call<SignResponseModel> Register(@Body SignModel signModel);


}
