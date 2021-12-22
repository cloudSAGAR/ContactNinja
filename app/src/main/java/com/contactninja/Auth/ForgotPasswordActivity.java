package com.contactninja.Auth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.contactninja.MainActivity;
import com.contactninja.R;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.gson.JsonObject;

import org.json.JSONException;

import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edit_email;
    TextView btn_login,tv_signUP,iv_invalid;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    LoadingDialog loadingDialog;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        loadingDialog=new LoadingDialog(this);
        sessionManager=new SessionManager(this);
        initUI();
        retrofitCalls = new RetrofitCalls(this);
        tv_signUP.setOnClickListener(this);
        btn_login.setOnClickListener(this);

    }

    private void initUI() {
        edit_email=findViewById(R.id.edit_email);
        btn_login=findViewById(R.id.btn_login);
        tv_signUP=findViewById(R.id.tv_signUP);
        iv_invalid=findViewById(R.id.iv_invalid);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.tv_signUP:
                finish();
                break;
            case R.id.btn_login:
                ValidationError();
                break;
        }

    }

    private void ValidationError() {
        iv_invalid.setText("");
        if (edit_email.getText().toString().trim().equals("")) {
            iv_invalid.setText(getResources().getString(R.string.invalid_email));
        } else if (!edit_email.getText().toString().matches(emailPattern)) {
            iv_invalid.setText(getResources().getString(R.string.invalid_email));
        }
        else {
            iv_invalid.setText("");
            try {
                Uservalidate();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    private void Uservalidate() throws JSONException {

        String txt_email=edit_email.getText().toString();
        loadingDialog.showLoadingDialog();
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("email", txt_email);
        paramObject.addProperty("first_name", "");
        paramObject.addProperty("last_name", "");
        paramObject.addProperty("login_type","EMAIL");
        obj.add("data", paramObject);
        retrofitCalls.Userexistcheck(sessionManager,obj, loadingDialog, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                if (response.body().getStatus() == 200) {

                    try {
                        ForgotPassword();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    loadingDialog.cancelLoading();
                    iv_invalid.setText(getResources().getString(R.string.invalid_email1));
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });
    }


    private void ForgotPassword() throws JSONException {

        String txt_email=edit_email.getText().toString();
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("email", txt_email);
        obj.add("data", paramObject);
        retrofitCalls.ForgotPassword(sessionManager,obj, loadingDialog, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                if (response.body().getStatus() == 200) {
                    loadingDialog.cancelLoading();

                    showAlertDialogButtonClicked();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            finish();
                        }
                    }, 2000);

                } else {
                    loadingDialog.cancelLoading();
                    iv_invalid.setText(getResources().getString(R.string.invalid_email1));
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });
    }

    public void showAlertDialogButtonClicked() {

        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(this, R.style.MyDialogStyle);

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.permision_dialog, null);
        builder.setView(customLayout);
        AlertDialog dialog
                = builder.create();
        dialog.show();
    }
}