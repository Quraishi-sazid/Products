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
import com.example.hishab.models.entities.CategoryAndProductModel
import com.example.hishab.repository.Repository
//import com.example.hishab.databinding.FragmentPurchaseHistoryBinding
import com.example.hishab.viewmodel.PurchaseHistoryViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
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
        if (args.categoryId == -1) {
            CoroutineScope(Dispatchers.Main).launch {//Main thread query needs to be changed
                purchaseHistoryViewModel.getPurchaseItems().observe(viewLifecycleOwner, Observer
                {
                    adapter.submitList(purchaseHistoryViewModel.getDateSeparatedPurchaseHistoryList(it))
                })
            }
        } else {
            inflate.findViewById<FloatingActionButton>(R.id.fab).visibility = View.GONE
            CoroutineScope(Dispatchers.Main).launch {//Main thread query needs to be changed
                purchaseHistoryViewModel.getdetailsOfCategoryfromDate(
                    args.categoryId,
                    args.customDateModel!!
                ).observe(viewLifecycleOwner, Observer
                {
                    adapter.submitList(purchaseHistoryViewModel.getDateSeparatedPurchaseHistoryList(it))
                })
            }
        }
        recyclerView = inflate.findViewById<RecyclerView>(R.id.recycler_view)!!
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter;
        return inflate
    }
}