package com.example.hishab.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.hishab.R
import com.example.hishab.databinding.LayoutPurchaseItemInputBinding
import com.example.hishab.models.ShoppingItemProxy

class AddShoppingAdapter() : ListAdapter<ShoppingItemProxy, ViewHolder>(diffUtilCallBack) {
    var dataSource = ArrayList<ShoppingItemProxy>()
    var selectedProductId = -1L
    companion object {
        var diffUtilCallBack = object : DiffUtil.ItemCallback<ShoppingItemProxy>() {
            override fun areItemsTheSame(oldItem: ShoppingItemProxy, newItem: ShoppingItemProxy): Boolean {
                return oldItem.proxyId == newItem.proxyId
            }

            override fun areContentsTheSame(oldItem: ShoppingItemProxy, newItem: ShoppingItemProxy): Boolean {
                return oldItem.category.getCategoryName()
                    .equals(newItem.category.getCategoryName()) &&
                        oldItem.purchaseItem.getDescription()
                            .equals(newItem.purchaseItem.getDescription()) &&
                        oldItem.product.getProductName()
                            .equals(newItem.product.getProductName()) &&
                        oldItem.purchaseItem.getCost() == newItem.purchaseItem.getCost()

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
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(getItem(position))
        }
    }

    fun getElementAt(pos: Int): ShoppingItemProxy {
        return getItem(pos);
    }


    inner class ViewHolder(val binding: LayoutPurchaseItemInputBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(shoppingItemProxy: ShoppingItemProxy) {
            if(shoppingItemProxy.product.productId == selectedProductId){
                binding.clParent.setBackgroundColor(Color.GREEN)
            }
            binding.product = shoppingItemProxy
        }
    }


}