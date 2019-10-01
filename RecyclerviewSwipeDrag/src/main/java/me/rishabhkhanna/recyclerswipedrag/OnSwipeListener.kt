package me.rishabhkhanna.recyclerswipedrag

import android.support.v7.widget.RecyclerView

/**
 * Created by rishabhkhanna on 15/11/17.
 */
interface OnSwipeListener {
    fun onSwipeItemListener(oldPosition: RecyclerView.ViewHolder?)
}