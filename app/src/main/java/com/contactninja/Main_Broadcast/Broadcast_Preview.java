package com.contactninja.Main_Broadcast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.contactninja.Campaign.Add_Camp_First_Step_Activity;
import com.contactninja.Campaign.Campaign_Name_Activity;
import com.contactninja.Campaign.ContectAndGroup_Actvity;
import com.contactninja.Campaign.List_itm.Campaign_List_Activity;
import com.contactninja.Model.CampaignTask;
import com.contactninja.Model.CampaignTask_overview;
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
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Broadcast_Preview extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {
    public static TopUserListDataAdapter topUserListDataAdapter;
    ImageView iv_back;
    TextView save_button,tv_start_broadcast;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    RecyclerView  user_contect;
    /* List<String> stringList;*/
    List<CampaignTask_overview.SequenceTask> campaignTasks = new ArrayList<>();
    int sequence_id, sequence_task_id;
    BottomSheetDialog bottomSheetDialog;
    TextView tv_name;
    TextView contect_count;
    ImageView iv_toolbar_manu;
    Toolbar toolbar;
    RelativeLayout contect_layout;
    ImageView add_icon,iv_camp_edit;
    LinearLayout layout_toolbar_logo;
    ConstraintLayout mMainLayout;
    List<CampaignTask_overview.SequenceTask> main_data = new ArrayList<>();
    String Camp_name = "";
    private BroadcastReceiver mNetworkReceiver;
    LinearLayout layout_name;
    private long mLastClickTime=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_preview);
        mNetworkReceiver = new ConnectivityReceiver();
        IntentUI();
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        Global.count = 1;
        toolbar.inflateMenu(R.menu.option_menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        //  Toast.makeText(getApplicationContext(), "Manu Clcik", Toast.LENGTH_LONG).show();
        //Log.e("Option Manu is Select", "Yes");
        switch (item.getItemId()) {
            case R.id.mv_save:
                startActivity(new Intent(getApplicationContext(), Campaign_List_Activity.class));
                finish();
                return true;
            case R.id.mv_edit:
                SessionManager.setCampign_flag("edit");
                Intent intent = new Intent(getApplicationContext(), Broadcast_Preview.class);
                intent.putExtra("sequence_id", sequence_id);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("WrongViewCast")
    private void IntentUI() {
        tv_start_broadcast=findViewById(R.id.tv_start_broadcast);
        tv_start_broadcast.setOnClickListener(this);
        iv_camp_edit=findViewById(R.id.iv_camp_edit);
        iv_camp_edit.setOnClickListener(this);
        layout_name=findViewById(R.id.layout_name);
        mMainLayout = findViewById(R.id.mMainLayout);
        add_icon = findViewById(R.id.add_icon);
        add_icon.setVisibility(View.GONE);
        add_icon.setOnClickListener(this);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button = findViewById(R.id.save_button);
        save_button.setVisibility(View.VISIBLE);
        save_button.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        save_button.setText("Done");
        layout_toolbar_logo = findViewById(R.id.layout_toolbar_logo);
        layout_toolbar_logo.setVisibility(View.GONE);
        save_button.setVisibility(View.GONE);
        tv_name = findViewById(R.id.tv_name);
        layout_name.setOnClickListener(this);
        tv_name.setOnClickListener(this);
        user_contect = findViewById(R.id.user_contect);
        user_contect.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        user_contect.setOnClickListener(this);
        iv_toolbar_manu = findViewById(R.id.iv_toolbar_manu);
        iv_toolbar_manu.setOnClickListener(this);

        contect_count = findViewById(R.id.contect_count);
        contect_count.setOnClickListener(this);
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.option_menu);
        setSupportActionBar(toolbar);
        contect_layout = findViewById(R.id.contect_layout);
        contect_layout.setOnClickListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Broadcast_Preview.this, mMainLayout);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
            case R.id.tv_start_broadcast:
                Intent intent=new Intent(getApplicationContext(),Brodcsast_Tankyou.class);
                intent.putExtra("s_name","final");
                startActivity(intent);
                break;

            case R.id.iv_toolbar_manu:

                break;
            case R.id.add_icon:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                SessionManager.setContect_flag("edit");
                Intent intent_two = new Intent(getApplicationContext(), ContectAndGroup_Actvity.class);
                intent_two.putExtra("sequence_id", sequence_id);
                intent_two.putExtra("seq_task_id", sequence_task_id);
                startActivity(intent_two);
                finish();
                break;
            case R.id.contect_count:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                SessionManager.setContect_flag("read");
                Intent intent1 = new Intent(getApplicationContext(), ContectAndGroup_Actvity.class);
                intent1.putExtra("sequence_id", sequence_id);
                intent1.putExtra("seq_task_id", sequence_task_id);
                startActivity(intent1);
                break;

            case R.id.user_contect:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (SessionManager.getTask(getApplicationContext()).size() != 0) {
                    sequence_id = SessionManager.getTask(getApplicationContext()).get(0).getSequenceId();
                } else {
                    Intent getintent = getIntent();
                    Bundle bundle = getintent.getExtras();
                    sequence_id = bundle.getInt("sequence_id");
                }
                SessionManager.setgroup_broadcste(getApplicationContext(), new ArrayList<>());
                SessionManager.setGroupList(this, new ArrayList<>());

                break;
            case R.id.save_button:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Global.hideKeyboard(Broadcast_Preview.this);
                Intent intent2=new Intent(getApplicationContext(),Brodcsast_Tankyou.class);
                intent2.putExtra("s_name","final");
                startActivity(intent2);


              /*  if (Camp_name.equals(tv_name.getText().toString())) {
                    SessionManager.setCampign_flag("read");
                    Intent in = new Intent(getApplicationContext(), Broadcast_Preview.class);
                    in.putExtra("sequence_id", sequence_id);
                    startActivity(in);
                    finish();
                } else {
                    if(!tv_name.getText().toString().equals("")){

                    }else {
                        Global.Messageshow(getApplicationContext(),mMainLayout,getResources().getString(R.string.enter_campaign_name),false);
                    }
                }*/

                break;

            case R.id.layout_name:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                Intent name_intent = new Intent(getApplicationContext(), Campaign_Name_Activity.class);
                name_intent.putExtra("sequence_id", sequence_id);
                name_intent.putExtra("seq_task_id", sequence_task_id);
                name_intent.putExtra("sequence_Name", tv_name.getText().toString());
                name_intent.putExtra("flag","edit");
                startActivity(name_intent);
              //  finish();
                break;

            case R.id.iv_camp_edit:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Intent name_intent1 = new Intent(getApplicationContext(), Campaign_Name_Activity.class);
                name_intent1.putExtra("sequence_id", sequence_id);
                name_intent1.putExtra("seq_task_id", sequence_task_id);
                name_intent1.putExtra("sequence_Name", tv_name.getText().toString());
                name_intent1.putExtra("flag","edit");
                startActivity(name_intent1);
                //finish();
                break;

            case R.id.tv_name:

                break;

        }
    }

    @Override
    public void onBackPressed() {
        if (SessionManager.getCampign_flag(getApplicationContext()).equals("read")) {

            startActivity(new Intent(getApplicationContext(), Campaign_List_Activity.class));
            finish();
        } else if (SessionManager.getCampign_flag(getApplicationContext()).equals("edit")) {
            startActivity(new Intent(getApplicationContext(), Campaign_List_Activity.class));
            finish();
        } else if (SessionManager.getCampign_flag(getApplicationContext()).equals("read_name")) {
            //  startActivity(new Intent(getApplicationContext(),Campaign_List_Activity.class));
            finish();
        } else {
            finish();
        }
        super.onBackPressed();
    }

    private void broadcast_manu(CampaignTask_overview.SequenceTask sequenceTask, int position) {
        final View mView = getLayoutInflater().inflate(R.layout.brodcaste_campaign_manu, null);
        bottomSheetDialog = new BottomSheetDialog(Broadcast_Preview.this, R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);


        LinearLayout delete_layout = bottomSheetDialog.findViewById(R.id.delete_layout);
        LinearLayout edit_layout = bottomSheetDialog.findViewById(R.id.edit_layout);

        delete_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        edit_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (sequenceTask.getType().equals("EMAIL")) {
                    if (SessionManager.getTask(getApplicationContext()).size() != 0) {
                        sequence_id = SessionManager.getTask(getApplicationContext()).get(0).getSequenceId();
                    } else {
                        Intent getintent = getIntent();
                        Bundle bundle = getintent.getExtras();
                        sequence_id = bundle.getInt("sequence_id");
                    }
                    Log.e("Sequence is is", String.valueOf(sequence_id));

                    Intent intent = new Intent(getApplicationContext(), Add_Camp_First_Step_Activity.class);
                    intent.putExtra("flag", "edit");
                    intent.putExtra("body", sequenceTask.getContentBody());
                    intent.putExtra("day", sequenceTask.getDay());
                    intent.putExtra("manage_by", sequenceTask.getManageBy());
                    intent.putExtra("seq_task_id", sequenceTask.getId());
                    intent.putExtra("sequence_id", sequence_id);
                    intent.putExtra("type", sequenceTask.getType());
                    intent.putExtra("minute", sequenceTask.getMinute());
                    intent.putExtra("header", sequenceTask.getContentHeader());
                    intent.putExtra("step", sequenceTask.getStepNo());
                    intent.putExtra("from_ac",sequenceTask.getMail_module());
                    intent.putExtra("from_ac_id",sequenceTask.getSent_tbl_id());
                    startActivity(intent);
                    finish();
                    // startActivity(new Intent(getActivity(),First_Step_Activity.class));
                    SessionManager.setCampaign_Day(String.valueOf(sequenceTask.getDay()));
                    SessionManager.setCampaign_minute(String.valueOf(sequenceTask.getMinute()));
                    SessionManager.setCampaign_type(String.valueOf(sequenceTask.getType()));
                    SessionManager.setCampaign_type_name(String.valueOf(sequenceTask.getManageBy()));

//                    finish();
                    bottomSheetDialog.cancel();

                } else {

                    Log.e("sequence id", String.valueOf(sequence_id));


                    Log.e("Sequence is is", String.valueOf(sequence_id));
                    if (SessionManager.getTask(getApplicationContext()).size() != 0) {
                        sequence_id = SessionManager.getTask(getApplicationContext()).get(0).getSequenceId();
                    } else {
                        Intent getintent = getIntent();
                        Bundle bundle = getintent.getExtras();
                        sequence_id = bundle.getInt("sequence_id");
                    }
                    Intent intent = new Intent(getApplicationContext(), Add_Camp_First_Step_Activity.class);
                    intent.putExtra("flag", "edit");
                    intent.putExtra("body", sequenceTask.getContentBody());
                    intent.putExtra("day", sequenceTask.getDay());
                    intent.putExtra("manage_by", sequenceTask.getManageBy());
                    intent.putExtra("seq_task_id", sequenceTask.getId());
                    intent.putExtra("sequence_id", sequence_id);
                    intent.putExtra("type", sequenceTask.getType());
                    intent.putExtra("minute", sequenceTask.getMinute());
                    intent.putExtra("step", sequenceTask.getStepNo());
                    intent.putExtra("from_ac",sequenceTask.getMail_module());
                    intent.putExtra("from_ac_id",sequenceTask.getSent_tbl_id());
                    startActivity(intent);
                    //  SessionManager.setTask(getActivity(),campaignTasks1);
                    SessionManager.setCampaign_Day(String.valueOf(sequenceTask.getDay()));
                    SessionManager.setCampaign_minute(String.valueOf(sequenceTask.getMinute()));
                    SessionManager.setCampaign_type(String.valueOf(sequenceTask.getType()));
                    SessionManager.setCampaign_type_name(String.valueOf(sequenceTask.getManageBy()));

                    //                finish();
                    bottomSheetDialog.cancel();
                }
                //              finish();
                bottomSheetDialog.cancel();

            }
        });
        bottomSheetDialog.show();

    }




    public void onResume() {
        loadingDialog.cancelLoading();
        super.onResume();
    }


    public static class TopUserListDataAdapter extends RecyclerView.Adapter<TopUserListDataAdapter.InviteListDataclass> {

        private final Context mcntx;
        private final List<CampaignTask_overview.SequenceProspect> userDetailsfull;
        private final List<CampaignTask_overview.SequenceProspect> userDetails;
        public Activity mCtx;
        int last_postion = 0;
        String second_latter = "";
        String current_latter = "", image_url = "";


        public TopUserListDataAdapter(Activity Ctx, Context mCtx, List<CampaignTask_overview.SequenceProspect> userDetails) {
            this.mcntx = mCtx;
            this.mCtx = Ctx;
            this.userDetails = userDetails;
            userDetailsfull = new ArrayList<>(userDetails);
        }

        @NonNull
        @Override
        public InviteListDataclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.top_user_details, parent, false);
            return new InviteListDataclass(view);
        }

        @Override
        public void onBindViewHolder(@NonNull InviteListDataclass holder, int position) {
            CampaignTask_overview.SequenceProspect inviteUserDetails = userDetails.get(position);
            last_postion = position;
            holder.userName.setText(inviteUserDetails.getFirstname());
            holder.top_layout.setVisibility(View.VISIBLE);

            if(Global.IsNotNull(inviteUserDetails.getFirstname())||!inviteUserDetails.getFirstname().equals("")){
                String first_latter =inviteUserDetails.getFirstname().substring(0, 1).toUpperCase();

                if (second_latter.equals("")) {
                    current_latter = first_latter;
                    second_latter = first_latter;

                } else if (second_latter.equals(first_latter)) {
                    current_latter = second_latter;
                } else {

                    current_latter = first_latter;
                    second_latter = first_latter;
                }
            }





            holder.no_image.setVisibility(View.VISIBLE);
            holder.profile_image.setVisibility(View.GONE);
            String name = inviteUserDetails.getFirstname();
            holder.profile_image.setVisibility(View.GONE);
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


            holder.no_image.setText(add_text);
            holder.no_image.setVisibility(View.VISIBLE);

        }

        @Override
        public int getItemCount() {
            return userDetails.size();
        }

        public static class InviteListDataclass extends RecyclerView.ViewHolder {

            TextView no_image;
            TextView userName;
            CircleImageView profile_image;
            LinearLayout top_layout;


            public InviteListDataclass(@NonNull View itemView) {
                super(itemView);

                userName = itemView.findViewById(R.id.username);
                profile_image = itemView.findViewById(R.id.profile_image);
                no_image = itemView.findViewById(R.id.no_image);
                top_layout = itemView.findViewById(R.id.main_layout);


            }

        }

    }

}
