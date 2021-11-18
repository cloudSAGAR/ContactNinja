package com.intricare.test.retrofit;


import retrofit2.Response;

public interface RetrofitCallback {
    void success(Response<ApiResponse> response);
    void error(Response<ApiResponse> response);
}
