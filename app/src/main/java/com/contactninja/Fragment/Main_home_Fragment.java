package com.contactninja.Fragment;

import android.annotation.SuppressLint;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.browser.trusted.Token;
import androidx.fragment.app.Fragment;

import com.contactninja.Auth.SignupActivity;
import com.contactninja.MainActivity;
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
@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Main_home_Fragment extends Fragment {


    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    SessionManager sessionManager;
    ImageView iv_toolbar_mail,iv_toolbar_select;
    LinearLayout layout_toolbar_logo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        retrofitCalls = new RetrofitCalls(getActivity());
        loadingDialog=new LoadingDialog(getActivity());
        sessionManager=new SessionManager(getActivity());
     /*   try {
            if(Global.isNetworkAvailable(getActivity(), MainActivity.mMainLayout)) {
                Refreess_token();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        try {
            if(Global.isNetworkAvailable(getActivity(),MainActivity.mMainLayout)) {
                TimeZooneUpdate();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        View view= inflater.inflate(R.layout.fragment_main_home, container, false);
        intentView(view);

        return view;
    }

    private void intentView(View view) {
        iv_toolbar_mail=view.findViewById(R.id.iv_toolbar_mail);
        iv_toolbar_mail.setVisibility(View.VISIBLE);
        iv_toolbar_select=view.findViewById(R.id.iv_toolbar_select);
        iv_toolbar_select.setVisibility(View.VISIBLE);
        layout_toolbar_logo=view.findViewById(R.id.layout_toolbar_logo);
        layout_toolbar_logo.setVisibility(View.VISIBLE);
    }

    private void Refreess_token() throws JSONException {


        String token = sessionManager.getAccess_token();
        String Refresh_token = sessionManager.getRefresh_token();
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("refresh_token", sessionManager.getRefresh_token());
        obj.add("data", paramObject);
        Log.e("Tokem is ",new Gson().toJson(obj));
        retrofitCalls.Refress_Token(sessionManager,obj, loadingDialog, token,Global.getVersionname(getActivity()),Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {

                loadingDialog.cancelLoading();
                ApiResponse apiResponse=response.body();
                try{
                    if (apiResponse.getStatus() == 200) {
                        Gson gson = new Gson();
                        String headerString = gson.toJson(response.body().getData());
                        Type listType = new TypeToken<SignResponseModel>() {
                        }.getType();
                        SignResponseModel data= new Gson().fromJson(headerString, listType);
                        sessionManager.setRefresh_token(data.getRefreshToken());
                        sessionManager.setAccess_token(data.getTokenType()+" "+data.getAccessToken());

                        Log.e("Access_token",data.getTokenType()+" "+data.getAccessToken());
                        Log.e("Refresh_token",data.getRefreshToken());

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


    private void TimeZooneUpdate() throws JSONException {

        Calendar cal = Calendar.getInstance();
        TimeZone tz1 = cal.getTimeZone();
        Calendar calendar = Calendar.getInstance(tz1,
                Locale.getDefault());

        Date currentLocalTime = calendar.getTime();
        @SuppressLint("SimpleDateFormat") DateFormat date = new SimpleDateFormat("Z");
        String localTime = date.format(currentLocalTime);
        String  offset = localTime.substring(0, 1);

//      //  Log.e("GMT offset is %s hours",""+ TimeUnit.MINUTES.convert(tz1.getRawOffset(), TimeUnit.MILLISECONDS));

        String time=offset+TimeUnit.MINUTES.convert(tz1.getRawOffset(), TimeUnit.MILLISECONDS);
        Log.e("offset",time);
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
            retrofitCalls.Working_hour(sessionManager,obj, loadingDialog, token,Global.getVersionname(getActivity()),Global.Device,new RetrofitCallback() {
                @Override
                public void success(Response<ApiResponse> response) {
                    //Log.e("Response is",new Gson().toJson(response));
                }

                @Override
                public void error(Response<ApiResponse> response) {
                }
            });
        }


    }
}