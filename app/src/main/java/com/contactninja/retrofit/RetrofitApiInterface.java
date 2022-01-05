package com.contactninja.retrofit;


import android.annotation.SuppressLint;

import com.contactninja.Model.SignModel;
import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

@SuppressLint("UnknownNullness")
public interface RetrofitApiInterface {
    @POST("login")
    Call<ApiResponse> loginUser(@Body SignModel signModel);

/*    @Headers("Content-Type: application/json")
    @POST("register")
    Call<ApiResponse> Register(@Body Map<String, String> registerinfo);*/

  /*  @POST("register")
    Call<JsonObject> Register(@Body JsonObject body);*/

    @POST("register")
    Call<ApiResponse> Register(@Header("Accept") String api, @Body JsonObject body,
                               @Header("deviceType") String deviceType , @Header("appVersion") String appVersion);


    @POST("uservalidate")
    Call<ApiResponse> Uservalidate(@Header("Accept") String api, @Body JsonObject body,
                                   @Header("deviceType") String deviceType , @Header("appVersion") String appVersion);


    @POST("login")
    Call<ApiResponse> Userlogin(@Header("Accept") String api, @Body JsonObject body,
                                @Header("deviceType") String deviceType , @Header("appVersion") String appVersion);


    @POST("emailNumberUpdate")
    Call<ApiResponse> EmailNumberUpdate(@Header("Accept") String api, @Header("Authorization") String auth,
                                        @Body JsonObject body,
                                        @Header("deviceType") String deviceType , @Header("appVersion") String appVersion);


    @POST("contact/add")
    Call<ApiResponse> Addcontect(@Header("Accept") String api, @Header("Authorization") String auth,
                                 @Body JsonObject body,
                                 @Header("deviceType") String deviceType , @Header("appVersion") String appVersion);



    @POST("contact/bulk/update")
    Call<ApiResponse> updatecontect(@Header("Accept") String api, @Header("Authorization") String auth,
                                 @Body JsonObject body,
                                 @Header("deviceType") String deviceType , @Header("appVersion") String appVersion);


    @POST("contact/group/add")
    Call<ApiResponse> Addgroup(@Header("Accept") String api, @Header("Authorization") String auth,
                               @Body JsonObject jsonObject,
                               @Header("deviceType") String deviceType , @Header("appVersion") String appVersion);


    @POST("contact/group/list")
    Call<ApiResponse> Group_List(@Header("Accept") String api, @Header("Authorization") String auth,
                                 @Body JsonObject jsonObject,
                                 @Header("deviceType") String deviceType , @Header("appVersion") String appVersion);


    @POST("contact/details/update")
    Call<ApiResponse> Contect_delete(@Header("Accept") String api, @Header("Authorization") String auth,
                                     @Body JsonObject jsonObject,
                                     @Header("deviceType") String deviceType , @Header("appVersion") String appVersion);


    @POST("contact/list")
    Call<ApiResponse> Contect_List(@Header("Accept") String api, @Header("Authorization") String auth,
                                   @Body JsonObject jsonObject,
                                    @Header("appVersion") String appVersion,@Header("deviceType") String deviceType);


    @POST("contact/details/update")
    Call<ApiResponse> update_contect(@Header("Accept") String api, @Header("Authorization") String auth,
                                     @Body JsonObject jsonObject,
                                     @Header("deviceType") String deviceType , @Header("appVersion") String appVersion);


    @POST("refreshToken")
    Call<ApiResponse> RefressToken(@Header("Accept") String api, @Header("Authorization") String auth,
                                   @Body JsonObject jsonObject,
                                   @Header("deviceType") String deviceType , @Header("appVersion") String appVersion);

    @POST("user/changepassword")
    Call<ApiResponse> ResetPassword(@Header("Accept") String api, @Header("Authorization") String auth,
                                    @Body JsonObject jsonObject,
                                    @Header("deviceType") String deviceType , @Header("appVersion") String appVersion);

    @POST("hashtags/list")
    Call<ApiResponse> Hastag_list(@Header("Accept") String api, @Header("Authorization") String auth,
                                  @Body JsonObject jsonObject,
                                  @Header("deviceType") String deviceType , @Header("appVersion") String appVersion);

     @POST("templates/list")
    Call<ApiResponse> Template_list(@Header("Accept") String api, @Header("Authorization") String auth,
                                    @Body JsonObject jsonObject,
                                    @Header("deviceType") String deviceType , @Header("appVersion") String appVersion);

   @POST("templates/add")
    Call<ApiResponse> CreateTemplate(@Header("Accept") String api, @Header("Authorization") String auth,
                                     @Body JsonObject jsonObject,
                                     @Header("deviceType") String deviceType , @Header("appVersion") String appVersion);


   @POST("usergmail/list")
    Call<ApiResponse> Mail_list(@Header("Accept") String api, @Header("Authorization") String auth,
                                @Body JsonObject jsonObject,
                                @Header("deviceType") String deviceType , @Header("appVersion") String appVersion);

