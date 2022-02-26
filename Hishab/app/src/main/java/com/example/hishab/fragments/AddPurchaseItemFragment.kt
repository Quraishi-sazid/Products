package com.example.hishab.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.hishab.R
import com.example.hishab.databinding.FragmentAddPurchaseItemBinding
import com.example.hishab.interfaces.IAutoCompleteClickedCallback
import com.example.hishab.models.ShoppingItemProxy
import com.example.hishab.models.entities.Category
import com.example.hishab.models.CategoryAndProductModel
import com.example.hishab.utils.AutoCompleteTextViewManager
import com.example.hishab.viewmodel.AddPurchaseItemViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddPurchaseItemFragment : DialogFragment() {
    private lateinit var binding: FragmentAddPurchaseItemBinding
    private val addPurchaseItemViewModel: AddPurchaseItemViewModel by viewModels()
    lateinit var categoryAutoCompleteTextVIewManager: AutoCompleteTextViewManager<Category>
    lateinit var productAutoCompleteTextVIewManager: AutoCompleteTextViewManager<CategoryAndProductModel>
    lateinit var categoryClickedCallback: IAutoCompleteClickedCallback<Category>
    lateinit var productClickedCallback: IAutoCompleteClickedCallback<CategoryAndProductModel>
    val args: AddPurchaseItemFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_purchase_item, container, false)
        if (checkIsUpdating()) {
            addPurchaseItemViewModel.setValuesForUpdating(args.shoppingItemProxy!!)
        }
        setValuesToView()
        handleProductAutoCompleteClick()
        handleCategoryAutoCompleteClick()
        setAdapterToAutoCompleteTextBoxes()

        setSubmitButtonClick()
        return binding.root
    }

    private fun checkIsUpdating() = args.shoppingItemProxy != null

    private fun handleCategoryAutoCompleteClick() {
        categoryClickedCallback=object : IAutoCompleteClickedCallback<Category>{
            override fun onAutoCompleteClickedCallback(category: Category?) {
                if(category!=null)
                {
                    addPurchaseItemViewModel.category=category
                }
            }
        }
    }

    private fun handleProductAutoCompleteClick() {
        productClickedCallback=object : IAutoCompleteClickedCallback<CategoryAndProductModel>{
            override fun onAutoCompleteClickedCallback(categoryAndProductModel: CategoryAndProductModel?) {
                if(categoryAndProductModel!=null)
                {
                    addPurchaseItemViewModel.category.setCategoryName(categoryAndProductModel.getCategoryName()!!)
                    addPurchaseItemViewModel.product.productId =
                        categoryAndProductModel.getProductId()!!
                }
            }
        }
    }

    private fun setAdapterToAutoCompleteTextBoxes() {
        categoryAutoCompleteTextVIewManager = AutoCompleteTextViewManager<Category>(
            requireContext(),
            binding.etCategory,
            args.distinctCategoryArray.toList()
        )
        productAutoCompleteTextVIewManager = AutoCompleteTextViewManager<CategoryAndProductModel>(
            requireContext(),
            binding.etProductName,
            args.productCategoryArray.toList()
        )
        categoryAutoCompleteTextVIewManager.setAdapter()
        productAutoCompleteTextVIewManager.setAdapter()
        categoryAutoCompleteTextVIewManager.setCallBack(categoryClickedCallback)
        productAutoCompleteTextVIewManager.setCallBack(productClickedCallback)
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