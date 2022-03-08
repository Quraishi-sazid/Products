package com.example.hishab.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.hishab.R
import com.example.hishab.databinding.FragmentAddPurchaseItemBinding
import com.example.hishab.models.ShoppingItemProxy
import com.example.hishab.models.entities.Category
import com.example.hishab.models.CategoryAndProductModel
import com.example.hishab.utils.AutoCompleteTextViewManager
import com.example.hishab.viewmodel.AddPurchaseItemViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable

@AndroidEntryPoint
class AddPurchaseItemFragment : DialogFragment() {
    private lateinit var binding: FragmentAddPurchaseItemBinding
    private val addPurchaseItemViewModel: AddPurchaseItemViewModel by viewModels()
    lateinit var categoryAutoCompleteTextVIewManager: AutoCompleteTextViewManager<Category>
    lateinit var productAutoCompleteTextVIewManager: AutoCompleteTextViewManager<CategoryAndProductModel>
    val args: AddPurchaseItemFragmentArgs by navArgs()
    val compositeDisposable = CompositeDisposable()
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
        setAdapterToAutoCompleteTextBoxes()
        setSubmitButtonClick()
        return binding.root
    }

    private fun checkIsUpdating() = args.shoppingItemProxy != null

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
        compositeDisposable.add(categoryAutoCompleteTextVIewManager.onAutoCompleteSelectionChanged.subscribe { selectedCategory ->
            if (selectedCategory != null) {
                addPurchaseItemViewModel.category = selectedCategory
            }
        })
        compositeDisposable.add(productAutoCompleteTextVIewManager.onAutoCompleteSelectionChanged.subscribe { selectedProduct ->
            if (selectedProduct != null) {
                addPurchaseItemViewModel.category.setCategoryName(selectedProduct.getCategoryName()!!)
                addPurchaseItemViewModel.product.productId =
                    selectedProduct.getProductId()!!
            }
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

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}