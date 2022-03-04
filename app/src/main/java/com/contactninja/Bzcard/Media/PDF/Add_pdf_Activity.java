package com.contactninja.Bzcard.Media.PDF;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.contactninja.Bzcard.Media.Select_Media_Activity;
import com.contactninja.Model.BZcardListModel;
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
    BZcardListModel.Bizcard bzcard_model;
    Bzcard_Fields_Model.BZ_media_information information;
    EditText edt_pdf_title, edt_Add_description;
    LinearLayout layout_replace, layout_Cancel;
    private long mLastClickTime = 0;
    Integer is_featured = 0;
    private String SelectFilePath = "",SelectFileName="";
    private String tag="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pdf);
        mNetworkReceiver = new ConnectivityReceiver();
        sessionManager = new SessionManager(this);
        bzcard_model = SessionManager.getBzcard(Add_pdf_Activity.this);
        bzMediaInformationList = bzcard_model.getBzcardFieldsModel().getBzMediaInformationList();
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

        if(bzcard_model.isEdit()){
            String hostURL = information.getMedia_url().substring(information.getMedia_url().lastIndexOf("/") + 1, information.getMedia_url().length());
            String pdfname=hostURL.replace("pdf_A_","");
            txt_selected_file_name.setText(pdfname);
        }else {
            txt_selected_file_name.setText(information.getFileName());
        }
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
                        bzcard_model.getBzcardFieldsModel().setBzMediaInformationList(bzMediaInformationList);
                        SessionManager.setBzcard(Add_pdf_Activity.this, bzcard_model);
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
                                information.setMedia_filePath(SelectFilePath);
                                information.setFileName(SelectFileName);
                                information.setMedia_title(edt_pdf_title.getText().toString().trim());
                                information.setMedia_description(edt_Add_description.getText().toString().trim());
                                information.setIs_featured(is_featured);
                                bzMediaInformationList.set(i, information);
                                bzcard_model.getBzcardFieldsModel().setBzMediaInformationList(bzMediaInformationList);
                                SessionManager.setBzcard(Add_pdf_Activity.this, bzcard_model);

                                break;
                            }
                        }
                    } else {
                        Bzcard_Fields_Model.BZ_media_information information = new Bzcard_Fields_Model.BZ_media_information();
                        information.setId(bzMediaInformationList.size());
                        information.setMedia_type("pdf");
                        information.setMedia_filePath(SelectFilePath);
                        information.setFileName(SelectFileName);
                        information.setMedia_title(edt_pdf_title.getText().toString().trim());
                        information.setMedia_description(edt_Add_description.getText().toString().trim());
                        if (bzMediaInformationList.size() == 0) {
                            information.setIs_featured(1);
                        } else {
                            information.setIs_featured(0);
                        }
                        bzMediaInformationList.add(information);
                        bzcard_model.getBzcardFieldsModel().setBzMediaInformationList(bzMediaInformationList);
                        SessionManager.setBzcard(Add_pdf_Activity.this, bzcard_model);
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

               /* intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/pdf");
                Intent i = Intent.createChooser(intent, "File");
                startActivityForResult(i, CHOOSE_FILE_REQUESTCODE);*/
                intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select a PDF "), CHOOSE_FILE_REQUESTCODE);

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
        Uri selectedUri_PDF = data.getData();
        SelectFilePath = getPath(selectedUri_PDF);
        importFile(data.getData());
    }

    public void importFile(Uri uri) {
        SelectFileName = getFileName(uri);
        txt_selected_file_name.setText(SelectFileName);
        layout_pdf_add.setVisibility(View.GONE);
        // Done!
    }
    private String getPath(final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if(isKitKat) {
            // MediaStore (and general)
            return getForApi19(uri);
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    @TargetApi(19)
    private String getForApi19(Uri uri) {
        if (DocumentsContract.isDocumentUri(this, uri)) {
            Log.e(tag, "+++ Document URI");
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                Log.e(tag, "+++ External Document URI");
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    Log.e(tag, "+++ Primary External Document URI");
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                Log.e(tag, "+++ Downloads External Document URI");
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                Log.e(tag, "+++ Media Document URI");
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    Log.e(tag, "+++ Image Media Document URI");
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    Log.e(tag, "+++ Video Media Document URI");
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    Log.e(tag, "+++ Audio Media Document URI");
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            Log.e(tag, "+++ No DOCUMENT URI :: CONTENT ");

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            Log.e(tag, "+++ No DOCUMENT URI :: FILE ");
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
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
        String SelectFilePath = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));

        cursor.close();

        return fileName;
    }


    private boolean Vlidaction() {
        if (edt_pdf_title.getText().toString().trim().equals("")) {
            Global.Messageshow(getApplicationContext(), mMainLayout, getResources().getString(R.string.image_title_error), false);
        } else if (edt_Add_description.getText().toString().trim().equals("")) {
            Global.Messageshow(getApplicationContext(), mMainLayout, getResources().getString(R.string.image_description_error), false);
        } else if (Global.IsNotNull(information)) {
            if (Global.IsNotNull(information.getMedia_url()) && SelectFilePath.equals("")) {
                Global.Messageshow(getApplicationContext(), mMainLayout, getResources().getString(R.string.select_pdf), false);
            } else {
                return true;
            }
        } else if (SelectFilePath.equals("")) {
            Global.Messageshow(getApplicationContext(), mMainLayout, getResources().getString(R.string.select_pdf), false);
        }else {
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