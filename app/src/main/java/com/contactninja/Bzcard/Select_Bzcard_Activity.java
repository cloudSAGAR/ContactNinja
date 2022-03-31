package com.contactninja.Bzcard;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.contactninja.Bzcard.CreateBzcard.Add_New_Bzcard_Activity;
import com.contactninja.MainActivity;
import com.contactninja.Model.BZcardListModel;
import com.contactninja.Model.Bzcard_Fields_Model;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Setting.WebActivity;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Select_Bzcard_Activity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener {
    ImageView iv_back;
    private BroadcastReceiver mNetworkReceiver;
    RelativeLayout mMainLayout;
    ViewPager2 viewPager2;
    TextView txt_footer, txt_Use,tv_Preview;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    List<BZcardListModel.Bizcard> bizcardList = new ArrayList<>();
    int Card_id=1;
    private long mLastClickTime=0;
    BZcardListModel bZcardListModel = new BZcardListModel();
    public static  Select_Bzcard_Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bzcard);
        activity=Select_Bzcard_Activity.this;
        mNetworkReceiver = new ConnectivityReceiver();
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(Select_Bzcard_Activity.this);
        loadingDialog = new LoadingDialog(Select_Bzcard_Activity.this);
        initUI();


    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (Global.isNetworkAvailable(Select_Bzcard_Activity.this, MainActivity.mMainLayout)) {
                Data_list();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void Data_list() throws JSONException {

        loadingDialog.showLoadingDialog();

        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Select_Bzcard_Activity.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        obj.add("data", paramObject);
        retrofitCalls.BZcard_list(sessionManager, obj, loadingDialog, token, Global.getVersionname(Select_Bzcard_Activity.this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    bizcardList.clear();
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<BZcardListModel>() {
                    }.getType();
                     bZcardListModel = new Gson().fromJson(headerString, listType);
                     bizcardList = bZcardListModel.getBizcard();
                    setImage();
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });


    }

    private void setImage() {
        viewPager2.setAdapter(new ViewPageAdepter(getApplicationContext(),  bizcardList));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePagerTransformer = new CompositePageTransformer();
        compositePagerTransformer.addTransformer(new MarginPageTransformer(40));
        compositePagerTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);

                txt_footer.setText(getResources().getString(R.string.bz_footer));
  /*              if (viewPager2.getCurrentItem() == 0) {
                } else if (viewPager2.getCurrentItem() == 1) {
                    txt_footer.setText(getResources().getString(R.string.bz_footer2));
                } else if (viewPager2.getCurrentItem() == 2) {
                    txt_footer.setText(getResources().getString(R.string.bz_footer));
                } else if (viewPager2.getCurrentItem() == 3) {
                    txt_footer.setText(getResources().getString(R.string.bz_footer2));
                } else if (viewPager2.getCurrentItem() == 4) {
                    txt_footer.setText(getResources().getString(R.string.bz_footer));
                } else if (viewPager2.getCurrentItem() == 5) {
                    txt_footer.setText(getResources().getString(R.string.bz_footer2));
                }
*/

            }
        });
        viewPager2.setPageTransformer(compositePagerTransformer);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initUI() {
        mMainLayout = findViewById(R.id.mMainLayout);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
        viewPager2 = findViewById(R.id.viewpager);
        txt_footer = findViewById(R.id.txt_footer);
        txt_Use = findViewById(R.id.txt_Use);
        tv_Preview = findViewById(R.id.tv_Preview);
        txt_Use.setOnClickListener(this);
        tv_Preview.setOnClickListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Select_Bzcard_Activity.this, mMainLayout);
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
                onBackPressed();
                break;
            case R.id.txt_Use:
                if(bZcardListModel.getUser_total()<5){
                    SessionManager.setBzcard(getApplicationContext(), new BZcardListModel.Bizcard());
                    BZcardListModel.Bizcard main_model;
                    main_model = SessionManager.getBzcard(this);
                    for(int i=0;i<bizcardList.size();i++){
                        if(viewPager2.getCurrentItem()==i){
                            Card_id=bizcardList.get(i).getId();
                            break;
                        }
                    }
                    main_model.setCard_id(Card_id);
                    SessionManager.setBzcard(this, main_model);
                    startActivity(new Intent(getApplicationContext(), Add_New_Bzcard_Activity.class));
                }else {
                    Global.Messageshow(getApplicationContext(),mMainLayout,"only 5 card add",false);
                }

                break;
                case R.id.tv_Preview:
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    Intent intent = new Intent(getApplicationContext(), WebActivity.class);
                    if (viewPager2.getCurrentItem() == 0) {
                        intent.putExtra("WebUrl", Global.bzcard_master +1);
                    } else if (viewPager2.getCurrentItem() == 1) {
                        intent.putExtra("WebUrl", Global.bzcard_master+2);
                    } else if (viewPager2.getCurrentItem() == 2) {
                        intent.putExtra("WebUrl",Global.bzcard_master+3);
                    } else if (viewPager2.getCurrentItem() == 3) {
                        intent.putExtra("WebUrl", Global.bzcard_master+4);
                    } else if (viewPager2.getCurrentItem() == 4) {
                        intent.putExtra("WebUrl", Global.bzcard_master+5);
                    } else if (viewPager2.getCurrentItem() == 5) {
                        intent.putExtra("WebUrl",Global.bzcard_master +6);
                    }
                    startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public static class ViewPageAdepter extends RecyclerView.Adapter<ViewPageAdepter.viewholder> {

        public Context mCtx;
        List<BZcardListModel.Bizcard> bizcardList = new ArrayList<>();

        public ViewPageAdepter(Context applicationContext, List<BZcardListModel.Bizcard> bizcardList) {
            this.mCtx = applicationContext;
            this.bizcardList = bizcardList;
        }

        @NonNull
        @Override
        public ViewPageAdepter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.bzcard_fix_item_select, parent, false);
            return new ViewPageAdepter.viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewPageAdepter.viewholder holder, int position) {
            BZcardListModel.Bizcard bizcard = bizcardList.get(position);

            int resID = mCtx.getResources().getIdentifier(bizcard.getCardName()
                    .replace(" ", "_").toLowerCase(), "drawable", mCtx.getPackageName());
            if (resID != 0) {
                Glide.with(mCtx.getApplicationContext()).load(resID).into(holder.iv_card);
            }

        }

        @Override
        public int getItemCount() {
            return bizcardList.size();
        }

        public static class viewholder extends RecyclerView.ViewHolder {
            ImageView iv_card;

            public viewholder(View view) {
                super(view);
                iv_card = view.findViewById(R.id.iv_card);

            }
        }
    }
}