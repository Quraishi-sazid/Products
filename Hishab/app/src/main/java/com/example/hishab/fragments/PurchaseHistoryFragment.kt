package com.example.hishab.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hishab.R
import com.example.hishab.adapter.PurchaseItemsAdapter
import com.example.hishab.repository.ShoppingRepository
//import com.example.hishab.databinding.FragmentPurchaseHistoryBinding
import com.example.hishab.viewmodel.PurchaseHistoryViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class PurchaseHistoryFragment : Fragment() {
    val purchaseHistoryViewModel:PurchaseHistoryViewModel by viewModels()
    lateinit var NavController:NavController
    lateinit var recyclerView: RecyclerView
    var adapter=PurchaseItemsAdapter()
    @Inject
    lateinit var repository: ShoppingRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflate = inflater.inflate(R.layout.fragment_purchase_history, container, false)
        NavController =findNavController()
        CoroutineScope(Dispatchers.Main).launch {//Main thread query needs to be changed
            purchaseHistoryViewModel.getPurchaseItems().observe(viewLifecycleOwner, Observer
            {
                val convertedList = purchaseHistoryViewModel.convert(it);
                adapter.submitList(convertedList)
            })
        }
        recyclerView = inflate.findViewById<RecyclerView>(R.id.recycler_view)!!
        recyclerView.layoutManager=LinearLayoutManager(activity)
        recyclerView.adapter= adapter;
        val fab = inflate.findViewById<FloatingActionButton>(R.id.fab);
        fab.setOnClickListener(View.OnClickListener {
          NavController.navigate(R.id.action_purchaseHistoryFragment_to_addShoppingFragment)
        })
        return inflate
    }
}