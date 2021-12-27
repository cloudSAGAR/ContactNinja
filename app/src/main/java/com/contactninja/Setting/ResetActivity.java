package com.contactninja.Setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

import retrofit2.Response;

public class ResetActivity extends AppCompatActivity implements View.OnClickListener {

    TextView btn_Create_password,iv_invalid;
    EditText edit_Current_Password,edit_New_Password,edit_Confirm_Password;
    ImageView iv_current_showPassword,iv_new_showPassword,iv_Confirm_showPassword;
    CoordinatorLayout mMainLayout;
    SessionManager sessionManager;
    LoadingDialog loadingDialog;
    RetrofitCalls retrofitCalls;
    @Override
    protected void onCreate(@SuppressLint("UnknownNullness") Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        retrofitCalls = new RetrofitCalls(ResetActivity.this);
        sessionManager=new SessionManager(ResetActivity.this);
        loadingDialog=new LoadingDialog(ResetActivity.this);
        intentView();

    }



    private void intentView() {
        mMainLayout=findViewById(R.id.mMainLayout);
        iv_invalid=findViewById(R.id.iv_invalid);
        iv_Confirm_showPassword=findViewById(R.id.iv_Confirm_showPassword);
        iv_new_showPassword=findViewById(R.id.iv_new_showPassword);
        iv_current_showPassword=findViewById(R.id.iv_current_showPassword);
        edit_Confirm_Password=findViewById(R.id.edit_Confirm_Password);
        edit_New_Password=findViewById(R.id.edit_New_Password);
        edit_Current_Password=findViewById(R.id.edit_Current_Password);
        btn_Create_password=findViewById(R.id.btn_Create_password);
        btn_Create_password.setOnClickListener(this);

        iv_Confirm_showPassword.setOnClickListener(this);
        iv_new_showPassword.setOnClickListener(this);
        iv_current_showPassword.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(@SuppressLint("UnknownNullness") View v) {
        switch (v.getId()){
            case R.id.btn_Create_password:
                if(checkVelidaction()){

                    try {
                        apiCall();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                break;
                case R.id.iv_current_showPassword:
                    if(iv_current_showPassword.isSelected()){
                        iv_current_showPassword.setSelected(false);
                        //hide password
                        iv_current_showPassword.setImageResource(R.drawable.ic_visibility_off);
                        edit_Current_Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        edit_Current_Password.setSelection(edit_Current_Password.getText().length());

                    }else {
                        iv_current_showPassword.setSelected(true);
                        //show password
                        iv_current_showPassword.setImageResource(R.drawable.ic_visibility);
                        edit_Current_Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        edit_Current_Password.setSelection(edit_Current_Password.getText().length());
                    }
                break;
                case R.id.iv_new_showPassword:
                    if(iv_new_showPassword.isSelected()){
                        iv_new_showPassword.setSelected(false);
                        //hide password
                        iv_new_showPassword.setImageResource(R.drawable.ic_visibility_off);
                        edit_New_Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        edit_New_Password.setSelection(edit_New_Password.getText().length());

                    }else {
                        iv_new_showPassword.setSelected(true);
                        //show password
                        iv_new_showPassword.setImageResource(R.drawable.ic_visibility);
                        edit_New_Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        edit_New_Password.setSelection(edit_New_Password.getText().length());
                    }
                break;
                case R.id.iv_Confirm_showPassword:
                    if(iv_Confirm_showPassword.isSelected()){
                        iv_Confirm_showPassword.setSelected(false);
                        //hide password
                        iv_Confirm_showPassword.setImageResource(R.drawable.ic_visibility_off);
                        edit_Confirm_Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        edit_Confirm_Password.setSelection(edit_Confirm_Password.getText().length());

                    }else {
                        iv_Confirm_showPassword.setSelected(true);
                        //show password
                        iv_Confirm_showPassword.setImageResource(R.drawable.ic_visibility);
                        edit_Confirm_Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        edit_Confirm_Password.setSelection(edit_Confirm_Password.getText().length());
                    }
                break;
        }
    }

        private void apiCall() throws JSONException {


            String token = Global.getToken(sessionManager);
            JsonObject obj = new JsonObject();
            JsonObject paramObject = new JsonObject();
            paramObject.addProperty("old_password", edit_Current_Password.getText().toString().trim());
            paramObject.addProperty("password", edit_New_Password.getText().toString().trim());
            paramObject.addProperty("c_password", edit_Confirm_Password.getText().toString().trim());
            paramObject.addProperty("organization_id", "1");
            paramObject.addProperty("team_id", "1");
            paramObject.addProperty("user_id", "1");
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
                        //   Toast.makeText(getActivity(),"Token :(",Toast.LENGTH_SHORT).show();


                    }
                }

                @Override
                public void error(Response<ApiResponse> response) {
                    loadingDialog.cancelLoading();
                }


            });








        }


    private boolean checkVelidaction() {
        String CurrentPassword=edit_Current_Password.getText().toString().trim();
        String NewPassword=edit_New_Password.getText().toString().trim();
        String ConfirmPassword=edit_Confirm_Password.getText().toString().trim();


        if(CurrentPassword.equals("")){
            iv_invalid.setText(getResources().getString(R.string.AddCurrentPassword));
        }else  if(NewPassword.equals("")){
            iv_invalid.setText(getResources().getString(R.string.AddNewPassword));
        } else if(NewPassword.length()<8){
            iv_invalid.setText(getResources().getString(R.string.characters8));
        }else if(!Global.isPasswordValidMethod(NewPassword)){
            iv_invalid.setText(getResources().getString(R.string.passwordCheck));
        }else if(ConfirmPassword.equals("")){
            iv_invalid.setText(getResources().getString(R.string.AddConfirmPassword));
        } else if(ConfirmPassword.length()<8){
            iv_invalid.setText(getResources().getString(R.string.characters8));
        }else if(!Global.isPasswordValidMethod(ConfirmPassword)){
            iv_invalid.setText(getResources().getString(R.string.passwordCheck));
        } else if(!NewPassword.equals(ConfirmPassword)){
            iv_invalid.setText(getResources().getString(R.string.passwordNotMetch));
        }else {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}