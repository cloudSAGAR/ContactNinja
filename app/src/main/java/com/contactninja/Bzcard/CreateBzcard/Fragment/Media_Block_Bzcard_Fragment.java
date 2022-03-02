package com.contactninja.Bzcard.CreateBzcard.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.contactninja.Bzcard.Media.Image.Add_image_Activity;
import com.contactninja.Bzcard.Media.PDF.Add_pdf_Activity;
import com.contactninja.Bzcard.Media.Select_Media_Activity;
import com.contactninja.Bzcard.Media.SwipeHelper;
import com.contactninja.Bzcard.Media.Video.Add_Video_Activity;
import com.contactninja.Interface.Bz_MediaClick;
import com.contactninja.Model.Bzcard_Fields_Model;
import com.contactninja.R;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.SessionManager;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Media_Block_Bzcard_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Media_Block_Bzcard_Fragment extends Fragment implements View.OnClickListener, Bz_MediaClick {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Media_Block_Bzcard_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Media_Block_Bzcard_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Media_Block_Bzcard_Fragment newInstance(String param1, String param2) {
        Media_Block_Bzcard_Fragment fragment = new Media_Block_Bzcard_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    RecyclerView rv_color_list,rv_media_list;
    ColorAdepter colorAdepter;
    List<Bzcard_Fields_Model.Bz_color_Model> color_modelList = new ArrayList<>();
    ImageView iv_Cutom_Button, iv_Cutom_Button_other, iv_Add_call, iv_Schedule_a_meeting_radio, iv_Know_more,
            iv_Visit_now, iv_Inquire_now, iv_Learn_more, iv_Custom_HTML, iv_media_title;
    LinearLayout layout_Cutom_Button, layout_Add_call, layout_Schedule_a_meeting, layout_Know_more, layout_Visit_now,
            layout_Inquire_now, layout_Learn_more, layout_Custom_HTML, layout_media, layout_Schedule_a_meeting_edit,
            layout_Know_more_edit, layout_Visit_now_edit, layout_Inquire_now_edit, layout_Learn_more_edit,
            layout_media_list,layout_item_add_new,layout_Cutom_Button_other;
    EditText edt_title_1, edt_add_url_1,edt_title_2, edt_add_url_2, edt_Bio, edt_Add_description, edt_Schedule_a_meeting_url1, edt_Know_more_url, edt_Visit_now_url,
            edt_Inquire_now_url, edt_Learn_more_url, edt_add_Custom_HTML;
    TextView txt_invalid_Schedule_a_meeting, txt_invalid_txt_Know_more, txt_invalid_Visit_now, txt_invalid_Inquire_now, txt_invalid_Learn_more;


    List<Bzcard_Fields_Model.BZ_media_information> bzMediaInformationList = new ArrayList<>();
    public static Bzcard_Fields_Model main_model;
    MedialistAdepter medialistAdepter;
    boolean media_show=true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_media__block__bzcard_, container, false);
         main_model = SessionManager.getBzcard(getActivity());
         bzMediaInformationList= main_model.getBzMediaInformationList();

        IntentView(view);
        setColor();
        if(bzMediaInformationList.size()!=0){
            setCreatedVideoandImage();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setColor();
        if(bzMediaInformationList.size()!=0){
            setCreatedVideoandImage();
        }
    }

    private void setCreatedVideoandImage() {
        iv_media_title.setBackgroundResource(R.drawable.ic_select_on);
        layout_media_list.setVisibility(View.VISIBLE);
        if(bzMediaInformationList.size() != 0||bzMediaInformationList.size()<10){
            layout_item_add_new.setVisibility(View.VISIBLE);
        }else {
            layout_item_add_new.setVisibility(View.GONE);
        }

        SwipeHelper swipeHelper = new SwipeHelper(getActivity()) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {

                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Edit",
                        0,
                        Color.parseColor("#5495EC"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                final Bzcard_Fields_Model.BZ_media_information item = medialistAdepter.getData().get(pos);
                                Intent intent=null;
                                switch (item.getMedia_type()) {
                                    case "video":
                                        intent = new Intent(getActivity(), Add_Video_Activity.class);
                                        break;
                                    case "image":
                                        intent = new Intent(getActivity(), Add_image_Activity.class);
                                        break;
                                    case "pdf":
                                        intent = new Intent(getActivity(), Add_pdf_Activity.class);
                                        break;
                                }
                                intent.putExtra("MyClass", item);
                                startActivity(intent);
                            }
                        }
                ));
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Delete",
                        0,
                        Color.parseColor("#FF3C30"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(final int pos) {
                                final Bzcard_Fields_Model.BZ_media_information item = medialistAdepter.getData().get(pos);
                                medialistAdepter.removeItem(pos,item);

                                Toast.makeText(getContext(), "Item was removed from the list.", Toast.LENGTH_LONG).show();
                            }
                        }
                ));

            }
        };
        swipeHelper.attachToRecyclerView(rv_media_list);

    }

    private void IntentView(View view) {
        rv_color_list = view.findViewById(R.id.rv_color_list);
        iv_Cutom_Button = view.findViewById(R.id.iv_Cutom_Button);
        iv_Cutom_Button_other = view.findViewById(R.id.iv_Cutom_Button_other);
        layout_Cutom_Button = view.findViewById(R.id.layout_Cutom_Button);
        layout_Add_call = view.findViewById(R.id.layout_Add_call);
        iv_Add_call = view.findViewById(R.id.iv_Add_call);
        iv_Schedule_a_meeting_radio = view.findViewById(R.id.iv_Schedule_a_meeting_radio);
        iv_Know_more = view.findViewById(R.id.iv_Know_more);
        iv_Visit_now = view.findViewById(R.id.iv_Visit_now);
        iv_Inquire_now = view.findViewById(R.id.iv_Inquire_now);
        iv_Learn_more = view.findViewById(R.id.iv_Learn_more);
        iv_Custom_HTML = view.findViewById(R.id.iv_Custom_HTML);
        iv_media_title = view.findViewById(R.id.iv_media_title);
        layout_Schedule_a_meeting = view.findViewById(R.id.layout_Schedule_a_meeting);
        layout_Schedule_a_meeting_edit = view.findViewById(R.id.layout_Schedule_a_meeting_edit);
        layout_Know_more = view.findViewById(R.id.layout_Know_more);
        layout_Know_more_edit = view.findViewById(R.id.layout_Know_more_edit);
        layout_Visit_now = view.findViewById(R.id.layout_Visit_now);
        layout_Visit_now_edit = view.findViewById(R.id.layout_Visit_now_edit);
        layout_Inquire_now = view.findViewById(R.id.layout_Inquire_now);
        layout_Inquire_now_edit = view.findViewById(R.id.layout_Inquire_now_edit);
        layout_Learn_more = view.findViewById(R.id.layout_Learn_more);
        layout_Learn_more_edit = view.findViewById(R.id.layout_Learn_more_edit);
        layout_Custom_HTML = view.findViewById(R.id.layout_Custom_HTML);
        layout_media = view.findViewById(R.id.layout_media);
        layout_media_list = view.findViewById(R.id.layout_media_list);
        layout_item_add_new = view.findViewById(R.id.layout_item_add_new);
        rv_media_list = view.findViewById(R.id.rv_media_list);
        layout_Cutom_Button_other = view.findViewById(R.id.layout_Cutom_Button_other);

        edt_title_1 = view.findViewById(R.id.edt_title_1);
        edt_add_url_1 = view.findViewById(R.id.edt_add_url_1);
        edt_title_2 = view.findViewById(R.id.edt_title_2);
        edt_add_url_2 = view.findViewById(R.id.edt_add_url_2);

        edt_Bio = view.findViewById(R.id.edt_Bio);
        edt_Add_description = view.findViewById(R.id.edt_Add_description);
        edt_Schedule_a_meeting_url1 = view.findViewById(R.id.edt_Schedule_a_meeting_url1);
        txt_invalid_Schedule_a_meeting = view.findViewById(R.id.txt_invalid_Schedule_a_meeting);
        edt_Know_more_url = view.findViewById(R.id.edt_Know_more_url);
        txt_invalid_txt_Know_more = view.findViewById(R.id.txt_invalid_txt_Know_more);
        edt_Visit_now_url = view.findViewById(R.id.edt_Visit_now_url);
        txt_invalid_Visit_now = view.findViewById(R.id.txt_invalid_Visit_now);
        edt_Inquire_now_url = view.findViewById(R.id.edt_Inquire_now_url);
        txt_invalid_Inquire_now = view.findViewById(R.id.txt_invalid_Inquire_now);
        edt_Learn_more_url = view.findViewById(R.id.edt_Learn_more_url);
        txt_invalid_Learn_more = view.findViewById(R.id.txt_invalid_Learn_more);
        edt_add_Custom_HTML = view.findViewById(R.id.edt_add_Custom_HTML);

        layout_Schedule_a_meeting.setOnClickListener(this);
        layout_Know_more.setOnClickListener(this);
        layout_Visit_now.setOnClickListener(this);
        layout_Inquire_now.setOnClickListener(this);
        layout_Learn_more.setOnClickListener(this);
        layout_media.setOnClickListener(this);
        layout_item_add_new.setOnClickListener(this);

        iv_Cutom_Button.setBackgroundResource(R.drawable.ic_select_off);
        iv_Cutom_Button_other.setBackgroundResource(R.drawable.ic_select_off);
        iv_Add_call.setBackgroundResource(R.drawable.ic_select_off);
        iv_Custom_HTML.setBackgroundResource(R.drawable.ic_select_off);
        iv_media_title.setBackgroundResource(R.drawable.ic_select_off);
        iv_Schedule_a_meeting_radio.setBackgroundResource(R.drawable.ic_radio_off);
        iv_Know_more.setBackgroundResource(R.drawable.ic_radio_off);
        iv_Visit_now.setBackgroundResource(R.drawable.ic_radio_off);
        iv_Inquire_now.setBackgroundResource(R.drawable.ic_radio_off);
        iv_Learn_more.setBackgroundResource(R.drawable.ic_radio_off);

        iv_Cutom_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView iv = (ImageView) v;
                if (iv.isSelected()) {
                    v.setSelected(false);
                    iv_Cutom_Button.setBackgroundResource(R.drawable.ic_select_off);
                    layout_Cutom_Button.setVisibility(View.GONE);
                } else {
                    v.setSelected(true);
                    layout_Cutom_Button.setVisibility(View.VISIBLE);
                    iv_Cutom_Button.setBackgroundResource(R.drawable.ic_select_on);
                }
            }
        });
        iv_Cutom_Button_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView iv = (ImageView) v;
                if (iv.isSelected()) {
                    v.setSelected(false);
                    iv_Cutom_Button_other.setBackgroundResource(R.drawable.ic_select_off);
                    layout_Cutom_Button_other.setVisibility(View.GONE);
                } else {
                    v.setSelected(true);
                    layout_Cutom_Button_other.setVisibility(View.VISIBLE);
                    iv_Cutom_Button_other.setBackgroundResource(R.drawable.ic_select_on);
                }
            }
        });
        iv_Add_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView iv = (ImageView) v;
                if (iv.isSelected()) {
                    v.setSelected(false);
                    iv_Add_call.setBackgroundResource(R.drawable.ic_select_off);
                    layout_Add_call.setVisibility(View.GONE);
                } else {
                    v.setSelected(true);
                    layout_Add_call.setVisibility(View.VISIBLE);
                    iv_Add_call.setBackgroundResource(R.drawable.ic_select_on);
                }
            }
        });
        iv_Custom_HTML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView iv = (ImageView) v;
                if (iv.isSelected()) {
                    v.setSelected(false);
                    iv_Custom_HTML.setBackgroundResource(R.drawable.ic_select_off);
                    layout_Custom_HTML.setVisibility(View.GONE);
                } else {
                    v.setSelected(true);
                    layout_Custom_HTML.setVisibility(View.VISIBLE);
                    iv_Custom_HTML.setBackgroundResource(R.drawable.ic_select_on);
                }
            }
        });

        edt_title_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                main_model.setButton1_name(edt_title_1.getText().toString().trim());
                SessionManager.setBzcard(getActivity(), main_model);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edt_add_url_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                main_model.setButton1_url(edt_add_url_1.getText().toString().trim());
                SessionManager.setBzcard(getActivity(), main_model);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edt_title_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                main_model.setButton2_name(edt_title_2.getText().toString().trim());
                SessionManager.setBzcard(getActivity(), main_model);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edt_add_url_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                main_model.setButton2_url(edt_add_url_2.getText().toString().trim());
                SessionManager.setBzcard(getActivity(), main_model);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edt_Bio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                main_model.setBio_head(edt_Bio.getText().toString().trim());
                SessionManager.setBzcard(getActivity(), main_model);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edt_Add_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                main_model.setBio_description(edt_Add_description.getText().toString().trim());
                SessionManager.setBzcard(getActivity(), main_model);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edt_add_Custom_HTML.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                main_model.setHtml(edt_add_Custom_HTML.getText().toString().trim());
                SessionManager.setBzcard(getActivity(), main_model);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edt_Schedule_a_meeting_url1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (Global.isValidURL(s.toString()) == true) {
                    txt_invalid_Schedule_a_meeting.setVisibility(View.GONE);
                } else {
                    txt_invalid_Schedule_a_meeting.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edt_Know_more_url.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (Global.isValidURL(s.toString()) == true) {
                    txt_invalid_txt_Know_more.setVisibility(View.GONE);
                } else {
                    txt_invalid_txt_Know_more.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edt_Visit_now_url.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (Global.isValidURL(s.toString()) == true) {
                    txt_invalid_Visit_now.setVisibility(View.GONE);
                } else {
                    txt_invalid_Visit_now.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edt_Inquire_now_url.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (Global.isValidURL(s.toString()) == true) {
                    txt_invalid_Inquire_now.setVisibility(View.GONE);
                } else {
                    txt_invalid_Inquire_now.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edt_Learn_more_url.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (Global.isValidURL(s.toString()) == true) {
                    txt_invalid_Learn_more.setVisibility(View.GONE);
                } else {
                    txt_invalid_Learn_more.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        rv_media_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        medialistAdepter=new MedialistAdepter(getActivity(),bzMediaInformationList,this);
        rv_media_list.setAdapter(medialistAdepter);

    }

    private void setColor() {
        String[] color_list = getContext().getResources().getStringArray(R.array.Select_color);
        for (int i = 0; i < color_list.length; i++) {
            Bzcard_Fields_Model.Bz_color_Model bzColorModel = new Bzcard_Fields_Model.Bz_color_Model();
            bzColorModel.setColorName(color_list[i]);
            if (i == 0) {
                bzColorModel.setIs_Select(true);
            } else {
                bzColorModel.setIs_Select(false);
            }
            color_modelList.add(bzColorModel);
        }
        rv_color_list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        colorAdepter = new ColorAdepter(getActivity(), color_modelList);
        rv_color_list.setAdapter(colorAdepter);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_Schedule_a_meeting:
                iv_Schedule_a_meeting_radio.setBackgroundResource(R.drawable.ic_radio_on);
                iv_Know_more.setBackgroundResource(R.drawable.ic_radio_off);
                iv_Visit_now.setBackgroundResource(R.drawable.ic_radio_off);
                iv_Inquire_now.setBackgroundResource(R.drawable.ic_radio_off);
                iv_Learn_more.setBackgroundResource(R.drawable.ic_radio_off);
                layout_Schedule_a_meeting_edit.setVisibility(View.VISIBLE);
                layout_Know_more_edit.setVisibility(View.GONE);
                layout_Visit_now_edit.setVisibility(View.GONE);
                layout_Inquire_now_edit.setVisibility(View.GONE);
                layout_Learn_more_edit.setVisibility(View.GONE);
                break;
            case R.id.layout_Know_more:
                iv_Schedule_a_meeting_radio.setBackgroundResource(R.drawable.ic_radio_off);
                iv_Know_more.setBackgroundResource(R.drawable.ic_radio_on);
                iv_Visit_now.setBackgroundResource(R.drawable.ic_radio_off);
                iv_Inquire_now.setBackgroundResource(R.drawable.ic_radio_off);
                iv_Learn_more.setBackgroundResource(R.drawable.ic_radio_off);
                layout_Schedule_a_meeting_edit.setVisibility(View.GONE);
                layout_Know_more_edit.setVisibility(View.VISIBLE);
                layout_Visit_now_edit.setVisibility(View.GONE);
                layout_Inquire_now_edit.setVisibility(View.GONE);
                layout_Learn_more_edit.setVisibility(View.GONE);

                break;
            case R.id.layout_Visit_now:
                iv_Schedule_a_meeting_radio.setBackgroundResource(R.drawable.ic_radio_off);
                iv_Know_more.setBackgroundResource(R.drawable.ic_radio_off);
                iv_Visit_now.setBackgroundResource(R.drawable.ic_radio_on);
                iv_Inquire_now.setBackgroundResource(R.drawable.ic_radio_off);
                iv_Learn_more.setBackgroundResource(R.drawable.ic_radio_off);
                layout_Schedule_a_meeting_edit.setVisibility(View.GONE);
                layout_Know_more_edit.setVisibility(View.GONE);
                layout_Visit_now_edit.setVisibility(View.VISIBLE);
                layout_Inquire_now_edit.setVisibility(View.GONE);
                layout_Learn_more_edit.setVisibility(View.GONE);
                break;
            case R.id.layout_Inquire_now:
                iv_Schedule_a_meeting_radio.setBackgroundResource(R.drawable.ic_radio_off);
                iv_Know_more.setBackgroundResource(R.drawable.ic_radio_off);
                iv_Visit_now.setBackgroundResource(R.drawable.ic_radio_off);
                iv_Inquire_now.setBackgroundResource(R.drawable.ic_radio_on);
                iv_Learn_more.setBackgroundResource(R.drawable.ic_radio_off);
                layout_Schedule_a_meeting_edit.setVisibility(View.GONE);
                layout_Know_more_edit.setVisibility(View.GONE);
                layout_Visit_now_edit.setVisibility(View.GONE);
                layout_Inquire_now_edit.setVisibility(View.VISIBLE);
                layout_Learn_more_edit.setVisibility(View.GONE);
                break;
            case R.id.layout_Learn_more:
                iv_Schedule_a_meeting_radio.setBackgroundResource(R.drawable.ic_radio_off);
                iv_Know_more.setBackgroundResource(R.drawable.ic_radio_off);
                iv_Visit_now.setBackgroundResource(R.drawable.ic_radio_off);
                iv_Inquire_now.setBackgroundResource(R.drawable.ic_radio_off);
                iv_Learn_more.setBackgroundResource(R.drawable.ic_radio_on);
                layout_Schedule_a_meeting_edit.setVisibility(View.GONE);
                layout_Know_more_edit.setVisibility(View.GONE);
                layout_Visit_now_edit.setVisibility(View.GONE);
                layout_Inquire_now_edit.setVisibility(View.GONE);
                layout_Learn_more_edit.setVisibility(View.VISIBLE);
                break;
            case R.id.layout_media:
                if(bzMediaInformationList.size()==0){
                    startActivity(new Intent(getActivity(), Select_Media_Activity.class));
                    layout_media_list.setVisibility(View.GONE);
                    iv_media_title.setBackgroundResource(R.drawable.ic_select_off);
                }else {
                    setCreatedVideoandImage();
                    if(media_show){
                        media_show=false;
                        layout_media_list.setVisibility(View.GONE);
                        iv_media_title.setBackgroundResource(R.drawable.ic_select_off);
                    }else {
                        media_show=true;
                        layout_media_list.setVisibility(View.VISIBLE);
                        iv_media_title.setBackgroundResource(R.drawable.ic_select_on);
                    }
                }
                break;
                case R.id.layout_item_add_new:
                    startActivity(new Intent(getActivity(), Select_Media_Activity.class));
                break;
        }
    }

    @Override
    public void OnVideoClick(Bzcard_Fields_Model.BZ_media_information information) {
        Intent intent = null;
        switch (information.getMedia_type()) {
            case "video":
                intent = new Intent(getActivity(), Add_Video_Activity.class);
                break;
            case "image":

                intent = new Intent(getActivity(), Add_image_Activity.class);
                break;
            case "pdf":

                intent = new Intent(getActivity(), Add_pdf_Activity.class);
                break;
        }
        intent.putExtra("MyClass", information);
        startActivity(intent);
    }


    public static class ColorAdepter extends RecyclerView.Adapter<ColorAdepter.viewholder> {

        public Context mCtx;
        List<Bzcard_Fields_Model.Bz_color_Model> color_modelList;

        public ColorAdepter(Context applicationContext, List<Bzcard_Fields_Model.Bz_color_Model> color_modelList) {
            this.mCtx = applicationContext;
            this.color_modelList = color_modelList;
        }

        @NonNull
        @Override
        public ColorAdepter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.color_item_bzcard, parent, false);
            return new ColorAdepter.viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ColorAdepter.viewholder holder, int position) {
            Bzcard_Fields_Model.Bz_color_Model item = color_modelList.get(position);
            setBackground(holder.cv_main, item.getColorName(), mCtx);
            if (item.isIs_Select()) {
                holder.iv_select.setVisibility(View.VISIBLE);
            } else {
                holder.iv_select.setVisibility(View.GONE);
            }
            holder.cv_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < color_modelList.size(); i++) {
                        if (color_modelList.get(i).isIs_Select()) {
                            color_modelList.get(i).setIs_Select(false);
                            break;
                        }
                    }
                    item.setIs_Select(true);
                    main_model.setTheme("#"+item.getColorName());
                    SessionManager.setBzcard(mCtx, main_model);
                    notifyDataSetChanged();
                }
            });
        }

        private static void setBackground(CardView layout_item, String color, Context mCtx) {
            switch (color) {
                case "5495EC":
                    layout_item.setCardBackgroundColor(mCtx.getResources().getColor(R.color.bz_5495EC));
                    break;
                case "F97C74":
                    layout_item.setCardBackgroundColor(mCtx.getResources().getColor(R.color.bz_F97C74));
                    break;
                case "3A59AE":
                    layout_item.setCardBackgroundColor(mCtx.getResources().getColor(R.color.bz_3A59AE));
                    break;
                case "EAC19B":
                    layout_item.setCardBackgroundColor(mCtx.getResources().getColor(R.color.bz_EAC19B));
                    break;
                case "0DC0C0":
                    layout_item.setCardBackgroundColor(mCtx.getResources().getColor(R.color.bz_0DC0C0));
                    break;
                case "6ECF41":
                    layout_item.setCardBackgroundColor(mCtx.getResources().getColor(R.color.bz_6ECF41));
                    break;
                case "864879":
                    layout_item.setCardBackgroundColor(mCtx.getResources().getColor(R.color.bz_864879));
                    break;
                case "CF8A39":
                    layout_item.setCardBackgroundColor(mCtx.getResources().getColor(R.color.bz_CF8A39));
                    break;
                case "4A4A4A":
                    layout_item.setCardBackgroundColor(mCtx.getResources().getColor(R.color.bz_4A4A4A));
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return color_modelList.size();
        }

        public static class viewholder extends RecyclerView.ViewHolder {
            View layout_item;
            ImageView iv_select;
            CardView cv_main;

            public viewholder(View view) {
                super(view);
                layout_item = view.findViewById(R.id.layout_item);
                iv_select = view.findViewById(R.id.iv_select);
                cv_main = view.findViewById(R.id.cv_main);

            }
        }
    }

    public static class MedialistAdepter extends RecyclerView.Adapter<MedialistAdepter.viewholder> {

        public Context mCtx;
        List<Bzcard_Fields_Model.BZ_media_information> bzMediaInformationList;
        Bz_MediaClick videoClick;
        public MedialistAdepter(Context applicationContext, List<Bzcard_Fields_Model.BZ_media_information> bzMediaInformationList,
                                Bz_MediaClick videoClick) {
            this.mCtx = applicationContext;
            this.bzMediaInformationList = bzMediaInformationList;
            this.videoClick = videoClick;
        }

        @NonNull
        @Override
        public MedialistAdepter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_bzcard_midea, parent, false);
            return new MedialistAdepter.viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MedialistAdepter.viewholder holder, int position) {
            Bzcard_Fields_Model.BZ_media_information information=bzMediaInformationList.get(position);

            if(information.getIs_featured()==1){
                holder.iv_Featured.setVisibility(View.VISIBLE);
            }else {
                holder.iv_Featured.setVisibility(View.GONE);
            }
            switch (information.getMedia_type()) {
                case "video":
                    Glide.with(mCtx)
                            .load(Global.getYoutubeThumbnailUrlFromVideoUrl(information.getMedia_url()))
                            .into(holder.iv_video);

                    holder.layout_pdf.setVisibility(View.GONE);
                    holder.layout_video_image.setVisibility(View.VISIBLE);
                    break;
                case "pdf":
                    holder.layout_pdf.setVisibility(View.VISIBLE);
                    holder.layout_video_image.setVisibility(View.GONE);
                    break;
                case "image":

                    holder.layout_pdf.setVisibility(View.GONE);
                    holder.layout_video_image.setVisibility(View.VISIBLE);
                    Glide.with(mCtx)
                            .load(information.getMedia_url())
                            .into(holder.iv_video);
                    break;
            }
            holder.txt_title.setText(information.getMedia_title());
            holder.txt_dicription.setText(information.getMedia_description());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoClick.OnVideoClick(information);
                }
            });

        }
        public void removeItem(int position, Bzcard_Fields_Model.BZ_media_information item) {
            for (int i = 0; i < bzMediaInformationList.size(); i++) {
                if (bzMediaInformationList.get(i).getId().equals(item.getId())) {
                    bzMediaInformationList.remove(i);
                    main_model.setBzMediaInformationList(bzMediaInformationList);
                    SessionManager.setBzcard(mCtx, main_model);
                    break;
                }
            }
            notifyItemRemoved(position);
        }

        public void restoreItem(Bzcard_Fields_Model.BZ_media_information item, int position) {
            bzMediaInformationList.add(position, item);
            notifyItemInserted(position);
        }

        public List<Bzcard_Fields_Model.BZ_media_information> getData() {
            return bzMediaInformationList;
        }

        @Override
        public int getItemCount() {
            return bzMediaInformationList.size();
        }

        public static class viewholder extends RecyclerView.ViewHolder {
            RoundedImageView iv_video;
            TextView txt_title,txt_dicription;
            LinearLayout layout_swap,layout_item;
            ImageView iv_Featured;
            RelativeLayout layout_pdf,layout_video_image;

            public viewholder(View view) {
                super(view);
                iv_video = view.findViewById(R.id.iv_video);
                txt_title = view.findViewById(R.id.txt_title);
                txt_dicription = view.findViewById(R.id.txt_dicription);
                layout_swap = view.findViewById(R.id.layout_swap);
                iv_Featured = view.findViewById(R.id.iv_Featured);
                layout_item = view.findViewById(R.id.layout_item);
                layout_pdf = view.findViewById(R.id.layout_pdf);
                layout_video_image = view.findViewById(R.id.layout_video_image);
            }
        }
    }
}