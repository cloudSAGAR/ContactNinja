package com.contactninja.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.contactninja.R;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.google.gson.Gson;
import com.contactninja.AddContect.Addnewcontect_Activity;
import com.contactninja.Model.InviteListData;
import com.contactninja.Utils.DatabaseClient;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.reddit.indicatorfastscroll.FastScrollItemIndicator;
import com.reddit.indicatorfastscroll.FastScrollerThumbView;
import com.reddit.indicatorfastscroll.FastScrollerView;


import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContectFragment extends Fragment {

    ConstraintLayout mMainLayout;
    public static final int RequestPermissionCode = 1;
    Context mCtx;
    Cursor cursor;
    public static UserListDataAdapter userListDataAdapter;
    public static ArrayList<InviteListData> inviteListData=new ArrayList<>();
    RecyclerView rvinviteuserdetails;
    String userName, user_phone_number,user_image,user_des,strtext="",old_latter="",contect_type="",contect_email,
    contect_type_work="",email_type_home="",email_type_work="",country="",city="",region="",street="",
            postcode="",postType="",note="";

    FastScrollerView fastscroller;
    FastScrollerThumbView fastscroller_thumb;
    // ImageView iv_back,iv_more;
    SearchView contect_search;
    TextView add_new_contect,num_count;
    Handler mHandler=new Handler();
    ImageView add_new_contect_icon;
    View view1;
    FragmentActivity fragmentActivity;
    LinearLayout add_new_contect_layout;
    int c=0;
    LoadingDialog loadingDialog;
    private static final int PERMISSION_REQUEST_CODE = 100;
    StringBuilder data;

    public ContectFragment(String strtext, View view, FragmentActivity activity) {

        this.strtext=strtext;
        this.view1=view;
        this.fragmentActivity=activity;
        // Log.e("View is ", String.valueOf(view1.getVisibility()));
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View content_view=inflater.inflate(R.layout.fragment_contect, container, false);
        IntentUI(content_view);
        mCtx=getContext();
        loadingDialog=new LoadingDialog(getActivity());
        rvinviteuserdetails.setLayoutManager(new LinearLayoutManager(mCtx, LinearLayoutManager.VERTICAL, false));
        rvinviteuserdetails.setHasFixedSize(true);
        //inviteListData.clear();

       // EnableRuntimePermission();


        //Faste View Code
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

        fastscroller.setupWithRecyclerView(
                rvinviteuserdetails,
                (position) -> {
                    // ItemModel item = data.get(position);
                    FastScrollItemIndicator fastScrollItemIndicator= new FastScrollItemIndicator.Text(


                            inviteListData.get(position).getUserName().substring(0, 1)
                                    .substring(0, 1)
                                    .toUpperCase()// Grab the first letter and capitalize it
                    );
                    return  fastScrollItemIndicator;
                }
        );

        GetContactsIntoArrayList();
        getAllContect();
        add_new_contect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addnewcontect=new Intent(getActivity(), Addnewcontect_Activity.class);
                startActivity(addnewcontect);
            }
        });
        add_new_contect_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addnewcontect=new Intent(getActivity(), Addnewcontect_Activity.class);
                startActivity(addnewcontect);
            }
        });
        add_new_contect_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addnewcontect=new Intent(getActivity(), Addnewcontect_Activity.class);
                startActivity(addnewcontect);
               // splitdata(inviteListData);
            }
        });




        return content_view;

    }

    void filter(String text, View view, FragmentActivity activity){



        if (!text.equals(""))
        {
            List<InviteListData> temp = new ArrayList();
            for(InviteListData d: inviteListData){
                if(d.getUserName().contains(text)){
                    temp.add(d);
                    // Log.e("Same Data ",d.getUserName());
                }
            }
            rvinviteuserdetails=view.findViewById(R.id.contect_list);
            userListDataAdapter = new UserListDataAdapter(activity, activity, inviteListData);
            rvinviteuserdetails.setAdapter(userListDataAdapter);
            userListDataAdapter.notifyDataSetChanged();
            userListDataAdapter.updateList(temp);
        }
        else {
            rvinviteuserdetails=view.findViewById(R.id.contect_list);
            userListDataAdapter = new UserListDataAdapter(activity, activity, inviteListData);
            rvinviteuserdetails.setAdapter(userListDataAdapter);
            userListDataAdapter.notifyDataSetChanged();
        }

    }


    private void IntentUI(View content_view) {
        mMainLayout=content_view.findViewById(R.id.mMainLayout);
        rvinviteuserdetails=content_view.findViewById(R.id.contect_list);
        fastscroller=content_view.findViewById(R.id.fastscroller);
        fastscroller_thumb=content_view.findViewById(R.id.fastscroller_thumb);
        // iv_back=content_view.findViewById(R.id.iv_back);
        //iv_more=content_view.findViewById(R.id.iv_more);
        contect_search=content_view.findViewById(R.id.contect_search);
        add_new_contect=content_view.findViewById(R.id.add_new_contect);
        num_count=content_view.findViewById(R.id.num_count);
        add_new_contect_icon=content_view.findViewById(R.id.add_new_contect_icon);
        add_new_contect_layout=content_view.findViewById(R.id.add_new_contect_layout);
    }
    public void GetContactsIntoArrayList() {
        cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
        while (cursor.moveToNext()) {

            userName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            user_phone_number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            user_image=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));
            user_des=cursor.getString(cursor.getColumnIndex(String.valueOf(ContactsContract.CommonDataKinds.Phone.DATA2)));

            try {
                contect_type=cursor.getString(cursor.getColumnIndex(String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_HOME)));
                contect_type_work=cursor.getString(cursor.getColumnIndex(String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_WORK)));
                contect_email = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                email_type_home=cursor.getString(cursor.getColumnIndex(String.valueOf(ContactsContract.CommonDataKinds.Email.TYPE_HOME)));
                email_type_work=cursor.getString(cursor.getColumnIndex(String.valueOf(ContactsContract.CommonDataKinds.Email.TYPE_WORK)));



                country = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
                // StructuredPostal.CITY == data7
                city = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
                // StructuredPostal.REGION == data8
                region = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
                // StructuredPostal.STREET == data4
                street = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                // StructuredPostal.POSTCODE == data9
                postcode = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
                // StructuredPostal.TYPE == data2
                postType = String.valueOf(cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE)));
                note = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));

                Log.e("country",country);
                Log.e("city",city);
                Log.e("region",region);
                Log.e("street",street);
                Log.e("postcode",postcode);
                Log.e("posttype",postType);
                Log.e("note",note);

            }
            catch (Exception e)
            {

            }

            String unik_key=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)).substring(0, 1)
                    .substring(0, 1)
                    .toUpperCase();
            if (old_latter.equals(""))
            {
                old_latter=unik_key;

            }
            else if (old_latter.equals(unik_key))
            {
                old_latter="";
            }
            else if (!old_latter.equals(unik_key)){
                old_latter=unik_key;
            }
            boolean found = inviteListData.stream().anyMatch(p -> p.getUserPhoneNumber().equals(user_phone_number));

            if (found)
            {

            }
            else {




                //String  contactID = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(String.valueOf(getId())));
                String contactID= String.valueOf(Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY));
                inviteListData.add(new InviteListData( ""+userName.toString().trim(),
                        user_phone_number.toString().trim(),
                        user_image,
                        user_des,
                        old_latter.toString().trim(),""));
                //  userListDataAdapter.notifyDataSetChanged();
                getTasks(new InviteListData( userName, user_phone_number,user_image,user_des,old_latter,""));


            }

        }


        cursor.close();

    }



    private void splitdata(ArrayList<InviteListData> response) {
        System.out.println("GET DATA IS "+response);

        // response will have a @ symbol so that we can split individual user data
      //  String res_data[] = response.split("@");
        //StringBuilder  to store the data
        data = new StringBuilder();

        data.append("id,firstname,phone");

        for(int i = 0; i<response.size();i++){
          //  Log.e("MObile Number is",response.get(i).getUserPhoneNumber());
            data.append("\n"+ response.get(i).getId()+","+ response.get(i).getUserName().toString()+","+ response.get(i).getUserPhoneNumber().toString());
        }
        CreateCSV(data);
    }
    private void CreateCSV(StringBuilder data) {
        Calendar calendar = Calendar.getInstance();
        long time= calendar.getTimeInMillis();
        try {
            //
            FileOutputStream out = getActivity().openFileOutput("CSV_Data_"+time+".csv", Context.MODE_PRIVATE);

            //store the data in CSV file by passing String Builder data
            out.write(data.toString().getBytes());
            out.close();
            Context context = getActivity();
            final File newFile = new File(Environment.getExternalStorageDirectory(),"SimpleCVS");
            if(!newFile.exists())
            {
                newFile.mkdir();
            }
            File file = new File(context.getFilesDir(),"CSV_Data_"+time+".csv");
            Uri path = FileProvider.getUriForFile(context,"com.contactninja",file);
            //once the file is ready a share option will pop up using which you can share
            // the same CSV from via Gmail or store in Google Drive

            Log.e("File Pathe is ", String.valueOf(path));
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/csv");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Data");
            intent.putExtra(Intent.EXTRA_STREAM, path);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(intent,"Excel Data"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }







    public void EnableRuntimePermission() {

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                //Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }

        };
        TedPermission.with(getActivity())
                .setPermissionListener(permissionlistener)
                .setDeniedTitle("Contactninja would like to access your contacts")
                .setDeniedMessage("Contact Ninja uses your contacts to improve your business’s marketing outreach by aggrregating your contacts.")
                .setGotoSettingButtonText("setting")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                .setRationaleConfirmText("OK")
                .check();


    }

    @Override
    public void onRequestPermissionsResult(int RC, String[] per, int[] PResult) {

        super.onRequestPermissionsResult(RC, per, PResult);
        if (RC == RequestPermissionCode) {
            if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Global.Messageshow(getActivity(), mMainLayout, "Permission Canceled, Now your application cannot access CONTACTS", false);
                startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", getActivity().getPackageName(), null)));
                // Toast.makeText(MainActivity.this, "Permission Canceled, Now your application cannot access CONTACTS.", Toast.LENGTH_LONG).show();
            }
        }
    }
    public static class UserListDataAdapter extends RecyclerView.Adapter<UserListDataAdapter.InviteListDataclass>
            implements Filterable {

        int last_postion=0;
        public Activity mCtx;
        private final Context mcntx;
        private List<InviteListData> userDetails;
        private List<InviteListData> userDetailsfull;
        String second_latter="";
        String current_latter="",image_url="";
        private Filter exampleFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<InviteListData> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(userDetailsfull);
                } else {
                    String userName = constraint.toString().toLowerCase().trim();
                    String userNumber = constraint.toString().toLowerCase().trim();
                    for (InviteListData item : userDetailsfull) {
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
                userDetails.addAll((List<InviteListData>) results.values);
                notifyDataSetChanged();
            }
        };

        public UserListDataAdapter(Activity Ctx, Context mCtx, ArrayList<InviteListData> userDetails) {
            this.mcntx = mCtx;
            this.mCtx = Ctx;
            this.userDetails = userDetails;
            userDetailsfull = new ArrayList<>(userDetails);
        }

        @NonNull
        @Override
        public InviteListDataclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.invite_user_details, parent, false);
            return new InviteListDataclass(view);
        }

        @Override
        public void onBindViewHolder(@NonNull InviteListDataclass holder, int position) {
            InviteListData inviteUserDetails = userDetails.get(position);

            last_postion=position;


            if (inviteUserDetails.getF_latter().equals(""))
            {
                holder.first_latter.setVisibility(View.GONE);
                holder.top_layout.setVisibility(View.GONE);
            }
            else {

                holder.first_latter.setVisibility(View.VISIBLE);
                holder.first_latter.setText(inviteUserDetails.getF_latter());
                holder.top_layout.setVisibility(View.VISIBLE);
                String first_latter=inviteUserDetails.getUserName().substring(0,1).toUpperCase();

                if (second_latter.equals(""))
                {
                    current_latter=first_latter;
                    second_latter=first_latter;
                    holder.first_latter.setVisibility(View.VISIBLE);
                    holder.top_layout.setVisibility(View.VISIBLE);

                }
                else if (second_latter.equals(first_latter))
                {
                    current_latter=second_latter;
                    inviteUserDetails.setF_latter("");
                    holder.first_latter.setVisibility(View.GONE);
                    holder.top_layout.setVisibility(View.GONE);

                }
                else {

                    current_latter=first_latter;
                    second_latter=first_latter;
                    holder.first_latter.setVisibility(View.VISIBLE);
                    holder.top_layout.setVisibility(View.VISIBLE);


                }
            }

            if (inviteUserDetails.getUserImageURL()==null)
            {
                String name =inviteUserDetails.getUserName();
                String add_text="";
                String[] split_data=name.split(" ");
                try {
                    for (int i=0;i<split_data.length;i++)
                    {
                        if (i==0)
                        {
                            add_text=split_data[i].substring(0,1);
                        }
                        else {
                            add_text=add_text+split_data[i].substring(0,1);
                            break;
                        }
                    }
                }
                catch (Exception e)
                {

                }


                holder.no_image.setText(add_text);
                holder.no_image.setVisibility(View.VISIBLE);
                holder.profile_image.setVisibility(View.GONE);
            }
            else {
                Glide.with(mCtx).
                        load(inviteUserDetails.getUserImageURL())
                        .placeholder(R.drawable.shape_primary_circle)
                        .error(R.drawable.shape_primary_circle)
                        .into(holder.profile_image);
                holder.no_image.setVisibility(View.GONE);
                holder.profile_image.setVisibility(View.VISIBLE);
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
            Log.e("Fillter is",new Gson().toJson(exampleFilter));
            return exampleFilter;
        }

        public void updateList(List<InviteListData> list){
            userDetails=list;
            notifyDataSetChanged();
        }

        public  class InviteListDataclass extends RecyclerView.ViewHolder {

            TextView no_image;
            TextView userName, userNumber,first_latter;
            CircleImageView profile_image;
            LinearLayout top_layout;


            public InviteListDataclass(@NonNull View itemView) {
                super(itemView);
                first_latter=itemView.findViewById(R.id.first_latter);
                userName = itemView.findViewById(R.id.username);
                userNumber = itemView.findViewById(R.id.user_number);
                profile_image=itemView.findViewById(R.id.profile_image);
                no_image=itemView.findViewById(R.id.no_image);
                top_layout=itemView.findViewById(R.id.top_layout);
            }

        }

    }

    public void update(String strtext1, View view, FragmentActivity activity){
        filter(strtext1,view,activity);

    }

    private void getAllContect() {
        loadingDialog.showLoadingDialog();
        class GetTasks extends AsyncTask<Void, Void, List<InviteListData>> {
            @Override
            protected List<InviteListData> doInBackground(Void... voids) {
                List<InviteListData> taskList = DatabaseClient
                        .getInstance(getActivity())
                        .getAppDatabase()
                        .taskDao()
                        .getvalue1();

                return taskList;
            }

            @Override
            protected void onPostExecute(List<InviteListData> tasks) {
                if (tasks.size()==0)
                {
                    loadingDialog.cancelLoading();
                    getAllContect();
                }
                else {
                    loadingDialog.cancelLoading();
                    num_count.setText(tasks.size()+" Contacts");
                    userListDataAdapter = new UserListDataAdapter(getActivity(), getActivity(), (ArrayList<InviteListData>) tasks);
                    rvinviteuserdetails.setAdapter(userListDataAdapter);
                    userListDataAdapter.notifyDataSetChanged();
                    super.onPostExecute(tasks);
                }

            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }




    private void getTasks(InviteListData inser_data) {



        class GetTasks extends AsyncTask<Void, Void, List<InviteListData>> {
            @Override
            protected List<InviteListData> doInBackground(Void... voids) {
                List<InviteListData> taskList = DatabaseClient
                        .getInstance(getActivity())
                        .getAppDatabase()
                        .taskDao()
                        .getvalue();

                return taskList;
            }

            @Override
            protected void onPostExecute(List<InviteListData> tasks) {

                // if (tasks.size()==inviteListData.size()) {
                // Log.e("Size is Same ","Yse");
                boolean found = tasks.stream().anyMatch(p -> p.getUserPhoneNumber().equals(inser_data.getUserPhoneNumber()));
                if (found) {
                    if (tasks.size() == 1) {

                    } else {
                        getdataanme_mobile(inser_data);
                    }

                } else {
                    inser_data.setFlag("Add");
                    getTasks1(inser_data);

                }
                // }
             /*   else {
                    boolean found = tasks.stream().anyMatch(p -> p.getUserPhoneNumber().equals(inser_data.getUserPhoneNumber()));
                    if (found) {
                        delete();
                    }
                    else {
                        getTasks1(inser_data);
                    }


                }*/
                super.onPostExecute(tasks);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }
    public void updatedata(InviteListData inviteListData)
    {
        class UpdateTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(getContext()).getAppDatabase()
                        .taskDao()
                        .updatevalue(inviteListData.getUserName(),inviteListData.getUserPhoneNumber(),inviteListData.getFlag());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }

        UpdateTask ut = new UpdateTask();
        ut.execute();
    }


    public void delete()
    {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(getContext()).getAppDatabase()
                        .taskDao()
                        .deleteDuplicates();
                //.DeleteData(inviteListData.getUserPhoneNumber());
                // .RemoveData();

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                //   Log.e("Delete Task","Yes"+c);
                super.onPostExecute(aVoid);

            }
        }

        DeleteTask ut = new DeleteTask();
        ut.execute();
    }


    private void SetDatainDatabase(InviteListData inviteListData) {



        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(getActivity()).getAppDatabase()
                        .taskDao()
                        .insert(inviteListData);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }

        SaveTask st = new SaveTask();
        st.execute();
    }



    private void getTasks1(InviteListData inser_data) {
        class GetTasks extends AsyncTask<Void, Void, List<InviteListData>> {
            @Override
            protected List<InviteListData> doInBackground(Void... voids) {
                List<InviteListData> taskList = DatabaseClient
                        .getInstance(getActivity())
                        .getAppDatabase()
                        .taskDao()
                        .getTask(inser_data.getUserPhoneNumber());

                return taskList;
            }

            @Override
            protected void onPostExecute(List<InviteListData> tasks) {
                // Log.e("Insert Task list Size ", String.valueOf(tasks.size()));
                if (tasks.size()==0)
                {
                    SetDatainDatabase(inser_data);
                }

                super.onPostExecute(tasks);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }





    private final static String[] DATA_COLS = {

            ContactsContract.Data.MIMETYPE,
            ContactsContract.Data.DATA1,//phone number
            ContactsContract.Data.CONTACT_ID
    };

    //Update Contect Number Direct
    public static boolean updateNameAndNumber(final Context context, String number, String newName, String newNumber) {

        if (context == null || number == null || number.trim().isEmpty()) return false;

        if (newNumber != null && newNumber.trim().isEmpty()) newNumber = null;

        if (newNumber == null) return false;


        String contactId = getContactId(context, number);

        if (contactId == null) return false;

        //selection for name
        String where = String.format(
                "%s = '%s' AND %s = ?",
                DATA_COLS[0], //mimetype
                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,
                DATA_COLS[2]/*contactId*/);

        String[] args = {contactId};

        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        operations.add(
                ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(where, args)
                        .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, newName)
                        .build()
        );

        //change selection for number
        where = String.format(
                "%s = '%s' AND %s = ?",
                DATA_COLS[0],//mimetype
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                DATA_COLS[1]/*number*/);

        //change args for number
        args[0] = number;

        operations.add(
                ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(where, args)
                        .withValue(DATA_COLS[1]/*number*/, newNumber)
                        .build()
        );

        try {

            ContentProviderResult[] results = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, operations);

            for (ContentProviderResult result : results) {
                Log.e("Upadte Contect",result.toString());

                Log.d("Update Result", result.toString());
            }

            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    public static String getContactId(Context context, String number) {

        if (context == null) return null;

        Cursor cursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.NUMBER + "=?",
                new String[]{number},
                null
        );

        if (cursor == null || cursor.getCount() == 0) return null;

        cursor.moveToFirst();

        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));

        cursor.close();
        return id;
    }



    private void getdataanme_mobile(InviteListData inser_data) {
        // c=c+1;
        // Log.e("C is ", String.valueOf(c));

        class GetTasks extends AsyncTask<Void, Void, List<InviteListData>> {
            @Override
            protected List<InviteListData> doInBackground(Void... voids) {
                List<InviteListData> taskList = DatabaseClient
                        .getInstance(getActivity())
                        .getAppDatabase()
                        .taskDao()
                        .getTaskUpdate(inser_data.getUserPhoneNumber(),inser_data.getUserName());

                return taskList;
            }

            @Override
            protected void onPostExecute(List<InviteListData> tasks) {

                if (tasks.size()==0)
                {
                    inser_data.setFlag("Update");
                    getdataanme_mobile1(inser_data);
                    Log.e("Event Update ","Call");
                }
                //One more then Remove Contect
                else if (tasks.size()<2){
                   /* boolean found = tasks.stream().anyMatch(p -> p.getUserPhoneNumber().equals(inser_data.getUserPhoneNumber()));
                    if (found)
                    {

                    }*/
                    delete();

                }



                super.onPostExecute(tasks);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    private void getdataanme_mobile1(InviteListData inser_data) {

        class GetTasks extends AsyncTask<Void, Void, List<InviteListData>> {
            @Override
            protected List<InviteListData> doInBackground(Void... voids) {
                List<InviteListData> taskList = DatabaseClient
                        .getInstance(getActivity())
                        .getAppDatabase()
                        .taskDao()
                        .getTaskUpdate1(inser_data.getUserPhoneNumber(),inser_data.getUserName());

                return taskList;
            }

            @Override
            protected void onPostExecute(List<InviteListData> tasks) {
                Log.e("Update Data Event ","CAll"+tasks.size());
                if (tasks.size()==0)
                {
                    inser_data.setFlag("Add");
                    Log.e("Insert Flag",inser_data.getFlag());
                    SetDatainDatabase(inser_data);
                }
                else {
                    inser_data.setFlag("Update");
                    updatedata(inser_data);
                }


                super.onPostExecute(tasks);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }



}

