package com.example.hishab.fragments

import android.nfc.Tag
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hishab.R
import com.example.hishab.adapter.SimpleGenericAdapterWithBinding
import com.example.hishab.databinding.FragmentAddBudgetBinding
import com.example.hishab.databinding.LayoutBudgetAddItemBinding
import com.example.hishab.databinding.LayoutBudgetSpentItemBinding
import com.example.hishab.models.entities.Budget
import com.example.hishab.utils.Util
import com.example.hishab.viewmodel.AddBudgetViewModel
import com.example.hishab.viewmodel.AddShoppingViewModel
import com.example.hishab.viewmodel.BudgetAndSpentViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class AddBudgetFragment : Fragment() {

    private val viewModel: AddBudgetViewModel by viewModels()
    val args: AddBudgetFragmentArgs by navArgs()
    lateinit var binding: FragmentAddBudgetBinding
    lateinit var categoryBudgetMappingAdapter: SimpleGenericAdapterWithBinding<Budget,LayoutBudgetAddItemBinding>
    var budgetList:List<Budget>?=null
    val compositeDisposable=CompositeDisposable()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_add_budget,container,false)
        budgetList=args.budgetList?.toList()
        setRecyclerView()
        setSearchBox()
        setSubmitButton()
        return binding.root
    }

    private fun setSubmitButton() {
        binding.btnSubmit.setOnClickListener{
            if(budgetList!=null)
                viewModel.updateBudgetList(budgetList!!)
            findNavController().popBackStack()
        }
    }


    private fun setRecyclerView() {
        var diffUtilCallback= object : DiffUtil.ItemCallback<Budget>() {
            override fun areItemsTheSame(oldItem: Budget, newItem: Budget): Boolean {
                return oldItem.getBudgetId()==newItem.getBudgetId()
            }

            override fun areContentsTheSame(oldItem: Budget, newItem: Budget): Boolean {
                return oldItem.categoryName.equals(newItem.categoryName) &&
                        oldItem.budget==newItem.budget
            }
        }

        categoryBudgetMappingAdapter= SimpleGenericAdapterWithBinding.Create<Budget,LayoutBudgetAddItemBinding>(R.layout.layout_budget_add_item,diffUtilCallback)
        categoryBudgetMappingAdapter.viewInlateLiveData.observe(viewLifecycleOwner) {
            it.second.budget = it.first
        }
        binding.rvBudgetInput.layoutManager=LinearLayoutManager(activity)
        binding.rvBudgetInput.adapter=categoryBudgetMappingAdapter
        if(budgetList!=null)
        categoryBudgetMappingAdapter.update(budgetList!!)
    }

    private fun setSearchBox() {
        compositeDisposable.add(Observable.create<String> { emitter->
            binding.etSearch.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                   emitter.onNext(p0.toString().lowercase())
                }

            })
        }.debounce(1000,TimeUnit.MILLISECONDS).filter{budgetList!=null}.subscribe{
            searchText->
            Log.d("AddBudgetFragment",searchText)
            if(searchText==null || searchText.equals(""))
            {
                categoryBudgetMappingAdapter.update(budgetList!!)
            }
            else
            {
                var filteredList=budgetList?.filter { budget -> budget.categoryName!!.lowercase().contains(searchText)  }
                categoryBudgetMappingAdapter.update(filteredList!!)
            }
        })


    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

}