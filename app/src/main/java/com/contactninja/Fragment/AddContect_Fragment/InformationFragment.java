package com.contactninja.Fragment.AddContect_Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contactninja.Model.AddcontectModel;
import com.contactninja.Model.Contactdetail;
import com.contactninja.Model.WorkTypeData;
import com.contactninja.R;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import ru.rambler.libs.swipe_layout.SwipeLayout;


public class InformationFragment extends Fragment implements View.OnClickListener {


    public InformationFragment() {
        // Required empty public constructor
    }

    EditText   ev_address, ev_city, ev_zip, ev_zoom, ev_note,
            ev_company_url, ev_state, ev_job, ev_bob, ev_fb, ev_twitter, ev_breakout,
            ev_linkedin;
    LinearLayout   select_state, add_mobile_Number,
            layout_Add_phone, layout_Add_email, layout_mobile,  fb_layout;
    TextView tv_phone,   tv_more_field, tv_company_url, tv_job,
            zone_txt;
    ImageView pulse_icon, pulse_icon1;
    String Name = "", job_titel = "";
    SessionManager sessionManager;
    AddcontectModel addcontectModel;
    BottomSheetDialog bottomSheetDialog;
    LinearLayout city_layout, zoom_layout, note_layout, company_url_layout, job_layout,
            layout_time_zone, select_label_zone, layout_bod, twitter_layout, breakout_layout,
            linkedin_layout, state_layout, time_layout, media_layout;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String show = "0";

