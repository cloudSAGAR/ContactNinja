package com.contactninja.Campaign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.contactninja.Interface.TextClick;
import com.contactninja.Model.TemplateText;
import com.contactninja.R;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class Automated_Email_Activity extends AppCompatActivity implements View.OnClickListener , TextClick{
    ImageView iv_back;
    TextView save_button,tv_use_tamplet;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    EditText ev_subject,edit_template;
    LinearLayout top_layout;
    List<TemplateText> templateTextList1=new ArrayList<>();
    TemplateAdepter templateAdepter;
    RecyclerView rv_direct_list;
    PicUpTextAdepter picUpTextAdepter;
    List<TemplateText> templateTextList=new ArrayList<>();
    public static final int PICKFILE_RESULT_CODE = 1;
     String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automated_email);

        IntentUI();
        Listset();
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
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
    }


    private void Listset() {
        for(int i=0;i<=4;i++){
            TemplateText templateText=new TemplateText();

            if(i==0){
                templateText.setFile(R.drawable.ic_a);
                templateText.setSelect(false);
            }
            if(i==1){
                templateText.setFile(R.drawable.ic_file);
                templateText.setSelect(false);
            }
            if(i==2){

                templateText.setTemplateText("Placeholders #");
                templateText.setSelect(true);

            }
            else if(i==3){
                templateText.setTemplateText("First Name");
                templateText.setSelect(false);
            }else if(i==4){
                templateText.setTemplateText("Last Name");
                templateText.setSelect(false);
            }else if(i==5){
                templateText.setTemplateText("Hi");
                templateText.setSelect(false);
            }else if(i==6){
                templateText.setTemplateText("Hello");
                templateText.setSelect(false);
            }
            templateTextList.add(i,templateText);
        }
        rv_direct_list.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_direct_list.setHasFixedSize(true);
        picUpTextAdepter = new PicUpTextAdepter(getApplicationContext(), templateTextList, this);
        rv_direct_list.setAdapter(picUpTextAdepter);
    }
    private void IntentUI() {
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button = findViewById(R.id.save_button);
        save_button.setVisibility(View.VISIBLE);
        save_button.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        save_button.setText("Next");
        ev_subject=findViewById(R.id.ev_subject);
        edit_template=findViewById(R.id.edit_template);
        top_layout=findViewById(R.id.top_layout);
        tv_use_tamplet=findViewById(R.id.tv_use_tamplet);
        tv_use_tamplet.setOnClickListener(this);
        rv_direct_list = findViewById(R.id.rv_direct_list);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.save_button:
                //Add Api Call
                // startActivity(new Intent(getApplicationContext(),Campaign_Overview.class));
                break;
            case R.id.tv_use_tamplet:
                bouttomSheet();
                break;


        }

    }

    private void bouttomSheet() {
        templateTextList1.clear();
        @SuppressLint("InflateParams") final View mView = getLayoutInflater().inflate(R.layout.template_list_dialog_item, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Automated_Email_Activity.this, R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);
        //  LinearLayout layout_list_template=bottomSheetDialog.findViewById(R.id.layout_list_template);
        TextView tv_error=bottomSheetDialog.findViewById(R.id.tv_error);
        RecyclerView templet_list=bottomSheetDialog.findViewById(R.id.templet_list);
        templet_list.setVisibility(View.VISIBLE);
        Listset1();
        templet_list.setLayoutManager(new LinearLayoutManager(this));
        templateAdepter = new TemplateAdepter(getApplicationContext(), templateTextList1, Automated_Email_Activity.this);
        templet_list.setAdapter(templateAdepter);

        tv_error.setVisibility(View.GONE);

        bottomSheetDialog.show();
    }

    private void Listset1() {
        for(int i=0;i<=4;i++){
            TemplateText templateText1=new TemplateText();
            if(i==0){
                templateText1.setTemplateText("Please select template");
                templateText1.setSelect(false);
            }
            if(i==1){
                templateText1.setTemplateText("New member joining");
                templateText1.setSelect(true);
            }else if(i==2){
                templateText1.setTemplateText("Customer service");
                templateText1.setSelect(true);
            }else if(i==3){
                templateText1.setTemplateText("New Product development");
                templateText1.setSelect(true);
            }else if(i==4){
                templateText1.setTemplateText("Save Current as template");
                templateText1.setSelect(true);
            }
            templateTextList1.add(i,templateText1);
        }

    }
    public void OnClick(@SuppressLint("UnknownNullness") String s) {
        String curenttext=edit_template.getText().toString();
        String Newtext=curenttext+"{"+s+"}";
        edit_template.setText(Newtext);
        edit_template.setSelection(edit_template.getText().length());
    }

    class TemplateAdepter extends RecyclerView.Adapter<TemplateAdepter.viewholder>{

        public Context mCtx;
        List<TemplateText> templateTextList1;
        TextClick interfaceClick;
        public TemplateAdepter(Context applicationContext, List<TemplateText> templateTextList1,TextClick interfaceClick) {
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
            TemplateText item=templateTextList1.get(position);
            holder.tv_item.setText(item.getTemplateText());
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


    class PicUpTextAdepter extends RecyclerView.Adapter<PicUpTextAdepter.viewholder>{

        public Context mCtx;
        List<TemplateText> templateTextList;
        TextClick interfaceClick;
        public PicUpTextAdepter(Context applicationContext, List<TemplateText> templateTextList,TextClick interfaceClick) {
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
            TemplateText item=templateTextList.get(position);
            holder.tv_item.setText(item.getTemplateText());
            holder.tv_item.setBackgroundResource(R.drawable.shape_unselect_back);
            holder.tv_item.setTextColor(mCtx.getResources().getColor(R.color.tv_medium));
            if (item.getFile()!=0)
            {
                holder.im_file.setVisibility(View.VISIBLE);
                holder.tv_item.setVisibility(View.GONE);
                holder.im_file.setImageDrawable(mCtx.getDrawable(item.getFile()));
                holder.line_view.setVisibility(View.GONE);

            }
            else {

                holder.im_file.setVisibility(View.GONE);
                holder.tv_item.setVisibility(View.VISIBLE);

            }
             if (position==0)
            {

                holder.line_view.setVisibility(View.GONE);

            }
            else if (position==1)
            {
                holder.line_view.setVisibility(View.GONE);
            }
            else {

            }
            holder.im_file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (position==1)
                    {
                        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                        chooseFile.setType("*/*");
                        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                        startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
                    }
                }
            });
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
                        interfaceClick.OnClick(item.getTemplateText());
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
                tv_item=view.findViewById(R.id.tv_item);
                line_view=view.findViewById(R.id.line_view);
                im_file=view.findViewById(R.id.im_file);
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
                    Log.e("File Pathe uis ",filePath);

                }

                break;
        }
    }


}