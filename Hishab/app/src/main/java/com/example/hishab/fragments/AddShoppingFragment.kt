package com.example.hishab.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hishab.R
import com.example.hishab.databinding.FragmentAddShoppingBinding
import com.example.hishab.models.entities.PurchaseHistory
import com.example.hishab.viewmodel.AddShoppingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddShoppingFragment : Fragment() {

    private lateinit var binding: FragmentAddShoppingBinding
    private val addShoppingViewModel:AddShoppingViewModel by viewModels()
    private lateinit var updatePurchaseHistory:PurchaseHistory
    val myCalendar = Calendar.getInstance()
    val args: AddShoppingFragmentArgs by navArgs()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_add_shopping,container,false)
        binding.shoppingItem=addShoppingViewModel.shoppingItem
        binding.purchaseItem=addShoppingViewModel.purchaseItem
        binding.category=addShoppingViewModel.category
        if(args.editHistory==null)
        {
            addShoppingViewModel.isUpdating=false
        }
        else
        {
            updatePurchaseHistory= args.editHistory!!
            addShoppingViewModel.setUpdateField(updatePurchaseHistory);
        }
        if(addShoppingViewModel.isUpdating)
        {
                var dateTimeString=updatePurchaseHistory.getDay().toString()+"/"+updatePurchaseHistory.getMonth().toString()+"/"+updatePurchaseHistory.getYear().toString()
                binding.etDateTime.setText(dateTimeString)
        }
        setButtonClick()
        binding.etDateTime.transformIntoDatePicker(requireContext(), "MM/dd/yyyy",myCalendar,
            Date()
        );
        return binding.root
    }

    fun EditText.transformIntoDatePicker(context: Context, format: String,myCalendar:Calendar ,maxDate: Date? = null) {
        isFocusableInTouchMode = false
        isClickable = true
        isFocusable = false

        val datePickerOnDataSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val sdf = SimpleDateFormat(format, Locale.UK)
                setText(sdf.format(myCalendar.time))
            }

        setOnClickListener {
            var datePickerDialog:DatePickerDialog
            datePickerDialog=DatePickerDialog(
                context, datePickerOnDataSetListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH))
            if(addShoppingViewModel.isUpdating)
            {
                datePickerDialog.updateDate(updatePurchaseHistory.getYear(),updatePurchaseHistory.getMonth(),updatePurchaseHistory.getDay())
            }

                datePickerDialog.run {
                maxDate?.time?.also { datePicker.maxDate = it }
                show()
            }
        }
    }
    private fun setButtonClick() {
        binding.btnSubmit.setOnClickListener(View.OnClickListener {
            if(!addShoppingViewModel.isUpdating)
            {
                CoroutineScope(Dispatchers.IO).launch {
                    addShoppingViewModel.purchaseItem.day=myCalendar.get(Calendar.DAY_OF_MONTH)
                    addShoppingViewModel.purchaseItem.month=myCalendar.get(Calendar.MONTH)+1
                    addShoppingViewModel.purchaseItem.year=myCalendar.get(Calendar.YEAR)
                    addShoppingViewModel.insertPurchaseItem()
                    withContext(Dispatchers.Main)
                    {
                        Toast.makeText(requireContext(),"purchase data inserted",Toast.LENGTH_LONG).show()
                        activity?.onBackPressed()
                       // findNavController().navigate(R.id.action_addShoppingFragment_to_purchaseHistoryFragment)
                    }
                }
            }
            else
            {
                CoroutineScope(Dispatchers.IO).launch {
                    addShoppingViewModel.purchaseItem.day=myCalendar.get(Calendar.DAY_OF_MONTH)
                    addShoppingViewModel.purchaseItem.month=myCalendar.get(Calendar.MONTH)+1
                    addShoppingViewModel.purchaseItem.year=myCalendar.get(Calendar.YEAR)
                    addShoppingViewModel.updatePurchaseItem(updatePurchaseHistory)
                    withContext(Dispatchers.Main)
                    {
                        Toast.makeText(requireContext(),"purchase data updated",Toast.LENGTH_LONG).show()
                        activity?.onBackPressed()
                     //   findNavController().navigate(R.id.action_addShoppingFragment_to_purchaseHistoryFragment)
                    }
                }
            }
        })
    }
}