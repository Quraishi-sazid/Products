package com.example.hishab.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hishab.R
import com.example.hishab.databinding.FragmentAddShoppingBinding
import com.example.hishab.models.entities.ShoppingItem
import com.example.hishab.viewmodel.AddShoppingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AddShoppingFragment : Fragment() {

    private lateinit var binding: FragmentAddShoppingBinding
    private val vm:AddShoppingViewModel by viewModels()
    val myCalendar = Calendar.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_add_shopping,container,false)
        binding.shoppingItem=vm.shoppingItem
        binding.purchaseItem=vm.purchaseItem
        binding.category=vm.category
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
            DatePickerDialog(
                context, datePickerOnDataSetListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).run {
                maxDate?.time?.also { datePicker.maxDate = it }
                show()
            }
        }
    }
    private fun setButtonClick() {
        binding.btnSubmit.setOnClickListener(View.OnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                vm.purchaseItem.day=myCalendar.get(Calendar.DAY_OF_MONTH)
                vm.purchaseItem.month=myCalendar.get(Calendar.MONTH)+1
                vm.purchaseItem.year=myCalendar.get(Calendar.YEAR)
                vm.insertPurchaseItem()
                withContext(Dispatchers.Main)
                {
                    Toast.makeText(requireContext(),"purchase data inserted",Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_addShoppingFragment_to_purchaseHistoryFragment)
                }
            }
        })
    }
}