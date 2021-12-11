package com.example.hishab.adapter

import android.provider.ContactsContract.CommonDataKinds.Note
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hishab.R
import com.example.hishab.databinding.LayoutPurchaseItemBinding
import com.example.hishab.models.entities.PurchaseHistory
import com.example.hishab.utils.Util


class PurchaseItemsAdapter() : ListAdapter<Any, RecyclerView.ViewHolder>(diffUtilCallBack) {

    companion object {
        var diffUtilCallBack = object : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                if (!Util.getType(oldItem).equals(Util.getType(newItem))) {
                    return false;
                }
                if (oldItem is String && newItem is String)
                    return oldItem.equals(newItem)
                else if (oldItem is PurchaseHistory && newItem is PurchaseHistory) {
                    return oldItem.getPurchaseId() == newItem.getPurchaseId()
                }
                return false
            }

            override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                if (oldItem is PurchaseHistory && newItem is PurchaseHistory) {
                    val b = oldItem.getCategoryName().equals(newItem.getCategoryName()) &&
                            oldItem.getItemName().equals(newItem.getItemName()) &&
                            oldItem.getCost() == newItem.getCost() &&
                            oldItem.getDay() == newItem.getDay() &&
                            oldItem.getMonth() == newItem.getMonth() &&
                            oldItem.getYear() == newItem.getYear()
                    return b;
                }
                return true
            }
        }
    }

    //private lateinit var objectList:List<Any>;
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            ViewType.PurchaseHistory.ordinal -> {
                val binding: LayoutPurchaseItemBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.layout_purchase_item,
                    parent,
                    false
                )
                return ViewHolder(binding)
            }
           else-> {
                val inflate = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_month_item, parent, false)
                return MonthViewHolder(inflate)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(getItem(position) as PurchaseHistory)
        } else if (holder is MonthViewHolder) {
            holder.textview.text = getItem(position) as String
        }

    }

    override fun getItemViewType(position: Int): Int {
        if (getItem(position) is String)
            return ViewType.String.ordinal;
        else
            return ViewType.PurchaseHistory.ordinal;
    }

    class ViewHolder(val binding: LayoutPurchaseItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(purchaseHistory: PurchaseHistory) {
            binding.purchaseHistory = purchaseHistory;
        }
    }

    class MonthViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var textview: TextView

        init {
            textview = view.findViewById<TextView>(R.id.month);
        }
    }

    enum class ViewType {
        String, PurchaseHistory
    }
}