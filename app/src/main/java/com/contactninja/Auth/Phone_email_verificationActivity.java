package com.contactninja.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.contactninja.Auth.PlanTyep.PlanType_Screen;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitApiClient;
import com.contactninja.retrofit.RetrofitApiInterface;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.gson.JsonObject;

import org.json.JSONException;

import retrofit2.Response;

public class Phone_email_verificationActivity extends AppCompatActivity implements View.OnClickListener {
    public static RetrofitApiInterface apiService;
    TextView btn_getStarted, iv_invalid;
    SessionManager sessionManager;
    LinearLayout layout_phonenumber, layout_email;
    EditText edit_Mobile, edit_email;
    String u_mobile, u_email;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    LoadingDialog loadingDialog;
    RetrofitCalls retrofitCalls;
    String login_type;

    RelativeLayout mMainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_email_verification);
        initUI();

        sessionManager = new SessionManager(this);

        check_login_type();
        retrofitCalls = new RetrofitCalls();
        loadingDialog = new LoadingDialog(this);
        apiService = RetrofitApiClient.getClient().create(RetrofitApiInterface.class);

    }

    private void check_login_type() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String type = bundle.getString("login_type");
        if (type.equals("PHONE")) {

            layout_email.setVisibility(View.VISIBLE);
            layout_phonenumber.setVisibility(View.GONE);
            u_email = edit_email.getText().toString();


        } else {
            layout_email.setVisibility(View.GONE);
            layout_phonenumber.setVisibility(View.VISIBLE);
            u_mobile = edit_Mobile.getText().toString();
        }
    }


    private void check_login_type1() {

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String login_type = bundle.getString("login_type");
        if (login_type.equals("PHONE")) {
            u_email = edit_email.getText().toString();
            if (u_email.equals("")) {
                iv_invalid.setText(getResources().getString(R.string.invalid_email));
            } else if (!u_email.matches(emailPattern)) {
                iv_invalid.setText(getResources().getString(R.string.invalid_email));

            } else {
                // login_type="EMAIL";
                try {
                    EmailUpdate();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } else {
            layout_email.setVisibility(View.GONE);
            layout_phonenumber.setVisibility(View.VISIBLE);
            u_mobile = edit_Mobile.getText().toString();
            if (u_mobile.trim().equals("")) {
                iv_invalid.setText(getResources().getString(R.string.invalid_phone));
            } else if (u_mobile.length() != 10) {
                iv_invalid.setText(getResources().getString(R.string.invalid_phone));
            } else {
                // login_type="PHONE";
                try {
                    PhoneUpdate();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    private void initUI() {
        btn_getStarted = findViewById(R.id.btn_getStarted);
        btn_getStarted.setOnClickListener(this);
        sessionManager = new SessionManager(this);
        layout_phonenumber = findViewById(R.id.layout_phonenumber);
        layout_email = findViewById(R.id.layout_email);
        edit_email = findViewById(R.id.edit_email);
        edit_Mobile = findViewById(R.id.edit_Mobile);
        iv_invalid = findViewById(R.id.iv_invalid);
        mMainLayout = findViewById(R.id.mMainLayout);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_getStarted:
                check_login_type1();

                break;
        }
    }


    private void EmailUpdate() throws JSONException {

        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(this);
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String type = bundle.getString("login_type");
        if (type.equals("PHONE")) {
            type = "EMAIL";

        } else {

            type = "PHONE";
        }
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("email", edit_email.getText().toString());
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("team_id", team_id);
        paramObject.addProperty("update_type", type);
        paramObject.addProperty("user_id", user_id);
        obj.add("data", paramObject);

        retrofitCalls.EmailNumberUpdate(sessionManager,obj, loadingDialog, Global.getToken(this), new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                if (response.body().getStatus() == 200) {
                    sessionManager.Email_Update();
                    loadingDialog.cancelLoading();
                    startActivity(new Intent(getApplicationContext(), PlanType_Screen.class));
                    finish();
                    //Global.Messageshow(getApplicationContext(), mMainLayout, response.body().getMessage(), true);

                } else {
                    loadingDialog.cancelLoading();
                    Global.Messageshow(getApplicationContext(), mMainLayout, response.body().getMessage(), false);

                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });
    }


    private void PhoneUpdate() throws JSONException {

        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(this);


        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String type = bundle.getString("login_type");

        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("contact_number", edit_Mobile.toString());
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("team_id", team_id);
        paramObject.addProperty("update_type", type);
        paramObject.addProperty("user_id", user_id);
        obj.add("data", paramObject);

        retrofitCalls.EmailNumberUpdate(sessionManager,obj, loadingDialog, Global.getToken(this), new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                if (response.body().getStatus() == 200) {
                    loadingDialog.cancelLoading();

                    sessionManager.Email_Update();
                    Global.Messageshow(getApplicationContext(), mMainLayout, response.body().getMessage(), true);
                    startActivity(new Intent(getApplicationContext(), PlanType_Screen.class));
                    finish();

                } else {
                    loadingDialog.cancelLoading();
                    Global.Messageshow(getApplicationContext(), mMainLayout, response.body().getMessage(), false);


                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });
    }
}


