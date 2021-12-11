package com.example.hishab.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hishab.R
import com.example.hishab.adapter.BuyingListAdapter.ViewHolder
import com.example.hishab.interfaces.INavigationCallback
import com.example.hishab.databinding.LayoutBuyingItemBinding
import com.example.hishab.models.BuyingHistory

class BuyingListAdapter(
    var buyingHistoryList: List<BuyingHistory>, var navigationCallBack: INavigationCallback
) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: LayoutBuyingItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.layout_buying_item,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(buyingHistoryList[position])
    }

    override fun getItemCount(): Int {
        return buyingHistoryList.size
    }

    inner class ViewHolder(val binding: LayoutBuyingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(buyingHistory: BuyingHistory) {
            binding.buyingHistory = buyingHistory
            binding.rvBuyingListItem.setOnClickListener(View.OnClickListener {
                navigationCallBack.navigate(buyingHistoryList[adapterPosition])
            })

        }
    }
}