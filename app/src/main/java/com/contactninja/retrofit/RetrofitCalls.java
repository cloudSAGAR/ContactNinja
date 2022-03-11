package com.contactninja.retrofit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
@SuppressLint("UnknownNullness")
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

    public void SignUp_user(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Register(RetrofitApiClient.API_Header,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }

    public void Uservalidate(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Uservalidate(RetrofitApiClient.API_Header,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }


    public void LoginUser(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Userlogin(RetrofitApiClient.API_Header,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }



    public void EmailNumberUpdate(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog,String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.EmailNumberUpdate(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }



    public void Addcontect(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Addcontect(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }

    public void Updatecontect(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.updatecontect(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }




    public void UpdateUser_Profile(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.update_user_profiel(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }





    public void AddGroup(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Addgroup(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }



    public void Group_List(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Group_List(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }


    public void Contact_details_update(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Contect_delete(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }


    public void Refress_Token(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.RefressToken(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }
    public void Hastag_list(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Hastag_list(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }
    public void Mail_list(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Mail_list(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }
    public void zoomIntegrationExists(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.zoomIntegrationExists(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }
    public void zoomAuthApp(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.zoomAuthApp(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }
    public void helpZoomOauth(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.helpZoomOauth(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }
    public void ZoomDisconnect(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.ZoomDisconnect(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }
    public void ZoomCreate(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.ZoomCreate(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }

    public void Contect_list(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Contect_list(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }

    public void Mail_Activiy_list(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Mail_Activiy_list(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }
    public void Mail_setDefault(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Mail_setDefault(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }

    public void Template_list(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Template_list(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }

    public void CreateTemplate(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.CreateTemplate(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }
    public void ResetPassword(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.ResetPassword(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }



    public void update_contect(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.update_contect(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }

    public void Contect_List(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Contect_List(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }

    public void Upload_csv(SessionManager session,LoadingDialog loadingDialog, String token, RequestBody organization_id,
                           RequestBody team_id, RequestBody user_id,RequestBody id,MultipartBody.Part import_file,
                           String version ,String device_id,RequestBody imei, RetrofitCallback retrofitCallback) {
       call = retrofitApiInterface.Upload_csv(RetrofitApiClient.API_Header,token,import_file,team_id,user_id,organization_id,id,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }



    public void Userexistcheck(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Userexistcheck(RetrofitApiClient.API_Header,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }




    public void Dashboard(SessionManager session, JsonObject registerinfo, LoadingDialog loadingDialog, String token, String version , String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Dashboard(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }
    public void Timezone(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Timezone(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }
    public void Working_hour(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Working_hour(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }

    public void CompanyList(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.CompanyList(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }


  /*  public void Send_SMS_Api(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Send_SMS_Api(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }
*/

    public void ForgotPassword(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.ForgotPassword(RetrofitApiClient.API_Header,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }



    public void Task_store(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Task_store(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }

    public void Task_delete(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Task_delete(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }



    public void manual_task_store(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.manual_task_store(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }
    public void active_task_update(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.active_task_update(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }
    public void manual_task_store_snooze(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.manual_task_store_snooze(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }
    public void Email_execute(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Email_execute(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }

    public void Timezone_list(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog,
                              String token, String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Timezone_list(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }




    public void Task_Data_Return(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog,
                                 String token, String version ,String device_id,RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Sequence_list(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }

    public void working_hour(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog,
                                 String token, String version ,String device_id,RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.working_hour(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }


    public void Gmailauth_update(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog,
                                 String token, String version ,String device_id,RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Gmailauth_update(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }
    public void Userinfo(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog,
                                 String token, String version ,String device_id,RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Userinfo(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }




    public void Sequence_contact_store(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Sequence_contact_store(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }


    public void SingleGroup_List(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.SingleGroup_List(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }
    public void Sequence_settings(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Sequence_settings(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }

    public void Block_contact(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Block_contact(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }

    public void Block_Company(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Block_Company(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }

    public void Company_add(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Add_Company(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }

    public void Broadcast_Activiy_list(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Broadcast_Activiy_list(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }


    public void Broadcast_store(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.Broadcast_store(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }
    public void BZcard_list(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.BZcard_list(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }
    public void BZcard_User_list(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.BZcard_User_list(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
        this.retrofitCallback = retrofitCallback;
        this.session = session;
        call_api(retrofitCallback, loadingDialog);

    }
    public void BZcard_Add_Update(SessionManager session,JsonObject registerinfo, LoadingDialog loadingDialog, String token,String version ,String device_id, RetrofitCallback retrofitCallback) {
        call = retrofitApiInterface.BZcard_Add_Update(RetrofitApiClient.API_Header,token,registerinfo,device_id,version);
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
                        //session.logoutUser();
                    }else
                    {
                        if(response.code()==401&&response.message().equals("Unauthenticated.")){
                            try {
                                Refreess_token(session,loadingDialog);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            retrofitCallback.error(response);
                        }
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

    void Refreess_token(SessionManager sessionManager, LoadingDialog loadingDialog) throws JSONException {


        String token = sessionManager.getAccess_token();
        String Refresh_token = sessionManager.getRefresh_token();
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("refresh_token", sessionManager.getRefresh_token());
        obj.add("data", paramObject);
        Log.e("Tokem is ",new Gson().toJson(obj));
        retrofitCalls.Refress_Token(sessionManager,obj, loadingDialog, token, Global.AppVersion,Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {

                loadingDialog.cancelLoading();
                ApiResponse apiResponse=response.body();
                try{
                    if (apiResponse.getHttp_status() == 200) {
                        Gson gson = new Gson();
                        String headerString = gson.toJson(response.body().getData());
                        Type listType = new TypeToken<SignResponseModel>() {
                        }.getType();
                        SignResponseModel data= new Gson().fromJson(headerString, listType);
                        sessionManager.setRefresh_token(data.getRefreshToken());
                        sessionManager.setAccess_token(data.getTokenType()+" "+data.getAccessToken());

                        Log.e("Access_token",data.getTokenType()+" "+data.getAccessToken());
                        Log.e("Refresh_token",data.getRefreshToken());

                        Toast.makeText(context,context.getResources().getString(R.string.tryAgain),Toast.LENGTH_SHORT).show();

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }


        });








    }

}

