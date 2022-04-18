package com.contactninja;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

public class WrapContentViewPager extends ViewPager {
    public WrapContentViewPager (Context context) {
        super(context);
    }

    public WrapContentViewPager (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        View child = getChildAt(getCurrentItem());

        if (child != null) {
            //Log.e("Cueent uitem", String.valueOf(getCurrentItem()));
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY);
        }
        else {
            /*int height=0;
            if (getCurrentItem()==2)
            {


                View child1 = getChildAt(2);
                child1.measure(200, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int childMeasuredHeight = child1.getMeasuredHeight();
                if (childMeasuredHeight > height) {
                    height = childMeasuredHeight;
                }
            }
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
*/



        }






        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}