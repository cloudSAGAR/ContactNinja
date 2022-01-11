package com.contactninja.UserPofile;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contactninja.Model.UserData.AffiliateInfo;
import com.contactninja.Model.UserData.LevelModel;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle")
public class Affiliate_Report_LavelActivity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {
    private BroadcastReceiver mNetworkReceiver;

    LinearLayout mMainLayout;
    ImageView iv_back;
    TextView tv_lavel_name;

    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;

    RecyclerView rv_lavel_list;
    LavelAdapter lavelAdapter;
    AffiliateInfo affiliateInfo=new AffiliateInfo();
    List<String> lavelName=new ArrayList<>();
    LinearLayout select_label_zone;
    EditText contect_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affiliate_report_lavel);
        sessionManager = new SessionManager(Affiliate_Report_LavelActivity.this);
        retrofitCalls = new RetrofitCalls(Affiliate_Report_LavelActivity.this);
        loadingDialog = new LoadingDialog(Affiliate_Report_LavelActivity.this);

        IntentView();

        setdate();

        mNetworkReceiver = new ConnectivityReceiver();




    }

    private void setdate() {

        SignResponseModel user_data = SessionManager.getGetUserdata(Affiliate_Report_LavelActivity.this);

        affiliateInfo = user_data.getUser().getAffiliateInfo();
        lavelName.clear();
        if(affiliateInfo!=null){
            if(affiliateInfo.getLevel1()!=null&&affiliateInfo.getLevel1().size()!=0){
                lavelName.add(getResources().getString(R.string.lavel1));
            }
            if(affiliateInfo.getLevel2()!=null&&affiliateInfo.getLevel2().size()!=0){
                lavelName.add(getResources().getString(R.string.lavel2));
            }
            if(affiliateInfo.getLevel3()!=null&&affiliateInfo.getLevel3().size()!=0){
                lavelName.add(getResources().getString(R.string.lavel3));
            }
            if(affiliateInfo.getLevel4()!=null&&affiliateInfo.getLevel4().size()!=0){
                lavelName.add(getResources().getString(R.string.lavel4));
            }
            if(affiliateInfo.getLevel5()!=null&&affiliateInfo.getLevel5().size()!=0){
                lavelName.add(getResources().getString(R.string.lavel5));
            }


            tv_lavel_name.setText(lavelName.get(0));
            List<LevelModel> level1List=new ArrayList<>();
            switch (lavelName.get(0)){
                case "Lavel 1":
                    level1List=affiliateInfo.getLevel1();
                    break;
                case "Lavel 2":
                    level1List=affiliateInfo.getLevel2();
                    break;
                case "Lavel 3":
                    level1List=affiliateInfo.getLevel3();
                    break;
                case "Lavel 4":
                    level1List=affiliateInfo.getLevel4();
                    break;
                case "Lavel 5":
                    level1List=affiliateInfo.getLevel5();
                    break;
            }
            lavelAdapter = new LavelAdapter(Affiliate_Report_LavelActivity.this,level1List);
            rv_lavel_list.setAdapter(lavelAdapter);


            List<LevelModel> finalLevel1List = level1List;
            contect_search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    List<LevelModel> temp=new ArrayList<>();
                    for(LevelModel d: finalLevel1List){
                        if(d.getName().toLowerCase().contains(s.toString().toLowerCase())){
                            temp.add(d);
                            // Log.e("Same Data ",d.getUserName());
                        }
                    }
                    lavelAdapter.updateList(temp);
                    //groupContectAdapter.notifyDataSetChanged();
                    contect_search.setText("");
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

    }
    void showBottomSheetDialog(List<String> lavelName, TextView tv_lavel_name) {
        BottomSheetDialog  bottomSheetDialog = new BottomSheetDialog(Affiliate_Report_LavelActivity.this,
                R.style.BottomSheetDialog);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_for_home);
        RecyclerView home_type_list = bottomSheetDialog.findViewById(R.id.home_type_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Affiliate_Report_LavelActivity.this);
        home_type_list.setLayoutManager(layoutManager);


        LavelSelectAdapter workAdapter = new LavelSelectAdapter(Affiliate_Report_LavelActivity.this,
                lavelName,affiliateInfo, rv_lavel_list,bottomSheetDialog,tv_lavel_name);
        home_type_list.setAdapter(workAdapter);

        bottomSheetDialog.show();
    }
    private void IntentView() {
        contect_search =findViewById(R.id.contect_search);
        rv_lavel_list =findViewById(R.id.lavel_list);
        tv_lavel_name =findViewById(R.id.tv_lavel_name);
        select_label_zone =findViewById(R.id.select_label_zone);
        select_label_zone.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(Affiliate_Report_LavelActivity.this);
        rv_lavel_list.setLayoutManager(layoutManager);
        mMainLayout=findViewById(R.id.mMainLayout);
        iv_back=findViewById(R.id.iv_back);
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
        switch (v.getId()){
            case R.id.iv_back:
                 onBackPressed();
                break;
                case R.id.select_label_zone:
                    showBottomSheetDialog(lavelName,tv_lavel_name);
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
    AffiliateInfo affiliateInfo;
    List<String> list;
    LavelAdapter lavelAdapter;
    RecyclerView rv_lavel_list;
    List<LevelModel> level1List=new ArrayList<>();
    BottomSheetDialog bottomSheetDialog;
    TextView tv_lavel_name;
    public LavelSelectAdapter(Context context, List<String> list, AffiliateInfo affiliateInfo,
                              RecyclerView rv_lavel_list, BottomSheetDialog bottomSheetDialog, TextView tv_lavel_name) {
        this.mCtx = context;
        this.list = list;
        this.affiliateInfo = affiliateInfo;
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
        String name =list.get(position);
        holder.tv_item.setText(name);
        holder.tv_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                level1List.clear();
                tv_lavel_name.setText(name);
                switch (name){
                    case "Lavel 1":
                        level1List=affiliateInfo.getLevel1();
                    break;
                    case "Lavel 2":
                        level1List=affiliateInfo.getLevel2();
                        break;
                    case "Lavel 3":
                        level1List=affiliateInfo.getLevel3();
                        break;
                    case "Lavel 4":
                        level1List=affiliateInfo.getLevel4();
                        break;
                    case "Lavel 5":
                        level1List=affiliateInfo.getLevel5();
                        break;
                }


                lavelAdapter = new LavelAdapter(mCtx,level1List);
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
    public LavelAdapter(Context applicationContext,List<LevelModel> level1List) {
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
        item=level1List.get(position);
        holder.tv_user_name.setText(item.getName());

        String name =item.getName();
        String add_text="";
        String[] split_data=name.split(" ");
        try {
            for (int i=0;i<split_data.length;i++)
            {
                if (i==0)
                {
                    add_text=split_data[i].substring(0,1);
                }
                else {
                    add_text=add_text+split_data[i].substring(0,1);
                    break;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        holder.no_image.setText(add_text);
        SignResponseModel signResponseModel= SessionManager.getGetUserdata(mCtx);
        String namebyreferrer="";
        if(signResponseModel.getUser().getId().equals(item.getReferredBy())){
            namebyreferrer="you";
        }else {
            namebyreferrer=item.getReferredName();
        }
        holder.tv_Referrer_by.setText("Referrer by "+namebyreferrer);

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
        TextView tv_user_name,no_image,tv_Referrer_by;


        public viewholder(@SuppressLint("UnknownNullness") View view) {
            super(view);
            tv_user_name = view.findViewById(R.id.tv_user_name);
            no_image = view.findViewById(R.id.no_image);
            tv_Referrer_by = view.findViewById(R.id.tv_Referrer_by);

        }
    }
}