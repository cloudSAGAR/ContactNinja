package com.contactninja.UserPofile;

import static com.contactninja.Utils.PaginationListener.PAGE_START;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contactninja.Bzcard.Media.SwipeHelper;
import com.contactninja.MainActivity;
import com.contactninja.Model.AddcontectModel;
import com.contactninja.Model.CompanyModel;
import com.contactninja.Model.Contactdetail;
import com.contactninja.Model.ContectListData;
import com.contactninja.Model.TimezoneModel;
import com.contactninja.Model.UserData.ContactDetail;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.Model.UserData.User;
import com.contactninja.Model.UserData.Userprofile;
import com.contactninja.Model.WorkTypeData;
import com.contactninja.R;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.PaginationListener;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;
import retrofit2.Response;
import ru.rambler.libs.swipe_layout.SwipeLayout;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class User_InformationFragment extends Fragment implements View.OnClickListener {
    private long mLastClickTime = 0;
    List<TimezoneModel> timezoon;
    List<TimezoneModel> timezoneModels = new ArrayList<>();
    BottomSheetDialog bottomSheetDialog_time, bottomSheetDialog_company;
    EditText ev_address, ev_city, ev_zip, ev_zoom, ev_note,
            ev_company_url, ev_state, ev_job, ev_bob, ev_fb, ev_twitter, ev_breakout,
            ev_linkedin, ev_company, edt_email;
    LinearLayout select_state, add_mobile_Number,
            layout_Add_phone, layout_Add_email, layout_mobile, fb_layout;
    TextView tv_phone, tv_more_field, tv_company_url, tv_job,
            zone_txt, tv_add_social;
    ImageView pulse_icon, pulse_icon1, img_fb, img_twitter, img_linkdin, img_breakout;
    String Name = "", job_titel = "", time_zone_id;
    SessionManager sessionManager;
    AddcontectModel addcontectModel;
    BottomSheetDialog bottomSheetDialog;
    LinearLayout city_layout, zoom_layout, note_layout, company_url_layout, job_layout,
            layout_time_zone, select_label_zone, layout_bod, twitter_layout, breakout_layout,
            linkedin_layout, state_layout, time_layout, media_layout, media_link, layout_referenceCode;
    String show = "0";
    String organization_id = "", team_id = "", referenceCode = "";
    int contect_id = 0;
    PhoneAdapter phoneAdapter;
    EmailAdapter emailAdapter;
    RecyclerView rv_phone, rv_email;
    List<Contactdetail> contactdetails = new ArrayList<>();
    List<Contactdetail> phonedetails_list = new ArrayList<>();
    List<Contactdetail> emaildetails_list = new ArrayList<>();
    LoadingDialog loadingDialog;
    RetrofitCalls retrofitCalls;
    LinearLayout mMainLayout, company_layout, other_company_layout;
    boolean edit = false;
    EditText ev_othre_company;
    ImageView iv_down;
    EditText edt_mobile_no;
    LinearLayout layout_country_piker;
    ImageView copany_down_arrow;
    private int mYear, mMonth, mDay, mHour, mMinute;


    CompanyAdapter companyAdapter;
    List<CompanyModel.Company> companyList = new ArrayList<>();
    int perPage = 20;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;

    public User_InformationFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_information, container, false);
        IntentUI(view);
        //Log.e("On Click Method ","3");
        addcontectModel = new AddcontectModel();
        sessionManager = new SessionManager(getActivity());
        loadingDialog = new LoadingDialog(getActivity());
        retrofitCalls = new RetrofitCalls(getActivity());
        companyAdapter = new CompanyAdapter(getActivity(), new ArrayList<>());

        return view;
    }

    @Override
    public void onStart() {
        setdata();

        layout_bod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                OpenBob();
            }
        });
        ev_bob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                OpenBob();
            }
        });
        tv_more_field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                media_layout.setVisibility(View.VISIBLE);

                company_url_layout.setVisibility(View.VISIBLE);
                job_layout.setVisibility(View.VISIBLE);
                zoom_layout.setVisibility(View.VISIBLE);
                city_layout.setVisibility(View.VISIBLE);
                state_layout.setVisibility(View.VISIBLE);
                time_layout.setVisibility(View.VISIBLE);
                layout_bod.setVisibility(View.VISIBLE);
                note_layout.setVisibility(View.GONE);
                tv_more_field.setVisibility(View.GONE);
            }

        });

        company_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                showBottomSheetDialog_For_Company();
            }
        });
        super.onStart();
    }

    private void setdata() {

        List<ContectListData.Contact> test_list = new ArrayList<>();
        test_list.add(SessionManager.getOneCotect_deatil(getActivity()));
        // Log.e("Size is", String.valueOf(test_list));
        String flag = SessionManager.getContect_flag(getActivity());


        //Set DATA

        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());


        User user_data_model = user_data.getUser();
        referenceCode = String.valueOf(user_data_model.getReferenceCode());

        ContectListData.Contact set_contact = new ContectListData.Contact();
        set_contact.setFirstname(user_data_model.getFirstName());
        set_contact.setLastname(user_data_model.getLastName());
        List<ContectListData.Contact.ContactDetail> contactDetails_list = new ArrayList<>();

        List<Contactdetail> contactdetails = new ArrayList<>();


        if (user_data.getUser().getUserprofile().getContactDetails() != null) {
            for (int i = 0; i < user_data.getUser().getUserprofile().getContactDetails().size(); i++) {

                ContectListData.Contact.ContactDetail contactDetail_model2 = new ContectListData.Contact.ContactDetail();
                contactDetail_model2.setEmailNumber(user_data.getUser().getUserprofile().getContactDetails().get(i).getEmailNumber());
                contactDetail_model2.setLabel(user_data.getUser().getUserprofile().getContactDetails().get(i).getLabel());
                /*contactDetail_model.setEmailNumber(user_data_model.getEmail());*/
                // contactDetail_model2.setContactId(user_data_model.getId());
                contactDetail_model2.setType(user_data.getUser().getUserprofile().getContactDetails().get(i).getType());
                contactDetail_model2.setIsDefault(0);
                contactDetails_list.add(i, contactDetail_model2);
                Contactdetail contactdetail2 = new Contactdetail();
                contactdetail2.setEmail_number(user_data.getUser().getUserprofile().getContactDetails().get(i).getEmailNumber());
                contactdetail2.setType(user_data.getUser().getUserprofile().getContactDetails().get(i).getType());
                contactdetail2.setIs_default(0);
                contactdetail2.setLabel(user_data.getUser().getUserprofile().getContactDetails().get(i).getLabel());
                contactdetails.add(i, contactdetail2);
            }
        }

        set_contact.setContactDetails(contactDetails_list);
        addcontectModel.setContactdetails(contactdetails);
   //     SessionManager.setOneCotect_deatil(getActivity(), set_contact);

        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Showlayout();
            }
        });


        if (flag.equals("edit")) {
            company_layout.setEnabled(true);
            tv_add_social.setVisibility(View.GONE);
            media_link.setVisibility(View.GONE);
            ev_company.setEnabled(false);
            ev_company_url.setEnabled(true);
            ev_job.setEnabled(true);
            ev_zoom.setEnabled(true);
            ev_address.setEnabled(true);
            ev_city.setEnabled(true);
            ev_state.setEnabled(true);
            ev_zip.setEnabled(true);
            //  ev_bob.setEnabled(true);
            ev_note.setEnabled(true);
            ev_breakout.setEnabled(true);
            ev_linkedin.setEnabled(true);
            ev_twitter.setEnabled(true);
            ev_fb.setEnabled(true);

            layout_bod.setEnabled(true);
            edt_mobile_no.setEnabled(false);
            edt_mobile_no.setText(user_data_model.getContactNumber());
            edt_email.setText(user_data_model.getEmail());
            edt_email.setEnabled(false);
            layout_country_piker.setVisibility(View.VISIBLE);

            // Log.e("Null", "No Call");
            edit = true;
            iv_down.setVisibility(View.VISIBLE);
            copany_down_arrow.setVisibility(View.VISIBLE);
            //ContectListData.Contact Contect_data = SessionManager.getOneCotect_deatil(getActivity());
            Userprofile Contect_data = user_data_model.getUserprofile();

            addcontectModel.setTime(String.valueOf(Contect_data.getTimezoneId()));
            addcontectModel.setJob_title(Contect_data.getJob_title());
            addcontectModel.setState(Contect_data.getState());
            addcontectModel.setCity(Contect_data.getCity());
            addcontectModel.setCompany(Contect_data.getCompany_name());
            addcontectModel.setZoom_id(Contect_data.getZoom_id());
            addcontectModel.setAddress(Contect_data.getAddress());
            addcontectModel.setZoom_id(Contect_data.getZipcode());
            addcontectModel.setCompany_url(Contect_data.getCompany_url());
            addcontectModel.setBreakoutu(Contect_data.getBreakout_link());
            addcontectModel.setLinkedin(Contect_data.getLinkedin_link());
            addcontectModel.setFacebook(Contect_data.getFacebook_link());
            addcontectModel.setBirthday(Contect_data.getDob());

            ev_zip.setText(Contect_data.getZipcode());
            ev_address.setText(Contect_data.getAddress());
            ev_zoom.setText(Contect_data.getZoom_id());
            ev_company.setText(Contect_data.getCompany_name());

            ev_state.setText(Contect_data.getState());
            ev_city.setText(Contect_data.getCity());
            if (String.valueOf(Contect_data.getTimezoneId()).equals("null")) {
                time_zone_id = TimeZone.getDefault().getID();
                zone_txt.setText(time_zone_id);

            } else {
                /*                zone_txt.setText());*/

              /*  for (int i = 0; i < timezoneModels.size(); i++) {
                    if (String.valueOf(Contect_data.getTimezoneId()).equals(timezoneModels.get(i).getValue().toString())) {
                        zone_txt.setText(timezoneModels.get(i).getText());
                        Log.e("No Same Data", "NO");
                    } else {
                        Log.e("No Same Data", "Yes");
                    }
                }*/
            }
            ev_job.setText(Contect_data.getJob_title());
            try {
                contect_id = Contect_data.getId();
            } catch (Exception e) {
                e.printStackTrace();
            }


            ev_company_url.setText(Contect_data.getCompany_url());
            ev_bob.setText(Contect_data.getDob());
            ev_twitter.setText(Contect_data.getTwitter_link());
            ev_fb.setText(Contect_data.getFacebook_link());
            ev_linkedin.setText(Contect_data.getLinkedin_link());
            ev_breakout.setText(Contect_data.getBreakout_link());
          /*  PhoneViewAdd();
            EmailViewAdd();*/
            try {
                TextSet();
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                List<ContactDetail> detail_contect = Contect_data.getContactDetails();
                for (int i = 0; i < detail_contect.size(); i++) {
                    if (!detail_contect.get(i).getEmailNumber().trim().equalsIgnoreCase("")) {
                        if (detail_contect.get(i).getType().equals("EMAIL")) {
                            Contactdetail contactdetail = new Contactdetail();
/*
                        contactdetail.setCountry_code(detail_contect.get(i).getCountryCode());
*/
                            contactdetail.setType(detail_contect.get(i).getType());
                            contactdetail.setEmail_number(detail_contect.get(i).getEmailNumber());
                            contactdetail.setId(0);
                            contactdetail.setLabel(detail_contect.get(i).getLabel());
                            contactdetail.setIs_default(0);
                            emaildetails_list.add(contactdetail);
                            Collections.reverse(emaildetails_list);
                            contactdetails.add(contactdetail);

                            layout_Add_email.setVisibility(View.GONE);
                            emailAdapter = new EmailAdapter(getActivity(), emaildetails_list, layout_Add_email);
                            rv_email.setLayoutManager(new LinearLayoutManager(getActivity()));
                            rv_email.setAdapter(emailAdapter);
                            addcontectModel.setContactdetails(contactdetails);


                        } else {
                            //    Log.e("Label is ", String.valueOf(detail_contect.get(i).getIsDefault()));
                            Contactdetail contactdetail = new Contactdetail();
                            /*                        contactdetail.setCountry_code(detail_contect.get(i).getCountryCode());*/
                            contactdetail.setType(detail_contect.get(i).getType());
                            contactdetail.setEmail_number(detail_contect.get(i).getEmailNumber());


                            contactdetail.setLabel(detail_contect.get(i).getLabel());
                            contactdetail.setIs_default(0);

                            phonedetails_list.add(contactdetail);
                            Collections.reverse(phonedetails_list);
                            contactdetails.add(contactdetail);

                            phoneAdapter = new PhoneAdapter(getActivity(), phonedetails_list, layout_Add_phone);
                            rv_phone.setLayoutManager(new LinearLayoutManager(getActivity()));
                            rv_phone.setAdapter(phoneAdapter);
                            addcontectModel.setContactdetails(contactdetails);
                            Log.e("Add Contect Model is ", new Gson().toJson(addcontectModel));
                        }
                    }
                }
                SessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);
            } catch (Exception e) {
                e.printStackTrace();
            }


            EmailViewAdd();
            PhoneViewAdd();

            phonelistSwipe();
            EmailistSwipe();




        } else if (flag.equals("read")) {

           // Log.e("Model Data", new Gson().toJson(user_data_model));
            edt_mobile_no.setEnabled(false);
            edt_mobile_no.setText(user_data_model.getContactNumber());
            edt_email.setText(user_data_model.getEmail());
            edt_email.setEnabled(false);
            layout_country_piker.setVisibility(View.GONE);
            layout_bod.setEnabled(false);

            company_layout.setEnabled(false);
            media_link.setVisibility(View.GONE);
            tv_add_social.setVisibility(View.VISIBLE);
            ev_company.setEnabled(false);
            ev_company_url.setEnabled(false);
            iv_down.setVisibility(View.GONE);
            copany_down_arrow.setVisibility(View.GONE);
            ev_job.setEnabled(false);
            ev_zoom.setEnabled(false);
            ev_address.setEnabled(false);
            ev_city.setEnabled(false);
            ev_state.setEnabled(false);
            ev_zip.setEnabled(false);
            ev_bob.setEnabled(false);
            ev_note.setEnabled(false);

            ev_breakout.setEnabled(false);
            ev_linkedin.setEnabled(false);
            ev_twitter.setEnabled(false);
            ev_fb.setEnabled(false);

            tv_add_social.setVisibility(View.GONE);
            edt_mobile_no.setTextColor(getActivity().getResources().getColor(R.color.purple_200));
            edt_email.setTextColor(getActivity().getResources().getColor(R.color.purple_200));


            tv_add_social.setTextColor(getActivity().getResources().getColor(R.color.purple_200));
            ev_company.setTextColor(getActivity().getResources().getColor(R.color.purple_200));
            ev_company_url.setTextColor(getActivity().getResources().getColor(R.color.purple_200));
            ev_job.setTextColor(getActivity().getResources().getColor(R.color.purple_200));
            ev_zoom.setTextColor(getActivity().getResources().getColor(R.color.purple_200));
            ev_address.setTextColor(getActivity().getResources().getColor(R.color.purple_200));
            ev_city.setTextColor(getActivity().getResources().getColor(R.color.purple_200));
            ev_state.setTextColor(getActivity().getResources().getColor(R.color.purple_200));
            ev_zip.setTextColor(getActivity().getResources().getColor(R.color.purple_200));
            ev_bob.setTextColor(getActivity().getResources().getColor(R.color.purple_200));
            ev_note.setTextColor(getActivity().getResources().getColor(R.color.purple_200));

            // ContectListData.Contact Contect_data = SessionManager.getOneCotect_deatil(getActivity());
            Userprofile Contect_data = user_data_model.getUserprofile();
            addcontectModel.setTime(String.valueOf(Contect_data.getTimezoneId()));
            addcontectModel.setJob_title(Contect_data.getJob_title());
            addcontectModel.setState(Contect_data.getState());
            addcontectModel.setCity(Contect_data.getCity());
            addcontectModel.setCompany(Contect_data.getCompany_name());
            addcontectModel.setZoom_id(Contect_data.getZoom_id());
            addcontectModel.setAddress(Contect_data.getAddress());
            addcontectModel.setZoom_id(Contect_data.getZipcode());
            addcontectModel.setCompany_url(Contect_data.getCompany_url());
            addcontectModel.setBreakoutu(Contect_data.getBreakout_link());
            addcontectModel.setLinkedin(Contect_data.getLinkedin_link());
            addcontectModel.setFacebook(Contect_data.getFacebook_link());
            addcontectModel.setBirthday(Contect_data.getDob());
            /* addcontectModel.setTime(Contect_data.getTimezoneId());*//*
          /* img_fb.setVisibility(View.GONE);
            img_breakout.setVisibility(View.GONE);
            img_linkdin.setVisibility(View.GONE);
            img_twitter.setVisibility(View.GONE);
*/

            ev_zip.setText(Contect_data.getZipcode());
            ev_address.setText(Contect_data.getAddress());
            ev_zoom.setText(Contect_data.getZoom_id());
            ev_company.setText(Contect_data.getCompany_name());
            ev_state.setText(Contect_data.getState());
            ev_city.setText(Contect_data.getCity());
            if (String.valueOf(Contect_data.getTimezoneId()).equals("null")) {
                time_zone_id = TimeZone.getDefault().getID();
                zone_txt.setText(time_zone_id);
            } else {
             /*   Log.e("Time Zone Size", String.valueOf(timezoneModels.size()));
                for (int i = 0; i < timezoneModels.size(); i++) {
                    Log.e("Time Id1",timezoneModels.get(i).getValue().toString());
                    Log.e("Time Id2",Contect_data.getTimezoneId().toString());

                    if (String.valueOf(Contect_data.getTimezoneId()).equals(timezoneModels.get(i).getValue().toString())) {
                        zone_txt.setText(timezoneModels.get(i).getText());
                        break;
                    }
                    else {
                        Log.e("No Same Data", "Yes");
                    }
                } */
            }
            ev_job.setText(Contect_data.getJob_title());
            // contect_id = Contect_data.getId();
            /*            organization_id = String.valueOf(Contect_data.getOrg));*/
            //team_id = String.valueOf(Contect_data.getTeamId());
            ev_company_url.setText(Contect_data.getCompany_url());
            ev_bob.setText(Contect_data.getDob());
            media_link.setVisibility(View.GONE);
            try {

                if (Contect_data.getFacebook_link().equals("")) {
                    img_fb.setVisibility(View.GONE);
                } else {
                    img_fb.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                img_fb.setVisibility(View.GONE);
            }


            img_fb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    Uri uri = Uri.parse(Contect_data.getFacebook_link()); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
            try {
                if (Contect_data.getTwitter_link().equals("")) {
                    img_twitter.setVisibility(View.GONE);
                } else {
                    img_twitter.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                img_twitter.setVisibility(View.GONE);

            }

            ev_twitter.setText(Contect_data.getTwitter_link());
            ev_fb.setText(Contect_data.getFacebook_link());
            ev_linkedin.setText(Contect_data.getLinkedin_link());
            ev_breakout.setText(Contect_data.getBreakout_link());
            img_twitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    Uri uri = Uri.parse(Contect_data.getTwitter_link()); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
           /*     try {
                    if (Contect_data.getLinkedin_link().equals("")) {
                        img_linkdin.setVisibility(View.GONE);
                    } else {
                        img_linkdin.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    img_linkdin.setVisibility(View.GONE);

                }*/


             /*   try {

                    if (Contect_data.getBreakout_link().equals("")) {
                        img_breakout.setVisibility(View.GONE);
                    } else {
                        img_breakout.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    img_breakout.setVisibility(View.GONE);

                }*/



       /*     PhoneViewAdd();
            EmailViewAdd();
            TextSet();*/
            try {
                List<ContactDetail> detail_contect = Contect_data.getContactDetails();
                for (int i = 0; i < detail_contect.size(); i++) {
                    if (!detail_contect.get(i).getEmailNumber().trim().equalsIgnoreCase("")) {
                        if (detail_contect.get(i).getType().equals("EMAIL")) {
                            Contactdetail contactdetail = new Contactdetail();
                            // contactdetail.setCountry_code(detail_contect.get(i).getCountryCode());
                            contactdetail.setType(detail_contect.get(i).getType());
                            contactdetail.setEmail_number(detail_contect.get(i).getEmailNumber());
                            //contactdetail.setId(detail_contect.get(i).getId());
                            contactdetail.setLabel(detail_contect.get(i).getLabel());
                            contactdetail.setIs_default(0);
                            emaildetails_list.add(contactdetail);
                            Collections.reverse(emaildetails_list);
                            contactdetails.add(contactdetail);

                            layout_Add_email.setVisibility(View.GONE);


                        } else {
                            /*                        Log.e("Label is ", String.valueOf(detail_contect.get(i).getIsDefault()));*/
                            Contactdetail contactdetail = new Contactdetail();
                            /*                        contactdetail.setCountry_code(detail_contect.get(i).getCountryCode());*/
                            contactdetail.setType(detail_contect.get(i).getType());
                            contactdetail.setEmail_number(detail_contect.get(i).getEmailNumber());
                            //contactdetail.setId(detail_contect.get(i).getId());
                            contactdetail.setLabel(detail_contect.get(i).getLabel());
                            contactdetail.setIs_default(0);

                            phonedetails_list.add(contactdetail);
                            Collections.reverse(phonedetails_list);
                            contactdetails.add(contactdetail);

                            Log.e("Contect List Is", new Gson().toJson(phonedetails_list));


                        }
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            phoneAdapter = new PhoneAdapter(getActivity(), phonedetails_list, layout_Add_phone);
            rv_phone.setLayoutManager(new LinearLayoutManager(getActivity()));
            rv_phone.setAdapter(phoneAdapter);
            emailAdapter = new EmailAdapter(getActivity(), emaildetails_list, layout_Add_email);
            rv_email.setLayoutManager(new LinearLayoutManager(getActivity()));
            rv_email.setAdapter(emailAdapter);


        } else {
            PhoneViewAdd();
            EmailViewAdd();

            try {
                TextSet();
            } catch (Exception e) {
                e.printStackTrace();
            }
            iv_down.setVisibility(View.VISIBLE);
            media_link.setVisibility(View.GONE);
            tv_add_social.setVisibility(View.GONE);
        }
    }
    private void EmailistSwipe() {
        SwipeHelper swipeHelper = new SwipeHelper(getActivity()) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Delete",
                        0,
                        Color.parseColor("#FF3C30"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(final int pos) {
                                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                                    return;
                                }
                                mLastClickTime = SystemClock.elapsedRealtime();
                                final Contactdetail item = emaildetails_list.get(pos);
                                emailAdapter.removeItem(pos,item);

                                Toast.makeText(getContext(), "Item was removed from the list.", Toast.LENGTH_LONG).show();
                            }
                        }
                ));

            }
        };
        swipeHelper.attachToRecyclerView(rv_email);
    }
    private void phonelistSwipe() {
        SwipeHelper swipeHelper = new SwipeHelper(getActivity()) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Delete",
                        0,
                        Color.parseColor("#FF3C30"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(final int pos) {
                                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                                    return;
                                }
                                mLastClickTime = SystemClock.elapsedRealtime();
                                final Contactdetail item = phonedetails_list.get(pos);
                                phoneAdapter.removeItem(pos,item);

                                Toast.makeText(getContext(), "Item was removed from the list.", Toast.LENGTH_LONG).show();
                            }
                        }
                ));

            }
        };
        swipeHelper.attachToRecyclerView(rv_phone);
    }
    private void Showlayout() {
        tv_more_field.setVisibility(View.GONE);
        media_layout.setVisibility(View.VISIBLE);

        company_url_layout.setVisibility(View.VISIBLE);
        job_layout.setVisibility(View.VISIBLE);
        zoom_layout.setVisibility(View.VISIBLE);
        city_layout.setVisibility(View.VISIBLE);
        state_layout.setVisibility(View.VISIBLE);
        time_layout.setVisibility(View.VISIBLE);
        layout_bod.setVisibility(View.VISIBLE);
        note_layout.setVisibility(View.GONE);
    }


    private void EmailViewAdd() {

        String flag = SessionManager.getContect_flag(getActivity());
        if (flag.equals("edit")) {
            layout_Add_email.setVisibility(View.VISIBLE);
            layout_Add_email.setOnClickListener(v -> {
                Contactdetail contactdetail1 = new Contactdetail();
                contactdetail1.setId(0);
                contactdetail1.setEmail_number("");
                contactdetail1.setIs_default(0);
                contactdetail1.setLabel("Home");
                contactdetail1.setType("EMAIL");
                emaildetails_list.add(contactdetail1);
                contactdetails.add(contactdetail1);
                //emailAdapter.notifyDataSetChanged();
                layout_Add_email.setVisibility(View.GONE);
                emailAdapter = new EmailAdapter(getActivity(), emaildetails_list, layout_Add_email);
                rv_email.setLayoutManager(new LinearLayoutManager(getActivity()));
                rv_email.setAdapter(emailAdapter);
                //     Log.e("layout_Add_email",new Gson().toJson(emaildetails_list));
               // Log.e("Concet List size", String.valueOf(contactdetails.size()));
               // Log.e("Email  List is ", new Gson().toJson(SessionManager.getAdd_Contect_Detail(getActivity())));
            });
        } else {


            Contactdetail contactdetail = new Contactdetail();
            contactdetail.setId(contactdetails.size());
            contactdetail.setEmail_number("");
            contactdetail.setIs_default(0);
            contactdetail.setLabel("Home");
            contactdetail.setType("EMAIL");
            emaildetails_list.add(contactdetail);
            contactdetails.add(contactdetail);
            emailAdapter = new EmailAdapter(getActivity(), emaildetails_list, layout_Add_email);
            rv_email.setLayoutManager(new LinearLayoutManager(getActivity()));
            rv_email.setAdapter(emailAdapter);

            layout_Add_email.setOnClickListener(v -> {
                Contactdetail contactdetail1 = new Contactdetail();
                contactdetail1.setId(contactdetails.size());
                contactdetail1.setEmail_number("");
                contactdetail1.setIs_default(0);
                contactdetail1.setLabel("Home");
                contactdetail1.setType("EMAIL");
                emaildetails_list.add(contactdetail1);
                contactdetails.add(contactdetail1);
                //emailAdapter.notifyDataSetChanged();
                layout_Add_email.setVisibility(View.GONE);
                emailAdapter = new EmailAdapter(getActivity(), emaildetails_list, layout_Add_email);
                rv_email.setLayoutManager(new LinearLayoutManager(getActivity()));
                rv_email.setAdapter(emailAdapter);
                //     Log.e("layout_Add_email",new Gson().toJson(emaildetails_list));
                Log.e("Concet List size", String.valueOf(contactdetails.size()));
                Log.e("Email  List is ", new Gson().toJson(SessionManager.getAdd_Contect_Detail(getActivity())));
            });
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void PhoneViewAdd() {

        String flag = SessionManager.getContect_flag(getActivity());
        if (flag.equals("edit")) {

            layout_Add_phone.setVisibility(View.VISIBLE);

            layout_Add_phone.setOnClickListener(v -> {
                Contactdetail contactdetail1 = new Contactdetail();
                contactdetail1.setId(0);
                contactdetail1.setEmail_number("");
                contactdetail1.setIs_default(0);
                contactdetail1.setLabel("Home");
                contactdetail1.setType("NUMBER");
                phonedetails_list.add(contactdetail1);
                contactdetails.add(contactdetail1);
                Log.e("Add View is", new Gson().toJson(phonedetails_list));
                phoneAdapter = new PhoneAdapter(getActivity(), phonedetails_list, layout_Add_phone);
                rv_phone.setLayoutManager(new LinearLayoutManager(getActivity()));
                rv_phone.setAdapter(phoneAdapter);
                layout_Add_phone.setVisibility(View.GONE);
            });

        } else {


            Contactdetail contactdetail = new Contactdetail();
            contactdetail.setId(contactdetails.size());
            contactdetail.setEmail_number("");
            contactdetail.setIs_default(1);
            contactdetail.setLabel("Home");
            contactdetail.setType("NUMBER");
            phonedetails_list.add(contactdetail);
            contactdetails.add(contactdetail);


            //   Log.e("phonedetails_list",new Gson().toJson(phonedetails_list));


            phoneAdapter = new PhoneAdapter(getActivity(), phonedetails_list, layout_Add_phone);
            rv_phone.setLayoutManager(new LinearLayoutManager(getActivity()));
            rv_phone.setAdapter(phoneAdapter);

            layout_Add_phone.setOnClickListener(v -> {
                Contactdetail contactdetail1 = new Contactdetail();
                contactdetail1.setId(contactdetails.size());
                contactdetail1.setEmail_number("");
                contactdetail1.setIs_default(0);
                contactdetail1.setLabel("Home");
                contactdetail1.setType("NUMBER");
                phonedetails_list.add(contactdetail1);
                contactdetails.add(contactdetail1);
                phoneAdapter = new PhoneAdapter(getActivity(), phonedetails_list, layout_Add_phone);
                rv_phone.setLayoutManager(new LinearLayoutManager(getActivity()));
                rv_phone.setAdapter(phoneAdapter);
                layout_Add_phone.setVisibility(View.GONE);
            });
        }


    }

    private void TextSet() {


        ev_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                addcontectModel.setAddress(charSequence.toString());
                SessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ev_city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                addcontectModel.setCity(charSequence.toString());
                SessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ev_zip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                addcontectModel.setZip_code(charSequence.toString());
                SessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ev_zoom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                addcontectModel.setZoom_id(charSequence.toString());
                SessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ev_note.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                addcontectModel.setNote(charSequence.toString());
                SessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        ev_state.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                addcontectModel.setState(charSequence.toString());
                SessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ev_company.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addcontectModel.setCompany(s.toString());
                SessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        ev_company_url.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addcontectModel.setCompany_url(s.toString());
                SessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        select_label_zone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                showBottomSheetDialog_For_TimeZone();

            }
        });


        ev_fb.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addcontectModel.setFacebook(s.toString());
                SessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        ev_twitter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addcontectModel.setTwitter(s.toString());
                SessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        ev_linkedin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addcontectModel.setLinkedin(s.toString());
                SessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        ev_breakout.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addcontectModel.setBreakoutu(s.toString());
                SessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        ev_job.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addcontectModel.setJob_title(s.toString());
                SessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void IntentUI(View view) {
        layout_referenceCode = view.findViewById(R.id.layout_referenceCode);
        layout_country_piker = view.findViewById(R.id.layout_country_piker);
        edt_email = view.findViewById(R.id.edt_email);
        edt_mobile_no = view.findViewById(R.id.edt_mobile_no);
        copany_down_arrow = view.findViewById(R.id.copany_down_arrow);
        iv_down = view.findViewById(R.id.iv_down);
        tv_phone = view.findViewById(R.id.tv_phone);
        ev_address = view.findViewById(R.id.ev_address);
        ev_city = view.findViewById(R.id.ev_city);
        select_state = view.findViewById(R.id.select_state);
        ev_zip = view.findViewById(R.id.ev_zip);
        ev_zoom = view.findViewById(R.id.ev_zoom);
        ev_note = view.findViewById(R.id.ev_note);
        add_mobile_Number = view.findViewById(R.id.add_mobile_Number);
        pulse_icon = view.findViewById(R.id.pulse_icon);
        pulse_icon.setColorFilter(getActivity().getResources().getColor(R.color.purple_200));
        layout_Add_phone = view.findViewById(R.id.layout_Add_phone);
        pulse_icon1 = view.findViewById(R.id.pulse_icon1);
        pulse_icon1.setColorFilter(getActivity().getResources().getColor(R.color.purple_200));
        layout_Add_email = view.findViewById(R.id.layout_Add_email);
        layout_mobile = view.findViewById(R.id.layout_mobile);
        ev_state = view.findViewById(R.id.ev_state);
        city_layout = view.findViewById(R.id.city_layout);
        zoom_layout = view.findViewById(R.id.zoom_layout);
        note_layout = view.findViewById(R.id.note_layout);
        tv_more_field = view.findViewById(R.id.tv_more_field);
        company_url_layout = view.findViewById(R.id.company_url_layout);
        tv_company_url = view.findViewById(R.id.tv_company_url);
        ev_company_url = view.findViewById(R.id.ev_company_url);
        ev_company = view.findViewById(R.id.ev_company);
        job_layout = view.findViewById(R.id.job_layout);
        tv_job = view.findViewById(R.id.tv_job);
        ev_job = view.findViewById(R.id.ev_job);
        layout_time_zone = view.findViewById(R.id.layout_time_zone);
        select_label_zone = view.findViewById(R.id.select_label_zone);
        zone_txt = view.findViewById(R.id.zone_txt);
        layout_bod = view.findViewById(R.id.layout_bod);
        ev_bob = view.findViewById(R.id.ev_bob);
        fb_layout = view.findViewById(R.id.fb_layout);
        ev_fb = view.findViewById(R.id.ev_fb);
        twitter_layout = view.findViewById(R.id.twitter_layout);
        ev_twitter = view.findViewById(R.id.ev_twitter);
        breakout_layout = view.findViewById(R.id.breakout_layout);
        ev_breakout = view.findViewById(R.id.ev_breakout);
        linkedin_layout = view.findViewById(R.id.linkedin_layout);
        ev_linkedin = view.findViewById(R.id.ev_linkedin);
        state_layout = view.findViewById(R.id.state_layout);
        time_layout = view.findViewById(R.id.time_layout);
        media_layout = view.findViewById(R.id.media_layout);
        rv_phone = view.findViewById(R.id.rv_phone);
        rv_email = view.findViewById(R.id.rv_email);
        mMainLayout = view.findViewById(R.id.mMainLayout);
        tv_add_social = view.findViewById(R.id.tv_add_social);
        media_link = view.findViewById(R.id.media_link);
        img_fb = view.findViewById(R.id.img_fb);
        img_twitter = view.findViewById(R.id.img_twitter);
        img_linkdin = view.findViewById(R.id.img_linkdin);
        img_breakout = view.findViewById(R.id.img_breakout);
        company_layout = view.findViewById(R.id.company_layout);
        company_layout.setOnClickListener(this);
        other_company_layout = view.findViewById(R.id.other_company_layout);
        ev_othre_company = view.findViewById(R.id.ev_othre_company);
        layout_referenceCode.setOnClickListener(this);

    }


    void showBottomSheetDialog_For_Home(String moble, TextView phone_txt, TextView email_txt, Contactdetail item, int item_postion) {
        bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialog);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_for_home);
        RecyclerView home_type_list = bottomSheetDialog.findViewById(R.id.home_type_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        home_type_list.setLayoutManager(layoutManager);

        WorkTypeData nameList = new WorkTypeData("1", "Home", "true");
        WorkTypeData nameList1 = new WorkTypeData("1", "Main", "true");
        WorkTypeData nameList2 = new WorkTypeData("1", "Work", "true");
        WorkTypeData nameList3 = new WorkTypeData("1", "Other", "true");
        WorkTypeData nameList4 = new WorkTypeData("1", "Add custom", "true");

        List<WorkTypeData> workTypeData = new ArrayList<>();
        workTypeData.add(0, nameList);
        workTypeData.add(1, nameList1);
        workTypeData.add(2, nameList2);
        workTypeData.add(3, nameList3);
        workTypeData.add(4, nameList4);
        WorkAdapter workAdapter = new WorkAdapter(getActivity(), workTypeData, moble, phone_txt, email_txt, item, item_postion);
        home_type_list.setAdapter(workAdapter);
        bottomSheetDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_referenceCode:
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Code", referenceCode);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "Code copy", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void showAlertDialogButtonClicked(View view) {

        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(getActivity(), R.style.BottomSheetDialog);
        final View customLayout
                = getLayoutInflater()
                .inflate(
                        R.layout.custom_add_work_layout,
                        null);
        builder.setView(customLayout);
        EditText editText = customLayout.findViewById(R.id.editText);
        TextView tv_cancel = customLayout.findViewById(R.id.tv_cancel);
        TextView tv_add = customLayout.findViewById(R.id.tv_add);
        AlertDialog dialog
                = builder.create();
        dialog.show();
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                dialog.dismiss();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                dialog.dismiss();
            }
        });

    }

    public void OpenBob() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {


                        String sMonth = "";
                        if (monthOfYear + 1 < 10) {
                            sMonth = "0" + (monthOfYear + 1);
                        } else {
                            sMonth = String.valueOf(monthOfYear + 1);
                        }


                        String sdate = "";
                        if (dayOfMonth < 10) {
                            sdate = "0" + dayOfMonth;
                        } else {
                            sdate = String.valueOf(dayOfMonth);
                        }


                        ev_bob.setText(year + "-" + sMonth + "-" + sdate);
                        addcontectModel.setBirthday(year + "-" + sMonth + "-" + sdate);
                        SessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + (1000 * 60 * 60));

        datePickerDialog.show();

    }

    private void RemoveContect(int id) throws JSONException {
        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());

        String token = Global.getToken(sessionManager);
        JSONObject obj = new JSONObject();
        JSONObject paramObject = new JSONObject();
        paramObject.put("organization_id", SessionManager.getOneCotect_deatil(getActivity()).getOrganizationId());
        paramObject.put("team_id", SessionManager.getOneCotect_deatil(getActivity()).getTeamId());
        paramObject.put("user_id", user_id);
        paramObject.put("id", id);
        paramObject.put("contact_id", SessionManager.getOneCotect_deatil(getActivity()).getId());
        paramObject.put("status", "D");
        obj.put("data", paramObject);
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        //Log.e("Obbject data", new Gson().toJson(gsonObject));
        retrofitCalls.Contact_details_update(sessionManager, gsonObject, loadingDialog, token, Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {

                loadingDialog.cancelLoading();
                Global.Messageshow(getActivity(), mMainLayout, response.body().getMessage(), response.body().getHttp_status() == 200);
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }


        });


    }

    private void UpdateContect(Contactdetail id) throws JSONException {
        Log.e("Update Api Call", "Yes");
        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());

        String token = Global.getToken(sessionManager);
        JSONObject obj = new JSONObject();
        JSONObject paramObject = new JSONObject();
        paramObject.put("organization_id", SessionManager.getOneCotect_deatil(getActivity()).getOrganizationId());
        paramObject.put("team_id", SessionManager.getOneCotect_deatil(getActivity()).getTeamId());
        paramObject.put("user_id", user_id);
        Log.e("Id is", String.valueOf(id.getId()));
        if (id.getId() == 0) {

        } else {
            paramObject.put("id", id.getId());
        }

        paramObject.put("contact_id", SessionManager.getOneCotect_deatil(getActivity()).getId());
        paramObject.put("label", id.getLabel());
        paramObject.put("type", id.getType());
        paramObject.put("email_number", id.getEmail_number());
        paramObject.put("country_code", id.getCountry_code());
        obj.put("data", paramObject);
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        //Log.e("Obbject data", new Gson().toJson(gsonObject));
        retrofitCalls.update_contect(sessionManager, gsonObject, loadingDialog, token, Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {

                loadingDialog.cancelLoading();
                Global.Messageshow(getActivity(), mMainLayout, response.body().getMessage(), response.body().getHttp_status() == 200);
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }

        });


    }

    public void EnableRuntimePermission() {

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {


            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {

                //EnableRuntimePermission();
            }

        };
        TedPermission.with(getActivity())
                .setPermissionListener(permissionlistener)
                .setDeniedTitle("Contactninja would like to access your contacts")
                .setDeniedMessage("Contact Ninja uses your contacts to improve your business’s marketing outreach by aggrregating your contacts.")
                .setGotoSettingButtonText("setting")
                .setPermissions(Manifest.permission.CALL_PHONE)
                .setRationaleConfirmText("OK")
                .check();


    }

    private void Timezoneget() throws JSONException {

        //loadingDialog.showLoadingDialog();

        SignResponseModel user_data = SessionManager.getGetUserdata(getContext());
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());
        JsonObject obj = new JsonObject();

        JsonObject paramObject = new JsonObject();

        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("user_id", user_id);
        paramObject.addProperty("team_id", "1");
        obj.add("data", paramObject);

      /*  JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        Log.e("Gson Data is",new Gson().toJson(gsonObject));
*/

        retrofitCalls.Timezone_list(sessionManager, obj, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                if (response.body().getHttp_status() == 200) {

                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<List<TimezoneModel>>() {
                    }.getType();
                    timezoon = new Gson().fromJson(headerString, listType);
                    timezoneModels.addAll(timezoon);

                    SignResponseModel user_data1 = SessionManager.getGetUserdata(getActivity());
                    if (String.valueOf(user_data1.getUser().getUserprofile().getTimezoneId()).equals("null")) {
                        time_zone_id = TimeZone.getDefault().getID();
                        zone_txt.setText(time_zone_id);

                    } else {
                        String t_id = user_data1.getUser().getUserprofile().getTimezoneId().toString();
                        for (int i = 0; i < timezoneModels.size(); i++) {
                            if (t_id.equals(timezoneModels.get(i).getValue().toString() + ".0")) {
                                zone_txt.setText(timezoneModels.get(i).getText());

                                Log.e("No Same Data", "NO");
                                break;
                            } else {
                                Log.e("No Same Data", "Yes");
                            }
                        }
                    }

                    loadingDialog.cancelLoading();

                } else {
                    // loadingDialog.cancelLoading();
                }


            }

            @Override
            public void error(Response<ApiResponse> response) {
                //  loadingDialog.cancelLoading();
            }
        });
    }

    void showBottomSheetDialog_For_TimeZone() {
        bottomSheetDialog_time = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialog);
        bottomSheetDialog_time.setContentView(R.layout.bottom_sheet_dialog_for_home);
        RecyclerView home_type_list = bottomSheetDialog_time.findViewById(R.id.home_type_list);
        TextView tv_item = bottomSheetDialog_time.findViewById(R.id.tv_item);
        tv_item.setText("Please select Timezone");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        home_type_list.setLayoutManager(layoutManager);

        TimezoneAdapter timezoneAdapter = new TimezoneAdapter(getActivity(), timezoneModels);
        home_type_list.setAdapter(timezoneAdapter);

        bottomSheetDialog_time.show();
    }

    void showBottomSheetDialog_For_Company() {
        bottomSheetDialog_company = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialog);
        bottomSheetDialog_company.setContentView(R.layout.bottom_sheet_dialog_for_compnay);
        RecyclerView home_type_list = bottomSheetDialog_company.findViewById(R.id.home_type_list);
        TextView tv_item = bottomSheetDialog_company.findViewById(R.id.tv_item);
        tv_item.setText("Please select company");
        tv_item.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        home_type_list.setLayoutManager(layoutManager);
        ImageView search_icon = bottomSheetDialog_company.findViewById(R.id.search_icon);
        EditText ev_search = bottomSheetDialog_company.findViewById(R.id.ev_search);
        LinearLayout add_new = bottomSheetDialog_company.findViewById(R.id.add_new);
        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                ev_search.requestFocus();
            }
        });

        home_type_list.setAdapter(companyAdapter);
        home_type_list.addOnScrollListener(new PaginationListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                try {
                    if (Global.isNetworkAvailable(getActivity(), MainActivity.mMainLayout)) {
                        CompanyList();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        add_new.setVisibility(View.GONE);
        add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                bottomSheetDialog_company.cancel();
            }
        });
        ev_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<CompanyModel.Company> temp = new ArrayList();
                for (CompanyModel.Company d : companyList) {
                    if (d.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        temp.add(d);
                        // Log.e("Same Data ",d.getUserName());
                    }
                }
                companyAdapter.updateList(temp);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        bottomSheetDialog_company.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        currentPage = PAGE_START;
        isLastPage = false;
        companyList.clear();
        companyAdapter.clear();
        try {
            if (Global.isNetworkAvailable(getActivity(), MainActivity.mMainLayout)) {

                CompanyList();
                Timezoneget();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void CompanyList() throws JSONException {

        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());

        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", user_id);
        paramObject.addProperty("perPage", perPage);
        paramObject.addProperty("page", currentPage);
        obj.add("data", paramObject);
        retrofitCalls.CompanyList(sessionManager, obj, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                //Log.e("Response is",new Gson().toJson(response));
                if (response.body().getHttp_status().equals(200)) {
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    if (response.body().getHttp_status() == 200) {
                        //    sessionManager.setCompanylist(getActivity(), new ArrayList<>());
                        Type listType = new TypeToken<CompanyModel>() {
                        }.getType();
                        CompanyModel data = new Gson().fromJson(headerString, listType);
                        List<CompanyModel.Company> companyList = data.getData();
                        // sessionManager.setCompanylist(getActivity(), data.getData());


                        if (currentPage != PAGE_START) companyAdapter.removeLoading();
                        companyAdapter.addItems(companyList);
                        // check weather is last page or not
                        if (data.getTotal() > companyAdapter.getItemCount()) {
                            companyAdapter.addLoading();
                        } else {
                            isLastPage = true;
                        }
                        isLoading = false;

                    } else {
                        // Global.Messageshow(getApplicationContext(), mMainLayout, headerString, false);

                    }

                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
            }
        });
    }

    public class WorkAdapter extends RecyclerView.Adapter<WorkAdapter.InviteListDataclass> {

        public Context mCtx;
        String type;
        TextView phone_txt;
        TextView email_txt;
        Contactdetail item;
        private List<WorkTypeData> worklist;
        int item_postion;

        public WorkAdapter(Context context, List<WorkTypeData> worklist, String type, TextView phone_txt, TextView email_txt, Contactdetail item, int item_postion) {
            this.mCtx = context;
            this.worklist = worklist;
            this.type = type;
            this.phone_txt = phone_txt;
            this.email_txt = email_txt;
            this.item = item;
            this.item_postion = item_postion;
        }

        @NonNull
        @Override
        public WorkAdapter.InviteListDataclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.work_type_selecte, parent, false);
            return new WorkAdapter.InviteListDataclass(view);
        }

        @Override
        public void onBindViewHolder(@NonNull WorkAdapter.InviteListDataclass holder, int position) {
            WorkTypeData WorkData = worklist.get(position);
            holder.tv_item.setText(WorkData.getTitale());
            holder.tv_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    if (WorkData.getTitale().equals("Add custom")) {
                        showAlertDialogButtonClicked(v);
                    } else {
                        if (type.equals("mobile")) {
                            bottomSheetDialog.cancel();
                            phone_txt.setText(holder.tv_item.getText().toString());
                            item.setLabel(holder.tv_item.getText().toString());
                            List<Contactdetail> s = addcontectModel.getContactdetails();
                            s.get(item_postion).setLabel(holder.tv_item.getText().toString());
                            addcontectModel.setContactdetails(s);
                            SessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);


                        } else if (type.equals("email")) {
                            bottomSheetDialog.cancel();
                            email_txt.setText(holder.tv_item.getText().toString());
                            item.setLabel(holder.tv_item.getText().toString());
                            List<Contactdetail> s = addcontectModel.getContactdetails();
                            s.get(item_postion).setLabel(holder.tv_item.getText().toString());
                            addcontectModel.setContactdetails(s);
                            SessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);

                        }

                    }

                }
            });

        }

        @Override
        public int getItemCount() {
            return worklist.size();
        }

        public void updateList(List<WorkTypeData> list) {
            worklist = list;
            notifyDataSetChanged();
        }

        public class InviteListDataclass extends RecyclerView.ViewHolder {
            TextView tv_item;

            public InviteListDataclass(@NonNull View itemView) {
                super(itemView);
                tv_item = itemView.findViewById(R.id.tv_item);
            }

        }

    }

    public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.InviteListDataclass> {


        public Activity mCtx;

        List<Contactdetail> contactdetails;
        LinearLayout layout_Add_phone;

        public PhoneAdapter(Activity context, List<Contactdetail> contactdetails, LinearLayout layout_Add_phone) {
            this.mCtx = context;
            this.contactdetails = contactdetails;
            this.layout_Add_phone = layout_Add_phone;
        }

        @NonNull
        @Override
        public InviteListDataclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.phone_add_layout, parent, false);
            return new InviteListDataclass(view);
        }

        @Override
        public void onBindViewHolder(@NonNull InviteListDataclass holder, int position) {
            Contactdetail item = contactdetails.get(position);
            Log.e("All Mobile Data ", new Gson().toJson(contactdetails));
            String flag = SessionManager.getContect_flag(getActivity());

            holder.select_label.setVisibility(View.VISIBLE);
            holder.contect_msg.setVisibility(View.GONE);
            holder.ccp_id.setVisibility(View.VISIBLE);
            holder.edt_mobile_no.setEnabled(true);
            if (edit) {

                holder.ccp_id.setCountryForNameCode(item.getCountry_code());
                if (contactdetails.get(position).getIs_default() == 1) {
                    holder.iv_set_default.setVisibility(View.VISIBLE);
                } else {
                    holder.iv_set_default.setVisibility(View.GONE);
                }
                holder.ccp_id.setDefaultCountryUsingNameCode(String.valueOf(Global.Countrycode(mCtx, item.getEmail_number())));
                holder.ccp_id.setDefaultCountryUsingPhoneCode(Global.Countrycode(mCtx, item.getEmail_number()));
                holder.ccp_id.resetToDefaultCountry();
                String main_data = item.getEmail_number().replace("+" + String.valueOf(Global.Countrycode(mCtx, item.getEmail_number())), "");
                holder.edt_mobile_no.setText(main_data);
                holder.phone_txt.setText(item.getLabel());
                holder.edt_mobile_no.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        if (edt_mobile_no.getText().toString().equals(""))
                        {

                        }
                       else {
                            item.setEmail_number(holder.ccp_id.getSelectedCountryCodeWithPlus() + s.toString());
                            item.setCountry_code(holder.ccp_id.getSelectedCountryNameCode());
                            String countryCode = holder.ccp_id.getSelectedCountryCodeWithPlus();
                            String phoneNumber = holder.edt_mobile_no.getText().toString().trim();
                            if (contactdetails.size() <= 12) {
                                layout_Add_phone.setVisibility(View.VISIBLE);
                                addcontectModel.setContactdetails(contactdetails);
                                SessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);
                            }
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
              /*  holder.edt_mobile_no.setOnKeyListener(new View.OnKeyListener() {
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                            try {

                                if (Global.isNetworkAvailable(getActivity(), mMainLayout)) {
                                    UpdateContect(contactdetails.get(position));
                                }
                                //break;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            return true;
                        }
                        return false;
                    }
                });

*/

                holder.select_label.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        addcontectModel.setContactdetails(contactdetails);
                        SessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);

                        showBottomSheetDialog_For_Home("mobile", holder.phone_txt, holder.phone_txt, item, position);
                    }
                });



                holder.ccp_id.setDefaultCountryUsingNameCode(String.valueOf(Global.Countrycode(getActivity(),
                        contactdetails.get(position).getEmail_number())));
                holder.ccp_id.setDefaultCountryUsingPhoneCode(Global.Countrycode(getActivity(),contactdetails.get(position).getEmail_number()));
                holder.ccp_id.resetToDefaultCountry();
                holder.ccp_id.registerCarrierNumberEditText(holder.edt_mobile_no);
            } else if (flag.equals("read")) {
                // EnableRuntimePermission();
                holder.layout_icon_call.setVisibility(View.GONE);
                holder.layout_icon_message.setVisibility(View.GONE);
                holder.select_label.setVisibility(View.GONE);
                holder.contect_msg.setVisibility(View.GONE);
                holder.layout_country_piker.setVisibility(View.GONE);
                holder.edt_mobile_no.setEnabled(false);
                holder.edt_mobile_no.setTextColor(getActivity().getResources().getColor(R.color.purple_200));
                holder.tv_phone.setText(holder.tv_phone.getText().toString() + "(" + item.getLabel() + ")");
                holder.ccp_id.setCountryForNameCode(item.getCountry_code());
                if (contactdetails.get(position).getIs_default() == 1) {
                    holder.iv_set_default.setVisibility(View.VISIBLE);
                } else {
                    holder.iv_set_default.setVisibility(View.GONE);
                }
                String getFirstletter = String.valueOf(item.getEmail_number().charAt(0));

                holder.edt_mobile_no.setText(item.getEmail_number());
                holder.phone_txt.setText(item.getLabel());
                holder.edt_mobile_no.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        if (edt_mobile_no.getText().toString().equals(""))
                        {

                        }
                        else {
                            item.setEmail_number(holder.ccp_id.getSelectedCountryCodeWithPlus() + s.toString());
                            item.setCountry_code(holder.ccp_id.getSelectedCountryNameCode());
                            String countryCode = holder.ccp_id.getSelectedCountryCodeWithPlus();
                            String phoneNumber = holder.edt_mobile_no.getText().toString().trim();
                            if (contactdetails.size() <= 12) {
                                layout_Add_phone.setVisibility(View.VISIBLE);
                                Log.e("Contect id ", String.valueOf(contactdetails.get(position).getId()));
                                addcontectModel.setContactdetails(contactdetails);
                                Log.e("Add Contect Model is ", new Gson().toJson(addcontectModel));
                                SessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);

                            }
                        }


                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });



                holder.select_label.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        showBottomSheetDialog_For_Home("mobile", holder.phone_txt, holder.phone_txt, item, position);
                    }
                });



            } else {


              if (item.getIs_default() == 1) {
                    holder.iv_set_default.setVisibility(View.VISIBLE);
                } else {
                    holder.iv_set_default.setVisibility(View.GONE);
                }
     /*           TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(getActivity().TELEPHONY_SERVICE);
                String country = tm.getNetworkCountryIso();
                int countryCode = 0;
                PhoneNumberUtil phoneUtil = PhoneNumberUtil.createInstance(getActivity());
                try {
                    // phone must begin with '+'
                    Phonenumber.PhoneNumber numberProto = phoneUtil.parse(item.getEmail_number(), country.toUpperCase());
                    countryCode = numberProto.getCountryCode();
                } catch (NumberParseException e) {
                    System.err.println("NumberParseException was thrown: " + e.toString());
                }
                String main_data = item.getEmail_number().replace("+" + countryCode, "");*/
                String main_data = item.getEmail_number().replace("+" + Global.Countrycode_Country(mCtx, item.getEmail_number()), "");

                holder.edt_mobile_no.setText(main_data);
                holder.phone_txt.setText(item.getLabel());


                holder.edt_mobile_no.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        if (holder.edt_mobile_no.getText().toString().equals(""))
                        {

                        }
                        else {
                            item.setEmail_number(holder.ccp_id.getSelectedCountryCodeWithPlus() + s.toString());
                            item.setCountry_code(holder.ccp_id.getSelectedCountryNameCode());
                            String countryCode = holder.ccp_id.getSelectedCountryCodeWithPlus();
                            String phoneNumber = holder.edt_mobile_no.getText().toString().trim();
                   /* if (countryCode.length() > 0 && phoneNumber.length() > 0) {
                        if (Global.isValidPhoneNumber(phoneNumber)) {
                            boolean status = validateUsing_libphonenumber(countryCode, phoneNumber);
                            if (status) {
                                iv_invalid.setText("");
                                return true;
                            } else {
                                iv_invalid.setText(getResources().getString(R.string.invalid_phone));
                            }
                        } else {
                            iv_invalid.setText(getResources().getString(R.string.invalid_phone));
                        }
                    }*/
                            if (contactdetails.size() <= 4) {
                                layout_Add_phone.setVisibility(View.VISIBLE);
                                addcontectModel.setContactdetails(contactdetails);
                                SessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);

                            }
                        }


                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                holder.select_label.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        showBottomSheetDialog_For_Home("mobile", holder.phone_txt, holder.phone_txt, item, position);
                    }
                });

                holder.ccp_id.setDefaultCountryUsingNameCode(String.valueOf(Global.Countrycode(getActivity(),
                        contactdetails.get(position).getEmail_number())));
                holder.ccp_id.setDefaultCountryUsingPhoneCode(Global.Countrycode(getActivity(),contactdetails.get(position).getEmail_number()));
                holder.ccp_id.resetToDefaultCountry();
                holder.ccp_id.registerCarrierNumberEditText(holder.edt_mobile_no);
            }

            holder.layout_icon_call.setVisibility(View.GONE);
            holder.layout_icon_message.setVisibility(View.GONE);



        }


        @Override
        public int getItemCount() {
            return contactdetails.size();
        }

        private boolean validateUsing_libphonenumber(String countryCode, String phNumber) {
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.createInstance(mCtx);
            String isoCode = phoneNumberUtil.getRegionCodeForCountryCode(Integer.parseInt(countryCode));
            Phonenumber.PhoneNumber phoneNumber = null;
            try {
                phoneNumber = phoneNumberUtil.parse(phNumber, isoCode);
            } catch (NumberParseException e) {
                System.err.println(e);
            }

            boolean isValid = phoneNumberUtil.isValidNumber(phoneNumber);
            if (isValid) {
                String internationalFormat = phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
                return true;
            } else {
                return false;
            }
        }

        public void removeItem(int pos, Contactdetail item) {
            contactdetails.remove(pos);
            notifyDataSetChanged();
            addcontectModel.setContactdetails(contactdetails);
            SessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);

