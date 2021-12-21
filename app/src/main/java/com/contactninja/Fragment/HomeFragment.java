package com.contactninja.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.contactninja.Model.Grouplist;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import retrofit2.Response;
public class HomeFragment extends Fragment {


    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    SessionManager sessionManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        retrofitCalls = new RetrofitCalls(getActivity());
        loadingDialog=new LoadingDialog(getActivity());
        sessionManager=new SessionManager(getActivity());
        try {
            Refreess_token();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            TimeZooneUpdate();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private void Refreess_token() throws JSONException {


        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("refresh_token", ""+token);
        obj.add("data", paramObject);
        Log.e("Tokem is ",new Gson().toJson(obj));
        retrofitCalls.Refress_Token(sessionManager,obj, loadingDialog, token, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {

                loadingDialog.cancelLoading();
                if (response.body().getStatus() == 200) {
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<Grouplist>() {
                    }.getType();
                    SignResponseModel data= new Gson().fromJson(headerString, listType);
                    sessionManager.setRefresh_token(data.getTokenType()+" "+data.getAccessToken());
                    //   sessionManager.setUserdata(getApplicationContext(),data);

                } else {

                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }


        });








    }


    private void TimeZooneUpdate() throws JSONException {

        Calendar cal = Calendar.getInstance();
        TimeZone tz1 = cal.getTimeZone();
        Calendar calendar = Calendar.getInstance(tz1,
                Locale.getDefault());

        Date currentLocalTime = calendar.getTime();
        DateFormat date = new SimpleDateFormat("Z");
        String localTime = date.format(currentLocalTime);
        String  offset = localTime.substring(0, 1);

//      //  Log.e("GMT offset is %s hours",""+ TimeUnit.MINUTES.convert(tz1.getRawOffset(), TimeUnit.MILLISECONDS));

        String time=offset+TimeUnit.MINUTES.convert(tz1.getRawOffset(), TimeUnit.MILLISECONDS);
        Log.e("offset",time);
       // loadingDialog.showLoadingDialog();
        String token = Global.getToken(sessionManager);
        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        String user_id = String.valueOf(user_data.getUser().getId());
        //String workin_time=user_data.getUser().getWorkingHoursList();
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());

        Log.e("Size is",new Gson().toJson(user_data.getUser().getWorkingHoursList()));
        if (user_data.getUser().getWorkingHoursList().size()==0)
        {
            JsonObject obj = new JsonObject();
            JsonObject paramObject = new JsonObject();
            paramObject.addProperty("diff_minutes", time);
            paramObject.addProperty("is_default", "1");
            paramObject.addProperty("organization_id", "1");
            paramObject.addProperty("team_id","1");
            paramObject.addProperty("user_id",user_id);
            obj.add("data", paramObject);
            retrofitCalls.Working_hour(sessionManager,obj, loadingDialog, token,new RetrofitCallback() {
                @Override
                public void success(Response<ApiResponse> response) {
                    //Log.e("Response is",new Gson().toJson(response));


                    if (response.body().getStatus() == 200) {
                      //  loadingDialog.cancelLoading();
                    } else {
                       // loadingDialog.cancelLoading();
                    }
                }

                @Override
                public void error(Response<ApiResponse> response) {
                    //loadingDialog.cancelLoading();
                }
            });
        }


    }
}