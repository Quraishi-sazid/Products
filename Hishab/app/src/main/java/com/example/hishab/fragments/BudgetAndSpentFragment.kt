package com.example.hishab.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hishab.R
import com.example.hishab.adapter.SimpleGenericAdapterWithBinding
import com.example.hishab.databinding.FragmentBudgetAndSpentBinding
import com.example.hishab.databinding.LayoutBudgetSpentItemBinding
import com.example.hishab.models.entities.Budget
import com.example.hishab.utils.Util
import com.example.hishab.viewmodel.BudgetAndSpentViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.collections.ArrayList

@AndroidEntryPoint
class BudgetAndSpentFragment : Fragment() {


    private lateinit var binding: FragmentBudgetAndSpentBinding;
    private val budgetAndSpentViewModel: BudgetAndSpentViewModel by viewModels()
    private lateinit var simpleGenericAdapterWithBinding: SimpleGenericAdapterWithBinding<Budget,LayoutBudgetSpentItemBinding>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d("BudgetAndSpentFragment ","in BudgetAndSpentFragment")
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_budget_and_spent, container, false)
        fetchCategoryBudgetListFromDatabase()
        setRecyclerView()
        setFabClick()
        return binding.root
    }

    private fun setFabClick() {
        binding.fabEditBudget.setOnClickListener{
            var directions=ViewPagerTabFragmentDirections.actionViewPagerTabFragmentToAddBudgetFragment()
            //var directions=BudgetAndSpentFragmentDirections.actionBudgetAndSpentFragmentToAddBudgetFragment()
            directions.budgetList=simpleGenericAdapterWithBinding.dataSource.toTypedArray()
            findNavController().navigate(directions)
        }
    }

    private fun setRecyclerView() {
        simpleGenericAdapterWithBinding=SimpleGenericAdapterWithBinding.Create<Budget,LayoutBudgetSpentItemBinding>(R.layout.layout_budget_spent_item)
        simpleGenericAdapterWithBinding.viewInlateLiveData.observe(viewLifecycleOwner){
            it.second.budget=it.first
        }
        simpleGenericAdapterWithBinding.viewClickLiveData.observe(viewLifecycleOwner){

        }
        binding.rvCategoryBudget.layoutManager=LinearLayoutManager(activity)
        binding.rvCategoryBudget.adapter=simpleGenericAdapterWithBinding
    }

    private fun fetchCategoryBudgetListFromDatabase() {
        budgetAndSpentViewModel.getBudgetAndCategoryList(Util.getCurrentMonth(),Util.getCurrentYear()).subscribeOn(Schedulers.io())
            .map { budgetList->
                var categoryIdList=ArrayList<Long>()
                budgetList.forEach{categoryIdList.add(it.categoryId)}
                val categorySpentsOfMonth = budgetAndSpentViewModel.getCategorySpentsOfMonth(
                    categoryIdList,
                    Util.getCurrentMonth(),
                    Util.getCurrentYear()
                )
                for(categoryCost in categorySpentsOfMonth)
                {
                    var budget=budgetList.filter { budget ->  budget.categoryId==categoryCost.getCategoryId().toLong()}[0]
                    budget.spent=categoryCost.getCost()
                    budget.categoryName=categoryCost.getCategoryName()

                }
                return@map budgetList
            }.observeOn(AndroidSchedulers.mainThread()).filter{it!=null}.subscribe{
                budgetList->updateViewOnDataBaseChange(budgetList)
            }
    }

    private fun updateViewOnDataBaseChange(budgetList: List<Budget>) {
        simpleGenericAdapterWithBinding.update(budgetList)
    }

}