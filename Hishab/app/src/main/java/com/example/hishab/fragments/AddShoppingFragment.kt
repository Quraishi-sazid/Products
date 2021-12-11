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
import com.example.hishab.databinding.FragmentAddShoppingBinding
import com.example.hishab.models.BuyingItemProxy
import com.example.hishab.models.entities.Category
import com.example.hishab.models.entities.CategoryAndProductModel
import com.example.hishab.models.entities.PurchaseHistory
import com.example.hishab.viewmodel.AddShoppingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddShoppingFragment : DialogFragment() {

    private lateinit var binding: FragmentAddShoppingBinding
    private val addShoppingViewModel:AddShoppingViewModel by viewModels()
    private lateinit var updatePurchaseHistory:PurchaseHistory

    val args: AddShoppingFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_add_shopping, container, false)
        if(args.buyingItemProxy!=null)
        {
            addShoppingViewModel.isUpdating=true
            addShoppingViewModel.product= args.buyingItemProxy!!.product
            addShoppingViewModel.category= args.buyingItemProxy!!.category
            addShoppingViewModel.purchaseItem= args.buyingItemProxy!!.purchaseItem
            addShoppingViewModel.backUpCategory=addShoppingViewModel.category.copyOf()
            addShoppingViewModel.backUpProduct=addShoppingViewModel.product.copyOf()
        }
        binding.product=addShoppingViewModel.product
        binding.purchaseItem=addShoppingViewModel.purchaseItem
        binding.category=addShoppingViewModel.category

                binding.etProductName.setAdapter(context?.let {
                    ArrayAdapter<CategoryAndProductModel>(
                        it, android.R.layout.simple_dropdown_item_1line, args.productCategoryArray)
                })
                binding.etProductName.setOnItemClickListener(AdapterView.OnItemClickListener(){ adapterView: AdapterView<*>, view1: View, position: Int, id: Long ->
                    val item = adapterView.adapter.getItem(position)
                    val categoryAndProductModel = item as CategoryAndProductModel;
                        addShoppingViewModel.category.setCategoryName(categoryAndProductModel.getCategoryName()!!)
                    addShoppingViewModel.product.productId= categoryAndProductModel.getProductId()!!;
                })
        binding.etCategory.setAdapter(context?.let {
            ArrayAdapter<Category>(
                it, android.R.layout.simple_dropdown_item_1line, args.distinctCategoryArray)
        })
        binding.etCategory.setOnItemClickListener(AdapterView.OnItemClickListener(){ adapterView: AdapterView<*>, view1: View, position: Int, id: Long ->
            val item = adapterView.adapter.getItem(position)
            addShoppingViewModel.category = item as Category;
        })
        setButtonClick()
        return binding.root
    }


    private fun setButtonClick() {
        binding.btnSubmit.setOnClickListener(View.OnClickListener {
                if(addShoppingViewModel.isUpdating)
                {
                    addShoppingViewModel.handleInformationChange()
                    args.callback?.onUpdateCallBack(args.buyingItemProxy!!)
                }
                else
                {
                    args.callback?.onAddedCallback(BuyingItemProxy((args.buyingListSize).toLong(),addShoppingViewModel.category,addShoppingViewModel.product,addShoppingViewModel.purchaseItem))
                }
               dismiss()
        })
    }
}