package com.contactninja.Bzcard.Media.PDF;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.contactninja.Bzcard.Media.Select_Media_Activity;
import com.contactninja.Model.Bzcard_Fields_Model;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("SimpleDateFormat,StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Add_pdf_Activity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener {
    private static final int CHOOSE_FILE_REQUESTCODE = 101;
    private BroadcastReceiver mNetworkReceiver;
    RelativeLayout mMainLayout, layout_pdf_add;
    ImageView iv_back;
    TextView save_button, txt_selected_file_name;
    SessionManager sessionManager;
    List<Bzcard_Fields_Model.BZ_media_information> bzMediaInformationList = new ArrayList<>();
    Bzcard_Fields_Model model;
    Bzcard_Fields_Model.BZ_media_information information;
    EditText edt_pdf_title, edt_Add_description;
    LinearLayout layout_replace, layout_Cancel;
    private long mLastClickTime = 0;
    Integer is_featured = 0;
    private String SelectFilePath = "",SelectFileName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pdf);
        mNetworkReceiver = new ConnectivityReceiver();
        sessionManager = new SessionManager(this);
        model = SessionManager.getBzcard(Add_pdf_Activity.this);
        bzMediaInformationList = model.getBzMediaInformationList();
        initUI();
        try {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            information = (Bzcard_Fields_Model.BZ_media_information) getIntent().getSerializableExtra("MyClass");

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Global.IsNotNull(information)) {
            set_Pdf_info();
        }

    }

    private void set_Pdf_info() {
        layout_pdf_add.setVisibility(View.GONE);

        edt_pdf_title.setText(information.getMedia_title());
        edt_Add_description.setText(information.getMedia_description());

        txt_selected_file_name.setText(information.getFileName());
        layout_pdf_add.setVisibility(View.GONE);
        layout_Cancel.setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initUI() {
        mMainLayout = findViewById(R.id.mMainLayout);
        edt_pdf_title = findViewById(R.id.edt_pdf_title);
        edt_Add_description = findViewById(R.id.edt_Add_description);
        layout_replace = findViewById(R.id.layout_replace);
        layout_Cancel = findViewById(R.id.layout_Cancel);

        layout_pdf_add = findViewById(R.id.layout_pdf_add);
        txt_selected_file_name = findViewById(R.id.txt_selected_file_name);


        layout_replace.setOnClickListener(this);
        layout_Cancel.setOnClickListener(this);
        layout_pdf_add.setOnClickListener(this);

        save_button = findViewById(R.id.save_button);
        save_button.setVisibility(View.VISIBLE);
        save_button.setOnClickListener(this);

        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Add_pdf_Activity.this, mMainLayout);
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
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.layout_Cancel:
                for (int i = 0; i < bzMediaInformationList.size(); i++) {
                    if (bzMediaInformationList.get(i).getId().equals(information.getId())) {
                        bzMediaInformationList.remove(i);
                        model.setBzMediaInformationList(bzMediaInformationList);
                        SessionManager.setBzcard(Add_pdf_Activity.this, model);
                        break;
                    }
                }
                intent = new Intent(getApplicationContext(), Select_Media_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            case R.id.save_button:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (Vlidaction()) {

                    if (Global.IsNotNull(information)) {
                        for (int i = 0; i < bzMediaInformationList.size(); i++) {
                            if (bzMediaInformationList.get(i).getId().equals(information.getId())) {
                                Bzcard_Fields_Model.BZ_media_information information = new Bzcard_Fields_Model.BZ_media_information();
                                information.setId(bzMediaInformationList.get(i).getId());
                                information.setMedia_type(bzMediaInformationList.get(i).getMedia_type());
                                information.setMedia_url(SelectFilePath);
                                information.setFileName(SelectFileName);
                                information.setMedia_title(edt_pdf_title.getText().toString().trim());
                                information.setMedia_description(edt_Add_description.getText().toString().trim());
                                information.setIs_featured(is_featured);
                                bzMediaInformationList.set(i, information);
                                model.setBzMediaInformationList(bzMediaInformationList);
                                SessionManager.setBzcard(Add_pdf_Activity.this, model);

                                break;
                            }
                        }
                    } else {
                        Bzcard_Fields_Model.BZ_media_information information = new Bzcard_Fields_Model.BZ_media_information();
                        information.setId(bzMediaInformationList.size());
                        information.setMedia_type("pdf");
                        information.setMedia_url(SelectFilePath);
                        information.setFileName(SelectFileName);
                        information.setMedia_title(edt_pdf_title.getText().toString().trim());
                        information.setMedia_description(edt_Add_description.getText().toString().trim());
                        if (bzMediaInformationList.size() == 0) {
                            information.setIs_featured(1);
                        } else {
                            information.setIs_featured(0);
                        }
                        bzMediaInformationList.add(information);
                        model.setBzMediaInformationList(bzMediaInformationList);
                        SessionManager.setBzcard(Add_pdf_Activity.this, model);
                    }
                    intent = new Intent(getApplicationContext(), Select_Media_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.layout_replace:
            case R.id.layout_pdf_add:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/pdf");
                Intent i = Intent.createChooser(intent, "File");
                startActivityForResult(i, CHOOSE_FILE_REQUESTCODE);

                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If the user doesn't pick a file just return
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != CHOOSE_FILE_REQUESTCODE || resultCode != RESULT_OK) {
            return;
        }

        // Import the file
        importFile(data.getData());

    }

    public void importFile(Uri uri) {
        SelectFileName = getFileName(uri);
        SelectFilePath = uri.getPath();
        txt_selected_file_name.setText(SelectFileName);
        layout_pdf_add.setVisibility(View.GONE);
        // Done!
    }


    private String getFileName(Uri uri) throws IllegalArgumentException {
        // Obtain a cursor with information regarding this uri
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor.getCount() <= 0) {
            cursor.close();
            throw new IllegalArgumentException("Can't obtain file name, cursor is empty");
        }

        cursor.moveToFirst();

        String fileName = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));

        cursor.close();

        return fileName;
    }


    private boolean Vlidaction() {
        if (edt_pdf_title.getText().toString().trim().equals("")) {
            Global.Messageshow(getApplicationContext(), mMainLayout, getResources().getString(R.string.image_title_error), false);
        } else if (edt_Add_description.getText().toString().trim().equals("")) {
            Global.Messageshow(getApplicationContext(), mMainLayout, getResources().getString(R.string.image_description_error), false);
        } else if (SelectFilePath.equals("")) {
            Global.Messageshow(getApplicationContext(), mMainLayout, getResources().getString(R.string.select_image), false);
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}