package com.example.hishab.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hishab.R
import com.example.hishab.`interface`.INavigationCallback
import com.example.hishab.databinding.LayoutCategoryCostBinding
import com.example.hishab.models.CategoryCostModel
import com.example.hishab.models.entities.PurchaseHistory

class CategoryCostAdapter(public val dataSet: List<CategoryCostModel>,public val navCallback:INavigationCallback):
    RecyclerView.Adapter<CategoryCostAdapter.CategoryCostAdapterViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryCostAdapterViewHolder {
         val binding:LayoutCategoryCostBinding=DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.layout_category_cost,
            parent,
            false
        )
        return  CategoryCostAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryCostAdapterViewHolder, position: Int) {
        holder.bind(dataSet.get(position))
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    inner class CategoryCostAdapterViewHolder(val binding:LayoutCategoryCostBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.clCategoryCostItem.setOnClickListener(View.OnClickListener {
                val model = dataSet.get(adapterPosition)
                navCallback.navigate(model.getCategoryId())
            });
        }

        fun bind(model:CategoryCostModel)
        {
            binding.categoryCost=model
        }
    }


}