   @POST("smtpdefault_update")
    Call<ApiResponse> Mail_setDefault(@Header("Accept") String api, @Header("Authorization") String auth,
                                      @Body JsonObject jsonObject,
                                      @Header("deviceType") String deviceType , @Header("appVersion") String appVersion);



 /*   @Multipart
    @POST("contact/import")
    Call<ApiResponse> Upload_csv(@Header("Accept") String api,
                                 @Header("Authorization") String auth,
                                 @Part("items") RequestBody organization_id,
                                 @Field("team_id") String team_id,
                                 @Field("user_id") String user_id,
                                 @Field("import_file") String import_file);*/


    @Multipart
    @POST("contact/import")
    Call<ApiResponse>Upload_csv(
            @Header("Accept") String api,
    @Header("Authorization") String auth,
            @Part MultipartBody.Part file,
            @Part("team_id") RequestBody team_id,
            @Part("user_id") RequestBody user_id,
            @Part("organization_id") RequestBody organization_id,
            @Part("is_phonebook") RequestBody is_phonebook,
            @Header("deviceType") String deviceType , @Header("appVersion") String appVersion
    );


    @POST("userexistcheck")
    Call<ApiResponse> Userexistcheck(@Header("Accept") String api, @Body JsonObject body,
                                     @Header("deviceType") String deviceType , @Header("appVersion") String appVersion);



    @POST("working_hour/add/default")
    Call<ApiResponse> Working_hour(@Header("Accept") String api, @Header("Authorization") String auth,
                                   @Body JsonObject jsonObject,
                                   @Header("deviceType") String deviceType , @Header("appVersion") String appVersion);


   /* @POST("manual_task/store")
    Call<ApiResponse> Send_SMS_Api(@Header("Accept") String api, @Header("Authorization") String auth,
                                   @Body JsonObject jsonObject,
                                   @Header("deviceType") String deviceType , @Header("appVersion") String appVersion);
*/

    @POST("forgotPassword")
    Call<ApiResponse>  ForgotPassword(@Header("Accept") String api, @Body JsonObject jsonObject,
                                      @Header("deviceType") String deviceType , @Header("appVersion") String appVersion);

    @POST("sequence/task_store")
    Call<ApiResponse>  Task_store(@Header("Accept") String api, @Header("Authorization") String auth,
                                  @Body JsonObject jsonObject,
                                  @Header("deviceType") String deviceType , @Header("appVersion") String appVersion);

    @POST("manual_task/store")
    Call<ApiResponse>  manual_task_store(@Header("Accept") String api, @Header("Authorization") String auth,
                                         @Body JsonObject jsonObject,
                                         @Header("deviceType") String deviceType , @Header("appVersion") String appVersion);


    @POST("manual_task/execute")
    Call<ApiResponse>  Email_execute(@Header("Accept") String api, @Header("Authorization") String auth,
                                     @Body JsonObject jsonObject,
                                     @Header("deviceType") String deviceType , @Header("appVersion") String appVersion);



    @POST("timezone/list")
    Call<ApiResponse>  Timezone_list(@Header("Accept") String api, @Header("Authorization") String auth,
                                     @Body JsonObject jsonObject,
                                     @Header("deviceType") String deviceType , @Header("appVersion") String appVersion);



    @POST("sequence/list")
    Call<ApiResponse>  Sequence_list(@Header("Accept") String api, @Header("Authorization") String auth,
                                     @Body JsonObject jsonObject,
                                     @Header("deviceType") String deviceType , @Header("appVersion") String appVersion);


    @POST("gmailauth_update")
    Call<ApiResponse>  Gmailauth_update(@Header("Accept") String api, @Header("Authorization") String auth,
                                     @Body JsonObject jsonObject,
                                     @Header("deviceType") String deviceType , @Header("appVersion") String appVersion);

 @POST("user/info")
    Call<ApiResponse>  Userinfo(@Header("Accept") String api, @Header("Authorization") String auth,
                                     @Body JsonObject jsonObject,
                                     @Header("deviceType") String deviceType , @Header("appVersion") String appVersion);


    @POST("sequence/contact_store")
    Call<ApiResponse>  Sequence_contact_store(@Header("Accept") String api, @Header("Authorization") String auth,
                                  @Body JsonObject jsonObject,
                                  @Header("deviceType") String deviceType , @Header("appVersion") String appVersion);



    @POST("sequence/settings")
    Call<ApiResponse> Sequence_settings(@Header("Accept") String api, @Header("Authorization") String auth,
                                              @Body JsonObject jsonObject,
                                              @Header("deviceType") String deviceType , @Header("appVersion") String appVersion);

    @POST("contact/group/list")
    Call<ApiResponse> SingleGroup_List(@Header("Accept") String api, @Header("Authorization") String auth,
                                 @Body JsonObject jsonObject,
                                 @Header("deviceType") String deviceType , @Header("appVersion") String appVersion);


}
