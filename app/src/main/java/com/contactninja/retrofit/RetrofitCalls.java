package com.contactninja.retrofit;

import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;
import okhttp3.RequestBody;

import okhttp3.MultipartBody;

import com.contactninja.Utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.contactninja.Utils.LoadingDialog;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitCalls {

    private final RetrofitApiInterface retrofitApiInterface = Objects.requireNonNull(RetrofitApiClient.getClient()).create(RetrofitApiInterface.class);
    private Call<ApiResponse> call;
    private Context context;
    SessionManager session;
    RetrofitCalls retrofitCalls;
    private RetrofitCallback retrofitCallback;

    public RetrofitCalls() {
    }

    public RetrofitCalls(Context context) {
        this.context = context;
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE"); }

    public void login_user(SessionManager session,String email_address, LoadingDialog loadingDialog, RetrofitCallback retrofitCallback) {
        // call = retrofitApiInterface.loginUser(email_address);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);
    }
    public void SignUp_user(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Register(RetrofitApiClient.API_Header,registerinfo);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }

    public void Uservalidate(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Uservalidate(RetrofitApiClient.API_Header,registerinfo);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }


    public void LoginUser(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Userlogin(RetrofitApiClient.API_Header,registerinfo);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }



    public void EmailNumberUpdate(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog,String token, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.EmailNumberUpdate(RetrofitApiClient.API_Header,token,registerinfo);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }



    public void Addcontect(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Addcontect(RetrofitApiClient.API_Header,token,registerinfo);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }






    public void AddGroup(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Addgroup(RetrofitApiClient.API_Header,token,registerinfo);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }



    public void Group_List(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Group_List(RetrofitApiClient.API_Header,token,registerinfo);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }



    public void Contect_List(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Contect_List(RetrofitApiClient.API_Header,token,registerinfo);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }



    public void Contact_details_update(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Contect_delete(RetrofitApiClient.API_Header,token,registerinfo);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }


    public void Refress_Token(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.RefressToken(RetrofitApiClient.API_Header,token,registerinfo);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }



    public void update_contect(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.update_contect(RetrofitApiClient.API_Header,token,registerinfo);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }


    public void Upload_csv(SessionManager session,LoadingDialog loadingDialog, String token, RequestBody organization_id, RequestBody team_id, RequestBody user_id, MultipartBody.Part import_file, RetrofitCallback retrofitCallback) {
       call = retrofitApiInterface.Upload_csv(RetrofitApiClient.API_Header,token,import_file,team_id,user_id,organization_id);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }



    public void Userexistcheck(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Userexistcheck(RetrofitApiClient.API_Header,registerinfo);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }


    private void call_api(final RetrofitCallback retrofitCallback, LoadingDialog loadingDialog) {
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NotNull Call<ApiResponse> call, @NotNull Response<ApiResponse> response) {

                if (response.code() != 200) {
                    if(response.code()==404){
                        session.logoutUser();
                    }else
                    {
                        retrofitCallback.error(response);
                    }
                } else {
                    retrofitCallback.success(response);
                }
            }

            @Override
            public void onFailure(@NotNull Call<ApiResponse> call, @NotNull Throwable t) {
                loadingDialog.cancelLoading();
            }
        });
    }


}
