package com.example.hishab.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hishab.R
import com.example.hishab.interfaces.IRecyclerViewItemClickCallback
import com.example.hishab.adapter.CategoryCostAdapter
import com.example.hishab.models.entities.CustomDate
import com.example.hishab.viewmodel.CategoryCostViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class CategoryCostFragment : Fragment() {
    private lateinit var categoryCostViewModel: CategoryCostViewModel
    private lateinit var navCallback: IRecyclerViewItemClickCallback
    val month = 1
    val day = 1
    val year = 2000
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflate = inflater.inflate(R.layout.fragment_category_details, container, false)
        categoryCostViewModel = ViewModelProvider(this).get(CategoryCostViewModel::class.java)
        setNavCallBack()
        CoroutineScope(Dispatchers.IO).launch {
            val getCategoryCostModels = categoryCostViewModel.getCategoryCostModels()
            withContext(Dispatchers.Main)
            {
                val categoryDetailsRecyclerView = inflate.findViewById<RecyclerView>(R.id.rv_category_details)
                categoryDetailsRecyclerView.layoutManager = LinearLayoutManager(activity)
                categoryDetailsRecyclerView.adapter = CategoryCostAdapter(getCategoryCostModels, navCallback)
            }
        }
        return inflate;
    }

    private fun setNavCallBack() {
        navCallback = object : IRecyclerViewItemClickCallback {
            override fun onItemClick(categoryId: Any) {

                val directions = ViewPagerTabFragmentDirections.actionViewPagerTabFragmentToPurchaseHistoryFragment()
                directions.categoryId = (categoryId as Long).toInt()
                directions.customDateModel = CustomDate(day, month, year)
                findNavController().navigate(directions)
            }
        }
    }
}