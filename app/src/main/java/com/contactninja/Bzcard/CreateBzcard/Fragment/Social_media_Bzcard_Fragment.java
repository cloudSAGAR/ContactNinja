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
                bzcard_model.getBzcardFieldsModel().getSocial_links().setFacebook_url(ev_fb.getText().toString().trim());
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

                bzcard_model.getBzcardFieldsModel().getSocial_links().setTwitter_url(ev_twitter.getText().toString().trim());
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
                bzcard_model.getBzcardFieldsModel().getSocial_links().setYoutube_url(ev_youtube.getText().toString().trim());
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
                bzcard_model.getBzcardFieldsModel().getSocial_links().setBreakout_url(ev_breakout.getText().toString().trim());
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

                bzcard_model.getBzcardFieldsModel().getSocial_links().setInstagram_url(ev_instagram.getText().toString().trim());
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
                bzcard_model.getBzcardFieldsModel().getSocial_links().setLinkedin_url(ev_linkedin.getText().toString().trim());
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
                bzcard_model.getBzcardFieldsModel().getSocial_links().setPinterest_url(ev_pintrest.getText().toString().trim());
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
                bzcard_model.getBzcardFieldsModel().getSocial_links().setVenmo_url(ev_venmo.getText().toString().trim());
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
                bzcard_model.getBzcardFieldsModel().getSocial_links().setSkype_url(ev_skypay.getText().toString().trim());
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
                bzcard_model.getBzcardFieldsModel().getSocial_links().setTiktok_url(ev_tiktok.getText().toString().trim());
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
                bzcard_model.getBzcardFieldsModel().getSocial_links().setSnapchat_url(ev_snap_chat.getText().toString().trim());
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
                bzcard_model.getBzcardFieldsModel().getSocial_links().setOther_1(ev_other_filed.getText().toString().trim());
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
                bzcard_model.getBzcardFieldsModel().getSocial_links().setOther_2(ev_other_filed1.getText().toString().trim());
                SessionManager.setBzcard(getActivity(),bzcard_model);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void setData()
    {

        if(bzcard_model.getCard_id()==1){

            ev_fb.setEnabled(false);
            ev_twitter.setEnabled(false);
            ev_youtube.setEnabled(false);
            ev_breakout.setEnabled(false);
            ev_instagram.setEnabled(false);
            ev_linkedin.setEnabled(false);
            ev_pintrest.setEnabled(false);
            ev_venmo.setEnabled(false);
            ev_skypay.setEnabled(false);
            ev_tiktok.setEnabled(false);
            ev_snap_chat.setEnabled(false);
            ev_other_filed.setEnabled(false);
            ev_other_filed1.setEnabled(false);

        }else {
            ev_fb.setEnabled(true);
            ev_twitter.setEnabled(true);
            ev_youtube.setEnabled(true);
            ev_breakout.setEnabled(true);
            ev_instagram.setEnabled(true);
            ev_linkedin.setEnabled(true);
            ev_pintrest.setEnabled(true);
            ev_venmo.setEnabled(true);
            ev_skypay.setEnabled(true);
            ev_tiktok.setEnabled(true);
            ev_snap_chat.setEnabled(true);
            ev_other_filed.setEnabled(true);
            ev_other_filed1.setEnabled(true);

        }


        ev_fb.setText(bzcard_model.getBzcardFieldsModel().getSocial_links().getFacebook_url().toString().trim());
        ev_twitter.setText(bzcard_model.getBzcardFieldsModel().getSocial_links().getTwitter_url().toString().trim());
        ev_youtube.setText(bzcard_model.getBzcardFieldsModel().getSocial_links().getYoutube_url().toString().trim());
        ev_breakout.setText(bzcard_model.getBzcardFieldsModel().getSocial_links().getBreakout_url().toString().trim());
        ev_instagram.setText(bzcard_model.getBzcardFieldsModel().getSocial_links().getInstagram_url().toString().trim());
        ev_linkedin.setText(bzcard_model.getBzcardFieldsModel().getSocial_links().getLinkedin_url().toString().trim());
        ev_pintrest.setText(bzcard_model.getBzcardFieldsModel().getSocial_links().getPinterest_url().toString().trim());
        ev_venmo.setText(bzcard_model.getBzcardFieldsModel().getSocial_links().getVenmo_url().toString().trim());
        ev_skypay.setText(bzcard_model.getBzcardFieldsModel().getSocial_links().getSkype_url().toString().trim());
        ev_tiktok.setText(bzcard_model.getBzcardFieldsModel().getSocial_links().getTiktok_url().toString().trim());
        ev_snap_chat.setText(bzcard_model.getBzcardFieldsModel().getSocial_links().getSnapchat_url().toString().trim());
        ev_other_filed.setText(bzcard_model.getBzcardFieldsModel().getSocial_links().getOther_1().toString().trim());
        ev_other_filed1.setText(bzcard_model.getBzcardFieldsModel().getSocial_links().getOther_2().toString().trim());
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