package com.contactninja.ARE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.Util;
import com.chinalwb.are.styles.IARE_Style;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Abstract;
import com.chinalwb.are.styles.toolitems.IARE_ToolItem_Updater;
import com.chinalwb.are.styles.toolitems.styles.ARE_Style_Link;
import com.contactninja.R;
@SuppressLint("SimpleDateFormat,StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class ARE_ToolItem_Link extends ARE_ToolItem_Abstract {

    @Override
    public IARE_ToolItem_Updater getToolItemUpdater() {
        return null;
    }

    @Override
    public IARE_Style getStyle() {
        if (mStyle == null) {
            AREditText editText = this.getEditText();
            mStyle = new ARE_Style_Link(editText, (ImageView) mToolItemView);
        }
        return mStyle;
    }

    @Override
    public View getView(Context context) {
        if (null == context) {
            return mToolItemView;
        }
        if (mToolItemView == null) {
            ImageView imageView = new ImageView(context);
            int size = Util.getPixelByDp(context, 25);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
            imageView.setLayoutParams(params);
            imageView.setImageResource(R.drawable.link);
            imageView.bringToFront();
            mToolItemView = imageView;
        }

        return mToolItemView;
    }

    @Override
    public void onSelectionChanged(int selStart, int selEnd) {
        return;
    }
}
