package com.example.hishab.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hishab.R
import com.example.hishab.adapter.CategoryAdapter
import com.example.hishab.databinding.FragmentCategoryListBinding
import com.example.hishab.databinding.LayoutCategoryInputBinding
import com.example.hishab.interfaces.IHandleAlertDialog
import com.example.hishab.interfaces.ISwipeItemCallback
import com.example.hishab.models.CategoryProxy
import com.example.hishab.models.ShoppingItemProxy
import com.example.hishab.models.entities.Category
import com.example.hishab.utils.CustomAlertDialog
import com.example.hishab.utils.SwipeToDeleteCallback
import com.example.hishab.utils.Util
import com.example.hishab.viewmodel.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class CategoryListFragment : Fragment() {
    lateinit var binding: FragmentCategoryListBinding
    lateinit var categoryAdapter: CategoryAdapter
    private val categoryListViewModel: CategoryViewModel by viewModels()
    lateinit var handleAlertDialog: IHandleAlertDialog
    lateinit var swipeToDeleteCallback: SwipeToDeleteCallback<CategoryProxy>
    var compositeDisposable= CompositeDisposable()
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
        CoroutineScope(Dispatchers.Main).launch {
            categoryListViewModel.getCategoryWithTotalProductMapped().observe(viewLifecycleOwner,
                Observer {
                    var categoryList = it
                    categoryList.forEach({
                        it.proxyId = proxyId++
                    })
                    setUpRecyclerView(categoryList)
                })
        }
    }

    private fun setUpRecyclerView(categoryList: List<CategoryProxy>) {
        categoryAdapter = CategoryAdapter(categoryList)
        binding.rvCategory.layoutManager = LinearLayoutManager(activity)
        binding.rvCategory.adapter = categoryAdapter
        categoryAdapter.submitList(categoryAdapter.dataSource)
        swipeToDeleteCallback = SwipeToDeleteCallback<CategoryProxy>(requireContext())
        ItemTouchHelper(swipeToDeleteCallback).attachToRecyclerView(
            binding.rvCategory
        )
        swipeToDeleteCallback.onSwipeObservable.subscribe { rvSwipeItemResponse ->
            if (rvSwipeItemResponse.direction == ItemTouchHelper.LEFT) {
                CoroutineScope(Dispatchers.IO).launch {
                    var id =
                        categoryAdapter.getElementAt(rvSwipeItemResponse.adapterPosition).categoryId
                    activity?.runOnUiThread(Runnable {
                        Util.showItemSwipeDeleteAlertDialog(
                            requireContext(),
                            "Delete Entry",
                            "Do you want to delete this entry?",
                            id,
                            handleAlertDialog
                        )
                    })
                }
            } else {
                showDialog(categoryAdapter.dataSource[rvSwipeItemResponse.adapterPosition].getCategory())
            }
        }
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
        var isUpdating = updateCategory == null
        var customAlertDialog = CustomAlertDialog<LayoutCategoryInputBinding>(
            requireContext(),
            R.layout.layout_category_input,
            R.id.btn_yes
        )
        compositeDisposable.add(customAlertDialog.onViewCreated.subscribe { binding ->
            binding.category = updateCategory
        })
        compositeDisposable.add(customAlertDialog.onSubmitButtonPressed.subscribe { binding ->
            CoroutineScope(Dispatchers.IO).launch {
                if (binding.category?.getCategoryName() != null && !binding.category?.getCategoryName()
                        .equals("")
                )
                    if (isUpdating)
                        categoryListViewModel.insertCategory(binding.category!!)
                    else {
                        categoryListViewModel.updateCategory(binding.category!!)
                    }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }
}