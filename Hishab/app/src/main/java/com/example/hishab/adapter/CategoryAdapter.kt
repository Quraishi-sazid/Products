package com.example.hishab.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hishab.R
import com.example.hishab.databinding.LayoutCategroyItemBinding
import com.example.hishab.models.CategoryProxy

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
        var xx=4
    }

    fun getElementAt(adapterPosition: Int): CategoryProxy {
        return dataSource[adapterPosition]
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