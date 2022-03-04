package com.contactninja.Bzcard.CreateBzcard.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.contactninja.Model.BZcardListModel;
import com.contactninja.Model.Bzcard_Fields_Model;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.RetrofitCalls;

public class Social_media_Bzcard_Fragment extends Fragment {
    LoadingDialog loadingDialog;
    RetrofitCalls retrofitCalls;
    SessionManager sessionManager;
    BZcardListModel.Bizcard bzcard_model;
    EditText ev_fb,ev_twitter,ev_breakout,ev_youtube,
            ev_instagram,ev_linkedin,ev_pintrest,ev_venmo,
            ev_skypay,ev_tiktok,ev_snap_chat,ev_other_filed,
            ev_other_filed1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_social_media__bzcard_, container, false);
        IntentUI(view);
        bzcard_model=SessionManager.getBzcard(getActivity());
        sessionManager = new SessionManager(getActivity());
        loadingDialog = new LoadingDialog(getActivity());
        retrofitCalls = new RetrofitCalls(getActivity());
        setData();
        addtextchnageListner();
        return view;
    }

    private void addtextchnageListner() {
        ev_fb.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bzcard_model.getBzcardFieldsModel().setFb(ev_fb.getText().toString().trim());
                SessionManager.setBzcard(getActivity(),bzcard_model);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ev_twitter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                bzcard_model.getBzcardFieldsModel().setTwitter(ev_twitter.getText().toString().trim());
                SessionManager.setBzcard(getActivity(),bzcard_model);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ev_youtube.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bzcard_model.getBzcardFieldsModel().setYoutube(ev_youtube.getText().toString().trim());
                SessionManager.setBzcard(getActivity(),bzcard_model);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ev_breakout.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bzcard_model.getBzcardFieldsModel().setBreakout(ev_breakout.getText().toString().trim());
                SessionManager.setBzcard(getActivity(),bzcard_model);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ev_instagram.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                bzcard_model.getBzcardFieldsModel().setInstagram(ev_instagram.getText().toString().trim());
                SessionManager.setBzcard(getActivity(),bzcard_model);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ev_linkedin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bzcard_model.getBzcardFieldsModel().setLinkedin(ev_linkedin.getText().toString().trim());
                SessionManager.setBzcard(getActivity(),bzcard_model);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ev_pintrest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bzcard_model.getBzcardFieldsModel().setPintrest(ev_pintrest.getText().toString().trim());
                SessionManager.setBzcard(getActivity(),bzcard_model);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ev_venmo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bzcard_model.getBzcardFieldsModel().setVenmo(ev_venmo.getText().toString().trim());
                SessionManager.setBzcard(getActivity(),bzcard_model);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ev_skypay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bzcard_model.getBzcardFieldsModel().setSkypay(ev_skypay.getText().toString().trim());
                SessionManager.setBzcard(getActivity(),bzcard_model);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ev_tiktok.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bzcard_model.getBzcardFieldsModel().setTiktok(ev_tiktok.getText().toString().trim());
                SessionManager.setBzcard(getActivity(),bzcard_model);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ev_snap_chat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bzcard_model.getBzcardFieldsModel().setSnapchat(ev_snap_chat.getText().toString().trim());
                SessionManager.setBzcard(getActivity(),bzcard_model);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ev_other_filed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bzcard_model.getBzcardFieldsModel().setOther_filed(ev_other_filed.getText().toString().trim());
                SessionManager.setBzcard(getActivity(),bzcard_model);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ev_other_filed1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bzcard_model.getBzcardFieldsModel().setOther_filed1(ev_other_filed1.getText().toString().trim());
                SessionManager.setBzcard(getActivity(),bzcard_model);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void setData()
    {
        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        ev_fb.setText(user_data.getUser().getUserprofile().getFacebook_link());
        bzcard_model.getBzcardFieldsModel().setFb(user_data.getUser().getUserprofile().getFacebook_link());
        ev_breakout.setText(user_data.getUser().getUserprofile().getBreakout_link());
        bzcard_model.getBzcardFieldsModel().setBreakout(user_data.getUser().getUserprofile().getBreakout_link());
        ev_linkedin.setText(user_data.getUser().getUserprofile().getLinkedin_link());
        bzcard_model.getBzcardFieldsModel().setLinkedin(user_data.getUser().getUserprofile().getLinkedin_link());
        ev_twitter.setText(user_data.getUser().getUserprofile().getTwitter_link());
        bzcard_model.getBzcardFieldsModel().setTwitter(user_data.getUser().getUserprofile().getTwitter_link());
        SessionManager.setBzcard(getActivity(),bzcard_model);
    }

    private void IntentUI(View view) {
        ev_fb=view.findViewById(R.id.ev_fb);
        ev_twitter=view.findViewById(R.id.ev_twitter);
        ev_youtube=view.findViewById(R.id.ev_youtube);
        ev_breakout=view.findViewById(R.id.ev_breakout);
        ev_instagram=view.findViewById(R.id.ev_instagram);
        ev_linkedin=view.findViewById(R.id.ev_linkedin);
        ev_pintrest=view.findViewById(R.id.ev_pintrest);
        ev_venmo=view.findViewById(R.id.ev_venmo);
        ev_skypay=view.findViewById(R.id.ev_skypay);
        ev_tiktok=view.findViewById(R.id.ev_tiktok);
        ev_snap_chat=view.findViewById(R.id.ev_snap_chat);
        ev_other_filed=view.findViewById(R.id.ev_other_filed);
        ev_other_filed1=view.findViewById(R.id.ev_other_filed1);
    }
}