package com.contactninja.Auth;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.contactninja.Auth.PlanTyep.PlanType_Screen;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.Model.UservalidateModel;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitApiClient;
import com.contactninja.retrofit.RetrofitApiInterface;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Phone_email_verificationActivity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {
    public static RetrofitApiInterface apiService;
    public String fcmToken = "";
    TextView btn_getStarted, iv_invalid, tv_welcome;
    SessionManager sessionManager;
    LinearLayout layout_phonenumber, layout_email;
    EditText edit_Mobile, edit_email;
    String u_mobile, u_email;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    LoadingDialog loadingDialog;
    RetrofitCalls retrofitCalls;
    String login_type;
    CountryCodePicker ccp_id;
    RelativeLayout mMainLayout;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private BroadcastReceiver mNetworkReceiver;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_email_verification);
        mNetworkReceiver = new ConnectivityReceiver();
        initUI();
        Global.checkConnectivity(Phone_email_verificationActivity.this, mMainLayout);
        mAuth = FirebaseAuth.getInstance();
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
        sessionManager = new SessionManager(this);
        loadingDialog = new LoadingDialog(this);
        check_login_type();
        retrofitCalls = new RetrofitCalls();
        
        apiService = RetrofitApiClient.getClient().create(RetrofitApiInterface.class);
        firebase();
        enterPhoneNumber();
    }
    
    private void enterPhoneNumber() {
        edit_Mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String countryCode = ccp_id.getSelectedCountryCodeWithPlus();
                String phoneNumber = edit_Mobile.getText().toString().trim();
                if (countryCode.length() > 0 && phoneNumber.length() > 0) {
                    if (Global.isValidPhoneNumber(phoneNumber)) {
                        boolean status = validateUsing_libphonenumber(countryCode, phoneNumber);
                        if (status) {
                            iv_invalid.setText("");
                        } else {
                            iv_invalid.setText(getResources().getString(R.string.invalid_phone));
                        }
                    } else {
                        iv_invalid.setText(getResources().getString(R.string.invalid_phone));
                    }
                } else {
                    //Toast.makeText(getApplicationContext(), "Country Code and Phone Number is required", Toast.LENGTH_SHORT).show();
                }
                
            }
            
            @Override
            public void afterTextChanged(Editable s) {
            
            }
        });
    }
    
    private boolean validateUsing_libphonenumber(String countryCode, String phNumber) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.createInstance(getApplicationContext());
        String isoCode = phoneNumberUtil.getRegionCodeForCountryCode(Integer.parseInt(countryCode));
        Phonenumber.PhoneNumber phoneNumber = null;
        try {
            phoneNumber = phoneNumberUtil.parse(phNumber, isoCode);
            
            
            boolean isValid = phoneNumberUtil.isValidNumber(phoneNumber);
            if (isValid) {
                String internationalFormat = phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
                return true;
            } else {
                return false;
            }
        } catch (NumberParseException e) {
            System.err.println(e);
        }
        return false;
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
                        sessionManager.setFcm_Token(fcmToken);
                    }
                });
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                //Log.e(TAG, "onVerificationCompleted:" + credential);
                loadingDialog.cancelLoading();
            }
            
            @Override
            public void onVerificationFailed(FirebaseException e) {
                //   Log.e(TAG, "onVerificationFailed", e);
                loadingDialog.cancelLoading();
                
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.please_try_again_later), Toast.LENGTH_LONG).show();
                
            }
            
            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                loadingDialog.cancelLoading();
                String countryCode = ccp_id.getSelectedCountryCodeWithPlus();
                Intent intent = new Intent(getApplicationContext(), VerificationActivity.class);
                intent.putExtra("v_id", verificationId);
                intent.putExtra("mobile", edit_Mobile.getText().toString());
                intent.putExtra("countrycode", countryCode);
                intent.putExtra("f_name", "");
                intent.putExtra("l_name", "");
                intent.putExtra("email", edit_email.getText().toString().trim());
                intent.putExtra("login_type", "PHONE");
                intent.putExtra("activity_flag", "login");
                intent.putExtra("referred_by", "");
                startActivity(intent);
                finish();
                
                
            }
        };
    }
    
    private void check_login_type() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        try {
            String type = bundle.getString("login_type");
            if (type.equals("PHONE")) {
                tv_welcome.setText(getResources().getString(R.string.welcome_Email));
                layout_email.setVisibility(View.VISIBLE);
                layout_phonenumber.setVisibility(View.GONE);
                u_email = edit_email.getText().toString().trim();
                
                
            } else {
                tv_welcome.setText(getResources().getString(R.string.welcome_phone));
                layout_email.setVisibility(View.GONE);
                layout_phonenumber.setVisibility(View.VISIBLE);
                u_mobile = edit_Mobile.getText().toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    private void check_login_type1() {
        
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String login_type = bundle.getString("login_type");
        if (login_type.equals("PHONE")) {
            u_email = edit_email.getText().toString().trim();
            if (u_email.equals("")) {
                iv_invalid.setText(getResources().getString(R.string.invalid_email));
            } else if (!u_email.matches(emailPattern)) {
                iv_invalid.setText(getResources().getString(R.string.invalid_email));
                
            } else {
                // login_type="EMAIL";
                try {
                    if (Global.isNetworkAvailable(Phone_email_verificationActivity.this, mMainLayout)) {
                        EmailUpdate();
                    }
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
                    if (Global.isNetworkAvailable(Phone_email_verificationActivity.this, mMainLayout)) {
                        PhoneUpdate();
                    }
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
        ccp_id = findViewById(R.id.ccp_id);
        tv_welcome = findViewById(R.id.tv_welcome);
        
    }
    
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_getStarted) {
            check_login_type1();
        }
    }
    
    
    private void EmailUpdate() throws JSONException {
        // Log.e("Email","Yes");
        
        Global.getInstance().setConnectivityListener(this);
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
        paramObject.addProperty("email", edit_email.getText().toString().trim());
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("team_id", team_id);
        paramObject.addProperty("update_type", type);
        paramObject.addProperty("user_id", user_id);
        obj.add("data", paramObject);
        
        retrofitCalls.EmailNumberUpdate(sessionManager, obj, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(Phone_email_verificationActivity.this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                if (response.body().getHttp_status() == 200) {
                    loadingDialog.cancelLoading();
                    sessionManager.Email_Update();
                    loadingDialog.cancelLoading();
                    startActivity(new Intent(getApplicationContext(), PlanType_Screen.class));
                    finish();
                    //Global.Messageshow(getApplicationContext(), mMainLayout, response.body().getMessage(), true);
                    
                } else if (response.body().getHttp_status() == 404) {
                    loadingDialog.cancelLoading();
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    //   Log.e("String is",headerString);
                    Type listType = new TypeToken<UservalidateModel>() {
                    }.getType();
                    UservalidateModel user_model = new Gson().fromJson(headerString, listType);
                    Global.Messageshow(getApplicationContext(), mMainLayout, user_model.getEmail().get(0), false);
                    
                } else {
                    loadingDialog.cancelLoading();
                    try {
                        Gson gson = new Gson();
                        String headerString = gson.toJson(response.body().getData());
                        //  Log.e("String is",headerString);
                        Type listType = new TypeToken<UservalidateModel>() {
                        }.getType();
                        UservalidateModel user_model = new Gson().fromJson(headerString, listType);
                        if (login_type.equals("EMAIL")) {
                            Global.Messageshow(getApplicationContext(), mMainLayout, user_model.getEmail().get(0), false);
                        } else {
                            Global.Messageshow(getApplicationContext(), mMainLayout, user_model.getContact_number().get(0), false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                    
                }
            }
            
            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });
    }
    
    
    private void PhoneUpdate() throws JSONException {
        // Log.e("Phone","Yes");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String type = bundle.getString("login_type");
        if (type.equals("PHONE")) {
            type = "EMAIL";
            
        } else {
            
            type = "PHONE";
        }
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(this);
        
        
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());
        
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("contact_number", ccp_id.getSelectedCountryCodeWithPlus() + edit_Mobile.getText().toString().trim());
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("team_id", team_id);
        paramObject.addProperty("update_type", type);
        paramObject.addProperty("user_id", user_id);
        obj.add("data", paramObject);
        // Log.e("Data is",new Gson().toJson(obj));
        
        retrofitCalls.EmailNumberUpdate(sessionManager, obj, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(Phone_email_verificationActivity.this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                if (response.body().getHttp_status() == 200) {
                    loadingDialog.cancelLoading();
                    
                    sessionManager.Email_Update();
                    startActivity(new Intent(getApplicationContext(), PlanType_Screen.class));
                    finish();
                } else if (response.body().getHttp_status() == 404) {
                    loadingDialog.cancelLoading();
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    //  Log.e("String is",headerString);
                    Type listType = new TypeToken<UservalidateModel>() {
                    }.getType();
                    UservalidateModel user_model = new Gson().fromJson(headerString, listType);
                    Global.Messageshow(getApplicationContext(), mMainLayout, user_model.getContact_number().get(0), false);
                    
                } else {
                    loadingDialog.cancelLoading();
                    try {
                        Gson gson = new Gson();
                        String headerString = gson.toJson(response.body().getData());
                        //  Log.e("String is",headerString);
                        Type listType = new TypeToken<UservalidateModel>() {
                        }.getType();
                        UservalidateModel user_model = new Gson().fromJson(headerString, listType);
                        Intent intent = getIntent();
                        Bundle bundle = intent.getExtras();
                        String type = bundle.getString("login_type");
                        if (type.equals("EMAIL")) {
                            Global.Messageshow(getApplicationContext(), mMainLayout, user_model.getEmail().get(0), false);
                        } else {
                            Global.Messageshow(getApplicationContext(), mMainLayout, user_model.getContact_number().get(0), false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                    
                }
            }
            
            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });
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
    
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Phone_email_verificationActivity.this, mMainLayout);
    }
    
    @SuppressLint("ObsoleteSdkInt")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }
    
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
    
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }
}


