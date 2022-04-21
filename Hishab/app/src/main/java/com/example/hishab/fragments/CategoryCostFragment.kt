package com.example.hishab.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hishab.R
import com.example.hishab.interfaces.IRecyclerViewItemClickCallback
import com.example.hishab.adapter.CategoryCostAdapter
import com.example.hishab.interfaces.IViewPagerSwipeListener
import com.example.hishab.models.entities.CustomDate
import com.example.hishab.utils.Util
import com.example.hishab.viewmodel.CategoryCostViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class CategoryCostFragment : Fragment(),IViewPagerSwipeListener {
    private lateinit var categoryCostViewModel: CategoryCostViewModel
    private lateinit var navCallback: IRecyclerViewItemClickCallback
    private val args: CategoryCostFragmentArgs by navArgs()
    var month = -1
    var year = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflate = inflater.inflate(R.layout.fragment_category_details, container, false)
        assignIfMonthAndYearAvailable()
        categoryCostViewModel = ViewModelProvider(this).get(CategoryCostViewModel::class.java)
        setNavCallBack()
        CoroutineScope(Dispatchers.IO).launch {
            var categoryCostModels = if (month == -1)
                categoryCostViewModel.getAllTimeTotalCostByCategory()
            else
                categoryCostViewModel.getCategoryCostFromMonthAndYear(month, year)
            withContext(Dispatchers.Main)
            {
                val categoryDetailsRecyclerView =
                    inflate.findViewById<RecyclerView>(R.id.rv_category_details)
                categoryDetailsRecyclerView.layoutManager = LinearLayoutManager(activity)
                categoryDetailsRecyclerView.adapter =
                    CategoryCostAdapter(categoryCostModels, navCallback)
                onSwipedRightOrLeft=Util.getViewSwipeObservable(categoryDetailsRecyclerView)
            }
        }
        return inflate;
    }

    private fun assignIfMonthAndYearAvailable() {
        try {
            month = args.month
            year = args.year
        } catch (ex: Exception) {
        }
    }

    private fun setNavCallBack() {
        navCallback = object : IRecyclerViewItemClickCallback {
            override fun onItemClick(categoryId: Any) {
                val directions =
                    ViewPagerTabFragmentDirections.actionViewPagerTabFragmentToPurchaseHistoryFragment()
                directions.categoryId = (categoryId as Long).toInt()
                findNavController().navigate(directions)
            }
        }
    }

    override lateinit var onSwipedRightOrLeft: Observable<Pair<Float, Float>>
}