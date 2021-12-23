package com.contactninja.Setting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contactninja.Interface.TextClick;
import com.contactninja.Model.TemplateText;
import com.contactninja.R;
import com.contactninja.Utils.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class TemplateCreateActivity extends AppCompatActivity implements View.OnClickListener, TextClick {
    ImageView iv_back;
    TextView save_button;
    EditText edit_template,edit_template_name;
    PicUpTextAdepter picUpTextAdepter;
    RecyclerView rv_direct_list;
    List<TemplateText> templateTextList=new ArrayList<>();
    LinearLayout layout_title;

    @Override
    protected void onCreate(@SuppressLint("UnknownNullness") Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_create);
        IntentUI();
        Listset();
    }

    private void Listset() {
        for(int i=0;i<=4;i++){
            TemplateText templateText=new TemplateText();
            if(i==0){
                templateText.setTemplateText("Placeholders #");
                templateText.setSelect(true);
            }else if(i==1){
                templateText.setTemplateText("First Name");
                templateText.setSelect(false);
            }else if(i==2){
                templateText.setTemplateText("Last Name");
                templateText.setSelect(false);
            }else if(i==3){
                templateText.setTemplateText("Hi");
                templateText.setSelect(false);
            }else if(i==4){
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
        layout_title = findViewById(R.id.layout_title);
        save_button = findViewById(R.id.save_button);
        save_button.setText(getResources().getText(R.string.save));
        save_button.setVisibility(View.VISIBLE);
        save_button.setOnClickListener(this);
        edit_template_name = findViewById(R.id.edit_template_name);
        rv_direct_list = findViewById(R.id.rv_direct_list);
        edit_template = findViewById(R.id.edit_template);

        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
        edit_template.requestFocus();
        layout_title.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(@SuppressLint("UnknownNullness") View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
                case R.id.save_button:
                finish();
                break;

                case R.id.layout_title:
                    showAlertDialogButtonClicked();
                    break;
        }
    }

    public void showAlertDialogButtonClicked() {

        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(this, R.style.MyDialogStyle);

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.template_edit_dialog, null);
        builder.setView(customLayout);
        AlertDialog dialog
                = builder.create();

        TextView tv_ok = customLayout.findViewById(R.id.tv_ok);
        TextView tv_cancel = customLayout.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager sessionManager = new SessionManager(getApplicationContext());
                sessionManager.logoutUser();
                finish();
            }
        });
        dialog.show();
    }

    private void bouttomSheet() {
        @SuppressLint("InflateParams") final View mView = getLayoutInflater().inflate(R.layout.template_list_dialog_item, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(TemplateCreateActivity.this, R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);
      //  LinearLayout layout_list_template=bottomSheetDialog.findViewById(R.id.layout_list_template);
        TextView tv_error=bottomSheetDialog.findViewById(R.id.tv_error);
        tv_error.setVisibility(View.VISIBLE);

        bottomSheetDialog.show();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public void OnClick(@SuppressLint("UnknownNullness") String s) {
        String curenttext=edit_template.getText().toString();
        String Newtext=curenttext+"{"+s+"}";
        edit_template.setText(Newtext);
        edit_template.setSelection(edit_template.getText().length());
    }

    static class PicUpTextAdepter extends RecyclerView.Adapter<PicUpTextAdepter.viewholder>{

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
        public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.template_text_selecte, parent, false);
            return new viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull viewholder holder, int position) {
            TemplateText item=templateTextList.get(position);
            holder.tv_item.setText(item.getTemplateText());
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
                        Handler  handler = new Handler();
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
            public viewholder(View view) {
                super(view);
                tv_item=view.findViewById(R.id.tv_item);
                line_view=view.findViewById(R.id.line_view);
            }
        }
    }
}