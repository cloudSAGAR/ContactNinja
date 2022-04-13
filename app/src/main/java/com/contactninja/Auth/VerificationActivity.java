package com.contactninja.Auth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.chaos.view.PinView;
import com.contactninja.Auth.PlanTyep.PlanType_Screen;
import com.contactninja.MainActivity;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.OTP_Receiver;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class VerificationActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private static final String TAG = "VerificationActivity";
    public static CountDownTimer countDownTimer;
    private final long mLastClickTime = 0;
    public String fcmToken = "";
    PinView otp_pinview;
    TextView verfiy_button, resend_txt, tc_wrong, tvTimer;
    String phoneAuthCredential;
    String mobile_number = "", countrycode = "", v_id = "";
    int second;
    LoadingDialog loadingDialog;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    String first_name = "", last_name = "", email_address = "", login_type = "", activity_flag = "";
    CoordinatorLayout mMainLayout;
    String referred_by = "";
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private BroadcastReceiver mNetworkReceiver;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        mNetworkReceiver = new ConnectivityReceiver();
        mAuth = FirebaseAuth.getInstance();
        sessionManager = new SessionManager(this);
        IntentUI();
        Global.checkConnectivity(VerificationActivity.this, mMainLayout);
        Intent getIntent = getIntent();
        Bundle getbunBundle = getIntent.getExtras();
        try {
            first_name = getbunBundle.getString("f_name");
            last_name = getbunBundle.getString("l_name");
            email_address = getbunBundle.getString("email");
            login_type = getbunBundle.getString("login_type");
            mobile_number = getbunBundle.getString("mobile");
            v_id = getbunBundle.getString("v_id");
            countrycode = getbunBundle.getString("countrycode");
            activity_flag = getbunBundle.getString("activity_flag");
            referred_by = getbunBundle.getString("referred_by");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        retrofitCalls = new RetrofitCalls();
        EnableRuntimePermission();
        firebase();
        showTimer();
        loadingDialog = new LoadingDialog(this);
        verfiy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                String pin_text = otp_pinview.getText().toString();
                if (pin_text.equals("")) {
                    tc_wrong.setVisibility(View.VISIBLE);
                } else {
                    countDownTimer.cancel();
                    Global.hideKeyboard(VerificationActivity.this);
                    tc_wrong.setVisibility(View.GONE);
                    loadingDialog.showLoadingDialog();
                    
                    tc_wrong.setVisibility(View.GONE);
                    loadingDialog.showLoadingDialog();
                    try {
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(v_id, otp_pinview.getText().toString());
                        signInWithCredential(credential);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                
            }
        });
        resend_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log.e("On Resend ", "call");
                VerifyPhone(mobile_number.trim());
                showTimer();
                resend_txt.setVisibility(View.GONE);
                tvTimer.setVisibility(View.VISIBLE);
                verfiy_button.setEnabled(true);
                //  firebase();
            }
        });
        new OTP_Receiver().setEditText(otp_pinview);
        otp_pinview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            
            }
            
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                
                if (charSequence.toString().length() == 6) {
                    countDownTimer.cancel();
                    Global.hideKeyboard(VerificationActivity.this);
                    tc_wrong.setVisibility(View.GONE);
                    loadingDialog.showLoadingDialog();
                    try {
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(v_id, otp_pinview.getText().toString());
                        signInWithCredential(credential);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    verfiy_button.setEnabled(false);
                    
                } else {
                    verfiy_button.setEnabled(true);
                    
                }
                
                
            }
            
            @Override
            public void afterTextChanged(Editable editable) {
            
            }
        });
        
    }
    
    
    public void VerifyPhone(String phoneNumber) {
        
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                                           .setPhoneNumber(countrycode + phoneNumber)       // Phone number to verify
                                           .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                           .setActivity(this)                 // Activity (for callback binding)
                                           .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                           .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        
    }
    
    
    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        loadingDialog.cancelLoading();
                        if (task.isSuccessful()) {
                            tc_wrong.setVisibility(View.GONE);
                            // sessionManager.login();
                            if (activity_flag.equals("login")) {
                                try {
                                    if (Global.isNetworkAvailable(VerificationActivity.this, mMainLayout)) {
                                        LoginData();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    if (Global.isNetworkAvailable(VerificationActivity.this, mMainLayout)) {
                                        SignAPI();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            
                            
                        } else {
                            tc_wrong.setVisibility(View.VISIBLE);
                            Toast.makeText(VerificationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingDialog.cancelLoading();
            }
        });
    }
    
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i(TAG, "signInWithCredential:success");
                            
                            FirebaseUser user = task.getResult().getUser();
                            SignResponseModel user_data = SessionManager.getGetUserdata(getApplicationContext());
                            if (!sessionManager.isEmail_Update()) {
                                Intent intent = new Intent(getApplicationContext(), Phone_email_verificationActivity.class);
                                intent.putExtra("login_type", login_type);
                                startActivity(intent);
                                finish();
                            } else if (user_data.getUser().getUserprofile().getPurchased_planid().equals("") ||
                                               user_data.getUser().getUserprofile().getPurchased_planid().equals("null")) {
                                startActivity(new Intent(getApplicationContext(), PlanType_Screen.class));
                                finish();
                            } else {
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }
                            
                            
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        otp_pinview.setText("");
                    }
                });
    }
    
    private void IntentUI() {
        otp_pinview = findViewById(R.id.otp_pinview);
        verfiy_button = findViewById(R.id.verfiy_button);
        resend_txt = findViewById(R.id.resend_txt);
        tc_wrong = findViewById(R.id.tc_wrong);
        tvTimer = findViewById(R.id.tvTimer);
        mMainLayout = findViewById(R.id.mMainLayout);
    }
    
    public void EnableRuntimePermission() {
        
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
            
            }
            
            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                //Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
            
            
        };
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.RECEIVE_SMS)
                .check();
        
        //startActivity(new Intent(getApplicationContext(), VerificationActivity.class));
        
    }
    
    
    private void firebase() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            //  Log.e("FCM registration failed", task.getException() + "");
                            return;
                        }
                        
                        fcmToken = task.getResult();
                    }
                });
        // Initialize phone auth callbacks  [START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                //  Log.e("FCM registration failed", credential + "");
                
            }
            
            @Override
            public void onVerificationFailed(FirebaseException e) {
                //   Log.e(TAG, "onVerificationFailed", e);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.please_try_again_later), Toast.LENGTH_LONG).show();
                
            }
            
            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                
                //     Log.e("Code is Send", "Yes");
                
                v_id = verificationId;
                second = -1;
                showTimer();
            }
            
        };
    }
    
    public void showTimer() {
        // Log.e("Show TImmer", "Yes");
        countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @SuppressLint({"NewApi", "DefaultLocale"})
            @Override
            public void onTick(long millisUntilFinished) {
                second++;
                tvTimer.setVisibility(View.VISIBLE);
                resend_txt.setVisibility(View.GONE);
                tvTimer.setText(String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }
            
            public void onFinish() {
                countDownTimer.cancel();
                tvTimer.setText("00:00");
                tvTimer.setVisibility(View.GONE);
                resend_txt.setVisibility(View.VISIBLE);
            }
        };
        countDownTimer.start();
    }
    
    private void SignAPI() throws JSONException {
        Global.getInstance().setConnectivityListener(this);
        loadingDialog.showLoadingDialog();
        String otp = otp_pinview.getText().toString();
        
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("firebase_fcm_token", SessionManager.getFcm_Token(getApplicationContext()));
        paramObject.addProperty("first_name", first_name);
        paramObject.addProperty("last_name", last_name);
        paramObject.addProperty("email", email_address);
        paramObject.addProperty("contact_number", countrycode + mobile_number);
        paramObject.addProperty("login_type", login_type);
        paramObject.addProperty("otp", otp);
        paramObject.addProperty("referred_by", referred_by);
        obj.add("data", paramObject);
        retrofitCalls.SignUp_user(sessionManager, obj, loadingDialog, Global.getVersionname(VerificationActivity.this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                //     Log.e("Response is", new Gson().toJson(response.body()));
                if (response.body().getHttp_status() == 200) {
                    
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<SignResponseModel>() {
                    }.getType();
                    SignResponseModel user_model = new Gson().fromJson(headerString, listType);
                    SessionManager.setUserdata(getApplicationContext(), user_model);
                    sessionManager.setRefresh_token(user_model.getRefreshToken());
                    sessionManager.setAccess_token(user_model.getTokenType() + " " + user_model.getAccessToken());
                    if (login_type.equals("EMAIL")) {
                        
                        try {
                            if (user_model.getUser().getContactNumber().equals("")) {
                                Intent intent = new Intent(getApplicationContext(), Phone_email_verificationActivity.class);
                                intent.putExtra("login_type", login_type);
                                startActivity(intent);
                            } else {
                                if (user_model.getUser().getUserprofile().getPurchased_planid().equals("") ||
                                            user_model.getUser().getUserprofile().getPurchased_planid().equals("null")) {
                                    startActivity(new Intent(getApplicationContext(), PlanType_Screen.class));
                                } else {
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                }
                            }
                            finish();
                        } catch (Exception e) {
                            Intent intent = new Intent(getApplicationContext(), Phone_email_verificationActivity.class);
                            intent.putExtra("login_type", login_type);
                            startActivity(intent);
                            finish();
                        }
                        
                    } else if (login_type.equals("PHONE")) {
                        
                        try {
                            if (user_model.getUser().getEmail().equals("")) {
                                Intent intent = new Intent(getApplicationContext(), Phone_email_verificationActivity.class);
                                intent.putExtra("login_type", login_type);
                                startActivity(intent);
                            } else {
                                if (user_model.getUser().getUserprofile().getPurchased_planid().equals("") ||
                                            user_model.getUser().getUserprofile().getPurchased_planid().equals("null")) {
                                    startActivity(new Intent(getApplicationContext(), PlanType_Screen.class));
                                } else {
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                }
                            }
                            finish();
                        } catch (Exception e) {
                            Intent intent = new Intent(getApplicationContext(), Phone_email_verificationActivity.class);
                            intent.putExtra("login_type", login_type);
                            startActivity(intent);
                            finish();
                        }
                        
                    } else if (user_model.getUser().getEmail().equals("") || user_model.getUser().getContactNumber().equals("")) {
                        Intent intent = new Intent(getApplicationContext(), Phone_email_verificationActivity.class);
                        intent.putExtra("login_type", login_type);
                        startActivity(intent);
                        finish();
                    } else if (user_model.getUser().getUserprofile().getPurchased_planid().equals("") ||
                                       user_model.getUser().getUserprofile().getPurchased_planid().equals("null")) {
                        startActivity(new Intent(getApplicationContext(), PlanType_Screen.class));
                        finish();
                    } else {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                    
                }
            }
            
            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                
            }
        });
        
    }
    
    public void LoginData() throws JSONException {
        // Log.e("Code is", countrycode);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("firebase_fcm_token", SessionManager.getFcm_Token(getApplicationContext()));
        paramObject.addProperty("email", email_address);
        paramObject.addProperty("contact_number", countrycode + mobile_number);
        paramObject.addProperty("login_type", login_type);
        paramObject.addProperty("otp", otp_pinview.getText().toString());
        obj.add("data", paramObject);
        retrofitCalls.LoginUser(sessionManager, obj, loadingDialog, Global.getVersionname(VerificationActivity.this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                // Log.e("Response is",new Gson().toJson(response));
                loadingDialog.cancelLoading();
                
                
                if (response.body().getHttp_status() == 200) {
                    
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<SignResponseModel>() {
                    }.getType();
                    
                    
                    // Log.e("Reponse is",gson.toJson(response.body().getData()));
                    SignResponseModel user_model = new Gson().fromJson(headerString, listType);
                    SessionManager.setUserdata(getApplicationContext(), user_model);
                    sessionManager.setRefresh_token(user_model.getRefreshToken());
                    sessionManager.setAccess_token(user_model.getTokenType() + " " + user_model.getAccessToken());
                    try {
                        
                        if (user_model.getUser().getEmail().equals("") || user_model.getUser().getContactNumber().equals("")) {
                            Intent intent = new Intent(getApplicationContext(), Phone_email_verificationActivity.class);
                            intent.putExtra("login_type", login_type);
                            startActivity(intent);
                            finish();
                        } else if (user_model.getUser().getUserprofile().getPurchased_planid().equals("") ||
                                           user_model.getUser().getUserprofile().getPurchased_planid().equals("null")) {
                            startActivity(new Intent(getApplicationContext(), PlanType_Screen.class));
                            finish();
                        } else {
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                    } catch (Exception e) {
                        Intent intent = new Intent(getApplicationContext(), Phone_email_verificationActivity.class);
                        intent.putExtra("login_type", login_type);
                        startActivity(intent);
                        finish();
                    }
                    
                    
                    //  Global.Messageshow(getApplicationContext(), mMainLayout, response.body().getMessage(), true);
                    
                    
                } else {
                    verfiy_button.setEnabled(false);
                    Global.Messageshow(getApplicationContext(), mMainLayout, response.body().getMessage(), false);
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
        Global.checkConnectivity(VerificationActivity.this, mMainLayout);
        
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