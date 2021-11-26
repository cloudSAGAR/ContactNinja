package com.intricare.test.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
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
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.intricare.test.Auth.PlanTyep.PlanType_Screen;
import com.intricare.test.R;
import com.intricare.test.Utils.LoadingDialog;
import com.intricare.test.Utils.OTP_Receiver;
import com.intricare.test.Utils.SessionManager;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class VerificationActivity extends AppCompatActivity {
    private static final String TAG = "VerificationActivity";

    PinView otp_pinview;
    TextView verfiy_button,resend_txt,tc_wrong,tvTimer;
    private FirebaseAuth mAuth;
    String phoneAuthCredential;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mobile_number,countrycode,v_id;
    public String fcmToken = "";
    public static CountDownTimer countDownTimer;
    int second;
    private long mLastClickTime = 0;
    LoadingDialog loadingDialog;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        mAuth = FirebaseAuth.getInstance();
        sessionManager =new SessionManager(this);
        IntentUI();
        EnableRuntimePermission();

       /* Intent getdata=getIntent();
        Bundle bundle=getdata.getExtras();
        v_id=bundle.getString("v_id");
        mobile_number=bundle.getString("mobile");
        countrycode=bundle.getString("countrycode");
        loadingDialog = new LoadingDialog(VerificationActivity.this);

*/

        firebase();

        verfiy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(VerificationActivity.this, Phone_email_verificationActivity.class);
                startActivity(i);
                finish();

               /* String pin_text=otp_pinview.getText().toString();
                if (pin_text.equals(""))
                {
                    tc_wrong.setVisibility(View.VISIBLE);
                }
                else {
                    tc_wrong.setVisibility(View.GONE);
                    loadingDialog.showLoadingDialog();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(v_id, otp_pinview.getText().toString());
                    signInWithCredential(credential);


                }*/

            }
        });
        resend_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("On Resend ","call");
                VerifyPhone(mobile_number.trim());
              //  firebase();
            }
        });
        new OTP_Receiver().setEditText(otp_pinview);

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
                        if (task.isSuccessful()) {
                            tc_wrong.setVisibility(View.GONE);
                            sessionManager.login();
                            loadingDialog.cancelLoading();
                            Intent i = new Intent(VerificationActivity.this, Phone_email_verificationActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            tc_wrong.setVisibility(View.VISIBLE);
                            loadingDialog.cancelLoading();
                            Toast.makeText(VerificationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
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
                            startActivity(new Intent(getApplicationContext(), Phone_email_verificationActivity.class));



                        } else {
                            try {
                           //     iosDialog.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                         //   Toast.makeText(getApplicationContext(), getResources().getString(R.string.Invalid_otp), Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        try {
                        //    iosDialog.dismiss();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        otp_pinview.setText("");
                    }
                });
    }

    private void IntentUI() {
        otp_pinview=findViewById(R.id.otp_pinview);
        verfiy_button=findViewById(R.id.verfiy_button);
        resend_txt=findViewById(R.id.resend_txt);
        tc_wrong=findViewById(R.id.tc_wrong);
        tvTimer=findViewById(R.id.tvTimer);
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
                            Log.e("FCM registration failed", task.getException() + "");
                            return;
                        }

                        fcmToken = task.getResult();
                    }
                });
        // Initialize phone auth callbacks  [START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.e("FCM registration failed",credential + "");

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.e(TAG, "onVerificationFailed", e);
                Toast.makeText(getApplicationContext(), "VERIFY FAILED", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {

                Log.e("Code is Send","Yes");

                v_id=verificationId;
                second = -1;
                showTimer();
            }

        };
    }

    public void showTimer() {
        countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @SuppressLint("NewApi")
            @Override
            public void onTick(long millisUntilFinished) {
                second++;
                tvTimer.setVisibility(View.VISIBLE);
                resend_txt.setVisibility(View.INVISIBLE);
                tvTimer.setText(String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                countDownTimer.cancel();
                tvTimer.setText("00:00");
                tvTimer.setVisibility(View.INVISIBLE);
                resend_txt.setVisibility(View.VISIBLE);
            }
        };
        countDownTimer.start();
    }


}