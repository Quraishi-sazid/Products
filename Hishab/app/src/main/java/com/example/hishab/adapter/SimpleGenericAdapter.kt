package com.example.hishab.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlin.reflect.typeOf

class SimpleGenericAdapterWithBinding<T, B : ViewDataBinding>(
    val layoutId: Int,
    private val diffUtilCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, SimpleGenericAdapterWithBinding<T, B>.ViewHolder>(diffUtilCallback) {
    lateinit var binding: B
    var viewInflateObservable = PublishSubject.create<Pair<T, B>>()
    var viewClickObservable = PublishSubject.create<T>()
    lateinit var dataSource: List<T>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var viewBinding= DataBindingUtil.inflate<B>(LayoutInflater.from(parent.context), layoutId, parent, false)
        return ViewHolder(viewBinding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.notify(dataSource[position])
    }

    fun update(updatedDataSource:List<T>) {
        dataSource=updatedDataSource
        submitList(dataSource)
    }

    inner class ViewHolder(val binding: B) : RecyclerView.ViewHolder(binding.root) {
        init
        {
            binding.root.setOnClickListener{view ->
               viewClickObservable.onNext(dataSource.get(adapterPosition))
        }
        }

        fun notify(data: T) {
            viewInflateObservable.onNext(Pair(data, binding))
        }

    }


}