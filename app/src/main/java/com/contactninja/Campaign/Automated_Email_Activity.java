package com.contactninja.Campaign;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contactninja.Auth.SignupActivity;
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

public class Automated_Email_Activity extends AppCompatActivity implements View.OnClickListener, TextClick ,TemplateClick{
    ImageView iv_back;
    TextView save_button, tv_use_tamplet;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    EditText ev_subject, edit_template;
    LinearLayout top_layout;
    TemplateAdepter templateAdepter;
    RecyclerView rv_direct_list;
    PicUpTextAdepter picUpTextAdepter;
    List<HastagList.TemplateText> templateTextList = new ArrayList<>();
    List<TemplateList.Template> templateList=new ArrayList<>();
    public static final int PICKFILE_RESULT_CODE = 1;
    String filePath;
    BottomSheetDialog bottomSheetDialog_templateList;
    TemplateClick templateClick;
    static CoordinatorLayout mMainLayout;
    String step_no = "1", time = "09:00", sequence_id = "",seq_task_id="";
    int minite = 00, day = 1;
    public String template_id_is="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automated_email);
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        templateClick=Automated_Email_Activity.this;

        IntentUI();
        try {
            if (Global.isNetworkAvailable(Automated_Email_Activity.this, MainActivity.mMainLayout)) {
                Hastag_list();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        edit_template.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                top_layout.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Intent inten=getIntent();
        Bundle bundle=inten.getExtras();
        String flag=bundle.getString("flag");
        if (flag.equals("edit"))
        {
            edit_template.setText(bundle.getString("body"));

            seq_task_id= bundle.getString("seq_task_id");
            sequence_id= bundle.getString("sequence_id");

            ev_subject.setText(bundle.getString("header"));
            step_no= String.valueOf(bundle.getInt("step"));

            SessionManager.setCampaign_type(bundle.getString("type"));
            SessionManager.setCampaign_type_name(bundle.getString("manage_by"));
          try {
              day= Integer.parseInt(bundle.getString("day"));
              minite= Integer.parseInt(bundle.getString("minute"));
              SessionManager.setCampaign_Day(String.valueOf(day));
              SessionManager.setCampaign_minute(String.valueOf(minite));
          }
          catch (Exception e)
          {

          }

        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),First_Step_Activity.class));
        finish();

