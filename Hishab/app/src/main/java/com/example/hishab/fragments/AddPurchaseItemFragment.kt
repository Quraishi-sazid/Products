package com.example.hishab.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.hishab.R
import com.example.hishab.databinding.FragmentAddPurchaseItemBinding
import com.example.hishab.models.ShoppingItemProxy
import com.example.hishab.models.entities.Category
import com.example.hishab.models.CategoryAndProductModel
import com.example.hishab.viewmodel.AddPurchaseItemViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddPurchaseItemFragment : DialogFragment() {
    private lateinit var binding: FragmentAddPurchaseItemBinding
    private val addPurchaseItemViewModel: AddPurchaseItemViewModel by viewModels()

    val args: AddPurchaseItemFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_purchase_item, container, false)
        if (checkIsUpdating()) {
            addPurchaseItemViewModel.setValuesForUpdating(args.shoppingItemProxy!!)
            setValuesToView()
        }
        setAdapterToAutoCompleteTextBoxes()
        handleProductAutoCompleteClick()
        handleCategoryAutoCompleteClick()
        setSubmitButtonClick()
        return binding.root
    }

    private fun checkIsUpdating() = args.shoppingItemProxy != null

    private fun handleCategoryAutoCompleteClick() {
        binding.etCategory.onItemClickListener =
            AdapterView.OnItemClickListener() { adapterView: AdapterView<*>, view1: View, position: Int, id: Long ->
                val item = adapterView.adapter.getItem(position)
                addPurchaseItemViewModel.category = item as Category;
            }
    }

    private fun handleProductAutoCompleteClick() {
        binding.etProductName.onItemClickListener =
            AdapterView.OnItemClickListener() { adapterView: AdapterView<*>, view1: View, position: Int, id: Long ->
                val item = adapterView.adapter.getItem(position)
                val categoryAndProductModel = item as CategoryAndProductModel
                addPurchaseItemViewModel.category.setCategoryName(categoryAndProductModel.getCategoryName()!!)
                addPurchaseItemViewModel.product.productId =
                    categoryAndProductModel.getProductId()!!
            }
    }

    private fun setAdapterToAutoCompleteTextBoxes() {
        setAdapterToProductAutoCompleteBox()
        setAdapterToCategoryAutoCompleteBox()
    }

    private fun setAdapterToCategoryAutoCompleteBox() {
        binding.etCategory.setAdapter(context?.let {
            ArrayAdapter<Category>(
                it, android.R.layout.simple_dropdown_item_1line, args.distinctCategoryArray
            )
        })
    }

    private fun setAdapterToProductAutoCompleteBox() {
        binding.etProductName.setAdapter(context?.let {
            ArrayAdapter<CategoryAndProductModel>(
                it, android.R.layout.simple_dropdown_item_1line, args.productCategoryArray
            )
        })
    }

    private fun setValuesToView() {
        binding.product = addPurchaseItemViewModel.product
        binding.purchaseItem = addPurchaseItemViewModel.purchaseItem
        binding.category = addPurchaseItemViewModel.category
    }


    private fun setSubmitButtonClick() {
        binding.btnSubmit.setOnClickListener(View.OnClickListener {
            if (addPurchaseItemViewModel.isUpdating) {
                addPurchaseItemViewModel.handleInformationChange()
                args.callback?.onUpdateCallBack(args.shoppingItemProxy!!)
            } else {
                var returnShopingItem = ShoppingItemProxy(
                    (args.buyingListSize).toLong(),
                    addPurchaseItemViewModel.category,
                    addPurchaseItemViewModel.product,
                    addPurchaseItemViewModel.purchaseItem
                )
                args.callback?.onAddedCallback(returnShopingItem)
            }
            dismiss()
        })
    }
}