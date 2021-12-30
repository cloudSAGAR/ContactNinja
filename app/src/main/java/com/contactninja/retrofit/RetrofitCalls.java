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
    public void Hastag_list(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Hastag_list(RetrofitApiClient.API_Header,token,registerinfo);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }
    public void Mail_list(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Mail_list(RetrofitApiClient.API_Header,token,registerinfo);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }
    public void Mail_setDefault(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Mail_setDefault(RetrofitApiClient.API_Header,token,registerinfo);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }

    public void Template_list(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Template_list(RetrofitApiClient.API_Header,token,registerinfo);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }
    public void CreateTemplate(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.CreateTemplate(RetrofitApiClient.API_Header,token,registerinfo);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }
    public void ResetPassword(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.ResetPassword(RetrofitApiClient.API_Header,token,registerinfo);
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


    public void Upload_csv(SessionManager session,LoadingDialog loadingDialog, String token, RequestBody organization_id, RequestBody team_id, RequestBody user_id,RequestBody id, MultipartBody.Part import_file, RetrofitCallback retrofitCallback) {
       call = retrofitApiInterface.Upload_csv(RetrofitApiClient.API_Header,token,import_file,team_id,user_id,organization_id,id);
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




    public void Working_hour(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Working_hour(RetrofitApiClient.API_Header,token,registerinfo);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }


    public void Send_SMS_Api(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Send_SMS_Api(RetrofitApiClient.API_Header,token,registerinfo);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }


    public void ForgotPassword(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.ForgotPassword(RetrofitApiClient.API_Header,registerinfo);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }



    public void Task_store(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Task_store(RetrofitApiClient.API_Header,token,registerinfo);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }



    public void manual_task_store(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.manual_task_store(RetrofitApiClient.API_Header,token,registerinfo);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }





    public void Email_execute(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Email_execute(RetrofitApiClient.API_Header,token,registerinfo);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }

    public void Timezone_list(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Timezone_list(RetrofitApiClient.API_Header,token,registerinfo);
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
