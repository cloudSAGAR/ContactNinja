package com.contactninja.Bzcard.Media.Video;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.contactninja.Bzcard.Media.Select_Media_Activity;
import com.contactninja.Bzcard.Select_Bzcard_Activity;
import com.contactninja.Interface.Bz_VideoClick;
import com.contactninja.Model.Bzcard_Model;
import com.contactninja.R;
import com.contactninja.Setting.EmailListActivity;
import com.contactninja.Setting.WebActivity;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.SessionManager;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import ru.rambler.libs.swipe_layout.SwipeLayout;

public class Video_List_Activity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener,
        View.OnClickListener, Bz_VideoClick {
    private BroadcastReceiver mNetworkReceiver;
    LinearLayout mMainLayout,layout_media;
    ImageView iv_back,iv_media_title;
    RecyclerView rv_videoList;
    VideolistAdepter videolistAdepter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        mNetworkReceiver = new ConnectivityReceiver();
        initUI();
        setList();
    }

    private void setList() {
        Bzcard_Model model= SessionManager.getBzcard(Video_List_Activity.this);
        List<Bzcard_Model.BZ_media_information> bzMediaInformationList=model.getBzMediaInformationList();


        rv_videoList.setLayoutManager(new LinearLayoutManager(Video_List_Activity.this, LinearLayoutManager.VERTICAL, false));
        videolistAdepter=new VideolistAdepter(Video_List_Activity.this,bzMediaInformationList,this);
        rv_videoList.setAdapter(videolistAdepter);

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initUI() {
        layout_media = findViewById(R.id.layout_media);
        mMainLayout = findViewById(R.id.mMainLayout);
        rv_videoList = findViewById(R.id.rv_videoList);
        iv_media_title = findViewById(R.id.iv_media_title);

        iv_media_title.setBackgroundResource(R.drawable.ic_select_off);


        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
        layout_media.setOnClickListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Video_List_Activity.this, mMainLayout);
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
                startActivity(new Intent(getApplicationContext(), Video_LinkAdd_Activity.class));
                break;

        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void OnVideoClick(Bzcard_Model.BZ_media_information information) {
        Intent intent=new Intent(getApplicationContext(),Add_Video_Activity.class);
        intent.putExtra("MyClass", information);
        startActivity(intent);
    }


    public static class VideolistAdepter extends RecyclerView.Adapter<VideolistAdepter.viewholder> {

        public Context mCtx;
        List<Bzcard_Model.BZ_media_information> bzMediaInformationList;
        Bz_VideoClick videoClick;
        public VideolistAdepter(Context applicationContext, List<Bzcard_Model.BZ_media_information> bzMediaInformationList,
                                Bz_VideoClick videoClick) {
            this.mCtx = applicationContext;
            this.bzMediaInformationList = bzMediaInformationList;
            this.videoClick = videoClick;
        }

        @NonNull
        @Override
        public VideolistAdepter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_video_bzcard, parent, false);
            return new VideolistAdepter.viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull VideolistAdepter.viewholder holder, int position) {
            Bzcard_Model.BZ_media_information information=bzMediaInformationList.get(position);

            if(information.getIs_featured()==1){
                holder.iv_Featured.setVisibility(View.VISIBLE);
            }else {
                holder.iv_Featured.setVisibility(View.GONE);
            }
            Glide.with(mCtx)
                    .load(Global.getYoutubeThumbnailUrlFromVideoUrl(information.getMedia_url()))
                    .into(holder.iv_video);
            holder.txt_title.setText(information.getMedia_title());
            holder.txt_dicription.setText(information.getMedia_description());


            holder.swipe_layout.setLeftSwipeEnabled(true);
            holder.swipe_layout.setRightSwipeEnabled(true);
            holder.swipe_layout.setOnSwipeListener(new SwipeLayout.OnSwipeListener() {
                @Override
                public void onBeginSwipe(SwipeLayout swipeLayout, boolean moveToRight) {
                    // Log.e("Swipe Call ","MOveto right");
                    if (holder.layout_swap.getVisibility() == View.GONE) {
                        holder.layout_swap.setVisibility(View.VISIBLE);
                    } else {
                        holder.layout_swap.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onSwipeClampReached(SwipeLayout swipeLayout, boolean moveToRight) {
                    Log.e("Swipe Call ", "MOveto right1");

                }

                @Override
                public void onLeftStickyEdge(SwipeLayout swipeLayout, boolean moveToRight) {
                    Log.e("Swipe Call ", "Left");
                }

                @Override
                public void onRightStickyEdge(SwipeLayout swipeLayout, boolean moveToRight) {
                    Log.e("Swipe Call ", "Right");

                }
            });

            holder.swipe_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoClick.OnVideoClick(information);
                }
            });
        }

        @Override
        public int getItemCount() {
            return bzMediaInformationList.size();
        }

        public static class viewholder extends RecyclerView.ViewHolder {
            RoundedImageView iv_video;
            TextView txt_title,txt_dicription;
            SwipeLayout swipe_layout;
            LinearLayout layout_swap,layout_item;
            ImageView iv_Featured;

            public viewholder(View view) {
                super(view);
                iv_video = view.findViewById(R.id.iv_video);
                txt_title = view.findViewById(R.id.txt_title);
                txt_dicription = view.findViewById(R.id.txt_dicription);
                swipe_layout = view.findViewById(R.id.swipe_layout);
                layout_swap = view.findViewById(R.id.layout_swap);
                iv_Featured = view.findViewById(R.id.iv_Featured);
                layout_item = view.findViewById(R.id.layout_item);
            }
        }
    }
}