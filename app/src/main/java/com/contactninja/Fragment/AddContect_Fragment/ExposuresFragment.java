package com.contactninja.Fragment.AddContect_Fragment;

import static com.contactninja.Utils.PaginationListener.PAGE_START;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contactninja.Campaign.Add_Camp_Tab_Select_Activity;
import com.contactninja.Campaign.Campaign_Preview;
import com.contactninja.Contect.Contact;
import com.contactninja.Model.CampaignTask;
import com.contactninja.Model.CampaignTask_overview;
import com.contactninja.Model.CompanyModel;
import com.contactninja.Model.ContectListData;
import com.contactninja.Model.ExposuresModel;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class ExposuresFragment extends Fragment {
    private long mLastClickTime = 0;
    LoadingDialog loadingDialog;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LinearLayout no_data_view;
    RecyclerView rv_exposure;
    RecyclerView.LayoutManager layoutManager;
    Exposures_Adapter exposures_adapter;
    public ExposuresFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_exposures, container, false);
        IntentUI(view);
        exposures_adapter=new Exposures_Adapter(getActivity());
        rv_exposure.setAdapter(exposures_adapter);

        return view;
    }

    @Override
    public void onResume() {
        try {
            ExposuresList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    private void IntentUI(View view) {
        sessionManager = new SessionManager(getActivity());
        loadingDialog = new LoadingDialog(getActivity());
        retrofitCalls = new RetrofitCalls(getActivity());
        no_data_view=view.findViewById(R.id.no_data_view);
        rv_exposure=view.findViewById(R.id.rv_exposure);
        layoutManager=new LinearLayoutManager(getActivity());
        rv_exposure.setLayoutManager(layoutManager);

    }




    private void ExposuresList() throws JSONException {
        loadingDialog.showLoadingDialog();
        ContectListData.Contact Contect_data = SessionManager.getOneCotect_deatil(getActivity());

        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());

        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", user_data.getUser().getId());
        paramObject.addProperty("prospect_id",Contect_data.getId());
        obj.add("data", paramObject);
        retrofitCalls.ExposuresList(sessionManager, obj, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                    if (response.body().getHttp_status().equals(200)) {
                        Gson gson = new Gson();
                        String headerString = gson.toJson(response.body().getData());
                        Type listType = new TypeToken<ExposuresModel>() {
                        }.getType();
                        ExposuresModel data = new Gson().fromJson(headerString, listType);
                        exposures_adapter.addAll(data.getMailActivity());
                        no_data_view.setVisibility(View.GONE);
                    }
                    else {
                        no_data_view.setVisibility(View.VISIBLE);
                    }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                no_data_view.setVisibility(View.VISIBLE);
            }
        });
    }


    public class Exposures_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int LOADING = 0;
        private static final int ITEM = 1;
        private final Context context;
        private List<ExposuresModel.MailActivity> movieList;
        private boolean isLoadingAdded = false;

        public Exposures_Adapter(Context context) {
            this.context = context;
            movieList = new LinkedList<>();
        }


        public void setMovieList(List<ExposuresModel.MailActivity> movieList) {
            this.movieList = movieList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            switch (viewType) {
                case ITEM:
                    View viewItem = inflater.inflate(R.layout.exposures_item, parent, false);
                    viewHolder = new Exposures_Adapter.MovieViewHolder(viewItem);
                    break;
                case LOADING:
                    View viewLoading = inflater.inflate(R.layout.item_loading, parent, false);
                    viewHolder = new Exposures_Adapter.LoadingViewHolder(viewLoading);
                    break;
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            ExposuresModel.MailActivity movieList_data = movieList.get(position);
            switch (getItemViewType(position)) {
                case ITEM:
                    Exposures_Adapter.MovieViewHolder movieViewHolder = (Exposures_Adapter.MovieViewHolder) holder;
                    if (movieList.size()==1)
                    {
                        movieViewHolder.line_one.setVisibility(View.INVISIBLE);
                        movieViewHolder.cam_line.setVisibility(View.INVISIBLE);
                    }
                    else if (position == 0 || position==-1) {
                        movieViewHolder.line_one.setVisibility(View.INVISIBLE);
                    }
                    else if (position == movieList.size()-1) {
                        movieViewHolder.cam_line.setVisibility(View.INVISIBLE);
                    }
                    else {
                        movieViewHolder.line_one.setVisibility(View.VISIBLE);
                    }

                    if (movieList_data.getStatus().equals("SENT"))
                    {
                        if (movieList_data.getMailModule().equals("USERMAIL"))
                        {
                            movieViewHolder.tv_title.setText("You sent a Email to "+movieList_data.getContactFirstname());

                        }
                        else {
                            movieViewHolder.tv_title.setText("You sent a SMS to "+movieList_data.getContactFirstname());

                        }

                    }
                    else if (movieList_data.getStatus().equals("DELIVERED"))
                    {
                        if (movieList_data.getMailModule().equals("USERMAIL"))
                        {
                            movieViewHolder.tv_title.setText("Email was Delivered to "+movieList_data.getContactFirstname());

                        }
                        else {
                            movieViewHolder.tv_title.setText("SMS was Delivered to "+movieList_data.getContactFirstname());

                        }
                    }
                    else if (movieList_data.getStatus().equals("FAILED"))
                    {
                        if (movieList_data.getMailModule().equals("USERMAIL"))
                        {
                            movieViewHolder.tv_title.setText("Send Email Failed to "+movieList_data.getContactFirstname());
                        }
                        else {
                            movieViewHolder.tv_title.setText("Send SMS Failed to "+movieList_data.getContactFirstname());
                        }
                    }
                    if (Global.IsNotNull(movieList.get(position).getCreatedAt())){
                        movieViewHolder.tv_date.setText(Global.formateChange(movieList.get(position).getCreatedAt()));
                    }

                    break;

                case LOADING:
                    Exposures_Adapter.LoadingViewHolder loadingViewHolder = (Exposures_Adapter.LoadingViewHolder) holder;
                    loadingViewHolder.progressBar.setVisibility(View.VISIBLE);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return movieList == null ? 0 : movieList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return (position == movieList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
        }

        public void addLoadingFooter() {
            isLoadingAdded = true;
        }

        public void remove_all() {
            movieList.clear();
            notifyDataSetChanged();
        }

        public void removeLoadingFooter() {
            isLoadingAdded = false;

            int position = movieList.size() - 1;
            ExposuresModel.MailActivity result = getItem(position);

            if (result != null) {
                movieList.remove(position);
                notifyItemRemoved(position);
            }
        }

        public void add(ExposuresModel.MailActivity movie) {
            movieList.add(movie);
            notifyItemInserted(movieList.size() - 1);
        }

        public void addAll(List<ExposuresModel.MailActivity> moveResults) {
            for (ExposuresModel.MailActivity result : moveResults) {
                add(result);
            }
        }

        public ExposuresModel.MailActivity getItem(int position) {
            return movieList.get(position);
        }


        public class MovieViewHolder extends RecyclerView.ViewHolder {
            TextView tv_title,tv_date,tv_item_num;
            View line_one,cam_line;
            public MovieViewHolder(View itemView) {
                super(itemView);
                tv_title=itemView.findViewById(R.id.tv_title);
                tv_date=itemView.findViewById(R.id.tv_date);
                line_one=itemView.findViewById(R.id.line_one);
                cam_line=itemView.findViewById(R.id.cam_line);
                tv_item_num=itemView.findViewById(R.id.tv_item_num);
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