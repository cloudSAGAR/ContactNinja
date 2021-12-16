package com.contactninja.Fragment.Broadcast_Frgment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.contactninja.Model.Broadcast_image_list;
import com.contactninja.R;
import com.contactninja.Utils.Global;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class Broadcst_Activty extends AppCompatActivity implements View.OnClickListener {
    TabLayout tabLayout;
    ViewPager viewPager;
    EditText contect_search;
    String strtext = "";
    ViewpaggerAdapter adapter;
    ImageView search_icon;
    ImageView iv_back, iv_more;
    TextView save_button;
    List<Broadcast_image_list> broadcast_image_list=new ArrayList<>();
    CardListAdepter cardListAdepter;

    LinearLayout main_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcst_activty);
        IntentUI();


        tabLayout.addTab(tabLayout.newTab().setText("Contacts"));
        tabLayout.addTab(tabLayout.newTab().setText("Groups"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new ViewpaggerAdapter(getApplicationContext(), getSupportFragmentManager(),
                tabLayout.getTabCount(), strtext);

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contect_search.requestFocus();
            }
        });

    }

    private void IntentUI() {
        main_layout = findViewById(R.id.main_layout);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        contect_search = findViewById(R.id.contect_search);
        search_icon = findViewById(R.id.search_icon);
        iv_back = findViewById(R.id.iv_back);
        save_button = findViewById(R.id.save_button);
        iv_more = findViewById(R.id.iv_more);
        iv_more.setVisibility(View.GONE);
        iv_back.setOnClickListener(this);
        save_button.setOnClickListener(this);
        save_button.setVisibility(View.VISIBLE);
        save_button.setText("Next");
        save_button.setTextColor(getResources().getColor(R.color.purple_200));
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_back:
                finish();

                break;
            case R.id.save_button:


                final View mView = getLayoutInflater().inflate(R.layout.brodcaste_link_dialog_item, null);
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Broadcst_Activty.this, R.style.DialogStyle);
                bottomSheetDialog.setContentView(mView);
                TextView tv_text_link = bottomSheetDialog.findViewById(R.id.tv_text_link);
                ImageView iv_card_list = bottomSheetDialog.findViewById(R.id.iv_card_list);
                ImageView iv_link_icon = bottomSheetDialog.findViewById(R.id.iv_link_icon);
                ImageView iv_selected = bottomSheetDialog.findViewById(R.id.iv_selected);
                LinearLayout lay_link_copy = bottomSheetDialog.findViewById(R.id.lay_link_copy);
                RecyclerView rv_image_card = bottomSheetDialog.findViewById(R.id.rv_image_card);
                EditText edit_message = bottomSheetDialog.findViewById(R.id.edit_message);

                edit_message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iv_card_list.setImageResource(R.drawable.ic_card_blank);
                        rv_image_card.setVisibility(View.GONE);
                        iv_card_list.setSelected(false);
                    }
                });



                broadcast_image_list.clear();
                for (int i=0;i<=20;i++){
                    Broadcast_image_list item=new Broadcast_image_list();
                    if(i%2 == 0){
                        item.setImagename("card_1");
                    }else {
                        item.setImagename("card_2");
                    }
                    broadcast_image_list.add(item);
                }
                rv_image_card.setLayoutManager(new LinearLayoutManager(Broadcst_Activty.this,
                        LinearLayoutManager.HORIZONTAL, false));
                rv_image_card.setHasFixedSize(true);
                cardListAdepter = new CardListAdepter(Broadcst_Activty.this, broadcast_image_list,iv_selected);
                rv_image_card.setAdapter(cardListAdepter);


                lay_link_copy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iv_link_icon.setImageResource(R.drawable.ic_link_dark);
                        tv_text_link.setTextColor(getResources().getColor(R.color.purple_200));
                        tv_text_link.setText(getResources().getString(R.string.link_click));
                    }
                });
                iv_card_list.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v.isSelected()) {


                                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);

                            iv_card_list.setImageResource(R.drawable.ic_card_blank);
                            rv_image_card.setVisibility(View.GONE);
                            iv_card_list.setSelected(false);
                        }else {
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);

                            iv_card_list.setImageResource(R.drawable.ic_card_fill);
                            rv_image_card.setVisibility(View.VISIBLE);
                            iv_card_list.setSelected(true);
                        }
                    }
                });


                bottomSheetDialog.show();


                break;
        }
    }

    class ViewpaggerAdapter extends FragmentPagerAdapter {

        Context context;
        int totalTabs;
        String strtext1;

        public ViewpaggerAdapter(Context c, FragmentManager fm, int totalTabs, String strtext1) {
            super(fm);
            context = c;
            this.totalTabs = totalTabs;
            this.strtext1 = strtext1;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    Broadcste_Contect_Fragment contectFragment = new Broadcste_Contect_Fragment();
                    return contectFragment;

                case 1:
                    Broadcast_Group_Fragment c_Fragment = new Broadcast_Group_Fragment();
                    return c_Fragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return totalTabs;
        }
    }

}

class CardListAdepter extends RecyclerView.Adapter<CardListAdepter.CardListData> {

    Broadcast_image_list item;
    List<Broadcast_image_list> broadcast_image_list;
    Activity activity;
    ImageView iv_selected;

    public CardListAdepter(Activity activity, List<Broadcast_image_list> broadcast_image_list,ImageView iv_selected) {
        this.activity = activity;
        this.broadcast_image_list = broadcast_image_list;
        this.iv_selected = iv_selected;
    }

    @NonNull
    @Override
    public CardListData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list, parent, false);
        return new CardListData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardListData holder, int position) {
        item = broadcast_image_list.get(position);


        int resID = activity.getResources().getIdentifier(item.getImagename()
                .replace(" ", "_").toLowerCase(), "drawable", activity.getPackageName());
        if (resID != 0) {
            Glide.with(activity.getApplicationContext()).load(resID).into(holder.iv_card);
        }
        holder.itemView.setOnClickListener(v -> {
            holder.layout_select_image.setBackgroundResource(R.drawable.shape_blue_10);
            item.setScelect(true);
            int resID1 = activity.getResources().getIdentifier(item.getImagename()
                    .replace(" ", "_").toLowerCase(), "drawable", activity.getPackageName());
            if (resID1 != 0) {
                Glide.with(activity.getApplicationContext()).load(resID1).into(iv_selected);
            }
            notifyDataSetChanged();
        });
        if(item.isScelect()){
            holder.layout_select_image.setBackgroundResource(R.drawable.shape_blue_10);
        }else {
            holder.layout_select_image.setBackground(null);
        }



    }

    @Override
    public int getItemCount() {
        return broadcast_image_list.size();
    }

    public class CardListData extends RecyclerView.ViewHolder {
        ImageView iv_card;
        LinearLayout layout_select_image;

        public CardListData(@NonNull View itemView) {
            super(itemView);
            iv_card = itemView.findViewById(R.id.iv_card);
            layout_select_image = itemView.findViewById(R.id.layout_select_image);
        }
    }
}