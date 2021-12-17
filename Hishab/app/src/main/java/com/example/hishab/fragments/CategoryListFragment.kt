package com.example.hishab.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.hishab.R
import com.example.hishab.adapter.CategoryAdapter
import com.example.hishab.databinding.FragmentCategoryListBinding
import com.example.hishab.models.CategoryProxy
import com.example.hishab.models.entities.Category
import com.example.hishab.viewmodel.CategoryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryListFragment : Fragment() {
    lateinit var binding: FragmentCategoryListBinding
    lateinit var categoryAdapter: CategoryAdapter
    private val categoryListViewModel: CategoryViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_category_list, container, false)
        getCategoryData()
        return binding.root
    }

    private fun getCategoryData() {
        CoroutineScope(Dispatchers.IO).launch {
            val categoryList = categoryListViewModel.getCategoryWithTotalProductMapped()
            var proxyId = 1
            categoryList.forEach({
                it.proxyId = proxyId++
            })
            withContext(Dispatchers.Main)
            {
                setUpRecyclerView(categoryList)
            }
        }
    }

    private fun setUpRecyclerView(categoryList: List<CategoryProxy>) {
        categoryAdapter = CategoryAdapter(categoryList)
        binding.rvCategory.adapter = categoryAdapter
    }

}