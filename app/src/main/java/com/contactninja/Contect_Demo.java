package com.contactninja;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.contactninja.Model.Contactdetail;
import com.contactninja.Model.ContectListData;
import com.contactninja.Utils.PaginationListener;
import com.contactninja.Utils.SessionManager;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.contactninja.Utils.PaginationListener.PAGE_START;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle")

public class Contect_Demo extends AppCompatActivity {
TextView save;
EditText editText;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@SuppressLint("UnknownNullness") Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contect_demo);
        save=findViewById(R.id.save);

        editText=findViewById(R.id.editText);
      //  stylesBar.setStylesList(new MarkdownEditText.TextStyle[]{MarkdownEditText.TextStyle.BOLD, MarkdownEditText.TextStyle.ITALIC,MarkdownEditText.TextStyle.LINK});


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int startSelection=editText.getSelectionStart();
                int endSelection=editText.getSelectionEnd();

                if (startSelection==endSelection)
                {
                    /*String selectedText = "<b>"+editText.getText().toString().substring(startSelection, endSelection)+"</b>";
                    Toast.makeText(getApplicationContext(),selectedText,Toast.LENGTH_LONG).show();
                    Log.e("String is",selectedText);
                    StringBuilder stringBuilder = new StringBuilder(editText.getText().toString());
                    System.out.println("String :- " + stringBuilder);

                    stringBuilder.replace(startSelection, endSelection, selectedText);
                    System.out.println("After Replace :- " + stringBuilder);
                    editText.setText(Html.fromHtml(stringBuilder.toString()));
*/
                }
                else {
                    String selectedText = "<b>"+Html.fromHtml(editText.getText().toString()).toString().substring(startSelection, endSelection)+"</b>";
                    Toast.makeText(getApplicationContext(),selectedText,Toast.LENGTH_LONG).show();
                    Log.e("String is",selectedText);
                    StringBuilder stringBuilder = new StringBuilder(Html.fromHtml(editText.getText().toString()));
                    System.out.println("String :- " + stringBuilder);

                    stringBuilder.replace(startSelection, endSelection, selectedText);
                    System.out.println("After Replace :- " + stringBuilder);
                    editText.setText(Html.fromHtml(stringBuilder.toString()));
                }

            }
        });


    }
}
