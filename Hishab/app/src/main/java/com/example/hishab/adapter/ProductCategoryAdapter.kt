package com.example.hishab.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hishab.R
import com.example.hishab.databinding.LayoutCategroyItemBinding
import com.example.hishab.databinding.LayoutProductCategoryItemBinding
import com.example.hishab.models.CategoryAndProductModel
import com.example.hishab.models.CategoryProxy

class ProductCategoryAdapter(var dataSource: List<CategoryAndProductModel>) :
    ListAdapter<CategoryAndProductModel, ProductCategoryAdapter.ViewHolder>(diffUtilCallBack) {


    companion object {
        var diffUtilCallBack = object : DiffUtil.ItemCallback<CategoryAndProductModel>() {
            override fun areItemsTheSame(
                oldItem: CategoryAndProductModel,
                newItem: CategoryAndProductModel
            ): Boolean {
                return oldItem.getCategoryId() == newItem.getCategoryId() && oldItem.getProductId() == newItem.getProductId()
            }

            override fun areContentsTheSame(
                oldItem: CategoryAndProductModel,
                newItem: CategoryAndProductModel
            ): Boolean {
                return oldItem.getCategoryName().equals(newItem.getCategoryName())
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: LayoutProductCategoryItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.layout_product_category_item,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSource[position])
    }

    class ViewHolder(var binding: LayoutProductCategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(categoryAndProductModel: CategoryAndProductModel) {
            binding.productCategoryMapping = categoryAndProductModel
        }
    }


}