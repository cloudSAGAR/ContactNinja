package com.contactninja.AddContect;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;
import retrofit2.Response;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.contactninja.Model.CompanyModel;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hbb20.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class Add_Company_Activity extends AppCompatActivity implements View.OnClickListener {
ImageView iv_back,iv_toolbar_manu_vertical,iv_block;
TextView save_button,no_image,tv_remain_txt,tv_error,iv_invalid,iv_invalid1;
EditText add_name,add_detail,edit_Mobile,edit_email,edit_address,edit_company_url;
CountryCodePicker ccp_id;
ConstraintLayout main_layout;
String flag;
    LoadingDialog loadingDialog;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    CompanyModel.Company WorkData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_company);
        IntentUI();
        WorkData=SessionManager.getCompnay_detail(getApplicationContext());
        sessionManager = new SessionManager(getApplicationContext());
        loadingDialog = new LoadingDialog(this);
        retrofitCalls = new RetrofitCalls(this);

        add_detail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() <= 40) {
                    int num = 40 - charSequence.toString().length();
                    tv_remain_txt.setText(num + " " + getResources().getString(R.string.camp_remaingn));
                } else if (charSequence.toString().length() == 0) {
                    tv_error.setVisibility(View.VISIBLE);
                } else {
                    tv_error.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        enterPhoneNumber();
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        flag=bundle.getString("flag");
        if (flag.equals("read"))
        {
            save_button.setText("Edit");
            iv_toolbar_manu_vertical.setVisibility(View.VISIBLE);

            String name = WorkData.getName();
            String add_text = "";
            String[] split_data = name.split(" ");
            try {
                for (int i = 0; i < split_data.length; i++) {
                    if (i == 0) {
                        add_text = split_data[i].substring(0, 1);
                    } else {
                        add_text = add_text + split_data[i].charAt(0);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            no_image.setText(add_text);
            iv_block.setVisibility(View.VISIBLE);

            if (WorkData.getIs_blocked().equals("1"))
            {
                iv_block.setVisibility(View.VISIBLE);
                no_image.setVisibility(View.VISIBLE);
            }
            else {
                iv_block.setVisibility(View.GONE);
                no_image.setVisibility(View.VISIBLE);
            }
            add_name.setEnabled(false);
            add_detail.setEnabled(false);
            edit_Mobile.setEnabled(false);
            edit_email.setEnabled(false);
            edit_company_url.setEnabled(false);
            edit_address.setEnabled(false);
        }
        else if (flag.equals("edit"))
        {
            save_button.setText("Save");
            iv_toolbar_manu_vertical.setVisibility(View.VISIBLE);
            String name = WorkData.getName();
            String add_text = "";
            String[] split_data = name.split(" ");
            try {
                for (int i = 0; i < split_data.length; i++) {
                    if (i == 0) {
                        add_text = split_data[i].substring(0, 1);
                    } else {
                        add_text = add_text + split_data[i].charAt(0);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            no_image.setText(add_text);
            iv_block.setVisibility(View.VISIBLE);

            if (WorkData.getIs_blocked().equals("1"))
            {
                iv_block.setVisibility(View.VISIBLE);
                no_image.setVisibility(View.VISIBLE);
            }
            else {
                iv_block.setVisibility(View.GONE);
                no_image.setVisibility(View.VISIBLE);
            }
            add_name.setEnabled(true);
            add_detail.setEnabled(true);
            edit_Mobile.setEnabled(true);
            edit_email.setEnabled(true);
            edit_company_url.setEnabled(true);
            edit_address.setEnabled(true);
            add_name.setText(WorkData.getName().toString());
            add_detail.setText("");
            edit_Mobile.setText("");
            edit_email.setText(WorkData.getEmail());
            edit_company_url.setText("");
            edit_address.setText("");
            add_name.setText(WorkData.getName().toString());
            add_detail.setText("");
            edit_Mobile.setText("");
            edit_email.setText(WorkData.getEmail());
            edit_company_url.setText("");
            edit_address.setText("");

         }
        else {
            add_name.setEnabled(true);
            add_detail.setEnabled(true);
            edit_Mobile.setEnabled(true);
            edit_email.setEnabled(true);
            edit_company_url.setEnabled(true);
            edit_address.setEnabled(true);
            save_button.setText("Save");
            iv_toolbar_manu_vertical.setVisibility(View.GONE);
        }

    }

    private void IntentUI() {
        main_layout=findViewById(R.id.main_layout);
        iv_invalid1=findViewById(R.id.iv_invalid1);
        edit_email=findViewById(R.id.edit_email);
        iv_invalid=findViewById(R.id.iv_invalid);
        edit_Mobile=findViewById(R.id.edit_Mobile);
        ccp_id = findViewById(R.id.ccp_id);
        tv_error=findViewById(R.id.tv_error);
        add_detail=findViewById(R.id.add_detail);
        tv_remain_txt=findViewById(R.id.tv_remain_txt);
        iv_back=findViewById(R.id.iv_back);
        save_button=findViewById(R.id.save_button);
        save_button.setVisibility(View.VISIBLE);
        iv_back.setVisibility(View.VISIBLE);
        iv_toolbar_manu_vertical=findViewById(R.id.iv_toolbar_manu_vertical);
        no_image=findViewById(R.id.no_image);
        add_name=findViewById(R.id.add_name);
        save_button.setOnClickListener(this);
        edit_address=findViewById(R.id.edit_address);
        edit_company_url=findViewById(R.id.edit_company_url);
        iv_back.setOnClickListener(this);
        iv_block=findViewById(R.id.iv_block);
        iv_toolbar_manu_vertical.setOnClickListener(this);
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


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.save_button:

                if (edit_Mobile.getText().toString().trim().equals("")) {
                    iv_invalid.setVisibility(View.VISIBLE);
                }
                else if (edit_email.getText().toString().trim().equals("")) {
                    iv_invalid.setVisibility(View.GONE);
                    iv_invalid1.setText(getResources().getString(R.string.invalid_email));
                    iv_invalid1.setVisibility(View.VISIBLE);
                }
                else if (edit_company_url.getText().toString().equals(""))
                {
                    iv_invalid.setVisibility(View.GONE);
                    iv_invalid1.setVisibility(View.GONE);
                    Global.Messageshow(getApplicationContext(),main_layout,"Add company url",false);
                }
                else if (edit_address.getText().toString().equals(""))
                {
                    iv_invalid.setVisibility(View.GONE);
                    iv_invalid1.setVisibility(View.GONE);
                    Global.Messageshow(getApplicationContext(),main_layout,"Add company address",false);

                }
                else {
                    iv_invalid.setVisibility(View.GONE);
                    iv_invalid1.setVisibility(View.GONE);
                }
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_toolbar_manu_vertical:
                broadcast_manu(SessionManager.getCompnay_detail(getApplicationContext()));
                break;
        }
    }


    private void broadcast_manu(CompanyModel.Company Company) {

        @SuppressLint("InflateParams") final View mView = getLayoutInflater().inflate(R.layout.remove_block_layout, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);
        TextView selected_block = bottomSheetDialog.findViewById(R.id.selected_block);
        View line_block=bottomSheetDialog.findViewById(R.id.line_block);
        View line_unblock=bottomSheetDialog.findViewById(R.id.line_unblock);
        TextView selected_un_block = bottomSheetDialog.findViewById(R.id.selected_unblock);
        TextView selected_delete=bottomSheetDialog.findViewById(R.id.selected_delete);
        selected_block.setText(getString(R.string.add_blacklist));
        selected_un_block.setText(getString(R.string.remove_blacklist));
        selected_delete.setText(getString(R.string.delete_contact));

        if (Company.getIs_blocked().equals("1"))
        {
            selected_block.setVisibility(View.GONE);
            line_block.setVisibility(View.GONE);
            selected_un_block.setVisibility(View.VISIBLE);
            line_unblock.setVisibility(View.VISIBLE);
        }
        else {
            line_block.setVisibility(View.VISIBLE);
            selected_block.setVisibility(View.VISIBLE);
            selected_un_block.setVisibility(View.GONE);
            line_unblock.setVisibility(View.GONE);
        }


        selected_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Block Contect

                try {
                    Contect_BLock(Company,"1",bottomSheetDialog);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        selected_un_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Block Contect

                try {
                    Contect_BLock(Company,"0",bottomSheetDialog);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        selected_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Block Contect

               /* try {
                    Contect_Remove(Company,"0",bottomSheetDialog);
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/


            }
        });
        bottomSheetDialog.show();

    }


    public void Contect_BLock(CompanyModel.Company Company, String block, BottomSheetDialog bottomSheetDialog) throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(this);
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());
        JSONObject obj = new JSONObject();
        JSONObject paramObject = new JSONObject();
        paramObject.put("organization_id", "1");
        paramObject.put("team_id", "1");
        paramObject.put("user_id", user_id);
        paramObject.put("is_block",block);
        JSONArray block_array = new JSONArray();
        block_array.put(Company.getId());
        paramObject.put("blockCompanyIds", block_array);
        obj.put("data", paramObject);
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        Log.e("Main Data is ", new Gson().toJson(gsonObject));
        retrofitCalls.Block_Company(sessionManager, gsonObject, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(this), Global.Device, new RetrofitCallback() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void success(Response<ApiResponse> response) {

                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    Global.Messageshow(getApplicationContext(), main_layout, response.body().getMessage(), false);
                  
                    if (block.equals("1"))
                    {
                        WorkData.setIs_blocked(block);
                        iv_block.setVisibility(View.VISIBLE);
                    }
                    else {
                        iv_block.setVisibility(View.GONE);
                        WorkData.setIs_blocked(block);
                    }
                    bottomSheetDialog.cancel();
                }
                else {
                    Global.Messageshow(getApplicationContext(), main_layout, response.body().getMessage(), false);
                    bottomSheetDialog.cancel();
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                bottomSheetDialog.cancel();
            }
        });

    }



    public void Contect_Remove(CompanyModel.Company Company, String block, BottomSheetDialog bottomSheetDialog) throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(getApplicationContext());
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());JSONObject obj = new JSONObject();
        JSONObject paramObject = new JSONObject();
        paramObject.put("organization_id", "1");
        paramObject.put("team_id", "1");
        paramObject.put("user_id", user_id);
        paramObject.put("id",Company.getId());
        paramObject.put("status","D");

        obj.put("data", paramObject);
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        Log.e("Main Data is ", new Gson().toJson(gsonObject));
        retrofitCalls.Addcontect(sessionManager, gsonObject, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(this), Global.Device, new RetrofitCallback() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void success(Response<ApiResponse> response) {

                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    Global.Messageshow(getApplicationContext(), main_layout, response.body().getMessage(), false);
                 finish();
                    bottomSheetDialog.cancel();
                }
                else {
                    Global.Messageshow(getApplicationContext(), main_layout, response.body().getMessage(), false);
                    bottomSheetDialog.cancel();
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                bottomSheetDialog.cancel();
            }
        });

    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}