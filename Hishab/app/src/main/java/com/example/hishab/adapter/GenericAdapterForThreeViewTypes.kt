package com.example.hishab.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hishab.interfaces.IOnBindViewHolderCallback
import com.example.hishab.models.ThreeLayout
import com.example.hishab.models.TwoPredicate

class GenericAdapterForThreeViewTypes<T1, T2, T3, B1 : ViewDataBinding, B2 : ViewDataBinding, B3 : ViewDataBinding> constructor(
    var threeLayouts: ThreeLayout,
    var twoPredicate: TwoPredicate,
    var diffUtilCallback: DiffUtil.ItemCallback<Any>
) : ListAdapter<Any, RecyclerView.ViewHolder>(diffUtilCallback) {

    var firstViewInlateLiveData = MutableLiveData<Pair<T1, B1>>()
    var secondViewInlateLiveData = MutableLiveData<Pair<T2, B2>>()
    var thirdViewInlateLiveData = MutableLiveData<Pair<T3, B3>>()
    var firstViewClickLiveData = MutableLiveData<T1>()
    var secondViewClickLiveData = MutableLiveData<T2>()
    var thirdViewClickLiveData = MutableLiveData<T3>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            1 -> {
                val binding: B1 = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    threeLayouts.layout1,
                    parent,
                    false
                )
                return FirstViewHolder(binding)
            }
            2 -> {
                val binding: B2 = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    threeLayouts.layout2,
                    parent,
                    false
                )
                return SecondViewHolder(binding)
            }
            else -> {
                val binding: B3 = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    threeLayouts.layout3,
                    parent,
                    false
                )
                return ThirdViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is IOnBindViewHolderCallback)
            holder.onCallBindViewHolder()
    }


    override fun getItemViewType(position: Int): Int {
        return when {
            twoPredicate.isFirstPredicate(position) -> 1
            twoPredicate.isSecondPredicate(position) -> 2
            else -> 3
        }
    }
    fun getItemAt(position: Int):Any
    {
        return getItem(position)
    }

    inner class FirstViewHolder(val binding: B1) : RecyclerView.ViewHolder(binding.root),
        IOnBindViewHolderCallback {
        init {
            binding.root.setOnClickListener {
                firstViewClickLiveData.value = getItem(adapterPosition) as T1
            }
        }

        override fun onCallBindViewHolder() {
            firstViewInlateLiveData.value = Pair(getItem(adapterPosition) as T1, binding)
        }
    }

    inner class SecondViewHolder(val binding: B2) : RecyclerView.ViewHolder(binding.root),
        IOnBindViewHolderCallback {
        init {
            binding.root.setOnClickListener {
                secondViewClickLiveData.value = getItem(adapterPosition) as T2
            }
        }

        override fun onCallBindViewHolder() {
            secondViewInlateLiveData.value = Pair(getItem(adapterPosition) as T2, binding)
        }
    }

    inner class ThirdViewHolder(val binding: B3) : RecyclerView.ViewHolder(binding.root),
        IOnBindViewHolderCallback {
        init {
            binding.root.setOnClickListener {
                thirdViewClickLiveData.value = getItem(adapterPosition) as T3
            }
        }

        override fun onCallBindViewHolder() {
            thirdViewInlateLiveData.value = Pair(getItem(adapterPosition) as T3, binding)
        }
    }

    companion object {
        fun <T1, T2, T3, B1 : ViewDataBinding, B2 : ViewDataBinding, B3 : ViewDataBinding> Create(
            threeLayouts: ThreeLayout,
            twoPredicate: TwoPredicate,
            diffUtilCallback: DiffUtil.ItemCallback<Any>? = null
        )
                : GenericAdapterForThreeViewTypes<T1, T2, T3, B1, B2, B3> {
            var passingDiffUtil = diffUtilCallback
            if (passingDiffUtil == null) {
                passingDiffUtil = object : DiffUtil.ItemCallback<Any>() {
                    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                        return false
                    }

                    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                        return false
                    }
                }
            }
            return GenericAdapterForThreeViewTypes<T1, T2, T3, B1, B2, B3>(
                threeLayouts, twoPredicate, passingDiffUtil
            )
        }
    }

}