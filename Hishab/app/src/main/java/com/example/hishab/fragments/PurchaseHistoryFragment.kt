package com.example.hishab.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hishab.adapter.PurchaseItemsAdapter
import com.example.hishab.R
//import com.example.hishab.databinding.FragmentPurchaseHistoryBinding
import com.example.hishab.viewmodel.PurchaseHistoryViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*


class PurchaseHistoryFragment : Fragment() {
    lateinit var purchaseHistoryViewModel:PurchaseHistoryViewModel
    lateinit var NavController:NavController
   // lateinit var fragmentPurchaseHistoryBinding :FragmentPurchaseHistoryBinding
    lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflate = inflater.inflate(R.layout.fragment_purchase_history, container, false)
        NavController =findNavController()
        purchaseHistoryViewModel= ViewModelProvider(this).get(PurchaseHistoryViewModel::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            purchaseHistoryViewModel.getPurchaseItems()
            withContext (Dispatchers.Main) {
                recyclerView = inflate.findViewById<RecyclerView>(R.id.recycler_view)!!
                recyclerView.layoutManager=LinearLayoutManager(activity)
                recyclerView.adapter= PurchaseItemsAdapter(purchaseHistoryViewModel.purchaseHistoryList)
            }
        }

        val fab = inflate.findViewById<FloatingActionButton>(R.id.fab);
        fab.setOnClickListener(View.OnClickListener {
          NavController.navigate(R.id.action_purchaseHistoryFragment_to_addShoppingFragment)
        })

        return inflate
    }


}