package com.contactninja.Auth;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.contactninja.Auth.PlanTyep.PlanType_Screen;
import com.contactninja.MainActivity;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.Model.UservalidateModel;
import com.contactninja.R;
import com.contactninja.Setting.WebActivity;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.HintRequest;
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

@SuppressLint("UnknownNullness,SyntheticAccessor")
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {
    private static final String TAG = "LoginActivity";
    private static final int CREDENTIAL_PICKER_REQUEST = 1;
    public String fcmToken = "";
    TextView btn_chnage_phone_email, btn_login, iv_invalid, tv_signUP, tv_terms;
    boolean is_PhoneShow = true;
    LinearLayout layout_email, layout_phonenumber, email_password;
    CountryCodePicker ccp_id;
    EditText edit_email, edit_Mobile, edit_password;
    CoordinatorLayout mMainLayout;
    LoadingDialog loadingDialog;
    RetrofitCalls retrofitCalls;
    String Login_type = "PHONE", password = "";
    SessionManager sessionManager;
    TextView btn_chnage_forgot, iv_password_invalid;
    RelativeLayout forgot_password;
    ImageView iv_showPassword;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private BroadcastReceiver mNetworkReceiver;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mNetworkReceiver = new ConnectivityReceiver();
        mAuth = FirebaseAuth.getInstance();
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
        loadingDialog = new LoadingDialog(LoginActivity.this);
        initUI();
        retrofitCalls = new RetrofitCalls(this);
        firebase();
        Global.checkConnectivity(LoginActivity.this, mMainLayout);
        btn_chnage_forgot.setOnClickListener(this);
        enterPhoneNumber();
        
        HintRequest hintRequest = new HintRequest.Builder()
                                          .setPhoneNumberIdentifierSupported(true)
                                          .build();
        
        
        PendingIntent intent = Credentials.getClient(this).getHintPickerIntent(hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(), CREDENTIAL_PICKER_REQUEST, null, 0, 0, 0, new Bundle());
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
        
    }
    
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == RESULT_OK) {
            // Obtain the phone number from the result
            Credential credentials = data.getParcelableExtra(Credential.EXTRA_KEY);
            edit_Mobile.setText(credentials.getId().substring(3));
        }
        
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
    
    @Override
    protected void onResume() {
        super.onResume();
        Global.getInstance().setConnectivityListener(this);
        
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
                //    Log.e(TAG, "onVerificationCompleted:" + credential);
                loadingDialog.cancelLoading();
            }
            
            @Override
            public void onVerificationFailed(FirebaseException e) {
                // Log.e(TAG, "onVerificationFailed", e);
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
                intent.putExtra("email", edit_email.getText().toString());
                intent.putExtra("login_type", Login_type);
                intent.putExtra("activity_flag", "login");
                intent.putExtra("referred_by", "");
                startActivity(intent);
                
                
            }
        };
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
    
    private void initUI() {
        sessionManager = new SessionManager(this);
        ccp_id = findViewById(R.id.ccp_id);
        iv_showPassword = findViewById(R.id.iv_showPassword);
        edit_email = findViewById(R.id.edit_email);
        edit_Mobile = findViewById(R.id.edit_Mobile);
        btn_login = findViewById(R.id.btn_login);
        iv_invalid = findViewById(R.id.iv_invalid);
        tv_signUP = findViewById(R.id.tv_signUP);
        mMainLayout = findViewById(R.id.mMainLayout);
        edit_password = findViewById(R.id.edit_password);
        layout_phonenumber = findViewById(R.id.layout_phonenumber);
        layout_email = findViewById(R.id.layout_email);
        btn_chnage_phone_email = findViewById(R.id.btn_chnage_phone_email);
        btn_chnage_phone_email.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        tv_signUP.setOnClickListener(this);
        btn_chnage_forgot = findViewById(R.id.btn_chnage_forgot);
        iv_password_invalid = findViewById(R.id.iv_password_invalid);
        forgot_password = findViewById(R.id.forgot_password);
        email_password = findViewById(R.id.email_password);
        tv_terms = findViewById(R.id.tv_terms);
        
        iv_showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    iv_showPassword.setSelected(false);
                    //hide password
                    iv_showPassword.setImageResource(R.drawable.ic_visibility_off);
                    edit_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    
                } else {
                    iv_showPassword.setSelected(true);
                    //show password
                    iv_showPassword.setImageResource(R.drawable.ic_visibility);
                    edit_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                edit_password.setSelection(edit_password.getText().length());
            }
        });
        
        
        /**
         * set URL in terms and condition */
        
        String text = "I have read and agree to Contact Ninja’s Terms & Conditions";
        SpannableString spannableString = new SpannableString(text);
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(getApplicationContext(), WebActivity.class);
                intent.putExtra("WebUrl", Global.conditions);
                startActivity(intent);
            }
        };
        spannableString.setSpan(clickableSpan1, 41, 59, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_terms.setText(spannableString);
        try {
            tv_terms.setMovementMethod(LinkMovementMethod.getInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_chnage_phone_email:
                iv_invalid.setVisibility(View.GONE);
                iv_password_invalid.setVisibility(View.GONE);
                if (is_PhoneShow) {
                    layout_email.setVisibility(View.VISIBLE);
                    layout_phonenumber.setVisibility(View.GONE);
                    email_password.setVisibility(View.VISIBLE);
                    forgot_password.setVisibility(View.VISIBLE);
                    is_PhoneShow = false;
                    btn_chnage_phone_email.setText(getResources().getString(R.string.or_phone));
                } else {
                    layout_phonenumber.setVisibility(View.VISIBLE);
                    layout_email.setVisibility(View.GONE);
                    email_password.setVisibility(View.GONE);
                    forgot_password.setVisibility(View.GONE);
                    is_PhoneShow = true;
                    btn_chnage_phone_email.setText(getResources().getString(R.string.or_email));
                }
                break;
            case R.id.btn_login:
                if (checkVelidaction()) {
                    sessionManager.setlogin_type(Login_type);
                    
                    if (Login_type.equals("EMAIL")) {
                        /**
                         * if login with email */
                        try {
                            iv_password_invalid.setVisibility(View.GONE);
                            iv_invalid.setVisibility(View.GONE);
                            if (Global.isNetworkAvailable(LoginActivity.this, mMainLayout)) {
                                Uservalidate();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        
                        try {
                            iv_invalid.setVisibility(View.GONE);
                            if (Global.isNetworkAvailable(LoginActivity.this, mMainLayout)) {
                                Uservalidate();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        
                        
                    }
                    
                }
                
                
                break;
            case R.id.tv_signUP:
                
                startActivity(new Intent(getApplicationContext(), SignupActivity.class));
                // finish();
                
                break;
            case R.id.btn_chnage_forgot:
                startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
                
                break;
        }
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
            Login_type = "PHONE";
            if (edit_Mobile.getText().toString().trim().equals("")) {
                iv_invalid.setText(getResources().getString(R.string.invalid_phone));
            } else {
                String countryCode = ccp_id.getSelectedCountryCodeWithPlus();
                String phoneNumber = edit_Mobile.getText().toString().trim();
                if (countryCode.length() > 0 && phoneNumber.length() > 0) {
                    if (Global.isValidPhoneNumber(phoneNumber)) {
                        boolean status = validateUsing_libphonenumber(countryCode, phoneNumber);
                        if (status) {
                            iv_invalid.setText("");
                            iv_invalid.setVisibility(View.VISIBLE);
                            return true;
                        } else {
                            iv_invalid.setText(getResources().getString(R.string.invalid_phone));
                            iv_invalid.setVisibility(View.VISIBLE);
                        }
                    } else {
                        iv_invalid.setText(getResources().getString(R.string.invalid_phone));
                        iv_invalid.setVisibility(View.VISIBLE);
                    }
                }
            }
        } else {
            Login_type = "EMAIL";
            if (edit_email.getText().toString().trim().equals("")) {
                iv_invalid.setText(getResources().getString(R.string.invalid_email));
                iv_invalid.setVisibility(View.VISIBLE);
            } else if (Global.emailValidator(edit_email.getText().toString().trim())) {
                iv_invalid.setVisibility(View.GONE);
                if (edit_password.getText().toString().trim().equals("")) {
                    iv_password_invalid.setText(getResources().getString(R.string.invalid_password));
                    iv_password_invalid.setVisibility(View.VISIBLE);
                } else {
                    return true;
                }
            } else {
                iv_invalid.setText(getResources().getString(R.string.invalid_email));
                iv_invalid.setVisibility(View.VISIBLE);
            }
            
        }
        return false;
    }
    
    
    public void LoginData() {
        password = edit_password.getText().toString();
        
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("firebase_fcm_token", SessionManager.getFcm_Token(getApplicationContext()));
        paramObject.addProperty("email", edit_email.getText().toString());
        paramObject.addProperty("login_type", Login_type);
        paramObject.addProperty("password", password);
        
        obj.add("data", paramObject);
        retrofitCalls.LoginUser(sessionManager, obj, loadingDialog, Global.getVersionname(LoginActivity.this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                //Log.e("Response is",new Gson().toJson(response));
                
                loadingDialog.cancelLoading();
                
                if (response.body().getHttp_status() == 200) {
                    
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<SignResponseModel>() {
                    }.getType();
                    SignResponseModel user_model = new Gson().fromJson(headerString, listType);
                    SessionManager.setUserdata(getApplicationContext(), user_model);
                    sessionManager.setRefresh_token(user_model.getRefreshToken());
                    sessionManager.setAccess_token(user_model.getTokenType() + " " + user_model.getAccessToken());
                    try {
                        if (user_model.getUser().getContactNumber().equals("")) {
                            Intent intent = new Intent(getApplicationContext(), Phone_email_verificationActivity.class);
                            intent.putExtra("login_type", Login_type);
                            startActivity(intent);
                        } else if (user_model.getUser().getUserprofile().getPurchased_planid().equals("") ||
                                           user_model.getUser().getUserprofile().getPurchased_planid().equals("null")) {
                            startActivity(new Intent(getApplicationContext(), PlanType_Screen.class));
                        } else {
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        finish();
                    } catch (Exception e) {
                        Intent intent = new Intent(getApplicationContext(), Phone_email_verificationActivity.class);
                        intent.putExtra("login_type", Login_type);
                        startActivity(intent);
                        finish();
                    }
                    
                    
                } else if (response.body().getHttp_status() == 404) {
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    // Log.e("String is",response.body().getMessage());
                    Type listType = new TypeToken<UservalidateModel>() {
                    }.getType();
                    try {
                        //Log.e("Login_type",Login_type);
                        if (Login_type.equals("EMAIL")) {
                            if (response.body().getMessage().toString().equals("Invalid Password.")) {
                                iv_password_invalid.setText(getResources().getString(R.string.invalid_password));
                                iv_password_invalid.setVisibility(View.VISIBLE);
                                //Global.Messageshow(getApplicationContext(), mMainLayout, response.body().getMessage(), false);
                                
                            } else {
                                iv_invalid.setVisibility(View.VISIBLE);
                            }
                        }
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Global.Messageshow(getApplicationContext(), mMainLayout, headerString, false);
                    
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
        Global.checkConnectivity(LoginActivity.this, mMainLayout);
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
    
    
    private void Uservalidate() throws JSONException {
        
        String txt_email = edit_email.getText().toString();
        String txt_contect = edit_Mobile.getText().toString();
        loadingDialog.showLoadingDialog();
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        if (Login_type.equals("EMAIL")) {
            paramObject.addProperty("email", txt_email);
        } else {
            paramObject.addProperty("contact_number", ccp_id.getSelectedCountryCodeWithPlus() + txt_contect);
        }
        paramObject.addProperty("first_name", "");
        paramObject.addProperty("last_name", "");
        paramObject.addProperty("login_type", Login_type);
        obj.add("data", paramObject);
        retrofitCalls.Userexistcheck(sessionManager, obj, loadingDialog, Global.getVersionname(LoginActivity.this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                
                if (response.body().getHttp_status() == 200) {
                    
                    if (!Login_type.equals("EMAIL")) {
                        /**
                         * verification phone */
                        VerifyPhone(edit_Mobile.getText().toString());
                    } else {
                        if (Global.isNetworkAvailable(LoginActivity.this, mMainLayout)) {
                            /**
                             * login with email and password */
                            LoginData();
                        }
                    }
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
