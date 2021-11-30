package com.contactninja.Auth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.contactninja.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hbb20.CountryCodePicker;
import com.contactninja.Model.SignModel;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {
    private static final String TAG = "LoginActivity";
    TextView btn_chnage_phone_email, btn_login, iv_invalid, tv_signUP;
    boolean is_PhoneShow = true;

    LinearLayout layout_email, layout_phonenumber;
    CountryCodePicker ccp_id;

    EditText edit_email, edit_Mobile,edit_password;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    public String fcmToken = "";
    CoordinatorLayout mMainLayout;
    LoadingDialog loadingDialog;
    RetrofitCalls retrofitCalls;
    String Login_type="PHONE",password="";
    SessionManager sessionManager;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();



        loadingDialog = new LoadingDialog(LoginActivity.this);
        initUI();
        retrofitCalls=new RetrofitCalls(this);
        firebase();
        Global.checkConnectivity(LoginActivity.this,mMainLayout);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Global.getInstance().setConnectivityListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void firebase() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            //Log.i("FCM registration failed", task.getException() + "");
                            return;
                        }

                        fcmToken = task.getResult();
                    }
                });
        // Initialize phone auth callbacks  [START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.e(TAG, "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
                // [END_EXCLUDE]

//                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.e(TAG, "onVerificationFailed", e);
                loadingDialog.cancelLoading();

                Toast.makeText(getApplicationContext(), "VERIFY FAILED", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                loadingDialog.cancelLoading();
                String countryCode = ccp_id.getSelectedCountryCodeWithPlus();
                Intent intent=new Intent(getApplicationContext(), VerificationActivity.class);
                intent.putExtra("v_id",verificationId);
                intent.putExtra("mobile",edit_Mobile.getText().toString());
                intent.putExtra("countrycode",countryCode);
                intent.putExtra("f_name","");

                intent.putExtra("l_name","");
                intent.putExtra("email",edit_email.getText().toString());
                intent.putExtra("login_type",Login_type);
                intent.putExtra("activity_flag","login");
                startActivity(intent);


            }
        };
    }



    private boolean validateUsing_libphonenumber(String countryCode, String phNumber) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.createInstance(getApplicationContext());
        String isoCode = phoneNumberUtil.getRegionCodeForCountryCode(Integer.parseInt(countryCode));
        Phonenumber.PhoneNumber phoneNumber = null;
        try {
            //phoneNumber = phoneNumberUtil.parse(phNumber, "IN");  //if you want to pass region code
            phoneNumber = phoneNumberUtil.parse(phNumber, isoCode);
        } catch (NumberParseException e) {
            System.err.println(e);
        }

        boolean isValid = phoneNumberUtil.isValidNumber(phoneNumber);
        if (isValid) {
            String internationalFormat = phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
            // Toast.makeText(this, "Phone Number is Valid " + internationalFormat, Toast.LENGTH_LONG).show();
            return true;
        } else {
            //Toast.makeText(this, "Phone Number is Invalid " + phoneNumber, Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void initUI() {
        sessionManager=new SessionManager(this);
        ccp_id = findViewById(R.id.ccp_id);
        edit_email = findViewById(R.id.edit_email);
        edit_Mobile = findViewById(R.id.edit_Mobile);
        btn_login = findViewById(R.id.btn_login);
        iv_invalid = findViewById(R.id.iv_invalid);
        tv_signUP = findViewById(R.id.tv_signUP);
        mMainLayout=findViewById(R.id.mMainLayout);
        edit_password=findViewById(R.id.edit_password);
        layout_phonenumber = findViewById(R.id.layout_phonenumber);
        layout_email = findViewById(R.id.layout_email);
        btn_chnage_phone_email = findViewById(R.id.btn_chnage_phone_email);
        btn_chnage_phone_email.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        tv_signUP.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_chnage_phone_email:
                if (is_PhoneShow) {
                    layout_email.setVisibility(View.VISIBLE);
                    layout_phonenumber.setVisibility(View.GONE);
                    is_PhoneShow = false;
                    btn_chnage_phone_email.setText(getResources().getString(R.string.or_phone));
                } else {
                    layout_phonenumber.setVisibility(View.VISIBLE);
                    layout_email.setVisibility(View.GONE);
                    is_PhoneShow = true;
                    btn_chnage_phone_email.setText(getResources().getString(R.string.or_email));
                }
                break;
            case R.id.btn_login:
                if (checkVelidaction()) {
                     sessionManager.setlogin_type(Login_type);
                     if (Login_type.equals("EMAIL"))
                     {
                         LoginData();
                     }
                     else {
                         loadingDialog.showLoadingDialog();
                         VerifyPhone(edit_Mobile.getText().toString().trim());

                     }
                  //  LoginData();
                     //startActivity(new Intent(getApplicationContext(),VerificationActivity.class));
                     //loadingDialog.showLoadingDialog();
                    // LoginApicall();
                    /* if(is_PhoneShow){
                         VerifyPhone(edit_Mobile.getText().toString().trim());
                     }else {


                     }*/
                }


                break;
            case R.id.tv_signUP:

                startActivity(new Intent(getApplicationContext(), SignupActivity.class));
                finish();

                break;
        }
    }

    private void LoginApicall() {

       //retrofitCalls.login_user();
    }

    public void VerifyPhone(String phoneNumber) {
        String countryCode = ccp_id.getSelectedCountryCodeWithPlus();
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(countryCode + phoneNumber)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private boolean checkVelidaction() {

        if (is_PhoneShow) {
            Login_type="PHONE";
            if (edit_Mobile.getText().toString().trim().equals("")) {
                iv_invalid.setText(getResources().getString(R.string.invalid_phone));
            }
            else if (edit_Mobile.getText().length()!=10)
            {
                iv_invalid.setText(getResources().getString(R.string.invalid_phone));
            }
            else {
                String countryCode = ccp_id.getSelectedCountryCodeWithPlus();
                String phoneNumber = edit_Mobile.getText().toString().trim();
                if (countryCode.length() > 0 && phoneNumber.length() > 0) {
                    if (Global.isValidPhoneNumber(phoneNumber)) {
                        boolean status = validateUsing_libphonenumber(countryCode, phoneNumber);
                        if (status) {
                            iv_invalid.setText("");
                            return true;
                        } else {
                            iv_invalid.setText(getResources().getString(R.string.invalid_phone));
                        }
                    } else {
                        iv_invalid.setText(getResources().getString(R.string.invalid_phone));
                    }
                }
            }
        } else {
            Login_type="EMAIL";
            if (edit_email.getText().toString().trim().equals("")) {
                iv_invalid.setText(getResources().getString(R.string.invalid_email));

            }
            else if (!edit_email.getText().toString().matches(emailPattern))
            {
                iv_invalid.setText(getResources().getString(R.string.invalid_email));

            }
            else if (edit_password.getText().equals(""))
            {
                iv_invalid.setText(getResources().getString(R.string.invalid_password));
            }
            else {
                if(Global.emailValidator(edit_email.getText().toString().trim())){
                    return true;
                }else {
                    iv_invalid.setText(getResources().getString(R.string.invalid_email));
                }

            }
        }
        return false;
    }




    public void LoginData()
    {
        password=edit_password.getText().toString();

        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("email", edit_email.getText().toString());
        paramObject.addProperty("login_type", Login_type);
        paramObject.addProperty("otp", "123456");
        obj.add("data",paramObject);
        retrofitCalls.LoginUser(obj, loadingDialog, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                //Log.e("Response is",new Gson().toJson(response));


                if(response.body().getStatus()==200) {

                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<SignModel>() {
                    }.getType();
                    SignModel user_model=new Gson().fromJson(headerString, listType);
                    sessionManager.setUserdata(getApplicationContext(),user_model);
                    Intent i = new Intent(LoginActivity.this, Phone_email_verificationActivity.class);
                    startActivity(i);
                    finish();
                }
                else {

                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(LoginActivity.this,mMainLayout);
    }
}