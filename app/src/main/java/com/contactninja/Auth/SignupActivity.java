package com.contactninja.Auth;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.contactninja.Model.UservalidateModel;
import com.contactninja.R;
import com.contactninja.Setting.WebActivity;
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
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class SignupActivity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {
    private static final String TAG = "SignupActivity";
    public static RetrofitApiInterface apiService;
    public String fcmToken = "";
    TextView btn_chnage_phone_email, btn_signup, iv_invalid, tv_Login,tv_terms;
    boolean lay_PhoneShow = true;
    LinearLayout layout_email, layout_phonenumber, layout_code;
    CountryCodePicker ccp_id;
    EditText edit_email, edit_Mobile, edit_First, edit_Last, edit_code;
    CoordinatorLayout mMainLayout;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    String first_name, last_name, mobile_number, email_address, login_type = "PHONE", referred_by = "", Otp = "";
    Integer status = 0;
    SessionManager sessionManager;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    CheckBox select_code;
    View view_layout;
    private BroadcastReceiver mNetworkReceiver;

    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mNetworkReceiver = new ConnectivityReceiver();
        retrofitCalls = new RetrofitCalls();
        mAuth = FirebaseAuth.getInstance();
        loadingDialog = new LoadingDialog(SignupActivity.this);
        initUI();
        Global.checkConnectivity(SignupActivity.this, mMainLayout);
        apiService = RetrofitApiClient.getClient().create(RetrofitApiInterface.class);
        //showAlertDialogButtonClicked();
        enterPhoneNumber();
        firebase();
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
        // Initialize phone auth callbacks  [START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                loadingDialog.cancelLoading();
               // Log.e(TAG, "onVerificationCompleted:" + credential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                loadingDialog.cancelLoading();
              //  Log.e(TAG, "onVerificationFailed", e);
                Toast.makeText(getApplicationContext(), "VERIFY FAILED", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {

                loadingDialog.cancelLoading();
                first_name = edit_First.getText().toString().trim();
                last_name = edit_Last.getText().toString().trim();
                mobile_number = edit_Mobile.getText().toString().trim();
                email_address = edit_email.getText().toString();
                referred_by = "";
                Otp = getRandomNumberString();
                if (first_name.equals("")) {
                    iv_invalid.setText(getResources().getString(R.string.invalid_first_name));
                } else if (last_name.equals("")) {
                    iv_invalid.setText(getResources().getString(R.string.invalid_last_name));
                } else {
                    loadingDialog.cancelLoading();
                    String countryCode = ccp_id.getSelectedCountryCodeWithPlus();
                    Intent intent = new Intent(getApplicationContext(), VerificationActivity.class);
                    intent.putExtra("v_id", verificationId);
                    intent.putExtra("mobile", mobile_number);
                    intent.putExtra("countrycode", countryCode);
                    intent.putExtra("f_name", first_name);
                    intent.putExtra("l_name", last_name);
                    intent.putExtra("email", email_address);
                    intent.putExtra("login_type", login_type);
                    intent.putExtra("activity_flag", "signup");
                    intent.putExtra("referred_by", referred_by);
                    startActivity(intent);
                }


            }
        };
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
        sessionManager = new SessionManager(this);
        ccp_id = findViewById(R.id.ccp_id);
        edit_email = findViewById(R.id.edit_email);
        edit_Mobile = findViewById(R.id.edit_Mobile);
        btn_signup = findViewById(R.id.btn_signup);
        iv_invalid = findViewById(R.id.iv_invalid);
        tv_Login = findViewById(R.id.tv_Login);
        edit_First = findViewById(R.id.edit_First);
        edit_Last = findViewById(R.id.edit_Last);

        layout_phonenumber = findViewById(R.id.layout_phonenumber);
        layout_email = findViewById(R.id.layout_email);
        btn_chnage_phone_email = findViewById(R.id.btn_chnage_phone_email);
        mMainLayout = findViewById(R.id.mMainLayout);
        btn_chnage_phone_email.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
        tv_Login.setOnClickListener(this);
        select_code = findViewById(R.id.select_code);
        layout_code = findViewById(R.id.layout_code);
        view_layout = findViewById(R.id.view_layout);
        edit_code = findViewById(R.id.edit_code);
        select_code.setOnClickListener(this);
        tv_terms=findViewById(R.id.tv_terms);


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
        spannableString.setSpan(clickableSpan1, 41,59, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_terms.setText(spannableString);
        try {
            tv_terms.setMovementMethod(LinkMovementMethod.getInstance());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_chnage_phone_email:
                iv_invalid.setText("");
                if (lay_PhoneShow) {
                    layout_email.setVisibility(View.VISIBLE);
                    layout_phonenumber.setVisibility(View.GONE);
                    lay_PhoneShow = false;
                    btn_chnage_phone_email.setText(getResources().getString(R.string.or_phone));
                } else {
                    layout_phonenumber.setVisibility(View.VISIBLE);
                    layout_email.setVisibility(View.GONE);
                    lay_PhoneShow = true;
                    btn_chnage_phone_email.setText(getResources().getString(R.string.or_email));
                }
                break;
            case R.id.btn_signup:
                if (checkVelidaction()) {

                    try {
                        if(Global.isNetworkAvailable(SignupActivity.this,mMainLayout)) {
                            Uservalidate();
                            sessionManager.setlogin_type(login_type);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // startActivity(new Intent(getApplicationContext(), VerificationActivity.class));
                }


                break;
            case R.id.tv_Login:

                // startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();

                break;
            case R.id.select_code:

                if (select_code.isChecked()) {
                    layout_code.setVisibility(View.VISIBLE);
                    view_layout.setVisibility(View.VISIBLE);
                } else {
                    layout_code.setVisibility(View.INVISIBLE);
                    view_layout.setVisibility(View.INVISIBLE);
                }
                break;
        }
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



        new Handler().postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();
                onBackPressed();
            }
        }, 2000);
    }

    private void Uservalidate() throws JSONException {

        loadingDialog.showLoadingDialog();
        first_name = edit_First.getText().toString().trim();
        last_name = edit_Last.getText().toString().trim();
        mobile_number = edit_Mobile.getText().toString().trim();
        email_address = edit_email.getText().toString();
        Otp = getRandomNumberString();

        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("first_name", first_name);
        paramObject.addProperty("last_name", last_name);
        paramObject.addProperty("email", email_address);
        paramObject.addProperty("contact_number", ccp_id.getSelectedCountryCodeWithPlus()+mobile_number);
        paramObject.addProperty("login_type", login_type);
        paramObject.addProperty("otp", "1231220");
        obj.add("data", paramObject);
        retrofitCalls.Uservalidate(sessionManager, obj, loadingDialog, Global.getVersionname(SignupActivity.this),Global.Device,new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                //Log.e("Response is",new Gson().toJson(response));


                if (response.body().getHttp_status() == 200) {

                    if (!login_type.equals("EMAIL")) {
                        VerifyPhone(edit_Mobile.getText().toString());
                    } else {
                        try {
                            if(Global.isNetworkAvailable(SignupActivity.this,mMainLayout)){
                                SignAPI();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    loadingDialog.cancelLoading();

                    try {
                        Gson gson = new Gson();
                        String headerString = gson.toJson(response.body().getData());
                       // Log.e("String is", headerString);
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



                    /*Log.e("getAccessToken", user_model.getAccessToken());*/

                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();

    }
    private void SignAPI() throws JSONException {
        Global.getInstance().setConnectivityListener(this);

        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("firebase_fcm_token", SessionManager.getFcm_Token(getApplicationContext()));
        paramObject.addProperty("first_name", first_name);
        paramObject.addProperty("last_name", last_name);
        paramObject.addProperty("email", email_address);
        paramObject.addProperty("contact_number", mobile_number);
        paramObject.addProperty("login_type", login_type);
        paramObject.addProperty("otp", "");
        paramObject.addProperty("referred_by", referred_by);
        obj.add("data", paramObject);
        retrofitCalls.SignUp_user(sessionManager, obj, loadingDialog,Global.getVersionname(SignupActivity.this),Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();

                if (response.isSuccessful()) {
                    showAlertDialogButtonClicked();
                    //Toast.makeText(getApplicationContext(),"Your Password In Mail",Toast.LENGTH_SHORT).show();

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

    private boolean checkVelidaction() {
        if (select_code.isChecked()) {

            if (lay_PhoneShow) {

                referred_by = edit_code.getText().toString();
                if (edit_First.getText().toString().trim().equals("")) {
                    iv_invalid.setText(getResources().getString(R.string.invalid_first_name));
                } else if (edit_Last.getText().toString().trim().equals("")) {
                    iv_invalid.setText(getResources().getString(R.string.invalid_last_name));
                } else if (edit_Mobile.getText().toString().trim().equals("")) {
                    iv_invalid.setText(getResources().getString(R.string.invalid_phone));
                } else if (edit_Mobile.getText().length() != 10) {
                    iv_invalid.setText(getResources().getString(R.string.invalid_phone));
                } else if (edit_Mobile.getText().toString().trim().equals("")) {
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
                                if (referred_by.equals("")) {
                                    Global.Messageshow(getApplicationContext(), mMainLayout, getResources().getString(R.string.invalid_code), false);
                                } else {
                                    login_type = "PHONE";
                                    return true;
                                }
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
                referred_by = edit_code.getText().toString();
                if (edit_First.getText().toString().trim().equals("")) {
                    iv_invalid.setText(getResources().getString(R.string.invalid_first_name));
                } else if (edit_Last.getText().toString().trim().equals("")) {
                    iv_invalid.setText(getResources().getString(R.string.invalid_last_name));
                } else if (edit_email.getText().toString().trim().equals("")) {
                    iv_invalid.setText(getResources().getString(R.string.invalid_email));
                    iv_invalid.setVisibility(View.VISIBLE);
                } else if (Global.emailValidator(edit_email.getText().toString().trim())) {
                    iv_invalid.setVisibility(View.GONE);
                    if (referred_by.equals("")) {
                        Global.Messageshow(getApplicationContext(), mMainLayout, getResources().getString(R.string.invalid_code), false);
                    } else {
                        login_type = "EMAIL";
                        return true;
                    }
                } else {
                    iv_invalid.setText(getResources().getString(R.string.invalid_email));
                    iv_invalid.setVisibility(View.VISIBLE);
                }
            }
        } else {
            if (lay_PhoneShow) {


                if (edit_First.getText().toString().trim().equals("")) {
                    iv_invalid.setText(getResources().getString(R.string.invalid_first_name));
                } else if (edit_Last.getText().toString().trim().equals("")) {
                    iv_invalid.setText(getResources().getString(R.string.invalid_last_name));
                } else if (edit_Mobile.getText().toString().trim().equals("")) {
                    iv_invalid.setText(getResources().getString(R.string.invalid_phone));
                } else if (edit_Mobile.getText().length() != 10) {
                    iv_invalid.setText(getResources().getString(R.string.invalid_phone));
                } else if (edit_Mobile.getText().toString().trim().equals("")) {
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
                                login_type = "PHONE";
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
                if (edit_First.getText().toString().trim().equals("")) {
                    iv_invalid.setText(getResources().getString(R.string.invalid_first_name));
                } else if (edit_Last.getText().toString().trim().equals("")) {
                    iv_invalid.setText(getResources().getString(R.string.invalid_last_name));
                } else if (edit_email.getText().toString().trim().equals("")) {
                    iv_invalid.setText(getResources().getString(R.string.invalid_email));
                    iv_invalid.setVisibility(View.VISIBLE);
                } else if (Global.emailValidator(edit_email.getText().toString().trim())) {
                    iv_invalid.setVisibility(View.GONE);
                    login_type = "EMAIL";
                    return true;
                } else {
                    iv_invalid.setText(getResources().getString(R.string.invalid_email));
                    iv_invalid.setVisibility(View.VISIBLE);
                }
            }
        }
        return false;
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(SignupActivity.this, mMainLayout);
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