package com.contactninja.Group;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.contactninja.AddContect.Addnewcontect_Activity;
import com.contactninja.Model.AddGroup;
import com.contactninja.Model.Contactdetail;
import com.contactninja.Model.GroupListData;
import com.contactninja.Model.Grouplist;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.makeramen.roundedimageview.RoundedImageView;
import com.reddit.indicatorfastscroll.FastScrollItemIndicator;
import com.reddit.indicatorfastscroll.FastScrollerThumbView;
import com.reddit.indicatorfastscroll.FastScrollerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

public class Final_Group extends AppCompatActivity implements View.OnClickListener {
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    GroupListData groupListData;
    TextView save_button;
    ImageView iv_more, iv_back;
    String fragment_name,user_image_Url;
    EditText add_new_contect,add_detail,contect_search;
    LinearLayout add_new_member;
    public static UserListDataAdapter userListDataAdapter;
    public static List<GroupListData> inviteListData = new ArrayList<>();
    RecyclerView  contect_list_unselect;
    RecyclerView.LayoutManager layoutManager;
    FastScrollerView fastscroller;
    FastScrollerThumbView fastscroller_thumb;
    LoadingDialog loadingDialog;
    SessionManager sessionManager;
    RoundedImageView iv_user;
    ImageView iv_dummy;
    String group_name,group_description, File_name,File_extension;
    LinearLayout mMainLayout;
    RetrofitCalls retrofitCalls;
    String old_image="",group_id="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_group);
        IntentUI();
        sessionManager=new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        if (SessionManager.getGroupData(this)!=null)
        {
            Grouplist.Group group_data = SessionManager.getGroupData(this);
            add_new_contect.setText(group_data.getGroupName());
            add_detail.setText(group_data.getDescription());
            iv_user.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext()).
                    load(group_data.getGroupImage()).
                    placeholder(R.drawable.shape_primary_back).
                    error(R.drawable.shape_primary_back).into(iv_user);
            iv_dummy.setVisibility(View.GONE);
            old_image=group_data.getGroupImage();
            group_id= String.valueOf(group_data.getId());


        }

        iv_more.setVisibility(View.GONE);
        save_button.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        save_button.setText("Save");
        save_button.setVisibility(View.VISIBLE);
        inviteListData.clear();
        inviteListData.addAll(sessionManager.getGroupList(this));
        Log.e("Data Is ",new Gson().toJson(sessionManager.getGroupList(this)));
        fastscroller_thumb.setupWithFastScroller(fastscroller);
        fastscroller.setUseDefaultScroller(false);
        fastscroller.getItemIndicatorSelectedCallbacks().add(
                new FastScrollerView.ItemIndicatorSelectedCallback() {
                    @Override
                    public void onItemIndicatorSelected(
                            FastScrollItemIndicator indicator,
                            int indicatorCenterY,
                            int itemPosition
                    ) {

                    }
                }
        );

        add_new_member.setOnClickListener(this);

        fastscroller.setupWithRecyclerView(
                contect_list_unselect,
                (position) -> {
                    // ItemModel item = data.get(position);
                    FastScrollItemIndicator fastScrollItemIndicator = new FastScrollItemIndicator.Text(
                            inviteListData.get(position).getUserName().substring(0, 1)
                                    .substring(0, 1)
                                    .toUpperCase()// Grab the first letter and capitalize it
                    );
                    return fastScrollItemIndicator;
                }
        );


        userListDataAdapter = new UserListDataAdapter(this, getApplicationContext(), inviteListData);
        contect_list_unselect.setAdapter(userListDataAdapter);
        loadingDialog = new LoadingDialog(this);

        contect_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                List<GroupListData> temp = new ArrayList();
                for(GroupListData d: inviteListData){
                    if(d.getUserName().contains(s.toString())){
                        temp.add(d);
                        // Log.e("Same Data ",d.getUserName());
                    }
                }
                userListDataAdapter = new UserListDataAdapter(Final_Group.this, getApplicationContext(), inviteListData);
                contect_list_unselect.setAdapter(userListDataAdapter);
                userListDataAdapter.notifyDataSetChanged();
                userListDataAdapter.updateList(temp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void IntentUI() {
        save_button = findViewById(R.id.save_button);
        iv_more = findViewById(R.id.iv_more);
        iv_back = findViewById(R.id.iv_back);
        add_new_contect=findViewById(R.id.add_new_contect);
        add_detail=findViewById(R.id.add_detail);
        add_new_member=findViewById(R.id.add_new_member);
        layoutManager=new LinearLayoutManager(this);
        contect_list_unselect=findViewById(R.id.contect_list_unselect);
        contect_list_unselect.setLayoutManager(layoutManager);
        fastscroller = findViewById(R.id.fastscroller);
        fastscroller_thumb = findViewById(R.id.fastscroller_thumb);
        contect_search=findViewById(R.id.contect_search);
        iv_user=findViewById(R.id.iv_user);
        iv_dummy=findViewById(R.id.iv_dummy);
        iv_dummy.setOnClickListener(this);
        mMainLayout=findViewById(R.id.mMainLayout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.save_button:
                try {
                    SaveEvent();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.add_new_member:

                sessionManager.setGroupList(getApplicationContext(), inviteListData);
                startActivity(new Intent(getApplicationContext(), GroupActivity.class));
                finish();

                break;
            case R.id.iv_dummy:
                if(checkAndRequestPermissions(Final_Group.this)){
                    captureimageDialog(true);

                }

                break;

            default:


        }
    }

    private void SaveEvent() throws JSONException {

        group_name=add_new_contect.getText().toString();
        group_description=add_detail.getText().toString();
        if (group_name.equals(""))
        {
            Global.Messageshow(getApplicationContext(),mMainLayout,getString(R.string.add_group_txt),false);
        }
        else if (group_description.equals(""))
        {
            Global.Messageshow(getApplicationContext(),mMainLayout,getString(R.string.add_group_description),false);
        }
        else {

            SignResponseModel user_data = sessionManager.getGetUserdata(this);
            String user_id = String.valueOf(user_data.getUser().getId());
            String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
            String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());
            JSONArray contect_array = new JSONArray();
            contect_array.put(1);
            contect_array.put(3);
            String token=Global.getToken(this);
            JSONObject obj = new JSONObject();
            JSONObject paramObject = new JSONObject();
            if (!group_id.equals(""))
            {
                paramObject.put("id", group_id);
            }

            paramObject.put("group_name", group_name);
            paramObject.put("oldImage", old_image);
            paramObject.put("image_extension", File_extension);
            paramObject.put("group_image_name", File_name);
            paramObject.put("group_image", user_image_Url);
            paramObject.put("organization_id", "1");
            paramObject.put("team_id", "1");
            paramObject.put("user_id", user_id);
            paramObject.put("contact_ids",contect_array);
            paramObject.put("description", group_description);
            obj.put("data", paramObject);
           // Log.e("Data IS ",new Gson().toJson(obj));

            JsonParser jsonParser = new JsonParser();
            JsonObject gsonObject = (JsonObject)jsonParser.parse(obj.toString());
            Log.e("Obbject data",new Gson().toJson(gsonObject));
            retrofitCalls.AddGroup(sessionManager,gsonObject, loadingDialog,token, new RetrofitCallback() {
                @Override
                public void success(Response<ApiResponse> response) {

                    loadingDialog.cancelLoading();
                    if (response.body().getStatus() == 200) {
                        Global.Messageshow(getApplicationContext(),mMainLayout,response.body().getMessage(),true);
                        finish();
                    } else {
                        Global.Messageshow(getApplicationContext(),mMainLayout,response.body().getMessage(),false);
                    }
                }

                @Override
                public void error(Response<ApiResponse> response) {
                    loadingDialog.cancelLoading();
                }
            });


        }


    }

    // function to check permission
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
    // Handled permission Result

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        Log.e("Uri is ", String.valueOf(selectedImage));
                      //  filePath.substring(filePath.lastIndexOf(".") + 1); // Without dot jpg, png

                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);

                                //user_image_Url = encodeFileToBase64Binary(picturePath);
                                iv_user.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                iv_user.setVisibility(View.VISIBLE);
                                iv_dummy.setVisibility(View.GONE);
                                File file= new File(selectedImage.getPath());
                                File_name=file.getName();

                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                                String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                                user_image_Url="data:image/JPEG;base64,"+imageString;
                                File_extension="JPEG";
                                Log.e("url is",user_image_Url);
                            }
                        }
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        Log.e("Uri is ", String.valueOf(selectedImage));
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);

                                //user_image_Url = encodeFileToBase64Binary(picturePath);
                               // Log.e("Image Url is ",user_image_Url);

                                iv_user.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();


                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

                                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                                String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                                user_image_Url="data:image/JPEG;base64,"+imageString;
                                Log.e("url is",user_image_Url);
                                File_extension="JPEG";


                                iv_user.setVisibility(View.VISIBLE);
                                iv_dummy.setVisibility(View.GONE);

                                File file= new File(selectedImage.getPath());
                                File_name=file.getName();


                            }
                        }
                    }
                    break;
            }
        }
    }


    private void captureimageDialog(boolean remove) {
        final View mView = getLayoutInflater().inflate(R.layout.capture_userpicture_dialog_item, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Final_Group.this, R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);

        TextView cameraId = bottomSheetDialog.findViewById(R.id.cameraId);
        TextView tv_remove = bottomSheetDialog.findViewById(R.id.tv_remove);
        if(remove){
            tv_remove.setVisibility(View.VISIBLE);
        }else {
            tv_remove.setVisibility(View.GONE);
        }
        tv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iv_user.setVisibility(View.GONE);
                iv_dummy.setVisibility(View.VISIBLE);
                bottomSheetDialog.dismiss();

            }
        });
        cameraId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);

                bottomSheetDialog.dismiss();
            }
        });
        TextView galleryId = bottomSheetDialog.findViewById(R.id.galleryId);
        galleryId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);
                bottomSheetDialog.dismiss();

            }
        });

        bottomSheetDialog.show();

    }

    public static String encodeFileToBase64Binary(String str) {
        try {
            return new String(Base64.decode(str, 0), "UTF-8");
        } catch (UnsupportedEncodingException | IllegalArgumentException unused) {
            return "";
        }
    }

    public class UserListDataAdapter extends RecyclerView.Adapter<UserListDataAdapter.InviteListDataclass>
            implements Filterable {

        private final Context mcntx;
        public Activity mCtx;
        int last_postion = 0;
        String second_latter = "";
        String current_latter = "", image_url = "";
        private List<GroupListData> userDetails;
        private final List<GroupListData> userDetailsfull;
        private final Filter exampleFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<GroupListData> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(userDetailsfull);
                } else {
                    String userName = constraint.toString().toLowerCase().trim();
                    String userNumber = constraint.toString().toLowerCase().trim();
                    for (GroupListData item : userDetailsfull) {
                        if (item.getUserName().toLowerCase().contains(userName)
                                || item.getUserPhoneNumber().toLowerCase().contains(userNumber)) {
                            filteredList.add(item);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                userDetails.clear();
                userDetails.addAll((List<GroupListData>) results.values);
                notifyDataSetChanged();
            }
        };

        public UserListDataAdapter(Activity Ctx, Context mCtx, List<GroupListData> userDetails) {
            this.mcntx = mCtx;
            this.mCtx = Ctx;
            this.userDetails = userDetails;
            userDetailsfull = new ArrayList<>(userDetails);
        }

        @NonNull
        @Override
        public UserListDataAdapter.InviteListDataclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.invite_user_details, parent, false);
            return new UserListDataAdapter.InviteListDataclass(view);
        }

        @Override
        public void onBindViewHolder(@NonNull UserListDataAdapter.InviteListDataclass holder, int position) {
            GroupListData inviteUserDetails = userDetailsfull.get(position);
            last_postion = position;
                //Log.e("Size is", String.valueOf(userDetailsfull.size()));

            holder.remove_contect_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Posirion is", String.valueOf(position));
                   // removeite(position);
                    Log.e("Main Data Is",new Gson().toJson(inviteListData));
                    holder.main_layout.setVisibility(View.GONE);
                    userDetails.get(position).setFlag("true");

                }
            });
               /* if (userDetailsfull.get(position).getFlag().toString().equals("null"))
                {
                    holder.remove_contect_icon.setVisibility(View.VISIBLE);
                    holder.add_new_contect_icon.setVisibility(View.GONE);
                }
                else */if (userDetailsfull.get(position).getFlag().equals("false"))
                {
                    holder.remove_contect_icon.setVisibility(View.VISIBLE);
                    holder.add_new_contect_icon1.setVisibility(View.GONE);
                }
                else {
                    holder.remove_contect_icon.setVisibility(View.GONE);
                    holder.add_new_contect_icon1.setVisibility(View.VISIBLE);


                }




