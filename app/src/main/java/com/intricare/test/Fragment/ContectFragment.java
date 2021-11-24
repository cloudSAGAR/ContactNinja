package com.intricare.test.Fragment;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.data.DataHolder;
import com.google.gson.Gson;
import com.intricare.test.Auth.AddContect.Addnewcontect_Activity;
import com.intricare.test.MainActivity;
import com.intricare.test.Model.InviteListData;
import com.intricare.test.R;
import com.reddit.indicatorfastscroll.FastScrollItemIndicator;
import com.reddit.indicatorfastscroll.FastScrollerThumbView;
import com.reddit.indicatorfastscroll.FastScrollerView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContectFragment extends Fragment {

    ConstraintLayout mMainLayout;
    public static final int RequestPermissionCode = 1;
    Context mCtx;
    Cursor cursor;
    public static UserListDataAdapter userListDataAdapter;
    public static ArrayList<InviteListData> inviteListData=new ArrayList<>();
    RecyclerView rvinviteuserdetails;
    String userName, user_phone_number,user_image,user_des,strtext="",old_latter="";

    FastScrollerView fastscroller;
    FastScrollerThumbView fastscroller_thumb;
   // ImageView iv_back,iv_more;
    SearchView contect_search;
    TextView add_new_contect,num_count;
    Handler mHandler=new Handler();

    public ContectFragment(String strtext) {

        this.strtext=strtext;
        Log.e("String is",strtext);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View content_view=inflater.inflate(R.layout.fragment_contect, container, false);

        IntentUI(content_view);
        mCtx=getContext();
        Log.e("frgamebt Call","Yse0");
        rvinviteuserdetails.setLayoutManager(new LinearLayoutManager(mCtx, LinearLayoutManager.VERTICAL, false));
        rvinviteuserdetails.setHasFixedSize(true);
        inviteListData.clear();
        userListDataAdapter = new UserListDataAdapter(getActivity(), getActivity(), inviteListData);
        rvinviteuserdetails.setAdapter(userListDataAdapter);
        userListDataAdapter.notifyDataSetChanged();
        //Faste View Code
        fastscroller_thumb.setupWithFastScroller(fastscroller);
        fastscroller.setUseDefaultScroller(false);
        fastscroller.getItemIndicatorSelectedCallbacks().add(
                new FastScrollerView.ItemIndicatorSelectedCallback() {
                    @Override
                    public void onItemIndicatorSelected(
                            FastScrollItemIndicator indicator,
                            int indicatorCenterY,
                            int itemPosition
                    ) {
                        //Log.e("On Top ","Yes");
                    }
                }
        );
        fastscroller.setupWithRecyclerView(
                rvinviteuserdetails,
                (position) -> {
                    // ItemModel item = data.get(position);
                    return new FastScrollItemIndicator.Text(
                            inviteListData.get(position).getUserName().substring(0, 1)
                                    .substring(0, 1)
                                    .toUpperCase()// Grab the first letter and capitalize it
                    );
                }
        );

        GetContactsIntoArrayList();


        contect_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText.toString());
                return false;
            }
        });

        num_count.setText(inviteListData.size()+" Contacts");

       if (strtext.equals(""))
       {

       }
       else {
           filter(strtext.trim());
       }
        /*strtext = ""+getArguments().getString("data");
        if (strtext.equals(""))
        {

        }
        else {
            Log.e("Data IS aaa",strtext);

            Runnable mRunnable = () -> {
                filter(strtext.trim());
            };
            int SPLASH_DISPLAY_LENGTH = 1000;
            mHandler.postDelayed(mRunnable, SPLASH_DISPLAY_LENGTH);

        }*/
        add_new_contect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addnewcontect=new Intent(getActivity(), Addnewcontect_Activity.class);
                startActivity(addnewcontect);
            }
        });

        return content_view;

    }

    void filter(String text){
       // Log.e("Data is Size",String.valueOf(inviteListData.size()));
        List<InviteListData> temp = new ArrayList();
        for(InviteListData d: inviteListData){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getUserName().contains(text)){
                temp.add(d);
            }
        }
        //update recyclerview
        userListDataAdapter.updateList(temp);
    }


    private void IntentUI(View content_view) {
        mMainLayout=content_view.findViewById(R.id.mMainLayout);
        rvinviteuserdetails=content_view.findViewById(R.id.contect_list);
        fastscroller=content_view.findViewById(R.id.fastscroller);
        fastscroller_thumb=content_view.findViewById(R.id.fastscroller_thumb);
       // iv_back=content_view.findViewById(R.id.iv_back);
        //iv_more=content_view.findViewById(R.id.iv_more);
        contect_search=content_view.findViewById(R.id.contect_search);
        add_new_contect=content_view.findViewById(R.id.add_new_contect);
        num_count=content_view.findViewById(R.id.num_count);
    }
    public void GetContactsIntoArrayList() {
        cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {

            userName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            user_phone_number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            user_image=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));
            user_des=cursor.getString(cursor.getColumnIndex(String.valueOf(ContactsContract.CommonDataKinds.Phone.DATA)));

            String unik_key=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)).substring(0, 1)
                    .substring(0, 1)
                    .toUpperCase();
            if (old_latter.equals(""))
            {
                old_latter=unik_key;

            }
            else if (old_latter.equals(unik_key))
            {
                old_latter="";
            }
            else if (!old_latter.equals(unik_key)){
                old_latter=unik_key;
            }


            inviteListData.add(new InviteListData( userName, user_phone_number,user_image,user_des,old_latter));
            userListDataAdapter.notifyDataSetChanged();
        }
        cursor.close();
    }
    public class UserListDataAdapter extends RecyclerView.Adapter<UserListDataAdapter.InviteListDataclass>
            implements Filterable {

        int last_postion=0;
        public Activity mCtx;
        private final Context mcntx;
        private List<InviteListData> userDetails;
        private List<InviteListData> userDetailsfull;
        String second_latter="";
        String current_latter="",image_url="";
        private Filter exampleFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<InviteListData> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(userDetailsfull);
                } else {
                    String userName = constraint.toString().toLowerCase().trim();
                    String userNumber = constraint.toString().toLowerCase().trim();
                    for (InviteListData item : userDetailsfull) {
                        if (item.getUserName().toLowerCase().contains(userName)
                                || item.getUserPhoneNumber().toLowerCase().contains(userNumber)) {
                            filteredList.add(item);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                userDetails.clear();
                userDetails.addAll((List<InviteListData>) results.values);
                notifyDataSetChanged();
            }
        };

        public UserListDataAdapter(Activity Ctx, Context mCtx, ArrayList<InviteListData> userDetails) {
            this.mcntx = mCtx;
            this.mCtx = Ctx;
            this.userDetails = userDetails;
            userDetailsfull = new ArrayList<>(userDetails);
        }

        @NonNull
        @Override
        public InviteListDataclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.invite_user_details, parent, false);
            return new InviteListDataclass(view);
        }

        @Override
        public void onBindViewHolder(@NonNull InviteListDataclass holder, int position) {
            InviteListData inviteUserDetails = userDetails.get(position);

                last_postion=position;


                if (inviteUserDetails.getF_latter().equals(""))
                {
                    holder.first_latter.setVisibility(View.GONE);
                    holder.top_layout.setVisibility(View.GONE);
                }
                else {

                    holder.first_latter.setVisibility(View.VISIBLE);
                    holder.first_latter.setText(inviteUserDetails.getF_latter());
                    holder.top_layout.setVisibility(View.VISIBLE);
                    String first_latter=inviteUserDetails.getUserName().substring(0,1).toUpperCase();

                    if (second_latter.equals(""))
                    {
                        current_latter=first_latter;
                        second_latter=first_latter;
                        holder.first_latter.setVisibility(View.VISIBLE);
                        holder.top_layout.setVisibility(View.VISIBLE);

                    }
                    else if (second_latter.equals(first_latter))
                    {
                        current_latter=second_latter;
                        inviteUserDetails.setF_latter("");
                        holder.first_latter.setVisibility(View.GONE);
                        holder.top_layout.setVisibility(View.GONE);

                    }
                    else {

                        current_latter=first_latter;
                        second_latter=first_latter;
                        holder.first_latter.setVisibility(View.VISIBLE);
                        holder.top_layout.setVisibility(View.VISIBLE);


                    }
                }






            String file=""+inviteUserDetails.getUserImageURL();
            if (file.equals("null"))
            {

                holder.no_image.setVisibility(View.VISIBLE);
                holder.profile_image.setVisibility(View.GONE);
                String name =inviteUserDetails.getUserName();
                holder.profile_image.setVisibility(View.GONE);
                String add_text="";
                String[] split_data=name.split(" ");
               try {
                   for (int i=0;i<split_data.length;i++)
                   {
                       if (i==0)
                       {
                           add_text=split_data[i].substring(0,1);
                       }
                       else {
                           add_text=add_text+split_data[i].substring(0,1);
                           break;
                       }
                   }
               }
               catch (Exception e)
               {

               }

                //Log.e("NEW Text ",add_text);
                holder.no_image.setText(add_text);
                holder.no_image.setVisibility(View.VISIBLE);


                // Log.e("User Name",name);
            }
            else {
                image_url=inviteUserDetails.getUserImageURL().toString();
                //Log.e("Image Url Is ",image_url);
                if (!image_url.equals(""))
                {
                    Log.e("No Image","NO"+ " "+position);
                    Log.e("Image Url is ",image_url);

/*
                    Uri uri = Uri.parse(image_url);
                    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(getContext().getContentResolver(), uri);
                    Bitmap bm = BitmapFactory.decodeStream(input);
                    Drawable d = new BitmapDrawable(bm);
                    holder.profile_image.setImageBitmap(bm);
*/


                    Glide.with(getContext()).
                            load(inviteUserDetails.getUserImageURL()).
                            apply(new RequestOptions().placeholder(R.drawable.shape_primary_circle)).
                            error(R.drawable.shape_primary_circle).
                            error(R.drawable.shape_primary_circle).
                            into(holder.profile_image);
                    holder.no_image.setVisibility(View.GONE);



                }
                else {

                    Log.e("No Image","Yes"+ " "+position);

                    holder.profile_image.setVisibility(View.GONE);
                    String name =inviteUserDetails.getUserName();

                   // Log.e("User Name",name);
                    String add_text="";
                    String[] split_data=name.split(" ");
                    for (int i=0;i<split_data.length;i++)
                    {
                        if (i==0)
                        {
                            add_text=split_data[i].substring(0,1);
                        }
                        else {
                            add_text=add_text+split_data[i].substring(0,1);
                        }
                    }
                    //Log.e("NEW Text ",add_text);
                    holder.no_image.setText(add_text);
                    holder.no_image.setVisibility(View.VISIBLE);

                }

            }
            holder.userName.setText(inviteUserDetails.getUserName());




          /*  String unik_key=inviteListData.get(position).getUserName().substring(0, 1)
                    .substring(0, 1)
                    .toUpperCase();



            Log.e("Unik Code",unik_key);*/

            holder.userNumber.setText(inviteUserDetails.getUserDescription());

        }

        @Override
        public int getItemCount() {
            return userDetails.size();
        }

        @Override
        public Filter getFilter() {
            Log.e("Fillter is",new Gson().toJson(exampleFilter));
            return exampleFilter;
        }

        public void updateList(List<InviteListData> list){
            userDetails = list;
            notifyDataSetChanged();
        }

        public  class InviteListDataclass extends RecyclerView.ViewHolder {

            TextView no_image;
            TextView userName, userNumber,first_latter;
            CircleImageView profile_image;
            LinearLayout top_layout;


            public InviteListDataclass(@NonNull View itemView) {
                super(itemView);
                first_latter=itemView.findViewById(R.id.first_latter);
                userName = itemView.findViewById(R.id.username);
                userNumber = itemView.findViewById(R.id.user_number);
                profile_image=itemView.findViewById(R.id.profile_image);
                no_image=itemView.findViewById(R.id.no_image);
                top_layout=itemView.findViewById(R.id.top_layout);
            }

        }

    }



}