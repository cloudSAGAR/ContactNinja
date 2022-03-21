package com.contactninja.UserPofile;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contactninja.MainActivity;
import com.contactninja.Model.Dashboard.Dashboard;
import com.contactninja.Model.Dashboard.Des_AffiliateInfo;
import com.contactninja.Model.UserData.LevelModel;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle")
public class Affiliate_Report_LavelActivity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {
    private long mLastClickTime = 0;
    LinearLayout mMainLayout;
    ImageView iv_back;
    TextView tv_lavel_name;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    RecyclerView rv_lavel_list;
    LavelAdapter lavelAdapter;
    Des_AffiliateInfo desAffiliateInfo = new Des_AffiliateInfo();
    Dashboard dashboard = new Dashboard();

    List<String> lavelName = new ArrayList<>();
    LinearLayout select_label_zone;
    EditText ev_search;
    private BroadcastReceiver mNetworkReceiver;
    SignResponseModel user_data;
    List<LevelModel> list = new ArrayList<>();

    Integer user_id = 0;
    String token_api = "", organization_id = "", team_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affiliate_report_lavel);
        sessionManager = new SessionManager(Affiliate_Report_LavelActivity.this);
        retrofitCalls = new RetrofitCalls(Affiliate_Report_LavelActivity.this);
        loadingDialog = new LoadingDialog(Affiliate_Report_LavelActivity.this);
        token_api = Global.getToken(sessionManager);
        user_data = SessionManager.getGetUserdata(Affiliate_Report_LavelActivity.this);

        user_id = user_data.getUser().getId();

        IntentView();
        try {
            if (Global.isNetworkAvailable(Affiliate_Report_LavelActivity.this, MainActivity.mMainLayout)) {
                Api_List();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        mNetworkReceiver = new ConnectivityReceiver();

    }

    public class MyAsyncTasks extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
        }

        @Override
        protected String doInBackground(String... params) {

            // implement API in background and store the response in current variable
            String current = "";
            try {
                if (Global.isNetworkAvailable(Affiliate_Report_LavelActivity.this, MainActivity.mMainLayout)) {
                    Api_List();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return current;
        }

        @Override
        protected void onPostExecute(String s) {

        }

    }

    private void Api_List() throws JSONException {
        loadingDialog.showLoadingDialog();
        JSONObject obj = new JSONObject();
        JSONObject paramObject = new JSONObject();
        paramObject.put("organization_id", 1);
        paramObject.put("team_id", 1);
        paramObject.put("user_id", user_id);

        obj.put("data", paramObject);
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        Log.e("Gson Data is", new Gson().toJson(gsonObject));
        retrofitCalls.affiliate_list(sessionManager, gsonObject, loadingDialog, token_api, Global.getVersionname(Affiliate_Report_LavelActivity.this), Global.Device, new RetrofitCallback() {
            @SuppressLint("NewApi")
            @Override
            public void success(Response<ApiResponse> response) {


                Gson gson = new Gson();
                String headerString = gson.toJson(response.body().getData());
                Type listType = new TypeToken<Dashboard>() {
                }.getType();
                dashboard = new Gson().fromJson(headerString, listType);
                desAffiliateInfo = dashboard.getAffiliate();

                setdate();
                loadingDialog.cancelLoading();

            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();

            }
        });
    }

    private void setdate() {


        lavelName.clear();
        list.clear();
        if (Global.IsNotNull(desAffiliateInfo)) {
            lavelName.add("All");
            if (desAffiliateInfo.getLevel1().size() != 0) {
                lavelName.add(getResources().getString(R.string.lavel1));
                for (int i = 0; i < desAffiliateInfo.getLevel1().size(); i++) {
                    LevelModel levelModel = new LevelModel();
                    levelModel.setLevel_name(getResources().getString(R.string.lavel1));
                    levelModel.setUserId(desAffiliateInfo.getLevel1().get(i).getUserId());
                    levelModel.setReferredBy(desAffiliateInfo.getLevel1().get(i).getReferredBy());
                    levelModel.setProfilePic(desAffiliateInfo.getLevel1().get(i).getProfilePic());
                    levelModel.setFirstName(desAffiliateInfo.getLevel1().get(i).getFirstName());
                    levelModel.setReferredName(desAffiliateInfo.getLevel1().get(i).getReferredName());
                    levelModel.setCreatedAt(desAffiliateInfo.getLevel1().get(i).getCreatedAt());
                    levelModel.setReward_earned(desAffiliateInfo.getLevel1().get(i).getReward_earned());
                    list.add(levelModel);
                }

                desAffiliateInfo.setLevelAll(list);
            }
            if (desAffiliateInfo.getLevel2().size() != 0) {
                lavelName.add(getResources().getString(R.string.lavel2));
                for (int i = 0; i < desAffiliateInfo.getLevel2().size(); i++) {
                    LevelModel levelModel = new LevelModel();
                    levelModel.setLevel_name(getResources().getString(R.string.lavel2));
                    levelModel.setUserId(desAffiliateInfo.getLevel2().get(i).getUserId());
                    levelModel.setReferredBy(desAffiliateInfo.getLevel2().get(i).getReferredBy());
                    levelModel.setProfilePic(desAffiliateInfo.getLevel2().get(i).getProfilePic());
                    levelModel.setFirstName(desAffiliateInfo.getLevel2().get(i).getFirstName());
                    levelModel.setReferredName(desAffiliateInfo.getLevel2().get(i).getReferredName());
                    levelModel.setCreatedAt(desAffiliateInfo.getLevel2().get(i).getCreatedAt());
                    levelModel.setReward_earned(desAffiliateInfo.getLevel2().get(i).getReward_earned());
                    list.add(levelModel);
                }

                desAffiliateInfo.setLevelAll(list);
            }
            if (desAffiliateInfo.getLevel3().size() != 0) {
                lavelName.add(getResources().getString(R.string.lavel3));
                for (int i = 0; i < desAffiliateInfo.getLevel3().size(); i++) {
                    LevelModel levelModel = new LevelModel();
                    levelModel.setLevel_name(getResources().getString(R.string.lavel3));
                    levelModel.setUserId(desAffiliateInfo.getLevel3().get(i).getUserId());
                    levelModel.setReferredBy(desAffiliateInfo.getLevel3().get(i).getReferredBy());
                    levelModel.setProfilePic(desAffiliateInfo.getLevel3().get(i).getProfilePic());
                    levelModel.setFirstName(desAffiliateInfo.getLevel3().get(i).getFirstName());
                    levelModel.setReferredName(desAffiliateInfo.getLevel3().get(i).getReferredName());
                    levelModel.setCreatedAt(desAffiliateInfo.getLevel3().get(i).getCreatedAt());
                    levelModel.setReward_earned(desAffiliateInfo.getLevel3().get(i).getReward_earned());
                    list.add(levelModel);
                }

                desAffiliateInfo.setLevelAll(list);
            }
            if (desAffiliateInfo.getLevel4().size() != 0) {
                lavelName.add(getResources().getString(R.string.lavel4));
                for (int i = 0; i < desAffiliateInfo.getLevel4().size(); i++) {
                    LevelModel levelModel = new LevelModel();
                    levelModel.setLevel_name(getResources().getString(R.string.lavel4));
                    levelModel.setUserId(desAffiliateInfo.getLevel4().get(i).getUserId());
                    levelModel.setReferredBy(desAffiliateInfo.getLevel4().get(i).getReferredBy());
                    levelModel.setProfilePic(desAffiliateInfo.getLevel4().get(i).getProfilePic());
                    levelModel.setFirstName(desAffiliateInfo.getLevel4().get(i).getFirstName());
                    levelModel.setReferredName(desAffiliateInfo.getLevel4().get(i).getReferredName());
                    levelModel.setCreatedAt(desAffiliateInfo.getLevel4().get(i).getCreatedAt());
                    levelModel.setReward_earned(desAffiliateInfo.getLevel4().get(i).getReward_earned());
                    list.add(levelModel);
                }

                desAffiliateInfo.setLevelAll(list);
            }
            if (desAffiliateInfo.getLevel5().size() != 0) {
                lavelName.add(getResources().getString(R.string.lavel5));
                for (int i = 0; i < desAffiliateInfo.getLevel5().size(); i++) {
                    LevelModel levelModel = new LevelModel();
                    levelModel.setLevel_name(getResources().getString(R.string.lavel5));
                    levelModel.setUserId(desAffiliateInfo.getLevel5().get(i).getUserId());
                    levelModel.setReferredBy(desAffiliateInfo.getLevel5().get(i).getReferredBy());
                    levelModel.setProfilePic(desAffiliateInfo.getLevel5().get(i).getProfilePic());
                    levelModel.setFirstName(desAffiliateInfo.getLevel5().get(i).getFirstName());
                    levelModel.setReferredName(desAffiliateInfo.getLevel5().get(i).getReferredName());
                    levelModel.setCreatedAt(desAffiliateInfo.getLevel5().get(i).getCreatedAt());
                    levelModel.setReward_earned(desAffiliateInfo.getLevel5().get(i).getReward_earned());
                    list.add(levelModel);
                }

                desAffiliateInfo.setLevelAll(list);
            }


            tv_lavel_name.setText(lavelName.get(0));
            List<LevelModel> level1List = new ArrayList<>();
            try {
                switch (lavelName.get(0)) {
                    case "All":
                        level1List = desAffiliateInfo.getLevelAll();
                        break;
                    case "Lavel 1":
                        level1List = desAffiliateInfo.getLevel1();
                        break;
                    case "Lavel 2":
                        level1List = desAffiliateInfo.getLevel2();
                        break;
                    case "Lavel 3":
                        level1List = desAffiliateInfo.getLevel3();
                        break;
                    case "Lavel 4":
                        level1List = desAffiliateInfo.getLevel4();
                        break;
                    case "Lavel 5":
                        level1List = desAffiliateInfo.getLevel5();
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            lavelAdapter = new LavelAdapter(Affiliate_Report_LavelActivity.this, level1List);
            rv_lavel_list.setAdapter(lavelAdapter);
            List<LevelModel> finalLevel1List = level1List;
            ev_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        Global.hideKeyboard(Affiliate_Report_LavelActivity.this);
                        List<LevelModel> temp = new ArrayList<>();
                        for (LevelModel d : finalLevel1List) {
                            if (d.getFirstName().toLowerCase().contains(ev_search.getText().toString().toLowerCase())) {
                                temp.add(d);
                                // Log.e("Same Data ",d.getUserName());
                            }
                        }
                        lavelAdapter.updateList(temp);
                        return true;
                    }
                    return false;
                }
            });

        }

    }

    void showBottomSheetDialog(List<String> lavelName, TextView tv_lavel_name) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Affiliate_Report_LavelActivity.this,
                R.style.BottomSheetDialog);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_for_home);
        RecyclerView home_type_list = bottomSheetDialog.findViewById(R.id.home_type_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Affiliate_Report_LavelActivity.this);
        home_type_list.setLayoutManager(layoutManager);


        LavelSelectAdapter workAdapter = new LavelSelectAdapter(Affiliate_Report_LavelActivity.this,
                lavelName, desAffiliateInfo, rv_lavel_list, bottomSheetDialog, tv_lavel_name);
        home_type_list.setAdapter(workAdapter);

        bottomSheetDialog.show();
    }

    private void IntentView() {
        ev_search = findViewById(R.id.ev_search);
        rv_lavel_list = findViewById(R.id.lavel_list);
        tv_lavel_name = findViewById(R.id.tv_lavel_name);
        select_label_zone = findViewById(R.id.select_label_zone);
        select_label_zone.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(Affiliate_Report_LavelActivity.this);
        rv_lavel_list.setLayoutManager(layoutManager);
        mMainLayout = findViewById(R.id.mMainLayout);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Affiliate_Report_LavelActivity.this, mMainLayout);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                onBackPressed();
                break;
            case R.id.select_label_zone:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                showBottomSheetDialog(lavelName, tv_lavel_name);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}

