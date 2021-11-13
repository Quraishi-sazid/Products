package com.example.hishab.utils


import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.hishab.R
import com.example.hishab.adapter.PurchaseItemsAdapter
import com.example.hishab.fragments.PurchaseHistoryFragment
import com.example.hishab.fragments.PurchaseHistoryFragmentDirections
import com.example.hishab.models.entities.PurchaseHistory
import com.example.hishab.viewmodel.PurchaseHistoryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/*
* This class is used to draw a delete background when swipe is done.
*
* */
class SwipeToDeleteCallback(var context: Context?,var listAdapter: PurchaseItemsAdapter,var purchaseHistoryViewModel: PurchaseHistoryViewModel,var purchaseHistoryFragment: PurchaseHistoryFragment) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    private val deleteIcon: Drawable?
    private lateinit var editIcon: Drawable
    private val background: ColorDrawable
    private lateinit var canvas:Canvas;
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if(direction==ItemTouchHelper.LEFT)
        {
            if(listAdapter.getElementAt(viewHolder.adapterPosition) is PurchaseHistory)
            {
                context?.let { showAlertDialog(it,"Delete Entry","Do you want to delete this entry?",(listAdapter.getElementAt(viewHolder.adapterPosition) as PurchaseHistory).getPurchaseId()) }
            }
        }
        else
            run {
                var purchaseHistory =(listAdapter.getElementAt(viewHolder.adapterPosition) as PurchaseHistory)
            /*    var directions=PurchaseHistoryFragmentDirections.actionPurchaseHistoryFragmentToAddShoppingFragment()
                directions.editHistory=purchaseHistory
                purchaseHistoryFragment.findNavController()
                    .navigate(directions)*/
            }


    }

    override fun getSwipeDirs(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        if(viewHolder is PurchaseItemsAdapter.MonthViewHolder) return  0
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
        if(viewHolder is PurchaseItemsAdapter.MonthViewHolder)
            return;
        canvas=c
        val itemView = viewHolder.itemView
        val backgroundCornerOffset = 20 //so background is behind the rounded corners of itemView
        val h = deleteIcon!!.intrinsicHeight
        val oh = deleteIcon.minimumHeight
        val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2
        val iconTop = itemView.top + (itemView.height - deleteIcon.intrinsicHeight) / 2
        val iconBottom = iconTop + deleteIcon.intrinsicHeight
        if (dX > 0) { // Swiping to the right
            val iconLeft = itemView.left + iconMargin + (dX * .5).toInt()
            val iconRight = itemView.left + iconMargin + deleteIcon.intrinsicWidth
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
            background.setBounds(0, 0, 0, 0)
        }
        background.draw(c)
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
        background = ColorDrawable(Color.RED)
    }

    fun showAlertDialog(context:Context,title:String,message:String,pos:Int?)
    {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message) // Specifying a listener allows you to take an action before dismissing the dialog.
            // The dialog is automatically dismissed when a dialog button is clicked.
            .setPositiveButton(
                android.R.string.yes,
                DialogInterface.OnClickListener { dialog, which ->
                    CoroutineScope(Dispatchers.IO).launch{
                        if (pos != null) {
                            purchaseHistoryViewModel.delete(pos)
                        };
                    }
                })
            .setNegativeButton(android.R.string.no, DialogInterface.OnClickListener{dialog, which ->
                listAdapter.notifyDataSetChanged()
            })
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }
}