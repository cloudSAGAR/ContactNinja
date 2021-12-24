package com.contactninja.Setting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.contactninja.Model.Plandetail;
import com.contactninja.R;

import java.util.ArrayList;
import java.util.List;

public class CurrentPlanActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView iv_back;
    ViewPager2 viewPager2;
    List<Plandetail> plandetailslist=new ArrayList<>();
    TextView tv_save;

    @Override
    protected void onCreate(@SuppressLint("UnknownNullness") Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_plan);
        IntentUI();
        ListShow();
    }

    private void ListShow() {
        // set adapter on viewpager
    if(plandetailslist.size()!=0){
        plandetailslist.clear();
    }

    for(int k=0;k<=4;k++) {



        if (k ==0 ) {
            List<Plandetail.Plansublist> plansublists123 = new ArrayList<>();
            Plandetail plan=new Plandetail();
            plan.setPlan_name("Ninja Text Master");
            plan.setPlan_description("Master text marketing with broadcasts and lead tracking to multiply the chances of your messages to be opened and converted.\n");
            plan.setPlan_free("$39.95/Monthly");
            for (int i = 0; i <= 5; i++) {
                if (i == 0) {
                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("1");
                    plansublist.setCheck_text("Contact Aggregation");
                    plansublists123.add(i, plansublist);
                } else if (i == 1) {
                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();

                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("2");
                    plansublist.setCheck_text("1 BZcard  ");
                    plansublists123.add(i, plansublist);

                } else if (i == 2) {

                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();

                    plansublist.setCheck_flag("false");
                    plansublist.setCheck_id("3");
                    plansublist.setCheck_text("Broadcasting");
                    plansublists123.add(i, plansublist);
                } else if (i == 3) {

                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();

                    plansublist.setCheck_flag("false");
                    plansublist.setCheck_id("3");
                    plansublist.setCheck_text("Broadcasting");
                    plansublists123.add(i, plansublist);

                } else if (i == 4) {
                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                    plansublist.setCheck_flag("false");
                    plansublist.setCheck_id("4");
                    plansublist.setCheck_text("Lead Tracking");
                    plansublists123.add(i, plansublist);

                } else if (i == 5) {

                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                    plansublist.setCheck_flag("false");
                    plansublist.setCheck_id("5");
                    plansublist.setCheck_text("Automated Campaigns");
                    plansublists123.add(i, plansublist);
                } else if (i == 6) {

                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                    plansublist.setCheck_flag("false");
                    plansublist.setCheck_id("6");
                    plansublist.setCheck_text("Calendar Integration");
                    plansublists123.add(i, plansublist);
                }
                plan.setPlansublist(plansublists123);

            }
            plandetailslist.add(plan);

        } else if (k == 1) {
            List<Plandetail.Plansublist> plansublists123 = new ArrayList<>();
            Plandetail plan=new Plandetail();

            plan.setPlan_name("Ninja BZcard");
            plan.setPlan_description("Master text marketing with broadcasts and lead tracking to multiply the chances of your messages to be opened and converted.\n");
            plan.setPlan_free("$9.95/Monthly");
            for (int i = 0; i <= 5; i++) {
                if (i == 0) {
                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();

                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("1");
                    plansublist.setCheck_text("Contact Aggregation");


                    plansublists123.add(i, plansublist);
                } else if (i == 1) {
                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();

                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("2");
                    plansublist.setCheck_text("1 BZcard  ");
                    plansublists123.add(i, plansublist);


                } else if (i == 2) {

                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();

                    plansublist.setCheck_flag("false");
                    plansublist.setCheck_id("3");
                    plansublist.setCheck_text("Broadcasting");
                    plansublists123.add(i, plansublist);
                } else if (i == 3) {

                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();

                    plansublist.setCheck_flag("false");
                    plansublist.setCheck_id("3");
                    plansublist.setCheck_text("Broadcasting");
                    plansublists123.add(i, plansublist);

                } else if (i == 4) {
                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                    plansublist.setCheck_flag("false");
                    plansublist.setCheck_id("4");
                    plansublist.setCheck_text("Lead Tracking");
                    plansublists123.add(i, plansublist);

                } else if (i == 5) {

                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                    plansublist.setCheck_flag("false");
                    plansublist.setCheck_id("5");
                    plansublist.setCheck_text("Automated Campaigns");
                    plansublists123.add(i, plansublist);
                } else if (i == 6) {

                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                    plansublist.setCheck_flag("false");
                    plansublist.setCheck_id("6");
                    plansublist.setCheck_text("Calendar Integration");
                    plansublists123.add(i, plansublist);

                }
                plan.setPlansublist(plansublists123);

            }
            plandetailslist.add(plan);

        } else if (k == 2) {
            List<Plandetail.Plansublist> plansublists123 = new ArrayList<>();

            Plandetail plan=new Plandetail();


            plan.setPlan_name("Ninja BZcard");
            plan.setPlan_description("Master text marketing with broadcasts and lead tracking to multiply the chances of your messages to be opened and converted.\n");
            plan.setPlan_free("$9.95/Monthly");
            for (int i = 0; i <= 5; i++) {
                if (i == 0) {
                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();

                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("1");
                    plansublist.setCheck_text("Contact Aggregation");


                    plansublists123.add(i, plansublist);
                } else if (i == 1) {
                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();

                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("2");
                    plansublist.setCheck_text("1 BZcard  ");
                    plansublists123.add(i, plansublist);


                } else if (i == 2) {

                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();

                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("3");
                    plansublist.setCheck_text("Broadcasting");
                    plansublists123.add(i, plansublist);
                } else if (i == 3) {

                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();

                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("3");
                    plansublist.setCheck_text("Broadcasting");
                    plansublists123.add(i, plansublist);

                } else if (i == 4) {
                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("4");
                    plansublist.setCheck_text("Lead Tracking");
                    plansublists123.add(i, plansublist);

                } else if (i == 5) {

                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                    plansublist.setCheck_flag("false");
                    plansublist.setCheck_id("5");
                    plansublist.setCheck_text("Automated Campaigns");
                    plansublists123.add(i, plansublist);
                } else if (i == 6) {

                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                    plansublist.setCheck_flag("false");
                    plansublist.setCheck_id("6");
                    plansublist.setCheck_text("Calendar Integration");
                    plansublists123.add(i, plansublist);

                }
                plan.setPlansublist(plansublists123);


            }
            plandetailslist.add(plan);
        } else if (k == 3) {
            List<Plandetail.Plansublist> plansublists123 = new ArrayList<>();

            Plandetail plan=new Plandetail();

            plan.setPlan_name("Ninja BZcard");
            plan.setPlan_description("Master text marketing with broadcasts and lead tracking to multiply the chances of your messages to be opened and converted.\n");
            plan.setPlan_free("Free");
            for (int i = 0; i <= 5; i++) {
                if (i == 0) {
                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();

                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("1");
                    plansublist.setCheck_text("Contact Aggregation");


                    plansublists123.add(i, plansublist);
                } else if (i == 1) {
                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();

                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("2");
                    plansublist.setCheck_text("1 BZcard  ");
                    plansublists123.add(i, plansublist);


                } else if (i == 2) {

                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();

                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("3");
                    plansublist.setCheck_text("Broadcasting");
                    plansublists123.add(i, plansublist);
                } else if (i == 3) {

                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();

                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("3");
                    plansublist.setCheck_text("Broadcasting");
                    plansublists123.add(i, plansublist);

                } else if (i == 4) {
                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("4");
                    plansublist.setCheck_text("Lead Tracking");
                    plansublists123.add(i, plansublist);

                } else if (i == 5) {

                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("5");
                    plansublist.setCheck_text("Automated Campaigns");
                    plansublists123.add(i, plansublist);
                } else if (i == 6) {

                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("6");
                    plansublist.setCheck_text("Calendar Integration");
                    plansublists123.add(i, plansublist);

                }
                plan.setPlansublist(plansublists123);

            }
            plandetailslist.add(plan);
        }

    }










        viewPager2.setAdapter(new ViewPageAdepter(getApplicationContext(),plandetailslist));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePagerTransformer=new CompositePageTransformer();
        compositePagerTransformer.addTransformer(new MarginPageTransformer(40));
        compositePagerTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r=1-Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
                if(viewPager2.getCurrentItem()==0){
                    tv_save.setText(getResources().getString(R.string.Current_Plan));
                }else {
                    tv_save.setText(getResources().getString(R.string.Upgrade_Plan));
                }
            }
        });
        viewPager2.setPageTransformer(compositePagerTransformer);
    }

    private void IntentUI() {
        tv_save = findViewById(R.id.tv_save);
        viewPager2 = findViewById(R.id.viewpager);
        iv_back=findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(@SuppressLint("UnknownNullness") View v) {
        switch (v.getId()){

            case R.id.iv_back:
                onBackPressed();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public static class ViewPageAdepter extends RecyclerView.Adapter<ViewPageAdepter.viewholder>{

        public Context mCtx;
        List<Plandetail> plandetailslist;
        Plandetail plandetail;
        public ViewPageAdepter(Context applicationContext,    List<Plandetail> plandetailslist) {
            this.mCtx=applicationContext;
            this.plandetailslist=plandetailslist;
        }

        @NonNull
        @Override
        public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.plan_item_select, parent, false);
            return new viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull viewholder holder, int position) {
            plandetail=plandetailslist.get(position);
holder.tv_title.setText(plandetail.getPlan_name());
holder.tv_prise.setText(plandetail.getPlan_free());
holder.tv_text.setText(plandetail.getPlan_description());

            holder.plan_condition.setLayoutManager(new LinearLayoutManager(mCtx));
            PlanDataAdapter planDataAdapter = new PlanDataAdapter(mCtx, plandetail.getPlansublist());
            holder.plan_condition.setAdapter(planDataAdapter);


        }

        @Override
        public int getItemCount() {
            return plandetailslist.size();
        }

        public class viewholder extends RecyclerView.ViewHolder {
            RecyclerView plan_condition;
            TextView tv_title,tv_prise,tv_text;
            public viewholder(View view) {
                super(view);
                plan_condition=view.findViewById(R.id.plan_condition);
                tv_title=view.findViewById(R.id.tv_title);
                tv_prise=view.findViewById(R.id.tv_prise);
                tv_text=view.findViewById(R.id.tv_text);

            }
        }
        public class PlanDataAdapter extends RecyclerView.Adapter<PlanDataAdapter.PlanDataclass> {

            public Activity mCtx;
            private final Context mcntx;
            private final List<Plandetail.Plansublist> planDetails;


            public PlanDataAdapter(Context mCtx, List<Plandetail.Plansublist> planDetails) {
                this.mcntx = mCtx;
                this.planDetails = planDetails;
            }


            @NonNull
            @Override
            public PlanDataclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View view = inflater.inflate(R.layout.plandetail_layout_user, parent, false);
                return new PlanDataclass(view);
            }

            @Override
            public void onBindViewHolder(@NonNull PlanDataclass holder, int position) {
                holder.check_text.setText(planDetails.get(position).getCheck_text());
                holder.check_text1.setText(planDetails.get(position).getCheck_text());


                String plan_check = planDetails.get(position).getCheck_flag();
                if (plan_check.equals("true")) {
                    holder.unselected_layout.setVisibility(View.GONE);
                    holder.selected_layout.setVisibility(View.VISIBLE);
                } else {
                    holder.unselected_layout.setVisibility(View.VISIBLE);
                    holder.selected_layout.setVisibility(View.GONE);
                }
            }

            @Override
            public int getItemCount() {
                return planDetails.size();
            }


            public class PlanDataclass extends RecyclerView.ViewHolder {

                ImageView plan_selected_or_not, plan_selected_or_not1;
                TextView check_text, check_text1;
                LinearLayout unselected_layout, selected_layout;

                public PlanDataclass(@NonNull View itemView) {
                    super(itemView);
                    check_text = itemView.findViewById(R.id.check_text);
                    check_text1 = itemView.findViewById(R.id.check_text1);
                    selected_layout = itemView.findViewById(R.id.selected_layout);
                    unselected_layout = itemView.findViewById(R.id.unselected_layout);


                }

            }

        }
    }
}