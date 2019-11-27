package com.shahu.weathery.helper;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.shahu.weathery.interface2.OnDragListener;
import com.shahu.weathery.interface2.OnSwipeListener;

import java.util.ArrayList;
import java.util.Collections;


/**
 * Created by Shahu Ronghe on 27, November, 2019
 * in Weathery
 */
public class RecyclerViewItemHelper<T> extends ItemTouchHelper.Callback {
    private static final String TAG = "RecyclerViewItemHelper";
    private OnDragListener onDragListener;
    private OnSwipeListener onSwipeListener;
    private boolean isItemDragEnabled;
    private boolean isItemSwipeEnabled;
    private ArrayList list;
    private RecyclerView.Adapter mAdapter;

    public RecyclerViewItemHelper(ArrayList list, RecyclerView.Adapter mAdapter) {
        super();
        this.list = list;
        this.mAdapter = mAdapter;
    }

    public final OnDragListener getOnDragListener() {
        return this.onDragListener;
    }

    public final void setOnDragListener(OnDragListener var1) {
        this.onDragListener = var1;
    }

    public final OnSwipeListener getOnSwipeListener() {
        return this.onSwipeListener;
    }

    public final void setOnSwipeListener(OnSwipeListener var1) {
        this.onSwipeListener = var1;
    }

    public final void onMoved(int fromPos, int toPos) {
        this.list.remove(toPos);
        this.mAdapter.notifyItemRemoved(toPos);
    }

    public final void onItemMoved(int fromPosition, int toPosition) {
        Collections.swap(this.list, fromPosition, toPosition);
        this.mAdapter.notifyItemMoved(fromPosition, toPosition);
    }

    public int getMovementFlags(@Nullable RecyclerView recyclerView, @Nullable RecyclerView.ViewHolder viewHolder) {
        int dragFlags = 0;
        int swipeFlags = 0;
        if (this.isItemDragEnabled) {
            dragFlags = 3;
        }

        if (this.isItemSwipeEnabled) {
            swipeFlags = 12;
        }

        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags);
    }

    public boolean onMove(@Nullable RecyclerView recyclerView, @Nullable RecyclerView.ViewHolder viewHolder, @Nullable RecyclerView.ViewHolder target) {
        if (viewHolder == null) {
            Log.d(TAG, "onMove: viewHolder is null");
            return false;
        }

        int adapterPosition = viewHolder.getAdapterPosition();
        if (target == null) {
            Log.d(TAG, "onMove: target viewHolder is null");
            return false;
        }

        this.onItemMoved(adapterPosition, target.getAdapterPosition());
        OnDragListener onDragListener = this.onDragListener;
        if (onDragListener != null) {
            onDragListener.onDragItemListener(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        }

        return true;
    }

    public void onSwiped(@Nullable RecyclerView.ViewHolder viewHolder, int direction) {
        if (viewHolder == null) {
            Log.d(TAG, "onSwiped: viewHolder is null");
            return;
        }

        this.onMoved(viewHolder.getOldPosition(), viewHolder.getAdapterPosition());
        OnSwipeListener onSwipeListener = this.onSwipeListener;
        if (onSwipeListener != null) {
            onSwipeListener.onSwipeItemListener(viewHolder);
        }

    }

    public boolean isLongPressDragEnabled() {
        return true;
    }

    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    public final RecyclerViewItemHelper<T> setRecyclerItemDragEnabled(boolean isDragEnabled) {
        this.isItemDragEnabled = isDragEnabled;
        return this;
    }


    public final RecyclerViewItemHelper<T> setRecyclerItemSwipeEnabled(boolean isSwipeEnabled) {
        this.isItemSwipeEnabled = isSwipeEnabled;
        return this;
    }


    public final RecyclerViewItemHelper<T> setOnDragItemListener(OnDragListener onDragListener) {
        this.onDragListener = onDragListener;
        return this;
    }


    public final RecyclerViewItemHelper<T> setOnSwipeItemListener(OnSwipeListener onSwipeListener) {
        this.onSwipeListener = onSwipeListener;
        return this;
    }

    public final ArrayList getList() {
        return this.list;
    }

    public final void setList(ArrayList var1) {
        this.list = var1;
    }


    public final RecyclerView.Adapter getMAdapter() {
        return this.mAdapter;
    }

    public final void setMAdapter(RecyclerView.Adapter var1) {
        this.mAdapter = var1;
    }
}
