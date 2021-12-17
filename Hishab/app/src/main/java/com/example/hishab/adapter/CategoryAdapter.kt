package com.example.hishab.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hishab.R
import com.example.hishab.databinding.LayoutCategroyItemBinding
import com.example.hishab.databinding.LayoutPurchaseItemInputBinding
import com.example.hishab.models.CategoryProxy
import com.example.hishab.models.ShoppingItemProxy
import com.example.hishab.models.entities.Category
import java.util.*
import kotlin.collections.ArrayList

class CategoryAdapter(var dataSource:List<CategoryProxy>) : ListAdapter<CategoryProxy, CategoryAdapter.CategoryViewHolder>(diffUtilCallBack) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding: LayoutCategroyItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.layout_categroy_item,
            parent,
            false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(dataSource[position])
    }

    companion object {
        var diffUtilCallBack = object : DiffUtil.ItemCallback<CategoryProxy>() {
            override fun areItemsTheSame(oldItem: CategoryProxy, newItem: CategoryProxy): Boolean {
                return oldItem.proxyId == newItem.proxyId
            }

            override fun areContentsTheSame(oldItem: CategoryProxy, newItem: CategoryProxy): Boolean {
                return oldItem.categoryName.equals(newItem.categoryName)
            }

        }
    }


    class CategoryViewHolder(val binding: LayoutCategroyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: CategoryProxy) {
            binding.category=category
        }
    }
}