        super.onBackPressed();
    }

    private void Hastag_list() throws JSONException {

        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Automated_Email_Activity.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        obj.add("data", paramObject);
        retrofitCalls.Hastag_list(sessionManager, obj, loadingDialog, token,Global.getVersionname(Automated_Email_Activity.this),Global.Device, new RetrofitCallback() {
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


                    HastagList.TemplateText text = new HastagList.TemplateText();
                    text.setFile(R.drawable.ic_a);
                    text.setSelect(false);
                    templateTextList.add(0, text);

                    HastagList.TemplateText text1 = new HastagList.TemplateText();
                    text1.setFile(R.drawable.ic_file);
                    text1.setSelect(false);
                    templateTextList.add(1, text1);


                    HastagList.TemplateText templateText = new HastagList.TemplateText();
                    templateText.setDescription("Placeholders #");
                    templateText.setSelect(true);
                    templateTextList.add(2, templateText);


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
        mMainLayout = findViewById(R.id.mMainLayout);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button = findViewById(R.id.save_button);
        save_button.setVisibility(View.VISIBLE);
        save_button.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        save_button.setText("Next");
        ev_subject = findViewById(R.id.ev_subject);
        edit_template = findViewById(R.id.edit_template);
        top_layout = findViewById(R.id.top_layout);
        tv_use_tamplet = findViewById(R.id.tv_use_tamplet);
        tv_use_tamplet.setOnClickListener(this);
        rv_direct_list = findViewById(R.id.rv_direct_list);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(@SuppressLint("UnknownNullness") View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.save_button:
                if (ev_subject.getText().equals("")) {
                    Global.Messageshow(getApplicationContext(), mMainLayout, "Add Subject", false);

                }
                else if (edit_template.getText().equals("")) {
                    Global.Messageshow(getApplicationContext(), mMainLayout, getString(R.string.ComposeEmail), false);

                } else {
                    StepData();
                }
                break;
            case R.id.tv_use_tamplet:
                bouttomSheet();
                break;


        }

    }

    private void bouttomSheet() {
        @SuppressLint("InflateParams") final View mView = getLayoutInflater().inflate(R.layout.template_list_dialog_item, null);
        bottomSheetDialog_templateList= new BottomSheetDialog(Automated_Email_Activity.this, R.style.CoffeeDialog);
        bottomSheetDialog_templateList.setContentView(mView);
        //  LinearLayout layout_list_template=bottomSheetDialog.findViewById(R.id.layout_list_template);
        TextView tv_error = bottomSheetDialog_templateList.findViewById(R.id.tv_error);
        RecyclerView templet_list = bottomSheetDialog_templateList.findViewById(R.id.templet_list);
        templet_list.setVisibility(View.VISIBLE);
        try {
            if(Global.isNetworkAvailable(Automated_Email_Activity.this, MainActivity.mMainLayout)) {
                Template_list(templet_list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        tv_error.setVisibility(View.GONE);

        bottomSheetDialog_templateList.show();
    }
    private void Template_list(RecyclerView templet_list) throws JSONException {
        SignResponseModel signResponseModel= SessionManager.getGetUserdata(Automated_Email_Activity.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        obj.add("data", paramObject);
        retrofitCalls.Template_list(sessionManager,obj, loadingDialog, token,Global.getVersionname(Automated_Email_Activity.this),Global.Device, new RetrofitCallback() {
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
                    templateAdepter = new TemplateAdepter(getApplicationContext(), templateList, templateClick,edit_template,ev_subject);
                    templet_list.setAdapter(templateAdepter);

                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }


        });


    }

    public void OnClick(@SuppressLint("UnknownNullness") String s) {
        String curenttext = edit_template.getText().toString();
        String Newtext = curenttext + "{" + s + "}";
        edit_template.setText(Newtext);
        edit_template.setSelection(edit_template.getText().length());
    }

    @Override
    public void OnClick(TemplateList.Template template) {
        ev_subject.setText(template.getContentHeader());
        edit_template.setText(template.getContentBody());
        edit_template.setSelection(edit_template.getText().length());
        template_id_is= String.valueOf(template.getId());
        bottomSheetDialog_templateList.dismiss();
    }

    class TemplateAdepter extends RecyclerView.Adapter<TemplateAdepter.viewholder> {

        public Context mCtx;
        List<TemplateList.Template> templateTextList1;
        TemplateClick interfaceClick;
        EditText edit_template,ev_subject;

        public TemplateAdepter(Context applicationContext, List<TemplateList.Template> templateTextList1,
                               TemplateClick interfaceClick, EditText edit_template, EditText ev_subject) {
            this.mCtx = applicationContext;
            this.templateTextList1 = templateTextList1;
            this.interfaceClick = interfaceClick;
            this.edit_template = edit_template;
            this.ev_subject = ev_subject;
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
            TemplateList.Template item = templateTextList1.get(position);
            holder.tv_item.setText(item.getTemplateName());
            //holder.tv_item.setBackgroundResource(R.drawable.shape_unselect_back);
            holder.tv_item.setTextColor(mCtx.getResources().getColor(R.color.tv_medium));

            if (item.isSelect()) {
                holder.tv_item.setTextColor(mCtx.getResources().getColor(R.color.purple_200));
                holder.line_view.setVisibility(View.VISIBLE);
            } else {
                holder.line_view.setVisibility(View.GONE);
            }

            holder.tv_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.tv_item.getText().toString().equals("Save current as template")) {
                        showAlertDialogButtonClicked(view,edit_template);
                    }else {
                        interfaceClick.OnClick(item);
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
                tv_item = view.findViewById(R.id.selected_broadcast);
                line_view = view.findViewById(R.id.line_view);
            }
        }
    }

    public void showAlertDialogButtonClicked(View view, EditText edit_template) {

        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(this, R.style.BottomSheetDialog);
        final View customLayout
                = getLayoutInflater()
                .inflate(
                        R.layout.add_titale_for_templet,
                        null);
        builder.setView(customLayout);
        EditText edt_template_name = customLayout.findViewById(R.id.editText);
        TextView tv_cancel = customLayout.findViewById(R.id.tv_cancel);
        TextView tv_add = customLayout.findViewById(R.id.tv_add);
        AlertDialog dialog
                = builder.create();

        dialog.show();
        tv_add.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SyntheticAccessor")
            @Override
            public void onClick(View v) {
                try {
                    if (Global.isNetworkAvailable(Automated_Email_Activity.this, Automated_Email_Activity.mMainLayout)) {
                        if (isValidation(edit_template,ev_subject,edt_template_name))
                            CreateTemplate(edit_template,ev_subject,edt_template_name,dialog);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

        Intent inten=getIntent();
        Bundle bundle=inten.getExtras();
        String flag=bundle.getString("flag");

        JsonObject obj = new JsonObject();
        if (flag.equals("add"))
        {
            loadingDialog.showLoadingDialog();
            SignResponseModel user_data = SessionManager.getGetUserdata(this);
            String user_id = String.valueOf(user_data.getUser().getId());
            String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
            String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());

            try {
                if (!SessionManager.getTask(getApplicationContext()).equals(null)) {

                    CampaignTask main_data = SessionManager.getTask(getApplicationContext()).get(0);

                    int step = main_data.getStepNo() + 1;
                    step_no = String.valueOf(step);
                    day = Integer.parseInt(SessionManager.getCampaign_Day(getApplicationContext()));
                    minite = Integer.parseInt(SessionManager.getCampaign_minute(getApplicationContext()));

                    if (SessionManager.getTask(getApplicationContext()).get(0).getSequenceId().toString().equals("null")) {
                        sequence_id = "null";
                    } else {
                        sequence_id = SessionManager.getTask(getApplicationContext()).get(0).getSequenceId().toString();
                    }
                } else {
                    sequence_id = "null";
                }
            } catch (Exception e) {
                sequence_id = "null";
            }

            JsonObject paramObject = new JsonObject();
            paramObject.addProperty("content_body", edit_template.getText().toString());
            paramObject.addProperty("day", day);
            paramObject.addProperty("manage_by", SessionManager.getCampaign_type_name(getApplicationContext()));
            paramObject.addProperty("minute", minite);
            paramObject.addProperty("organization_id", "1");
            paramObject.addProperty("template_id",template_id_is);

            if (!sequence_id.equals("null"))
            {
                paramObject.addProperty("sequence_id", sequence_id);
            }
            paramObject.addProperty("content_header",ev_subject.getText().toString());
            paramObject.addProperty("step_no", step_no);
            paramObject.addProperty("team_id", "1");
            paramObject.addProperty("type", SessionManager.getCampaign_type(getApplicationContext()));
            paramObject.addProperty("time", Global.getCurrentTime());
            paramObject.addProperty("user_id", user_id);
            obj.add("data", paramObject);
        }
        else {
            SignResponseModel user_data = SessionManager.getGetUserdata(this);
            String user_id = String.valueOf(user_data.getUser().getId());
            String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
            String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());
            JsonObject paramObject = new JsonObject();
            paramObject.addProperty("content_body", edit_template.getText().toString());
            paramObject.addProperty("day", day);
            paramObject.addProperty("manage_by", SessionManager.getCampaign_type_name(getApplicationContext()));
            paramObject.addProperty("minute", minite);
            paramObject.addProperty("organization_id", "1");
            paramObject.addProperty("sequence_id", sequence_id);
            paramObject.addProperty("seq_task_id", seq_task_id);
            paramObject.addProperty("content_header",ev_subject.getText().toString());
            paramObject.addProperty("team_id", "1");
            paramObject.addProperty("type", SessionManager.getCampaign_type(getApplicationContext()));
            paramObject.addProperty("time", Global.getCurrentTime());
            paramObject.addProperty("user_id", user_id);
            paramObject.addProperty("step_no", step_no);

            obj.add("data", paramObject);

        }

        retrofitCalls.Task_store(sessionManager, obj, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(Automated_Email_Activity.this),Global.Device,new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                //Log.e("Response is",new Gson().toJson(response));

                loadingDialog.cancelLoading();

                if (response.body().getStatus() == 200) {

                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<List<CampaignTask>>() {
                    }.getType();
                    List<CampaignTask> user_model1 = new Gson().fromJson(headerString, listType);
                    SessionManager.setTask(getApplicationContext(), user_model1);
                    startActivity(new Intent(getApplicationContext(), Campaign_Overview.class));
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
    private void CreateTemplate(EditText edit_template, EditText ev_subject, EditText edt_template_name, AlertDialog dialog) throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Automated_Email_Activity.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        paramObject.addProperty("template_name", edt_template_name.getText().toString().trim());
        paramObject.addProperty("content_header", ev_subject.getText().toString().trim());
        String template_slug = edt_template_name.getText().toString().toUpperCase().replace(" ", "_");
        paramObject.addProperty("template_slug", template_slug);
        paramObject.addProperty("content_body", edit_template.getText().toString().trim());
        paramObject.addProperty("type", "EMAIL");

        obj.add("data", paramObject);
        retrofitCalls.CreateTemplate(sessionManager, obj, loadingDialog, token,Global.getVersionname(Automated_Email_Activity.this),Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                if (response.body().getStatus() == 200) {
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<UservalidateModel>() {
                    }.getType();
                    UservalidateModel user_model = new Gson().fromJson(headerString, listType);
                    if (user_model.getTemplate_slug() != null) {
                        Global.Messageshow(getApplicationContext(), mMainLayout,
                                "The template title has already been taken.", false);
                    }
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                dialog.dismiss();
                loadingDialog.cancelLoading();
            }


        });


    }

    private boolean isValidation(EditText editTemplate, EditText ev_subject, EditText edt_template_name) {
        if (edt_template_name.getText().toString().equals("")) {
            Global.Messageshow(getApplicationContext(), mMainLayout, getResources().getString(R.string.template_name), false);
        }
        if (ev_subject.getText().toString().equals("")) {
            Global.Messageshow(getApplicationContext(), mMainLayout, getResources().getString(R.string.template_subject), false);
        }
        if (editTemplate.getText().toString().equals("")) {
            Global.Messageshow(getApplicationContext(), mMainLayout, getResources().getString(R.string.template_dody), false);
        } else {
            return true;
        }
        return false;
    }


    class PicUpTextAdepter extends RecyclerView.Adapter<PicUpTextAdepter.viewholder> {

        public Context mCtx;
        List<HastagList.TemplateText> templateTextList;
        TextClick interfaceClick;

        public PicUpTextAdepter(Context applicationContext, List<HastagList.TemplateText> templateTextList, TextClick interfaceClick) {
            this.mCtx = applicationContext;
            this.templateTextList = templateTextList;
            this.interfaceClick = interfaceClick;
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
            HastagList.TemplateText item = templateTextList.get(position);
            holder.tv_item.setText(item.getDescription());
            holder.tv_item.setBackgroundResource(R.drawable.shape_unselect_back);
            holder.tv_item.setTextColor(mCtx.getResources().getColor(R.color.tv_medium));
            if (item.getFile() != 0) {
                holder.im_file.setVisibility(View.VISIBLE);
                holder.tv_item.setVisibility(View.GONE);
                holder.im_file.setImageDrawable(mCtx.getDrawable(item.getFile()));
                holder.line_view.setVisibility(View.GONE);

            } else {

                holder.im_file.setVisibility(View.GONE);
                holder.tv_item.setVisibility(View.VISIBLE);

            }
            if (position == 0) {

                holder.line_view.setVisibility(View.GONE);

            } else if (position == 1) {
                holder.line_view.setVisibility(View.GONE);
            } else {

            }
            holder.im_file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (position == 1) {
                        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                        chooseFile.setType("*/*");
                        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                        startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
                    }
                }
            });
            if (item.isSelect()) {
                holder.tv_item.setBackground(null);
                holder.tv_item.setTextColor(mCtx.getResources().getColor(R.color.tv_medium));
                holder.line_view.setVisibility(View.VISIBLE);
            } else {
                holder.line_view.setVisibility(View.GONE);
            }
            holder.tv_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!item.isSelect()) {
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
            ImageView im_file;

            public viewholder(View view) {
                super(view);
                tv_item = view.findViewById(R.id.tv_item);
                line_view = view.findViewById(R.id.line_view);
                im_file = view.findViewById(R.id.im_file);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == -1) {
                    Uri fileUri = data.getData();
                    filePath = fileUri.getPath();
                    Log.e("File Pathe uis ", filePath);

                }

                break;
        }
    }


}