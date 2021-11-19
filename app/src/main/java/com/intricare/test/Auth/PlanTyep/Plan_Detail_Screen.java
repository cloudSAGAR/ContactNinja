package com.intricare.test.Auth.PlanTyep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.intricare.test.Auth.Thankyou_Screen;
import com.intricare.test.MainActivity;
import com.intricare.test.Model.InviteListData;
import com.intricare.test.Model.Plandetail;
import com.intricare.test.Model.Plansublist;
import com.intricare.test.R;

import java.util.ArrayList;
import java.util.List;

public class Plan_Detail_Screen extends AppCompatActivity {
    TextView tv_main_text,tv_sub_titale,start_button;
    RecyclerView plan_condition;
    RecyclerView.LayoutManager layoutManager;
    List<Plandetail> plandetailslist;
    int flag=0;
    Plandetail plandetail;
    List<Plandetail.Plansublist> plansublists;
    ImageView tv_back;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_detail_screen);
        IntentUI();
        plan_condition.setLayoutManager(layoutManager);
        Intent pre_data=getIntent();
        Bundle pre_bundle=pre_data.getExtras();
        flag=pre_bundle.getInt("flag");
        mainflagdata(flag);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Thankyou_Screen.class));
            }
        });
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void mainflagdata(int flag) {
        plandetail=new Plandetail();
       // plansublist=new Plandetail.Plansublist();
        Plandetail plandetail1 =new Plandetail();

        List<Plandetail.Plansublist> plansublists123=new ArrayList<>();


        if (flag==1)
        {

            tv_main_text.setText("Ninja Free Card");
            tv_sub_titale.setText("Join the thousands of users who use Contact Ninja to manage their connections and communicate quickly and easily with their network");
            for (int i=0;i<5;i++)
            {
                if (i==0)
                {
                    Plandetail.Plansublist plansublist=new Plandetail.Plansublist();

                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("1");
                    plansublist.setCheck_text("Contact Aggregation");


                    plansublists123.add(i,plansublist);
                }
                else if (i==1)
                {
                    Plandetail.Plansublist plansublist=new Plandetail.Plansublist();

                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("2");
                    plansublist.setCheck_text("1 BZcard  ");
                    plansublists123.add(i,plansublist);


                }
                else if (i==2)
                {

                    Plandetail.Plansublist plansublist=new Plandetail.Plansublist();

                    plansublist.setCheck_flag("false");
                    plansublist.setCheck_id("3");
                    plansublist.setCheck_text("Broadcasting");
                    plansublists123.add(i,plansublist);
                }
                else if (i==3)
                {

                    Plandetail.Plansublist plansublist=new Plandetail.Plansublist();

                    plansublist.setCheck_flag("false");
                    plansublist.setCheck_id("3");
                    plansublist.setCheck_text("Broadcasting");
                    plansublists123.add(i,plansublist);

                }
                else if (i==4)
                {
                    Plandetail.Plansublist plansublist=new Plandetail.Plansublist();
                    plansublist.setCheck_flag("false");
                    plansublist.setCheck_id("4");
                    plansublist.setCheck_text("Lead Tracking");
                    plansublists123.add(i,plansublist);

                }
                else if (i==5)
                {

                    Plandetail.Plansublist plansublist=new Plandetail.Plansublist();
                    plansublist.setCheck_flag("false");
                    plansublist.setCheck_id("5");
                    plansublist.setCheck_text("Automated Campaigns");
                    plansublists123.add(i,plansublist);
                }
                else if (i==6)
                {

                    Plandetail.Plansublist plansublist=new Plandetail.Plansublist();
                    plansublist.setCheck_flag("false");
                    plansublist.setCheck_id("6");
                    plansublist.setCheck_text("Calendar Integration");
                    plansublists123.add(i,plansublist);

                }



            }
            PlanDataAdapter planDataAdapter=new PlanDataAdapter(getApplicationContext(),plansublists123);
            plan_condition.setAdapter(planDataAdapter);



        }
        else if (flag == 2)
        {

            tv_main_text.setText("Ninja Bzcard");
            tv_sub_titale.setText("Join the thousands of users who use Contact Ninja to manage their connections and communicate quickly and easily with their network");
            for (int i=0;i<5;i++)
            {
                if (i==0)
                {
                    Plandetail.Plansublist plansublist=new Plandetail.Plansublist();

                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("1");
                    plansublist.setCheck_text("Contact Aggregation");


                    plansublists123.add(i,plansublist);
                }
                else if (i==1)
                {
                    Plandetail.Plansublist plansublist=new Plandetail.Plansublist();

                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("2");
                    plansublist.setCheck_text("1 BZcard  ");
                    plansublists123.add(i,plansublist);


                }
                else if (i==2)
                {

                    Plandetail.Plansublist plansublist=new Plandetail.Plansublist();

                    plansublist.setCheck_flag("false");
                    plansublist.setCheck_id("3");
                    plansublist.setCheck_text("Broadcasting");
                    plansublists123.add(i,plansublist);
                }
                else if (i==3)
                {

                    Plandetail.Plansublist plansublist=new Plandetail.Plansublist();

                    plansublist.setCheck_flag("false");
                    plansublist.setCheck_id("3");
                    plansublist.setCheck_text("Broadcasting");
                    plansublists123.add(i,plansublist);

                }
                else if (i==4)
                {
                    Plandetail.Plansublist plansublist=new Plandetail.Plansublist();
                    plansublist.setCheck_flag("false");
                    plansublist.setCheck_id("4");
                    plansublist.setCheck_text("Lead Tracking");
                    plansublists123.add(i,plansublist);

                }
                else if (i==5)
                {

                    Plandetail.Plansublist plansublist=new Plandetail.Plansublist();
                    plansublist.setCheck_flag("false");
                    plansublist.setCheck_id("5");
                    plansublist.setCheck_text("Automated Campaigns");
                    plansublists123.add(i,plansublist);
                }
                else if (i==6)
                {

                    Plandetail.Plansublist plansublist=new Plandetail.Plansublist();
                    plansublist.setCheck_flag("false");
                    plansublist.setCheck_id("6");
                    plansublist.setCheck_text("Calendar Integration");
                    plansublists123.add(i,plansublist);

                }



            }
            PlanDataAdapter planDataAdapter=new PlanDataAdapter(getApplicationContext(),plansublists123);
            plan_condition.setAdapter(planDataAdapter);

        }
        else if (flag == 3)
        {


            tv_main_text.setText("Ninja Text Master");
            tv_sub_titale.setText("Join the thousands of users who use Contact Ninja to manage their connections and communicate quickly and easily with their network");
            for (int i=0;i<5;i++)
            {
                if (i==0)
                {
                    Plandetail.Plansublist plansublist=new Plandetail.Plansublist();

                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("1");
                    plansublist.setCheck_text("Contact Aggregation");


                    plansublists123.add(i,plansublist);
                }
                else if (i==1)
                {
                    Plandetail.Plansublist plansublist=new Plandetail.Plansublist();

                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("2");
                    plansublist.setCheck_text("1 BZcard  ");
                    plansublists123.add(i,plansublist);


                }
                else if (i==2)
                {

                    Plandetail.Plansublist plansublist=new Plandetail.Plansublist();

                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("3");
                    plansublist.setCheck_text("Broadcasting");
                    plansublists123.add(i,plansublist);
                }
                else if (i==3)
                {

                    Plandetail.Plansublist plansublist=new Plandetail.Plansublist();

                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("3");
                    plansublist.setCheck_text("Broadcasting");
                    plansublists123.add(i,plansublist);

                }
                else if (i==4)
                {
                    Plandetail.Plansublist plansublist=new Plandetail.Plansublist();
                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("4");
                    plansublist.setCheck_text("Lead Tracking");
                    plansublists123.add(i,plansublist);

                }
                else if (i==5)
                {

                    Plandetail.Plansublist plansublist=new Plandetail.Plansublist();
                    plansublist.setCheck_flag("false");
                    plansublist.setCheck_id("5");
                    plansublist.setCheck_text("Automated Campaigns");
                    plansublists123.add(i,plansublist);
                }
                else if (i==6)
                {

                    Plandetail.Plansublist plansublist=new Plandetail.Plansublist();
                    plansublist.setCheck_flag("false");
                    plansublist.setCheck_id("6");
                    plansublist.setCheck_text("Calendar Integration");
                    plansublists123.add(i,plansublist);

                }



            }
            PlanDataAdapter planDataAdapter=new PlanDataAdapter(getApplicationContext(),plansublists123);
            plan_condition.setAdapter(planDataAdapter);
        }
        else if (flag == 4)
        {

            tv_main_text.setText("ContactNinja");
            tv_sub_titale.setText("Join the thousands of users who use Contact Ninja to manage their connections and communicate quickly and easily with their network");
            for (int i=0;i<5;i++)
            {
                if (i==0)
                {
                    Plandetail.Plansublist plansublist=new Plandetail.Plansublist();

                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("1");
                    plansublist.setCheck_text("Contact Aggregation");


                    plansublists123.add(i,plansublist);
                }
                else if (i==1)
                {
                    Plandetail.Plansublist plansublist=new Plandetail.Plansublist();

                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("2");
                    plansublist.setCheck_text("1 BZcard  ");
                    plansublists123.add(i,plansublist);


                }
                else if (i==2)
                {

                    Plandetail.Plansublist plansublist=new Plandetail.Plansublist();

                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("3");
                    plansublist.setCheck_text("Broadcasting");
                    plansublists123.add(i,plansublist);
                }
                else if (i==3)
                {

                    Plandetail.Plansublist plansublist=new Plandetail.Plansublist();

                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("3");
                    plansublist.setCheck_text("Broadcasting");
                    plansublists123.add(i,plansublist);

                }
                else if (i==4)
                {
                    Plandetail.Plansublist plansublist=new Plandetail.Plansublist();
                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("4");
                    plansublist.setCheck_text("Lead Tracking");
                    plansublists123.add(i,plansublist);

                }
                else if (i==5)
                {

                    Plandetail.Plansublist plansublist=new Plandetail.Plansublist();
                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("5");
                    plansublist.setCheck_text("Automated Campaigns");
                    plansublists123.add(i,plansublist);
                }
                else if (i==6)
                {

                    Plandetail.Plansublist plansublist=new Plandetail.Plansublist();
                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("6");
                    plansublist.setCheck_text("Calendar Integration");
                    plansublists123.add(i,plansublist);

                }



            }
            PlanDataAdapter planDataAdapter=new PlanDataAdapter(getApplicationContext(),plansublists123);
            plan_condition.setAdapter(planDataAdapter);

        }

    }

    private void IntentUI() {
        tv_main_text=findViewById(R.id.tv_main_text);
        tv_sub_titale=findViewById(R.id.tv_sub_titale);
        plan_condition=findViewById(R.id.plan_condition);
        layoutManager=new LinearLayoutManager(this);
        plandetailslist=new ArrayList<>();
        plansublists=new ArrayList<>();
        start_button=findViewById(R.id.start_button);
        tv_back=findViewById(R.id.tv_back);

       }


    public class PlanDataAdapter extends RecyclerView.Adapter<PlanDataAdapter.PlanDataclass>
    {

        public Activity mCtx;
        private  Context mcntx;
        private List<Plandetail.Plansublist> planDetails;



        public PlanDataAdapter(Context mCtx, List<Plandetail.Plansublist> planDetails) {
            this.mcntx = mCtx;
            this.planDetails = planDetails;
        }



        @NonNull
        @Override
        public PlanDataclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.plandetail_layout, parent, false);
            return new PlanDataclass(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PlanDataclass holder, int position) {
            holder.check_text.setText(planDetails.get(position).getCheck_text());
            holder.check_text1.setText(planDetails.get(position).getCheck_text());


            String plan_check=planDetails.get(position).getCheck_flag();
            if (plan_check.equals("true"))

            {
                holder.unselected_layout.setVisibility(View.GONE);
                holder.selected_layout.setVisibility(View.VISIBLE);
            }
            else {
                holder.unselected_layout.setVisibility(View.VISIBLE);
                holder.selected_layout.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return planDetails.size();
        }



        public  class PlanDataclass extends RecyclerView.ViewHolder {

            ImageView plan_selected_or_not,plan_selected_or_not1;
            TextView check_text,check_text1;
            LinearLayout unselected_layout,selected_layout;

            public PlanDataclass(@NonNull View itemView) {
                super(itemView);
                plan_selected_or_not=findViewById(R.id.plan_selected_or_not);
                check_text = itemView.findViewById(R.id.check_text);
                plan_selected_or_not1=findViewById(R.id.plan_selected_or_not1);
                check_text1 = itemView.findViewById(R.id.check_text1);
                selected_layout=itemView.findViewById(R.id.selected_layout);
                unselected_layout=itemView.findViewById(R.id.unselected_layout);


            }

        }

    }

}
