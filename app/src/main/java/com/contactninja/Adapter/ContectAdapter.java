package com.contactninja.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.contactninja.AddContect.Addnewcontect_Activity;
import com.contactninja.ContectListAdapter;
import com.contactninja.Model.AddcontectModel;
import com.contactninja.Model.ContectListData;
import com.contactninja.R;
import com.contactninja.Utils.SessionManager;

import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ContectAdapter extends RecyclerView.Adapter<ContectAdapter.FollowFollowingListClass> {

    private final List<ContectListData.Contact> getcontect;
    public Context mCtx;
    String second_latter = "";
    String current_latter = "", image_url = "";
    public ContectAdapter(Context mCtx, List<ContectListData.Contact> getcontect) {
        this.mCtx = mCtx;
        this.getcontect = getcontect;
    }

    @NonNull
    @Override
    public FollowFollowingListClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invite_user_details, parent, false);
        return new FollowFollowingListClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowFollowingListClass holder1, int position) {
       ContectListData.Contact Contact_data=getcontect.get(position);
        holder1.userName.setText(Contact_data.getFirstname());
        holder1.userNumber.setVisibility(View.GONE);

        holder1.first_latter.setVisibility(View.VISIBLE);
        holder1.top_layout.setVisibility(View.VISIBLE);



        String first_latter = Contact_data.getFirstname().substring(0, 1).toUpperCase();
        holder1.first_latter.setText(first_latter);
        if (second_latter.equals("")) {
            current_latter = first_latter;
            second_latter = first_latter;
            holder1.first_latter.setVisibility(View.VISIBLE);
            holder1.top_layout.setVisibility(View.VISIBLE);

        } else if (second_latter.equals(first_latter)) {
            current_latter = second_latter;
            // inviteUserDetails.setF_latter("");
            holder1.first_latter.setVisibility(View.GONE);
            holder1.top_layout.setVisibility(View.GONE);

        } else {

            current_latter = first_latter;
            second_latter = first_latter;
            holder1.first_latter.setVisibility(View.VISIBLE);
            holder1.top_layout.setVisibility(View.VISIBLE);


        }


        if (Contact_data.getContactImage() == null) {
            String name = Contact_data.getFirstname() + " " + Contact_data.getLastname();
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


            holder1.no_image.setText(add_text);
            holder1.no_image.setVisibility(View.VISIBLE);
            holder1.profile_image.setVisibility(View.GONE);
        } else {
            Glide.with(mCtx).
                    load(Contact_data.getContactImage())
                    .placeholder(R.drawable.shape_primary_circle)
                    .error(R.drawable.shape_primary_circle)
                    .into(holder1.profile_image);
            holder1.no_image.setVisibility(View.GONE);
            holder1.profile_image.setVisibility(View.VISIBLE);
        }


        holder1.main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager.setAdd_Contect_Detail(mCtx, new AddcontectModel());
                SessionManager.setOneCotect_deatil(mCtx, Contact_data);
                Intent addnewcontect = new Intent(mCtx, Addnewcontect_Activity.class);
                SessionManager.setContect_flag("read");
                mCtx.startActivity(addnewcontect);
            }
        });


    }

    @Override
    public int getItemCount() {
        return getcontect.size();
    }

    public class FollowFollowingListClass extends RecyclerView.ViewHolder {
        TextView no_image;
        TextView userName, userNumber, first_latter;
        CircleImageView profile_image;
        LinearLayout top_layout;
        RelativeLayout main_layout;


        public FollowFollowingListClass(@NonNull View itemView) {
            super(itemView);
            first_latter = itemView.findViewById(R.id.first_latter);
            userName = itemView.findViewById(R.id.username);
            userNumber = itemView.findViewById(R.id.user_number);
            profile_image = itemView.findViewById(R.id.profile_image);
            no_image = itemView.findViewById(R.id.no_image);
            top_layout = itemView.findViewById(R.id.top_layout);
            main_layout = itemView.findViewById(R.id.main_layout);


        }


    }
}
