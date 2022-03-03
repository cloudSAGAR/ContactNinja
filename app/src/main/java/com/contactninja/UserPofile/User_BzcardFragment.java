package com.contactninja.UserPofile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.contactninja.Bzcard.Select_Bzcard_Activity;
import com.contactninja.MainActivity;
import com.contactninja.Model.BZcardListModel;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Setting.EmailListActivity;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle")
public class User_BzcardFragment extends Fragment implements View.OnClickListener {

    LinearLayout demo_layout;
    TextView tv_create,sub_txt;
    private long mLastClickTime=0;

    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    List<BZcardListModel.Bizcard> bizcardList = new ArrayList<>();
    ViewPager2 viewPager2;
    ViewPageAdepter viewPageAdepter;

    public User_BzcardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_user_bzcard, container, false);
        sessionManager = new SessionManager(getActivity());
        retrofitCalls = new RetrofitCalls(getActivity());
        loadingDialog = new LoadingDialog(getActivity());
        IntentUI(view);
        try {
            if (Global.isNetworkAvailable(getActivity(), MainActivity.mMainLayout)) {
                Data_list();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    private void IntentUI(View view) {
        demo_layout=view.findViewById(R.id.demo_layout);
        viewPager2=view.findViewById(R.id.viewpager);
        demo_layout.setOnClickListener(this);
        tv_create=view.findViewById(R.id.tv_create);
        sub_txt=view.findViewById(R.id.sub_txt);
        tv_create.setText("Click to create\n" +
                " BZcard");
        sub_txt.setText("A digital business card that acts like a mini-website sharing your business information, work portfolio, customer testimonials, contact information, and so much more!\n");



    }
    void Data_list() throws JSONException {

        loadingDialog.showLoadingDialog();

        SignResponseModel signResponseModel = SessionManager.getGetUserdata(getActivity());
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        obj.add("data", paramObject);
        retrofitCalls.BZcard_User_list(sessionManager, obj, loadingDialog, token, Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    bizcardList.clear();
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<BZcardListModel>() {
                    }.getType();
                    BZcardListModel bZcardListModel = new Gson().fromJson(headerString, listType);

                    bizcardList = bZcardListModel.getBizcardList_user();


                    setImage();
                    viewPager2.setVisibility(View.VISIBLE);
                    demo_layout.setVisibility(View.GONE);

                }else {
                    viewPager2.setVisibility(View.GONE);
                    demo_layout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });


    }

    private void setImage() {
        viewPager2.setAdapter(new Select_Bzcard_Activity.ViewPageAdepter(getActivity(),  bizcardList));
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

              /*  if (viewPager2.getCurrentItem() == 0) {
                    txt_footer.setText(getResources().getString(R.string.bz_footer));
                } else if (viewPager2.getCurrentItem() == 1) {
                    txt_footer.setText(getResources().getString(R.string.bz_footer2));
                } else if (viewPager2.getCurrentItem() == 2) {
                    txt_footer.setText(getResources().getString(R.string.bz_footer));
                } else if (viewPager2.getCurrentItem() == 3) {
                    txt_footer.setText(getResources().getString(R.string.bz_footer2));
                } else if (viewPager2.getCurrentItem() == 4) {
                    txt_footer.setText(getResources().getString(R.string.bz_footer));
                } else if (viewPager2.getCurrentItem() == 5) {
                    txt_footer.setText(getResources().getString(R.string.bz_footer2));
                }*/


            }
        });
        viewPager2.setPageTransformer(compositePagerTransformer);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.demo_layout:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                startActivity(new Intent(getActivity(), Select_Bzcard_Activity.class));
                break;
        }
    }
    public static class ViewPageAdepter extends RecyclerView.Adapter<ViewPageAdepter.viewholder> {

        public Context mCtx;
        List<BZcardListModel.Bizcard> bizcardList = new ArrayList<>();

        public ViewPageAdepter(Context applicationContext, List<BZcardListModel.Bizcard> bizcardList) {
            this.mCtx = applicationContext;
            this.bizcardList = bizcardList;
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
            BZcardListModel.Bizcard bizcard = bizcardList.get(position);

            int resID = mCtx.getResources().getIdentifier(bizcard.getCardName()
                    .replace(" ", "_").toLowerCase(), "drawable", mCtx.getPackageName());
            if (resID != 0) {
                Glide.with(mCtx.getApplicationContext()).load(resID).into(holder.iv_card);
            }

        }

        @Override
        public int getItemCount() {
            return bizcardList.size();
        }

        public static class viewholder extends RecyclerView.ViewHolder {
            ImageView iv_card;

            public viewholder(View view) {
                super(view);
                iv_card = view.findViewById(R.id.iv_card);

            }
        }
    }
}