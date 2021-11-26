package com.intricare.test.Fragment.AddContect_Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.intricare.test.Model.WorkTypeData;
import com.intricare.test.R;

import java.util.ArrayList;
import java.util.List;

import ru.rambler.libs.swipe_layout.SwipeLayout;


public class InformationFragment extends Fragment {



    public InformationFragment() {
        // Required empty public constructor
    }
    EditText tv_mobile_no,ev_email,ev_address,ev_city,ev_zip,ev_zoom,ev_note;
    LinearLayout select_label,select_email_label,select_state,add_mobile_Number,
            layout_phone,layout_email,layout_mobile,delete_layout;
    TextView tv_phone;
    ImageView pulse_icon,pulse_icon1;
    SwipeLayout swipe_layout;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_information, container, false);
        IntentUI(view);


        select_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog_For_Home();
            }
        });
        tv_mobile_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout_phone.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ev_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                layout_email.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        swipe_layout.setOnSwipeListener(new SwipeLayout.OnSwipeListener() {
            @Override
            public void onBeginSwipe(SwipeLayout swipeLayout, boolean moveToRight) {
               // Log.e("Swipe Call ","MOveto right");
                if (delete_layout.getVisibility()==View.GONE)
                {
                    delete_layout.setVisibility(View.VISIBLE);
                }
                else {
                    delete_layout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSwipeClampReached(SwipeLayout swipeLayout, boolean moveToRight) {
                Log.e("Swipe Call ","MOveto right1");

            }

            @Override
            public void onLeftStickyEdge(SwipeLayout swipeLayout, boolean moveToRight) {
                Log.e("Swipe Call ","Left");
            }

            @Override
            public void onRightStickyEdge(SwipeLayout swipeLayout, boolean moveToRight) {
                Log.e("Swipe Call ","Right");

            }
        });
        return view;
    }

    private void IntentUI(View view) {
        tv_phone=view.findViewById(R.id.tv_phone);
        tv_mobile_no=view.findViewById(R.id.tv_mobile_no);
        select_label=view.findViewById(R.id.select_label);
        select_email_label=view.findViewById(R.id.select_email_label);
        ev_email=view.findViewById(R.id.ev_email);
        ev_address=view.findViewById(R.id.ev_address);
        ev_city=view.findViewById(R.id.ev_city);
        select_state=view.findViewById(R.id.select_state);
        ev_zip=view.findViewById(R.id.ev_zip);
        ev_zoom=view.findViewById(R.id.ev_zoom);
        ev_note=view.findViewById(R.id.ev_note);
        add_mobile_Number=view.findViewById(R.id.add_mobile_Number);
        pulse_icon=view.findViewById(R.id.pulse_icon);
        pulse_icon.setColorFilter(getActivity().getColor(R.color.purple_200));
        layout_phone=view.findViewById(R.id.layout_phone);
        pulse_icon1=view.findViewById(R.id.pulse_icon1);
        pulse_icon1.setColorFilter(getActivity().getColor(R.color.purple_200));
        layout_email=view.findViewById(R.id.layout_email);
        layout_mobile=view.findViewById(R.id.layout_mobile);
        swipe_layout=view.findViewById(R.id.swipe_layout);
        delete_layout=view.findViewById(R.id.delete_layout);

    }


    private void showBottomSheetDialog_For_Home() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(),R.style.BottomSheetDialog);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_for_home);
        RecyclerView home_type_list=bottomSheetDialog.findViewById(R.id.home_type_list);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        home_type_list.setLayoutManager(layoutManager);

        WorkTypeData nameList = new WorkTypeData("1","Home","true");
        WorkTypeData nameList1 = new WorkTypeData("1","Main","true");
        WorkTypeData nameList2 = new WorkTypeData("1","Work","true");
        WorkTypeData nameList3 = new WorkTypeData("1","Other","true");
        WorkTypeData nameList4= new WorkTypeData("1","Add custom","true");

        List<WorkTypeData> workTypeData=new ArrayList<>();
        workTypeData.add(0,nameList);
        workTypeData.add(1,nameList1);
        workTypeData.add(2,nameList2);
        workTypeData.add(3,nameList3);
        workTypeData.add(4,nameList4);



        WorkAdapter workAdapter=new WorkAdapter(getActivity(),workTypeData);
        home_type_list.setAdapter(workAdapter);

        bottomSheetDialog.show();
    }


    public class WorkAdapter extends RecyclerView.Adapter<WorkAdapter.InviteListDataclass> {

        public Context mCtx;

        private List<WorkTypeData> worklist;
        public WorkAdapter(Context context, List<WorkTypeData> worklist) {
            this.mCtx = context;
            this.worklist = worklist;

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
                    if (WorkData.getTitale().equals("Add custom"))
                    {
                        showAlertDialogButtonClicked(v);
                    }
                    else {

                    }

                }
            });

        }
        @Override
        public int getItemCount() {
            return worklist.size();
        }

        public void updateList(List<WorkTypeData> list){
            worklist = list;
            notifyDataSetChanged();
        }

        public  class InviteListDataclass extends RecyclerView.ViewHolder {
            TextView tv_item;

            public InviteListDataclass(@NonNull View itemView) {
                super(itemView);
                tv_item=itemView.findViewById(R.id.tv_item);
            }

        }

    }


    public void showAlertDialogButtonClicked(View view)
    {

        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(getActivity(),R.style.BottomSheetDialog);
        final View customLayout
                = getLayoutInflater()
                .inflate(
                        R.layout.custom_add_work_layout,
                        null);
        builder.setView(customLayout);
        EditText editText=customLayout.findViewById(R.id.editText);
        TextView tv_cancel=customLayout.findViewById(R.id.tv_cancel);
        TextView tv_add=customLayout.findViewById(R.id.tv_add);
        AlertDialog dialog
                = builder.create();
        dialog.show();
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
}