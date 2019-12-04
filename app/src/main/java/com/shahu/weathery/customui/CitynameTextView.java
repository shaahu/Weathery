package com.shahu.weathery.customui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.core.content.res.ResourcesCompat;

import com.shahu.weathery.R;

/**
 * Created by Shahu Ronghe on 04, December, 2019
 * in Weathery
 */
public class CitynameTextView extends androidx.appcompat.widget.AppCompatTextView {

    Context mContext;

    public CitynameTextView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public CitynameTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public CitynameTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }



    private void init() {
        Typeface typeface = ResourcesCompat.getFont(mContext,R.font.lato_black);
        this.setTypeface(typeface);
    }
}
