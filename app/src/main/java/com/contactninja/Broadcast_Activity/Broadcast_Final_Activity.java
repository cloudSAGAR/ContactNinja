package com.contactninja.Broadcast_Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.contactninja.Fragment.Broadcast_Frgment.CardClick;
import com.contactninja.Model.Broadcast_Data;
import com.contactninja.Model.Broadcast_image_list;
import com.contactninja.Model.ContectListData;
import com.contactninja.Model.Grouplist;
import com.contactninja.R;
import com.contactninja.Utils.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Broadcast_Final_Activity extends AppCompatActivity implements View.OnClickListener ,CardClick {
    ImageView iv_back, iv_Setting;
    TextView save_button,tv_start,tv_repite,tv_ends;
    RecyclerView add_contect_list,add_group_list;
    LinearLayoutManager layoutManager,layoutManager1;
    public TopUserListDataAdapter topUserListDataAdapter;
    List<ContectListData.Contact> select_contectListData;
    SessionManager sessionManager;
    List<Grouplist.Group> select_group;
    GroupDataAdapter groupDataAdapter;
    String start_date="",end_date="",time="",type="";
    List<Broadcast_image_list> broadcast_image_list=new ArrayList<>();
    CardListAdepter cardListAdepter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_final);
        IntentUI();

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        start_date=bundle.getString("start_date","");
        end_date=bundle.getString("end_date","");
        type=bundle.getString("type","");
        time=bundle.getString("time","");
        tv_start.setText(""+start_date+" @ "+time);
        tv_ends.setText(""+end_date);
        tv_repite.setText(type);
        sessionManager=new SessionManager(this);

        select_contectListData=new ArrayList<>();
        select_contectListData.addAll(sessionManager.getContectList_broadcste(this));

        topUserListDataAdapter=new TopUserListDataAdapter(this,getApplicationContext(),select_contectListData);
        add_contect_list.setAdapter(topUserListDataAdapter);
        topUserListDataAdapter.notifyDataSetChanged();
        select_group=new ArrayList<>();
        select_group.addAll(sessionManager.getgroup_broadcste(this));
        Log.e("Group List",new Gson().toJson(sessionManager.getgroup_broadcste(this)));


        groupDataAdapter=new GroupDataAdapter(this,getApplicationContext(),select_group);
        add_group_list.setAdapter(groupDataAdapter);
        broadcast_image_list.addAll(sessionManager.getAdd_Broadcast_Data(getApplicationContext()).getBroadcast_image_lists());

        alertbox();
       save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Intent intent1= new Intent(getApplicationContext(),Brodcsast_Tankyou.class);
               intent1.putExtra("s_name","final");
                startActivity(intent1);
                finish();
            }
        });
    }

    public void alertbox()
    {
        final View mView = getLayoutInflater().inflate(R.layout.brodcaste_link_dialog_item, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.DialogStyle);
        bottomSheetDialog.setContentView(mView);
        TextView tv_text_link = bottomSheetDialog.findViewById(R.id.tv_text_link);
        ImageView iv_send = bottomSheetDialog.findViewById(R.id.iv_send);
        ImageView iv_card_list = bottomSheetDialog.findViewById(R.id.iv_card_list);
        ImageView iv_link_icon = bottomSheetDialog.findViewById(R.id.iv_link_icon);
        ImageView iv_cancle_select_image = bottomSheetDialog.findViewById(R.id.iv_cancle_select_image);
        ImageView iv_selected = bottomSheetDialog.findViewById(R.id.iv_selected);
        LinearLayout lay_link_copy = bottomSheetDialog.findViewById(R.id.lay_link_copy);
        LinearLayout lay_main_choose_send = bottomSheetDialog.findViewById(R.id.lay_main_choose_send);
        RecyclerView rv_image_card = bottomSheetDialog.findViewById(R.id.rv_image_card);
        EditText edit_message = bottomSheetDialog.findViewById(R.id.edit_message);
        CoordinatorLayout layout_select_image=bottomSheetDialog.findViewById(R.id.layout_select_image);
        LinearLayout lay_sendnow = bottomSheetDialog.findViewById(R.id.lay_sendnow);
        LinearLayout lay_schedule = bottomSheetDialog.findViewById(R.id.lay_schedule);

        Broadcast_Data b_Data= sessionManager.getAdd_Broadcast_Data(getApplicationContext());

        Log.e("Data Is ",new Gson().toJson(b_Data));
        edit_message.setText(b_Data.getLink_text());
        tv_text_link.setText(b_Data.getLink());
        lay_sendnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1= new Intent(getApplicationContext(),Brodcsast_Tankyou.class);
                intent1.putExtra("s_name","default");
                startActivity(intent1);
                finish();
            }
        });
        lay_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Broadcast_to_repeat.class));
                finish();
            }
        });
        edit_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_card_list.setImageResource(R.drawable.ic_card_blank);
                rv_image_card.setVisibility(View.GONE);
                iv_card_list.setSelected(false);
            }
        });


        iv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Broadcast_Data broadcast_data=new Broadcast_Data();
                broadcast_data.setLink(tv_text_link.getText().toString());
                broadcast_data.setLink_text(edit_message.getText().toString());
                broadcast_data.setBroadcast_image_lists(broadcast_image_list);
                sessionManager.setAdd_Broadcast_Data(broadcast_data);
                lay_main_choose_send.setVisibility(View.VISIBLE);

            }
        });



        broadcast_image_list.clear();
        for (int i=0;i<=20;i++){
            Broadcast_image_list item=new Broadcast_image_list();
            if(i%2 == 0){
                item.setId(i);
                item.setScelect(false);
                item.setImagename("card_1");
            }else {
                item.setId(i);
                item.setScelect(false);
                item.setImagename("card_2");
            }
            broadcast_image_list.add(item);
        }
        rv_image_card.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        rv_image_card.setHasFixedSize(true);
        cardListAdepter = new CardListAdepter(this, broadcast_image_list,iv_selected,this,layout_select_image);
        rv_image_card.setAdapter(cardListAdepter);


        lay_link_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_link_icon.setImageResource(R.drawable.ic_link_dark);
                tv_text_link.setTextColor(getResources().getColor(R.color.purple_200));
                tv_text_link.setText(getResources().getString(R.string.link_click));
            }
        });
        iv_card_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {


                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);

                    iv_card_list.setImageResource(R.drawable.ic_card_blank);
                    rv_image_card.setVisibility(View.GONE);
                    iv_card_list.setSelected(false);
                }else {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);

                    iv_card_list.setImageResource(R.drawable.ic_card_fill);
                    rv_image_card.setVisibility(View.VISIBLE);
                    iv_card_list.setSelected(true);
                }
            }
        });


        iv_cancle_select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_select_image.setVisibility(View.GONE);
                for(int i=0;i<broadcast_image_list.size();i++){
                    if(broadcast_image_list.get(i).isScelect()){
                        broadcast_image_list.get(i).setScelect(false);
                        break;
                    }
                }
            }
        });

        bottomSheetDialog.show();


    }
    private void IntentUI() {
        iv_back = findViewById(R.id.iv_back);
        save_button = findViewById(R.id.save_button);
        iv_Setting = findViewById(R.id.iv_Setting);
        iv_Setting.setVisibility(View.GONE);
        iv_back.setOnClickListener(this);
        save_button.setOnClickListener(this);
        save_button.setVisibility(View.VISIBLE);
        save_button.setText("Next");
        tv_start=findViewById(R.id.tv_start);
        tv_repite=findViewById(R.id.tv_repite);
        tv_ends=findViewById(R.id.tv_ends);
        save_button.setTextColor(getResources().getColor(R.color.purple_200));
        layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
        add_contect_list = findViewById(R.id.add_contect_list);
        add_contect_list.setLayoutManager(layoutManager);
        add_group_list=findViewById(R.id.add_group_list);
        layoutManager1 = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
        add_group_list.setLayoutManager(layoutManager1);
    }


    @Override
    public void onClick(View view) {

    }
    @Override
    public void Onclick(Broadcast_image_list broadcastImageList) {
        for(int i=0;i<broadcast_image_list.size();i++){
            if(broadcastImageList.getId()==broadcast_image_list.get(i).getId()){
                broadcast_image_list.get(i).setScelect(true);
            }else {
                broadcast_image_list.get(i).setScelect(false);
            }
        }
        cardListAdepter.notifyDataSetChanged();
    }


    public class GroupDataAdapter extends RecyclerView.Adapter<GroupDataAdapter.InviteListDataclass>
    {

        private final Context mcntx;
        public Activity mCtx;
        int last_postion = 0;
        String second_latter = "";
        String current_latter = "", image_url = "";
        private List<Grouplist.Group> userDetails;
        private final List<Grouplist.Group> userDetailsfull;


        public GroupDataAdapter(Activity Ctx, Context mCtx, List<Grouplist.Group> userDetails) {
            this.mcntx = mCtx;
            this.mCtx = Ctx;
            this.userDetails = userDetails;
            userDetailsfull = new ArrayList<>(userDetails);
        }

        @NonNull
        @Override
        public GroupDataAdapter.InviteListDataclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.top_user_details, parent, false);
            return new GroupDataAdapter.InviteListDataclass(view);
        }

        @Override
        public void onBindViewHolder(@NonNull GroupDataAdapter.InviteListDataclass holder, int position) {
            Grouplist.Group inviteUserDetails = userDetails.get(position);
            last_postion = position;
            holder.userName.setText(select_group.get(position).getGroupName());
            holder.top_layout.setVisibility(View.VISIBLE);

            String first_latter =select_group.get(position).getGroupName().substring(0, 1).toUpperCase();

            if (second_latter.equals("")) {
                current_latter = first_latter;
                second_latter = first_latter;

            } else if (second_latter.equals(first_latter)) {
                current_latter = second_latter;
            } else {

                current_latter = first_latter;
                second_latter = first_latter;
            }



            String file = "" + select_group.get(position).getGroupImage();
            if (file.equals("null")) {
                holder.no_image.setVisibility(View.VISIBLE);
                holder.profile_image.setVisibility(View.GONE);
                String name = select_group.get(position).getGroupName();
                holder.profile_image.setVisibility(View.GONE);
                String add_text = "";
                String[] split_data = name.split(" ");
                try {
                    for (int i = 0; i < split_data.length; i++) {
                        if (i == 0) {
                            add_text = split_data[i].substring(0, 1);
                        } else {
                            add_text = add_text + split_data[i].charAt(0);
                            break;
                        }
                    }
                } catch (Exception e) {

                }


                holder.no_image.setText(add_text);
                holder.no_image.setVisibility(View.VISIBLE);

            } else {

                image_url = select_group.get(position).getGroupImage();
                Glide.with(mCtx).
                        load(select_group.get(position).getGroupImage())
                        .placeholder(R.drawable.shape_primary_circle)
                        .error(R.drawable.shape_primary_circle)
                        .into(holder.profile_image);

            }

        /*    holder.top_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i=0;i<select_group.size();i++)
                    {
                        if (select_group.get(i).getId().equals(select_contectListData.get(position).getId()))
                        {
                            paginationAdapter.notifyItemChanged(i);
                        }
                    }
                    userDetails.remove(position);
                    topUserListDataAdapter.notifyDataSetChanged();
                    sessionManager.setgroup_broadcste(getActivity(),new ArrayList<>());
                    sessionManager.setgroup_broadcste(getActivity(),select_contectListData);


                }
            });*/


        }

        @Override
        public int getItemCount() {
            return userDetails.size();
        }

        public void remove_item(int item)
        {
            try {
                userDetails.remove(item);
                notifyItemRemoved(item);
            }
            catch (Exception e)
            {

            }

        }

        public void updateList(List<Grouplist.Group> list) {
            userDetails = list;
            notifyDataSetChanged();
        }

        public class InviteListDataclass extends RecyclerView.ViewHolder {

            TextView no_image;
            TextView userName;
            CircleImageView profile_image;
            LinearLayout top_layout;


            public InviteListDataclass(@NonNull View itemView) {
                super(itemView);

                userName = itemView.findViewById(R.id.username);
                profile_image = itemView.findViewById(R.id.profile_image);
                no_image = itemView.findViewById(R.id.no_image);
                top_layout = itemView.findViewById(R.id.main_layout);


            }

        }

    }

    public class TopUserListDataAdapter extends RecyclerView.Adapter<TopUserListDataAdapter.InviteListDataclass>
    {

        private final Context mcntx;
        public Activity mCtx;
        int last_postion = 0;
        String second_latter = "";
        String current_latter = "", image_url = "";
        private List<ContectListData.Contact> userDetails;
        private final List<ContectListData.Contact> userDetailsfull;


        public TopUserListDataAdapter(Activity Ctx, Context mCtx, List<ContectListData.Contact> userDetails) {
            this.mcntx = mCtx;
            this.mCtx = Ctx;
            this.userDetails = userDetails;
            userDetailsfull = new ArrayList<>(userDetails);
        }

        @NonNull
        @Override
        public TopUserListDataAdapter.InviteListDataclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.top_user_details, parent, false);
            return new TopUserListDataAdapter.InviteListDataclass(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TopUserListDataAdapter.InviteListDataclass holder, int position) {
            ContectListData.Contact inviteUserDetails = userDetails.get(position);
            last_postion = position;
            holder.userName.setText(select_contectListData.get(position).getFirstname());
            holder.top_layout.setVisibility(View.VISIBLE);

            String first_latter =select_contectListData.get(position).getFirstname().substring(0, 1).toUpperCase();

            if (second_latter.equals("")) {
                current_latter = first_latter;
                second_latter = first_latter;

            } else if (second_latter.equals(first_latter)) {
                current_latter = second_latter;
            } else {

                current_latter = first_latter;
                second_latter = first_latter;
            }



            String file = "" + select_contectListData.get(position).getContactImage();
            if (file.equals("null")) {
                holder.no_image.setVisibility(View.VISIBLE);
                holder.profile_image.setVisibility(View.GONE);
                String name = select_contectListData.get(position).getFirstname();
                holder.profile_image.setVisibility(View.GONE);
                String add_text = "";
                String[] split_data = name.split(" ");
                try {
                    for (int i = 0; i < split_data.length; i++) {
                        if (i == 0) {
                            add_text = split_data[i].substring(0, 1);
                        } else {
                            add_text = add_text + split_data[i].charAt(0);
                            break;
                        }
                    }
                } catch (Exception e) {

                }


                holder.no_image.setText(add_text);
                holder.no_image.setVisibility(View.VISIBLE);

            } else {
                image_url = select_contectListData.get(position).getContactImage();

                if (holder.profile_image.getDrawable() == null) {
                    Glide.with(mCtx).
                            load(select_contectListData.get(position).getContactImage())
                            .placeholder(R.drawable.shape_primary_circle)
                            .error(R.drawable.shape_primary_circle)
                            .into(holder.profile_image);
                    //Log.e("Image ","View "+position);
                } else {
                    holder.profile_image.setVisibility(View.GONE);
                    String name = select_contectListData.get(position).getFirstname();
                    String add_text = "";
                    String[] split_data = name.split(" ");
                    for (int i = 0; i < split_data.length; i++) {
                        if (i == 0) {
                            add_text = split_data[i].substring(0, 1);
                        } else {
                            add_text = add_text + split_data[i].charAt(0);
                        }
                    }
                    holder.no_image.setText(add_text);
                    holder.no_image.setVisibility(View.VISIBLE);
                }

            }

           /* holder.top_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i=0;i<contectListData.size();i++)
                    {
                        if (contectListData.get(i).getId().equals(select_contectListData.get(position).getId()))
                        {
                            groupContectAdapter.notifyItemChanged(i);
                        }
                    }
                    userDetails.remove(position);
                    topUserListDataAdapter=new Broadcste_Contect_Fragment.TopUserListDataAdapter(getActivity(),getActivity(),select_contectListData);
                    add_contect_list.setAdapter(topUserListDataAdapter);
                    sessionManager.setContectList_broadcste(getActivity(),new ArrayList<>());
                    sessionManager.setContectList_broadcste(getActivity(),select_contectListData);
                    // topUserListDataAdapter.notifyDataSetChanged();




                }
            });
*/
            if (userDetails.get(position).getFlag().equals("true"))
            {
                Log.e("Call","Yes");
                holder.top_layout.setVisibility(View.GONE);


            }
            else {
                holder.top_layout.setVisibility(View.VISIBLE);

            }

        }

        @Override
        public int getItemCount() {
            return userDetails.size();
        }

        public void remove_item(int item)
        {
            userDetails.remove(item);
            notifyItemRemoved(item);

        }

        public void updateList(List<ContectListData.Contact> list) {
            userDetails = list;
            notifyDataSetChanged();
        }

        public class InviteListDataclass extends RecyclerView.ViewHolder {

            TextView no_image;
            TextView userName;
            CircleImageView profile_image;
            LinearLayout top_layout;


            public InviteListDataclass(@NonNull View itemView) {
                super(itemView);

                userName = itemView.findViewById(R.id.username);
                profile_image = itemView.findViewById(R.id.profile_image);
                no_image = itemView.findViewById(R.id.no_image);
                top_layout = itemView.findViewById(R.id.main_layout);


            }

        }

    }



    class CardListAdepter extends RecyclerView.Adapter<CardListAdepter.cardListData> {

        Activity activity;
        List<Broadcast_image_list> broadcast_image_list;
        ImageView iv_selected;
        CardClick cardClick;
        CoordinatorLayout layout_select_image;


        public CardListAdepter(Activity activity, List<Broadcast_image_list> broadcast_image_list,
                               ImageView iv_selected,CardClick cardClick,CoordinatorLayout coordinatorLayout) {
            this.activity = activity;
            this.broadcast_image_list = broadcast_image_list;
            this.iv_selected = iv_selected;
            this.cardClick = cardClick;
            this.layout_select_image = coordinatorLayout;
        }


        @NonNull
        @Override
        public CardListAdepter.cardListData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list, parent, false);
            return new CardListAdepter.cardListData(view);
        }

        @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
        @Override
        public void onBindViewHolder(@NonNull CardListAdepter.cardListData holder, int position) {
            Broadcast_image_list item = this.broadcast_image_list.get(position);


            int resID = activity.getResources().getIdentifier(item.getImagename()
                    .replace(" ", "_").toLowerCase(), "drawable", activity.getPackageName());
            if (resID != 0) {
                Glide.with(activity.getApplicationContext()).load(resID).into(holder.iv_card);
            }
            holder.layout_select_image.setOnClickListener(v -> {
                cardClick.Onclick(item);
                item.setScelect(true);
                layout_select_image.setVisibility(View.VISIBLE);
                int resID1 = activity.getResources().getIdentifier(item.getImagename()
                        .replace(" ", "_").toLowerCase(), "drawable", activity.getPackageName());
                if (resID1 != 0) {
                    Glide.with(activity.getApplicationContext()).load(resID1).into(iv_selected);
                }
            });
            if(item.isScelect()){
                holder.layout_select_image.setBackgroundResource(R.drawable.shape_blue_10);
            }else {
                holder.layout_select_image.setBackground(null);
            }
        }


        @Override
        public int getItemCount() {
            return broadcast_image_list.size();
        }

        public class cardListData extends RecyclerView.ViewHolder {

            ImageView iv_card;
            LinearLayout layout_select_image;

            public cardListData(@NonNull View itemView) {
                super(itemView);
                iv_card = itemView.findViewById(R.id.iv_card);
                layout_select_image = itemView.findViewById(R.id.layout_select_image);
            }
        }


    }
}