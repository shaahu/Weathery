package com.shahu.weathery.customui

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.shahu.weathery.R

/**
 * Created by Shahu Ronghe on 30, December, 2019
 * in Weathery
 */
class TextHolderRegular : AppCompatTextView {
    private var mContext: Context

    constructor(context: Context) : super(context) {
        mContext = context
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mContext = context
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mContext = context
        init()
    }

    private fun init() {
        val typeface = ResourcesCompat.getFont(mContext, R.font.hg_reg)
        this.typeface = typeface
    }
}