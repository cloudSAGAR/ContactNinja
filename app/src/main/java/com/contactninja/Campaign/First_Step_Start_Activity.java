package com.contactninja.Campaign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.contactninja.Auth.Phone_email_verificationActivity;
import com.contactninja.Interface.TextClick;
import com.contactninja.MainActivity;
import com.contactninja.Model.CampaignTask;

import com.contactninja.Model.HastagList;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.Model.UservalidateModel;
import com.contactninja.R;
import com.contactninja.Setting.TemplateCreateActivity;
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
import java.util.List;

import retrofit2.Response;

public class First_Step_Start_Activity extends AppCompatActivity implements View.OnClickListener , TextClick {
    ImageView iv_back;
    TextView save_button,tv_use_tamplet;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    RecyclerView rv_direct_list;
    PicUpTextAdepter picUpTextAdepter;
    TemplateAdepter templateAdepter;
    EditText edit_template,edit_template_name;
    List<HastagList.TemplateText> templateTextList=new ArrayList<>();
    List<HastagList.TemplateText> templateTextList1=new ArrayList<>();
    CoordinatorLayout mMainLayout;
    String step_no="1",time="09:00",day="1",minite="0",sequence_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_step_start);
        loadingDialog=new LoadingDialog(this);
        sessionManager=new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        IntentUI();
        Listset();

        edit_template.requestFocus();
    }

    private void bouttomSheet() {
        templateTextList1.clear();
        @SuppressLint("InflateParams") final View mView = getLayoutInflater().inflate(R.layout.template_list_dialog_item, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(First_Step_Start_Activity.this, R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);
        //  LinearLayout layout_list_template=bottomSheetDialog.findViewById(R.id.layout_list_template);
        TextView tv_error=bottomSheetDialog.findViewById(R.id.tv_error);
        RecyclerView templet_list=bottomSheetDialog.findViewById(R.id.templet_list);
        templet_list.setVisibility(View.VISIBLE);
        Listset1();
        templet_list.setLayoutManager(new LinearLayoutManager(this));
        templateAdepter = new TemplateAdepter(getApplicationContext(), templateTextList1, this);
        templet_list.setAdapter(templateAdepter);

        tv_error.setVisibility(View.GONE);

        bottomSheetDialog.show();
    }

    private void Listset() {
        for(int i=0;i<=4;i++){
            HastagList.TemplateText templateText=new HastagList.TemplateText();

            if(i==0){
                templateText.setDescription("Placeholders #");
                templateText.setSelect(true);
            }else if(i==1){
                templateText.setDescription("First Name");
                templateText.setSelect(false);
            }else if(i==2){
                templateText.setDescription("Last Name");
                templateText.setSelect(false);
            }else if(i==3){
                templateText.setDescription("Hi");
                templateText.setSelect(false);
            }else if(i==4){
                templateText.setDescription("Hello");
                templateText.setSelect(false);
            }
            templateTextList.add(i,templateText);
        }
        rv_direct_list.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_direct_list.setHasFixedSize(true);
        picUpTextAdepter = new PicUpTextAdepter(getApplicationContext(), templateTextList, this);
        rv_direct_list.setAdapter(picUpTextAdepter);
    }
    private void Listset1() {
        for(int i=0;i<=4;i++){
            HastagList.TemplateText templateText1=new HastagList.TemplateText();
            if(i==0){
                templateText1.setDescription("Please select template");
                templateText1.setSelect(false);
            }
            if(i==1){
                templateText1.setDescription("New member joining");
                templateText1.setSelect(true);
            }else if(i==2){
                templateText1.setDescription("Customer service");
                templateText1.setSelect(true);
            }else if(i==3){
                templateText1.setDescription("New Product development");
                templateText1.setSelect(true);
            }else if(i==4){
                templateText1.setDescription("Save Current as template");
                templateText1.setSelect(true);
            }
            templateTextList1.add(i,templateText1);
        }

    }
    private void IntentUI() {
        iv_back=findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button=findViewById(R.id.save_button);
        save_button.setVisibility(View.VISIBLE);
        save_button.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        save_button.setText("Next");

        edit_template_name = findViewById(R.id.edit_template_name);
        rv_direct_list = findViewById(R.id.rv_direct_list);
        tv_use_tamplet=findViewById(R.id.tv_use_tamplet);
        edit_template = findViewById(R.id.edit_template);
        tv_use_tamplet.setOnClickListener(this);
        mMainLayout=findViewById(R.id.mMainLayout);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.save_button:
                //Add Api Call
                Global.count=1;
                if (edit_template.getText().equals(""))
                {
                    Global.Messageshow(getApplicationContext(),mMainLayout,"ERROR",false);

                }
                else {
                    StepData();
                }


                break;
            case R.id.tv_use_tamplet:
                bouttomSheet();
                break;

        }
    }


    public void OnClick(@SuppressLint("UnknownNullness") String s) {
        String curenttext=edit_template.getText().toString();
        String Newtext=curenttext+"{"+s+"}";
        edit_template.setText(Newtext);
        edit_template.setSelection(edit_template.getText().length());
    }

    static class PicUpTextAdepter extends RecyclerView.Adapter<PicUpTextAdepter.viewholder>{

        public Context mCtx;
        List<HastagList.TemplateText> templateTextList;
        TextClick interfaceClick;
        public PicUpTextAdepter(Context applicationContext, List<HastagList.TemplateText> templateTextList,TextClick interfaceClick) {
            this.mCtx=applicationContext;
            this.templateTextList=templateTextList;
            this.interfaceClick=interfaceClick;
        }

        @NonNull
        @Override
        public PicUpTextAdepter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.template_text_selecte, parent, false);
            return new PicUpTextAdepter.viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PicUpTextAdepter.viewholder holder, int position) {
            HastagList.TemplateText item=templateTextList.get(position);
            holder.tv_item.setText(item.getId());
            holder.tv_item.setBackgroundResource(R.drawable.shape_unselect_back);
            holder.tv_item.setTextColor(mCtx.getResources().getColor(R.color.tv_medium));
            if(item.isSelect()){
                holder.tv_item.setBackground(null);
                holder.tv_item.setTextColor(mCtx.getResources().getColor(R.color.tv_medium));
                holder.line_view.setVisibility(View.VISIBLE);
            }else {
                holder.line_view.setVisibility(View.GONE);
            }
            holder.tv_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!item.isSelect()){
                        Handler handler = new Handler();
                        Runnable r = new Runnable() {
                            @SuppressLint("NotifyDataSetChanged")
                            public void run() {
                                notifyDataSetChanged();
                            }
                        };
                        handler.postDelayed(r, 1000);
                        holder.tv_item.setBackgroundResource(R.drawable.shape_blue_back);
                        holder.tv_item.setTextColor(mCtx.getResources().getColor(R.color.white));
                        interfaceClick.OnClick(item.getDescription());
                    }
                }
            });




        }

        @Override
        public int getItemCount() {
            return templateTextList.size();
        }

        public class viewholder extends RecyclerView.ViewHolder {
            TextView tv_item;
            View line_view;
            public viewholder(View view) {
                super(view);
                tv_item=view.findViewById(R.id.tv_item);
                line_view=view.findViewById(R.id.line_view);
            }
        }
    }





    class TemplateAdepter extends RecyclerView.Adapter<TemplateAdepter.viewholder>{

        public Context mCtx;
        List<HastagList.TemplateText> templateTextList1;
        TextClick interfaceClick;
        public TemplateAdepter(Context applicationContext, List<HastagList.TemplateText> templateTextList1,TextClick interfaceClick) {
            this.mCtx=applicationContext;
            this.templateTextList1=templateTextList1;
            this.interfaceClick=interfaceClick;
        }

        @NonNull
        @Override
        public TemplateAdepter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.select_templet_layout, parent, false);
            return new TemplateAdepter.viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TemplateAdepter.viewholder holder, int position) {
            HastagList.TemplateText item=templateTextList1.get(position);
            holder.tv_item.setText(item.getDescription());
            //holder.tv_item.setBackgroundResource(R.drawable.shape_unselect_back);
            holder.tv_item.setTextColor(mCtx.getResources().getColor(R.color.tv_medium));
            if(item.isSelect()){
                holder.tv_item.setTextColor(mCtx.getResources().getColor(R.color.purple_200));
                holder.line_view.setVisibility(View.VISIBLE);
            }else {
                holder.line_view.setVisibility(View.GONE);
            }

            holder.tv_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.tv_item.getText().toString().equals("Save Current as template"))
                    {
                        showAlertDialogButtonClicked(view);
                    }
                }
            });




        }

        @Override
        public int getItemCount() {
            return templateTextList1.size();
        }

        public class viewholder extends RecyclerView.ViewHolder {
            TextView tv_item;
            View line_view;
            public viewholder(View view) {
                super(view);
                tv_item=view.findViewById(R.id.selected_broadcast);
                line_view=view.findViewById(R.id.line_view);
            }
        }
    }

    public void showAlertDialogButtonClicked(View view) {

        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(this, R.style.BottomSheetDialog);
        final View customLayout
                = getLayoutInflater()
                .inflate(
                        R.layout.add_titale_for_templet,
                        null);
        builder.setView(customLayout);
        EditText editText = customLayout.findViewById(R.id.editText);
        TextView tv_cancel = customLayout.findViewById(R.id.tv_cancel);
        TextView tv_add = customLayout.findViewById(R.id.tv_add);
        AlertDialog dialog
                = builder.create();

        dialog.show();
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


    public void StepData() {
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(this);
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());

        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("content_body", edit_template.getText().toString());
        paramObject.addProperty("day", "1");
        paramObject.addProperty("manage_by", sessionManager.getCampaign_type_name(getApplicationContext()));
        paramObject.addProperty("minute",minite );
        paramObject.addProperty("organization_id","1");
        paramObject.addProperty("sequence_id", sequence_id);
        paramObject.addProperty("step_no", step_no);
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("type", sessionManager.getCampaign_type(getApplicationContext()));
        paramObject.addProperty("time", time);
        paramObject.addProperty("user_id", user_id);
        obj.add("data", paramObject);
        retrofitCalls.Task_store(sessionManager,obj, loadingDialog, Global.getToken(sessionManager),new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                //Log.e("Response is",new Gson().toJson(response));

                loadingDialog.cancelLoading();

                if (response.body().getStatus() == 200) {

                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<CampaignTask>() {
                    }.getType();
                    CampaignTask user_model = new Gson().fromJson(headerString, listType);
                    List<CampaignTask> campaignTasks=new ArrayList<>();
                    campaignTasks.add(user_model);
                    SessionManager.setTask(getApplicationContext(), campaignTasks);
                    startActivity(new Intent(getApplicationContext(),Campaign_Overview.class));
                }
                else {
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Global.Messageshow(getApplicationContext(), mMainLayout, headerString, false);

                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });
    }

}