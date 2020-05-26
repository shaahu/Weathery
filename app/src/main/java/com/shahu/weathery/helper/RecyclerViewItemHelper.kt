package com.shahu.weathery.helper

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.shahu.weathery.interface2.OnDragListener
import com.shahu.weathery.interface2.OnSwipeListener
import java.util.*

/**
 * Created by Shahu Ronghe on 27, November, 2019
 * in Weathery
 */
class RecyclerViewItemHelper<T>(private var list: ArrayList<*>,
                                private var mAdapter: RecyclerView.Adapter<*>?) : ItemTouchHelper.Callback() {
    lateinit var onDragItemListener: OnDragListener
    lateinit var onSwipeItemListener: OnSwipeListener
    private var isItemDragEnabled = false
    private var isItemSwipeEnabled = false

    private fun onMoved(fromPos: Int, toPos: Int) {
        list.removeAt(toPos)
        mAdapter?.notifyItemRemoved(toPos)
    }

    private fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(list, fromPosition, toPosition)
        mAdapter?.notifyItemMoved(fromPosition, toPosition)
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: ViewHolder): Int {
        var dragFlags = 0
        var swipeFlags = 0
        if (isItemDragEnabled) {
            dragFlags = 3
        }
        if (isItemSwipeEnabled) {
            swipeFlags = 12
        }
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: ViewHolder, target: ViewHolder): Boolean {
        val adapterPosition = viewHolder.adapterPosition
        onItemMoved(adapterPosition, target.adapterPosition)
        val onDragListener = onDragItemListener
        onDragListener.onDragItemListener(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
        this.onMoved(viewHolder.oldPosition, viewHolder.adapterPosition)
        val onSwipeListener = onSwipeItemListener
        onSwipeListener.onSwipeItemListener(viewHolder)
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    fun setRecyclerItemDragEnabled(isDragEnabled: Boolean): RecyclerViewItemHelper<T> {
        isItemDragEnabled = isDragEnabled
        return this
    }

    fun setRecyclerItemSwipeEnabled(isSwipeEnabled: Boolean): RecyclerViewItemHelper<T> {
        isItemSwipeEnabled = isSwipeEnabled
        return this
    }

    companion object {
        private const val TAG = "RecyclerViewItemHelper"
    }

}