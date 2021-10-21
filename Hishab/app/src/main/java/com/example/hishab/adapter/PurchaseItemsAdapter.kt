package com.example.hishab.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hishab.R
import com.example.hishab.databinding.LayoutPurchaseItemBinding
import com.example.hishab.models.entities.PurchaseHistory
import com.example.hishab.utils.Util
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlin.collections.ArrayList

class PurchaseItemsAdapter @AssistedInject constructor(@Assisted dataSet: List<PurchaseHistory>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    public val objectList=ArrayList<Any>()
    init {
        var pMonth="";
        for (it in dataSet)
        {
            val month_name=Util.getMonthForInt(it.getMonth())
            if(!pMonth.equals(month_name))
            {
                objectList.add(month_name);
                pMonth=month_name
            }
            objectList.add(it)
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType==1)
        {
            val binding: LayoutPurchaseItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.layout_purchase_item,
                parent,
                false
            )
            return ViewHolder(binding)
        }
        else
        {
            val inflate = LayoutInflater.from(parent.context).inflate(R.layout.layout_month_item, parent, false)
            return MonthViewHolder(inflate)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ViewHolder)
        {
            holder.bind(objectList.get(position) as PurchaseHistory)
        }
        else if(holder is MonthViewHolder)
        {
            holder.textview.setText(objectList.get(position) as String)
        }

    }

    override fun getItemCount(): Int {
            return objectList.size
    }

    override fun getItemViewType(position: Int): Int {
        if(objectList.get(position) is String)
            return 2;
        else
            return 1;
    }

    class ViewHolder(val binding: LayoutPurchaseItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(purchaseHistory: PurchaseHistory)
        {
            binding.purchaseHistory=purchaseHistory;
        }
    }

    class MonthViewHolder(view: View): RecyclerView.ViewHolder(view) {
        lateinit var textview: TextView
        init {
            textview=view.findViewById<TextView>(R.id.month);
        }
    }
}