/*
            if (edit) {
                try {
                    if (Global.isNetworkAvailable(getActivity(), mMainLayout)) {
                        RemoveContect(item.getId());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }*/
        }

        public void setdefault(int pos, Contactdetail item) {
            for (int i = 0; i < contactdetails.size(); i++) {
                if (item.getId() == contactdetails.get(i).getId()) {
                    contactdetails.get(i).setIs_default(1);
                    notifyDataSetChanged();
                } else {
                    contactdetails.get(i).setIs_default(0);
                    notifyDataSetChanged();
                }
            }
        }

        public class InviteListDataclass extends RecyclerView.ViewHolder {
            EditText edt_mobile_no;
            ImageView iv_set_default;
            LinearLayout layout_swap, select_label, contect_msg, layout_icon_call,
                    layout_icon_message, layout_country_piker;
            TextView phone_txt;
            CountryCodePicker ccp_id;
            TextView tv_phone;


            public InviteListDataclass(@NonNull View itemView) {
                super(itemView);

                edt_mobile_no = itemView.findViewById(R.id.edt_mobile_no);
                iv_set_default = itemView.findViewById(R.id.iv_set_default);
                layout_swap = itemView.findViewById(R.id.layout_swap);
                phone_txt = itemView.findViewById(R.id.phone_txt);
                ccp_id = itemView.findViewById(R.id.ccp_id);
                layout_country_piker = itemView.findViewById(R.id.layout_country_piker);
                select_label = itemView.findViewById(R.id.select_label);
                contect_msg = itemView.findViewById(R.id.contect_msg);
                layout_icon_call = itemView.findViewById(R.id.layout_icon_call);
                layout_icon_message = itemView.findViewById(R.id.layout_icon_message);
                tv_phone = itemView.findViewById(R.id.tv_phone);


            }

        }


    }

    public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.InviteListDataclass> {

        private final List<Contactdetail> contactdetails;
        public Context mCtx;
        LinearLayout layout_Add_email;

        public EmailAdapter(Context context, List<Contactdetail> contactdetails, LinearLayout layout_Add_email) {
            this.mCtx = context;
            this.contactdetails = contactdetails;
            this.layout_Add_email = layout_Add_email;
        }

        @NonNull
        @Override
        public InviteListDataclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.eamil_add_layout, parent, false);
            return new InviteListDataclass(view);
        }

        @Override
        public void onBindViewHolder(@NonNull InviteListDataclass holder, int position) {
            Contactdetail item = contactdetails.get(position);
            holder.edt_email.setText(item.getEmail_number());
            holder.email_txt.setText(item.getLabel());
            String flag = SessionManager.getContect_flag(getActivity());
            holder.select_email_label.setVisibility(View.VISIBLE);
            holder.layout_icon_email.setVisibility(View.GONE);
            holder.edt_email.setEnabled(true);


            if (edit) {

                holder.edt_email.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        Log.e("Action done", "Yes");
                        if (holder.edt_email.getText().toString().equals(""))
                        {

                        }
                        else {
                            if (Global.emailValidator(s.toString())) {
                                holder.iv_invalid.setVisibility(View.GONE);
                                item.setEmail_number(s.toString());
                                if (contactdetails.size() <= 4) {
                                    layout_Add_email.setVisibility(View.VISIBLE);
                                }
                                addcontectModel.setContactdetails_email(contactdetails);
                                //addcontectModel.setContactdetails(contactdetails);
                                SessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);
                            } else {
                                holder.iv_invalid.setVisibility(View.VISIBLE);
                            }
                        }



                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
               /* holder.edt_email.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int i, KeyEvent event) {
                        Log.e("Action done","Yes");
                        if (event.getAction() == KeyEvent.ACTION_DOWN) {
                            try {
                                if (Global.isNetworkAvailable(getActivity(), mMainLayout)) {
                                    UpdateContect(contactdetails.get(position));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return true;
                        }
                        return true;
                    }
                });
*/


                holder.select_email_label.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        showBottomSheetDialog_For_Home("email", holder.email_txt, holder.email_txt, item, position);
                    }
                });


            } else if (flag.equals("read")) {
               ;
                holder.select_email_label.setVisibility(View.GONE);
                holder.layout_icon_email.setVisibility(View.GONE);
                holder.edt_email.setEnabled(false);
                holder.edt_email.setTextColor(getActivity().getResources().getColor(R.color.purple_200));
                holder.tv_email.setText(holder.tv_email.getText().toString() + "(" + item.getLabel() + ")");
            } else {

                holder.edt_email.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (Global.emailValidator(s.toString())) {
                            holder.iv_invalid.setVisibility(View.GONE);
                            item.setEmail_number(s.toString());
                            if (contactdetails.size() <= 4) {
                                layout_Add_email.setVisibility(View.VISIBLE);
                            }
                            addcontectModel.setContactdetails_email(contactdetails);
                            //addcontectModel.setContactdetails(contactdetails);
                            SessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);
                        } else {
                            holder.iv_invalid.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                holder.select_email_label.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        showBottomSheetDialog_For_Home("email", holder.email_txt, holder.email_txt, item, position);
                    }
                });



            }


        }

        @Override
        public int getItemCount() {
            return contactdetails.size();
        }

        public void setdefault(int pos, Contactdetail item) {
            for (int i = 0; i < contactdetails.size(); i++) {
                if (item.getId() == contactdetails.get(i).getId()) {
                    contactdetails.get(i).setIs_default(1);
                    notifyDataSetChanged();
                    break;
                } else {
                    contactdetails.get(i).setIs_default(0);
                    notifyDataSetChanged();
                }
            }
        }

        public void removeItem(int pos, Contactdetail item) {

            contactdetails.remove(pos);
            notifyDataSetChanged();
            addcontectModel.setContactdetails_email(contactdetails);
            SessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);


            /*if (edit) {
                try {
                    if (Global.isNetworkAvailable(getActivity(), mMainLayout)) {
                        RemoveContect(item.getId());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }*/

        }


        public class InviteListDataclass extends RecyclerView.ViewHolder {
            EditText edt_email;
            TextView email_txt, iv_invalid, tv_email;
            LinearLayout select_email_label, layout_swap, layout_icon_email;

            public InviteListDataclass(@NonNull View itemView) {
                super(itemView);

                edt_email = itemView.findViewById(R.id.edt_email);
                email_txt = itemView.findViewById(R.id.email_txt);
                select_email_label = itemView.findViewById(R.id.select_email_label);
                iv_invalid = itemView.findViewById(R.id.iv_invalid);
                layout_swap = itemView.findViewById(R.id.layout_swap);
                layout_icon_email = itemView.findViewById(R.id.layout_icon_email);
                tv_email = itemView.findViewById(R.id.tv_email);

            }

        }

    }

    public class TimezoneAdapter extends RecyclerView.Adapter<TimezoneAdapter.InviteListDataclass> {

        public Context mCtx;
        TextView phone_txt;
        Contactdetail item;
        private List<TimezoneModel> timezoneModels;

        public TimezoneAdapter(Context context, List<TimezoneModel> timezoneModels) {
            this.mCtx = context;
            this.timezoneModels = timezoneModels;
        }

        @NonNull
        @Override
        public InviteListDataclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.work_type_selecte, parent, false);
            return new InviteListDataclass(view);
        }

        @Override
        public void onBindViewHolder(@NonNull InviteListDataclass holder, int position) {
            TimezoneModel WorkData = timezoneModels.get(position);
            holder.tv_item.setText(WorkData.getText());
            holder.tv_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    bottomSheetDialog_time.cancel();
                    zone_txt.setText(holder.tv_item.getText().toString());
                    addcontectModel.setTime(String.valueOf(WorkData.getValue()));
                    SessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);


                }
            });

        }

        @Override
        public int getItemCount() {
            return timezoneModels.size();
        }

        public void updateList(List<TimezoneModel> list) {
            timezoneModels = list;
            notifyDataSetChanged();
        }

        public class InviteListDataclass extends RecyclerView.ViewHolder {
            TextView tv_item;

            public InviteListDataclass(@NonNull View itemView) {
                super(itemView);
                tv_item = itemView.findViewById(R.id.tv_item);
            }

        }

    }


    public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.viewData> {
        private static final int VIEW_TYPE_LOADING = 0;
        private static final int VIEW_TYPE_NORMAL = 1;
        private boolean isLoaderVisible = false;
        public Context mCtx;
        TextView phone_txt;
        Contactdetail item;
        private List<CompanyModel.Company> companyList;

        public CompanyAdapter(Context context, List<CompanyModel.Company> companyList) {
            this.mCtx = context;
            this.companyList = companyList;
        }

        @NonNull
        @Override
        public CompanyAdapter.viewData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            switch (viewType) {
                case VIEW_TYPE_NORMAL:
                    return new CompanyAdapter.viewData(
                            LayoutInflater.from(parent.getContext()).inflate(R.layout.company_type_selecte, parent, false));
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
                return position == companyList.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
            } else {
                return VIEW_TYPE_NORMAL;
            }
        }

        public void addItems(List<CompanyModel.Company> postItems) {
            companyList.addAll(postItems);
            notifyDataSetChanged();
        }

        public void addLoading() {
            isLoaderVisible = true;
            companyList.add(new CompanyModel.Company());
            notifyItemInserted(companyList.size() - 1);
        }

        public void removeLoading() {
            isLoaderVisible = false;
            int position = companyList.size() - 1;
            CompanyModel.Company item = getItem(position);
            if (item != null) {
                companyList.remove(position);
                notifyItemRemoved(position);
            }
        }

        public void clear() {
            companyList.clear();
            notifyDataSetChanged();
        }


        CompanyModel.Company getItem(int position) {
            return companyList.get(position);
        }

        @Override
        public void onBindViewHolder(@NonNull CompanyAdapter.viewData holder, int position) {
            CompanyModel.Company WorkData = companyList.get(position);
            if (Global.IsNotNull(WorkData) && !WorkData.getName().equals("")) {
                holder.tv_item.setText(WorkData.getName());
                holder.tv_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        bottomSheetDialog_company.cancel();
                        ev_company.setText(holder.tv_item.getText().toString());
                        ev_company_url.setText(WorkData.getCompany_url());
                        //addcontectModel.setCompany(String.valueOf(WorkData.getName()));
                        addcontectModel.setCompany_id(String.valueOf(WorkData.getId()));
                        SessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);
                    }
                });
            }

        }

        @Override
        public int getItemCount() {
            return companyList.size();
        }

        public void updateList(List<CompanyModel.Company> list) {
            companyList = list;
            notifyDataSetChanged();
        }

        public class viewData extends RecyclerView.ViewHolder {
            TextView tv_item;

            public viewData(@NonNull View itemView) {
                super(itemView);
                tv_item = itemView.findViewById(R.id.tv_item);
            }
        }

        public class ProgressHolder extends CompanyAdapter.viewData {
            ProgressHolder(View itemView) {
                super(itemView);
            }

        }
    }


}