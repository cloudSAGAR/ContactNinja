package com.contactninja;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.contactninja.AddContect.Add_Company_Activity;
import com.contactninja.AddContect.Add_Newcontect_Activity;
import com.contactninja.Fragment.AddContect_Fragment.Company_Fragment;
import com.contactninja.Fragment.AddContect_Fragment.ContectFragment;
import com.contactninja.Model.AddcontectModel;
import com.contactninja.Model.CompanyModel;
import com.contactninja.Model.Contactdetail;
import com.contactninja.Model.ContectListData;
import com.contactninja.Model.Csv_InviteListData;
import com.contactninja.Model.Grouplist;
import com.contactninja.Model.InviteListData;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.PaginationListener;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitApiClient;
import com.contactninja.retrofit.RetrofitApiInterface;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.reddit.indicatorfastscroll.FastScrollItemIndicator;
import com.reddit.indicatorfastscroll.FastScrollerThumbView;
import com.reddit.indicatorfastscroll.FastScrollerView;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.OptionalInt;
import java.util.TimeZone;
import java.util.stream.IntStream;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.contactninja.Utils.PaginationListener.PAGE_START;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle")

public class Contect_Demo extends AppCompatActivity  {

    RecyclerView contact_list;
    ContectListAdapter paginationAdapter;
    private List<ContectListData.Contact> contectListData=new ArrayList<>();
    int currentPage = 1;
    int perPage = 20;
    boolean isLoading = false;
    boolean isLastPage = false;
    LinearLayoutManager layoutManager;
    int count=10;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@SuppressLint("UnknownNullness") Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contect_demo);

        contact_list=findViewById(R.id.contact_list);
        layoutManager=new LinearLayoutManager(this);
        contact_list.setLayoutManager(layoutManager);
        paginationAdapter = new ContectListAdapter(getApplicationContext());
        contact_list.setAdapter(paginationAdapter);

        if (SessionManager.getContectList(getApplicationContext()).size() != 0) {


            for ( int i=0;i<count;i++)
            {
                contectListData.add(SessionManager.getContectList(getApplicationContext()).get(0).getContacts().get(i));
            }
            if (currentPage != PAGE_START)
                paginationAdapter.removeLoading();
                paginationAdapter.addAll(contectListData);
            // check weather is last page or not
            if (SessionManager.getContectList(getApplicationContext()).size() > paginationAdapter.getItemCount()) {
                paginationAdapter.addLoading();
            } else {
                isLastPage = true;
            }
            isLoading = false;


        }

        contact_list.addOnScrollListener(new PaginationListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                count=count+10;

                isLoading = true;
                currentPage++;
                for ( int i=0;i<count;i++)
                {
                    contectListData.add(SessionManager.getContectList(getApplicationContext()).get(0).getContacts().get(i));
                }
                if (currentPage != PAGE_START)
                    paginationAdapter.removeLoading();
                    paginationAdapter.addAll(contectListData);
                // check weather is last page or not
                if (SessionManager.getContectList(getApplicationContext()).size() > paginationAdapter.getItemCount()) {
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


    public class ContectListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int VIEW_TYPE_LOADING = 0;
        private static final int VIEW_TYPE_NORMAL = 1;
        private boolean isLoaderVisible = false;
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
                    View viewItem = inflater.inflate(R.layout.invite_user_details1, parent, false);
                    viewHolder = new ContectListAdapter.MovieViewHolder(viewItem);
                    break;
                case LOADING:
                    View viewLoading = inflater.inflate(R.layout.item_loading, parent, false);
                    viewHolder = new ContectListAdapter.LoadingViewHolder(viewLoading);

                    break;
            }
            return viewHolder;
        }

        @Override
        public int getItemViewType(int position) {
            if (isLoaderVisible) {
                return position == contacts.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
            } else {
                return VIEW_TYPE_NORMAL;
            }
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
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            ContectListData.Contact Contact_data = contacts.get(position);
            switch (getItemViewType(position)) {
                case ITEM:
                    ContectListAdapter.MovieViewHolder holder1 = (ContectListAdapter.MovieViewHolder) holder;
                    try {

                        if (Contact_data.getIs_blocked().equals(1)) {
                            holder1.iv_block.setVisibility(View.VISIBLE);
                            holder1.userName.setTextColor(context.getResources().getColor(R.color.block_item));

                        } else {
                            holder1.userName.setTextColor(context.getResources().getColor(R.color.unblock_item));
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

                                SessionManager.setAdd_Contect_Detail(context, new AddcontectModel());
                                SessionManager.setOneCotect_deatil(context, Contact_data);
                                Intent addnewcontect = new Intent(context, Add_Newcontect_Activity.class);
                                SessionManager.setContect_flag("read");
                                context.startActivity(addnewcontect);
                            }
                        });

                        holder1.main_layout.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {

                             //   broadcast_manu(Contact_data);
                                return false;
                            }
                        });

                    } catch (Exception e) {
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
            return contacts == null ? 0 : contacts.size();
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


        public class MovieViewHolder extends RecyclerView.ViewHolder {
            TextView no_image;
            TextView userName, userNumber, first_latter;
            CircleImageView profile_image;
            LinearLayout top_layout;
            RelativeLayout main_layout;
            ImageView iv_block;

            public MovieViewHolder(View itemView) {
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

        public class LoadingViewHolder extends RecyclerView.ViewHolder {

            private final ProgressBar progressBar;

            public LoadingViewHolder(View itemView) {
                super(itemView);
                progressBar = itemView.findViewById(R.id.idPBLoading);

            }
        }

    }


}