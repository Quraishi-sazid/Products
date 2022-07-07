package com.example.hishab.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hishab.R
import com.example.hishab.adapter.GenericAdapterForTwoViewTypes
import com.example.hishab.adapter.SimpleGenericAdapterWithBinding
import com.example.hishab.databinding.FragmentProductCategoryMappingBinding
import com.example.hishab.databinding.LayoutMonthItemBinding
import com.example.hishab.databinding.LayoutProductCategoryItemBinding
import com.example.hishab.databinding.LayoutProductCategoryMappingInputBinding
import com.example.hishab.interfaces.ISwipeEnableChecker
import com.example.hishab.interfaces.IViewPagerSwipeListener
import com.example.hishab.models.CategoryAndProductModel
import com.example.hishab.models.entities.Category
import com.example.hishab.models.entities.Product
import com.example.hishab.utils.AutoCompleteTextViewManager
import com.example.hishab.utils.CustomAlertDialog
import com.example.hishab.utils.SwipeToDeleteCallback
import com.example.hishab.utils.Util
import com.example.hishab.viewmodel.ProductCategoryMappingViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.function.Predicate
import kotlin.coroutines.coroutineContext

@AndroidEntryPoint
class ProductCategoryMappingFragment : Fragment(), IViewPagerSwipeListener {
    private lateinit var binding: FragmentProductCategoryMappingBinding
    private val productCategoryMappingViewModel: ProductCategoryMappingViewModel by viewModels()
    lateinit var simpleGenericAdapterWithBinding: GenericAdapterForTwoViewTypes<String, CategoryAndProductModel, LayoutMonthItemBinding, LayoutProductCategoryItemBinding>
    var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var swipeToDeleteCallback: SwipeToDeleteCallback<CategoryAndProductModel>
    public val args: ProductCategoryMappingFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_product_category_mapping,
            container,
            false
        )
        productCategoryMappingViewModel
        setRecyclerView()
        setFabClick()
        fetchProductCategoryMapping()
        return binding.root
    }

    private fun setRecyclerView() {
        val diffUtilCallback = object : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(
                oldItem: Any,
                newItem: Any
            ): Boolean {
                if (oldItem is String && newItem is String)
                    return oldItem.equals(newItem)
                else if (oldItem is CategoryAndProductModel && newItem is CategoryAndProductModel)
                    return oldItem.getProductId() == newItem.getProductId()
                            && oldItem.getCategoryId() == newItem.getCategoryId()
                return false
            }

            override fun areContentsTheSame(
                oldItem: Any,
                newItem: Any
            ): Boolean {
                if (oldItem is String && newItem is String)
                    return true
                else if (oldItem is CategoryAndProductModel && newItem is CategoryAndProductModel)
                    return oldItem.getCategoryName().equals(newItem.getCategoryName()) &&
                            oldItem.getProductName().equals(newItem.getProductName())
                return false
            }
        }
        binding.rvProductCategory.layoutManager = LinearLayoutManager(activity)
        simpleGenericAdapterWithBinding =
            GenericAdapterForTwoViewTypes.Create(
                R.layout.layout_month_item,
                R.layout.layout_product_category_item,
                { simpleGenericAdapterWithBinding.getItemAt(it) is String },
                diffUtilCallback
            )
        simpleGenericAdapterWithBinding.firstViewInlateLiveData.observe(viewLifecycleOwner) {
            it.second.month1 = it.first
        }
        simpleGenericAdapterWithBinding.secondViewInlateLiveData.observe(viewLifecycleOwner) {
            it.second.productCategoryMapping = it.first
        }
        simpleGenericAdapterWithBinding.secondviewClickLiveData.observe(viewLifecycleOwner) {
            try {
                val directions =
                    ViewPagerTabFragmentDirections.actionViewPagerTabFragmentToProductDetailsFragment()
                directions.productId = it.getProductId()!!
                findNavController().navigate(directions)
            } catch (ex: Exception) {
                val directions =
                    ProductCategoryMappingFragmentDirections.actionProductCategoryMappingFragmentToProductDetailsFragment()
                directions.productId = it.getProductId()!!
                findNavController().navigate(directions)
            }

        }
        binding.rvProductCategory.adapter = simpleGenericAdapterWithBinding
        onSwipedRightOrLeft = Util.getViewSwipeObservable(binding.rvProductCategory)
        swipeToDeleteCallback = SwipeToDeleteCallback<CategoryAndProductModel>(requireContext())
        swipeToDeleteCallback.swipeEnableChecker = object : ISwipeEnableChecker {
            override fun isSwipeEnabled(position: Int): Boolean {
                return !(simpleGenericAdapterWithBinding.dataSource[position] is String)
            }
        }
        ItemTouchHelper(swipeToDeleteCallback).attachToRecyclerView(binding.rvProductCategory)
        compositeDisposable.add(swipeToDeleteCallback.onSwipeObservable.subscribe { swipedItemResponse ->
            if (swipedItemResponse.direction == ItemTouchHelper.LEFT) {
                val swippedCategoryAndProductModel =
                    simpleGenericAdapterWithBinding.dataSource[swipedItemResponse.adapterPosition] as CategoryAndProductModel
                compositeDisposable.add(
                    Observable.create<Int> { emitter ->
                        val count = productCategoryMappingViewModel.getPurchaseCountOfProductId(
                            swippedCategoryAndProductModel.getProductId()!!
                        )
                        emitter.onNext(count)
                    }.subscribeOn(Schedulers.io())
                        .map { count ->
                            if (count == 0)
                                productCategoryMappingViewModel.deleteProduct(
                                    swippedCategoryAndProductModel.getProductId()!!
                                )
                            return@map count
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .filter { count -> count > 0 }
                        .subscribe { count ->
                            Toast.makeText(
                                context,
                                count.toString() + " items found in purchased history of this product.can't be deleted",
                                Toast.LENGTH_LONG
                            ).show()
                        })
            } else {
                val swippedCategoryAndProductModel =
                    simpleGenericAdapterWithBinding.dataSource[swipedItemResponse.adapterPosition] as CategoryAndProductModel;
                var customAlertDialog = CustomAlertDialog<LayoutProductCategoryMappingInputBinding>(
                    requireContext(),
                    R.layout.layout_product_category_mapping_input,
                    R.id.btn_yes
                )
                compositeDisposable.add(customAlertDialog.onViewCreated.subscribe { binding ->
                    binding.product = Product(
                        swippedCategoryAndProductModel.getProductName()!!,
                        swippedCategoryAndProductModel.getCategoryId()!!,
                        swippedCategoryAndProductModel.getProductId()!!,

                        )
                    binding.category = Category(swippedCategoryAndProductModel.getCategoryName()!!)
                    binding.actvCategory.isEnabled = false
                })
                compositeDisposable.add(customAlertDialog.onSubmitButtonPressed.filter {
                    it.product!!.getProductName() != null
                }.observeOn(Schedulers.io()).map { binding ->
                    var isUpdateNeed = !binding.product!!.getProductName()
                        .equals(swippedCategoryAndProductModel.getCategoryName()) && isNewProduct(
                        binding.product!!.getProductName()
                    )
                    if (isUpdateNeed) {
                        productCategoryMappingViewModel.updateProductName(
                            swippedCategoryAndProductModel.getProductId()!!,
                            binding.product!!.getProductName()!!
                        )
                    } else
                        requireActivity().runOnUiThread() {
                            Toast.makeText(
                                requireContext(),
                                "product already exists",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                }.subscribe { _ ->
                    customAlertDialog.dismiss()
                })
            }
        })
    }

    private fun fetchProductCategoryMapping() {
        CoroutineScope(Dispatchers.IO).launch {
            productCategoryMappingViewModel.getAllCategories()
        }
        try {
            productCategoryMappingViewModel.getProductListFromCategoryIdInnerJoin(args.categoryId.toLong())
                .observe(viewLifecycleOwner) {
                    var categorySeparatedProductList =
                        productCategoryMappingViewModel.getCategorySeparatedProductList(it)
                    simpleGenericAdapterWithBinding.update(categorySeparatedProductList);
                }
        } catch (exception: Exception) {
            productCategoryMappingViewModel.getProductCategoryListInnerJoin()
                .observe(viewLifecycleOwner) {
                    var categorySeparatedProductList =
                        productCategoryMappingViewModel.getCategorySeparatedProductList(it)
                    simpleGenericAdapterWithBinding.update(categorySeparatedProductList);
                }
        }
    }

    private fun setFabClick() {
        binding.fabProductCategory.setOnClickListener({
            var customAlertDialog = CustomAlertDialog<LayoutProductCategoryMappingInputBinding>(
                requireContext(),
                R.layout.layout_product_category_mapping_input,
                R.id.btn_yes
            )
            compositeDisposable.add(customAlertDialog.onViewCreated.subscribe { binding ->
                binding.category = Category()
                binding.product = Product()
                CategoryAndProductModel.isCategoryStringNeeded = true
                var autoCompleteTextViewManager = AutoCompleteTextViewManager<Category>(
                    requireContext(),
                    binding.actvCategory,
                    productCategoryMappingViewModel.catagoryList
                );
                compositeDisposable.add(autoCompleteTextViewManager.onAutoCompleteSelectionChanged.subscribe {
                    binding.category = it!!
                })
            })
            compositeDisposable.add(customAlertDialog.onSubmitButtonPressed.filter { binding ->
                binding.category != null && binding.product != null
            }.subscribe { binding ->
                CoroutineScope(Dispatchers.IO).launch {
                    if (isNewProduct(binding.product!!.getProductName())) {
                        productCategoryMappingViewModel.insertCategoryProductMapping(
                            binding.category!!.categoryId,
                            binding.category!!.getCategoryName(),
                            binding.product!!.getProductName()
                        )
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                requireContext(),
                                "Product already exists",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    CategoryAndProductModel.isCategoryStringNeeded = false
                    customAlertDialog.dismiss()
                }
            })
            customAlertDialog.alertDialog.setOnCancelListener {
                CategoryAndProductModel.isCategoryStringNeeded = false
            }
        })
    }

    private fun isNewProduct(productName: String): Boolean {
        simpleGenericAdapterWithBinding.dataSource.forEach {
            if (it is CategoryAndProductModel && it.getProductName().equals(productName)) {
                return false
            }
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
    }

    override lateinit var onSwipedRightOrLeft: Observable<Pair<Float, Float>>
}