package com.example.hishab.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hishab.R
import com.example.hishab.adapter.BuyingAdapter
import com.example.hishab.interfaces.IAddProductCallback
import com.example.hishab.interfaces.IHandleAlertDialog
import com.example.hishab.interfaces.ISwipeItemCallback
import com.example.hishab.databinding.FragmentAddBuyingBinding
import com.example.hishab.models.BuyingItemProxy
import com.example.hishab.models.entities.Category
import com.example.hishab.models.entities.CategoryAndProductModel
import com.example.hishab.models.entities.CustomDate
import com.example.hishab.utils.SwipeToDeleteCallback
import com.example.hishab.utils.Util
import com.example.hishab.viewmodel.AddBuyingViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddBuyingFragment : Fragment() {
    private lateinit var binding: FragmentAddBuyingBinding
    val myCalendar = Calendar.getInstance()
    lateinit var inflate: View
    lateinit var recyclerView: RecyclerView
    lateinit var addProductCallBack: IAddProductCallback
    var adapter = BuyingAdapter()
    val args: AddBuyingFragmentArgs by navArgs()
    private val viewModel: AddBuyingViewModel by viewModels()
    lateinit var categoryAndProductModelList: List<CategoryAndProductModel>
    lateinit var distinctCategoryList: List<Category?>
    lateinit var swipeItemCallback: ISwipeItemCallback
    lateinit var handleAlertDialog: IHandleAlertDialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_buying, container, false)
        if (args.buyingHistory != null) {
            viewModel.buyingId = args.buyingHistory!!.getBuyingId()
            myCalendar.set(
                args.buyingHistory!!.getYear(),
                args.buyingHistory!!.getMonth(),
                args.buyingHistory!!.getDay()
            )
            binding.etDateTime.setDateText()
        }
        if (viewModel.buyingId != -1L)
            viewModel.isUpdating = true
        getExistingCategoryAndProducts()
        setSwipeItemCallback()
        setRecyclerView()
        setProductCallback()
        setButtonClickText()
        handleSubmitButtonClick()
        setHandleAlertDialog()
        if (viewModel.isUpdating)
            handleUpdateProduct()
        binding.etDateTime.transformIntoDatePicker(
            requireContext(), "MM/dd/yyyy", myCalendar,
            Date()
        );
        return binding.root;
    }

    private fun setButtonClickText() {
        if (viewModel.isUpdating)
            binding.btnSubmit.text = "update"
        else
            binding.btnSubmit.text = "add all"
    }

    private fun handleUpdateProduct() {
        CoroutineScope(Dispatchers.IO).launch {
            var purchaseHistoryList = viewModel.getPurchaseHistoryByBuyingId(viewModel.buyingId)
            var proxyId = 1
            purchaseHistoryList?.forEach({
                adapter.dataSource.add(
                    Util.convertPurchaseHistoryToAddItemProxy(
                        proxyId.toLong(),
                        viewModel.buyingId,
                        it
                    )
                )
                proxyId++
            })
            withContext(Dispatchers.Main)
            {
                adapter.submitList(adapter.dataSource)
            }
        }
    }

    private fun setHandleAlertDialog() {
        handleAlertDialog = object : IHandleAlertDialog {
            override fun onHandleDialog(isYes: Boolean, deleteId: Long) {
                if (isYes) {
                    var addItemProxy = adapter.dataSource.filter { it.proxyId == deleteId }[0]
                    adapter.dataSource.remove(addItemProxy)
                    adapter.submitList(adapter.dataSource)
                    if (addItemProxy.isUpdating()) {
                        CoroutineScope(Dispatchers.IO).launch {
                            viewModel.deletePurchaseItem(addItemProxy.purchaseItem);
                        }
                    }
                }
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun handleSubmitButtonClick() {
        binding.btnSubmit.setOnClickListener(View.OnClickListener {
            var buyingDate = CustomDate(
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            )
            CoroutineScope(Dispatchers.Main).launch {
                if (!viewModel.isUpdating) {
                    viewModel.insertBuying(adapter.dataSource, buyingDate, myCalendar.timeInMillis)
                } else {
                    viewModel.updateBuying(adapter.dataSource, buyingDate)
                }
            }

        })
    }

    private fun getExistingCategoryAndProducts() {
        CoroutineScope(Dispatchers.Main).launch {//Main thread query needs to be changed
            viewModel.getProductCategoryList().observe(viewLifecycleOwner, {
                categoryAndProductModelList = it
                distinctCategoryList =
                    categoryAndProductModelList.distinctBy { it.getCategoryId() }.map {
                        it.getCategoryId()?.let { it1 ->
                            it.getCategoryName()?.let { it2 -> Category(it1, it2) }
                        }
                    }
            });
        }

        binding.root.findViewById<FloatingActionButton>(R.id.fab)
            .setOnClickListener(View.OnClickListener {
                goToAddDialog()
            })
    }

    private fun goToAddDialog(buyingItemProxy: BuyingItemProxy? = null) {
        val actionAddBuyingFragmentToAddShoppingFragment =
            AddBuyingFragmentDirections.actionAddBuyingFragmentToAddShoppingFragment(
                categoryAndProductModelList.toTypedArray(),
                distinctCategoryList.toTypedArray(),
                if (buyingItemProxy == null) adapter.currentList.size + 1 else buyingItemProxy.proxyId.toInt()
            )
        actionAddBuyingFragmentToAddShoppingFragment.buyingItemProxy = buyingItemProxy
        actionAddBuyingFragmentToAddShoppingFragment.callback = addProductCallBack
        findNavController().navigate(actionAddBuyingFragmentToAddShoppingFragment)
    }

    private fun setProductCallback() {
        addProductCallBack = object : IAddProductCallback {
            override fun onAddedCallback(product: BuyingItemProxy) {
                product.setBuyingId(viewModel.buyingId)
                adapter.dataSource.add(product)
                adapter.submitList(adapter.dataSource)
            }

            override fun onUpdateCallBack(product: BuyingItemProxy) {
                adapter.submitList(adapter.dataSource)
                adapter.notifyDataSetChanged()
            }
        }
    }

    fun EditText.transformIntoDatePicker(
        context: Context,
        format: String,
        myCalendar: Calendar,
        maxDate: Date? = null
    ) {
        isFocusableInTouchMode = false
        isClickable = true
        isFocusable = false
        val datePickerOnDataSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                setDateText()
            }

        setOnClickListener {
            var datePickerDialog: DatePickerDialog
            datePickerDialog = DatePickerDialog(
                context, datePickerOnDataSetListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.run {
                maxDate?.time?.also { datePicker.maxDate = it }
                show()
            }
        }
    }

    private fun EditText.setDateText() {
        val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.UK)
        setText(sdf.format(myCalendar.time))
    }

    private fun setRecyclerView() {
        recyclerView = binding.root.findViewById<RecyclerView>(R.id.rv_add_buying)!!
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        ItemTouchHelper(SwipeToDeleteCallback(context, swipeItemCallback)).attachToRecyclerView(
            recyclerView
        )
    }

    private fun setSwipeItemCallback() {
        swipeItemCallback = object : ISwipeItemCallback {
            override fun onSwipeItem(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.LEFT) {
                    CoroutineScope(Dispatchers.IO).launch {
                        var id = adapter.getElementAt(viewHolder.adapterPosition).proxyId
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
                    goToAddDialog(adapter.dataSource[viewHolder.adapterPosition])
                }
            }
        }
    }
}