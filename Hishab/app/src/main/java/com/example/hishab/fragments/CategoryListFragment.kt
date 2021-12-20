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
import com.example.hishab.interfaces.IHandleAlertDialog
import com.example.hishab.interfaces.ISwipeItemCallback
import com.example.hishab.models.CategoryProxy
import com.example.hishab.models.entities.Category
import com.example.hishab.utils.SwipeToDeleteCallback
import com.example.hishab.utils.Util
import com.example.hishab.viewmodel.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class CategoryListFragment : Fragment() {
    lateinit var binding: FragmentCategoryListBinding
    lateinit var categoryAdapter: CategoryAdapter
    lateinit var swipeItemCallback: ISwipeItemCallback
    private val categoryListViewModel: CategoryViewModel by viewModels()
    lateinit var handleAlertDialog: IHandleAlertDialog
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
        setSwipeItemCallback()
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
        ItemTouchHelper(SwipeToDeleteCallback(context, swipeItemCallback)).attachToRecyclerView(
            binding.rvCategory
        )
    }

    private fun setSwipeItemCallback() {
        swipeItemCallback = object : ISwipeItemCallback {
            override fun onSwipeItem(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.LEFT) {
                    CoroutineScope(Dispatchers.IO).launch {
                        var id = categoryAdapter.getElementAt(viewHolder.adapterPosition).categoryId
                        activity?.runOnUiThread(Runnable {
                            Util.showItemSwipeDeleteAlertDialog(
                                context!!,
                                "Delete Entry",
                                "Do you want to delete this entry?",
                                id,
                                handleAlertDialog
                            )
                        })
                    }
                } else {
                    showDialog(categoryAdapter.dataSource[viewHolder.adapterPosition].getCategory())
                }
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

    private fun showDialog(updateCategory:Category?=null) {
        val customDialog = Dialog(requireActivity())
        customDialog.setContentView(R.layout.layout_category_input)
        customDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        var editText = customDialog.findViewById(R.id.et_category) as EditText
        if(updateCategory!=null)
            editText.setText(updateCategory.getCategoryName())
        val yesBtn = customDialog.findViewById(R.id.btn_yes) as Button
        yesBtn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                var categoryName = editText.text.toString()
                if (categoryName != null && !categoryName.equals(""))
                    if(updateCategory==null)
                        categoryListViewModel.insertCategory(Category(categoryName))
                    else
                    {
                        updateCategory.setCategoryName(categoryName)
                        categoryListViewModel.updateCategory(updateCategory)
                    }
            }
        }
        customDialog.show()
    }

}