class LavelSelectAdapter extends RecyclerView.Adapter<LavelSelectAdapter.InviteListDataclass> {

    public Context mCtx;
    Des_AffiliateInfo desAffiliateInfo;
    List<String> list;
    LavelAdapter lavelAdapter;
    RecyclerView rv_lavel_list;
    List<LevelModel> level1List = new ArrayList<>();
    BottomSheetDialog bottomSheetDialog;
    TextView tv_lavel_name;

    public LavelSelectAdapter(Context context, List<String> list, Des_AffiliateInfo desAffiliateInfo,
                              RecyclerView rv_lavel_list, BottomSheetDialog bottomSheetDialog, TextView tv_lavel_name) {
        this.mCtx = context;
        this.list = list;
        this.desAffiliateInfo = desAffiliateInfo;
        this.rv_lavel_list = rv_lavel_list;
        this.bottomSheetDialog = bottomSheetDialog;
        this.tv_lavel_name = tv_lavel_name;

    }

    @NonNull
    @Override
    public LavelSelectAdapter.InviteListDataclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.work_type_selecte, parent, false);
        return new InviteListDataclass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LavelSelectAdapter.InviteListDataclass holder, int position) {
        String name = list.get(position);
        holder.tv_item.setText(name);
        holder.tv_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                level1List.clear();
                tv_lavel_name.setText(name);
                switch (name) {
                    case "All":
                        level1List = desAffiliateInfo.getLevelAll();
                        break;
                    case "Lavel 1":
                        level1List = desAffiliateInfo.getLevel1();
                        break;
                    case "Lavel 2":
                        level1List = desAffiliateInfo.getLevel2();
                        break;
                    case "Lavel 3":
                        level1List = desAffiliateInfo.getLevel3();
                        break;
                    case "Lavel 4":
                        level1List = desAffiliateInfo.getLevel4();
                        break;
                    case "Lavel 5":
                        level1List = desAffiliateInfo.getLevel5();
                        break;
                }


                lavelAdapter = new LavelAdapter(mCtx, level1List);
                rv_lavel_list.setAdapter(lavelAdapter);
                bottomSheetDialog.dismiss();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class InviteListDataclass extends RecyclerView.ViewHolder {
        TextView tv_item;

        public InviteListDataclass(@NonNull View itemView) {
            super(itemView);
            tv_item = itemView.findViewById(R.id.tv_item);
        }

    }

}

