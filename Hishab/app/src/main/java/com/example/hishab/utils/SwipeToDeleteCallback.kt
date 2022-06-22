package com.example.hishab.utils


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.hishab.R
import com.example.hishab.models.RVItemSwipeResponse
import io.reactivex.subjects.PublishSubject

/*
* This class is used to draw a delete background when swipe is done.
*
* */
class SwipeToDeleteCallback<T>(context: Context) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    lateinit var context:Context
    public var onSwipeObservable= PublishSubject.create<RVItemSwipeResponse>()
    private val deleteIcon: Drawable?
    private lateinit var editIcon: Drawable
    private val background: GradientDrawable
    private lateinit var canvas:Canvas;
    private var recyclerView: RecyclerView? = null
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onSwipeObservable.onNext(RVItemSwipeResponse(viewHolder.adapterPosition,direction))
        recyclerView?.adapter?.notifyItemChanged(viewHolder.adapterPosition)
    }
    //need to be handled for tab navigations
    override fun getSwipeDirs(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        Log.v("SwipeToDeleteCallback","getSwipeDirs")
        this.recyclerView = recyclerView
        return super.getSwipeDirs(recyclerView, viewHolder)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        canvas=c
        val itemView = viewHolder.itemView
        val backgroundCornerOffset = 20 //so background is behind the rounded corners of itemView
        val iconMargin = (itemView.height - deleteIcon!!.intrinsicHeight) / 2
        val iconTop = itemView.top + (itemView.height - deleteIcon.intrinsicHeight) / 2
        val iconBottom = iconTop + deleteIcon.intrinsicHeight
        if (dX > 0) { // Swiping to the right
            val iconLeft = itemView.left + iconMargin + (dX * .5).toInt()
            val iconRight = itemView.left + iconMargin + editIcon.intrinsicWidth
            editIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            background.setBounds(
                itemView.left, itemView.top,
                itemView.left + dX.toInt() + backgroundCornerOffset, itemView.bottom
            )
        } else if (dX < 0) { // Swiping to the left
            val iconLeft = itemView.right - iconMargin - deleteIcon.intrinsicWidth
            val iconRight = itemView.right - iconMargin
            deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            background.setBounds(
                itemView.right + dX.toInt() - backgroundCornerOffset,
                itemView.top, itemView.right, itemView.bottom
            )
        } else { // view is unSwiped
            editIcon.setBounds(0, 0, 0, 0)
            deleteIcon.setBounds(0, 0, 0, 0)
            background.setBounds(0, 0, 0, 0)
        }
        background.draw(c)
        editIcon.draw(c)
        deleteIcon.draw(c)
    }

    init {
        deleteIcon = context?.let {
            ContextCompat.getDrawable(
                it,
                R.drawable.ic_baseline_delete_24
            )
        }
        editIcon= context?.let { ContextCompat.getDrawable(it,R.drawable.ic_baseline_edit_24) }!!
        val gd = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(context.getResources().getColor(R.color.gradient_start),context.getResources().getColor(R.color.gradient_end))
        )
        gd.cornerRadius = 0f
        background = gd
    }
}