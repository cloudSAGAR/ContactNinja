package com.contactninja;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.contactninja.Model.Contactdetail;
import com.contactninja.Model.ContectListData;
import com.contactninja.Utils.PaginationListener;
import com.contactninja.Utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.contactninja.Utils.PaginationListener.PAGE_START;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle")

public class Contect_Demo extends AppCompatActivity {

    RecyclerView contact_list;
    CompanyAdapter paginationAdapter;
    int currentPage = 1;
    boolean isLoading = false;
    boolean isLastPage = false;
    int Total = 0;
    LinearLayoutManager layoutManager;
    int count = 49;
    private List<ContectListData.Contact> contectListData = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@SuppressLint("UnknownNullness") Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contect_demo);
        Total = SessionManager.getContectList(getApplicationContext()).get(0).getContacts().size();
        Log.e("Size is a", String.valueOf(SessionManager.getContectList(getApplicationContext()).get(0).getContacts().size()));
        contact_list = findViewById(R.id.contact_list);
        layoutManager = new LinearLayoutManager(this);
        contact_list.setLayoutManager(layoutManager);
        paginationAdapter = new CompanyAdapter(getApplicationContext(),contectListData);
        contact_list.setAdapter(paginationAdapter);
        contact_list.setItemViewCacheSize(15000);


        if (SessionManager.getContectList(getApplicationContext()).size() != 0) {
            List<ContectListData.Contact> s = SessionManager.getContectList(getApplicationContext()).get(0).getContacts();
            List<ContectListData.Contact> mainlist = s.subList(0, count);

            if (currentPage != PAGE_START)
                paginationAdapter.removeLoading();
                paginationAdapter.addItems(mainlist);
            // check weather is last page or not
            if (Total > paginationAdapter.getItemCount()) {
                paginationAdapter.addLoading();
            } else {
                isLastPage = true;
            }
            isLoading = false;


        }

        contact_list.addOnScrollListener(new PaginationListener(layoutManager) {
            @Override
            protected void loadMoreItems() {

                    count = count + 50;
                    isLoading = true;
                    currentPage++;
                    List<ContectListData.Contact> s = SessionManager.getContectList(getApplicationContext()).get(0).getContacts();
                    contectListData = s.subList(count - 49, count);
                    if (currentPage != PAGE_START)
                        paginationAdapter.removeLoading();
                        paginationAdapter.addItems(contectListData);
                    if (Total > paginationAdapter.getItemCount()) {
                        paginationAdapter.addLoading();
                    } else {
                        isLastPage = true;
                    }
                    isLoading = false;


            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

    }


    public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.viewData> {
        private static final int VIEW_TYPE_LOADING = 0;
        private static final int VIEW_TYPE_NORMAL = 1;
        public Context mCtx;
        TextView phone_txt;
        Contactdetail item;
        String second_latter = "", current_latter = "";
        private boolean isLoaderVisible = false;
        private List<ContectListData.Contact> contacts = new ArrayList<>();

        public CompanyAdapter(Context context,List<ContectListData.Contact> contacts) {
            this.mCtx = context;
            this.contacts=contacts;
        }

        @NonNull
        @Override
        public CompanyAdapter.viewData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            switch (viewType) {
                case VIEW_TYPE_NORMAL:
                    return new CompanyAdapter.viewData(
                            LayoutInflater.from(parent.getContext()).inflate(R.layout.invite_user_details1, parent, false));
                case VIEW_TYPE_LOADING:
                    return new CompanyAdapter.ProgressHolder(
                            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
                default:
                    return null;
            }

        }

        @Override
        public int getItemViewType(int position) {
            if (isLoaderVisible) {
                return position == contacts.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
            } else {
                return VIEW_TYPE_NORMAL;
            }
        }

        public void addItems(List<ContectListData.Contact> postItems) {
            contacts.addAll(postItems);
            notifyDataSetChanged();


        }

        public void addLoading() {
                isLoaderVisible = true;
                contacts.add(new ContectListData.Contact());
                notifyItemInserted(contacts.size() - 1);

        }

        public void removeLoading() {
            isLoaderVisible = false;
            int position = contacts.size() - 1;
            ContectListData.Contact item = getItem(position);
            if (item != null) {
                contacts.remove(position);
                notifyItemRemoved(position);
            }
        }

        public void clear() {
            contacts.clear();
            notifyDataSetChanged();
        }


        ContectListData.Contact getItem(int position) {
            return contacts.get(position);
        }

        @Override
        public void onBindViewHolder(@NonNull CompanyAdapter.viewData holder1, int position) {
            ContectListData.Contact Contact_data = contacts.get(position);

            try {

                if (Contact_data.getIs_blocked().equals(1)) {
                    holder1.iv_block.setVisibility(View.VISIBLE);
                    holder1.userName.setTextColor(mCtx.getResources().getColor(R.color.block_item));

                } else {
                    holder1.userName.setTextColor(mCtx.getResources().getColor(R.color.unblock_item));
                    holder1.iv_block.setVisibility(View.GONE);

                }
                holder1.userName.setText(Contact_data.getFirstname() + " " + Contact_data.getLastname());
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
                    Glide.with(getApplicationContext()).
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
                /*        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();

                        SessionManager.setAdd_Contect_Detail(getActivity(), new AddcontectModel());
                        SessionManager.setOneCotect_deatil(getActivity(), Contact_data);
                        Intent addnewcontect = new Intent(getActivity(), Add_Newcontect_Activity.class);
                        SessionManager.setContect_flag("read");
                        getActivity().startActivity(addnewcontect);
                   */
                    }
                });

                holder1.main_layout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        //    broadcast_manu(Contact_data);
                        return false;
                    }
                });

            } catch (Exception e) {
                contacts.remove(position);
                /*       notifyDataSetChanged();*/

            }

        }

        public void updateList(List<ContectListData.Contact> list) {
            contacts = list;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return contacts.size();
        }

        public void removeitem() {
            contacts.clear();
            notifyDataSetChanged();
        }

        public class viewData extends RecyclerView.ViewHolder {
            TextView no_image;
            TextView userName, userNumber, first_latter;
            CircleImageView profile_image;
            LinearLayout top_layout;
            RelativeLayout main_layout;
            ImageView iv_block;


            public viewData(@NonNull View itemView) {
                super(itemView);
                first_latter = itemView.findViewById(R.id.first_latter);
                userName = itemView.findViewById(R.id.username);
                userNumber = itemView.findViewById(R.id.user_number);
                profile_image = itemView.findViewById(R.id.profile_image);
                no_image = itemView.findViewById(R.id.no_image);
                top_layout = itemView.findViewById(R.id.top_layout);
                main_layout = itemView.findViewById(R.id.main_layout);
                iv_block = itemView.findViewById(R.id.iv_block);
            }
        }

        public class ProgressHolder extends CompanyAdapter.viewData {
            ProgressHolder(View itemView) {
                super(itemView);
            }

        }
    }

}