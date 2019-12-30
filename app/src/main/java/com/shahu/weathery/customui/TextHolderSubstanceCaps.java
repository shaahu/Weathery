package com.shahu.weathery.customui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.core.content.res.ResourcesCompat;

import com.shahu.weathery.R;

/**
 * Created by Shahu Ronghe on 30, December, 2019
 * in Weathery
 */
public class TextHolderSubstanceCaps extends androidx.appcompat.widget.AppCompatTextView {
    Context mContext;

    public TextHolderSubstanceCaps(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public TextHolderSubstanceCaps(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public TextHolderSubstanceCaps(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        Typeface typeface = ResourcesCompat.getFont(mContext, R.font.substance_light);
        this.setTypeface(typeface);
        this.setAllCaps(true);
        this.setTextSize(20);
    }
}