    PhoneAdapter phoneAdapter;
    EmailAdapter emailAdapter;
    RecyclerView rv_phone,rv_email;
    List<Contactdetail> contactdetails=new ArrayList<>();
    List<Contactdetail> phonedetails_list =new ArrayList<>();
    List<Contactdetail> emaildetails_list=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_information, container, false);
        IntentUI(view);
        addcontectModel = new AddcontectModel();

        sessionManager = new SessionManager(getActivity());


        PhoneViewAdd();
        EmailViewAdd();
        TextSet();


        layout_bod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenBob();
            }
        });

        tv_more_field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                media_layout.setVisibility(View.VISIBLE);

                company_url_layout.setVisibility(View.VISIBLE);
                job_layout.setVisibility(View.VISIBLE);
                zoom_layout.setVisibility(View.VISIBLE);
                city_layout.setVisibility(View.VISIBLE);
                state_layout.setVisibility(View.VISIBLE);
                time_layout.setVisibility(View.VISIBLE);
                layout_bod.setVisibility(View.VISIBLE);
                note_layout.setVisibility(View.VISIBLE);
                tv_more_field.setVisibility(View.GONE);
            }

        });

        return view;
    }

    private void EmailViewAdd() {
        Contactdetail contactdetail=new Contactdetail();
        contactdetail.setId(contactdetails.size());
        contactdetail.setEmail_number("");
        contactdetail.setIs_default(0);
        contactdetail.setLabel("Home");
        contactdetail.setType("EMAIL");
        emaildetails_list.add(contactdetail);
        contactdetails.add(contactdetail);
        emailAdapter = new EmailAdapter(getActivity(), emaildetails_list,layout_Add_email);
        rv_email.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_email.setAdapter(emailAdapter);

        layout_Add_email.setOnClickListener(v -> {
            Contactdetail contactdetail1 =new Contactdetail();
            contactdetail1.setId(contactdetails.size());
            contactdetail1.setEmail_number("");
            contactdetail1.setIs_default(0);
            contactdetail1.setLabel("Home");
            contactdetail1.setType("EMAIL");
            emaildetails_list.add(contactdetail1);
            contactdetails.add(contactdetail1);

            emailAdapter.notifyDataSetChanged();
            layout_Add_email.setVisibility(View.GONE);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void PhoneViewAdd() {

        Contactdetail contactdetail=new Contactdetail();
        contactdetail.setId(contactdetails.size());
        contactdetail.setEmail_number("");
        contactdetail.setIs_default(1);
        contactdetail.setLabel("Home");
        contactdetail.setType("NUMBER");
        phonedetails_list.add(contactdetail);
        contactdetails.add(contactdetail);


        phoneAdapter = new PhoneAdapter(getActivity(), phonedetails_list,layout_Add_phone);
        rv_phone.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_phone.setAdapter(phoneAdapter);

        layout_Add_phone.setOnClickListener(v -> {
            Contactdetail contactdetail1 =new Contactdetail();
            contactdetail1.setId(contactdetails.size());
            contactdetail1.setEmail_number("");
            contactdetail1.setIs_default(0);
            contactdetail1.setLabel("Home");
            contactdetail1.setType("NUMBER");
            phonedetails_list.add(contactdetail1);
            contactdetails.add(contactdetail1);
            phoneAdapter.notifyDataSetChanged();

            layout_Add_phone.setVisibility(View.GONE);

        });


    }

    private void TextSet() {



        ev_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                addcontectModel.setAddress(charSequence.toString());
                sessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);
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
                sessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);
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
                sessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);
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
                sessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);
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
                sessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        ev_state.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                addcontectModel.setNote(charSequence.toString());
                sessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void IntentUI(View view) {
        tv_phone = view.findViewById(R.id.tv_phone);
        ev_address = view.findViewById(R.id.ev_address);
        ev_city = view.findViewById(R.id.ev_city);
        select_state = view.findViewById(R.id.select_state);
        ev_zip = view.findViewById(R.id.ev_zip);
        ev_zoom = view.findViewById(R.id.ev_zoom);
        ev_note = view.findViewById(R.id.ev_note);
        add_mobile_Number = view.findViewById(R.id.add_mobile_Number);
        pulse_icon = view.findViewById(R.id.pulse_icon);
        pulse_icon.setColorFilter(getActivity().getResources().getColor(R.color.purple_200));
        layout_Add_phone = view.findViewById(R.id.layout_Add_phone);
        pulse_icon1 = view.findViewById(R.id.pulse_icon1);
        pulse_icon1.setColorFilter(getActivity().getResources().getColor(R.color.purple_200));
        layout_Add_email = view.findViewById(R.id.layout_Add_email);
        layout_mobile = view.findViewById(R.id.layout_mobile);
        ev_state = view.findViewById(R.id.ev_state);
        city_layout = view.findViewById(R.id.city_layout);
        zoom_layout = view.findViewById(R.id.zoom_layout);
        note_layout = view.findViewById(R.id.note_layout);
        tv_more_field = view.findViewById(R.id.tv_more_field);
        company_url_layout = view.findViewById(R.id.company_url_layout);
        tv_company_url = view.findViewById(R.id.tv_company_url);
        ev_company_url = view.findViewById(R.id.ev_company_url);
        job_layout = view.findViewById(R.id.job_layout);
        tv_job = view.findViewById(R.id.tv_job);
        ev_job = view.findViewById(R.id.ev_job);
        layout_time_zone = view.findViewById(R.id.layout_time_zone);
        select_label_zone = view.findViewById(R.id.select_label_zone);
        zone_txt = view.findViewById(R.id.zone_txt);
        layout_bod = view.findViewById(R.id.layout_bod);
        ev_bob = view.findViewById(R.id.ev_bob);
        fb_layout = view.findViewById(R.id.fb_layout);
        ev_fb = view.findViewById(R.id.ev_fb);
        twitter_layout = view.findViewById(R.id.twitter_layout);
        ev_twitter = view.findViewById(R.id.ev_twitter);
        breakout_layout = view.findViewById(R.id.breakout_layout);
        ev_breakout = view.findViewById(R.id.ev_breakout);
        linkedin_layout = view.findViewById(R.id.linkedin_layout);
        ev_linkedin = view.findViewById(R.id.ev_linkedin);
        state_layout = view.findViewById(R.id.state_layout);
        time_layout = view.findViewById(R.id.time_layout);
        media_layout = view.findViewById(R.id.media_layout);
        rv_phone = view.findViewById(R.id.rv_phone);
        rv_email = view.findViewById(R.id.rv_email);

    }


    void showBottomSheetDialog_For_Home(String moble,TextView phone_txt,TextView email_txt, Contactdetail item) {
        bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialog);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_for_home);
        RecyclerView home_type_list = bottomSheetDialog.findViewById(R.id.home_type_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        home_type_list.setLayoutManager(layoutManager);

        WorkTypeData nameList = new WorkTypeData("1", "Home", "true");
        WorkTypeData nameList1 = new WorkTypeData("1", "Main", "true");
        WorkTypeData nameList2 = new WorkTypeData("1", "Work", "true");
        WorkTypeData nameList3 = new WorkTypeData("1", "Other", "true");
        WorkTypeData nameList4 = new WorkTypeData("1", "Add custom", "true");

        List<WorkTypeData> workTypeData = new ArrayList<>();
        workTypeData.add(0, nameList);
        workTypeData.add(1, nameList1);
        workTypeData.add(2, nameList2);
        workTypeData.add(3, nameList3);
        workTypeData.add(4, nameList4);
        WorkAdapter workAdapter = new WorkAdapter(getActivity(), workTypeData, moble,phone_txt,email_txt,item);
        home_type_list.setAdapter(workAdapter);

        bottomSheetDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {




        }
    }


    public class WorkAdapter extends RecyclerView.Adapter<WorkAdapter.InviteListDataclass> {

        public Context mCtx;

        private List<WorkTypeData> worklist;
        String type;
        TextView phone_txt;
        TextView email_txt;
        Contactdetail item;
        public WorkAdapter(Context context, List<WorkTypeData> worklist, String type,TextView phone_txt,TextView email_txt, Contactdetail item) {
            this.mCtx = context;
            this.worklist = worklist;
            this.type = type;
            this.phone_txt = phone_txt;
            this.email_txt = email_txt;
            this.item = item;
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
                    if (WorkData.getTitale().equals("Add custom")) {
                        showAlertDialogButtonClicked(v);
                    } else {
                        if (type.equals("mobile")) {
                            bottomSheetDialog.cancel();
                            phone_txt.setText(holder.tv_item.getText().toString());
                            item.setLabel(holder.tv_item.getText().toString());

                        } else if (type.equals("email")) {
                            bottomSheetDialog.cancel();
                            email_txt.setText(holder.tv_item.getText().toString());
                            item.setLabel(holder.tv_item.getText().toString());
                        } else {

                        }

                    }

                }
            });

        }

        @Override
        public int getItemCount() {
            return worklist.size();
        }

        public void updateList(List<WorkTypeData> list) {
            worklist = list;
            notifyDataSetChanged();
        }

        public class InviteListDataclass extends RecyclerView.ViewHolder {
            TextView tv_item;

            public InviteListDataclass(@NonNull View itemView) {
                super(itemView);
                tv_item = itemView.findViewById(R.id.tv_item);
            }

        }

    }
    public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.InviteListDataclass> {

        public Context mCtx;

        List<Contactdetail> contactdetails;
        LinearLayout layout_Add_phone;
        public PhoneAdapter(Context context, List<Contactdetail> contactdetails,LinearLayout layout_Add_phone) {
            this.mCtx = context;
            this.contactdetails = contactdetails;
            this.layout_Add_phone = layout_Add_phone;
        }

        @NonNull
        @Override
        public InviteListDataclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.phone_add_layout, parent, false);
            return new InviteListDataclass(view);
        }

        @Override
        public void onBindViewHolder(@NonNull InviteListDataclass holder, int position) {
            Contactdetail item = contactdetails.get(position);
            if(item.getIs_default()==1){
                holder.iv_set_default.setVisibility(View.VISIBLE);
            }else {
                holder.iv_set_default.setVisibility(View.GONE);
            }


            holder.edt_mobile_no.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    item.setEmail_number(s.toString());
                    if(contactdetails.size()<=4){
                        layout_Add_phone.setVisibility(View.VISIBLE);
                    }
                    sessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            holder.swipe_layout.setOnSwipeListener(new SwipeLayout.OnSwipeListener() {
                @Override
                public void onBeginSwipe(SwipeLayout swipeLayout, boolean moveToRight) {
                    // Log.e("Swipe Call ","MOveto right");
                    if (holder.layout_swap.getVisibility() == View.GONE) {
                        holder.layout_swap.setVisibility(View.VISIBLE);
                    } else {
                        holder.layout_swap.setVisibility(View.GONE);
                    }


                }

                @Override
                public void onSwipeClampReached(SwipeLayout swipeLayout, boolean moveToRight) {
                    Log.e("Swipe Call ", "MOveto right1");

                }

                @Override
                public void onLeftStickyEdge(SwipeLayout swipeLayout, boolean moveToRight) {
                    Log.e("Swipe Call ", "Left");
                }

                @Override
                public void onRightStickyEdge(SwipeLayout swipeLayout, boolean moveToRight) {
                    Log.e("Swipe Call ", "Right");

                }
            });
            holder.select_label.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showBottomSheetDialog_For_Home("mobile",holder.phone_txt,holder.phone_txt,item);
                }
            });
            holder.layout_defult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.layout_swap.setVisibility(View.GONE);
                    for(int i=0;i<contactdetails.size();i++){
                        if(item.getId()==contactdetails.get(i).getId()){
                            contactdetails.get(i).setIs_default(1);
                            notifyDataSetChanged();
                        }else {
                            contactdetails.get(i).setIs_default(0);
                            notifyDataSetChanged();
                        }
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return contactdetails.size();
        }





        public class InviteListDataclass extends RecyclerView.ViewHolder {
            EditText edt_mobile_no;
            ImageView iv_set_default;
            SwipeLayout swipe_layout;
            LinearLayout layout_swap,select_label,layout_defult;
            TextView phone_txt;
            public InviteListDataclass(@NonNull View itemView) {
                super(itemView);

                edt_mobile_no=itemView.findViewById(R.id.edt_mobile_no);
                iv_set_default=itemView.findViewById(R.id.iv_set_default);
                swipe_layout=itemView.findViewById(R.id.swipe_layout);
                layout_swap=itemView.findViewById(R.id.layout_swap);
                select_label=itemView.findViewById(R.id.select_label);
                layout_defult=itemView.findViewById(R.id.layout_defult);
                phone_txt=itemView.findViewById(R.id.phone_txt);


            }

        }

    }

    public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.InviteListDataclass> {

        public Context mCtx;

        private List<Contactdetail> contactdetails;
        LinearLayout layout_Add_email;
        public EmailAdapter(Context context, List<Contactdetail> contactdetails,LinearLayout layout_Add_email) {
            this.mCtx = context;
            this.contactdetails = contactdetails;
            this.layout_Add_email = layout_Add_email;
        }

        @NonNull
        @Override
        public InviteListDataclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.eamil_add_layout, parent, false);
            return new InviteListDataclass(view);
        }

        @Override
        public void onBindViewHolder(@NonNull InviteListDataclass holder, int position) {
            Contactdetail item = contactdetails.get(position);

            holder.edt_email.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(Global.emailValidator(s.toString())){
                        holder.iv_invalid.setVisibility(View.GONE);
                        item.setEmail_number(s.toString());
                        if(contactdetails.size()<=4){
                            layout_Add_email.setVisibility(View.VISIBLE);
                        }
                        sessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);
                    }else {
                        holder.iv_invalid.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            holder.select_email_label.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showBottomSheetDialog_For_Home("email",holder.email_txt,holder.email_txt,item);
                }
            });

        }

        @Override
        public int getItemCount() {
            return contactdetails.size();
        }


        public class InviteListDataclass extends RecyclerView.ViewHolder {
            EditText edt_email;
            TextView email_txt,iv_invalid;
            LinearLayout select_email_label;
            public InviteListDataclass(@NonNull View itemView) {
                super(itemView);

                edt_email=itemView.findViewById(R.id.edt_email);
                email_txt=itemView.findViewById(R.id.email_txt);
                select_email_label=itemView.findViewById(R.id.select_email_label);
                iv_invalid=itemView.findViewById(R.id.iv_invalid);


            }

        }

    }

    public void showAlertDialogButtonClicked(View view) {

        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(getActivity(), R.style.BottomSheetDialog);
        final View customLayout
                = getLayoutInflater()
                .inflate(
                        R.layout.custom_add_work_layout,
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


    public void OpenBob() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        ev_bob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                     /*   mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);
*/
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + (1000 * 60 * 60));

        datePickerDialog.show();

    }
}