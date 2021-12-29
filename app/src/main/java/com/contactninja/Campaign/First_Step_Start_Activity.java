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

import com.contactninja.Interface.TemplateClick;
import com.contactninja.Interface.TextClick;
import com.contactninja.MainActivity;
import com.contactninja.Model.CampaignTask;

import com.contactninja.Model.HastagList;
import com.contactninja.Model.TemplateList;
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

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class First_Step_Start_Activity extends AppCompatActivity implements View.OnClickListener , TextClick,TemplateClick {
    ImageView iv_back;
    TextView save_button,tv_use_tamplet,tv_step;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    RecyclerView rv_direct_list;
    PicUpTextAdepter picUpTextAdepter;
    TemplateAdepter templateAdepter;
    EditText edit_template,edit_template_name;
    List<HastagList.TemplateText> templateTextList=new ArrayList<>();
    List<TemplateList.Template> templateList=new ArrayList<>();
    CoordinatorLayout mMainLayout;
    String step_no="1",time="09:00",day="1",minite="0",sequence_id="";
    TemplateClick templateClick;
    BottomSheetDialog bottomSheetDialog_templateList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_step_start);
        templateClick=First_Step_Start_Activity.this;
        loadingDialog=new LoadingDialog(this);
        sessionManager=new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        IntentUI();
        try {
            if (Global.isNetworkAvailable(First_Step_Start_Activity.this, MainActivity.mMainLayout)) {
                Hastag_list();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        edit_template.requestFocus();
        if (SessionManager.getTask(getApplicationContext()).size()==0)
        {
            String step_id= String.valueOf(SessionManager.getTask(getApplicationContext()).size()+1);
            String stpe_tyep=SessionManager.getCampaign_type_name(getApplicationContext());
            tv_step.setText("Step#"+step_id+"("+stpe_tyep+" "+SessionManager.getCampaign_type(getApplicationContext())+")");
        }
        else {
            String step_id= String.valueOf(SessionManager.getTask(getApplicationContext()).size()+1);
            String stpe_tyep=SessionManager.getCampaign_type_name(getApplicationContext());
            tv_step.setText("Step#"+step_id+"("+stpe_tyep+" "+SessionManager.getCampaign_type(getApplicationContext())+")");

        }
    }

    private void bouttomSheet() {
        @SuppressLint("InflateParams") final View mView = getLayoutInflater().inflate(R.layout.template_list_dialog_item, null);
        bottomSheetDialog_templateList = new BottomSheetDialog(First_Step_Start_Activity.this, R.style.CoffeeDialog);
        bottomSheetDialog_templateList.setContentView(mView);
        //  LinearLayout layout_list_template=bottomSheetDialog.findViewById(R.id.layout_list_template);
        TextView tv_error= bottomSheetDialog_templateList.findViewById(R.id.tv_error);
        RecyclerView templet_list= bottomSheetDialog_templateList.findViewById(R.id.templet_list);
        templet_list.setVisibility(View.VISIBLE);

        try {
            if(Global.isNetworkAvailable(First_Step_Start_Activity.this, MainActivity.mMainLayout)) {
                Template_list(templet_list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        tv_error.setVisibility(View.GONE);


    }
    private void Template_list(RecyclerView templet_list) throws JSONException {
        SignResponseModel signResponseModel= SessionManager.getGetUserdata(First_Step_Start_Activity.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        obj.add("data", paramObject);
        retrofitCalls.Template_list(sessionManager,obj, loadingDialog, token, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                if (response.body().getStatus() == 200) {
                    loadingDialog.cancelLoading();
                    templateList.clear();
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<TemplateList>() {
                    }.getType();
                    TemplateList list=new Gson().fromJson(headerString, listType);
                    String CurrentType=SessionManager.getCampaign_type(getApplicationContext());

                    TemplateList.Template template2=new TemplateList.Template();
                    template2.setTemplateName("Please select template");
                    template2.setSelect(false);
                    templateList.add(0,template2);

                    for(int i=0;i<list.getTemplate().size();i++){
                        if(CurrentType.equals("Email")){
                            if(list.getTemplate().get(i).getType().equals("EMAIL")){
                                TemplateList.Template template=new TemplateList.Template();
                                template.setId(list.getTemplate().get(i).getId());
                                template.setOrganizationId(list.getTemplate().get(i).getOrganizationId());
                                template.setTeamId(list.getTemplate().get(i).getTeamId());
                                template.setTemplateName(list.getTemplate().get(i).getTemplateName());
                                template.setTemplateSlug(list.getTemplate().get(i).getTemplateSlug());
                                template.setContentHeader(list.getTemplate().get(i).getContentHeader());
                                template.setType(list.getTemplate().get(i).getType());
                                template.setContentBody(list.getTemplate().get(i).getContentBody());
                                template.setStatus(list.getTemplate().get(i).getStatus());
                                template.setCreatedAt(list.getTemplate().get(i).getCreatedAt());
                                template.setUpdatedAt(list.getTemplate().get(i).getUpdatedAt());
                                templateList.add(template);
                            }
                        }else {
                            if(list.getTemplate().get(i).getType().equals("SMS")){
                                TemplateList.Template template=new TemplateList.Template();
                                template.setId(list.getTemplate().get(i).getId());
                                template.setOrganizationId(list.getTemplate().get(i).getOrganizationId());
                                template.setTeamId(list.getTemplate().get(i).getTeamId());
                                template.setTemplateName(list.getTemplate().get(i).getTemplateName());
                                template.setTemplateSlug(list.getTemplate().get(i).getTemplateSlug());
                                template.setContentHeader(list.getTemplate().get(i).getContentHeader());
                                template.setType(list.getTemplate().get(i).getType());
                                template.setContentBody(list.getTemplate().get(i).getContentBody());
                                template.setStatus(list.getTemplate().get(i).getStatus());
                                template.setCreatedAt(list.getTemplate().get(i).getCreatedAt());
                                template.setUpdatedAt(list.getTemplate().get(i).getUpdatedAt());
                                templateList.add(template);
                            }
                        }
                    }



                    TemplateList.Template template1=new TemplateList.Template();
                    template1.setTemplateName("Save Current as template");
                    template1.setSelect(true);
                    templateList.add(templateList.size(),template1);


                    templet_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    templateAdepter = new TemplateAdepter(getApplicationContext(), templateList, templateClick);
                    templet_list.setAdapter(templateAdepter);
                    bottomSheetDialog_templateList.show();
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }


        });


    }
    private void Hastag_list() throws JSONException {

        SignResponseModel signResponseModel = SessionManager.getGetUserdata(First_Step_Start_Activity.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        obj.add("data", paramObject);
        retrofitCalls.Hastag_list(sessionManager, obj, loadingDialog, token, new RetrofitCallback() {
            @SuppressLint("SyntheticAccessor")
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                if (response.body().getStatus() == 200) {
                    templateTextList.clear();
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<HastagList>() {
                    }.getType();
                    HastagList hastagList = new Gson().fromJson(headerString, listType);
                    templateTextList = hastagList.getHashtag();
                    HastagList.TemplateText templateText = new HastagList.TemplateText();
                    templateText.setDescription("Placeholders #");
                    templateText.setSelect(true);
                    templateTextList.add(0, templateText);

                    Listset(templateTextList);

                    //   sessionManager.setUserdata(getApplicationContext(),data);
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }


        });


    }
    private void Listset(List<HastagList.TemplateText> templateTextList) {

        rv_direct_list.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_direct_list.setHasFixedSize(true);
        picUpTextAdepter = new PicUpTextAdepter(getApplicationContext(), templateTextList, this);
        rv_direct_list.setAdapter(picUpTextAdepter);
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
        tv_step=findViewById(R.id.tv_step);
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

    @Override
    public void OnClick(TemplateList.Template template) {
        edit_template.setText(template.getContentBody());
        edit_template.setSelection(edit_template.getText().length());
        bottomSheetDialog_templateList.dismiss();
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
            holder.tv_item.setText(item.getDescription());
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
        List<TemplateList.Template> templateTextList1;
        TemplateClick interfaceClick;
        public TemplateAdepter(Context applicationContext, List<TemplateList.Template> templateTextList1, TemplateClick interfaceClick) {
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
            TemplateList.Template item=templateTextList1.get(position);
            holder.tv_item.setText(item.getTemplateName());
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
                        showAlertDialogButtonClicked(view,edit_template.getText().toString());
                        bottomSheetDialog_templateList.cancel();
                    }else {
                        interfaceClick.OnClick(item);
                        bottomSheetDialog_templateList.cancel();
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

    public void showAlertDialogButtonClicked(View view, String body_text) {

        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(this, R.style.BottomSheetDialog);
        final View customLayout
                = getLayoutInflater()
                .inflate(
                        R.layout.add_titale_for_templet,
                        null);
        builder.setView(customLayout);
        CoordinatorLayout c_layout=customLayout.findViewById(R.id.c_layout);
        EditText editText = customLayout.findViewById(R.id.editText);
        TextView tv_cancel = customLayout.findViewById(R.id.tv_cancel);
        TextView tv_add = customLayout.findViewById(R.id.tv_add);
        AlertDialog dialog
                = builder.create();

        dialog.show();
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().equals(""))
                {
                    Global.Messageshow(getApplicationContext(),c_layout,"Enter template name ",false);
                }
                else if (body_text.equals(""))
                {
                    Global.Messageshow(getApplicationContext(),c_layout,"Enter template Text ",false);

                }
                else {
                    try {
                        dialog.dismiss();
                        CreateTemplate(body_text,editText.getText().toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


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
                    Type listType = new TypeToken<List<CampaignTask>>() {
                    }.getType();
                    CampaignTask user_model1 = new Gson().fromJson(headerString, listType);
                    Log.e("User Model ",new Gson().toJson(user_model1));
                    List<CampaignTask> campaignTasks=new ArrayList<>();
                    campaignTasks.add(user_model1);
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


    private void CreateTemplate(String body_text, String s) throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel signResponseModel= SessionManager.getGetUserdata(First_Step_Start_Activity.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        paramObject.addProperty("template_name", s);
        paramObject.addProperty("content_header", "");
        String template_slug=  s.toUpperCase().replace(" ","_");
        paramObject.addProperty("template_slug", template_slug);
        paramObject.addProperty("content_body", body_text);
        paramObject.addProperty("type", "SMS");

        obj.add("data", paramObject);
        retrofitCalls.CreateTemplate(sessionManager,obj, loadingDialog, token, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                if (response.body().getStatus() == 200) {
                    // onBackPressed();
                }
                else {
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<UservalidateModel>() {
                    }.getType();
                    UservalidateModel user_model = new Gson().fromJson(headerString, listType);
                    if(user_model.getTemplate_slug()!=null){
                        Global.Messageshow(getApplicationContext(),mMainLayout,
                                "The template title has already been taken.",false);
                    }
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }


        });


    }

}