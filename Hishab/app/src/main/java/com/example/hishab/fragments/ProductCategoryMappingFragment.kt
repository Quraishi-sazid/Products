package com.example.hishab.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.example.hishab.interfaces.ISwipeItemCallback
import com.example.hishab.interfaces.IViewPagerSwipeListener
import com.example.hishab.models.CategoryAndProductModel
import com.example.hishab.models.entities.Category
import com.example.hishab.models.entities.Product
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

@AndroidEntryPoint
class ProductCategoryMappingFragment : Fragment(),IViewPagerSwipeListener {
    private lateinit var binding: FragmentProductCategoryMappingBinding
    private val productCategoryMappingViewModel: ProductCategoryMappingViewModel by viewModels()
    lateinit var categoryAndProductModelList: List<CategoryAndProductModel>
    lateinit var simpleGenericAdapterWithBinding: SimpleGenericAdapterWithBinding<CategoryAndProductModel, LayoutProductCategoryItemBinding>
    var compositeDisposable: CompositeDisposable = CompositeDisposable()
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
        var diffUtilCallback = object : DiffUtil.ItemCallback<CategoryAndProductModel>() {
            override fun areItemsTheSame(
                oldItem: CategoryAndProductModel,
                newItem: CategoryAndProductModel
            ): Boolean {
                return oldItem.getProductId() == newItem.getProductId()
                        && oldItem.getCategoryId() == newItem.getCategoryId()
            }

            override fun areContentsTheSame(
                oldItem: CategoryAndProductModel,
                newItem: CategoryAndProductModel
            ): Boolean {
                return oldItem.getCategoryName().equals(newItem.getCategoryName()) &&
                        oldItem.getProductName().equals(newItem.getProductName())
            }
        }
        binding.rvProductCategory.layoutManager = LinearLayoutManager(activity)
        simpleGenericAdapterWithBinding =
            SimpleGenericAdapterWithBinding.Create(R.layout.layout_product_category_item)
        simpleGenericAdapterWithBinding.viewInlateLiveData.observe(viewLifecycleOwner){
            it.second.productCategoryMapping = it.first
        }
        simpleGenericAdapterWithBinding.viewClickLiveData.observe(viewLifecycleOwner) {

        }
        binding.rvProductCategory.adapter = simpleGenericAdapterWithBinding
        onSwipedRightOrLeft=Util.getViewSwipeObservable(binding.rvProductCategory)
        swipeToDeleteCallback = SwipeToDeleteCallback<CategoryAndProductModel>(requireContext())
        ItemTouchHelper(swipeToDeleteCallback).attachToRecyclerView(binding.rvProductCategory)
        swipeToDeleteCallback.onSwipeObservable.subscribe { swipedItemResponse ->
            if (swipedItemResponse.direction == ItemTouchHelper.LEFT) {
                compositeDisposable.add(
                    Observable.create<Int> { emitter ->
                        var count = productCategoryMappingViewModel.getPurchaseCountOfProductId(
                            simpleGenericAdapterWithBinding.dataSource[swipedItemResponse.adapterPosition].getProductId()!!
                        )
                        emitter.onNext(count)
                    }.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { count ->
                            if (count == 0)
                                productCategoryMappingViewModel.deleteProduct(
                                    simpleGenericAdapterWithBinding.dataSource[swipedItemResponse.adapterPosition].getProductId()!!
                                )
                            else
                                Toast.makeText(
                                    context,
                                    count.toString() + " items found in purchased history of this product.can't be deleted",
                                    Toast.LENGTH_LONG
                                ).show()
                        })
            } else {
                var swippedCategoryAndProductModel =
                    simpleGenericAdapterWithBinding.dataSource[swipedItemResponse.adapterPosition];
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
                    productCategoryMappingViewModel.updateProductName(
                        swippedCategoryAndProductModel.getProductId()!!,
                        binding.product!!.getProductName()!!
                    )
                }.subscribe { _ ->
                    customAlertDialog.dismiss()
                })
            }
        }
    }

    private fun fetchProductCategoryMapping() {
        CoroutineScope(Dispatchers.Main).launch {
            productCategoryMappingViewModel.getProductCategoryListInnerJoin()
                .observe(viewLifecycleOwner, {
                    simpleGenericAdapterWithBinding.update(it);
                })
        }
    }

    private fun setFabClick() {
        binding.fabProductCategory.setOnClickListener(View.OnClickListener {
            var customAlertDialog = CustomAlertDialog<LayoutProductCategoryMappingInputBinding>(
                requireContext(),
                R.layout.layout_product_category_mapping_input,
                R.id.btn_yes
            )
            compositeDisposable.add(customAlertDialog.onViewCreated.subscribe { binding ->
                binding.category = Category()
                binding.product = Product()
            })
            compositeDisposable.add(customAlertDialog.onSubmitButtonPressed.filter { binding ->
                binding.category != null && binding.product != null
            }.subscribe { binding ->
                CoroutineScope(Dispatchers.IO).launch {
                    productCategoryMappingViewModel.insertCategoryProductMapping(
                        binding.category!!.categoryId,
                        binding.category!!.getCategoryName(),
                        binding.product!!.getProductName()
                    );
                }
            })
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
    }

    override lateinit var onSwipedRightOrLeft: Observable<Pair<Float, Float>>
}