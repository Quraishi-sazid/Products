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
import com.example.hishab.adapter.AddShoppingAdapter
import com.example.hishab.interfaces.IAddOrUpdateProductCallback
import com.example.hishab.interfaces.IHandleAlertDialog
import com.example.hishab.interfaces.ISwipeItemCallback
import com.example.hishab.databinding.FragmentAddShoppingBinding
import com.example.hishab.models.ShoppingItemProxy
import com.example.hishab.models.entities.Category
import com.example.hishab.models.CategoryAndProductModel
import com.example.hishab.models.entities.CustomDate
import com.example.hishab.utils.SwipeToDeleteCallback
import com.example.hishab.utils.Util
import com.example.hishab.viewmodel.AddShoppingViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.lang.Runnable
import java.text.SimpleDateFormat
import java.util.*


//This class refactoration remains
@AndroidEntryPoint
class AddShoppingFragment : Fragment() {
    private lateinit var binding: FragmentAddShoppingBinding
    val myCalendar = Calendar.getInstance()
    lateinit var inflate: View
    lateinit var recyclerView: RecyclerView
    lateinit var addOrUpdateProductCallBack: IAddOrUpdateProductCallback
    var adapter = AddShoppingAdapter()
    val args: AddShoppingFragmentArgs by navArgs()
    private val viewModel: AddShoppingViewModel by viewModels()
    lateinit var categoryAndProductModelList: List<CategoryAndProductModel>
    lateinit var distinctCategoryList: List<Category?>
    lateinit var handleAlertDialog: IHandleAlertDialog
    lateinit var swipeToDeleteCallback: SwipeToDeleteCallback<ShoppingItemProxy>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_shopping, container, false)
        if (args.shoppingHistory != null) {
            viewModel.buyingId = args.shoppingHistory!!.getBuyingId()
            viewModel.updatingShoppingHistory = args.shoppingHistory
            myCalendar.timeInMillis = args.shoppingHistory!!.getTime()
            viewModel.time = args.shoppingHistory!!.getTime()
        }
        else if(args.buyingId != -1){
            viewModel.buyingId = args.buyingId.toLong()
            viewModel.time = args.time
            viewModel.productId = args.productId
            myCalendar.timeInMillis = args.time
            adapter.selectedProductId = viewModel.productId
        }

        if (viewModel.buyingId != -1L)
            viewModel.isUpdating = true
        binding.etDateTime.setDateText()
        getExistingCategoryAndProducts()
        setRecyclerView()
        setProductCallback()
        setButtonClickText()
        handleSubmitButtonClick()
        setHandleAlertDialog()
        setTimePicker()
        if (viewModel.isUpdating)
            handleUpdateProduct()
        binding.etDateTime.transformIntoDatePicker(
            requireContext(), "MM/dd/yyyy", myCalendar,
            Date()
        );
        return binding.root;
    }

    private fun setTimePicker() {
        binding.tpShoppingTime.hour = viewModel.getTimePickerHour()
        binding.tpShoppingTime.minute = viewModel.getTimePickerMin()
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
                    var addItemProxy = deleteFromRecyclerView(deleteId)
                    deleteFromDataBase(addItemProxy)
                }
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun deleteFromDataBase(addItemProxy: ShoppingItemProxy) {
        if (addItemProxy.isUpdating()) {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.deletePurchaseItem(addItemProxy.purchaseItem);
            }
        }
    }

    private fun deleteFromRecyclerView(deleteId: Long): ShoppingItemProxy {
        var addItemProxy = adapter.dataSource.filter { it.proxyId == deleteId }[0]
        adapter.dataSource.remove(addItemProxy)
        adapter.submitList(adapter.dataSource)
        return addItemProxy
    }

    private fun handleSubmitButtonClick() {
        binding.btnSubmit.setOnClickListener(View.OnClickListener {
            var buyingDate = CustomDate(
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH) + 1,
                myCalendar.get(Calendar.DATE)
            )
            CoroutineScope(Dispatchers.IO).launch {
                if (!viewModel.isUpdating) {
                    viewModel.insertBuying(adapter.dataSource, buyingDate, Util.getMilisecFromTimePicker(myCalendar,binding.tpShoppingTime))
                } else {
                    viewModel.updateBuying(adapter.dataSource, buyingDate, Util.getMilisecFromTimePicker(myCalendar,binding.tpShoppingTime))
                }
                withContext(Dispatchers.Main){
                    activity?.onBackPressed()
                }

            }
        })
    }

    private fun getExistingCategoryAndProducts() {
            viewModel.getProductCategoryList().observe(viewLifecycleOwner, {
                categoryAndProductModelList = it
                distinctCategoryList =
                    categoryAndProductModelList.distinctBy { it.getCategoryId() }.map {
                        it.getCategoryId()?.let { it1 ->
                            it.getCategoryName()?.let { it2 -> Category(it1, it2) }
                        }
                    }
            });

        binding.root.findViewById<FloatingActionButton>(R.id.fab)
            .setOnClickListener(View.OnClickListener {
                goToAddDialog()
            })

    }

    private fun goToAddDialog(shoppingItemProxy: ShoppingItemProxy? = null) {
        val shoppingToPurchaseItemFragmentAction =
            AddShoppingFragmentDirections.actionAddShoppingFragmentToAddPurchaseItemFragment(
                categoryAndProductModelList.toTypedArray(),
                distinctCategoryList.toTypedArray(),
                shoppingItemProxy?.proxyId?.toInt() ?: adapter.currentList.size + 1
            )
        shoppingToPurchaseItemFragmentAction.shoppingItemProxy = shoppingItemProxy
        shoppingToPurchaseItemFragmentAction.callback = addOrUpdateProductCallBack
        findNavController().navigate(shoppingToPurchaseItemFragmentAction)
    }

    private fun setProductCallback() {
        addOrUpdateProductCallBack = object : IAddOrUpdateProductCallback {
            override fun onAddedCallback(product: ShoppingItemProxy) {
                product.setBuyingId(viewModel.buyingId)
                adapter.dataSource.add(product)
                adapter.submitList(adapter.dataSource)
            }

            override fun onUpdateCallBack(product: ShoppingItemProxy) {
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
                myCalendar.set(Calendar.YEAR,year)
                myCalendar.set(Calendar.MONTH,monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                setDateText()
            }

        setOnClickListener {
            var datePickerDialog: DatePickerDialog = DatePickerDialog(
                context, datePickerOnDataSetListener, myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DATE)
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
        swipeToDeleteCallback = SwipeToDeleteCallback<ShoppingItemProxy>(requireContext())
        ItemTouchHelper(swipeToDeleteCallback).attachToRecyclerView(
            recyclerView
        )
        swipeToDeleteCallback.onSwipeObservable.subscribe { rvItemSwipeResponse ->
            if (rvItemSwipeResponse.direction == ItemTouchHelper.LEFT) {
                CoroutineScope(Dispatchers.IO).launch {
                    var id = adapter.getElementAt(rvItemSwipeResponse.adapterPosition).proxyId
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
                goToAddDialog(adapter.dataSource[rvItemSwipeResponse.adapterPosition])
            }
        }
    }
}