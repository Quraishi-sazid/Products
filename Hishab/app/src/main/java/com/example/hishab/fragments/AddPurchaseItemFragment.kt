package com.example.hishab.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import io.reactivex.disposables.CompositeDisposable


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
        addPurchaseItemViewModel.categoryProductMappingList = args.productCategoryArray.toList()
        setValuesToView()
        setAdapterToAutoCompleteTextBoxes()
        setSubmitButtonClick()
        return binding.root
    }

    @Override
    override fun onStart() {
        super.onStart()
        if (getDialog() == null)
            return;
        var dialogWidth = 700
        var dialogHeight = 900
        getDialog()?.getWindow()?.setLayout(dialogWidth, dialogHeight);
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
            args.productCategoryArray.filter { it.getProductName() != null }.toList()
        )
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
            var validationModelWithMessage = addPurchaseItemViewModel.isValidInput()
            if (validationModelWithMessage.isValid) {
                if (addPurchaseItemViewModel.isUpdating) {
                    addPurchaseItemViewModel.handleInformationChange()
                    args.callback?.onUpdateCallBack(args.shoppingItemProxy!!)
                    addPurchaseItemViewModel.needToRestoreBackUp=false
                } else {
                    var returnShopingItem = ShoppingItemProxy(
                        (args.buyingListSize).toLong(),
                        addPurchaseItemViewModel.category,
                        addPurchaseItemViewModel.product,
                        addPurchaseItemViewModel.purchaseItem
                    )
                    args.callback?.onAddedCallback(returnShopingItem)
                    addPurchaseItemViewModel.needToRestoreBackUp=false
                }
                dismiss()
            } else{
                Toast.makeText(requireContext(), validationModelWithMessage.msg, Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }

    override fun onDismiss(dialog: DialogInterface) {
        if(addPurchaseItemViewModel.needToRestoreBackUp)
            addPurchaseItemViewModel.restoreBackUp()
        dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}