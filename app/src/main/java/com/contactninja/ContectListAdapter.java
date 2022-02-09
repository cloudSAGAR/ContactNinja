package com.contactninja;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.contactninja.AddContect.Addnewcontect_Activity;
import com.contactninja.Model.AddcontectModel;
import com.contactninja.Model.ContectListData;
import com.contactninja.Utils.SessionManager;
import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import de.hdodenhof.circleimageview.CircleImageView;
@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak")
public class ContectListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private long mLastClickTime = 0;
    private static final int LOADING = 0;
    private static final int ITEM = 1;
    private final Context context;
    String second_latter = "";
    String current_latter = "", image_url = "";
    private List<ContectListData.Contact> contacts;
    private boolean isLoadingAdded = false;

    public ContectListAdapter(@SuppressLint("UnknownNullness") Context context) {
        this.context = context;
        contacts = new LinkedList<>();
    }

    public void setContactList(@SuppressLint("UnknownNullness") List<ContectListData.Contact> contacts) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.invite_user_details, parent, false);
                viewHolder = new MovieViewHolder(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.item_loading, parent, false);
                viewHolder = new LoadingViewHolder(viewLoading);

                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ContectListData.Contact Contact_data = contacts.get(position);
        //Log.e("Postion is",String.valueOf(position));
        switch (getItemViewType(position)) {
            case ITEM:
                ContectListAdapter.MovieViewHolder holder1 = (ContectListAdapter.MovieViewHolder) holder;
              try {
                  holder1.userName.setText(Contact_data.getFirstname()+" "+Contact_data.getLastname());
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
                      String name = Contact_data.getFirstname();
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
                        e.printStackTrace();
                      }


                      holder1.no_image.setText(add_text);
                      holder1.no_image.setVisibility(View.VISIBLE);
                      holder1.profile_image.setVisibility(View.GONE);
                  } else {

                    /*  CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context.getApplicationContext());
                      circularProgressDrawable.setStrokeWidth(6f);
                      circularProgressDrawable.setCenterRadius(20f);
                      circularProgressDrawable.setColorSchemeColors(context.getResources().getColor(R.color.purple_200),context.getResources().getColor(R.color.purple_200),context.getResources().getColor(R.color.purple_200));
                      circularProgressDrawable.start();*/
                      Glide.with(context).
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
                          if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                              return;
                          }
                          mLastClickTime = SystemClock.elapsedRealtime();
                          SessionManager.setAdd_Contect_Detail(context, new AddcontectModel());
                          Log.e("List Of Contec is",new Gson().toJson(Contact_data));
                          SessionManager.setOneCotect_deatil(context, Contact_data);
                          Intent addnewcontect = new Intent(context, Addnewcontect_Activity.class);
                          SessionManager.setContect_flag("read");
                          context.startActivity(addnewcontect);
                      }
                  });
              }
              catch (Exception e)
              {
                  contacts.remove(position);
           /*       notifyDataSetChanged();*/

              }


                break;

            case LOADING:
                ContectListAdapter.LoadingViewHolder loadingViewHolder = (ContectListAdapter.LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public int getItemCount() {
       // Log.e("Size is :: ",contacts.size()+"");
        return contacts == null ? 0 : contacts.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == contacts.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new ContectListData.Contact());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = contacts.size() - 1;
        ContectListData.Contact result = getItem(position);

        if (result != null) {
            contacts.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void add(ContectListData.Contact contact) {
        contacts.add(contact);
        notifyItemInserted(contacts.size() - 1);
      //  notifyDataSetChanged();
    }

    public void addAll(List<ContectListData.Contact> contact) {
        for (ContectListData.Contact result : contact) {

            add(result);
        }

    }
    public void updateList(List<ContectListData.Contact> list) {
        contacts = list;
        notifyDataSetChanged();
    }

    public void removeloist() {
        contacts.clear();
        notifyDataSetChanged();
    }

    public ContectListData.Contact getItem(int position) {
        return contacts.get(position);
    }


    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView no_image;
        TextView userName, userNumber, first_latter;
        CircleImageView profile_image;
        LinearLayout top_layout;
        RelativeLayout main_layout;

        public MovieViewHolder(View itemView) {
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

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {

        private final ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.idPBLoading);

        }
    }

}


