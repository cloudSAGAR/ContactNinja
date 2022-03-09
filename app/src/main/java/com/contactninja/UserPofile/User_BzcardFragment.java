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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.contactninja.Bzcard.CreateBzcard.Add_New_Bzcard_Activity;
import com.contactninja.Bzcard.Select_Bzcard_Activity;
import com.contactninja.MainActivity;
import com.contactninja.Model.BZcardListModel;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Setting.WebActivity;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.aws.AmazonUtil;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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

    LinearLayout demo_layout, layout_list;
    TextView tv_create, sub_txt, txt_card_name, button_edit, button_Preview;
    private long mLastClickTime = 0;

    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    List<BZcardListModel.Bizcard> bizcardList = new ArrayList<>();
    ViewPager viewPager;
    ImageView nextButton, backButton;
    CustomViewPagerAdapter customViewPagerAdapter;

    public User_BzcardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_bzcard, container, false);
        sessionManager = new SessionManager(getActivity());
        retrofitCalls = new RetrofitCalls(getActivity());
        loadingDialog = new LoadingDialog(getActivity());
        IntentUI(view);
       
        return view;
    }

    @Override
    public void onResume() {
        try {
            if (Global.isNetworkAvailable(getActivity(), MainActivity.mMainLayout)) {
                Data_list();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    private void IntentUI(View view) {
        demo_layout = view.findViewById(R.id.demo_layout);
        layout_list = view.findViewById(R.id.layout_list);
        nextButton = view.findViewById(R.id.bottom_forword);
        backButton = view.findViewById(R.id.bottom_backword);
        viewPager = view.findViewById(R.id.images_pager);
        demo_layout.setOnClickListener(this);
        tv_create = view.findViewById(R.id.tv_create);
        sub_txt = view.findViewById(R.id.sub_txt);
        txt_card_name = view.findViewById(R.id.txt_card_name);
        button_edit = view.findViewById(R.id.button_edit);
        button_Preview = view.findViewById(R.id.button_Preview);
        button_edit.setOnClickListener(this);
        button_Preview.setOnClickListener(this);
        tv_create.setText("Click to create\n" +
                " BZcard");
        sub_txt.setText("A digital business card that acts like a mini-website sharing your business information, work portfolio, customer testimonials, contact information, and so much more!\n");


        nextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
            }
        });

    }

    private int getItemofviewpager(int i) {
        return viewPager.getCurrentItem() + i;
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
                    layout_list.setVisibility(View.VISIBLE);
                    demo_layout.setVisibility(View.GONE);

                } else {
                    layout_list.setVisibility(View.GONE);
                    demo_layout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });


    }

    void Select_item(Integer id) throws JSONException {

        // loadingDialog.showLoadingDialog();

        SignResponseModel signResponseModel = SessionManager.getGetUserdata(getActivity());
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        paramObject.addProperty("id", id);
        obj.add("data", paramObject);
        retrofitCalls.BZcard_User_list(sessionManager, obj, loadingDialog, token, Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<List<BZcardListModel.Bizcard>>() {
                    }.getType();
                    List<BZcardListModel.Bizcard> bizcard = new Gson().fromJson(headerString, listType);
                    manu(bizcard.get(0));
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });


    }

    private void setImage() {
        customViewPagerAdapter = new CustomViewPagerAdapter(getActivity(), bizcardList);
        viewPager.setAdapter(customViewPagerAdapter);
        backButton.setVisibility(View.INVISIBLE);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    backButton.setVisibility(View.INVISIBLE);
                } else {
                    backButton.setVisibility(View.VISIBLE);
                }
                if (position < viewPager.getAdapter().getCount() - 1) {
                    nextButton.setVisibility(View.VISIBLE);
                } else {
                    nextButton.setVisibility(View.INVISIBLE);
                }
                if (viewPager.getCurrentItem() == 0) {
                    txt_card_name.setText(bizcardList.get(0).getBzcardFieldsModel().getCard_name());
                } else if (viewPager.getCurrentItem() == 1) {
                    txt_card_name.setText(bizcardList.get(1).getBzcardFieldsModel().getCard_name());
                } else if (viewPager.getCurrentItem() == 2) {
                    txt_card_name.setText(bizcardList.get(2).getBzcardFieldsModel().getCard_name());
                } else if (viewPager.getCurrentItem() == 3) {
                    txt_card_name.setText(bizcardList.get(3).getBzcardFieldsModel().getCard_name());
                } else if (viewPager.getCurrentItem() == 4) {
                    txt_card_name.setText(bizcardList.get(4).getBzcardFieldsModel().getCard_name());
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.demo_layout:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                startActivity(new Intent(getActivity(), Select_Bzcard_Activity.class));
                break;
            case R.id.button_edit:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if (viewPager.getCurrentItem() == 0) {
                    try {
                        if (Global.isNetworkAvailable(getActivity(), MainActivity.mMainLayout)) {
                            Select_item(bizcardList.get(0).getId());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (viewPager.getCurrentItem() == 1) {
                    try {
                        if (Global.isNetworkAvailable(getActivity(), MainActivity.mMainLayout)) {
                            Select_item(bizcardList.get(1).getId());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (viewPager.getCurrentItem() == 2) {
                    try {
                        if (Global.isNetworkAvailable(getActivity(), MainActivity.mMainLayout)) {
                            Select_item(bizcardList.get(2).getId());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (viewPager.getCurrentItem() == 3) {
                    try {
                        if (Global.isNetworkAvailable(getActivity(), MainActivity.mMainLayout)) {
                            Select_item(bizcardList.get(3).getId());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (viewPager.getCurrentItem() == 4) {
                    try {
                        if (Global.isNetworkAvailable(getActivity(), MainActivity.mMainLayout)) {
                            Select_item(bizcardList.get(4).getId());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.button_Preview:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Intent intent = new Intent(getActivity(), WebActivity.class);
                if (viewPager.getCurrentItem() == 0) {
                    intent.putExtra("WebUrl", Global.bzcard_priview + bizcardList.get(0).getId_encoded());
                } else if (viewPager.getCurrentItem() == 1) {
                    intent.putExtra("WebUrl", Global.bzcard_priview + bizcardList.get(1).getId_encoded());
                } else if (viewPager.getCurrentItem() == 2) {
                    intent.putExtra("WebUrl", Global.bzcard_priview + bizcardList.get(2).getId_encoded());
                } else if (viewPager.getCurrentItem() == 3) {
                    intent.putExtra("WebUrl", Global.bzcard_priview + bizcardList.get(3).getId_encoded());
                } else if (viewPager.getCurrentItem() == 4) {
                    intent.putExtra("WebUrl", Global.bzcard_priview + bizcardList.get(4).getId_encoded());
                }
                startActivity(intent);

                break;
        }
    }

    private void manu(BZcardListModel.Bizcard bizcard) {
        @SuppressLint("InflateParams") final View mView = getLayoutInflater().inflate(R.layout.bz_edit_dialog_item, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);

        TextView selected_Edit = bottomSheetDialog.findViewById(R.id.selected_Edit);
        TextView selected_Delete = bottomSheetDialog.findViewById(R.id.selected_Delete);
        TextView selected_Share = bottomSheetDialog.findViewById(R.id.selected_Share);
        TextView selected_Preview = bottomSheetDialog.findViewById(R.id.selected_Preview);


        selected_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();


                SessionManager.setBzcard(getActivity(), new BZcardListModel.Bizcard());
                bizcard.setEdit(true);
                SessionManager.setBzcard(getActivity(), bizcard);

                startActivity(new Intent(getActivity(), Add_New_Bzcard_Activity.class));
                bottomSheetDialog.dismiss();
            }
        });
        selected_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();


                try {
                    if (Global.isNetworkAvailable(getActivity(), MainActivity.mMainLayout)) {
                        Delete_card(bizcard.getId(), bizcard);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                bottomSheetDialog.dismiss();
            }
        });
        selected_Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, Global.bzcard_share + bizcard.getId_encoded());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

                bottomSheetDialog.dismiss();
            }
        });
        selected_Preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();


                Intent intent = new Intent(getActivity(), WebActivity.class);
                if (viewPager.getCurrentItem() == 0) {
                    intent.putExtra("WebUrl", Global.bzcard_priview + bizcard.getId_encoded());
                } else if (viewPager.getCurrentItem() == 1) {
                    intent.putExtra("WebUrl", Global.bzcard_priview + bizcard.getId_encoded());
                } else if (viewPager.getCurrentItem() == 2) {
                    intent.putExtra("WebUrl", Global.bzcard_priview + bizcard.getId_encoded());
                } else if (viewPager.getCurrentItem() == 3) {
                    intent.putExtra("WebUrl", Global.bzcard_priview + bizcard.getId_encoded());
                } else if (viewPager.getCurrentItem() == 4) {
                    intent.putExtra("WebUrl", Global.bzcard_priview + bizcard.getId_encoded());
                }
                startActivity(intent);

                bottomSheetDialog.dismiss();
            }
        });


        bottomSheetDialog.show();
    }

    void Delete_card(Integer id, BZcardListModel.Bizcard bizcard) throws JSONException {

        loadingDialog.showLoadingDialog();

        SignResponseModel signResponseModel = SessionManager.getGetUserdata(getActivity());
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        paramObject.addProperty("status", "D");
        paramObject.addProperty("id", id);
        obj.add("data", paramObject);
        retrofitCalls.BZcard_Add_Update(sessionManager, obj, loadingDialog, token, Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                if (response.body().getHttp_status() == 200) {


                    if (Global.IsNotNull(bizcard.getBzcardFieldsModel().getProfile_image())) {
                        if (!bizcard.getBzcardFieldsModel().getProfile_image().equals("")) {
                            try {
                                AmazonUtil.deleteS3Client(getActivity(), bizcard.getBzcardFieldsModel().getProfile_image());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                    if (Global.IsNotNull(bizcard.getBzcardFieldsModel().getCover_image())) {
                        if (!bizcard.getBzcardFieldsModel().getCover_image().equals("")) {
                            try {
                                AmazonUtil.deleteS3Client(getActivity(), bizcard.getBzcardFieldsModel().getCover_image());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (Global.IsNotNull(bizcard.getBzcardFieldsModel().getCompany_logo())) {
                        if (!bizcard.getBzcardFieldsModel().getCompany_logo().equals("")) {
                            try {
                                AmazonUtil.deleteS3Client(getActivity(), bizcard.getBzcardFieldsModel().getCompany_logo());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (Global.IsNotNull(bizcard.getBzcardFieldsModel().getBzMediaInformationList()) && bizcard.getBzcardFieldsModel().getBzMediaInformationList().size() != 0) {

                        for (int i = 0; i < bizcard.getBzcardFieldsModel().getBzMediaInformationList().size(); i++) {
                            switch (bizcard.getBzcardFieldsModel().getBzMediaInformationList().get(i).getMedia_type()) {
                                case "video": {
                                    if (!bizcard.getBzcardFieldsModel().getBzMediaInformationList().get(i).getMedia_thumbnail().equals("")) {
                                        AmazonUtil.deleteS3Client(getActivity(), bizcard.getBzcardFieldsModel().getBzMediaInformationList().get(i).getMedia_thumbnail());
                                    }
                                    break;
                                }
                                case "image": {
                                    if (!bizcard.getBzcardFieldsModel().getBzMediaInformationList().get(i).getMedia_url().equals("")) {
                                        AmazonUtil.deleteS3Client(getActivity(), bizcard.getBzcardFieldsModel().getBzMediaInformationList().get(i).getMedia_url());
                                    }

                                    break;
                                }
                                case "pdf": {
                                    if (!bizcard.getBzcardFieldsModel().getBzMediaInformationList().get(i).getMedia_url().equals("")) {
                                        AmazonUtil.deleteS3Client(getActivity(), bizcard.getBzcardFieldsModel().getBzMediaInformationList().get(i).getMedia_url());
                                    }
                                    break;
                                }
                            }
                        }
                    }
                    loadingDialog.cancelLoading();
                    try {
                        if (Global.isNetworkAvailable(getActivity(), MainActivity.mMainLayout)) {
                            Data_list();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });


    }

    public class CustomViewPagerAdapter extends PagerAdapter {

        List<BZcardListModel.Bizcard> bizcardList = new ArrayList<>();
        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomViewPagerAdapter(Context context, List<BZcardListModel.Bizcard> List) {
            bizcardList = List;
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return bizcardList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.bzcard_my_item_select, container, false);

            BZcardListModel.Bizcard bizcard = bizcardList.get(position);
            ImageView imageView = itemView.findViewById(R.id.iv_card);

            int resID = mContext.getResources().getIdentifier("my_" + bizcard.getCardName()
                    .replace(" ", "_").toLowerCase(), "drawable", mContext.getPackageName());
            if (resID != 0) {
                Glide.with(mContext.getApplicationContext()).load(resID).into(imageView);
            }
            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }

    }


}