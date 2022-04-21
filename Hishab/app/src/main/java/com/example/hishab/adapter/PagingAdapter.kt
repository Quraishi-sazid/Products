package com.example.hishab.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import java.util.function.Predicate

class PagingAdapter<T1, T2, B1 : ViewDataBinding, B2 : ViewDataBinding> private constructor(
    val layoutId1: Int,
    val layoutId2: Int,
    val checkIfFirstViewTypePredicate: Predicate<Int>,
    private var diffUtilCallback: DiffUtil.ItemCallback<Any>
) : PagingDataAdapter<Any, RecyclerView.ViewHolder>(diffUtilCallback) {
    var firstViewInlateLiveData = MutableLiveData<Pair<T1, B1>>()
    var secondViewInlateLiveData = MutableLiveData<Pair<T2, B2>>()
    var firstviewClickLiveData = MutableLiveData<T1>()
    var secondviewClickLiveData = MutableLiveData<T2>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            1 -> {
                val binding: B1 = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    layoutId1,
                    parent,
                    false
                )
                return FirstViewHolder(binding)
            }
            else-> {
                val binding: B2 = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    layoutId2,
                    parent,
                    false
                )
                return SecondViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PagingAdapter<*, *, *, *>.FirstViewHolder) {
            firstViewInlateLiveData.value=Pair((getItem(position) as T1),holder.binding as B1)
        }
        else if(holder is PagingAdapter<*, *, *, *>.SecondViewHolder)
            secondViewInlateLiveData.value= Pair((getItem(position) as T2),holder.binding as B2)
    }
    override fun getItemViewType(position: Int): Int {
        if (checkIfFirstViewTypePredicate != null) {
            if (checkIfFirstViewTypePredicate!!.test(position))
                return 1
            else
                return 2
        }
        return 1
    }


    inner class FirstViewHolder(val binding: B1) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener{
                firstviewClickLiveData.value=getItem(adapterPosition) as T1
            }
        }
    }

    inner class SecondViewHolder(val binding: B2) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener{
                secondviewClickLiveData.value=getItem(adapterPosition) as T2
            }
        }
    }
    fun getItemAt(position: Int): Any?
    {
        return getItem(position)
    }


    companion object {
        fun <T1,T2, B1 : ViewDataBinding,B2 : ViewDataBinding> Create(layoutId1: Int,layoutId2: Int,predicate: Predicate<Int>, diffUtil: DiffUtil.ItemCallback<Any>?=null)
                : PagingAdapter<T1,T2, B1,B2> {
            var passingDiffUtil = diffUtil
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
            return PagingAdapter<T1,T2, B1,B2>(layoutId1,layoutId2,predicate, passingDiffUtil)
        }
    }
}