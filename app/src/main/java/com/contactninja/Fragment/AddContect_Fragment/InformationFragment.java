package com.contactninja.Fragment.AddContect_Fragment;

import static com.contactninja.Utils.PaginationListener.PAGE_START;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.telephony.SmsManager;
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

import com.contactninja.MainActivity;
import com.contactninja.Manual_email_text.Text_And_Email_Auto_Manual;
import com.contactninja.Model.AddcontectModel;
import com.contactninja.Model.CompanyModel;
import com.contactninja.Model.Contactdetail;
import com.contactninja.Model.ContectListData;
import com.contactninja.Model.TimezoneModel;
import com.contactninja.Model.UserData.SignResponseModel;
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

import org.json.JSONArray;
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

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle")
public class InformationFragment extends Fragment implements View.OnClickListener {
    private long mLastClickTime = 0;
    public int PhoneFieldNumber = 0;// total Phone add count
    public int emailFieldNumber = 0;// total email add count
    List<TimezoneModel> timezoneModels = new ArrayList<>();
    BottomSheetDialog bottomSheetDialog_time, bottomSheetDialog_company;
    EditText ev_address, ev_city, ev_zip, ev_zoom, ev_note,
            ev_company_url, ev_state, ev_job, ev_bob, ev_fb, ev_twitter, ev_breakout,
            ev_linkedin;
    LinearLayout select_state, add_mobile_Number,
            layout_Add_phone, layout_Add_email, layout_mobile, fb_layout;
    TextView tv_phone, tv_more_field, tv_company_url, tv_job,
            zone_txt, tv_add_social, ev_company;
    ImageView pulse_icon, pulse_icon1, img_fb, img_twitter, img_linkdin, img_breakout, image_list_show;
    String Name = "", job_titel = "";
    SessionManager sessionManager;
    AddcontectModel addcontectModel;
    BottomSheetDialog bottomSheetDialog;
    LinearLayout city_layout, zoom_layout, note_layout, company_url_layout, job_layout,
            layout_time_zone, select_label_zone, layout_bod, twitter_layout, breakout_layout,
            linkedin_layout, state_layout, time_layout, media_layout, media_link;
    String show = "0";
    String organization_id = "", team_id = "";
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
    CompanyAdapter companyAdapter;
    List<CompanyModel.Company> companyList = new ArrayList<>();
    int perPage = 20;
    Integer contact_Is_Block = 0;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_information, container, false);
        IntentUI(view);
        addcontectModel = new AddcontectModel();

        sessionManager = new SessionManager(getActivity());
        loadingDialog = new LoadingDialog(getActivity());
        retrofitCalls = new RetrofitCalls(getActivity());
        companyAdapter = new CompanyAdapter(getActivity(), new ArrayList<>());


        List<ContectListData.Contact> test_list = new ArrayList<>();
        test_list.add(SessionManager.getOneCotect_deatil(getActivity()));
        // Log.e("Size is", String.valueOf(test_list));
        ContectListData.Contact contact = SessionManager.getOneCotect_deatil(getActivity());
        contact_Is_Block = contact.getIs_blocked();


        String flag = sessionManager.getContect_flag(getActivity());
        Showlayout();
        if (flag.equals("edit")) {
            company_layout.setEnabled(true);
            image_list_show.setVisibility(View.VISIBLE);
            tv_add_social.setVisibility(View.GONE);
            media_link.setVisibility(View.GONE);
            ev_company_url.setEnabled(true);
            ev_job.setEnabled(true);
            ev_zoom.setEnabled(true);
            ev_address.setEnabled(true);
            ev_city.setEnabled(true);
            ev_state.setEnabled(true);
            ev_zip.setEnabled(true);
            ev_bob.setEnabled(true);
            ev_note.setEnabled(true);
            // Log.e("Null", "No Call");
            edit = true;
            iv_down.setVisibility(View.VISIBLE);
            ContectListData.Contact Contect_data = SessionManager.getOneCotect_deatil(getActivity());
            addcontectModel.setTime(String.valueOf(Contect_data.getTimezoneId()));
            addcontectModel.setJob_title(Contect_data.getJobTitle());
            addcontectModel.setState(Contect_data.getState());
            addcontectModel.setCity(Contect_data.getCity());
            addcontectModel.setCompany(Contect_data.getCompanyName());
            addcontectModel.setZoom_id(Contect_data.getZoomId());
            addcontectModel.setAddress(Contect_data.getAddress());
            addcontectModel.setZoom_id(Contect_data.getZipcode());
            addcontectModel.setCompany_url(Contect_data.getCompany_url());
            addcontectModel.setBreakoutu(Contect_data.getBreakout_link());
            addcontectModel.setLinkedin(Contect_data.getLinkedin_link());
            addcontectModel.setFacebook(Contect_data.getFacebook_link());
            addcontectModel.setBirthday(Contect_data.getDob());

            ev_zip.setText(Contect_data.getZipcode());
            ev_address.setText(Contect_data.getAddress());
            ev_zoom.setText(Contect_data.getZoomId());
            ev_company.setText(Contect_data.getCompanyName());
            ev_state.setText(Contect_data.getState());
            ev_city.setText(Contect_data.getCity());
            if (String.valueOf(Contect_data.getTimezoneId()).equals("null")) {
                String time_zone = TimeZone.getDefault().getID();
                zone_txt.setText(time_zone);
            } else {
                zone_txt.setText(String.valueOf(Contect_data.getTimezoneId()));
            }
            ev_job.setText(Contect_data.getJobTitle());
            contect_id = Contect_data.getId();
            organization_id = String.valueOf(Contect_data.getOrganizationId());
            team_id = String.valueOf(Contect_data.getTeamId());
            ev_company_url.setText(Contect_data.getCompany_url());
            ev_bob.setText(Contect_data.getDob());
            ev_twitter.setText(Contect_data.getTwitter_link());
            ev_fb.setText(Contect_data.getFacebook_link());
            ev_linkedin.setText(Contect_data.getLinkedin_link());
            ev_breakout.setText(Contect_data.getBreakout_link());
            TextSet();

            List<ContectListData.Contact.ContactDetail> detail_contect = Contect_data.getContactDetails();
            Log.e("Contect Detail is ", new Gson().toJson(detail_contect));
            for (int i = 0; i < detail_contect.size(); i++) {
                if (!detail_contect.get(i).getEmailNumber().trim().equalsIgnoreCase("")) {
                    if (detail_contect.get(i).getType().equals("EMAIL")) {
                        Contactdetail contactdetail = new Contactdetail();
                        contactdetail.setCountry_code(detail_contect.get(i).getCountryCode());
                        contactdetail.setType(detail_contect.get(i).getType());
                        contactdetail.setEmail_number(detail_contect.get(i).getEmailNumber());
                        contactdetail.setId(detail_contect.get(i).getId());
                        contactdetail.setLabel(detail_contect.get(i).getLabel());
                        contactdetail.setIs_default(detail_contect.get(i).getIsDefault());
                        emaildetails_list.add(contactdetail);
                        Collections.reverse(emaildetails_list);
                        contactdetails.add(contactdetail);

                        layout_Add_email.setVisibility(View.GONE);
                        emailAdapter = new EmailAdapter(getActivity(), emaildetails_list, layout_Add_email
                                , contact_Is_Block);
                        rv_email.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rv_email.setAdapter(emailAdapter);


                    } else {
                        Log.e("Label is ", String.valueOf(detail_contect.get(i).getIsDefault()));
                        Contactdetail contactdetail = new Contactdetail();
                        contactdetail.setCountry_code(detail_contect.get(i).getCountryCode());
                        contactdetail.setType(detail_contect.get(i).getType());
                        contactdetail.setEmail_number(detail_contect.get(i).getEmailNumber());
                        contactdetail.setId(detail_contect.get(i).getId());
                        contactdetail.setLabel(detail_contect.get(i).getLabel());
                        contactdetail.setIs_default(detail_contect.get(i).getIsDefault());

                        phonedetails_list.add(contactdetail);
                        Collections.reverse(phonedetails_list);
                        contactdetails.add(contactdetail);

                        phoneAdapter = new PhoneAdapter(getActivity(), phonedetails_list, layout_Add_phone, contact_Is_Block);
                        rv_phone.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rv_phone.setAdapter(phoneAdapter);


                    }
                }
            }

            EmailViewAdd();
            PhoneViewAdd();


        } else if (flag.equals("read")) {
            iv_down.setVisibility(View.GONE);

            company_layout.setEnabled(false);
            image_list_show.setVisibility(View.GONE);
            media_link.setVisibility(View.GONE);
            tv_add_social.setVisibility(View.VISIBLE);
            ev_company_url.setEnabled(false);
            ev_job.setEnabled(false);
            ev_zoom.setEnabled(false);
            ev_address.setEnabled(false);
            ev_city.setEnabled(false);
            ev_state.setEnabled(false);
            ev_zip.setEnabled(false);
            ev_bob.setEnabled(false);
            ev_note.setEnabled(false);
            tv_add_social.setVisibility(View.GONE);


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

            ContectListData.Contact Contect_data = SessionManager.getOneCotect_deatil(getActivity());
            Log.e("All Contect Data", new Gson().toJson(Contect_data));
            addcontectModel.setTime(String.valueOf(Contect_data.getTimezoneId()));
            addcontectModel.setJob_title(Contect_data.getJobTitle());
            addcontectModel.setState(Contect_data.getState());
            addcontectModel.setCity(Contect_data.getCity());
            addcontectModel.setCompany(Contect_data.getCompanyName());
            addcontectModel.setZoom_id(Contect_data.getZoomId());
            addcontectModel.setAddress(Contect_data.getAddress());
            addcontectModel.setZoom_id(Contect_data.getZipcode());
            addcontectModel.setCompany_url(Contect_data.getCompany_url());
            addcontectModel.setBreakoutu(Contect_data.getBreakout_link());
            addcontectModel.setLinkedin(Contect_data.getLinkedin_link());
            addcontectModel.setFacebook(Contect_data.getFacebook_link());
            addcontectModel.setBirthday(Contect_data.getDob());


            ev_zip.setText(Contect_data.getZipcode());
            ev_address.setText(Contect_data.getAddress());
            ev_zoom.setText(Contect_data.getZoomId());
            ev_company.setText(Contect_data.getCompanyName());
            ev_state.setText(Contect_data.getState());
            ev_city.setText(Contect_data.getCity());
            if (String.valueOf(Contect_data.getTimezoneId()).equals("null")) {
                String time_zone = TimeZone.getDefault().getID();
                zone_txt.setText(time_zone);
            } else {
                zone_txt.setText(String.valueOf(Contect_data.getTimezoneId()));
            }

            ev_job.setText(Contect_data.getJobTitle());
            contect_id = Contect_data.getId();
            organization_id = String.valueOf(Contect_data.getOrganizationId());
            team_id = String.valueOf(Contect_data.getTeamId());
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
            try {
                if (Contect_data.getLinkedin_link().equals("")) {
                    img_linkdin.setVisibility(View.GONE);
                } else {
                    img_linkdin.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                img_linkdin.setVisibility(View.GONE);

            }


            img_linkdin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    Uri uri = Uri.parse(Contect_data.getLinkedin_link()); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
            try {

                if (Contect_data.getBreakout_link().equals("")) {
                    img_breakout.setVisibility(View.GONE);
                } else {
                    img_breakout.setVisibility(View.VISIBLE);
                }

            } catch (Exception e) {
                img_breakout.setVisibility(View.GONE);

            }

            img_breakout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    Uri uri = Uri.parse(Contect_data.getBreakout_link()); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });


            List<ContectListData.Contact.ContactDetail> detail_contect = Contect_data.getContactDetails();
            Log.e("Contect Detail", new Gson().toJson(detail_contect));
            for (int i = 0; i < detail_contect.size(); i++) {
                if (!detail_contect.get(i).getEmailNumber().trim().equalsIgnoreCase("")) {
                    if (detail_contect.get(i).getType().equals("EMAIL")) {
                        Contactdetail contactdetail = new Contactdetail();
                        contactdetail.setCountry_code(detail_contect.get(i).getCountryCode());
                        contactdetail.setType(detail_contect.get(i).getType());
                        contactdetail.setEmail_number(detail_contect.get(i).getEmailNumber());
                        contactdetail.setId(detail_contect.get(i).getId());
                        contactdetail.setLabel(detail_contect.get(i).getLabel());
                        contactdetail.setIs_default(detail_contect.get(i).getIsDefault());
                        emaildetails_list.add(contactdetail);
                        Collections.reverse(emaildetails_list);
                        contactdetails.add(contactdetail);

                        layout_Add_email.setVisibility(View.GONE);
                        emailAdapter = new EmailAdapter(getActivity(), emaildetails_list, layout_Add_email
                                , contact_Is_Block);
                        rv_email.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rv_email.setAdapter(emailAdapter);

                    } else {
                        Log.e("Label is ", String.valueOf(detail_contect.get(i).getIsDefault()));
                        Contactdetail contactdetail = new Contactdetail();
                        contactdetail.setCountry_code(detail_contect.get(i).getCountryCode());
                        contactdetail.setType(detail_contect.get(i).getType());
                        contactdetail.setEmail_number(detail_contect.get(i).getEmailNumber());
                        contactdetail.setId(detail_contect.get(i).getId());
                        contactdetail.setLabel(detail_contect.get(i).getLabel());
                        contactdetail.setIs_default(detail_contect.get(i).getIsDefault());

                        phonedetails_list.add(contactdetail);
                        Collections.reverse(phonedetails_list);
                        contactdetails.add(contactdetail);

                        phoneAdapter = new PhoneAdapter(getActivity(), phonedetails_list, layout_Add_phone, contact_Is_Block);
                        rv_phone.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rv_phone.setAdapter(phoneAdapter);

                    }
                }

            }

        } else {
            iv_down.setVisibility(View.VISIBLE);
            PhoneViewAdd();
            EmailViewAdd();
            TextSet();
            media_link.setVisibility(View.GONE);
            tv_add_social.setVisibility(View.GONE);
        }
        try {
            Timezoneget();
        } catch (JSONException e) {
            e.printStackTrace();
        }


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
                note_layout.setVisibility(View.VISIBLE);
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

        return view;
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
        note_layout.setVisibility(View.VISIBLE);
    }

    private void EmailViewAdd() {

        String flag = sessionManager.getContect_flag(getActivity());
        if (flag.equals("edit")) {

            for (int i = 0; i < contactdetails.size(); i++) {
                if (contactdetails.get(i).getType().equals("EMAIL")) {
                    emailFieldNumber++;
                }
            }
            if (emailFieldNumber < 5) {
                layout_Add_email.setVisibility(View.VISIBLE);
            }
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
                emailAdapter = new EmailAdapter(getActivity(), emaildetails_list, layout_Add_email, contact_Is_Block);
                rv_email.setLayoutManager(new LinearLayoutManager(getActivity()));
                rv_email.setAdapter(emailAdapter);
                //     Log.e("layout_Add_email",new Gson().toJson(emaildetails_list));
                Log.e("Concet List size", String.valueOf(contactdetails.size()));
                Log.e("Email  List is ", new Gson().toJson(SessionManager.getAdd_Contect_Detail(getActivity())));
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
            emailAdapter = new EmailAdapter(getActivity(), emaildetails_list, layout_Add_email, contact_Is_Block);
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
                emailAdapter = new EmailAdapter(getActivity(), emaildetails_list, layout_Add_email, contact_Is_Block);
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

        String flag = sessionManager.getContect_flag(getActivity());
        if (flag.equals("edit")) {
            for (int i = 0; i < contactdetails.size(); i++) {
                if (contactdetails.get(i).getType().equals("NUMBER")) {
                    PhoneFieldNumber++;
                }
            }
            if (PhoneFieldNumber < 5) {
                layout_Add_phone.setVisibility(View.VISIBLE);
            }

            layout_Add_phone.setOnClickListener(v -> {
                Log.e("On Click", "Yes");
                Contactdetail contactdetail1 = new Contactdetail();
                //Defult id 0 Set Edit
                contactdetail1.setId(0);
                contactdetail1.setEmail_number("");
                contactdetail1.setIs_default(0);
                contactdetail1.setLabel("Home");
                contactdetail1.setType("NUMBER");
                phonedetails_list.add(contactdetail1);
                contactdetails.add(contactdetail1);
                phoneAdapter = new PhoneAdapter(getActivity(), phonedetails_list, layout_Add_phone, contact_Is_Block);
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


            phoneAdapter = new PhoneAdapter(getActivity(), phonedetails_list, layout_Add_phone, contact_Is_Block);
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
                phoneAdapter = new PhoneAdapter(getActivity(), phonedetails_list, layout_Add_phone, contact_Is_Block);
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

        ev_othre_company.addTextChangedListener(new TextWatcher() {
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


        /*ev_company_url.addTextChangedListener(new TextWatcher() {
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
        });*/
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
        image_list_show = view.findViewById(R.id.image_list_show);
        other_company_layout = view.findViewById(R.id.other_company_layout);
        ev_othre_company = view.findViewById(R.id.ev_othre_company);
        iv_down = view.findViewById(R.id.iv_down);
    }


    void showBottomSheetDialog_For_Home(String moble, TextView phone_txt, TextView email_txt, Contactdetail item) {
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
        WorkAdapter workAdapter = new WorkAdapter(getActivity(), workTypeData, moble, phone_txt, email_txt, item);
        home_type_list.setAdapter(workAdapter);

        bottomSheetDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {


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

        String token = Global.getToken(sessionManager);
        JSONObject obj = new JSONObject();
        JSONObject paramObject = new JSONObject();
        paramObject.put("organization_id", 1);
        paramObject.put("team_id", 1);
        paramObject.put("user_id", user_data.getUser().getId());
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

        String token = Global.getToken(sessionManager);
        JSONObject obj = new JSONObject();
        JSONObject paramObject = new JSONObject();
        paramObject.put("organization_id", 1);
        paramObject.put("team_id", 1);
        paramObject.put("user_id", user_data.getUser().getId());
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

    public void EnableRuntimePermission(Contactdetail item) {

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + item.getEmail_number()));
                getActivity().startActivity(intent);

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {

             //   EnableRuntimePermission();
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

    public void showAlertDialogButtonClicked1(String p_num, int id, String type) {

        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(getActivity(), R.style.BottomSheetDialog);
        final View customLayout
                = getLayoutInflater()
                .inflate(
                        R.layout.custom_message_send,
                        null);
        builder.setView(customLayout);
        EditText editText = customLayout.findViewById(R.id.editText);
        TextView tv_cancel = customLayout.findViewById(R.id.tv_cancel);
        TextView tv_add = customLayout.findViewById(R.id.tv_add);
        TextView tv_phone_a = customLayout.findViewById(R.id.tv_phone);
        tv_phone_a.setText("Mobile Number " + p_num);


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
                if (type.equals("sim")) {
                    sendSMS(p_num, editText.getText().toString());
                    dialog.dismiss();
                } else {
                    try {
                        SMSAPI(editText.getText().toString(), id, p_num);
                        dialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


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

    public void showAlertDialogMeassge(String p_num, int id) {

        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(getActivity(), R.style.BottomSheetDialog);
        final View customLayout
                = getLayoutInflater()
                .inflate(
                        R.layout.select_msg_send_type,
                        null);
        builder.setView(customLayout);
        TextView tv_munual = customLayout.findViewById(R.id.tv_munual);
        TextView tv_api = customLayout.findViewById(R.id.tv_api);


        AlertDialog dialog
                = builder.create();
        dialog.show();
        tv_munual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                showAlertDialogButtonClicked1(p_num, id, "sim");
                dialog.dismiss();
            }
        });
        tv_api.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                dialog.dismiss();
                SessionManager.setMessage_number(p_num);
                SessionManager.setMessage_type("app");
                SessionManager.setMessage_id(String.valueOf(id));
                SessionManager.setEmail_screen_name("only_sms");
                SessionManager.setCampaign_type("");
                SessionManager.setCampaign_type_name("");
                SessionManager.setCampaign_Day("");
                SessionManager.setCampaign_minute("");
                Intent intent1 = new Intent(getActivity(), Text_And_Email_Auto_Manual.class);
                intent1.putExtra("flag", "edit");
                intent1.putExtra("type", "SMS");
                startActivity(intent1); //  finish();


                /*   Intent intent=new Intent(getActivity(), Message_Activity.class);
           intent.putExtra("number",p_num);
           intent.putExtra("id",id);
           intent.putExtra("type","app");
           startActivity(intent);*/

                //  showAlertDialogButtonClicked1(p_num, id, "app");


            }
        });

    }

    private void SMSAPI(String text, int id, String email) throws JSONException {

        Log.e("Phone Number", email);
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());

        JSONObject obj = new JSONObject();

        JSONObject paramObject = new JSONObject();

        paramObject.put("type", "SMS");
        paramObject.put("team_id", 1);
        paramObject.put("organization_id", 1);
        paramObject.put("user_id", user_data.getUser().getId());
        paramObject.put("manage_by", "MANUAL");
        paramObject.put("time", Global.getCurrentTime());
        paramObject.put("date", Global.getCurrentDate());
        paramObject.put("assign_to", user_data.getUser().getId());
        paramObject.put("task_description", text);

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < 1; i++) {
            JSONObject paramObject1 = new JSONObject();
            paramObject1.put("prospect_id", id);
            paramObject1.put("mobile", email);
           /* JSONArray contect_array = new JSONArray();
            contect_array.put(email);
            paramObject1.put("email_mobile", contect_array);*/
            jsonArray.put(paramObject1);
            break;
        }
        JSONArray contact_group_ids = new JSONArray();
        contact_group_ids.put("");
        paramObject.put("contact_group_ids", contact_group_ids);
        paramObject.put("prospect_id", jsonArray);

        obj.put("data", paramObject);

        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        Log.e("Gson Data is", new Gson().toJson(gsonObject));


        retrofitCalls.manual_task_store(sessionManager, gsonObject, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                if (response.body().getHttp_status() == 200) {
                    loadingDialog.cancelLoading();
                    String jsonRawData = new Gson().toJson(response.body());

                    try {

                        JSONObject jsonObject = new JSONObject(jsonRawData);
                        JSONObject jsonDailyObject = jsonObject.getJSONObject("data");
                        JSONObject jsonDailyObject1 = jsonDailyObject.getJSONObject("0");
                        String _newid = jsonDailyObject1.getString("id");
                        Log.e("_newid", _newid);
                        SMS_execute(text, id, email, _newid);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    loadingDialog.cancelLoading();
                }


            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });
    }

    private void SMS_execute(String text, int id, String email, String record_id) throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        JsonObject obj = new JsonObject();

        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("content_body", text);
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("user_id", user_data.getUser().getId());
        paramObject.addProperty("prospect_id", id);
        paramObject.addProperty("record_id", record_id);
        paramObject.addProperty("type", "SMS");
        paramObject.addProperty("team_id", 1);
        obj.add("data", paramObject);

        retrofitCalls.Email_execute(sessionManager, obj, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });
    }


    private void EmailAPI(String subject, String text, int id, String email) throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        JSONObject obj = new JSONObject();

        JSONObject paramObject = new JSONObject();

        paramObject.put("type", "EMAIL");
        paramObject.put("team_id", 1);
        paramObject.put("organization_id", 1);
        paramObject.put("user_id", user_data.getUser().getId());
        paramObject.put("manage_by", "MANUAL");
        paramObject.put("time", Global.getCurrentTime());
        paramObject.put("date", Global.getCurrentDate());
        paramObject.put("assign_to", user_data.getUser().getId());
        paramObject.put("task_description", text);

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < 1; i++) {
            JSONObject paramObject1 = new JSONObject();
            paramObject1.put("prospect_id", id);
            paramObject1.put("email", email);
          /*  JSONArray contect_array = new JSONArray();
            contect_array.put(email);
            paramObject1.put("email_mobile", contect_array);*/
            jsonArray.put(paramObject1);
            break;
        }
        JSONArray contact_group_ids = new JSONArray();
        contact_group_ids.put("");
        paramObject.put("contact_group_ids", contact_group_ids);
        paramObject.put("prospect_id", jsonArray);

        obj.put("data", paramObject);

        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        Log.e("Gson Data is", new Gson().toJson(gsonObject));


        retrofitCalls.manual_task_store(sessionManager, gsonObject, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                if (response.body().getHttp_status() == 200) {
                    loadingDialog.cancelLoading();
                    String jsonRawData = new Gson().toJson(response.body());

                    try {

                        JSONObject jsonObject = new JSONObject(jsonRawData);
                        JSONObject jsonDailyObject = jsonObject.getJSONObject("data");
                        JSONObject jsonDailyObject1 = jsonDailyObject.getJSONObject("0");
                        String _newid = jsonDailyObject1.getString("id");
                        Log.e("_newid", _newid);
                        Email_execute(subject, text, id, email, _newid);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    loadingDialog.cancelLoading();
                }


            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });
    }

    private void Email_execute(String subject, String text, int id, String email, String record_id) throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        JsonObject obj = new JsonObject();

        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("content_body", text);
        paramObject.addProperty("content_header", subject);
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("user_id", user_data.getUser().getId());
        paramObject.addProperty("prospect_id", id);
        paramObject.addProperty("record_id", record_id);
        paramObject.addProperty("type", "EMAIL");
        paramObject.addProperty("team_id", 1);
        obj.add("data", paramObject);

        retrofitCalls.Email_execute(sessionManager, obj, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });
    }

    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.getMessage(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void showAlertDialogEmailMeassge(String email, int id) {

        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(getActivity(), R.style.BottomSheetDialog);
        final View customLayout
                = getLayoutInflater()
                .inflate(
                        R.layout.email_send_layout,
                        null);
        builder.setView(customLayout);
        EditText ev_subject = customLayout.findViewById(R.id.ev_subject);
        EditText ev_type = customLayout.findViewById(R.id.ev_type);
        TextView tv_cancel = customLayout.findViewById(R.id.tv_cancel);
        TextView tv_add = customLayout.findViewById(R.id.tv_add);


        AlertDialog dialog
                = builder.create();
        dialog.show();
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
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                dialog.dismiss();
                try {
                    EmailAPI(ev_subject.getText().toString(), ev_type.getText().toString(), id, email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void Timezoneget() throws JSONException {

        //loadingDialog.showLoadingDialog();

        SignResponseModel user_data = SessionManager.getGetUserdata(getContext());
        JsonObject obj = new JsonObject();

        JsonObject paramObject = new JsonObject();

        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("user_id", user_data.getUser().getId());
        paramObject.addProperty("team_id", 1);
        obj.add("data", paramObject);


        retrofitCalls.Timezone_list(sessionManager, obj, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                if (response.body().getHttp_status() == 200) {

                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<List<TimezoneModel>>() {
                    }.getType();
                    List<TimezoneModel> timezoon = new Gson().fromJson(headerString, listType);
                    timezoneModels.addAll(timezoon);
                    loadingDialog.cancelLoading();
                    for (int i = 0; i < timezoon.size(); i++) {
                        if (zone_txt.getText().toString().equals(timezoon.get(i).getValue().toString())) {
                            zone_txt.setText(timezoon.get(i).getText());
                            Log.e("No Same Data", "NO");
                        } else {
                            Log.e("No Same Data", "Yes");
                        }
                    }
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
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void CompanyList() throws JSONException {

        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());

        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", user_data.getUser().getId());
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

        public WorkAdapter(Context context, List<WorkTypeData> worklist, String type, TextView phone_txt, TextView email_txt, Contactdetail item) {
            this.mCtx = context;
            this.worklist = worklist;
            this.type = type;
            this.phone_txt = phone_txt;
            this.email_txt = email_txt;
            this.item = item;
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
                            addcontectModel.setContactdetails(contactdetails);
                            SessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);


                        } else if (type.equals("email")) {
                            bottomSheetDialog.cancel();
                            email_txt.setText(holder.tv_item.getText().toString());
                            item.setLabel(holder.tv_item.getText().toString());
                            addcontectModel.setContactdetails_email(contactdetails);
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
        Integer Is_blocked;

        public PhoneAdapter(Activity context, List<Contactdetail> contactdetails, LinearLayout layout_Add_phone, Integer Is_blocked) {
            this.mCtx = context;
            this.contactdetails = contactdetails;
            this.layout_Add_phone = layout_Add_phone;
            this.Is_blocked = Is_blocked;
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
            String flag = sessionManager.getContect_flag(getActivity());
            if (Is_blocked != 1) {
                holder.layout_icon_message.setVisibility(View.VISIBLE);
            } else {
                holder.layout_icon_message.setVisibility(View.GONE);
            }
            holder.select_label.setVisibility(View.VISIBLE);
            holder.contect_msg.setVisibility(View.GONE);
            holder.ccp_id.setVisibility(View.VISIBLE);
            holder.edt_mobile_no.setEnabled(true);
            if (edit) {
                holder.swipe_layout.setLeftSwipeEnabled(true);
                holder.swipe_layout.setRightSwipeEnabled(true);
                if (contactdetails.get(position).getIs_default() == 1) {
                    holder.iv_set_default.setVisibility(View.VISIBLE);
                } else {
                    holder.iv_set_default.setVisibility(View.GONE);
                }



                holder.ccp_id.setDefaultCountryUsingNameCode(String.valueOf(Global.Countrycode(mCtx,item.getEmail_number())));
                holder.ccp_id.setDefaultCountryUsingPhoneCode(Global.Countrycode(mCtx,item.getEmail_number()));
                holder.ccp_id.resetToDefaultCountry();
                String main_data = item.getEmail_number().replace("+" + String.valueOf(Global.Countrycode(mCtx,item.getEmail_number())), "");
                holder.edt_mobile_no.setText(main_data);
                holder.phone_txt.setText(item.getLabel());


                holder.edt_mobile_no.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        item.setEmail_number(holder.ccp_id.getSelectedCountryCodeWithPlus() + s.toString());
                        item.setCountry_code(holder.ccp_id.getSelectedCountryNameCode());
                        String countryCode = holder.ccp_id.getSelectedCountryCodeWithPlus();
                        String phoneNumber = holder.edt_mobile_no.getText().toString().trim();


                        addcontectModel.setContactdetails(contactdetails);
                        SessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);

                        if (PhoneFieldNumber < 5) {
                            layout_Add_phone.setVisibility(View.VISIBLE);
                        } else {
                            layout_Add_phone.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });


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
                        Log.e("Swipe Call", "MOveto right1");

                    }

                    @Override
                    public void onLeftStickyEdge(SwipeLayout swipeLayout, boolean moveToRight) {
                        Log.e("Swipe Call", "Left");
                    }

                    @Override
                    public void onRightStickyEdge(SwipeLayout swipeLayout, boolean moveToRight) {
                        Log.e("Swipe Call", "Right");

                    }
                });
                holder.select_label.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        item.setEmail_number(holder.ccp_id.getSelectedCountryCodeWithPlus() + holder.edt_mobile_no.getText().toString());
                        item.setCountry_code(holder.ccp_id.getSelectedCountryNameCode());
                        String countryCode = holder.ccp_id.getSelectedCountryCodeWithPlus();
                        String phoneNumber = holder.edt_mobile_no.getText().toString().trim();


                        showBottomSheetDialog_For_Home("mobile", holder.phone_txt, holder.phone_txt, item);

                    }
                });
                holder.layout_defult.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        holder.layout_swap.setVisibility(View.GONE);
                        for (int i = 0; i < contactdetails.size(); i++) {
                            if (item.getId() == contactdetails.get(i).getId()) {
                                contactdetails.get(i).setIs_default(1);
                            } else {
                                contactdetails.get(i).setIs_default(0);
                            }
                            notifyDataSetChanged();
                        }
                    }
                });

                holder.layout_remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        holder.layout_swap.setVisibility(View.GONE);
                        contactdetails.remove(position);
                        notifyDataSetChanged();

                        if (edit) {
                            try {
                                if (Global.isNetworkAvailable(getActivity(), mMainLayout)) {
                                    RemoveContect(item.getId());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });


            } else if (flag.equals("read")) {
             //   EnableRuntimePermission();
                holder.swipe_layout.setLeftSwipeEnabled(false);
                holder.swipe_layout.setRightSwipeEnabled(false);
                holder.select_label.setVisibility(View.GONE);
                holder.contect_msg.setVisibility(View.VISIBLE);
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

                        item.setEmail_number(holder.ccp_id.getSelectedCountryCodeWithPlus() + s.toString());
                        item.setCountry_code(holder.ccp_id.getSelectedCountryNameCode());
                        String countryCode = holder.ccp_id.getSelectedCountryCodeWithPlus();
                        String phoneNumber = holder.edt_mobile_no.getText().toString().trim();

                        Log.e("Contect id ", String.valueOf(contactdetails.get(position).getId()));
                        addcontectModel.setContactdetails(contactdetails);
                        Log.e("Add Contect Model is ", new Gson().toJson(addcontectModel));
                        SessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);

                        if (PhoneFieldNumber < 5) {
                            layout_Add_phone.setVisibility(View.VISIBLE);
                        } else {
                            layout_Add_phone.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                holder.edt_mobile_no.setOnKeyListener(new View.OnKeyListener() {
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                             /*try {
                               if (Global.isNetworkAvailable(getActivity(), mMainLayout)) {
                                    //UpdateContect(contactdetails.get(position));
                                }
                                //break;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
*/

                            return true;
                        }
                        return false;
                    }
                });


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
                holder.select_label.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        showBottomSheetDialog_For_Home("mobile", holder.phone_txt, holder.phone_txt, item);
                    }
                });
                holder.layout_defult.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        holder.layout_swap.setVisibility(View.GONE);
                        for (int i = 0; i < contactdetails.size(); i++) {
                            if (item.getId() == contactdetails.get(i).getId()) {
                                contactdetails.get(i).setIs_default(1);
                            } else {
                                contactdetails.get(i).setIs_default(0);
                            }
                            notifyDataSetChanged();
                        }
                    }
                });

                holder.layout_remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        holder.layout_swap.setVisibility(View.GONE);
                        contactdetails.remove(position);
                        notifyDataSetChanged();

                        if (edit) {
                            try {
                                if (Global.isNetworkAvailable(getActivity(), mMainLayout)) {
                                    RemoveContect(item.getId());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });


            } else {

                holder.swipe_layout.setLeftSwipeEnabled(true);
                holder.swipe_layout.setRightSwipeEnabled(true);
                if (item.getIs_default() == 1) {
                    holder.iv_set_default.setVisibility(View.VISIBLE);
                } else {
                    holder.iv_set_default.setVisibility(View.GONE);
                }
             /*   TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(getActivity().TELEPHONY_SERVICE);
                String country = tm.getNetworkCountryIso();
                int countryCode = 0;
                PhoneNumberUtil phoneUtil = PhoneNumberUtil.createInstance(getActivity());
                try {
                    // phone must begin with '+'
                    Phonenumber.PhoneNumber numberProto = phoneUtil.parse(item.getEmail_number(), country.toUpperCase());
                    countryCode = numberProto.getCountryCode();
                } catch (NumberParseException e) {
                    System.err.println("NumberParseException was thrown: " + e.toString());
                }*/
                String main_data = item.getEmail_number().replace("+" +Global.Countrycode_Country(mCtx,item.getEmail_number()), "");

                holder.edt_mobile_no.setText(main_data);
                holder.phone_txt.setText(item.getLabel());


                holder.edt_mobile_no.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        item.setEmail_number(holder.ccp_id.getSelectedCountryCodeWithPlus() + s.toString());
                        item.setCountry_code(holder.ccp_id.getSelectedCountryNameCode());
                        String countryCode = holder.ccp_id.getSelectedCountryCodeWithPlus();
                        String phoneNumber = holder.edt_mobile_no.getText().toString().trim();
                        if (contactdetails.size() <= 4) {
                            layout_Add_phone.setVisibility(View.VISIBLE);
                            addcontectModel.setContactdetails(contactdetails);
                            SessionManager.setAdd_Contect_Detail(getActivity(), addcontectModel);

                        }

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
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
                holder.select_label.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        showBottomSheetDialog_For_Home("mobile", holder.phone_txt, holder.phone_txt, item);
                    }
                });
                holder.layout_defult.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        holder.layout_swap.setVisibility(View.GONE);
                        for (int i = 0; i < contactdetails.size(); i++) {
                            if (item.getId() == contactdetails.get(i).getId()) {
                                contactdetails.get(i).setIs_default(1);
                            } else {
                                contactdetails.get(i).setIs_default(0);
                            }
                            notifyDataSetChanged();
                        }
                    }
                });

                holder.layout_remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        holder.layout_swap.setVisibility(View.GONE);
                        contactdetails.remove(position);
                        notifyDataSetChanged();

                        if (edit) {
                            try {
                                if (Global.isNetworkAvailable(getActivity(), mMainLayout)) {
                                    RemoveContect(item.getId());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }

            holder.layout_icon_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                   EnableRuntimePermission(item);
                }
            });


            holder.layout_icon_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    Log.e("Sms Event Call", "Yes");
                    Log.e("Number is", item.getEmail_number());
                    //showAlertDialogButtonClicked1(item.getEmail_number());
                    //Toast.makeText(getActivity(),String.valueOf(item.getId()),Toast.LENGTH_LONG).show();
                    showAlertDialogMeassge(item.getEmail_number(), item.getId());
                }
            });
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

        public class InviteListDataclass extends RecyclerView.ViewHolder {
            EditText edt_mobile_no;
            ImageView iv_set_default;
            SwipeLayout swipe_layout;
            LinearLayout layout_swap, select_label, layout_defult, layout_remove, contect_msg, layout_icon_call,
                    layout_icon_message, layout_country_piker;
            TextView phone_txt;
            CountryCodePicker ccp_id;
            TextView tv_phone;


            public InviteListDataclass(@NonNull View itemView) {
                super(itemView);

                edt_mobile_no = itemView.findViewById(R.id.edt_mobile_no);
                iv_set_default = itemView.findViewById(R.id.iv_set_default);
                swipe_layout = itemView.findViewById(R.id.swipe_layout);
                layout_swap = itemView.findViewById(R.id.layout_swap);
                layout_defult = itemView.findViewById(R.id.layout_defult);
                layout_remove = itemView.findViewById(R.id.layout_remove);
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
        Integer Is_blocked;

        public EmailAdapter(Context context, List<Contactdetail> contactdetails, LinearLayout layout_Add_email, Integer Is_blocked) {
            this.mCtx = context;
            this.contactdetails = contactdetails;
            this.layout_Add_email = layout_Add_email;
            this.Is_blocked = Is_blocked;
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
            String flag = sessionManager.getContect_flag(getActivity());
            holder.select_email_label.setVisibility(View.VISIBLE);
            holder.layout_icon_email.setVisibility(View.GONE);
            holder.edt_email.setEnabled(true);



            if (edit) {
                holder.swipe_layout.setLeftSwipeEnabled(true);
                holder.swipe_layout.setRightSwipeEnabled(true);

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
                        showBottomSheetDialog_For_Home("email", holder.email_txt, holder.email_txt, item);
                    }
                });

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

                holder.layout_defult.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        holder.layout_swap.setVisibility(View.GONE);
                        for (int i = 0; i < contactdetails.size(); i++) {
                            if (item.getId() == contactdetails.get(i).getId()) {
                                contactdetails.get(i).setIs_default(1);
                            } else {
                                contactdetails.get(i).setIs_default(0);
                            }
                            notifyDataSetChanged();
                        }
                    }
                });

                holder.layout_remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        holder.layout_swap.setVisibility(View.GONE);
                        contactdetails.remove(position);
                        notifyDataSetChanged();

                        if (edit) {
                            try {
                                if (Global.isNetworkAvailable(getActivity(), mMainLayout)) {
                                    RemoveContect(item.getId());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });


            } else if (flag.equals("read")) {

                if (Is_blocked != 1) {
                    holder.layout_icon_email.setVisibility(View.VISIBLE);
                } else {
                    holder.layout_icon_email.setVisibility(View.GONE);
                }

                holder.swipe_layout.setLeftSwipeEnabled(false);
                holder.swipe_layout.setRightSwipeEnabled(false);
                holder.select_email_label.setVisibility(View.GONE);
                holder.edt_email.setEnabled(false);
                holder.edt_email.setTextColor(getActivity().getResources().getColor(R.color.purple_200));
                holder.tv_email.setText(holder.tv_email.getText().toString() + "(" + item.getLabel() + ")");
            } else {


                holder.swipe_layout.setLeftSwipeEnabled(true);
                holder.swipe_layout.setRightSwipeEnabled(true);
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
                        showBottomSheetDialog_For_Home("email", holder.email_txt, holder.email_txt, item);
                    }
                });

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

                holder.layout_defult.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        holder.layout_swap.setVisibility(View.GONE);
                        for (int i = 0; i < contactdetails.size(); i++) {
                            if (item.getId() == contactdetails.get(i).getId()) {
                                contactdetails.get(i).setIs_default(1);
                            } else {
                                contactdetails.get(i).setIs_default(0);
                            }
                            notifyDataSetChanged();
                        }
                    }
                });

                holder.layout_remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        holder.layout_swap.setVisibility(View.GONE);
                        contactdetails.remove(position);
                        notifyDataSetChanged();

                        if (edit) {
                            try {
                                if (Global.isNetworkAvailable(getActivity(), mMainLayout)) {
                                    RemoveContect(item.getId());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });


            }


            holder.layout_icon_email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    SessionManager.setMessage_number(item.getEmail_number());
                    SessionManager.setMessage_id(String.valueOf(item.getId()));


                    SessionManager.setEmail_screen_name("only_email");
                    SessionManager.setCampaign_type("");
                    SessionManager.setCampaign_type_name("");
                    SessionManager.setCampaign_Day("");
                    SessionManager.setCampaign_minute("");
                    Intent intent1 = new Intent(getActivity(), Text_And_Email_Auto_Manual.class);
                    intent1.putExtra("flag", "edit");
                    intent1.putExtra("type", "EMAIL");
                    startActivity(intent1);


                  /*  Intent intent = new Intent(getActivity(), EmailSend_Activity.class);
                    intent.putExtra("email", item.getEmail_number());
                    intent.putExtra("id", String.valueOf(item.getId()));
                    startActivity(intent);*/
                    //  showAlertDialogEmailMeassge(item.getEmail_number(), item.getId());
                }
            });


        }

        @Override
        public int getItemCount() {
            return contactdetails.size();
        }


        public class InviteListDataclass extends RecyclerView.ViewHolder {
            EditText edt_email;
            TextView email_txt, iv_invalid, tv_email;
            LinearLayout select_email_label, layout_defult, layout_remove, layout_swap, layout_icon_email;
            SwipeLayout swipe_layout;

            public InviteListDataclass(@NonNull View itemView) {
                super(itemView);

                edt_email = itemView.findViewById(R.id.edt_email);
                email_txt = itemView.findViewById(R.id.email_txt);
                select_email_label = itemView.findViewById(R.id.select_email_label);
                iv_invalid = itemView.findViewById(R.id.iv_invalid);
                swipe_layout = itemView.findViewById(R.id.swipe_layout);
                layout_swap = itemView.findViewById(R.id.layout_swap);
                layout_defult = itemView.findViewById(R.id.layout_defult);
                layout_remove = itemView.findViewById(R.id.layout_remove);
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
        public Context mCtx;
        TextView phone_txt;
        Contactdetail item;
        private boolean isLoaderVisible = false;
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
        public void onBindViewHolder(@NonNull viewData holder, int position) {
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

        public class ProgressHolder extends viewData {
            ProgressHolder(View itemView) {
                super(itemView);
            }

        }
    }
}