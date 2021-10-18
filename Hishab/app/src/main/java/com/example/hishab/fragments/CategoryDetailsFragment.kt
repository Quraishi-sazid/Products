package com.example.hishab.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hishab.R
import com.example.hishab.adapter.CategoryCostAdapter
import com.example.hishab.adapter.PurchaseItemsAdapter
import com.example.hishab.viewmodel.AddShoppingViewModel
import com.example.hishab.viewmodel.CategoryCostViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class CategoryDetailsFragment : Fragment() {
    private lateinit var vm: CategoryCostViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflate = inflater.inflate(R.layout.fragment_category_details, container, false)
        vm= ViewModelProvider(this).get(CategoryCostViewModel::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val getCategoryCostModels = vm.GetCategoryCostModels()
            withContext(Dispatchers.Main)
            {
                val recyclerView=inflate.findViewById<RecyclerView>(R.id.rv_category_details)
                recyclerView.layoutManager= LinearLayoutManager(activity)
                recyclerView.adapter= CategoryCostAdapter(getCategoryCostModels)
            }
        }
        return inflate;
    }
}