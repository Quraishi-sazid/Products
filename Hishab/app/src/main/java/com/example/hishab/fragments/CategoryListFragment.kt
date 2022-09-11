package com.example.hishab.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hishab.R
import com.example.hishab.adapter.SimpleGenericAdapterWithBinding
import com.example.hishab.databinding.FragmentCategoryListBinding
import com.example.hishab.databinding.LayoutCategoryInputBinding
import com.example.hishab.databinding.LayoutCategroyItemBinding
import com.example.hishab.interfaces.IHandleAlertDialog
import com.example.hishab.models.CategoryProxy
import com.example.hishab.models.entities.Category
import com.example.hishab.utils.CustomAlertDialog
import com.example.hishab.utils.SwipeToDeleteCallback
import com.example.hishab.utils.Util
import com.example.hishab.viewmodel.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class CategoryListFragment : Fragment()/*,IViewPagerSwipeListener*/ {
    lateinit var binding: FragmentCategoryListBinding
    lateinit var categoryAdapter: SimpleGenericAdapterWithBinding<CategoryProxy, LayoutCategroyItemBinding>
    private val categoryListViewModel: CategoryViewModel by viewModels()
    lateinit var handleAlertDialog: IHandleAlertDialog
    lateinit var swipeToDeleteCallback: SwipeToDeleteCallback<CategoryProxy>
    var compositeDisposable = CompositeDisposable()
    var proxyId = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_category_list, container, false)
        getCategoryData()
        setFabClick()
        setHandleAlertDialog()
        return binding.root
    }

    private fun setFabClick() {
        binding.fabCategory.setOnClickListener(View.OnClickListener {
            showDialog()
        })
    }

    private fun getCategoryData() {
        categoryListViewModel.getCategoryWithTotalProductMapped().observe(viewLifecycleOwner,
            Observer { categoryList ->
                categoryList.forEach({
                    it.proxyId = proxyId++
                })
                setUpRecyclerView(categoryList)
            })
    }

    private fun setUpRecyclerView(categoryList: List<CategoryProxy>) {
        categoryAdapter = SimpleGenericAdapterWithBinding.Create(R.layout.layout_categroy_item)
        categoryAdapter.viewInlateLiveData.observe(viewLifecycleOwner) {
            it.second.category = it.first
        }
        categoryAdapter.viewClickLiveData.observe(viewLifecycleOwner) {
            var directions =
                ViewPagerTabFragmentDirections.actionViewPagerTabFragmentToProductCategoryMappingFragment()
            directions.categoryId = it!!.categoryId.toInt()
            findNavController().navigate(directions)
        }
        binding.rvCategory.layoutManager = LinearLayoutManager(activity)
        binding.rvCategory.adapter = categoryAdapter
        categoryAdapter.update(categoryList)
        swipeToDeleteCallback = SwipeToDeleteCallback<CategoryProxy>(requireContext())
        ItemTouchHelper(swipeToDeleteCallback).attachToRecyclerView(
            binding.rvCategory
        )
        swipeToDeleteCallback.onSwipeObservable.subscribe { rvSwipeItemResponse ->
            if (rvSwipeItemResponse.direction == ItemTouchHelper.LEFT) {
                CoroutineScope(Dispatchers.IO).launch {
                    var id =
                        categoryAdapter.getElementAt(rvSwipeItemResponse.adapterPosition).categoryId
                    var categoryName =
                        categoryAdapter.getElementAt(rvSwipeItemResponse.adapterPosition).categoryName
                    var totalProductMappedWithCategroy =
                        categoryListViewModel.getProductCountMappedWithCategoryId(id)
                    activity?.runOnUiThread(Runnable {
                        if (totalProductMappedWithCategroy == 0) {
                            Util.showItemSwipeDeleteAlertDialog(
                                requireContext(),
                                "Delete Entry",
                                "Do you want to delete this entry?",
                                id,
                                handleAlertDialog
                            )
                        } else
                            Toast.makeText(
                                context,
                                "$totalProductMappedWithCategroy products mapped with $categoryName category. can't be deleted",
                                Toast.LENGTH_LONG
                            ).show()

                    })
                }
            } else {
                showDialog(categoryAdapter.dataSource[rvSwipeItemResponse.adapterPosition].getCategory())
            }
        }
        //  onSwipedRightOrLeft=Util.getViewSwipeObservable(binding.rvCategory)
    }

    private fun setHandleAlertDialog() {
        handleAlertDialog = object : IHandleAlertDialog {
            override fun onHandleDialog(isYes: Boolean, deleteId: Long) {
                if (isYes) {
                    CoroutineScope(Dispatchers.IO).launch {
                        categoryListViewModel.deleteCategoryById(deleteId)
                    }
                }
                categoryAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun showDialog(updateCategory: Category? = null) {
        var isUpdating = updateCategory != null
        var oldCategoryName:String? = null
        var customAlertDialog = CustomAlertDialog<LayoutCategoryInputBinding>(
            requireContext(),
            R.layout.layout_category_input,
            R.id.btn_yes
        )
        compositeDisposable.add(customAlertDialog.onViewCreated.subscribe { binding ->
            if (updateCategory != null) {
                oldCategoryName = updateCategory.getCategoryName()
                binding.category = updateCategory
            } else
                binding.category = Category()
        })
        compositeDisposable.add(customAlertDialog.onSubmitButtonPressed.subscribe { binding ->
            CoroutineScope(Dispatchers.IO).launch {
                if (isNewCategory(binding.category?.getCategoryName())) {
                    if (binding.category?.getCategoryName() != null && !binding.category?.getCategoryName()
                            .equals("")
                    ) {
                        if (isUpdating)
                            categoryListViewModel.updateCategory(binding.category!!,oldCategoryName)
                        else {
                            categoryListViewModel.insertCategory(binding.category!!)
                        }
                    }
                    customAlertDialog.dismiss()
                } else
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            "Category already exists",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        })
    }

    private fun isNewCategory(categoryName: String?): Boolean {
        return categoryAdapter.dataSource?.filter { categoryProxy -> categoryProxy.categoryName == categoryName }
            .isEmpty()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

    lateinit var onSwipedRightOrLeft: Observable<Pair<Float, Float>>
}