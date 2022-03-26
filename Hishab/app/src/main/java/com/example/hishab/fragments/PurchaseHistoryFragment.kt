package com.example.hishab.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hishab.R
import com.example.hishab.adapter.GenericAdapterForTwoViewTypes
import com.example.hishab.databinding.LayoutMonthItemBinding
import com.example.hishab.databinding.LayoutPurchaseItemBinding
import com.example.hishab.models.PurchaseHistory
import com.example.hishab.repository.Repository
import com.example.hishab.utils.Util
//import com.example.hishab.databinding.FragmentPurchaseHistoryBinding
import com.example.hishab.viewmodel.PurchaseHistoryViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.SchedulerSupport.IO
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import java.util.function.Predicate
import javax.inject.Inject

@AndroidEntryPoint
class PurchaseHistoryFragment : Fragment() {
    val purchaseHistoryViewModel: PurchaseHistoryViewModel by viewModels()
    lateinit var recyclerView: RecyclerView
  //  var adapter = PurchaseItemsAdapter()
    lateinit var adapter: GenericAdapterForTwoViewTypes<PurchaseHistory, String, LayoutPurchaseItemBinding, LayoutMonthItemBinding>
    val args: PurchaseHistoryFragmentArgs by navArgs()

    @Inject
    lateinit var repository: Repository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflate = inflater.inflate(R.layout.fragment_purchase_history, container, false)
        try {
            if (args.categoryId == -1) {
                getAllData()
            } else {
                    purchaseHistoryViewModel.getdetailsOfCategoryfromDate(
                        args.categoryId,
                        args.customDateModel!!
                    ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{
                        adapter.submitList(
                            purchaseHistoryViewModel.getDateSeparatedPurchaseHistoryList(
                                it
                            )
                        )
                    }
            }
        } catch (e: Exception) {
            getAllData()
        }
        adapter= GenericAdapterForTwoViewTypes.Create<PurchaseHistory, String, LayoutPurchaseItemBinding, LayoutMonthItemBinding>(R.layout.layout_purchase_item,R.layout.layout_month_item)
        adapter.checkIfFirstViewTypePredicate= Predicate { adapter.getItemAt(it) is PurchaseHistory }
        adapter.firstViewInlateLiveData.observe(viewLifecycleOwner){
            it.second.purchaseHistory=it.first;
        }
        adapter.secondViewInlateLiveData.observe(viewLifecycleOwner){
            it.second.month1=it.first
        }
        recyclerView = inflate.findViewById<RecyclerView>(R.id.recycler_view)!!
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter;

        return inflate
    }

    private fun getAllData() {
        purchaseHistoryViewModel.getPurchaseItems().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe() {
            adapter.submitList(
                purchaseHistoryViewModel.getDateSeparatedPurchaseHistoryList(
                    it
                )
            )
        }
    }

    private fun getDiffUtilCallBack():DiffUtil.ItemCallback<Any>
    {
        var diffUtilCallBack = object : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                if (!Util.getType(oldItem).equals(Util.getType(newItem))) {
                    return false;
                }
                if (oldItem is String && newItem is String)
                    return oldItem.equals(newItem)
                else if (oldItem is PurchaseHistory && newItem is PurchaseHistory) {
                    return oldItem.getPurchaseId() == newItem.getPurchaseId()
                }
                return false
            }

            override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                if (oldItem is PurchaseHistory && newItem is PurchaseHistory) {
                    val b = oldItem.getCategoryName().equals(newItem.getCategoryName()) &&
                            oldItem.getItemName().equals(newItem.getItemName()) &&
                            oldItem.getCost() == newItem.getCost() &&
                            oldItem.getDay() == newItem.getDay() &&
                            oldItem.getMonth() == newItem.getMonth() &&
                            oldItem.getYear() == newItem.getYear()
                    return b;
                }
                return true
            }
        }
        return diffUtilCallBack
    }
}