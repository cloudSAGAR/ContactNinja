package com.contactninja.ARE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.Constants;
import com.chinalwb.are.Util;
import com.chinalwb.are.styles.IARE_Style;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Abstract;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_UpdaterDefault;
import com.chinalwb.are.styles.toolitems.IARE_ToolItem_Updater;
import com.chinalwb.are.styles.toolitems.styles.ARE_Style_Italic;
import com.contactninja.R;
@SuppressLint("SimpleDateFormat,StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class ARE_ToolItem_Italic extends ARE_ToolItem_Abstract {

    @Override
    public IARE_ToolItem_Updater getToolItemUpdater() {
        if (mToolItemUpdater == null) {
            mToolItemUpdater = new ARE_ToolItem_UpdaterDefault(this, Color.parseColor("#5495EC"), Constants.UNCHECKED_COLOR);
            setToolItemUpdater(mToolItemUpdater);
        }
        return mToolItemUpdater;
    }

    @Override
    public IARE_Style getStyle() {
        if (mStyle == null) {
            AREditText editText = this.getEditText();
            IARE_ToolItem_Updater toolItemUpdater = getToolItemUpdater();
            mStyle = new ARE_Style_Italic(editText, (ImageView) mToolItemView, toolItemUpdater);
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
            imageView.setImageResource(R.drawable.italic);
            imageView.bringToFront();
            mToolItemView = imageView;
        }

        return mToolItemView;
    }

    @Override
    public void onSelectionChanged(int selStart, int selEnd) {
        boolean italicExists = false;

        AREditText editText = this.getEditText();
        Editable editable = editText.getEditableText();
        if (selStart > 0 && selStart == selEnd) {
            CharacterStyle[] styleSpans = editable.getSpans(selStart - 1, selStart, CharacterStyle.class);

            for (int i = 0; i < styleSpans.length; i++) {
                if (styleSpans[i] instanceof StyleSpan) {
                    if (((StyleSpan) styleSpans[i]).getStyle() == Typeface.ITALIC) {
                        italicExists = true;
                    }
                }
            }
        } else {
            //
            // Selection is a range
            CharacterStyle[] styleSpans = editable.getSpans(selStart, selEnd, CharacterStyle.class);

            for (int i = 0; i < styleSpans.length; i++) {

                if (styleSpans[i] instanceof StyleSpan) {
                    if (((StyleSpan) styleSpans[i]).getStyle() == android.graphics.Typeface.ITALIC) {
                        if (editable.getSpanStart(styleSpans[i]) <= selStart
                                && editable.getSpanEnd(styleSpans[i]) >= selEnd) {
                            italicExists = true;
                        }
                    } else if (((StyleSpan) styleSpans[i]).getStyle() == android.graphics.Typeface.BOLD_ITALIC) {
                        if (editable.getSpanStart(styleSpans[i]) <= selStart
                                && editable.getSpanEnd(styleSpans[i]) >= selEnd) {
                            italicExists = true;
                        }
                    }
                }
            }
        }

        mToolItemUpdater.onCheckStatusUpdate(italicExists);
    }
}
