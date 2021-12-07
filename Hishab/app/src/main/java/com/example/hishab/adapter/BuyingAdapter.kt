package com.example.hishab.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.hishab.R
import com.example.hishab.databinding.LayoutPurchaseItemBinding
import com.example.hishab.databinding.LayoutPurchaseItemInputBinding
import com.example.hishab.models.AddItemProxy
import com.example.hishab.models.entities.PurchaseHistory
import com.example.hishab.utils.Util

class BuyingAdapter: ListAdapter<AddItemProxy, ViewHolder>(diffUtilCallBack) {

    var dataSource=ArrayList<AddItemProxy>()
    companion object
    {
        var diffUtilCallBack= object : DiffUtil.ItemCallback<AddItemProxy>(){
            override fun areItemsTheSame(oldItem: AddItemProxy, newItem: AddItemProxy): Boolean {
                return  oldItem.proxyId==newItem.proxyId
            }

            override fun areContentsTheSame(oldItem: AddItemProxy, newItem: AddItemProxy): Boolean {
                return oldItem.category.getCategoryName().equals(newItem.category.getCategoryName()) &&
                        oldItem.purchaseItem.getDescription().equals(newItem.purchaseItem.getDescription()) &&
                            oldItem.shoppingItem.getProductName().equals(newItem.shoppingItem.getProductName()) &&
                        oldItem.purchaseItem.getCost()==newItem.purchaseItem.getCost()

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: LayoutPurchaseItemInputBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.layout_purchase_item_input,
            parent,
            false
        )
        return BuyingAdapter.ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ViewHolder)
        {
            holder.bind(getItem(position))
        }
    }

    fun getElementAt(pos: Int):AddItemProxy {
        return getItem(pos);
    }


    class ViewHolder(val binding: LayoutPurchaseItemInputBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(addItemProxy: AddItemProxy)
        {
            binding.product=addItemProxy
        }
    }


}