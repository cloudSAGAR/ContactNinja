package com.contactninja.Bzcard.CreateBzcard.Fragment;

import static com.contactninja.Utils.PaginationListener.PAGE_START;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.contactninja.MainActivity;
import com.contactninja.Model.BZcardListModel;
import com.contactninja.Model.CompanyModel;
import com.contactninja.Model.Contactdetail;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.PaginationListener;
import com.contactninja.Utils.SessionManager;
import com.contactninja.aws.image_aws.S3Uploader;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hbb20.CountryCodePicker;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle")
public class Information_Bzcard_Fragment extends Fragment implements View.OnClickListener {
    
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    EditText ev_first, ev_last_name, edt_mobile_no, ev_email,
            ev_company, ev_company_url, ev_job, ev_address, ev_zip;
    CountryCodePicker ccp_id;
    TextView iv_invalid, iv_invalid1, tv_reupload, tv_image_size;
    LinearLayout company_layout;
    BottomSheetDialog bottomSheetDialog_company;
    CompanyAdapter companyAdapter;
    List<CompanyModel.Company> companyList = new ArrayList<>();
    int perPage = 20;
    LoadingDialog loadingDialog;
    RetrofitCalls retrofitCalls;
    SessionManager sessionManager;
    ImageView iv_company_dummy, iv_company_icon;
    int image_flag = 1;
    Uri mCapturedImageURI;
    Integer CAPTURE_IMAGE = 3;
    String logo_filePath = "";
    BZcardListModel.Bizcard bzcard_model;
    String urlFromS3 = null;
    S3Uploader s3uploaderObj;
    private long mLastClickTime = 0;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    
    public static boolean checkAndRequestPermissions(final Activity context) {
        int WExtstorePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded
                                                               .toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information_bzcard_, container, false);
        IntentUI(view);
        s3uploaderObj = new S3Uploader(getActivity());
        sessionManager = new SessionManager(getActivity());
        loadingDialog = new LoadingDialog(getActivity());
        retrofitCalls = new RetrofitCalls(getActivity());
        companyAdapter = new CompanyAdapter(getActivity(), new ArrayList<>());
        enterPhoneNumber();
        bzcard_model = SessionManager.getBzcard(getActivity());
        setData();
        onAddTextChnageListnerCall();
        return view;
        
    }
    
    private void onAddTextChnageListnerCall() {
        ev_first.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            
            }
            
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bzcard_model.getBzcardFieldsModel().setFirst_name(ev_first.getText().toString().trim());
                SessionManager.setBzcard(getActivity(), bzcard_model);
                
            }
            
            
            @Override
            public void afterTextChanged(Editable editable) {
            
            }
        });
        ev_company.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            
            }
            
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bzcard_model.getBzcardFieldsModel().setCompany_name(ev_company.getText().toString().trim());
                SessionManager.setBzcard(getActivity(), bzcard_model);
            }
            
            @Override
            public void afterTextChanged(Editable editable) {
            
            }
        });
        ev_last_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            
            }
            
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                
                bzcard_model.getBzcardFieldsModel().setLast_name(ev_last_name.getText().toString().trim());
                SessionManager.setBzcard(getActivity(), bzcard_model);
            }
            
            @Override
            public void afterTextChanged(Editable editable) {
            
            }
        });
        edt_mobile_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            
            }
            
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bzcard_model.getBzcardFieldsModel().setContant_number(edt_mobile_no.getText().toString().trim());
                SessionManager.setBzcard(getActivity(), bzcard_model);
            }
            
            @Override
            public void afterTextChanged(Editable editable) {
            
            }
        });
        ev_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            
            }
            
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bzcard_model.getBzcardFieldsModel().setEmail(ev_email.getText().toString().trim());
                SessionManager.setBzcard(getActivity(), bzcard_model);
            }
            
            @Override
            public void afterTextChanged(Editable editable) {
            
            }
        });
        ev_company_url.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            
            }
            
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bzcard_model.getBzcardFieldsModel().setCompany_url(ev_company_url.getText().toString().trim());
                SessionManager.setBzcard(getActivity(), bzcard_model);
            }
            
            @Override
            public void afterTextChanged(Editable editable) {
            
            }
        });
        ev_job.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            
            }
            
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                
                bzcard_model.getBzcardFieldsModel().setJobtitle(ev_job.getText().toString());
                SessionManager.setBzcard(getActivity(), bzcard_model);
            }
            
            @Override
            public void afterTextChanged(Editable editable) {
            
            }
        });
        ev_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            
            }
            
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bzcard_model.getBzcardFieldsModel().setAddrees(ev_address.getText().toString());
                SessionManager.setBzcard(getActivity(), bzcard_model);
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
                bzcard_model.getBzcardFieldsModel().setZipcode(ev_zip.getText().toString());
                SessionManager.setBzcard(getActivity(), bzcard_model);
                
            }
            
            @Override
            public void afterTextChanged(Editable editable) {
            
            }
        });
    }
    
    private void setData() {
        if (bzcard_model.getCard_id() == 1) {
            
            ev_company.setEnabled(false);
            iv_company_dummy.setEnabled(false);
            iv_company_icon.setEnabled(false);
            ev_company_url.setEnabled(false);
            ev_job.setEnabled(false);
            ev_address.setEnabled(false);
            ev_zip.setEnabled(false);
        } else {
            ev_company.setEnabled(true);
            iv_company_dummy.setEnabled(true);
            iv_company_icon.setEnabled(true);
            ev_company_url.setEnabled(true);
            ev_job.setEnabled(true);
            ev_address.setEnabled(true);
            ev_zip.setEnabled(true);
            
        }
        if (bzcard_model.isEdit()) {
            ev_first.setText(bzcard_model.getBzcardFieldsModel().getFirst_name().toString().trim());
            ev_last_name.setText(bzcard_model.getBzcardFieldsModel().getLast_name().toString().trim());
            String main_data = bzcard_model.getBzcardFieldsModel().getContant_number().replace("+" + String.valueOf(Global.Countrycode(getActivity(), bzcard_model.getBzcardFieldsModel().getContant_number())), "");
            edt_mobile_no.setText(main_data.toString().trim());
            //Country Code Set
            ccp_id.setDefaultCountryUsingNameCode(String.valueOf(Global.Countrycode(getActivity(), bzcard_model.getBzcardFieldsModel().getContant_number())));
            ccp_id.setDefaultCountryUsingPhoneCode(Global.Countrycode(getActivity(), bzcard_model.getBzcardFieldsModel().getContant_number()));
            ccp_id.resetToDefaultCountry();
            ccp_id.registerCarrierNumberEditText(edt_mobile_no);
            ev_email.setText(bzcard_model.getBzcardFieldsModel().getEmail().toString().trim());
            ev_company.setText(bzcard_model.getBzcardFieldsModel().getCompany_name().toString().trim());
            ev_company_url.setText(bzcard_model.getBzcardFieldsModel().getCompany_url().toString().trim());
            ev_job.setText(bzcard_model.getBzcardFieldsModel().getJobtitle().toString().trim());
            ev_address.setText(bzcard_model.getBzcardFieldsModel().getAddrees().toString().trim());
            ev_zip.setText(bzcard_model.getBzcardFieldsModel().getZipcode().toString().trim());
            if (!bzcard_model.getBzcardFieldsModel().getCompany_logo().equals("") ||
                        !bzcard_model.getBzcardFieldsModel().getCompany_logo_url().equals("")) {
                if (!bzcard_model.getBzcardFieldsModel().getCompany_logo().equals("")) {
                    Glide.with(getActivity()).
                                                     load(bzcard_model.getBzcardFieldsModel().getCompany_logo()).
                                                                                                                        apply(RequestOptions.bitmapTransform(new RoundedCorners(5))).
                                                                                                                                                                                            into(iv_company_icon);
                } else {
                    Glide.with(getActivity()).
                                                     load(bzcard_model.getBzcardFieldsModel().getCompany_logo_url()).
                                                                                                                            apply(RequestOptions.bitmapTransform(new RoundedCorners(5))).
                                                                                                                                                                                                into(iv_company_icon);
                    
                }
                iv_company_icon.setVisibility(View.VISIBLE);
                iv_company_dummy.setVisibility(View.GONE);
                
                
                tv_reupload.setVisibility(View.VISIBLE);
                tv_image_size.setVisibility(View.GONE);
            } else {
                iv_company_icon.setVisibility(View.GONE);
                iv_company_dummy.setVisibility(View.VISIBLE);
                
                tv_reupload.setVisibility(View.GONE);
                tv_image_size.setVisibility(View.VISIBLE);
            }
            
            
        } else {
            SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
            ev_first.setText("" + user_data.getUser().getFirstName().toString().trim());
            bzcard_model.getBzcardFieldsModel().setFirst_name("" + user_data.getUser().getFirstName().toString().trim());
            ev_last_name.setText("" + user_data.getUser().getLastName().toString().trim());
            bzcard_model.getBzcardFieldsModel().setLast_name("" + user_data.getUser().getLastName().toString().trim());
            String main_data = user_data.getUser().getContactNumber().replace("+" + String.valueOf(Global.Countrycode(getActivity(), user_data.getUser().getContactNumber())), "");
            edt_mobile_no.setText("" + main_data.toString().trim());
            bzcard_model.getBzcardFieldsModel().setContant_number("" + user_data.getUser().getContactNumber());
            //Country Code Set
            ccp_id.setDefaultCountryUsingNameCode(String.valueOf(Global.Countrycode(getActivity(), user_data.getUser().getContactNumber())));
            ccp_id.setDefaultCountryUsingPhoneCode(Global.Countrycode(getActivity(), user_data.getUser().getContactNumber()));
            ccp_id.resetToDefaultCountry();
            ccp_id.registerCarrierNumberEditText(edt_mobile_no);
            bzcard_model.getBzcardFieldsModel().setCountry_code(ccp_id.getSelectedCountryCode());
            ev_email.setText("" + user_data.getUser().getEmail().toString().trim());
            bzcard_model.getBzcardFieldsModel().setEmail(user_data.getUser().getEmail().toString().trim());
            ev_company.setText("" + user_data.getUser().getUserprofile().getCompany_name());
            bzcard_model.getBzcardFieldsModel().setCompany_name(user_data.getUser().getUserprofile().getCompany_name());
            ev_company_url.setText("" + user_data.getUser().getUserprofile().getCompany_url().toString().trim());
            bzcard_model.getBzcardFieldsModel().setCompany_url(user_data.getUser().getUserprofile().getCompany_url().toString().trim());
            ev_job.setText("" + user_data.getUser().getUserprofile().getJob_title().toString().trim());
            bzcard_model.getBzcardFieldsModel().setJobtitle(user_data.getUser().getUserprofile().getJob_title().toString().trim());
            ev_address.setText("" + user_data.getUser().getUserprofile().getAddress().toString().trim());
            bzcard_model.getBzcardFieldsModel().setAddrees(user_data.getUser().getUserprofile().getAddress().toString().trim());
            ev_zip.setText("" + user_data.getUser().getUserprofile().getZipcode().toString().trim());
            bzcard_model.getBzcardFieldsModel().setZipcode(user_data.getUser().getUserprofile().getZipcode().toString().trim());
            
            
            if (!bzcard_model.getBzcardFieldsModel().getCompany_logo().equals("") ||
                        !bzcard_model.getBzcardFieldsModel().getCompany_logo_url().equals("")) {
                if (!bzcard_model.getBzcardFieldsModel().getCompany_logo().equals("")) {
                    Glide.with(getActivity()).
                                                     load(bzcard_model.getBzcardFieldsModel().getCompany_logo()).
                                                                                                                        apply(RequestOptions.bitmapTransform(new RoundedCorners(5))).
                                                                                                                                                                                            into(iv_company_icon);
                } else {
                    Glide.with(getActivity()).
                                                     load(bzcard_model.getBzcardFieldsModel().getCompany_logo_url()).
                                                                                                                            apply(RequestOptions.bitmapTransform(new RoundedCorners(5))).
                                                                                                                                                                                                into(iv_company_icon);
                    
                }
                iv_company_icon.setVisibility(View.VISIBLE);
                iv_company_dummy.setVisibility(View.GONE);
                
                
                tv_reupload.setVisibility(View.VISIBLE);
                tv_image_size.setVisibility(View.GONE);
            } else {
                iv_company_icon.setVisibility(View.GONE);
                iv_company_dummy.setVisibility(View.VISIBLE);
                
                tv_reupload.setVisibility(View.GONE);
                tv_image_size.setVisibility(View.VISIBLE);
            }
            
            
            SessionManager.setBzcard(getActivity(), bzcard_model);
            
            
        }
    }
    
    
    private void IntentUI(View view) {
        ev_first = view.findViewById(R.id.ev_first);
        ev_last_name = view.findViewById(R.id.ev_last_name);
        ccp_id = view.findViewById(R.id.ccp_id);
        edt_mobile_no = view.findViewById(R.id.edt_mobile_no);
        iv_invalid = view.findViewById(R.id.iv_invalid);
        ev_email = view.findViewById(R.id.ev_email);
        iv_invalid1 = view.findViewById(R.id.iv_invalid1);
        company_layout = view.findViewById(R.id.company_layout);
        ev_company = view.findViewById(R.id.ev_company);
        iv_company_dummy = view.findViewById(R.id.iv_company_dummy);
        iv_company_icon = view.findViewById(R.id.iv_company_icon);
        ev_company_url = view.findViewById(R.id.ev_company_url);
        ev_job = view.findViewById(R.id.ev_job);
        ev_address = view.findViewById(R.id.ev_address);
        ev_zip = view.findViewById(R.id.ev_zip);
        tv_reupload = view.findViewById(R.id.tv_reupload);
        tv_image_size = view.findViewById(R.id.tv_image_size);
        tv_reupload.setOnClickListener(this);
        company_layout.setOnClickListener(this);
        iv_company_dummy.setOnClickListener(this);
        
        
    }
    
    private void enterPhoneNumber() {
        edt_mobile_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String countryCode = ccp_id.getSelectedCountryCodeWithPlus();
                String phoneNumber = edt_mobile_no.getText().toString().trim();
                if (countryCode.length() > 0 && phoneNumber.length() > 0) {
                    if (Global.isValidPhoneNumber(phoneNumber)) {
                        boolean status = validateUsing_libphonenumber(countryCode, phoneNumber);
                        if (status) {
                            iv_invalid.setText("");
                            iv_invalid.setVisibility(View.GONE);
                        } else {
                            iv_invalid.setText(getResources().getString(R.string.invalid_phone));
                            iv_invalid.setVisibility(View.VISIBLE);
                        }
                    } else {
                        iv_invalid.setText(getResources().getString(R.string.invalid_phone));
                        iv_invalid.setVisibility(View.VISIBLE);
                    }
                } else {
                    iv_invalid.setVisibility(View.VISIBLE);
                    //Toast.makeText(getApplicationContext(), "Country Code and Phone Number is required", Toast.LENGTH_SHORT).show();
                }
                
            }
            
            @Override
            public void afterTextChanged(Editable s) {
            
            }
        });
    }
    
    private boolean validateUsing_libphonenumber(String countryCode, String phNumber) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.createInstance(getActivity());
        String isoCode = phoneNumberUtil.getRegionCodeForCountryCode(Integer.parseInt(countryCode));
        Phonenumber.PhoneNumber phoneNumber = null;
        try {
            phoneNumber = phoneNumberUtil.parse(phNumber, isoCode);
            
            
            boolean isValid = phoneNumberUtil.isValidNumber(phoneNumber);
            if (isValid) {
                String internationalFormat = phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
                return true;
            } else {
                return false;
            }
        } catch (NumberParseException e) {
            System.err.println(e);
        }
        return false;
    }
    
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.company_layout:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                //showBottomSheetDialog_For_Company();
                break;
            case R.id.iv_company_dummy:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (checkAndRequestPermissions(getActivity())) {
                    
                    captureimageDialog(false);
                }
                break;
            case R.id.tv_reupload:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (checkAndRequestPermissions(getActivity())) {
                    
                    captureimageDialog(false);
                }
                break;
            
        }
    }
    
    private void captureimageDialog(boolean remove) {
        final View mView = getLayoutInflater().inflate(R.layout.capture_userpicture_dialog_item, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);
        
        TextView cameraId = bottomSheetDialog.findViewById(R.id.cameraId);
        TextView tv_remove = bottomSheetDialog.findViewById(R.id.tv_remove);
        if (remove) {
            tv_remove.setVisibility(View.VISIBLE);
        } else {
            tv_remove.setVisibility(View.GONE);
        }
        tv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                bottomSheetDialog.dismiss();
                
                
            }
        });
        cameraId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                image_flag = 0;
                mLastClickTime = SystemClock.elapsedRealtime();
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String fileName = "temp.jpg";
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, fileName);
                mCapturedImageURI = getActivity().getContentResolver()
                                            .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                                    values);
                takePictureIntent
                        .putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                startActivityForResult(takePictureIntent, CAPTURE_IMAGE);
                
                
                bottomSheetDialog.dismiss();
            }
        });
        TextView galleryId = bottomSheetDialog.findViewById(R.id.galleryId);
        galleryId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                image_flag = 0;
                mLastClickTime = SystemClock.elapsedRealtime();
                Intent takePictureIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                String fileName = "temp.jpg";
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, fileName);
                mCapturedImageURI = getActivity().getContentResolver()
                                            .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                                    values);
                takePictureIntent
                        .putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                startActivityForResult(takePictureIntent, 1);
                
                bottomSheetDialog.dismiss();
                
            }
        });
        
        bottomSheetDialog.show();
        
    }
    
    @SuppressLint("LongLogTag")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == getActivity().RESULT_OK) {
                    Uri resultUri = result.getUri();
                    bzcard_model.getBzcardFieldsModel().setCompany_logo_url(result.getUri().getPath());
                    
                    SessionManager.setBzcard(getActivity(), bzcard_model);
                    File file = new File(result.getUri().getPath());
                    Uri uri = Uri.fromFile(file);
                    
                    logo_filePath = uri.getPath();
                    
                    String profilePath = Global.getPathFromUri(getActivity(), uri);
                    Glide.with(getActivity()).
                                                     load(resultUri).
                                                                            apply(RequestOptions.bitmapTransform(new RoundedCorners(5))).
                                                                                                                                                into(iv_company_icon);
                    iv_company_icon.setVisibility(View.VISIBLE);
                    iv_company_dummy.setVisibility(View.GONE);
                    tv_reupload.setVisibility(View.VISIBLE);
                    tv_image_size.setVisibility(View.GONE);
                    
                    
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }
        } else if (requestCode == CAPTURE_IMAGE) {
            ImageCropFunctionCustom(mCapturedImageURI);
        } else if (requestCode == 203) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == getActivity().RESULT_OK) {
                    Uri resultUri = result.getUri();
                    bzcard_model = SessionManager.getBzcard(getActivity());
                    bzcard_model.getBzcardFieldsModel().setCompany_logo_url(result.getUri().getPath());
                    
                    SessionManager.setBzcard(getActivity(), bzcard_model);
                    
                    File file = new File(result.getUri().getPath());
                    Uri uri = Uri.fromFile(file);
                    logo_filePath = uri.getPath();
                    String profilePath = Global.getPathFromUri(getActivity(), uri);
                    
                    Glide.with(getActivity()).
                                                     load(resultUri).
                                                                            apply(RequestOptions.bitmapTransform(new RoundedCorners(5))).
                                                                                                                                                into(iv_company_icon);
                    iv_company_icon.setVisibility(View.VISIBLE);
                    iv_company_dummy.setVisibility(View.GONE);
                    
                    tv_reupload.setVisibility(View.VISIBLE);
                    tv_image_size.setVisibility(View.GONE);
                    
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }
        } else {
            if (image_flag == 0) {
                image_flag = 1;
                ImageCropFunctionCustom(data.getData());
            }
            
        }
        
        
    }
    
    public void ImageCropFunctionCustom(Uri uri) {
        Intent intent = CropImage.activity(uri)
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .getIntent(getActivity());
        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
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
    
    @Override
    public void onResume() {
        bzcard_model = SessionManager.getBzcard(getActivity());
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
        super.onResume();
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
                        ev_company_url.setText("");
                        ev_company_url.setText(WorkData.getCompany_url());
                        bzcard_model.getBzcardFieldsModel().setCompany_id(String.valueOf(WorkData.getId()));
                        bzcard_model.getBzcardFieldsModel().setCompany_name(holder.tv_item.getText().toString());
                        bzcard_model.getBzcardFieldsModel().setCompany_url(WorkData.getCompany_url());
                        SessionManager.setBzcard(getActivity(), bzcard_model);
                        
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