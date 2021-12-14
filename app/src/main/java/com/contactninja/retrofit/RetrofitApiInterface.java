package com.contactninja.retrofit;


import com.contactninja.Model.SignModel;
import com.google.gson.JsonObject;

import okhttp3.internal.connection.Exchange;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitApiInterface {
    @POST("login")
    Call<ApiResponse> loginUser(@Body SignModel signModel);

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
    Call<ApiResponse> EmailNumberUpdate(@Header("Accept") String api, @Header("Authorization") String auth, @Body JsonObject body);


    @POST("contact/add")
    Call<ApiResponse> Addcontect(@Header("Accept") String api, @Header("Authorization") String auth, @Body JsonObject body);

    @POST("contact/group/add")
    Call<ApiResponse> Addgroup(@Header("Accept") String api, @Header("Authorization") String auth, @Body JsonObject jsonObject);


    @POST("contact/group/list")
    Call<ApiResponse> Group_List(@Header("Accept") String api, @Header("Authorization") String auth, @Body JsonObject jsonObject);


    @POST("contact/details/update")
    Call<ApiResponse> Contect_delete(@Header("Accept") String api, @Header("Authorization") String auth, @Body JsonObject jsonObject);


    @POST("contact/list")
    Call<ApiResponse> Contect_List(@Header("Accept") String api, @Header("Authorization") String auth, @Body JsonObject jsonObject);


    @POST("contact/details/update")
    Call<ApiResponse> update_contect(@Header("Accept") String api, @Header("Authorization") String auth, @Body JsonObject jsonObject);


    @POST("refreshToken")
    Call<ApiResponse> RefressToken(@Header("Accept") String api, @Header("Authorization") String auth, @Body JsonObject jsonObject);

    @Multipart
    @POST("contact/import")
    Call<ApiResponse> Upload_csv(@Header("Accept") String api,
                                 @Header("Authorization") String auth,
                                 @Part("items")RequestBody organization_id,
                                 @Field("team_id") String team_id,
                                 @Field("user_id") String user_id,
                                 @Field("import_file") String import_file);


    @Multipart
    @POST("contact/import")
    Call<ApiResponse>Upload_csv1(
            @Header("Cookie") String sessionIdAndRz,
            @Part MultipartBody.Part file,
            @Part("items") RequestBody items,
            @Part("isAny") RequestBody isAny
    );


}
