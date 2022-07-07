package com.example.hishab.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hishab.R
import com.example.hishab.adapter.SimpleGenericAdapterWithBinding
import com.example.hishab.databinding.FragmentProductDetailsBinding
import com.example.hishab.databinding.ProductDetailsItemBinding
import com.example.hishab.models.ProductDetailsModel
import com.example.hishab.viewmodel.ProductDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {
    lateinit var binding: FragmentProductDetailsBinding
    lateinit var adapter: SimpleGenericAdapterWithBinding<ProductDetailsModel, ProductDetailsItemBinding>
    private val viewModel: ProductDetailsViewModel by viewModels()
    val args: ProductDetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_product_details, container, false)
        setUpRecyclerView()
        fetchProductDetailsData()
        return binding.root
    }

    private fun fetchProductDetailsData() {
        viewModel.getProductDetailsLiveData(args.productId).observe(viewLifecycleOwner) {
            adapter.update(it)
        }
    }

    private fun setUpRecyclerView() {
        adapter = SimpleGenericAdapterWithBinding.Create(R.layout.product_details_item)
        adapter.viewInlateLiveData.observe(viewLifecycleOwner) {
            it.second.productDetails = it.first
        }
        adapter.viewClickLiveData.observe(viewLifecycleOwner) {
            handleItemClick(it)
        }
        binding.rvProductDetails.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProductDetails.adapter = adapter
    }

    private fun handleItemClick(productDetailsModel: ProductDetailsModel) {
        var direction =
            ProductDetailsFragmentDirections.actionProductDetailsFragmentToAddShoppingFragment()
        direction.buyingId = productDetailsModel.shoppingId.toInt()
        direction.time = productDetailsModel.time
        direction.productId = productDetailsModel.productId
        findNavController().navigate(direction)
    }

}