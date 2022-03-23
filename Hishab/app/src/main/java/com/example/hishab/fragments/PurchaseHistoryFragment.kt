package com.example.hishab.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hishab.R
import com.example.hishab.adapter.PurchaseItemsAdapter
import com.example.hishab.repository.Repository
//import com.example.hishab.databinding.FragmentPurchaseHistoryBinding
import com.example.hishab.viewmodel.PurchaseHistoryViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.SchedulerSupport.IO
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class PurchaseHistoryFragment : Fragment() {
    val purchaseHistoryViewModel: PurchaseHistoryViewModel by viewModels()
    lateinit var recyclerView: RecyclerView
    var adapter = PurchaseItemsAdapter()
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
}