/*
            holder.add_new_contect_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.remove_contect_icon.setVisibility(View.VISIBLE);
                    holder.add_new_contect_icon.setVisibility(View.GONE);
                    userDetailsfull.get(position).setFlag("false");
                    save_button.setTextColor(getColor(R.color.purple_200));
                    inviteListData.add(userDetails.get(position));

                }
            });
*/


            if (inviteUserDetails.getF_latter().equals("")) {
                holder.first_latter.setVisibility(View.GONE);
                holder.top_layout.setVisibility(View.GONE);
            } else {

                holder.first_latter.setVisibility(View.VISIBLE);
                holder.first_latter.setText(inviteUserDetails.getF_latter());
                holder.top_layout.setVisibility(View.VISIBLE);
                String first_latter = inviteUserDetails.getUserName().substring(0, 1).toUpperCase();

                if (second_latter.equals("")) {
                    current_latter = first_latter;
                    second_latter = first_latter;
                    holder.first_latter.setVisibility(View.VISIBLE);
                    holder.top_layout.setVisibility(View.VISIBLE);

                } else if (second_latter.equals(first_latter)) {
                    current_latter = second_latter;
                    inviteUserDetails.setF_latter("");
                    holder.first_latter.setVisibility(View.GONE);
                    holder.top_layout.setVisibility(View.GONE);

                } else {

                    current_latter = first_latter;
                    second_latter = first_latter;
                    holder.first_latter.setVisibility(View.VISIBLE);
                    holder.top_layout.setVisibility(View.VISIBLE);


                }
            }


            String file = "" + inviteUserDetails.getUserImageURL();
            if (file.equals("null")) {
                holder.no_image.setVisibility(View.VISIBLE);
                holder.profile_image.setVisibility(View.GONE);
                String name = inviteUserDetails.getUserName();
                holder.profile_image.setVisibility(View.GONE);
                String add_text = "";
                String[] split_data = name.split(" ");
                try {
                    for (int i = 0; i < split_data.length; i++) {
                        if (i == 0) {
                            add_text = split_data[i].substring(0, 1);
                        } else {
                            add_text = add_text + split_data[i].charAt(0);
                            break;
                        }
                    }
                } catch (Exception e) {

                }


                holder.no_image.setText(add_text);
                holder.no_image.setVisibility(View.VISIBLE);

            } else {
                image_url = inviteUserDetails.getUserImageURL();

                if (holder.profile_image.getDrawable() == null) {
                    Glide.with(mCtx).
                            load(inviteUserDetails.getUserImageURL())
                            .placeholder(R.drawable.shape_primary_circle)
                            .error(R.drawable.shape_primary_circle)
                            .into(holder.profile_image);
                    //Log.e("Image ","View "+position);
                } else {
                    holder.profile_image.setVisibility(View.GONE);
                    String name = inviteUserDetails.getUserName();
                    String add_text = "";
                    String[] split_data = name.split(" ");
                    for (int i = 0; i < split_data.length; i++) {
                        if (i == 0) {
                            add_text = split_data[i].substring(0, 1);
                        } else {
                            add_text = add_text + split_data[i].charAt(0);
                        }
                    }
                    holder.no_image.setText(add_text);
                    holder.no_image.setVisibility(View.VISIBLE);
                }

            }
            holder.userName.setText(inviteUserDetails.getUserName());
            holder.userNumber.setText(inviteUserDetails.getUserPhoneNumber());



        }

        @Override
        public int getItemCount() {
            return userDetails.size();
        }

        @Override
        public Filter getFilter() {
            Log.e("Fillter is", new Gson().toJson(exampleFilter));
            return exampleFilter;
        }

        public void updateList(List<GroupListData> list) {
            userDetails = list;
            notifyDataSetChanged();
        }
        public void removeite(int value)
        {
            inviteListData.remove(value);

            userListDataAdapter = new UserListDataAdapter(Final_Group.this, getApplicationContext(), inviteListData);
            contect_list_unselect.setAdapter(userListDataAdapter);

        }

        public class InviteListDataclass extends RecyclerView.ViewHolder {

            TextView no_image;
            TextView userName, userNumber, first_latter;
            CircleImageView profile_image;
            LinearLayout top_layout;
            ImageView add_new_contect_icon1,remove_contect_icon;
            RelativeLayout main_layout;


            public InviteListDataclass(@NonNull View itemView) {
                super(itemView);
                first_latter = itemView.findViewById(R.id.first_latter);
                userName = itemView.findViewById(R.id.username);
                userNumber = itemView.findViewById(R.id.user_number);
                profile_image = itemView.findViewById(R.id.profile_image);
                no_image = itemView.findViewById(R.id.no_image);
                top_layout = itemView.findViewById(R.id.top_layout);
                add_new_contect_icon1=itemView.findViewById(R.id.add_new_contect_icon);
                remove_contect_icon=itemView.findViewById(R.id.remove_contect_icon);
                add_new_contect_icon1.setVisibility(View.VISIBLE);
                main_layout=itemView.findViewById(R.id.main_layout);

            }

        }

    }


}