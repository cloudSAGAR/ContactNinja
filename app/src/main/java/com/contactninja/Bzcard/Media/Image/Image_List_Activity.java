package com.contactninja.Bzcard.Media.Image;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.contactninja.Bzcard.Media.SwipeHelper;
import com.contactninja.Interface.Bz_MediaClick;
import com.contactninja.Model.Bzcard_Fields_Model;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.SessionManager;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
@SuppressLint("SimpleDateFormat,StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Image_List_Activity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener,
        View.OnClickListener, Bz_MediaClick {
    SessionManager sessionManager;
    private BroadcastReceiver mNetworkReceiver;
    LinearLayout mMainLayout,layout_media;
    ImageView iv_back,iv_media_title;
    RecyclerView rv_imageList;
    ImagelistAdepter imagelistAdepter;
    static Bzcard_Fields_Model model;
    static List<Bzcard_Fields_Model.BZ_media_information> bzMediaInformationList=new ArrayList<>();
    List<Bzcard_Fields_Model.BZ_media_information> bzMedia_image_List =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        mNetworkReceiver = new ConnectivityReceiver();
        sessionManager=new SessionManager(this);
        initUI();
        setList();
    }
    private void setList() {
        model= SessionManager.getBzcard(Image_List_Activity.this);
        bzMediaInformationList=model.getBzMediaInformationList();
        for(int i=0;i<bzMediaInformationList.size();i++){
            if(bzMediaInformationList.get(i).getMedia_type().equals("image")){
                bzMedia_image_List.add(bzMediaInformationList.get(i));
            }
        }

        rv_imageList.setLayoutManager(new LinearLayoutManager(Image_List_Activity.this, LinearLayoutManager.VERTICAL, false));
        imagelistAdepter =new ImagelistAdepter(Image_List_Activity.this, bzMedia_image_List,this);
        rv_imageList.setAdapter(imagelistAdepter);


        SwipeHelper swipeHelper = new SwipeHelper(Image_List_Activity.this) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {

                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Edit",
                        0,
                        Color.parseColor("#5495EC"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                final Bzcard_Fields_Model.BZ_media_information item = imagelistAdepter.getData().get(pos);
                                Intent intent=new Intent(getApplicationContext(), Add_image_Activity.class);
                                intent.putExtra("MyClass", item);
                                startActivity(intent);
                            }
                        }
                ));
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Delete",
                        0,
                        Color.parseColor("#FF3C30"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(final int pos) {
                                final Bzcard_Fields_Model.BZ_media_information item = imagelistAdepter.getData().get(pos);
                                imagelistAdepter.removeItem(pos,item);

                                Toast.makeText(Image_List_Activity.this, "Item was removed from the list.", Toast.LENGTH_LONG).show();
                            }
                        }
                ));

            }
        };
        swipeHelper.attachToRecyclerView(rv_imageList);

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initUI() {
        layout_media = findViewById(R.id.layout_media);
        mMainLayout = findViewById(R.id.mMainLayout);
        rv_imageList = findViewById(R.id.rv_imageList);
        iv_media_title = findViewById(R.id.iv_media_title);

        iv_media_title.setBackgroundResource(R.drawable.ic_select_off);


        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
        layout_media.setOnClickListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Image_List_Activity.this, mMainLayout);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.layout_media:
                startActivity(new Intent(getApplicationContext(), Add_image_Activity.class));
                break;

        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void OnVideoClick(Bzcard_Fields_Model.BZ_media_information information) {
        Intent intent=new Intent(getApplicationContext(), Add_image_Activity.class);
        intent.putExtra("MyClass", information);
        startActivity(intent);
    }


    public static class ImagelistAdepter extends RecyclerView.Adapter<ImagelistAdepter.viewholder> {

        public Context mCtx;
        List<Bzcard_Fields_Model.BZ_media_information> bzMedia_image_List;
        Bz_MediaClick videoClick;
        public ImagelistAdepter(Context applicationContext, List<Bzcard_Fields_Model.BZ_media_information> bzMedia_image_List,
                                Bz_MediaClick videoClick) {
            this.mCtx = applicationContext;
            this.bzMedia_image_List = bzMedia_image_List;
            this.videoClick = videoClick;
        }

        @NonNull
        @Override
        public ImagelistAdepter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_video_bzcard, parent, false);
            return new ImagelistAdepter.viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ImagelistAdepter.viewholder holder, int position) {
            Bzcard_Fields_Model.BZ_media_information information=bzMedia_image_List.get(position);

            if(information.getIs_featured()==1){
                holder.iv_Featured.setVisibility(View.VISIBLE);
            }else {
                holder.iv_Featured.setVisibility(View.GONE);
            }
            Glide.with(mCtx)
                    .load(information.getMedia_filePath())
                    .into(holder.iv_video);

            holder.txt_title.setText(information.getMedia_title());
            holder.txt_dicription.setText(information.getMedia_description());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoClick.OnVideoClick(information);
                }
            });
        }
        public void removeItem(int position, Bzcard_Fields_Model.BZ_media_information item) {
            bzMedia_image_List.remove(position);

            for (int i = 0; i < bzMediaInformationList.size(); i++) {
                if (bzMediaInformationList.get(i).getId().equals(item.getId())) {
                    bzMediaInformationList.remove(i);
                    model.setBzMediaInformationList(bzMediaInformationList);
                    SessionManager.setBzcard(mCtx, model);
                    break;
                }
            }
            notifyItemRemoved(position);
        }

        public List<Bzcard_Fields_Model.BZ_media_information> getData() {
            return bzMedia_image_List;
        }

        @Override
        public int getItemCount() {
            return bzMedia_image_List.size();
        }

        public static class viewholder extends RecyclerView.ViewHolder {
            RoundedImageView iv_video;
            TextView txt_title,txt_dicription;
            LinearLayout layout_item;
            ImageView iv_Featured;

            public viewholder(View view) {
                super(view);
                iv_video = view.findViewById(R.id.iv_video);
                txt_title = view.findViewById(R.id.txt_title);
                txt_dicription = view.findViewById(R.id.txt_dicription);
                iv_Featured = view.findViewById(R.id.iv_Featured);
                layout_item = view.findViewById(R.id.layout_item);
            }
        }
    }
}