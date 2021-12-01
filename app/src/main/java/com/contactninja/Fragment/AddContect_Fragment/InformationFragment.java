package com.contactninja.Fragment.AddContect_Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contactninja.Model.AddcontectModel;
import com.contactninja.R;
import com.contactninja.Utils.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.contactninja.Model.WorkTypeData;

import java.util.ArrayList;
import java.util.List;

import ru.rambler.libs.swipe_layout.SwipeLayout;


public class InformationFragment extends Fragment implements View.OnClickListener {



    public InformationFragment() {
        // Required empty public constructor
    }
    EditText tv_mobile_no,ev_email,ev_address,ev_city,ev_zip,ev_zoom,ev_note;
    LinearLayout select_label,select_email_label,select_state,add_mobile_Number,
            layout_phone,layout_email,layout_mobile,delete_layout;
    TextView tv_phone,phone_txt,email_txt;
    ImageView pulse_icon,pulse_icon1;
    SwipeLayout swipe_layout;
    String Name="",job_titel="";
    SessionManager sessionManager;
    AddcontectModel addcontectModel;
    BottomSheetDialog bottomSheetDialog;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_information, container, false);
        IntentUI(view);
        addcontectModel=new AddcontectModel();

        sessionManager=new SessionManager(getActivity());
        select_email_label.setOnClickListener(this);
        select_label.setOnClickListener(this);


        TextSet();

        swipe_layout.setOnSwipeListener(new SwipeLayout.OnSwipeListener() {
            @Override
            public void onBeginSwipe(SwipeLayout swipeLayout, boolean moveToRight) {
               // Log.e("Swipe Call ","MOveto right");
                if (delete_layout.getVisibility()==View.GONE)
                {
                    delete_layout.setVisibility(View.VISIBLE);
                }
                else {
                    delete_layout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSwipeClampReached(SwipeLayout swipeLayout, boolean moveToRight) {
                Log.e("Swipe Call ","MOveto right1");

            }

            @Override
            public void onLeftStickyEdge(SwipeLayout swipeLayout, boolean moveToRight) {
                Log.e("Swipe Call ","Left");
            }

            @Override
            public void onRightStickyEdge(SwipeLayout swipeLayout, boolean moveToRight) {
                Log.e("Swipe Call ","Right");

            }
        });
        return view;
    }

    private void TextSet() {

        tv_mobile_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout_phone.setVisibility(View.VISIBLE);
                addcontectModel.setMobile(s.toString());
                sessionManager.setAdd_Contect_Detail(getActivity(),addcontectModel);
                addcontectModel.setMobile_type(phone_txt.getText().toString());
                addcontectModel.setEmail_type(email_txt.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ev_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                layout_email.setVisibility(View.VISIBLE);
                addcontectModel.setEmail(s.toString());
                sessionManager.setAdd_Contect_Detail(getActivity(),addcontectModel);
                addcontectModel.setMobile_type(phone_txt.getText().toString());
                addcontectModel.setEmail_type(email_txt.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ev_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                addcontectModel.setAddress(charSequence.toString());
                sessionManager.setAdd_Contect_Detail(getActivity(),addcontectModel);
                addcontectModel.setMobile_type(phone_txt.getText().toString());
                addcontectModel.setEmail_type(email_txt.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ev_city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                addcontectModel.setCity(charSequence.toString());
                sessionManager.setAdd_Contect_Detail(getActivity(),addcontectModel);
                addcontectModel.setMobile_type(phone_txt.getText().toString());
                addcontectModel.setEmail_type(email_txt.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ev_zip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                addcontectModel.setZip_code(charSequence.toString());
                sessionManager.setAdd_Contect_Detail(getActivity(),addcontectModel);
                addcontectModel.setMobile_type(phone_txt.getText().toString());
                addcontectModel.setEmail_type(email_txt.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ev_zoom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                addcontectModel.setZoom_id(charSequence.toString());
                sessionManager.setAdd_Contect_Detail(getActivity(),addcontectModel);
                addcontectModel.setMobile_type(phone_txt.getText().toString());
                addcontectModel.setEmail_type(email_txt.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ev_note.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                addcontectModel.setNote(charSequence.toString());
                sessionManager.setAdd_Contect_Detail(getActivity(),addcontectModel);
                addcontectModel.setMobile_type(phone_txt.getText().toString());
                addcontectModel.setEmail_type(email_txt.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void IntentUI(View view) {
        tv_phone=view.findViewById(R.id.tv_phone);
        tv_mobile_no=view.findViewById(R.id.tv_mobile_no);
        select_label=view.findViewById(R.id.select_label);
        select_email_label=view.findViewById(R.id.select_email_label);
        ev_email=view.findViewById(R.id.ev_email);
        ev_address=view.findViewById(R.id.ev_address);
        ev_city=view.findViewById(R.id.ev_city);
        select_state=view.findViewById(R.id.select_state);
        ev_zip=view.findViewById(R.id.ev_zip);
        ev_zoom=view.findViewById(R.id.ev_zoom);
        ev_note=view.findViewById(R.id.ev_note);
        add_mobile_Number=view.findViewById(R.id.add_mobile_Number);
        pulse_icon=view.findViewById(R.id.pulse_icon);
        pulse_icon.setColorFilter(getActivity().getColor(R.color.purple_200));
        layout_phone=view.findViewById(R.id.layout_phone);
        pulse_icon1=view.findViewById(R.id.pulse_icon1);
        pulse_icon1.setColorFilter(getActivity().getColor(R.color.purple_200));
        layout_email=view.findViewById(R.id.layout_email);
        layout_mobile=view.findViewById(R.id.layout_mobile);
        swipe_layout=view.findViewById(R.id.swipe_layout);
        delete_layout=view.findViewById(R.id.delete_layout);
        phone_txt=view.findViewById(R.id.phone_txt);
        email_txt=view.findViewById(R.id.email_txt);

    }


    private void showBottomSheetDialog_For_Home(String moble) {
        bottomSheetDialog = new BottomSheetDialog(getActivity(),R.style.BottomSheetDialog);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_for_home);
        RecyclerView home_type_list=bottomSheetDialog.findViewById(R.id.home_type_list);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        home_type_list.setLayoutManager(layoutManager);

        WorkTypeData nameList = new WorkTypeData("1","Home","true");
        WorkTypeData nameList1 = new WorkTypeData("1","Main","true");
        WorkTypeData nameList2 = new WorkTypeData("1","Work","true");
        WorkTypeData nameList3 = new WorkTypeData("1","Other","true");
        WorkTypeData nameList4= new WorkTypeData("1","Add custom","true");

        List<WorkTypeData> workTypeData=new ArrayList<>();
        workTypeData.add(0,nameList);
        workTypeData.add(1,nameList1);
        workTypeData.add(2,nameList2);
        workTypeData.add(3,nameList3);
        workTypeData.add(4,nameList4);
        WorkAdapter workAdapter=new WorkAdapter(getActivity(),workTypeData,moble);
        home_type_list.setAdapter(workAdapter);

        bottomSheetDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.select_label:
                showBottomSheetDialog_For_Home("mobile");

                break;

            case R.id.select_email_label:
                showBottomSheetDialog_For_Home("email");
                break;


        }
    }


    public class WorkAdapter extends RecyclerView.Adapter<WorkAdapter.InviteListDataclass> {

        public Context mCtx;

        private List<WorkTypeData> worklist;
        String type;
        public WorkAdapter(Context context, List<WorkTypeData> worklist, String type) {
            this.mCtx = context;
            this.worklist = worklist;
            this.type=type;
        }
        @NonNull
        @Override
        public InviteListDataclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.work_type_selecte, parent, false);
            return new InviteListDataclass(view);
        }
        @Override
        public void onBindViewHolder(@NonNull InviteListDataclass holder, int position) {
            WorkTypeData WorkData = worklist.get(position);
            holder.tv_item.setText(WorkData.getTitale());
            holder.tv_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (WorkData.getTitale().equals("Add custom"))
                    {
                        showAlertDialogButtonClicked(v);
                    }
                    else {
                        if (type.equals("mobile"))
                        {
                            bottomSheetDialog.cancel();
                            phone_txt.setText(holder.tv_item.getText().toString());

                        }
                        else if (type.equals("email"))
                        {
                            bottomSheetDialog.cancel();
                            email_txt.setText(holder.tv_item.getText().toString());
                        }
                        else {

                        }

                    }

                }
            });

        }
        @Override
        public int getItemCount() {
            return worklist.size();
        }

        public void updateList(List<WorkTypeData> list){
            worklist = list;
            notifyDataSetChanged();
        }

        public  class InviteListDataclass extends RecyclerView.ViewHolder {
            TextView tv_item;

            public InviteListDataclass(@NonNull View itemView) {
                super(itemView);
                tv_item=itemView.findViewById(R.id.tv_item);
            }

        }

    }


    public void showAlertDialogButtonClicked(View view)
    {

        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(getActivity(),R.style.BottomSheetDialog);
        final View customLayout
                = getLayoutInflater()
                .inflate(
                        R.layout.custom_add_work_layout,
                        null);
        builder.setView(customLayout);
        EditText editText=customLayout.findViewById(R.id.editText);
        TextView tv_cancel=customLayout.findViewById(R.id.tv_cancel);
        TextView tv_add=customLayout.findViewById(R.id.tv_add);
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
}