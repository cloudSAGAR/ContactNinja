package com.intricare.test.retrofit;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitApiInterface {
    @FormUrlEncoded
    @POST("auth/login/otp")
    Call<ApiResponse> loginUser (@Field("user_name") String email_address);

}