class LavelAdapter extends RecyclerView.Adapter<LavelAdapter.viewholder> {

    public Context mCtx;

    List<LevelModel> level1List;
    LevelModel item;

    public LavelAdapter(Context applicationContext, List<LevelModel> level1List) {
        this.mCtx = applicationContext;
        this.level1List = level1List;

    }

    @NonNull
    @Override
    public LavelAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.lavel_item, parent, false);
        return new viewholder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LavelAdapter.viewholder holder, int position) {
        item = level1List.get(position);
        holder.tv_user_name.setText(item.getFirstName());

        String name = item.getFirstName();
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

        holder.no_image.setText(add_text);
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(mCtx);
      /*  String namebyreferrer = "";
        if (signResponseModel.getUser().getId().equals(item.getReferredBy())) {
            namebyreferrer = "you";
        } else {
            namebyreferrer = item.getReferredName();
        }*/

        if (Global.IsNotNull(item.getLevel_name())&&!item.getLevel_name().equals("")) {
            holder.layout_laval.setVisibility(View.GONE);
            holder.tv_Referrer_by.setVisibility(View.VISIBLE);
            holder.tv_Referrer_by.setText(item.getLevel_name());
        }
        else {
            holder.layout_laval.setVisibility(View.VISIBLE);
            holder.tv_Referrer_by.setVisibility(View.GONE);
            holder.tv_count.setText(String.valueOf(item.getReward_earned()));
        }

    }

    @Override
    public int getItemCount() {
        return level1List.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<LevelModel> list) {
        level1List = list;
        notifyDataSetChanged();
    }

    public static class viewholder extends RecyclerView.ViewHolder {
        TextView tv_user_name, no_image, tv_Referrer_by, tv_count;
        LinearLayout layout_laval;


        public viewholder(@SuppressLint("UnknownNullness") View view) {
            super(view);
            tv_user_name = view.findViewById(R.id.tv_user_name);
            no_image = view.findViewById(R.id.no_image);
            tv_Referrer_by = view.findViewById(R.id.tv_Referrer_by);
            layout_laval = view.findViewById(R.id.layout_laval);
            tv_count = view.findViewById(R.id.tv_count);

        }
    }
}