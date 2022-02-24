package com.contactninja.Bzcard;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.contactninja.Model.Plandetail;
import com.contactninja.R;
import com.contactninja.Setting.CurrentPlanActivity;
import com.contactninja.Setting.WebActivity;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;

import java.util.List;
@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Select_Bzcard_Activity extends AppCompatActivity  implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener {
    ImageView iv_back;
    private BroadcastReceiver mNetworkReceiver;
    RelativeLayout mMainLayout;
    ViewPager2 viewPager2;
    TextView txt_footer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bzcard);
        mNetworkReceiver = new ConnectivityReceiver();
        initUI();
        setImage();
    }

    private void setImage() {
        viewPager2.setAdapter(new ViewPageAdepter(getApplicationContext(),txt_footer));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePagerTransformer = new CompositePageTransformer();
        compositePagerTransformer.addTransformer(new MarginPageTransformer(40));
        compositePagerTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        viewPager2.setPageTransformer(compositePagerTransformer);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initUI() {
        mMainLayout = findViewById(R.id.mMainLayout);
        iv_back=findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
        viewPager2 = findViewById(R.id.viewpager);
        txt_footer = findViewById(R.id.txt_footer);
    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Select_Bzcard_Activity.this, mMainLayout);
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
        switch (v.getId()){
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    public static class ViewPageAdepter extends RecyclerView.Adapter<ViewPageAdepter.viewholder> {

        public Context mCtx;
        TextView txt_footer;

        public ViewPageAdepter(Context applicationContext,TextView txt_footer) {
            this.mCtx = applicationContext;
            this.txt_footer = txt_footer;
        }

        @NonNull
        @Override
        public ViewPageAdepter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.bzcard_fix_item_select, parent, false);
            return new ViewPageAdepter.viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewPageAdepter.viewholder holder, int position) {
            if(position==1||position==3||position==5){
                holder.iv_card.setImageDrawable(mCtx.getDrawable(R.drawable.card1));
                txt_footer.setText(mCtx.getResources().getString(R.string.bz_footer));
            }else {
                holder.iv_card.setImageDrawable(mCtx.getDrawable(R.drawable.card2));
                txt_footer.setText(mCtx.getResources().getString(R.string.bz_footer2));
            }
        }

        @Override
        public int getItemCount() {
            return 6;
        }

        public static class viewholder extends RecyclerView.ViewHolder {
            ImageView iv_card;

            public viewholder(View view) {
                super(view);
                iv_card=view.findViewById(R.id.iv_card);

            }
        }
    }
}