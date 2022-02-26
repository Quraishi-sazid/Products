package com.example.hishab.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hishab.R
import com.example.hishab.adapter.SimpleGenericAdapterWithBinding
import com.example.hishab.databinding.FragmentProductCategoryMappingBinding
import com.example.hishab.databinding.LayoutProductCategoryItemBinding
import com.example.hishab.databinding.LayoutProductCategoryMappingInputBinding
import com.example.hishab.models.CategoryAndProductModel
import com.example.hishab.models.entities.Category
import com.example.hishab.models.entities.Product
import com.example.hishab.utils.SwipeToDeleteCallback
import com.example.hishab.viewmodel.ProductCategoryMappingViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ProductCategoryMappingFragment : Fragment() {
    private lateinit var binding: FragmentProductCategoryMappingBinding
    private val productCategoryMappingViewModel: ProductCategoryMappingViewModel by viewModels()
    lateinit var categoryAndProductModelList: List<CategoryAndProductModel>
    lateinit var simpleGenericAdapterWithBinding : SimpleGenericAdapterWithBinding<CategoryAndProductModel,LayoutProductCategoryItemBinding>
    lateinit var adapterSubscription:Disposable;
    lateinit var swipeToDeleteCallback: SwipeToDeleteCallback<CategoryAndProductModel>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_product_category_mapping,
            container,
            false
        )
        setRecyclerView()
        setFabClick()
        fetchProductCategoryMapping()
        return binding.root
    }

    private fun setRecyclerView() {
        var diffUtilCallback=object : DiffUtil.ItemCallback<CategoryAndProductModel>() {
            override fun areItemsTheSame(
                oldItem: CategoryAndProductModel,
                newItem: CategoryAndProductModel
            ): Boolean {
                return oldItem.getProductId()==newItem.getProductId()
                        && oldItem.getCategoryId()==newItem.getCategoryId()
            }
            override fun areContentsTheSame(
                oldItem: CategoryAndProductModel,
                newItem: CategoryAndProductModel
            ): Boolean {
                return oldItem.getCategoryName().equals(newItem.getCategoryName())&&
                        oldItem.getProductName().equals(newItem.getProductName())
            }
        }
        binding.rvProductCategory.layoutManager=LinearLayoutManager(activity)
        simpleGenericAdapterWithBinding=
            SimpleGenericAdapterWithBinding(R.layout.layout_product_category_item,diffUtilCallback)
        adapterSubscription = simpleGenericAdapterWithBinding.viewInflateObservable.subscribe({
            it.second.productCategoryMapping=it.first
        })
        simpleGenericAdapterWithBinding.viewClickObservable.subscribe{clickedCategoryAndProductModel->

        }
        binding.rvProductCategory.adapter=simpleGenericAdapterWithBinding
        swipeToDeleteCallback= SwipeToDeleteCallback<CategoryAndProductModel>(requireContext())
        ItemTouchHelper(swipeToDeleteCallback).attachToRecyclerView(binding.rvProductCategory)
        swipeToDeleteCallback.onSwipeObservable.subscribe{swipedItemResponse->
            if(swipedItemResponse.direction==ItemTouchHelper.LEFT)
            {

            }
            else
            {
                productCategoryMappingViewModel.deleteProduct(simpleGenericAdapterWithBinding.dataSource[swipedItemResponse.adapterPosition].getProductId())
            }
        }
    }

    private fun fetchProductCategoryMapping() {
        CoroutineScope(Dispatchers.Main).launch {
            productCategoryMappingViewModel.getProductCategoryMapping().observe(viewLifecycleOwner,{
                simpleGenericAdapterWithBinding.update(it);
        })
        }
    }
    private fun setFabClick() {
        binding.fabProductCategory.setOnClickListener(View.OnClickListener {
            setFabClick()
        })
    }
    private fun showDialog(categoryAndProductModel: CategoryAndProductModel?=null) {
        val customDialog = Dialog(requireActivity())
        var binding= DataBindingUtil.inflate<LayoutProductCategoryMappingInputBinding>(LayoutInflater.from(getContext()), R.layout.layout_product_category_mapping_input, null, false)
        customDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        if(categoryAndProductModel!=null)
        {
            binding.category= Category(categoryAndProductModel.getCategoryId()!!,categoryAndProductModel.getCategoryName()!!)
            binding.product= Product(categoryAndProductModel.getProductId()!!,categoryAndProductModel.getProductName()!!,categoryAndProductModel.getCategoryId()!!)
        }
        val yesBtn = customDialog.findViewById(R.id.btn_yes) as Button
        yesBtn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                productCategoryMappingViewModel.insertProduct(binding.category?.categoryId!!,binding.category?.getCategoryName()!!,binding.product?.getProductName()!!)
                withContext(Dispatchers.Main)
                {
                    customDialog.dismiss()
                }
            }
        }
        customDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        adapterSubscription?.dispose()
    }
}