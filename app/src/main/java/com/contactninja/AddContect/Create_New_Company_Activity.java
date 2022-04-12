package com.contactninja.AddContect;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.contactninja.Model.CompanyModel;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.hbb20.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;
import retrofit2.Response;

@SuppressLint("SimpleDateFormat,StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Create_New_Company_Activity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {
    ImageView iv_back, iv_toolbar_manu_vertical, iv_block, iv_edit;
    TextView save_button, no_image, tv_remain_txt, tv_error, iv_invalid, iv_invalid1, tv_phone,
            tv_email, tv_company_url, tv_company_address;
    EditText add_name, add_detail, edit_Mobile, edit_email, edit_address, edit_company_url;
    CountryCodePicker ccp_id;
    ConstraintLayout mMainLayout;
    String flag = "";
    LoadingDialog loadingDialog;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    CompanyModel.Company WorkData;
    Integer id = 0;
    LinearLayout layout_phonenumber, tab_informnation, layout_email, layout_comapny, layout_comapny_address;
    private BroadcastReceiver mNetworkReceiver;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_company);
        mNetworkReceiver = new ConnectivityReceiver();
        IntentUI();
        //  WorkData = SessionManager.getCompnay_detail(getApplicationContext());
        sessionManager = new SessionManager(getApplicationContext());
        loadingDialog = new LoadingDialog(this);
        retrofitCalls = new RetrofitCalls(this);

        add_detail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() <= 100) {
                    int num = 100 - charSequence.toString().length();
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
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        flag = bundle.getString("flag");
        id = bundle.getInt("id");
        if (!flag.equals("add")) {
            if (flag.equals("read")) {
                tab_informnation.setVisibility(View.GONE);

            } else {
                tab_informnation.setVisibility(View.VISIBLE);
            }
            try {
                CompanyList();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            tab_informnation.setVisibility(View.VISIBLE);

        }


        add_name.setEnabled(true);
        add_detail.setEnabled(true);
        edit_Mobile.setEnabled(true);
        edit_email.setEnabled(true);
        edit_company_url.setEnabled(true);
        edit_address.setEnabled(true);
        save_button.setText("Save");
        iv_toolbar_manu_vertical.setVisibility(View.GONE);

    }

    private void IntentUI() {
        tab_informnation = findViewById(R.id.tab_informnation);
        iv_edit = findViewById(R.id.iv_edit);
        mMainLayout = findViewById(R.id.mMainLayout);
        iv_invalid1 = findViewById(R.id.iv_invalid1);
        edit_email = findViewById(R.id.edit_email);
        iv_invalid = findViewById(R.id.iv_invalid);
        edit_Mobile = findViewById(R.id.edit_Mobile);
        ccp_id = findViewById(R.id.ccp_id);
        tv_error = findViewById(R.id.tv_error);
        add_detail = findViewById(R.id.add_detail);
        tv_remain_txt = findViewById(R.id.tv_remain_txt);
        iv_back = findViewById(R.id.iv_back);
        save_button = findViewById(R.id.save_button);
        save_button.setVisibility(View.VISIBLE);
        iv_back.setVisibility(View.VISIBLE);
        iv_toolbar_manu_vertical = findViewById(R.id.iv_toolbar_manu_vertical);
        no_image = findViewById(R.id.no_image);
        add_name = findViewById(R.id.add_name);
        save_button.setOnClickListener(this);
        edit_address = findViewById(R.id.edit_address);
        edit_company_url = findViewById(R.id.edit_company_url);
        iv_back.setOnClickListener(this);
        iv_block = findViewById(R.id.iv_block);
        iv_toolbar_manu_vertical.setOnClickListener(this);
        tv_phone = findViewById(R.id.tv_phone);
        layout_phonenumber = findViewById(R.id.layout_phonenumber);
        tv_email = findViewById(R.id.tv_email);
        tv_company_url = findViewById(R.id.tv_company_url);
        tv_company_address = findViewById(R.id.tv_company_address);
        layout_email = findViewById(R.id.layout_email);
        layout_comapny = findViewById(R.id.layout_comapny);
        layout_comapny_address = findViewById(R.id.layout_comapny_address);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Create_New_Company_Activity.this, mMainLayout);
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

    private void CompanyList() throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(Create_New_Company_Activity.this);

        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", user_data.getUser().getId());
        paramObject.addProperty("id", id);
        obj.add("data", paramObject);
        retrofitCalls.CompanyList(sessionManager, obj, loadingDialog, Global.getToken(sessionManager),
                Global.getVersionname(Create_New_Company_Activity.this), Global.Device, new RetrofitCallback() {
                    @Override
                    public void success(Response<ApiResponse> response) {
                        loadingDialog.cancelLoading();
                        if (response.body().getHttp_status().equals(200)) {
                            Gson gson = new Gson();
                            String headerString = gson.toJson(response.body().getData());
                            if (response.body().getHttp_status() == 200) {
                                Type listType = new TypeToken<CompanyModel>() {
                                }.getType();
                                CompanyModel data = new Gson().fromJson(headerString, listType);
                                List<CompanyModel.Company> companyList = data.getCompanies_data();
                                WorkData = companyList.get(0);
                                setdata(WorkData);
                            }
                        }
                    }

                    @Override
                    public void error(Response<ApiResponse> response) {
                        loadingDialog.cancelLoading();
                    }
                });
    }

    private void setdata(CompanyModel.Company WorkData) {
        if (flag.equals("read")) {
            try {
                iv_edit.setVisibility(View.GONE);
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
                Log.e("Location is", WorkData.getAddress().toString().trim());
                edit_address.setText(WorkData.getAddress().toString().trim());
                if (WorkData.getIs_blocked().equals(1)) {
                    iv_block.setVisibility(View.VISIBLE);
                } else {
                    iv_block.setVisibility(View.GONE);
                }
                no_image.setVisibility(View.VISIBLE);
                add_name.setEnabled(false);
                add_detail.setEnabled(false);
                edit_Mobile.setEnabled(false);
                edit_email.setEnabled(false);
                edit_company_url.setEnabled(false);
                edit_address.setEnabled(false);
                add_name.setText(WorkData.getName());
                add_detail.setText(WorkData.getDescription());
                ccp_id.setDefaultCountryUsingNameCode(String.valueOf(Global.Countrycode(Create_New_Company_Activity.this,
                        WorkData.getContact_number())));
                ccp_id.setDefaultCountryUsingPhoneCode(Global.Countrycode(Create_New_Company_Activity.this, WorkData.getContact_number()));
                ccp_id.resetToDefaultCountry();
                String main_data = WorkData.getContact_number().replace("+" + String.valueOf(Global.Countrycode(Create_New_Company_Activity.this,
                        WorkData.getContact_number())), "");
                edit_Mobile.setText(main_data);
                edit_email.setText(WorkData.getEmail());
                edit_company_url.setText(WorkData.getCompany_url().toString().trim());
                ccp_id.registerCarrierNumberEditText(edit_Mobile);


                if (Global.IsNotNull(WorkData.getEmail().toString().trim())) {
                    tv_email.setVisibility(View.VISIBLE);
                    layout_email.setVisibility(View.VISIBLE);
                    tab_informnation.setVisibility(View.VISIBLE);
                } else {
                    layout_email.setVisibility(View.GONE);
                    tv_email.setVisibility(View.GONE);

                }
                Log.e("url is", WorkData.getCompany_url().toString().trim());
                Log.e("Email is", WorkData.getEmail().toString().trim());
                if (Global.IsNotNull(WorkData.getCompany_url().toString().trim())) {
                    layout_comapny.setVisibility(View.VISIBLE);
                    tab_informnation.setVisibility(View.VISIBLE);

                } else {
                    layout_comapny.setVisibility(View.GONE);
                }
                if (Global.IsNotNull(WorkData.getAddress())) {
                    layout_comapny_address.setVisibility(View.VISIBLE);
                    tab_informnation.setVisibility(View.VISIBLE);


                } else {
                    layout_comapny_address.setVisibility(View.GONE);
                }
                if (Global.IsNotNull(WorkData.getName().toString())) {
                    add_name.setVisibility(View.VISIBLE);


                } else {
                    add_name.setVisibility(View.GONE);
                }

                if (Global.IsNotNull(WorkData.getDescription())) {
                    add_detail.setVisibility(View.VISIBLE);
                    tv_remain_txt.setVisibility(View.VISIBLE);

                } else {
                    add_detail.setVisibility(View.GONE);
                    tv_remain_txt.setVisibility(View.GONE);
                }
                if (Global.IsNotNull(WorkData.getContact_number())) {
                    tv_phone.setVisibility(View.VISIBLE);
                    layout_phonenumber.setVisibility(View.VISIBLE);
                    tab_informnation.setVisibility(View.VISIBLE);

                } else {
                    tv_phone.setVisibility(View.GONE);
                    layout_phonenumber.setVisibility(View.GONE);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


        } else if (flag.equals("edit")) {
            try {
                tab_informnation.setVisibility(View.VISIBLE);

                iv_edit.setVisibility(View.GONE);
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
                add_name.setText(WorkData.getName());

                iv_block.setVisibility(View.VISIBLE);

                if (WorkData.getIs_blocked().equals(1)) {
                    iv_block.setVisibility(View.VISIBLE);
                } else {
                    iv_block.setVisibility(View.GONE);
                }
                no_image.setVisibility(View.VISIBLE);
                add_name.setEnabled(true);
                add_detail.setEnabled(true);
                edit_Mobile.setEnabled(true);
                edit_email.setEnabled(true);
                edit_company_url.setEnabled(true);
                edit_address.setEnabled(true);
                add_detail.setText(WorkData.getDescription());


                ccp_id.setDefaultCountryUsingNameCode(String.valueOf(Global.Countrycode(Create_New_Company_Activity.this,
                        WorkData.getContact_number())));
                ccp_id.setDefaultCountryUsingPhoneCode(Global.Countrycode(Create_New_Company_Activity.this, WorkData.getContact_number()));
                ccp_id.resetToDefaultCountry();
                String main_data = WorkData.getContact_number().replace("+" + String.valueOf(Global.Countrycode(Create_New_Company_Activity.this,
                        WorkData.getContact_number())), "");
                edit_Mobile.setText(main_data);

                edit_email.setText(WorkData.getEmail());
                edit_company_url.setText(WorkData.getCompany_url().toString().trim());
                edit_address.setText(WorkData.getAddress());

                ccp_id.registerCarrierNumberEditText(edit_Mobile);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                            iv_invalid.setVisibility(View.GONE);
                            iv_invalid.setText("");
                        } else {
                            iv_invalid.setVisibility(View.VISIBLE);
                            iv_invalid.setText(getResources().getString(R.string.invalid_phone));
                        }
                    } else {
                        iv_invalid.setVisibility(View.VISIBLE);
                        iv_invalid.setText(getResources().getString(R.string.invalid_phone));
                    }
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
        switch (view.getId()) {
            case R.id.save_button:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if (Global.isNetworkAvailable(this, mMainLayout)) {
                    if (flag.equals("read")) {
                        Intent intent = new Intent(getApplicationContext(), Create_New_Company_Activity.class);
                        intent.putExtra("flag", "edit");
                        intent.putExtra("id", WorkData.getId());
                        startActivity(intent);
                        finish();
                    } else {
                        if (add_name.getText().toString().equals("")) {
                            Global.Messageshow(getApplicationContext(), mMainLayout, getResources().getString(R.string.add_company_name), false);

                        } else {
                            iv_invalid.setVisibility(View.GONE);
                            iv_invalid1.setVisibility(View.GONE);
                            try {
                                AddConpany();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }


                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_toolbar_manu_vertical:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (Global.isNetworkAvailable(this, mMainLayout)) {
                    broadcast_manu();
                }
                break;
        }
    }


    private void broadcast_manu() {

        @SuppressLint("InflateParams") final View mView = getLayoutInflater().inflate(R.layout.remove_block_layout, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);
        TextView selected_block = bottomSheetDialog.findViewById(R.id.selected_block);
        View line_block = bottomSheetDialog.findViewById(R.id.line_block);
        View line_unblock = bottomSheetDialog.findViewById(R.id.line_unblock);
        TextView selected_un_block = bottomSheetDialog.findViewById(R.id.selected_unblock);
        TextView selected_delete = bottomSheetDialog.findViewById(R.id.selected_delete);
        selected_block.setText(getString(R.string.add_blacklist));
        selected_un_block.setText(getString(R.string.remove_blacklist));
        selected_delete.setText(getString(R.string.delete_company));

        if (WorkData.getIs_blocked().equals(1)) {
            selected_block.setVisibility(View.GONE);
            line_block.setVisibility(View.GONE);
            selected_un_block.setVisibility(View.VISIBLE);
            line_unblock.setVisibility(View.VISIBLE);
        } else {
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
                    if (Global.isNetworkAvailable(Create_New_Company_Activity.this, mMainLayout)) {
                        Contect_BLock(1, bottomSheetDialog);
                    }
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
                    if (Global.isNetworkAvailable(Create_New_Company_Activity.this, mMainLayout)) {
                        Contect_BLock(0, bottomSheetDialog);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        selected_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Block Contect

                try {
                    if (Global.isNetworkAvailable(Create_New_Company_Activity.this, mMainLayout)) {
                        Company_Remove(bottomSheetDialog);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        bottomSheetDialog.show();

    }


    public void Contect_BLock(int block, BottomSheetDialog bottomSheetDialog) throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(this);
        JSONObject obj = new JSONObject();
        JSONObject paramObject = new JSONObject();
        paramObject.put("organization_id", 1);
        paramObject.put("team_id", 1);
        paramObject.put("user_id", user_data.getUser().getId());
        paramObject.put("is_block", block);
        JSONArray block_array = new JSONArray();
        block_array.put(WorkData.getId());
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
                    Global.Messageshow(getApplicationContext(), mMainLayout, response.body().getMessage(), true);

                    if (block == 1) {
                        WorkData.setIs_blocked(block);
                        iv_block.setVisibility(View.VISIBLE);
                    } else {
                        iv_block.setVisibility(View.GONE);
                        WorkData.setIs_blocked(block);
                    }
                } else {
                    Global.Messageshow(getApplicationContext(), mMainLayout, response.body().getMessage(), false);
                }
                bottomSheetDialog.cancel();
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                bottomSheetDialog.cancel();
            }
        });

    }


    public void Company_Remove(BottomSheetDialog bottomSheetDialog) throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(getApplicationContext());
        JSONObject obj = new JSONObject();
        JSONObject paramObject = new JSONObject();
        paramObject.put("organization_id", 1);
        paramObject.put("team_id", 1);
        paramObject.put("user_id", user_data.getUser().getId());
        paramObject.put("id", WorkData.getId());
        paramObject.put("status", "D");
        obj.put("data", paramObject);
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        Log.e("Main Data is ", new Gson().toJson(gsonObject));
        retrofitCalls.Company_add(sessionManager, gsonObject, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(this), Global.Device, new RetrofitCallback() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void success(Response<ApiResponse> response) {

                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    finish();
                } else {
                    Global.Messageshow(getApplicationContext(), mMainLayout, response.body().getMessage(), false);
                }
                bottomSheetDialog.cancel();
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                bottomSheetDialog.cancel();
            }
        });

    }


    public void AddConpany() throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(getApplicationContext());
        JSONObject obj = new JSONObject();
        JSONObject paramObject = new JSONObject();
        paramObject.put("organization_id", 1);
        paramObject.put("team_id", 1);
        paramObject.put("user_id", user_data.getUser().getId());
        paramObject.put("name", add_name.getText().toString().trim());
        paramObject.put("address", edit_address.getText().toString().trim());
        paramObject.put("description", add_detail.getText().toString().trim());
        paramObject.put("email", edit_email.getText().toString().trim());
        paramObject.put("company_url", edit_company_url.getText().toString().trim());
        if (!edit_Mobile.getText().toString().trim().equals("")) {
            paramObject.put("contact_number", ccp_id.getSelectedCountryCodeWithPlus() + edit_Mobile.getText().toString().trim());

        }
        //paramObject.put("status", "A");
        if (id != 0) {
            paramObject.put("id", id);
        }

        obj.put("data", paramObject);
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        //    Log.e("Main Data is ", new Gson().toJson(gsonObject));
        retrofitCalls.Company_add(sessionManager, gsonObject, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(this), Global.Device, new RetrofitCallback() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void success(Response<ApiResponse> response) {

                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    //Global.Messageshow(getApplicationContext(), main_layout, response.body().getMessage(), false);
                    finish();
                } else {
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
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}