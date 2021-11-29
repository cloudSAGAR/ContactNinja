package com.intricare.test.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.intricare.test.Model.SignModel;
import com.intricare.test.Model.SignResponseModel;
import com.intricare.test.Model.UservalidateModel;
import com.intricare.test.R;
import com.intricare.test.Utils.Global;
import com.intricare.test.Utils.LoadingDialog;
import com.intricare.test.Utils.SessionManager;
import com.intricare.test.retrofit.ApiResponse;
import com.intricare.test.retrofit.RetrofitApiClient;
import com.intricare.test.retrofit.RetrofitApiInterface;
import com.intricare.test.retrofit.RetrofitCallback;
import com.intricare.test.retrofit.RetrofitCalls;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SignupActivity";
    TextView btn_chnage_phone_email, btn_signup, iv_invalid, tv_Login;
    boolean lay_PhoneShow = true;
    LinearLayout layout_email, layout_phonenumber;
    CountryCodePicker ccp_id;

    EditText edit_email, edit_Mobile,edit_First,edit_Last;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    CoordinatorLayout mMainLayout;
    public String fcmToken = "";
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    public static RetrofitApiInterface apiService;
    String first_name,last_name,mobile_number,email_address,login_type="PHONE",referred_by="",Otp="";
    Integer status=0;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        retrofitCalls = new RetrofitCalls();

        mAuth = FirebaseAuth.getInstance();
        loadingDialog = new LoadingDialog(SignupActivity.this);
        initUI();
        apiService = RetrofitApiClient.getClient().create(RetrofitApiInterface.class);

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
                    }
                });
        // Initialize phone auth callbacks  [START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                loadingDialog.cancelLoading();
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
                loadingDialog.cancelLoading();
                Log.e(TAG, "onVerificationFailed", e);

                Toast.makeText(getApplicationContext(), "VERIFY FAILED", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {

                //loadingDialog.showLoadingDialog();
                first_name=edit_First.getText().toString().trim();
                last_name=edit_Last.getText().toString().trim();
                mobile_number=edit_Mobile.getText().toString().trim();
                email_address=edit_email.getText().toString();
                referred_by="t2q2";
                Otp= getRandomNumberString();
                if (first_name.equals(""))
                {
                    iv_invalid.setText(getResources().getString(R.string.invalid_first_name));
                }
                else if (last_name.equals(""))
                {
                    iv_invalid.setText(getResources().getString(R.string.invalid_last_name));
                }
                else {
                    loadingDialog.cancelLoading();
                    String countryCode = ccp_id.getSelectedCountryCodeWithPlus();
                    Intent intent=new Intent(getApplicationContext(),VerificationActivity.class);
                    intent.putExtra("v_id",verificationId);
                    intent.putExtra("mobile",mobile_number);
                    intent.putExtra("countrycode",countryCode);
                    intent.putExtra("f_name",first_name);
                    intent.putExtra("l_name",last_name);
                    intent.putExtra("email",email_address);
                    intent.putExtra("login_type",login_type);
                    intent.putExtra("activity_flag","signup");
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
        sessionManager=new SessionManager(this);
        ccp_id = findViewById(R.id.ccp_id);
        edit_email = findViewById(R.id.edit_email);
        edit_Mobile = findViewById(R.id.edit_Mobile);
        btn_signup = findViewById(R.id.btn_signup);
        iv_invalid = findViewById(R.id.iv_invalid);
        tv_Login = findViewById(R.id.tv_Login);
        edit_First=findViewById(R.id.edit_First);
        edit_Last=findViewById(R.id.edit_Last);

        layout_phonenumber = findViewById(R.id.layout_phonenumber);
        layout_email = findViewById(R.id.layout_email);
        btn_chnage_phone_email = findViewById(R.id.btn_chnage_phone_email);
        mMainLayout = findViewById(R.id.mMainLayout);
        btn_chnage_phone_email.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
        tv_Login.setOnClickListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_chnage_phone_email:
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
                        Uservalidate();
                        sessionManager.setlogin_type(login_type);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // startActivity(new Intent(getApplicationContext(), VerificationActivity.class));
                }


                break;
            case R.id.tv_Login:

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();

                break;
        }
    }

    private void Uservalidate() throws JSONException {

        loadingDialog.showLoadingDialog();
        first_name=edit_First.getText().toString().trim();
        last_name=edit_Last.getText().toString().trim();
        mobile_number=edit_Mobile.getText().toString().trim();
        email_address=edit_email.getText().toString();
        referred_by="t2q2";
        Otp= getRandomNumberString();
        if (first_name.equals(""))
        {
            iv_invalid.setText(getResources().getString(R.string.invalid_first_name));
        }
        else if (last_name.equals(""))
        {
            iv_invalid.setText(getResources().getString(R.string.invalid_last_name));
        }
        else {
            JsonObject obj = new JsonObject();
            JsonObject paramObject = new JsonObject();
            paramObject.addProperty("first_name", first_name);
            paramObject.addProperty("last_name", last_name);
            paramObject.addProperty("email", email_address);
            paramObject.addProperty("contact_number", mobile_number);
            paramObject.addProperty("login_type", login_type);
            paramObject.addProperty("otp", "1231220");
            obj.add("data",paramObject);
            retrofitCalls.Uservalidate(obj, loadingDialog, new RetrofitCallback() {
                @Override
                public void success(Response<ApiResponse> response) {
                    //Log.e("Response is",new Gson().toJson(response));


                    if(response.body().getStatus()==200) {
                        VerifyPhone(edit_Mobile.getText().toString());
                    }
                    else {
                        loadingDialog.cancelLoading();
                        Gson gson = new Gson();
                        String headerString = gson.toJson(response.body().getData());
                        Type listType = new TypeToken<UservalidateModel>() {
                        }.getType();
                        UservalidateModel user_model=new Gson().fromJson(headerString, listType);
                        /*Log.e("getAccessToken", user_model.getAccessToken());*/
                        Global.Messageshow(getApplicationContext(),mMainLayout,user_model.getContactNumber().get(0).toString(),false);

                    }
                }

                @Override
                public void error(Response<ApiResponse> response) {
                    loadingDialog.cancelLoading();
                }
            });
        }
    }

    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();

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
        if (lay_PhoneShow) {
            if (edit_Mobile.getText().toString().trim().equals("")) {
                iv_invalid.setText(getResources().getString(R.string.invalid_phone));
            }
            else if (edit_Mobile.getText().length()!=10)
            {
                iv_invalid.setText(getResources().getString(R.string.invalid_phone));
            }
            else {
                login_type="PHONE";

                return true;

            }
        } else {
            if (edit_email.getText().toString().trim().equals("")) {
                iv_invalid.setText(getResources().getString(R.string.invalid_phone));
            }
            else if (!edit_email.getText().toString().matches(emailPattern))
            {
                iv_invalid.setText(getResources().getString(R.string.invalid_email));

            }
            else {
                login_type="EMAIL";

                return true;

            }
        }
        return false;
    }



}