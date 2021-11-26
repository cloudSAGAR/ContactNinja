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
    @Headers({"Accept : application/json", "Authorization : Bearer access_token"})
    @POST("auth/login/otp")
    Call<ApiResponse> loginUser (@Field("user_name") String email_address);



    @POST("register")
    Call<SignResponseModel> Register(@Body SignModel signModel);


}
