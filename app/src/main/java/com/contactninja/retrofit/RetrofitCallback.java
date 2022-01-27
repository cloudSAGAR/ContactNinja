package com.contactninja.retrofit;


import android.annotation.SuppressLint;

import org.json.JSONException;

import retrofit2.Response;
@SuppressLint("UnknownNullness")

public interface RetrofitCallback {
    void success(Response<ApiResponse> response) ;
    void error(Response<ApiResponse> response);
}
