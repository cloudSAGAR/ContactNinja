package com.contactninja.Group;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
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
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Response;

import com.bumptech.glide.Glide;
import com.contactninja.AddContect.Add_Company_Activity;
import com.contactninja.Fragment.GroupFragment.MembersFragment;
import com.contactninja.Fragment.Home.Task_Fragment;
import com.contactninja.Model.Grouplist;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.Model.UservalidateModel;
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
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class SendBroadcast extends AppCompatActivity implements View.OnClickListener,ConnectivityReceiver.ConnectivityReceiverListener {
    private long mLastClickTime = 0;
    TextView save_button;
    ImageView iv_Setting, iv_back,iv_toolbar_manu1;
    EditText add_detail, add_new_contect, ev_search;
    SessionManager sessionManager;
    RoundedImageView add_new_contect_icon;
    ConstraintLayout mMainLayout;
    TextView no_image, topic_remainingCharacter;

    private BroadcastReceiver mNetworkReceiver;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    int group_id;
    String group_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_broadcast);
        mNetworkReceiver = new ConnectivityReceiver();
        IntentUI();
        Global.checkConnectivity(SendBroadcast.this, mMainLayout);
        sessionManager = new SessionManager(this);
        Grouplist.Group group_data = SessionManager.getGroupData(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        loadingDialog=new LoadingDialog(this);
        add_new_contect.setText(group_data.getGroupName());
        add_detail.setText(group_data.getDescription());
        group_id=group_data.getId();
        group_name=group_data.getGroupName();

        save_button.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        save_button.setText("Edit");
        save_button.setVisibility(View.GONE);
        iv_Setting.setVisibility(View.GONE);

        if (group_data.getGroupImage() == null) {
            String name = group_data.getGroupName();
            String add_text = "";
            String[] split_data = name.split(" ");
            try {
                for (int i = 0; i < split_data.length; i++) {
                    if (i == 0) {
                        add_text = split_data[i].substring(0, 1);
                    } else {
                        add_text = add_text + split_data[i].substring(0, 1);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            no_image.setText(add_text);
            no_image.setVisibility(View.VISIBLE);
            add_new_contect_icon.setVisibility(View.GONE);
        }
        else {
            Glide.with(getApplicationContext()).
                    load(group_data.getGroupImage()).
                    placeholder(R.drawable.shape_primary_back).
                    error(R.drawable.shape_primary_back).into(add_new_contect_icon);
            no_image.setVisibility(View.GONE);
            add_new_contect_icon.setVisibility(View.VISIBLE);
        }


        add_detail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Log.e("Test Clcik ", String.valueOf(charSequence));
                if (charSequence.toString().length() <= 100) {
                    int num = 100 - charSequence.toString().length();
                    topic_remainingCharacter.setText(num + " " + getResources().getString(R.string.camp_remaingn));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //    topic_remainingCharacter.setText(100 - editable.length() + " Characters Remaining.");
            }
        });


        Fragment fragment = new MembersFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

    }

    private void IntentUI() {
        iv_toolbar_manu1=findViewById(R.id.iv_toolbar_manu_vertical);
        iv_toolbar_manu1.setVisibility(View.VISIBLE);
        iv_toolbar_manu1.setOnClickListener(this);
        save_button = findViewById(R.id.save_button);
        iv_Setting = findViewById(R.id.iv_Setting);
        iv_Setting.setVisibility(View.VISIBLE);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        add_detail = findViewById(R.id.add_detail);
        add_new_contect_icon = findViewById(R.id.add_new_contect_icon);
        add_new_contect = findViewById(R.id.add_new_contect);
        mMainLayout = findViewById(R.id.mMainLayout);
        no_image = findViewById(R.id.no_image);
        topic_remainingCharacter = findViewById(R.id.topic_remainingCharacter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_toolbar_manu_vertical:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (Global.isNetworkAvailable(this, mMainLayout)) {
                    broadcast_manu();
                }
                break;
            case R.id.iv_back:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                finish();
                break;
            case R.id.save_button:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                startActivity(new Intent(getApplicationContext(), Final_Group.class));
                finish();
                break;
            default:


        }
    }

    private void broadcast_manu() {

        @SuppressLint("InflateParams") final View mView = getLayoutInflater().inflate(R.layout.remove_group_layout, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);

        TextView selected_delete=bottomSheetDialog.findViewById(R.id.selected_delete);
        selected_delete.setText("Delete Group");
        TextView selected_edit=bottomSheetDialog.findViewById(R.id.selected_edit);
        selected_edit.setText("Edit Group");
        selected_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                startActivity(new Intent(getApplicationContext(), Final_Group.class));
                finish();
            }
        });
        selected_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    RemoveGroup();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                bottomSheetDialog.dismiss();

            }
        });
        bottomSheetDialog.show();

    }


    private void RemoveGroup() throws JSONException {


          loadingDialog.showLoadingDialog();

        SignResponseModel user_data = sessionManager.getGetUserdata(this);
        JSONArray jsonArray = new JSONArray();

        // contect_array.put(3);
        String token = Global.getToken(sessionManager);
        JSONObject obj = new JSONObject();
        JSONObject paramObject = new JSONObject();
        paramObject.put("id", group_id);
        paramObject.put("status","D");
        paramObject.put("organization_id", 1);
        paramObject.put("team_id", 1);
        paramObject.put("user_id", user_data.getUser().getId());
        //Log.e("Data IS ",new Gson().toJson(obj));
        obj.put("data",paramObject);
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        //  Log.e("Obbject data",new Gson().toJson(gsonObject));
        retrofitCalls.AddGroup(sessionManager, gsonObject, loadingDialog, token, Global.getVersionname(SendBroadcast.this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {

                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    finish();
                } else {
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Log.e("String is", response.body().getMessage());
                    Type listType = new TypeToken<UservalidateModel>() {
                    }.getType();
                    UservalidateModel user_model = new Gson().fromJson(headerString, listType);
                    Global.Messageshow(getApplicationContext(), mMainLayout, user_model.getEmail().get(0), false);
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(SendBroadcast.this, mMainLayout);
    }

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