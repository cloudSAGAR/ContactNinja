package com.contactninja.Broadcast_Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.contactninja.Fragment.AddContect_Fragment.EditContectFragment;
import com.contactninja.Model.WorkTypeData;
import com.contactninja.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Repeat_weekly_Activity extends AppCompatActivity {
    ImageView iv_back,iv_more;
    TextView save_button,date_spinner,tv_day;
    String type="",week_txt="";
    Spinner day_spinner;
    private int mYear, mMonth, mDay, mHour, mMinute;
    List<String> select_string=new ArrayList<>();
    String select_day="",end_date="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repeat_weekly);
        IntentUI();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        type=bundle.getString("type","");

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.broadcast_week));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        day_spinner.setAdapter(adapter);
        day_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                week_txt=String.valueOf(day_spinner.getSelectedItem());
                Log.e("Text is ",week_txt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        date_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Repeat_weekly_Activity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                date_spinner.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

        tv_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog_For_Home();
            }
        });
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                end_date=date_spinner.getText().toString();
                Intent intent=getIntent();
                Bundle bundle=intent.getExtras();
                String type=bundle.getString("type","");
                Intent Time_Selcet_Activity=new Intent(getApplicationContext(),Broadcast_time_select.class);
                Time_Selcet_Activity.putExtra("start_date","");
                Time_Selcet_Activity.putExtra("end_date",end_date);
                Time_Selcet_Activity.putExtra("type",type);
                Time_Selcet_Activity.putExtra("repeat",week_txt);
                Time_Selcet_Activity.putExtra("num_day",select_day);
                Time_Selcet_Activity.putExtra("m_day","");
                Time_Selcet_Activity.putExtra("m_month","");
                Time_Selcet_Activity.putExtra("m_first","");
                startActivity(Time_Selcet_Activity);
            }
        });

    }
    private void IntentUI() {
        iv_back=findViewById(R.id.iv_back);
        save_button=findViewById(R.id.save_button);
        save_button.setVisibility(View.VISIBLE);
        save_button.setText("Next");
        iv_more=findViewById(R.id.iv_more);
        iv_more.setVisibility(View.GONE);
        day_spinner=findViewById(R.id.day_spinner);
        date_spinner=findViewById(R.id.date_spinner);
        tv_day=findViewById(R.id.tv_day);
    }

    private void showBottomSheetDialog_For_Home() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this,R.style.BottomSheetDialog);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_for_day_select);
        RecyclerView home_type_list=bottomSheetDialog.findViewById(R.id.home_type_list);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(Repeat_weekly_Activity.this);
        home_type_list.setLayoutManager(layoutManager);
        TextView bt_done=bottomSheetDialog.findViewById(R.id.bt_done);
        bt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (select_string.size()==0)
                {
                    bt_done.setEnabled(false);
                }
                else {
                    for (int i=0;i<select_string.size();i++)
                    {
                        if (select_day.equals(""))
                        {
                            select_day=select_string.get(i).toString();
                        }
                        else {
                            select_day=select_day+","+select_string.get(i).toString();
                        }



                    }
                    tv_day.setText(select_day);
                    bt_done.setEnabled(true);
                }


                bottomSheetDialog.cancel();
            }
        });

        List<String> workTypeData=new ArrayList<>();
        workTypeData.add(0,"Sunday");
        workTypeData.add(1,"Monday");
        workTypeData.add(2,"Tuesday");
        workTypeData.add(3,"Wednesday");
        workTypeData.add(4,"Thursday");
        workTypeData.add(5,"Friday");
        workTypeData.add(6,"Saturday");


        WorkAdapter workAdapter=new WorkAdapter(this,workTypeData);
        home_type_list.setAdapter(workAdapter);

        bottomSheetDialog.show();
    }

    public class WorkAdapter extends RecyclerView.Adapter<WorkAdapter.InviteListDataclass> {

        public Context mCtx;

        private List<String> worklist;
        public WorkAdapter(Context context, List<String> worklist) {
            this.mCtx = context;
            this.worklist = worklist;

        }
        @NonNull
        @Override
        public WorkAdapter.InviteListDataclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.weekday_layout, parent, false);
            return new WorkAdapter.InviteListDataclass(view);
        }
        @Override
        public void onBindViewHolder(@NonNull WorkAdapter.InviteListDataclass holder, int position) {
            holder.tv_item.setText(worklist.get(position).toString());
            holder.add_new_contect_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    select_string.add(worklist.get(position).toString());
                    holder.remove_contect_icon.setVisibility(View.VISIBLE);
                    holder.add_new_contect_icon.setVisibility(View.GONE);
                }
            });
            holder.remove_contect_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    select_string.remove(worklist.get(position).toString());
                    holder.remove_contect_icon.setVisibility(View.GONE);
                    holder.add_new_contect_icon.setVisibility(View.VISIBLE);
                }
            });

        }
        @Override
        public int getItemCount() {
            return worklist.size();
        }

        public void updateList(List<String> list){
            worklist = list;
            notifyDataSetChanged();
        }

        public  class InviteListDataclass extends RecyclerView.ViewHolder {
            TextView tv_item;
            ImageView add_new_contect_icon,remove_contect_icon;

            public InviteListDataclass(@NonNull View itemView) {
                super(itemView);
                tv_item=itemView.findViewById(R.id.tv_item);
                add_new_contect_icon=itemView.findViewById(R.id.add_new_contect_icon);
                remove_contect_icon=itemView.findViewById(R.id.remove_contect_icon);
            }

        }

    }


}